package client;

public class CreateCommand implements Command {
	private String patientID;
	private String nurseID;

	public CreateCommand(String patientID, String nurseID) {
		this.patientID = patientID;
		this.nurseID = nurseID;
	}

	public String protocolString() {
		StringBuilder proto = new StringBuilder();
		proto.append("create").append(" ");
		proto.append(patientID).append(" ");
		proto.append(nurseID);
		return proto.toString();
	}
}

