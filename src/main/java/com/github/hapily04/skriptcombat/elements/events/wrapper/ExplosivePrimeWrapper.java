package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.InstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.ExplosivePrimeEvent;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class ExplosivePrimeWrapper extends EventWrapper<ExplosivePrimeEvent> implements InstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.simple(ExplosivePrimeWrapper.class, Point.class, from -> from.getEvent().getBlockPosition()));
		EventValues.registerEventValue(EventValue.builder(ExplosivePrimeWrapper.class, Integer.class)
			.patterns("fuse")
			.getter(from -> from.getEvent().getFuse())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setFuse(value))
			.build());
		EventValues.registerEventValue(EventValue.simple(ExplosivePrimeWrapper.class, Entity.class, from -> from.getEvent().getCause().causingEntity()));
	}

	public ExplosivePrimeWrapper(ExplosivePrimeEvent event) {
		super(event);
	}

}
