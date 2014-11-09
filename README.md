# WekAudio

This application is a prototype tool for audio pattern recognition by artificial intelligence. The purpose of this project is project is purely educational.

The main function can be summarized in two stages: training and classification.

![alt tag](https://github.com/SergioRevueltas/WekAudio/blob/master/help/img/HelpDiagram600.png)

## TRAINING

The training is the process that consists of loading, pre-processing, analysis and feature extraction of audio files indicating that class or type are. From that process it is obtained a model based on a classifier (Bayesian network, decision tree, ...) that subsequently allow recognize new patterns of these audio classes.

In another words, it comes to teaching the application to recognize particular audio signals or patterns like bells, horns, hooters, animals, instruments, human voices… by loading a lot of audio patterns of those classes to gain the knowledge that will allow classify and identify them in the future.

To train the application properly, we have to load at least 2 classes of audio patterns (e.g., siren and bell) to discern between them, of which we must load sufficient number of audio files (10 total at least). It is obvious to say that while we load more files of each class, training and classification will be more accurate.  

In this application, training process consists of 3 steps:
* 1. RECORDINGS panel: To load files
* 2. FEATURES panel: To choose features, compression method (Aggregator) and Analysis options.
* 3. DATA MINING panel: To choose classifier and to train.


##CLASSIFICATION

The classification is the process that consists of loading, pre-processing, analysis and feature extraction of audio files without indicating what class or type are, so the application is able to determine each class using a previously stored model or classifier. 

In this application, classification process consists of 3 steps:
* 1. FEATURES panel: To choose features, compression method (Aggregator) and Analysis options.
* 2. DATA MINING panel: To load an unknown audio file and previously stored model or classifier.
* 3. DATA MINING panel: Classify.



## License
WekAudio v.1.0
Copyright (C) 2014 Sergio Revueltas

This software is free software; you can redistribute it
and/or modify it under the terms of the GNU General 
Public License as published by the Free Software Foundation.

This software is distributed in the hope that it will be
useful, but WITHOUT ANY WARRANTY; without even the implied
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public 
License along with this software; if not, write to the Free 
Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139,
USA.

####Fork of jAudio by Cory McKay and Daniel McEnnis
* [jAudio](http://jmir.sourceforge.net/jAudio.html)
* [jMIR](https://github.com/DDMAL/jMIR)

####Data Mining powered by Weka
* [Weka](http://www.cs.waikato.ac.nz/ml/weka/)

####Follow me
[@SergioRevueltas](https://twitter.com/SergioRevueltas)