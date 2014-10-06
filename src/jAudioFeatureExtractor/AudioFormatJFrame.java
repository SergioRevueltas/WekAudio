package jAudioFeatureExtractor;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.sound.sampled.AudioFormat;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.srevueltas.gui.CustomJButton;
import com.srevueltas.gui.CustomJLabel;

/**
 * <code>JFrame</code> used to select encoding options for recording or synthsizing audio. These options include
 * sampling rate, bit depth, number of channels, whether samples are signed or not and byte order. Note that only PCM
 * encoding is accounted for here. Not all sound cards will support all settings.
 *
 * <p>
 * Options that are not available on the radio buttons can be entered in the text fields. The contents of text fields
 * are ignored unless the corresponding <i>Other</i> radio button is selected
 *
 * <p>
 * The <i>Low Quality Settings</i>, <i>Mid Quality Settings</i> and <i>High Quality Settings</i> buttons set the GUI
 * settings to pre-defined defaults.
 *
 * <p>
 * It is not necessary to press the <i>OK</i> button in order for changes to be accessible to external classes. However,
 * pressing the <i>Cancel</i> button will restore settings to those that were selected when the <code>JFrame</code> was
 * last made visible.
 *
 * <p>
 * The GUI settings may be set or accessed externally by the <code>setAudioFormat</code> and <code>getAudioFormat</code>
 * methods respectively.
 *
 * <p>
 * This class also includes several static methods that are unrelated to the GUI settings. These include the
 * <code>getStandardLowQualityRecordAudioFormat</code>, <code>getStandardMidQualityRecordAudioFormat</code> and
 * <code>getStandardHighQualityRecordAudioFormat</code> methods which return default <code>AudioFormat</code> presets
 * corresponding to the buttons with similar names. The <code>defineAudioFormat</code> static method does the same thing
 * as the basic PCM <code>AudioFormat</code> constructor, but is better documented.
 *
 * @author Cory McKay edited by Sergio Revueltas
 */
public class AudioFormatJFrame extends JFrame implements ActionListener {

	/* FIELDS ******************************************************************/

	static final long serialVersionUID = 1;

	// Temporarily holds the AudioFormat corresponding to the GUI's settings whenever
	// the setVisible method is called. For use in restoring settings if the cancel
	// button is pressed.
	AudioFormat temp_format;

	// Containers to hold Swing elements
	private Container content_pane;
	private JPanel settings_panel;
	private JPanel button_panel;

	// The sampling rate selectors
	private JComboBox<String> sample_rate_combo;

	// The bit depth selectors
	private JComboBox<String> bit_depth_combo;

	// The channels selectors
	private JComboBox<String> channels_combo;

	// The signed selectors
	private JComboBox<String> signed_samples_combo;

	// The signed selectors
	private JComboBox<String> bytes_order_combo;

	// Buttons
	private CustomJButton low_quality_button;
	private CustomJButton mid_quality_button;
	private CustomJButton high_quality_button;
	private CustomJButton cancel_button;
	private CustomJButton ok_button;
	private JPanel quality_panel;
	
	
	private JFrame parent;

	/* CONSTRUCTOR *************************************************************/

	/**
	 * Basic constructor. Configures the panel and its fields to low quality audio. Prepares the <code>JFrame</code>,
	 * but does not show it. The <code>setVisible</code> method must be called externally to show this.
	 * @param recordingFrame 
	 */
	public AudioFormatJFrame(JFrame parent) {
		this.parent = parent;
		// Configure overall window settings
		setTitle("PCM Audio Format Selector");
		content_pane = getContentPane();
		content_pane.setBackground(OuterFrame.GRAY_PANELS);

		settings_panel = new JPanel();
		settings_panel.setBackground(OuterFrame.GRAY_PANELS);
		button_panel = new JPanel();
		button_panel.setBackground(OuterFrame.GRAY_PANELS);
		settings_panel.setLayout(new MigLayout("", "[133px][133px]",
				"[23px][23px][23px][23px][23px][23px][23px][23px][23px][]"));
		settings_panel.add(new CustomJLabel("Sampling Rate (Hz):"), "cell 0 0,grow");

		// Instantiate channels GUI elements
		sample_rate_combo = new JComboBox<String>();
		String[] sampleRateItems = new String[] { "8000", "11025", "16000", "22050", "44100" };
		for (String item : sampleRateItems) {
			sample_rate_combo.addItem(item);
		}
		sample_rate_combo.setSelectedIndex(2);
		settings_panel.add(sample_rate_combo, "cell 1 0,growx,aligny center");

		bit_depth_combo = new JComboBox<String>();
		String[] bitDepthItems = new String[] { "8", "16" };
		for (String item : bitDepthItems) {
			bit_depth_combo.addItem(item);
		}
		bit_depth_combo.setSelectedIndex(1);
		settings_panel.add(bit_depth_combo, "cell 1 2,growx");

		CustomJLabel channelsLabel = new CustomJLabel("Channels:");
		settings_panel.add(channelsLabel, "cell 0 4,grow");
		channels_combo = new JComboBox<String>();
		String[] channelsItems = new String[] { "Mono", "Stereo" };
		for (String item : channelsItems) {
			channels_combo.addItem(item);
		}
		settings_panel.add(channels_combo, "cell 1 4,growx");

		signed_samples_combo = new JComboBox<String>();
		String[] signedSamplesItems = new String[] { "Signed", "Unsigned" };
		for (String item : signedSamplesItems) {
			signed_samples_combo.addItem(item);
		}

		// Instantiate signed GUI elemens
		CustomJLabel signedLabel = new CustomJLabel("Signed Samples:");
		settings_panel.add(signedLabel, "cell 0 6,grow");
		settings_panel.add(signed_samples_combo, "cell 1 6,growx");

		bytes_order_combo = new JComboBox<String>();
		String[] bytesOrderItems = new String[] { "Big Endian", "Little Endian" };
		for (String item : bytesOrderItems) {
			bytes_order_combo.addItem(item);
		}
		settings_panel.add(bytes_order_combo, "cell 1 8,growx");
		cancel_button = new CustomJButton("Cancel");
		cancel_button.addActionListener(this);

		// Initialize radio button settings to mid quality
		setAudioFormat(getStandardMidQualityRecordAudioFormat());

		// Cause program to react when the exit box is pressed
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});

		// Add items to settings panel (with labels)

		CustomJLabel bitDepthLabel = new CustomJLabel("Bit Depth (bits):");
		settings_panel.add(bitDepthLabel, "cell 0 2,grow");

		// Instantiate bit depth GUI elements

		CustomJLabel byteOrderLabel = new CustomJLabel("Byte Order:");
		settings_panel.add(byteOrderLabel, "cell 0 8,grow");
		ok_button = new CustomJButton("Save");
		ok_button.addActionListener(this);
		button_panel.add(ok_button);
		button_panel.add(cancel_button);
		content_pane.setLayout(new MigLayout("", "[500.00px,grow]", "[grow][grow][grow]"));
		this.setLocation(60,60);
		quality_panel = new JPanel();
		quality_panel.setBackground(OuterFrame.GRAY_PANELS);
		content_pane.add(quality_panel, "cell 0 0,grow");

		// Instantiate buttons
		low_quality_button = new CustomJButton("Low Quality Settings");
		quality_panel.add(low_quality_button);
		mid_quality_button = new CustomJButton("Mid Quality Settings");
		quality_panel.add(mid_quality_button);
		high_quality_button = new CustomJButton("High Quality Settings");
		quality_panel.add(high_quality_button);
		high_quality_button.addActionListener(this);
		mid_quality_button.addActionListener(this);

		// Attach action listeners to buttons
		low_quality_button.addActionListener(this);

		// Add panels to content pane
		content_pane.add(settings_panel, "cell 0 1,alignx center,aligny top");
		content_pane.add(button_panel, "cell 0 2,growx,aligny top");

		// Prepare for display
		pack();
	}

	/* STATIC METHODS **********************************************************/

	/**
	 * Returns a new mono <code>AudioFormat</code> that uses an 8 kHz sampling rate, a 8 bit bit-depth (signed) and big
	 * endian linear PCM encoding.
	 *
	 * <p>
	 * This audio format is a typical format for use when recording low-quality audio from a microphone.
	 */
	public static AudioFormat getStandardLowQualityRecordAudioFormat()
	{
		return defineAudioFormat(8000.0F, 8, 1, true, true);
	}

	/**
	 * Returns a new mono <code>AudioFormat</code> that uses an 8 kHz sampling rate, a 8 bit bit-depth (signed) and big
	 * endian linear PCM encoding.
	 *
	 * <p>
	 * This audio format is a typical format for use when recording low-quality audio from a microphone.
	 */
	public static AudioFormat getStandardMidQualityRecordAudioFormat()
	{
		return defineAudioFormat(16000.0F, 16, 1, true, true);
	}

	/**
	 * Returns a new mono <code>AudioFormat</code> that uses an 44.1 kHz sampling rate, a 16 bit bit-depth (signed) and
	 * big endian linear PCM encoding.
	 *
	 * <p>
	 * This audio format is a typical format for use when recording low-quality audio from a microphone.
	 */
	public static AudioFormat getStandardHighQualityRecordAudioFormat()
	{
		return defineAudioFormat(44100.0F, 16, 1, true, true);
	}

	/**
	 * Returns a new <code>AudioFormat</code> with the given parameters. This object describes the particular
	 * arrangement of data in a sound stream.
	 * 
	 * <p>
	 * Linear PCM encoding is used automatically. An alternative constructory of <code>AudioFormat</code> can be used if
	 * a different encoding is desired.
	 *
	 * <p>
	 * This method does not do anything that a basic <code>AudioFormat</code> constructor does not already do. The
	 * purpose of this method is to give better documentation.
	 *
	 * <p>
	 * The possible parameters given below may varay from sound card to sound card, and others may be available as well.
	 * 
	 * @param sample_rate Number of samples per second. Standard values are 8000,11025,16000,22050 or 44100.
	 * @param sample_size Number of bits per sample. Standard values are 8 or 16.
	 * @param channels Number of channels. Standard values are 1 or 2.
	 * @param signed True if data is signed, false if not.
	 * @param big_endian True if data is big endian, false if small endian.
	 * @return A linear PCM encoded <code>AudioFormat</code> with the specified parameters.
	 */
	public static AudioFormat defineAudioFormat(float sample_rate,
			int sample_size,
			int channels,
			boolean signed,
			boolean big_endian)
	{
		return new AudioFormat(sample_rate,
				sample_size,
				channels,
				signed,
				big_endian);
	}

	/* PUBLIC METHODS **********************************************************/

	/**
	 * Sets GUI settings to those of a pre-defined <code>AudioFormat</code>. Does nothing if null is passed to
	 * parameter.
	 *
	 * <b>IMPORTANT:</b> Only PCM encoding is made possible in this GUI.
	 *
	 * @param audio_format The <code>AudioFormat</code> to base GUI values on.
	 */
	public void setAudioFormat(AudioFormat audio_format) {
		if (audio_format != null) {
			// Set sampling rate GUI elements
			float sample_rate = audio_format.getSampleRate();
			if (sample_rate == 8000.0F)
				sample_rate_combo.setSelectedIndex(0);
			else if (sample_rate == 11025.0F)
				sample_rate_combo.setSelectedIndex(1);
			else if (sample_rate == 16000.0F)
				sample_rate_combo.setSelectedIndex(2);
			else if (sample_rate == 22050.0F)
				sample_rate_combo.setSelectedIndex(3);
			else if (sample_rate == 44100.0F)
				sample_rate_combo.setSelectedIndex(4);
			else { // default 16KHz
				sample_rate_combo.setSelectedIndex(2);
			}

			// Set bit depth GUI elements
			int bit_depth = audio_format.getSampleSizeInBits();
			if (bit_depth == 8)
				bit_depth_combo.setSelectedIndex(0);
			else if (bit_depth == 16)
				bit_depth_combo.setSelectedIndex(1);

			// Set channel GUI elements
			int channels = audio_format.getChannels();
			if (channels == 1)
				channels_combo.setSelectedIndex(0);
			else if (channels == 2)
				channels_combo.setSelectedIndex(1);

			// Set signed GUI elements
			AudioFormat.Encoding encoding = audio_format.getEncoding();
			if (encoding == AudioFormat.Encoding.PCM_SIGNED)
				signed_samples_combo.setSelectedIndex(0);
			else if (encoding == AudioFormat.Encoding.PCM_UNSIGNED)
				signed_samples_combo.setSelectedIndex(1);

			// Set endian GUI elements
			boolean is_big_endian = audio_format.isBigEndian();
			if (is_big_endian)
				bytes_order_combo.setSelectedIndex(0);
			else
				bytes_order_combo.setSelectedIndex(1);
		}
	}

	/**
	 * Gets the <code>AudioFormat</code> corresponding to the settings on the GUI. Note that only PCM encoding is
	 * possible.
	 *
	 * @param allow_text_selections If this is not set to true, then this method will throw an exception if the "Other"
	 *            radio button is selected for one or more of the sampling rate, bit depth or number of channels. If
	 *            this parameter is set to false, then an exception will not be thrown.
	 * @return The <code>AudioFormat</code> corresponding to the GUI settings.
	 * @throws Exception Throws an exception if the <i>allow_text_selections</i> parameter is true and the "Other" radio
	 *             button is selected for one or more of the sampling rate, bit depth or number of channels.
	 */
	public AudioFormat getAudioFormat(boolean allow_text_selections) throws Exception {
		// Throw an exception if one of the Other radio buttons is selected,
		// and this is not allowed
		if (!allow_text_selections) {
			/*
			if (sr_other_rb.isSelected())
				throw new Exception( "Illegal sampling rate of " + sr_text_area.getText() + ".\n" +
				                     "Only sampling rates of 8, 11.025, 16, 22.05 and 44.1 kHz are\n" +
				                     "accepted under the current settings." );
				                     
			if (bd_other_rb.isSelected())
				throw new Exception( "Illegal bit depth of " + bd_text_area.getText() + ".\n" +
				                     "Only bit depths of 8 or 16 bits are accepted under the current settings." );
				                    
			if (chan_other_rb.isSelected())
				throw new Exception( "Illegal number of channels (" + chan_text_area.getText() + ").\n" +
				                     "Only 1 or 2 channels are accepted under the current settings." );
				                      */
		}

		// Get sampling rate from GUI
		float sample_rate = 8000.0F;
		switch (sample_rate_combo.getSelectedIndex()) {
		case 0:
			sample_rate = 8000.0F;
			break;
		case 1:
			sample_rate = 11025.0F;
			break;
		case 2:
			sample_rate = 16000.0F;
			break;
		case 3:
			sample_rate = 22050.0F;
			break;
		case 4:
			sample_rate = 44100.0F;
			break;

		default:
			sample_rate = 16000.0F;
			break;
		}

		// Get bit depth from GUI
		int bit_depth = 8;
		switch (bit_depth_combo.getSelectedIndex()) {
		case 0:
			bit_depth = 8;
			break;
		case 1:
			bit_depth = 16;
			break;

		default:
			bit_depth = 16;
			break;
		}

		// Get channels from GUI
		int channels = 1;
		if (channels_combo.getSelectedIndex() == 0)
			channels = 1;
		else if (channels_combo.getSelectedIndex() == 1)
			channels = 2;

		// Get whether or not samples signed from GUI
		boolean is_signed = true;
		if (signed_samples_combo.getSelectedIndex() == 0)
			is_signed = true;
		else if (signed_samples_combo.getSelectedIndex() == 1)
			is_signed = false;

		// Get endian setting from GUI
		boolean is_big_endian = true;
		if (bytes_order_combo.getSelectedIndex() == 0)
			is_big_endian = true;
		else if (bytes_order_combo.getSelectedIndex() == 1)
			is_big_endian = false;

		// Return the AudioFormat
		return new AudioFormat(sample_rate, bit_depth, channels, is_signed, is_big_endian);
	}

	/**
	 * Calls the appropriate methods when the buttons are pressed.
	 *
	 * @param event The event that is to be reacted to.
	 */
	public void actionPerformed(ActionEvent event)
	{
		// React to the low_quality_button
		if (event.getSource().equals(low_quality_button))
			setAudioFormat(getStandardLowQualityRecordAudioFormat());

		// React to the mid_quality_button
		else if (event.getSource().equals(mid_quality_button))
			setAudioFormat(getStandardMidQualityRecordAudioFormat());

		// React to the high_quality_button
		else if (event.getSource().equals(high_quality_button))
			setAudioFormat(getStandardHighQualityRecordAudioFormat());

		// React to the cancel_button
		else if (event.getSource().equals(cancel_button))
			cancel();

		// React to the ok_button
		else if (event.getSource().equals(ok_button)) {
			this.setVisible(false);
			this.parent.setEnabled(true);
			this.parent.toFront();
		}
	}

	/**
	 * Makes this <code>JFrame</code> visible or hidden, exactly as the inherited <code>setVisible</code> method does.
	 * Also temporarily stores the currently selected settings on the GUI if the window the parameter is true.
	 *
	 * @param b Show if true, hide if false.
	 */
	public void setVisible(boolean b)
	{
		super.setVisible(b);
		try
		{
			if (b)
				temp_format = getAudioFormat(true);
		} catch (Exception e)
		{
			System.out.println(e);
			System.exit(0);
		}
	}

	/* PRIVATE METHODS *********************************************************/

	/**
	 * Hides the <code>JFrame</code> and restores the GUI settings to those that were selected when it was last made
	 * visible.
	 */
	private void cancel()
	{
		setAudioFormat(temp_format);
		this.setVisible(false);
		this.parent.setEnabled(true);
		this.parent.toFront();
	}
}
