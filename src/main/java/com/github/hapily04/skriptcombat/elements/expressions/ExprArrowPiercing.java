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

@Name("Arrow Piercing")
@Description("The piercing level of an arrow projectile.")
@Examples("""
	set piercing of {_arrow} to 3
	add 1 to arrow piercing of event-entity""")
@Since("1.0.0")
public class ExprArrowPiercing extends SimplePropertyExpression<Entity, Number> {

	static {
		register(ExprArrowPiercing.class, Number.class, "[arrow] piercing [level]", "entities");
	}

	@Override
	public @Nullable Number convert(Entity entity) {
		if (entity instanceof AbstractArrow arrow) {
			return (int) arrow.getPiercingLevel();
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
		int amount = delta == null || delta.length == 0 || !(delta[0] instanceof Number number) ? 0 : number.intValue();
		for (Entity entity : getExpr().getArray(event)) {
			if (!(entity instanceof AbstractArrow arrow)) continue;
			int current = arrow.getPiercingLevel();
			byte next = switch (mode) {
				case SET -> (byte) amount;
				case ADD -> (byte) (current + amount);
				case REMOVE -> (byte) (current - amount);
				case RESET -> 0;
				default -> (byte) current;
			};
			arrow.setPiercingLevel(next);
		}
	}

	@Override
	protected String getPropertyName() {
		return "arrow piercing";
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

}
