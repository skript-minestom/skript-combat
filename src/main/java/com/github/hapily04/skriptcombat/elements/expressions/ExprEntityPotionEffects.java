package com.github.hapily04.skriptcombat.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.github.hapily04.skriptcombat.elements.util.AppliedPotion;
import net.minestom.server.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Name("Entity Potion Effects")
@Description("The active potion effects of entities. Supports add, set, remove, and delete.")
@Examples("""
	set potion effects of player to potion effect of poison of tier 2 for 10 seconds
	add potion effect of speed of level {_tier} for {_time} to potion effects of player
	clear potion effects of {_victim}""")
@Since("1.0.0")
public class ExprEntityPotionEffects extends PropertyExpression<Entity, AppliedPotion> {

	static {
		register(ExprEntityPotionEffects.class, AppliedPotion.class, "potion effects", "entities");
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		setExpr((Expression<? extends Entity>) exprs[0]);
		return true;
	}

	@Override
	protected AppliedPotion[] get(Event event, Entity[] source) {
		List<AppliedPotion> effects = new ArrayList<>();
		for (Entity entity : source) {
			effects.addAll(Arrays.asList(AppliedPotion.getEffects(entity)));
		}
		return effects.toArray(new AppliedPotion[0]);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case ADD, SET, REMOVE, DELETE, RESET -> CollectionUtils.array(AppliedPotion[].class);
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		AppliedPotion[] effects = delta == null ? new AppliedPotion[0] : Arrays.copyOf(delta, delta.length, AppliedPotion[].class);
		for (Entity entity : getExpr().getArray(event)) {
			switch (mode) {
				case ADD -> AppliedPotion.apply(entity, effects);
				case SET -> AppliedPotion.set(entity, effects);
				case REMOVE -> AppliedPotion.remove(entity, effects);
				case DELETE, RESET -> entity.clearEffects();
			}
		}
	}

	@Override
	public Class<? extends AppliedPotion> getReturnType() {
		return AppliedPotion.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "potion effects of " + getExpr().toString(event, debug);
	}

}
