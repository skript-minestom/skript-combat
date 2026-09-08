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

@Name("Damage Item")
@Description("Increases the damage (durability loss) of items.")
@Examples("""
	damage player's tool by 1
	damage {_items::*} by 5""")
@Since("1.0.0")
public class EffDamageItem extends Effect {

	static {
		Skript.registerEffect(EffDamageItem.class, "damage %items% by %number%");
	}

	private Expression<Item> items;
	private Expression<Number> amount;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.items = (Expression<Item>) exprs[0];
		this.amount = (Expression<Number>) exprs[1];
		return true;
	}

	@Override
	protected void execute(Event event) {
		Number amountNumber = this.amount.getSingle(event);
		if (amountNumber == null) return;
		int amount = amountNumber.intValue();
		for (Item item : this.items.getArray(event)) {
			item.modify(stack -> {
				if (stack.has(DataComponents.UNBREAKABLE)) return stack;
				int max = stack.get(DataComponents.MAX_DAMAGE, 0);
				if (max <= 0) return stack;
				int current = stack.get(DataComponents.DAMAGE, 0);
				return stack.with(DataComponents.DAMAGE, Math.clamp(current + amount, 0, max));
			}, true);
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "damage " + this.items.toString(event, debug) + " by " + this.amount.toString(event, debug);
	}

}
