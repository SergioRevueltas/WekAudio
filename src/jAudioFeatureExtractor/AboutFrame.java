package jAudioFeatureExtractor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.srevueltas.gui.CustomJButton;
import com.srevueltas.gui.CustomJLabel;

/**
 * AboutFrame
 *
 * @author Sergio Revueltas
 *
 */
public class AboutFrame extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;

	private Controller controller = null;

	private JPanel jContentPane = null;
	private JPanel descriptionPanel = null;

	private CustomJLabel descriptionTitle = null;
	private JLabel lblAuthor;
	private JLabel lblAuthorlocation;
	private JLabel lblLicense;
	private JLabel lblWeka;
	private JLabel lblJaudio;
	private JLabel lblJaudioauthors;
	private JLabel lblWekaauthors;
	private JPanel panel;

	private JButton btnWekAudio;
	private JButton btnJaudio;
	private JButton btnWeka;

	private JButton btnGithub;
	private JButton btnTwitter;
	private JButton btnGoogleplus;
	private JButton btnLinkedin;
	private JButton btnFacebook;
	private CustomJButton btnOK;

	private boolean entered;
	private JPanel panel_1;
	private JLabel lblVersion;

	public AboutFrame(Controller c) {
		super();
		controller = c;
		initialize();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnWekAudio)){
			openURL("https://github.com/SergioRevueltas/WekAudio");
			
		} else if(e.getSource().equals(btnJaudio)){
			openURL("http://jaudio.sourceforge.net/");
			
		} else if(e.getSource().equals(btnWeka)){
			openURL("http://www.cs.waikato.ac.nz/ml/weka/");

		} else if(e.getSource().equals(btnGithub)){
			openURL("https://github.com/SergioRevueltas");
			
		} else if(e.getSource().equals(btnTwitter)){
			openURL("https://twitter.com/SergioRevueltas");

		} else if(e.getSource().equals(btnGoogleplus)){
			openURL("https://plus.google.com/+sergiorevueltas");
			
		} else if(e.getSource().equals(btnLinkedin)){
			openURL("http://es.linkedin.com/in/sergiorevueltas");
			
		} else if(e.getSource().equals(btnFacebook)){
			openURL("https://www.facebook.com/sergio.revueltasestrada");
			
		} 
		
	}

	
	private void openURL(String url) {
		try {
			java.awt.Desktop.getDesktop().browse(new URI(url));
		} catch (IOException e1) {
			System.out.println("URL: "+url);
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			System.out.println("URL: "+url);
			e1.printStackTrace();
		}
	}
	/**
	 * This method initializes this frame
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setTitle("About");
		// Icon from http://icons8.com/icons/#!/1391/audio-file
		this.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("img/icon.png"));
		this.setBounds(new Rectangle(260, 95, 500, 500));
		this.getContentPane().setBackground(OuterFrame.GRAY_PANELS);
		// this.getContentPane().setLayout(new MigLayout("", "[67.00][]", "[93.00px:80.00px][60.00px:23px][]"));
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		// Cause program to react when the exit box is pressed
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});
		
		
		btnGithub.addActionListener(this);
		btnTwitter.addActionListener(this);
		btnGoogleplus.addActionListener(this);
		btnLinkedin.addActionListener(this);
		btnFacebook.addActionListener(this);
		btnOK.addActionListener(this);
		btnWekAudio.addActionListener(this);
		btnJaudio.addActionListener(this);
		btnWeka.addActionListener(this);
		
		btnWekAudio.setToolTipText("TODO URL");
		btnJaudio.setToolTipText("http://jaudio.sourceforge.net/");
		btnWeka.setToolTipText("http://www.cs.waikato.ac.nz/ml/weka/");

	}

	private void cancel() {
		controller.getFrame().setEnabled(true);
		controller.getFrame().toFront();
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
			jContentPane.setLayout(new MigLayout("", "[520.00px]", "[400.00px:n:400.00px,grow]"));
			jContentPane.add(getDescription(), "cell 0 0,grow");
		}
		return jContentPane;
	}

	/**
	 * This method initializes Description
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getDescription() {
		if (descriptionPanel == null) {
			descriptionPanel = new JPanel();
			descriptionPanel
					.setLayout(new MigLayout("", "[330.00px:n:330.00px,grow][120.00:n,grow]", "[10.00px:n:10.00px][30.00px:n:30.00px][12.00:n][30.00px:n:30.00px][5.00px:n:5.00px][30.00px:n:30.00px][30.00px:n:30.00px][5.00px:n:5.00px][30.00px:n:30.00px][30.00px:n:30.00px][40.00px:n:40.00px][20.00px:n:20.00px][15.00px:n:15.00px][60.00px:n:60.00px][16.00px:n:16.00px][]"));
			descriptionPanel.setBackground(OuterFrame.GRAY_PANELS);
			descriptionTitle = new CustomJLabel("WekAudio");
			descriptionTitle.setFont(new Font("Arial", Font.BOLD, 32));
			descriptionPanel.add(descriptionTitle, "cell 0 1,alignx center,aligny bottom");
			descriptionPanel.add(getBtnWekaaudio(), "cell 1 1 1 3,alignx center,aligny center");
			descriptionPanel.add(getLblVersion(), "cell 0 2,alignx center");
			descriptionPanel.add(getLblLicense(), "cell 0 3,alignx center,aligny center");
			descriptionPanel.add(getLblJaudio(), "cell 0 5,alignx center,aligny bottom");
			descriptionPanel.add(getBtnJaudio(), "cell 1 5 1 2,alignx center,aligny center");
			descriptionPanel.add(getLblJaudioauthors(), "cell 0 6,alignx center,aligny top");
			descriptionPanel.add(getLblWeka(), "cell 0 8,alignx center,aligny bottom");
			descriptionPanel.add(getBtnWeka(), "cell 1 8 1 2,alignx center,aligny center");
			descriptionPanel.add(getLblWekaauthors(), "cell 0 9,alignx center,aligny top");
			descriptionPanel.add(getLblAuthor(), "cell 0 11 2 1,alignx center,aligny bottom");
			descriptionPanel.add(getLblAuthorlocation(), "cell 0 12 2 1,alignx center,aligny top");
			descriptionPanel.add(getPanel_1(), "flowx,cell 0 15 2 1,alignx center");
			descriptionPanel.add(getPanel(), "cell 0 13 2 1,alignx center,aligny top");
		}
		return descriptionPanel;
	}

	/**
	 * This method initializes Cancel
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOKbutton() {
		if (btnOK == null) {
			btnOK = new CustomJButton("Ok");
			btnOK.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {
					cancel();
				}
			});
			btnOK.setFocusable(false);
			btnOK.setFocusPainted(false);
			btnOK.setRequestFocusEnabled(false);
			btnOK.setOpaque(false);
			btnOK.setBorderPainted(false);
			btnOK.setRolloverEnabled(false);

		}
		return btnOK;
	}

	private JLabel getLblAuthor() {
		if (lblAuthor == null) {
			lblAuthor = new CustomJLabel("\tCreated by Sergio Revueltas.\n");
			lblAuthor.setFont(new Font("Arial", Font.PLAIN, 14));
		}
		return lblAuthor;
	}

	private JLabel getLblAuthorlocation() {
		if (lblAuthorlocation == null) {
			lblAuthorlocation = new CustomJLabel("University of Almeria, Spain. (2014)");
			lblAuthorlocation.setFont(new Font("Arial", Font.PLAIN, 10));
		}
		return lblAuthorlocation;
	}

	private JLabel getLblLicense() {
		if (lblLicense == null) {
			lblLicense = new CustomJLabel("GNU General Public License version 2.0 (GPLv2)");
			// new CustomJLabel(
			// "<html><a href=\"http://www.google.es\">GNU General Public License version 2.0 (GPLv2)</a></html>.");
			Font font = new Font("Arial", Font.PLAIN, 11);
			lblLicense.setFont(font);
			lblLicense.setForeground(Color.BLUE);
			lblLicense.setEnabled(true);
			lblLicense.setFocusable(true);
			lblLicense.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
					lblLicense.setFont(new Font("Arial", Font.PLAIN, 11));
					lblLicense.setForeground(Color.BLUE);
					try {
						if (entered) {
							java.awt.Desktop.getDesktop().browse(new URI("http://www.gnu.org/licenses/gpl-2.0.html"));
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					Font font = new Font("Arial", Font.PLAIN, 11);
					Map<TextAttribute, Integer> attributes = (Map<TextAttribute, Integer>) font.getAttributes();
					attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
					lblLicense.setFont(font.deriveFont(attributes));
					lblLicense.setForeground(Color.magenta.darker());

				}

				@Override
				public void mouseExited(MouseEvent e) {
					Cursor cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
					setCursor(cursor);
					lblLicense.setForeground(Color.BLUE);
					lblLicense.setFont(new Font("Arial", Font.PLAIN, 11));
					entered = false;
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					Font font = new Font("Arial", Font.PLAIN, 11);
					Map<TextAttribute, Integer> attributes = (Map<TextAttribute, Integer>) font.getAttributes();
					attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
					lblLicense.setFont(font.deriveFont(attributes));

					lblLicense.setForeground(Color.BLUE);
					Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
					setCursor(cursor);
					entered = true;
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

				}
			});
		}
		return lblLicense;
	}

	private JLabel getLblWeka() {
		if (lblWeka == null) {
			lblWeka = new CustomJLabel("Data Mining Software powered by Weka.");
			lblWeka.setFont(new Font("Arial", Font.BOLD, 12));
		}
		return lblWeka;
	}

	private JLabel getLblJaudio() {
		if (lblJaudio == null) {
			lblJaudio = new CustomJLabel("Fork of jAudio project.");
			lblJaudio.setFont(new Font("Arial", Font.BOLD, 14));
		}
		return lblJaudio;
	}

	private JLabel getLblJaudioauthors() {
		if (lblJaudioauthors == null) {
			lblJaudioauthors = new CustomJLabel("Created by Daniel McEnnis and Cory McKay.");
			lblJaudioauthors.setFont(new Font("Arial", Font.PLAIN, 10));
		}
		return lblJaudioauthors;
	}

	private JButton getBtnJaudio() {
		if (btnJaudio == null) {
			btnJaudio = new JButton("jAudio");
			Icon normalIcon = new ImageIcon("img/jAudioLogo60x60.png");
			btnJaudio = new CustomJButton(normalIcon, normalIcon, normalIcon);
		}
		return btnJaudio;
	}

	private JButton getBtnWeka() {
		if (btnWeka == null) {
			btnWeka = new JButton("weka");
			Icon normalIcon = new ImageIcon("img/wekaLogo60x60.png");
			btnWeka = new CustomJButton(normalIcon, normalIcon, normalIcon);
		}
		return btnWeka;
	}

	private JButton getBtnTwitter() {
		if (btnTwitter == null) {
			Icon normalIcon = new ImageIcon("img/twitterNormal100ppp.png");
			Icon selectedIcon = new ImageIcon("img/twitterSelected100ppp.png");
			Icon pressedIcon = new ImageIcon("img/twitterPressed100ppp.png");

			btnTwitter = new CustomJButton(normalIcon, selectedIcon, pressedIcon);
		}
		return btnTwitter;
	}

	private JButton getBtnGithub() {
		if (btnGithub == null) {
			Icon normalIcon = new ImageIcon("img/githubNormal100ppp.png");
			Icon selectedIcon = new ImageIcon("img/githubSelected100ppp.png");
			Icon pressedIcon = new ImageIcon("img/githubPressed100ppp.png");

			btnGithub = new CustomJButton(normalIcon, selectedIcon, pressedIcon);
		}
		return btnGithub;
	}

	private JButton getBtnGoogleplus() {
		if (btnGoogleplus == null) {
			Icon normalIcon = new ImageIcon("img/gplusNormal100ppp.png");
			Icon selectedIcon = new ImageIcon("img/gplusSelected100ppp.png");
			Icon pressedIcon = new ImageIcon("img/gplusPressed100ppp.png");

			btnGoogleplus = new CustomJButton(normalIcon, selectedIcon, pressedIcon);
		}
		return btnGoogleplus;
	}

	private JButton getBtnLinkedin() {
		if (btnLinkedin == null) {
			Icon normalIcon = new ImageIcon("img/linkedinNormal100ppp.png");
			Icon selectedIcon = new ImageIcon("img/linkedinSelected100ppp.png");
			Icon pressedIcon = new ImageIcon("img/linkedinPressed100ppp.png");

			btnLinkedin = new CustomJButton(normalIcon, selectedIcon, pressedIcon);
		}
		return btnLinkedin;
	}

	private JLabel getLblWekaauthors() {
		if (lblWekaauthors == null) {
			lblWekaauthors = new CustomJLabel("The University of Waikato");
			lblWekaauthors.setFont(new Font("Arial", Font.PLAIN, 10));
		}
		return lblWekaauthors;
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBackground(OuterFrame.GRAY_PANELS);
			panel.setLayout(new MigLayout("", "[][][][][]", "[40.00:n:40.00]"));
			panel.add(getBtnGithub(), "cell 0 0,alignx center,aligny center");
			panel.add(getBtnTwitter(), "flowy,cell 1 0");
			panel.add(getBtnFacebook(), "cell 2 0");
			panel.add(getBtnGoogleplus(), "cell 3 0");
			panel.add(getBtnLinkedin(), "cell 4 0");
		}
		return panel;
	}

	private JButton getBtnWekaaudio() {
		if (btnWekAudio == null) {
			btnWekAudio = new JButton("wekaAudio");
			Icon normalIcon = new ImageIcon("img/icon60x60.png");
			btnWekAudio = new CustomJButton(normalIcon, normalIcon, normalIcon);
		}
		return btnWekAudio;
	}

	private JButton getBtnFacebook() {
		if (btnFacebook == null) {
			Icon normalIcon = new ImageIcon("img/facebookNormal100ppp.png");
			Icon selectedIcon = new ImageIcon("img/facebookSelected100ppp.png");
			Icon pressedIcon = new ImageIcon("img/facebookPressed100ppp.png");

			btnFacebook = new CustomJButton(normalIcon, selectedIcon, pressedIcon);
		}
		return btnFacebook;
	}

	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.setLayout(new MigLayout("ins 0", "[60.00px:n:60.00px]", "[][]"));
			panel_1.add(getOKbutton(), "cell 0 0,growx");
			panel_1.setBackground(OuterFrame.GRAY_PANELS);
		}
		return panel_1;
	}
	private JLabel getLblVersion() {
		if (lblVersion == null) {
			lblVersion = new CustomJLabel("v1.0");
		}
		return lblVersion;
	}
}
