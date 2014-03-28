package com.subway.system;

public class MetroEdge {
	private MetroNode nodeX;
	private MetroNode nodeY;
	private MetroLine parentLine;
	private int weight = -1;
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
		parentLine.add(this);
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
}
