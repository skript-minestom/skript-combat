package com.github.hapily04.skriptcombat.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.github.hapily04.skriptcombat.elements.util.AppliedPotion;
import net.minestom.server.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Apply Potion Effect")
@Description("Applies one or more potion effects to entities.")
@Examples("""
	apply poison to player
	apply potion effect of speed of tier {_level} for {_time} to player""")
@Since("1.0.0")
public class EffApplyPotion extends Effect {

	static {
		Skript.registerEffect(EffApplyPotion.class, "apply %potioneffects% to %entities%");
	}

	private Expression<AppliedPotion> effects;
	private Expression<Entity> entities;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.effects = (Expression<AppliedPotion>) exprs[0];
		this.entities = (Expression<Entity>) exprs[1];
		return true;
	}

	@Override
	protected void execute(Event event) {
		AppliedPotion[] effects = this.effects.getArray(event);
		if (effects.length == 0) return;
		for (Entity entity : this.entities.getArray(event)) {
			AppliedPotion.apply(entity, effects);
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "apply " + this.effects.toString(event, debug) + " to " + this.entities.toString(event, debug);
	}

}
