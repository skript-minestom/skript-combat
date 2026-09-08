package com.github.hapily04.skriptcombat.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.github.hapily04.skriptcombat.elements.util.AppliedPotion;
import io.github.togar2.pvp.entity.projectile.Arrow;
import net.minestom.server.entity.Entity;
import net.minestom.server.item.component.PotionContents;
import net.minestom.server.potion.CustomPotionEffect;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Name("Arrow Potion Effects")
@Description("The custom potion effects applied by a tipped arrow projectile.")
@Examples("""
	set potion contents of {_arrow} to poison 2 for 5 seconds
	add slowness to potion of {_arrow}""")
@Since("1.0.0")
public class ExprArrowPotion extends PropertyExpression<Entity, AppliedPotion> {

	static {
		register(ExprArrowPotion.class, AppliedPotion.class, "[arrow] potion [contents|effects]", "entities");
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		setExpr((Expression<? extends Entity>) exprs[0]);
		return true;
	}

	@Override
	protected AppliedPotion[] get(Event event, Entity[] source) {
		List<AppliedPotion> effects = new ArrayList<>();
		for (Entity entity : source) {
			if (!(entity instanceof Arrow arrow)) continue;
			for (CustomPotionEffect custom : arrow.getPotion().customEffects()) {
				effects.add(AppliedPotion.from(custom));
			}
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
		for (Entity entity : getExpr().getArray(event)) {
			if (!(entity instanceof Arrow arrow)) continue;
			PotionContents current = arrow.getPotion();
			List<CustomPotionEffect> customs = new ArrayList<>(current.customEffects());
			switch (mode) {
				case ADD -> {
					for (AppliedPotion effect : effects) {
						customs.add(effect.toCustomPotionEffect());
					}
				}
				case SET -> {
					customs.clear();
					for (AppliedPotion effect : effects) {
						customs.add(effect.toCustomPotionEffect());
					}
				}
				case REMOVE -> customs.removeIf(custom -> {
					for (AppliedPotion effect : effects) {
						if (custom.id() == effect.type()
								&& (effect.amplifier() < 0 || custom.settings().amplifier() == effect.amplifier())) {
							return true;
						}
					}
					return false;
				});
				case DELETE, RESET -> customs.clear();
			}
			arrow.setPotion(new PotionContents(current.potion(), current.customColor(), customs, current.customName()));
		}
	}

	@Override
	public Class<? extends AppliedPotion> getReturnType() {
		return AppliedPotion.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "potion contents of " + getExpr().toString(event, debug);
	}

}
