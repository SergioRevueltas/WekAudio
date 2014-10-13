package jAudioFeatureExtractor.actions;

import jAudioFeatureExtractor.AnalysisOptionsFrame;
import jAudioFeatureExtractor.Controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Action that creates and displays the Analisys Options Frame
 * 
 * @author Sergio Revueltas
 */
public class AnalysisOptionsAction extends AbstractAction {

	static final long serialVersionUID = 1;

	Controller controller;
	public AnalysisOptionsFrame analysis_options = null;

	/**
	 * Constructor that sets th4e menu text and stores a reference to the
	 * controller.
	 * 
	 * @param c near global controller.
	 */
	public AnalysisOptionsAction(Controller c) {
		super("Analysis Options...");
		controller = c;
		analysis_options = null;
	}

	/**
	 * Creates and displays the RecordingFrame frame.
	 */
	public void actionPerformed(ActionEvent e) {
		if (analysis_options == null) {
			System.out.println("NEW frame Action");
			analysis_options = new AnalysisOptionsFrame(controller);
			controller.setObjectReferences(analysis_options,
					analysis_options.getWindow_size_combo(),
					analysis_options.getSlider_TextField());
		}
		analysis_options.loadDataFromController();
		analysis_options.setVisible(true);
	}

}
