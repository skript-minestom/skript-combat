package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.PlayerInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.PlayerSpectateEvent;
import net.minestom.server.entity.Entity;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class CombatSpectateWrapper extends EventWrapper<PlayerSpectateEvent> implements PlayerInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.builder(CombatSpectateWrapper.class, Entity.class)
			.patterns("target")
			.getter(from -> from.getEvent().getTarget())
			.build());
	}

	public CombatSpectateWrapper(PlayerSpectateEvent event) {
		super(event);
	}

}
