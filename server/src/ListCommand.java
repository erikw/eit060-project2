public class ListCommand implements Command { 
	public static final int NO_REF = -1;
	private int ref;

	public ListCommand () {
		this(ListCommand.NO_REF);
	}

	public ListCommand(int ref) {
		this.ref = ref;
	}
}
