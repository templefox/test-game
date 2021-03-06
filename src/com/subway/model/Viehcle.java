package com.subway.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;

import android.R.integer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.IntIntMap;
import com.subway.GameCenter;
import com.subway.GameScreen;
import com.subway.LogicCore;

public class Viehcle extends Observable {
	public Image image_v;
	public Group image;
	private final float velocity = 98;
	private Line line;
	private LogicCore logicCore;
	private Station fromStation;
	private ViehcleData data;
	private LinePart nextLinePart;
	private static final int pad = 2;
	private HashSet<Passenger> passengers = new HashSet<Passenger>();
	public final static int MAX_PASSENGERS = 9;
	private boolean inverse = false;

	public Viehcle(Line line, Station station) {
		image = new Group();
		image_v = new Image(GameCenter.viecleTexture);
		image_v.setColor(line.color);
		this.line = line;
		this.logicCore = line.getLogicCore();
		fromStation = station;
		nextLinePart = line.getLineParts().getFirst();
		image_v.setOrigin(image_v.getWidth() / 2, image_v.getHeight() / 2);

		image.addActor(image_v);
		startWork();
	}

	public void startWork() {

		float theta = nextLinePart.image.getRotation();
		image_v.setPosition(
				fromStation.image.getX() + fromStation.image.getWidth() / 2
						- image_v.getWidth() / 2,
				fromStation.image.getY() + fromStation.image.getHeight() / 2
						- image_v.getHeight() / 2);
		image.setOrigin(image_v.getX() + image_v.getWidth() / 2, image_v.getY()
				+ image_v.getHeight() / 2);
		// image_v.setScale(scaleX, scaleY);
		image.setRotation(theta);
		logicCore.addViecleToStage(this);
		SequenceAction action = new SequenceAction();
		action.addAction(createMoveAction());
		action.addAction(createOnStationAction(action));
		image.addAction(action);
	}

	public MoveByAction createMoveAction() {
		image.setRotation(nextLinePart.image.getRotation());
		MoveByAction moveByAction = new MoveByAction();
		int i = inverse ? -1 : 1;
		moveByAction.setAmount(i * (nextLinePart.to.x - nextLinePart.from.x), i
				* (nextLinePart.to.y - nextLinePart.from.y));
		moveByAction.setDuration(nextLinePart.getLength() / velocity);
		return moveByAction;
	}

	public RunnableAction createOnStationAction(
			final SequenceAction sequenceAction) {
		RunnableAction runnableAction = new RunnableAction() {
		};
		runnableAction.setRunnable(new Runnable() {

			@Override
			public void run() {
				// 达到新站
				fromStation = inverse ? nextLinePart.s1 : nextLinePart.s2;
				LinePart last = nextLinePart;
				nextLinePart = (LinePart) line.nextEdge(nextLinePart, inverse);
				if (nextLinePart == null) {
					inverse = !inverse;
					nextLinePart = last;
				}
				float delay = dealPassengers(fromStation, nextLinePart,
						inverse ? nextLinePart.s1 : nextLinePart.s2);
				sequenceAction.addAction(new DelayAction(delay));
				sequenceAction.addAction(createMoveAction());
				sequenceAction.addAction(createOnStationAction(sequenceAction));
			}
		});
		return runnableAction;
	}

	public float dealPassengers(Station current, LinePart linePart, Station next) {
		// 通知这个站的乘客和车上的乘客
		deleteObservers();
		for (Passenger passenger : passengers) {
			addObserver(passenger);
		}
		
		for (Passenger passenger : current.getPassengers()) {
			addObserver(passenger);
		}
		
		data = new ViehcleData(current, linePart, next);
		setChanged();
		notifyObservers(data);

		return data.numToLoad;
	}

	public static class ViehcleData {
		public Station current;
		public LinePart linePart;
		public Station next;
		public Integer numToLoad = 0;

		public ViehcleData(Station current, LinePart linePart, Station next) {
			this.current = current;
			this.linePart = linePart;
			this.next = next;
		}

	}

	public void loadPassengers(Passenger passenger) {
		passenger.image.setColor(Color.GRAY);
		try {
			passenger.image.getActions().get(0).restart();			
		} catch (IndexOutOfBoundsException e) {
			GameScreen.label.setText("I won't forgive you!");
		}
		passengers.add(passenger);
		rePositionPassengers();
	}

	public boolean contains(Passenger passenger) {
		return passengers.contains(passenger);
	}

	public void unloadPassenger(Passenger passenger) {
		passengers.remove(passenger);
		passenger.image.remove();
		rePositionPassengers();
	}

	private void rePositionPassengers() {
		int i = 0;
		for (Iterator iterator = passengers.iterator(); iterator.hasNext(); i++) {
			Passenger passenger = (Passenger) iterator.next();
			int offset = i % 4;
			int h = i / 4;
			passenger.image.setPosition(
					image_v.getX() + image_v.getWidth()
							* (1 - image_v.getScaleX()) / 2 + offset
							* (passenger.image.getWidth() + pad),
					image_v.getY() + image_v.getHeight()
							* (1 + image_v.getScaleY()) / 2 + h
							* (pad + passenger.image.getHeight()));
			image.addActor(passenger.image);
		}
	}

	public boolean hasEmptyPosition() {
		return passengers.size() < MAX_PASSENGERS;
	}
}
