package com.subway.system;

import java.util.ArrayList;
import java.util.LinkedList;

public class MetroLine {
	private LinkedList<MetroEdge> edges = new LinkedList<MetroEdge>();
	private String name = "";
	private MetroNode nodeX;
	private MetroNode nodeY;
	
	public MetroLine(String name) {
		this.name = name;
	}
	public void add(MetroEdge edge){
		if(nodeX == null && nodeY == null){
			nodeX = edge.getNodeX();
			nodeY = edge.getNodeY();
		}else {
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
