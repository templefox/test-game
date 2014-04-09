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
	public LinePart(Line line,String name,LogicCore core) {
		super();
		image = new Image(line.getRegion());
		this.name = name;
		logicCore = core;
		this.line = line;
	}
	
	public void drawConnectLine(Station s1,Station s2){
		this.s1 = s1;
		this.s2 = s2;
		from = new PointF(s1.getX()+s1.getOriginX(), s1.getY()+s1.getOriginY());
		to = new PointF(s2.getX()+s2.getOriginX(), s2.getY()+s2.getOriginY());
		image.setPosition(from.x, from.y);
		float theta = MathUtils.atan2(to.y-from.y, to.x-from.x);
		float length = (s2.getX()-s1.getX())/MathUtils.cos(theta);
		image.setRotation(theta*180/MathUtils.PI);
		image.setScale(length, 4);
		this.length = length;
	}
	
	public float[] getStartPosition(){
		return new float[]{image.getX(),image.getY()};
	}
	
	public float[] getPositiveD(){
		return new float[]{to.x-from.x,to.y-from.y};
	}
	
	public float getLength(){
		return length;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
		assert(s1!=null&&s2!=null):"LinePart need stations";
		line.addStation(s1,s2);
		line.addLinePart(this);
		//TODO
	}
	
	public void swapS1S2(){
		Station t = s1;
		s1 = s2;
		s2 = t;
		
		PointF tF = from;
		from = to;
		to = tF;
	}

	@Override
	public String toString() {
		return s1.getName()+"-->"+s2.getName();
	}
	
	
}
