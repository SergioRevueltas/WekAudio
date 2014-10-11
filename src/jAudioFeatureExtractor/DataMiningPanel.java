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
import jAudioFeatureExtractor.jAudioTools.FileFilterAudio;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.srevueltas.gui.CustomJButton;
import com.srevueltas.gui.CustomJLabel;
import com.srevueltas.gui.CustomJTextArea;
import com.srevueltas.gui.CustomJTextField;

/**
 * 
 * @author Sergio Revueltas
 */
public class DataMiningPanel extends JPanel implements ActionListener {

	/* FIELDS ***************************************************************** */

	static final long serialVersionUID = 1;

	public static final Color GRAY = Color.GRAY;
	/**
	 * Holds a reference to the JPanel that holds objects of this class.
	 */
	private Controller controller;
	public OuterFrame outer_frame;

	/**
	 * GUI Panels
	 */
	private JPanel trainPanel;
	private JPanel classifyPanel;
	/**
	 * GUI buttons
	 */
	private JButton extract_features_button;
	private JButton classify_button;
	private JButton saveBrowseButton;
	private JButton loadFileBrowseButton;
	private JButton loadModelBrowseButton;

	/**
	 * GUI dialog boxes
	 */
	private JFileChooser save_file_chooser;
	private JFileChooser load_file_chooser;
	private JFileChooser load_model_chooser;

	/**
	 * GUI labels
	 */
	private CustomJLabel lblClassifiers;
	private CustomJLabel lblFileToClassify;
	private CustomJLabel lblArffSavePath;
	private CustomJLabel lblModelLoadPath;

	private CustomJTextField arffSavePathTextField;
	private CustomJTextField loadFileToClassifyTextField;
	private CustomJTextField loadModelTextField;
	private JComboBox<String> cbClassifiers;
	private JPanel trainResultsPanel;
	private CustomJTextArea trainresultstextarea;

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
		setLayout(new MigLayout("", "[340.00px]", "[23px][240.00px:240.00px:240.00px][240.00px:240.00px:240.00px]"));

		// Add an overall title for this panel
		JLabel label = new CustomJLabel("DATA MINING:");
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Arial", Font.BOLD, 14));
		add(label, "cell 0 0,alignx left,aligny center");

		trainPanel = new JPanel();
		add(trainPanel, "flowx,cell 0 1,grow");
		trainPanel.setLayout(new MigLayout("ins 0", "[grow][grow][]", "[][][50.00px:50.00px:50.00px][grow]"));
		trainPanel.setBackground(GRAY);

		lblClassifiers = new CustomJLabel("Classifiers");
		trainPanel.add(lblClassifiers, "cell 0 0,alignx trailing");
		lblClassifiers.setFont(new Font("Arial", Font.BOLD, 12));

		loadClassifiersCombo();
		trainPanel.add(cbClassifiers, "cell 1 0 2 1,growx,aligny bottom");

		lblArffSavePath = new CustomJLabel("ARFF Save Path");
		trainPanel.add(lblArffSavePath, "cell 0 1");
		lblArffSavePath.setFont(new Font("Arial", Font.BOLD, 12));

		arffSavePathTextField = new CustomJTextField();
		trainPanel.add(arffSavePathTextField, "cell 1 1");
		arffSavePathTextField.setColumns(50);
		arffSavePathTextField.setText("exportedFeatureValues/default.arff");

		saveBrowseButton = new CustomJButton("Browse");
		trainPanel.add(saveBrowseButton, "cell 2 1");

		extract_features_button = new CustomJButton("Extract Features");
		trainPanel.add(extract_features_button, "cell 0 2 3 1,grow");
		extract_features_button.setText("Train");

		trainResultsPanel = new JPanel();
		trainPanel.add(trainResultsPanel, "flowx,cell 0 3 3 1,growy");
		trainResultsPanel.setBackground(GRAY);
		trainResultsPanel.setLayout(new MigLayout("", "[329.00px:104px]", "[120.00px:19px]"));

		trainresultstextarea = new CustomJTextArea();
		trainresultstextarea.setFont(new Font("Arial", Font.PLAIN, 10));
		trainResultsPanel.add(trainresultstextarea, "cell 0 0,grow");
		trainresultstextarea.setText("train Results Text Area slkdfglñkdfjg ñldfkgj ñldkgj dlfñkgj ñldskg"
				+ "asdfsadf sadfasd fsda fasd f asdf sda fsda f sdafjasdlkfjasñlk sdlkj fsdlkñj fsaldñk flñksd");

		extract_features_button.addActionListener(this);
		saveBrowseButton.addActionListener(this);

		classifyPanel = new JPanel();
		add(classifyPanel, "flowx,cell 0 2,growy");
		classifyPanel.setLayout(new MigLayout("ins 0", "[][][]", "[][][50.00px:50.00px:50.00px][][][]"));
		classifyPanel.setBackground(GRAY);

		lblFileToClassify = new CustomJLabel("File to classify");
		classifyPanel.add(lblFileToClassify, "cell 0 0");
		lblFileToClassify.setFont(new Font("Arial", Font.BOLD, 12));

		
		lblModelLoadPath = new CustomJLabel("Model Load Path");
		classifyPanel.add(lblModelLoadPath, "cell 0 1");
		lblModelLoadPath.setFont(new Font("Arial", Font.BOLD, 12));

		loadFileToClassifyTextField = new CustomJTextField();
		classifyPanel.add(loadFileToClassifyTextField, "cell 1 0");
		loadFileToClassifyTextField.setColumns(50);
		
		loadModelTextField = new CustomJTextField();
		classifyPanel.add(loadModelTextField, "cell 1 1");
		loadModelTextField.setColumns(50);

		loadFileBrowseButton = new CustomJButton("Browse");
		classifyPanel.add(loadFileBrowseButton, "cell 2 0");
		loadFileBrowseButton.addActionListener(this);
		
		loadModelBrowseButton = new CustomJButton("Browse");
		classifyPanel.add(loadModelBrowseButton, "cell 2 1");

		classify_button = new CustomJButton("Classify");
		classifyPanel.add(classify_button, "cell 0 2 3 1,grow");
		classify_button.addActionListener(this);
		loadModelBrowseButton.addActionListener(this);

		controller.dm_.aggregators = new Aggregator[] {
				// (Aggregator) (controller.dm_.aggregatorMap.get("Mean").clone()),
				(Aggregator) (controller.dm_.aggregatorMap.get("Density Based Average").clone()) };
	}

	/**
	 * Classifier interface implementing classes http://weka.sourceforge.net/doc.dev/weka/classifiers/Classifier.html
	 */
	private void loadClassifiersCombo() {
		this.cbClassifiers = new JComboBox<String>();
		String[] classifierItems =
				new String[] { "AbstractClassifier", "AdaBoostM1",
						"AdditiveRegression", "AttributeSelectedClassifier", "Bagging", "BayesNet",
						"BayesNetGenerator", "BIFReader", "ClassificationViaRegression", "CostSensitiveClassifier",
						"CVParameterSelection", "DecisionStump", "DecisionTable", "EditableBayesNet",
						"FilteredClassifier", "GaussianProcesses", "GeneralRegression", "HoeffdingTree",
						"IBk", "InputMappedClassifier", "IteratedSingleClassifierEnhancer", "J48", "JRip",
						"KStar", "LinearRegression", "LMT", "LMTNode", "Logistic", "LogisticBase",
						"LogitBoost", "LWL", "M5Base", "M5P", "M5Rules", "MultiClassClassifier",
						"MultiClassClassifierUpdateable", "MultilayerPerceptron", "MultipleClassifiersCombiner",
						"MultiScheme", "NaiveBayes", "NaiveBayesMultinomial", "NaiveBayesMultinomialText",
						"NaiveBayesMultinomialUpdateable", "NaiveBayesUpdateable", "NeuralNetwork", "OneR",
						"ParallelIteratedSingleClassifierEnhancer", "ParallelMultipleClassifiersCombiner",
						"PART", "PMMLClassifier", "PreConstructedLinearModel", "RandomCommittee",
						"RandomForest", "RandomizableClassifier", "RandomizableFilteredClassifier",
						"RandomizableIteratedSingleClassifierEnhancer", "RandomizableMultipleClassifiersCombiner",
						"RandomizableParallelIteratedSingleClassifierEnhancer",
						"RandomizableParallelMultipleClassifiersCombiner",
						"RandomizableSingleClassifierEnhancer", "RandomSubSpace", "RandomTree",
						"Regression", "RegressionByDiscretization", "REPTree", "RuleNode",
						"RuleSetModel", "SerializedClassifier", "SGD", "SGDText", "SimpleLinearRegression",
						"SimpleLogistic", "SingleClassifierEnhancer", "SMO", "SMOreg", "Stacking",
						"SupportVectorMachineModel", "TreeModel", "Vote", "VotedPerceptron", "ZeroR" };
		for (String item : classifierItems) {
			cbClassifiers.addItem(item);
		}
		cbClassifiers.setSelectedItem("BayesNet");
		// cbClassifiers.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

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
		} else if (event.getSource().equals(saveBrowseButton)) {
			browseFeatureValuesSavePath();
		} else if (event.getSource().equals(loadModelBrowseButton)) {
			browseModelLoadPath();
		} else if (event.getSource().equals(loadFileBrowseButton)) {
			browseFileLoadPath();
		}
	}

	/* PRIVATE METHODS ******************************************************** */
	/**
	 * Extract the features from all of the files added in the GUI. Use the features and feature settings entered in the
	 * GUI. Save the results in a feature_vector_file and classify features used in a stored model. Sergio Revueltas
	 */
	private void classifyInstances(boolean toClassify) {
		try {
			// Get the control parameters
			boolean save_features_for_each_window = false;
			boolean save_overall_recording_features = true;
			// String feature_values_save_path = arffSavePathTextField.getText();
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
						controller.getFrame(),
						"The Java Runtime ran out of memory. Please rerun this program\n"
								+ "with a higher amount of memory assigned to the Java Runtime heap.",
						"ERROR", JOptionPane.ERROR_MESSAGE);
			else if (t instanceof Exception) {
				Exception e = (Exception) t;
				JOptionPane.showMessageDialog(controller.getFrame(), e.getMessage(), "ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Extract the features from all of the files added in the GUI. Use the features and feature settings entered in the
	 * GUI. Save the results in a feature_vector_file and the features used in a feature_key_file. Daniel McEnnis
	 * 05-09-05 Moved guts into FeatureModel Edited by Sergio Revueltas
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
								controller.getFrame(),
								"The Java Runtime ran out of memory. Please rerun this program\n"
										+ "with a higher amount of memory assigned to the Java Runtime heap.",
								"ERROR", JOptionPane.ERROR_MESSAGE);
			else if (t instanceof Exception) {
				Exception e = (Exception) t;
				JOptionPane.showMessageDialog(controller.getFrame(), e.getMessage(), "ERROR",
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
		String path = chooseModelPath();
		if (path != null)
			loadModelTextField.setText(path);

	}
	
	private void browseFileLoadPath() {
		String path = chooseFilePath();
		if (path != null)
			loadFileToClassifyTextField.setText(path);

	}

	private String chooseFilePath() {
		// Create the JFileChooser if it does not already exist
				if (load_file_chooser == null) {
					load_file_chooser = new JFileChooser();
					load_file_chooser.setCurrentDirectory(new File("audioFiles/"));
					load_file_chooser.setFileFilter(new FileFilterAudio());
					load_file_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					load_file_chooser.setMultiSelectionEnabled(false);
					load_file_chooser.setLocation(30, 30);
				}

				// Process the user's entry
				String path = null;
				int dialog_result = load_file_chooser.showOpenDialog(this.controller.getFrame());
				// only do if OK chosen
				if (dialog_result == JFileChooser.APPROVE_OPTION) {
					// Get the file the user chose
					File to_load_model = load_file_chooser.getSelectedFile();

					// Make sure has .model extension
					path = to_load_model.getPath();
					int pos = path.lastIndexOf(".");
					String ext = path.substring(pos, path.length());
					// String ext = jAudioFeatureExtractor.GeneralTools.StringMethods.getExtension(path);
					if (ext == null) {
						path += ".wav";
					} else if (!ext.equals(".wav")) {
						String tmpPath = path.substring(0, pos);
						path = tmpPath + ".wav";
					}
					loadFileToClassifyTextField.setText(path);
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
	private String chooseModelPath() {
		// Create the JFileChooser if it does not already exist
		if (load_model_chooser == null) {
			load_model_chooser = new JFileChooser();
			load_model_chooser.setCurrentDirectory(new File("exportedFeatureValues/"));
			load_model_chooser.setFileFilter(new FileFilterMODEL());
			load_model_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			load_model_chooser.setMultiSelectionEnabled(false);
			load_model_chooser.setLocation(30, 30);
		}

		// Process the user's entry
		String path = null;
		int dialog_result = load_model_chooser.showOpenDialog(this.controller.getFrame());
		// only do if OK chosen
		if (dialog_result == JFileChooser.APPROVE_OPTION) {
			// Get the file the user chose
			File to_load_model = load_model_chooser.getSelectedFile();

			// Make sure has .model extension
			path = to_load_model.getPath();
			int pos = path.lastIndexOf(".");
			String ext = path.substring(pos, path.length());
			// String ext = jAudioFeatureExtractor.GeneralTools.StringMethods.getExtension(path);
			if (ext == null) {
				path += ".model";
			} else if (!ext.equals(".xml")) {
				String tmpPath = path.substring(0, pos);
				path = tmpPath + ".model";
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
		int dialog_result = save_file_chooser.showSaveDialog(this.controller.getFrame());
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
								controller.getFrame(),
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
