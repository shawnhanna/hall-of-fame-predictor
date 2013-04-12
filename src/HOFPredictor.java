import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class HOFPredictor {
	private static String[] colHeaders;
	ArrayList<BattingPlayer> players = new ArrayList<BattingPlayer>(1000);

	public HOFPredictor() {
		ArrayList<BattingPlayer> players = getCareers("batting.csv");

		// for (BattingPlayer bp : players)
		// System.out.println(bp);
		System.out.println("Starting # of players; " + players.size());
		removeIllegal(players);
		System.out.println("New # of players; " + players.size());

		double[] centers = getBattingAverageClusters(players, 5);

		for (double center : centers)
			System.out.println("BA cluster: " + center);

	}

	public static ArrayList<BattingYear> readFile(String filename) {
		ArrayList<BattingYear> years = null;
		File datafile = new File(filename);
		if (!datafile.exists()) {
			System.out
					.println("Could not find data file, please check your directory.");
			System.exit(1);
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String dataLine = br.readLine();// get first line in file
			String[] get = dataLine.split(",");
			colHeaders = new String[get.length + 1];
			for (int a = 0; a < get.length; a++) {
				colHeaders[a] = get[a];
			}
			colHeaders[get.length] = "Seasons Played";

			int totalLines = FeatureCreator.countLines(filename);
			years = new ArrayList<BattingYear>(totalLines);
			for (int y = 0; y < totalLines - 1; y++) {
				String nextLine = br.readLine();
				years.add(new BattingYear(nextLine));
				System.out.println("Added new year: "
						+ years.get(years.size() - 1));
			}

			br.close();// done with file
		} catch (Exception e) {
			e.printStackTrace();
		}

		return years;
	}

	public static void removeIllegal(ArrayList<BattingPlayer> players) {
		Iterator<BattingPlayer> it = players.iterator();
		while (it.hasNext()) {
			BattingPlayer player = it.next();
			if (player.numSeasons() < 10 || player.AB < 150) {
				it.remove();
			}
		}
	}

	public static ArrayList<BattingPlayer> getCareers(String string) {
		ArrayList<BattingYear> years = readFile("Batting.csv");
		ArrayList<BattingPlayer> players = new ArrayList<BattingPlayer>(
				years.size() / 10);
		Arrays.sort(years.toArray());

		String currPlayer = "-1";
		for (int i = 0; i < years.size(); i++) {
			if (years.get(i).playerID.compareTo(currPlayer) == 0) {
				players.get(players.size() - 1).addYear(years.get(i));
			} else {
				BattingPlayer newPlayer = new BattingPlayer();
				newPlayer.addYear(years.get(i));
				System.out.println("Added new Player: " + newPlayer);
				players.add(newPlayer);
				currPlayer = years.get(i).playerID;
			}
		}
		for (BattingPlayer bp : players) {
			bp.generateCareer();
		}
		return players;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new HOFPredictor();
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
