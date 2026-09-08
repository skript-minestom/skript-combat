package com.github.hapily04.skriptcombat.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.github.hapily04.skriptcombat.combat.CombatInstanceBinder;
import io.github.togar2.pvp.feature.config.CombatConfiguration;
import net.minestom.server.instance.Instance;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Combat Configuration of Instance")
@Description("""
	The combat configuration applied to an instance.
	Set it to enable combat on that instance, or delete/reset it to disable combat.""")
@Examples("""
	set combat config of {_arena} to a modern vanilla combat configuration:
		vanilla spectate: false
	delete combat configuration of {_lobby}""")
@Since("1.0.0")
public class ExprCombatConfigOfInstance extends SimplePropertyExpression<Instance, CombatConfiguration> {

	static {
		register(ExprCombatConfigOfInstance.class, CombatConfiguration.class, "combat config[uration]", "instances");
	}

	@Override
	public @Nullable CombatConfiguration convert(Instance instance) {
		return CombatInstanceBinder.getConfiguration(instance);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case SET -> CollectionUtils.array(CombatConfiguration.class);
			case DELETE, RESET -> CollectionUtils.array();
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		Instance[] instances = getExpr().getArray(event);
		if (mode == ChangeMode.SET) {
			if (delta == null || delta.length == 0 || !(delta[0] instanceof CombatConfiguration configuration)) return;
			for (Instance instance : instances) {
				CombatInstanceBinder.apply(instance, configuration);
			}
			return;
		}
		for (Instance instance : instances) {
			CombatInstanceBinder.disable(instance);
		}
	}

	@Override
	protected String getPropertyName() {
		return "combat configuration";
	}

	@Override
	public Class<? extends CombatConfiguration> getReturnType() {
		return CombatConfiguration.class;
	}

}
