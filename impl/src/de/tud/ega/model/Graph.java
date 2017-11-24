package de.tud.ega.model;

import java.util.ArrayList;

/**
 * Class representing a graph
 * 
 */
public class Graph {


	private ArrayList<Point> points;
	private ArrayList<Vertex> vertices;
	
	public Graph(ArrayList<Point> points, ArrayList<Vertex> vertices) {
		this.points = points;
		this.vertices = vertices;
	}

	public ArrayList<Vertex> getVertices() {
		return vertices;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	// TODO: further functionalities
}
