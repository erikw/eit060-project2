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
import javax.security.cert.X509Certificate;
import java.security.KeyStoreException;

public class JournalServer {
    private static final int LENGTH_LENGTH = 16; // length of the length field, bytes
    protected KeyStore keyStore;    
    
    public JournalServer() {
	System.setProperty("javax.net.ssl.keyStore", "../crypt_server/keystore");
	System.setProperty("javax.net.ssl.keyStorePassword", "passwd");
	try {
	    this.keyStore = KeyStore.getInstance("JKS");
	} catch (KeyStoreException e) {
	    this.log("could not open keystore");
	}
    }

    protected void log(String msg) {
	System.out.println("SERVER:\t" + msg);
    }

    public void start(int port) {
	SSLServerSocketFactory fac = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault(); 
	SSLServerSocket ss;
	try {
	    ss = (SSLServerSocket)fac.createServerSocket(port);
	} catch (java.io.IOException e) {
	    this.log("could not bind to port. " + e);
	    return;
	}
	while (true) {
	    this.log("waiting for incomming connection");
	    SSLSocket sock;
	    try {
		sock = (SSLSocket) ss.accept();
	    } catch (java.io.IOException e) {
		this.log("failed to accept connection");
		continue;
	    }
	    this.log("accepted incomming connection");
	    SSLSession sess = sock.getSession();
	    X509Certificate cert;
	    try {
		cert = (X509Certificate)sess.getPeerCertificateChain()[0];
	    } catch (javax.net.ssl.SSLPeerUnverifiedException e) {
		this.log("client not verified");
		try {
		    sock.close();
		} catch (java.io.IOException e2) {
		    this.log("failed closing socket, w/e");
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
		this.log("failed to get inputstream");
		try {
		    sock.close();
		} catch (java.io.IOException e2) {
		    this.log("failed closing socket, w/e");
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
		this.log("the msg is " + length + " bytes long");
	    } else {
		this.log("SERVER:\tfailed to read length field");
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