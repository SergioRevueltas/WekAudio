/*
 * @(#)RecordingSelectorPanel.java	1.02	April 27, 2005.
 *
 * McGill Univarsity
 */

package jAudioFeatureExtractor;

import jAudioFeatureExtractor.DataTypes.RecordingInfo;
import jAudioFeatureExtractor.jAudioTools.AudioMethods;
import jAudioFeatureExtractor.jAudioTools.AudioMethodsPlayback;
import jAudioFeatureExtractor.jAudioTools.AudioSamples;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import com.srevueltas.gui.CustomJButton;
import com.srevueltas.gui.CustomJTable;

/**
 * A window that allows users to select audio files to extract features from, edit or play. Alos allows the user to
 * record or synthesize audio.
 * <p>
 * The Save Features For Each Window checkbox determines whether feature values for individual windows are saved.
 * <p>
 * The Save For Overall Recordings checkbox determines whether averages and standard deviations of the features for each
 * window of a recording are calculated and saved.
 * <p>
 * The Add Recordings button allows the user to add one or more audio files to the table.
 * <p>
 * The Delete Recordings button deletes one or more recordings from the table.
 * <p>
 * The Store Samples checkbox sets whether samples are stored as doubles upon adding with the Add Recordings button.
 * This can use a lot of memory, but can also speed up processing.
 * <p>
 * The Validate Recordings checkbox sets whether files added to the table are verified to see if they can be read.
 * <p>
 * The View File Info. button displays information on the selected files in the table.
 * <p>
 * The Edit Recordings button allows users to modify a file selected in the table.
 * <p>
 * The Play Directly button plays a file selected on the table using a direct AudioInputStream from the file.
 * <p>
 * The Play MIDI button allows the user to play a MIDI file.
 * <p>
 * The Play Samples button plays samples extracted from a file selected on the table.
 * <p>
 * The Stop Playback button stops playback in progress.
 * <p>
 * The Record From Mic records samples coming into the system from a microphone, file playing, etc. and saves them to
 * disk.
 * <p>
 * The Synthesize Audio button allows the user to synthesize simple audio and save it to file.
 * 
 * @author Cory McKay
 */
public class RecordingSelectorPanel extends JPanel implements ActionListener {

	/* FIELDS ***************************************************************** */

	static final long serialVersionUID = 1;
	public static final Color BLUE = new Color((float) 0.75, (float) 0.85, (float) 1.0);
	public static final Color GRAY = OuterFrame.GRAY_PANELS;

	/**
	 * Holds a reference to the JPanel that holds objects of this class.
	 */
	public OuterFrame outer_frame;

	/**
	 * Holds references to files, associated meta-data and extracted features.
	 */
	// public RecordingInfo[] recording_list;
	/**
	 * GUI panels
	 */
	// private JPanel recordings_panel;
	private JPanel buttonsPanel;

	private JScrollPane recordings_scroll_pane;

	/**
	 * GUI table-related fields
	 */
	private CustomJTable recordings_table;

	// private RecordingsTableModel recordings_table_model;

	/**
	 * GUI buttons
	 */
	private JButton add_recordings_button;

	private JButton delete_recordings_button;

	private JButton add_from_mic_button;

	private JButton view_recording_information_button;

	private JButton play_recording_directly_button;

	private JButton play_recording_samples_button;

	private JButton use_MIDI_file_button;

	/**
	 * GUI check boxes
	 */
	// private JCheckBox store_audio_samples_check_box;
	// private JCheckBox validate_recordings_when_load_them_check_box;
	/**
	 * GUI dialog boxes
	 */
	private JFileChooser save_file_chooser;

	private JFileChooser load_recording_chooser;

	private JFileChooser load_feature_vector_file_chooser;

	private RecordingFromMicFrame recording_frame;

	private MIDIFrame midi_frame;

	/**
	 * GUI Text boxes
	 */

	// JTextArea definitions_save_path_text_field;

	// /**
	// * File to save to when save button pressed
	// */
	// private File save_file;

	// /**
	// * Thread for playing audio
	// */
	// private AudioMethodsPlayback.PlayThread playback_thread;

	/**
	 * Class that handles all actions - separating the model out of the view.
	 */
	private Controller controller;
	private FileInfoFrame fileInfoFrame;

	/* CONSTRUCTOR ************************************************************ */

	/**
	 * Set up frame.
	 * <p>
	 * Daniel McEnnis 05-08-05 Added extra line to layout to match FeatureSelectorPanel
	 * 
	 * @param outer_frame The GUI element that contains this object.
	 */
	public RecordingSelectorPanel(OuterFrame outer_frame, Controller c) {
		// Store containing panel
		this.outer_frame = outer_frame;
		this.controller = c;

		// Initialize some fields to null
		controller.dm_.recordingInfo = null;
		controller.dm_.playback_thread = null;
		save_file_chooser = null;
		// save_file = null;
		load_recording_chooser = null;
		recording_frame = null;
		midi_frame = null;

		setLayout(new MigLayout("ins 5", "[340.00px]", "[14px][50.00px:50.00px:50.00px][400.00:n,fill]"));

		// Add an overall title for this panel
		JLabel label = new JLabel("RECORDINGS:");
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Arial", Font.BOLD, 14));
		add(label, "cell 0 0,growx,aligny top");

		// Set up the list of recordings (initially blank)
		setUpRecordingListTable();

		recordings_table = new CustomJTable(controller.rtm_);

		recordings_table.getColumnModel().getColumn(0).setPreferredWidth(30);
		recordings_table.getColumnModel().getColumn(1).setPreferredWidth(200);
		recordings_table.getColumnModel().getColumn(2).setPreferredWidth(400);

		buttonsPanel = new JPanel();
		add(buttonsPanel, "flowx,cell 0 1,grow");
		buttonsPanel.setLayout(new MigLayout("ins 0", "[110.00px]2[110.00px]2[110.00px]", "[50.00px:50.00px:50.00px]"));
		buttonsPanel.setBackground(GRAY);
		add_recordings_button = new CustomJButton("Add Recordings");
		buttonsPanel.add(add_recordings_button, "cell 0 0,grow");

		add_from_mic_button = new CustomJButton("Add from Mic");
		buttonsPanel.add(add_from_mic_button, "cell 1 0,grow");

		delete_recordings_button = new CustomJButton("Delete Recordings");
		buttonsPanel.add(delete_recordings_button, "cell 2 0,grow");
		delete_recordings_button.addActionListener(controller.removeRecordingsAction);
		add_from_mic_button.addActionListener(controller.recordFromMicAction);
		add_recordings_button.addActionListener(controller.addRecordingsAction);

		controller.removeRecordingsAction.setModel(controller, recordings_table);
		controller.playSamplesAction.setTable(recordings_table);
		controller.playNowAction.setTable(recordings_table);
		controller.viewFileInfoAction.setTable(recordings_table);

		// Set up and display the table
		recordings_scroll_pane = new JScrollPane(recordings_table);
		add(recordings_scroll_pane, "cell 0 2,grow");
		recordings_scroll_pane.setBackground(OuterFrame.GRAY_BOXES_LINE);
		recordings_scroll_pane.getViewport().setBackground(OuterFrame.GRAY_PANELS);

		addTableMouseListener();
		/*
		controller.addBatchAction.setFilePath(values_save_path_text_field,
				definitions_save_path_text_field);
		controller.viewBatchAction.setRecordingFields(
				definitions_save_path_text_field, values_save_path_text_field);
				*/

	}

	/* PUBLIC METHODS ********************************************************* */

	/**
	 * Adds the given files to the table display and stores a reference to them. Ignores files that have already been
	 * added to the table.
	 * <p>
	 * Verifies that the files are valid audio files that can be read if the
	 * validate_recordings_when_load_them_check_box checkbox is selected. Only stores the actual samples if the
	 * store_audio_samples_check_box check box is selected (otherwise just stores file references).
	 * <p>
	 * If a given file path corresponds to a file that does not exist, then an error message is displayed.
	 * 
	 * @param files_to_add The files to add to the table.
	 */
	public void addRecordings(File[] files_to_add) {
		// Prepare to store the information about each file
		RecordingInfo[] recording_info = new RecordingInfo[files_to_add.length];

		// Go through the files one by one
		for (int i = 0; i < files_to_add.length; i++) {
			// Assume file is invalid as first guess
			recording_info[i] = null;

			// Verify that the file exists
			if (files_to_add[i].exists()) {
				try {
					// The samples extracted from each file
					AudioSamples audio_samples = null;

					// Load the samples if the
					// validate_recordings_when_load_them_check_box
					// is selected. Throw an exception if the file is not a
					// valid
					// audio file of a type that can be read and processed.
					if (controller.validate.isSelected()) {
						audio_samples = new AudioSamples(files_to_add[i],
								files_to_add[i].getPath(), false);
					}

					// Store the samples themselves in memory if the
					// store_audio_samples_check_box check box is selected.
					// Throw an
					// exception if the file is not a valid audio file of a type
					// that
					// can be read and processed.
					if (controller.storeSamples.isSelected()) {
						if (audio_samples == null) {
							audio_samples = new AudioSamples(files_to_add[i],
									files_to_add[i].getPath(), false);
						}
					} else
						audio_samples = null;

					// Generate a RecordingInfo object for the loaded file
					recording_info[i] = new RecordingInfo(files_to_add[i]
							.getName(), files_to_add[i].getPath(),
							audio_samples, false);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(controller.getFrame(), e.getMessage(),
							"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(controller.getFrame(), "The selected file "
						+ files_to_add[i].getName() + " does not exist.",
						"ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}

		// Update the recording_list field to include these new entries, while
		// removing
		// null entries due to problems with invalid files
		int number_old_recordings = 0;
		if (controller.dm_.recordingInfo != null)
			number_old_recordings = controller.dm_.recordingInfo.length;
		int number_new_recordings = 0;
		if (recording_info != null)
			number_new_recordings = recording_info.length;
		RecordingInfo[] temp_recording_list = new RecordingInfo[number_old_recordings
				+ number_new_recordings];
		for (int i = 0; i < number_old_recordings; i++)
			temp_recording_list[i] = controller.dm_.recordingInfo[i];
		for (int i = 0; i < number_new_recordings; i++)
			temp_recording_list[i + number_old_recordings] = recording_info[i];

		// Remove duplicate entries in the recording_list with the same file
		// path
		for (int i = 0; i < temp_recording_list.length - 1; i++)
			if (temp_recording_list[i] != null) {
				String current_path = temp_recording_list[i].file_path;
				for (int j = i + 1; j < temp_recording_list.length; j++)
					if (temp_recording_list[j] != null)
						if (current_path
								.equals(temp_recording_list[j].file_path))
							temp_recording_list[j] = null;
			}

		// Remove null entries in recording_list due to invalid files or
		// duplicate file names
		Object[] results = jAudioFeatureExtractor.GeneralTools.GeneralMethods
				.removeNullEntriesFromArray(temp_recording_list);
		if (results != null) {
			controller.dm_.recordingInfo = new RecordingInfo[results.length];
			for (int i = 0; i < results.length; i++)
				controller.dm_.recordingInfo[i] = (RecordingInfo) results[i];
		}

		// Update the table to display the new recording_list
		controller.rtm_.fillTable(controller.dm_.recordingInfo);
	}

	/**
	 * Calls the appropriate methods when the buttons are pressed.
	 * 
	 * @param event The event that is to be reacted to.
	 */
	public void actionPerformed(ActionEvent event) {
		// React to the delete_recordings_button
		if (event.getSource().equals(delete_recordings_button))
			deleteRecordings();

		// React to the process_samples_button
		// else if (event.getSource().equals(process_samples_button))
		// editRecording();

		// React to the view_recording_information_button
		else if (event.getSource().equals(view_recording_information_button))
			viewRecordingInformation();
	}

	/* PRIVATE METHODS ******************************************************** */

	/**
	 * Removes all rows selected from the table display as well as from the recording_list field.
	 */
	private void deleteRecordings() {
		int[] selected_rows = recordings_table.getSelectedRows();
		for (int i = 0; i < selected_rows.length; i++)
			controller.dm_.recordingInfo[selected_rows[i]] = null;
		Object[] results = jAudioFeatureExtractor.GeneralTools.GeneralMethods
				.removeNullEntriesFromArray(controller.dm_.recordingInfo);
		if (results != null) {
			controller.dm_.recordingInfo = new RecordingInfo[results.length];
			for (int i = 0; i < results.length; i++)
				controller.dm_.recordingInfo[i] = (RecordingInfo) results[i];
			controller.rtm_.fillTable(controller.dm_.recordingInfo);
		} else {
			controller.dm_.recordingInfo = null;
			controller.rtm_.clearTable();
		}
	}

	/**
	 * Bring up a ProcessSamplesFrame to allow the user to edit the samples of the selected recording. If multiple
	 * recordings are selected, only the top one is edited.
	 */
	// private void editRecording() {
	// int selected_row = recordings_table.getSelectedRow();
	// if (selected_row < 0)
	// JOptionPane.showMessageDialog(null,
	// "No recording selected for editing.", "ERROR",
	// JOptionPane.ERROR_MESSAGE);
	// else {
	// RecordingInfo selected_audio =
	// controller.fm_.recordingInfo[selected_row];
	// try {
	// ProcessSamplesFrame processor = new ProcessSamplesFrame(
	// RecordingSelectorPanel.this, selected_audio);
	// } catch (Exception e) {
	// JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR",
	// JOptionPane.ERROR_MESSAGE);
	// }
	// }
	// }
	/**
	 * Makes it so that if a row is double clicked on, a description of the corresponding feature is displayed along
	 * with its dependencies. Daniel McEnnis 05-07-05 Replaced message box with editDialog frame.
	 */
	public void addTableMouseListener() {
		recordings_table.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2) {
					int[] row_clicked = new int[1];
					row_clicked[0] = recordings_table.rowAtPoint(event
							.getPoint());
					viewRecordingInformation();
					//System.out.println("Clicked");
				} else {
					controller.viewFileInfoAction.setEnabled(true);
					controller.removeRecordingsAction.setEnabled(true);
				}
			}
		});
	}

	/**
	 * Displays audio encoding information about each selected files.
	 */
	private void viewRecordingInformation() {
		int[] selected_rows = recordings_table.getSelectedRows();
		for (int i = 0; i < selected_rows.length; i++) {
			try {
				File file = new File(
						controller.dm_.recordingInfo[selected_rows[i]].file_path);
				String data = jAudioFeatureExtractor.jAudioTools.AudioMethods
						.getAudioFileFormatData(file);
				fileInfoFrame = new FileInfoFrame(controller, controller.dm_.recordingInfo[selected_rows[i]].identifier,controller.dm_.recordingInfo[selected_rows[i]].file_path, data);
				fileInfoFrame.setVisible(true);
				controller.getFrame().setEnabled(false);
				/*
				JOptionPane.showMessageDialog(controller.getFrame(), data, "FILE INFORMATION",
						JOptionPane.INFORMATION_MESSAGE);
						*/
			} catch (Exception e) {
				String message = "Could not display file information for file "
						+ controller.dm_.recordingInfo[selected_rows[i]].file_path
						+ "\n" + e.getMessage();
				JOptionPane.showMessageDialog(controller.getFrame(), message, "ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Plays the selected audio file directly from the file referred to by the entry on the recordings_table. If
	 * multiple files are selected, plays only the first one. Any previous playback is stopped.
	 */
	private void playRecordingDirectly() {
		try {
			// Get the file selected for playback
			int selected_row = recordings_table.getSelectedRow();
			if (selected_row < 0)
				throw new Exception("No file selcected for playback.");
			File play_file = new File(
					controller.dm_.recordingInfo[selected_row].file_path);

			// Perform playback of the file
			try {
				// Get the AudioInputStream from the file and the SourceDataLine
				// to
				// play to from the system
				AudioInputStream audio_input_stream = AudioSystem
						.getAudioInputStream(play_file);
				audio_input_stream = AudioMethods
						.convertUnsupportedFormat(audio_input_stream);
				SourceDataLine source_data_line = AudioMethods
						.getSourceDataLine(audio_input_stream.getFormat(), null);

				// Stop any previous playback
				stopPlayback();

				// Begin playback
				controller.dm_.playback_thread = AudioMethodsPlayback
						.playAudioInputStreamInterruptible(audio_input_stream,
								source_data_line);
			} catch (UnsupportedAudioFileException ex) {
				throw new Exception("File " + play_file.getName()
						+ " has an unsupported audio format.");
			} catch (Exception ex) {
				throw new Exception("File " + play_file.getName()
						+ " is not playable.\n" + ex.getMessage());
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(controller.getFrame(), ex.getMessage(), "ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Plays the samples extracted from the audio file referred to by the selected entry on the recordings_table. If
	 * multiple files are selected, plays only the first one. Any previous playback is stopped.
	 */
	private void playRecordingSamples() {
		try {
			// Get the RecordingInfo selected for playback
			int selected_row = recordings_table.getSelectedRow();
			if (selected_row < 0)
				throw new Exception("No file selcected for playback.");
			RecordingInfo selected_audio = controller.dm_.recordingInfo[selected_row];

			// Perform playback of the file
			try {
				// Extract the audio data in the file into raw samples if not
				// done already, and convert this into an AudioInputStream
				AudioInputStream audio_input_stream = null;
				if (selected_audio.samples != null)
					audio_input_stream = selected_audio.samples
							.getAudioInputStreamChannelSegregated();
				else {
					File load_file = new File(selected_audio.file_path);
					AudioSamples samples = new AudioSamples(load_file,
							load_file.getPath(), false);

					// Store the extracted samples if this option is selected
					if (controller.storeSamples.isSelected())
						selected_audio.samples = samples;

					audio_input_stream = samples
							.getAudioInputStreamChannelSegregated();
				}

				// Get the SourceDataLine to play to from the system
				SourceDataLine source_data_line = AudioMethods
						.getSourceDataLine(audio_input_stream.getFormat(), null);

				// Stop any previous playback
				stopPlayback();

				// Begin playback
				controller.dm_.playback_thread = AudioMethodsPlayback
						.playAudioInputStreamInterruptible(audio_input_stream,
								source_data_line);
			} catch (UnsupportedAudioFileException ex) {
				throw new Exception("File " + selected_audio.file_path
						+ " has an unsupported audio format.");
			} catch (Exception ex) {
				throw new Exception("File " + selected_audio.file_path
						+ " is not playable.\n" + ex.getMessage());
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(controller.getFrame(), ex.getMessage(), "ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Stop any playback currently in progress.
	 */
	private void stopPlayback() {
		if (controller.dm_.playback_thread != null)
			controller.dm_.playback_thread.stopPlaying();
		controller.dm_.playback_thread = null;
	}

	/**
	 * Record (from mic or other audio in system) and save it to a file and add it to the table.
	 */
	private void recordNewRecording() {
		if (recording_frame == null)
			recording_frame = new RecordingFromMicFrame(controller);
		else
			recording_frame.setVisible(true);
	}

	// /**
	// * Synthesize audio and save it to a file and add it to the table.
	// */

	// /**
	// * Load and play MIDI files. This audio can then be recorded using the
	// * recordNewRecording method.
	// */

	/**
	 * Initialize the table displaying files whose references have been loaded.
	 */
	private void setUpRecordingListTable() {
		// Remove anything on the left side of the panel
		if (recordings_table != null)
			remove(recordings_table);

		// Initialize recordings_table_model and recordings_table
		Object[] column_names = {
				new String("#"),
				new String("Name"),
				new String("Path") };

		int number_recordings = 0;
		if (controller.dm_.recordingInfo != null)
			number_recordings = controller.dm_.recordingInfo.length;
		controller.rtm_ = new RecordingsTableModel(column_names,
				number_recordings);
		controller.rtm_.fillTable(controller.dm_.recordingInfo);
		controller.rtm_.fireTableDataChanged();
		repaint();
		outer_frame.repaint();
	}

	/**
	 * Allow the user to choose a save path for the feature_key_file XML file where feature values are to be saved. The
	 * selected path is entered in the definitions_save_path_text_field.
	 */
	/*
	private void browseFeatureDefinitionsSavePath() {
		String path = chooseSavePath();
		if (path != null)
			definitions_save_path_text_field.setText(path);
	}
	*/

}
