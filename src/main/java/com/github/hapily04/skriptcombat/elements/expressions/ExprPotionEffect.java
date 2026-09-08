package com.github.hapily04.skriptcombat.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import com.github.hapily04.skriptcombat.elements.util.AppliedPotion;
import com.github.hapily04.skriptminestom.util.NumberUtils;
import io.github.togar2.pvp.potion.item.CombatPotionTypes;
import net.minestom.server.ServerFlag;
import net.minestom.server.potion.PotionEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Potion Effect")
@Description("""
	Creates a potion effect, matching classic Skript syntax.
	Tier/level is 1-indexed (tier 1 is amplifier 0). When omitted, tier defaults to 1.
	When duration is omitted, uses the vanilla drinkable-potion duration for that effect and tier if one exists, otherwise 15 seconds.
	Defaults: ambient false, particles true, icon true.""")
@Examples("""
	set {_p} to potion effect of poison
	set {_p} to potion effect of speed of tier {_level} for {_time}
	set {_p} to potion effect of poison 2 without particles for 10 minutes
	set {_p} to ambient potion effect of strength of tier 5 without particles without icon for 30 seconds
	apply {_p} to player""")
@Since("1.0.0")
public class ExprPotionEffect extends SimpleExpression<AppliedPotion> {

	private static final int DEFAULT_DURATION_TICKS = 15 * ServerFlag.SERVER_TICKS_PER_SECOND;

	static {
		// Mirrors classic Skript ExprPotionEffect (2.5.2+), plus without-icon from newer Skript.
		Skript.registerExpression(ExprPotionEffect.class, AppliedPotion.class, ExpressionType.COMBINED,
				"[a] [new] potion effect of %potioneffecttypes% [[[of] (tier|level)] %-number%] "
						+ "[(noparticles:without [any] particles)] [(noicon:without [the] icon)] [for %-timespans%]",
				"[a] [new] ambient potion effect of %potioneffecttypes% [[[of] (tier|level)] %-number%] "
						+ "[(noparticles:without [any] particles)] [(noicon:without [the] icon)] [for %-timespans%]");
	}

	private Expression<PotionEffect> types;
	private @Nullable Expression<Number> tier;
	private @Nullable Expression<Timespan> duration;
	private boolean ambient;
	private boolean particles;
	private boolean icon;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.types = (Expression<PotionEffect>) exprs[0];
		this.tier = (Expression<Number>) exprs[1];
		this.duration = (Expression<Timespan>) exprs[2];
		this.ambient = matchedPattern == 1;
		this.particles = !parseResult.hasTag("noparticles");
		this.icon = !parseResult.hasTag("noicon");
		return true;
	}

	@Override
	protected AppliedPotion @Nullable [] get(Event event) {
		PotionEffect[] types = this.types.getArray(event);
		if (types.length == 0) return new AppliedPotion[0];

		int amplifier = 0;
		if (this.tier != null) {
			Number tierNumber = this.tier.getSingle(event);
			if (tierNumber == null) return new AppliedPotion[0];
			amplifier = Math.max(0, tierNumber.intValue() - 1);
		}

		Integer explicitDuration = null;
		if (this.duration != null) {
			Timespan timespan = this.duration.getSingle(event);
			if (timespan == null) return new AppliedPotion[0];
			explicitDuration = (int) NumberUtils.ticksFrom(timespan);
		}

		AppliedPotion[] effects = new AppliedPotion[types.length];
		for (int i = 0; i < types.length; i++) {
			int durationTicks = explicitDuration != null
					? explicitDuration
					: resolveDefaultDuration(types[i], amplifier);
			effects[i] = new AppliedPotion(types[i], amplifier, durationTicks, ambient, particles, icon);
		}
		return effects;
	}

	private static int resolveDefaultDuration(PotionEffect type, int amplifier) {
		Integer vanilla = CombatPotionTypes.defaultDuration(type, amplifier);
		return vanilla != null ? vanilla : DEFAULT_DURATION_TICKS;
	}

	@Override
	public boolean isSingle() {
		return types.isSingle();
	}

	@Override
	public Class<? extends AppliedPotion> getReturnType() {
		return AppliedPotion.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		StringBuilder builder = new StringBuilder();
		if (ambient) builder.append("ambient ");
		builder.append("potion effect of ").append(types.toString(event, debug));
		if (tier != null) builder.append(" of tier ").append(tier.toString(event, debug));
		if (!particles) builder.append(" without particles");
		if (!icon) builder.append(" without icon");
		if (duration != null) builder.append(" for ").append(duration.toString(event, debug));
		else builder.append(" for vanilla duration or 15 seconds");
		return builder.toString();
	}

}
