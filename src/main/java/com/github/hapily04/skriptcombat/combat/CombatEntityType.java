package com.github.hapily04.skriptcombat.combat;

import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * Custom MinestomPvP entity kinds that skript-combat can spawn or shoot.
 */
public enum CombatEntityType {
	ARROW("arrow", true),
	SPECTRAL_ARROW("spectral arrow", true),
	TRIDENT("trident", true),
	SNOWBALL("snowball", true),
	EGG("egg", true),
	ENDER_PEARL("ender pearl", true),
	SPLASH_POTION("splash potion", true),
	LINGERING_POTION("lingering potion", true),
	FIREWORK("firework rocket", true, "firework"),
	WIND_CHARGE("wind charge", true),
	FISHING_BOBBER("fishing bobber", true, "fishing hook"),
	PRIMED_TNT("primed tnt", false, "tnt"),
	END_CRYSTAL("end crystal", false, "crystal"),
	AREA_EFFECT_CLOUD("area effect cloud", false);

	private final String primaryName;
	private final boolean projectile;
	private final String[] aliases;

	CombatEntityType(String primaryName, boolean projectile, String... aliases) {
		this.primaryName = primaryName;
		this.projectile = projectile;
		this.aliases = aliases;
	}

	public String primaryName() {
		return primaryName;
	}

	public boolean isProjectile() {
		return projectile;
	}

	public static @Nullable CombatEntityType parse(String input) {
		if (input == null || input.isBlank()) return null;
		String normalized = input.toLowerCase(Locale.ENGLISH).trim().replace('_', ' ').replace('-', ' ');
		normalized = normalized.replaceAll("\\s+", " ");
		for (CombatEntityType type : values()) {
			if (type.primaryName.equals(normalized)) return type;
			for (String alias : type.aliases) {
				if (alias.equals(normalized)) return type;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return primaryName;
	}
}
