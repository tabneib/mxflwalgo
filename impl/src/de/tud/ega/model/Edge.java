package de.tud.ega.model;


/**
 * Class representing a vertex
 * 
 */
public class Edge implements Comparable<Edge> {

	private Vertex startVertex, endVertex;
	public int capacity;
	public int length;

	/**
	 * Construct an vertex by two points and its capacity
	 * 
	 * @param startVertex
	 * @param endVertex
	 */
	public Edge(Vertex startVertex, Vertex endVertex, int capacity) {
		this.startVertex = startVertex;
		this.endVertex = endVertex;
		this.capacity = capacity;
	}

	/**
	 * Construct an edge by two points
	 * 
	 * @param startPoint
	 * @param endPoint
	 */
	public Edge(Vertex startPoint, Vertex endPoint) {
		this.startVertex = startPoint;
		this.endVertex = endPoint;
		this.length = (int) Math.sqrt(Math.pow(endVertex.x - startVertex.x, 2)
				+ Math.pow(endVertex.y - startVertex.y, 2));
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Edge))
			return false;

		Edge other = (Edge) o;

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
	public int compareTo(Edge o) {

		if (o == null)
			throw new NullPointerException();
		if (!(o instanceof Edge))
			throw new RuntimeException("Input must be of type Edge!");
		
		Edge other = (Edge) o;
		if (other.length > this.length)
			return -1;
		else if (other.length < this.length)
			return 1;
		else if (!this.equals(other))
			return 1;
		return 0;
			
	}
}
