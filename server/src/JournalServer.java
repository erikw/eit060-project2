/*
 * documentation for java ssl stuff can be found here:
 * http://docs.oracle.com/javase/1.4.2/docs/api/javax/net/ssl/package-summary.html
 */

import java.security.KeyStore;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSession;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.security.cert.X509Certificate;
import java.security.KeyStoreException;
import javax.net.ssl.*;
import javax.net.*;

public class JournalServer {
	private static final int LENGTH_LENGTH = 16; // length of the length field, bytes
	protected KeyStore keyStore;

	public JournalServer() {
		System.setProperty("javax.net.ssl.trustStore", "myTrustStore");
		System.setProperty("javax.net.ssl.trustStorePassword", "password3");
		System.setProperty("javax.net.ssl.keyStore", "../keystore");
		System.setProperty("javax.net.ssl.keyStorePassword", "password1");
		System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
		// System.setProperty("javax.net.ssl.keyStore", "new.p12");
		// System.setProperty("javax.net.ssl.keyStorePassword", "newpasswd");

		try {
			this.keyStore = KeyStore.getInstance("JKS");
		} catch (KeyStoreException e) {
			log("could not open keystore. exiting");
		}		
		// this.keyStore.load(new FileInputstream
	}

	private void log(String msg) {
		System.out.println("SERVER:\t" + msg);
	}

	public void start(int port) {
		// SSLServerSocketFactory fac = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault(); 
	    SSLServerSocketFactory ssf = null;
		SSLServerSocket ss;
		// set up the server
		// try {
		// 	ss = (SSLServerSocket)fac.createServerSocket(port);
		// } catch (java.io.IOException e) {
		// 	log("could not bind to port. " + e);
		// 	return;
		// }
		char[] pw = "password1".toCharArray();

		// other stuff that might work
		SSLContext ctx;
		KeyManagerFactory kmf;
		KeyStore ks;
		try {
		    ctx = SSLContext.getInstance("TLS");
		    kmf = KeyManagerFactory.getInstance("SunX509");
		    ks = KeyStore.getInstance("JKS");
		    ks.load(new FileInputStream("../keystore"), pw);
		    kmf.init(ks, "password2".toCharArray());
		    ctx.init(kmf.getKeyManagers(), null, null);
		
		    ssf = ctx.getServerSocketFactory();
		} catch (Exception e) {
		    this.log("shit went south, bailing. (" + e + ")");
		    System.exit(1);
		}
		
		try {
		    ss = (SSLServerSocket) ssf.createServerSocket(port);
		} catch (Exception e) {
			log("could not bind to port. " + e);
			return;
		}

		ss.setNeedClientAuth(true);

		// accept clients, maybe sould be multithreaded later
		while (true) {
			log("waiting for incomming connection");
			SSLSocket sock;

			try {
				sock = (SSLSocket) ss.accept();
			} catch (java.io.IOException e) {
				log("failed to accept connection");
				continue;
			}

			log("accepted incomming connection");
			SSLSession sess = sock.getSession();
			X509Certificate cert;

			try {
				cert = (X509Certificate)sess.getPeerCertificateChain()[0];
			} catch (javax.net.ssl.SSLPeerUnverifiedException e) {
				log("client not verified");
				try {
					sock.close();
				} catch (java.io.IOException e2) {
					log("failed closing socket, w/e");
				}
				continue;
			}

			String subj = cert.getSubjectDN().getName();
			System.out.println("SERVER:\tclient DN: " + subj);

			int readBytes = 0;
			int tmp;
			int length = 0;
			InputStream in;
			try {
				in = sock.getInputStream();
			} catch (java.io.IOException e) {
				log("failed to get inputstream");
				try {
					sock.close();
				} catch (java.io.IOException e2) {
					log("failed closing socket, w/e");
				}
				continue;
			}

			while (readBytes < LENGTH_LENGTH) {
				try {
					tmp = in.read();
				} catch (java.io.IOException e) {
					continue;
				}
				readBytes += 1;
				length += tmp << (LENGTH_LENGTH - readBytes);
			}

			if (readBytes == LENGTH_LENGTH) {
				log("the msg is " + length + " bytes long");
			} else {
				log("SERVER:\tfailed to read length field");
				continue;
			}
			// got length, do work.
		}
	}

	protected String parseCmd(String cmd) {
		return "You wrote " + cmd + "\n";
	}

	public static void main(String args[]) {
		JournalServer js;

		js = new JournalServer();
		js.start(8080);
	}
}
