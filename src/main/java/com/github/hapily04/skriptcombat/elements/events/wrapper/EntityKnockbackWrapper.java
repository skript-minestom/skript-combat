package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.EntityInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.EntityKnockbackEvent;
import io.github.togar2.pvp.feature.knockback.KnockbackSettings;
import net.minestom.server.ServerFlag;
import net.minestom.server.entity.Entity;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class EntityKnockbackWrapper extends EventWrapper<EntityKnockbackEvent> implements EntityInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.builder(EntityKnockbackWrapper.class, Entity.class)
			.patterns("attacker")
			.getter(from -> from.getEvent().getAttacker())
			.build());

		EventValues.registerEventValue(EventValue.builder(EntityKnockbackWrapper.class, Number.class)
			.patterns("horizontal")
			.getter(from -> from.getEvent().getSettings().horizontal())
			.registerChanger(Changer.ChangeMode.SET, (event, value) ->
				event.getEvent().setSettings(with(event.getEvent().getSettings(), value.doubleValue(), null, null, null, null)))
			.build());
		EventValues.registerEventValue(EventValue.builder(EntityKnockbackWrapper.class, Number.class)
			.patterns("vertical")
			.getter(from -> from.getEvent().getSettings().vertical())
			.registerChanger(Changer.ChangeMode.SET, (event, value) ->
				event.getEvent().setSettings(with(event.getEvent().getSettings(), null, value.doubleValue(), null, null, null)))
			.build());
		EventValues.registerEventValue(EventValue.builder(EntityKnockbackWrapper.class, Number.class)
			.patterns("vertical-limit")
			.getter(from -> from.getEvent().getSettings().verticalLimit())
			.registerChanger(Changer.ChangeMode.SET, (event, value) ->
				event.getEvent().setSettings(with(event.getEvent().getSettings(), null, null, value.doubleValue(), null, null)))
			.build());
		EventValues.registerEventValue(EventValue.builder(EntityKnockbackWrapper.class, Number.class)
			.patterns("extra-horizontal")
			.getter(from -> from.getEvent().getSettings().extraHorizontal())
			.registerChanger(Changer.ChangeMode.SET, (event, value) ->
				event.getEvent().setSettings(with(event.getEvent().getSettings(), null, null, null, value.doubleValue(), null)))
			.build());
		EventValues.registerEventValue(EventValue.builder(EntityKnockbackWrapper.class, Number.class)
			.patterns("extra-vertical")
			.getter(from -> from.getEvent().getSettings().extraVertical())
			.registerChanger(Changer.ChangeMode.SET, (event, value) ->
				event.getEvent().setSettings(with(event.getEvent().getSettings(), null, null, null, null, value.doubleValue())))
			.build());
	}

	/**
	 * KnockbackSettings' constructor multiplies by TPS, while accessors return already-scaled values.
	 * Divide by TPS when reconstructing so SET keeps the same units as GET.
	 */
	private static KnockbackSettings with(KnockbackSettings settings, Double horizontal, Double vertical,
	                                      Double verticalLimit, Double extraHorizontal, Double extraVertical) {
		double tps = ServerFlag.SERVER_TICKS_PER_SECOND;
		return new KnockbackSettings(
			(horizontal != null ? horizontal : settings.horizontal()) / tps,
			(vertical != null ? vertical : settings.vertical()) / tps,
			(verticalLimit != null ? verticalLimit : settings.verticalLimit()) / tps,
			(extraHorizontal != null ? extraHorizontal : settings.extraHorizontal()) / tps,
			(extraVertical != null ? extraVertical : settings.extraVertical()) / tps
		);
	}

	public EntityKnockbackWrapper(EntityKnockbackEvent event) {
		super(event);
	}

}
