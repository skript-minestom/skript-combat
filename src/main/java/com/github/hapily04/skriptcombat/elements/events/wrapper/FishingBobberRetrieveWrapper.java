package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.PlayerInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.FishingBobberRetrieveEvent;
import net.minestom.server.entity.Entity;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class FishingBobberRetrieveWrapper extends EventWrapper<FishingBobberRetrieveEvent> implements PlayerInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.builder(FishingBobberRetrieveWrapper.class, Entity.class)
			.patterns("bobber")
			.getter(from -> from.getEvent().getBobber())
			.build());
	}

	public FishingBobberRetrieveWrapper(FishingBobberRetrieveEvent event) {
		super(event);
	}

}
