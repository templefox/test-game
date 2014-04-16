package com.subway.model;

import java.net.ConnectException;

import org.jgrapht.graph.DefaultWeightedEdge;

import android.graphics.Point;
import android.graphics.PointF;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.subway.GameCenter;
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
		}else {
			throw new CannotConnectException("Cannot connect "+s1.image.getName()+" "+s2.image.getName());
		}

	}

	private boolean validation() {
		if(line.isCycle())
			return false;
		if (line.head == null && line.tail == null) {
			return true;
		} else if (((line.head == s1 && line.tail == s2) || (line.head == s2 && line.tail == s1))
				&& line.getLineParts().size() > 1) {
			line.setCycle(true);
			return true;
		}else if ((line.containStation(s1)&&(s1==line.head||s1==line.tail)&&!line.containStation(s2))
				||(line.containStation(s2)&&(s2==line.head||s2==line.tail)&&!line.containStation(s1)))
			return true;
		 else {
			return false;
		}
	}

	private void drawConnectLine(Station s1, Station s2) {
		from = new PointF(s1.image.getX() + s1.image.getOriginX(), s1.image.getY()
				+ s1.image.getOriginY());
		to = new PointF(s2.image.getX() + s2.image.getOriginX(), s2.image.getY()
				+ s2.image.getOriginY());
		image.setPosition(from.x, from.y);
		float theta = MathUtils.atan2(to.y - from.y, to.x - from.x);
		float length = (s2.image.getX() - s1.image.getX()) / MathUtils.cos(theta);
		image.setRotation(theta * 180 / MathUtils.PI);
		image.setScale(length, 4);
		this.length = length;
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

}
