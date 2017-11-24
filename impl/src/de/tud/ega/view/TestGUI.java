package de.tud.ega.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import de.tud.ega.model.Vertex;
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
	public void addEdge(Vertex edge) {
		this.pnlEdges.addVertex(edge);
	}
	
	/**
	 * Sets the current list of edges
	 * @param edges
	 * 			new list of edges
	 */
	public void setEdges(List<Vertex> edges) {
		this.pnlEdges.setEdges(edges);
	}
	
	/**
	 * Shows a frame for visualization of the list of edges.
	 */
	public void showFrame () {
		this.frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		TestGUI drawer = new TestGUI();

		drawer.init();
		drawer.showFrame();
	}
	
	
	public static void main(String[] args) {		
		//SAMPLE DATA

		//first sample (edges) - not a complete triangulation
		List<Vertex> edges = new ArrayList<Vertex>();
		edges.add(new Vertex(new Point(10,100), new Point(50,50)));
		edges.add(new Vertex(new Point(100,100), new Point(50,50)));
		edges.add(new Vertex(new Point(300,100), new Point(50,50)));
		edges.add(new Vertex(new Point(45,500), new Point(50,50)));
		edges.add(new Vertex(new Point(45,100), new Point(50,50)));
		edges.add(new Vertex(new Point(75,350), new Point(50,50)));
		
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
		
		Vertex e1=new Vertex(p111, p15);
		Vertex e2=new Vertex(p111, p59);
		Vertex e3=new Vertex(p111, p711);
		Vertex e4=new Vertex(p711, p1111);
		Vertex e5=new Vertex(p711, p97);
		Vertex e6=new Vertex(p59, p97);
		Vertex e7=new Vertex(p1111, p97);
		Vertex e8=new Vertex(p97, p113);
		Vertex e9=new Vertex(p94, p113);
		Vertex e10=new Vertex(p94, p97);
		Vertex e11=new Vertex(p94, p51);
		Vertex e12=new Vertex(p55, p94);
		Vertex e13=new Vertex(p55, p97);
		Vertex e14=new Vertex(p55, p59);
		Vertex e15=new Vertex(p15, p55);
		Vertex e16=new Vertex(p51, p55);
		Vertex e17=new Vertex(p15, p51);
		Vertex e18=new Vertex(p59, p711);
		Vertex e19=new Vertex(p15, p59);
		Vertex e20 = new Vertex(p51, p113);
		Vertex e21 = new Vertex(p113, p1111);
		
		Vertex e22 = new Vertex(p28, p15);
		Vertex e23 = new Vertex(p28, p111);
		Vertex e24 = new Vertex(p28, p59);
		
		List<Vertex> all = new LinkedList<Vertex>();
		
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
