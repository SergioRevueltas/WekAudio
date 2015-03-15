package jAudioFeatureExtractor;

/*
Core SWING Advanced Programming 
By Kim Topley
ISBN: 0 13 083292 8       
Publisher: Prentice Hall  
*/

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.miginfocom.swing.MigLayout;

public class HelpMenuFrame extends JFrame {

	private Controller controller;
	private JScrollPane scrollPane;
	private JEditorPane editorPane;
	private File file;
	private String absolutePath;

	public HelpMenuFrame(Controller controller) {
		super("Help");
		this.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("img/icon.png"));
		this.controller = controller;
		this.getContentPane().setBackground(OuterFrame.GRAY_PANELS);
		this.setBackground(OuterFrame.GRAY_PANELS);
		this.setFont(new Font("Arial", Font.PLAIN, 10));
		this.setBounds(new Rectangle(100, 50, 800, 600));
		this.setResizable(false);
		getContentPane().setLayout(new MigLayout("", "[grow]", "[grow]"));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		editorPane = new JEditorPane();
		scrollPane = new JScrollPane(editorPane);
		getContentPane().add(scrollPane, "cell 0 0,grow");
		editorPane.setEditable(false); // Start read-only
		editorPane.setBackground(OuterFrame.GRAY_PANELS);
		editorPane.setFont(OuterFrame.NORMAL_FONT);

		// Listen for page load to complete
		editorPane.addPropertyChangeListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("page")) {
					setCursor(Cursor.getDefaultCursor());

					// Allow editing and saving if appropriate
					// editorPane.setEditable(file.canWrite());
				}
			}
		});

		loadUrl();

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});

	}

	private void cancel() {
		this.controller.getFrame().setEnabled(true);
		this.controller.getFrame().toFront();
		this.setVisible(false);
	}

	private void loadUrl() {
		String relativePath = "help/index.html";
		this.file = new File(relativePath);
		this.absolutePath = file.getAbsolutePath();
		String url = "file:///" + absolutePath;

		try {
			// Check if the new page and the old
			// page are the same.
			URL newURL = new URL(url);
			URL loadedURL = editorPane.getPage();
			if (loadedURL != null && loadedURL.sameFile(newURL)) {
				return;
			}

			// Try to display the page
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			// Busy cursor
			editorPane.setEditable(false);
			editorPane.setPage(url);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(editorPane, new String[] {
					"Unable to open file", url }, "File Open Error",
					JOptionPane.ERROR_MESSAGE);
			setCursor(Cursor.getDefaultCursor());
			cancel();
		}
	}

}
