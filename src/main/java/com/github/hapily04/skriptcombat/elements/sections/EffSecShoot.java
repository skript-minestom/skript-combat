package com.github.hapily04.skriptcombat.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.events.wrapper.EntitySpawnWrapper;
import ch.njol.skript.lang.EffectSection;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.util.Direction;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import com.github.hapily04.skriptcombat.combat.CombatEntityFactory;
import com.github.hapily04.skriptcombat.combat.CombatEntityType;
import io.github.togar2.pvp.entity.projectile.CustomEntityProjectile;
import io.github.togar2.pvp.entity.projectile.FishingBobber;
import io.github.togar2.pvp.feature.projectile.VanillaFishingRodFeature;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.entity.EntitySpawnEvent;
import net.minestom.server.instance.Instance;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Name("Shoot Combat Projectile")
@Description("""
	Launches a combat projectile from an entity or point.
	Speed defaults to 1 when omitted.
	Uses the same launch path as vanilla bow/trident/misc projectile shooting.
	Optional section code runs before the projectile is spawned into the instance.""")
@Examples("""
	shoot an arrow from player
	shoot an arrow from player with speed 1.5
	shoot combat arrow from me:
		set base damage of entity to 5
	launch a snowball from {_pos} in {_arena} with speed 1 north""")
@Since("1.0.0")
public class EffSecShoot extends EffectSection {

	private static final double DEFAULT_POWER = 1.0;

	static {
		Skript.registerSection(EffSecShoot.class,
				"(shoot|launch) [a[n]] [combat] %combatentitytypes% (from|at) %entities% [with (speed|velocity|power) %-number%] [%directions%]",
				"(shoot|launch) [a[n]] [combat] %combatentitytypes% (from|at) %points% [in %instances%] [with (speed|velocity|power) %-number%] %directions%");
	}

	private Expression<CombatEntityType> types;
	private @Nullable Expression<Entity> entities;
	private @Nullable Expression<Point> points;
	private @Nullable Expression<Instance> instances;
	private @Nullable Expression<Number> power;
	private @Nullable Expression<Direction> directions;
	private boolean fromEntities;
	private @Nullable Trigger beforeSpawnTrigger;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult,
						@Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItems) {
		this.types = (Expression<CombatEntityType>) exprs[0];
		this.fromEntities = matchedPattern == 0;
		if (fromEntities) {
			this.entities = (Expression<Entity>) exprs[1];
			this.power = (Expression<Number>) exprs[2];
			this.directions = (Expression<Direction>) exprs[3];
		} else {
			this.points = (Expression<Point>) exprs[1];
			this.instances = (Expression<Instance>) exprs[2];
			this.power = (Expression<Number>) exprs[3];
			this.directions = (Expression<Direction>) exprs[4];
		}

		if (this.types instanceof Literal<?> literal) {
			for (Object value : literal.getAll()) {
				CombatEntityType type = (CombatEntityType) value;
				if (!type.isProjectile()) {
					Skript.error("'" + type + "' is not a projectile and cannot be shot");
					return false;
				}
			}
		}

		if (sectionNode != null) {
			beforeSpawnTrigger = loadCode(sectionNode, "shoot", EntitySpawnWrapper.class);
		}
		return true;
	}

	@Override
	protected @Nullable TriggerItem walk(Event event) {
		double power = DEFAULT_POWER;
		if (this.power != null) {
			Number powerNumber = this.power.getSingle(event);
			if (powerNumber == null) return super.walk(event, false);
			power = powerNumber.doubleValue();
			if (power <= 0) return super.walk(event, false);
		}

		CombatEntityType[] types = this.types.getArray(event);
		if (types.length == 0) return super.walk(event, false);

		Object variables = Variables.copyLocalVariables(event);
		Object mostRecentLocals = variables;

		if (fromEntities) {
			assert entities != null;
			Direction direction = this.directions == null ? null : this.directions.getSingle(event);
			for (CombatEntityType type : types) {
				if (!type.isProjectile()) continue;
				for (Entity shooter : entities.getArray(event)) {
					Instance instance = shooter.getInstance();
					if (instance == null) continue;
					mostRecentLocals = shootFromEntity(type, shooter, instance, power, direction, variables, mostRecentLocals);
				}
			}
		} else {
			assert points != null;
			assert directions != null;
			Direction direction = directions.getSingle(event);
			if (direction != null) {
				Vec dir = direction.getDirection();
				if (dir.lengthSquared() > 0) {
					Instance[] instances = this.instances == null ? new Instance[0] : this.instances.getArray(event);
					for (CombatEntityType type : types) {
						if (!type.isProjectile()) continue;
						for (Instance instance : instances) {
							for (Point point : points.getArray(event)) {
								mostRecentLocals = shootFromPoint(type, instance, point, power, dir, variables, mostRecentLocals);
							}
						}
					}
				}
			}
		}

		Variables.setLocalVariables(event, mostRecentLocals);
		return super.walk(event, false);
	}

	private Object shootFromEntity(CombatEntityType type, Entity shooter, Instance instance, double power,
			@Nullable Direction direction, Object variables, Object mostRecentLocals) {
		Entity created = CombatEntityFactory.create(type, instance, shooter);
		if (!(created instanceof CustomEntityProjectile projectile)) {
			created.remove();
			return mostRecentLocals;
		}

		if (projectile instanceof FishingBobber bobber && shooter instanceof Player player) {
			player.setTag(VanillaFishingRodFeature.FISHING_BOBBER, bobber);
		}

		mostRecentLocals = runBeforeSpawn(projectile, instance, variables, mostRecentLocals);

		Pos eye = shooter.getPosition().add(0, shooter.getEyeHeight() - 0.1, 0);
		Vec dir = null;
		if (direction != null) {
			Vec explicit = direction.getDirection(shooter);
			if (explicit.lengthSquared() > 0) {
				dir = explicit;
			}
		}

		if (dir != null) {
			projectile.shootAndLaunch(instance, eye, dir.x(), dir.y(), dir.z(), power, 1.0, shooter);
		} else {
			projectile.shootFromRotation(eye.pitch(), eye.yaw(), 0, power, 1.0);
			projectile.launchInto(instance, eye, shooter);
		}
		return mostRecentLocals;
	}

	private Object shootFromPoint(CombatEntityType type, Instance instance, Point point, double power, Vec dir,
			Object variables, Object mostRecentLocals) {
		Entity created = CombatEntityFactory.create(type, instance, null);
		if (!(created instanceof CustomEntityProjectile projectile)) {
			created.remove();
			return mostRecentLocals;
		}

		mostRecentLocals = runBeforeSpawn(projectile, instance, variables, mostRecentLocals);

		Pos spawn = point instanceof Pos pos ? pos : new Pos(point);
		projectile.shootAndLaunch(instance, spawn, dir.x(), dir.y(), dir.z(), power, 1.0, null);
		return mostRecentLocals;
	}

	private Object runBeforeSpawn(Entity projectile, Instance instance, Object variables, Object mostRecentLocals) {
		if (beforeSpawnTrigger == null) return mostRecentLocals;
		Event e = new EntitySpawnWrapper(new EntitySpawnEvent(projectile, instance));
		Variables.setLocalVariables(e, variables);
		TriggerItem.walk(beforeSpawnTrigger, e);
		Object updated = Variables.copyLocalVariables(e);
		Variables.removeLocals(e);
		return updated != null ? updated : mostRecentLocals;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		String speed = this.power == null ? "1" : this.power.toString(event, debug);
		if (fromEntities) {
			assert entities != null;
			return "shoot " + this.types.toString(event, debug) + " from " + this.entities.toString(event, debug)
					+ " with speed " + speed
					+ (this.directions == null ? "" : " " + this.directions.toString(event, debug));
		}
		assert points != null;
		assert directions != null;
		return "shoot " + this.types.toString(event, debug) + " from " + this.points.toString(event, debug)
				+ (this.instances == null ? "" : " in " + this.instances.toString(event, debug))
				+ " with speed " + speed + " " + this.directions.toString(event, debug);
	}

}
