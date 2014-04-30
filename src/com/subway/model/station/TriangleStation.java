package com.subway.model.station;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.subway.GameCenter;
import com.subway.LogicCore;
import com.subway.model.Station;
import com.subway.model.Station.Shape_type;

public class TriangleStation extends Station {


	public TriangleStation(String name, LogicCore core) {
		super(GameCenter.stations[1], core);
		image.setName(name+"#triangle");
		type = Shape_type.triangle;
	}

	@Override
	public String toString() {
		return "("+image.getX()+","+image.getY()+")"+"triangle";
	}

}
