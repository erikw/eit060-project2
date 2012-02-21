import java.util.Map;

public class ListCommand extends Command { 
	public static final int NO_ID = -1;
	private int patientID;

	public ListCommand () {
		this(ListCommand.NO_ID);
	}

	public ListCommand(int patientID) {
		this.patientID = patientID;
	}

	public String execute(int userType, Map<String, Record> records) {
		return null;
	}
}
