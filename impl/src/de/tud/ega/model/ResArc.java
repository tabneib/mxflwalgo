package de.tud.ega.model;

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
	private MArc originalArc;

	public ResArc(MVertex startVertex, MVertex endVertex, int resValue,
			ResArc backwardArc, MArc originalArc) {
		super(startVertex, endVertex);
		this.resValue = resValue;
		this.backwardResArc = backwardArc;
		this.originalArc = originalArc;
	}

	public int getResValue() {
		return resValue;
	}

	public void setResValue(int resValue) {
		this.resValue = resValue;
	}

	public ResArc getBackwardResArc() {
		return backwardResArc;
	}

	public void setBackwardResArc(ResArc backwardResArc) {
		this.backwardResArc = backwardResArc;
	}

	public MArc getOriginalArc() {
		return originalArc;
	}

}
