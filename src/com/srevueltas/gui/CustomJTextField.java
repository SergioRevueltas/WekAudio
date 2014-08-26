package com.srevueltas.gui;

import jAudioFeatureExtractor.OuterFrame;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;


public class CustomJTextField extends JTextField{

	public CustomJTextField(String name) {
		super(name);
		loadSettings();
	}

	public CustomJTextField() {
		super();
		loadSettings();
	}
	
	
	private void loadSettings() {
		setBackground(OuterFrame.GRAY.brighter());
		setForeground(Color.WHITE);
		setFont(new Font("Arial", Font.PLAIN, 12));
	}
}
