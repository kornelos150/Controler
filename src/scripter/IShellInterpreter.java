package scripter;

import javax.script.ScriptException;
import javax.swing.JTextArea;

public interface IShellInterpreter {
	
	Boolean isCommandComplete(String command);	
	String evaluateCommand(String command) throws ScriptException;
	void runStript(String text, Boolean isFromFile) throws ScriptException, Exception;
}
