package de.tud.ega.model;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Class representing a 2D-point
 */

public class MVertex extends Point {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * List of arcs in the graph that have this vertex as startpoint
	 */
	private ArrayList<MArc> incidentArcs;
	
	/**
	 * List of arcs in the residual graph that have this vertex as startpoint
	 */
	private ArrayList<ResArc> resIncidentArcs;
	
	/**
	 * The ID of the graph search in which this vertex is already seen
	 */
	private long searchId;
	
	/**
	 * Construct a point by its coordinates
	 * 
	 * @param x first coordinate
	 * @param y second coordinate
	 */
	public MVertex(int x, int y) {
		super(x,y);
		this.incidentArcs = new ArrayList<>();
		this.resIncidentArcs = new ArrayList<>();
		this.searchId = -1;
	}
	

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof MVertex))
			return false;
		MVertex other = (MVertex) obj;
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
	public double euclidDistance(MVertex p) {
		return Math.sqrt((p.x-x)*(p.x-x) + (p.y-y)*(p.y-y));
	}

	/**
	 * Add an arc into the list of incident arcs 
	 * @param arc
	 * 				the arc to be added
	 * @return
	 * 			true if added, false if already existed
	 */
	public boolean addIncidentArc(MArc arc) {
		if (this.incidentArcs.contains(arc))
			return false;
		this.incidentArcs.add(arc);
		return true;
	}
	
	public ArrayList<MArc> getIncidentArcs() {
		return this.incidentArcs;
	}

	/**
	 * Add an residual arc into the list of incident arcs on the residual graph
	 * @param arc
	 * 				the residual arc to be added
	 * @return
	 * 			true if added, false if already existed
	 */
	public boolean addResIncidentArc(ResArc arc) {
		if (this.resIncidentArcs.contains(arc))
			return false;
		this.resIncidentArcs.add(arc);
		return true;
	}
	

	/**
	 * Remove an arc from the list of incident arcs on the residual graph
	 * @param arc
	 * 				the residual arc to be removed
	 * @return
	 * 			true if removed, false if not yet existed
	 */
	public boolean removeResIncidentArc(ResArc arc) {
		return this.resIncidentArcs.remove(arc);
	}
	
	public ArrayList<ResArc> getResIncidentArcs() {
		return this.resIncidentArcs;
	}
	
	/**
	 * Set this vertex as seen in the graph search with the given Id
	 * @param searchId
	 */
	public void setSeen(long searchId) {
		this.searchId = searchId;
	}
	
	/**
	 * Check if this vertex is already seen in the graph search with the given Id
	 * @param searchId
	 * @return
	 */
	public boolean isSeen(long searchId) {
		return this.searchId == searchId;
	}
	@Override
	public String toString() {
		return "(" + Integer.toString(x) + ", " + Integer.toString(y) + ")";
	}
}
