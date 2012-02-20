public class TestCommandFactory {

	public static void main (String[] args) {
		try {
			Command c = CommandFactory.makeCommand("list".getBytes(), JournalServer.USER_PATIENT);
		} catch (UnknownCommandException uce) {
			System.err.println("Caught UCE! on list as patient");
		}

		try {
			Command c = CommandFactory.makeCommand("list 123".getBytes(), JournalServer.USER_DOCTOR);
		} catch (UnknownCommandException uce) {
			System.err.println("Caught UCE! on list as doctor");
		}

		try {
			Command c = CommandFactory.makeCommand("delete".getBytes(), JournalServer.USER_DOCTOR);
		} catch (UnknownCommandException uce) {
			System.err.println("Caught UCE! on delete with no ref as doctor");
		}
		try {
			Command c = CommandFactory.makeCommand("delete 123".getBytes(), JournalServer.USER_DOCTOR);
		} catch (UnknownCommandException uce) {
			System.err.println("Caught UCE! on delete with ref as doctor");
		}
		try {
			Command c = CommandFactory.makeCommand("create 123".getBytes(), JournalServer.USER_DOCTOR);
		} catch (UnknownCommandException uce) {
			System.err.println("Caught UCE! on create as doctor");
		}
	}
}
