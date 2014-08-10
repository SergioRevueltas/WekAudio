package com.srevueltas.gui;

import jAudioFeatureExtractor.Controller;
import jAudioFeatureExtractor.OuterFrame;


public class Main {
	
	public static void main(String[] args) {
		Controller c = new Controller();
		new OuterFrame(c);
		//new MainWindow();
	}
}
