package com.subway;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.subway.GameScreen.Game_state;
import com.subway.gamemode.DefaultGameMode;
import com.subway.model.Line;
import com.subway.model.Line.line_type;
import com.subway.model.Station.Shape_type;
import com.subway.ui.LineSelector;

public class GameScreen implements Screen {
	private GameCenter gameCenter;
	private LogicCore logicCore;
	private Stage stage;
	private Screen pauseScreen;

	public static enum Game_state {
		RUN, PAUSE
	};

	private Game_state state = Game_state.RUN;
	public static Skin skin = new Skin(Gdx.files.internal("images/uiskin.json"));
	public static Label label;

	public GameScreen(final GameCenter gameCenter) {
		this.gameCenter = gameCenter;
		stage = new Stage();
		pauseScreen = new PauseScreen(logicCore, stage);
		logicCore = new LogicCore(gameCenter, stage, new DefaultGameMode());
		logicCore.getGameMode().initLines(logicCore);

		Gdx.input.setInputProcessor(stage);

		Skin skin = new Skin(Gdx.files.internal("images/uiskin.json"));
		label = new Label("xx", skin);
		label.setPosition(10, stage.getHeight() - label.getHeight());
		label.setColor(Color.BLACK);
		stage.addActor(label);

		LineSelector lineSelector = new LineSelector(logicCore);
		lineSelector.setSelectedLine(line_type.red);
		lineSelector.setPosition(stage.getWidth() - lineSelector.getWidth(),
				stage.getHeight() - lineSelector.getHeight());
		// lineSelector.setPosition(stage.getWidth()-lineSelector.getWidth(),
		// stage.getHeight()-lineSelector.getHeight());
		lineSelector.addToStage(stage);

		/*
		 * VerticalGroup verticalGroup = new VerticalGroup(); for(int i=
		 * 0;i<5;i++){ Image image = new Image(GameCenter.stations[i]);
		 * verticalGroup.addActor(image); } stage.addActor(verticalGroup);
		 * verticalGroup.setPosition(200, 200);
		 */

		

		TextButton textButton = new TextButton("pause", skin);
		textButton.setPosition(stage.getWidth() - textButton.getWidth() - 10,
				10);
		textButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				state = Game_state.PAUSE;
				gameCenter.setScreen(pauseScreen);
			}
		});
		stage.addActor(textButton);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(255, 255, 255, 0);

		switch (state) {
		case RUN:
			logicCore.update(delta);
			stage.act(delta);
			stage.draw();
			break;
		case PAUSE:
			stage.draw();

			break;
		default:
			break;
		}
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

	public void setState(Game_state state) {
		this.state = state;
	}

}
