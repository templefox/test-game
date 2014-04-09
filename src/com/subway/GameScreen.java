package com.subway;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.subway.model.Station.station_type;

public class GameScreen implements Screen {
	private GameCenter gameCenter;
	private LogicCore logicCore ;
	private Stage stage;
	public static Label label;
	public GameScreen(GameCenter gameCenter) {
		this.gameCenter = gameCenter;
		stage = new Stage();
		logicCore = new LogicCore(gameCenter,stage);
		Gdx.input.setInputProcessor(stage);
		
		Skin skin = new Skin(Gdx.files.internal("images/uiskin.json"));
		label = new Label("xxx", skin);
		label.setPosition(10, stage.getHeight()-label.getWidth());
		stage.addActor(label);
		
		logicCore.createStation(station_type.square,100,100);
		logicCore.createStation(station_type.circle, 400, 200);
		logicCore.createStation(station_type.square, 600, 100);
		logicCore.createStation(station_type.circle, 420, 100);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(255, 255, 255, 0);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		logicCore.dispose();
	}

}
