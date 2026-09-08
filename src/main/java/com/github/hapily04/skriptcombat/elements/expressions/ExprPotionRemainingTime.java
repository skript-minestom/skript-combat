package com.github.hapily04.skriptcombat.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.github.hapily04.skriptminestom.util.NumberUtils;
import io.github.togar2.pvp.utils.PotionFlags;
import net.minestom.server.entity.Entity;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.TimedPotion;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Potion Remaining Time")
@Description("""
	The remaining duration of a potion effect type on entities.
	Set it to change how long the effect lasts from now. Setting to 0 or deleting removes the effect.""")
@Examples("""
	set remaining time of poison of player to 5 seconds
	add 10 seconds to remaining duration of speed on {_entities::*}
	if remaining time of blindness of victim > 3 seconds:""")
@Since("1.0.0")
public class ExprPotionRemainingTime extends PropertyExpression<Entity, Timespan> {

	static {
		register(ExprPotionRemainingTime.class, Timespan.class,
				"remaining [potion] (time|duration) of %potioneffecttypes%", "entities");
	}

	private Expression<PotionEffect> types;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.types = (Expression<PotionEffect>) exprs[matchedPattern];
		setExpr((Expression<? extends Entity>) (matchedPattern == 0 ? exprs[1] : exprs[0]));
		return true;
	}

	@Override
	protected Timespan[] get(Event event, Entity[] source) {
		PotionEffect[] types = this.types.getArray(event);
		List<Timespan> timespans = new ArrayList<>();
		for (Entity entity : source) {
			for (PotionEffect type : types) {
				Timespan remaining = remainingOf(entity, type);
				if (remaining != null) timespans.add(remaining);
			}
		}
		return timespans.toArray(new Timespan[0]);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case SET, ADD, REMOVE, DELETE, RESET -> CollectionUtils.array(Timespan.class);
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		PotionEffect[] types = this.types.getArray(event);
		Timespan deltaTime = delta == null || delta.length == 0 || !(delta[0] instanceof Timespan timespan) ? null : timespan;

		for (Entity entity : getExpr().getArray(event)) {
			for (PotionEffect type : types) {
				TimedPotion current = entity.getEffect(type);
				switch (mode) {
					case DELETE, RESET -> entity.removeEffect(type);
					case SET -> {
						if (deltaTime == null) {
							entity.removeEffect(type);
							continue;
						}
						long ticks = NumberUtils.ticksFrom(deltaTime);
						if (ticks <= 0) {
							entity.removeEffect(type);
							continue;
						}
						int amplifier = current == null ? 0 : current.potion().amplifier();
						byte flags = current == null ? PotionFlags.defaultFlags() : current.potion().flags();
						entity.addEffect(new Potion(type, amplifier, (int) ticks, flags));
					}
					case ADD, REMOVE -> {
						if (current == null || deltaTime == null) continue;
						Timespan remaining = remainingOf(entity, type);
						if (remaining == null) continue;
						if (remaining.isInfinite()) continue;
						long currentTicks = NumberUtils.ticksFrom(remaining);
						long deltaTicks = NumberUtils.ticksFrom(deltaTime);
						long next = mode == ChangeMode.ADD ? currentTicks + deltaTicks : currentTicks - deltaTicks;
						if (next <= 0) {
							entity.removeEffect(type);
							continue;
						}
						Potion potion = current.potion();
						entity.addEffect(new Potion(type, potion.amplifier(), (int) Math.min(Integer.MAX_VALUE, next), potion.flags()));
					}
				}
			}
		}
	}

	private static @Nullable Timespan remainingOf(Entity entity, PotionEffect type) {
		TimedPotion timed = entity.getEffect(type);
		if (timed == null) return null;
		int duration = timed.potion().duration();
		if (duration == Potion.INFINITE_DURATION) return Timespan.infinite();
		long remaining = duration - (entity.getAliveTicks() - timed.startingTicks());
		return NumberUtils.timespanFrom(Math.max(0, remaining));
	}

	@Override
	public Class<? extends Timespan> getReturnType() {
		return Timespan.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "remaining time of " + this.types.toString(event, debug) + " of " + getExpr().toString(event, debug);
	}

}
