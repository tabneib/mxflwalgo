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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import de.tud.ega.model.MGraph;
import de.tud.ega.model.MaxFlowProblem;
import de.tud.ega.controller.FordFulkerson;
import de.tud.ega.controller.MaxFlowAlgo;
import de.tud.ega.model.GraphFactory;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;

	// GUI Constants
	public static final int WINDOW_HEIGHT = 650;
	public static final int GRAPH_CONTAINER_WIDTH = 900;
	public static final int MENU_CONTAINER_WIDTH = 380;
	public static final int BOXES_PADDING = 10;
	public static final int SCROLL_VIEW_PADDING = 20;
	
	private static final String FORD_FULKERSON = "FORD_FULKERSON";
	private static final String EDMONDS_KARP = "EDMONDS_KARP";
	private static final String DINIC = "DINIC";
	private static final String GOLDBERG_TARJAN = "GOLDBERG_TARJAN";

	// GUI Components
	private JLabel labelStatusBar;
	private JFrame frame;
	private JLabel labelInsGen;
	private JTextField textFieldParams;
	private JButton buttonInsGen;
	private JLabel labelParams;
	private JLabel labelAlgo;
	private JRadioButton radioFordFulkerson;
	private JRadioButton radioEdmondsKarp;
	private JRadioButton radioDinic;
	private JRadioButton radioGoldbergTarjan;
	private JButton buttonRun;
	private JButton buttonRunStep;
	private ButtonGroup buttonGroupAlgo;

	private final Font defaultFont = new JLabel().getFont();

	// Data
	MGraph mGraph;
	//private static final int DEFAULT_VERTEX_NUMBER = 39;
	private static final int DEFAULT_VERTEX_NUMBER = 6;
	private static final int DEFAULT_MAX_CAPACITY = 10;
	private int vertexNumber = DEFAULT_VERTEX_NUMBER;
	private int maxCapacity = DEFAULT_MAX_CAPACITY;

	@Deprecated
	private String algoName;
	private MaxFlowAlgo algorith;
	
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
				GRAPH_CONTAINER_WIDTH + SCROLL_VIEW_PADDING + MENU_CONTAINER_WIDTH,
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
		pane.add(makeGraphContainer(null), c);

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
	 * Create a panel that displays the graph
	 * 
	 * @return
	 */
	private Container makeGraphContainer(MGraph graph) {

		GraphPanel graphPanel;
		//mGraph = getSampleGraph();
		if (graph == null) {
			try {
				mGraph = GraphFactory.getBeautifulPlanarGraph(vertexNumber, maxCapacity);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Invalid arguments!", "Error",
						JOptionPane.INFORMATION_MESSAGE);
				e.printStackTrace();
			}
			graphPanel = new GraphPanel(mGraph);

			if (this.algorith != null){
				buttonGroupAlgo.clearSelection();
				buttonRun.setEnabled(false);
				buttonRunStep.setEnabled(false);
				this.algorith = null;
			}
		}
		else {
			graphPanel = new GraphPanel(graph);
		}
			
		// The whole grid panel is contained inside a scroll pane
		JScrollPane scrollPane = new JScrollPane(graphPanel);
		scrollPane.setPreferredSize(new Dimension(
				GRAPH_CONTAINER_WIDTH + SCROLL_VIEW_PADDING, WINDOW_HEIGHT));

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
		c.gridy = 3;
		c.gridwidth = 2;
		panel.add(labelParams, c);
		
		
		// Algos

		labelAlgo = new JLabel("Algorithm");
		labelAlgo.setFont(new Font(defaultFont.getFontName(), Font.BOLD,
				defaultFont.getSize() + 2));
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 3;
		panel.add(labelAlgo, c);
		
		radioFordFulkerson = new JRadioButton("Ford-Fulkerson");
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		panel.add(radioFordFulkerson, c);
		
		radioEdmondsKarp = new JRadioButton("Edmonds-Karp");
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 1;
		panel.add(radioEdmondsKarp, c);
			
		radioDinic = new JRadioButton("Dinic");
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 1;
		panel.add(radioDinic, c);
		
		radioGoldbergTarjan = new JRadioButton("Goldberg-Tarjan");
		c.gridx = 1;
		c.gridy = 6;
		c.gridwidth = 1;
		panel.add(radioGoldbergTarjan, c);
			
		buttonGroupAlgo = new ButtonGroup();
		buttonGroupAlgo.add(radioFordFulkerson);
		buttonGroupAlgo.add(radioEdmondsKarp);
		buttonGroupAlgo.add(radioDinic);
		buttonGroupAlgo.add(radioGoldbergTarjan);
		//radioFordFulkerson.setSelected(true);

		buttonRunStep = new JButton("1 Step");
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 1;
		buttonRunStep.setPreferredSize(new Dimension(100, 30));
		panel.add(buttonRunStep, c);
		buttonRunStep.setEnabled(false);
		
		buttonRun = new JButton("Run");
		c.gridx = 1;
		c.gridy = 8;
		c.gridwidth = 1;
		buttonRun.setPreferredSize(new Dimension(100, 30));
		panel.add(buttonRun, c);
		buttonRun.setEnabled(false);
		
		// TODO: remove this when all features are implemented
		radioEdmondsKarp.setEnabled(false);
		radioDinic.setEnabled(false);
		radioGoldbergTarjan.setEnabled(false);

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
						frame.getContentPane().add(makeGraphContainer(null), c, 0);
						frame.getContentPane().validate();
					}
				});

			}
		});

		radioFordFulkerson.addActionListener(new AlgorithmSelectListener());
		radioEdmondsKarp.addActionListener(new AlgorithmSelectListener());
		radioDinic.addActionListener(new AlgorithmSelectListener());
		radioGoldbergTarjan.addActionListener(new AlgorithmSelectListener());
		
		buttonRun.addActionListener(new ActionListener() {
			
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
						frame.getContentPane().add(makeGraphContainer(algorith.run()), c, 0);
						frame.getContentPane().validate();
					}
				});
			}
		});
		
	}

	
	private class AlgorithmSelectListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (radioFordFulkerson.isSelected()) {
				algoName = FORD_FULKERSON;
				buttonRun.setEnabled(true);
				algorith = new FordFulkerson(new MaxFlowProblem(mGraph));
			}
			else if (radioEdmondsKarp.isSelected()) {
				algoName = EDMONDS_KARP;
				// TODO
			}
			else if (radioDinic.isSelected()) {
				algoName = DINIC;
				// TODO
			}
			else if (radioGoldbergTarjan.isSelected()) {
				algoName = GOLDBERG_TARJAN;
				// TODO
			}
			else
				throw new RuntimeException("Unknown chosen algorithm!");
			
		}
	}
	
	
}