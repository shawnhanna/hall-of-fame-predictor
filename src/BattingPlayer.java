import java.util.ArrayList;

public class BattingPlayer implements Comparable {
	public String playerID = null;
	ArrayList<BattingYear> years = new ArrayList<BattingYear>(17);

	// Career Stats
	public int yearID;
	public String teamID;
	public String leagueID;
	public int games;
	public int gamesBatting;
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

	public void addYear(BattingYear by) {
		if (playerID == null)
			playerID = by.playerID;
		years.add(by);
	}

	public void generateCareer() {
		for (BattingYear year : years) {
			games += year.games;
			gamesBatting += year.gamesBatting;
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
	}

	public double getAverageBattingAverage() {
		double average = 0;
		for (BattingYear year : years) {
			average += year.average;
		}
		return average / years.size();
	}

	public double getAverageYear() {
		return yearID / years.size();
	}

	public double getAverageGames() {
		return games / years.size();
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
}
