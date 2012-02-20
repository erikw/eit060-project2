public class ReadCommand implements Command { 
	private int recordID;

	public ReadCommand(int recordID) {
		this.recordID = recordID;
	}
}
