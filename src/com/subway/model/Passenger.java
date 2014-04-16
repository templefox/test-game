package com.subway.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.traverse.ClosestFirstIterator;

import android.view.View;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.subway.GameScreen;
import com.subway.LogicCore;
import com.subway.model.Station.Shape_type;
import com.subway.model.Viehcle.ViehcleData;

public abstract class Passenger implements Observer {
	public Image image;
	protected Station currentStation;
	protected LogicCore logicCore;
	private LinePart nextLinePart;
	private Station startStation;
	private Station destinationStation;
	private boolean isGraphChanged = false;
	private Shape_type type;
	private List<LinePart> routine = new LinkedList<LinePart>();
	private Iterator<LinePart> routineIterator;

	public Passenger(TextureRegion textureRegion, Station station,
			Shape_type type, LogicCore core) {
		image = new Image(textureRegion);
		currentStation = station;
		startStation = station;
		this.logicCore = core;
		this.type = type;
		image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
	}

	/**
	 * 根据当前位置，遍历图，找到符合的结点，返回路径列表
	 * 
	 * @return null若目标不可达到
	 */
	public List<LinePart> findDestination() {
		ShortestPath<Station, LinePart> shortestPath = new ShortestPath<Station, LinePart>(
				logicCore, currentStation, type);
		destinationStation = shortestPath.getEndStation();
		return shortestPath.getPathEdgeList();
	}

	/**
	 * 每当所在站有车辆经过时触发，观察者模式 <br>
	 * 1.若没有路径或者图改变时，寻找最短路径,调用findDestination() <br>
	 * 2.
	 */
	@Override
	public void update(Observable observable, Object data) {
		Viehcle viehcle = (Viehcle) observable;
		ViehcleData viehcleData = (ViehcleData) data;
		if (viehcle.contains(this)) {
			// 在车上
			// 1.达到目标车站
			if (viehcleData.current == destinationStation) {
				dealReachDestination(viehcle);
				viehcleData.numToLoad++;
			} else if (viehcleData.linePart != nextLinePart) {
				// 2.换
				dealUnloadingPassenger(viehcle, viehcleData.current);
				this.currentStation = viehcleData.current;
				viehcleData.numToLoad++;
			}

		} else if (true) {
			// 在站上
			if (routine.isEmpty() || isGraphChanged) {
				List<LinePart> path = findDestination();
				isGraphChanged = false;
				if (path == null)
					return;// 若不可能达到
				else {
					routine.clear();
					routine.addAll(path);// 保存路径
					routineIterator = routine.iterator();
				}
			}
			// GameScreen.label.setText(String.valueOf(routine));
			// 检查来车是否符合路径

			if (nextLinePart != null || routineIterator.hasNext()) {
				if (nextLinePart == null) {
					nextLinePart = routineIterator.next();
				}
				if (nextLinePart == viehcleData.linePart) {
					// GameScreen.label.setText("Try to up:"+String.valueOf(nextLinePart));
					// synchronized (viehcle) {
					dealLoading(currentStation, viehcle);
					viehcleData.numToLoad++;
					// }
					if (routineIterator.hasNext()) {
						nextLinePart = routineIterator.next();
					} else {
						nextLinePart = null;
					}
				} else {
					// GameScreen.label.setText("inverse direction");
				}
			} else {
				// GameScreen.label.setText("???");
			}
		}
	}

	private void dealReachDestination(final Viehcle viehcle) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				synchronized (viehcle) {
					try {
						Thread.sleep(498);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					viehcle.unloadPassenger(Passenger.this);
					try {
						Thread.sleep(498);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void dealUnloadingPassenger(final Viehcle viehcle,
			final Station current) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				synchronized (viehcle) {
					try {
						Thread.sleep(498);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					viehcle.unloadPassenger(Passenger.this);
					current.loadPassenger(Passenger.this);
					try {
						Thread.sleep(498);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	private void dealLoading(Station currentStation2, final Viehcle viehcle) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				synchronized (viehcle) {
					try {
						Thread.sleep(498);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					currentStation.byebyePassenger(Passenger.this);
					currentStation = null;
					viehcle.loadPassengers(Passenger.this);
					try {
						Thread.sleep(498);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}).start();
	}

	public boolean isWaitingViehcle(ViehcleData viehcleData) {
		return false;
	}

	public void notifyGraphChanged() {
		isGraphChanged = true;
	}
}
