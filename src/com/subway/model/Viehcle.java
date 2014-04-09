package com.subway.model;

import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.subway.GameCenter;
import com.subway.GameScreen;
import com.subway.LogicCore;

public class Viehcle extends Image {
	private final float velocity = 98;
	private Line line;
	private LogicCore logicCore;
	private Station currentStation;
	private LinePart nextLinePart;
	private boolean inverse = false;

	public Viehcle(Line line, Station station) {
		super(GameCenter.stations[2]);
		this.line = line;
		this.logicCore = line.getLogicCore();
		currentStation = station;
		nextLinePart = line.getLineParts().getFirst();
		this.setOrigin(getWidth()/2, getHeight()/2);
		startWork();
	}

	public void startWork() {
		float scaleX = 0.5f;
		float scaleY = 0.3f;
		
		setScale(scaleX, scaleY);
		float theta = nextLinePart.image.getRotation();
		setRotation(theta);
		setPosition(nextLinePart.image.getX()-getWidth()/2, nextLinePart.image.getY()-getHeight()/2);
		logicCore.addViecleToStage(this);
		SequenceAction action = new SequenceAction();
		action.addAction(createMoveAction());
		action.addAction(createOnStationAction(action));
		this.addAction(action);
	}

	public MoveByAction createMoveAction() {
		setRotation(nextLinePart.image.getRotation());
		MoveByAction moveByAction = new MoveByAction();
		int i = inverse ? -1 : 1;
		moveByAction.setAmount(i * (nextLinePart.to.x-nextLinePart.from.x), i
				* (nextLinePart.to.y-nextLinePart.from.y));
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
				float delay = dealPassengers();

				LinePart last = nextLinePart;
				nextLinePart = (LinePart) line.nextEdge(nextLinePart, inverse);
				if (nextLinePart == null) {
					inverse = !inverse;
					nextLinePart = last;
				}

				sequenceAction.addAction(new DelayAction(delay));
				sequenceAction.addAction(createMoveAction());
				sequenceAction.addAction(createOnStationAction(sequenceAction));
			}
		});
		return runnableAction;
	}

	public float dealPassengers() {
		currentStation = inverse?nextLinePart.s1:nextLinePart.s2;
		GameScreen.label.setText(currentStation.getName());
		return 1;
	}
}
