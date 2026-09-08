package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.PlayerInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.AnchorChargeEvent;
import net.minestom.server.coordinate.Point;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class AnchorChargeWrapper extends EventWrapper<AnchorChargeEvent> implements PlayerInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.simple(AnchorChargeWrapper.class, Point.class, from -> from.getEvent().getBlockPosition()));
	}

	public AnchorChargeWrapper(AnchorChargeEvent event) {
		super(event);
	}

}
