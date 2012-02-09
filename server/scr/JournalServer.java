class JournalServer {
    protected KeyStore keyStore;    

    public JournalServer() {
	System.setProperty(javax.net.ssl.keyStore, "../crypt_server/keystore");
	System.setProperty(javax.net.ssl.keyStorePassword, "passwd");
	this.keyStore = KeyStore.getInstance("JKS");

	this.takeRequests();
    }

    protected void takeRequests() {
	SSLServerSocket ss;
	SSLSocket sock;
	SSLSesion sess;
	X509Certificate cert;
	String subj;
	SSLServerSocketFactory fac = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
	BufferedReader buffr;
	String cmd;

	ss = fac.createServerSocket("8080");
	while (true) {
	    System.out.println("server:\twaiting for incomming connection");
	    sock = (SSLSocket) ss.accept();
	    System.out.println("server:\taccepted incomming connection");
	    sess = socket.getSession();
	    cert = (X509Certificate)session.getPeerCertificateChain()[0];
	    subj = cert.getSubjectDN().getName();
	    
	    // cont with actual work...
            buffr = new BufferedReader(new InputStreamReader(ss.getInputStream()));
            while ((cmd = bufferedreader.readLine()) != null) {
                System.out.println("server:\trecieved command " + cmd + "\n");
		this.parseCmd(cmd);
            }
	}
    }
    
    protected String parseCmd(String cmd) {
	return "";
    }

    public static int main(String args[]) {
	JournalServer js = new Journalserver();
    }
