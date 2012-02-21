public class CreateFactory extends CommandFactory<CreateCommand> {
		public static final String COMMAND_NAME = "create";

		public CreateCommand makeCommand(String[] args) throws BadCommandParamException {
			CreateCommand command;
			if (args.length == 3) {
				if (args[1].matches("\\d+") && args[2].matches("n\\d+")) {
					command =  new CreateCommand(args[1]);	
				} else {
					throw new BadCommandParamException(COMMAND_NAME, "Patient ID must be digits only and nurse id must begin with 'n' and digits must follow");
				}
			} else {
				throw new BadCommandParamException(COMMAND_NAME, "Wrong number of arguments. Two expected.");
			}
			return command;
		}

		public String helpText() {
			StringBuilder sb = new StringBuilder();
			sb.append(COMMAND_NAME);
			sb.append(" - ");
			sb.append ("Creates a new records for a specified patient.").append("\n");
			sb.append("\t").append("[Doctor] Supply a patient ID identifying the patient to be give a new record and the associated nurse id.").append("\n");
			sb.append("\t").append("[Others] NA.").append("\n");
			return sb.toString();
		}
}
