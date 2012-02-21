package client;

import java.util.Map;
import java.util.HashMap;
import java.io.*;
import java.security.*;
import javax.net.ssl.*;
import java.net.*;
import java.util.Arrays;
import net.*;

public class Client {
	private static final String LINE_UI = "> ";
	private Map<String, CommandFactory> factories;
	private BufferedReader buffReader;
	private static InetAddress serverIP;
	private static int serverPort = 8080;
	private String user;
	private String keystorePassword;
	private String passwordKey;
    private String truststorePassword;
	private static String[] validUsers = new String[] {"patient", "doctor1", "doctor2", "nurse1", "nurse2", "agency"};
	private PrintWriter out;
	private InputStream in;
    private SSLSocket socket = null;
    private boolean isConnect = false;

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
		this.user = user;	
		buffReader = new BufferedReader(new InputStreamReader(System.in));

		factories = new HashMap<String, CommandFactory>();
		factories.put(ReadFactory.COMMAND_NAME,   new ReadFactory());
		factories.put(ListFactory.COMMAND_NAME,   new ListFactory());
		factories.put(AppendFactory.COMMAND_NAME, new AppendFactory());
		factories.put(CreateFactory.COMMAND_NAME, new CreateFactory());
		factories.put(DeleteFactory.COMMAND_NAME, new DeleteFactory());

		try {
			serverIP = InetAddress.getByAddress(new byte[] {(byte) 127,(byte) 0, 0, 1});
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
			while ((inputLine = buffReader.readLine()) != null && isConnect) {
				String[] parts = inputLine.split("\\s+");
				if (parts.length > 0 && parts[0].length() > 0) {
					if (parts[0].equals("q")) {
						break; // I found my self trying to quit with q so often...
					}

					CommandFactory<Command> factory = factories.get(parts[0]);
					if (factory == null) {
						System.err.println("Not a valid command.");
					} else {
						Command command = null;
						try {
							command = factory.makeCommand(parts);
						} catch (BadCommandParamException bcpe) {
							System.err.println(bcpe.getMessage());
							continue;
						}
						// Send command to server!
						String protoString = command.protocolString();
						int len = protoString.getBytes().length;
						int mask = 0x0f;
						try {
							for (int i = 3; i >= 0; i--) {
								socket.getOutputStream().write((len >> i) & mask);
							}
							OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
							osw.write(protoString, 0, len);
							osw.flush();
						} catch (IOException ioe) {
						    killConnection();
						    
						    System.err.println(ioe.getMessage());
						    ioe.printStackTrace();
						}
					}
					try {
						char[] resp = MessageReader2.readMessage(socket.getInputStream());
						System.out.println(resp);
					} catch(IOException e) {
						isConnect = false;
						killConnection();
					} catch(TerminateException e) {
						isConnect = false;
						killConnection();
					} catch(ContinueException e) {
						System.out.println("Server is bullshitting. But still alive.");
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
	  
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			

			KeyManagerFactory keyFactory = KeyManagerFactory.getInstance("SunX509");
			KeyStore keyStore = KeyStore.getInstance("JKS");
			keyStore.load(new FileInputStream("users/" + user + "/keystore"), keystorePassword.toCharArray());
			keyFactory.init(keyStore, passwordKey.toCharArray());

			TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			KeyStore trustStore = KeyStore.getInstance("JKS");
			trustStore.load(new FileInputStream("users/" + user + "/truststore"), truststorePassword.toCharArray());
			trustFactory.init(trustStore);

			sslContext.init(keyFactory.getKeyManagers(), trustFactory.getTrustManagers(), null);
			SSLSocketFactory sslfactory = sslContext.getSocketFactory();

			socket = (SSLSocket)sslfactory.createSocket(serverIP, serverPort);

			socket.setUseClientMode(true);
			socket.startHandshake();

			out = new PrintWriter(
						new OutputStreamWriter(
								       socket.getOutputStream()));

			
			if(out.checkError()) {
			    System.err.println("SSLSocketClient: java.io.PrintWriter error");
			}

			in = socket.getInputStream();
			isConnect = true;
			

			// works for now.
		} catch (GeneralSecurityException gse) {
			System.out.println("Could not authorize");
			System.exit(1);
		} catch (IOException ioe) {
			System.out.println("Could not connect");
			System.exit(1);
		} 

	}

    private void killConnection() {
	try {
	in.close();
	out.close();
	socket.close();
	} catch (IOException ioe) {
	    System.err.println("Disconnection failed.");
	    ioe.printStackTrace();
	}
    }

	private void readPassword() throws IOException {
		 // while (keystorePassword == null || keystorePassword.length() == 0) {
		 // 	 System.out.print("Keystore password:");
		 // 	 keystorePassword = new String(System.console().readPassword());
		 // }
		 // passwordKey = keystorePassword;

		 // while (truststorePassword == null || truststorePassword.length() == 0) {
		 // 	 System.out.print("Truststore password:");
		 // 	 truststorePassword = new String(System.console().readPassword());
		 // }
		keystorePassword = "pzNniiebY9oqs";
		passwordKey = "pzNniiebY9oqs";
		truststorePassword = "xLN75gzUJz7Yh";
	}
}
