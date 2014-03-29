package com.subway;

import android.R.integer;
import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuScreen implements Screen {
	private ImageButton startButton;
	private Stage stage;
	private Image bg;
	private Texture bgTexture;
	private boolean onScreenSwift = false;
	private final float HEIGHT = 40;
	private final int JUMP_ACCURATE = 10;
	private final float G = 9.8f * 70f;// m/ss to mm/ss.Not accurate gravity
										// simulation.
	private final float DELAY_TIME_FACTOR = 0.4f;
	private final float WAIT_TIME = 0.8f;
	private final float SWIFT_TIME = 4f;
	private final int DISPLAY_LINE_NUM = 4;
	private Image[] passengers = new Image[5];
	private Image[] lines = new Image[7];
	private Group passGroup;
	private GameCenter gameCenter;

	public MenuScreen(GameCenter gameCenter) {
		super();
		this.gameCenter = gameCenter;
		stage = new Stage();
		passGroup = new Group();
		initUI();
		initPassengers();
		stage.addActor(passGroup);
		passGroup.setZIndex(2);

		initLines();

		Gdx.input.setInputProcessor(stage);
	}

	private void initLines() {
		
		for (int i = DISPLAY_LINE_NUM - 1; i >= 0; i--) {
			lines[i] = new Image(gameCenter.colors[i]);
			lines[i].setPosition(8 - i * 2f, 50f - i * 3f);

			lines[i].scale(250, 10);
			stage.addActor(lines[i]);
			lines[i].setZIndex(5 - i);
		}
	}

	private void initPassengers() {
		
		for (int i = 0; i < passengers.length; i++) {
			passengers[i] = new Image(gameCenter.passengers[i]);
			passengers[i].setPosition(
					108 + 33 * i, 65);
			stage.addActor(passengers[i]);
			passGroup.addActor(passengers[i]);
			Action jumpAction = getJumpAction(i);
			passengers[i].setZIndex(2);
			passengers[i].addAction(jumpAction);
		}
	}

	private Action getJumpAction(int position) {
		SequenceAction jumpAction = null;
		MoveByAction[] moveByAction = new MoveByAction[JUMP_ACCURATE * 2];
		float height_part = HEIGHT / JUMP_ACCURATE;
		for (int i = 0; i < moveByAction.length; i++) {
			moveByAction[i] = new MoveByAction();
			if (i < JUMP_ACCURATE) {
				moveByAction[i].setAmount(0, height_part);
				moveByAction[i].setDuration((float) ((Math.sqrt(i + 1) - Math
						.sqrt(i)) * Math.sqrt(2 * height_part / G)));
				if (jumpAction == null) {
					jumpAction = Actions.sequence(moveByAction[i]);
				} else {
					jumpAction = Actions.sequence(moveByAction[i], jumpAction);
				}
			} else {
				int j = i - JUMP_ACCURATE + 1;
				moveByAction[i].setAmount(0, -height_part);
				moveByAction[i].setDuration((float) ((Math.sqrt(j) - Math
						.sqrt(j - 1)) * Math.sqrt(2 * height_part / G)));
				jumpAction = Actions.sequence(jumpAction, moveByAction[i]);
			}
		}
		return Actions.sequence(new DelayAction(
				(Math.abs(position - 1.1f) * DELAY_TIME_FACTOR)), Actions
				.repeat(RepeatAction.FOREVER, Actions.sequence(new DelayAction(
						WAIT_TIME), jumpAction)));
	}

	private void initUI() {
		Texture startTexture = new Texture(
				Gdx.files.internal("gfx/menu/play.png"));
		TextureRegion region = new TextureRegion(startTexture);
		TextureRegionDrawable drawable = new TextureRegionDrawable(region);
		startButton = new ImageButton(drawable);
		startButton.setPosition(
				(Gdx.graphics.getWidth() - startButton.getWidth()) / 2f,
				Gdx.graphics.getHeight() / 3f * 1);

		bgTexture = new Texture(
				Gdx.files.internal("gfx/menu/menu_background.png"));
		bg = new Image(bgTexture);
		bg.setWidth(Gdx.graphics.getWidth());
		bg.setHeight(Gdx.graphics.getHeight());
		bg.setPosition(0, 0);

		stage.addActor(startButton);
		stage.addActor(bg);

		bg.setZIndex(0);
		startButton.setZIndex(1);
	}

	@Override
	public void resize(int width, int height) {
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
		stage.dispose();
		bgTexture.dispose();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(255, 255, 255, 0);
		if (!onScreenSwift && startButton.isPressed()) {
			onScreenSwift = true;

			setup_runGroupAction();
		}
		stage.act(delta);
		stage.draw();
	}

	private void setup_runGroupAction() {
		MoveToAction groupAction = new MoveToAction();
		groupAction.setPosition(Gdx.graphics.getWidth(), passGroup.getOriginY());
		groupAction.setDuration(SWIFT_TIME);
		passGroup.addAction(Actions.sequence(groupAction, new RunnableAction() {
			@Override
			public void run() {
				toGameScreen();
			}
		}));

		for (int i = 0; i < DISPLAY_LINE_NUM; i++) {
			ScaleToAction lineAction = new ScaleToAction();
			lineAction.setScale(Gdx.graphics.getWidth()+50, 10);
			lineAction.setDuration(SWIFT_TIME*0.75f);
			lines[i].addAction(lineAction);
		}
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	private void toGameScreen() {
		AlphaAction alphaAction = new AlphaAction();
		alphaAction.setAlpha(0);
		alphaAction.setDuration(0.5f);
		stage.addAction(Actions.sequence(alphaAction, new RunnableAction(){

			@Override
			public void run() {
				gameCenter.setScreen(new GameScreen(gameCenter));
			}}));
	}

}
