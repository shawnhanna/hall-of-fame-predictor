import java.util.ArrayList;

/**
 * 
 * @author Shawn Hanna This class holds functions for getting creating features,
 *         given an array of values
 * 
 */

public class FeatureCreator {

	int[] getClusters(int[] data, int numClusters) {
		// /initialize the centers of the clusters
		// 1) pick a random center from all the points
		int[] centers = new int[numClusters];
		for (int i = 0; i < numClusters; i++) {
			centers[i] = data[(int) (Math.random() * ((data.length) + 1))];
		}

		double[] sums = new double[numClusters];
		int[] numInThatCluster = new int[numClusters];

		boolean doStop = false;
		int numIterations = 0;
		while (!doStop) {
			numIterations++;
			doStop = true;
			for (int i = 0; i < data.length; i++) {
				double minDistance = Double.MAX_VALUE;
				int bestIndex = -1;
				for (int j = 0; j < numClusters; j++) {
					double theD = Math.abs(data[i] - centers[j]);
					if (theD < minDistance) {
						bestIndex = j;
						minDistance = theD;
						
					}
				}
				sums[bestIndex] += data[bestIndex];
				numInThatCluster[bestIndex] += 1;
			}

			for (int i = 0; i < centers.length; i++) {
				int newCenter = (int) (sums[i] / numInThatCluster[i]);
				if (newCenter != centers[i]) {
					centers[i] = newCenter;
					doStop = false;
				}
			}
		}
		System.out.println("Found best clusters. took " +numIterations);
		return centers;
	}

	int[] stringArrayToIntArray(String[] in) {
		int[] out = new int[in.length];
		for (int i = 0; i < in.length; i++) {
			out[i] = Integer.parseInt(in[i]);
		}
		return out;
	}
}
