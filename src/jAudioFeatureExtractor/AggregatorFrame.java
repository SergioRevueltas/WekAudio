/**
 * 
 */
package jAudioFeatureExtractor;

import jAudioFeatureExtractor.Aggregators.Aggregator;
import jAudioFeatureExtractor.Aggregators.Mean;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;

import com.srevueltas.gui.CustomJButton;
import com.srevueltas.gui.CustomJLabel;
import com.srevueltas.gui.CustomJTable;

/**
 * AggregatorFrame
 *
 * Window for altering the available aggregators for per-file analysis.
 * 
 * @author Daniel McEnnis edited by Sergio Revueltas
 *
 */
public class AggregatorFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JScrollPane activeAggList = null;
	private CustomJTable activeAggTable = null;
	private JScrollPane aggList = null;
	private CustomJTable aggListTable = null;
	private JPanel aggButtonPanel = null;
	private CustomJButton aggAdd = null;
	private CustomJButton aggRemove = null;
	private AggEditorFrame aggEditorFrame = null;
	private Controller controller;
	private CustomJButton doneButton = null;
	private CustomJButton abort = null;
	private CustomJLabel lblCurrentAggregators = null;
	private CustomJLabel lblAggregatorsList = null;

	/**
	 * This is the default constructor
	 */
	public AggregatorFrame(Controller c) {
		super();

		controller = c;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle("Aggregators");
		this.setBounds(new Rectangle(30, 30, 750, 290));
		this.setResizable(false);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new MigLayout("", "[245.00px:452px][80.00:39.00][80.00px:32px][300.00:n]",
					"[][198.00px:462px:185.00px][::34.00]"));
			jContentPane.add(getLblCurrentAggregators(), "cell 0 0");
			jContentPane.add(getLblAggregatorsList(), "cell 3 0");
			jContentPane.add(getActiveAggList(), "cell 0 1,alignx left,aligny bottom");
			jContentPane.add(getAggButtonPanel(), "cell 1 1 2 1,alignx center,aligny center");
			jContentPane.add(getAggList(), "cell 3 1,alignx right,growy");
			jContentPane.add(getDoneButton(), "cell 1 2,growx");
			jContentPane.setBackground(OuterFrame.BLACK_BACKGROUND);
			jContentPane.add(getAbort(), "cell 2 2,growx");
		}
		return jContentPane;
	}

	/**
	 * This method initializes ActiveAggList
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getActiveAggList() {
		if (activeAggList == null) {
			activeAggList = new JScrollPane();
			activeAggList.setViewportView(getActiveAggTable());
			activeAggList.setBackground(OuterFrame.GRAY);
			activeAggList.getViewport().setBackground(OuterFrame.GRAY);
		}
		return activeAggList;
	}

	/**
	 * This method initializes ActiveAggTable
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getActiveAggTable() {
		if (activeAggTable == null) {
			activeAggTable = new CustomJTable();
			controller.activeAgg_ = new ActiveAggTableModel();
			controller.activeAgg_.init(controller);
			activeAggTable.setModel(controller.activeAgg_);
			activeAggTable.addMouseListener(new java.awt.event.MouseAdapter() {

				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (e.getClickCount() == 2) {
						int row = activeAggTable.getSelectedRow();
						if (row >= 0) {
							aggEditorFrame =
									new AggEditorFrame(
											(jAudioFeatureExtractor.Aggregators.Aggregator) controller.activeAgg_
													.getAggregator(row), controller);
							aggEditorFrame.setVisible(true);
							((ActiveAggTableModel) activeAggTable.getModel()).setAggregator(row, aggEditorFrame
									.getAggregator(), aggEditorFrame.isEdited());
						}
					}
				}
			});
		}
		return activeAggTable;
	}

	/**
	 * This method initializes AggList
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getAggList() {
		if (aggList == null) {
			aggList = new JScrollPane();
			aggList.setViewportView(getAggListTable());
			aggList.setBackground(OuterFrame.GRAY);
			aggList.getViewport().setBackground(OuterFrame.GRAY);
		}
		return aggList;
	}

	/**
	 * This method initializes AggListTable
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getAggListTable() {
		if (aggListTable == null) {
			aggListTable = new CustomJTable();
			controller.aggList_ = new AggListTableModel();
			controller.aggList_.init(controller.dm_.aggregatorMap);
			aggListTable.setModel(controller.aggList_);
			aggListTable.getColumnModel().getColumn(0).setPreferredWidth(60);
			aggListTable.getColumnModel().getColumn(0).setMaxWidth(60);
			aggListTable.getColumnModel().getColumn(1).setPreferredWidth(150);
			aggListTable.addMouseListener(new java.awt.event.MouseAdapter() {

				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (e.getClickCount() == 2) {
						int[] row = aggListTable.getSelectedRows();
						for (int i = 0; i < row.length; ++i) {
							AggListTableModel list = (AggListTableModel) aggListTable.getModel();
							Aggregator prototype = list.getAggregator(row[i]);
							Aggregator newAgg = (Aggregator) prototype.clone();
							((ActiveAggTableModel) activeAggTable.getModel()).addAggregator(newAgg);
						}
					}
				}
			});
		}
		return aggListTable;
	}

	/**
	 * This method initializes AggButtonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAggButtonPanel() {
		if (aggButtonPanel == null) {
			aggButtonPanel = new JPanel();
			aggButtonPanel.setLayout(new MigLayout("", "[144.00px:59px:150.00px]", "[25px][]"));
			aggButtonPanel.add(getAggAdd(), "cell 0 0,growx,aligny top");
			aggButtonPanel.setBackground(OuterFrame.BLACK_BACKGROUND);
			aggButtonPanel.add(getAggRemove(), "cell 0 1,growx,aligny top");
		}
		return aggButtonPanel;
	}

	/**
	 * This method initializes AggAdd
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAggAdd() {
		if (aggAdd == null) {
			aggAdd = new CustomJButton("Add");
			aggAdd.setToolTipText("Add a new Aggregator to be applied");
			aggAdd.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {
					int selectedRow = aggListTable.getSelectedRow();
					if (selectedRow != -1) {
						AggListTableModel list = (AggListTableModel) aggListTable.getModel();
						Aggregator prototype = list.getAggregator(selectedRow);
						String name = prototype.getAggregatorDefinition().name;
						boolean isAlreadySelected = false;
						for (int row = 0; row < activeAggTable.getRowCount(); row++) {
							if (name.equals(activeAggTable.getValueAt(row, 0))) {
								isAlreadySelected = true;
								break;
							}
						}
						if (!isAlreadySelected){
							Aggregator newAgg = (Aggregator) prototype.clone();
							((ActiveAggTableModel) activeAggTable.getModel()).addAggregator(newAgg);
						}
					}
				}
			});
		}
		return aggAdd;
	}

	/**
	 * This method initializes AggRemove
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAggRemove() {
		if (aggRemove == null) {
			aggRemove = new CustomJButton("Remove");
			aggRemove.setToolTipText("Remove an aggregator that has been previously defined");
			aggRemove.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {
					int row = activeAggTable.getSelectedRow();
					if (row >= 0) {
						((ActiveAggTableModel) activeAggTable.getModel()).removeAggregator(row);
					}
				}
			});
		}
		return aggRemove;
	}

	/**
	 * This method initializes DoneButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDoneButton() {
		if (doneButton == null) {
			doneButton = new CustomJButton("Save");
			doneButton.setToolTipText("Save and exit aggregator editing.");
			doneButton.addActionListener(this);
		}
		return doneButton;
	}

	/**
	 * This method initializes Abort
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAbort() {
		if (abort == null) {
			abort = new CustomJButton("Cancel");
			abort.setToolTipText("Exit without saving.");
			abort.addActionListener(this);
		}
		return abort;
	}

	/**
	 * handles events on this window
	 */
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == doneButton) {
			if ((controller.activeAgg_.getAggregator() != null) && (controller.activeAgg_.getAggregator().length > 0)) {
				controller.dm_.aggregators = controller.activeAgg_.getAggregator();
				this.setVisible(false);
			} else {
				controller.dm_.aggregators = new Aggregator[] { new Mean() };
			}
		} else if (event.getSource() == abort) {
			this.setVisible(false);
		}
	}

	private JLabel getLblCurrentAggregators() {
		if (lblCurrentAggregators == null) {
			lblCurrentAggregators = new CustomJLabel("Current aggregators:");
			lblCurrentAggregators.setFont(new Font("Arial", Font.BOLD, 14));
		}
		return lblCurrentAggregators;
	}

	private JLabel getLblAggregatorsList() {
		if (lblAggregatorsList == null) {
			lblAggregatorsList = new CustomJLabel("Aggregators list:");
			lblAggregatorsList.setFont(new Font("Arial", Font.BOLD, 14));
		}
		return lblAggregatorsList;
	}
}
