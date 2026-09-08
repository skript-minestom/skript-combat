package com.github.hapily04.skriptcombat.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import io.github.togar2.pvp.utils.FluidUtil;
import net.minestom.server.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("In Rain")
@Description("Checks whether entities are currently in rain.")
@Examples("""
	if player is in rain:
	if {_entities::*} are not in rain:""")
@Since("1.0.0")
public class CondInRain extends Condition {

	static {
		Skript.registerCondition(CondInRain.class,
				"%entities% (is|are) in rain",
				"%entities% (isn't|is not|aren't|are not) in rain");
	}

	private Expression<Entity> entities;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.entities = (Expression<Entity>) exprs[0];
		setNegated(matchedPattern == 1);
		return true;
	}

	@Override
	public boolean check(Event event) {
		return this.entities.check(event, FluidUtil::isInRain, isNegated());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return this.entities.toString(event, debug)
				+ (isNegated() ? " is not" : " is")
				+ " in rain";
	}

}
