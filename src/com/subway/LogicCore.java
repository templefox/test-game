package com.subway;

import org.jgrapht.experimental.permutation.CollectionPermutationIter;
import org.jgrapht.graph.WeightedMultigraph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.subway.model.BlinkAction;
import com.subway.model.Line;
import com.subway.model.LinePart;
import com.subway.model.Station;
import com.subway.model.Line.line_type;
import com.subway.model.Station.station_type;
import com.subway.model.Viehcle;

public class LogicCore extends WeightedMultigraph<Station, LinePart> implements
		Disposable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1002014587871239396L;
	private GameCenter gameCenter;
	private Stage stage;
	private Station selectedStation;

	public LogicCore(GameCenter gameCenter, Stage stage) {
		super(LinePart.class);

		this.gameCenter = gameCenter;
		this.stage = stage;
	}

	public void createStation(station_type type, int x, int y) {
		Station station = Station.newStation(type, "("+x+","+y+")", this);
		station.setPosition(x, y);
		stage.addActor(station);
		station.setZIndex(3);
		addVertex(station);
	}

	public void selectStation(final Station station) {
		if (selectedStation == null) {
			// 第一次选择
			selectedStation = station;
		} else {
			// 选择了两个
			// 确认提示,显示提示
			final LinePart part = PseudoConnect(selectedStation, station,
					Line.getOrNewLine(line_type.red, LogicCore.this));
			/*
			 * final Dialog dialog = new Dialog("abcd", new Window.WindowStyle(
			 * new BitmapFont(), Color.RED, new TextureRegionDrawable(
			 * GameCenter.colors[3])));
			 */
			Skin skin = new Skin(Gdx.files.internal("images/uiskin.json"));
			final Dialog dialog = new Dialog("a", skin);
			dialog.setPosition(300, 300);

			Button button = new Button(new TextureRegionDrawable(
					GameCenter.passengers[1]), new TextureRegionDrawable(
					GameCenter.passengers[2]));
			button.setPosition(19, 19);
			dialog.addActor(button);
			button.addListener(new ClickListener() {
				private Station selected = selectedStation;

				@Override
				public void clicked(InputEvent event, float x, float y) {
					// 点击确认
					Color color = part.image.getColor();
					color.a = 1f;
					part.image.setColor(color);
					part.image.removeAction(part.image.getActions().peek());
					part.setLine(part.getLine());
					addEdge(selected, station, part);
					setEdgeWeight(part, part.getLength());
					Line.getOrNewLine(line_type.red, LogicCore.this).addNewViehcle();
					dialog.remove();
				}
			});
			
			TextButton textButton = new TextButton("cancel", skin);
			textButton.setPosition(80, 19);
			dialog.addActor(textButton);
			textButton.addListener(new ClickListener(){

				@Override
				public void clicked(InputEvent event, float x, float y) {
					//取消
					part.image.remove();
					dialog.remove();
				}});
			
			stage.addActor(dialog);
			
			selectedStation.unselect();
			station.unselect();

			
		}
	}

	public void unselectStation(Station station) {
		selectedStation = null;
	}

	private LinePart PseudoConnect(Station s1, Station s2, Line line) {
		LinePart linePart = new LinePart(line, "a", this);
		linePart.drawConnectLine(s1, s2);
		linePart.image.addAction(BlinkAction.pool.obtain());
		stage.addActor(linePart.image);
		linePart.image.setZIndex(0);
		return linePart;
	}

	public void dispose() {
		stage.dispose();
		Line.dispose();
	}

	public void addViecleToStage(Viehcle viecle) {
		stage.addActor(viecle);
		viecle.setZIndex(10);
	}
}

/*
 * Drawable drawable = new TextureRegionDrawable(GameCenter.colors[1]);
 * SelectBoxStyle style = new SelectBoxStyle(new BitmapFont(), Color.WHITE,
 * drawable, new ScrollPaneStyle(drawable, drawable, drawable, drawable,
 * drawable), new ListStyle(new BitmapFont(), Color.BLUE, Color.WHITE,
 * drawable)); TextureAtlas atlas = new
 * TextureAtlas(Gdx.files.internal("images/uiskin.atlas")); final SelectBox box
 * = new SelectBox(new String[] { "aa", "bb" }, skin); box.setPosition(300,
 * 300); box.setBounds(300, 300, 100, 100);
 */
// //////////////////////////////////////////////////////