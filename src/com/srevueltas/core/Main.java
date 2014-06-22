package com.srevueltas.core;

import java.awt.BorderLayout;

import javax.swing.JFrame;


public class Main {

	public static void main(String[] args) {
		try {

			JFrame frame = new JFrame("Waveform Display Simulator"); 
			frame.setBounds(200,200, 500, 350);

			AudioManager audioManager = new AudioManager();
			audioManager.cargarWav("audioFiles/do.wav");
        
			WavePanel rawWavePanel = new WavePanel(audioManager.getAudioData()[0],audioManager.getSampleRate(),audioManager.getSampleSizeInBits()); 
       
			frame.getContentPane().setLayout(new BorderLayout());		
			frame.getContentPane().add(rawWavePanel, BorderLayout.CENTER);
		
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
			frame.show();
			frame.validate();
			frame.repaint();

		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
