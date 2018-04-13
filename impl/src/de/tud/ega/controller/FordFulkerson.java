package de.tud.ega.controller;

import java.util.ArrayList;

import de.tud.ega.model.Arc;
import de.tud.ega.model.MArc;
import de.tud.ega.model.MGraph;
import de.tud.ega.model.MVertex;
import de.tud.ega.model.MaxFlowProblem;
import de.tud.ega.model.ResArc;

public class FordFulkerson extends MaxFlowAlgo{
	
	private MGraph resGraph;

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
		ResArc a;
		for (Arc arc : this.problem.getGraph().getArcs()) {
			MArc mArc = (MArc) arc;
			if (mArc.getFlow() == 0){
				a = new ResArc(mArc.getStartVertex(), mArc.getEndVertex(), mArc.getCapacity(), 
						null, mArc);
				arcs.add(a);
				mArc.getStartVertex().addResIncidentArc(a);
			}
			else if (mArc.getFlow() == mArc.getCapacity()){
				a = new ResArc(mArc.getEndVertex(), mArc.getStartVertex(), mArc.getCapacity(),
						null, mArc);
				arcs.add(a);
				mArc.getEndVertex().addResIncidentArc(a);
			}
			else{
				ResArc forward = new ResArc(mArc.getStartVertex(), mArc.getEndVertex(), 
						mArc.getCapacity() - mArc.getFlow(), null, mArc);
				
				
				ResArc backward = new ResArc(mArc.getEndVertex(), mArc.getStartVertex(), 
						mArc.getFlow(), forward, mArc);
				forward.setBackwardResArc(backward);
				arcs.add(forward);
				arcs.add(backward);
				mArc.getStartVertex().addResIncidentArc(forward);
				mArc.getEndVertex().addResIncidentArc(backward);
			}
		}
		this.resGraph = new MGraph(this.problem.getGraph().getVertices(), arcs);
	}
	
	/**
	 * Update the residual graph along a graph augmenting path
	 * @param augPath
	 * 				The given graph augmenting path
	 */
	private void updateResGraph(AugmentingPath augPath) {
		for (ResArc arc : augPath.arcs) {
			if (arc.getResValue() > augPath.value) {
				arc.setResValue(arc.getResValue() - augPath.value);
				// Update the backward arc
				arc.getBackwardResArc().setResValue(
					arc.getBackwardResArc().getResValue() + augPath.value);
			}
			else if (arc.getResValue() == augPath.value) {
				arc.getStartVertex().removeResIncidentArc(arc);
					// Update the backward arc
				arc.getBackwardResArc().setResValue(arc.getResValue() + augPath.value);
				this.resGraph.getArcs().remove(arc);
			}
			else
				throw new RuntimeException("Invalid augmenting value!");
		}
	}
	

	/**
	 * Update the graph along a graph augmenting path
	 * @param path
	 * 				The given graph augmenting path
	 */
	private void updateGraph(AugmentingPath path) {
		for (ResArc arc : path.arcs) 
			arc.getOriginalArc().setFlow(arc.getOriginalArc().getFlow() + path.value);
	}
	
	
	/**
	 * Depth First Search
	 * @return
	 */
	private AugmentingPath dfs() {
		long searchId = System.currentTimeMillis();
		this.problem.getSource().setSeen(searchId);
		return recDfs(this.resGraph.getVertices().get(0), new ArrayList<ResArc>(), 0, searchId);
	
	}
	/**
	 * 
	 * @param currentNode
	 * 						The node that the DFS reaches in this round
	 * @param currentPath
	 * 						The path from source to (including) the current node
	 * @param augmentingValue
	 * 						The current minimal possible flow augmenting value
	 * @param searchId
	 * 						Id of the current DFS
	 * @return
	 */
	private AugmentingPath recDfs(MVertex currentNode, ArrayList<ResArc> currentPath, 
			int augmentingValue, long searchId) {
		for (ResArc arc : currentNode.getResIncidentArcs()) {
			if (arc.getEndVertex().equals(this.problem.getTarget())) {
				currentPath.add(arc);
				return new AugmentingPath(currentPath, 
						Math.min(augmentingValue, arc.getResValue()));
			}
			else if (!arc.getEndVertex().isSeen(searchId)) {
				arc.getEndVertex().setSeen(searchId);
				currentPath.add(arc);
				AugmentingPath res = recDfs(arc.getEndVertex(), 
						currentPath, Math.min(augmentingValue, arc.getResValue()), searchId);
				if (res != null)
					// Along this branch there is a result
					return res;
				// else: continue searching along other branches
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
	}

}
