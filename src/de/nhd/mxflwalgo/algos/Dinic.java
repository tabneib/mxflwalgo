package de.nhd.mxflwalgo.algos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

import de.nhd.mxflwalgo.model.MGraph;
import de.nhd.mxflwalgo.model.MVertex;
import de.nhd.mxflwalgo.model.MaxFlowProblem;
import de.nhd.mxflwalgo.model.ResArc;

public class Dinic extends MaxFlowAlgo {

	public Dinic(MaxFlowProblem maxFlowProblem) {
		super(maxFlowProblem);
	}

	@Override
	public MGraph run() {
		while (true) {
			String bfsSearchId = UUID.randomUUID().toString();
			if (!this.bfs(bfsSearchId))
				break;

			AugmentingPath augPath = this.dfs(bfsSearchId);
			while (augPath != null) {
				updateGraphs(augPath);
				augPath = this.dfs(bfsSearchId);
			}
		}

		this.problem.getGraph().clearAllHighlight();
		this.finished = true;
		return this.problem.getGraph();
	}

	@Override
	public MGraph runStep() {
		this.problem.getGraph().clearAllHighlight();
		String bfsSearchId = UUID.randomUUID().toString();

		if (!this.bfs(bfsSearchId)) {
			this.problem.getGraph().clearAllHighlight();
			this.finished = true;
			return this.problem.getGraph();
		}

		AugmentingPath augPath = this.dfs(bfsSearchId);
		while (augPath != null) {
			highlightAugPath(augPath, false);
			updateGraphs(augPath);
			augPath = this.dfs(bfsSearchId);
		}
		return this.problem.getGraph();
	}

	/**
	 * Breadth First Search
	 * 
	 * @return
	 */
	private boolean bfs(String bfsSearchId) {
		boolean targetReached = false;
		this.problem.getSource().setSeen(bfsSearchId);

		LinkedList<MVertex> queue = new LinkedList<>();
		this.problem.getSource().setDistance(bfsSearchId, 0);
		queue.addLast(this.problem.getSource());

		while (!queue.isEmpty()) {
			MVertex parent = queue.removeFirst();
			for (ResArc childArc : parent.getIncidentResArcs()) {
								
				if (childArc.getResValue() == 0)
					continue;
				if (childArc.getEndVertex().equals(this.problem.getTarget())) {
					if (!targetReached) {
						targetReached = true;
						this.problem.getTarget().setSeen(bfsSearchId);
						this.problem.getTarget().setDistance(bfsSearchId,
								parent.getDistance(bfsSearchId) + 1);
						this.problem.getTarget().addSearchPreArc(bfsSearchId, childArc);
					} else if (this.problem.getTarget().getDistance(
							bfsSearchId) == parent.getDistance(bfsSearchId) + 1){
						this.problem.getTarget().addSearchPreArc(bfsSearchId, childArc);
					}
					continue;
				}
				if (childArc.getEndVertex().isSeen(bfsSearchId)) {
					if (childArc.getEndVertex().getDistance(
							bfsSearchId) == parent.getDistance(bfsSearchId) + 1){
						childArc.getEndVertex().addSearchPreArc(bfsSearchId, childArc);
					}
					continue;
				}
				if (!targetReached) {
					childArc.getEndVertex().setSeen(bfsSearchId);
					childArc.getEndVertex().addSearchPreArc(bfsSearchId, childArc);
					childArc.getEndVertex().setDistance(bfsSearchId,
							parent.getDistance(bfsSearchId) + 1);
					queue.addLast(childArc.getEndVertex());
				}
			}
		}

		if (!targetReached)
			return false;

		// Preliminary information is created in the resGraph
		// Now we do one bfs more, this time from the target backwards
		// to generate data for the layer graph at each of its nodes.
		queue.add(this.problem.getTarget());
		this.problem.getTarget().setIncluded(bfsSearchId);

		while (!queue.isEmpty()) {
			MVertex child = queue.removeFirst();
			for (ResArc predArc : child.getSearchPreArcs(bfsSearchId)) {

				if (predArc.getStartVertex().isIncluded(bfsSearchId)) {
					predArc.getStartVertex().addIncidentLevelArcs(bfsSearchId, predArc);
					continue;
				}

				if (predArc.getStartVertex().equals(this.problem.getSource())) {
					if (!this.problem.getSource().isIncluded(bfsSearchId)) {
						this.problem.getSource().setIncluded(bfsSearchId);
					}
					this.problem.getSource().addIncidentLevelArcs(bfsSearchId, predArc);
					continue;
				}
				predArc.getStartVertex().setIncluded(bfsSearchId);
				predArc.getStartVertex().addIncidentLevelArcs(bfsSearchId, predArc);
				queue.addLast(predArc.getStartVertex());
			}
		}
		return true;
	}

	private AugmentingPath dfs(String bfsSearchId) {
		String dfsSearchId = UUID.randomUUID().toString();
		this.problem.getSource().setSeen(dfsSearchId);

		LinkedList<MVertex> stack = new LinkedList<>();
		stack.addLast(this.problem.getSource());

		MVertex nextVertex = null;
		ResArc nextArc = null;

		while (!stack.isEmpty()) {
			nextVertex = stack.getLast();
			nextArc = nextVertex.getNextLevelArc(dfsSearchId, bfsSearchId);
			if (nextArc == null) {
				// No [more] outgoing arc left
				nextVertex.setDfsFinished(dfsSearchId);
				stack.removeLast();
				nextVertex.setDfsRemoved(bfsSearchId);

			} else if (nextArc.getResValue() == 0 || 
					nextArc.getEndVertex().isDfsRemoved(bfsSearchId)) {
				// no res capacity or already removed by going back
				continue;
			}	
			else if (nextArc.getEndVertex().equals(this.problem.getTarget())) {
				if (!this.problem.getTarget().isSeen(dfsSearchId))
					this.problem.getTarget().setSeen(dfsSearchId);
				MVertex v = nextArc.getEndVertex();
				v.setParent(nextArc);
				// Construct and return the augmenting path
				ArrayList<ResArc> arcs = new ArrayList<>();
				int augValue = Integer.MAX_VALUE;
				while (v.getParent() != null) {
					arcs.add((ResArc) v.getParent());
					augValue = Math.min(augValue, ((ResArc) v.getParent()).getResValue());
					v = v.getParent().getStartVertex();
				}
				return new AugmentingPath(arcs, augValue);

			} else if (!nextArc.getEndVertex().isSeen(dfsSearchId)) {
				nextArc.getEndVertex().setSeen(dfsSearchId);
				nextArc.getEndVertex().setParent(nextArc);
				stack.addLast(nextArc.getEndVertex());
			}
		}
		return null;
	}
}
