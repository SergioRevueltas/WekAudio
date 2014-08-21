package jAudioFeatureExtractor;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import com.srevueltas.gui.CustomLabel;

/**
 * 
 * @author Sergio Revueltas
 *
 */
public class AnalysisOptionsFrame extends JFrame implements ActionListener{

	static final long serialVersionUID = 1;
	private Controller controller;
	private JLabel sample_rate_label;
	private JLabel window_size_label;
	private JLabel window_overlap_label;
	private JLabel normalise_label;
	private JComboBox<String> sample_rate_combo;
	private JComboBox<Integer> window_size_combo;
	private JSlider window_overlap_slider;
	private JTextField slider_value;
	private JCheckBox normalise_checkbox;
	private JButton accept_button;
	private JButton cancel_button;

	public AnalysisOptionsFrame(Controller controller) {
		super("Analysis options");
		this.controller = controller;
		setBounds(new Rectangle(0, 22, 500, 500));
		getContentPane().setBackground(Color.GRAY);
		getContentPane().setLayout(
				new MigLayout("", "[150.00:n:355.00][100.00:n][44.00][]", "[50.00:50.00][50.00:n][50.00:n][:462px:50.00px][][]"));

		sample_rate_label = new CustomLabel("Sample Rate (KHz)");
		sample_rate_combo = new JComboBox<String>();
		String[] sampleRateItems = new String[] { "8", "11.025", "16", "22.05", "44.1" };
		for (String item : sampleRateItems) {
			sample_rate_combo.addItem(item);
		}
		sample_rate_combo.setSelectedIndex(2);
		getContentPane().add(sample_rate_label, "cell 0 0,grow");
		getContentPane().add(sample_rate_combo, "cell 1 0 2 1,growx");

		window_size_label = new CustomLabel("Window size (samlples)");
		window_size_combo = new JComboBox<Integer>();
		Integer[] windowSizeItems = new Integer[] { 128, 256, 512, 1024, 2048, 4096 };
		for (Integer item : windowSizeItems) {
			window_size_combo.addItem(item);
		}
		window_size_combo.setSelectedIndex(3);
		getContentPane().add(window_size_label, "cell 0 1,grow");
		getContentPane().add(window_size_combo, "cell 1 1 2 1,growx");

		window_overlap_label = new CustomLabel("Window Overlap (%)");
		window_overlap_slider = new JSlider();
		window_overlap_slider.setValue(50);
		slider_value = new JTextField();
		slider_value.setText("50");
		slider_value.setEditable(false);
		getContentPane().add(window_overlap_label, "cell 0 2,grow");
		getContentPane().add(window_overlap_slider, "cell 1 2,growx,aligny center");
		getContentPane().add(slider_value, "cell 2 2,growx,aligny center");
		window_overlap_slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (window_overlap_slider.getValueIsAdjusting()) {// slider value is still being adjusted
					int num = (int) (Math.rint((double) window_overlap_slider.getValue() / 10) * 10);// round to nearest
																										// 10
					slider_value.setText(String.valueOf(num));// set textfield with value of nearest 10
				} else {// slider value has been set/no adjustments happenening
					slider_value.setText(String.valueOf(window_overlap_slider.getValue()));
				}

			}
		});

		normalise_label = new CustomLabel("Normalise recordings");
		normalise_checkbox = new JCheckBox();
		normalise_checkbox.setSelected(true);
		normalise_checkbox.setBackground(Color.GRAY);
		getContentPane().add(normalise_label, "cell 0 3");
		getContentPane().add(normalise_checkbox, "cell 1 3 2 1,alignx center,aligny center");

		accept_button = new CustomJButton("Accept");
		getContentPane().add(accept_button, "flowx,cell 1 5");
		accept_button.addActionListener(this);

		cancel_button = new CustomJButton("Cancel");
		getContentPane().add(cancel_button, "cell 1 5,alignx right");
		cancel_button.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == accept_button) {
			controller.samplingRateAction.setSelected(sample_rate_combo.getSelectedIndex());
			
		} else if (e.getSource() == cancel_button) {
			dispose();
		}
		
	}

}
