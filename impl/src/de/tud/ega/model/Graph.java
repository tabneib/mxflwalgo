package de.tud.ega.model;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class representing a graph
 * 
 */
public class Graph {


	private ArrayList<Vertex> vertices;
	private ArrayList<Arc> arcs;
	
	public Graph(ArrayList<Vertex> vertices, ArrayList<Arc> arcs) {
		this.vertices = vertices;
		this.arcs = arcs;
	}

	public ArrayList<Arc> getArcs() {
		return arcs;
	}

	public ArrayList<Vertex> getVertices() {
		return vertices;
	}

	// TODO: further functionalities
}
