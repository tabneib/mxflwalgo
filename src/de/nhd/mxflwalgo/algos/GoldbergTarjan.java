package de.nhd.mxflwalgo.algos;

import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

import de.nhd.mxflwalgo.model.Arc;
import de.nhd.mxflwalgo.model.MArc;
import de.nhd.mxflwalgo.model.MGraph;
import de.nhd.mxflwalgo.model.MVertex;
import de.nhd.mxflwalgo.model.MaxFlowProblem;
import de.nhd.mxflwalgo.model.ResArc;

public class GoldbergTarjan extends MaxFlowAlgo {

	private boolean isInitialized = false;
	private TreeSet<MVertex> activeVertices;
	private static final Comparator<MVertex> comparator = new Comparator<MVertex>() {

		@Override
		public int compare(MVertex v1, MVertex v2) {
			if (v1.equals(v2))
				return 0;
			if (v1.getHeight() >= v2.getHeight())
				return 1;
			else
				return -1;
		}
	};

	public GoldbergTarjan(MaxFlowProblem maxFlowProblem) {
		super(maxFlowProblem);
		this.activeVertices = new TreeSet<>(comparator);
	}

	@Override
	public MGraph run() {
		this.initialize();

		while (!this.activeVertices.isEmpty()) {
			MVertex highestVertex = this.activeVertices.last();
			ResArc currentArc = highestVertex.getCurrentResArc();
			while (currentArc != null
					&& (!currentArc.isAdmissible() || currentArc.getResValue() <= 0))
				currentArc = highestVertex.getCurrentResArc();
			if (currentArc != null && currentArc.isAdmissible()) {
				// Push
				if (!currentArc.getEndVertex().equals(this.problem.getSource())
						&& !currentArc.getEndVertex().equals(this.problem.getTarget())
						&& currentArc.getEndVertex().getExcess() == 0) {
					this.activeVertices.add(currentArc.getEndVertex());
				}
				currentArc.pushFlow();

				if (highestVertex.getExcess() == 0) {
					this.activeVertices.remove(highestVertex);
				}
			} else {
				// Relabel
				highestVertex.setHeight(highestVertex.getMinIncidentResArcHeight() + 1);
				highestVertex.resetCurrentResArc();
			}
		}
		this.problem.getGraph().clearAllHighlight();
		this.finished = true;
		return this.problem.getGraph();
	}

	@Override
	public MGraph runStep() {
		
		if (!this.isInitialized) {
			highlightArcs(this.initialize(), true);
			return this.problem.getGraph();
		}

		if (!this.activeVertices.isEmpty()) {
			MVertex highestVertex = this.activeVertices.last();
			ResArc currentArc = highestVertex.getCurrentResArc();
			while (currentArc != null
					&& (!currentArc.isAdmissible() || currentArc.getResValue() <= 0))
				currentArc = highestVertex.getCurrentResArc();
			if (currentArc != null && currentArc.isAdmissible()) {
				// Push
				if (!currentArc.getEndVertex().equals(this.problem.getSource())
						&& !currentArc.getEndVertex().equals(this.problem.getTarget())
						&& currentArc.getEndVertex().getExcess() == 0) {
					this.activeVertices.add(currentArc.getEndVertex());
				}
				currentArc.pushFlow();
				highlightNextArc(currentArc);
				if (highestVertex.getExcess() == 0) {
					this.activeVertices.remove(highestVertex);
				}
			} else {
				// Relabel
				highestVertex.setHeight(highestVertex.getMinIncidentResArcHeight() + 1);
				highestVertex.resetCurrentResArc();
			}
		} else
			this.finished = true;

		// highlightAugPath(augPath, true);
		// updateResGraph(augPath);
		// updateGraph(augPath);
		return this.problem.getGraph();
	}

	/**
	 * Set up the induction basis for the algorithm
	 */
	private HashSet<Arc> initialize() {
		HashSet<Arc> pushedArcs = new HashSet<>();
		this.problem.getSource().setHeight(this.problem.getGraph().getVertices().size());
		for (ResArc arc : this.problem.getSource().getIncidentResArcs()) {
			if (arc.getResValue() > 0) {
				arc.pushFlow();
				this.activeVertices.add(arc.getEndVertex());
				pushedArcs.add(arc);
			}
		}
		this.isInitialized = true;
		return pushedArcs;
	}

	@Override
	public void reset(){
		super.reset();
		this.isInitialized = false;
		this.activeVertices = new TreeSet<>(comparator);
	}

}
