package com.srevueltas.datamining;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.bayes.BayesNet;
import weka.core.Instances;


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
	 * @param train into exportedFeatureValues folder 
	 * @param args 
	 * @return weka class value or -1 if error
	 * @throws Exception
	 */
	public static String classify(String train, double[] at) throws Exception{
		String arffPath = "exportedFeatureValues/" + train + ".arff"; 
		String modelPath = "exportedFeatureValues/" + train + ".model"; 
		BufferedReader br = new BufferedReader(new FileReader(arffPath));
		Instances instancias = new Instances(br);
		br.close();
		if(instancias.numAttributes()-1 != at.length){
			return "-1";
		}
		if (instancias.classIndex()==-1)
			instancias.setClassIndex(instancias.numAttributes()-1);
		for (int i = 0; i < instancias.numAttributes()-1; i++){
			instancias.instance(0).setValue(i, at[i]);
		}
		double classValue = -1;
		BayesNet bayesNet = (BayesNet) weka.core.SerializationHelper.read(modelPath);
		//bayesNet.buildClassifier(instancias);
		//MultilayerPerceptron redNeuronal = (MultilayerPerceptron) weka.core.SerializationHelper.read(modelPath);
		try {
			classValue = bayesNet.classifyInstance(instancias.instance(0));			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(instancias.classAttribute().value((int)classValue));
		
	}
	
	
	
}
