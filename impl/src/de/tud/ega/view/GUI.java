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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import de.tud.ega.model.Graph;
import de.tud.ega.model.GraphFactory;
import de.tud.ega.model.Vertex;
import de.tud.ega.model.Edge;

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
	private static final int DEFAULT_VERTEX_NUMBER = 100;
	private static final int DEFAULT_MAX_CAPACITY = 10;
	private int vertexNumber = DEFAULT_VERTEX_NUMBER;
	private int maxCapacity = DEFAULT_MAX_CAPACITY;

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

		//mGraph = getSampleGraph();
		mGraph = GraphFactory.getPlanarGraph(vertexNumber, maxCapacity);
		GraphPanel graphPanel = new GraphPanel(mGraph.getEdges());

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

		textFieldParams = new JTextField(vertexNumber + " " + maxCapacity);
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

		labelParams = new JLabel("<Vertices Number> <Max Capacity>");
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

		buttonInsGen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Parse arguments
				final String[] argStrs = textFieldParams.getText().split(" ");

				javax.swing.SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						
						try {
							if (argStrs.length != 2)
								throw new Exception();
							else{
								vertexNumber = Integer.parseInt(argStrs[0]);
								maxCapacity = Integer.parseInt(argStrs[1]);
							}
								
						}
						catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Invalid arguments", 
									"Error", JOptionPane.ERROR_MESSAGE);
						}
						
						textFieldParams.setText(vertexNumber + " " + maxCapacity);
						
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
	// Auxiliary Methods
	// <----------------------------------------------------------------------------------

}