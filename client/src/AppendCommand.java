public class AppendCommand implements Command { 
	private String recordID;
	private String appendText;

	public AppendCommand(String recordID, String appendText) {
		this.recordID = recordID;
		this.appendText = appendText;
	}

	public String protocolString() {
		StringBuilder proto = new StringBuilder();
		proto.append("append").append(" ");
		proto.append(recordID).append(" ");
		proto.append(appendText);
		return proto.toString();
	}
}
