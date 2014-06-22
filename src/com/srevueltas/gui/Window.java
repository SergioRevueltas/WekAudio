package com.srevueltas.gui;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import com.srevueltas.core.AudioManager;
import com.srevueltas.core.WavePanel;


public class Window extends JFrame implements ActionListener{

	private JMenuBar jmenuBar;
	private JMenu menuArchivo;
	private JMenuItem abrir;
	private JMenuItem salir;
	private JMenu menuEdicion;
	private JMenuItem calcularPitch;
	private JMenuItem calcularMFCCs;
	private WavePanel rawWavePanel;
	
	private AudioManager audioManager;
	
	public Window() {
		super("SergioRevueltasPFC");
		setResizable(true);
		setBounds(200,200, 500, 350);
		getContentPane().setLayout(new BorderLayout());

		audioManager = new AudioManager();
		
		loadJMenuBar();
		setJMenuBarListening();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   
		show();
		validate();
		repaint();
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
			abrir();
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

	private void abrir() {
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
			rawWavePanel = new WavePanel(audioManager.getAudioData()[0],audioManager.getSampleRate(),audioManager.getSampleSizeInBits()); 
			getContentPane().add(rawWavePanel, BorderLayout.CENTER);
			show();
			validate();
			repaint();
		}
		
	}
}
