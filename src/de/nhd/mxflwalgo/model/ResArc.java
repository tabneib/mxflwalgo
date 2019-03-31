package de.nhd.mxflwalgo.algos.model;

public class ResArc extends Arc {

	/**
	 * The residual flow value of this arc
	 */
	private int resValue;

	/**
	 * The corresponding backward residual arc
	 */
	private ResArc backwardResArc;

	/**
	 * The corresponding arc on the original graph
	 */
	private Arc originalArc;
	
	/**
	 * If this residual arc is forward or backward regarding the original arc
	 */
	private boolean isForward;

	public ResArc(MVertex startVertex, MVertex endVertex, int resValue,
			ResArc backwardArc, Arc originalArc, boolean isForward) {
		super(startVertex, endVertex);
		this.resValue = resValue;
		this.backwardResArc = backwardArc;
		this.originalArc = originalArc;
		this.isForward = isForward;
	}

	public int getResValue() {
		return resValue;
	}

	public void setResValue(int resValue) {
		this.resValue = resValue;
	}

	public ResArc getBackwardResArc() {
		return this.backwardResArc;
	}

	public void setBackwardResArc(ResArc backwardResArc) {
		this.backwardResArc = backwardResArc;
	}

	public Arc getOriginalArc() {
		return originalArc;
	}

	public boolean isForward() {
		return this.isForward;
	}
}
