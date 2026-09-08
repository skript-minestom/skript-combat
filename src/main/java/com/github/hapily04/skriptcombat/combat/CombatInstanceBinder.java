package com.github.hapily04.skriptcombat.combat;

import io.github.togar2.pvp.feature.CombatFeatureSet;
import io.github.togar2.pvp.feature.CombatFeatures;
import io.github.togar2.pvp.feature.FeatureType;
import io.github.togar2.pvp.feature.config.CombatConfiguration;
import io.github.togar2.pvp.feature.explosion.ExplosionFeature;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.instance.ExplosionSupplier;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Attaches a built combat feature set to a Minestom instance event node.
 * Combat listeners stay instance-scoped; jump physics from CombatPlayer remain global.
 */
public final class CombatInstanceBinder {

	private static final Map<Instance, Binding> BINDINGS = new IdentityHashMap<>();

	private CombatInstanceBinder() {}

	public static boolean isEnabled(Instance instance) {
		return BINDINGS.containsKey(instance);
	}

	public static void apply(Instance instance, CombatConfiguration configuration) {
		disable(instance);

		CombatFeatureSet featureSet = configuration.build();
		EventNode<EntityInstanceEvent> node = featureSet.createNode();
		instance.eventNode().addChild(node);

		ExplosionSupplier installed = null;
		ExplosionSupplier previous = instance.getExplosionSupplier();
		ExplosionFeature explosion = featureSet.get(FeatureType.EXPLOSION);
		ExplosionSupplier supplier = explosion.getExplosionSupplier();
		if (supplier != null) {
			instance.setExplosionSupplier(supplier);
			installed = supplier;
		}

		BINDINGS.put(instance, new Binding(configuration, featureSet, node, previous, installed));
	}

	public static @Nullable CombatConfiguration getConfiguration(Instance instance) {
		Binding binding = BINDINGS.get(instance);
		return binding == null ? null : binding.configuration();
	}

	public static @Nullable CombatFeatureSet getFeatureSet(Instance instance) {
		Binding binding = BINDINGS.get(instance);
		return binding == null ? null : binding.featureSet();
	}

	/**
	 * Whether this instance has the PvP explosion supplier installed from its bound combat config.
	 * Combat can be enabled with {@code vanilla explosion: false}, which leaves no supplier.
	 */
	public static boolean hasExplosionSupplier(Instance instance) {
		Binding binding = BINDINGS.get(instance);
		return binding != null
				&& binding.installedSupplier() != null
				&& instance.getExplosionSupplier() == binding.installedSupplier();
	}

	/**
	 * Features for constructing PvP entities. Uses the instance binding when present,
	 * otherwise modern vanilla construction defaults.
	 */
	public static CombatFeatureSet featuresFor(Instance instance) {
		CombatFeatureSet bound = getFeatureSet(instance);
		return bound != null ? bound : CombatFeatures.modernVanilla();
	}

	public static void disable(Instance instance) {
		Binding binding = BINDINGS.remove(instance);
		if (binding == null) return;

		instance.eventNode().removeChild(binding.node());
		if (binding.installedSupplier() != null
				&& instance.getExplosionSupplier() == binding.installedSupplier()) {
			instance.setExplosionSupplier(binding.previousSupplier());
		}
	}

	private record Binding(
			CombatConfiguration configuration,
			CombatFeatureSet featureSet,
			EventNode<EntityInstanceEvent> node,
			@Nullable ExplosionSupplier previousSupplier,
			@Nullable ExplosionSupplier installedSupplier
	) {}
}
