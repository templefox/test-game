package com.subway;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameCenter extends Game {
	public TextureRegion[] colors = new TextureRegion[7];
	public TextureRegion[] passengers = new TextureRegion[5];
	public TextureRegion[] stations = new TextureRegion[5];
	@Override
	public void create() {
		setScreen(new MenuScreen(this));
	}
	@Override
	public void render() {
	
		super.render();
	}

}
