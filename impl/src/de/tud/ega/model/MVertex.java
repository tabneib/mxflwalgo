package de.tud.ega.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
	 * List of arcs in the level graph that have this vertex as startpoint
	 */
	private Pair<String, ArrayList<ResArc>> incidentLevelArcs;

	/**
	 * List of arcs in the residual graph that precedes this vertex during a
	 * search
	 */
	private Pair<String, ArrayList<ResArc>> searchPredecessorArcs;

	/**
	 * The index of the current arc in the level graph for a given DFS id
	 */
	private Pair<String, Integer> currentLevelArcIdx;

	/**
	 * This is the arc to the parent vertex of this vertex in some arborescence.
	 * This field is optional and can be used by an algorithm.
	 */
	private Arc parent = null;

	/**
	 * The ID of the graph search in which this vertex is already seen
	 */
	private String searchId = "";

	/**
	 * List of real distances from this vertex to source in a search
	 */
	private HashMap<String, Integer> distances;

	/**
	 * Set of searchIDs of searches that have already included this vertex into
	 * its level graph
	 */
	private HashSet<String> levGraphIncluded;

	/**
	 * Set of searchIDs of DFS searches that have already finished at this
	 * vertex
	 */
	private HashSet<String> dfsFinished;

	/**
	 * Set of searchIDs of DFS searches that have already been removed at this
	 * vertex, used in Dinic
	 */
	private HashSet<String> dfsRemoved;

	/**
	 * Construct a point by its coordinates
	 * 
	 * @param x
	 *            first coordinate
	 * @param y
	 *            second coordinate
	 */
	public MVertex(int x, int y) {
		super(x, y);
		this.incidentArcs = new ArrayList<>();
		this.resIncidentArcs = new ArrayList<>();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof MVertex))
			return false;
		MVertex other = (MVertex) obj;
		return this.x == other.x && this.y == other.y;
	}

	@Override
	public int hashCode() {
		long r = Math.round(x * 100 + y * 10);

		if (r < Integer.MAX_VALUE)
			return (int) r;
		return Integer.MIN_VALUE + (int) (r % Integer.MAX_VALUE);
	}

	/**
	 * Euclidian distance between two points
	 * 
	 * @param p
	 *            second point
	 * @return distance
	 */
	public double euclidDistance(MVertex p) {
		return Math.sqrt((p.x - x) * (p.x - x) + (p.y - y) * (p.y - y));
	}

	/**
	 * Determine the next incident arc in the level graph. This is used in DFS.
	 * @param dfsSearchId	The ID of the corresponding DFS
	 * @param bfsSearchId	The ID of the BFS that generates the level graph
	 * @return	The next incident residual arc or null if no more such arc
	 */
	public ResArc getNextLevelArc(String dfsSearchId, String bfsSearchId) {
		if (!this.incidentLevelArcs.getKey().equals(bfsSearchId))
			throw new RuntimeException("Search ID not matched! Expected: "
					+ this.incidentLevelArcs.getKey() + ", given: " + bfsSearchId);
		if (this.currentLevelArcIdx == null
				|| !this.currentLevelArcIdx.getKey().equals(dfsSearchId))
			this.currentLevelArcIdx = new Pair<>(dfsSearchId, 0);
		if (this.incidentLevelArcs.getValue().isEmpty() || this.currentLevelArcIdx
				.getValue() >= this.incidentLevelArcs.getValue().size())
			return null;
		this.currentLevelArcIdx.setValue(this.currentLevelArcIdx.getValue() + 1);
		return this.incidentLevelArcs.getValue()
				.get(this.currentLevelArcIdx.getValue() - 1);
	}

	/**
	 * Add an arc into the list of incident arcs
	 * 
	 * @param arc
	 *            the arc to be added
	 * @return true if added, false if already existed
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
	 * 
	 * @param arc
	 *            the residual arc to be added
	 * @return true if added, false if already existed
	 */
	public boolean addResIncidentArc(ResArc arc) {
		if (this.resIncidentArcs.contains(arc))
			return false;
		this.resIncidentArcs.add(arc);
		return true;
	}

	/**
	 * Remove an arc from the list of incident arcs on the residual graph
	 * 
	 * @param arc
	 *            the residual arc to be removed
	 * @return true if removed, false if not yet existed
	 */
	public boolean removeResIncidentArc(ResArc arc) {
		return this.resIncidentArcs.remove(arc);
	}

	public ArrayList<ResArc> getResIncidentArcs() {
		return this.resIncidentArcs;
	}

	public boolean addIncidentLevelArcs(String searchId, ResArc arc) {
		if (this.incidentLevelArcs == null
				|| !this.incidentLevelArcs.getKey().equals(searchId))
			this.incidentLevelArcs = new Pair<>(searchId, new ArrayList<ResArc>());
		if (this.incidentLevelArcs.getValue().contains(arc))
			return false;
		this.incidentLevelArcs.getValue().add(arc);
		return true;
	}

	public ArrayList<ResArc> getIncidentLevelArcs(String searchId) {
		if (!this.incidentLevelArcs.getKey().equals(searchId))
			throw new RuntimeException("Search ID not matched!");
		return this.incidentLevelArcs.getValue();
	}

	public void setDistance(String searchId, Integer distance) {
		if (this.distances == null)
			this.distances = new HashMap<>();
		this.distances.put(searchId, distance);
	}

	public int getDistance(String searchId) {
		if (this.distances.containsKey(searchId))
			return this.distances.get(searchId);
		else
			return -1000;
		// throw new RuntimeException(
		// "No distance value set for the search with id " + searchId);
	}

	/**
	 * Add an residual arc into the list of preceding arcs during a search.
	 * Update the search id if not matched
	 * 
	 * @param searchId
	 *            ID of the corresponding search
	 * @param arc
	 *            the residual arc to be added
	 * @return true if added, false if already existed
	 */
	public boolean addSearchPreArc(String searchId, ResArc arc) {
		if (this.searchPredecessorArcs == null)
			this.searchPredecessorArcs = new Pair<>(searchId, new ArrayList<ResArc>());
		if (!this.searchPredecessorArcs.getKey().equals(searchId))
			this.searchPredecessorArcs = new Pair<>(searchId, new ArrayList<ResArc>());
		if (this.searchPredecessorArcs.getValue().contains(arc))
			return false;
		this.searchPredecessorArcs.getValue().add(arc);
		return true;
	}

	/**
	 * Remove an arc from the list of preceding arcs during a search
	 * 
	 * @param searchId
	 *            ID of the corresponging search
	 * @param arc
	 *            the residual arc to be removed
	 * @return true if removed, false if not yet existed
	 */
	public void removeSearchPreArc(String searchId, ResArc arc) {
		if (!this.searchPredecessorArcs.getKey().equals(searchId))
			throw new RuntimeException("Search ID not matched!");
		this.searchPredecessorArcs.getValue().remove(arc);
	}

	public ArrayList<ResArc> getSearchPreArcs(String searchId) {
		if (!this.searchPredecessorArcs.getKey().equals(searchId))
			throw new RuntimeException("Search ID not matched!");
		return this.searchPredecessorArcs.getValue();
	}

	public void setIncluded(String searchId) {
		if (this.levGraphIncluded == null)
			this.levGraphIncluded = new HashSet<>();
		this.levGraphIncluded.add(searchId);
	}

	public boolean isIncluded(String searchId) {
		if (this.levGraphIncluded == null)
			return false;
		return this.levGraphIncluded.contains(searchId);
	}

	public void setDfsFinished(String searchId) {
		if (this.dfsFinished == null)
			this.dfsFinished = new HashSet<>();
		this.dfsFinished.add(searchId);
	}

	public boolean isDfsFinished(String searchId) {
		if (this.dfsFinished == null)
			return false;
		return this.dfsFinished.contains(searchId);
	}

	public void setDfsRemoved(String searchId) {
		if (this.dfsRemoved == null)
			this.dfsRemoved = new HashSet<>();
		this.dfsRemoved.add(searchId);
	}

	public boolean isDfsRemoved(String searchId) {
		if (this.dfsRemoved == null)
			return false;
		return this.dfsRemoved.contains(searchId);
	}

	/**
	 * Set this vertex as seen in the graph search with the given Id
	 * 
	 * @param searchId
	 */
	public void setSeen(String searchId) {
		this.searchId = searchId;
	}

	/**
	 * Check if this vertex is already seen in the graph search with the given
	 * Id
	 * 
	 * @param searchId
	 * @return
	 */
	public boolean isSeen(String searchId) {
		return this.searchId.equals(searchId);
	}

	@Override
	public String toString() {
		return "(" + Integer.toString(x) + ", " + Integer.toString(y) + ")";
	}

	public Arc getParent() {
		return parent;
	}

	public void setParent(Arc parent) {
		this.parent = parent;
	}
}
