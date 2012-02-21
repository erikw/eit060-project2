package client;

public class AppendFactory extends CommandFactory<AppendCommand> {
		public static final String COMMAND_NAME = "append";

		public AppendCommand makeCommand(String[] args) throws BadCommandParamException {
			AppendCommand command;
			if (args.length >= 3) {
				if (args[1].matches("\\d+")) {
					StringBuilder rebuilder = new StringBuilder(); // The correct way is ofc. to change the interface for parsing arguments at command line...
					for (int i = 2; i < args.length; ++i ) {
						rebuilder.append(args[i]);
						if (i != args.length - 1) {
							rebuilder.append(" ");
						}
					}
					command =  new AppendCommand(args[1], rebuilder.toString());
				} else {
					throw new BadCommandParamException(COMMAND_NAME, "Record ID must be digits only and at least one character of text is needed.");
				}
			} else {
				throw new BadCommandParamException(COMMAND_NAME, "Wrong number of arguments. 2 expected.");
			}
			return command;
		}

		public String helpText() {
			StringBuilder sb = new StringBuilder();
			sb.append(COMMAND_NAME);
			sb.append(" - ");
			sb.append ("Appends text to a specified record.").append("\n");
			sb.append("\t").append("[Patient, Nurse] Specify the recor ID followed by space and the text to append in ONE line.").append("\n");
			sb.append("\t").append("[Others] NA.").append("\n");
			return sb.toString();
		}

}
