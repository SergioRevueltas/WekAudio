/**
 * Density Based Average
 * @author Sergio Revueltas 
 * 
 * Based on P.Angelov and R.Yager paper 
 * http://dx.doi.org/10.1016/j.ins.2012.08.006
 */
package jAudioFeatureExtractor.Aggregators;

import jAudioFeatureExtractor.ACE.DataTypes.AggregatorDefinition;
import jAudioFeatureExtractor.ACE.DataTypes.FeatureDefinition;
import jAudioFeatureExtractor.AudioFeatures.FeatureExtractor;

/**
 * Edited by Sergio Revueltas
 * 
 * Calculates the density based average of a feature accross all windows where it is defined. When the feature has more
 * than one dimension, the mean has an equal number of dimensions and the value of each dimension is the mean of that
 * dimension. If the feature has a variable number of dimensions, the dimensionality of the result is the largest number
 * of dimensions present and the mean for each dimension is calculated over all values defined for that dimension.
 * 
 * @author Daniel McEnnis
 */
public class DensityBasedAverage extends Aggregator {

	int feature;

	public DensityBasedAverage() {
		metadata =
				new AggregatorDefinition(
						"Density Based Average",
						"This is the overall density based average over all windows. "
						+ "Test Test Test Test Test Test Test Test Test Test Test Test Test ",
						true, null);
	}

	/**
	 * Provide a list of features that are to be aggregated by this feature. Returning null indicates that this
	 * aggregator accepts only one feature and every feature avaiable should be used.
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
	 * 
	 * @param double[][][] values where data are [window][feature][values]
	 * @author Sergio Revueltas
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
				if (!isTestRunning()) {
					definition.dimensions = max;
				}
				double[] densities = new double[values.length];
				double[] weights = new double[values.length];
				// iterate over all dimensions (number of values)
				for (int i = 0; i < max; ++i) {
					// int count = 0;
					double densityAddition = 0.0;
					// iterate over all windows
					for (int j = 0; j < values.length; j++) {
						if ((values[j][feature] != null) && (values[j][feature].length > i)) {
							double distance = 0.0;
							// calculate the distance/dissimilarity between current i value from j window and
							// the rest of i values from k windows
							for (int k = 0; k < values.length; k++) {
								if ((values[k][feature] != null) && (values[k][feature].length > i) && j != k) {
									distance += Math.pow(values[j][feature][i] - values[k][feature][i], 2);
									// count++;
								}
							}
							// calculate density of current value
							densities[j] = 1 / (1 + (distance / densities.length));
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
					// calculate DBA (density based average)
					for (int j = 0; j < weights.length; j++) {
						if (values[j][feature] != null) {
							result[i] += (weights[j] * values[j][feature][i]);
						}
					}
					// to fix bug related to double precision
					// 52 bit mantissa, so will be able to represent a 32bit integer without lost of data.
					// result[i] = Math.round(result[i]);
				}

			}
		}
	}

}
