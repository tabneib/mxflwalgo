package de.tud.ega.model;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class representing a graph
 * 
 */
public class MGraph {

	private ArrayList<MVertex> vertices;
	private ArrayList<Arc> arcs;

	private HashSet<MVertex> hightlightedVertices;
	private HashSet<Arc> hightlightedArcs;

	public MGraph(ArrayList<MVertex> vertices, ArrayList<Arc> arcs) {
		this.vertices = vertices;
		this.arcs = arcs;
		this.hightlightedVertices = new HashSet<>();
		this.hightlightedArcs = new HashSet<>();
	}

	public ArrayList<Arc> getArcs() {
		return arcs;
	}

	public ArrayList<MVertex> getVertices() {
		return vertices;
	}

	public String toString() {
		String str = "MGraph[" + vertices.size() + ", " + arcs.size() + "]\n";
		for (Arc a : arcs)
			str += a.toString() + "\t";

		return str;
	}
	// TODO: further functionalities: Lists of arcs & nodes that should be
	// highlighted for the purpose of
	// describing intermediate steps of the algorithms

	public boolean isHighlighted(Arc arc) {
		return this.hightlightedArcs.contains(arc);
	}

	public boolean isHighlighted(MVertex vertex) {
		return this.hightlightedVertices.contains(vertex);
	}
	
	public boolean highlightMode() {
		return !this.hightlightedArcs.isEmpty() || !this.hightlightedVertices.isEmpty();
	}
	
	public void clearAllHighlight() {
		this.hightlightedArcs.clear();
		this.hightlightedVertices.clear();
	}
	
	public void highlightArc(Arc arc) {
		this.hightlightedArcs.add(arc);
	}

	public void hightlightVertex(MVertex vertex) {
		this.hightlightedVertices.add(vertex);
	}
}
