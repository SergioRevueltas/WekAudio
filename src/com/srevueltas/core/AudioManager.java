package com.srevueltas.core;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {

	private AudioInputStream audioInputStream;
	private byte[] buffer; // Buffer de datos de audio en bytes
	private int[][] audioData; // Samples en canales
	private int numChannels; // 1, 2
	private int sampleRate; // numero de samples por segundo: 8000,11025,16000,22050,44100
	private int sampleSizeInBits; // 8, 16
	private int frameRate;
	private int numFrames;
	private int frameSize;
	private boolean isBigEndian;

	/*
	public static float SAMPLE_RATE = 44100; // 8000,11025,16000,22050,44100
	public static int SAMPLE_SIZE_IN_BITS = 8; //8, 16
	public static int N_CHANNELS = 1; //1, 2
	public static boolean SIGNO = true;
	public static boolean BIG_ENDIAN = false;
	
	private AudioFormat audioFormat; // Formato de audio de entrada
	private Mixer.Info[] mixerInfo;  // Lista de mezcladores disponibles
	private Mixer mezclador; // Mezclador
	private DataLine.Info linea; // Linea de entrada de captura
	private TargetDataLine tarjetaSonido; // Puente entre la zona de memoria
	private boolean stopCapture;
	*/

	public AudioManager() {
		this.audioInputStream = null;
		this.buffer = null;
		this.audioData = null;
		this.numChannels = 0;
		this.sampleRate = 0;
		this.sampleSizeInBits = 0;
		this.frameRate = 0;
		this.numFrames = 0;
		this.frameSize = 0;
		this.isBigEndian = false;

		// this.mixerInfo = AudioSystem.getMixerInfo();
		// this.audioFormat = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, N_CHANNELS, SIGNO, BIG_ENDIAN);
		// this.linea = new DataLine.Info(TargetDataLine.class, audioFormat);
		// inicializarCaptura();
	}


	private void getAudioProperties(AudioInputStream audioInputStream) {
		if (audioInputStream != null) {
			this.numChannels = audioInputStream.getFormat().getChannels();
			this.sampleRate = (int) audioInputStream.getFormat().getSampleRate();
			this.sampleSizeInBits = audioInputStream.getFormat().getSampleSizeInBits();
			this.frameRate = (int) audioInputStream.getFormat().getFrameRate();
			this.numFrames = (int) audioInputStream.getFrameLength();
			this.frameSize = audioInputStream.getFormat().getFrameSize();
			this.isBigEndian = audioInputStream.getFormat().isBigEndian();
		}
	}

	public void cargarWav(String ruta) {
		int result = 0;
		File file = new File(ruta);
		try {
			audioInputStream = AudioSystem.getAudioInputStream(file);
			getAudioProperties(audioInputStream);
			// totalBytes = bytes per frame * total number of frames
			int totalBytes = frameSize * numFrames;
			this.buffer = new byte[totalBytes];
			result = audioInputStream.read(buffer);
			System.out.println("Resultado readInputStream: " + result + " bytes leidos.");
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		convertToSamplesAndChannels();
		System.out.println("Archivo: " + ruta +
				"\nEncoding: "+audioInputStream.getFormat().getEncoding().toString() +
				"\nNumero de canales = " + numChannels +
				"\nSample rate = " + sampleRate + " -- Frame rate = " + frameRate +
				"\nSample size = " + sampleSizeInBits + " bits" +
				"\nFrame size = " + frameSize + " bytes" +
				"\nNumero de frames = " + numFrames +
				"\nIs Big Endian = " + isBigEndian);
	}

	private void convertToSamplesAndChannels() {
		audioData = new int[numChannels][numFrames];
		int sampleIndex = 0;
		for (int t = 0; t < buffer.length;) {
			for (int channel = 0; channel < numChannels; channel++) {
				int low = (int) buffer[t++];
				int high = (int) buffer[t++];
				int sample = getSixteenBitSample(high, low);
				audioData[channel][sampleIndex] = sample;
			}
			sampleIndex++;
		}
	}

	private int getSixteenBitSample(int high, int low) {
		return (high << 8) + (low & 0x00ff);
	}
	/*
	private void inicializarCaptura() {
		//Construccion de una linea de datos con el formato de audio deseado
		linea = new DataLine.Info(TargetDataLine.class, audioFormat);
		try {
			//Bucle para buscar un mezclador compatible con la linea de datos
			for (int i = 0; i < mixerInfo.length; i++) {
				mezclador = AudioSystem.getMixer(mixerInfo[i]);
				if (mezclador.isLineSupported(linea)) {
					break;
				}
			}
			tarjetaSonido = (TargetDataLine) mezclador.getLine(linea);
			// Prepare the line for use.
			tarjetaSonido.open(audioFormat);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/

	/*
	public void run() {
		//Variable para dejar de capturar
		stopCapture = false;
		try {

			while (!stopCapture) {
				//Lee los datos capturados por el sismeta de audio
				int cnt = tarjetaSonido.read(buffer, 0, buffer.length);
				if (cnt > 0) {
					// Save data in output stream object.
					//byteArrayOutputStream.write(tempBuffer,0,cnt);
					for (int i = 0; i < buffer.length; i++) {
						System.out.println(buffer[i]);
					}

				}
			}
			tarjetaSonido.stop();
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
	}
	*/

	
	
	public int[][] getAudioData() {
		return audioData;
	}

	
	public AudioInputStream getAudioInputStream() {
		return audioInputStream;
	}


	
	public void setAudioInputStream(AudioInputStream audioInputStream) {
		this.audioInputStream = audioInputStream;
	}


	
	public int getNumChannels() {
		return numChannels;
	}


	
	public void setNumChannels(int numChannels) {
		this.numChannels = numChannels;
	}


	
	public int getFrameRate() {
		return frameRate;
	}


	
	public void setFrameRate(int frameRate) {
		this.frameRate = frameRate;
	}


	
	public int getNumFrames() {
		return numFrames;
	}


	
	public void setNumFrames(int numFrames) {
		this.numFrames = numFrames;
	}


	
	public int getFrameSize() {
		return frameSize;
	}


	
	public void setFrameSize(int frameSize) {
		this.frameSize = frameSize;
	}


	
	public boolean isBigEndian() {
		return isBigEndian;
	}


	
	public void setBigEndian(boolean isBigEndian) {
		this.isBigEndian = isBigEndian;
	}


	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setAudioData(int[][] audioData) {
		this.audioData = audioData;
	}

	
	public int getSampleRate() {
		return sampleRate;
	}

	
	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}

	
	public int getSampleSizeInBits() {
		return sampleSizeInBits;
	}

	
	public void setSampleSizeInBits(int sampleSizeInBits) {
		this.sampleSizeInBits = sampleSizeInBits;
	}
	
	
	/*
	void capturar() {
		tarjetaSonido.start();
	//	this.start();
	}

	*/
}
