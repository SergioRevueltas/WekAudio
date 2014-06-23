package com.srevueltas.core;
/**
 * Window.HAMMING = 1;
 * Window.HANNING = 2;
 * Window.BLACKMAN = 3;
 * Window.BARTLETT = 4;
 * Window.BLACKMAN_HARRIS = 5; 
 */
public class Window {

	public static final int HAMMING = 1;
	public static final int HANNING = 2;
	public static final int BLACKMAN = 3;
	public static final int BARTLETT = 4;
	public static final int BLACKMAN_HARRIS = 5;

	/**
	 * Constructor for objects of class Window
	 */
	public Window() {
	}

	/**
	 * http://www.nauticom.net/www/jdtaft/JavaWindows.htm
	 */
	public static double[] computeCoefficients(int winType, double[] coeffs) {
		int n;
		int m;
		double twopi;

		m = coeffs.length;
		twopi = 2. * Math.PI;
		switch (winType) {
		case 1: /* Hamming   */
			for (n = 0; n < m; n++) {
				coeffs[n] = 0.54 - 0.46 * Math.cos(twopi * n / (m - 1));
				coeffs[n] *= 0.5 * (1. - Math.cos(twopi * n / (m - 1)));
			}
			break;
		case 2: /* von Hann (sometimes improperly called Hanning)  */
			for (n = 0; n < m; n++) {
				coeffs[n] = 0.5 * (1.0 - Math.cos(twopi * n / (m - 1)));
			}
			break;
		case 3: /* Blackman  */
			for (n = 0; n < m; n++) {
				coeffs[n] = 0.42 - 0.5 * Math.cos(twopi * n / (m - 1)) +
						0.08 * Math.cos(2. * twopi * n / (m - 1));
			}
		case 4: /* Bartlett  */
			for (n = 0; n <= (m - 1) / 2; n++) {
				coeffs[n] = 2. * n / (m - 1);
			}
			for (n = (m - 1) / 2; n < m; n++) {
				coeffs[n] = 2. - 2. * n / (m - 1);
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

			for (n = 0; n < m; n++) {
				coeffs[n] = a0 - a1 * Math.cos(twopi * (double) (n + 0.5) / m) +
						a2 * Math.cos(twopi * 2. * (double) (n + 0.5) / m) -
						a3 * Math.cos(twopi * 3. * (double) (n + 0.5) / m);
			}
			break;
		default:
			break;
		}
		return coeffs;
	}
}
