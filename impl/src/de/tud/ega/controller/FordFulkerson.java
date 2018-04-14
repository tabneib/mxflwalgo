package de.tud.ega.controller;

import java.util.ArrayList;
import java.util.UUID;

import de.tud.ega.model.Arc;
import de.tud.ega.model.MArc;
import de.tud.ega.model.MGraph;
import de.tud.ega.model.MVertex;
import de.tud.ega.model.MaxFlowProblem;
import de.tud.ega.model.ResArc;

public class FordFulkerson extends MaxFlowAlgo {

	private MGraph resGraph;
	private String dfsSearchId;

	public FordFulkerson(MaxFlowProblem maxFlowProblem) {
		super(maxFlowProblem);
		createResGraph();
	}

	@Override
	public MGraph run() {
		while (true) {
			AugmentingPath augPath = dfs();
			if (augPath == null)
				break;
			updateResGraph(augPath);
			updateGraph(augPath);
		}
		return this.problem.getGraph();
	}

	@Override
	public MGraph runStep() {
		AugmentingPath augPath = dfs();
		if (augPath == null)
			return null;
		updateResGraph(augPath);
		updateGraph(augPath);
		return this.problem.getGraph();
	}

	/**
	 * Recompute the residual graph.
	 */
	private void createResGraph() {
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
		System.out.println("[Ford-Fulkerson] Residual Graph created. (V,A) = ("
				+ this.resGraph.getVertices().size() + ", "
				+ this.resGraph.getArcs().size() + ")");
	}

	/**
	 * Update the residual graph along a graph augmenting path
	 * 
	 * @param augPath
	 *            The given graph augmenting path
	 */
	private void updateResGraph(AugmentingPath augPath) {
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
	private void updateGraph(AugmentingPath augPath) {
		for (ResArc arc : augPath.arcs) {
			if (arc.isForward())
				((MArc) arc.getOriginalArc())
						.setFlow(((MArc) arc.getOriginalArc()).getFlow() + augPath.value);
			else
				((MArc) arc.getOriginalArc())
						.setFlow(((MArc) arc.getOriginalArc()).getFlow() - augPath.value);
		}
	}

	/**
	 * Depth First Search
	 * 
	 * @return
	 */
	private AugmentingPath dfs() {
		this.dfsSearchId = UUID.randomUUID().toString();
		this.problem.getSource().setSeen(dfsSearchId);
		return recDfs(this.resGraph.getVertices().get(0), new ArrayList<ResArc>(),
				Integer.MAX_VALUE, dfsSearchId);

	}

	/**
	 * 
	 * @param currentNode
	 *            The node that the DFS reaches in this round
	 * @param currentPath
	 *            The path from source to (including) the current node
	 * @param augmentingValue
	 *            The current minimal possible flow augmenting value
	 * @param searchId
	 *            Id of the current DFS
	 * @return
	 */
	private AugmentingPath recDfs(MVertex currentNode, ArrayList<ResArc> currentPath,
			int augmentingValue, String searchId) {
		for (ResArc arc : currentNode.getResIncidentArcs()) {
			if (arc.getResValue() > 0
					&& arc.getEndVertex().equals(this.problem.getTarget())) {
				currentPath.add(arc);
				return new AugmentingPath(currentPath,
						Math.min(augmentingValue, arc.getResValue()));
			} else if (arc.getResValue() > 0 && !arc.getEndVertex().isSeen(searchId)) {
				arc.getEndVertex().setSeen(searchId);
				currentPath.add(arc);
				AugmentingPath res = recDfs(arc.getEndVertex(), currentPath,
						Math.min(augmentingValue, arc.getResValue()), searchId);
				if (res != null)
					// Along this branch there is a result
					return res;
				else
					// Remove the arcs on the wrong branch and
					// continue searching along other branches
					currentPath.remove(arc);
			}
		}
		// Unable to go any further
		return null;
	}

	public MGraph getResGraph() {
		return this.resGraph;
	}

	/**
	 * Inner class that represents a graph augmenting path which is a tuple of
	 * generic type for its elements
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
			String str = value + " - ";
			for (ResArc arc : this.arcs)
				str += arc.getDirection() + " - ";
			str = str.substring(0, str.length() - 3);
			return str;
		}
	}

}
