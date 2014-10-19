package com.srevueltas.datamining;


public class WekaStatistic {

	private String model;
	private String summary;
	private String confusionMatrix;
	private String staticsDetails;
	
	
	public WekaStatistic() {
		model = "No data available.";
		summary = "No data available.";
		confusionMatrix = "No data available.";
		staticsDetails = "No data available.";
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
