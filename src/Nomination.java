public class Nomination {
	String playerID;
	boolean nominated;

	public Nomination(String nextLine) {
		String[] split = nextLine.split(",");
		playerID = split[0];
		if (split[1].equals("Y"))
			this.nominated = true;
		else
			this.nominated = false;
		System.out.println("Added nom: " + playerID + " nom=" + nominated);
	}
}
