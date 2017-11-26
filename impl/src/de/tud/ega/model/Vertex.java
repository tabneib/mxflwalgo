package de.tud.ega.model;

import java.awt.Point;

/**
 * Class representing a 2D-point
 */

public class Vertex extends Point {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construct a point by its coordinates
	 * 
	 * @param x first coordinate
	 * @param y second coordinate
	 */
	public Vertex(int x, int y) {
		super(x,y);
	}
	

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Vertex))
			return false;
		Vertex other = (Vertex) obj;
		return this.x==other.x && this.y == other.y;
	}
	

	@Override
	public int hashCode()
	{
		long r = Math.round(x*100+y*10);
		
		if(r<Integer.MAX_VALUE)
			return (int) r;
		return Integer.MIN_VALUE+ (int) (r%Integer.MAX_VALUE);
	}
	
	/**
	 * Euclidian distance between two points
	 * 
	 * @param p second point
	 * @return distance
	 */
	public double euclidDistance(Vertex p) {
		return Math.sqrt((p.x-x)*(p.x-x) + (p.y-y)*(p.y-y));
	}

	
	@Override
	public String toString() {
		return "(" + Integer.toString(x) + ", " + Integer.toString(y) + ")";
	}
}
