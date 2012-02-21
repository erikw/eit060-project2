import java.util.Map;

public class ReadCommand extends Command { 
	private int recordID;

	public ReadCommand(int recordID) {
		this.recordID = recordID;
	}

	public String execute(Map<String, Record> records) {
		Record record =  records.get(recordID);
		if (record == null) {
			return "Record [" + recordID + "] not found.";
		} 
		boolean canDo = false;		
		String subject = cert.getSubjectDN().getName();
		switch (subject.charAt(0)) {
			case 'a':
					 canDo = true;
					 break;
			case 'd':
					 if (subject.equals(record.doctorID) || record.departmentID.equals(departmentID)) {
						canDo = true;
					 }
					 break;
			case 'n':
					 if (subject.equals(record.nurseID) || record.departmentID.equals(departmentID)) {
						canDo = true;
					 }
					 break;
			default: // patient
					 if (record.patientID.equals(subject)) {
						 canDo = true;
					 }
		}
		if (canDo) {
			return record.recordText;
		} else {
			return "You shall not pass!";
		}
	}
}
