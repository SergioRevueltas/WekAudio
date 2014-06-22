package com.srevueltas.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class WavePanel extends JPanel {

	private static final Color AXIS_COLOR = Color.GRAY;
	private static final Color WAVE_COLOR = Color.BLUE;
	
	private int[] audioData;
	private int sampleRate;
	private int sampleMaxValue;
	
	public WavePanel(int [] audioData, int sampleRate, int sampleSizeInBits) {
		setLayout(new GridLayout(0,1));
		this.audioData = audioData;
		this.sampleRate = sampleRate;
		this.sampleMaxValue = (int) Math.pow(2,sampleSizeInBits-1);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintAmplitudeTimeAxis(g);
		paintAudioRawData(g);
		
		// to test audio data max value
		//Arrays.sort(audioData);
		//System.out.println(audioData[audioData.length-1]);
	}

	/**
	 * Paint scaled samples which depend on jpanel's width and height.
	 * @param g
	 */
	private void paintAudioRawData(Graphics g) {
		g.setColor(WAVE_COLOR);
		int sampleIndexIncrement = audioData.length / getWidth();
		int x0 = 0;
		int y0 = (int) (getHeight() / 2);
		int x1 = 0;
		for (int i = 0; i < audioData.length; i += sampleIndexIncrement) {
			double scaledSample = getHeight() * audioData[i] / sampleMaxValue;
			int y1 = (int) ((getHeight() / 2) - (scaledSample));
			g.drawLine(x0, y0, x1, y1);
			x1++;
			x0 = x1;
			y0 = y1;
		}
	}

	/**
	 * paint responsive axis
	 * @param g
	 */
	private void paintAmplitudeTimeAxis(Graphics g) {
		g.setColor(AXIS_COLOR);
		//x axis
		int xAxisPosition = (int) getHeight() / 2;
		g.drawLine(0, xAxisPosition, (int) getWidth(), xAxisPosition);
		//y axis per second
		int secondsScaleIncrement = sampleRate * getWidth() / audioData.length;
		for (int i = 0; i < (int)getWidth(); i+= secondsScaleIncrement) {
			g.drawLine(i, 0, i, getWidth());
		}
		
	}
	
}
