package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.EntityInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.entity.EntityShootEvent;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class EntityShootWrapper extends EventWrapper<EntityShootEvent> implements EntityInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.builder(EntityShootWrapper.class, Entity.class)
			.patterns("projectile")
			.getter(from -> from.getEvent().getProjectile())
			.build());
		EventValues.registerEventValue(EventValue.builder(EntityShootWrapper.class, Number.class)
			.patterns("power")
			.getter(from -> from.getEvent().getPower())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setPower(value.doubleValue()))
			.build());
		EventValues.registerEventValue(EventValue.builder(EntityShootWrapper.class, Number.class)
			.patterns("spread")
			.getter(from -> from.getEvent().getSpread())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setSpread(value.doubleValue()))
			.build());
	}

	public EntityShootWrapper(EntityShootEvent event) {
		super(event);
	}

}
