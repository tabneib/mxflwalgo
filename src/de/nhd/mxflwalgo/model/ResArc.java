package de.nhd.mxflwalgo.model;

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

	/**
	 * Add the given flow value onto this residual arc. The flow value of the
	 * original arc and the residual value of the corresponding residual arc are
	 * updated accordingly.
	 * 
	 * @param flowValue
	 *            the flow value to add
	 */
	public void addFlow(int flowValue) {
		if (this.resValue >= flowValue) {
			this.resValue -= flowValue;
			this.backwardResArc
					.setResValue(this.backwardResArc.getResValue() + flowValue);
			((MArc) this.originalArc).setFlow(this.isForward
					? ((MArc) this.originalArc).getFlow() + flowValue
					: ((MArc) this.originalArc).getFlow() - flowValue);
		} else
			throw new RuntimeException(
					"Flow value to add too large: " + flowValue + " > " + this.resValue);
	}

	/**
	 * Push the largest flow value onto this arc and update all related arcs and
	 * vertices accordingly
	 */
	public void pushFlow() {
		int flowValue = Math.min(this.startVertex.getExcess(), this.resValue);
		if (flowValue <= 0)
			throw new RuntimeException("There is nothing to push! Arc: " + this + " : "
					+ this.startVertex.getExcess() + "-" + this.resValue + "->"
					+ this.endVertex.getExcess());
		this.addFlow(flowValue);
		this.endVertex.setExcess(this.endVertex.getExcess() + flowValue);
		this.startVertex.setExcess(this.startVertex.getExcess() - flowValue);
	}

	public boolean isAdmissible() {
		return this.startVertex.getHeight() == (this.endVertex.getHeight() + 1);
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

	/**
	 * Reset all calculated stuffs
	 */
	public void reset() {
		this.resValue = this.isForward ? ((MArc) originalArc).getCapacity() : 0;
		
	}
}
