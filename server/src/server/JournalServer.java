package server;

/*
 * Documentation for Jessy:
 * http://docs.oracle.com/javase/1.4.2/docs/api/javax/net/ssl/package-summary.html
 */

import java.security.*;
import javax.security.cert.X509Certificate;
import javax.net.*;
import javax.net.ssl.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.net.Socket;
import net.*;

public class JournalServer {
	private static final int LENGTH_LENGTH = 4; // length of the length field, bytes
	private KeyStore keyStore;
	private Logger log;
	private Map<Integer, Record> records;

	public static void main(String args[]) {
		new JournalServer().start(8080);
	}

	public JournalServer() {
		try {
			keyStore = KeyStore.getInstance("JKS");
		} catch (KeyStoreException e) {
			trace("could not open keystore. exiting (" + e + ")");
		}		

		FileHandler fileHandler = null;
		try {
			fileHandler = new FileHandler("logs/audit.log", 10*1024*1024, 100, true);
		} catch (IOException ioe) {
			trace("Could not open log file.");
		}
		fileHandler.setFormatter(new SimpleFormatter());
		log = Logger.getLogger("auditLog");
		log.addHandler(fileHandler);

		records = new HashMap<Integer, Record>();
		putStaticRecords();
	}

	private void trace(String msg) {
		System.out.println("Trace---> " + msg);
	}

	private void log(String subj, Socket sock, String logmsg) {
		String ip = (sock != null) ? sock.getInetAddress().toString() : "#";
		if (subj == null)
			subj = "#";
		
		log.info(String.format("| %15s | %15s | %s ", subj, ip, logmsg));
	}

	private void log(String logmsg) {
		log(null, null, logmsg);
	}

	public void start(int port) {
		SSLServerSocketFactory ssf = null;
		SSLServerSocket ss;

		String passwds[];

		SSLContext ctx;
		KeyManagerFactory kmf;
		KeyStore ks, ts;
		TrustManagerFactory tmf;
		String keyStorePasswd = null;
		String keyPasswd  = null;
		String trustPasswd = null;

		log("Server started");
		try {
			passwds = this.readPassword();
			keyStorePasswd = passwds[0];
			keyPasswd = passwds[1];
			trustPasswd = passwds[2];
		} catch (java.io.IOException e) {
			this.trace("could not read passwords (" + e + ")");
			log("could not read passwords, exiting (" + e + ")");
			System.exit(1);
		}

		try {
			ctx = SSLContext.getInstance("TLS");
			kmf = KeyManagerFactory.getInstance("SunX509");
			ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream("./keystore"), keyStorePasswd.toCharArray());
			kmf.init(ks, keyPasswd.toCharArray());

			tmf = TrustManagerFactory.getInstance("SunX509");
			ts = KeyStore.getInstance("JKS");
			ts.load(new FileInputStream("./truststore"), trustPasswd.toCharArray());
			tmf.init(ts);

			ctx = SSLContext.getInstance("TLS");
			ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			ssf = ctx.getServerSocketFactory();
		} catch (Exception e) {
			trace("shit went south, bailing. (" + e + ")");
			log("Error accessing stores, exiting");
			System.exit(1);
		}

		try {
			ss = (SSLServerSocket) ssf.createServerSocket(port);
		} catch (Exception e) {
			trace("could not bind to port. " + e);
			return;
		}

		ss.setNeedClientAuth(true);

		// accept clients, maybe sould be multithreaded later
		while (true) {
			trace("waiting for incomming connection");
			SSLSocket sock;

			try {
				sock = (SSLSocket) ss.accept();
			} catch (java.io.IOException e) {
				trace("failed to accept connection");
				continue;
			}

			trace("accepted incomming connection");

			// String[] suites = {"TLS_DHE_DSS_WITH_AES_256_CBC_SHA"};
			// sock.setEnabledCipherSuites(suites);
			SSLSession sess = sock.getSession();
			X509Certificate cert;

			try {
				cert = (X509Certificate)sess.getPeerCertificateChain()[0];
			} catch (javax.net.ssl.SSLPeerUnverifiedException e) {
				trace("client not verified");
				log("#", sock, "Failed to verify");
				try {
					sock.close();
				} catch (java.io.IOException e2) {
					trace("failed closing socket, w/e");
				}
				continue;
			}

			String subj = cert.getSubjectDN().getName();
			trace("client DN: " + subj);
			log(subj, sock, "connected");

			InputStream in;
			OutputStream out;
			try {
				in = sock.getInputStream();
				out = sock.getOutputStream();
			} catch (java.io.IOException e) {
				trace("failed to get inputstream or output stream.");
				try {
					sock.close();
				} catch (java.io.IOException e2) {
					trace("failed closing socket, w/e");
				}
				continue;
			}
			boolean terminated = false;
			while (!terminated) {
				char[] message;
				try {
					message = MessageReader2.readMessage(in);
				} catch (ContinueException e) {
					continue;
				} catch (TerminateException e) {
					terminated = true;
					this.trace("logical err in msg");
					continue;
				} catch (IOException e) {
					terminated = true;
					this.trace("connection error");
					continue;
				}					
				Command command;
				try {
					command = CommandFactory.makeCommand(message, cert);
				} catch (UnknownCommandException uce) {
					trace("Got unparsable command.");
					terminated = true;
					break;
				}

				// TODO skriv ut det med prefix längd.
				//out.write(command.execute(records, units).toBytes)
				String resp = command.execute(records);
				int mask = 0x0f;
				int len = resp.getBytes().length;
				try {
					for (int i = 3; i >= 0; i--) {
						sock.getOutputStream().write((len >> i) & mask);
					}
					OutputStreamWriter osw = new OutputStreamWriter(out);
					osw.write(resp, 0, len);
					osw.flush();
				} catch (IOException ioe) {
					terminated = true;
				}

			}
			if (terminated)
				log("terminated");
				trace("terminated");
		}
	}

	private String[] readPassword() throws IOException {
		String keystorePassword = null,
			   truststorePassword = null;
		while (keystorePassword == null || keystorePassword.length() == 0) {
			System.out.print("Server keystore password:");
			keystorePassword = new String(System.console().readPassword());
		}

		while (truststorePassword == null || truststorePassword.length() == 0) {
			System.out.print("Server truststore password:");
			truststorePassword = new String(System.console().readPassword());
		}

		String pws[] = new String[3];
	    pws[0] = keystorePassword;
	    pws[1] = keystorePassword;
	    pws[2] = truststorePassword;
		return pws;
	}

	private void putStaticRecords() {
		records.put(Record.getNextRecordID(), new Record("DEP1", "9010211192", "N001", "D001", "Ill, not sick." ));
		records.put(Record.getNextRecordID(), new Record("DEP1", "7001010000", "N001", "D001", "Bruten stortå. Lagad med tajp." ));
		records.put(Record.getNextRecordID(), new Record("DEP2", "9008144553", "N002", "D002", "Y2K error in brain." ));
	}
}
