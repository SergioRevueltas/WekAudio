package com.srevueltas.core;

/**
 * libmfcc.c - Code implementation for libMFCC Copyright (c) 2010 Jeremy Sawruk
 * This code is released under the MIT License. For conditions of distribution and use, see the license in LICENSE
 */
public class MFCC {

	private float[] outerMFCCs;

	public MFCC(int numFilters) {
		this.outerMFCCs = new float[numFilters];
	}

	public static float getCoefficient(float[] spectralData, int samplingRate, int NumFilters, int m) {
		int binSize = spectralData.length;
		float result = 0.0f;
		float outerSum = 0.0f;
		float innerSum = 0.0f;
		int k, l;

		// 0 <= m < L
		if (m >= NumFilters) {
			// This represents an error condition - the specified coefficient is greater than or equal to the number of
			// filters. The behavior in this case is undefined.
			return 0.0f;
		}
		result = normalizationFactor(NumFilters, m);
		for (l = 1; l <= NumFilters; l++) {
			// Compute inner sum
			innerSum = 0.0f;
			for (k = 0; k < binSize - 1; k++) {
				innerSum += Math.abs(spectralData[k] * getFilterParameter(samplingRate, binSize, k, l));
			}

			if (innerSum > 0.0f) {
				innerSum = (float) Math.log(innerSum); // The log of 0 is undefined, so don't use it
			}

			innerSum = (float) (innerSum * Math.cos(((m * Math.PI) / NumFilters) * (l - 0.5f)));
			outerSum += innerSum;
		}
		result *= outerSum;
		return result;
	}

	/**
	 * Computes the specified (mth) MFCC
	 * 
	 * @param spectrum - array of floats containing the results of FFT computation. This data is already assumed to be
	 *            purely real
	 * @param samplingRate - the rate that the original time-series data was sampled at (i.e 44100)
	 * @param numFilters - the number of filters to use in the computation. Recommended value = 48
	 * @param m - The mth MFCC coefficient to compute
	 * @return
	 */
	public float[] computeMFCCs(float[] spectrum, int samplingRate, int numFilters) {
		int binSize = spectrum.length;
		float normFactor = 0.0f;
		float innerSum = 0.0f;

		int k, l;

		// 0 <= m < L
		if (outerMFCCs.length >= numFilters) {
			// This represents an error condition - the specified coefficient is greater than or equal to the number of
			// filters. The behavior in this case is undefined.
			return null;
		}

		for (l = 1; l <= numFilters; l++) {
			// Compute inner sum
			innerSum = 0.0f;
			for (k = 0; k < binSize - 1; k++) {
				innerSum += Math.abs(spectrum[k] * getFilterParameter(samplingRate, binSize, k, l));
			}

			if (innerSum > 0.0f) {
				innerSum = (float) Math.log(innerSum); // The log of 0 is undefined, so don't use it
			}

			for (int i = 0; i < outerMFCCs.length; i++) {
				outerMFCCs[i] += innerSum * Math.cos(((i * Math.PI) / numFilters) * (l - 0.5f));
			}
		}

		for (int i = 0; i < outerMFCCs.length; i++) {
			normFactor = normalizationFactor(numFilters, i);
			outerMFCCs[i] *= normFactor;
		}

		return outerMFCCs;
	}

	/**
	 * Computes the Normalization Factor (Equation 6) Used for internal computation only - not to be called directly
	 */
	private static float normalizationFactor(int NumFilters, int m) {
		float normalizationFactor = 0.0f;

		if (m == 0) {
			normalizationFactor = (float) Math.sqrt(1.0f / NumFilters);
		}
		else {
			normalizationFactor = (float) Math.sqrt(2.0f / NumFilters);
		}

		return normalizationFactor;
	}

	/**
	 * Compute the filter parameter for the specified frequency and filter bands (Eq. 2) Used for internal computation
	 * only - not the be called directly
	 */
	private static float getFilterParameter(int samplingRate, int binSize, int frequencyBand, int filterBand) {
		float filterParameter = 0.0f;

		float boundary = (frequencyBand * samplingRate) / binSize;             // k * Fs / N
		float prevCenterFrequency = getCenterFrequency(filterBand - 1);                // fc(l - 1) etc.
		float thisCenterFrequency = getCenterFrequency(filterBand);
		float nextCenterFrequency = getCenterFrequency(filterBand + 1);

		if (boundary >= 0 && boundary < prevCenterFrequency) {
			filterParameter = 0.0f;
		}
		else if (boundary >= prevCenterFrequency && boundary < thisCenterFrequency) {
			filterParameter = (boundary - prevCenterFrequency) / (thisCenterFrequency - prevCenterFrequency);
			filterParameter *= getMagnitudeFactor(filterBand);
		} else if (boundary >= thisCenterFrequency && boundary < nextCenterFrequency) {
			filterParameter = (boundary - nextCenterFrequency) / (thisCenterFrequency - nextCenterFrequency);
			filterParameter *= getMagnitudeFactor(filterBand);
		} else if (boundary >= nextCenterFrequency && boundary < samplingRate) {
			filterParameter = 0.0f;
		}

		return filterParameter;
	}

	/**
	 * Compute the band-dependent magnitude factor for the given filter band (Eq. 3) Used for internal computation only
	 * - not the be called directly
	 */
	private static float getMagnitudeFactor(int filterBand) {
		float magnitudeFactor = 0.0f;

		if (filterBand >= 1 && filterBand <= 14) {
			magnitudeFactor = 0.015f;
		}
		else if (filterBand >= 15 && filterBand <= 48) {
			magnitudeFactor = 2.0f / (getCenterFrequency(filterBand + 1) - getCenterFrequency(filterBand - 1));
		}

		return magnitudeFactor;
	}

	/**
	 * Compute the center frequency (fc) of the specified filter band (l) (Eq. 4) This where the mel-frequency scaling
	 * occurs. Filters are specified so that their center frequencies are equally spaced on the mel scale Used for
	 * internal computation only - not the be called directly
	 */
	private static float getCenterFrequency(int filterBand) {
		float centerFrequency = 0.0f;
		float exponent;

		if (filterBand == 0) {
			centerFrequency = 0;
		}
		else if (filterBand >= 1 && filterBand <= 14) {
			centerFrequency = (200.0f * filterBand) / 3.0f;
		} else {
			exponent = filterBand - 14.0f;
			centerFrequency = (float) Math.pow(1.0711703, exponent);
			centerFrequency *= 1073.4;
		}

		return centerFrequency;
	}

	public float[] getMFCCs() {
		return outerMFCCs;
	}
}
