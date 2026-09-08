package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.EntityInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.PotionVisibilityEvent;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class PotionVisibilityWrapper extends EventWrapper<PotionVisibilityEvent> implements EntityInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.builder(PotionVisibilityWrapper.class, Boolean.class)
			.patterns("ambient")
			.getter(from -> from.getEvent().isAmbient())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setAmbient(value))
			.build());
		EventValues.registerEventValue(EventValue.builder(PotionVisibilityWrapper.class, Boolean.class)
			.patterns("invisible")
			.getter(from -> from.getEvent().isInvisible())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setInvisible(value))
			.build());
	}

	public PotionVisibilityWrapper(PotionVisibilityEvent event) {
		super(event);
	}

}
