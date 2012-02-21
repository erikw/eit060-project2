import java.util.Map;
import javax.security.cert.X509Certificate;
// TODO put cert stuff in attr.

public abstract class  Command { 
	protected X509Certificate cert;
	
	public abstract String execute(Map<String, Record> records);

	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}
}
