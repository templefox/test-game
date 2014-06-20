package com.subway.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.subway.GameScreen;

public class MessageBoard implements Ui {
	private VerticalGroup group = new VerticalGroup();
	private Stage stage;

	public MessageBoard() {

	}

	public void appendText(String text, long time) {
		appendText(text, time,Color.BLACK);
	}
	
	public void appendText(String text,long time,Color color){
		final Label label = new Label(text, GameScreen.skin);
		label.setColor(color);
		group.addActor(label);
		label.addAction(Actions.delay(time, new RunnableAction() {
			@Override
			public void run() {
				group.removeActor(label);
			}
		}));
	}

	@Override
	public void setPosition(float x, float y) {
		group.setPosition(x, y);
	}

	@Override
	public void addToStage(Stage stage) {
		stage.addActor(group);
		this.stage = stage;
		group.setZIndex(10);
	}

	@Override
	public float getHeight() {
		return group.getHeight();
	}

	@Override
	public float getWidth() {
		return group.getWidth();
	}

}
