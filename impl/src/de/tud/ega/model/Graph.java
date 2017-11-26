package de.tud.ega.model;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class representing a graph
 * 
 */
public class Graph {


	private HashSet<Vertex> vertices;
	private ArrayList<Edge> edges;
	
	public Graph(HashSet<Vertex> vertices, ArrayList<Edge> edges) {
		this.vertices = vertices;
		this.edges = edges;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public HashSet<Vertex> getVertices() {
		return vertices;
	}

	// TODO: further functionalities
}
