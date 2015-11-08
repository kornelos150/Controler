package scripter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JTextArea;

public class JavaScriptInterpreter implements IShellInterpreter {
	
	private final String InitialScripts = "src\\scripter\\cmd.js";
	private ScriptEngine engine;
	private Invocable invocable;
	private EventStringWriter stringWriter;
	private JTextArea outputScript;
	private WriterActionListener listener;
	
	public JavaScriptInterpreter() {
		engine = new ScriptEngineManager().getEngineByName("nashorn");
		invocable = (Invocable)engine;
		stringWriterInit();
		engine.getContext().setWriter(stringWriter);
		
		loadInitScript();
		
	}
	
	private void loadInitScript()
	{
		try {
			engine.eval(new FileReader("src/scripter/commandScript.js"));
		} catch (FileNotFoundException e) {
			System.out.println("Unexpected file not found");
			e.printStackTrace();
		} catch (ScriptException e) {
			System.out.println("Unexpected sript error");
			e.printStackTrace();
		}
	}
	
	private void stringWriterInit()
	{
		stringWriter = new EventStringWriter();
		stringWriter.getBuffer().setLength(0);
		listener = new WriterActionListener();
		stringWriter.addActionListener(listener);
	}
	
	public void setOutputScript(JTextArea outputScript) {
		this.outputScript = outputScript;
	}


	@Override
	public Boolean isCommandComplete(String command) {
		Boolean result = false;
		if(command.endsWith(";"))
			result = true;
		
		return result;
	}

	@Override
	public String evaluateCommand(String command) throws ScriptException {
		engine.eval(command);
		String res = stringWriter.toString();
		stringWriter.getBuffer().setLength(0);
		return res;
	}
		

	@Override
	public void runStript(String text, Boolean isFromFile) throws Exception, ScriptException {
		stringWriter.setIsEvent(true);
		if(isFromFile)
			engine.eval(new FileReader(text));
		else
			engine.eval(text);
		stringWriter.setIsEvent(false);
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	class WriterActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			outputScript.setEditable(true);
			String text = outputScript.getText();
			outputScript.setText(text + stringWriter.toString());
			stringWriter.getBuffer().setLength(0);
			outputScript.setEditable(false);
		}
	}
}


