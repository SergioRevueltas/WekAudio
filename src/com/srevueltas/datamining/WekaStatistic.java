package com.srevueltas.datamining;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;

public class WekaStatistic {

	private Classifier cls;
	private Evaluation eval;
	private String model;
	private String summary;
	private String confusionMatrix;
	private String staticsDetails;

	public WekaStatistic() {
		this.model = "No data available.";
		this.summary = "No data available.";
		this.confusionMatrix = "No data available.";
		this.staticsDetails = "No data available.";
	}

	public WekaStatistic(Classifier cls, Evaluation eval) {
		this.cls = cls;
		this.eval = eval;
		loadStatistics();
	}



	private void loadStatistics() {
		// save statics
		try {
			this.setSummary(eval.toSummaryString());
			this.setConfusionMatrix(eval.toMatrixString());
			this.setModel(cls.toString());
			this.setStaticsDetails(eval.toClassDetailsString());
		} catch (Exception e) {
			this.model = "No data available.";
			this.summary = "No data available.";
			this.confusionMatrix = "No data available.";
			this.staticsDetails = "No data available.";
			e.printStackTrace();
		}

	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getConfusionMatrix() {
		return confusionMatrix;
	}

	public void setConfusionMatrix(String confusionMatrix) {
		this.confusionMatrix = confusionMatrix;
	}

	public String getStaticsDetails() {
		return staticsDetails;
	}

	public void setStaticsDetails(String staticsDetails) {
		this.staticsDetails = staticsDetails;
	}

}
