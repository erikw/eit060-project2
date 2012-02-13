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
		factories.put(ReadFactory.COMMAND_NAME,   new ReadFactory());
		factories.put(ListFactory.COMMAND_NAME,   new ListFactory());
		factories.put(AppendFactory.COMMAND_NAME, new AppendFactory());
		factories.put(CreateFactory.COMMAND_NAME, new CreateFactory());
		factories.put(DeleteFactory.COMMAND_NAME, new DeleteFactory());
	}

	public void run() {
		System.out.println("Started secure client.");
		System.out.println("Quit by sending an EOF.");
		try {
			openKeystores();

			System.out.println("Available commands are:");
			for (CommandFactory<Command> factory : factories.values()) {
				System.out.println(factory.helpText());
			}
			System.out.println("-------");
			
			System.out.print(LINE_UI);
			String inputLine;
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

	private void connectServer() {
		
	}

	private void openKeystores() throws IOException {
			System.out.print("Keystore password:");
			String passwordKeystore = null;
			ConsoleEraser eraser = new ConsoleEraser();
				System.out.print("Keystore password:");
			eraser.run();
			while (passwordKeystore == null || passwordKeystore.length() == 0) {
				passwordKeystore = buffReader.readLine();
				// TODO test opening keystore
			}
			eraser.stopIt();
	}

	//private static void sttyEcho(boolean on) {
		//String command = "/bin/sh -c /bin/stty ";
		//command	+= on ? "" : "-";
		//command += "echo";
		//command += " </dev/tty";
		//try {
			//Process proc = Runtime.getRuntime().exec("/bin/sh -c /bin/stty -echo < /dev/tty");
			//proc.waitFor();
		//} catch (InterruptedException ie) {
		//} catch (IOException ioe) {

		//}
	//}

	private class ConsoleEraser extends Thread {
		private boolean stop;

		public void run() {
			while (!stop) {
				System.out.print("\b");
			}
		}

		synchronized void stopIt() {
			stop = true;
		}
	}
}
