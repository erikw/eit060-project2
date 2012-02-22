package server;

import java.util.*;

public class ListCommand extends Command { 
	public static final String NO_ID = "NoName";
	private String patientID;

	public ListCommand () {
		this(ListCommand.NO_ID);
	}

	public ListCommand(String patientID) {
		this.patientID = patientID;
	}

	public String execute(Map<Integer, Record> records) {

		if (subject.matches("[0-9]{10}")) {
			return getAllRecords(records, subject);
		} else {
			if (patientID.equals("") || !patientID.matches("[0-9]{10}")) {
				log.info("Incorrect patient ID in List");
				return "Incorrect patient ID";
			}
			//TODO add logging messages here?
			log.info(String.format("%s@%s fetched record for patient with ID %s", subject, departmentID, patientID));
			return getAllRecords(records, patientID);
		}

	}


	private String getAllRecords(Map<Integer, Record> records, String getID) {
		StringBuilder sb = new StringBuilder();
		Collection<Record> list = records.values();

		sb.append("Patient with ID " + getID + " has:\n");
		for (Record rec : list) {
			if (rec.patientID.equals(getID)) {
				sb.append(rec.recordID).append("\n");
			}
		}
		return sb.toString();
	}
}
