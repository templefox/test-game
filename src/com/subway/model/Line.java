package com.subway.model;

import java.util.HashMap;
import java.util.LinkedList;

import android.nfc.NfcAdapter.CreateBeamUrisCallback;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.subway.GameCenter;
import com.subway.system.MetroLine;

public class Line extends MetroLine{
	private TextureRegion region;
	private static HashMap<line_type,Line> lines = new HashMap<Line.line_type, Line>(7);
	public enum line_type {red,orange,yellow,green,blue,indigo,purple}
	private Line(TextureRegion region ,String name) {
		super(name);
		this.region = region;
	}
	
	public static Line getOrNewLine(line_type type){
		Line r = lines.get(type);
		if(r == null){
			switch (type) {
			case red:
				r = new Line(GameCenter.colors[0],"RED");
				break;
			case orange:
				r = new Line(GameCenter.colors[1],"ORANGE");
				break;
			default:
				break;
			}
			lines.put(type, r);
		}
		return r;
	}
	public static void dispose(){
		lines.clear();
	}
	
	public TextureRegion getRegion() {
		return region;
	}
	
}
