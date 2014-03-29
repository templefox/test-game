package com.subway;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.subway.model.Line;
import com.subway.model.LinePart;
import com.subway.model.Station;
import com.subway.model.Line.line_type;
import com.subway.system.MetroSystem;

public class LogicCore extends MetroSystem implements Disposable{
	private GameCenter gameCenter;
	private Stage stage;
	private Station selectedStation;

	public enum station_type {
		circle, square, star, triangle, quinquangular
	};

	public LogicCore(GameCenter gameCenter, Stage stage) {
		this.gameCenter = gameCenter;
		this.stage = stage;
	}

	public void createStation(station_type type, int x, int y) {
		Station station = Station.newStation(type, "first", this);
		station.setPosition(x, y);
		stage.addActor(station);
		station.setZIndex(3);
		addNode(station);
	}

	public void selectStation(Station station) {
		if (selectedStation == null) {
			selectedStation = station;
		} else {
			/*final Dialog dialog = new Dialog("abcd", new Window.WindowStyle(
					new BitmapFont(), Color.RED, new TextureRegionDrawable(
							GameCenter.colors[3])));
			*/
			Skin skin = new Skin(Gdx.files.internal("images/uiskin.json"));
			final Dialog dialog = new Dialog("a", skin);
			dialog.setPosition(300, 300);
			//stage.addActor(dialog);
			Button button = new Button(new TextureRegionDrawable(
					GameCenter.passengers[1]), new TextureRegionDrawable(
					GameCenter.passengers[2]));
			button.setPosition(19, 19);
			dialog.addActor(button);
			button.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					dialog.remove();
				}
			});
			
			
			/*Drawable drawable = new TextureRegionDrawable(GameCenter.colors[1]);
			SelectBoxStyle style = new SelectBoxStyle(new BitmapFont(),
					Color.WHITE, drawable, new ScrollPaneStyle(drawable,
							drawable, drawable, drawable, drawable),
					new ListStyle(new BitmapFont(), Color.BLUE, Color.WHITE,
							drawable));*/
			TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/uiskin.atlas"));
			final SelectBox box = new SelectBox(new String[] { "aa", "bb" },
					skin);
			box.setPosition(300, 300);
			box.setBounds(300, 300, 100, 100);
			
			stage.addActor(dialog);
			// //////////////////////////////////////////////////////
			connect(selectedStation, station, Line.getOrNewLine(line_type.red));
			
			selectedStation.unselect();
			station.unselect();
		}
	}
	
	public void unselectStation(Station station) {
		selectedStation = null;
	}

	public void connect(Station s1, Station s2, Line line) {
		LinePart linePart = new LinePart(line, "a", this);
		linePart.setConnectLine(s1, s2);
		addEdge(linePart, s1, s2, line);
		stage.addActor(linePart);
		linePart.setZIndex(0);
	}
	
	public void dispose(){
		stage.dispose();
		lines.clear();
	}
}
