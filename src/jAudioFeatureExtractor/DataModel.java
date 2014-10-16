package jAudioFeatureExtractor;

import jAudioFeatureExtractor.ACE.DataTypes.FeatureDefinition;
import jAudioFeatureExtractor.ACE.XMLParsers.XMLDocumentParser;
import jAudioFeatureExtractor.Aggregators.Aggregator;
import jAudioFeatureExtractor.Aggregators.AggregatorContainer;
import jAudioFeatureExtractor.AudioFeatures.Derivative;
import jAudioFeatureExtractor.AudioFeatures.FeatureExtractor;
import jAudioFeatureExtractor.AudioFeatures.Mean;
import jAudioFeatureExtractor.AudioFeatures.MetaFeatureFactory;
import jAudioFeatureExtractor.AudioFeatures.StandardDeviation;
import jAudioFeatureExtractor.DataTypes.RecordingInfo;
import jAudioFeatureExtractor.jAudioTools.AudioMethodsPlayback;
//import jAudioFeatureExtractor.jAudioTools.AudioSamples;
import jAudioFeatureExtractor.jAudioTools.FeatureProcessor;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JOptionPane;

import com.srevueltas.datamining.WekaManager;

/**
 * All components that are not tightly tied to GUI. Used by console interface as well as the GUI interface.
 * 
 * @author Daniel McEnnis
 */
public class DataModel {

	/**
	 * Reference to use for piping progress updates
	 */
	public ModelListener ml_;

	/**
	 * Handle for killing in-progress analysis
	 */
	public Cancel cancel_;

	/**
	 * list of which features are enabled by default
	 */
	public boolean[] defaults;

	/**
	 * list of all features available
	 */
	public FeatureExtractor[] features;

	/**
	 * Mapping between aggregator names and aggregator prototypes
	 */
	public java.util.HashMap<String, Aggregator> aggregatorMap;

	/**
	 * List of aggreggators to apply
	 * <p>
	 * Must be set externally. Duplicates of a class are permitted (hence not a map) but each entry in the array must be
	 * fully initialized prior to calling extract().
	 */
	public Aggregator[] aggregators;

	/**
	 * wrapper object for the aggregators. This reference is null until a file extraction has been performed.
	 */
	public AggregatorContainer container = null;

	/**
	 * whether or a feature is a derived feature or not
	 */
	public boolean[] is_primary;

	/**
	 * cached FeatureDefinitions for all available features
	 */
	public FeatureDefinition[] featureDefinitions;

	/**
	 * info on all recordings that are made avaiable for feature extraction
	 */
	public RecordingInfo[] recordingsInfo;
	/**
	 * info on all recordings that are made avaiable for feature extraction
	 */
	public RecordingInfo recordinInfo;

	/**
	 * thread for playing back a recording
	 */
	public AudioMethodsPlayback.PlayThread playback_thread;

	Updater updater = null;

	public OutputStream featureKey = null;

	public OutputStream featureValue = null;

	public ArrayList<double[]> feature_values_per_file = null;

	/**
	 * Initializes each of the arrays with all available efeatures. Place to add new features.
	 * 
	 * @param c reference to a controller that will handle table updates.
	 */
	public DataModel(String featureXMLLocation, ModelListener c) {
		ml_ = c;
		cancel_ = new Cancel();
		LinkedList<MetaFeatureFactory> metaExtractors = new LinkedList<MetaFeatureFactory>();

		metaExtractors.add(new Derivative());
		metaExtractors.add(new Mean());
		metaExtractors.add(new StandardDeviation());
		metaExtractors.add(new Derivative(new Mean()));
		metaExtractors.add(new Derivative(new StandardDeviation()));

		LinkedList<FeatureExtractor> extractors = new LinkedList<FeatureExtractor>();
		LinkedList<Boolean> def = new LinkedList<Boolean>();
		aggregatorMap = new java.util.HashMap<String, Aggregator>();

		try {

			Object[] lists = (Object[]) XMLDocumentParser.parseXMLDocument(
					featureXMLLocation, "feature_list");
			extractors = (LinkedList<FeatureExtractor>) lists[0];
			def = (LinkedList<Boolean>) lists[1];
			Aggregator[] aggArray = ((LinkedList<Aggregator>) lists[2]).toArray(new Aggregator[] {});

			for (int i = 0; i < aggArray.length; ++i) {
				aggregatorMap.put(aggArray[i].getAggregatorDefinition().name,
						aggArray[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		populateMetaFeatures(metaExtractors, extractors, def);
		feature_values_per_file = new ArrayList<>();
	}

	void populateMetaFeatures(LinkedList<MetaFeatureFactory> listMFF,
			LinkedList<FeatureExtractor> listFE, LinkedList<Boolean> def) {
		LinkedList<Boolean> tmpDefaults = new LinkedList<Boolean>();
		LinkedList<FeatureExtractor> tmpFeatures = new LinkedList<FeatureExtractor>();
		LinkedList<Boolean> isPrimaryList = new LinkedList<Boolean>();
		Iterator<FeatureExtractor> lFE = listFE.iterator();
		Iterator<Boolean> lD = def.iterator();
		while (lFE.hasNext()) {
			FeatureExtractor tmpF = lFE.next();
			Boolean tmpB = lD.next();
			tmpFeatures.add(tmpF);
			tmpDefaults.add(tmpB);
			isPrimaryList.add(new Boolean(true));
			tmpF.setParent(this);
			if (tmpF.getFeatureDefinition().dimensions != 0) {
				Iterator<MetaFeatureFactory> lM = listMFF.iterator();
				while (lM.hasNext()) {
					MetaFeatureFactory tmpMFF = lM.next();
					FeatureExtractor tmp = tmpMFF
							.defineFeature((FeatureExtractor) tmpF.clone());
					tmp.setParent(this);
					tmpFeatures.add(tmp);
					tmpDefaults.add(new Boolean(false));
					isPrimaryList.add(new Boolean(false));
				}
			}
		}
		this.features = tmpFeatures.toArray(new FeatureExtractor[1]);
		Boolean[] defaults_temp = tmpDefaults.toArray(new Boolean[1]);
		Boolean[] is_primary_temp = isPrimaryList.toArray(new Boolean[] {});
		this.defaults = new boolean[defaults_temp.length];
		is_primary = new boolean[defaults_temp.length];
		for (int i = 0; i < this.defaults.length; i++) {
			this.defaults[i] = defaults_temp[i].booleanValue();
			is_primary[i] = is_primary_temp[i].booleanValue();
		}
		this.featureDefinitions = new FeatureDefinition[this.defaults.length];
		for (int i = 0; i < this.featureDefinitions.length; ++i) {
			this.featureDefinitions[i] = features[i].getFeatureDefinition();
		}

	}

	/**
	 * This is the function called when features change in such a way as the main display becomes out of date. WHen
	 * executed from the consol, this value is null.
	 */
	public void updateTable() {
		if (ml_ != null) {
			ml_.updateTable();
		}
	}

	/**
	 * Function for executing the feature extraction process against a set of files.
	 * 
	 * @param windowSize Size of the window in samples
	 * @param windowOverlap Percent of the window to be overlapped - must be between 0 and 1.
	 * @param samplingRate Sample rate given in samples per second
	 * @param normalise indicates whether or not the file should be normalised before feature extraction
	 * @param perWindowStats should features be extracted for every window
	 * @param overallStats should features be extracted over the entire window
	 * @param destinationFV file where the extracted features should be stored
	 * @param destinationFK file where descriptions of features extracted should be stored
	 * @param info list of the files that are to be analyzed
	 * @param arff output format of the data
	 * @param toClassify
	 * @param modelLoadPath
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> extractAndClassify(int windowSize, double windowOverlap,
			double samplingRate, boolean normalise, boolean perWindowStats,
			boolean overallStats, RecordingInfo[] info, int arff, boolean toClassify, String modelLoadPath)
			throws Exception {
		// Get the control parameters
		boolean save_features_for_each_window = perWindowStats;
		boolean save_overall_recording_features = overallStats;
		int window_size = windowSize;
		double window_overlap = windowOverlap;
		double sampling_rate = samplingRate;
		int outputType = arff;
		// Get the audio recordings to extract features from and throw an
		// exception
		// if there are none
		RecordingInfo[] recordings = info;
		if (recordings == null){
			return null;
		}
		ArrayList<String> listFileNames = null;
		// Obtain set of file names to create classes in arff
		if (!toClassify) {
			Set<String> setFileNames = new HashSet<String>();
			boolean areTypos = false;
			for (RecordingInfo r : recordings) {
				
				try {
					setFileNames.add(r.file_path.substring(r.file_path.lastIndexOf("\\") + 1, r.file_path.lastIndexOf(" ")));					
				} catch (Exception e) {
					areTypos = true;
				}
				if (areTypos){
					JOptionPane.showMessageDialog(((Controller)ml_).getFrame(), "You select incorrect file names format.\nPlease see the instructions in the Help menu.", "Info",
							JOptionPane.INFORMATION_MESSAGE);
					return null;
				}
				
			}
			listFileNames = new ArrayList<String>(setFileNames);
			if (listFileNames.size() < 2){
				JOptionPane.showMessageDialog(((Controller)ml_).getFrame(), "You have to add at least 2 classes of sounds.\nPlease see the instructions in the Help menu.", "Info",
						JOptionPane.INFORMATION_MESSAGE);
				return null;
			}
			Collections.sort(listFileNames);
			System.out.println("SetFileNames: "+ listFileNames.toString());
		}
		if (updater != null) {
			updater.setNumberOfFiles(recordings.length);
		}
		container = new AggregatorContainer();
		if ((aggregators == null) || (aggregators.length == 0)) {
			aggregators = new Aggregator[3];
			aggregators[0] = new jAudioFeatureExtractor.Aggregators.Mean();
			aggregators[1] = new jAudioFeatureExtractor.Aggregators.StandardDeviation();
			aggregators[2] = new jAudioFeatureExtractor.Aggregators.AreaMoments();
			aggregators[2].setParameters(new String[] { "Area Method of Moments of MFCCs" }, new String[] { "" });
		}
		container.add(aggregators);

		// Prepare to extract features
		FeatureProcessor processor = new FeatureProcessor(window_size,
				window_overlap, sampling_rate, normalise, this.features,
				this.defaults, save_features_for_each_window,
				save_overall_recording_features, featureValue, featureKey,
				outputType, cancel_, container, toClassify);

		feature_values_per_file.clear();
		
		// Extract features from recordings one by one and save them
		for (int i = 0; i < recordings.length; i++) {
			File load_file = new File(recordings[i].file_path);
			// update progressbar
			if (updater != null) {
				updater.announceUpdate(i, 0);
			}
			processor.extractFeatures(load_file, updater, listFileNames);
			if (toClassify) {
				feature_values_per_file.add(container.getResultsToSingleArray());
			}
		}
		ArrayList<String> classPerFile = null;
		if (toClassify) {
			classPerFile = new ArrayList<String>();
			for (int i = 0; i < feature_values_per_file.size(); i++) {
				classPerFile.add("File " + i + ": "
						+ WekaManager.classify(modelLoadPath, feature_values_per_file.get(i)) + "\n");
			}
		} else {
			//to notify that all works fine
			classPerFile = new ArrayList<String>();
			// Finalize saved XML files
			processor.finalize();
		}
		return classPerFile;

		// JOptionPane.showMessageDialog(null,
		// "Features successfully extracted and saved.", "DONE",
		// JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Establish a listener for periodic updates on the feature extraction progress.
	 * 
	 * @param u
	 */
	public void setUpdater(Updater u) {
		this.updater = u;
	}

	public void validateFile(String values) throws Exception {
		File feature_values_save_file = new File(values);
		// Throw an exception if the given file paths are not writable. Involves
		// creating a blank file if one does not already exist.
		if (feature_values_save_file.exists())
			if (!feature_values_save_file.canWrite())
				throw new Exception("Cannot write to " + values + ".");
	}

}
