package server;

public class Record {
	public static int nextRecordID = 0;
	public int recordID;
	public String recordText;

	public String departmentID;
	public String patientID;
	public String nurseID;
	public String doctorID;

	public Record(String departmentID, String patientID, String nurseID, String doctorID, String recordText) {
		this.recordID     = nextRecordID++;
		this.departmentID = departmentID;
		this.patientID    = patientID;
		this.nurseID      = nurseID;
		this.doctorID     = doctorID;
		this.recordText   = recordText;
	}

	public static int getNextRecordID() {
		return nextRecordID;
	}
}
