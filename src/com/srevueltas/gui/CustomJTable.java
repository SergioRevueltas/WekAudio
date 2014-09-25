package com.srevueltas.gui;

import jAudioFeatureExtractor.OuterFrame;
import jAudioFeatureExtractor.RecordingsTableModel;
import jAudioFeatureExtractor.SortingTableModelDecorator;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTable;


public class CustomJTable extends JTable{

	
	public CustomJTable(RecordingsTableModel rtm_) {
		super(rtm_);
		loadSettings();
	}

	public CustomJTable(SortingTableModelDecorator decorator) {
		super(decorator);
		loadSettings();
	}
	
	public CustomJTable() {
		super();
		loadSettings();
	}

	private void loadSettings(){
		getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
		getTableHeader().setOpaque(false);
		getTableHeader().setBackground(OuterFrame.GRAY_BOXES_LINE);
		getTableHeader().setForeground(Color.WHITE);
		setFont(new Font("Arial", Font.PLAIN, 10));
		setBackground(OuterFrame.GRAY_PANELS);
		setForeground(Color.WHITE);
	}
}
