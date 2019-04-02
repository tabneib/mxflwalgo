package de.nhd.mxflwalgo.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Class representing a decoratable graph
 * 
 */
public class MGraph extends Graph {

	private static final Color DEFAULT_HIGHLIGHTED_VERTEX_COLOR = Color.red;
	private static final Color DEFAULT_HIGHLIGHTED_ARC_COLOR = Color.black;

	private HashMap<MVertex, Color> hightlightedVertices;

	private HashMap<Arc, Color> hightlightedArcs;

	public MGraph(ArrayList<MVertex> vertices, ArrayList<Arc> arcs) {
		super(vertices, arcs);
		this.hightlightedVertices = new HashMap<>();
		this.hightlightedArcs = new HashMap<>();
	}

	// Lists of arcs & nodes that should be highlighted for the purpose of
	// describing intermediate steps of the algorithms

	public boolean isHighlighted(Arc arc) {
		return this.hightlightedArcs.containsKey(arc);
	}

	public boolean isHighlighted(MVertex vertex) {
		return this.hightlightedVertices.containsKey(vertex);
	}

	public boolean isInHighlightMode() {
		return !this.hightlightedArcs.isEmpty() || !this.hightlightedVertices.isEmpty();
	}

	public Color getHighlightColor(Arc arc) {
		return this.hightlightedArcs.get(arc);
	}

	public Color getHighlightColor(MVertex vertex) {
		return this.hightlightedVertices.get(vertex);
	}

	public void clearAllHighlight() {
		this.hightlightedArcs.clear();
		this.hightlightedVertices.clear();
	}

	public void highlightArc(Arc arc, Color color) {
		this.hightlightedArcs.put(arc,
				color == null ? DEFAULT_HIGHLIGHTED_ARC_COLOR : color);
	}

	public void hightlightVertex(MVertex vertex, Color color) {
		this.hightlightedVertices.put(vertex,
				color == null ? DEFAULT_HIGHLIGHTED_VERTEX_COLOR : color);
	}

	public HashMap<Arc, Color> getHightlightedArcs() {
		return hightlightedArcs;
	}
	
	public HashMap<MVertex, Color> getHightlightedVertices() {
		return hightlightedVertices;
	}

	/**
	 * Reset all calculated stuffs of this graph
	 */
	public void reset() {
		this.clearAllHighlight();
		for (MVertex v : this.getVertices())
			v.reset();
		for (Arc a : this.getArcs())
			a.reset();
	}
}
