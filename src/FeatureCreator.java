import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 
 * @author Shawn Hanna This class holds functions for getting creating features,
 *         given an array of values
 * 
 * 
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
				if (assignments[i] != bestIndex) {
					doStop = false;
					assignments[i] = bestIndex;
				}
			}

			for (int j = 0; j < centers.length; j++) {
				double newCenter = 0;
				int numInCluster = 0;
				for (int i = 0; i < data.length; i++) {
					if (assignments[i] == j) {
						newCenter += data[i];
						numInCluster++;
					}
				}
				newCenter = newCenter / numInCluster;
				centers[j] = newCenter;
			}
			if (numIterations % 100 == 0)
				System.out
						.println("Clustering - currently at iteration number: "
								+ numIterations);
		}
		// System.out.println("Found best clusters. took " + numIterations);
		return centers;
	}

	int[] stringArrayToIntArray(String[] in) {
		int[] out = new int[in.length];
		for (int i = 0; i < in.length; i++) {
			out[i] = Integer.parseInt(in[i]);
		}
		return out;
	}

	public static void main(String[] args) throws FileNotFoundException {
		double[] test = new double[100000];

		// for (int i = 0; i < test.length; i++) {
		// test[i] = (Math.random() * 50);
		// System.out.println("D[" + i + "] = " + test[i]);
		// }

		test = arrayFromFile("batting.txt");

		double[] clusters = getClusters(test, 4);
		Arrays.sort(clusters);
		for (double center : clusters)
			System.out.println("Center: " + center);

		System.out.println(classFromClusters(clusters, 0.3));
	}

	public static double[] arrayFromFile(String file)
			throws FileNotFoundException {
		Scanner s = new Scanner(new File(file));
		double[] out = new double[countLines("batting.txt")];
		int i = 0;
		while (s.hasNextDouble()) {
			double d = s.nextDouble();
			out[i++] = d;
		}
		return out;
	}

	// function to count the number of lines
	public static int countLines(String filename) {
		try {
			LineNumberReader reader = new LineNumberReader(new FileReader(
					filename));
			int cnt = 0;

			while ((reader.readLine()) != null) {
			}

			cnt = reader.getLineNumber();
			reader.close();
			return cnt;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	static int classFromClusters(double[] clusters, double value) {
		double minDistance = Double.MAX_VALUE;
		int bestIndex = -1;
		for (int j = 0; j < clusters.length; j++) {
			double theD = Math.abs(value - clusters[j]);
			if (theD < minDistance) {
				bestIndex = j;
				minDistance = theD;
			}
		}

		return bestIndex;
	}

	static double getTopPercentage(double[] data, double percent) {
		Arrays.sort(data);
		return data[(int) (data.length * (100.0 - percent) / 100.0)];
	}

	static ArrayList<BattingPlayer> getPlayersEra(
			ArrayList<BattingPlayer> players, int startYear, int endYear) {
		ArrayList<BattingPlayer> retPlayers = new ArrayList<BattingPlayer>(
				players.size() / 5);

		for (int i = 0; i < players.size(); i++) {
			BattingPlayer curP = players.get(i);
			if (curP.getAverageYear() >= startYear
					&& curP.getAverageYear() <= endYear) {
				retPlayers.add(curP);
			}
		}

		return retPlayers;
	}

	static double[] getBattingAverages(ArrayList<BattingPlayer> players) {
		double[] averages = new double[players.size()];
		for (int i = 0; i < players.size(); i++) {
			averages[i] = ((double) players.get(i).H) / players.get(i).AB;
		}
		return averages;
	}

	static double[] getBattingAverageClusters(ArrayList<BattingPlayer> players,
			int numClusters) {
		double[] averages = new double[players.size()];
		for (int i = 0; i < players.size(); i++) {
			averages[i] = ((double) players.get(i).H) / players.get(i).AB;
		}
		double[] clusters = FeatureCreator.getClusters(averages, numClusters);
		Arrays.sort(clusters);
		return clusters;
	}
}
