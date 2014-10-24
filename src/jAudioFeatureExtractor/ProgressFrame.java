package jAudioFeatureExtractor;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.srevueltas.gui.CustomJButton;
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

	private Controller controller;
	private JButton btnCancel;
	private CustomJLabel lblFilename;

	/**
	 * Creates the progress window but does not show it.
	 * @param c 
	 *
	 */
	public ProgressFrame(Controller c) {
		this.controller = c;
		//Icon from http://icons8.com/icons/#!/1391/audio-file
		this.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("img/icon.png"));
		this.setTitle("Feature Procesor");
		this.setBounds(new Rectangle(360, 180, 500, 400));
		this.getContentPane().setBackground(OuterFrame.GRAY_PANELS);
		fileProgressBar = new JProgressBar();
		fileProgressBar.setStringPainted(true);
		overallProgressBar = new JProgressBar();
		overallProgressBar.setStringPainted(true);
		getContentPane().setLayout(new MigLayout("", "[250.00px:250.00px:250.00px]", "[30.00px:30.00px:30.00px][17px][15.00px:n][40.00px:n:40.00px][17px][][]"));
		
		CustomJLabel lblFileProgress = new CustomJLabel("File Progress");
		lblFileProgress.setHorizontalAlignment(SwingConstants.CENTER);
		lblFileProgress.setFont(new Font("Arial", Font.BOLD, 12));
		getContentPane().add(lblFileProgress, "cell 0 0,growx,aligny bottom");
		getContentPane().add(fileProgressBar, "cell 0 1,grow");
		
		lblFilename = new CustomJLabel("fileName");
		getContentPane().add(lblFilename, "cell 0 2,alignx center");
		lblFilename.setFont(new Font("Arial", Font.PLAIN, 10));
		
		CustomJLabel lblOvelralProgress = new CustomJLabel("Overall Progress");
		lblOvelralProgress.setHorizontalAlignment(SwingConstants.CENTER);
		lblOvelralProgress.setFont(new Font("Arial", Font.BOLD, 12));

		getContentPane().add(lblOvelralProgress, "cell 0 3,growx,aligny bottom");
		getContentPane().add(overallProgressBar, "cell 0 4,grow");
		
		btnCancel = new CustomJButton("Cancel");
		getContentPane().add(btnCancel, "cell 0 6,alignx center");
		
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cancel();
				
			}
		});
		
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});
		
		this.setAlwaysOnTop(true);
		pack();
	}

	
	public CustomJLabel getLblFilename() {
		return lblFilename;
	}

	
	public void setLblFilename(String text) {
		this.lblFilename.setText(text);
	}

	private void cancel() {
		controller.feIsRunning = false;
		this.setVisible(false);
		controller.getFrame().setEnabled(true);
		controller.getFrame().toFront();
	}
}
