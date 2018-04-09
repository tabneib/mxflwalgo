package de.tud.ega.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import de.tud.ega.model.Arc;

/**
 * JPanel for showing a list of vertices.
 */
public class GraphPanel extends JPanel {

	private static final long serialVersionUID = 1;

	private List<Arc> arcs;
	private int offX = 25;
	private int offY = 25;
	private double scale = 1.;
	
	/**
	 * 
	 */
	private final int ARROW_HEAD_SIZE = 8;
	
	/**
	 * 
	 */
	private final double ARROW_HEAD_POSITION = 0.3;

	

	/**
	 * Creates a new JGraphPanel with a given list of arcs and updates the
	 * scale factor.
	 * 
	 * @param arcs
	 *            list of arcs
	 */
	public GraphPanel(List<Arc> arcs) {
		this.arcs = arcs;
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

		for (Arc v : this.arcs) {
			// Draw the arc
			drawdArrowHead(g, Color.BLACK,
					(int) (v.getStartVertex().x * this.scale + this.offX),
					(int) (v.getStartVertex().y * this.scale + this.offY),
					(int) (v.getEndVertex().x * this.scale + this.offX),
					(int) (v.getEndVertex().y * this.scale + this.offY),
					false);
			g.setColor(Color.BLACK);
			g2d.drawLine(
					(int) (v.getStartVertex().x * this.scale + this.offX),
					(int) (v.getStartVertex().y * this.scale + this.offY),
					(int) (v.getEndVertex().x * this.scale + this.offX),
					(int) (v.getEndVertex().y * this.scale + this.offY));
			
			
			// Draw the points
			g2d.setColor(Color.RED);
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
		Iterator<Arc> iter = this.arcs.iterator();
		double maxX = 0;
		double maxY = 0;

		while (iter.hasNext()) {
			Arc item = iter.next();
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
	 * @param rotate
	 * 			  optional rotation regarding the direction defined by start- & endpoints
	 */
	private void drawdArrowHead(final Graphics g1, Color color, int x1, int y1, int x2,
			int y2, boolean rotate) {
		Graphics2D g = (Graphics2D) g1.create();
		g.setColor(color);
		
		if (rotate) {
			int tmp = x1;
			x1 = x2;
			x2 = tmp;
			tmp = y1;
			y1 = y2;
			y2 = tmp;
		}

		double dx = x2 - x1, dy = y2 - y1;
		double len = Math.sqrt(dx * dx + dy * dy);
		double cos = dx / len;
		double sin = dy / len;
		// shorten the length to make things not overlapping
		x2 = (int) (x2 - len * ARROW_HEAD_POSITION * cos);
		y2 = (int) (y2 - len * ARROW_HEAD_POSITION * sin);
		
		// recalculate based on new length
		dx = x2 - x1;
		dy = y2 - y1;
		len = Math.sqrt(dx * dx + dy * dy);
		
		//g.drawLine(x1, y1, x2, y2);

		double angle = Math.atan2(dy, dx);
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		g.transform(at);

		g.fillPolygon(new int[] { (int) len, (int) len - ARROW_HEAD_SIZE,
				(int) len - ARROW_HEAD_SIZE, (int) len },
				new int[] { 0, -ARROW_HEAD_SIZE/2, ARROW_HEAD_SIZE/2, 0 }, 4);
	}

}
