package de.nhd.mxflwalgo.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.nhd.mxflwalgo.algos.model.Arc;
import de.nhd.mxflwalgo.algos.model.MArc;
import de.nhd.mxflwalgo.algos.model.MGraph;
import de.nhd.mxflwalgo.algos.model.MVertex;
import de.nhd.mxflwalgo.algos.model.MaxFlowProblem;

/**
 * JPanel for displaying and updating a beautiful graph
 */
public class GraphPanel extends JPanel {

	private static final long serialVersionUID = 1;

	/**
	 * Left horizontal offset of the graph
	 */
	private static final int OFF_X = 25;

	/**
	 * Above vertical offset of the graph
	 */
	private static final int OFF_Y = 25;

	/**
	 * The distance from the end point to the bottom line of the arrow head
	 */
	private static final int ARROW_HEAD_SIZE = 8;

	/**
	 * Relative position of the arrow head on the corresponding line
	 */
	private static final double ARROW_HEAD_POSITION = 0.33;

	/**
	 * The size of the edges of a node
	 */
	private static final int NODE_SIZE = 6;

	/**
	 * Height of the label of an arc in the graph
	 */
	private static final int LABEL_HEIGHT = 12;

	/**
	 * Width of the label of an arc in the graph
	 */
	private static final int LABEL_WIDTH = 25;
	
	/**
	 * Offset from the label to the arc
	 */
	private static final int LABEL_OFFSET = 7;
	
	/**
	 * Threshold that defines small scales.
	 * For small scales no arc label is drawn.
	 * 0.71 = from 56 vertices
	 */
	private static final double SMALL_SCALE = 0.71;

	private ArrayList<Arc> arcs;
	private ArrayList<MVertex> vertices;
	private ArrayList<Line> lines;
	private ArrayList<Node> nodes;
	private HashMap<MVertex, Node> vertexToNode;
	private HashMap<Arc, Line> arcToLine;

	/**
	 * Parameter to adapt the size of the to be drawn graph
	 */
	private double scale = 1.;

	private boolean scaleUpdated;
	private boolean labelInserted;

	private static final Color NODE_HIGHLIGHTED_COLOR = Color.RED;
	private static final Color NODE_UNDEREMPHASIZED_COLOR = Color.GRAY;
	
	private static final Color LINE_NORMAL_COLOR = Color.BLACK;
	private static final Color LINE_HIGHLIGHTED_COLOR = Color.BLACK;
	private static final Color LINE_UNDEREMPHASIZED_COLOR = Color.GRAY;
	
	private static final Color TRANSPARENT_COLOR = new Color(0,0,0,1);
	
	private final MaxFlowProblem maxFlowProblem;

	/**
	 * Creates a new JGraphPanel for the given max flow problem
	 * 
	 * @param arcs
	 *            list of arcs
	 */
	public GraphPanel(MaxFlowProblem problem) {
		this.maxFlowProblem = problem;
		this.arcs = this.maxFlowProblem.getGraph().getArcs();
		this.vertices = this.maxFlowProblem.getGraph().getVertices();
		this.scaleUpdated = false;
		this.labelInserted = false;

		// Create mapping from arcs to lines
		this.lines = new ArrayList<>();
		this.arcToLine = new HashMap<>();
		for (Arc arc : this.arcs) {
			Line l = new Line(arc, arc.getStartVertex().x, arc.getStartVertex().y,
					arc.getEndVertex().x, arc.getEndVertex().y);
			this.lines.add(l);
			arcToLine.put(arc, l);
		}

		// Create mapping from vertices to nodes
		this.nodes = new ArrayList<>();
		this.vertexToNode = new HashMap<>();
		for (MVertex vertex : this.vertices) {
			Node n = new Node(vertex, vertex.x, vertex.y);
			this.nodes.add(n);
			this.vertexToNode.put(vertex, n);
		}

		this.setLayout(null);
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

		for (Arc arc : this.arcs)
			this.drawArc(g2d, arc);

		for (MVertex vertex : this.vertices)
			this.drawVertex(g2d, vertex);
		
		if (this.scale > SMALL_SCALE && !labelInserted)
			this.insertLabels();
	}

	/**
	 * Draw the given arc
	 * 
	 * @param g
	 * @param arc
	 */
	private void drawArc(final Graphics2D g, Arc arc) {
		if (!this.arcs.contains(arc))
			throw new RuntimeException("Cannot draw an unknown arc: " + arc);
		this.arcToLine.get(arc).draw(g);
	}

	/**
	 * Draw the given vertex
	 * 
	 * @param g
	 * @param vertex
	 */
	private void drawVertex(final Graphics2D g, MVertex vertex) {
		if (!this.vertices.contains(vertex))
			throw new RuntimeException("Cannot draw an unknown vertex: " + vertex);
		this.vertexToNode.get(vertex).draw(g);
	}

	/**
	 * Draw an arrow head with the given coordinates of the corresponding line
	 * and color. If the given graphics and color are null, so do not draw anything but 
	 * just calculate the position of the arrow head.
	 * 
	 * @param g
	 * @param color
	 * @param x1
	 *            x-coordinate of the startpoint
	 * @param y1
	 *            y-coordinate of the startpoint
	 * @param x2
	 *            x-coordinate of the endpoint
	 * @param y2
	 *            y-coordinate of the endpoint
	 */
	private Point drawdArrowHead(final Graphics g, Color color, int x1, int y1, int x2,
			int y2) {

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

		double angle = Math.atan2(dy, dx);
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		
		if (g != null && color != null) {
			Graphics2D g2D = (Graphics2D) g.create();
			g2D.setColor(color);
			g2D.transform(at);

			g2D.fillPolygon(
					new int[] { (int) len, (int) len - ARROW_HEAD_SIZE,
							(int) len - ARROW_HEAD_SIZE, (int) len },
					new int[] { 0, -ARROW_HEAD_SIZE / 2, ARROW_HEAD_SIZE / 2, 0 }, 4);
		}
			
		return new Point(x2, y2);
	}

	/**
	 * Adapt the scale to draw the graph according to the far most nodes of the
	 * graph
	 */
	private void updateScale() {
		if (scaleUpdated)
			return;

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

		double scaleX = (this.getWidth() - OFF_X - 10) / (maxX + LABEL_WIDTH + LABEL_OFFSET);
		double scaleY = (this.getHeight() - OFF_Y - 10) / maxY;

		this.scale = Math.min(scaleX, scaleY);
		//System.out.println("[GraphPanel] scale = " + this.scale);
		scaleUpdated = true;
	}

	/**
	 * Insert labels for the arcs of the graph.
	 * We don't do this in paintComponent to prevent infinite loop due to repainting.
	 */
	public void insertLabels() {
		if (!labelInserted) 
			for (Line line : this.lines)
				add(line.getLabel());
		labelInserted = true;
	}
	
	/**
	 * Internal class representing a line in the to be drawn graph
	 *
	 */
	private class Line {
		final int x1;
		final int y1;
		final int x2;
		final int y2;
		final Arc arc;
		JLabel label;

		public Line(Arc arc, int x1, int y1, int x2, int y2) {
			this.arc = arc;
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
		
		/**
		 * Generate the label of this arc
		 */
		private void generateLabel() {

			if (this.arc instanceof MArc)
				this.label = new JLabel(
						((MArc) arc).getFlow() + "|" + ((MArc) arc).getCapacity());
			// else: TODO
			
			// Get the position of the arrow head
			Point arrowHeadPos = drawdArrowHead(null, null, (int) (x1 * scale + OFF_X),
					(int) (y1 * scale + OFF_Y), (int) (x2 * scale + OFF_X),
					(int) (y2 * scale + OFF_Y));

			// No label if small scale
			if (scale <= SMALL_SCALE)
				this.label = null;
			
			// Scale is OK, add the label
			label.setSize(LABEL_WIDTH, LABEL_HEIGHT);
			
			Font labelFont = label.getFont();
			String labelText = label.getText();

			int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
			int componentWidth = label.getWidth();

			// Find out how much the font can grow in width.
			double widthRatio = (double)componentWidth / (double)stringWidth;

			int newFontSize = (int)(labelFont.getSize() * widthRatio);
			int componentHeight = label.getHeight();

			// Pick a new font size so it will not be larger than the height of label.
			int fontSizeToUse = Math.min(newFontSize, componentHeight);

			// Set the label's font size to the newly determined size.
			label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
			
			switch (this.arc.getDirection()) {
			case HORIZONTAL_TO_RIGHT:
				label.setLocation((int) (arrowHeadPos.x - 2 * ARROW_HEAD_SIZE), 
						arrowHeadPos.y - LABEL_HEIGHT - LABEL_OFFSET);
				break;

			case HORIZONTAL_TO_LEFT:
				label.setLocation((int) (arrowHeadPos.x - ARROW_HEAD_SIZE), 
						arrowHeadPos.y - LABEL_HEIGHT - LABEL_OFFSET);
				break;

			case VERTICAL_UP:
				label.setLocation((int) (arrowHeadPos.x + LABEL_OFFSET), 
						arrowHeadPos.y);
				break;
				
			case VERTICAL_DOWN:
				label.setLocation((int) (arrowHeadPos.x + LABEL_OFFSET), 
						(int) (arrowHeadPos.y - 1.2 * ARROW_HEAD_SIZE));
				break;

			case DIAGONAL_TO_BOTTOMRIGHT:
				label.setLocation((int) (arrowHeadPos.x + LABEL_OFFSET - ARROW_HEAD_SIZE * 0.5), 
						arrowHeadPos.y - ARROW_HEAD_SIZE);
				break;
			case DIAGONAL_TO_BOTTOMLEFT:
				label.setLocation((int) (arrowHeadPos.x + LABEL_OFFSET + ARROW_HEAD_SIZE), 
						(int) (arrowHeadPos.y - ARROW_HEAD_SIZE));
				break;
			case DIAGONAL_TO_TOPRIGHT:
				label.setLocation((int) (arrowHeadPos.x + LABEL_OFFSET - 0.5 * ARROW_HEAD_SIZE), 
						arrowHeadPos.y);
				break;
			case DIAGONAL_TO_TOPLEFT:
				label.setLocation((int) (arrowHeadPos.x + LABEL_OFFSET + ARROW_HEAD_SIZE), 
						(int) (arrowHeadPos.y - 0.3 * ARROW_HEAD_SIZE));
				break;
			default:
				throw new RuntimeException("Unkown arc direction!");
			}
		}

		/**
		 * Draw this line, an arrow head and the corresponding label onto the
		 * given graphic
		 * 
		 * @param g
		 */
		public void draw(final Graphics2D g) {

			g.setStroke(new BasicStroke(1));
			// Draw the line
			if (maxFlowProblem.getGraph().highlightMode()) {
				if (!(maxFlowProblem.getGraph().isHighlighted(this.arc))) {
					if ((maxFlowProblem.getGraph().isHighlighted(this.arc.getTwinArc())))
						g.setColor(TRANSPARENT_COLOR);
					else
						g.setColor(LINE_UNDEREMPHASIZED_COLOR);
				}
				else{
					g.setStroke(new BasicStroke(3));
					g.setColor(LINE_HIGHLIGHTED_COLOR);
				}
			}
			else
				g.setColor(LINE_NORMAL_COLOR);

			g.drawLine((int) (x1 * scale + OFF_X), (int) (y1 * scale + OFF_Y),
					(int) (x2 * scale + OFF_X), (int) (y2 * scale + OFF_Y));

			// Draw the arrow head
			drawdArrowHead(g, g.getColor(), (int) (x1 * scale + OFF_X),
					(int) (y1 * scale + OFF_Y), (int) (x2 * scale + OFF_X),
					(int) (y2 * scale + OFF_Y));

		}
		
		public JLabel getLabel() {
			if (this.label == null)
				// Create the label
				generateLabel();
			return this.label;
		}
	}

	/**
	 * Internal class representing a node in the to be drawn graph
	 *
	 */
	private class Node {
		final MVertex vertex;
		final int x;
		final int y;

		public Node(MVertex vertex, int x, int y) {
			this.vertex = vertex;
			this.x = x;
			this.y = y;
		}

		/**
		 * Draw this node onto the given graphic
		 * 
		 * @param g
		 */
		public void draw(final Graphics2D g) {

			// Draw the points
			if (this.vertex.equals(maxFlowProblem.getSource()) ||
					this.vertex.equals(maxFlowProblem.getTarget())) {
				g.setColor(NODE_HIGHLIGHTED_COLOR);
				g.fillOval((int) (x * scale - NODE_SIZE + OFF_X),
						(int) (y * scale - NODE_SIZE + OFF_Y), 2*NODE_SIZE, 2*NODE_SIZE);
			}
			else  {
				if (maxFlowProblem.getGraph().highlightMode() &&
						!(maxFlowProblem.getGraph().isHighlighted(this.vertex)))
					g.setColor(NODE_UNDEREMPHASIZED_COLOR);
				else
					g.setColor(NODE_HIGHLIGHTED_COLOR);
				
				g.fillOval((int) (x * scale - NODE_SIZE / 2 + OFF_X),
						(int) (y * scale - NODE_SIZE / 2 + OFF_Y), NODE_SIZE, NODE_SIZE);
			}
		}
	}
}
