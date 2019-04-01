package de.nhd.mxflwalgo.algos;

import java.util.ArrayList;
import java.util.UUID;

import de.nhd.mxflwalgo.model.MGraph;
import de.nhd.mxflwalgo.model.MVertex;
import de.nhd.mxflwalgo.model.MaxFlowProblem;
import de.nhd.mxflwalgo.model.ResArc;

public class FordFulkerson extends MaxFlowAlgo {

	public FordFulkerson(MaxFlowProblem maxFlowProblem) {
		super(maxFlowProblem);
	}

	@Override
	public MGraph run() {
		while (true) {
			AugmentingPath augPath = dfs();
			if (augPath == null)
				break;
			updateGraphs(augPath);
		}
		this.problem.getGraph().clearAllHighlight();
		this.finished = true;
		return this.problem.getGraph();
	}

	@Override
	public MGraph runStep() {
		AugmentingPath augPath = dfs();
		if (augPath == null) {
			this.problem.getGraph().clearAllHighlight();
			this.finished = true;
			return this.problem.getGraph();
		}
		highlightAugPath(augPath, true);
		updateGraphs(augPath);
		return this.problem.getGraph();
	}

	/**
	 * Depth First Search
	 * 
	 * @return
	 */
	private AugmentingPath dfs() {
		String searchId = UUID.randomUUID().toString();
		this.problem.getSource().setSeen(searchId);
		return recDfs(this.resGraph.getVertices().get(0), new ArrayList<ResArc>(),
				Integer.MAX_VALUE, searchId);
	}

	/**
	 * 
	 * @param currentNode
	 *            The node in residual graph that the DFS reaches in this round
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
		for (ResArc arc : currentNode.getIncidentResArcs()) {
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
}
