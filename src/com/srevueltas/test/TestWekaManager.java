package com.srevueltas.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.srevueltas.datamining.WekaManager;


public class TestWekaManager {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_amb_A() {
		String result = "";
		double[] at = {2.713E-2,3.097E3,1.176E2,-1.311E2,-1.084E1,-5.714E0,-9.473E-1,4.177E0,3.388E0,1.239E0,2.714E0,-2.183E-1,-1.802E0,1.921E-1,7.793E-1,6.329E-1,3.972E-1,3.298E-1,-5.625E-1,-8.92E-1,-7.487E-1,-9.691E-1,5.735E-1,-1.552E-2,8.422E-1,-8.074E-1,1.232E-13,8.074E-1,-8.422E-1,1.552E-2,-5.735E-1,9.691E-1,7.487E-1,-9.136E-1,9.143E-1,-1.244E-1,3.429E-1,3.254E-1,3.609E-1,2.399E-1,1.014E-1,1.677E-1,0E0};
		try {
			result = WekaManager.classify("AB_DBA", at);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("RESULT: " + result);
	}
	
	@Test
	public void test_amb_B() {
		String result = "-";
		double[] at = {5.86E-3,2.86E3,2.602E2,-1.05E2,-2.363E1,-5.589E0,-5.048E-1,1.996E0,4.998E0,-2.995E0,-3.598E-1,5.587E-1,3.42E-1,-3.808E-1,-7.219E-1,6.891E-1,2.265E0,1.473E0,5.411E-2,1.83E-2,1.225E0,3.574E-1,7.041E-1,1.569E0,5.899E-1,2.571E-1,1.035E-13,-2.571E-1,-5.899E-1,-1.569E0,-7.041E-1,-3.574E-1,-1.225E0,-6.879E-1,7.075E-1,-2.219E-1,2.762E-1,-1.415E-1,4.509E-1,1.469E-2,1.609E-1,1.938E-1,0E0};
		try {
			result = WekaManager.classify("AB_DBA", at);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("RESULT: " + result);
	}

}
