public class ReadFactory extends CommandFactory<CommandRead> { 
		public static final String COMMAND_NAME = "read";

		public CommandRead makeCommand(String[] args) throws BadCommandParamException {
			CommandRead command;
			if (args.length == 1) {
				command =  new CommandRead(CommandRead.NO_ID);	
			} else if (args.length == 2) {
				if (args[1].matches("\\d+")) {
					command =  new CommandRead(args[1]);	
				} else {
					throw new BadCommandParamException(COMMAND_NAME, "Records ID must be digits only.");
				}
			} else {
				throw new BadCommandParamException(COMMAND_NAME, "Too many arguments. One expected.");
			}
			return command;
		}

		public String helpText() {
			StringBuilder sb = new StringBuilder();
			sb.append(COMMAND_NAME);
			sb.append(" - ");
			sb.append ("Retrives records.").append("\n");
			sb.append("\t").append("[Patient] No arguments given and your records are retrived").append("\n");
			sb.append("\t").append("[Others] Supply an record ID (numbers only) you want to retrive.").append("\n");
			return sb.toString();
		}

}
