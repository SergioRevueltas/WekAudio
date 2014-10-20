/**
 * 
 */
package jAudioFeatureExtractor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Rectangle;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;

import com.srevueltas.gui.CustomJButton;
import com.srevueltas.gui.CustomJLabel;

/**
 * AggEditorFrame
 * 
 * Provides a window for setting parameters and features on an aggregator.
 *
 * @author Daniel McEnnis
 *
 */
public class AboutFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private Controller controller = null;

	private JPanel jContentPane = null;

	private CustomJButton saveButton = null;

	private JPanel descriptionPanel = null;

	private CustomJButton cancelButton = null;

	private CustomJLabel descriptionTitle = null;

	private JPanel featureChooser = null;

	private JScrollPane chosenFeatures = null;

	private JTable chosenFieldTable = null;

	private JPanel featureControls = null;

	private JScrollPane featureList = null;

	private JTable featureListTable = null;

	private JButton addFeature = null;

	private JButton removeFeature = null;

	private JPanel chosenFeaturePanel = null;

	private JPanel featureListPanel = null;

	private JLabel lblAuthor;
	private JLabel lblAuthorlocation;
	private JLabel lblLicense;
	private JLabel lblWeka;
	private JLabel lblJaudio;
	private JButton btnJaudio;
	private JButton btnWeka;
	private JButton btnTwitter;
	private JButton btnGithub;
	private JButton btnGoogleplus;
	private JButton btnLinkedin;
	private JLabel lblJaudioauthors;
	private JLabel lblWekaauthors;
	private JPanel panel;
	private JButton btnWekaaudio;
	private JButton btnFacebook;

	/**
	 * This is the default constructor
	 */
	public AboutFrame(Controller c) {
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
		this.setTitle("About WekaAudio");
		// Icon from http://icons8.com/icons/#!/1391/audio-file
		this.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("img/icon.png"));
		this.setBounds(new Rectangle(260, 60, 500, 440));
		this.getContentPane().setBackground(OuterFrame.GRAY_PANELS);
		// this.getContentPane().setLayout(new MigLayout("", "[67.00][]", "[93.00px:80.00px][60.00px:23px][]"));
		this.setAlwaysOnTop(true);
		// Cause program to react when the exit box is pressed
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});

	}

	private void cancel() {
		this.setVisible(false);
		controller.getFrame().setEnabled(true);
		controller.getFrame().toFront();
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
					.setLayout(new MigLayout("", "[330.00px:n:330.00px,grow][120.00:n,grow]", "[15.00px:n:15.00px][30.00px:n:30.00px][][15.00px:n:15.00px][25.00px:n:25.00px][][15.00px:n:15.00px][25.00px:n:25.00px][][30.00px:n:30.00px][20.00px:n:20.00px][15.00px:n:15.00px][50.00px:n:50.00px][20.00px:n:20.00px][]"));
			descriptionPanel.setBackground(OuterFrame.GRAY_PANELS);
			descriptionTitle = new CustomJLabel("WekaAudio");
			descriptionTitle.setFont(new Font("Arial", Font.BOLD, 20));
			descriptionPanel.add(descriptionTitle, "cell 0 1,alignx center,aligny bottom");
			descriptionPanel.add(getBtnWekaaudio(), "cell 1 1 1 2,grow");
			descriptionPanel.add(getLblLicense(), "cell 0 2,alignx center,aligny center");
			descriptionPanel.add(getLblJaudio(), "cell 0 4,alignx center,aligny bottom");
			descriptionPanel.add(getBtnJaudio(), "cell 1 4 1 2,grow");
			descriptionPanel.add(getLblJaudioauthors(), "cell 0 5,alignx center,aligny top");
			descriptionPanel.add(getLblWeka(), "cell 0 7,alignx center,aligny bottom");
			descriptionPanel.add(getBtnWeka(), "cell 1 7 1 2,grow");
			descriptionPanel.add(getLblWekaauthors(), "cell 0 8,alignx center,aligny top");
			descriptionPanel.add(getLblAuthor(), "cell 0 10 2 1,alignx center,aligny bottom");
			descriptionPanel.add(getLblAuthorlocation(), "cell 0 11 2 1,alignx center,aligny top");
			descriptionPanel.add(getOKbutton(), "cell 0 14 2 1,alignx center");
			descriptionPanel.add(getPanel(), "cell 0 12 2 1,alignx center,aligny top");
		}
		return descriptionPanel;
	}

	/**
	 * This method initializes Cancel
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOKbutton() {
		if (cancelButton == null) {
			cancelButton = new CustomJButton("Ok");
			cancelButton.setToolTipText("Return to previous window without changing the aggregator");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {
					cancel();
				}
			});
		}
		return cancelButton;
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
						java.awt.Desktop.getDesktop().browse(new URI("http://www.gnu.org/licenses/gpl-2.0.html"));
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
			lblJaudio = new CustomJLabel("Fork from jAudio project.");
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
		}
		return btnJaudio;
	}

	private JButton getBtnWeka() {
		if (btnWeka == null) {
			btnWeka = new JButton("weka");
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
			panel.setLayout(new MigLayout("", "[][][][][]", "[]"));
			panel.add(getBtnGithub(), "cell 0 0,alignx center,aligny center");
			panel.add(getBtnTwitter(), "flowy,cell 1 0");
			panel.add(getBtnFacebook(), "cell 2 0");
			panel.add(getBtnGoogleplus(), "cell 3 0");
			panel.add(getBtnLinkedin(), "cell 4 0");
		}
		return panel;
	}

	private JButton getBtnWekaaudio() {
		if (btnWekaaudio == null) {
			btnWekaaudio = new JButton("wekaAudio");
		}
		return btnWekaaudio;
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
}
