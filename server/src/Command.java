import java.util.Map;
import javax.security.cert.X509Certificate;
import java.util.logging.*;
// TODO put cert stuff in attr.

public abstract class  Command { 
	protected String subject;
	protected String departmentID;
	protected Logger log = Logger.getLogger("auditLog");
	
	public abstract String execute(Map<Integer, Record> records);

	public void setCert(X509Certificate cert) {
		String[] tmp = cert.getSubjectDN().getName().split(" ");
		subject = tmp[0].substring(3, tmp[0].length() - 1);
		departmentID = tmp[1].substring(3, tmp[1].length() - 1);
	}
}
