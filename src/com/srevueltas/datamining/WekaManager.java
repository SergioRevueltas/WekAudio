package com.srevueltas.datamining;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.bayes.BayesNet;
import weka.classifiers.functions.MultilayerPerceptron;
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
	 * @param bd into exportedFeatureValues folder 
	 * @param args 
	 * @return weka class value or -1 if error
	 * @throws Exception
	 */
	public static String classify(String bd, String[] attributes) throws Exception{
		String arffPath = "exportedFeatureValues/" + bd + ".arff"; 
		String modelPath = "exportedFeatureValues/" + bd + ".model"; 
		Instances instancias = new Instances(new BufferedReader(new FileReader(arffPath)));
		
		if(instancias.numAttributes()-1 != attributes.length){
			return "-1";
		}
		
		if (instancias.classIndex()==-1)
			instancias.setClassIndex(instancias.numAttributes()-1);

		for (int i = 0; i < instancias.numAttributes()-1; i++){
			if (instancias.attribute(i).isNumeric())
				instancias.instance(0).setValue(i, Double.parseDouble(attributes[i]));
			else 	
				instancias.instance(0).setValue(i, attributes[i]);
		}
	
		double classValue = 0;
		MultilayerPerceptron redNeuronal = (MultilayerPerceptron) weka.core.SerializationHelper.read(modelPath);
		classValue = redNeuronal.classifyInstance(instancias.lastInstance());
		return new String(instancias.classAttribute().value((int)classValue));

	}


	public static String classify(String bd, double[] at) throws Exception{
		String arffPath = "exportedFeatureValues/" + bd + ".arff"; 
		String modelPath = "exportedFeatureValues/" + bd + ".model"; 
		BufferedReader br = new BufferedReader(new FileReader(arffPath));
		Instances instancias = new Instances(br);
		br.close();
		if(instancias.numAttributes()-1 != at.length){
			return "-1";
		}
		
		if (instancias.classIndex()==-1)
			instancias.setClassIndex(instancias.numAttributes()-1);
		int i = 0;
		for (; i < instancias.numAttributes()-1; i++){
			instancias.instance(0).setValue(i, at[i]);
		}
		
		double classValue = -1;
		BayesNet bayesNet = (BayesNet) weka.core.SerializationHelper.read(modelPath);
		bayesNet.buildClassifier(instancias);
		//MultilayerPerceptron redNeuronal = (MultilayerPerceptron) weka.core.SerializationHelper.read(modelPath);
		try {
			classValue = bayesNet.classifyInstance(instancias.instance(0));			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(instancias.classAttribute().value((int)classValue));
		
	}
	
	
	
}
