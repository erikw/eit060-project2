import java.util.Map;

public class ReadCommand extends Command { 
	private int recordID;

	public ReadCommand(int recordID) {
		this.recordID = recordID;
	}

	public String execute(Map<Integer, Record> records) {
		Record record =  records.get(recordID);
		if (record == null) {
			log.info("User tried to access record which was not found.");
			return "Record [" + recordID + "] not found.";
		} 
		boolean canDo = false;		
		switch (subject.charAt(0)) {
			case 'a':
					 canDo = true;
					 log.info("Agency reads record " + recordID);
					 break;
			case 'd':
					 if (subject.equals(record.doctorID) || record.departmentID.equals(departmentID)) {
						canDo = true;
						 log.info("Doctor reads record " + recordID);
					 } else {
						 log.severe("Doctor was denied reading record " + recordID);
					 }
					 break;
			case 'n':
					 if (subject.equals(record.nurseID) || record.departmentID.equals(departmentID)) {
						canDo = true;
						 log.info("Nurse reads record " + recordID);
					 } else {
						 log.severe("Nurse was denied reading record " + recordID);
					 }
					 break;
			default: // patient
					 if (record.patientID.equals(subject)) {
						 canDo = true;
						 log.info("Patient reads record " + recordID);
					 } else {
						 log.severe("Patient was denied reading record " + recordID);
					 }
		}
		if (canDo) {
			return record.recordText;
		} else {
			return "You shall not pass!";
		}
	}
}
