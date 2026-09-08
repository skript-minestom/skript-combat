package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.PlayerInstanceEventMarker;
import ch.njol.skript.registrations.EventValues;
import io.github.togar2.pvp.events.CrystalPlaceEvent;
import net.minestom.server.coordinate.Point;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class CrystalPlaceWrapper extends EventWrapper<CrystalPlaceEvent> implements PlayerInstanceEventMarker {

	static {
		EventValues.registerEventValue(EventValue.builder(CrystalPlaceWrapper.class, Point.class)
			.getter(from -> from.getEvent().getSpawnPosition())
			.registerChanger(Changer.ChangeMode.SET, (event, value) -> event.getEvent().setSpawnPosition(value))
			.build());
	}

	public CrystalPlaceWrapper(CrystalPlaceEvent event) {
		super(event);
	}

}
