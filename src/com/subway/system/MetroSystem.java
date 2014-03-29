package com.subway.system;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class MetroSystem {
	protected HashSet<MetroNode> nodes = new HashSet<MetroNode>();
	protected HashSet<MetroLine> lines = new HashSet<MetroLine>();

	public int adjacent(MetroNode x, MetroNode y) {
		return x.adjacent(y);
	}

	public MetroNode[] neighbors(MetroNode x) {
		return x.neighbors();
	}

	public void addEdge(MetroEdge edge,MetroNode x, MetroNode y, MetroLine line) {
		x.addEdge(edge,y, line);
	}

	public void addNode(MetroNode... node) {
		for (MetroNode metroNode : node) {
			nodes.add(metroNode);
		}
	}

	public void addLine(MetroLine... line) {
		for (MetroLine metroLine : line) {
			lines.add(metroLine);
		}
	}

	public static void main(String[] args) {
		
		/*MetroSystem system = new MetroSystem();
		MetroNode a = new MetroNode("A");
		MetroNode b = new MetroNode("B");
		MetroNode c = new MetroNode("C");
		MetroNode d = new MetroNode("D");
		MetroLine no1 = new MetroLine("#1");
		MetroLine no2 = new MetroLine("#2");

		system.addLine(no1,no2);
		system.addNode(a,b,c,d);
		
		system.addEdge(a, b, no1);
		system.addEdge(b, d, no1);
		system.addEdge(d, c, no1);
		
		system.addEdge(b, c, no2);
		system.addEdge(c, a, no2);
		system.addEdge(d, a, no2);
		
		System.out.println("");*/
	}
}
