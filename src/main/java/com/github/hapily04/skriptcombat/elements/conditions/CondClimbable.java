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
import io.github.togar2.pvp.utils.BlockUtil;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Climbable")
@Description("Checks whether the block at a point is climbable.")
@Examples("""
	if the block at {_pos} in {_arena} is climbable:
	if {_pos} is climbable in {_arena}:""")
@Since("1.0.0")
public class CondClimbable extends Condition {

	static {
		Skript.registerCondition(CondClimbable.class,
				"[the] block at %points% [in %instances%] is climbable",
				"[the] block at %points% [in %instances%] is(n't| not) climbable",
				"%points% is climbable [in %instances%]",
				"%points% is(n't| not) climbable [in %instances%]");
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
				if (BlockUtil.isClimbable(instance, point)) return true;
			}
			return false;
		}, isNegated());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "block at " + this.points.toString(event, debug)
				+ (this.instances == null ? "" : " in " + this.instances.toString(event, debug))
				+ (isNegated() ? " is not" : " is")
				+ " climbable";
	}

}
