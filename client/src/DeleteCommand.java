public class DeleteCommand implements Command {
	private String recordID;

	public DeleteCommand(String recordID) {
		this.recordID = recordID;
	}

	public String protocolString() {
		StringBuilder proto = new StringBuilder();
		proto.append("delete").append(" ");
		proto.append(recordID);
		return proto.toString();
	}
}


