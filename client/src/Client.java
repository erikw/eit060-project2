import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Client { 
	private static final String LINE_UI = "> ";
	private Map<String, CommandFactory> factories;
	private BufferedReader buffReader;

	public static void main(String args[]) {
		new Client().run();
	}

	public Client() {
		buffReader = new BufferedReader(new InputStreamReader(System.in));
		factories = new HashMap<String, CommandFactory>();
		factories.put(ReadFactory.COMMAND_NAME, new ReadFactory());
		factories.put(ListFactory.COMMAND_NAME, new ListFactory());
		factories.put(AppendFactory.COMMAND_NAME, new AppendFactory());
		factories.put(CreateFactory.COMMAND_NAME, new CreateFactory());
	}

	public void run() {
		System.out.println("Started secure client...");
		System.out.println("Quit by sending an EOF.");
		System.out.println("Available commands are:");
		for (CommandFactory<Command> factory : factories.values()) {
			System.out.println(factory.helpText());
			//System.out.println("----");
		}
		
		System.out.print(LINE_UI);
		String inputLine;
		try {
			while ((inputLine = buffReader.readLine()) != null) {
				String[] parts = inputLine.split("\\s+");
				if (parts.length > 0 && parts[0].length() > 0) {
					if (parts[0].equals("q")) {
						break; // I found my self trying to quit with q so often...
					}
					CommandFactory<Command> factory = factories.get(parts[0]);
					if (factory == null) {
						System.err.println("Not a valid command.");
					} else {
						try {
							Command command = factory.makeCommand(parts);
							// TODO send command over the wire with send(command.protocolString()) or such.
						} catch (BadCommandParamException bcpe) {
							System.err.println(bcpe.getMessage());
						}
					}
				}
				System.out.print(LINE_UI);
			}
		} catch (IOException ioe) {
			System.err.println("Error reading input.");
		}

		System.out.println("\nTerminating...");
	}

}
