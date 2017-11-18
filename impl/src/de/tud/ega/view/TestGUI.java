package de.tud.ega.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import de.tud.ega.model.Edge;
import de.tud.ega.model.Point;



/**
 * Class controls the Window for visualization of a list of edges.
 * 
 */
public class TestGUI implements ActionListener {

	
	private JFrame frame = new JFrame("Test GUI");
	private GraphPanel pnlEdges = new GraphPanel();
	
	/**
	 * Creates a new EdgeDrawer.
	 */
	public TestGUI() {
		this.init();
	}
	
	
	private void init() {
		
		this.frame.getContentPane().add(this.pnlEdges, BorderLayout.CENTER);
		
		this.frame.setBounds(50, 50, 750, 500);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Adds an Edge to the current list of edges.
	 * @param edge
	 * 			added edge
	 */
	public void addEdge(Edge edge) {
		this.pnlEdges.addEdge(edge);
	}
	
	/**
	 * Sets the current list of edges
	 * @param edges
	 * 			new list of edges
	 */
	public void setEdges(List<Edge> edges) {
		this.pnlEdges.setEdges(edges);
	}
	
	/**
	 * Shows a frame for visualization of the list of edges.
	 */
	public void showFrame () {
		this.frame.setVisible(true);
	}
	
	public void createTriangulation() {

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		TestGUI drawer = new TestGUI();

		drawer.createTriangulation();
		drawer.init();
		drawer.showFrame();
	}
	
	
	public static void main(String[] args) {		
		//SAMPLE DATA

		//first sample (edges) - not a complete triangulation
		List<Edge> edges = new ArrayList<Edge>();
		edges.add(new Edge(new Point(10,100), new Point(50,50)));
		edges.add(new Edge(new Point(100,100), new Point(50,50)));
		edges.add(new Edge(new Point(300,100), new Point(50,50)));
		edges.add(new Edge(new Point(45,500), new Point(50,50)));
		edges.add(new Edge(new Point(45,100), new Point(50,50)));
		edges.add(new Edge(new Point(75,350), new Point(50,50)));
		
		//--------------------------------------------------------------------
		
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
		
		Edge e1=new Edge(p111, p15);
		Edge e2=new Edge(p111, p59);
		Edge e3=new Edge(p111, p711);
		Edge e4=new Edge(p711, p1111);
		Edge e5=new Edge(p711, p97);
		Edge e6=new Edge(p59, p97);
		Edge e7=new Edge(p1111, p97);
		Edge e8=new Edge(p97, p113);
		Edge e9=new Edge(p94, p113);
		Edge e10=new Edge(p94, p97);
		Edge e11=new Edge(p94, p51);
		Edge e12=new Edge(p55, p94);
		Edge e13=new Edge(p55, p97);
		Edge e14=new Edge(p55, p59);
		Edge e15=new Edge(p15, p55);
		Edge e16=new Edge(p51, p55);
		Edge e17=new Edge(p15, p51);
		Edge e18=new Edge(p59, p711);
		Edge e19=new Edge(p15, p59);
		Edge e20 = new Edge(p51, p113);
		Edge e21 = new Edge(p113, p1111);
		
		Edge e22 = new Edge(p28, p15);
		Edge e23 = new Edge(p28, p111);
		Edge e24 = new Edge(p28, p59);
		
		List<Edge> all = new LinkedList<Edge>();
		
		all.add(e1); all.add(e2); all.add(e3); all.add(e4); all.add(e5); all.add(e6);
		all.add(e7); all.add(e8); all.add(e9); all.add(e10); all.add(e11); all.add(e12); 
		all.add(e13); all.add(e14); all.add(e15); all.add(e16); all.add(e17); all.add(e18);
		all.add(e19); all.add(e20); all.add(e21); all.add(e22); all.add(e23); all.add(e24); 
		
		
		//=================================================================================
		
		//Call edge drawer...
		TestGUI drawer = new TestGUI();
		drawer.showFrame();
		
		//...with sample data "all" - you may also use "edges"
		drawer.setEdges(all);
	}
	
	
	
}
