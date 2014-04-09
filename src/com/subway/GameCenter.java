package com.subway;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameCenter extends Game {
	public static TextureRegion[] colors = new TextureRegion[7];
	public static TextureRegion[] passengers = new TextureRegion[5];
	public static TextureRegion[] stations = new TextureRegion[5];
	@Override
	public void create() {
		loadPics();
		setScreen(new GameScreen(this));
	}
	private void loadPics() {
		Texture t = new Texture(Gdx.files.internal("images/colors.png"));
		TextureRegion[][] colors = TextureRegion.split(t, 1, 1);
		for (int i = 0; i < 7; i++) {
			this.colors[i] = colors[0][i];
		}
		
		Texture passengersTexture = new Texture(
				Gdx.files.internal("images/passengers.png"));
		TextureRegion textureRegion[][] = TextureRegion.split(
				passengersTexture, 32, 32);
		for (int i = 0; i < 5; i++) {
			this.passengers[i] = textureRegion[0][i];
		}
		
		Texture stationTexture = new Texture(Gdx.files.internal("images/stations.png"));
		TextureRegion stationsRegion[][] = TextureRegion.split(stationTexture, 128, 128);
		for (int i = 0; i < 5; i++) {
			stations[i]= stationsRegion[0][i]; 
		}
	}
	@Override
	public void render() {
	
		super.render();
	}

}
