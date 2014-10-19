package com.srevueltas.datamining;

import jAudioFeatureExtractor.Controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.KStar;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.ClassificationViaRegression;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.JRip;
import weka.classifiers.rules.OneR;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.trees.HoeffdingTree;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.core.Debug.Random;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.UnsupportedAttributeTypeException;
import weka.core.Utils;

/**
 * Class for interact with weka
 * @author Sergio Revueltas
 */
public class WekaManager {

	/**
	 * Serialize trainning set of instances into a BayesNet model
	 * @param controller 
	 * @param classifierName 
	 * @return 
	 */
	public static WekaStatistic saveModel(Controller controller, String arffPath, String classifierName) {
		int pos = arffPath.lastIndexOf(".");
		String tmp = arffPath.substring(0, pos);
		String modelPath = tmp + ".model";
		
		Classifier cls = loadSelectedClassifier(classifierName);

		System.out.println("SELECTED CLASSIFIER: " + cls.getClass().toString());
		
		Instances inst = null;
		ObjectOutputStream oos = null;
		try {
			// train
			inst = new Instances(new BufferedReader(new FileReader(arffPath)));
			inst.setClassIndex(inst.numAttributes() - 1);
			cls.buildClassifier(inst);
			// serialize model
			oos = new ObjectOutputStream(new FileOutputStream(modelPath));
			oos.writeObject(cls);
			oos.flush();
			oos.close();			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAttributeTypeException e) {
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(controller.getFrame(), "Weka cannot build selected classifier.\nPlease see the instructions in the Help menu.", "Info",
					JOptionPane.INFORMATION_MESSAGE);
			System.out.println(e.getMessage());
		}
		
		WekaStatistic wekaStatistic = runEval(controller, cls, inst);
		return wekaStatistic;

	}

	private static Classifier loadSelectedClassifier(String classifierName) {
		Classifier cls;
		switch (classifierName) {
		case "AdaBoostM1":
			cls = new AdaBoostM1();
			break;
		case "BayesNet":
			cls = new BayesNet();
			break;
		case "ClassificationViaRegression":
			cls = new ClassificationViaRegression();
			break;
		case "CostSensitiveClassifier":
			cls = new CostSensitiveClassifier();
			break;
		case "DecisionTable":
			cls = new DecisionTable();
			break;
		case "FilteredClassifier":
			cls = new FilteredClassifier();
			break;
		case "HoeffdingTree":
			cls = new HoeffdingTree();
			break;
		case "InputMappedClassifier":
			cls = new InputMappedClassifier();
			break;
		case "IBk":
			cls = new IBk();
			break;
		case "J48":
			cls = new J48();
			break;
		case "JRip":
			cls = new JRip();
			break;
		case "KStar":
			cls = new KStar();
			break;
		case "NaiveBayes":
			cls = new NaiveBayes();
			break;
		case "OneR":
			cls = new OneR();
			break;
		case "RandomForest":
			cls = new RandomForest();
			break;
		case "REPTree":
			cls = new REPTree();
			break;
		case "ZeroR":
			cls = new ZeroR();
			break;
		default:
			cls = new BayesNet();
			break;
		}
		return cls;
	}

	private static WekaStatistic runEval(Controller controller, Classifier cls, Instances inst) {
		Evaluation eval = null;
		WekaStatistic wekaStatistic = null;
		try {
			eval = new Evaluation(inst);
			Random rand = new Random(1);  // using seed = 1
			int folds = 10;
			eval.crossValidateModel(cls, inst, folds, rand);
			
			wekaStatistic = new WekaStatistic(cls, eval);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//String resultInfo = cls.toString();
		return wekaStatistic;
	}

	/**
	 * Classify the attributes with the stored model.
	 * NOTE: attributes length and order have to be the same that the model
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
