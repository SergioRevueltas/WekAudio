package jAudioFeatureExtractor;

import jAudioFeatureExtractor.AudioFeatures.FeatureExtractor;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

import com.srevueltas.gui.CustomJButton;
import com.srevueltas.gui.CustomJLabel;
import com.srevueltas.gui.CustomJTextField;

/**
 * Window for allowing a user to edit the individual features in the table. The window appears when a feature is double
 * clicked in the FeatureSelectorPanel table of features.
 * <p>
 * This window has three sections. The first is a sequence of textAreas and labels dynamically matching the number of
 * editable features present in the feature that caused the window to appear. The second secction immediately below the
 * first contains the description of the feature. (NOTE features should not include newline characters in their
 * descriptions as words are wrapped in the description box as needed.) The third section contains 2 buttons - save and
 * cancel. The Save button attempts to save all the features and then exits only if successful. If unsuccesful, an error
 * box with an explanation is presented to the user. The cancel box closes the window without saving changes and has the
 * same effect as closing the window directly.
 * <p>
 * Note that this window is recreated every time a feature is double-clicked. This is a workaround since I have not been
 * able to figure out how to dynamically change the contents of the frame when a new feature is double clicked. This
 * also has the desirable property of allowing multiple instances of this window to coexist at the same time.
 * 
 * @author mcennis edited by Sergio Revueltas
 */
public class EditFeaturesFrame extends JFrame implements ActionListener {

	static final long serialVersionUID = 1;

	private JPanel editingPanel;

	private JPanel descriptionPanel;

	private CustomJTextField[] inputBoxes;

	private CustomJLabel[] inputBoxLabels;

	private CustomJLabel descriptionTitle;

	private JTextArea infoTextArea;

	private CustomJButton save;

	private CustomJButton cancel;

	private int row;

	private FeatureExtractor fe_;

	/**
	 * Create a new instance of this panel for editing a variable.
	 * 
	 * @param parent parent window which created this window
	 * @param fe The feature which is being edited by this window
	 */
	public EditFeaturesFrame(FeatureExtractor fe) {
		this.fe_ = fe;
		String[] attributes = fe.getFeatureDefinition().attributes;
		if (attributes.length != 0) {
			this.setTitle("Edit Feature");
		} else {
			this.setTitle("Feature info");
		}
		this.setBounds(new Rectangle(200, 30, 500, 400));
		this.getContentPane().setBackground(OuterFrame.GRAY_PANELS);
		this.getContentPane().setLayout(new MigLayout("", "[67.00][]", "[93.00px:80.00px][60.00px:23px][]"));
		this.setAlwaysOnTop(true);

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});
		descriptionPanel = new JPanel();
		descriptionPanel.setBackground(OuterFrame.GRAY_PANELS);
		descriptionPanel.setLayout(new MigLayout("", "[550.00px:151px]", "[15px][58.00px:22px]"));
		descriptionTitle = new CustomJLabel(fe.getFeatureDefinition().name + ":");
		descriptionTitle.setFont(OuterFrame.H1_FONT);
		infoTextArea = new JTextArea(fe.getFeatureDefinition().description);
		infoTextArea.setFont(OuterFrame.NORMAL_FONT);
		infoTextArea.setForeground(Color.WHITE);
		infoTextArea.setWrapStyleWord(true);
		infoTextArea.setLineWrap(true);
		infoTextArea.setEditable(false);
		infoTextArea.setBackground(this.getContentPane().getBackground());
		descriptionPanel.add(descriptionTitle, "cell 0 0,growx,aligny top");
		descriptionPanel.add(infoTextArea, "cell 0 1,grow");
		getContentPane().add(descriptionPanel, "cell 0 0,grow");

		
		inputBoxes = new CustomJTextField[attributes.length];
		inputBoxLabels = new CustomJLabel[attributes.length];
		for (int i = 0; i < inputBoxes.length; ++i) {
			inputBoxes[i] = new CustomJTextField();
			inputBoxes[i].setFont(OuterFrame.NORMAL_FONT);
			try {
				inputBoxes[i].setText(fe_.getElement(i));
				inputBoxLabels[i] = new CustomJLabel(attributes[i]);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		editingPanel = new JPanel();
		editingPanel.setBackground(OuterFrame.GRAY_PANELS);
		editingPanel.setLayout(new MigLayout("", "[][]", "[][]"));
		for (int i = 0; i < inputBoxLabels.length; ++i) {
			editingPanel.add(inputBoxLabels[i], "cell " + 0 + " " + i + ",grow");
			editingPanel.add(inputBoxes[i], "cell " + 1 + " " + i + ",grow");
		}
		if (inputBoxes.length != 0) {
			getContentPane().add(editingPanel, "cell 0 1,growx,aligny center");
			save = new CustomJButton("Save");
			getContentPane().add(save, "flowx,cell 0 2,alignx center");
			cancel = new CustomJButton("Cancel");
			getContentPane().add(cancel, "cell 0 2,alignx center");
			cancel.addActionListener(this);
			save.addActionListener(this);
		} else {
			cancel = new CustomJButton("OK");
			getContentPane().add(cancel, "cell 0 2,alignx center");
			cancel.addActionListener(this);
		}
		pack();
		setVisible(true);
	}

	/**
	 * Calls the appropriate method function when buttons are pressed
	 * 
	 * @param e event to be acted upon
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(cancel)) {
			cancel();
		} else if (e.getSource().equals(save)) {
			save();
		}

	}

	/**
	 * Called either when either the cancel button is pressed or when the window is eplicitly closed by a kill box.
	 */
	private void cancel() {
		setVisible(false);
	}

	/**
	 * Called when the save button is pressed. Saves all the features and exits if the saving is successful. NOTE: it is
	 * possible to only save a portion of the features if an error appears in the process of saving features.
	 */
	private void save() {
		boolean good = true;
		for (int i = 0; i < inputBoxes.length; ++i) {
			try {
				fe_.setElement(i, inputBoxes[i].getText());
			} catch (Exception e) {
				good = false;
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		if (good) {
			setVisible(false);
		}
	}

	/**
	 * Set which feature is to be edited
	 * 
	 * @param i row of feature to be extracted
	 */
	void setRow(int i) {
		row = i;
	}
}
