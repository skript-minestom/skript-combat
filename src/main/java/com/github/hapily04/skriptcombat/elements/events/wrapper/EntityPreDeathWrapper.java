package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.EntityInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.EntityPreDeathEvent;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class EntityPreDeathWrapper extends EventWrapper<EntityPreDeathEvent> implements EntityInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.builder(EntityPreDeathWrapper.class, Boolean.class)
			.patterns("cancel-death")
			.getter(from -> from.getEvent().isCancelDeath())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setCancelDeath(value))
			.build());
	}

	public EntityPreDeathWrapper(EntityPreDeathEvent event) {
		super(event);
	}

}
