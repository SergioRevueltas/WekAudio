package com.srevueltas.datamining;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;

import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

public class WekaManager {

	private String arffPath;
	private String modelPath;
	private String[] attributes;

	public WekaManager(String arffPath, String modelPath, String[] attributes) {
		this.arffPath = arffPath;
		this.modelPath = modelPath;
		this.attributes = attributes;
	}

	/**
	 * amb_A, amb_B, amb_C
	 * 
	 * @param trainSetName into exportedFeatureValues folder
	 * @param at attributes of the new instance which is going to be classified
	 * @return weka class value or -1 if error
	 * @throws Exception
	 */
	public static String classify(String trainSetName, double[] at) throws Exception {
		String arffPath = "exportedFeatureValues/" + trainSetName + ".arff";
		String modelPath = "exportedFeatureValues/" + trainSetName + ".model";
		String classLabel = "-1";
		try {
			// Read instances of train
			BufferedReader br = new BufferedReader(new FileReader(arffPath));
			Instances instancias = new Instances(br);
			br.close();
			if (instancias.numAttributes() - 1 != at.length) {
				return "-1";
			}
			// set class index
			if (instancias.classIndex() == -1)
				instancias.setClassIndex(instancias.numAttributes() - 1);

			// Instance instanceToClassify = (Instance) instancias.instance(instancias.numInstances()-1).copy();

			// Empty instance sets weight to one, all values to be missing, and the reference to the dataset to null.
			Instance instanceToClassify = (Instance) new DenseInstance(instancias.numAttributes());
			for (int i = 0; i < instancias.numAttributes() - 1; i++) {
				instanceToClassify.setValue(i, at[i]);
			}
			// set dataset
			instanceToClassify.setDataset(instancias);
			// optional because of DenseInstance initialization
			instanceToClassify.setClassValue(Utils.missingValue());
			// deserialize model
			Classifier cls = null;
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(modelPath));
			cls = (Classifier) ois.readObject();
			ois.close();
			// build classifier
			cls.buildClassifier(instancias);
			// classify
			double classValue = -1;
			classValue = cls.classifyInstance(instanceToClassify);
			classLabel = instancias.classAttribute().value((int) classValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classLabel;

	}

}
