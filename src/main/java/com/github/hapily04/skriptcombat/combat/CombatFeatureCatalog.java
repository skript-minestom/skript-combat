package com.github.hapily04.skriptcombat.combat;

import io.github.togar2.pvp.feature.CombatFeatures;
import io.github.togar2.pvp.feature.FeatureType;
import io.github.togar2.pvp.feature.config.DefinedFeature;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.LiteralEntryData;

import java.util.List;

public final class CombatFeatureCatalog {

	public record FeatureEntry(String key, DefinedFeature<?> feature) {}

	public static final List<FeatureEntry> FEATURES = List.of(
			entry("vanilla armor", CombatFeatures.VANILLA_ARMOR),
			entry("vanilla attack", CombatFeatures.VANILLA_ATTACK),
			entry("vanilla critical", CombatFeatures.VANILLA_CRITICAL),
			entry("vanilla sweeping", CombatFeatures.VANILLA_SWEEPING),
			entry("vanilla smash attack", CombatFeatures.VANILLA_SMASH_ATTACK),
			entry("vanilla spear", CombatFeatures.VANILLA_SPEAR),
			entry("vanilla equipment", CombatFeatures.VANILLA_EQUIPMENT),
			entry("vanilla block", CombatFeatures.VANILLA_BLOCK),
			entry("vanilla attack cooldown", CombatFeatures.VANILLA_ATTACK_COOLDOWN),
			entry("vanilla item cooldown", CombatFeatures.VANILLA_ITEM_COOLDOWN),
			entry("vanilla damage", CombatFeatures.VANILLA_DAMAGE),
			entry("vanilla effect", CombatFeatures.VANILLA_EFFECT),
			entry("vanilla enchantment", CombatFeatures.VANILLA_ENCHANTMENT),
			entry("vanilla explosion", CombatFeatures.VANILLA_EXPLOSION),
			entry("vanilla explosive", CombatFeatures.VANILLA_EXPLOSIVE),
			entry("vanilla fall", CombatFeatures.VANILLA_FALL),
			entry("vanilla exhaustion", CombatFeatures.VANILLA_EXHAUSTION),
			entry("vanilla food", CombatFeatures.VANILLA_FOOD),
			entry("vanilla regeneration", CombatFeatures.VANILLA_REGENERATION),
			entry("vanilla item damage", CombatFeatures.VANILLA_ITEM_DAMAGE),
			entry("vanilla knockback", CombatFeatures.VANILLA_KNOCKBACK),
			entry("vanilla potion", CombatFeatures.VANILLA_POTION),
			entry("vanilla bow", CombatFeatures.VANILLA_BOW),
			entry("vanilla crossbow", CombatFeatures.VANILLA_CROSSBOW),
			entry("vanilla fishing rod", CombatFeatures.VANILLA_FISHING_ROD),
			entry("vanilla misc projectile", CombatFeatures.VANILLA_MISC_PROJECTILE),
			entry("vanilla projectile item", CombatFeatures.VANILLA_PROJECTILE_ITEM),
			entry("vanilla trident", CombatFeatures.VANILLA_TRIDENT),
			entry("vanilla spectate", CombatFeatures.VANILLA_SPECTATE),
			entry("vanilla player state", CombatFeatures.VANILLA_PLAYER_STATE),
			entry("vanilla totem", CombatFeatures.VANILLA_TOTEM),
			entry("vanilla death message", CombatFeatures.VANILLA_DEATH_MESSAGE),
			entry("vanilla environment damage", CombatFeatures.VANILLA_ENVIRONMENT_DAMAGE),
			entry("legacy vanilla block", CombatFeatures.LEGACY_VANILLA_BLOCK),
			entry("fair rising knockback", CombatFeatures.FAIR_RISING_KNOCKBACK),
			entry("fair rising falling knockback", CombatFeatures.FAIR_RISING_FALLING_KNOCKBACK)
	);

	public static final EntryValidator VALIDATOR;

	static {
		EntryValidator.EntryValidatorBuilder builder = EntryValidator.builder();
		for (FeatureEntry feature : FEATURES) {
			builder.addEntryData(new LiteralEntryData<>(feature.key(), null, true, Boolean.class));
		}
		VALIDATOR = builder.build();
	}

	private CombatFeatureCatalog() {}

	public static boolean isKnockback(DefinedFeature<?> feature) {
		return feature.featureType() == FeatureType.KNOCKBACK;
	}

	private static FeatureEntry entry(String key, DefinedFeature<?> feature) {
		return new FeatureEntry(key, feature);
	}
}
