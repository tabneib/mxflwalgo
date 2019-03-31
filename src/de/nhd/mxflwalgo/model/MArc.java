package de.nhd.mxflwalgo.algos.model;

/**
 * Class representing an arc for the max flow problem
 * 
 */
public class MArc extends Arc {

	private int capacity;
	private int flow = 0;
	public int length;
	Direction direction = null;

	/**
	 * Construct an edge by two points and its capacity
	 * 
	 * @param startVertex
	 * @param endVertex
	 * @param capacity
	 */
	public MArc(MVertex startVertex, MVertex endVertex, int capacity) {
		super(startVertex, endVertex);
		this.capacity = capacity;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getFlow() {
		return flow;
	}
	
	public void setFlow(int flow) {
		this.flow = flow;
	}


	@Override
	public String toString() {
		return "Arc[(" + startVertex.toString() + " - " + endVertex.toString() + "), (" +
				this.flow + "| " + this.capacity + ")]";
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
