package ega;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomEuclideanGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;



class TestGUI {
	
	// API: https://data.graphstream-project.org/api/gs-core/current/
	// Problem 1: Performance ??
	// Problem 2: The provided data structures (node,edge,graph,...)do not fit our project
	// Problem 3: Too complicated, too many unnecessary features that we don't need for our project
	// => This is just for fun, we should implement our own GUI to display the graph
	// What do we need as input for the graph: 
	// + (as simple as possible) Data structures for node, edge and graph
	// +  A graph generator 
	
	
	public static void main(String[] args) {
		

				
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				
				Graph graph = new SingleGraph("random euclidean");
			    Generator gen = new RandomEuclideanGenerator();
			    
			    gen.addSink(graph);
			    gen.begin();
			    for(int i=0; i<500; i++) {
			            gen.nextEvents();
			    }
			    gen.end();
				
				System.out.println(graph.getNode(32).getAttribute("x"));
				
				Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

				View view = viewer.addDefaultView(false);   // false indicates "no JFrame".

				
				
				JFrame frame = new JFrame("Test GUI");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLayout(new BorderLayout());
				frame.add((ViewPanel) view, BorderLayout.CENTER);
				frame.setPreferredSize(new Dimension(800, 800));
				
				//Container pane = frame.getContentPane();
				//pane.setLayout(new GridLayout(1, 1));
				//pane.add((Container)view);
				
				frame.pack();
				frame.setVisible(true);
				
				 
				
			}
		});
		
	}
	
	
	
	
}

