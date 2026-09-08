package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.EntityInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.EquipmentDamageEvent;
import net.minestom.server.entity.EquipmentSlot;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class EquipmentDamageWrapper extends EventWrapper<EquipmentDamageEvent> implements EntityInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.simple(EquipmentDamageWrapper.class, EquipmentSlot.class, from -> from.getEvent().getSlot()));
		EventValues.registerEventValue(EventValue.builder(EquipmentDamageWrapper.class, Integer.class)
			.patterns("amount")
			.getter(from -> from.getEvent().getAmount())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setAmount(value))
			.build());
	}

	public EquipmentDamageWrapper(EquipmentDamageEvent event) {
		super(event);
	}

}
