package com.github.hapily04.skriptcombat.elements;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.EnumClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Timespan;
import com.github.hapily04.skriptcombat.combat.CombatEntityType;
import com.github.hapily04.skriptcombat.elements.util.AppliedPotion;
import com.github.hapily04.skriptminestom.util.NumberUtils;
import io.github.togar2.pvp.feature.config.CombatConfiguration;
import io.github.togar2.pvp.potion.item.CombatPotionTypes;
import net.kyori.adventure.key.Key;
import net.minestom.server.ServerFlag;
import net.minestom.server.entity.EntityType;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.PotionType;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.converter.Converters;

import java.util.Locale;

public class Types {

	private static final int DEFAULT_DURATION_TICKS = 15 * ServerFlag.SERVER_TICKS_PER_SECOND;

	static {
		Converters.registerConverter(PotionEffect.class, AppliedPotion.class,
				type -> {
					Integer vanilla = CombatPotionTypes.defaultDuration(type, 0);
					return new AppliedPotion(type, 0, vanilla != null ? vanilla : DEFAULT_DURATION_TICKS);
				});

		Classes.registerClass(new ClassInfo<>(CombatConfiguration.class, "combatconfiguration")
				.user("combat configurations?")
				.name("Combat Configuration")
				.description("A set of MinestomPvP combat features that can be applied to an instance.")
				.examples("""
					set combat configuration of {_arena} to a modern vanilla combat configuration:
						vanilla spectate: false""")
				.parser(new Parser<>() {
					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(CombatConfiguration configuration, int flags) {
						return "combat configuration";
					}

					@Override
					public String toVariableNameString(CombatConfiguration configuration) {
						return "combat configuration";
					}
				}));

		Classes.registerClass(new EnumClassInfo<>(CombatEntityType.class, "combatentitytype")
				.user("combat ?entity ?types?")
				.name("Combat Entity Type")
				.description("A custom MinestomPvP entity that can be spawned or shot with combat syntax.")
				.examples("""
					spawn combat arrow at player
					shoot a snowball from player with speed 1.5"""));

		// Allow `%entitytypes%` like `arrow` to convert when a combatentitytype is expected.
		Converters.registerConverter(EntityType.class, CombatEntityType.class, entityType -> {
			String key = entityType.key().value().replace('_', ' ');
			return CombatEntityType.parse(key);
		});

		Classes.registerClass(new ClassInfo<>(PotionEffect.class, "potioneffecttype")
				.user("potion ?effect ?types?")
				.name("Potion Effect Type")
				.description("A Minecraft potion effect type such as poison or speed.")
				.examples("""
					apply poison to player
					remove speed from player""")
				.usage("<effect namespace>")
				.defaultExpression(new EventValueExpression<>(PotionEffect.class))
				.parser(new Parser<>() {
					@Override
					public @Nullable PotionEffect parse(String s, ParseContext context) {
						return parsePotionEffectType(s);
					}

					@Override
					public boolean canParse(ParseContext context) {
						return true;
					}

					@Override
					public String toString(PotionEffect o, int flags) {
						return keyToString(o.key());
					}

					@Override
					public String toVariableNameString(PotionEffect o) {
						return keyToString(o.key());
					}
				}));

		Classes.registerClass(new ClassInfo<>(AppliedPotion.class, "potioneffect")
				.user("potion ?effects?")
				.name("Potion Effect")
				.description("A potion effect with an amplifier and duration. Prefer 'potion effect of %type% of tier %number% for %timespan%' for dynamic values.")
				.examples("""
					set {_e} to potion effect of poison of tier {_level} for {_time}
					apply {_e} to player""")
				.usage("potion effect of <effect type> [of tier <number>] [for <timespan>]")
				.defaultExpression(new EventValueExpression<>(AppliedPotion.class)));

		Classes.registerClass(new ClassInfo<>(PotionType.class, "potiontype")
				.user("potion ?types?")
				.name("Potion Type")
				.description("A potion type used in potion item contents, such as strong healing.")
				.examples("""
					set potion type of {_arrow} to long poison""")
				.usage("<potion type namespace>")
				.defaultExpression(new EventValueExpression<>(PotionType.class))
				.parser(new Parser<>() {
					@Override
					public @Nullable PotionType parse(String s, ParseContext context) {
						return parsePotionType(s);
					}

					@Override
					public boolean canParse(ParseContext context) {
						return true;
					}

					@Override
					public String toString(PotionType o, int flags) {
						return keyToString(o.key());
					}

					@Override
					public String toVariableNameString(PotionType o) {
						return keyToString(o.key());
					}
				}));
	}

	private static @Nullable PotionEffect parsePotionEffectType(String s) {
		String nameSpace = normalizeKey(s);
		if (nameSpace == null) return null;
		return PotionEffect.fromKey(nameSpace);
	}

	private static @Nullable PotionType parsePotionType(String s) {
		String nameSpace = normalizeKey(s);
		if (nameSpace == null) return null;
		return PotionType.fromKey(nameSpace);
	}

	private static @Nullable String normalizeKey(String s) {
		s = s.toLowerCase(Locale.ENGLISH).trim().replace(' ', '_').replace('-', '_');
		if (!s.contains(":")) s = "minecraft:" + s;
		else if (!s.startsWith("minecraft:")) return null;
		if (!Key.parseable(s)) return null;
		return s;
	}

	private static String keyToString(Key key) {
		if ("minecraft".equals(key.namespace())) return key.value().replace('_', ' ');
		return key.asString().replace('_', ' ');
	}
}
