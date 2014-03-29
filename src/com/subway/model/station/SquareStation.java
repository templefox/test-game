package com.subway.model.station;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.subway.GameCenter;
import com.subway.LogicCore;
import com.subway.model.Station;
import com.subway.system.MetroSystem;

public class SquareStation extends Station {

	public SquareStation(String name, LogicCore core,MetroSystem system) {
		super(GameCenter.stations[2], core,system);
		setName(name+"#square");
	}

}
