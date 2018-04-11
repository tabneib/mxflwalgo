package de.tud.ega.model;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

public class GraphFactory {

	private static final int MAX_COORDINATE = 1000;
	@Deprecated
	private static final int MIN_COORDINATE = 0;
	@Deprecated
	private static final int MIN_DISTANCE = 50;
	
	
	/**
	 * Create a quadratic maximal planar graph.
	 * @param vertexNumber	Total number of vertices of the graph
	 * @param maxCapacity	Upper bound for the capacity of the arcs
	 * @return	a maximal planar graph
	 * @throws Exception
	 */
	public static Graph getBeautifulPlanarGraph(int vertexNumber, int maxCapacity)
			throws Exception {
		int height = vertexNumber / ((int) Math.sqrt(vertexNumber));
		int shortWidth = vertexNumber / height;
		int longWidth = shortWidth + 1;
		int longRowNum = vertexNumber % height;
		ArrayList<ArrayList<Vertex>> vertices = new ArrayList<>();
		ArrayList<Arc> arcs = new ArrayList<>();
		int verticalDist = MAX_COORDINATE / height;
		int shortHorizontalDist = MAX_COORDINATE / (shortWidth - 1);
		int longHorizontalDist = MAX_COORDINATE / (longWidth - 1);
		int y = 0;
		int x;

		// Create vertices on short rows
		for (int i = 0; i < (height - longRowNum); i++) {
			vertices.add(new ArrayList<Vertex>());
			x = 0;
			for (int j = 0; j < (shortWidth - 1); j++) {
				vertices.get(i).add(new Vertex(x, y));
				x += shortHorizontalDist;
			}
			// Last distance is the remaining
			vertices.get(i).add(new Vertex(
					x + MAX_COORDINATE - shortHorizontalDist * (shortWidth - 1), y));
			y += verticalDist;
		}

		// Create vertices on long rows
		for (int i = (height - longRowNum); i < height; i++) {
			vertices.add(new ArrayList<Vertex>());
			x = 0;
			for (int j = 0; j < (longWidth - 1); j++) {
				vertices.get(i).add(new Vertex(x, y));
				x += longHorizontalDist;
			}
			// Last distance is the remaining
			vertices.get(i).add(new Vertex(
					x + MAX_COORDINATE - longHorizontalDist * (longWidth - 1), y));
			y += verticalDist;
		}

		// Create all horizontal arcs
		Random random = new Random();
		for (ArrayList<Vertex> row : vertices) 
			for (int i = 1; i < row.size(); i++) 
				arcs.add(
						new Arc(row.get(i - 1), row.get(i), random.nextInt(maxCapacity)));

		// Create all vertical and diagonal arcs
		for (int i = 0; i < vertices.size() - 1; i++) {
			for (int j = 0; j < vertices.get(i).size(); j++) {
				// vertical arc
				arcs.add(new Arc(vertices.get(i).get(j), vertices.get(i + 1).get(j),
						random.nextInt(maxCapacity)));
				// diagonal arc
				if (j < vertices.get(i).size() - 1) {
					// Not yet the last vertex on this row
					if (random.nextBoolean())
						arcs.add(new Arc(vertices.get(i).get(j),
								vertices.get(i + 1).get(j + 1),
								random.nextInt(maxCapacity)));
					else
						arcs.add(new Arc(vertices.get(i).get(j + 1),
								vertices.get(i + 1).get(j), random.nextInt(maxCapacity)));
				}
			}
			// Turning from short width to long width
			if (vertices.get(i + 1).size() > vertices.get(i).size())
				arcs.add(new Arc(vertices.get(i).get(vertices.get(i).size() - 1), 
						vertices.get(i+1).get(vertices.get(i+1).size() - 1),
						random.nextInt(maxCapacity)));
		}
		
		// Insert all created vertices into the final list
		ArrayList<Vertex> v = new ArrayList<>();
		for (ArrayList<Vertex> row : vertices)
			v.addAll(row);

		// For each arc, insert the corresponding reversed arc
		ArrayList<Arc> reversedArcs = new ArrayList<>();
		for (Arc arc: arcs)
			reversedArcs.add(new Arc(arc.getEndVertex(), 
					arc.getStartVertex(), random.nextInt(maxCapacity)));
		arcs.addAll(reversedArcs);
		
		System.out.println("[Graph Factory] New Graph Created. (V,A) = ("
		+ v.size() + ", " + arcs.size() + ")");
		
		return new Graph(v, arcs);
	}
	

	/**
	 * Random approach.
	 * This approach cannot generate a beautiful maximal planar graph.
	 * @param vertexNumber
	 * @param maxCapacity
	 * @return
	 */
	@Deprecated
	public static Graph getPlanarGraph(int vertexNumber, int maxCapacity) {
		ArrayList<Vertex> vertices = new ArrayList<>();
		TreeSet<Arc> allArcs = new TreeSet<>();
		ArrayList<Arc> arcs = new ArrayList<>();

		// Create vertices randomly
		Vertex v;
		for (int i = 0; i < vertexNumber; i++) {
			while (true) {
				v = new Vertex(random(MIN_COORDINATE, MAX_COORDINATE),
						random(MIN_COORDINATE, MAX_COORDINATE));
				if (!vertices.contains(v)) {
					boolean tooNear = false;
					for (Vertex addedV : vertices) {
						if (v.distance(addedV.getX(), addedV.getY()) < MIN_DISTANCE){
							tooNear = true;
							break;
						}
					}
					if (!tooNear)		
						break;
				}
			}
			vertices.add(v);
		}

		// Create all possible arcs from the vertices
		Random random = new Random();
		for (Vertex v1 : vertices)
			for (Vertex v2 : vertices)
				if (!(v1.equals(v2))) 
					allArcs.add(new Arc(v1, v2, random.nextInt(maxCapacity)));
		
		// Step by step insert arcs into the result graph
		boolean intersected;
		for (Arc e : allArcs) {

			// Check for intersection
			intersected = false;
			for (Arc addedArc : arcs)
				if (intersected = intersect(e, addedArc))
					break;

			// If the arc does not cut any added arc, then add it
			if (!intersected) {
				arcs.add(e);
				vertices.add(e.getStartVertex());
				vertices.add(e.getEndVertex());
			}
		}

		System.out.println("Graph Factory: New Graph Created!\n#Vertices: "
		+ vertices.size() + "\n#Arcs = " + arcs.size());
		
		return new Graph(vertices, arcs);
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
	private static boolean intersect(Arc e1, Arc e2) {

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
