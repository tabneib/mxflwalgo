package de.tud.ega.model;

/**
 * Class representing an Edge
 * 
 */
public class Edge {

	private Point p1, p2;

	/**
	 * Construct an Edge by two Points
	 * 
	 * @param p1
	 *            first point
	 * @param p2
	 *            second point
	 * @param t1
	 *            first neighbor triangle
	 * @param t2
	 *            second neighbor triangle (may be null)
	 */
	public Edge(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Edge))
			return false;

		Edge other = (Edge) o;

		// Check whether two edges have the same endpoints The order of P1 and P2
		// has not to be the same
		return (other.getP1().equals(this.getP1()) && other.getP2().equals(this.getP2()))
				|| (other.getP1().equals(this.getP2())
						&& other.getP2().equals(this.getP1()));
	}

	@Override
	public int hashCode() {

		return  p1.hashCode() + p2.hashCode();
	}


	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	@Override
	public String toString() {
		return "Edge(" + p1.toString() + " - " + p2.toString() + ")";
	}
}
