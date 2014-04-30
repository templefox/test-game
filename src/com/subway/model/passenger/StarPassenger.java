package com.subway.model.passenger;

import com.subway.GameCenter;
import com.subway.LogicCore;
import com.subway.model.Passenger;
import com.subway.model.Station;
import com.subway.model.Station.Shape_type;

public class StarPassenger extends Passenger{
	public StarPassenger(Station station,LogicCore logicCore) {
		super(GameCenter.passengers[4], station,Shape_type.star,logicCore);
		// TODO Auto-generated constructor stub
	}
}
