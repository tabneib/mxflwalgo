package de.tud.ega.model;

import java.util.ArrayList;

/**
 * Class representing an Edge
 * 
 */
public class Graph {


	private ArrayList<Point> points;
	private ArrayList<Vertex> vertices;
	
	public Graph(ArrayList<Point> points, ArrayList<Vertex> edges) {
		this.points = points;
		this.vertices = edges;
	}
	
	// TODO: further functionalities
}
