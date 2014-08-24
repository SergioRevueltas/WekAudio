package com.srevueltas.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;


public class CustomJLabel extends JLabel{

	
	public CustomJLabel(String name) {
		super(name);
		setForeground(Color.WHITE);
		setFont(new Font("Arial", Font.PLAIN, 12));
	}
	
}
