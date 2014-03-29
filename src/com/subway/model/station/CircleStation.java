package com.subway.model.station;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.subway.GameCenter;
import com.subway.LogicCore;
import com.subway.model.Station;
import com.subway.system.MetroSystem;

public class CircleStation extends Station {

	public CircleStation(String name, LogicCore core,MetroSystem system) {
		super(GameCenter.stations[0], core,system);
		setName(name+"#circle");
	}

}
