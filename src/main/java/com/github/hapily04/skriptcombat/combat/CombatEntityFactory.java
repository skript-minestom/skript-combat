package com.github.hapily04.skriptcombat.combat;

import io.github.togar2.pvp.entity.AreaEffectCloud;
import io.github.togar2.pvp.entity.explosion.CrystalEntity;
import io.github.togar2.pvp.entity.explosion.TntEntity;
import io.github.togar2.pvp.entity.projectile.*;
import io.github.togar2.pvp.feature.CombatFeatureSet;
import io.github.togar2.pvp.feature.FeatureType;
import io.github.togar2.pvp.feature.effect.EffectFeature;
import io.github.togar2.pvp.feature.enchantment.EnchantmentFeature;
import io.github.togar2.pvp.feature.fall.FallFeature;
import io.github.togar2.pvp.utils.CombatVersion;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.PotionContents;
import org.jetbrains.annotations.Nullable;

/**
 * Creates MinestomPvP custom entities for spawn/shoot syntax.
 */
public final class CombatEntityFactory {

	private CombatEntityFactory() {}

	public static Entity create(CombatEntityType type, Instance instance, @Nullable Entity shooter) {
		CombatFeatureSet features = CombatInstanceBinder.featuresFor(instance);
		EffectFeature effect = features.get(FeatureType.EFFECT);
		EnchantmentFeature enchantment = features.get(FeatureType.ENCHANTMENT);
		FallFeature fall = features.get(FeatureType.FALL);
		CombatVersion version = features.get(FeatureType.VERSION);

		return switch (type) {
			case ARROW -> new Arrow(shooter, effect, enchantment);
			case SPECTRAL_ARROW -> new SpectralArrow(shooter, enchantment);
			case TRIDENT -> new ThrownTrident(shooter, ItemStack.of(Material.TRIDENT), enchantment);
			case SNOWBALL -> new Snowball(shooter);
			case EGG -> new ThrownEgg(shooter);
			case ENDER_PEARL -> new ThrownEnderpearl(shooter, fall);
			case SPLASH_POTION -> new ThrownPotion(shooter, effect, false);
			case LINGERING_POTION -> new ThrownPotion(shooter, effect, true);
			case FIREWORK -> new FireworkRocket(shooter, ItemStack.of(Material.FIREWORK_ROCKET), true);
			case WIND_CHARGE -> new WindCharge(shooter, fall);
			case FISHING_BOBBER -> new FishingBobber(shooter, version.legacy());
			case PRIMED_TNT -> new TntEntity(shooter);
			case END_CRYSTAL -> new CrystalEntity();
			case AREA_EFFECT_CLOUD -> new AreaEffectCloud(PotionContents.EMPTY, shooter, effect);
		};
	}

	public static void shoot(CustomEntityProjectile projectile, Entity from, double power, @Nullable Pos lookOverride) {
		Pos look = lookOverride != null ? lookOverride : from.getPosition();
		Instance instance = from.getInstance();
		if (instance == null) return;
		Pos eye = look.add(0, from.getEyeHeight() - 0.1, 0);
		projectile.shootFromRotationAndLaunch(instance, eye, 0, power, 1.0, from);
	}

	public static void shoot(CustomEntityProjectile projectile, float pitch, float yaw, double power) {
		projectile.shootFromRotation(pitch, yaw, 0, power, 1.0);
	}
}
