package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.PlayerInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.PlayerExhaustEvent;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class PlayerExhaustWrapper extends EventWrapper<PlayerExhaustEvent> implements PlayerInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.builder(PlayerExhaustWrapper.class, Number.class)
			.patterns("amount", "exhaustion")
			.getter(from -> from.getEvent().getAmount())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setAmount(value.floatValue()))
			.build());
	}

	public PlayerExhaustWrapper(PlayerExhaustEvent event) {
		super(event);
	}

}
