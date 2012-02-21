import java.util.Map;

public class CreateCommand extends Command { 

	private int patientID;

	public CreateCommand(int patientID) {
		this.patientID = patientID;
	}

	public String execute(int userType, Map<String, Record> records) {
		return null;
	}
}
