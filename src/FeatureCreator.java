import java.util.ArrayList;

/**
 * 
 * @author Shawn Hanna This class holds functions for getting creating features,
 *         given an array of values
 * 
 */

public class FeatureCreator {

	static double[] getClusters(double[] data, int numClusters) {
		// /initialize the centers of the clusters
		// 1) pick a random center from all the points
		double[] centers = new double[numClusters];
		int[] assignments = new int[data.length];

		for (int i = 0; i < numClusters; i++) {
			centers[i] = data[(int) (Math.random() * ((data.length)))];
			boolean stop = false;
			while (!stop) {
				stop = true;
				for (int j = i - 1; j >= 0; j--) {
					if (centers[j] == centers[i]) {
						centers[i] = data[(int) (Math.random() * ((data.length)))];
						stop = false;
						break;
					}
				}
			}
		}

		for (double num : centers)
			System.out.println("Center: " + num);

		double[] sums = new double[numClusters];
		double[] numInThatCluster = new double[numClusters];

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
				sums[bestIndex] += data[i];
				numInThatCluster[bestIndex] += 1;
			}

			for (int i = 0; i < centers.length; i++) {
				int newCenter = (int) (sums[i] / numInThatCluster[i]);
				if (newCenter != centers[i]) {
					centers[i] = newCenter;
					doStop = false;
				}
			}
			System.out.println("Iteration number: " + numIterations);
		}
		System.out.println("Found best clusters. took " + numIterations);
		return centers;
	}

	int[] stringArrayToIntArray(String[] in) {
		int[] out = new int[in.length];
		for (int i = 0; i < in.length; i++) {
			out[i] = Integer.parseInt(in[i]);
		}
		return out;
	}

	public static void main(String[] args) {
		double[] test = new double[40];

		test[0] = 0;
		test[1] = 0;
		test[2] = 3;
		test[3] = 3;
		test[4] = 3;
		test[5] = 4;
		test[6] = 12;
		test[7] = 12;
		test[8] = 8;
		test[9] = 8;
		test[10] = 33;
		test[11] = 34;
		test[12] = 35;
		test[13] = 36;

		for (int i = 0; i < test.length; i++) {
			test[i] = (Math.random() * 40);
			System.out.println("D[" + i + "] = " + test[i]);
		}

		double[] clusters = getClusters(test, 3);
		for (double center : clusters)
			System.out.println("Center: " + center);
	}
}
