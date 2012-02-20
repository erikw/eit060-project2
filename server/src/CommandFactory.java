public class CommandFactory {

	public static Command makeCommand(byte[] data, int userType) throws UnknownCommandException {
		String[] parts = new String(data).split(" ", 2);
		String commandName = parts[0];
		System.out.println("CommandFactory: parsed command name = " + commandName);
		Command command = null;
		try {
			if (commandName.equals("list")) {
				System.out.print("Command is list");
				if (userType == JournalServer.USER_PATIENT) {
					System.out.println(" for patient.");
					command = new ListCommand();
				} else {
					int ref = Integer.parseInt(parts[1]);
					command = new ListCommand(ref);
				}
			} else if (commandName.equals("append")) {

			} else if (commandName.equals("create")) {
				int patientID = Integer.parseInt(parts[1]);
				command = new DeleteCommand(patientID);
			} else if (commandName.equals("delete")) {
				int ref = Integer.parseInt(parts[1]);
				command = new DeleteCommand(ref);
			} else if (commandName.equals("read")) {

			} else {
				throw new UnknownCommandException();
			}

		} catch (Exception e) {
			throw new UnknownCommandException();
		}
		return command;
	}
}
