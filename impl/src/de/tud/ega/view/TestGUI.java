package de.tud.ega.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import de.tud.ega.model.Edge;
import de.tud.ega.model.Graph;
import de.tud.ega.model.Vertex;



/**
 * Class controls the Window for visualization of a list of edges.
 * 
 */
public class TestGUI{

	
	private JFrame frame = new JFrame("Test GUI");
	private GraphPanel graphPanel = new GraphPanel();
	
	/**
	 * Creates a new EdgeDrawer.
	 */
	public TestGUI(Graph graph) {
		this.init();
	}
	
	
	private void init() {
		
		this.frame.getContentPane().add(this.graphPanel, BorderLayout.CENTER);
		
		this.frame.setBounds(50, 50, 750, 500);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Adds an Edge to the current list of edges.
	 * @param edge
	 * 			added edge
	 */
	public void addEdge(Edge edge) {
		this.graphPanel.addVertex(edge);
	}
	
	/**
	 * Sets the current list of edges
	 * @param edges
	 * 			new list of edges
	 */
	public void setEdges(List<Edge> edges) {
		this.graphPanel.setEdges(edges);
	}
	
	/**
	 * Shows a frame for visualization of the list of edges.
	 */
	public void showFrame () {
		this.frame.setVisible(true);
	}

	
	public static void main(String[] args) {		

		//second sample (all)
		Vertex p111 = new Vertex(1,11);
		Vertex p15 = new Vertex(1,5);
		Vertex p51 = new Vertex(5,1);
		Vertex p59 = new Vertex(5,9);
		Vertex p55 = new Vertex(5,5);
		Vertex p711 = new Vertex(7,11);
		Vertex p94 = new Vertex(9,4);
		Vertex p97 = new Vertex(9,7);
		Vertex p113 = new Vertex(11,3);
		Vertex p1111 = new Vertex(11,11);
		Vertex p28 = new Vertex(2,8);
		
		Edge v1=new Edge(p111, p15);
		Edge v2=new Edge(p111, p59);
		Edge v3=new Edge(p111, p711);
		Edge v4=new Edge(p711, p1111);
		Edge v5=new Edge(p711, p97);
		Edge v6=new Edge(p59, p97);
		Edge v7=new Edge(p1111, p97);
		Edge v8=new Edge(p97, p113);
		Edge v9=new Edge(p94, p113);
		Edge v10=new Edge(p94, p97);
		Edge v11=new Edge(p94, p51);
		Edge v12=new Edge(p55, p94);
		Edge v13=new Edge(p55, p97);
		Edge v14=new Edge(p55, p59);
		Edge v15=new Edge(p15, p55);
		Edge v16=new Edge(p51, p55);
		Edge v17=new Edge(p15, p51);
		Edge v18=new Edge(p59, p711);
		Edge v19=new Edge(p15, p59);
		Edge v20 = new Edge(p51, p113);
		Edge v21 = new Edge(p113, p1111);
		
		Edge v22 = new Edge(p28, p15);
		Edge v23 = new Edge(p28, p111);
		Edge v24 = new Edge(p28, p59);
		
		ArrayList<Edge> vertices = new ArrayList<Edge>();
		
		vertices.add(v1); vertices.add(v2); vertices.add(v3); vertices.add(v4); 
		vertices.add(v5); vertices.add(v6); vertices.add(v7); vertices.add(v8); 
		vertices.add(v9); vertices.add(v10); vertices.add(v11); vertices.add(v12); 
		vertices.add(v13); vertices.add(v14); vertices.add(v15); vertices.add(v16);
		vertices.add(v17); vertices.add(v18); vertices.add(v19); vertices.add(v20);
		vertices.add(v21); vertices.add(v22); vertices.add(v23); vertices.add(v24); 
		

		Graph graph = new Graph(null, vertices);
		TestGUI drawer = new TestGUI(graph);
		drawer.showFrame();
		
		//...with sample data "all" - you may also use "edges"
		drawer.setEdges(vertices);
	}
	
	
	
}
