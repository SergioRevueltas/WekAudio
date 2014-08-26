/*
 * @(#)FeatureSelectorPanel.java	1.01	April 9, 2005.
 *
 * McGill Univarsity
 */

package jAudioFeatureExtractor;

import jAudioFeatureExtractor.ACE.XMLParsers.FileFilterARFF;
import jAudioFeatureExtractor.ACE.XMLParsers.FileFilterMODEL;
import jAudioFeatureExtractor.Aggregators.Aggregator;
import jAudioFeatureExtractor.DataTypes.RecordingInfo;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.srevueltas.gui.CustomJButton;
import com.srevueltas.gui.CustomJLabel;
import com.srevueltas.gui.CustomJTextField;

/**
 * 
 * @author Sergio Revueltas
 */
public class DataMiningPanel extends JPanel implements ActionListener {

	/* FIELDS ***************************************************************** */

	static final long serialVersionUID = 1;

	public static final Color BLUE = new Color((float) 0.75, (float) 0.85, (float) 1.0);
	public static final Color GREY = Color.GRAY;
	/**
	 * Holds a reference to the JPanel that holds objects of this class.
	 */
	public OuterFrame outer_frame;

	/**
	 * GUI buttons
	 */
	private JButton extract_features_button;

	private JButton classify_button;

	/**
	 * GUI dialog boxes
	 */
	private JFileChooser save_file_chooser;
	private JFileChooser load_file_chooser;

	/**
	 * Responsible for redistributing the control to another class
	 */
	private Controller controller;
	private CustomJTextField arffSavePathTextField;
	private JButton saveBrowseButton;
	private CustomJTextField loadModelTextField;
	private JButton loadBrowseButton;
	private JLabel lblArffSavePath;
	private JLabel lblModelLoadPath;

	/* CONSTRUCTOR ************************************************************ */

	/**
	 * Set up frame.
	 * <p>
	 * Daniel McEnnis 05-08-05 Added GlobalWindowChange button
	 * 
	 * @param outer_frame The GUI element that contains this object.
	 */
	public DataMiningPanel(OuterFrame outer_frame, Controller c) {
		// Store containing panel
		this.outer_frame = outer_frame;
		this.controller = c;
		// Set the file chooser to null initially
		save_file_chooser = null;
		setLayout(new MigLayout("", "[][240.00px:271.00px:240.00px,grow][90.00px:92.00px:97.00px]", "[23px][][50.00:n:50.00][138.00:n][][50.00:n:50.00]"));

		// Add an overall title for this panel
		JLabel label = new CustomJLabel("DATA MINING:");
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Arial", Font.BOLD, 14));
		add(label, "cell 0 0,alignx left,aligny center");

		lblArffSavePath = new CustomJLabel("ARFF Save Path");
		add(lblArffSavePath, "cell 0 1,alignx trailing");
		lblArffSavePath.setFont(new Font("Arial", Font.BOLD, 12));
		
		arffSavePathTextField = new CustomJTextField();
		add(arffSavePathTextField, "cell 1 1,growx");
		arffSavePathTextField.setColumns(50);
		arffSavePathTextField.setText("exportedFeatureValues/default.arff");

		saveBrowseButton = new CustomJButton("Browse");
		add(saveBrowseButton, "cell 2 1,growx");
		saveBrowseButton.addActionListener(this);

		extract_features_button = new CustomJButton("Extract Features");
		extract_features_button.setText("Train");
		add(extract_features_button, "cell 0 2 3 1,grow");
		extract_features_button.addActionListener(this);

		lblModelLoadPath = new CustomJLabel("Model Load Path");
		add(lblModelLoadPath, "cell 0 4,alignx trailing");
		lblModelLoadPath.setFont(new Font("Arial", Font.BOLD, 12));

		loadModelTextField = new CustomJTextField();
		add(loadModelTextField, "cell 1 4,growx");
		loadModelTextField.setColumns(50);

		loadBrowseButton = new CustomJButton("Browse");
		add(loadBrowseButton, "cell 2 4,grow");
		loadBrowseButton.addActionListener(this);

		classify_button = new CustomJButton("Classify");
		add(classify_button, "cell 0 5 3 1,grow");
		classify_button.addActionListener(this);

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
		if (event.getSource().equals(extract_features_button))
			train(false);
		else if (event.getSource().equals(classify_button)) {
			classifyInstances(true);
		} else if (event.getSource().equals(saveBrowseButton)){
			browseFeatureValuesSavePath();
		} else if (event.getSource().equals(loadBrowseButton)){
			browseModelLoadPath();
		}
	}

	

	/* PRIVATE METHODS ******************************************************** */
	/**
	 * Extract the features from all of the files added in the GUI. 
	 * Use the features and feature settings entered in the GUI. 
	 * Save the results in a feature_vector_file and classify features used in a stored model. 
	 * Sergio Revueltas
	 */
	private void classifyInstances(boolean toClassify) {
		try {
			// Get the control parameters
			boolean save_features_for_each_window = false;
			boolean save_overall_recording_features = true;
			//String feature_values_save_path = arffSavePathTextField.getText();
			controller.windowSizeCombo.setSelectedIndex(controller.window_size_index);
			int window_size = (int) controller.windowSizeCombo.getSelectedItem();
			double window_overlap_percentage = controller.getWindow_overlap_value();
			double window_overlap_fraction = window_overlap_percentage / 100;

			// Get the audio recordings to extract features from and throw an exception if there are none
			RecordingInfo[] recordings = controller.dm_.recordingInfo;
			if (recordings == null)
				throw new Exception(
						"No recordings available to extract features from.");

			// Find which features are selected to be saved
			for (int i = 0; i < controller.dm_.defaults.length; i++) {
				controller.dm_.defaults[i] = ((Boolean) controller.fstm_
						.getValueAt(i, 0)).booleanValue();
			}

			// threads can only execute once. Rebuild the thread here
			controller.extractionThread = new ExtractionThread(controller, outer_frame);

			controller.extractionThread.setup(save_overall_recording_features,
					save_features_for_each_window, "",
					"", window_size, window_overlap_fraction, toClassify, loadModelTextField.getText());
			// extract the features
			controller.extractionThread.start();

		} catch (Throwable t) {
			// React to the Java Runtime running out of memory
			if (t.toString().equals("java.lang.OutOfMemoryError"))
				JOptionPane.showMessageDialog(
						null,
						"The Java Runtime ran out of memory. Please rerun this program\n"
								+ "with a higher amount of memory assigned to the Java Runtime heap.",
						"ERROR", JOptionPane.ERROR_MESSAGE);
			else if (t instanceof Exception) {
				Exception e = (Exception) t;
				JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Extract the features from all of the files added in the GUI. 
	 * Use the features and feature settings entered in the GUI. 
	 * Save the results in a feature_vector_file and the features used in a feature_key_file. 
	 * Daniel McEnnis 05-09-05 Moved guts into FeatureModel
	 * Edited by Sergio Revueltas
	 */
	private void train(boolean toClassify) {
		try {
			// Get the control parameters
			boolean save_features_for_each_window = false;
			boolean save_overall_recording_features = true;
			String feature_values_save_path = arffSavePathTextField.getText();
			// String feature_definitions_save_path =
			// outer_frame.recording_selector_panel.definitions_save_path_text_field.getText();
			controller.windowSizeCombo.setSelectedIndex(controller.window_size_index);
			int window_size = (int) controller.windowSizeCombo.getSelectedItem();
			double window_overlap_percentage = controller.getWindow_overlap_value();
			double window_overlap_fraction = window_overlap_percentage / 100;

			boolean normalise = controller.normalise.isSelected();
			double sampling_rate = controller.samplingRateAction.getSamplingRate();
			int outputType = controller.outputTypeAction.getSelected();

			// Get the audio recordings to extract features from and throw an
			// exception
			// if there are none
			RecordingInfo[] recordings = controller.dm_.recordingInfo;
			if (recordings == null)
				throw new Exception(
						"No recordings available to extract features from.");

			// Find which features are selected to be saved
			for (int i = 0; i < controller.dm_.defaults.length; i++) {
				controller.dm_.defaults[i] = ((Boolean) controller.fstm_
						.getValueAt(i, 0)).booleanValue();
			}

			// threads can only execute once. Rebuild the thread here
			controller.extractionThread = new ExtractionThread(controller,
					outer_frame);
			// setup thread
			controller.extractionThread.setup(save_overall_recording_features,
					save_features_for_each_window, feature_values_save_path,
					"", window_size, window_overlap_fraction, toClassify, "");
			// extract the features
			controller.extractionThread.start();

		} catch (Throwable t) {
			// React to the Java Runtime running out of memory
			if (t.toString().equals("java.lang.OutOfMemoryError"))
				JOptionPane
						.showMessageDialog(
								null,
								"The Java Runtime ran out of memory. Please rerun this program\n"
										+ "with a higher amount of memory assigned to the Java Runtime heap.",
								"ERROR", JOptionPane.ERROR_MESSAGE);
			else if (t instanceof Exception) {
				Exception e = (Exception) t;
				JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	
	/**
	 * Allow the user to choose a save path for the feature_vector_file XML file where feature values are to be saved.
	 * The selected path is entered in the values_save_path_text_field.
	 */
	private void browseFeatureValuesSavePath() {
		String path = chooseSavePath();
		if (path != null)
			arffSavePathTextField.setText(path);
	}
	
	private void browseModelLoadPath() {
		String path = chooseLoadPath();
		if (path != null)
			loadModelTextField.setText(path);
		
	}
	
	
	/**
	 * Allows the user to select or enter a file path using a JFileChooser. If the selected path does not have an
	 * extension of .arff, it is given this extension. If the chosen path refers to a file that already exists, then the
	 * user is asked if s/he wishes to overwrite the selected file.
	 * <p>
	 * No file is actually saved or overwritten by this method. The selected path is simply returned.
	 * 
	 * @return The path of the selected or entered file. A value of null is returned if the user presses the cancel
	 *         button or chooses not to overwrite a file.
	 */
	private String chooseLoadPath() {
		// Create the JFileChooser if it does not already exist
		if (load_file_chooser == null) {
			load_file_chooser = new JFileChooser();
			load_file_chooser.setCurrentDirectory(new File("exportedFeatureValues/"));
			load_file_chooser.setFileFilter(new FileFilterMODEL());
			load_file_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			load_file_chooser.setMultiSelectionEnabled(false);
			load_file_chooser.setLocation(30, 30);
		}

		// Process the user's entry
		String path = null;
		int dialog_result = load_file_chooser.showOpenDialog(this);
		// only do if OK chosen
		if (dialog_result == JFileChooser.APPROVE_OPTION) {
			// Get the file the user chose
			File to_load_model = load_file_chooser.getSelectedFile();

			// Make sure has .model extension
			path = to_load_model.getPath();
			int pos = path.lastIndexOf(".");
			String ext = path.substring(pos, path.length());
			//String ext = jAudioFeatureExtractor.GeneralTools.StringMethods.getExtension(path);
			if (ext == null) {
				path += ".model";
			} else if (!ext.equals(".xml")) {
				String tmpPath = path.substring(0, pos);
				path = tmpPath	+ ".model";
			}
			loadModelTextField.setText(path);
		}

		// Return the selected file path
		return path;
	}
	

	/**
	 * Allows the user to select or enter a file path using a JFileChooser. If the selected path does not have an
	 * extension of .arff, it is given this extension. If the chosen path refers to a file that already exists, then the
	 * user is asked if s/he wishes to overwrite the selected file.
	 * <p>
	 * No file is actually saved or overwritten by this method. The selected path is simply returned.
	 * 
	 * @return The path of the selected or entered file. A value of null is returned if the user presses the cancel
	 *         button or chooses not to overwrite a file.
	 */
	private String chooseSavePath() {
		// Create the JFileChooser if it does not already exist
		if (save_file_chooser == null) {
			save_file_chooser = new JFileChooser();
			save_file_chooser.setCurrentDirectory(new File("exportedFeatureValues/"));
			save_file_chooser.setFileFilter(new FileFilterARFF());
		}

		// Process the user's entry
		String path = null;
		int dialog_result = save_file_chooser.showSaveDialog(this);
		if (dialog_result == JFileChooser.APPROVE_OPTION) // only do if OK
		// chosen
		{
			// Get the file the user chose
			File to_save_to = save_file_chooser.getSelectedFile();

			// Make sure has .arff extension
			path = to_save_to.getPath();
			String ext = jAudioFeatureExtractor.GeneralTools.StringMethods
					.getExtension(path);
			if (ext == null) {
				path += ".arff";
				to_save_to = new File(path);
			} else if (!ext.equals(".xml")) {
				path = jAudioFeatureExtractor.GeneralTools.StringMethods
						.removeExtension(path)
						+ ".arff";
				to_save_to = new File(path);
			}

			// See if user wishes to overwrite if a file with the same name
			// exists
			if (to_save_to.exists()) {
				int overwrite = JOptionPane
						.showConfirmDialog(
								null,
								"This file already exists.\nDo you wish to overwrite it?",
								"WARNING", JOptionPane.YES_NO_OPTION);
				if (overwrite != JOptionPane.YES_OPTION)
					path = null;
			}
		}

		// Return the selected file path
		return path;
	}

}
