import java.util.Map;

public class CreateCommand extends Command { 
	// cannot have record here. would change global stuf w/o calling execute.
	private String patientID;
	private String nurseID;
	private String departMentID;
	private String text;

	public CreateCommand(String pid, String nid, String text) {
		this.patientID = pid;
		this.nurseID = nid;
		this.text = text;
	}

	public String execute(Map<String, Record> records, Map<String, String> units) {
		records.put(Record.getNextRecordID(), new Record(super.departmentID, this.patientID, this.nurseID, super.subject, this.text));
		
	}
}
