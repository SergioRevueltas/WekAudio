package com.srevueltas.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;


public class CustomJButton extends JButton{
	
	public CustomJButton(String name) {
		super(name);
		setFont(new Font("Arial", Font.BOLD, 12));
		setBackground(Color.GRAY);
		setForeground(Color.WHITE);
		setFocusPainted(false);
	}

}
