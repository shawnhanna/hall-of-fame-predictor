public class BattingYear implements Comparable {
	public static int ABthreshold = 100;
	
	public String playerID;
	public int yearID;
	public int stint;
	public String teamID;
	public String leagueID;
	public int G;
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
	public String inputLine;

	public boolean valid = true;

	// created stats
	public double average = -1;

	public BattingYear(String inputLine) {
		this.inputLine = inputLine;
		String[] ins = inputLine.split(",");
		playerID = ins[0];
		yearID = Integer.parseInt(ins[1]);
		stint = Integer.parseInt(ins[2]);
		teamID = ins[3];
		leagueID = ins[4];
		G = Integer.parseInt(ins[5]);
		G_batting = Integer.parseInt(ins[6]);
		AB = Integer.parseInt(ins[7]);
		R = Integer.parseInt(ins[8]);
		H = Integer.parseInt(ins[9]);
		_2B = Integer.parseInt(ins[10]);
		_3B = Integer.parseInt(ins[11]);
		HR = Integer.parseInt(ins[12]);
		RBI = Integer.parseInt(ins[13]);
		SB = Integer.parseInt(ins[14]);
		CS = Integer.parseInt(ins[15]);
		BB = Integer.parseInt(ins[16]);
		SO = Integer.parseInt(ins[17]);
		IBB = Integer.parseInt(ins[18]);
		HBP = Integer.parseInt(ins[19]);
		SH = Integer.parseInt(ins[20]);
		SF = Integer.parseInt(ins[21]);
		GIDP = Integer.parseInt(ins[22]);
		G_old = Integer.parseInt(ins[23]);

		valid = isValid();

		if (valid)
			average = H / AB;
	}

	public boolean isValid() {
		if (AB < 30)
			return false;

		if (G < 20)
			return false;

		if (G_batting < 20)
			return false;

		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != getClass())
			return false;

		BattingYear rhs = (BattingYear) obj;
		if (rhs.yearID == this.yearID && rhs.playerID == this.playerID) {
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(Object obj) {
		BattingYear rhs = (BattingYear) obj;
		return rhs.playerID.compareTo(this.playerID);
	}

	@Override
	public String toString() {
		return playerID+" "+yearID;
	}
}
