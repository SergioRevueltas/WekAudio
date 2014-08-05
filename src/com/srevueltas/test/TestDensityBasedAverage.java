package com.srevueltas.test;

import jAudioFeatureExtractor.Aggregators.DensityBasedAverage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;





public class TestDensityBasedAverage {
	
	DensityBasedAverage dba; 
	
	@Before
	public void setUp() throws Exception {
		try {
			dba = new DensityBasedAverage();			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	
	@Test
	public static void when_values_are_10_then_mean_is_10() throws Exception{
		DensityBasedAverage dba = new DensityBasedAverage();
		double [][][] values = new double[10][1][10];
		
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				for (int k = 0; k < values[i][j].length; k++) {
					values[i][j][k] = 10.0;
				}
			}
		}
		dba.aggregate(values);
		double[] expected = new double[]{10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0,10.0};
		double[] results = dba.getResults();
		
		System.out.println("Expected: " + expected);
		System.out.println("Results: " + results);
		
		Assert.assertArrayEquals(expected, results, 0);
	}
	
	
}
