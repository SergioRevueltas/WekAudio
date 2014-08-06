package com.srevueltas.core;


public class FourierAnalyzer {
	
	private MFCC mfcc;
	private float[] samples;
	private float[] spectrum;
	private int sampleRate;
	
	private double[][] samplesBuffer;
	private double[] window;

	/**
	 * @param numberMFCCs 0-40
	 * @param windowType Window.HAMMING = 1 Window.HANNING = 2 Window.BLACKMAN = 3 Window.BARTLETT = 4
	 *            Window.BLACKMAN_HARRIS = 5;
	 */
	public FourierAnalyzer(int sampleSize, int sampleRate, int numberMFCCs, int windowType) {
		this.mfcc = new MFCC(numberMFCCs);
		this.samples = new float[sampleSize];
		this.spectrum = new float[sampleSize / 2];
		this.sampleRate = sampleRate;
		this.samplesBuffer = new double[sampleSize / 2][2];
		this.window = new double[sampleSize];
		WindowFunction.computeCoefficients(windowType, window);
	}

	public void forwardConvert(float[] samples, float[] spectrum) {
		if (samplesBuffer.length != spectrum.length) {
			throw new IllegalArgumentException("Arrays' length must be the same");
		}
		for (int i = 0; i < spectrum.length; i++) {
			// Real unit
			samplesBuffer[i][0] = samples[i] * window[i];
			// Imaginary unit
			samplesBuffer[i][1] = 0.0;
		}
		// Direct FFT transform
		fftDirect(samplesBuffer);
		// Spectral module
		for (int i = 0; i < spectrum.length; i++) {
			spectrum[i] = (float) Math.sqrt(samplesBuffer[i][0] * samplesBuffer[i][0] + samplesBuffer[i][1] * samplesBuffer[i][1]);
		}

	}

	/**
	 * The Fast Fourier Transform (FFT). The array length must be a power of two. The array size is [L][2], where each
	 * sample is complex. array[n][0] is the real part and array[n][1] is the imaginary part of the sample n.
	 * 
	 * @author URL: http://www.nauticom.net/www/jdtaft/JavaFFT.htm
	 */
	public static void fftDirect(double[][] array) {
		double u_r, u_i, w_r, w_i, t_r, t_i;
		int ln, nv2, k, l, le, le1, j, ip, i, n;

		n = array.length;
		ln = (int) (Math.log((double) n) / Math.log(2) + 0.5);
		nv2 = n / 2;
		j = 1;
		for (i = 1; i < n; i++) {
			if (i < j) {
				t_r = array[i - 1][0];
				t_i = array[i - 1][1];
				array[i - 1][0] = array[j - 1][0];
				array[i - 1][1] = array[j - 1][1];
				array[j - 1][0] = t_r;
				array[j - 1][1] = t_i;
			}
			k = nv2;
			while (k < j) {
				j = j - k;
				k = k / 2;
			}
			j = j + k;
		}

		for (l = 1; l <= ln; l++) /* loops thru stages */{
			le = (int) (Math.exp((double) l * Math.log(2)) + 0.5);
			le1 = le / 2;
			u_r = 1.0;
			u_i = 0.0;
			w_r = Math.cos(Math.PI / (double) le1);
			w_i = -Math.sin(Math.PI / (double) le1);
			for (j = 1; j <= le1; j++) /* loops thru 1/2 twiddle values per stage */{
				for (i = j; i <= n; i += le) /* loops thru points per 1/2 twiddle */{
					ip = i + le1;
					t_r = array[ip - 1][0] * u_r - u_i * array[ip - 1][1];
					t_i = array[ip - 1][1] * u_r + u_i * array[ip - 1][0];

					array[ip - 1][0] = array[i - 1][0] - t_r;
					array[ip - 1][1] = array[i - 1][1] - t_i;

					array[i - 1][0] = array[i - 1][0] + t_r;
					array[i - 1][1] = array[i - 1][1] + t_i;
				}
				t_r = u_r * w_r - w_i * u_i;
				u_i = w_r * u_i + w_i * u_r;
				u_r = t_r;
			}
		}
	}

	public static void fftInverse(double[][] array) {
		double u_r, u_i, w_r, w_i, t_r, t_i;
		int ln, nv2, k, l, le, le1, j, ip, i, n;

		n = array.length;
		ln = (int) (Math.log((double) n) / Math.log(2) + 0.5);
		nv2 = n / 2;
		j = 1;
		for (i = 1; i < n; i++) {
			if (i < j) {
				t_r = array[i - 1][0];
				t_i = array[i - 1][1];
				array[i - 1][0] = array[j - 1][0];
				array[i - 1][1] = array[j - 1][1];
				array[j - 1][0] = t_r;
				array[j - 1][1] = t_i;
			}
			k = nv2;
			while (k < j) {
				j = j - k;
				k = k / 2;
			}
			j = j + k;
		}

		for (l = 1; l <= ln; l++) /* loops thru stages */{
			le = (int) (Math.exp((double) l * Math.log(2)) + 0.5);
			le1 = le / 2;
			u_r = 1.0;
			u_i = 0.0;
			w_r = Math.cos(Math.PI / (double) le1);
			w_i = Math.sin(Math.PI / (double) le1);
			for (j = 1; j <= le1; j++) /* loops thru 1/2 twiddle values per stage */{
				for (i = j; i <= n; i += le) /* loops thru points per 1/2 twiddle */{
					ip = i + le1;
					t_r = array[ip - 1][0] * u_r - u_i * array[ip - 1][1];
					t_i = array[ip - 1][1] * u_r + u_i * array[ip - 1][0];

					array[ip - 1][0] = array[i - 1][0] - t_r;
					array[ip - 1][1] = array[i - 1][1] - t_i;

					array[i - 1][0] = array[i - 1][0] + t_r;
					array[i - 1][1] = array[i - 1][1] + t_i;
				}
				t_r = u_r * w_r - w_i * u_i;
				u_i = w_r * u_i + w_i * u_r;
				u_r = t_r;
			}
		}
	} /* end of ifft_1d */
	
	
	/**
	 * Computes stored spectrum's Mel's Frequency Cepstrum Coefficients
	 * 
	 * @param sampleRate
	 * @param numFilters
	 * @return mfccs The MFCCs array
	 */
	public float[] computeMFCCs(int sampleRate, int numFilters) {
		return mfcc.computeMFCCs(spectrum, sampleRate, numFilters);
	}

	public float[] getMFCCs() {
		return mfcc.getMFCCs();
	}
	
	/**
	 * Increases the highers frequencies of the input signal
	 * 
	 * Arrays must be the same length
	 * 
	 * @param rawSignal The input signal samples
	 * @param outSignal The output signal samples
	 */
	public static void preEmphasis(float[] rawSignal, float[] outSignal) {
		if (rawSignal.length != outSignal.length) {
			throw new IllegalArgumentException("Arrays' length must be the same.");
		}

		for (int i = rawSignal.length - 1; i > 0; i--) {
			outSignal[i] = rawSignal[i] - 0.95f * rawSignal[i - 1];
		}
		outSignal[0] = rawSignal[0];
	}
	/**
	 * Increases the highers frequencies of the input signal 
	 * 
	 * Arrays must be the same length
	 * 
	 * @param rawSignal The input signal samples
	 * @return outSignal The output signal samples
	 */
	public float[] preEmphasis(float[] rawSignal) {
		preEmphasis(rawSignal, samples);
		return samples;
	}
	/**
	 * 1- Apply Threshold value 2- Pre Emphasis 3- Forward FFT 4- MFFCs
	 * 
	 * @param sampleRate
	 * @param numFilters
	 * @param signalLevel
	 * @return mfcc
	 */
	public float[] computeMFCCsAudioData(float[] samples, int numFilters, float signalLevel) {

		// Pre Emphasis
		preEmphasis(samples);

		// Forward FFT
		forwardConvert(samples, samples);

		// MFFCs
		computeMFCCs(sampleRate, numFilters);

		return getMFCCs();
	}
}
