package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.EntityInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.FinalDamageEvent;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class FinalDamageWrapper extends EventWrapper<FinalDamageEvent> implements EntityInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.builder(FinalDamageWrapper.class, Number.class)
			.patterns("damage")
			.getter(from -> from.getEvent().getDamage().getAmount())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().getDamage().setAmount(value.floatValue()))
			.build());
		EventValues.registerEventValue(EventValue.builder(FinalDamageWrapper.class, Integer.class)
			.patterns("invulnerability-ticks")
			.getter(from -> from.getEvent().getInvulnerabilityTicks())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setInvulnerabilityTicks(value))
			.build());
		EventValues.registerEventValue(EventValue.builder(FinalDamageWrapper.class, Boolean.class)
			.patterns("animate")
			.getter(from -> from.getEvent().shouldAnimate())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setAnimate(value))
			.build());
		EventValues.registerEventValue(EventValue.builder(FinalDamageWrapper.class, Boolean.class)
			.patterns("killing")
			.getter(from -> from.getEvent().doesKillEntity())
			.build());
	}

	public FinalDamageWrapper(FinalDamageEvent event) {
		super(event);
	}

}
