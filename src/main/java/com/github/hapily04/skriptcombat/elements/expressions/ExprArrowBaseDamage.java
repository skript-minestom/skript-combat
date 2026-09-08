package com.github.hapily04.skriptcombat.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import io.github.togar2.pvp.entity.projectile.AbstractArrow;
import net.minestom.server.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Arrow Base Damage")
@Description("The base damage of an arrow or other abstract arrow projectile.")
@Examples("""
	set base damage of {_arrow} to 5
	add 2 to arrow base damage of event-entity""")
@Since("1.0.0")
public class ExprArrowBaseDamage extends SimplePropertyExpression<Entity, Number> {

	static {
		register(ExprArrowBaseDamage.class, Number.class, "[arrow] base damage", "entities");
	}

	@Override
	public @Nullable Number convert(Entity entity) {
		if (entity instanceof AbstractArrow arrow) {
			return arrow.getBaseDamage();
		}
		return null;
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case SET, ADD, REMOVE, RESET -> CollectionUtils.array(Number.class);
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		double amount = delta == null || delta.length == 0 || !(delta[0] instanceof Number number) ? 0 : number.doubleValue();
		for (Entity entity : getExpr().getArray(event)) {
			if (!(entity instanceof AbstractArrow arrow)) continue;
			switch (mode) {
				case SET -> arrow.setBaseDamage(amount);
				case ADD -> arrow.setBaseDamage(arrow.getBaseDamage() + amount);
				case REMOVE -> arrow.setBaseDamage(arrow.getBaseDamage() - amount);
				case RESET -> arrow.setBaseDamage(2.0);
			}
		}
	}

	@Override
	protected String getPropertyName() {
		return "arrow base damage";
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

}
