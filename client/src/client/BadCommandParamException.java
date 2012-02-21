package client;

import java.io.IOException;

public class BadCommandParamException extends IOException {

	public BadCommandParamException(String commandName, String err) {
		super("Bad parameters to \"" + commandName + "\" was given: " + err);	
	}
}
