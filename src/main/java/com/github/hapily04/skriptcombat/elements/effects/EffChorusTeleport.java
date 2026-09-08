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
import io.github.togar2.pvp.feature.food.ChorusFruitUtil;
import net.minestom.server.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Chorus Fruit Teleport")
@Description("Attempts a chorus fruit style random teleport for entities. Diameter defaults to 16.")
@Examples("""
	chorus fruit teleport player
	chorus fruit teleport {_entities::*} with diameter 32""")
@Since("1.0.0")
public class EffChorusTeleport extends Effect {

	static {
		Skript.registerEffect(EffChorusTeleport.class,
				"chorus fruit teleport %entities% [with diameter %number%]");
	}

	private Expression<Entity> entities;
	private @Nullable Expression<Number> diameter;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.entities = (Expression<Entity>) exprs[0];
		this.diameter = (Expression<Number>) exprs[1];
		return true;
	}

	@Override
	protected void execute(Event event) {
		float diameter = 16f;
		if (this.diameter != null) {
			Number value = this.diameter.getSingle(event);
			if (value == null) return;
			diameter = value.floatValue();
		}
		for (Entity entity : this.entities.getArray(event)) {
			if (entity.getInstance() == null) continue;
			ChorusFruitUtil.tryChorusTeleport(entity, diameter);
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "chorus fruit teleport " + this.entities.toString(event, debug)
				+ (this.diameter == null ? "" : " with diameter " + this.diameter.toString(event, debug));
	}

}
