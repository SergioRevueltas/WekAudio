package com.srevueltas.test;

import jAudioFeatureExtractor.Controller;
import jAudioFeatureExtractor.Aggregators.DensityBasedAverage;
import jAudioFeatureExtractor.Aggregators.Mean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
/**
 * Unit tests for DBA method
 * @author Sergio Revueltas
 */
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
		double[] results = dba.getResults();
		Assert.assertEquals(0.0, results[0], 0);
	}
	
	@Test
	public void when_values_are_1_then_dba_is_1() throws Exception{	
		double [][][] values = new double[100][1][1];
		
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				for (int k = 0; k < values[i][j].length; k++) {
					values[i][j][k] = 1.0;
				}
			}
		}
		dba.aggregate(values);
		double[] results = dba.getResults();
		Assert.assertEquals(1.0, results[0], 0);
	}
	
	@Test
	public void when_values_are_big_then_dba_is_like_mean() throws Exception{	
		double [][][] values = new double[100][1][1];
		
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				for (int k = 0; k < values[i][j].length; k++) {
					values[i][j][k] = Integer.MAX_VALUE;
				}
			}
		}
		dba.aggregate(values);
		mean.aggregate(values);
		double[] dbaResults = dba.getResults();
		double[] meanResults = mean.getResults();
		Assert.assertArrayEquals(meanResults, dbaResults, 0);
	}

	/**
	 * For example [-10,10,-10,10,-10,10]
	 * @throws Exception
	 */
	@Test
	public void when_values_are_simetric_to_zero_with_the_same_distance_then_dba_is_zero() throws Exception{
		double [][][] values = new double[100][1][1];
		
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

		double[] dbaResults = dba.getResults();
		double[] meanResults = mean.getResults();
		Assert.assertArrayEquals(meanResults, dbaResults, 0);

	}
	
	/**
	 * For example: data = [-200..-1 + 1..200]
	 * @throws Exception
	 */
	@Test
	public void when_values_are_simetric_to_zero_with_different_distances_then_mean_is_zero() throws Exception{	
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
	
	/**
	 * From 900 to 1100
	 * @throws Exception
	 */
	@Test
	public void when_values_are_simetric_to_1000_then_dba_is_like_mean() throws Exception{	
		double [][][] values = new double[200][1][1];
		int count = 900;
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				for (int k = 0; k < values[i][j].length; k++) {
					values[i][j][k] = count;
					if (count != 999){
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
	
		Assert.assertEquals(1000.0, dbaResults[0], 0);
		Assert.assertArrayEquals(meanResults, dbaResults, 0);
	}
	
	@Test
	public void when_values_are_compacted_except_one_dba_offers_better_sol() throws Exception{	
		double [][][] values = new double[200][1][1];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				for (int k = 0; k < values[i][j].length; k++) {
					if (i == 0){
						values[i][j][k] = 1000;
					} else {
						values[i][j][k] = 100;
					}
				}
			}
		}
		mean.aggregate(values);
		dba.aggregate(values);
		double[] dbaResults = dba.getResults();
		double[] meanResults = mean.getResults();
	
		Assert.assertTrue(dbaResults[0] < meanResults[0]);
	}
	
}
