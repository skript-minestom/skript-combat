package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.EntityInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithBlockEvent;
import net.minestom.server.instance.block.Block;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class ProjectileCollideBlockWrapper extends EventWrapper<ProjectileCollideWithBlockEvent> implements EntityInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.simple(ProjectileCollideBlockWrapper.class, Block.class, from -> from.getEvent().getBlock()));
		EventValues.registerEventValue(EventValue.simple(ProjectileCollideBlockWrapper.class, Point.class, from -> from.getEvent().getCollisionPosition()));
	}

	public ProjectileCollideBlockWrapper(ProjectileCollideWithBlockEvent event) {
		super(event);
	}

}
