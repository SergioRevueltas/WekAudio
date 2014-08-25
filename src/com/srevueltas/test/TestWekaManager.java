package com.srevueltas.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.srevueltas.datamining.WekaManager;

/**
 * 
 * @author Sergio
 *
 */
public class TestWekaManager {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void when_classify_an_existing_instance_of_amb_A_then_return_class_name() {
		String trainSetName = "exportedFeatureValues/dafault.model";
		String result = "-";
		double[] at = {5.371E-3,3.096E3,1.342E2,-1.264E2,-3.086E0,-1.114E0,2.505E0,3.611E0,3.268E0,4.627E-1,2.2E0,-3.061E-1,-1.111E0,9.093E-1,1.135E0,-3.019E-1,-2.636E-1,1.226E0,-5.798E-1,-1.868E-1,4.008E-1,-1.838E-1,3.085E-1,1.316E0,7.449E-1,-5.157E-1,1.354E-13,5.157E-1,-7.449E-1,-1.316E0,-3.085E-1,1.838E-1,-4.008E-1,-9.015E-1,8.16E-1,-1.054E-1,3.355E-1,1.889E-1,1.652E-1,5.63E-2,-8.396E-2,-1.29E-1,0E0};
		try {
			result = WekaManager.classify(trainSetName, at);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals("amb_A", result);
	}
	
	@Test
	public void when_classify_an_existing_instance_of_amb_B_then_return_class_name() {
		String trainSetName = "exportedFeatureValues/AB_DBA_TestSuite.model";
		String result = "-";
		double[] at = {5.86E-3,2.86E3,2.602E2,-1.05E2,-2.363E1,-5.589E0,-5.048E-1,1.996E0,4.998E0,-2.995E0,-3.598E-1,5.587E-1,3.42E-1,-3.808E-1,-7.219E-1,6.891E-1,2.265E0,1.473E0,5.411E-2,1.83E-2,1.225E0,3.574E-1,7.041E-1,1.569E0,5.899E-1,2.571E-1,1.035E-13,-2.571E-1,-5.899E-1,-1.569E0,-7.041E-1,-3.574E-1,-1.225E0,-6.879E-1,7.075E-1,-2.219E-1,2.762E-1,-1.415E-1,4.509E-1,1.469E-2,1.609E-1,1.938E-1,0E0};
		try {
			result = WekaManager.classify(trainSetName, at);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals("amb_B", result);
	}

	@Test
	public void when_classify_a_non_existing_instance_of_amb_A_then_return_class_name() {
		String trainSetName = "exportedFeatureValues/AB_DBA_TestSuite.model";
		String result = "-";
		double[] at = {1.975E-3,3.074E3,2.195E2,-1.215E2,-4.474E0,8.735E-1,2.961E0,2.021E0,3.542E0,-7.56E-1,-1.231E0,-6.302E-1,3.242E0,-2.326E0,5.41E-1,3.643E-3,8.161E-1,5.263E-1,-2.089E0,1.683E0,-1.654E-1,-2.27E-1,-6.367E-1,1.589E0,-6.68E-1,7.569E-1,1.365E-13,-7.569E-1,6.68E-1,-1.589E0,6.367E-1,2.27E-1,1.654E-1,-7.668E-1,7.259E-1,-6.406E-1,4.266E-1,4.366E-2,-1.051E-1,-1.82E-2,8.388E-3,4.484E-2,0E0};
		try {
			result = WekaManager.classify(trainSetName, at);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals("amb_A", result);
	}
	
	

}
