import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import java.util.Arrays;

public class Client {
	private static final String LINE_UI = "> ";
	private Map<String, CommandFactory> factories;
	private BufferedReader buffReader;
	private static InetAddress serverIP;
	private static int serverPort = 1025;
	private String passwordKeystore;
	private static String[] validUsers = new String[] {"patient", "doctor", "nurse", "agency"};
	private PrintWriter out;
	private BufferedReader in;

	public static void main(String args[]) {
		Client client = null;
		boolean error = false;
		if (args.length == 0) {
			System.out.println("No user specified. Running with user \"patient\"");
			client = new Client();
		} else if (args.length == 1) {
			if (Arrays.asList(validUsers).contains(args[0])) {
				client = new Client(args[0]);
			} else {
				error = true;
			}
		} else {
			error = true;
		}

		if (error) {
			System.err.print("Specify none or one user to run the program as. Valid users are: ");
			for (String user : validUsers) {
				System.err.print(user + " ");
			}
			System.err.println(".");
			System.exit(1);

		} else {
			client.run();
		}
	}

	public Client() {
		this("patient");
	}

	public Client(String user) {
		
		buffReader = new BufferedReader(new InputStreamReader(System.in));

		factories = new HashMap<String, CommandFactory>();
		factories.put(ReadFactory.COMMAND_NAME,   new ReadFactory());
		factories.put(ListFactory.COMMAND_NAME,   new ListFactory());
		factories.put(AppendFactory.COMMAND_NAME, new AppendFactory());
		factories.put(CreateFactory.COMMAND_NAME, new CreateFactory());
		factories.put(DeleteFactory.COMMAND_NAME, new DeleteFactory());

		try {
			serverIP = InetAddress.getByAddress(new byte[] {(byte) 192,(byte) 168, 0, 1});
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
	}

	public void run() {
		System.out.println("Started secure client.");
		System.out.println("Quit by sending an EOF.");
		try {
			readPassword();
			connectServer();

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
							// Send command to server!
							out.print(command.protocolString());

							String serverResponse;

							while ((serverResponse = in.readLine()) != null)
								System.out.println(serverResponse); // TODO do something more fancy here?

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
		SSLSocketFactory socketFactory = null;
		SSLContext sslContext;
		KeyManagerFactory keyFactory;
		try {
			sslContext = SSLContext.getInstance("TLS");
			keyFactory = KeyManagerFactory.getInstance("SunX509");
			// TODO continue here...
		} catch (java.security.GeneralSecurityException gse) {
			gse.printStackTrace();
		}

		BufferedReader in = new BufferedReader(
				new InputStreamReader(
					socket.getInputStream()));
	}

	private void readPassword() throws IOException {
		System.out.print("Keystore password:");
		while (passwordKeystore == null || passwordKeystore.length() == 0) {
			System.out.print("Keystore password:");
			passwordKeystore = new String(System.console().readPassword());
		}
	}
}
