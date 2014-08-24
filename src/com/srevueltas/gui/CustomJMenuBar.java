package com.srevueltas.gui;

import jAudioFeatureExtractor.OuterFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JMenuBar;

public class CustomJMenuBar extends JMenuBar {

	static final long serialVersionUID = 1;
	
	private Color bgColor = OuterFrame.GRAY;
	private Color fgColor = Color.WHITE;

	public CustomJMenuBar() {
		super();
	}

	public void setBackgroundColor(Color color) {
		bgColor = color;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(bgColor);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g.setFont(new Font("Arial", Font.BOLD, 14));
		g.setColor(fgColor);
		setOpaque(false);

	}
}
