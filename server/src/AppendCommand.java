import java.util.Map;

public class AppendCommand implements Command { 
	private int recordID;
	private String text;

	public AppendCommand(int recordID, String text) {
		this.recordID = recordID;
		this.text = text;
	}
	
	public String execute(int userType, Map<String, Record> records) {
		return null;
	}

}
