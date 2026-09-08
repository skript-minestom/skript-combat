package com.github.hapily04.skriptcombat.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.events.wrapper.EntitySpawnWrapper;
import ch.njol.skript.lang.EffectSection;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.util.Direction;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import com.github.hapily04.skriptcombat.combat.CombatEntityFactory;
import com.github.hapily04.skriptcombat.combat.CombatEntityType;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.entity.EntitySpawnEvent;
import net.minestom.server.instance.Instance;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;

import java.util.List;

@Name("Spawn Combat Entity")
@Description("Spawns one or more combat entities at a location, with optional before/after spawn sections.")
@Examples("""
	spawn a combat primed tnt at player in {_arena}
	spawn 3 combat arrows at {_pos} in {_arena}:
		before spawn:
			set base damage of entity to 5""")
@Since("1.0.0")
public class EffSecCombatSpawn extends EffectSection {

	private static final EntryValidator ENTRY_VALIDATOR = EntryValidator.builder()
			.addSection("before spawn", true)
			.addSection("after spawn", true)
			.build();

	static {
		Skript.registerSection(EffSecCombatSpawn.class,
				"(summon|spawn) [a] combat %combatentitytypes% [%directions% %points%] [in [(world|instance)[s]] %instances%]",
				"(summon|spawn) %integer% [of] combat %combatentitytypes% [%directions% %points%] [in [(world|instance)[s]] %instances%]");
	}

	private @Nullable Expression<Integer> amount;
	private Expression<CombatEntityType> types;
	private Expression<Point> points;
	private Expression<Instance> instances;
	private @Nullable Trigger beforeSpawnTrigger;
	private @Nullable Trigger afterSpawnTrigger;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult,
						@Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItems) {
		if (matchedPattern == 1) {
			this.amount = (Expression<Integer>) expressions[0];
		}
		this.types = (Expression<CombatEntityType>) expressions[matchedPattern];
		this.points = Direction.combine(
				(Expression<? extends Direction>) expressions[1 + matchedPattern],
				(Expression<? extends Point>) expressions[2 + matchedPattern]
		);
		this.instances = (Expression<Instance>) expressions[3 + matchedPattern];

		if (sectionNode != null) {
			EntryContainer container = ENTRY_VALIDATOR.validate(sectionNode);
			if (container == null) return false;
			SectionNode beforeSpawn = container.getOptional("before spawn", SectionNode.class, false);
			if (beforeSpawn != null) {
				beforeSpawnTrigger = loadCode(beforeSpawn, "before spawn", EntitySpawnWrapper.class);
			}
			SectionNode afterSpawn = container.getOptional("after spawn", SectionNode.class, false);
			if (afterSpawn != null) {
				afterSpawnTrigger = loadCode(afterSpawn, "after spawn", EntitySpawnWrapper.class);
			}
			if (beforeSpawn == null && afterSpawn == null) {
				Skript.error("You can't run abstract code within this section! Either put it under 'before spawn' or 'after spawn'.");
				return false;
			}
		}
		return true;
	}

	@Override
	protected @Nullable TriggerItem walk(Event event) {
		Integer amountValue = this.amount == null ? null : this.amount.getSingle(event);
		int amount = amountValue == null ? 1 : amountValue;
		CombatEntityType[] types = this.types.getArray(event);
		Point[] points = this.points.getArray(event);
		Instance[] instances = this.instances.getArray(event);
		Object variables = Variables.copyLocalVariables(event);
		Object mostRecentLocals = variables;

		for (CombatEntityType type : types) {
			for (Instance instance : instances) {
				for (Point point : points) {
					for (int i = 0; i < amount; i++) {
						Entity entity = CombatEntityFactory.create(type, instance, null);
						if (beforeSpawnTrigger != null) {
							Event e = new EntitySpawnWrapper(new EntitySpawnEvent(entity, instance));
							Variables.setLocalVariables(e, variables);
							TriggerItem.walk(beforeSpawnTrigger, e);
							mostRecentLocals = Variables.copyLocalVariables(e);
						}
						Object finalMostRecentLocals = mostRecentLocals;
						entity.setInstance(instance, point).whenComplete((_, throwable) -> {
							if (throwable != null || afterSpawnTrigger == null) return;
							Event e = new EntitySpawnWrapper(new EntitySpawnEvent(entity, instance));
							Variables.setLocalVariables(e, finalMostRecentLocals);
							TriggerItem.walk(afterSpawnTrigger, e);
							Variables.removeLocals(e);
						});
					}
				}
			}
		}

		Variables.setLocalVariables(event, mostRecentLocals);
		return super.walk(event, false);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		String amount = this.amount == null ? "1" : this.amount.toString(event, debug);
		return "spawn " + amount + " combat " + this.types.toString(event, debug) + " "
				+ this.points.toString(event, debug) + " in " + this.instances.toString(event, debug);
	}

}
