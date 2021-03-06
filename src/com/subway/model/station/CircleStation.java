package com.subway.model.station;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.subway.GameCenter;
import com.subway.LogicCore;
import com.subway.model.Station;

public class CircleStation extends Station {

	public CircleStation(String name, LogicCore core) {
		super(GameCenter.stations[0], core);
		image.setName(name+"#circle");
		type = Shape_type.circle;
	}
	@Override
	public String toString() {
		return "("+image.getX()+","+image.getY()+")"+"circle";
	}


}
