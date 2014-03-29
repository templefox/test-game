package com.subway.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class MetroNode extends Image{
	protected LinkedList<MetroEdge> edges = new LinkedList<MetroEdge>();
	protected MetroSystem metroSystem;
	public LinkedList<MetroEdge> getEdges() {
		return edges;
	}
	
	public MetroNode(TextureRegion region,MetroSystem system){
		super(region);
		this.metroSystem = system;
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
	
	public void addEdge(MetroEdge edge,MetroNode y,MetroLine line){
		edge.setNodeX(y);
		edge.setNodeY(this);
		edge.setParentLine(line);
		
		edges.add(edge);
		y.getEdges().add(edge);
	}
}
