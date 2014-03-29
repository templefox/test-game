package com.subway.system;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class MetroLine {
	protected LinkedList<MetroEdge> edges = new LinkedList<MetroEdge>();
	protected String name = "";
	protected MetroNode nodeX;
	protected MetroNode nodeY;
	
	public MetroLine(String name) {
		this.name = name;
	}
	public void addEdge(MetroEdge edge){
		if(nodeX == null && nodeY == null){
			nodeX = edge.getNodeX();
			nodeY = edge.getNodeY();
		}else {
			if(edges.contains(edge))
				throw new IllegalArgumentException("Should not add one edge twice");
			if(edge.contain(nodeX)){
				nodeX = edge.getAnotherNode(nodeX);
			}else if(edge.contain(nodeY)){
				nodeY = edge.getAnotherNode(nodeY);
			}else {
				throw new IllegalStateException();
			}
		}
		edges.add(edge);
	}
	public void remove(MetroEdge edge){
		edges.remove(edge);
	}
	public String getName() {
		return name;
	}
}
