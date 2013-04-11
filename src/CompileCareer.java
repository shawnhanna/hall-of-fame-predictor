/*
 * Michael Staudt
 * CS 1573 - Spring 2013
 * Sports Project
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class CompileCareer {
	long numLines;
	String colHeaders[];
	String data[][];
	ArrayList<String[]> newData = new ArrayList<String[]>();
	int numPlayers;

	public CompileCareer() {
		readFile();
		compileCareer();
		outputData();
		System.out.println("Done");
	}

	void readFile() {
		String file = "Batting.csv";

		File datafile = new File(file);
		if (!datafile.exists()) {
			System.out
					.println("Could not find data file, please check your directory.");
			System.exit(1);
		}

		// count the total number of lines that we will be dealing with for for
		// loops
		int totalLines;
		totalLines = countLines(file);

		System.out.println("Total number of lines in Data File: " + totalLines);

		// get the first line of the data file
		// has column information
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String dataLine = br.readLine();// get first line in file
			String[] get = dataLine.split(",");
			// split by comma and add final column for number of seasons a
			// player
			// played in
			// this column will be used to throw out players that do not meet
			// the 10
			// season min
			colHeaders = new String[get.length + 1];
			for (int a = 0; a < get.length; a++) {
				colHeaders[a] = get[a];
			}
			colHeaders[get.length] = "Seasons Played";

			// create matrix (lines-1 x data columns)
			data = new String[totalLines - 1][colHeaders.length + 1];
			// fill the matrix
			String nextLine;
			for (int y = 0; y < totalLines - 1; y++) {
				String lineValues[], line[];
				nextLine = br.readLine();// get line
				line = nextLine.split(",");// split line
				lineValues = new String[line.length + 1];// split line
				for (int a = 0; a < line.length; a++) {
					lineValues[a] = line[a];
				}
				lineValues[line.length] = "0";
				for (int z = 0; z < lineValues.length; z++) {
					data[y][z] = lineValues[z];// add line to data matrix
				}
			}

			br.close();// done with file
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void outputData() {
		try {
			// so, now we have the data in the matrix called "data" and the
			// column
			// headers in the array
			// called "colheaders"

			// newData will now hold the career stats
			// so we can print out the data to a new file (separation = ",")

			// new file will throw out useless data [1]-[4]
			System.out.println("Making Comma Del File");
			FileWriter tf = new FileWriter("BattingCareer.txt");
			BufferedWriter newWrite = new BufferedWriter(tf);
			// write the header line
			for (int c = 0; c < colHeaders.length; c++) {
				if (c == 0 || c == 1) {
					newWrite.write(colHeaders[c] + ",");
				} else if (c > 4 & c < colHeaders.length - 1) {
					newWrite.write(colHeaders[c] + ",");
				} else if (c == colHeaders.length - 1) {
					newWrite.write(colHeaders[c] + "\n");
				}
			}
			for (int d = 0; d < numPlayers; d++) {
				for (int x = 0; x < colHeaders.length; x++) {
					if (x == 0 || x == 1) {
						newWrite.write(newData.get(d)[x] + ",");
					} else if (x > 4 & x < colHeaders.length - 1) {
						newWrite.write(newData.get(d)[x] + ",");
					} else if (x == colHeaders.length - 1) {
						newWrite.write(newData.get(d)[x] + "\n");
					}
				}
			}
			newWrite.write("-\n");
			newWrite.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void compileCareer() {

		// next, go through and find matching playerID's, total up the stats and
		// make a
		// new data matrix
		// Since the actual number of unique players in unknown we need to
		// continue with
		// ArrayList or arrays so the array of players can be dynamic
		String current[];// for current player
		String next[];// array for next read in player
		int numPlayers = 0;// count number of players
		// read in first player from data
		current = data[0];
		int seasons = 1;
		for (int i = 1; i < numLines - 1; i++) {
			/*
			 * for every line check for the player id since we know the ID's are
			 * in alphabetical order we can run through the data from start to
			 * finish when we find an ID match for the current player, add the
			 * stats for the current line to the players career stats when we do
			 * not find a match, we know that it is the next player in the list
			 * and we can add the current player to the ArrayList and move the
			 * next player into the current array
			 */
			next = data[i];
			int value;
			int year;
			int era;
			if (current[0].equals(next[0])) {// ID match
				seasons++;// increase number of seasons played
				for (int a = 1; a < colHeaders.length; a++) {
					if (a == 1) {// year matters, add up the years and average
									// them over
						// the number of seasons so we can put a player in a
						// era
						year = Integer.parseInt(current[a])
								+ Integer.parseInt(next[a]);
						current[a] = Integer.toString(year);// add up years
					} else if (a > 1 & a < 5) {// columns 2, 3, and 4 dont
												// really matter and get thrown
												// out
						current[a] = current[a] + next[a];// update stats
					} else if (a > 4 & a < colHeaders.length) {// after that we
																// need
																// to make sure
																// we
																// are adding
																// the
																// values
						// and not the strings
						value = Integer.parseInt(current[a])
								+ Integer.parseInt(next[a]);
						current[a] = Integer.toString(value);

					}

				}
			} else {// Id does not match
					// System.out.println("Career Stats Calculated for " +
					// current[0]);
					// get the average years
				era = Integer.parseInt(current[1]) / seasons;// get average
																// season
				current[1] = Integer.toString(era);// put into current array
				System.out.print(seasons + " ");
				current[colHeaders.length - 1] = Integer.toString(seasons);
				System.out.println(current[colHeaders.length - 1]);
				// reset seasons
				seasons = 1;
				numPlayers++;// update player count so we know how big the Array
								// List is
				newData.add(current);// add current player to ArrayList
				System.out.print(newData.get(numPlayers - 1)[0] + " - ");
				System.out
						.println(newData.get(numPlayers - 1)[colHeaders.length - 1]);
				current = next;// move next player to current player
			}

		}

		System.out.println("Total Number of Players: " + numPlayers);

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

	public static void main(String[] args) {
		new CompileCareer();
	}
}
