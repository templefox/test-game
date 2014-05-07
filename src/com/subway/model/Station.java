package com.subway.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import android.R.integer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.subway.GameCenter;
import com.subway.LogicCore;
import com.subway.model.Viehcle.ViehcleData;
import com.subway.model.passenger.CirclePassenger;
import com.subway.model.passenger.QuinPassenger;
import com.subway.model.passenger.SquarePasenger;
import com.subway.model.passenger.StarPassenger;
import com.subway.model.passenger.TrianglePassenger;
import com.subway.model.station.CircleStation;
import com.subway.model.station.QuinStation;
import com.subway.model.station.SquareStation;
import com.subway.model.station.StarStation;
import com.subway.model.station.TriangleStation;

public abstract class Station {
	public Image image;
	private final int MAX = 25;
	private boolean isSelected = false;
	private LogicCore logicCore;
	protected Shape_type type;
	private final static float pad = 2;
	private List<Passenger> passengers = new ArrayList<Passenger>();
	private ReentrantLock lock = new ReentrantLock();
	
	public static enum Shape_type {
		circle, square, star, triangle, quinquangular
	};

	public Station(TextureRegion region, LogicCore core) {
		image = new Image(region);
		image.addListener(onClick);
		logicCore = core;
		image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
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
		image.addAction(BlinkAction.pool.obtain());
		logicCore.selectStation(this);
	}

	public void unselect() {
		isSelected = false;
		image.clearActions();

		Color color = image.getColor();
		color.a = 1f;
		image.setColor(color);
		logicCore.unselectStation(this);
	}

	public static Station newStation(Shape_type type, String name,
			LogicCore core) {
		switch (type) {
		case circle:
			return new CircleStation(name, core);
		case square:
			return new SquareStation(name, core);
		case triangle:
			return new TriangleStation(name, core);
		case quinquangular:
			return new QuinStation(name, core);
		case star:
			return new StarStation(name, core);
		default:
			throw new RuntimeException();
		}
	}

	public void generatePassenger(Shape_type[] shape_types) {
		if (passengers.size() > logicCore.getGameMode().getStationLimit()) {
			logicCore.getGameMode().onStationFull();
			return;
		}
		if (shape_types.length<1) {
			return;
		}
		
		Passenger passenger = null;
		if (MathUtils.randomBoolean(logicCore.getGameMode().genPassangerRate())) {
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

	public Shape_type getType() {
		return type;
	}

	public List<Passenger> getPassengers() {
		return passengers;
	}

	public boolean byebyePassenger(Passenger passenger) {
		passenger.image.remove();
		boolean result= passengers.remove(passenger);
		if (result) {
			rePositonPassenger();			
		}
		return result;
	}

	public void loadPassenger(Passenger passenger) {
		passengers.add(passenger);
		rePositonPassenger();
		logicCore.drawPassagerToStation(passenger, this);
	}
	
	private void rePositonPassenger(){
		int i = 0;
		for (Iterator iterator = passengers.iterator(); iterator.hasNext();i++) {
			Passenger passenger = (Passenger) iterator.next();
			int offset = i % 4;
			int h = i / 4;
			passenger.image.setPosition(image.getX() + (passenger.image.getWidth()+pad)
					* offset, image.getY() - passenger.image.getHeight()
					- (passenger.image.getHeight()+pad) * h);
		}
	}

	public void lock() {
		lock.lock();
	}
	
	public void unlock() {
		lock.unlock();
	}
}
