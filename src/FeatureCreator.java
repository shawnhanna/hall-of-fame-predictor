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
			centers[i] = (int) (Math.random() * ((data.length) + 1));
		}
		
		ArrayList<ArrayList>clusters = new ArrayList<ArrayList>();
		for (int i=0; i<numClusters; i++) {
			clusters.add(new ArrayList());
		}
		
		boolean doStop = false;
		while (!doStop) {
			doStop = true;
			for (int i = 0; i < data.length; i++) {
				double minDistance = Double.MAX_VALUE;
				for (int i=0; i<numClusters; i++){
					
				}
			}
		}
		return centers;
	}
}
