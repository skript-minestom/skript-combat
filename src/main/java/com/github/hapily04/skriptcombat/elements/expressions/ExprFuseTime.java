package com.github.hapily04.skriptcombat.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import io.github.togar2.pvp.entity.explosion.TntEntity;
import net.minestom.server.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("TNT Fuse Time")
@Description("The fuse time in ticks of a primed TNT combat entity.")
@Examples("""
	set fuse time of {_tnt} to 40
	set fuse of event-entity to 1""")
@Since("1.0.0")
public class ExprFuseTime extends SimplePropertyExpression<Entity, Number> {

	static {
		register(ExprFuseTime.class, Number.class, "fuse [time]", "entities");
	}

	@Override
	public @Nullable Number convert(Entity entity) {
		if (entity instanceof TntEntity tnt) {
			return tnt.getFuse();
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
			if (!(entity instanceof TntEntity tnt)) continue;
			int current = tnt.getFuse();
			int next = switch (mode) {
				case SET -> amount;
				case ADD -> current + amount;
				case REMOVE -> current - amount;
				case RESET -> 80;
				default -> current;
			};
			tnt.setFuse(next);
		}
	}

	@Override
	protected String getPropertyName() {
		return "fuse time";
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

}
