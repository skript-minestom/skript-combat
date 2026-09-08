package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.EntityInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.DamageBlockEvent;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class DamageBlockWrapper extends EventWrapper<DamageBlockEvent> implements EntityInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.builder(DamageBlockWrapper.class, Number.class)
			.patterns("damage")
			.getter(from -> from.getEvent().getDamage())
			.build());
		EventValues.registerEventValue(EventValue.builder(DamageBlockWrapper.class, Number.class)
			.patterns("resulting-damage")
			.getter(from -> from.getEvent().getResultingDamage())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setResultingDamage(value.floatValue()))
			.build());
		EventValues.registerEventValue(EventValue.builder(DamageBlockWrapper.class, Boolean.class)
			.patterns("knockback-attacker")
			.getter(from -> from.getEvent().knockbackAttacker())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setKnockbackAttacker(value))
			.build());
	}

	public DamageBlockWrapper(DamageBlockEvent event) {
		super(event);
	}

}
