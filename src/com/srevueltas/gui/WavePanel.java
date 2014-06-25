package com.srevueltas.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class WavePanel extends JPanel {

	private static final Color AXIS_COLOR = Color.GRAY;
	private static final Color WAVE_COLOR = Color.BLUE;
	
	private int[] audioData;
	private int sampleRate;
	private int sampleMaxValue;
	
	private int maxHeight;
	private int maxWidth;
	private int y[];

	public WavePanel(int [] audioData, int sampleRate, int sampleSizeInBits, int maxHeight, int maxWidth) {
		setLayout(new MigLayout("", "[grow]", "[grow]"));
		this.audioData = audioData;
		this.sampleRate = sampleRate;
		this.sampleMaxValue = (int) Math.pow(2,sampleSizeInBits-1);
		this.maxHeight = maxHeight;
		this.maxWidth = maxWidth;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintTimeAxis(g);
		paintAudioRawData(g);
		//scaleAndPaintAudioData(g);
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
	/*
	private void scaleAndPaintAudioData(Graphics g) {
		this.y = new int[audioData.length];
		for (int i = 0; i < audioData.length; i++) {
			y[i] = (int) (audioData[i] * 0.95 + maxHeight/2);
		}
		g.setColor(WAVE_COLOR);
		int sampleIndexIncrement = audioData.length / getWidth();
		int x0 = 0;
		int y0 = (int) (getHeight() / 2);
		//int x1 = 0;
		for (int i = 1; i < y.length; i++) {
			int x1 = (int) ((i - 1) * sampleIndexIncrement);
			int x2 = (int) (i * sampleIndexIncrement);
			int y1 = y[i - 1];
			int y2 = y[i];
			g.drawLine(x1, y1, x2, y2);
		}
	}
	*/
	/**
	 * paint responsive time axis, one per second.
	 * @param g
	 */
	private void paintTimeAxis(Graphics g) {
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
