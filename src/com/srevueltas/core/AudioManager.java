package com.srevueltas.core;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {

	private FourierAnalyzer fourierAnalyzer;
	private AudioInputStream audioInputStream;
	private String filePath;
	private byte[] buffer; // Buffer de datos de audio en bytes
	private int[][] audioData; // Samples en canales
	private int numChannels; // 1, 2
	private int sampleRate; // numero de samples por segundo: 8000,11025,16000,22050,44100
	private int sampleSizeInBits; // 8, 16
	private int frameRate;
	private int numFrames;
	private int frameSize;
	private boolean isBigEndian;

	public AudioManager() {
		this.fourierAnalyzer = null;
		this.audioInputStream = null;
		this.filePath = "";
		this.buffer = null;
		this.audioData = null;
		this.numChannels = 0;
		this.sampleRate = 0;
		this.sampleSizeInBits = 0;
		this.frameRate = 0;
		this.numFrames = 0;
		this.frameSize = 0;
		this.isBigEndian = false;
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

	public String getAudioInfo() {
		if (audioInputStream != null) {
			return filePath +
					"\tEncoding: "+audioInputStream.getFormat().getEncoding().toString() +
					"\tIs Big Endian = " + isBigEndian + 
					"\nSample rate = " + sampleRate + " Hz" +
					"\tSample size = " + sampleSizeInBits + " bits" +
					"\tFrame size = " + frameSize + " bytes" +
					"\tNumber of frames = " + numFrames;
		} else {
			return "";
		}
	}


	public void loadWav(String ruta) {
		this.filePath = ruta;
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
		
		/*System.out.println("Archivo: " + ruta +
				"\nEncoding: "+audioInputStream.getFormat().getEncoding().toString() +
				"\nNumero de canales = " + numChannels +
				"\nSample rate = " + sampleRate + " -- Frame rate = " + frameRate +
				"\nSample size = " + sampleSizeInBits + " bits" +
				"\nFrame size = " + frameSize + " bytes" +
				"\nNumero de frames = " + numFrames +
				"\nIs Big Endian = " + isBigEndian);
				*/
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
	
}
