public class CreateFactory extends CommandFactory<CreateCommand> { 
		public static final String COMMAND_NAME = "create";

		public CreateCommand makeCommand(String[] args) throws BadCommandParamException {
			CreateCommand command;
			if (args.length == 2) {
				if (args[1].matches("\\d+")) {
					command =  new CreateCommand(args[1]);	
				} else {
					throw new BadCommandParamException(COMMAND_NAME, "Patient ID must be digits only.");
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
			sb.append ("Creates a new records for a specified patient.").append("\n");
			sb.append("\t").append("[Doctor] Supply an patient ID identifying the patient to be give a new record.").append("\n");
			sb.append("\t").append("[Others] NA.").append("\n");
			return sb.toString();
		}
}
