package com.srevueltas.gui;

import jAudioFeatureExtractor.Controller;
import jAudioFeatureExtractor.OuterFrame;


public class Main {
	
	public static void main(String[] args) {
		Controller c = new Controller();
		OuterFrame outerFrame = new OuterFrame(c);
		c.setFrame(outerFrame);
	}
}
