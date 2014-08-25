package jAudioFeatureExtractor.ACE.XMLParsers;

import java.io.File;

import javax.swing.filechooser.FileFilter;


/**
 * A file filter for the JFileChooser class.
 * Implements the two methods of the FileFilter
 * abstract class.
 * 
 * @author	Sergio Revueltas
 * @see		FileFilter
 */
public class FileFilterARFF extends FileFilter {
	public boolean accept(File f)
	{
		return f.getName().toLowerCase().endsWith(".arff") || f.isDirectory();
	}

	public String getDescription()
	{
		return "ARFF File";
	}
}
