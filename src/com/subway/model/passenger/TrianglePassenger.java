package com.subway.model.passenger;

import com.subway.GameCenter;
import com.subway.LogicCore;
import com.subway.model.Passenger;
import com.subway.model.Station;
import com.subway.model.Station.Shape_type;

public class TrianglePassenger extends Passenger{
	public TrianglePassenger(Station station,LogicCore logicCore) {
		super(GameCenter.passengers[1], station,Shape_type.triangle,logicCore);
		// TODO Auto-generated constructor stub
	}

}
