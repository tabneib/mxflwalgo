package de.tud.ega.model;

public class MaxFlowProblem {
	private MGraph graph;
	private MVertex source;
	private MVertex target;
	
	public MaxFlowProblem(MGraph graph){
		this.graph = graph;
		this.determineSourceTarget();
	}
	
	/**
	 * Determine the source and target nodes on the graph such that no arc incident to
	 * the source or target node belongs to a minimum cut.
	 * 
	 * TODO: Do this in a mathematical rigorous way. Current approach: select 2 nodes that
	 * are farthest to each other (on two facing corners)
	 */
	private void determineSourceTarget() {
		this.source = this.graph.getVertices().get(0);
		this.target = this.graph.getVertices().get(this.graph.getVertices().size() - 1);
	}
	
	public MGraph getGraph() {
		return this.graph;
	}

	public MVertex getSource() {
		return source;
	}

	public MVertex getTarget() {
		return target;
	}
}
