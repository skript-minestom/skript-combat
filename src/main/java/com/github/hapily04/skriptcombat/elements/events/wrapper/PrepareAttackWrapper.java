package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.EntityInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.PrepareAttackEvent;
import net.minestom.server.entity.Entity;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class PrepareAttackWrapper extends EventWrapper<PrepareAttackEvent> implements EntityInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.builder(PrepareAttackWrapper.class, Entity.class)
			.patterns("attacker")
			.getter(from -> from.getEvent().getEntity())
			.build());
		EventValues.registerEventValue(EventValue.builder(PrepareAttackWrapper.class, Entity.class)
			.patterns("victim")
			.getter(from -> from.getEvent().getTarget())
			.build());
	}

	public PrepareAttackWrapper(PrepareAttackEvent event) {
		super(event);
	}

}
