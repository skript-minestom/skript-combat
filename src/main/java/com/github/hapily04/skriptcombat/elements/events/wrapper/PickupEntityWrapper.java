package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.PlayerInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.PickupEntityEvent;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class PickupEntityWrapper extends EventWrapper<PickupEntityEvent> implements PlayerInstanceEventMarker {

	static {
		// PickupEntityEvent is not a PlayerEvent; register player explicitly.
		EventValues.registerEventValue(EventValue.simple(PickupEntityWrapper.class, Player.class, from -> from.getEvent().getPlayer()));
		EventValues.registerEventValue(EventValue.builder(PickupEntityWrapper.class, Entity.class)
			.patterns("picked-up")
			.getter(from -> from.getEvent().getPickedUp())
			.build());
	}

	public PickupEntityWrapper(PickupEntityEvent event) {
		super(event);
	}

}
