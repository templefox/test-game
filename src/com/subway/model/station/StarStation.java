package com.subway.model.station;

import android.util.Log;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.subway.GameCenter;
import com.subway.GameScreen;
import com.subway.LogicCore;
import com.subway.model.Passenger;
import com.subway.model.Station;
import com.subway.model.Station.Shape_type;
import com.subway.model.passenger.CirclePassenger;
import com.subway.model.passenger.QuinPassenger;
import com.subway.model.passenger.SquarePasenger;
import com.subway.model.passenger.StarPassenger;
import com.subway.model.passenger.TrianglePassenger;

public class StarStation extends Station {
	private static float timeCheck = 0;
	private final static float boomTime = 120;
	private final static float boomLong = 10;
	private final static int NORMAL = 0;
	private final static int BOOM = 1;
	private static int state = NORMAL;
	
	public StarStation(String name, LogicCore core) {
		super(GameCenter.stations[4], core);
		image.setName(name+"#star");
		type = Shape_type.star;
	}

	@Override
	public String toString() {
		return "("+image.getX()+","+image.getY()+")"+"star";
	}

	@Override
	public void generatePassenger(Shape_type[] shape_types) {
		if (NORMAL==state) {
			super.generatePassenger(shape_types);			
		}else {
			if (passengers.size() > logicCore.getGameMode().getStationLimit()) {
				logicCore.getGameMode().onStationFull();
				return;
			}
			if (shape_types.length<1) {
				return;
			}
			
			Passenger passenger = null;
			if (MathUtils.randomBoolean((float) Math.cbrt(logicCore.getGameMode().genPassangerRate()))) {
				// do
				int i = MathUtils.random(0, shape_types.length - 1);
				Shape_type type = shape_types[i];

				switch (type) {
				case circle:
					passenger = new CirclePassenger(this, logicCore);
					break;
				case square:
					passenger = new SquarePasenger(this,logicCore);
					break;
				case star:
					passenger = new StarPassenger(this,logicCore);
					break;
				case quinquangular:
					passenger = new QuinPassenger(this,logicCore);
					break;
				case triangle:
					passenger = new TrianglePassenger(this,logicCore);
					break;
				default:
					throw new IllegalStateException();
				}
				if (passenger != null) {
					loadPassenger(passenger);
				}
			}
			return;
		}
	}

	@Override
	public void update(float detla){
		timeCheck+=detla;
		if (state==NORMAL) {
			if (timeCheck>boomTime) {
				timeCheck=0;
				state=BOOM;
				GameScreen.messageBoard.appendText("Star booming!!", (long) boomLong/2);
			}
		}else if(state==BOOM){
			if (timeCheck>boomLong) {
				timeCheck=0;
				state=NORMAL;
				GameScreen.messageBoard.appendText("Booming end", (long) 3);
			}
		}
	}	
}
