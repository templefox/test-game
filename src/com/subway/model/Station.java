package com.subway.model;

import java.util.jar.Attributes.Name;

import android.R.integer;
import android.util.Log;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.subway.LogicCore;
import com.subway.LogicCore.station_type;
import com.subway.model.station.CircleStation;
import com.subway.model.station.SquareStation;
import com.subway.system.MetroNode;
import com.subway.system.MetroSystem;

public abstract class Station extends MetroNode {
	private final int MAX = 25;
	private boolean isSelected = false;
	private LogicCore logicCore;

	public Station(TextureRegion region, LogicCore core,MetroSystem system) {
		super(region,system);
		this.addListener(onClick);
		logicCore = core;
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
			return new CircleStation(name, core,core);
		case square:
			return new SquareStation(name, core,core);
		default:
			throw new RuntimeException();
		}
	}

}
