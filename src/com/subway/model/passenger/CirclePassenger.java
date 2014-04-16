package com.subway.model.passenger;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.subway.GameCenter;
import com.subway.LogicCore;
import com.subway.model.Passenger;
import com.subway.model.Station;
import com.subway.model.Station.Shape_type;

public class CirclePassenger extends Passenger {

	public CirclePassenger(Station station,LogicCore logicCore) {
		super(GameCenter.passengers[0], station,Shape_type.circle,logicCore);
	}



}
