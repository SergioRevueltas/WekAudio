package com.srevueltas.core;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {
	
	private static int FRAME_LENGHT = 1024;

	private FourierAnalyzer fourierAnalyzer;
	private AudioInputStream audioInputStream;
	private String filePath;
	private byte[] buffer; // Buffer de datos de audio en bytes
	private int[][] audioData; // Samples en canales
	private int numChannels; // 1, 2
	private int sampleRate; // numero de samples por segundo: 8000,11025,16000,22050,44100
	private int sampleSizeInBits; // 8, 16
	private int frameRate;
	private long numFrames;
	private int frameSize;
	private boolean isBigEndian;
	
	private int[] currentFrame;

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
		
		this.currentFrame = null;
	}


	private void loadAudioProperties(AudioInputStream audioInputStream) {
		if (audioInputStream != null) {
			this.numChannels = audioInputStream.getFormat().getChannels();
			this.sampleRate = (int) audioInputStream.getFormat().getSampleRate();
			this.sampleSizeInBits = audioInputStream.getFormat().getSampleSizeInBits();
			this.frameRate = (int) audioInputStream.getFormat().getFrameRate();
			this.numFrames = audioInputStream.getFrameLength();
			this.frameSize = audioInputStream.getFormat().getFrameSize();
			this.isBigEndian = audioInputStream.getFormat().isBigEndian();
		}
	}
	
	private AudioInputStream downSampleRate(AudioInputStream audioInputStream, float newSampleRate) {
		if (audioInputStream != null) {
			AudioFormat	targetFormat = new AudioFormat(
					audioInputStream.getFormat().getEncoding(),
					newSampleRate,
					sampleSizeInBits,
					numChannels,
					frameSize,
					newSampleRate,
					isBigEndian);
			if(!AudioSystem.isConversionSupported(targetFormat, audioInputStream.getFormat())){
				System.out.println("Conversion is not supported");
				return null;
			}
			AudioInputStream targetStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
			return targetStream;
		}
		return null;
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
			loadAudioProperties(audioInputStream);
			//AudioInputStream a = downSampleRate(audioInputStream, 8000f);
			//loadAudioProperties(a);
			//int n = AudioSystem.write(a, AudioSystem.getAudioFileFormat(file).getType(), new File("a.wav"));
			// totalBytes = bytes per frame * total number of frames
			int totalBytes = (int) (frameSize * numFrames);
			this.buffer = new byte[totalBytes];
			result = audioInputStream.read(buffer);
			System.out.println("Resultado readInputStream: " + result + " bytes leidos.");
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		convertToSamplesAndChannels();
	}
	
	private void convertToSamplesAndChannels() {
		audioData = new int[numChannels][(int)numFrames];
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
	
	
	
	private void loadCurrentFrame(int startPosition){
		if (startPosition >= 0){
			if (startPosition + FRAME_LENGHT > (audioData.length - 1)) {
				startPosition = audioData.length - FRAME_LENGHT - 1;
			}
		} else {
			System.out.println("frame start position exception");
			startPosition = 0;
		}
		currentFrame = new int[FRAME_LENGHT];
		for (int i = startPosition; i < FRAME_LENGHT; i++) {
			currentFrame[i] = audioData[0][i];
		}
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
	
	public long getNumFrames() {
		return numFrames;
	}
	
	public void setNumFrames(long numFrames) {
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
