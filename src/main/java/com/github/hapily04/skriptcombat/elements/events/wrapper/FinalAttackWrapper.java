package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.EntityInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.FinalAttackEvent;
import net.minestom.server.entity.Entity;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class FinalAttackWrapper extends EventWrapper<FinalAttackEvent> implements EntityInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.builder(FinalAttackWrapper.class, Entity.class)
			.patterns("attacker")
			.getter(from -> from.getEvent().getEntity())
			.build());
		EventValues.registerEventValue(EventValue.builder(FinalAttackWrapper.class, Entity.class)
			.patterns("victim")
			.getter(from -> from.getEvent().getTarget())
			.build());

		EventValues.registerEventValue(EventValue.builder(FinalAttackWrapper.class, Boolean.class)
			.patterns("critical")
			.getter(from -> from.getEvent().isCritical())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setCritical(value))
			.build());
		EventValues.registerEventValue(EventValue.builder(FinalAttackWrapper.class, Boolean.class)
			.patterns("sweeping")
			.getter(from -> from.getEvent().isSweeping())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setSweeping(value))
			.build());
		EventValues.registerEventValue(EventValue.builder(FinalAttackWrapper.class, Boolean.class)
			.patterns("sprint")
			.getter(from -> from.getEvent().isSprint())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setSprint(value))
			.build());
		EventValues.registerEventValue(EventValue.builder(FinalAttackWrapper.class, Boolean.class)
			.patterns("attack-sounds")
			.getter(from -> from.getEvent().hasAttackSounds())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setAttackSounds(value))
			.build());
		EventValues.registerEventValue(EventValue.builder(FinalAttackWrapper.class, Boolean.class)
			.patterns("play-sounds-on-fail")
			.getter(from -> from.getEvent().playSoundsOnFail())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setPlaySoundsOnFail(value))
			.build());

		EventValues.registerEventValue(EventValue.builder(FinalAttackWrapper.class, Number.class)
			.patterns("base-damage")
			.getter(from -> from.getEvent().getBaseDamage())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setBaseDamage(value.floatValue()))
			.build());
		EventValues.registerEventValue(EventValue.builder(FinalAttackWrapper.class, Number.class)
			.patterns("enchant-damage")
			.getter(from -> from.getEvent().getEnchantsExtraDamage())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setEnchantsExtraDamage(value.floatValue()))
			.build());
	}

	public FinalAttackWrapper(FinalAttackEvent event) {
		super(event);
	}

}
