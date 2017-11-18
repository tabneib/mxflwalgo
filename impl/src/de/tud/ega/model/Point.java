package de.tud.ega.model;

/**
 * Class representing a 2D-point
 */

public class Point {
	
	public double x, y;
	
	/**
	 * Construct a point by its coordinates
	 * 
	 * @param x first coordinate
	 * @param y second coordinate
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Point))
			return false;
		Point p2 = (Point) obj;
		return this.x==p2.x && this.y == p2.y;
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
	public double euclidDistance(Point p) {
		return Math.sqrt((p.x-x)*(p.x-x) + (p.y-y)*(p.y-y));
	}

	
	@Override
	public String toString() {
		return "(" + Double.toString(x) + ", " + Double.toString(y) + ")";
	}
}
