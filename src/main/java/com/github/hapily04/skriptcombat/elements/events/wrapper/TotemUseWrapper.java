package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.EntityInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.TotemUseEvent;
import net.minestom.server.entity.PlayerHand;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class TotemUseWrapper extends EventWrapper<TotemUseEvent> implements EntityInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.simple(TotemUseWrapper.class, PlayerHand.class, from -> from.getEvent().getHand()));
	}

	public TotemUseWrapper(TotemUseEvent event) {
		super(event);
	}

}
