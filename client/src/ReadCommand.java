public class ReadCommand implements Command {
	private String recordID;

	public ReadCommand(String recordID) {
		this.recordID = recordID;
	}

	public String protocolString() {
		StringBuilder proto = new StringBuilder();
		proto.append("read").append(" ");;
		proto.append(recordID);
		return proto.toString();
	}
}
