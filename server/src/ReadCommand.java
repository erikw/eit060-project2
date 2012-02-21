import java.util.Map;

public class ReadCommand implements Command { 
	private int recordID;

	public ReadCommand(int recordID) {
		this.recordID = recordID;
	}

	public String execute(int userType, Map<String, Record> records) {
		return null;
	}
}
