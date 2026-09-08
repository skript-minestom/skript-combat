package com.github.hapily04.skriptcombat.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.github.hapily04.skriptcombat.combat.CombatInstanceBinder;
import net.minestom.server.instance.Instance;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Combat Is Enabled")
@Description("Checks whether an instance has a combat configuration set.")
@Examples("""
	if combat is enabled in {_arena}:
		broadcast "fight!"
""")
@Since("1.0.0")
public class CondCombatEnabled extends Condition {

	static {
		Skript.registerCondition(CondCombatEnabled.class,
				"combat is enabled (in|on|for) %instances%",
				"combat is(n't| not) enabled (in|on|for) %instances%");
	}

	private Expression<Instance> instances;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.instances = (Expression<Instance>) exprs[0];
		setNegated(matchedPattern == 1);
		return true;
	}

	@Override
	public boolean check(Event event) {
		return this.instances.check(event, CombatInstanceBinder::isEnabled, isNegated());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "combat is " + (isNegated() ? "not " : "") + "enabled in " + this.instances.toString(event, debug);
	}

}
