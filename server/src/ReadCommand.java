import java.util.Map;

public class ReadCommand extends Command { 
	private int recordID;

	public ReadCommand(int recordID) {
		this.recordID = recordID;
	}

	public String execute(Map<String, Record> records) {
		Record record =  map.get(recordID);
		if (id == null) {
			return "Record [" + recordID + "] not found.";
		} 
		boolean canDo = false;		
		String subject = cert.getSubjectDN();
		switch (subject.charAt(0P)) {
			case 'a':
					 canDo = true;
					 break;
			case 'd':
					 String doctorDivision = units.get(subject);
					 if (subject.equals(record.doctorID) || doctorDivision.equals(record.departmentID)) {
						canDo = true;
					 }
					 break;
			case 'n':
					 canDo = true;
					 break;
			defalt: 

		}

	}
}
