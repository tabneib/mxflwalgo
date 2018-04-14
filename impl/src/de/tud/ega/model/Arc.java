package de.tud.ega.model;

import java.util.UUID;

public abstract class Arc implements Comparable<Arc>{

	/**
	 * Each arc has an unique ID to overcome the fact that the set of arc is a multiset
	 */
	public final String id;
	
	protected MVertex startVertex;
	protected MVertex endVertex;
	public int length;
	Direction direction = null;

	/**
	 * Construct an arc by two points
	 * 
	 * @param startVertex
	 * @param endVertex
	 */
	public Arc(MVertex startVertex, MVertex endVertex) {
		this.startVertex = startVertex;
		this.endVertex = endVertex;
		this.length = (int) Math.sqrt(Math.pow(endVertex.x - startVertex.x, 2)
				+ Math.pow(endVertex.y - startVertex.y, 2));
		this.id = UUID.randomUUID().toString();
	}
	
	/**
	 * Get the direction of this arc
	 * @return
	 */
	public Direction getDirection() {
		if (this.direction != null)
			return this.direction;
		
		if (this.getStartVertex().x == this.endVertex.x) {
			if (this.getStartVertex().y < this.endVertex.y)
				this.direction = Direction.VERTICAL_DOWN;
			else
				this.direction = Direction.VERTICAL_UP;
		}
		else if (this.getStartVertex().x > this.endVertex.x) {
				if (this.getStartVertex().y == this.endVertex.y)
					this.direction = Direction.HORIZONTAL_TO_LEFT;
				else if (this.getStartVertex().y > this.endVertex.y)
					this.direction = Direction.DIAGONAL_TO_TOPLEFT;
				else
					this.direction = Direction.DIAGONAL_TO_BOTTOMLEFT;
			}
			else if (this.getStartVertex().y == this.endVertex.y)
				this.direction = Direction.HORIZONTAL_TO_RIGHT;
			else if (this.getStartVertex().y > this.endVertex.y)
				this.direction = Direction.DIAGONAL_TO_TOPRIGHT;
			else
				this.direction = Direction.DIAGONAL_TO_BOTTOMRIGHT;
		
		return this.direction;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Arc))
			return false;

		Arc other = (Arc) o;

		// Check whether two edges have the same start vertex and end vertex.
		return this.id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return startVertex.hashCode() + endVertex.hashCode();
	}

	public MVertex getStartVertex() {
		return startVertex;
	}

	public MVertex getEndVertex() {
		return endVertex;
	}

	@Override
	public String toString() {
		return "Arc(" + startVertex.toString() + " - " + endVertex.toString() + ")";
	}

	@Override
	public int compareTo(Arc o) {

		if (o == null)
			throw new NullPointerException();
		if (!(o instanceof Arc))
			throw new RuntimeException("Input must be of type Arc!");
		
		Arc other = (Arc) o;
		if (other.length > this.length)
			return -1;
		else if (other.length < this.length)
			return 1;
		else if (!this.equals(other))
			return 1;
		return 0;
			
	}
	
	/**
	 * Possible directions of the arcs
	 */
	public enum Direction {
		HORIZONTAL_TO_RIGHT,
		HORIZONTAL_TO_LEFT,
		VERTICAL_UP,
		VERTICAL_DOWN,
		DIAGONAL_TO_TOPRIGHT,
		DIAGONAL_TO_TOPLEFT,
		DIAGONAL_TO_BOTTOMRIGHT,
		DIAGONAL_TO_BOTTOMLEFT
	}
}
