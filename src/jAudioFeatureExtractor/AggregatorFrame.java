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

	private JScrollPane ActiveAggList = null;
	
	private CustomJTable ActiveAggTable = null;

	private JScrollPane AggList = null;
	
	private CustomJTable AggListTable = null;

	private JPanel AggButtonPanel = null;

	private CustomJButton AggAdd = null;

	private CustomJButton AggRemove = null;
	
	private AggEditorFrame aggEditorFrame = null;
	
	private Controller controller;

	private CustomJButton DoneButton = null;

	private CustomJButton Abort = null;
	private CustomJLabel lblCurrentAggregators;
	private CustomJLabel lblAggregatorsList;

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
			jContentPane.setLayout(new MigLayout("", "[245.00px:452px][80.00:39.00][80.00px:32px][300.00:n]", "[][198.00px:462px:185.00px][::34.00]"));
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
		if (ActiveAggList == null) {
			ActiveAggList = new JScrollPane();
			ActiveAggList.setViewportView(getActiveAggTable());
			ActiveAggList.setBackground(OuterFrame.GRAY);
			ActiveAggList.getViewport().setBackground(OuterFrame.GRAY);
		}
		return ActiveAggList;
	}

	/**
	 * This method initializes ActiveAggTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getActiveAggTable() {
		if (ActiveAggTable == null) {
			ActiveAggTable = new CustomJTable();
			controller.activeAgg_ = new ActiveAggTableModel();
			controller.activeAgg_.init(controller);
			ActiveAggTable.setModel(controller.activeAgg_);
			ActiveAggTable.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if(e.getClickCount()==2){
						int row = ActiveAggTable.getSelectedRow();
						if(row>=0){
							aggEditorFrame = new AggEditorFrame((jAudioFeatureExtractor.Aggregators.Aggregator)controller.activeAgg_.getAggregator(row),controller);
							aggEditorFrame.setVisible(true);
							((ActiveAggTableModel)ActiveAggTable.getModel()).setAggregator(row, aggEditorFrame.getAggregator(), aggEditorFrame.isEdited());			
						}
					}
				}
			});
		}
		return ActiveAggTable;
	}

	/**
	 * This method initializes AggList	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getAggList() {
		if (AggList == null) {
			AggList = new JScrollPane();
			AggList.setViewportView(getAggListTable());
			AggList.setBackground(OuterFrame.GRAY);
			AggList.getViewport().setBackground(OuterFrame.GRAY);
		}
		return AggList;
	}

	/**
	 * This method initializes AggListTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getAggListTable() {
		if (AggListTable == null) {
			AggListTable = new CustomJTable();
			controller.aggList_ = new AggListTableModel();
			controller.aggList_.init(controller.dm_.aggregatorMap);
			AggListTable.setModel(controller.aggList_);
			AggListTable.getColumnModel().getColumn(0).setPreferredWidth(60);
			AggListTable.getColumnModel().getColumn(0).setMaxWidth(60);
			AggListTable.getColumnModel().getColumn(1).setPreferredWidth(150);
			AggListTable.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if(e.getClickCount()==2){
						int[] row = AggListTable.getSelectedRows();
						for(int i=0;i<row.length;++i){
							AggListTableModel list = (AggListTableModel)AggListTable.getModel();
							Aggregator prototype = list.getAggregator(row[i]);
							Aggregator newAgg = (Aggregator)prototype.clone();
							((ActiveAggTableModel)ActiveAggTable.getModel()).addAggregator(newAgg);							
						}
					}
				}
			});
		}
		return AggListTable;
	}

	/**
	 * This method initializes AggButtonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAggButtonPanel() {
		if (AggButtonPanel == null) {
			AggButtonPanel = new JPanel();
			AggButtonPanel.setLayout(new MigLayout("", "[144.00px:59px:150.00px]", "[25px][]"));
			AggButtonPanel.add(getAggAdd(), "cell 0 0,growx,aligny top");
			AggButtonPanel.setBackground(OuterFrame.BLACK_BACKGROUND);
			AggButtonPanel.add(getAggRemove(), "cell 0 1,growx,aligny top");
		}
		return AggButtonPanel;
	}

	/**
	 * This method initializes AggAdd	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAggAdd() {
		if (AggAdd == null) {
			AggAdd = new CustomJButton("Add");
			AggAdd.setToolTipText("Add a new Aggregator to be applied");
			AggAdd.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int row = AggListTable.getSelectedRow();
					if(row != -1){
						AggListTableModel list = (AggListTableModel)AggListTable.getModel();
						Aggregator prototype = list.getAggregator(row);
						Aggregator newAgg = (Aggregator)prototype.clone();
						((ActiveAggTableModel)ActiveAggTable.getModel()).addAggregator(newAgg);
					}
					System.out.println("actionPerformed()"); 				}
			});
		}
		return AggAdd;
	}

	/**
	 * This method initializes AggRemove	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAggRemove() {
		if (AggRemove == null) {
			AggRemove = new CustomJButton("Remove");
			AggRemove.setToolTipText("Remove an aggregator that has been previously defined");
			AggRemove.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int row = ActiveAggTable.getSelectedRow();
					if(row >= 0){
						((ActiveAggTableModel)ActiveAggTable.getModel()).removeAggregator(row);
					}
				}
			});
		}
		return AggRemove;
	}

	/**
	 * This method initializes DoneButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDoneButton() {
		if (DoneButton == null) {
			DoneButton = new CustomJButton("Save");
			DoneButton.setToolTipText("Save and exit aggregator editing.");
			DoneButton.addActionListener(this);
		}
		return DoneButton;
	}

	/**
	 * This method initializes Abort	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAbort() {
		if (Abort == null) {
			Abort = new CustomJButton("Cancel");
			Abort.setToolTipText("Exit without saving.");
			Abort.addActionListener(this);
		}
		return Abort;
	}
	
	/**
	 * handles events on this window
	 */
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == DoneButton){
			if((controller.activeAgg_.getAggregator() !=null)&&(controller.activeAgg_.getAggregator().length>0)){
				controller.dm_.aggregators = controller.activeAgg_.getAggregator();
				this.setVisible(false);
			}else{
				controller.dm_.aggregators = new Aggregator[]{new Mean()};
			}
		}else if(event.getSource() == Abort){
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
