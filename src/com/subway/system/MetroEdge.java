package com.subway.system;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class MetroEdge extends Image{
	private MetroNode nodeX;
	private MetroNode nodeY;
	private MetroLine parentLine;
	private int weight = -1;
	
	public MetroEdge(TextureRegion region){
		super(region);
	}
	
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public MetroNode getNodeY() {
		return nodeY;
	}
	public void setNodeY(MetroNode y) {
		this.nodeY = y;
	}
	public MetroNode getNodeX() {
		return nodeX;
	}
	public void setNodeX(MetroNode x) {
		this.nodeX = x;
	}
	public MetroLine getParentLine() {
		return parentLine;
	}
	public void setParentLine(MetroLine parentLine) {
		this.parentLine = parentLine;
		parentLine.addEdge(this);
	}
	public boolean contain(MetroNode node){
		return node == nodeX||node == nodeY;
	}
	
	public MetroNode getAnotherNode(MetroNode tis){
		if(nodeX == tis)
			return nodeY;
		else {
			return nodeX;
		}
	}

	@Override
	public boolean equals(Object o) {
		MetroEdge edge = (MetroEdge)o;
		return edge.contain(nodeX)&&edge.contain(nodeY);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return parentLine.hashCode();
	}
	
	
}
