package com.srevueltas.gui;

import jAudioFeatureExtractor.OuterFrame;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;


public class CustomJPanel extends JPanel{

	
	public CustomJPanel() {
		super();
		Border border = BorderFactory.createLineBorder(OuterFrame.GRAY_BOXES_LINE, 1);
		setBackground(OuterFrame.GRAY);
		setBorder(border);
	}
	
}
