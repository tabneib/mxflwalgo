package de.tud.ega.controller;

import java.util.ArrayList;

import de.tud.ega.model.Arc;
import de.tud.ega.model.MArc;
import de.tud.ega.model.MGraph;
import de.tud.ega.model.MaxFlowProblem;
import de.tud.ega.model.ResArc;

public abstract class MaxFlowAlgo {

	protected final MaxFlowProblem problem;
	protected boolean finished = false;
	protected MGraph resGraph;
	
	public MaxFlowAlgo(MaxFlowProblem maxFlowProblem) {
		this.problem = maxFlowProblem;
		createResGraph();
	}
	
	/**
	 * Run the whole algorithm
	 * @return
	 * 			A graph that represents the final result
	 */
	public abstract MGraph run();
	
	/**
	 * Run one step of the algorithm
	 * @return
	 * 			A graph that represents the intermediate result
	 */
	public abstract MGraph runStep();
	
	/**
	 * Check if the algorithm has already finished
	 * @return
	 * 			True if finished, False otherwise
	 */
	public boolean isFinished() {
		return finished;
	};
	
	/**
	 * Initialize the residual graph.
	 */
	protected void createResGraph() {
		ArrayList<Arc> arcs = new ArrayList<>();
		for (Arc arc : this.problem.getGraph().getArcs()) {
			MArc mArc = (MArc) arc;

			ResArc forward = new ResArc(mArc.getStartVertex(), mArc.getEndVertex(),
					mArc.getCapacity() - mArc.getFlow(), null, arc, true);

			ResArc backward = new ResArc(mArc.getEndVertex(), mArc.getStartVertex(),
					mArc.getFlow(), forward, arc, false);

			forward.setBackwardResArc(backward);
			arcs.add(forward);
			arcs.add(backward);
			mArc.getStartVertex().addResIncidentArc(forward);
			mArc.getEndVertex().addResIncidentArc(backward);
		}
		this.resGraph = new MGraph(this.problem.getGraph().getVertices(), arcs);
		System.out.println("[Max-Flow Algorithm] Residual Graph initialized. (V,A) = ("
				+ this.resGraph.getVertices().size() + ", "
				+ this.resGraph.getArcs().size() + ")");
	}
	
	/**
	 * Update the residual graph along a graph augmenting path
	 * 
	 * @param augPath
	 *            The given graph augmenting path
	 */
	protected void updateResGraph(AugmentingPath augPath) {
		System.out.println("AugPath = " + augPath);
		for (ResArc arc : augPath.arcs) {
			if (arc.getResValue() >= augPath.value) {
				arc.setResValue(arc.getResValue() - augPath.value);
				// Update the backward arc
				arc.getBackwardResArc().setResValue(
						arc.getBackwardResArc().getResValue() + augPath.value);
			} else
				throw new RuntimeException("Invalid augmenting value: " + augPath.value);
		}
	}

	/**
	 * Update the graph along a graph augmenting path
	 * 
	 * @param augPath
	 *            The given graph augmenting path
	 */
	protected void updateGraph(AugmentingPath augPath) {
		for (ResArc arc : augPath.arcs) {
			if (arc.isForward())
				((MArc) arc.getOriginalArc())
						.setFlow(((MArc) arc.getOriginalArc()).getFlow() + augPath.value);
			else
				((MArc) arc.getOriginalArc())
						.setFlow(((MArc) arc.getOriginalArc()).getFlow() - augPath.value);
		}
	}

	public MGraph getResGraph() {
		return this.resGraph;
	}

	/**
	 * Inner class that represents a graph augmenting path
	 *
	 */
	public class AugmentingPath {

		/**
		 * The arcs on the residual graph that belong to this path
		 */
		public final ArrayList<ResArc> arcs;
		public final int value;

		public AugmentingPath(ArrayList<ResArc> arcs, int value) {
			this.arcs = arcs;
			this.value = value;
		}

		public String toString() {
			String str = value + " | ";
			for (ResArc arc : this.arcs)
				str += arc.getDirection() + " - ";
			return str.substring(0, str.length() - 2);
		}
	}
}
