package de.nhd.mxflwalgo.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.nhd.mxflwalgo.algos.Dinic;
import de.nhd.mxflwalgo.algos.EdmondsKarp;
import de.nhd.mxflwalgo.algos.FordFulkerson;
import de.nhd.mxflwalgo.algos.GoldbergTarjan;
import de.nhd.mxflwalgo.model.Arc;
import de.nhd.mxflwalgo.model.GraphFactory;
import de.nhd.mxflwalgo.model.MArc;
import de.nhd.mxflwalgo.model.MGraph;
import de.nhd.mxflwalgo.model.MVertex;
import de.nhd.mxflwalgo.model.MaxFlowProblem;

public class AlgosTest {

	 @Test
	 public void verySmallGraph() {
	 runAlgos(4, 10);
	 }
	
	 @Test
	 public void smallGraph() {
	 runAlgos(20, 30);
	 }
	
	 @Test
	 public void mediumGraph() {
	 runAlgos(50, 30);
	 }
	
	 @Test
	 public void largeGraph() {
	 runAlgos(1000, 50);
	 }
	
//	 @Test
//	 public void largerGraph() {
//	 runAlgos(4000, 300);
//	 }

//	@Test
//	public void giantGraph() {
//		runAlgos(50000, 300);
//	}

	private void runAlgos(int vertexNumber, int maxCapacity) {
		long startTime = System.currentTimeMillis();
		MGraph graph;
		try {
			graph = GraphFactory.getBeautifulPlanarGraph(vertexNumber, maxCapacity);
			int fordFulkersonFlow;
			int edmondsKarpFlow;
			int dinicFlow;
			int goldbergTarjanFlow;

			MaxFlowProblem problem = new MaxFlowProblem(graph);
			FordFulkerson fordFulkerson = new FordFulkerson(problem);
			fordFulkerson.run();
			fordFulkersonFlow = checkGraph(graph, problem.getSource(),
					problem.getTarget());

			graph.reset();
			EdmondsKarp edmondsKarp = new EdmondsKarp(new MaxFlowProblem(graph));
			edmondsKarp.run();
			edmondsKarpFlow = checkGraph(graph, problem.getSource(), problem.getTarget());

			graph.reset();
			Dinic dinic = new Dinic(new MaxFlowProblem(graph));
			dinic.run();
			dinicFlow = checkGraph(graph, problem.getSource(), problem.getTarget());

			graph.reset();
			GoldbergTarjan goldbergTarjan = new GoldbergTarjan(new MaxFlowProblem(graph));
			goldbergTarjan.run();
			goldbergTarjanFlow = checkGraph(graph, problem.getSource(),
					problem.getTarget());
			assertTrue(fordFulkersonFlow == edmondsKarpFlow
					&& edmondsKarpFlow == dinicFlow && dinicFlow == goldbergTarjanFlow);

			System.out.println(
					"Total run time: " + (System.currentTimeMillis() - startTime) + "ms");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int checkGraph(MGraph graph, MVertex source, MVertex target) {
		for (Arc arc : graph.getArcs()) {
			assertTrue(((MArc) arc).getFlow() <= ((MArc) arc).getCapacity());
		}
		for (MVertex vertex : graph.getVertices()) {
			if (vertex.equals(source) || vertex.equals(target))
				continue;
			int inFlow = 0;
			int outFlow = 0;
			for (MArc inArc : vertex.getInArcs()) {
				inFlow += inArc.getFlow();
			}
			for (MArc outArc : vertex.getIncidentArcs()) {
				outFlow += outArc.getFlow();
			}
			assertTrue(inFlow == outFlow);
		}

		int sourceOutFlow = 0;
		int targetInFlow = 0;

		for (MArc arc : source.getIncidentArcs()) {
			sourceOutFlow += arc.getFlow();
		}

		for (MArc arc : source.getInArcs()) {
			sourceOutFlow -= arc.getFlow();
		}

		for (MArc arc : target.getInArcs()) {
			targetInFlow += arc.getFlow();
		}

		for (MArc arc : target.getIncidentArcs()) {
			targetInFlow -= arc.getFlow();
		}

		if (sourceOutFlow != targetInFlow)
			assertTrue(sourceOutFlow == targetInFlow);

		return sourceOutFlow;
	}
}
