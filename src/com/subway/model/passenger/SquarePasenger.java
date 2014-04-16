package com.subway.model.passenger;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.subway.GameCenter;
import com.subway.LogicCore;
import com.subway.model.Passenger;
import com.subway.model.Station;
import com.subway.model.Station.Shape_type;

public class SquarePasenger extends Passenger {

	public SquarePasenger(Station station,LogicCore logicCore) {
		super(GameCenter.passengers[2], station,Shape_type.square,logicCore);
		// TODO Auto-generated constructor stub
	}



}
