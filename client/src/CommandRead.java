public class CommandRead implements Command { 
	public static final String NO_ID = "-1";
	private String recordID;

	public CommandRead(String recordID) {
		this.recordID = recordID;
	}

	public String protocolString() {
		StringBuilder proto = new StringBuilder("read");
		if (recordID != NO_ID) {
			proto.append(" ").append(recordID);
		} 
		return proto.toString();
	}
}
