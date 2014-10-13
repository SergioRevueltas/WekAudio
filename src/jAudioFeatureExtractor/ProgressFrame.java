package jAudioFeatureExtractor;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import com.srevueltas.gui.CustomJLabel;

/**
 * This class provides a set of progress bars for the normal (non-batch) feature
 * extrcation
 * 
 * @author Daniel McEnnis
 */
public class ProgressFrame extends JFrame {

	static final long serialVersionUID = 1;

	/**
	 * Progress within this file
	 */
	public JProgressBar fileProgressBar;

	/**
	 * Overall progress (in files)
	 */
	public JProgressBar overallProgressBar;

	/**
	 * Creates the progress window but does not show it.
	 *
	 */
	public ProgressFrame() {
		//Icon from http://icons8.com/icons/#!/1391/audio-file
		this.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("img/icon.png"));
		this.setLocation(15, 15);
		this.getContentPane().setBackground(OuterFrame.GRAY_PANELS);
		fileProgressBar = new JProgressBar();
		fileProgressBar.setStringPainted(true);
		overallProgressBar = new JProgressBar();
		overallProgressBar.setStringPainted(true);
		this.setLayout(new GridLayout(4, 1, 6, 11));
		CustomJLabel tmpLabel = new CustomJLabel("File Progress");
		tmpLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(tmpLabel);
		this.add(fileProgressBar);
		tmpLabel = new CustomJLabel("Overall Progress");
		tmpLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(tmpLabel);
		this.add(overallProgressBar);
		this.setAlwaysOnTop(true);
		pack();
	}

}
