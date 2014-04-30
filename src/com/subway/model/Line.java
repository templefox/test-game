package com.subway.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.MaskFunctor;
import org.jgrapht.graph.UndirectedMaskSubgraph;

import android.R.integer;
import android.annotation.SuppressLint;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.subway.GameCenter;
import com.subway.GameScreen;
import com.subway.LogicCore;

public class Line implements MaskFunctor<Station, LinePart> {
	private TextureRegion region;
	private Set<Station> stations = new HashSet<Station>();
	private LinkedList<LinePart> lineParts = new LinkedList<LinePart>();
	private List<Viehcle> viehcles = new ArrayList<Viehcle>();
	private LogicCore logicCore;
	public Station head;
	public Station tail;
	private static int cycleNum = 0;
	private boolean isCycle = false;
	private UndirectedMaskSubgraph<Station, LinePart> subgraph;
	private static HashMap<line_type, Line> lines = new HashMap<Line.line_type, Line>(
			7);

	public enum line_type {
		red, orange, yellow, green, blue, indigo, purple
	}
	
	public Color color;
	
	

	@Override
	public String toString() {
		return "head:"+head+"("+lineParts.toString()+")"+"tail:"+tail;
	}

	private Line(TextureRegion region, String name, LogicCore logicCore,Color color) {
		this.region = region;
		this.logicCore = logicCore;
		this.color = color;
		subgraph = new UndirectedMaskSubgraph<Station, LinePart>(logicCore,
				this);
	}

	public static Line getOrNewLine(line_type type, LogicCore logicCore) {
		Line r = lines.get(type);
		if (r == null) {
			if (logicCore == null) {
				throw new IllegalStateException();
			}
			switch (type) {
			case red:
				r = new Line(GameCenter.colors[0], "RED", logicCore,Color.RED);
				break;
			case orange:
				r = new Line(GameCenter.colors[1], "ORANGE", logicCore,Color.ORANGE);
				break;
			case yellow:
				r = new Line(GameCenter.colors[2], "YELLOW", logicCore,Color.YELLOW);
				break;
			case green:
				r = new Line(GameCenter.colors[3], "GREEN", logicCore,Color.GREEN);
				break;
			case blue:
				r = new Line(GameCenter.colors[4], "BLUE", logicCore,Color.BLUE);
				break;
			case indigo:
				r = new Line(GameCenter.colors[5], "INDIGO", logicCore,Color.CYAN);
				break;
			case purple:
				r = new Line(GameCenter.colors[6], "PURPLE", logicCore,Color.MAGENTA);
				break;
			default:
				break;
			}
			lines.put(type, r);
		}
		return r;
	}
	
	public static HashMap<line_type, Line> getOldLines(){
		return lines;
	}

	public static void dispose() {
		for (Line line : lines.values()) {
			line.stations.clear();
			line.viehcles.clear();
		}
		lines.clear();
	}

	public TextureRegion getRegion() {
		return region;
	}

	public void addNewViehcle() {
		if (viehcles.isEmpty())
			viehcles.add(new Viehcle(this,head));
	}

	public int getViecleNum() {
		return viehcles.size();
	}

	public LogicCore getLogicCore() {
		return logicCore;
	}

	@Override
	public boolean isEdgeMasked(LinePart arg0) {
		if (lineParts.contains(arg0)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isVertexMasked(Station arg0) {
		if (stations.add(arg0)) {
			return false;
		}
		return true;
	}

	public void addStation(Station s1, Station s2) {
		stations.add(s1);
		stations.add(s2);
	}

	public void addLinePart(LinePart linePart) {
		if(head == null&&tail == null){
			lineParts.add(linePart);
			head = linePart.s1;
			tail = linePart.s2;
		}
		else {
			if (tail == linePart.s1||tail == linePart.s2) {
				if(tail == linePart.s2)
					linePart.swapS1S2();
				tail = linePart.s2;
				lineParts.addLast(linePart);
			}else {
				if(head == linePart.s1)
					linePart.swapS1S2();
				head = linePart.s1;
				lineParts.addFirst(linePart);
			}
		}
	}

	public boolean containLinePart(LinePart part){
		return lineParts.contains(part);
	}
	public boolean containStation(Station station){
		return stations.contains(station);
	}
	
	public LinkedList<LinePart> getLineParts(){
		return lineParts;
	}

	@SuppressLint("NewApi")
	public LinePart nextEdge(LinePart nextLinePart, boolean inverse) {
		//正序，返回下一个edge，否则返回上一个edge,若没有（到底）判断是否环路，否则返回null，是这返回头或尾
		
		
		Iterator<LinePart> iterator = inverse?lineParts.descendingIterator():lineParts.iterator(); 
		for (; iterator.hasNext();) {
			LinePart part = iterator.next();
			if(part == nextLinePart){
				if (iterator.hasNext()) {
					return iterator.next();					
				}else {
					if (isCycle()&&!inverse) {
						return lineParts.getFirst();
					}else if(isCycle()&&inverse){
						return lineParts.getLast();
					}else {
						return null;
					}
				}
			}
		}
		//如果找不到则状态错误
		throw new IllegalStateException();
	}

	public boolean isCycle() {
		return isCycle;
	}

	public void setCycle(boolean isCycle) {
		this.isCycle = isCycle;
	}

	public static int getCycleNum() {
		return cycleNum;
	}

	public static void addCycleNum() {
		Line.cycleNum+=1;
	}
	
}
