package server;

import java.util.Map;

public class CreateCommand extends Command { 
	private String patientID;
	private String nurseID;

	public CreateCommand(String pid, String nid) {
		this.patientID = pid;
		this.nurseID = nid;
	}

	public String execute(Map<Integer, Record> records) {
		boolean ok = false;
		if (super.subject.charAt(0) == 'D') {
			int rID = Record.getNextRecordID();
			Record rec = new Record(super.departmentID, this.patientID, this.nurseID, super.subject, "");
			records.put(rID, rec);
			log.info(String.format(String.format("%s@%s created record %s", super.subject, super.departmentID, rec.recordID)));
			ok = true;
		} else {
			log.info(String.format("Access denied for %s when creating record", super.subject));
		}
		return (ok) ? "Record created" : "You shall not pass!";
	}
}
