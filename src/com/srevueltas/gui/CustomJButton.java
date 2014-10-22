package com.srevueltas.gui;

import jAudioFeatureExtractor.OuterFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Source:
 * http://stackoverflow.com/questions/10973073/background-color-of-button-when-using-windows-laf/23934901#23934901
 * 
 * @author Ladislav Gallay edited by Sergio Revueltas
 *
 */
public class CustomJButton extends JButton implements ActionListener, MouseListener {

	private boolean hovered;
	private boolean clicked;

	private Color normalColor;
	private Color pressedColor;
	private Color selectedColor;

	private Icon normalIcon;
	private Icon selectedIcon;
	private Icon pressedIcon;

	/**
	 * @wbp.parser.constructor
	 */
	public CustomJButton(String name) {
		super(name);
		this.hovered = false;
		this.clicked = false;
		this.normalColor = OuterFrame.GRAY_BOXES_LINE;
		this.pressedColor = normalColor.brighter();
		this.selectedColor = normalColor.darker();

		this.normalIcon = null;
		this.selectedIcon = null;

		setForeground(Color.WHITE);
		setFont(new Font("Arial", Font.BOLD, 12));
		setFocusPainted(false);
		setMargin(new Insets(3, 3, 3, 3));

		addActionListener(this);
		addMouseListener(this);
		setContentAreaFilled(false);
	}

	public CustomJButton(Icon normalIcon, Icon selectedIcon, Icon pressedIcon) {
		super();
		this.normalIcon = normalIcon;
		this.selectedIcon = selectedIcon;
		this.pressedIcon = pressedIcon;
		this.setBackground(OuterFrame.GRAY_PANELS);
		this.hovered = false;
		this.clicked = false;
		this.addActionListener(this);
		this.addMouseListener(this);
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setFocusable(false);
		this.setFocusPainted(false);
		this.setRequestFocusEnabled(false);
		this.setOpaque(false);
		this.setBorderPainted(false);
		this.setRolloverEnabled(false);

	}

	/**
	 * Overpainting component, so it can have different colors
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		GradientPaint gp = null;
		if (normalIcon == null) {
			if (clicked) {
				gp = new GradientPaint(0, 0, selectedColor, 0, getHeight(), selectedColor.darker());
			} else if (hovered) {
				gp = new GradientPaint(0, 0, pressedColor, 0, getHeight(), pressedColor.darker());
			} else {
				gp = new GradientPaint(0, 0, normalColor, 0, getHeight(), normalColor.darker());
			}
			g2d.setPaint(gp);
			// Draws the rounded opaque panel with borders
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // For High
																										// quality
			g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 7, 7);
			g2d.setColor(selectedColor.darker().darker());
			g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 7, 7);
		} else {
			if (clicked) {
				this.setIcon(pressedIcon);
			} else if (hovered) {
				this.setIcon(selectedIcon);
			} else {
				this.setIcon(normalIcon);
			}
			g2d.setColor(OuterFrame.GRAY_PANELS);
		}
		super.paintComponent(g);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		hovered = true;
		clicked = false;
		this.setIcon(selectedIcon);
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		hovered = false;
		clicked = false;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		hovered = true;
		clicked = true;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		hovered = false;
		clicked = false;
		repaint();
	}

}
