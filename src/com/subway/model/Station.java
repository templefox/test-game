package com.subway.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.subway.LogicCore;
import com.subway.model.station.CircleStation;
import com.subway.model.station.SquareStation;

public abstract class Station extends Image {
	private final int MAX = 25;
	private boolean isSelected = false;
	private LogicCore logicCore;
	public enum station_type {
		circle, square, star, triangle, quinquangular
	};

	public Station(TextureRegion region, LogicCore core) {
		super(region);
		this.addListener(onClick);
		logicCore = core;
		setOrigin(getWidth()/2, getHeight()/2);
	}

	private ClickListener onClick = new ClickListener() {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			if (isSelected) {
				unselect();
			} else {
				select();
			}
		}
	};

	public void select() {
		isSelected = true;
		this.addAction(BlinkAction.pool.obtain());
		logicCore.selectStation(this);
	}

	public void unselect() {
		isSelected = false;
		this.clearActions();

		this.setColor(getColor().r, getColor().g, getColor().b, 1f);
		logicCore.unselectStation(this);
	}

	public static Station newStation(station_type type, String name, LogicCore core) {
		switch (type) {
		case circle:
			return new CircleStation(name, core);
		case square:
			return new SquareStation(name, core);
		default:
			throw new RuntimeException();
		}
	}

}
