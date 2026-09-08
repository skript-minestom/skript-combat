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

@Name("Arrow Critical")
@Description("Whether an arrow projectile is a critical hit arrow.")
@Examples("""
	set arrow critical of {_arrow} to true
	if {_arrow} is critical:""")
@Since("1.0.0")
public class ExprArrowCritical extends SimplePropertyExpression<Entity, Boolean> {

	static {
		register(ExprArrowCritical.class, Boolean.class, "[arrow] critical", "entities");
	}

	@Override
	public @Nullable Boolean convert(Entity entity) {
		if (entity instanceof AbstractArrow arrow) {
			return arrow.isCritical();
		}
		return null;
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case SET, RESET -> CollectionUtils.array(Boolean.class);
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		boolean value = mode != ChangeMode.RESET
				&& delta != null
				&& delta.length > 0
				&& delta[0] instanceof Boolean bool
				&& bool;
		for (Entity entity : getExpr().getArray(event)) {
			if (!(entity instanceof AbstractArrow arrow)) continue;
			arrow.setCritical(mode == ChangeMode.RESET ? false : value);
		}
	}

	@Override
	protected String getPropertyName() {
		return "arrow critical";
	}

	@Override
	public Class<? extends Boolean> getReturnType() {
		return Boolean.class;
	}

}
