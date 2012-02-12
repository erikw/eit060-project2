public class ReadFactory extends CommandFactory<ReadCommand> {
		public static final String COMMAND_NAME = "read";

		public ReadCommand makeCommand(String[] args) throws BadCommandParamException {
			ReadCommand command;
			if (args.length == 2) {
				if (args[1].matches("\\d+")) {
					command =  new ReadCommand(args[1]);	
				} else {
					throw new BadCommandParamException(COMMAND_NAME, "Records ID must be digits only.");
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
			sb.append ("Retrives records.").append("\n");
			sb.append("\t").append("[All] Supply an record ID (numbers only) you want to retrive.").append("\n");
			return sb.toString();
		}

}
