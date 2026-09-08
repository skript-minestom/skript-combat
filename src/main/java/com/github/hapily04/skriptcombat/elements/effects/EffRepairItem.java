package com.github.hapily04.skriptcombat.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Item;
import ch.njol.util.Kleenean;
import net.minestom.server.component.DataComponents;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Repair Item")
@Description("Repairs items by reducing their damage, or fully repairs them when no amount is given.")
@Examples("""
	repair player's tool
	repair {_sword} by 10""")
@Since("1.0.0")
public class EffRepairItem extends Effect {

	static {
		Skript.registerEffect(EffRepairItem.class, "repair %items% [by %number%]");
	}

	private Expression<Item> items;
	private @Nullable Expression<Number> amount;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.items = (Expression<Item>) exprs[0];
		this.amount = (Expression<Number>) exprs[1];
		return true;
	}

	@Override
	protected void execute(Event event) {
		Integer repairBy = null;
		if (this.amount != null) {
			Number amountNumber = this.amount.getSingle(event);
			if (amountNumber == null) return;
			repairBy = amountNumber.intValue();
		}
		Integer finalRepairBy = repairBy;
		for (Item item : this.items.getArray(event)) {
			item.modify(stack -> {
				int current = stack.get(DataComponents.DAMAGE, 0);
				int next = finalRepairBy == null ? 0 : Math.max(0, current - finalRepairBy);
				return stack.with(DataComponents.DAMAGE, next);
			}, true);
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "repair " + this.items.toString(event, debug)
				+ (this.amount == null ? "" : " by " + this.amount.toString(event, debug));
	}

}
