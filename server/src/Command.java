import java.util.Map;

public interface Command { 
	public String execute(int userType, Map<String, Record> records);
}
