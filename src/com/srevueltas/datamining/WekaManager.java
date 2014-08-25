package com.srevueltas.datamining;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

/**
 * 
 * @author Sergio Revueltas
 *
 */
public class WekaManager {

	public static void saveModel(String arffPath) {
		int pos = arffPath.lastIndexOf(".");
		String tmp = arffPath.substring(0, pos);
		String modelPath = tmp + ".model";
		
		// create J48
		Classifier clsJ48 = new J48();
		
		// create a BayesNet
		Classifier cls = new BayesNet();

		// train
		Instances inst = null;
		try {
			inst = new Instances(new BufferedReader(new FileReader(arffPath)));
			inst.setClassIndex(inst.numAttributes() - 1);
			cls.buildClassifier(inst);
			
			// serialize model
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(modelPath));
			oos.writeObject(cls);
			oos.flush();
			oos.close();			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * amb_A, amb_B, amb_C
	 * 
	 * @param modelLoadPath into exportedFeatureValues folder
	 * @param attributes of the new instance which is going to be classified
	 * @return weka class value or -1 if error
	 * @throws Exception
	 */
	public static String classify(String modelLoadPath, double[] attributes) throws Exception {
		String modelPath = modelLoadPath;
		int pos = modelLoadPath.lastIndexOf(".");
		String tmp = modelLoadPath.substring(0, pos);
		String arffPath = tmp + ".arff";
		String classLabel = "-1";
		try {
			// Read instances of train
			BufferedReader br = new BufferedReader(new FileReader(arffPath));
			Instances instancias = new Instances(br);
			br.close();

			if (instancias.numAttributes() - 1 != attributes.length) {
				return "-1";
			}
			// set class index
			if (instancias.classIndex() == -1)
				instancias.setClassIndex(instancias.numAttributes() - 1);

			// Instance instanceToClassify = (Instance) instancias.instance(instancias.numInstances()-1).copy();

			// Empty instance sets weight to one, all values to be missing, and the reference to the dataset to null.
			Instance instanceToClassify = (Instance) new DenseInstance(instancias.numAttributes());
			for (int i = 0; i < instancias.numAttributes() - 1; i++) {
				instanceToClassify.setValue(i, attributes[i]);
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
