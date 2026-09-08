package com.github.hapily04.skriptcombat.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import io.github.togar2.pvp.entity.projectile.SpectralArrow;
import net.minestom.server.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Spectral Arrow Duration")
@Description("The glowing effect duration in ticks applied by a spectral arrow.")
@Examples("""
	set spectral duration of {_arrow} to 200
	set spectral arrow duration of event-entity to 40""")
@Since("1.0.0")
public class ExprSpectralDuration extends SimplePropertyExpression<Entity, Number> {

	static {
		register(ExprSpectralDuration.class, Number.class, "spectral [arrow] duration", "entities");
	}

	@Override
	public @Nullable Number convert(Entity entity) {
		if (entity instanceof SpectralArrow arrow) {
			return arrow.getDuration();
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
			if (!(entity instanceof SpectralArrow arrow)) continue;
			int current = arrow.getDuration();
			int next = switch (mode) {
				case SET -> amount;
				case ADD -> current + amount;
				case REMOVE -> current - amount;
				case RESET -> 200;
				default -> current;
			};
			arrow.setDuration(next);
		}
	}

	@Override
	protected String getPropertyName() {
		return "spectral arrow duration";
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

}
