import java.util.Map;

public class DeleteCommand extends Command { 
	private int recordID;

	public DeleteCommand (int recordID) {
		this.recordID = recordID;
	}

	public String execute(int userType, Map<String, Record> records) {
		return null;
	}
}
