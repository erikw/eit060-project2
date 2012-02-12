public class ListCommand implements Command {
	public static final String NO_ID = "-1";
	private String patientID;

	public ListCommand() {
		this.patientID = NO_ID;
	}

	public ListCommand(String patientID) {
		this.patientID = patientID;
	}

	public String protocolString() {
		StringBuilder proto = new StringBuilder();
		proto.append("list");
		if (patientID != NO_ID) {
			proto.append(" ").append(patientID);
		}
		return proto.toString();
	}
}
