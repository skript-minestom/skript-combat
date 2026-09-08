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
import net.minestom.server.potion.PotionEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Remove Potion Effect")
@Description("Removes specific potion effect types from entities, or clears all potion effects.")
@Examples("""
	remove poison from player
	clear all potion effects of {_entities::*}""")
@Since("1.0.0")
public class EffRemovePotion extends Effect {

	static {
		Skript.registerEffect(EffRemovePotion.class,
				"remove %potioneffecttypes% from %entities%",
				"clear [all] potion effects (of|from) %entities%");
	}

	private @Nullable Expression<PotionEffect> types;
	private Expression<Entity> entities;
	private boolean clearAll;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.clearAll = matchedPattern == 1;
		if (clearAll) {
			this.entities = (Expression<Entity>) exprs[0];
		} else {
			this.types = (Expression<PotionEffect>) exprs[0];
			this.entities = (Expression<Entity>) exprs[1];
		}
		return true;
	}

	@Override
	protected void execute(Event event) {
		for (Entity entity : this.entities.getArray(event)) {
			if (clearAll) {
				entity.clearEffects();
			} else {
				assert types != null;
				AppliedPotion.removeTypes(entity, types.getArray(event));
			}
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (clearAll) {
			return "clear all potion effects of " + this.entities.toString(event, debug);
		}
		assert types != null;
		return "remove " + this.types.toString(event, debug) + " from " + this.entities.toString(event, debug);
	}

}
