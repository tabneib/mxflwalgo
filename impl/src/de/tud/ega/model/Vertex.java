package de.tud.ega.model;

/**
 * Class representing a vertex
 * 
 */
public class Vertex {

	private Point startPoint, endPoint;
	private int capacity;

	/**
	 * Construct an vertex by two points and its capacity
	 * 
	 * @param startPoint
	 * @param endPoint
	 */
	public Vertex(Point startPoint, Point endPoint, int capacity) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.setCapacity(capacity);
	}
	
	/**
	 * Construct an edge by two points
	 * 
	 * @param startPoint
	 * @param endPoint
	 */
	public Vertex(Point startPoint, Point endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Vertex))
			return false;

		Vertex other = (Vertex) o;

		// Check whether two edges have the same startpoint and endpoint.
		return (other.getStartPoint().equals(this.getStartPoint()) &&
				other.getEndPoint().equals(this.getEndPoint()));
	}

	@Override
	public int hashCode() {
		return  startPoint.hashCode() + endPoint.hashCode();
	}


	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point p1) {
		this.startPoint = p1;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point p2) {
		this.endPoint = p2;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public String toString() {
		return "Vertex(" + startPoint.toString() + " - " + endPoint.toString() + ")";
	}
}
