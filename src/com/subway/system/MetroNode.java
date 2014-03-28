package com.subway.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class MetroNode {
	private String name;
	
	private LinkedList<MetroEdge> edges = new LinkedList<MetroEdge>();
	
	public LinkedList<MetroEdge> getEdges() {
		return edges;
	}

	public MetroNode(String name) {
		this.name = name;
	}
	
	public int adjacent(MetroNode y){
		for (MetroEdge edge : edges) {
			if(edge.getAnotherNode(this)==y)
				return edge.getWeight();
		}
		return -1;
	}
	
	public MetroNode[] neighbors(){
		MetroNode[] nodes = new MetroNode[edges.size()];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = edges.get(i).getAnotherNode(this);
		}
		return nodes;
	}
	
	public void addEdge(MetroNode y,MetroLine line){
		MetroEdge edge = new MetroEdge();
		edge.setNodeX(y);
		edge.setNodeY(this);
		edge.setParentLine(line);
		
		edges.add(edge);
		y.getEdges().add(edge);
	}

	public String getName() {
		return name;
	}

}
