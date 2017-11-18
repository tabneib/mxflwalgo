package de.tud.ega.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import de.tud.ega.model.Edge;
import de.tud.ega.model.Point;

/**
 * JPanel for showing a list of edges.
 */
public class GraphPanel extends JPanel {

	private static final long serialVersionUID = 1;

	private List<Edge> edges;
	private HashSet<Point> points = new HashSet<Point>();

	private int offX = 25;
	private int offY = 25;
	private double scale = 1.;

	/**
	 * Creates a new JGraphPanel without a list of edges. So the list of edges
	 * will hold no elements.
	 */
	public GraphPanel() {
		this.edges = new ArrayList<Edge>();
		this.updatePoints();
		this.init();
	}

	/**
	 * Creates a new JGraphPanel with a given list of edges and updates the
	 * scale factor.
	 * 
	 * @param edges
	 *            list of edges
	 */
	public GraphPanel(List<Edge> edges) {
		this.edges = edges;
		this.updatePoints();
		this.updateScale();

		this.init();
	}

	private void init() {

	}

	/**
	 * {@inheritDoc}
	 */
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		this.updateScale();

		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

		for (Edge edge : this.edges) {
			g2d.setColor(Color.GRAY);
			g2d.drawLine((int) (edge.getP1().x * this.scale + this.offX),
					(int) (edge.getP1().y * this.scale + this.offY),
					(int) (edge.getP2().x * this.scale + this.offX),
					(int) (edge.getP2().y * this.scale + this.offY));

			g2d.setColor(Color.BLACK);
			g2d.fillOval((int) (edge.getP1().x * this.scale - 3 + this.offX),
					(int) (edge.getP1().y * this.scale - 3 + this.offY), 6, 6);
			g2d.fillOval((int) (edge.getP2().x * this.scale - 3 + this.offX),
					(int) (edge.getP2().y * this.scale - 3 + this.offY), 6, 6);

		}

	}

	/**
	 * Method adds an edge to the maintained list, updates the scale factor and
	 * repaints the JPanel.
	 * 
	 * @param edge
	 *            added Edge to list
	 */
	public void addEdge(Edge edge) {
		this.edges.add(edge);
		this.updatePoints();
		this.updateScale();
		this.repaint();
	}

	/**
	 * Method sets the maintained list, updates the scale facor and repaints the
	 * JPanel.
	 * 
	 * @param edges
	 *            new list of edges
	 */
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
		this.updateScale();
		this.updatePoints();
		this.repaint();

	}

	private void updatePoints() {
		this.points.clear();// delete all points

		for (Edge e : this.edges) {
			this.points.add(e.getP1());
			this.points.add(e.getP2());
		}
	}

	private void updateScale() {
		Iterator<Edge> it = this.edges.iterator();
		double maxX = 0;
		double maxY = 0;

		while (it.hasNext()) {
			Edge item = it.next();
			maxX = Math.max(item.getP1().x, maxX);
			maxX = Math.max(item.getP2().x, maxX);

			maxY = Math.max(item.getP1().y, maxY);
			maxY = Math.max(item.getP2().y, maxY);
		}

		double scaleX = (this.getWidth() - this.offX - 10) / maxX;
		double scaleY = (this.getHeight() - this.offY - 10) / maxY;

		this.scale = Math.min(scaleX, scaleY);
	}

}
