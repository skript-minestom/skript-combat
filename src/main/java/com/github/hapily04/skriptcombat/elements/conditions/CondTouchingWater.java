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
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Touching Water")
@Description("Checks whether entities are touching water, optionally at specific positions.")
@Examples("""
	if player is touching water:
	if {_entity} is touching water at {_pos}:""")
@Since("1.0.0")
public class CondTouchingWater extends Condition {

	static {
		Skript.registerCondition(CondTouchingWater.class,
				"%entities% (is|are) touching water [at %points%]",
				"%entities% (isn't|is not|aren't|are not) touching water [at %points%]");
	}

	private Expression<Entity> entities;
	private @Nullable Expression<Point> points;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.entities = (Expression<Entity>) exprs[0];
		this.points = (Expression<Point>) exprs[1];
		setNegated(matchedPattern == 1);
		return true;
	}

	@Override
	public boolean check(Event event) {
		Point[] points = this.points == null ? null : this.points.getArray(event);
		return this.entities.check(event, entity -> {
			if (entity.getInstance() == null) return false;
			if (points == null || points.length == 0) {
				return FluidUtil.isTouchingWater(entity);
			}
			for (Point point : points) {
				Pos pos = point instanceof Pos p ? p : new Pos(point);
				if (FluidUtil.isTouchingWater(entity, pos)) return true;
			}
			return false;
		}, isNegated());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return this.entities.toString(event, debug)
				+ (isNegated() ? " is not" : " is")
				+ " touching water"
				+ (this.points == null ? "" : " at " + this.points.toString(event, debug));
	}

}
