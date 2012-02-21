import javax.security.cert.X509Certificate;

public class CommandFactory {
	public static final int USER_PATIENT = 0;
	public static final int USER_NURSE   = 1;
	public static final int USER_DOCTOR  = 2;
	public static final int USER_AGENCY  = 3;

	public static Command makeCommand(char[] data, X509Certificate cert) throws UnknownCommandException {
		String[] parts = new String(data).split(" ", 2);
		String commandName = parts[0];
		System.out.println("CommandFactory: parsed command name = " + commandName);
		Command command = null;
		int userType = CommandFactory.getType(cert.getSubjectDN().getName());
		try {
			if (commandName.equals("list")) {
				System.out.print("Command is list");
				if (userType == CommandFactory.USER_PATIENT) {
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
				parts = parts[1].split(" ", 3);
				if (parts.length != 3)
					throw new UnknownCommandException();
				command = new CreateCommand(parts[0], parts[1], parts[2]);
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
		command.setCert(cert);
		return command;
	}

	private static int getType(String s) {
		switch (s.charAt(0)) {
		case 'd': return CommandFactory.USER_DOCTOR;
		case 'n': return CommandFactory.USER_NURSE;
		case 'a': return CommandFactory.USER_AGENCY;
		default:  return CommandFactory.USER_PATIENT;
		}
	}

}
