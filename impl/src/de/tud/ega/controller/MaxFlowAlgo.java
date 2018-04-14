package de.tud.ega.controller;

import de.tud.ega.model.MGraph;
import de.tud.ega.model.MaxFlowProblem;

public abstract class MaxFlowAlgo {

	protected final MaxFlowProblem problem;
	protected boolean finished = false;
	
	public MaxFlowAlgo(MaxFlowProblem maxFlowProblem) {
		this.problem = maxFlowProblem;
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
}
