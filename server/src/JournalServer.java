/*
 * documentation for java ssl stuff can be found here:
 * http://docs.oracle.com/javase/1.4.2/docs/api/javax/net/ssl/package-summary.html
 */

import java.security.KeyStore;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import java.net.ssl.SSLSession;
import java.io.OutputStream;
import java.io.InputStream;
import java.security.cert.X509Certificate;

class JournalServer {
    protected KeyStore keyStore;    

    public JournalServer() {
	System.setProperty(javax.net.ssl.keyStore, "../crypt_server/keystore");
	System.setProperty(javax.net.ssl.keyStorePassword, "passwd");
	this.keyStore = KeyStore.getInstance("JKS");
    }

    public void start(int port) {
	SSLServerSocket ss;
	SSLSocket sock;
	SSLSession sess;
	X509Certificate cert;
	String subj;
	SSLServerSocketFactory fac;
	InputStream in;
	OutputStream out;
	String cmd;
	int readBytes;
	byte[] buff;

	fac = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault(); 
	ss = (SSLServerSocket)fac.createServerSocket(port);
	while (true) {
	    System.out.println("SERVER:\twaiting for incomming connection");
	    sock = (SSLSocket) ss.accept();
	    System.out.println("SERVER:\taccepted incomming connection");
	    sess = sock.getSession();
	    cert = (X509Certificate)sess.getPeerCertificateChain()[0];
	    subj = cert.getSubjectDN().getName();
	    buff = new byte[16];
	    System.out.println("SERVER:\tclient DN: " + subj);
	    readBytes = 0;
            while (readBytes < 16) {
		readBytes += sock.getInputStream().read();
            }
	}
    }
    
    protected String parseCmd(String cmd) {
	return "You wrote " + cmd + "\n";
    }

    public static int main(String args[]) {
	JournalServer js;

	js = new JournalServer();
	js.start(8080);
    }
}