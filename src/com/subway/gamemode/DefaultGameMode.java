package com.subway.gamemode;

import android.renderscript.Element;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.IntIntMap;
import com.subway.GameCenter;
import com.subway.GameScreen;
import com.subway.LogicCore;
import com.subway.model.Line;
import com.subway.model.Line.line_type;
import com.subway.model.Station.Shape_type;

public class DefaultGameMode implements GameMode {
	private float rate = 0.003f;
	private int score = 0;
	private final static int LOSE = 2;
	private final static int RUN = 1;
	private int state = RUN;
	private LogicCore logicCore;

	public DefaultGameMode() {

	}

	@Override
	public void onPassagerToDestination() {
		if (state == LOSE) {
			return;
		} else {
			score += 10;
			synchronized (this) {
				GameScreen.label.setText(Integer.toString(score));				
			}
		}
	}

	@Override
	public void onPassagerRed(LogicCore logicCore) {
		logicCore.setLose();
	}

	@Override
	public void onStationFull() {

	}

	@Override
	public void setProperties(LogicCore core) {
	}

	@Override
	public int getCycleLimit() {
		return 0;
	}

	@Override
	public int getRepeatLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void initLines(LogicCore logicCore) {
		this.logicCore = logicCore;
		logicCore.setSelectedLine(Line.getOrNewLine(line_type.red, logicCore));
		Line.getOrNewLine(line_type.orange, logicCore);
		Line.getOrNewLine(line_type.blue, logicCore);
		Line.getOrNewLine(line_type.green, logicCore);
		logicCore.createStation(Shape_type.circle,
				MathUtils.random(50, 800),
				MathUtils.random(50, 400));logicCore.createStation(Shape_type.square,
						MathUtils.random(10, 920),
						MathUtils.random(10, 500));
		/*logicCore.createStation(Shape_type.circle,100,100);
		logicCore.createStation(Shape_type.circle,250,100);
		logicCore.createStation(Shape_type.square,400,100);
		logicCore.createStation(Shape_type.square,550,100);
		logicCore.createStation(Shape_type.quinquangular,700,100);
		logicCore.createStation(Shape_type.quinquangular,730,400);
		logicCore.createStation(Shape_type.star,101,400);
		logicCore.createStation(Shape_type.star,249,400);
		logicCore.createStation(Shape_type.triangle,400,400);
		logicCore.createStation(Shape_type.triangle,550,400);*/
		
	}

	@Override
	public float genPassangerRate() {
		return rate;
	}

	private float count = 0;
	private float count2 = 0;
	private float f = 15;
	private float x = 1;
	private int circleNum = 0;
	private int triNum = 0;
	private int rectNum = 0;
	private int quinNum = 0;
	private int starNum = 0;

	private float stationCount = 0;
	private float rateCount = 0;
	private float scoreCount = 0;
	
	@Override
	public void update(float delta, LogicCore logicCore) {

		if (rateCount>30) {
			updateRate();
			rateCount=0;
		}else {
			rateCount+=delta;
		}
		
		if (stationCount>9) {
			newStation();
			stationCount=0;
		}else {
			stationCount+=delta;
		}
		
		if (scoreCount>1) {
			score++;
			synchronized (this) {
				GameScreen.label.setText(Integer.toString(score));				
			}
			scoreCount=0;
		}else {
			scoreCount+=delta;
		}
	}

	private float stationRateBase = 0.6f;
	private float stationRate = stationRateBase;
	private void newStation() {
		if (!MathUtils.randomBoolean(stationRate)) {
			stationRate = (float) Math.sqrt(stationRate);
			return;
		}else {
			stationRate = stationRateBase;
		}
		Shape_type type = getType();
		for (int i = 0; i < 2&&!logicCore.createStation(type,
				MathUtils.random(10, 920),
				MathUtils.random(10, 500)); i++) {
			stationRate = (float) Math.sqrt(stationRate)/1.15f;
		}
	}

	private Shape_type getType() {
		Shape_type type = null;
		int r = MathUtils.random(0, 41);
		if (r < 5) {
			type = Shape_type.quinquangular;
		} else if (r < 13) {
			type = Shape_type.star;
		} else if (r < 22) {
			type = Shape_type.triangle;
		} else if (r < 32) {
			type = Shape_type.square;
		} else if (r < 42) {
			type = Shape_type.circle;
		}
		return type;
	}

	private void updateRate() {
		rate = (float) (Math.sqrt(rate)/8.5);
	}

	@Override
	public int getStationLimit() {
		return 7;
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return score;
	}

}
