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
		try {
			Command c = CommandFactory.makeCommand("read 123".getBytes(), JournalServer.USER_DOCTOR);
		} catch (UnknownCommandException uce) {
			System.err.println("Caught UCE! on read as doctor");
		}
		try {
			Command c = CommandFactory.makeCommand("append 123 en lång text om en patient".getBytes(), JournalServer.USER_DOCTOR);
		} catch (UnknownCommandException uce) {
			System.err.println("Caught UCE! on read as doctor");
		}
	}
}
