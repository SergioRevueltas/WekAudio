/**
 * Density Based Average
 * Created by Sergio Revueltas (2014) 
 * Based on P.Angelov and R.Yager paper 
 */
package jAudioFeatureExtractor.Aggregators;

import jAudioFeatureExtractor.ACE.DataTypes.AggregatorDefinition;
import jAudioFeatureExtractor.ACE.DataTypes.FeatureDefinition;
import jAudioFeatureExtractor.AudioFeatures.FeatureExtractor;

/**
 * Edited by Sergio Revueltas
 * 
 * Calculates the density based average of a feature accross all windows where it is defined.
 * When the feature has more than one dimension, the mean has an equal number of
 * dimensions and the value of each dimension is the mean of that dimension. If
 * the feature has a variable number of dimensions, the dimensionality of the
 * result is the largest number of dimensions present and the mean for each
 * dimension is calculated over all values defined for that dimension.
 * 
 * @author Daniel McEnnis 
 */
public class DensityBasedAverage extends Aggregator {
	
	int feature;
	
	public DensityBasedAverage(){
		metadata = new AggregatorDefinition("Density Based Average","This is the overall average over all windows.",true,null);
	}

	/**
	 * Provide a list of features that are to be aggregated by this feature.
	 * Returning null indicates that this aggregator accepts only one feature
	 * and every feature avaiable should be used.
	 * 
	 * @return list of features to be used by this aggregator or null
	 */
	public String[] getFeaturesToApply() {
		return null;
	}

	/**
	 * 
	 * @see jAudioFeatureExtractor.Aggregators.Aggregator#getFeatureDefinition()
	 */
	public FeatureDefinition getFeatureDefinition() {
		return definition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jAudioFeatureExtractor.Aggregators.Aggregator#init(jAudioFeatureExtractor.AudioFeatures.FeatureExtractor[])
	 */
	public void init(int[] featureIndeci) throws Exception {
		feature = featureIndeci[0];
	}
	
	

	@Override
	public Object clone() {
		return new DensityBasedAverage();
	}

	@Override
	public void setSource(FeatureExtractor feature) {
		FeatureDefinition this_def = feature.getFeatureDefinition();
		definition = new FeatureDefinition(this_def.name + " Density Based Average",
				this_def.description + System.getProperty("line.separator")
						+ "This is the overall average over all windows.",
				this_def.is_sequential, this_def.dimensions);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jAudioFeatureExtractor.Aggregators.Aggregator#aggregate(double[][][])
	 */
	/**
	 * Aggregates the values of the features specified by the init function accross all windows of the data recieved.
	 * @param double[][][] values where data are [window][feature][values]
	 */
	public void aggregate(double[][][] values) {
		if ((values == null) || (values.length == 0)) {
			result = new double[1];
			result[0] = Double.NaN;
			definition.dimensions = 1;
		} else {
			// find the max number of dimensions (number of values)
			int max = -1;
			for (int i = 0; i < values.length; ++i) {
				if ((values[i][feature] != null) && (values[i][feature].length > max)) {
					max = values[i][feature].length;
				}
			}
			if (max <= 0) {
				result = new double[] { 0.0 };
				definition.dimensions = 1;
			} else {
							
				// now calculate density based average over all the dimensions
				result = new double[max];
				definition.dimensions = max;
				double[] densities = new double[values.length];
				double[] weights = new double[values.length];
				// iterate over all dimensions (number of values)
				for (int i = 0; i < max; ++i) {
					int count = 0;
					double densityAddition = 0.0;
					// iterate over all windows
					for (int j = 0; j < values.length; j++) {
						if ((values[j][feature] != null) && (values[j][feature].length > i)) {
							double distance = 0.0;
							// calculate the distance/dissimilarity between current value i from window j 
							// and the rest of values i from windows k
							for (int k = 0; k < values.length; k++) {
								if ((values[k][feature] != null) && (values[k][feature].length > i)) {
									distance += Math.pow(values[j][feature][i] - values[k][feature][i], 2);
									count++;
								}
							}
							// calculate density of current value
							densities[j] = 1 / (1 + (distance / count));
						}
					}
					// calculate densityAddition
					for (int j = 0; j < densities.length; j++) {
						densityAddition += densities[j];						
					}
					// calculate the weights
					for (int j = 0; j < weights.length; j++) {
						weights[j] = densities[j] / densityAddition;				
					}
					// calculate density based average
					for (int j = 0; j < weights.length; j++) {
						if (values[j][feature] != null){
							result[i] += (weights[j] * values[j][feature][i]);
						}
					}

				}
			
			
			
			}	
		}
	}
	
	
	private void calculateDensity(double[][][]values, int max){
		result = new double[max];
		definition.dimensions = max;
		double[] densities = new double[max];
		double[] weights = new double[max];
		for (int i = 0; i < max; ++i) {
			int count = 0;
			double sum = 0.0;
			double densityAddition = 0.0;
			
			for (int j = 0; j < values.length; ++j) {
				if ((values[j][feature] != null) && (values[j][feature].length > i)) {
					double distance = 0.0;
					for (int k = 0; k < values.length; k++) {
						// calculate the distance/dissimilarity between current value and the rest of values
						distance += Math.pow(values[j][feature][i] - values[k][feature][i], 2);
					}
					// calculate density of current value
					densities[j] = 1 / (1 + (distance / max));
					count++;
				}
			}
			// calculate densityAddition
			for (int j = 0; j < densities.length; j++) {
				densityAddition += densities[j];						
			}
			// calculate the weights
			for (int j = 0; j < weights.length; j++) {
				weights[j] = densities[j] / densityAddition;				
			}
			// calculate density based average
			for (int j = 0; j < weights.length; j++) {
				result[i] += (weights[j] * values[j][feature][i]);				
			}

		}
	}
	
}