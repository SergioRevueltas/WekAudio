package jAudioFeatureExtractor.GeneralTools;

import jAudioFeatureExtractor.OuterFrame;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

public class RecordingDisplay extends DefaultTableCellRenderer {
	
	static final long serialVersionUID = 1;
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel tmp =  (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		/*
		// if checked
		if (((Boolean)table.getModel().getValueAt(row, 0)).booleanValue()) {
			setBackground(OuterFrame.GRAY_BACKGROUND);
		} else {
			setBackground(OuterFrame.GRAY_PANELS);
		}
		*/
		// if error when classify
		String fileName = (String)table.getModel().getValueAt(row,1);
		String className = (String) table.getModel().getValueAt(row,2);
		if(!className.equals("???") && !fileName.contains(className)){
			setBackground(OuterFrame.GRAY_BACKGROUND);
			tmp.setFont(tmp.getFont().deriveFont(Font.BOLD, 12));		
		}else{
			setBackground(OuterFrame.GRAY_PANELS);
		}
		// if selected
		if (isSelected){
			setBackground(UIManager.getColor("MenuItem.selectionBackground"));
		}
		return tmp;
		
	}

}
