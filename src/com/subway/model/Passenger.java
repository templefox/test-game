package com.subway.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.traverse.ClosestFirstIterator;

import android.view.View;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
		image.setColor(Color.GRAY);
		image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
		
		image.addAction(Actions.sequence(Actions.color(Color.RED, 80), new RunnableAction(){

			@Override
			public void run() {
				logicCore.getGameMode().onPassagerRed(logicCore);
			}}));
		
		image.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Info info = Info.instance(Passenger.this);
				if (!info.isAlive()) {
					info.start();
				}
			}});
	}
	
	static class Info extends Thread{
		Passenger passenger;
		static Info info = new Info();
		private Info() {
		}
		public static Info instance(Passenger passenger){
			 info.passenger = passenger;
			 return info;
		}
		@Override
		public void run() {
			while (passenger!=null){
				GameScreen.label.setText(String.valueOf(passenger.nextLinePart));
			}
		}
	}

	/**
	 * ���ݵ�ǰλ�ã�����ͼ���ҵ����ϵĽ�㣬����·���б�
	 * 
	 * @return null��Ŀ�겻�ɴﵽ
	 */
	public List<LinePart> findDestination() {
		ShortestPath<Station, LinePart> shortestPath = new ShortestPath<Station, LinePart>(
				logicCore, currentStation, type);
		destinationStation = shortestPath.getEndStation();
		return shortestPath.getPathEdgeList();
	}

	/**
	 * ÿ������վ�г�������ʱ�������۲���ģʽ <br>
	 * 1.��û��·������ͼ�ı�ʱ��Ѱ�����·��,����findDestination() <br>
	 * 2.
	 */
	@Override
	public void update(Observable observable, Object data) {
		Viehcle viehcle = (Viehcle) observable;
		ViehcleData viehcleData = (ViehcleData) data;
		if (viehcle.contains(this)) {
			// �ڳ���
			// 1.�ﵽĿ�공վ
			if (viehcleData.current == destinationStation) {
				dealReachDestination(viehcle);
				viehcleData.numToLoad++;
			} else if (!viehcleData.linePart.equals(nextLinePart)) {
				// 2.��
				dealUnloadingPassenger(viehcle, viehcleData.current);
				this.currentStation = viehcleData.current;
				viehcleData.numToLoad++;
			}else {
				if (routineIterator.hasNext()) {
					nextLinePart = routineIterator.next();					
				}
			}

		} else if (true) {
			// ��վ��
			if (routine.isEmpty() || isGraphChanged) {
				List<LinePart> path = findDestination();
				isGraphChanged = false;
				if (path == null)
					return;// �������ܴﵽ
				else {
					routine.clear();
					routine.addAll(path);// ����·��
					routineIterator = routine.iterator();
				}
			}
			// GameScreen.label.setText(String.valueOf(routine));
			//��鳵�Ƿ��п�
			if (!viehcle.hasEmptyPosition()) {
				return;
			}
			
			// ��������Ƿ����·��

			if (nextLinePart != null || routineIterator.hasNext()) {
				if (nextLinePart == null) {
					nextLinePart = routineIterator.next();
				}
				if (nextLinePart.equals(viehcleData.linePart)) {
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
						e.printStackTrace();
					}
					viehcle.unloadPassenger(Passenger.this);
					logicCore.getGameMode().onPassagerToDestination();
					try {
						Thread.sleep(498);
					} catch (InterruptedException e) {
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
						e.printStackTrace();
					}
					viehcle.unloadPassenger(Passenger.this);
					current.loadPassenger(Passenger.this);
					try {
						Thread.sleep(498);
					} catch (InterruptedException e) {
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
						e.printStackTrace();
					}
					currentStation.byebyePassenger(Passenger.this);
					currentStation = null;
					viehcle.loadPassengers(Passenger.this);
					try {
						Thread.sleep(498);
					} catch (InterruptedException e) {
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
