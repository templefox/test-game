package com.subway.model.passenger;

import com.subway.GameCenter;
import com.subway.LogicCore;
import com.subway.model.Passenger;
import com.subway.model.Station;
import com.subway.model.Station.Shape_type;

public class QuinPassenger extends Passenger{

	public QuinPassenger(Station station,LogicCore logicCore) {
		super(GameCenter.passengers[3], station,Shape_type.quinquangular,logicCore);
	}

}
