package com.srevueltas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import net.miginfocom.swing.MigLayout;

import com.srevueltas.core.AudioManager;


public class MainWindow extends JFrame implements ActionListener{

	private JMenuBar jmenuBar;
	private JMenu menuArchivo;
	private JMenuItem abrir;
	private JMenuItem salir;
	private JMenu menuEdicion;
	private JMenuItem calcularPitch;
	private JMenuItem calcularMFCCs;
	private JLabel lblChannel;
	
	private JPanel rawWavePanelContainer;
	private JPanel channel0Container;
	private JPanel channel1Container;
	private WavePanel rawWavePanelChannel0;	
	private WavePanel rawWavePanelChannel1;
	private AudioManager audioManager;
	private JSeparator separator;
	private JPanel audioInfoPanel;
	private JTextArea txtrAudioinfo;

	public MainWindow() {
		super("SergioRevueltasPFC");
		loadGUI();
		audioManager = new AudioManager();
		
		show();
		validate();
		repaint();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void loadGUI() {
		setResizable(true);
		setBounds(200,200, 1024, 800);
		rawWavePanelChannel0 = null;
		rawWavePanelChannel1 = null;
		loadJMenuBar();
		setJMenuBarListening();
		getContentPane().setLayout(new MigLayout("", "[1008px,grow]", "[::40.00px,grow][294.00px:42.00px:291.00px]"));
		
		audioInfoPanel = new JPanel();
		getContentPane().add(audioInfoPanel, "cell 0 0,grow");
		audioInfoPanel.setLayout(new BorderLayout(0, 0));
		
		/*txtrAudioinfo = new JTextArea();
		txtrAudioinfo.setBackground(SystemColor.menu);
		txtrAudioinfo.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
		txtrAudioinfo.setEditable(false);
		audioInfoPanel.add(txtrAudioinfo);
		*/
		rawWavePanelContainer = new JPanel();
		rawWavePanelContainer.setLayout(new MigLayout("", "[:5.00sp:5.00sp,grow][::45.00sp,grow]", "[50.87%:109.00:44.94%,grow][][:33.00:40.00][50.29%:168.00px:44.90%,grow,center]"));
		
		getContentPane().add(rawWavePanelContainer, "cell 0 1,grow");
		
		lblChannel = new JLabel("Channel 0");
		rawWavePanelContainer.add(lblChannel, "cell 0 0,alignx center,aligny center");
		rawWavePanelContainer.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
		
		channel0Container = new JPanel();
		rawWavePanelContainer.add(channel0Container, "cell 1 0,grow");
		channel0Container.setLayout(new BorderLayout());
		
		separator = new JSeparator();
		rawWavePanelContainer.add(separator, "cell 0 1 2 1,grow");
		separator.setVisible(true);
		
		JLabel lblChannel_1 = new JLabel("Channel 1");
		rawWavePanelContainer.add(lblChannel_1, "cell 0 3,alignx center,aligny center");
		
		channel1Container = new JPanel();
		rawWavePanelContainer.add(channel1Container, "cell 1 3,grow");
		channel1Container.setLayout(new BorderLayout());
	}

	private void loadJMenuBar() {
		jmenuBar = new JMenuBar();
	    menuArchivo = new JMenu("Archivo"); 
	    abrir = new JMenuItem("Abrir");
	    abrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
	    salir = new JMenuItem("Salir");
	    salir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
	    
	    menuEdicion = new JMenu("Edicion");
	    calcularPitch = new JMenuItem("Calcular Pitch");
	    calcularPitch.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
	    calcularMFCCs =new JMenuItem("Calcular MFCCs");
	    calcularMFCCs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, Event.CTRL_MASK));
	    calcularPitch.setEnabled(false);
	    calcularMFCCs.setEnabled(false);

	    menuArchivo.add(abrir);
	    menuArchivo.add(salir);
	    menuEdicion.add(calcularPitch);
	    menuEdicion.add(calcularMFCCs);
	    jmenuBar.add(menuArchivo);
		jmenuBar.add(menuEdicion);
		
		this.setJMenuBar(jmenuBar);	
		
	}
	
	private void setJMenuBarListening() {
		abrir.addActionListener(this);
	    salir.addActionListener(this);
	    calcularPitch.addActionListener(this);
	    calcularMFCCs.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==abrir){
			abrirWav();
		}
		if(e.getSource()==salir){
			System.out.println("salir");
			this.dispose();
		}
		if(e.getSource()==calcularPitch){
			//TODO pitch
		}
		if(e.getSource()==calcularMFCCs){
			//TODO mfccs
		}
	}

	private void abrirWav() {
		//configuramos el filechooser y el filtro de archivos
		JFileChooser jfc = new JFileChooser("audioFiles");
		jfc.setDialogTitle("Abrir");
		FileFilter filtro = new FileFilter() {
			@Override
			public String getDescription() {
				return "wav";
			}
			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
				      return true;
				}
				if (file.getName().toLowerCase().endsWith(".wav")){
					return true;
				}
				return false;
			}
		};
		jfc.setFileFilter(filtro);
		//mostramos el dialog
		int seleccion = jfc.showOpenDialog(this);
		if(seleccion == JFileChooser.APPROVE_OPTION){
			String rutaAbs = jfc.getSelectedFile().getAbsolutePath();
			audioManager.cargarWav(rutaAbs);
			//txtrAudioinfo.setText(audioManager.getAudioInfo());
			channel0Container.removeAll();
			channel1Container.removeAll();
			rawWavePanelChannel0 = new WavePanel(audioManager.getAudioData()[0],audioManager.getSampleRate(),audioManager.getSampleSizeInBits());
			channel0Container.add(rawWavePanelChannel0, BorderLayout.CENTER);
			if(audioManager.getNumChannels() == 2) {
				rawWavePanelChannel1 = new WavePanel(audioManager.getAudioData()[1],audioManager.getSampleRate(),audioManager.getSampleSizeInBits());
				channel1Container.add(rawWavePanelChannel1, BorderLayout.CENTER);
			}
			validate();
			repaint();
		}
		
	}
	

}
