package com.github.hapily04.skriptcombat.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import io.github.togar2.pvp.entity.projectile.Arrow;
import net.minestom.server.entity.Entity;
import net.minestom.server.item.component.PotionContents;
import net.minestom.server.potion.PotionType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Arrow Potion Type")
@Description("The potion type of a tipped arrow projectile.")
@Examples("""
	set potion type of {_arrow} to long poison
	delete potion type of event-entity""")
@Since("1.0.0")
public class ExprArrowPotionType extends SimplePropertyExpression<Entity, PotionType> {

	static {
		register(ExprArrowPotionType.class, PotionType.class, "potion type", "entities");
	}

	@Override
	public @Nullable PotionType convert(Entity entity) {
		if (entity instanceof Arrow arrow) {
			return arrow.getPotion().potion();
		}
		return null;
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
		for (Entity entity : getExpr().getArray(event)) {
			if (!(entity instanceof Arrow arrow)) continue;
			PotionContents current = arrow.getPotion();
			arrow.setPotion(new PotionContents(
					mode == ChangeMode.SET ? type : null,
					current.customColor(),
					current.customEffects(),
					current.customName()
			));
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
