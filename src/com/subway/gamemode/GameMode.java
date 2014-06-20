package com.subway.gamemode;

import com.subway.LogicCore;

import android.R.integer;

public interface GameMode {
	void onPassagerToDestination();
	void onStationFull();
	void setProperties(LogicCore core);
	int getCycleLimit();
	int getStationLimit();
	int getRepeatLimit();
	void initLines(LogicCore core);
	float genPassangerRate();
	void update(float delta,LogicCore logicCore);
	void onPassagerRed(LogicCore logicCore);
	int getScore();
}
