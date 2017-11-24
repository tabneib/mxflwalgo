package de.tud.ega.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import de.tud.ega.model.Vertex;
import de.tud.ega.model.Graph;
import de.tud.ega.model.Point;



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
	public void addEdge(Vertex edge) {
		this.graphPanel.addVertex(edge);
	}
	
	/**
	 * Sets the current list of edges
	 * @param edges
	 * 			new list of edges
	 */
	public void setEdges(List<Vertex> edges) {
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
		Point p111 = new Point(1,11);
		Point p15 = new Point(1,5);
		Point p51 = new Point(5,1);
		Point p59 = new Point(5,9);
		Point p55 = new Point(5,5);
		Point p711 = new Point(7,11);
		Point p94 = new Point(9,4);
		Point p97 = new Point(9,7);
		Point p113 = new Point(11,3);
		Point p1111 = new Point(11,11);
		Point p28 = new Point(2,8);
		
		Vertex v1=new Vertex(p111, p15);
		Vertex v2=new Vertex(p111, p59);
		Vertex v3=new Vertex(p111, p711);
		Vertex v4=new Vertex(p711, p1111);
		Vertex v5=new Vertex(p711, p97);
		Vertex v6=new Vertex(p59, p97);
		Vertex v7=new Vertex(p1111, p97);
		Vertex v8=new Vertex(p97, p113);
		Vertex v9=new Vertex(p94, p113);
		Vertex v10=new Vertex(p94, p97);
		Vertex v11=new Vertex(p94, p51);
		Vertex v12=new Vertex(p55, p94);
		Vertex v13=new Vertex(p55, p97);
		Vertex v14=new Vertex(p55, p59);
		Vertex v15=new Vertex(p15, p55);
		Vertex v16=new Vertex(p51, p55);
		Vertex v17=new Vertex(p15, p51);
		Vertex v18=new Vertex(p59, p711);
		Vertex v19=new Vertex(p15, p59);
		Vertex v20 = new Vertex(p51, p113);
		Vertex v21 = new Vertex(p113, p1111);
		
		Vertex v22 = new Vertex(p28, p15);
		Vertex v23 = new Vertex(p28, p111);
		Vertex v24 = new Vertex(p28, p59);
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		
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
