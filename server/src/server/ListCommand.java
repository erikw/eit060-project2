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

		if(subject.matches("[0-9]{6}-[0-9]{4}")) {
			if(patientID.equals(NO_ID)) {
				//TODO Add logging messages here?
				return getAllRecords(records, subject);
			} else {
				//TODO Add logging messages here?
				return "No journals available for you.";
			}
		} else {
			if(patientID.equals(NO_ID)) {
				//TODO Add logging messages here?
				return "Add a patientID dumbass.";
			}
			//TODO add logging messages here?
			return getAllRecords(records, patientID);
		}

	}


	private String getAllRecords(Map<Integer, Record> records, String getID) {
		StringBuilder sb = new StringBuilder();
		String search = getID;
		Collection<Record> list = records.values();

		for(Record rec : list) {
			if(rec.patientID.equals(search)) {
				sb.append(rec.recordID).append(": ").append(rec.recordText).append(" ");
				//TODO Add logging messages here?
			}
		}
		return sb.toString();
	}
}
