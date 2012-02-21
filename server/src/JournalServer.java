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

public class JournalServer {
	private static final int LENGTH_LENGTH = 4; // length of the length field, bytes
	private KeyStore keyStore;
	private Logger log;
	private Map<String, Record> records;
	private Map<String, String> units;
	

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

		records = new HashMap<String, Record>();
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
				int readBytes = 0;
				int tmp, tmp_shift;
				int length = 0;

				while (readBytes < LENGTH_LENGTH) {
					try {
						tmp = in.read();
					} catch (java.io.IOException e) {
						continue;
					}
					++readBytes;
					tmp_shift = tmp << (LENGTH_LENGTH - readBytes);
					length |= tmp_shift;
					System.out.printf("raw:%s shifted:%d addedToLength:%d\n", Integer.toHexString(tmp), tmp_shift, length);
				}
				if (length < 0) {
					trace("the client is fucking w/ us. Alternativly the connection died");
					terminated = true;
					continue;
				} else if (readBytes == LENGTH_LENGTH) {
					trace("the msg is " + length + " bytes long");
				} else {
					trace("failed to read length field");
					continue;
				}
				// got length, do work.
				InputStreamReader reader = new InputStreamReader(in);
				char[] message = new char[length];
				int ret;
				int offset = 0;
				while (offset < length) {
					try {
						ret = reader.read(message, offset, (length - offset));
					} catch(Exception e) {
						trace("got exception while reading message: " + e.toString());
						break;
					}

					if (ret == -1) {
						trace("fuck. something went south. breaking the parsing of message.");
						break;
					}
					offset += ret;
				}
				if (offset < length) {
					trace("could not read complete message");
					terminated = true;
					break;
				}

				Command command;
				try {
					//command = CommandFactory.makeCommand(message, userType);
					command = CommandFactory.makeCommand(message, cert);
				} catch (UnknownCommandException uce) {
					trace("Got unparsable command.");
					terminated = true;
					break;
				}

				// TDOD skriv ut det med prefix längd.
				//out.write(command.execute(userType, records).toBytes)

			}
			if (terminated)
				log("terminated");
				trace("terminated");
		}
	}

	private String[] readPassword() throws IOException {
		// while (keystorePassword == null || keystorePassword.length() == 0) {
		// 	System.out.print("Server keystore password:");
		// 	keystorePassword = new String(System.console().readPassword());
		// }

		// while (keyPassword == null || keyPassword.length() == 0) {
		// 	System.out.print("Server keystore password:");
		// 	keystorePassword = new String(System.console().readPassword());
		// }

		// while (truststorePassword == null || truststorePassword.length() == 0) {
		// 	System.out.print("Server truststore password:");
		// 	truststorePassword = new String(System.console().readPassword());
		// }
		String pws[] = new String[3];
	    pws[0] = "LrLV3w4TZ5I6a";
	    pws[1] = "LrLV3w4TZ5I6a";
	    pws[2] = "QWH7yedDGR18y";
		return pws;
	}

	private void putStaticRecords() {
		records.put(Record.getNextRecordID(), new Record("1", "901021-1192", "1", "1", "Ill, not sick." ));
		records.put(Record.getNextRecordID(), new Record("1", "700101-0000", "1", "1", "Bruten stortå. Lagad med tajp." ));
		records.put(Record.getNextRecordID(), new Record("2", "900814-4553", "2", "2", "Y2K error in brain." ));

		units.put("n1", "unit1");
		units.put("n2", "unit2");
		unit.put("d1", "unit1");
		unit.put("d2", "unit2");
	}
}
