/*
 * @(#)RecordingFrame.java	1.0	April 5, 2005.
 *
 * Cory McKay
 * McGill Univarsity
 */

package jAudioFeatureExtractor;

import jAudioFeatureExtractor.GeneralTools.StringMethods;
import jAudioFeatureExtractor.jAudioTools.AudioFormatJFrame;
import jAudioFeatureExtractor.jAudioTools.AudioMethods;
import jAudioFeatureExtractor.jAudioTools.AudioMethodsPlayback;
import jAudioFeatureExtractor.jAudioTools.AudioMethodsRecording;
import jAudioFeatureExtractor.jAudioTools.FileFilterAudio;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;


/**
 * A JFrame that allows the user to record audio in the system (coming in through
 * a mic, playing from a file, etc.) and store it in a buffer. It may then be
 * previewed. If it is satisfactory, it can be saved to disk and added to a parent
 * window. A variety of audio and file formats may be selected.
 *
 * <p>The Change Encoding Format button allows the user to select the AudioFormat to
 * use for encoding.
 *
 * <p>The Display Current Encoding button displays the AudioFormat of the last
 * recorded audio.
 *
 * <p>The Record and Stop Recording buttons begin and end recording, erasing any
 * previously recorded audio.
 *
 * <p>The Play Last Recording and Stop Playback buttons play and stop the last
 * recorded audio.
 *
 * <p>The Cancel button hides this window and erases any recorded audio.
 *
 * <p>The Save button saves the last recorded audio using the format selected
 * in the File Format For Saving combo box. This window is then hidden.
 */
public class RecordingFrame
	extends JFrame
	implements ActionListener
{
	/* FIELDS ******************************************************************/
	
	static final long serialVersionUID = 1;

	/**
	 * Holds the last recorded audio. Null if nothing has been recorded.
	 */
	private	AudioInputStream					last_recorded_audio;

	/**
	 *	A thread used to record audio. Null if not recording.
	 */
	private AudioMethodsRecording.RecordThread	record_thread;

	/**
	 * Thread for playing back recorded audio. Null if nothing playing.
	 */
	private	AudioMethodsPlayback.PlayThread		playback_thread;
	
//	/**
//	 * The parent window where references to saved files can be
//	 * stored.
//	 */
//	private RecordingSelectorPanel				parent_window;

	/**
	 * Access to all actions that can be taken
	 */
	Controller controller;
	
	/**
	 * JFileChooser for saving recorded audio.
	 */
	private JFileChooser						save_file_chooser;

	/**
	 * Dialog box to choose and store audio format for next recording.
	 */
	private AudioFormatJFrame					audio_format_selector;

	/**
	 * GUI buttons
	 */
	private JButton								choose_encoding_format_button;
	private JButton								display_current_audio_format_button;
	private JButton								record_button;
	private JButton								stop_recording_button;
	private JButton								play_recording_button;
	private JButton								cancel_button;
	private JButton								save_button;

	/**
	 * GUI combo box
	 */
	private JComboBox							choose_file_format_combo_box;
	private JLabel lblAudioFormat;
	private JLabel lblCurrentformat;


	/* CONSTRUCTOR *************************************************************/


	/**
	 * Set up the GUI.
	 *
	 * @param	c	near global controller
	 */
	public RecordingFrame(Controller c)
	{
		// Set window title
		setTitle("Record Audio");
		Color blue = new Color((float)0.75,(float)0.85,(float)1.0);
		getContentPane().setBackground(blue);

		// Cause program to react when the exit box is pressed
		addWindowListener(new WindowAdapter() {
			public void windowClosing (WindowEvent e) {
				cancel();
			}
		});

		// Set the parent_window
//		this.parent_window = parent_window;
		controller = c;

		// Set recordings and save dialog box to null initially
		record_thread = null;
		last_recorded_audio = null;
		playback_thread = null;
		save_file_chooser = null;

		// Prepare the audio format selection dialog box
		audio_format_selector = new AudioFormatJFrame();
		AudioFormat default_format = AudioFormatJFrame.getStandardMidQualityRecordAudioFormat();
		audio_format_selector.setAudioFormat(default_format);

		// Set up buttons and combo boxes
		int horizontal_gap = 6; // horizontal space between GUI elements
		int vertical_gap = 11;
		getContentPane().setLayout(new MigLayout("", "[153px][153px][]", "[23px][25.00:n][23px][23px][23px][23px]"));
		record_button = new JButton("Record");
		record_button.addActionListener(this);
		choose_encoding_format_button = new JButton("Change Encoding Format");
		choose_encoding_format_button.addActionListener(this);
		display_current_audio_format_button = new JButton("Display Current Encoding");
		display_current_audio_format_button.addActionListener(this);
		
		lblAudioFormat = new JLabel("Audio Format:");
		getContentPane().add(lblAudioFormat, "cell 0 0");
		
		lblCurrentformat = new JLabel("currentFormat");
		lblCurrentformat.setText(displayCurrentAudioFormat());
		getContentPane().add(lblCurrentformat, "cell 1 0 2 1");
		getContentPane().add(display_current_audio_format_button, "cell 1 1,grow");
		getContentPane().add(choose_encoding_format_button, "cell 2 1,grow");
		getContentPane().add(record_button, "cell 0 2,grow");
		stop_recording_button = new JButton("Stop");
		stop_recording_button.addActionListener(this);
		getContentPane().add(stop_recording_button, "cell 1 2,grow");
		choose_file_format_combo_box = new JComboBox();
		String file_types[] = AudioMethods.getAvailableFileFormatTypes();
		for (int i = 0; i < file_types.length; i++)
			choose_file_format_combo_box.addItem(file_types[i]);
		choose_file_format_combo_box.setBackground(this.getContentPane().getBackground());
		play_recording_button = new JButton("Play");
		play_recording_button.addActionListener(this);
		getContentPane().add(play_recording_button, "cell 2 2,grow");
		getContentPane().add(new JLabel("File Format For Saving:"), "cell 0 4,grow");
		getContentPane().add(choose_file_format_combo_box, "cell 1 4,grow");
		getContentPane().add(new JLabel(""), "cell 0 5,grow");
		save_button = new JButton("Save");
		save_button.addActionListener(this);
		getContentPane().add(save_button, "flowy,cell 1 5,grow");
		getContentPane().add(new JLabel(""), "cell 1 5,grow");
		cancel_button = new JButton("Cancel");
		cancel_button.addActionListener(this);
		getContentPane().add(cancel_button, "cell 2 5,grow");

		// Display GUI
		pack();
		setVisible(true);
	}


	/* PUBLIC METHODS **********************************************************/


	/**
	 * Call the appropriate methods when the buttons are pressed.
	 *
	 * @param	event		The event that is to be reacted to.
	 */
	public void actionPerformed(ActionEvent event)
	{
		// React to the choose_encoding_format_button
		if (event.getSource().equals(choose_encoding_format_button))
			chooseEncodingFormt();

		// React to the display_current_audio_format_button
		else if (event.getSource().equals(display_current_audio_format_button))
			displayCurrentAudioFormat();

		// React to the record_button
		else if (event.getSource().equals(record_button))
			record();

		// React to the stop_recording_button
		else if (event.getSource().equals(stop_recording_button))
			stopRecording();

		// React to the play_recording_button
		else if (event.getSource().equals(play_recording_button))
			play();

		// React to the cancel_button
		else if (event.getSource().equals(cancel_button))
			cancel();

		// React to the save_button
		else if (event.getSource().equals(save_button))
			save();
	}


	/* PRIVATE METHODS *********************************************************/


	/**
	 * Display the audio format selector.
	 */
	private void chooseEncodingFormt()
	{
		audio_format_selector.setVisible(true);
	}


	/**
	 * Display the encoding details for the last recorded audio.
	 */
	private String displayCurrentAudioFormat() {
		String data = "";
		if (last_recorded_audio != null)
		{
			data = AudioMethods.getAudioFormatData(last_recorded_audio.getFormat());
			JOptionPane.showMessageDialog(null, data, "Current Audio Encoding", JOptionPane.INFORMATION_MESSAGE);							
		}
		else
			JOptionPane.showMessageDialog(null, "No audio has been stored.", "WARNING", JOptionPane.ERROR_MESSAGE);
		return data;
	}


	/**
	 * Records audio coming in through a microphone or line in. Stores incoming
	 * audio in the record_thread field. Overwrites any existing recording.
	 * Stops any recording (or playback) already in progress.
	 *
	 * <b>IMPORTANT:</b> Note that this method also records audio from other sources
	 * such as a MIDI file being played by the system.
	 *
	 * <b>IMPORTANT:</b> Note that this method could cause the system to run out of
	 * memory if recording goes on for too long.
	 */
	private void record()
	{
		try
		{
			stopRecording();
			//stopPlayback();
			AudioFormat audio_format = audio_format_selector.getAudioFormat(true);
			TargetDataLine target_data_line = AudioMethods.getTargetDataLine(audio_format, null);
			record_thread = AudioMethodsRecording.recordByteArrayOutputStream(target_data_line);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Could not record because:\n" + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}


	/**
	 * Stop any recording in progress. Store the recorded data in the
	 * last_recorded_audio field. Set the record_thread> field to null.
	 */
	private void stopRecording()
	{
		if (record_thread != null)
		{
			record_thread.stopRecording();
			ByteArrayOutputStream audio_buffer = record_thread.getRecordedData();
			AudioFormat audio_buffer_format = record_thread.getFormatUsedForRecording();
			last_recorded_audio = AudioMethods.getInputStream(audio_buffer, audio_buffer_format);
			record_thread = null;
		}
		if (playback_thread != null)
		{
			playback_thread.stopPlaying();
			try
			{
				last_recorded_audio.reset();
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, "Could not reset playback position:\n" + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		playback_thread = null;
	}

	
	/**
	 * Begin playback of last recorded audio, if any. Stop any playback
	 * or recording currently in progress
	 */
	private void play()
	{
		if (last_recorded_audio != null)
		{
			stopRecording();
			//stopPlayback();
			SourceDataLine source_data_line = AudioMethods.getSourceDataLine( last_recorded_audio.getFormat(),
																			  null);
			try
			{
				playback_thread = AudioMethodsPlayback.playAudioInputStreamInterruptible( last_recorded_audio,
																						  source_data_line );
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, "Could not play because:\n" + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
	}


	/**
	 * Stop any playback currently in progress and reposition the marker
	 * in last_recorded_audio so that it will start from the beginning
	 * the next time that play is invoked.
	 */
	
	/*
	private void stopPlayback()
	{
		if (playback_thread != null)
		{
			playback_thread.stopPlaying();
			try
			{
				last_recorded_audio.reset();
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, "Could not reset playback position:\n" + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
		playback_thread = null;
	}
	*/

	/**
	 * Hides this window and clears any stored recording. Ends any recording
	 * or playback in progress.
	 */
	private void cancel()
	{
		stopRecording();
		//stopPlayback();
		last_recorded_audio = null;
		this.setVisible(false);
	}


	/**
	 * If no recording has been made, then give the user the choiceof closing this 
	 * window or of going back to make a recording. If a recording has been made,
	 * give the user a save dialog box, so that s(he) can save the file and add 
	 * it to the table in the parent_window. Then hide this window and remove
	 * any buffered recordings and end any ongoing playback or recording.
	 */
	private void save()
	{
		// If no recording has been made, then give the user the choiceof closing this window
		// or of going back to make a recording.
		if (last_recorded_audio == null)
		{
			int end = JOptionPane.showConfirmDialog( null,
												     "No recording has been made.\nDo you wish to make a recording?",
													 "WARNING",
													 JOptionPane.YES_NO_OPTION );
			if (end == JOptionPane.NO_OPTION)
				cancel();
		}

		// If a recording has been made, give the user a save dialog box, so
		// that s(he) can save the file and add it to the table. Then
		// hide this window and remove any buffered recordings and end
		// any ongoing playback or recording.
		else
		{
			// Stop any recording or playback in progress
			stopRecording();
			//stopPlayback();

			// Initialize the save_file_chooser if it has not been opened yet
			if (save_file_chooser == null)
			{
				save_file_chooser = new JFileChooser();
				save_file_chooser.setCurrentDirectory(new File("."));
				save_file_chooser.setFileFilter(new FileFilterAudio());
			}

			// Save the recording and send the reference to the parent window
			// if the user chooses OK. Also hide this window, delete the recording
			// in the buffer and end any recording or playback in progress
			int dialog_result = save_file_chooser.showSaveDialog(RecordingFrame.this);
			if (dialog_result == JFileChooser.APPROVE_OPTION) // only do if OK chosen
			{
				// Prepare a temporary File
				File save_file = save_file_chooser.getSelectedFile();
				boolean proceed = true;
				
				// Verify that the file has the correct extension
				String correct_format_name = (String) choose_file_format_combo_box.getSelectedItem();
				AudioFileFormat.Type correct_format = AudioMethods.getAudioFileFormatType(correct_format_name);
				save_file = ensureCorrectExtension(save_file, correct_format);

				// See if user wishes to overwrite if a file with the same name exists
				if (save_file.exists())
				{
					int overwrite = JOptionPane.showConfirmDialog( null,
																  "This file already exists.\nDo you wish to overwrite it?",
																  "WARNING",
																  JOptionPane.YES_NO_OPTION );
					if (overwrite != JOptionPane.YES_OPTION)
						proceed = false;
				}

				// If appropriate, save the final File, add it to the parent window
				// and close this window
				if (proceed)
				{
					try
					{
						AudioMethods.saveToFile(last_recorded_audio, save_file, correct_format);
						File[] to_add_to_table = new File[1];
						to_add_to_table[0] = save_file;
						controller.addRecordingsAction.addRecording(to_add_to_table);
//						parent_window.addRecordings(to_add_to_table);
						cancel();
					}
					catch (Exception e)
					{
						JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);							
					}
				}
			}
		}
	}


	/**
	 * Ensures that the given file has an extension corresponding to the given
	 * AudioFileFormat.Type. If it does not, then displays a warning
	 * and returns a File with the correct extension. Otherwise returns
	 * the original File.
	 *
	 * @param	file_to_verify		The File whose extension is to be validated.
	 * @param	file_format_type	The type of encoding that should correspond to the file.
	 * @return						The original file_to_verify if it had the correct
	 *								extension, and a new File with the correct
	 *								extension if it did not.
	 */
	private File ensureCorrectExtension(File file_to_verify, AudioFileFormat.Type file_format_type)
	{
		// Find the correct file extension
		String correct_extension = "." + file_format_type.getExtension();

		// Ensure that the file has the extension corresponding to its type
		String path = file_to_verify.getAbsolutePath();
		String ext = StringMethods.getExtension(path);
		if (ext == null)
			path += correct_extension;
		else if (!ext.equals(correct_extension))
			path = StringMethods.removeExtension(path) + correct_extension;
		else
			return file_to_verify;
		JOptionPane.showMessageDialog(null, "Incorrect file extension specified.\nChanged from " + ext + " to " + correct_extension + ".", "WARNING", JOptionPane.ERROR_MESSAGE);
		return new File(path);
	}
}