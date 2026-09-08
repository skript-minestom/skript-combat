package com.github.hapily04.skriptcombat.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Item;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.github.hapily04.skriptcombat.elements.util.AppliedPotion;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Name("Item Potion Effects")
@Description("The custom potion effects stored on an item's potion contents.")
@Examples("""
	set potion effects of {_potion} to poison 2 for 10 seconds
	add slowness to potion contents of {_arrow}""")
@Since("1.0.0")
public class ExprItemPotionEffects extends PropertyExpression<Item, AppliedPotion> {

	static {
		register(ExprItemPotionEffects.class, AppliedPotion.class, "potion [contents|effects]", "items");
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		setExpr((Expression<? extends Item>) exprs[0]);
		return true;
	}

	@Override
	protected AppliedPotion[] get(Event event, Item[] source) {
		List<AppliedPotion> effects = new ArrayList<>();
		for (Item item : source) {
			effects.addAll(Arrays.asList(AppliedPotion.getItemEffects(item)));
		}
		return effects.toArray(new AppliedPotion[0]);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case ADD, SET, REMOVE, DELETE, RESET -> CollectionUtils.array(AppliedPotion[].class);
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		AppliedPotion[] effects = delta == null ? new AppliedPotion[0] : Arrays.copyOf(delta, delta.length, AppliedPotion[].class);
		for (Item item : getExpr().getArray(event)) {
			switch (mode) {
				case ADD -> AppliedPotion.addItemEffects(item, effects);
				case SET -> AppliedPotion.setItemEffects(item, effects);
				case REMOVE -> AppliedPotion.removeItemEffects(item, effects);
				case DELETE, RESET -> AppliedPotion.setItemEffects(item);
			}
		}
	}

	@Override
	public Class<? extends AppliedPotion> getReturnType() {
		return AppliedPotion.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "potion effects of " + getExpr().toString(event, debug);
	}

}
