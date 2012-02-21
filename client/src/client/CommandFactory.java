package client;

public abstract class CommandFactory<C extends Command> {
	public static String COMMAND_NAME;
	public abstract C makeCommand(String[] args) throws BadCommandParamException;
	public abstract String helpText();
}
