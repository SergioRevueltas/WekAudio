package com.srevueltas.gui;

import jAudioFeatureExtractor.OuterFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxEditor;

/**
 * http://www.codejava.net/java-se/swing/create-custom-gui-for-jcombobox
 * @author Sergio Revueltas
 */

public class CustomJComboBox extends JComboBox{
	
	private static final long serialVersionUID = 1L;
	private DefaultComboBoxModel model;
    
    public CustomJComboBox() {
        model = new DefaultComboBoxModel();
        setModel(model);
        setRenderer(new ClassifierItemRenderer());
        setEditor(new ClassifierItemEditor());
    }
     
    public void addItems(String[][] items) {
        for (String[] anItem : items) {
            model.addElement(anItem);
        }
    }
	
    
    /**
     * Customer renderer for JComboBox
     * @author www.codejava.net
     *
     */
    public class ClassifierItemRenderer extends JPanel implements ListCellRenderer {
        private CustomJLabel labelItem = new CustomJLabel();
         
        public ClassifierItemRenderer() {
            setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 1.0;
            constraints.insets = new Insets(0, 0, 0, 0);
             
            labelItem.setOpaque(true);
            labelItem.setHorizontalAlignment(JLabel.LEFT);
             
            add(labelItem, constraints);
            setBackground(OuterFrame.GRAY_PANELS);
        }
         
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            String classifierItem = (String) value;
     
            // set classifier name
            labelItem.setText(classifierItem);
             
    		setFont(new Font("Arial", Font.PLAIN, 10)); 
    		labelItem.setFont(new Font("Arial", Font.PLAIN, 10)); 

            
            if (isSelected) {
            	labelItem.setForeground(Color.WHITE);
                labelItem.setBackground(OuterFrame.GRAY_BACKGROUND);
            } else {
                labelItem.setForeground(Color.WHITE);
                labelItem.setBackground(OuterFrame.GRAY_BOXES_LINE);
            }
             
            return this;
        }
     
    }
    
    /**
     * Editor for JComboBox
     * @author wwww.codejava.net
     *
     */
    public class ClassifierItemEditor extends BasicComboBoxEditor {
        private JPanel panel = new JPanel();
        private JLabel labelItem = new JLabel();
        private String selectedValue;
         
        public ClassifierItemEditor() {
            panel.setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 1.0;
            constraints.insets = new Insets(2, 5, 2, 2);
             
            labelItem.setOpaque(false);
            labelItem.setHorizontalAlignment(JLabel.LEFT);
            labelItem.setForeground(Color.WHITE);
             
            panel.add(labelItem, constraints);
            panel.setBackground(Color.BLUE);       
        }
         
        public Component getEditorComponent() {
            return this.panel;
        }
         
        public Object getItem() {
            return this.selectedValue;
        }
         
        public void setItem(Object item) {
            if (item == null) {
                return;
            }
            String classifierItem = (String) item;
            selectedValue = classifierItem;
            labelItem.setText(selectedValue);
        }  
    }
    
}
