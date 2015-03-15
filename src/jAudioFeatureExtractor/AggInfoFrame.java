/**
 * 
 */
package jAudioFeatureExtractor;

import jAudioFeatureExtractor.Aggregators.Aggregator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import com.srevueltas.gui.CustomJButton;
import com.srevueltas.gui.CustomJLabel;
import com.srevueltas.gui.CustomJTextArea;

/**
 * AggEditorFrame
 * 
 * Provides a window for setting parameters and features on an aggregator.
 *
 * @author Daniel McEnnis
 *
 */
public class AggInfoFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private Aggregator aggregator = null;  // @jve:decl-index=0:

	private Controller controller = null;

	private boolean edited = false;

	private JPanel jContentPane = null;

	private JPanel Attributes = null;

	private JTextField[] attributes = null;

	private JPanel buttonPanel = null;

	private CustomJButton saveButton = null;

	private JPanel descriptionPanel = null;

	private CustomJButton cancelButton = null;

	private CustomJLabel descriptionTitle = null;

	private JTextArea descriptionTextArea = null;

	private JPanel featureChooser = null;

	private JLabel attributesLabel = null;

	private JScrollPane chosenFeatures = null;

	private JTable chosenFieldTable = null;

	private JPanel featureControls = null;

	private JScrollPane featureList = null;

	private JTable featureListTable = null;

	private JButton addFeature = null;

	private JButton removeFeature = null;

	private JPanel chosenFeaturePanel = null;

	private JPanel featureListPanel = null;
	
	private JFrame parent = null;

	/**
	 * This is the default constructor
	 * @param parent 
	 */
	public AggInfoFrame(Aggregator agg, Controller c, JFrame parent) {
		super();
		this.parent = parent;
		controller = c;
		aggregator = agg;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle(aggregator.getAggregatorDefinition().name);
		//Icon from http://icons8.com/icons/#!/1391/audio-file
		this.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("img/icon.png"));
		this.setBounds(new Rectangle(260, 245, 500, 220));
		this.getContentPane().setBackground(OuterFrame.GRAY_PANELS);
		// this.getContentPane().setLayout(new MigLayout("", "[67.00][]", "[93.00px:80.00px][60.00px:23px][]"));
		this.setAlwaysOnTop(true);
		// Cause program to react when the exit box is pressed
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});

	}

	private void cancel(){
		this.parent.setEnabled(true);
		this.parent.toFront();
		this.setVisible(false);
	}

	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setBackground(OuterFrame.GRAY_BOXES_LINE);
			/*
			if(aggregator.getAggregatorDefinition().parameters != null){
				jContentPane.add(getAttributes(), null);
			}
			if(!aggregator.getAggregatorDefinition().generic){
				jContentPane.add(getFeatureChooser(), null);
			}
			*/
			jContentPane.setLayout(new MigLayout("", "[520.00px]", "[347.00px][23px]"));
			jContentPane.add(getDescription(), "cell 0 0,grow");
			jContentPane.add(getButtonPanel(), "cell 0 1,alignx center,growy");
		}
		return jContentPane;
	}

	/**
	 * returns the aggregator being edited by this particular editor.
	 *
	 * @returns aggregator associated with this editor.
	 */
	public Aggregator getAggregator() {
		return aggregator;
	}

	/**
	 * Switch which aggregator this editor is editing.
	 * 
	 * @param a new aggregator to be edited by this window.
	 */
	public void setAggregator(Aggregator a) {
		aggregator = a;
	}

	/**
	 * Returns whether or not this aggregator has been edited or not.
	 * 
	 * @return is this aggregator edited
	 */
	public boolean isEdited() {
		return edited;
	}

	/**
	 * This method initializes Attributes
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAttributes() {
		if (Attributes == null) {
			attributesLabel = new JLabel();
			attributesLabel.setText("Aggregator Attributes");
			Attributes = new JPanel();
			Attributes.setLayout(new GridLayout(aggregator.getAggregatorDefinition().parameters.length + 1, 2, 6, 11));
			Attributes.setBackground(new Color(192, 218, 255));
			Attributes.add(attributesLabel);
			Attributes.add(new JLabel(""));
			attributes = new JTextField[aggregator.getAggregatorDefinition().parameters.length];
			JLabel[] attributeLabel = new JLabel[aggregator.getAggregatorDefinition().parameters.length];
			for (int i = 0; i < attributes.length; ++i) {
				String[] p = aggregator.getParamaters();
				attributes[i] = new JTextField();
				attributes[i].setText(p[i]);
				attributeLabel[i] = new JLabel();
				attributeLabel[i].setText(aggregator.getAggregatorDefinition().parameters[i]);
				Attributes.add(attributes[i]);
				Attributes.add(attributeLabel[i]);
			}
		}
		return Attributes;
	}

	/**
	 * This method initializes ButtonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setBackground(OuterFrame.GRAY_PANELS);
			buttonPanel.setLayout(new MigLayout("", "[49px]", "[25px]"));
			// buttonPanel.add(getSave(), "cell 0 0,grow");
			buttonPanel.add(getOKbutton(), "cell 0 0,grow");
		}
		return buttonPanel;
	}

	/**
	 * This method initializes Save
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSave() {
		if (saveButton == null) {
			saveButton = new CustomJButton("Save");
			saveButton.setBackground(new Color(192, 218, 255));
			saveButton.setToolTipText("Save the changes made to this aggregator and return to the previous window");
			saveButton.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (!aggregator.getAggregatorDefinition().generic && (aggregator.getFeaturesToApply() == null)) {
						// TODO: set aggregator as not edited

					} else {
						// TODO: set aggreagtors as edited
					}

					java.util.Vector<String> features = new java.util.Vector<String>();
					DefaultTableModel model = (DefaultTableModel) chosenFieldTable.getModel();
					for (int i = 0; i < model.getRowCount(); ++i) {
						features.add((String) model.getValueAt(i, 0));
					}
					String[] parameters = new String[features.size()];
					for (int i = 0; i < parameters.length; ++i) {
						parameters[i] = attributes[i].getText();
					}
					try {
						aggregator.setParameters(features.toArray(new String[] {}), parameters);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					cancel();
				}
			});
		}
		return saveButton;
	}

	/**
	 * This method initializes Description
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getDescription() {
		if (descriptionPanel == null) {
			descriptionPanel = new JPanel();
			descriptionPanel.setLayout(new MigLayout("", "[474.00px]", "[14px][:180.00px:90.00px]"));
			descriptionPanel.setBackground(OuterFrame.GRAY_PANELS);
			descriptionTitle = new CustomJLabel(aggregator.getAggregatorDefinition().name);
			descriptionTitle.setFont(OuterFrame.H1_FONT);
			descriptionPanel.add(descriptionTitle, "cell 0 0,growx,aligny top");
			descriptionPanel.add(getDescriptionText(), "cell 0 1,grow");
		}
		return descriptionPanel;
	}

	/**
	 * This method initializes Cancel
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOKbutton() {
		if (cancelButton == null) {
			cancelButton = new CustomJButton("Ok");
			cancelButton.setToolTipText("Return to previous window without changing the aggregator");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {
					cancel();
				}
			});
		}
		return cancelButton;
	}

	/**
	 * This method initializes DescriptionText
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getDescriptionText() {
		if (descriptionTextArea == null) {
			descriptionTextArea = new CustomJTextArea(aggregator.getAggregatorDefinition().description);
		}
		return descriptionTextArea;
	}

	/**
	 * This method initializes FeatureChooser
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getFeatureChooser() {
		if (featureChooser == null) {
			featureChooser = new JPanel();
			featureChooser.setLayout(new BorderLayout());
			featureChooser.add(getFeatureControls(), BorderLayout.CENTER);
			featureChooser.add(getChosenFeaturePanel(), BorderLayout.WEST);
			featureChooser.add(getFeatureListPanel(), BorderLayout.EAST);
		}
		return featureChooser;
	}

	/**
	 * This method initializes ChosenFeatures
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getChosenFeatures() {
		if (chosenFeatures == null) {
			chosenFeatures = new JScrollPane();
			chosenFeatures.setPreferredSize(new Dimension(354, 420));
			chosenFeatures.setViewportView(getChosenFieldTable());
		}
		return chosenFeatures;
	}

	/**
	 * This method initializes ChosenFieldTable
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getChosenFieldTable() {
		if (chosenFieldTable == null) {
			chosenFieldTable = new JTable();
			chosenFieldTable.setLocation(new Point(0, 0));
			DefaultTableModel model = new AggFeatureListModel(new Object[] { "Selected Features" }, 0);
			String[] names = aggregator.getFeaturesToApply();
			if (names != null) {
				for (int i = 0; i < names.length; ++i) {
					model.addRow(new Object[] { names[i] });
				}
			}
			chosenFieldTable.setModel(model);
		}
		return chosenFieldTable;
	}

	/**
	 * This method initializes FeatureControls
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getFeatureControls() {
		if (featureControls == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			featureControls = new JPanel();
			featureControls.setLayout(new GridBagLayout());
			featureControls.setBackground(new Color(192, 218, 255));
			featureControls.add(getAddFeature(), new GridBagConstraints());
			featureControls.add(getRemoveFeature(), gridBagConstraints1);
		}
		return featureControls;
	}

	/**
	 * This method initializes FeatureList
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getFeatureList() {
		if (featureList == null) {
			featureList = new JScrollPane();
			featureList.setPreferredSize(new Dimension(354, 420));
			featureList.setViewportView(getFeatureListTable());
		}
		return featureList;
	}

	/**
	 * This method initializes FeatureListTable
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getFeatureListTable() {
		if (featureListTable == null) {
			featureListTable = new JTable();
			DefaultTableModel model = new AggFeatureListModel(new Object[] { "Feature List" }, 0);
			for (int i = 0; i < controller.dm_.featureDefinitions.length; ++i) {
				model.addRow(new Object[] { controller.dm_.featureDefinitions[i].name });
			}
			featureListTable.setModel(model);
		}
		return featureListTable;
	}

	/**
	 * This method initializes AddFeature
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddFeature() {
		if (addFeature == null) {
			addFeature = new JButton();
			addFeature.setText("Add");
			addFeature.setToolTipText("Add a feature to the list of features analyzed by this aggregator");
			addFeature.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {
					int[] rows = featureListTable.getSelectedRows();
					for (int i = 0; i < rows.length; ++i) {
						String name = (String) featureListTable.getModel().getValueAt(rows[i], 0);
						((DefaultTableModel) chosenFieldTable.getModel()).addRow(new Object[] { name });
					}
				}
			});
		}
		return addFeature;
	}

	/**
	 * This method initializes RemoveFeature
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRemoveFeature() {
		if (removeFeature == null) {
			removeFeature = new JButton();
			removeFeature.setToolTipText("Remove an aggregator from the list of applied features.");
			removeFeature.setText("Remove");
			removeFeature.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {
					int[] rows = chosenFieldTable.getSelectedRows();
					for (int i = rows.length - 1; i >= 0; --i) {
						((DefaultTableModel) chosenFieldTable.getModel()).removeRow(rows[i]);
					}
				}
			});
		}
		return removeFeature;
	}

	/**
	 * This method initializes ChosenFeaturePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getChosenFeaturePanel() {
		if (chosenFeaturePanel == null) {
			chosenFeaturePanel = new JPanel();
			chosenFeaturePanel.setLayout(new GridLayout(1, 1));
			chosenFeaturePanel.add(getChosenFeatures());
		}
		return chosenFeaturePanel;
	}

	/**
	 * This method initializes FeatureListPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getFeatureListPanel() {
		if (featureListPanel == null) {
			featureListPanel = new JPanel();
			featureListPanel.setLayout(new GridLayout(1, 1));
			featureListPanel.add(getFeatureList());
		}
		return featureListPanel;
	}

}
