package com.subway.gamemode;

import com.badlogic.gdx.math.MathUtils;
import com.subway.GameCenter;
import com.subway.GameScreen;
import com.subway.LogicCore;
import com.subway.model.Line;
import com.subway.model.Line.line_type;
import com.subway.model.Station.Shape_type;

public class DefaultGameMode implements GameMode {
	private float rate = 0.3f;
	private int score = 0;
	private final static int LOSE = 2;
	private final static int RUN = 1;
	private int state = RUN;

	public DefaultGameMode() {

	}

	@Override
	public void onPassagerToDestination() {
		if (state == LOSE) {
			return;
		} else {
			score += 10;
			GameScreen.label.setText(Integer.toString(score));
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
		logicCore.setSelectedLine(Line.getOrNewLine(line_type.red, logicCore));
		Line.getOrNewLine(line_type.orange, logicCore);
		Line.getOrNewLine(line_type.blue, logicCore);

		logicCore.createStation(Shape_type.circle, MathUtils.random(300, 400),
				MathUtils.random(200, 300));
		logicCore.createStation(Shape_type.square, MathUtils.random(200, 300),
				MathUtils.random(250, 400));
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

	@Override
	public void update(float delta, LogicCore logicCore) {
		count += delta;
		count2 += delta;
		if (count > 1) {
			count -= 1;
			score += 1;
			GameScreen.label.setText(Integer.toString(score));
		}
		if (count2 > f) {
			count2 -= f;
			if (rate < 0.8) {
				rate += 0.01;
			}

			if (MathUtils.randomBoolean(0.9f)) {
				if (x < 200) {
					x++;
					f = (float) (7 * Math.pow(x, -0.1) + 8);
				}

				if (MathUtils.randomBoolean(0.8f)) {
					Shape_type type = null;
					int r = MathUtils.random(0, 13);
					if (r == 0) {
						type = Shape_type.quinquangular;
					} else if (r < 3) {
						type = Shape_type.star;
					} else if (r < 6) {
						type = Shape_type.triangle;
					} else if (r < 10) {
						type = Shape_type.square;
					} else if (r < 14) {
						type = Shape_type.circle;
					}

					int rr = 0;
					switch (type) {
					case circle:
						rr = circleNum;
						break;
					case triangle:
						rr = triNum;
						break;
					case square:
						rr = rectNum;
						break;
					case star:
						rr = starNum;
						break;
					case quinquangular:
						rr = quinNum;
						break;
					default:
						break;
					}

					if (MathUtils.randomBoolean((float) Math
							.pow(rr + 1f, -0.7f))) {
						if (logicCore.createStation(type,
								MathUtils.random(50, 800),
								MathUtils.random(50, 400))) {
							switch (type) {
							case circle:
								++circleNum;
								break;
							case triangle:
								++triNum;
								break;
							case square:
								++rectNum;
								break;
							case star:
								++starNum;
								break;
							case quinquangular:
								++quinNum;
								break;
							default:
								break;
							}
						}
					}

				}

				/*
				 * if (MathUtils.randomBoolean(0.8f)) { Shape_type type
				 * logicCore.createStation(Shape_type.circle,
				 * MathUtils.random(50, 800), MathUtils.random(50, 400)); } else
				 * if (MathUtils.randomBoolean(0.65f)) {
				 * logicCore.createStation(Shape_type.square,
				 * MathUtils.random(50, 800), MathUtils.random(50, 400)); } else
				 * if (MathUtils.randomBoolean(0.52f)) {
				 * logicCore.createStation(Shape_type.triangle,
				 * MathUtils.random(50, 800), MathUtils.random(50, 400)); } else
				 * if (MathUtils.randomBoolean(0.39f)) {
				 * logicCore.createStation(Shape_type.quinquangular,
				 * MathUtils.random(50, 800), MathUtils.random(50, 400)); } else
				 * if (MathUtils.randomBoolean(0.29f)) {
				 * logicCore.createStation(Shape_type.star, MathUtils.random(50,
				 * 800), MathUtils.random(50, 400)); }
				 */
			}
		}
	}

	@Override
	public int getStationLimit() {
		return 11;
	}

}
