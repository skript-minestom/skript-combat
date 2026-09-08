package com.github.hapily04.skriptcombat.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import io.github.togar2.pvp.entity.projectile.CustomEntityProjectile;
import net.minestom.server.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Projectile Shooter")
@Description("The shooter of a combat projectile entity.")
@Examples("""
	set shooter of {_arrow} to player
	broadcast "%shooter of event-entity%"
""")
@Since("1.0.0")
public class ExprShooter extends SimplePropertyExpression<Entity, Entity> {

	static {
		register(ExprShooter.class, Entity.class, "shooter", "entities");
	}

	@Override
	public @Nullable Entity convert(Entity entity) {
		if (entity instanceof CustomEntityProjectile projectile) {
			return projectile.getShooter();
		}
		return null;
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case SET -> CollectionUtils.array(Entity.class);
			case DELETE, RESET -> CollectionUtils.array();
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		Entity shooter = delta == null || delta.length == 0 ? null : (Entity) delta[0];
		for (Entity entity : getExpr().getArray(event)) {
			if (!(entity instanceof CustomEntityProjectile projectile)) continue;
			if (mode == ChangeMode.SET) {
				projectile.setShooter(shooter);
			} else {
				projectile.setShooter(null);
			}
		}
	}

	@Override
	protected String getPropertyName() {
		return "shooter";
	}

	@Override
	public Class<? extends Entity> getReturnType() {
		return Entity.class;
	}

}
