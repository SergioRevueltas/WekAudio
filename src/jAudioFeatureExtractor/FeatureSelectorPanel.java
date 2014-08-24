/*
 * @(#)FeatureSelectorPanel.java	1.01	April 9, 2005.
 *
 * McGill Univarsity
 */

package jAudioFeatureExtractor;

import jAudioFeatureExtractor.Aggregators.Aggregator;
import jAudioFeatureExtractor.AudioFeatures.FeatureExtractor;
import jAudioFeatureExtractor.GeneralTools.FeatureDisplay;
import jAudioFeatureExtractor.actions.MultipleToggleAction;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.miginfocom.swing.MigLayout;

import com.srevueltas.gui.CustomJButton;
import com.srevueltas.gui.CustomJTable;

/**
 * A window that allows users to select which features to save as well as some basic parameters relating to these
 * features. These parameters include the window length to use for analyses, the amount of overlap between analysis
 * windows, whether or not to normalise recordings and thh sampling rate to convert files to before analysis. The user
 * may also see the features that can be extracted and some details about them.
 * <p>
 * The resulting feature values and the features used are saved to the specified feature_vector_file and a
 * feature_key_file respectively.
 * <p>
 * For multi-track audio, analyses are performed of all tracks mixed down into one channel.
 * <p>
 * Note that some features need other features in order to be extracted. Even if a feature is not checked for saving, it
 * will be extracted (but not saved) if another feature that needs it is checked for saving.
 * <p>
 * The table allows the user to view all features which are possible to extract. The Save click box indicates whether
 * this feature is to be saved during feature extraction. The Dimensions indicate how many values are produced for a
 * given feature each time that it is extracted. Double clicking on a feature brings up a window describing it.
 * <p>
 * The Window Size indicates the number of samples that are used in each window that features are extracted from.
 * <p>
 * The Window Overlap indicates the fraction, from 0 to 1, of overlap between adjacent analysis windows.
 * <p>
 * The Normalise check box indicates whether recordings are to be normalised before playback.
 * <p>
 * The Extract Features button extracts all appropriate features and from the loaded recordings, and saves the results
 * to disk.
 * <p>
 * The Feature Values Save Path and Feature Definitions Save Path allow the user to choose what paths to save extracted
 * feature values and feature definitions respectively.
 * <p>
 * Double clicking on a feature will bring up a description of it.
 * 
 * @author Cory McKay
 */
public class FeatureSelectorPanel extends JPanel implements ActionListener {

	/* FIELDS ***************************************************************** */

	static final long serialVersionUID = 1;

	public static final Color BLUE = new Color((float) 0.75, (float) 0.85, (float) 1.0);
	public static final Color GREY = Color.GRAY;
	/**
	 * Holds a reference to the JPanel that holds objects of this class.
	 */
	public OuterFrame outer_frame;

	/**
	 * Holds references to all of the features that it's possible to extract.
	 */
	// private FeatureExtractor[] feature_extractors;
	/**
	 * The default as to whether each feature is to be saved after feature extraction. Indices correspond to those of
	 * feature_extractors.
	 */
	// private boolean[] feature_save_defaults;
	/**
	 * Replaces feature_extractors and feature_save_defaults with a view neutral model.
	 */
	// private FeatureModel featureModel;
	private MultipleToggleAction multipleToggleAction;

	/**
	 * GUI panels
	 */
	private JPanel features_panel;

	private JScrollPane features_scroll_pane;

	/**
	 * GUI table-related fields
	 */
	private CustomJTable features_table;

	private SortingTableModelDecorator decorator;

	/**
	 * GUI text areas
	 */
	//private JTextArea window_length_text_field;

	//private JTextArea window_overlap_fraction_text_field;


	/**
	 * GUI check boxes
	 */
	private JCheckBox save_window_features_check_box;

	private JCheckBox save_overall_file_featurese_check_box;

	/**
	 * GUI buttons
	 */
	private JButton set_aggregators_button;
	
	private JButton config_button;

	/**
	 * GUI dialog boxes
	 */
	private JFileChooser save_file_chooser;

	private AggregatorFrame aggregator_editor = null;
	
	private AnalysisOptionsFrame analysis_options = null;

	/**
	 * Children Windows
	 */
	private EditFeatures ef_ = null;

	/**
	 * Responsible for redistributing the control to another class
	 */
	private Controller controller;

	/* CONSTRUCTOR ************************************************************ */

	/**
	 * Set up frame.
	 * <p>
	 * Daniel McEnnis 05-08-05 Added GlobalWindowChange button
	 * 
	 * @param outer_frame The GUI element that contains this object.
	 */
	public FeatureSelectorPanel(OuterFrame outer_frame, Controller c) {
		// Store containing panel
		this.outer_frame = outer_frame;
		this.controller = c;
		// Set the file chooser to null initially
		save_file_chooser = null;

		// General container preparations containers
		int horizontal_gap = 6; // horizontal space between GUI elements
		int vertical_gap = 11;

		// Set up the list of feature extractors
		setUpFeatureTable();

		// Add an overall title for this panel
		JLabel label = new JLabel("FEATURES:");
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Arial", Font.BOLD, 14));
		add(label, "cell 0 0,growx,aligny top");

		// Set up buttons and text area
		JPanel control_panel = new JPanel(new GridLayout(4, 2, horizontal_gap, vertical_gap));

		save_window_features_check_box = new JCheckBox("Save Features For Each Window", false);
		save_window_features_check_box.setBackground(GREY);
		save_window_features_check_box.addActionListener(this);
		control_panel.add(save_window_features_check_box);

		save_overall_file_featurese_check_box = new JCheckBox("Save For Overall Recordings", true);
		save_overall_file_featurese_check_box.setBackground(GREY);
		save_overall_file_featurese_check_box.addActionListener(this);

		control_panel.add(save_overall_file_featurese_check_box);

		//control_panel.add(new JLabel("Window Size (samples):"));
		//window_length_text_field = new JTextArea("1024", 1, 20);
		//control_panel.add(window_length_text_field);

		//control_panel.add(new JLabel("Window Overlap (fraction):"));
		//window_overlap_fraction_text_field = new JTextArea("0.5", 1, 20);
		//control_panel.add(window_overlap_fraction_text_field);

		control_panel.setBackground(GREY);
		//add(control_panel, "cell 0 3,growx,aligny top");
		control_panel.setVisible(false);

		// Cause the table to respond to double clicks
		addTableMouseListener();
		analysis_options = new AnalysisOptionsFrame(controller);
		
		controller.setObjectReferences(
				analysis_options.getWindow_size_combo(),
				analysis_options.getSlider_TextField());
		
		/*
		controller.loadAction.setObjectReferences(window_length_text_field,
				window_overlap_fraction_text_field,
				save_window_features_check_box,
				save_overall_file_featurese_check_box);
		*/
		controller.outputTypeAction.setTarget(outer_frame.ace,
				outer_frame.arff, save_window_features_check_box,
				save_window_features_check_box);
		/*
		controller.addBatchAction.setSettings(save_window_features_check_box,
				save_overall_file_featurese_check_box,
				window_length_text_field, window_overlap_fraction_text_field);
		controller.viewBatchAction.setFeatureFields(
				save_window_features_check_box,
				save_overall_file_featurese_check_box,
				window_length_text_field, window_overlap_fraction_text_field);
		*/
		
		controller.dm_.aggregators = new Aggregator[] {
				// (Aggregator) (controller.dm_.aggregatorMap.get("Mean").clone()),
				(Aggregator) (controller.dm_.aggregatorMap.get("Density Based Average").clone()) };
	}

	/* PUBLIC METHODS ********************************************************* */

	/**
	 * Calls the appropriate methods when the buttons are pressed.
	 * 
	 * @param event The event that is to be reacted to.
	 */
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(set_aggregators_button)) {
			launchAggEditTable();
		} else if(event.getSource().equals(config_button)) {
			launchOptionsFrame();
		}
	}

	private void launchOptionsFrame() {
		if (analysis_options == null) {
			analysis_options = new AnalysisOptionsFrame(controller);
		}
		analysis_options.loadDataFromController();
		analysis_options.setVisible(true);
	}

	/* PRIVATE METHODS ******************************************************** */
	/**
	 * Initialize the table displaying the features which can be extracted.
	 */
	private void setUpFeatureTable() {
		controller.fstm_.fillTable(controller.dm_.featureDefinitions, controller.dm_.defaults,
				controller.dm_.is_primary);
		decorator = new SortingTableModelDecorator(controller.fstm_);
		features_table = new CustomJTable(decorator);

		multipleToggleAction = new MultipleToggleAction(features_table);
		String key = "MultipleToggleAction";
		features_table.getInputMap().put(KeyStroke.getKeyStroke(' '), key);
		features_table.getActionMap().put(key, multipleToggleAction);
		/*
		int[] width = new int[3];
		width[0] = decorator.getRealPrefferedWidth(features_table, 0);
		width[1] = decorator.getRealPrefferedWidth(features_table, 1);
		width[1] -= 100;
		width[2] = decorator.getRealPrefferedWidth(features_table, 2);

		for (int i = 0; i < width.length; ++i) {
			features_table.getColumnModel().getColumn(i).setPreferredWidth(
					width[i]);
		}
		*/
		features_table.getColumnModel().getColumn(0).setPreferredWidth(30);
		features_table.getColumnModel().getColumn(1).setPreferredWidth(360);
		features_table.getColumnModel().getColumn(2).setPreferredWidth(50);
		
		features_table.getColumnModel().getColumn(0).setMinWidth(30);
		features_table.getColumnModel().getColumn(1).setMinWidth(360);
		features_table.getColumnModel().getColumn(2).setMinWidth(50);

		// add handler for sorting panel
		JTableHeader header = (JTableHeader) features_table.getTableHeader();
		header.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					TableColumnModel tcm = features_table.getColumnModel();
					int column = features_table.convertColumnIndexToModel(tcm
							.getColumnIndexAtX(e.getX()));
					decorator.sort(column);
				} else {
					decorator.resetIndeci();
				}
			}
		});
		setLayout(new MigLayout("", "[:450px:250.00px][245.00:n]", "[17px][60.00:51.00:60.00][400.00px:315.00px][125px]"));

		set_aggregators_button = new CustomJButton("Alter Aggregators");
		add(set_aggregators_button, "flowx,cell 0 1,grow");
		set_aggregators_button.addActionListener(this);
		config_button = new CustomJButton("Analysis Options");
		add(config_button, "cell 1 1,grow");
		config_button.addActionListener(this);

		// Set up and display the table
		features_scroll_pane = new JScrollPane(features_table);
		features_panel = new JPanel(new GridLayout(1, 1));
		features_panel.add(features_scroll_pane);
		features_scroll_pane.setBackground(OuterFrame.GRAY_BOXES_LINE);
		features_scroll_pane.getViewport().setBackground(OuterFrame.GRAY);
		add(features_panel, "cell 0 2 2 1,grow");
		controller.fstm_.fireTableDataChanged();
		TableColumn tableColumn = features_table.getColumn(features_table
				.getColumnName(1));
		tableColumn.setCellRenderer(new FeatureDisplay());
		features_table.removeColumn(features_table.getColumn(features_table
				.getColumnName(3)));
		repaint();
		outer_frame.repaint();
	}

	/**
	 * Makes it so that if a row is double clicked on, a description of the corresponding feature is displayed along
	 * with its dependencies. Daniel McEnnis 05-07-05 Replaced message box with editDialog frame.
	 */
	public void addTableMouseListener() {
		features_table.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2) {
					int[] row_clicked = new int[1];
					row_clicked[0] = features_table
							.rowAtPoint(event.getPoint());
					editDialog(controller.dm_.features[row_clicked[0]]);

				}
			}
		});
	}

	/**
	 * Allows the user to select or enter a file path using a JFileChooser. If the selected path does not have an
	 * extension of .XML, it is given this extension. If the chosen path refers to a file that already exists, then the
	 * user is asked if s/he wishes to overwrite the selected file.
	 * <p>
	 * No file is actually saved or overwritten by this method. The selected path is simply returned.
	 * 
	 * @return The path of the selected or entered file. A value of null is returned if the user presses the cancel
	 *         button or chooses not to overwrite a file.
	 */

	/**
	 * Creates and displays the Dialog for editing feature attributes.
	 */
	private void editDialog(FeatureExtractor fe) {
		ef_ = new EditFeatures(this, fe);
		ef_.setVisible(true);
	}

	private void launchAggEditTable() {
		aggregator_editor = new AggregatorFrame(controller);
		aggregator_editor.setVisible(true);
	}

}
