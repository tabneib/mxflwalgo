package de.tud.ega.model;

import java.util.ArrayList;

/**
 * Class representing a graph
 * 
 */
public class MGraph {

	
	private ArrayList<MVertex> vertices;
	private ArrayList<Arc> arcs;
	
	public MGraph(ArrayList<MVertex> vertices, ArrayList<Arc> arcs) {
		this.vertices = vertices;
		this.arcs = arcs;
	}

	public ArrayList<Arc> getArcs() {
		return arcs;
	}

	public ArrayList<MVertex> getVertices() {
		return vertices;
	}

	// TODO: further functionalities: Lists of arcs & nodes that should be highlighted for the purpose of
	// describing intermediate steps of the algorithms
	
}
