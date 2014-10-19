package jAudioFeatureExtractor;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import com.srevueltas.gui.CustomJButton;
import com.srevueltas.gui.CustomJLabel;
import com.srevueltas.gui.CustomJTextField;

/**
 * 
 * @author Sergio Revueltas
 *
 */
public class AnalysisOptionsFrame extends JFrame implements ActionListener {

	static final long serialVersionUID = 1;
	private Controller controller;
	private CustomJLabel sample_rate_label;
	private CustomJLabel window_size_label;
	private CustomJLabel window_overlap_label;
	private CustomJLabel normalise_label;
	private JComboBox<String> sample_rate_combo;
	private JComboBox<Integer> window_size_combo;
	private JSlider window_overlap_slider;
	private CustomJTextField window_overlap_textField;
	private JCheckBox normalise_checkbox;
	private CustomJButton save_button;
	private CustomJButton cancel_button;

	public AnalysisOptionsFrame(Controller controller) {
		super("Analysis options");
		//Icon from http://icons8.com/icons/#!/1391/audio-file
		this.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("img/icon.png"));
		this.controller = controller;
		setBounds(new Rectangle(310, 130, 400, 300));
		setResizable(false);
		getContentPane().setBackground(OuterFrame.GRAY_PANELS);
		getContentPane().setLayout(
				new MigLayout("", "[150.00:n:355.00][100.00:n][44.00][]",
						"[50.00:50.00][50.00:n][50.00:n][:462px:50.00px][][]"));

		sample_rate_label = new CustomJLabel("Sample Rate (KHz)");
		sample_rate_combo = new JComboBox<String>();
		String[] sampleRateItems = new String[] { "8", "11.025", "16", "22.05", "44.1" };
		for (String item : sampleRateItems) {
			sample_rate_combo.addItem(item);
		}
		// sample_rate_combo.setSelectedIndex(2);
		if (controller.samplingRateAction.sampling_rates == null)
			controller.samplingRateAction.setTarget(sample_rate_combo);
		sample_rate_combo.setSelectedIndex(controller.samplingRateAction.getSelected());
		getContentPane().add(sample_rate_label, "cell 0 0,grow");
		getContentPane().add(sample_rate_combo, "cell 1 0 2 1,growx");

		window_size_label = new CustomJLabel("Window size (samlples)");
		window_size_combo = new JComboBox<Integer>();
		Integer[] windowSizeItems = new Integer[] { 128, 256, 512, 1024, 2048, 4096 };
		for (Integer item : windowSizeItems) {
			window_size_combo.addItem(item);
		}
		window_size_combo.setSelectedIndex(3);
		getContentPane().add(window_size_label, "cell 0 1,grow");
		getContentPane().add(window_size_combo, "cell 1 1 2 1,growx");

		window_overlap_label = new CustomJLabel("Window Overlap (%)");
		window_overlap_slider = new JSlider();
		window_overlap_slider.setBackground(Color.GRAY);
		window_overlap_slider.setValue(50);
		window_overlap_textField = new CustomJTextField();
		window_overlap_textField.setText("50");
		window_overlap_textField.setEditable(false);
		getContentPane().add(window_overlap_label, "cell 0 2,grow");
		getContentPane().add(window_overlap_slider, "cell 1 2,growx,aligny center");
		getContentPane().add(window_overlap_textField, "cell 2 2,growx,aligny center");
		window_overlap_slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (window_overlap_slider.getValueIsAdjusting()) {// slider value is still being adjusted
					// int num = (int) (Math.rint((double) window_overlap_slider.getValue() / 10) * 10);// round to
					// nearest 10
					window_overlap_textField.setText(String.valueOf(window_overlap_slider.getValue()));// set textfield
																										// with value of
																										// nearest 10
				} else {// slider value has been set/no adjustments happenening
					window_overlap_textField.setText(String.valueOf(window_overlap_slider.getValue()));
				}

			}
		});

		normalise_label = new CustomJLabel("Normalise recordings");
		normalise_checkbox = new JCheckBox();
		normalise_checkbox.setSelected(true);
		normalise_checkbox.setBackground(Color.GRAY);
		getContentPane().add(normalise_label, "cell 0 3");
		getContentPane().add(normalise_checkbox, "cell 1 3 2 1,alignx center,aligny center");

		save_button = new CustomJButton("Save");
		getContentPane().add(save_button, "flowx,cell 1 5");
		save_button.addActionListener(this);

		cancel_button = new CustomJButton("Cancel");
		getContentPane().add(cancel_button, "cell 1 5,alignx right");
		cancel_button.addActionListener(this);
		// Cause program to react when the exit box is pressed
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});

		setAlwaysOnTop(true);
	}

	
	private void cancel() {
		this.setVisible(false);
		this.controller.getFrame().setEnabled(true);
		this.controller.getFrame().toFront();

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == save_button) {
			controller.samplingRateAction.setSelected(sample_rate_combo.getSelectedIndex());
			controller.window_size_index = window_size_combo.getSelectedIndex();
			controller.window_overlap_value = Integer.parseInt(window_overlap_textField.getText());
			controller.normalise.setSelected(normalise_checkbox.isSelected());
			cancel();

		} else if (e.getSource() == cancel_button) {
			cancel();
		}

	}

	public void loadDataFromController() {
		if (controller.samplingRateAction.sampling_rates == null) {
			controller.samplingRateAction.setTarget(sample_rate_combo);
		}
		sample_rate_combo.setSelectedIndex(controller.samplingRateAction.getSelected());
		window_size_combo.setSelectedIndex(controller.window_size_index);
		window_overlap_textField.setText(controller.window_overlap_value + "");
		window_overlap_slider.setValue(controller.window_overlap_value);
		normalise_checkbox.setSelected(controller.normalise.isSelected());
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public JLabel getSample_rate_label() {
		return sample_rate_label;
	}

	public JLabel getWindow_size_label() {
		return window_size_label;
	}

	public JLabel getWindow_overlap_label() {
		return window_overlap_label;
	}

	public JLabel getNormalise_label() {
		return normalise_label;
	}

	public JComboBox<String> getSample_rate_combo() {
		return sample_rate_combo;
	}

	public void setSample_rate_combo(JComboBox<String> sample_rate_combo) {
		this.sample_rate_combo = sample_rate_combo;
	}

	public JComboBox<Integer> getWindow_size_combo() {
		return window_size_combo;
	}

	public void setWindow_size_combo(JComboBox<Integer> window_size_combo) {
		this.window_size_combo = window_size_combo;
	}

	public JSlider getWindow_overlap_slider() {
		return window_overlap_slider;
	}

	public void setWindow_overlap_slider(JSlider window_overlap_slider) {
		this.window_overlap_slider = window_overlap_slider;
	}

	public JTextField getSlider_TextField() {
		return window_overlap_textField;
	}

	public JCheckBox getNormalise_checkbox() {
		return normalise_checkbox;
	}

	public void setNormalise_checkbox(JCheckBox normalise_checkbox) {
		this.normalise_checkbox = normalise_checkbox;
	}

	public JButton getAccept_button() {
		return save_button;
	}

	public JButton getCancel_button() {
		return cancel_button;
	}
}
