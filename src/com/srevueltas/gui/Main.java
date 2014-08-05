package com.srevueltas.gui;

import jAudioFeatureExtractor.Controller;
import jAudioFeatureExtractor.OuterFrame;


public class Main {
	
	public static void main(String[] args) {
		Controller c = new Controller();
		new OuterFrame(c);
		
		//new MainWindow();
	}
	
	
	/*
	public static void main(String[] args) {
		Controller c = new Controller();
		new OuterFrame(c);
		
		try {
			when_values_are_10_then_dba_is_10();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("------------End of test");
	}
	
	public static void when_values_are_10_then_dba_is_10() throws Exception{
		DensityBasedAverage dba = new DensityBasedAverage();
		Mean mean = new Mean();
		double [][][] values = new double[100][1][10];
		
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				for (int k = 0; k < values[i][j].length; k++) {
					if (k == 0 && (i)%2 == 0){
						values[i][j][k] = -10.0;
					} else {
						values[i][j][k] = 10.0;						
					}
				}
			}
		}
		dba.aggregate(values);
		mean.aggregate(values);

		double[] results = dba.getResults();
		double[] results2 = mean.getResults();

		
		System.out.println("DBA:");
		for (int i = 0; i < results.length; i++) {
			System.out.print(results[i] + ", ");
		}
		System.out.println();
		
		System.out.println("MEAN");
		for (int i = 0; i < results.length; i++) {
			System.out.print(results2[i] + ", ");
		}
		System.out.println();
	}
	
	*/
}
