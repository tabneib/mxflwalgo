package de.nhd.mxflwalgo.algos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

import de.nhd.mxflwalgo.model.MGraph;
import de.nhd.mxflwalgo.model.MVertex;
import de.nhd.mxflwalgo.model.MaxFlowProblem;
import de.nhd.mxflwalgo.model.ResArc;

public class EdmondsKarp extends MaxFlowAlgo {

	public EdmondsKarp(MaxFlowProblem maxFlowProblem) {
		super(maxFlowProblem);
	}

	@Override
	public MGraph run() {
		while (true) {
			AugmentingPath augPath = bfs();
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
		AugmentingPath augPath = bfs();
		if (augPath == null){
			this.problem.getGraph().clearAllHighlight();
			this.finished = true;
			return this.problem.getGraph();
		}
		highlightAugPath(augPath, true);
		updateGraphs(augPath);
		return this.problem.getGraph();
	}

	/**
	 * Breadth First Search
	 * 
	 * @return
	 */
	private AugmentingPath bfs() {
		String bfsSearchId = UUID.randomUUID().toString();
		this.problem.getSource().setSeen(bfsSearchId);
		
		LinkedList<MVertex> queue = new LinkedList<>();
		queue.addFirst(this.problem.getSource());
		
		while (!queue.isEmpty()) {
			MVertex parent = queue.removeFirst();
			for (ResArc childArc : parent.getIncidentResArcs()) {
				
				if (childArc.getEndVertex().isSeen(bfsSearchId) || 
						childArc.getResValue() == 0)
					continue;
				
				if (childArc.getEndVertex().equals(problem.getTarget())) {
					// create augPath and return.
					ArrayList<ResArc> arcs = new ArrayList<>();
					int augValue = Integer.MAX_VALUE;
					
					MVertex v = childArc.getEndVertex();
					v.setParent(childArc);
					
					while (v.getParent() != null) {
						arcs.add((ResArc) v.getParent());
						augValue = Math.min(augValue, ((ResArc) v.getParent()).getResValue());
						v = v.getParent().getStartVertex();
					}
					return new AugmentingPath(arcs, augValue);
				}
				childArc.getEndVertex().setSeen(bfsSearchId);
				childArc.getEndVertex().setParent(childArc);
				queue.addLast(childArc.getEndVertex());
			}
		}
		return null;
	}
}
