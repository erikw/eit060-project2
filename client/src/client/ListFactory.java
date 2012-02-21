package client;

public class ListFactory extends CommandFactory<ListCommand> {
		public static final String COMMAND_NAME = "list";

		public ListCommand makeCommand(String[] args) throws BadCommandParamException {
			ListCommand command;
			if (args.length == 1) {
				command =  new ListCommand();	
			} else if (args.length == 2) {
				if (args[1].matches("\\d+")) {
					command =  new ListCommand(args[1]);	
				} else {
					throw new BadCommandParamException(COMMAND_NAME, "Patient ID must be digits only.");
				}
			} else {
				throw new BadCommandParamException(COMMAND_NAME, "Too many arguments. None or one expected.");
			}
			return command;
		}

		public String helpText() {
			StringBuilder sb = new StringBuilder();
			sb.append(COMMAND_NAME);
			sb.append(" - ");
			sb.append ("List records for a patient.").append("\n");
			sb.append("\t").append("[Patient] No arguments given and all your records are listed.").append("\n");
			sb.append("\t").append("[Others] Supply an patient ID (numbers only) you want to list records for retrive.").append("\n");
			return sb.toString();
		}

}
