package com.srevueltas.gui;

import jAudioFeatureExtractor.OuterFrame;

import java.awt.Color;

import javax.swing.JTextArea;


public class CustomJTextArea extends JTextArea{

	
	public CustomJTextArea(String text) {
		super(text);
		load();
	}

	public CustomJTextArea() {
		super();
		load();
	}
	
	
	private void load() {
		this.setFont(OuterFrame.NORMAL_FONT);
		this.setBackground(OuterFrame.GRAY_PANELS);
		this.setForeground(Color.WHITE);
		this.setWrapStyleWord(true);
		this.setLineWrap(true);
		this.setEditable(false);
	}
}
