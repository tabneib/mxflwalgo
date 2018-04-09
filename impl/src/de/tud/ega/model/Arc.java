package de.tud.ega.model;

import java.awt.event.AWTEventListener;

/**
 * Class representing a vertex
 * 
 */
public class Arc implements Comparable<Arc>{

	private Vertex startVertex, endVertex;
	public final int capacity;
	private int flow;
	public int length;

	/**
	 * Construct an edge by two points and its capacity
	 * 
	 * @param startVertex
	 * @param endVertex
	 */
	public Arc(Vertex startVertex, Vertex endVertex, int capacity) {
		this.startVertex = startVertex;
		this.endVertex = endVertex;
		this.capacity = capacity;
		this.length = (int) Math.sqrt(Math.pow(endVertex.x - startVertex.x, 2)
				+ Math.pow(endVertex.y - startVertex.y, 2));
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Arc))
			return false;

		Arc other = (Arc) o;

		// Check whether two edges have the same start vertex and end vertex.
		return (other.getStartVertex().equals(this.getStartVertex())
				&& other.getEndVertex().equals(this.getEndVertex()));
	}

	@Override
	public int hashCode() {
		return startVertex.hashCode() + endVertex.hashCode();
	}

	public Vertex getStartVertex() {
		return startVertex;
	}

	public Vertex getEndVertex() {
		return endVertex;
	}

	@Override
	public String toString() {
		return "Edge(" + startVertex.toString() + " - " + endVertex.toString() + ")";
	}

	@Override
	public int compareTo(Arc o) {

		if (o == null)
			throw new NullPointerException();
		if (!(o instanceof Arc))
			throw new RuntimeException("Input must be of type Edge!");
		
		Arc other = (Arc) o;
		if (other.length > this.length)
			return -1;
		else if (other.length < this.length)
			return 1;
		else if (!this.equals(other))
			return 1;
		return 0;
			
	}
}
