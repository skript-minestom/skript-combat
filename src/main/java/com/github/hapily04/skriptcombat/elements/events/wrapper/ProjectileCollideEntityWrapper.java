package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.EntityInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithEntityEvent;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class ProjectileCollideEntityWrapper extends EventWrapper<ProjectileCollideWithEntityEvent> implements EntityInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.builder(ProjectileCollideEntityWrapper.class, Entity.class)
			.patterns("victim", "target")
			.getter(from -> from.getEvent().getTarget())
			.build());
		EventValues.registerEventValue(EventValue.simple(ProjectileCollideEntityWrapper.class, Point.class, from -> from.getEvent().getCollisionPosition()));
	}

	public ProjectileCollideEntityWrapper(ProjectileCollideWithEntityEvent event) {
		super(event);
	}

}
