package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.PlayerInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.BedExplodeEvent;
import net.minestom.server.coordinate.Point;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class BedExplodeWrapper extends EventWrapper<BedExplodeEvent> implements PlayerInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.simple(BedExplodeWrapper.class, Point.class, from -> from.getEvent().getBlockPosition()));
	}

	public BedExplodeWrapper(BedExplodeEvent event) {
		super(event);
	}

}
