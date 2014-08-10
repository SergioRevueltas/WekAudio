package com.srevueltas.test;

import jAudioFeatureExtractor.Controller;
import jAudioFeatureExtractor.Aggregators.DensityBasedAverage;
import jAudioFeatureExtractor.Aggregators.Mean;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestDensityBasedAverage {
	
	Controller c;
	DensityBasedAverage dba; 
	Mean mean;
	
	@Before
	public void setUp() {
		try {
			c = new Controller();
			dba = new DensityBasedAverage();	
			mean = new Mean();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	@Test
	public void when_values_are_zero_then_dba_is_zero() throws Exception{
		double [][][] values = new double[100][1][1];
		
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				for (int k = 0; k < values[i][j].length; k++) {
					values[i][j][k] = 0.0;
				}
			}
		}
		dba.aggregate(values);
		double[] expected = new double[]{0.0};
		double[] results = dba.getResults();
		Assert.assertEquals(Arrays.equals(expected, results), true);
	}
	
	@Test
	public void when_values_are_1_then_dba_is_1() throws Exception{	
		double [][][] values = new double[10][1][1];
		
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				for (int k = 0; k < values[i][j].length; k++) {
					values[i][j][k] = 1.0;
				}
			}
		}
		dba.aggregate(values);
		double[] results = dba.getResults();
		
		System.out.println("Expected: 1.0");
		System.out.println("\nResults: ");
		printArray(results);
		Assert.assertEquals(1.0, results[0], 0);
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void when_values_are_simetric_to_zero_then_mean_is_zero() throws Exception{	
		double [][][] values = new double[200][1][1];
		int count = -100;
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				for (int k = 0; k < values[i][j].length; k++) {
					values[i][j][k] = count;
					if (count != -1){
						count++;
					} else {
						count += 2;
					}
				}
			}
		}
		mean.aggregate(values);
		dba.aggregate(values);
		double[] dbaResults = dba.getResults();
		double[] meanResults = mean.getResults();
	
		Assert.assertEquals(0.0, dbaResults[0], 0);
		Assert.assertArrayEquals(meanResults, dbaResults, 0);
	}
	
	@Test
	public void when_values_are_simetric_to_zero_then_dba_is_zero() throws Exception{
		double [][][] values = new double[100][1][1];
		
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				for (int k = 0; k < values[i][j].length; k++) {
					if (k == 0 && (i)%2 == 0){
						values[i][j][k] = -100.0;
					} else {
						values[i][j][k] = 100.0;						
					}
				}
			}
		}
		dba.aggregate(values);
		mean.aggregate(values);

		double[] dbaResults = dba.getResults();
		double[] meanResults = mean.getResults();
		Assert.assertArrayEquals(meanResults, dbaResults, 0);

	}
	
	private void printArray(double[] data) {
		for (int i = 0; i < data.length; i++) {
			System.out.print(data[i] + ", ");
		}
		System.out.println("");
	}
	
}
