package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.PlayerInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.PlayerRegenerateEvent;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class PlayerRegenerateWrapper extends EventWrapper<PlayerRegenerateEvent> implements PlayerInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.builder(PlayerRegenerateWrapper.class, Number.class)
			.patterns("amount", "health")
			.getter(from -> from.getEvent().getAmount())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setAmount(value.floatValue()))
			.build());
		EventValues.registerEventValue(EventValue.builder(PlayerRegenerateWrapper.class, Number.class)
			.patterns("exhaustion")
			.getter(from -> from.getEvent().getExhaustion())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setExhaustion(value.floatValue()))
			.build());
	}

	public PlayerRegenerateWrapper(PlayerRegenerateEvent event) {
		super(event);
	}

}
