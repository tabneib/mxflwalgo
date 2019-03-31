package de.tud.ega.model;

import java.util.ArrayList;

/**
 * Class representing a graph
 * 
 */
public class Graph {

	private ArrayList<MVertex> vertices;
	private ArrayList<Arc> arcs;

	public Graph(ArrayList<MVertex> vertices, ArrayList<Arc> arcs) {
		this.vertices = vertices;
		this.arcs = arcs;
	}

	public ArrayList<Arc> getArcs() {
		return arcs;
	}

	public ArrayList<MVertex> getVertices() {
		return vertices;
	}

	public String toString() {
		String str = "MGraph[" + vertices.size() + ", " + arcs.size() + "]\n";
		for (Arc a : arcs)
			str += a.toString() + "\t";

		return str;
	}
}
