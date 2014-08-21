package com.srevueltas.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;


public class CustomLabel extends JLabel{

	
	public CustomLabel(String name) {
		super(name);
		setForeground(Color.WHITE);
		setFont(new Font("Arial", Font.PLAIN, 12));
	}
	
}
