package com.subway.model;

import java.util.HashSet;
import java.util.Set;
import org.jgrapht.graph.DefaultWeightedEdge;
import android.graphics.PointF;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.subway.GameScreen;
import com.subway.LogicCore;

public class LinePart extends DefaultWeightedEdge {
	private String name;
	private LogicCore logicCore;
	private Line line;
	public Station s1;
	public Station s2;
	private float length;
	public PointF from;
	public PointF to;
	public Image image;
	private float sinTheta;
	private float cosTheta;
	public static final float THICK = 8;

	public LinePart(Line line, String name, LogicCore core, Station s1,
			Station s2) throws CannotConnectException {
		super();
		image = new Image(line.getRegion());
		this.name = name;
		logicCore = core;
		this.line = line;
		this.s1 = s1;
		this.s2 = s2;
		if (validation()) {
			drawConnectLine(s1, s2);
		} else {
			throw new CannotConnectException("Cannot connect "
					+ s1.image.getName() + " " + s2.image.getName());
		}

	}

	private boolean validation() {
		if (line.isCycle())
			return false;
		if (line.head == null && line.tail == null) {
			return true;
		} else if (((line.head == s1 && line.tail == s2) || (line.head == s2 && line.tail == s1))
				&& line.getLineParts().size() > 1) {
			if (Line.getCycleNum() >= logicCore.getGameMode().getCycleLimit()) {
				return false;
			} else {
				line.setCycle(true);
				return true;
			}
		} else if ((line.containStation(s1)
				&& (s1 == line.head || s1 == line.tail) && !line
					.containStation(s2))
				|| (line.containStation(s2)
						&& (s2 == line.head || s2 == line.tail) && !line
							.containStation(s1)))
			return true;
		else {
			return false;
		}
	}

	private void drawConnectLine(Station s1, Station s2) {
		Set<LinePart> edges = logicCore.edgesOf(s1);

		HashSet<LinePart> sameEdges = new HashSet<LinePart>();
		for (LinePart linePart : edges) {
			if (linePart.equals(this)) {
				sameEdges.add(linePart);
			}
		}
		
		float thick = THICK / (sameEdges.size() + 1);

		from = new PointF(s1.image.getX() + s1.image.getOriginX(),
				s1.image.getY() + s1.image.getOriginY());
		to = new PointF(s2.image.getX() + s2.image.getOriginX(),
				s2.image.getY() + s2.image.getOriginY());
		float theta = MathUtils.atan2(to.y - from.y, to.x - from.x);
		//让三角函数值保持在0~PI/4,大大提高精确度
		if (Math.abs(to.x - from.x) > Math.abs(to.y - from.y)) {
			cosTheta = MathUtils.cos(theta);
			length = (s2.image.getX() - s1.image.getX()) / cosTheta;
			sinTheta = (to.y - from.y) / length;
		} else {
			sinTheta = MathUtils.sin(theta);
			length = (s2.image.getY() - s1.image.getY())
						/ sinTheta;
			cosTheta = (to.x - from.x) / length;
		}


		//考虑重复线路时的情况
		int i = 1;
		for (LinePart linePart : sameEdges) {
			adjustPosition(linePart.image, i, thick);
			i++;
		}
		adjustPosition(image, i, thick);
		image.setScaleX(length);
		image.setRotation(theta * 180 / MathUtils.PI);
	}
	
	private void adjustPosition(Image image,int i,float thick2){
		image.setPosition(from.x - (THICK / 2 - thick2 * i) * sinTheta, from.y
				+ (THICK / 2 - thick2 * i) * cosTheta);
		image.setScaleY(thick2);
	}

	public float[] getStartPosition() {
		return new float[] { image.getX(), image.getY() };
	}

	public float[] getPositiveD() {
		return new float[] { to.x - from.x, to.y - from.y };
	}

	public float getLength() {
		return length;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
		assert (s1 != null && s2 != null) : "LinePart need stations";
		line.addStation(s1, s2);
		line.addLinePart(this);
	}

	public void swapS1S2() {
		Station t = s1;
		s1 = s2;
		s2 = t;

		PointF tF = from;
		from = to;
		to = tF;
	}

	@Override
	public String toString() {
		return s1.image.getName() + "-->" + s2.image.getName();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof LinePart) {
			LinePart oo = (LinePart) o;
			if ((oo.s1 == s1 && oo.s2 == s2) || (oo.s1 == s2 && oo.s2 == s1)) {
				return true;
			}

		}

		return false;
	}

	public void remove() {
		Set<LinePart> edges = logicCore.edgesOf(s1);

		HashSet<LinePart> sameEdges = new HashSet<LinePart>();
		for (LinePart linePart : edges) {
			if (linePart.equals(this)) {
				sameEdges.add(linePart);
			}
		}

		if (!sameEdges.isEmpty()) {
			float thick = THICK / sameEdges.size();
			int i = 1;
			for (LinePart linePart : sameEdges) {
				adjustPosition(linePart.image, i, thick);
				i++;
			}
		}
		
		image.remove();
	}

}
