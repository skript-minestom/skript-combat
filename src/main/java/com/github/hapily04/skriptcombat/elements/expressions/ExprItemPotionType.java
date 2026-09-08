package com.github.hapily04.skriptcombat.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Item;
import ch.njol.util.coll.CollectionUtils;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.component.PotionContents;
import net.minestom.server.potion.PotionType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Item Potion Type")
@Description("The potion type of an item's potion contents, such as long poison or strong healing.")
@Examples("""
	set potion type of {_potion} to long poison
	delete potion type of player's tool""")
@Since("1.0.0")
public class ExprItemPotionType extends SimplePropertyExpression<Item, PotionType> {

	static {
		register(ExprItemPotionType.class, PotionType.class, "potion type", "items");
	}

	@Override
	public @Nullable PotionType convert(Item item) {
		PotionContents contents = item.getItem().get(DataComponents.POTION_CONTENTS);
		return contents == null ? null : contents.potion();
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case SET -> CollectionUtils.array(PotionType.class);
			case DELETE, RESET -> CollectionUtils.array();
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		PotionType type = delta == null || delta.length == 0 ? null : (PotionType) delta[0];
		for (Item item : getExpr().getArray(event)) {
			item.modify(stack -> {
				PotionContents current = stack.get(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
				PotionContents updated = new PotionContents(
						mode == ChangeMode.SET ? type : null,
						current.customColor(),
						current.customEffects(),
						current.customName()
				);
				return stack.with(DataComponents.POTION_CONTENTS, updated);
			}, true);
		}
	}

	@Override
	protected String getPropertyName() {
		return "potion type";
	}

	@Override
	public Class<? extends PotionType> getReturnType() {
		return PotionType.class;
	}

}
