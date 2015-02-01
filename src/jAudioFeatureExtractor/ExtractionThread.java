package jAudioFeatureExtractor;

import jAudioFeatureExtractor.DataTypes.RecordingInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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

	public static final int TRAIN = 0;
	public static final int CLASSIFY_ALL = 1;
	public static final int CLASSIFY_SINGLE_FILE = 2;
	
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

	private int extractionOption;
	
	private String modelLoadPath;
	
	private ArrayList<String> classificationResults;

	private String classifierName;
	
	private String time;
	
	private long a, b;

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
				if (extractionOption == TRAIN && classificationResults != null) {
					CustomJTextArea trainingTextArea = outerFrame.dataMiningPanel.getTrainningResultsTextArea();
					trainingTextArea.setText(controller.getWekaStatistics().getConfusionMatrix() +
								controller.getWekaStatistics().getSummary()
								//controller.getWekaStatistics().getStaticsDetails()
								);
					trainingTextArea.setVisible(true);
					trainingTextArea.setCaretPosition(0);
					String modelLoadPath = outerFrame.dataMiningPanel.getLoadModelTextField().getText();
					int pos = modelLoadPath.lastIndexOf("/");
					String classifierName = modelLoadPath.substring(pos+1, modelLoadPath.length());
					long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(b - a);
					time = timeSeconds + " seconds.";
					JOptionPane.showMessageDialog(controller.getFrame(),
							"Features successfully extracted and saved.\nBuilt " + classifierName + " classifier.\nTotal time: " + (b - a) + " ms.", "Congrats!!",
							JOptionPane.INFORMATION_MESSAGE);
							
				} //classify 
				else if (classificationResults != null){
					CustomJTextArea classificationTextArea = outerFrame.dataMiningPanel.getClassificationResultsTextArea();
					if (classificationResults.size() > 0) {
						if (extractionOption == CLASSIFY_ALL){
							controller.rtm_.fillTable(controller.dm_.recordingsInfo, classificationResults);
							controller.rtm_.fireTableDataChanged();
							JOptionPane.showMessageDialog(controller.getFrame(),
									"Classification done.", "Info",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							classificationTextArea.setText(classificationResults.get(0));
							outerFrame.dataMiningPanel.getLblClassificationDone().setVisible(true);
							outerFrame.repaint();
						}
					} else {
						classificationTextArea.setText("No data available.");
					}
					classificationTextArea.setVisible(true);
					//classificationTextArea.setCaretPosition(0);
					
				}
			}
		};

		updateGUI = new UpdateGUI();

		c.dm_.setUpdater(this);
		progressFrame = new ProgressFrame(c);
		errorGUI = new ErrorGUI(progressFrame);
		classificationResults = null;
	}

	
	public ProgressFrame getProgressFrame() {
		return progressFrame;
	}

	
	public void setProgressFrame(ProgressFrame progressFrame) {
		this.progressFrame = progressFrame;
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
	 * @param extractionOption
	 * @param modelLoadPath 
	 * @param classifierName 
	 */
	public void setup(boolean perFile, boolean perWindow,
			String valuesSavePath, String definitionSavePath, int windowSize,
			double windowOverlap, int extractionOption, String modelLoadPath, String classifierName) {
		this.perFile = perFile;
		this.perWindow = perWindow;
		this.valuesSavePath = valuesSavePath;
		//this.definitionSavePath = definitionSavePath;
		this.windowSize = windowSize;
		this.windowOverlap = windowOverlap;
		this.extractionOption = extractionOption;
		this.modelLoadPath = modelLoadPath;
		this.classifierName = classifierName;
	}

	/**
	 * Execute the thread, suspending the main frame, extracting the features, then enabling the main frame.
	 */
	public void run() {
		controller.feIsRunning = true;
		try {
			SwingUtilities.invokeAndWait(suspendGUI);
			a = System.currentTimeMillis();
			if (extractionOption == TRAIN) { //train
				controller.dm_.validateFile(valuesSavePath);
				File feature_values_save_file = new File(valuesSavePath);
				FileOutputStream values_to = new FileOutputStream(feature_values_save_file);
				controller.dm_.featureValue = values_to;
				classificationResults = controller.dm_.extractAndClassify(windowSize, windowOverlap,
						controller.samplingRateAction.getSamplingRate(),
						controller.normalise.isSelected(), perWindow, perFile,
						controller.dm_.recordingsInfo, controller.outputTypeAction
						.getSelected(), extractionOption, modelLoadPath);
			} else if (extractionOption == CLASSIFY_ALL) { //classify
				classificationResults = controller.dm_.extractAndClassify(windowSize, windowOverlap,
						controller.samplingRateAction.getSamplingRate(),
						controller.normalise.isSelected(), perWindow, perFile,
						controller.dm_.recordingsInfo, controller.outputTypeAction
								.getSelected(), extractionOption, modelLoadPath);
			} else { //classify
				classificationResults = controller.dm_.extractAndClassify(windowSize, windowOverlap,
						controller.samplingRateAction.getSamplingRate(),
						controller.normalise.isSelected(), perWindow, perFile,
						new RecordingInfo[]{controller.dm_.recordinInfo}, controller.outputTypeAction
								.getSelected(), extractionOption, modelLoadPath);
			}
			b = System.currentTimeMillis();
			if (extractionOption == TRAIN && classificationResults != null){
				WekaStatistic wekaStatistic = WekaManager.saveModel(controller, valuesSavePath, classifierName);
				controller.setWekaStatistics(wekaStatistic);
				int pos = valuesSavePath.lastIndexOf(".");
				String tmp = valuesSavePath.substring(0, pos);
				String modelPath = tmp + ".model";
				controller.getFrame().dataMiningPanel.getLoadModelTextField().setText(modelPath);
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
		controller.feIsRunning = true;
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
