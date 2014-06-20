package com.subway;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
import com.subway.ui.MessageBoard;

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
	public static MessageBoard messageBoard;

	public GameScreen(final GameCenter gameCenter) {
		this.gameCenter = gameCenter;
		gameCenter.setGameScreen(this);
		stage = new Stage();
		OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
		camera.position.set(GameCenter.SCREEN_WIDTH / 2,
				GameCenter.SCREEN_HEIGHT / 2, 0);
		camera.zoom = Math.max(
				GameCenter.SCREEN_HEIGHT / Gdx.graphics.getHeight(),
				GameCenter.SCREEN_WIDTH / Gdx.graphics.getWidth());
		Log.v("aaa", Float.toString(stage.getHeight()));
		pauseScreen = new PauseScreen(logicCore, stage);
		logicCore = new LogicCore(gameCenter, stage, new DefaultGameMode());
		logicCore.getGameMode().initLines(logicCore);

		Gdx.input.setInputProcessor(stage);

		Skin skin = new Skin(Gdx.files.internal("images/uiskin.json"));
		label = new Label("xx", skin);
		label.setPosition(10, GameCenter.SCREEN_HEIGHT - label.getHeight());
		label.setColor(Color.BLACK);
		stage.addActor(label);

		LineSelector lineSelector = new LineSelector(logicCore);
		lineSelector.setSelectedLine(line_type.red);
		lineSelector.setPosition(
				GameCenter.SCREEN_WIDTH - lineSelector.getWidth(),
				GameCenter.SCREEN_HEIGHT - lineSelector.getHeight());
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
		textButton.setPosition(GameCenter.SCREEN_WIDTH - textButton.getWidth()
				- 10, 10);
		textButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				state = Game_state.PAUSE;
				gameCenter.setScreen(pauseScreen);
			}
		});
		stage.addActor(textButton);
		
		TextButton textButton2 = new TextButton("restart", skin);
		textButton2.setPosition(GameCenter.SCREEN_WIDTH - textButton2.getWidth()*2
				- 20, 10);
		textButton2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				state = Game_state.PAUSE;
				gameCenter.setScreen(new MenuScreen(gameCenter));
			}
		});
		stage.addActor(textButton2);

		messageBoard = new MessageBoard();
		messageBoard.addToStage(stage);
		messageBoard.setPosition(GameCenter.SCREEN_WIDTH / 2,
				GameCenter.SCREEN_HEIGHT - messageBoard.getHeight());
		messageBoard.appendText("hi", 5);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(255, 255, 255, 0);
		switch (state) {
		case RUN:
			logicCore.update(delta);

			if (stage == null) {
				Log.e("sss", "stage is null");
			} else {

				stage.act(delta);
				stage.draw();
			}

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
