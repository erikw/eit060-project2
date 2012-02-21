import java.util.Map;
import javax.security.cert.X509Certificate;

public abstract class  Command { 
	protected X509Certificate cert;
	
	public abstract String execute(int userType, Map<String, Record> records);

	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}
}
