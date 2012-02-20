public class CommandFactory {

	public static Command makeCommand(char[] data, int userType) throws UnknownCommandException {
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
					int patientID = Integer.parseInt(parts[1]);
					command = new ListCommand(patientID);
				}
			} else if (commandName.equals("append")) {
				System.out.println("Command is append");
				parts = parts[1].split(" ", 2);
				int recordID = Integer.parseInt(parts[0]);
				String text = parts[1];
				System.out.println("with recordID=" + recordID + " and text=" + text);
				command = new AppendCommand(recordID, text);
			} else if (commandName.equals("create")) {
				System.out.println("Command is create");
				int patientID = Integer.parseInt(parts[1]);
				command = new CreateCommand(patientID);
			} else if (commandName.equals("delete")) {
				System.out.println("Command is delete");
				int recordID = Integer.parseInt(parts[1]);
				command = new DeleteCommand(recordID);
			} else if (commandName.equals("read")) {
				System.out.println("Command is read");
				int recordID = Integer.parseInt(parts[1]);
				command = new ReadCommand(recordID);
			} else {
				throw new UnknownCommandException();
			}

		} catch (Exception e) {
			throw new UnknownCommandException();
		}
		return command;
	}
}
