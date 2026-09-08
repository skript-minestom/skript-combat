package com.github.hapily04.skriptcombat.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Direction;
import ch.njol.util.Kleenean;
import com.github.hapily04.skriptcombat.combat.CombatInstanceBinder;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Create Explosion")
@Description("Creates an explosion at the given points when the instance has combat with the explosion feature enabled.")
@Examples("""
	create an explosion of force 4 at player in {_arena}
	make an explosion with power 6 at {_pos} in {_arena} with fire""")
@Since("1.0.0")
public class EffExplosion extends Effect {

	static {
		Skript.registerEffect(EffExplosion.class,
				"[(create|make)] [an] explosion (of|with) (force|strength|power) %number% [%directions% %points%] [in %-instances%] [(fire:with fire)]");
	}

	private Expression<Number> power;
	private Expression<Point> points;
	private @Nullable Expression<Instance> instances;
	private boolean withFire;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.power = (Expression<Number>) exprs[0];
		this.points = Direction.combine(
				(Expression<? extends Direction>) exprs[1],
				(Expression<? extends Point>) exprs[2]
		);
		this.instances = (Expression<Instance>) exprs[3];
		this.withFire = parseResult.hasTag("fire");
		return true;
	}

	@Override
	protected void execute(Event event) {
		Number powerNumber = this.power.getSingle(event);
		if (powerNumber == null) return;
		float power = powerNumber.floatValue();
		if (this.instances == null) return;
		Instance[] instances = this.instances.getArray(event);
		if (instances.length == 0) return;

		CompoundBinaryTag nbt = withFire
				? CompoundBinaryTag.builder().putBoolean("fire", true).build()
				: CompoundBinaryTag.empty();

		for (Instance instance : instances) {
			if (!CombatInstanceBinder.hasExplosionSupplier(instance)) continue;
			for (Point point : this.points.getArray(event)) {
				instance.explode((float) point.x(), (float) point.y(), (float) point.z(), power, nbt);
			}
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "explosion of force " + this.power.toString(event, debug) + " "
				+ this.points.toString(event, debug)
				+ (this.instances == null ? "" : " in " + this.instances.toString(event, debug))
				+ (withFire ? " with fire" : "");
	}

}
