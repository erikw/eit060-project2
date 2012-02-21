package client;

public class CreateCommand implements Command {
	private String patientID;

	public CreateCommand(String patientID) {
		this.patientID = patientID;
	}

	public String protocolString() {
		StringBuilder proto = new StringBuilder();
		proto.append("create").append(" ");
		proto.append(patientID);
		return proto.toString();
	}
}

