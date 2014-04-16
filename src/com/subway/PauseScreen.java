package com.subway;

import java.util.Iterator;

import android.R.anim;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;

public class PauseScreen implements Screen {
	LogicCore logicCore;
	Stage stage;
	Stage oldStage;
	public PauseScreen(LogicCore logicCore, Stage old){
		this.logicCore = logicCore;
		oldStage = old;		
		stage = new Stage(old.getWidth(),old.getHeight(),true,old.getSpriteBatch());
		
		
		Image image = new Image(GameCenter.colors[7]);
		image.setBounds(30, 30, stage.getWidth()-60, stage.getHeight()-60);
		Color color = image.getColor();
		color.a = 0.9f;
		image.setColor(color);
		stage.addActor(image);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(255, 255, 255, 0);
		
		oldStage.draw();
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		
		TextButton textButton = new TextButton("buy new line",GameScreen.skin);
		textButton.setPosition(200, 200);
		stage.addActor(textButton);
		
		Array<Actor> actors = oldStage.getActors();
		for (Actor actor : actors) {
			Color color = actor.getColor();
			color.a = 0.2f;
			actor.setColor(color);
		}
	}

	@Override
	public void hide() {
		Array<Actor> actors = oldStage.getActors();
		for (Actor actor : actors) {
			Color color = actor.getColor();
			color.a = 1f;
			actor.setColor(color);
		}
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
		// TODO Auto-generated method stub

	}

}
