package jAudioFeatureExtractor.actions;

import jAudioFeatureExtractor.Controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Displays author information about jAudio.
 * 
 * @author Daniel McEnnis edited by Sergio Revueltas
 *
 */
public class AboutAction extends AbstractAction {

	static final long serialVersionUID = 1;
	
	Controller controller = null;

	/**
	 * Basic constructor that supplies the menu item with a name.
	 * @param controller 
	 *
	 */
	public AboutAction(Controller controller) {
		super("About...");
		this.controller = controller;
	}

	/**
	 * Pops up a message giving author information.
	 */
	public void actionPerformed(ActionEvent e) {
		controller.aboutFrame.setVisible(true);
		controller.getFrame().setEnabled(false);
		/*
		String data = "Created by Sergio Revueltas (2014).\n Fork from jAudio project created by Daniel McEnnis and Cory McKay.";
		JOptionPane.showMessageDialog(controller.getFrame(), data, "About",
				JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon("img/(icon.png"));
				*/
		

	}

}
