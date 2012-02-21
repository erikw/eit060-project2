import java.util.Map;

public interface Command { 
	public String execute(Map<String, Record> records);
}
