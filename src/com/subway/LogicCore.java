package com.subway;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.experimental.permutation.CollectionPermutationIter;
import org.jgrapht.graph.WeightedMultigraph;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.subway.gamemode.GameMode;
import com.subway.model.BlinkAction;
import com.subway.model.CannotConnectException;
import com.subway.model.Line;
import com.subway.model.LinePart;
import com.subway.model.Passenger;
import com.subway.model.Station;
import com.subway.model.Line.line_type;
import com.subway.model.Station.Shape_type;
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
	private Line selectedLine;
	private GameMode gameMode;
	private Set<Shape_type> shape_types = new HashSet<Station.Shape_type>();

	public void setSelectedLine(Line selectedLine) {
		this.selectedLine = selectedLine;
	}

	public LogicCore(GameCenter gameCenter, Stage stage, GameMode gameMode) {
		super(LinePart.class);

		this.gameCenter = gameCenter;
		this.stage = stage;
		this.gameMode = gameMode;
		gameMode.setProperties(this);
	}

	public boolean createStation(Shape_type type, int x, int y) {
		Set<Station> set = vertexSet();
		for (Station station : set) {
			if ((x >= (station.image.getX() - station.image.getWidth()))
					&& (x <= (station.image.getX() + station.image.getWidth()))
					&& (y >= (station.image.getY() - station.image.getHeight()))
					&& (y <= (station.image.getY() + station.image.getHeight()))) {
				return false;
			}
		}

		shape_types.add(type);
		Station station = Station.newStation(type, "(" + x + "," + y + ")",
				this);

		station.image.setPosition(x, y);
		stage.addActor(station.image);
		station.image.setZIndex(3);
		addVertex(station);
		return true;
	}

	private float updateCount = 0;
	public void update(float delta) {
		// 每次更新，每个车站有一定概率生成一个乘客
		// 有一定概率生成一个新车站
		gameMode.update(delta, this);
		
		if (updateCount<0.1) {
			updateCount+=delta;
			return;
		}
		
		for (Station station : vertexSet()) {
			Set<Shape_type> set = new HashSet<Station.Shape_type>(shape_types);
			set.remove(station.getType());
			station.generatePassenger(set.toArray(new Shape_type[] {}));
			station.update(delta+updateCount);
		}
		
		updateCount=0;
	}

	public void selectStation(final Station station) {
		if (selectedStation == null) {
			// 第一次选择
			selectedStation = station;
		} else {
			// 选择了两个
			// 验证，确认提示,显示提示
			final LinePart part;
			try {
				part = PseudoConnect(selectedStation, station, selectedLine);
				/*
				 * final Dialog dialog = new Dialog("abcd", new
				 * Window.WindowStyle( new BitmapFont(), Color.RED, new
				 * TextureRegionDrawable( GameCenter.colors[3])));
				 */
				final Dialog dialog = new Dialog("a", GameScreen.skin);
				dialog.setPosition(300, 300);

				TextButton button = new TextButton(" O.K. ",
						GameScreen.skin);
				button.setPosition(19, 19);
				dialog.addActor(button);
				button.addListener(new ClickListener() {
					private Station selected = selectedStation;

					@Override
					public void clicked(InputEvent event, float x, float y) {
						// 点击确认
						Color color = part.image.getColor();
						color.a = 1f;
						if (part.getLine().isCycle()) {
							Line.addCycleNum();
						}
						part.image.setColor(color);
						part.image.removeAction(part.image.getActions().peek());
						part.setLine(part.getLine());
						addEdge(selected, station, part);
						setEdgeWeight(part, part.getLength());
						for (Station station : vertexSet()) {
							for (Passenger passenger : station.getPassengers()) {
								passenger.notifyGraphChanged();
							}
						}
						selectedLine.addNewViehcle();
						dialog.remove();
					}
				});

				TextButton textButton = new TextButton("Cancel",
						GameScreen.skin);
				textButton.setPosition(80, 19);
				dialog.addActor(textButton);
				textButton.addListener(new ClickListener() {

					@Override
					public void clicked(InputEvent event, float x, float y) {
						// 取消
						if (part.getLine().isCycle()) {
							part.getLine().setCycle(false);
						}
						part.remove();
						dialog.remove();
					}
				});

				stage.addActor(dialog);
			} catch (CannotConnectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				selectedStation.unselect();
				station.unselect();
			}

		}
	}

	public void unselectStation(Station station) {
		selectedStation = null;
	}

	/**
	 * 验证，若通过则伪连接，否则异常
	 * 
	 * @throws 抛出异常
	 */
	private LinePart PseudoConnect(Station s1, Station s2, Line line)
			throws CannotConnectException {
		if (s1.image.getX() > s2.image.getX()
				|| (s1.image.getX() == s2.image.getX() && s1.image.getY() > s2.image
						.getY())) {
			Station temp = s1;
			s1 = s2;
			s2 = temp;
		}

		LinePart linePart = new LinePart(line, "a", this, s1, s2);
		linePart.image.addAction(BlinkAction.pool.obtain());
		stage.addActor(linePart.image);
		linePart.image.setZIndex(0);
		return linePart;
	}

	public void dispose() {
		selectedStation = null;
		selectedLine = null;
		shape_types.clear();

		stage.dispose();
		Line.dispose();
	}

	public void addViecleToStage(Viehcle viecle) {
		stage.addActor(viecle.image);
		viecle.image.setZIndex(10);
	}

	public void drawPassagerToStation(Passenger passenger, Station station) {
		stage.addActor(passenger.image);
	}

	public void setLose() {
		int score = gameMode.getScore();
		SharedPreferences sharedPreferences = gameCenter.getActivity().getSharedPreferences("score", Context.MODE_PRIVATE);
		
		int max = sharedPreferences.getInt("max", 0);
		if (max<score) {			
			Editor editor = sharedPreferences.edit();
			editor.putInt("max", score);
			editor.commit();
		}
		Screen screen = gameCenter.getScreen();
		if (screen instanceof GameScreen) {
			GameScreen gameScreen = (GameScreen)screen;
			gameScreen.setState(GameScreen.Game_state.PAUSE);
			GameScreen.messageBoard.appendText("=====YOU====== Final Score:"+score, 1200);
			GameScreen.messageBoard.appendText("=====LOSE====== Max Score:"+max, 1200);
		}
	}

	public GameMode getGameMode() {
		return gameMode;
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