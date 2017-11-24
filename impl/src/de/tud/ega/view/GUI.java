package de.tud.ega.view;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import de.tud.ega.model.Graph;
import de.tud.ega.model.Point;
import de.tud.ega.model.Vertex;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;

	// GUI Constants
	public static final int WINDOW_HEIGHT = 650;
	public static final int BOXES_CONTAINER_WIDTH = 900;
	public static final int MENU_CONTAINER_WIDTH = 380;
	public static final int BOXES_PADDING = 10;
	public static final int SCROLL_VIEW_PADDING = 20;

	// GUI Components
	private JLabel labelStatusBar;
	private JFrame frame;
	private JLabel labelInsGen;
	private JTextField textFieldParams;
	private JButton buttonInsGen;
	private JLabel labelParams;

	private final Font defaultFont = new JLabel().getFont();

	// Data
	Graph mGraph;

	public static void main(String[] args) {
		new GUI();
	}

	public GUI() {
		// Job for the event-dispatching thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initGUI();
			}
		});
	}

	/**
	 * create GUI window and call method to add stuff onto it
	 */
	public void initGUI() {
		frame = new JFrame("EGA GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(
				BOXES_CONTAINER_WIDTH + SCROLL_VIEW_PADDING + MENU_CONTAINER_WIDTH,
				WINDOW_HEIGHT));
		addComponentsToPane(frame.getContentPane());

		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Add components to the GUI window
	 * 
	 * @param pane
	 */
	private void addComponentsToPane(Container pane) {

		// setup status bar
		labelStatusBar = new JLabel("");
		labelStatusBar.setFont(new Font("arial", Font.PLAIN, defaultFont.getSize()));

		pane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;

		// The container of all the boxes
		c.weightx = 1.0;
		c.gridx = 0;
		pane.add(makeGraphContainer(), c);

		// The container of the menu
		c.weightx = 0.5;
		c.gridx = 1;
		pane.add(makeMenuContainer(), c);

		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 2;
		c.ipady = 10;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		pane.add(labelStatusBar, c);
	}

	/**
	 * Create a panel that displays all the boxes
	 * 
	 * @return
	 */
	private Container makeGraphContainer() {

		mGraph = getSampleGraph();
		GraphPanel graphPanel = new GraphPanel(mGraph.getVertices());

		// The whole grid panel is contained inside a scroll pane
		JScrollPane scrollPane = new JScrollPane(graphPanel);
		scrollPane.setPreferredSize(new Dimension(
				BOXES_CONTAINER_WIDTH + SCROLL_VIEW_PADDING, WINDOW_HEIGHT));

		// Update the status bar
		// labelStatusBar.setText(" #Box: " + boxes.size() + " #Rect: "
		// + mInstance.getRechtangles().size() + " boxLength: " + boxLength);

		return scrollPane;
	}

	/**
	 * Create a panel that displays the menu
	 * 
	 * @return
	 */
	private Container makeMenuContainer() {
		JPanel panel = new JPanel();

		panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.ipady = 10;
		c.anchor = GridBagConstraints.NORTHWEST;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		// Dummy label to keep the "width" beast at bay
		JLabel dummy = new JLabel(" ");
		dummy.setPreferredSize(new Dimension(MENU_CONTAINER_WIDTH, 1));
		panel.add(dummy, c);

		// Instance generation

		labelInsGen = new JLabel("Graph Generator");
		labelInsGen.setFont(new Font(defaultFont.getFontName(), Font.BOLD,
				defaultFont.getSize() + 2));
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		panel.add(labelInsGen, c);

		textFieldParams = new JTextField("params");
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		textFieldParams.setPreferredSize(new Dimension(260, 30));
		c.fill = GridBagConstraints.VERTICAL;
		panel.add(textFieldParams, c);
		c.fill = GridBagConstraints.NONE;

		buttonInsGen = new JButton("Generate");
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 1;
		buttonInsGen.setMaximumSize(new Dimension(100, 30));
		panel.add(buttonInsGen, c);

		labelParams = new JLabel("[params]");
		labelParams.setFont(new Font("arial", Font.PLAIN, 11));
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		panel.add(labelParams, c);

		setListeners();
		return panel;
	}

	/**
	 * Setup listeners for the GUI components
	 */
	private void setListeners() {

		// Radio buttons
		buttonInsGen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				javax.swing.SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						Component gContainer = frame.getContentPane().getComponent(0);
						GridBagLayout l = (GridBagLayout) frame.getContentPane()
								.getLayout();
						GridBagConstraints c = l.getConstraints(gContainer);

						frame.getContentPane().remove(gContainer);
						frame.getContentPane().add(makeGraphContainer(), c, 0);
						frame.getContentPane().validate();
					}
				});

			}
		});
	}

	// ---------------------------------------------------------------------------------->
	// Auxiliary
	// <----------------------------------------------------------------------------------

	private Graph getSampleGraph() {
		// second sample (all)
		Point p111 = new Point(1, 11);
		Point p15 = new Point(1, 5);
		Point p51 = new Point(5, 1);
		Point p59 = new Point(5, 9);
		Point p55 = new Point(5, 5);
		Point p711 = new Point(7, 11);
		Point p94 = new Point(9, 4);
		Point p97 = new Point(9, 7);
		Point p113 = new Point(11, 3);
		Point p1111 = new Point(11, 11);
		Point p28 = new Point(2, 8);

		Vertex v1 = new Vertex(p111, p15);
		Vertex v2 = new Vertex(p111, p59);
		Vertex v3 = new Vertex(p111, p711);
		Vertex v4 = new Vertex(p711, p1111);
		Vertex v5 = new Vertex(p711, p97);
		Vertex v6 = new Vertex(p59, p97);
		Vertex v7 = new Vertex(p1111, p97);
		Vertex v8 = new Vertex(p97, p113);
		Vertex v9 = new Vertex(p94, p113);
		Vertex v10 = new Vertex(p94, p97);
		Vertex v11 = new Vertex(p94, p51);
		Vertex v12 = new Vertex(p55, p94);
		Vertex v13 = new Vertex(p55, p97);
		Vertex v14 = new Vertex(p55, p59);
		Vertex v15 = new Vertex(p15, p55);
		Vertex v16 = new Vertex(p51, p55);
		Vertex v17 = new Vertex(p15, p51);
		Vertex v18 = new Vertex(p59, p711);
		Vertex v19 = new Vertex(p15, p59);
		Vertex v20 = new Vertex(p51, p113);
		Vertex v21 = new Vertex(p113, p1111);

		Vertex v22 = new Vertex(p28, p15);
		Vertex v23 = new Vertex(p28, p111);
		Vertex v24 = new Vertex(p28, p59);

		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);
		vertices.add(v4);
		vertices.add(v5);
		vertices.add(v6);
		vertices.add(v7);
		vertices.add(v8);
		vertices.add(v9);
		vertices.add(v10);
		vertices.add(v11);
		vertices.add(v12);
		vertices.add(v13);
		vertices.add(v14);
		vertices.add(v15);
		vertices.add(v16);
		vertices.add(v17);
		vertices.add(v18);
		vertices.add(v19);
		vertices.add(v20);
		vertices.add(v21);
		vertices.add(v22);
		vertices.add(v23);
		vertices.add(v24);

		return new Graph(null, vertices);
	}

}