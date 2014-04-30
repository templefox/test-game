package com.subway.model.station;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.subway.GameCenter;
import com.subway.LogicCore;
import com.subway.model.Station;
import com.subway.model.Station.Shape_type;

public class StarStation extends Station {
	public StarStation(String name, LogicCore core) {
		super(GameCenter.stations[4], core);
		image.setName(name+"#star");
		type = Shape_type.star;
	}

	@Override
	public String toString() {
		return "("+image.getX()+","+image.getY()+")"+"star";
	}

}
