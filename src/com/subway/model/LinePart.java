package com.subway.model;

import java.net.ConnectException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.subway.LogicCore;
import com.subway.system.MetroEdge;

public class LinePart extends MetroEdge {
	private String name;
	private LogicCore logicCore;
	private Line line;
	private Station s1;
	private Station s2;
	public LinePart(Line line,String name,LogicCore core) {
		super(line.getRegion());
		this.name = name;
		this.line = line;
		logicCore = core;
	}
	
	public void setConnectLine(Station s1,Station s2){
		this.s1 = s1;
		this.s2 = s2;
		setPosition(s1.getX()+s1.getWidth()/2, s1.getY()+s1.getHeight()/2);
		float theta = MathUtils.atan2(s2.getY()-s1.getY(), s2.getX()-s1.getX());
		float length = (s2.getX()-s1.getX())/MathUtils.cos(theta);
		setRotation(theta*180/MathUtils.PI);
		setScale(length, 4);
	}
}
