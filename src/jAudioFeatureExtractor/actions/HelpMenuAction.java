package jAudioFeatureExtractor.actions;

import jAudioFeatureExtractor.Controller;
import jAudioFeatureExtractor.HelpMenuFrame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Displays help about WekAudio.
 * 
 * @author Sergio Revueltas
 *
 */
public class HelpMenuAction extends AbstractAction {

	static final long serialVersionUID = 1;
	
	Controller controller = null;

	/**
	 * Basic constructor that supplies the menu item with a name.
	 * @param controller 
	 *
	 */
	public HelpMenuAction(Controller controller) {
		super("Help");
		this.controller = controller;
	}

	/**
	 * Pops up a message giving author information.
	 */
	public void actionPerformed(ActionEvent e) {
		//TODO quit this line. It´s only for debugging
		controller.helpMenuFrame = new HelpMenuFrame(controller);
		
		controller.helpMenuFrame.setVisible(true);
		controller.getFrame().setEnabled(false);
		/*
		String data = "Created by Sergio Revueltas (2014).\n Fork from jAudio project created by Daniel McEnnis and Cory McKay.";
		JOptionPane.showMessageDialog(controller.getFrame(), data, "About",
				JOptionPane.INFORMATION_MESSAGE, new javax.swing.ImageIcon("img/(icon.png"));
				*/
		

	}

}
