package com.srevueltas.core;
/**
 * Window.HAMMING = 1;
 * Window.HANNING = 2;
 * Window.BLACKMAN = 3;
 * Window.BARTLETT = 4;
 * Window.BLACKMAN_HARRIS = 5; 
 */
public class WindowFunction {

	public static final int HAMMING = 1;
	public static final int HANNING = 2;
	public static final int BLACKMAN = 3;
	public static final int BARTLETT = 4;
	public static final int BLACKMAN_HARRIS = 5;

	public WindowFunction() {
	}

	/**
	 * http://www.nauticom.net/www/jdtaft/JavaWindows.htm
	 */
	public static double[] computeCoefficients(int winType, double[] coeffs) {
		int size = coeffs.length;
		double twoPi = 2. * Math.PI;
		int i;		
		switch (winType) {
		case 1: /* Hamming   */
			for (i = 0; i < size; i++) {
				coeffs[i] = 0.54 - 0.46 * Math.cos(twoPi * i / (size - 1));
				coeffs[i] *= 0.5 * (1. - Math.cos(twoPi * i / (size - 1)));
			}
			break;
		case 2: /* von Hann (sometimes improperly called Hanning)  */
			for (i = 0; i < size; i++) {
				coeffs[i] = 0.5 * (1.0 - Math.cos(twoPi * i / (size - 1)));
			}
			break;
		case 3: /* Blackman  */
			for (i = 0; i < size; i++) {
				coeffs[i] = 0.42 - 0.5 * Math.cos(twoPi * i / (size - 1)) +
						0.08 * Math.cos(2. * twoPi * i / (size - 1));
			}
		case 4: /* Bartlett  */
			for (i = 0; i <= (size - 1) / 2; i++) {
				coeffs[i] = 2. * i / (size - 1);
			}
			for (i = (size - 1) / 2; i < size; i++) {
				coeffs[i] = 2. - 2. * i / (size - 1);
			}
			break;
		case 5: /* 4 term Blackman-Harris  */
			double a0;
			double a1;
			double a2;
			double a3;

			a0 = 0.35875;
			a1 = 0.48829;
			a2 = 0.14128;
			a3 = 0.01168;

			for (i = 0; i < size; i++) {
				coeffs[i] = a0 - a1 * Math.cos(twoPi * (double) (i + 0.5) / size) +
						a2 * Math.cos(twoPi * 2. * (double) (i + 0.5) / size) -
						a3 * Math.cos(twoPi * 3. * (double) (i + 0.5) / size);
			}
			break;
		default:
			break;
		}
		return coeffs;
	}
	
	/**
	 * https://github.com/Adam140/SoundProcessing
	 */
	public static double[] Hamming(double[] array)
	{
		double[] result = new double[array.length];
		
		for(int i = 0; i < result.length; i++)
		{
			result[i] = 0.53836 - 0.46164 * Math.cos((2 * Math.PI * i) / (array.length - 1 ));
			result[i] = result[i] * array[i];
		}
		return result;
	}
	
	
	public static double[] Triangular(double[] array)
	{
		int N = array.length;
		double[] result = new double[array.length];
		
		for(int i = 0; i < result.length; i++)
		{
			result[i] = 1 - Math.abs( (i - 0.5*(N-1)) / 0.5*(N+1) );

		}
		return result;
	}
	
	
	public static float[] Hamming(float[] array)
	{
		float[] result = new float[array.length];
		
		for(int i = 0; i < result.length; i++)
		{
			result[i] = (float) (0.53836 - 0.46164 * Math.cos((2 * Math.PI * i) / (array.length - 1 )));
			result[i] = (float) (result[i] * array[i]);
		}
		return result;
	}
	
	
	
	public static double[] Hanning(double[] array)
	{
		double[] result = new double[array.length];
		
		for(int i = 0; i < result.length; i++)
		{
			result[i] = 0.5 * (1 - Math.cos((2 * Math.PI * i) / (array.length - 1 )));
			result[i] = result[i] * array[i];
		}
		return result;
	}
	
	public static double[][] sampling(double[] array, int sampRate)
	{
		int samplingRate = sampRate;
		if(isPowerOfTwo(sampRate))
		{
			samplingRate = sampRate;
		}
		
		double[][] result = new double[ (int) Math.ceil(array.length / (double) samplingRate)][];
		
		for(int i = 0; i < result.length; i++){
			if(i * samplingRate + samplingRate <= array.length)
				result[i] = new double[samplingRate];
			else
				result[i] = new double[array.length - i * samplingRate];
				
			for(int j = 0; j < samplingRate; j++)
			{
				if( i * samplingRate + j >= array.length)
					break;
				
				result[i][j] = array[i * samplingRate + j];
			}
		}
		
		return result;
	}
	
    public static boolean isPowerOfTwo(int n) {  
        return ((n & (n - 1)) == 0) && n > 0;  
    }  
	
}
