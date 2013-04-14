import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class HOFPredictor {
	private static String[] colHeaders;

	static ArrayList<BattingPlayer> players = new ArrayList<BattingPlayer>(1000);

	public HOFPredictor() {
		BattingPlayer.ABthreshold = 2000;
		BattingYear.ABthreshold = 100;

		players = null;
		try {
			players = getCareers("batting.csv", "nominations.csv");
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(1);
		}

		System.out.println("Starting # of players; " + players.size());
		removeIllegal(players);
		System.out.println("New # of players; " + players.size());

		double[] averages = FeatureCreator.getBattingAverages(players);
		double[] centers = FeatureCreator.getClusters(averages, 5);

		Arrays.sort(centers);
		for (double center : centers)
			System.out.println("BA cluster: " + center);

		double top10Perc = FeatureCreator.getTopPercentage(averages, 10);
		System.out.println("Top 10% of batters: " + top10Perc);

		String[] importantFields = { "AB", "H", "G", "HR", "battingAverage",
				"top10perc", "2B", "3B", "RBI", "SB", "BB", "SO" };

		try {
			createARFFfile("outfile.arff", players, importantFields);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<BattingYear> readYears(String filename)
			throws IOException {
		ArrayList<BattingYear> years = null;
		File datafile = new File(filename);
		if (!datafile.exists()) {
			System.out
					.println("Could not find data file, please check your directory.");
			System.exit(1);
		}

		BufferedReader br = new BufferedReader(new FileReader(filename));
		String dataLine = br.readLine();// get first line in file
		String[] get = dataLine.split(",");
		colHeaders = new String[get.length];
		for (int a = 0; a < get.length; a++) {
			colHeaders[a] = get[a];
		}
		// colHeaders[get.length] = "Seasons Played";

		int totalLines = FeatureCreator.countLines(filename);
		years = new ArrayList<BattingYear>(totalLines);
		for (int y = 0; y < totalLines - 1; y++) {
			String nextLine = br.readLine();
			years.add(new BattingYear(nextLine));
		}

		br.close();// done with file

		return years;
	}

	public static ArrayList<Nomination> readNominations(String filename)
			throws IOException {
		ArrayList<Nomination> noms = null;
		File datafile = new File(filename);
		if (!datafile.exists()) {
			System.out
					.println("Could not find data file, please check your directory.");
		}

		BufferedReader br = new BufferedReader(new FileReader(filename));
		br.readLine();// get first line

		int totalLines = FeatureCreator.countLines(filename);
		noms = new ArrayList<Nomination>(totalLines);
		for (int y = 0; y < totalLines - 1; y++) {
			String nextLine = br.readLine();
			noms.add(new Nomination(nextLine));
		}

		br.close();// done with file

		return noms;
	}

	public static void removeIllegal(ArrayList<BattingPlayer> players) {
		Iterator<BattingPlayer> it = players.iterator();
		while (it.hasNext()) {
			BattingPlayer player = it.next();
			if (!player.isValid()) {
				it.remove();
			}
		}
	}

	public static ArrayList<BattingPlayer> getCareers(String batting,
			String nominations) throws IOException {
		ArrayList<BattingYear> years = readYears(batting);
		ArrayList<Nomination> noms = readNominations(nominations);
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
				// System.out.println("Added new Player: " + newPlayer);
				players.add(newPlayer);
				currPlayer = years.get(i).playerID;
			}
		}
		for (BattingPlayer bp : players) {
			bp.generateCareer();
		}
		for (int i = 0; i < noms.size(); i++) {
			for (int j = 0; j < players.size(); j++) {
				if (noms.get(i).playerID.equals(players.get(j).playerID)) {
					players.get(j).setNomination(noms.get(i));
				}
			}
		}
		return players;
	}

	public static void main(String[] args) {
		new HOFPredictor();
	}

	static void createARFFfile(String filename,
			ArrayList<BattingPlayer> players, String[] attributes)
			throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File(filename));
		pw.println("@RELATION HOF\n");

		// create attributes. Edit if not numeric
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i].equals("top10perc"))
				pw.println("@ATTRIBUTE " + attributes[i] + " {true,false}");
			else
				pw.println("@ATTRIBUTE " + attributes[i] + " NUMERIC");
		}

		pw.println("@ATTRIBUTE class {true,false}\n");
		pw.println("@DATA");

		for (int i = 0; i < players.size(); i++) {
			pw.println(players.get(i).outString(attributes));
		}
		pw.close();
	}

	public static boolean isTop10Percent(double average) {
		if (average >= FeatureCreator.getTopPercentage(
				FeatureCreator.getBattingAverages(players), 10)) {
			return true;
		}
		return false;
	}
}
