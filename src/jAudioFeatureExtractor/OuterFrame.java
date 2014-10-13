/*
 * 
 * 
 * @(#)OuterFrame.java	1.0	April 5, 2005.
 *
 * McGill Univarsity
 */

package jAudioFeatureExtractor;

import jAudioFeatureExtractor.actions.ExecuteBatchAction;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import net.miginfocom.swing.MigLayout;
//import javax.help.*;

import org.multihelp.HelpWindow;

import com.srevueltas.gui.CustomJMenuBar;

/**
 * A panel holding various components of the jAudio Feature Extractor GUI
 * 
 * @author Cory McKay edited by Sergio Revueltas
 */
public class OuterFrame extends JFrame {
	/* FIELDS ***************************************************************** */

	static final long serialVersionUID = 1;
	//public static final Color BLUE = UIManager.getColor("MenuItem.selectionBackground");
	public static final Color GRAY_BOXES_LINE = Color.LIGHT_GRAY;
	public static final Color GRAY_PANELS = Color.GRAY;
	public static final Color GRAY_BACKGROUND = new Color(105, 105, 105);
	//public static final Color BLACK_BACKGROUND = UIManager.getColor("inactiveCaptionText");
	//public static final Color DARK_GRAY = Color.DARK_GRAY;
	public static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 12);
	public static final Font H1_FONT = new Font("Arial", Font.BOLD, 14);

	/**
	 * A panel allowing the user to select files to extract features from.
	 * <p>
	 * Audio files may also be played, edited and generated here. MIDI files may
	 * be converted to audio.
	 */
	public RecordingSelectorPanel recording_selector_panel;

	/**
	 * A panel allowing the user to select features to extract from audio files
	 * and extract the features. Basic feature parameters may be set and feature
	 * values and definitions can be saved to disk.
	 */
	public FeatureSelectorPanel feature_selector_panel;
	
	/**
	 * A panel allowing the user to train and classify.
	 */
	public DataMiningPanel dataMiningPanel;

	/**
	 * A class that contains all the logic for handling events fired from this
	 * gui. Utilizes the Mediator pattern to control dependencies between
	 * objects. Also contains all the menu bar actions.
	 */
	public Controller controller;

	/**
	 * Global menu bar for this application
	 */
	public CustomJMenuBar menu;

	/**
	 * Radio button for choosing the ACE data format
	 */
	public JRadioButtonMenuItem ace;

	/**
	 * Radio button for chosing the ARFF data format
	 */
	public JRadioButtonMenuItem arff;
	
	/**
	 * Window for displaying the help system.
	 */
	public HelpWindow helpWindow=null;

	/* CONSTRUCTOR ************************************************************ */

	/**
	 * Basic constructor that sets up the GUI.
	 */
	public OuterFrame(Controller c) {
		// Splash code modified by Daniel McEnnis from Graphic Java: Mastering the JFC: AWT
		SplashFrame splash = new SplashFrame();

		splash.loadSplash();		
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
		// Set window title
		setTitle("WekAudio");

		// Make quit when exit box pressed
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// set controller
		controller = c;

		ace = new JRadioButtonMenuItem("ACE");
		arff = new JRadioButtonMenuItem("ARFF");
		ButtonGroup bg = new ButtonGroup();
		bg.add(arff);
		bg.add(ace);

		getContentPane().setBackground(GRAY_BACKGROUND);
		Border border = BorderFactory.createLineBorder(GRAY_BOXES_LINE, 1);
		
		// Instantiate panels
		recording_selector_panel = new RecordingSelectorPanel(this, c);
		feature_selector_panel = new FeatureSelectorPanel(this, c);
		dataMiningPanel = new DataMiningPanel(this, c);
		feature_selector_panel.setBackground(GRAY_PANELS);
		feature_selector_panel.setBorder(border);
		recording_selector_panel.setBackground(GRAY_PANELS);
		recording_selector_panel.setBorder(border);
		dataMiningPanel.setBackground(GRAY_PANELS);
		dataMiningPanel.setBorder(border);

		controller.normalise = new JCheckBoxMenuItem("Normalise Recordings", true);
		arff.setSelected(true);
		ace.addActionListener(controller.outputTypeAction);
		arff.addActionListener(controller.outputTypeAction);
		controller.extractionThread = new ExtractionThread(controller, this);

		controller.executeBatchAction = new ExecuteBatchAction(controller, this);

		controller.removeBatch = new JMenu();
		controller.viewBatch = new JMenu();

		JMenuItem helpTopics = new JMenuItem("Help Topics");

		menu = new CustomJMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setForeground(Color.WHITE);
		fileMenu.setBackground(GRAY_BACKGROUND);
		fileMenu.add(c.addRecordingsAction);
		//recordingMenu.add(c.editRecordingsAction);
		fileMenu.add(c.recordFromMicAction);
		//fileMenu.add(c.saveAction);
		//fileMenu.add(c.saveBatchAction);
		//fileMenu.add(c.loadAction);
		//fileMenu.add(c.loadBatchAction);
		//fileMenu.addSeparator();
		//fileMenu.add(c.addBatchAction);
		//fileMenu.add(c.executeBatchAction);
		//controller.removeBatch = new JMenu("Remove Batch");
		//controller.removeBatch.setEnabled(false);
		//fileMenu.add(c.removeBatch);
		//controller.viewBatch = new JMenu("View Batch");
		//controller.viewBatch.setEnabled(false);
		//fileMenu.add(c.viewBatch);
		//fileMenu.addSeparator();
		fileMenu.add(c.exitAction);
		JMenu editMenu = new JMenu("Edit");
		editMenu.setForeground(Color.WHITE);
		editMenu.setBackground(GRAY_BACKGROUND);
		editMenu.add(c.cutAction);
		editMenu.add(c.copyAction);
		editMenu.add(c.pasteAction);
		
		JMenu recordingMenu = new JMenu("Recording");
		recordingMenu.setForeground(Color.WHITE);
		recordingMenu.setBackground(GRAY_BACKGROUND);
		
		recordingMenu.add(c.removeRecordingsAction);
		//recordingMenu.add(c.synthesizeAction);
		recordingMenu.add(c.viewFileInfoAction);
		//recordingMenu.add(c.storeSamples);
		//recordingMenu.add(c.validate);
		
		JMenu analysisMenu = new JMenu("Analysis");
		analysisMenu.setForeground(Color.WHITE);
		analysisMenu.setBackground(GRAY_BACKGROUND);
		//analysisMenu.add(c.globalWindowChangeAction);
		c.outputType = new JMenu("Output Format");
		c.outputType.add(ace);
		c.outputType.add(arff);
		//analysisMenu.add(c.outputType);
		c.sampleRate = new JMenu("Sample Rate (kHz)");
		/*
		c.sampleRate.add(sample8);
		c.sampleRate.add(sample11);
		c.sampleRate.add(sample16);
		c.sampleRate.add(sample22);
		c.sampleRate.add(sample44);
		*/
		//analysisMenu.add(c.sampleRate);
		analysisMenu.add(c.analisysOptionsAction);
		
		JMenu playbackMenu = new JMenu("Playback");
		playbackMenu.add(c.playNowAction);
		playbackMenu.add(c.playSamplesAction);
		playbackMenu.add(c.stopPlayBackAction);
		playbackMenu.add(c.playMIDIAction);
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setForeground(Color.WHITE);
		helpMenu.setBackground(GRAY_BACKGROUND);
		//helpMenu.add(helpTopics);
		helpMenu.add(c.aboutAction);

//		HelpSet hs = getHelpSet("Sample.hs");
//		HelpBroker hb = hs.createHelpBroker();
//
//		CSH.setHelpIDString(helpTopics, "top");
//		helpTopics.addActionListener(new CSH.DisplayHelpFromSource(hb));

		helpTopics.addActionListener(new AbstractAction(){

			public void actionPerformed(ActionEvent e) {
				System.out.println("Help Window Started");
				if(helpWindow == null){
					helpWindow = new HelpWindow();
				}
			}
		});
		
		menu.add(fileMenu);
		menu.add(editMenu);
		menu.add(recordingMenu);
		menu.add(analysisMenu);
		//menu.add(playbackMenu);
		menu.add(helpMenu);
		getContentPane().setLayout(new MigLayout("ins 2 2 -7 -7", "[335.00px][335.00px][335.00px]", "[21px][500.00px]"));
		getContentPane().add(recording_selector_panel, "cell 0 1,grow");
		getContentPane().add(feature_selector_panel, "cell 1 1,grow");
		getContentPane().add(dataMiningPanel,"cell 2 1,grow");
		getContentPane().add(menu, "cell 0 0 3 1,alignx left,aligny top");
	
		//Icon from http://icons8.com/icons/#!/1391/audio-file
		this.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("img/icon.png"));
		// Display GUI
		//this.setMaximumSize(new Dimension(10, 10));
		this.pack();
		splash.endSplash();
		splash = null;
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
	}

	/**
	 * This method creates the online help system.
	 * 
	 * @param helpsetfile
	 *            Name of the file where the help file metadata is stored.
	 * @return Reference to a newly created help set.
	 */
	public HelpWindow getHelpSet(String helpsetfile) {
		HelpWindow hs = new HelpWindow();
		return hs;
	}
	
	// helper class for creating a splashscreen from Graphic Java: Mastering the JFC: AWT
	protected class SplashFrame extends Frame{
			private java.awt.Window window = new java.awt.Window(this);
			//Icon from http://icons8.com/icons/#!/1391/audio-file
			private java.awt.Image image = java.awt.Toolkit.getDefaultToolkit().getImage("img/icon.png");
			private ImageCanvas canvas;
			
			public void loadSplash(){
				canvas = new ImageCanvas(image);
				window.add(canvas,"Center");
				//Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
				window.setLocation(200,50);
				window.pack();
				window.show();
				window.toFront();
			}
			
			public void endSplash(){
				window.dispose();
			}
	}
		
	protected class ImageCanvas extends Canvas{
		private Image image;
		public ImageCanvas(Image image){
			this.image=image;
			MediaTracker mt = new MediaTracker(this);
			mt.addImage(image,0);
			try{
				mt.waitForID(0);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		public void paint(Graphics g){
			g.drawImage(image,0,0,this);
		}
		public void update(Graphics g){
			paint(g);
		}
		public Dimension getPreferredSize(){
			return new Dimension(512,512);
		}	
	}
} 