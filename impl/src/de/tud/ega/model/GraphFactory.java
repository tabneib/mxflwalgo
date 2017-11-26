package de.tud.ega.model;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

public class GraphFactory {

	private static final int MIN_COORDINATE = 0;
	private static final int MAX_COORDINATE = 1000;

	public static Graph getPlanarGraph(int vertexNumber, int maxCapacity) {
		HashSet<Vertex> vertices = new HashSet<>();
		TreeSet<Edge> allEdges = new TreeSet<>();
		ArrayList<Edge> edges = new ArrayList<>();

		// Create vertices randomly
		Vertex v;
		for (int i = 0; i < vertexNumber; i++) {
			while (true) {
				v = new Vertex(random(MIN_COORDINATE, MAX_COORDINATE),
						random(MIN_COORDINATE, MAX_COORDINATE));
				if (!vertices.contains(v))
					break;
			}
			vertices.add(v);
		}

		// Create all possible edges from the vertices
		for (Vertex v1 : vertices)
			for (Vertex v2 : vertices)
				if (!(v1.equals(v2))) {
					allEdges.add(new Edge(v1, v2));
				}

		// Step by step insert edges into the result graph
		boolean intersected;
		for (Edge e : allEdges) {

			// Check for intersection
			intersected = false;
			for (Edge addedEdge : edges)
				if (intersected = intersect(e, addedEdge))
					break;

			// If the edge does not cut any added edge, then add it
			if (!intersected) {
				edges.add(e);
				vertices.add(e.getStartVertex());
				vertices.add(e.getEndVertex());
			}
		}

		return new Graph(vertices, edges);
	}

	// ---------------------------------------------------------------------------------->
	// Auxiliary Methods
	// <----------------------------------------------------------------------------------

	/**
	 * 
	 * @param e1
	 * @param e2
	 * @return
	 */
	private static boolean intersect(Edge e1, Edge e2) {

		if (e1.getStartVertex().equals(e2.getStartVertex())
				|| e1.getStartVertex().equals(e2.getEndVertex())
				|| e1.getEndVertex().equals(e2.getStartVertex())
				|| e1.getEndVertex().equals(e2.getEndVertex()))
			return false;
		else

			return Line2D.linesIntersect(e1.getStartVertex().x, e1.getStartVertex().y,
					e1.getEndVertex().x, e1.getEndVertex().y, e2.getStartVertex().x,
					e2.getStartVertex().y, e2.getEndVertex().x, e2.getEndVertex().y);
	}

	/**
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	private static int random(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
}
