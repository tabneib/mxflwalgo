package de.nhd.mxflwalgo.view;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import de.nhd.mxflwalgo.algos.Dinic;
import de.nhd.mxflwalgo.algos.EdmondsKarp;
import de.nhd.mxflwalgo.algos.FordFulkerson;
import de.nhd.mxflwalgo.algos.GoldbergTarjan;
import de.nhd.mxflwalgo.algos.MaxFlowAlgo;
import de.nhd.mxflwalgo.model.GraphFactory;
import de.nhd.mxflwalgo.model.MGraph;
import de.nhd.mxflwalgo.model.MaxFlowProblem;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;

	// GUI Constants
	public static final int WINDOW_HEIGHT = 650;
	public static final int GRAPH_CONTAINER_WIDTH = 900;
	public static final int MENU_CONTAINER_WIDTH = 380;
	public static final int BOXES_PADDING = 10;
	public static final int SCROLL_VIEW_PADDING = 20;
	public static final int DEFAULT_DELAY = 500;

	protected static final String FORD_FULKERSON = "FORD_FULKERSON";
	protected static final String EDMONDS_KARP = "EDMONDS_KARP";
	protected static final String DINIC = "DINIC";
	protected static final String GOLDBERG_TARJAN = "GOLDBERG_TARJAN";

	// GUI States
	private static final String STATE_GRAPH_GENERATED = "STATE_GRAPH_GENERATED";
	private static final String STATE_ALGO_SELECTED = "STATE_ALGO_SELECTED";
	private static final String STATE_RUNNING = "STATE_RUNNING";
	private static final String STATE_PAUSED = "STATE_PAUSED";
	private static final String STATE_FINISHED = "STATE_FINISHED";

	// GUI Components
	private JLabel labelStatusBar;
	private JFrame frame;
	private JLabel labelInsGen;
	private JTextField textFieldParams;
	private JTextField textFieldDelay;
	private JButton buttonInsGen;
	private JLabel labelParams;
	private JLabel labelDelay;
	private JLabel labelAlgo;
	private JRadioButton radioFordFulkerson;
	private JRadioButton radioEdmondsKarp;
	private JRadioButton radioDinic;
	private JRadioButton radioGoldbergTarjan;
	private JButton buttonRun;
	private JButton buttonRunStep;
	private JButton buttonReset;
	private ButtonGroup buttonGroupAlgo;

	private final Font defaultFont = new JLabel().getFont();

	// Data
	MGraph mGraph;
	// private static final int DEFAULT_VERTEX_NUMBER = 39;
	private static final int DEFAULT_VERTEX_NUMBER = 53;
	// private static final int DEFAULT_VERTEX_NUMBER = 7;
	private static final int DEFAULT_MAX_CAPACITY = 100;
	private int vertexNumber = DEFAULT_VERTEX_NUMBER;
	private int maxCapacity = DEFAULT_MAX_CAPACITY;

	private String algoName;
	private MaxFlowAlgo algorithm;
	private MaxFlowProblem problem;

	private Timer timer = new Timer();
	private TimerTask algoRunTask = null;

	private String state = STATE_GRAPH_GENERATED;

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
		frame = new JFrame("mxflwalgo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(
				GRAPH_CONTAINER_WIDTH + SCROLL_VIEW_PADDING + MENU_CONTAINER_WIDTH,
				WINDOW_HEIGHT));
		this.initMaxFlowProblem();
		
		//this.revalidateAlgo();
		addComponentsToPane(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
	}

	
	private void initMaxFlowProblem() {
		try {
			this.mGraph = GraphFactory.getBeautifulPlanarGraph(vertexNumber, maxCapacity);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Invalid arguments!", "Error",
					JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}
		this.problem = new MaxFlowProblem((MGraph) this.mGraph);
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
	 * Create a panel that displays the graph of the current problem instance.
	 * If there is no current problem => create a new instance.
	 * 
	 * @return
	 */
	private Container makeGraphContainer() {

		GraphPanel graphPanel;
		// mGraph = getSampleGraph();

		graphPanel = new GraphPanel(this.problem, this.algoName);

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
		// radioFordFulkerson.setSelected(true);

		buttonRunStep = new JButton("1 Step");
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 1;
		buttonRunStep.setPreferredSize(new Dimension(100, 20));
		panel.add(buttonRunStep, c);
		buttonRunStep.setEnabled(false);

		buttonRun = new JButton("Run");
		c.gridx = 1;
		c.gridy = 8;
		c.gridwidth = 1;
		buttonRun.setPreferredSize(new Dimension(100, 20));
		panel.add(buttonRun, c);
		buttonRun.setEnabled(false);

		buttonReset = new JButton("Reset");
		c.gridx = 2;
		c.gridy = 8;
		c.gridwidth = 1;
		buttonReset.setPreferredSize(new Dimension(100, 20));
		panel.add(buttonReset, c);
		buttonReset.setEnabled(false);

		labelDelay = new JLabel("Running Delay:");
		c.gridx = 0;
		c.gridy = 9;
		c.gridwidth = 1;
		panel.add(labelDelay, c);
		labelDelay.setEnabled(false);

		textFieldDelay = new JTextField("" + DEFAULT_DELAY);
		c.gridx = 1;
		c.gridy = 9;
		c.gridwidth = 1;
		textFieldDelay.setPreferredSize(new Dimension(80, 20));
		c.fill = GridBagConstraints.VERTICAL;
		textFieldDelay.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(panel.getBackground(), 5),
				new EtchedBorder()));
		panel.add(textFieldDelay, c);
		textFieldDelay.setEnabled(false);
		c.fill = GridBagConstraints.NONE;

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

				javax.swing.SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						try {
							// Parse arguments
							final String[] argStrs = textFieldParams.getText().split(" ");

							if (argStrs.length != 2)
								throw new Exception();
							else {
								vertexNumber = Integer.parseInt(argStrs[0]);
								maxCapacity = Integer.parseInt(argStrs[1]);
							}

						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Invalid arguments",
									"Error", JOptionPane.ERROR_MESSAGE);
						}

						removeAlgoRunTask();
						updateState(STATE_GRAPH_GENERATED);
						initMaxFlowProblem();
						algorithm = null;
						
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

		radioFordFulkerson.addActionListener(new AlgorithmSelectListener());
		radioEdmondsKarp.addActionListener(new AlgorithmSelectListener());
		radioDinic.addActionListener(new AlgorithmSelectListener());
		radioGoldbergTarjan.addActionListener(new AlgorithmSelectListener());

		// Run 1 step
		buttonRunStep.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						removeAlgoRunTask();
						updateState(STATE_PAUSED);
						runStep();
					}
				});
			}
		});

		// Run the whole algorithm stepwise with given delay
		buttonRun.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						if (state.equals(STATE_RUNNING)) {
							removeAlgoRunTask();
							updateState(STATE_PAUSED);
						} else {
							algoRunTask = genAlgoRunTask();

							try {
								final int delay = Integer
										.parseInt(textFieldDelay.getText());
								timer.schedule(algoRunTask, 0, delay);
								updateState(STATE_RUNNING);
							} catch (Exception e) {
								JOptionPane.showMessageDialog(null, "Invalid delay",
										"Error", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				});
			}
		});

		buttonReset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
					
						//revalidateAlgo();
						algorithm.reset();
						Component gContainer = frame.getContentPane().getComponent(0);
						GridBagLayout layout = (GridBagLayout) frame.getContentPane()
								.getLayout();
						GridBagConstraints c = layout.getConstraints(gContainer);

						frame.getContentPane().remove(gContainer);
						frame.getContentPane().add(makeGraphContainer(), c, 0);
						frame.getContentPane().validate();
						updateState(STATE_ALGO_SELECTED);
					}
				});

			}
		});

	}

	private class AlgorithmSelectListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (radioFordFulkerson.isSelected()) {
				mGraph.reset();
				algoName = FORD_FULKERSON;
				algorithm = new FordFulkerson(problem);
				updateState(STATE_ALGO_SELECTED);
			} else if (radioEdmondsKarp.isSelected()) {
				mGraph.reset();
				algoName = EDMONDS_KARP;
				buttonRun.setEnabled(true);
				buttonRunStep.setEnabled(true);
				algorithm = new EdmondsKarp(problem);
				updateState(STATE_ALGO_SELECTED);
			} else if (radioDinic.isSelected()) {
				mGraph.reset();
				algoName = DINIC;
				algorithm = new Dinic(problem);
				updateState(STATE_ALGO_SELECTED);
			} else if (radioGoldbergTarjan.isSelected()) {
				mGraph.reset();
				algoName = GOLDBERG_TARJAN;
				algorithm = new GoldbergTarjan(problem);
				updateState(STATE_ALGO_SELECTED);
			} else
				throw new RuntimeException("Unknown chosen algorithm!");

		}
	}

	/**
	 * Run one single step of the current chosen algorithm
	 */
	private void runStep() {
		if (!algorithm.isFinished()) {
			algorithm.runStep();
			Component gContainer = frame.getContentPane().getComponent(0);
			GridBagLayout layout = (GridBagLayout) frame.getContentPane().getLayout();
			GridBagConstraints c = layout.getConstraints(gContainer);

			frame.getContentPane().remove(gContainer);
			frame.getContentPane().add(makeGraphContainer(), c, 0);
			frame.getContentPane().validate();
		}
		if (algorithm.isFinished()) {
			removeAlgoRunTask();
			updateState(STATE_FINISHED);
		}
	}

	/**
	 * Generate the asynchronous task to run the algorithm periodically
	 * 
	 * @return
	 */
	private TimerTask genAlgoRunTask() {
		return new TimerTask() {

			@Override
			public void run() {
				runStep();
				Component gContainer = frame.getContentPane().getComponent(0);
				GridBagLayout l = (GridBagLayout) frame.getContentPane().getLayout();
				GridBagConstraints c = l.getConstraints(gContainer);

				frame.getContentPane().remove(gContainer);
				frame.getContentPane().add(makeGraphContainer(), c, 0);
				frame.getContentPane().validate();
			}
		};
	}

	/**
	 * Cancel and remove any asynchronous algorithm running task
	 */
	private void removeAlgoRunTask() {
		if (algoRunTask != null) {
			algoRunTask.cancel();
			algoRunTask = null;
		}
	}

	/**
	 * Update the GUI to the given state. The state transition is validated
	 * first.
	 * 
	 * @param newState
	 */
	private void updateState(String newState) {
		switch (newState) {
			case STATE_GRAPH_GENERATED :
				if (!this.state.equals(STATE_GRAPH_GENERATED)
						&& !this.state.equals(STATE_ALGO_SELECTED))
					throw new RuntimeException(
							"Invalid state change: " + state + " -> " + newState);
				this.state = newState;
				labelInsGen.setEnabled(true);
				textFieldParams.setEnabled(true);
				buttonInsGen.setEnabled(true);
				labelParams.setEnabled(true);
				labelAlgo.setEnabled(true);
				radioDinic.setEnabled(true);
				radioEdmondsKarp.setEnabled(true);
				radioFordFulkerson.setEnabled(true);
				radioGoldbergTarjan.setEnabled(true);
				buttonGroupAlgo.clearSelection();
				buttonRunStep.setEnabled(false);
				buttonRun.setEnabled(false);
				buttonReset.setEnabled(false);
				labelDelay.setEnabled(false);
				textFieldDelay.setEnabled(false);

				buttonRun.setText("Run");
				break;
			case STATE_ALGO_SELECTED :
				if (this.state.equals(STATE_RUNNING))
					throw new RuntimeException("Invalid state change");
				this.state = newState;
				labelInsGen.setEnabled(true);
				textFieldParams.setEnabled(true);
				buttonInsGen.setEnabled(true);
				labelParams.setEnabled(true);
				labelAlgo.setEnabled(true);
				radioDinic.setEnabled(true);
				radioEdmondsKarp.setEnabled(true);
				radioFordFulkerson.setEnabled(true);
				radioGoldbergTarjan.setEnabled(true);
				buttonRunStep.setEnabled(true);
				buttonRun.setEnabled(true);
				buttonReset.setEnabled(false);
				labelDelay.setEnabled(true);
				textFieldDelay.setEnabled(true);

				buttonRun.setText("Run");
				break;
			case STATE_RUNNING :
				if (this.state.equals(STATE_GRAPH_GENERATED)
						|| this.state.equals(STATE_FINISHED))
					throw new RuntimeException("Invalid state change");
				this.state = newState;
				labelInsGen.setEnabled(false);
				textFieldParams.setEnabled(false);
				buttonInsGen.setEnabled(false);
				labelParams.setEnabled(false);
				labelAlgo.setEnabled(false);
				radioDinic.setEnabled(false);
				radioEdmondsKarp.setEnabled(false);
				radioFordFulkerson.setEnabled(false);
				radioGoldbergTarjan.setEnabled(false);
				buttonRunStep.setEnabled(false);
				buttonRun.setEnabled(true);
				buttonReset.setEnabled(false);
				labelDelay.setEnabled(false);
				textFieldDelay.setEnabled(false);

				buttonRun.setText("Pause");
				break;
			case STATE_PAUSED :
				if (!this.state.equals(STATE_RUNNING)
						&& !this.state.equals(STATE_ALGO_SELECTED)
						&& !this.state.equals(STATE_PAUSED))
					throw new RuntimeException("Invalid state change");
				this.state = newState;
				labelInsGen.setEnabled(false);
				textFieldParams.setEnabled(false);
				buttonInsGen.setEnabled(false);
				labelParams.setEnabled(false);
				labelAlgo.setEnabled(false);
				radioDinic.setEnabled(false);
				radioEdmondsKarp.setEnabled(false);
				radioFordFulkerson.setEnabled(false);
				radioGoldbergTarjan.setEnabled(false);
				buttonRunStep.setEnabled(true);
				buttonRun.setEnabled(true);
				buttonReset.setEnabled(true);
				labelDelay.setEnabled(true);
				textFieldDelay.setEnabled(true);

				buttonRun.setText("Run");
				break;
			case STATE_FINISHED :
				if (!this.state.equals(STATE_RUNNING) && !this.state.equals(STATE_PAUSED))
					throw new RuntimeException("Invalid state change");
				this.state = newState;
				labelInsGen.setEnabled(false);
				textFieldParams.setEnabled(false);
				buttonInsGen.setEnabled(false);
				labelParams.setEnabled(false);
				labelAlgo.setEnabled(false);
				radioDinic.setEnabled(false);
				radioEdmondsKarp.setEnabled(false);
				radioFordFulkerson.setEnabled(false);
				radioGoldbergTarjan.setEnabled(false);
				buttonRunStep.setEnabled(false);
				buttonRun.setEnabled(false);
				buttonReset.setEnabled(true);
				labelDelay.setEnabled(false);
				textFieldDelay.setEnabled(false);

				buttonRun.setText("Run");
				break;
		}
	}
}