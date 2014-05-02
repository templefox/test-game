package com.subway.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;

public interface Ui {

	public abstract void setPosition(float x, float y);

	public abstract void addToStage(Stage stage);

	public abstract float getHeight();

	public abstract float getWidth();

}