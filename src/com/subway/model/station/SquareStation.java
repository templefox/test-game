package com.subway.model.station;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.subway.GameCenter;
import com.subway.LogicCore;
import com.subway.model.Station;

public class SquareStation extends Station {

	public SquareStation(String name, LogicCore core) {
		super(GameCenter.stations[2], core);
		setName(name+"#square");
	}

}
