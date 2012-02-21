package client;

public class DeleteFactory extends CommandFactory<DeleteCommand> {
		public static final String COMMAND_NAME = "delete";

		public DeleteCommand makeCommand(String[] args) throws BadCommandParamException {
			DeleteCommand command;
			if (args.length == 2) {
				if (args[1].matches("\\d+")) {
					command =  new DeleteCommand(args[1]);	
				} else {
					throw new BadCommandParamException(COMMAND_NAME, "Record ID must be digits only.");
				}
			} else {
				throw new BadCommandParamException(COMMAND_NAME, "Wrong number of arguments. One expected.");
			}
			return command;
		}

		public String helpText() {
			StringBuilder sb = new StringBuilder();
			sb.append(COMMAND_NAME);
			sb.append(" - ");
			sb.append ("Deletes a specified records.").append("\n");
			sb.append("\t").append("[Agency] Supply and patient ID identifying the patient to be give a new record.").append("\n");
			sb.append("\t").append("[Others] NA.").append("\n");
			return sb.toString();
		}
}
