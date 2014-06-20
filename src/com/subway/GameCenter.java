package com.subway;

import android.app.Activity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameCenter extends Game {
	public static TextureRegion[] colors = new TextureRegion[9];
	public static TextureRegion[] passengers = new TextureRegion[5];
	public static TextureRegion[] stations = new TextureRegion[5];
	public static Texture viecleTexture;
	public static final float SCREEN_HEIGHT = 540;
	public static final float SCREEN_WIDTH = 960;
	public static final float DENSITY = 1.5f;
	private GameScreen gameScreen;
	private Activity activity;
	
	public GameCenter(Activity activity) {
		super();
		this.activity = activity;
	}
	
	@Override
	public void create() {
		loadPics();
		MenuScreen menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	private void loadPics() {
		Texture t = new Texture(Gdx.files.internal("images/colors.png"));
		TextureRegion[][] colors = TextureRegion.split(t, 1, 1);
		for (int i = 0; i < 9; i++) {
			this.colors[i] = colors[0][i];
		}

		Texture passengersTexture = new Texture(
				Gdx.files.internal("images/passengers.png"));
		TextureRegion textureRegion[][] = TextureRegion.split(
				passengersTexture, 16, 16);
		for (int i = 0; i < 5; i++) {
			this.passengers[i] = textureRegion[0][i];
		}

		Texture stationTexture = new Texture(
				Gdx.files.internal("images/stations.png"));
		TextureRegion stationsRegion[][] = TextureRegion.split(stationTexture,
				64, 64);
		for (int i = 0; i < 5; i++) {
			stations[i] = stationsRegion[0][i];
		}

		viecleTexture = new Texture(Gdx.files.internal("images/viehcle.png"));
	}

	@Override
	public void render() {

		super.render();
	}

	public void setGameScreen(GameScreen gameScreen2) {
		gameScreen =gameScreen2;
	}

	public Activity getActivity() {
		return activity;
	}
}
