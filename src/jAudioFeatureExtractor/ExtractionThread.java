package jAudioFeatureExtractor;

import jAudioFeatureExtractor.DataTypes.RecordingInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.srevueltas.datamining.WekaManager;
import com.srevueltas.datamining.WekaStatistic;
import com.srevueltas.gui.CustomJTextArea;

/**
 * This is a thread for executing the DataModel.extractFeatures without tying up the swing dispatch thread.
 * 
 * @author Daniel McEnnis
 */
public class ExtractionThread extends Thread implements Updater {

	Runnable suspendGUI, resumeGUI;

	ErrorGUI errorGUI;

	UpdateGUI updateGUI;

	Controller controller;

	OuterFrame outerFrame;

	boolean perFile;

	boolean perWindow;

	String valuesSavePath;

	//String definitionSavePath;

	int windowSize;

	double windowOverlap;

	boolean hasRun = false;

	private ProgressFrame progressFrame;

	boolean toClassify;
	
	private String modelLoadPath;
	
	private ArrayList<String> classificationResults;

	private String classifierName;

	/**
	 * This constructor constructs the thread, partially preparing it for execution
	 * 
	 * @param c Near global container for numerous controller and model objects
	 * @param of Link to outerframe of the gui. Used to disable the main frame to prevent race conditions in the feature
	 *            settings.
	 */
	public ExtractionThread(Controller c, OuterFrame of) {

		controller = c;
		outerFrame = of;
		suspendGUI = new Runnable() {

			public void run() {
				outerFrame.setEnabled(false);
			}
		};

		resumeGUI = new Runnable() {

			public void run() {
				progressFrame.setVisible(false);
				outerFrame.setEnabled(true);
				outerFrame.toFront();
				// train
				if (!toClassify && classificationResults != null) {
					CustomJTextArea trainingTextArea = outerFrame.dataMiningPanel.getTrainningResultsTextArea();
					trainingTextArea.setText(controller.getWekaStatistics().getConfusionMatrix() +
								controller.getWekaStatistics().getSummary()
								//controller.getWekaStatistics().getStaticsDetails()
								);
					trainingTextArea.setVisible(true);
					trainingTextArea.setCaretPosition(0);
					
					JOptionPane.showMessageDialog(controller.getFrame(),
							"Features successfully extracted and saved.", "Congrats",
							JOptionPane.INFORMATION_MESSAGE);
							
				} //classify 
				else if (classificationResults != null){
					CustomJTextArea classificationTextArea = outerFrame.dataMiningPanel.getClassificationResultsTextArea();
					if (classificationResults.size() > 0) {
						classificationTextArea.setText(classificationResults.get(0));
						outerFrame.dataMiningPanel.getLblClassificationDone().setVisible(true);
					} else {
						classificationTextArea.setText("No data available.");
					}
					classificationTextArea.setVisible(true);
					//classificationTextArea.setCaretPosition(0);
					/*					
					JOptionPane.showMessageDialog(controller.getFrame(),
							"Classification done.", "Info",
							JOptionPane.INFORMATION_MESSAGE);
					*/
				}
			}
		};

		updateGUI = new UpdateGUI();

		c.dm_.setUpdater(this);
		progressFrame = new ProgressFrame();
		errorGUI = new ErrorGUI(progressFrame);
		classificationResults = null;
	}

	/**
	 * This is the method to finish preparing the thread for execution
	 * 
	 * @param perFile Should features be extracted over the entire file
	 * @param perWindow Should features be extracted on a window by window basis
	 * @param valuesSavePath File to save extracted features
	 * @param definitionSavePath File to save descriptions of the features extracted
	 * @param windowSize Size of the analysis window in samples
	 * @param windowOverlap Percent of the window that is duplicated between analysis windows
	 * @param toClassify
	 * @param modelLoadPath 
	 * @param classifierName 
	 */
	public void setup(boolean perFile, boolean perWindow,
			String valuesSavePath, String definitionSavePath, int windowSize,
			double windowOverlap, boolean toClassify, String modelLoadPath, String classifierName) {
		this.perFile = perFile;
		this.perWindow = perWindow;
		this.valuesSavePath = valuesSavePath;
		//this.definitionSavePath = definitionSavePath;
		this.windowSize = windowSize;
		this.windowOverlap = windowOverlap;
		this.toClassify = toClassify;
		this.modelLoadPath = modelLoadPath;
		this.classifierName = classifierName;
	}

	/**
	 * Execute the thread, suspending the main frame, extracting the features, then enabling the main frame.
	 */
	public void run() {
		try {
			SwingUtilities.invokeAndWait(suspendGUI);
			
			if (!toClassify) { //train
				controller.dm_.validateFile(valuesSavePath);
				File feature_values_save_file = new File(valuesSavePath);
				FileOutputStream values_to = new FileOutputStream(feature_values_save_file);
				controller.dm_.featureValue = values_to;
				classificationResults = controller.dm_.extractAndClassify(windowSize, windowOverlap,
						controller.samplingRateAction.getSamplingRate(),
						controller.normalise.isSelected(), perWindow, perFile,
						controller.dm_.recordingsInfo, controller.outputTypeAction
						.getSelected(), toClassify, modelLoadPath);
			} else { //classify
				classificationResults = controller.dm_.extractAndClassify(windowSize, windowOverlap,
						controller.samplingRateAction.getSamplingRate(),
						controller.normalise.isSelected(), perWindow, perFile,
						new RecordingInfo[]{controller.dm_.recordinInfo}, controller.outputTypeAction
								.getSelected(), toClassify, modelLoadPath);
			}
			
			if (!toClassify && classificationResults != null){
				WekaStatistic wekaStatistic = WekaManager.saveModel(controller, valuesSavePath, classifierName);
				controller.setWekaStatistics(wekaStatistic);
			}
			SwingUtilities.invokeLater(resumeGUI);
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(controller.getFrame(), 
					"No valid arff save path selected.", "Info",
					JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
			SwingUtilities.invokeLater(resumeGUI);
		}
		hasRun = true;
	}

	class UpdateGUI implements Runnable {

		int numberOfFiles;
		int file;
		int thisFileLength = 0;
		int pos;

		public void setLengths(int file) {
			numberOfFiles = file;
		}

		public void setMaxWindows(int maxWin) {
			thisFileLength = maxWin;
		}

		public void setPos(int file, int pos) {
			this.file = file;
			this.pos = pos;
		}

		public void setPos(int pos) {
			this.pos = pos;
		}

		public void run() {
			progressFrame.setVisible(true);
			progressFrame.fileProgressBar.setMaximum(thisFileLength);
			progressFrame.overallProgressBar.setMaximum(numberOfFiles);
			progressFrame.fileProgressBar.setValue(pos);
			progressFrame.overallProgressBar.setValue(file);
		}
	}

	/**
	 * This is part of the Updater interface. It notifies the gui that a file has been completed.
	 */
	public void announceUpdate(int fileNumber, int fileDone) {
		updateGUI.setPos(fileNumber, fileDone);
		SwingUtilities.invokeLater(updateGUI);
	}

	/**
	 * This is part of the Updater interface. It notifies the gui of an increase in the amount of the file processed.
	 */
	public void announceUpdate(int fileDone) {
		updateGUI.setPos(fileDone);
		SwingUtilities.invokeLater(updateGUI);
	}

	/**
	 * This is part of the Updater interface. It is used to set the total number of files to be processed.
	 */
	public void setNumberOfFiles(int files) {
		updateGUI.setLengths(files);
	}

	/**
	 * This is part of the Updater interface. It is used to notify the gui of the total size of the file (in windows of
	 * data).
	 */
	public void setFileLength(int window) {
		updateGUI.setMaxWindows(window);
	}

	/**
	 * Used to prevent this thread from executing twice.
	 * 
	 * @return whether or not this thread has run before
	 */
	public boolean hasRun() {
		return hasRun();
	}
}
