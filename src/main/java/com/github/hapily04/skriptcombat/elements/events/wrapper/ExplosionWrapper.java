package com.github.hapily04.skriptcombat.elements.events.wrapper;

import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.events.wrapper.marker.InstanceEventMarker;
import io.github.togar2.pvp.events.ExplosionEvent;

public class ExplosionWrapper extends EventWrapper<ExplosionEvent> implements InstanceEventMarker {

	public ExplosionWrapper(ExplosionEvent event) {
		super(event);
	}

}
