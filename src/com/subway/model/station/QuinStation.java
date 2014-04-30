package com.subway.model.station;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.subway.GameCenter;
import com.subway.LogicCore;
import com.subway.model.Station;
import com.subway.model.Station.Shape_type;

public class QuinStation extends Station {

	public QuinStation(String name, LogicCore core) {
		super(GameCenter.stations[3], core);
		image.setName(name+"#quinquangular");
		type = Shape_type.quinquangular;
	}

	@Override
	public String toString() {
		return "("+image.getX()+","+image.getY()+")"+"quinquangular";
	}

}
