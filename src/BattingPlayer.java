import java.sql.Time;
import java.util.ArrayList;

public class BattingPlayer implements Comparable {
	static double ABthreshold = 2000;

	public String playerID = null;
	ArrayList<BattingYear> years = new ArrayList<BattingYear>(17);

	public Nomination nomination;

	// Career Stats
	public int yearID;
	public String teamID;
	public String leagueID;
	// games
	public int G;
	// games batting
	public int G_batting;
	public int AB;
	public int R;
	public int H;
	public int _2B;
	public int _3B;
	public int HR;
	public int RBI;
	public int SB;
	public int CS;
	public int BB;
	public int SO;
	public int IBB;
	public int HBP;
	public int SH;
	public int SF;
	public int GIDP;
	public int G_old;
	public double AvgVSEra;

	// User created stat/features
	public boolean isValid = true;

	public void addYear(BattingYear by) {
		if (playerID == null)
			playerID = by.playerID;
		years.add(by);
	}

	public void generateCareer() {
		for (BattingYear year : years) {
			yearID += year.yearID;
			G += year.G;
			G_batting += year.G_batting;
			AB += year.AB;
			R += year.R;
			H += year.H;
			_2B += year._2B;
			_3B += year._3B;
			HR += year.HR;
			RBI += year.RBI;
			SB += year.SB;
			CS += year.CS;
			BB += year.BB;
			SO += year.SO;
			IBB += year.IBB;
			HBP += year.HBP;
			SH += year.SH;
			SF += year.SF;
			GIDP += year.GIDP;
			G_old += year.G_old;
		}
		isValid = isValid();
	}

	public boolean isValid() {
		/*
		 * if (G < 100) return false; if (G_batting < 100) return false;
		 */
		if (AB < ABthreshold)
			return false;

		if (numSeasons() < 10)
			return false;
		if ((2013 - getLastYear()) < 5)
			return false;

		// if(((float)H/(float)AB) < .250) return false;

		// if(_2B < 116) return false;

		// if(_3B < 16) return false;

		// if(HR < 16) return false;

		// if(RBI < 250) return false;

		return true;
	}

	private int getLastYear() {
		int max = -1;
		for (int i = 0; i < years.size(); i++) {
			if (years.get(i).yearID > max)
				max = years.get(i).yearID;
		}
		return max;
	}

	public void setNomination(Nomination n) {
		nomination = n;
	}

	public boolean isNominated() {
		return nomination.nominated;
	}

	public double getAverageBattingAverage() {
		return ((double) H) / (AB);

	}

	public double getAverageYear() {
		// System.out.println(yearID);
		// System.out.println(years.size());
		return (double) yearID / years.size();

	}

	public double getAverageGames() {
		return G / years.size();
	}

	public double getAverageHR() {
		return HR / years.size();
	}

	@Override
	public int compareTo(Object obj) {
		BattingYear rhs = (BattingYear) obj;
		return rhs.playerID.compareTo(this.playerID);
	}

	@Override
	public String toString() {
		return playerID;
	}

	public int numSeasons() {
		return years.size();
	}

	public String outString(String[] attributes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i].equalsIgnoreCase("AB"))
				sb.append(AB);
			else if (attributes[i].equalsIgnoreCase("H"))
				sb.append(H);
			else if (attributes[i].equalsIgnoreCase("R"))
				sb.append(R);
			else if (attributes[i].equalsIgnoreCase("G"))
				sb.append(G);
			else if (attributes[i].equalsIgnoreCase("G_batting"))
				sb.append(G_batting);
			else if (attributes[i].equalsIgnoreCase("RBI"))
				sb.append(RBI);
			else if (attributes[i].equalsIgnoreCase("HR"))
				sb.append(HR);
			else if (attributes[i].equalsIgnoreCase("2B"))
				sb.append(_2B);
			else if (attributes[i].equalsIgnoreCase("3B"))
				sb.append(_3B);
			else if (attributes[i].equalsIgnoreCase("SB"))
				sb.append(SB);
			else if (attributes[i].equalsIgnoreCase("CS"))
				sb.append(CS);
			else if (attributes[i].equalsIgnoreCase("BB"))
				sb.append(BB);
			else if (attributes[i].equalsIgnoreCase("SO"))
				sb.append(SO);
			else if (attributes[i].equalsIgnoreCase("IBB"))
				sb.append(IBB);
			else if (attributes[i].equalsIgnoreCase("HBP"))
				sb.append(HBP);
			else if (attributes[i].equalsIgnoreCase("SH"))
				sb.append(SH);
			else if (attributes[i].equalsIgnoreCase("SF"))
				sb.append(SF);
			else if (attributes[i].equalsIgnoreCase("GIDP"))
				sb.append(GIDP);
			else if (attributes[i].equalsIgnoreCase("G_old"))
				sb.append(G_old);
			// USER GENERATED FEATURES
			else if (attributes[i].equalsIgnoreCase("battingAverage"))
				sb.append(getAverageBattingAverage());
			else if (attributes[i].equalsIgnoreCase("top10perc"))
				sb.append(HOFPredictor
						.isTop10Percent(getAverageBattingAverage()));
			else if (attributes[i].equalsIgnoreCase("AvgVSEra"))
				sb.append(AvgVSEra);
			else {
				System.out.println("ERROR formatting output: " + attributes[i]);
			}
			sb.append(",");
		}
		sb.append(this.isNominated());
		return sb.toString();
	}

	public void setEraDiff(double eraAvg) {
		AvgVSEra = eraAvg - getAverageBattingAverage();
	}
}
