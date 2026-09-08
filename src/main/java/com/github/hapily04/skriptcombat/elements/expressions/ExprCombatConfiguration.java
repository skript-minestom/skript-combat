package com.github.hapily04.skriptcombat.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.github.hapily04.skriptcombat.combat.CombatFeatureCatalog;
import com.github.hapily04.skriptcombat.combat.CombatFeatureCatalog.FeatureEntry;
import io.github.togar2.pvp.feature.CombatFeatures;
import io.github.togar2.pvp.feature.config.CombatConfiguration;
import io.github.togar2.pvp.feature.config.DefinedFeature;
import io.github.togar2.pvp.feature.provider.DifficultyProvider;
import io.github.togar2.pvp.utils.CombatVersion;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;

import java.util.ArrayList;
import java.util.List;

@Name("Combat Configuration")
@Description("""
	Creates a combat configuration to set as an instance's combat configuration.
	Use 'a combat configuration' for an empty set of features, or a modern/legacy vanilla preset.
	Optional section entries are feature names set to true or false. Omitted features keep the preset default.""")
@Examples("""
	set {_pvp} to a modern vanilla combat configuration:
		vanilla spectate: false
		fair rising knockback: true
	set combat configuration of {_arena} to {_pvp}

	set {_custom} to a combat configuration:
		vanilla attack: true
		vanilla damage: true""")
@Since("1.0.0")
public class ExprCombatConfiguration extends SectionExpression<CombatConfiguration> {

	static {
		Skript.registerExpression(ExprCombatConfiguration.class, CombatConfiguration.class, ExpressionType.SIMPLE,
				"[a] [new] combat config[uration]",
				"[a] modern vanilla combat config[uration]",
				"[a] legacy vanilla combat config[uration]");
	}

	private int pattern;
	private CombatConfiguration configuration;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult,
	                    @Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItems) {
		this.pattern = matchedPattern;
		CombatConfiguration config = createPreset(matchedPattern);

		if (sectionNode != null) {
			EntryContainer container = CombatFeatureCatalog.VALIDATOR.validate(sectionNode);
			if (container == null) return false;
			if (!applyEntries(config, container)) return false;
		}

		this.configuration = config;
		return true;
	}

	private static CombatConfiguration createPreset(int matchedPattern) {
		return switch (matchedPattern) {
			case 1 -> CombatFeatures.getVanilla(CombatVersion.MODERN, DifficultyProvider.DEFAULT);
			case 2 -> CombatFeatures.getVanilla(CombatVersion.LEGACY, DifficultyProvider.DEFAULT)
					.add(CombatFeatures.LEGACY_VANILLA_BLOCK);
			default -> CombatFeatures.empty()
					.version(CombatVersion.MODERN)
					.difficulty(DifficultyProvider.DEFAULT);
		};
	}

	private static boolean applyEntries(CombatConfiguration config, EntryContainer container) {
		List<DefinedFeature<?>> disables = new ArrayList<>();
		List<DefinedFeature<?>> enables = new ArrayList<>();
		List<String> enabledKnockback = new ArrayList<>();

		for (FeatureEntry feature : CombatFeatureCatalog.FEATURES) {
			if (!container.hasEntry(feature.key())) continue;
			Boolean enabled = container.getOptional(feature.key(), Boolean.class, false);
			if (enabled == null) {
				Skript.error("Combat feature '" + feature.key() + "' must be true or false.");
				return false;
			}
			if (enabled) {
				if (CombatFeatureCatalog.isKnockback(feature.feature())) {
					enabledKnockback.add(feature.key());
				}
				enables.add(feature.feature());
			} else {
				disables.add(feature.feature());
			}
		}

		if (enabledKnockback.size() > 1) {
			Skript.error("Only one knockback feature can be enabled (got " + String.join(", ", enabledKnockback) + ").");
			return false;
		}

		for (DefinedFeature<?> feature : disables) {
			config.remove(feature.featureType());
		}
		for (DefinedFeature<?> feature : enables) {
			config.add(feature);
		}
		return true;
	}

	@Override
	protected CombatConfiguration[] get(Event event) {
		return CollectionUtils.array(configuration);
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends CombatConfiguration> getReturnType() {
		return CombatConfiguration.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return switch (pattern) {
			case 1 -> "a modern vanilla combat configuration";
			case 2 -> "a legacy vanilla combat configuration";
			default -> "a combat configuration";
		};
	}

}
