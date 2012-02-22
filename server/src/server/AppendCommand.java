package server;

import java.util.Map;

public class AppendCommand extends Command { 
	private int recordID;
	private String text;

	public AppendCommand(int recordID, String text) {
		this.recordID = recordID;
		this.text = text;
	}
	
	public String execute(Map<Integer, Record> records) {
		Record rec = records.get(this.recordID);
		boolean ok = false;
		if (super.subject.charAt(0) == 'N') {
			if (super.subject.equals(rec.nurseID) || super.departmentID.equals(rec.departmentID)) {
				ok = true;
			}
		} else if (super.subject.charAt(0) == 'D') {
			if (super.subject.equals(rec.doctorID) || super.departmentID.equals(rec.departmentID)) {			
				ok = true;
			}
		}
		if (ok) {
			rec.recordText = rec.recordText + "\n\n================\n\n" + this.text;
			log.info(String.format(String.format("%s@%s appended record %s", super.subject, super.departmentID, rec.recordID)));
		} else {
			log.info(String.format("Access denied for %s on %s", super.subject, rec.recordID));
		}
		return (ok) ? "Record changed" : "You shall not pass!";
	}

}
