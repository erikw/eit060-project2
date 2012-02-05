public abstract class CommandFactory<C extends Command> {
	public static String COMMAND_NAME;
	//public static String getCommandName();
	public abstract C makeCommand(String[] args) throws BadCommandParamException;
	public abstract String helpText();
}
