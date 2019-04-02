package de.nhd.mxflwalgo.algos;

import java.awt.Color;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
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
			if (v1.getHeight() > v2.getHeight())
				return 1;
			else if (v1.getHeight() == v2.getHeight()) {
				if (v1.hashCode() > v2.hashCode())
					return 1;
				else
					return -1;
			} else
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
						&& currentArc.getEndVertex().getExcess() == 0) 
					this.activeVertices.add(currentArc.getEndVertex());
				
				currentArc.pushFlow();
				highlightPushingArc(currentArc);
				
				if (highestVertex.getExcess() == 0) 
					this.activeVertices.remove(highestVertex);
			} else {
				// Relabel
				// Bug fixed: we have to remove and re-add the vertex since its
				// height attribute is modified and the comparator depends on
				// height
				this.activeVertices.remove(highestVertex);
				highestVertex.setHeight(highestVertex.getMinIncidentResArcHeight() + 1);
				highestVertex.resetCurrentResArc();
				highlightRelabelingVertex(highestVertex);
				this.activeVertices.add(highestVertex);
			}
		} else
			this.finished = true;
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

	/**
	 * Highlight the given arc with different color but still highlight all
	 * already highlighted arcs with default highlight color
	 * 
	 * @param resArc
	 */
	protected void highlightPushingArc(ResArc resArc) {

		Iterator<Arc> iterator = problem.getGraph().getHightlightedArcs().keySet()
				.iterator();
		while (iterator.hasNext()) {
			Arc arc = iterator.next();
			if (((MArc) arc).getFlow() <= 0) {
				iterator.remove();
				problem.getGraph().getHightlightedVertices().remove(arc.getStartVertex());
				problem.getGraph().getHightlightedVertices().remove(arc.getEndVertex());
			} else {
				this.problem.getGraph().highlightArc(arc, null);
				this.problem.getGraph().hightlightVertex(arc.getStartVertex(),
						new Color(188, 103, 103));
				this.problem.getGraph().hightlightVertex(arc.getEndVertex(),
						new Color(188, 103, 103));
			}
			arc.setzIndex(0);
		}

		this.problem.getGraph().highlightArc(resArc.getOriginalArc(),
				new Color(13, 165, 46));
		this.problem.getGraph().hightlightVertex(resArc.getStartVertex(),
				new Color(83, 178, 105));
		this.problem.getGraph().hightlightVertex(resArc.getEndVertex(),
				new Color(83, 178, 105));
		resArc.getOriginalArc().setzIndex(100);
	}

	private void highlightRelabelingVertex(MVertex vertex) {
		this.problem.getGraph().hightlightVertex(vertex, new Color(255, 231, 22));
	}

	@Override
	public void reset() {
		super.reset();
		this.isInitialized = false;
		this.activeVertices = new TreeSet<>(comparator);
	}

}
