public class DeleteCommand implements Command { 
	private int recordID;

	public DeleteCommand (int recordID) {
		this.recordID = recordID;
	}
}
