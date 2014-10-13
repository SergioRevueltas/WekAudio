package jAudioFeatureExtractor.GeneralTools;

import jAudioFeatureExtractor.OuterFrame;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

public class FeatureDisplay extends DefaultTableCellRenderer {
	
	static final long serialVersionUID = 1;
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel tmp =  (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		// if checked
		if (((Boolean)table.getModel().getValueAt(row, 0)).booleanValue()) {
			setBackground(OuterFrame.GRAY_BACKGROUND);
		} else {
			setBackground(OuterFrame.GRAY_PANELS);
		}
		// if selected
		if (isSelected){
			setBackground(UIManager.getColor("MenuItem.selectionBackground"));
		}
		// if primary feature
		if(((Boolean)table.getModel().getValueAt(row,3)).booleanValue()){
			tmp.setFont(tmp.getFont().deriveFont(Font.BOLD, 12));		
			return tmp;
		}else{
			return tmp;
		}
		
	}

}
