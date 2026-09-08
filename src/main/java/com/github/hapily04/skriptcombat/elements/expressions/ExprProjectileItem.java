package com.github.hapily04.skriptcombat.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Item;
import ch.njol.util.coll.CollectionUtils;
import io.github.togar2.pvp.entity.projectile.Arrow;
import io.github.togar2.pvp.entity.projectile.FireworkRocket;
import io.github.togar2.pvp.entity.projectile.ItemHoldingProjectile;
import io.github.togar2.pvp.entity.projectile.ThrownPotion;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.metadata.item.SnowballMeta;
import net.minestom.server.entity.metadata.item.ThrownEggMeta;
import net.minestom.server.entity.metadata.item.ThrownEnderPearlMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.PotionContents;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Projectile Item")
@Description("The item associated with a combat projectile such as an arrow, snowball, or firework.")
@Examples("""
	set projectile item of {_arrow} to tipped arrow
	set item of {_snowball} to snowball named "special"
""")
@Since("1.0.0")
public class ExprProjectileItem extends SimplePropertyExpression<Entity, Item> {

	static {
		register(ExprProjectileItem.class, Item.class, "[projectile] item", "entities");
	}

	@Override
	public @Nullable Item convert(Entity entity) {
		ItemStack stack = getItemStack(entity);
		return stack == null ? null : new Item(stack);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case SET -> CollectionUtils.array(Item.class);
			case DELETE, RESET -> CollectionUtils.array();
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		ItemStack stack = ItemStack.AIR;
		if (mode == ChangeMode.SET) {
			if (delta == null || delta.length == 0 || !(delta[0] instanceof Item item)) return;
			stack = item.getItem();
		}
		for (Entity entity : getExpr().getArray(event)) {
			setItemStack(entity, stack);
		}
	}

	private static @Nullable ItemStack getItemStack(Entity entity) {
		if (entity instanceof Arrow arrow) {
			PotionContents potion = arrow.getPotion();
			if (potion.equals(PotionContents.EMPTY)) {
				return ItemStack.of(Material.ARROW);
			}
			return ItemStack.of(Material.TIPPED_ARROW).with(DataComponents.POTION_CONTENTS, potion);
		}
		if (entity instanceof FireworkRocket rocket) {
			return rocket.getItemStack();
		}
		if (entity instanceof ThrownPotion potion) {
			return potion.getItem();
		}
		if (entity.getEntityMeta() instanceof SnowballMeta meta) {
			return meta.getItem();
		}
		if (entity.getEntityMeta() instanceof ThrownEggMeta meta) {
			return meta.getItem();
		}
		if (entity.getEntityMeta() instanceof ThrownEnderPearlMeta meta) {
			return meta.getItem();
		}
		return null;
	}

	private static void setItemStack(Entity entity, ItemStack stack) {
		if (entity instanceof Arrow arrow) {
			arrow.setItemStack(stack);
			return;
		}
		if (entity instanceof FireworkRocket rocket) {
			rocket.setItemStack(stack);
			return;
		}
		if (entity instanceof ItemHoldingProjectile holding) {
			holding.setItem(stack);
		}
	}

	@Override
	protected String getPropertyName() {
		return "projectile item";
	}

	@Override
	public Class<? extends Item> getReturnType() {
		return Item.class;
	}

}
