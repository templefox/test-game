package com.subway.ui;

import java.util.HashMap;
import java.util.Iterator;

import android.graphics.PointF;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.subway.GameScreen;
import com.subway.GameCenter;
import com.subway.LogicCore;
import com.subway.model.Line;
import com.subway.model.Line.line_type;

public class LineSelector {
	private VerticalGroup group = new VerticalGroup();
	private Image selectedImage = new Image();
	private float padTop = 10f;
	private float padRight = 5f;
	private float scaleX = 80;
	private float scaleY = 15;
	private HashMap<line_type, Image> imageMap = new HashMap<Line.line_type, Image>(
			7);
	private Stage stage;
	private ClickListener onSelectClicked = new ClickListener() {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			final HashMap<line_type, Line> lines = Line.getOldLines();
			for (final line_type type : lines.keySet()) {
				if (imageMap.containsKey(type)) {
					continue;
				} else {
					MyImage image = new MyImage(lines.get(type).getRegion());
					image.setWidth(image.getOldPrefWidth() * scaleX);
					image.setHeight(image.getOldPrefHeight() * scaleY);
					image.setScaling(Scaling.stretch);
					image.setAlign(Align.center);
					imageMap.put(type, image);
					
					image.addListener(new ClickListener(){

						@Override
						public void clicked(InputEvent event, float x, float y) {
							selectedImage.setDrawable(new TextureRegionDrawable(lines.get(type).getRegion()));
							Color color = selectedImage.getColor();
							color.a = 1f;
							selectedImage.setColor(color);
							logicCore.setSelectedLine(lines.get(type));
							group.remove();
						}});
					
					stage.addActor(image);
				}
			}
			for (Image iterable : imageMap.values()) {
				group.addActorAt(0,iterable);
			}
			Color color = selectedImage.getColor();
			color.a = 0.3f;
			selectedImage.setColor(color);
			stage.addActor(group);
			group.setZIndex(11);
			GameScreen.label.setText("clicked");
		}
	};
	private LogicCore logicCore;

	public LineSelector(LogicCore core) {
		selectedImage.addListener(onSelectClicked);
		group.setSpacing(10);
		this.logicCore = core;
		//group.set
	}

	public void setSelectedLine(line_type type) {
		selectedImage.setDrawable(new TextureRegionDrawable(Line.getOldLines()
				.get(type).getRegion()));
		selectedImage.setWidth(selectedImage.getPrefWidth() * scaleX);
		selectedImage.setHeight(selectedImage.getPrefHeight() * scaleY);
		selectedImage.setScaling(Scaling.stretch);
		selectedImage.setAlign(Align.center);
	}

	public void setPosition(float x, float y) {
		selectedImage.setPosition(x - padRight, y - padTop);
		group.setPosition(x-selectedImage.getWidth()*0.66f-padRight, y - padTop);
	}

	public void addToStage(Stage stage) {

		stage.addActor(selectedImage);
		selectedImage.setZIndex(10);
		this.stage = stage;
	}

	public float getHeight() {
		return Math.max(group.getHeight(), selectedImage.getHeight());
	}

	public float getWidth() {
		return Math.max(group.getWidth(), selectedImage.getWidth());
	}

	public void setPadTop(float padTod) {
		this.padTop = padTod;
	}

	public void setpadRight(float padLeft) {
		this.padRight = padLeft;
	}

	class MyImage extends Image {

		public MyImage() {
			super();
		}

		public MyImage(Drawable drawable) {
			super(drawable);
		}

		public MyImage(TextureRegion region) {
			super(region);
		}

		@Override
		public float getPrefWidth() {
			return getWidth();
		}

		@Override
		public float getPrefHeight() {
			return getHeight();
		}

		public float getOldPrefHeight() {
			return super.getPrefHeight();
		}

		public float getOldPrefWidth() {
			return super.getPrefWidth();
		}

	}
}
