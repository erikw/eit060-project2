public class ListCommand implements Command { 
	public static final int NO_ID = -1;
	private int patientID;

	public ListCommand () {
		this(ListCommand.NO_ID);
	}

	public ListCommand(int patientID) {
		this.patientID = patientID;
	}
}