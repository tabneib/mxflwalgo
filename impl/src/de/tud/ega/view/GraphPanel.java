package de.tud.ega.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import de.tud.ega.model.Edge;
import de.tud.ega.model.Vertex;

/**
 * JPanel for showing a list of vertices.
 */
public class GraphPanel extends JPanel {

	private static final long serialVersionUID = 1;

	private List<Edge> vertices;
	private int offX = 25;
	private int offY = 25;
	private double scale = 1.;
	private final int ARROW_SIZE = 5;

	

	/**
	 * Creates a new JGraphPanel with a given list of edges and updates the
	 * scale factor.
	 * 
	 * @param edges
	 *            list of edges
	 */
	public GraphPanel(List<Edge> edges) {
		this.vertices = edges;
		this.updateScale();
	}

	/**
	 * {@inheritDoc}
	 */
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		this.updateScale();

		Graphics2D g2d = (Graphics2D) g;

		// Draw the white background
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

		for (Edge v : this.vertices) {

			// Draw the edge
			/*drawdArrow(g, Color.GRAY,
					(int) (v.getStartVertex().x * this.scale + this.offX),
					(int) (v.getStartVertex().y * this.scale + this.offY),
					(int) (v.getEndVertex().x * this.scale + this.offX),
					(int) (v.getEndVertex().y * this.scale + this.offY));*/
			g.setColor(Color.GRAY);
			g2d.drawLine(
					(int) (v.getStartVertex().x * this.scale + this.offX),
					(int) (v.getStartVertex().y * this.scale + this.offY),
					(int) (v.getEndVertex().x * this.scale + this.offX),
					(int) (v.getEndVertex().y * this.scale + this.offY));
			
			
			// Draw the points
			g2d.setColor(Color.BLACK);
			g2d.fillOval((int) (v.getStartVertex().x * this.scale - 3 + this.offX),
					(int) (v.getStartVertex().y * this.scale - 3 + this.offY), 6, 6);
			g2d.fillOval((int) (v.getEndVertex().x * this.scale - 3 + this.offX),
					(int) (v.getEndVertex().y * this.scale - 3 + this.offY), 6, 6);
		}
	}

	/**
	 * 
	 */
	private void updateScale() {
		Iterator<Edge> iter = this.vertices.iterator();
		double maxX = 0;
		double maxY = 0;

		while (iter.hasNext()) {
			Edge item = iter.next();
			maxX = Math.max(item.getStartVertex().x, maxX);
			maxX = Math.max(item.getEndVertex().x, maxX);

			maxY = Math.max(item.getStartVertex().y, maxY);
			maxY = Math.max(item.getEndVertex().y, maxY);
		}

		double scaleX = (this.getWidth() - this.offX - 10) / maxX;
		double scaleY = (this.getHeight() - this.offY - 10) / maxY;

		this.scale = Math.min(scaleX, scaleY);
	}

	/**
	 * Draw an arrow with the given coordinates and color
	 * @param g1
	 * @param x1
	 *            x-coordinate of the startpoint
	 * @param y1
	 *            y-coordinate of the startpoint
	 * @param x2
	 *            x-coordinate of the endpoint
	 * @param y2
	 *            y-coordinate of the endpoint
	 */
	@SuppressWarnings("unused")
	private void drawdArrow(final Graphics g1, Color color, int x1, int y1, int x2,
			int y2) {
		Graphics2D g = (Graphics2D) g1.create();
		g.setColor(color);

		double dx = x2 - x1, dy = y2 - y1;
		double len = Math.sqrt(dx * dx + dy * dy);
		double cos = dx / len;
		double sin = dy / len;
		// shorten the length to make things not overlapping
		x2 = (int) (x2 - ARROW_SIZE * cos);
		y2 = (int) (y2 - ARROW_SIZE * sin);
		
		// recalculate based on new length
		dx = x2 - x1;
		dy = y2 - y1;
		len = Math.sqrt(dx * dx + dy * dy);
		
		g.drawLine(x1, y1, x2, y2);

		double angle = Math.atan2(dy, dx);
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		g.transform(at);

		g.fillPolygon(new int[] { (int) len, (int) len - ARROW_SIZE,
				(int) len - ARROW_SIZE, (int) len },
				new int[] { 0, -ARROW_SIZE, ARROW_SIZE, 0 }, 4);
	}

}
