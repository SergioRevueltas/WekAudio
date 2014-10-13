package jAudioFeatureExtractor.actions;

import jAudioFeatureExtractor.Controller;
import jAudioFeatureExtractor.RecordingFromMicFrame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Action that creates and displays the RecordingFrame
 * 
 * @author Daniel McEnnis
 */
public class RecordFromMicAction extends AbstractAction {

	static final long serialVersionUID = 1;

	Controller parent;

	RecordingFromMicFrame rec_ = null;

	/**
	 * Constructor that sets th4e menu text and stores a reference to the
	 * controller.
	 * 
	 * @param c
	 *            near global controller.
	 */
	public RecordFromMicAction(Controller c) {
		super("Record From Mic...");
		parent = c;
	}

	/**
	 * Creates and displays the RecordingFrame frame.
	 */
	public void actionPerformed(ActionEvent e) {
		if (rec_ == null) {
			rec_ = new RecordingFromMicFrame(parent);
		}
		rec_.setVisible(true);
		parent.getFrame().setEnabled(false);
	}

}
