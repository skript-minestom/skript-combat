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
import net.minestom.server.instance.Instance;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Raining At")
@Description("Checks whether it is raining at the given points in instances.")
@Examples("""
	if it is raining at player in {_arena}:
	if {_pos} is in rain in {_arena}:""")
@Since("1.0.0")
public class CondRainingAt extends Condition {

	static {
		Skript.registerCondition(CondRainingAt.class,
				"it is raining at %points% [in %instances%]",
				"it is(n't| not) raining at %points% [in %instances%]",
				"%points% (is|are) in rain [in %instances%]",
				"%points% (isn't|is not|aren't|are not) in rain [in %instances%]");
	}

	private Expression<Point> points;
	private @Nullable Expression<Instance> instances;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.points = (Expression<Point>) exprs[0];
		this.instances = (Expression<Instance>) exprs[1];
		setNegated(matchedPattern == 1 || matchedPattern == 3);
		return true;
	}

	@Override
	public boolean check(Event event) {
		if (this.instances == null) return isNegated();
		Instance[] instances = this.instances.getArray(event);
		if (instances.length == 0) return isNegated();
		return this.points.check(event, point -> {
			for (Instance instance : instances) {
				if (FluidUtil.isRainingAt(instance, point.blockX(), point.blockY(), point.blockZ())) {
					return true;
				}
			}
			return false;
		}, isNegated());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "it is " + (isNegated() ? "not " : "") + "raining at " + this.points.toString(event, debug)
				+ (this.instances == null ? "" : " in " + this.instances.toString(event, debug));
	}

}
