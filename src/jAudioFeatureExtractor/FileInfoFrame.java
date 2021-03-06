package jAudioFeatureExtractor;

import jAudioFeatureExtractor.jAudioTools.AudioMethods;
import jAudioFeatureExtractor.jAudioTools.AudioMethodsPlayback;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.srevueltas.core.ThreadCompleteListener;
import com.srevueltas.gui.CustomJButton;
import com.srevueltas.gui.CustomJLabel;
import com.srevueltas.gui.CustomJPanel;
import com.srevueltas.gui.CustomJTextArea;

/**
 * 
 * @author Sergio Revueltas
 *
 */
public class FileInfoFrame extends JFrame implements ActionListener, ThreadCompleteListener {

	private static final long serialVersionUID = 1L;
	/**
	 * Thread for playing back recorded audio. Null if nothing playing.
	 */
	private AudioMethodsPlayback.PlayThread playback_thread;
	private Controller controller;

	private String fileName;
	private String filePath;
	private String fileInfo;

	private CustomJLabel lblFileName;
	private CustomJLabel lblPathName;
	private JPanel redorderButtonsPanel;
	private CustomJPanel fileInfoPanel;
	private CustomJTextArea fileInfoTextArea;
	private CustomJButton play_recording_button;
	private CustomJButton okButton;

	public FileInfoFrame(Controller c, String fileName, String path, String fileInfo) {
		super();
		// Icon from http://icons8.com/icons/#!/1391/audio-file
		this.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("img/icon.png"));
		this.controller = c;
		this.fileName = fileName;
		this.filePath = path;
		this.fileInfo = fileInfo;
		this.getContentPane().setBackground(OuterFrame.GRAY_PANELS);
		this.setBounds(new Rectangle(260, 160, 500, 380));
		this.getContentPane().setLayout(new MigLayout("ins 5", "[270.00,grow][70.00,grow]", "[][][200.00,grow][]"));
		this.setTitle("File info");
		this.setResizable(false);
		//this.setAlwaysOnTop(true);

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});

		redorderButtonsPanel = new JPanel();
		redorderButtonsPanel.setBackground(OuterFrame.GRAY_PANELS);
		redorderButtonsPanel.setLayout(new MigLayout("", "[150.00:n:100.00]", "[]"));

		lblFileName = new CustomJLabel(fileName);
		lblFileName.setFont(new Font("Arial", Font.BOLD, 14));
		this.getContentPane().add(lblFileName, "cell 0 0");
		
		lblPathName = new CustomJLabel(filePath);
		lblPathName.setFont(new Font("Arial", Font.PLAIN, 10));
		this.getContentPane().add(lblPathName, "cell 0 1 2 1");

		play_recording_button = new CustomJButton("Play");
		getContentPane().add(play_recording_button, "cell 1 0,growx");
		play_recording_button.addActionListener(this);

		fileInfoPanel = new CustomJPanel();
		fileInfoPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		this.getContentPane().add(fileInfoPanel, "cell 0 2 2 1,grow");

		fileInfoTextArea = new CustomJTextArea();
		fileInfoPanel.add(fileInfoTextArea, "cell 0 0,grow");
		fileInfoTextArea.setText(fileInfo);
		this.getContentPane().add(redorderButtonsPanel, "cell 0 3 2 1,alignx center");

		okButton = new CustomJButton("Ok");
		redorderButtonsPanel.add(okButton, "cell 0 0,grow");
		okButton.addActionListener(this);

		play();
	}

	private void cancel() {
		stopRecording();
		this.controller.getFrame().setEnabled(true);
		this.controller.getFrame().toFront();
		setVisible(false);
	}

	/**
	 * Call the appropriate methods when the buttons are pressed.
	 *
	 * @param event The event that is to be reacted to.
	 */
	public void actionPerformed(ActionEvent event) {
		// React to the play_recording_button
		if (event.getSource().equals(play_recording_button))
			if (playback_thread != null)
				stopRecording();
			else
				play();
		if (event.getSource().equals(okButton))
			cancel();
	}

	/**
	 * Stop any recording in progress. Store the recorded data in the last_recorded_audio field. Set the record_thread>
	 * field to null.
	 */
	private void stopRecording()
	{
		if (playback_thread != null) {
			playback_thread.stopPlaying();
			play_recording_button.setText("Play");
		}
		playback_thread = null;
	}

	/**
	 * Begin playback of last recorded audio, if any. Stop any playback or recording currently in progress
	 */
	private void play() {
		// stopRecording();
		try {
			AudioInputStream ais = AudioMethods.getInputStream(new File(this.filePath));
			SourceDataLine source_data_line = AudioMethods.getSourceDataLine(ais.getFormat(), null);
			playback_thread =
					AudioMethodsPlayback.playAudioInputStreamInterruptible(ais, source_data_line);
			playback_thread.addListener(this);
			play_recording_button.setText("Stop");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(controller.getFrame(), "Could not play because:\n" + e.getMessage(),
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Callback when PlayThread ends
	 */
	@Override
	public void notifyOfThreadComplete(Thread thread) {
		play_recording_button.setText("Play");
		playback_thread = null;
	}
}
