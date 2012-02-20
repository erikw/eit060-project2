public class CreateCommand implements Command { 
	private int patientID;

	public CreateCommand(int patientID) {
		this.patientID = patientID;
	}
}
