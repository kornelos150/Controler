package scripter;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class InterpreterPanel extends JPanel {
	

	
	private JTextArea shell;
	private int _promptCursor = 0;
	public String mainPrompt = ">> ";
    private StringBuffer _commandBuffer = new StringBuffer();
    private IShellInterpreter interpreter;
    public String contPrompt = "";    
    private LinkedList<String> cmdHistory;
    private int cmdIndex;
	
	public InterpreterPanel(IShellInterpreter interpreter)
	{
		super(new BorderLayout());
		shell = new JTextArea();
		cmdHistory = new LinkedList<>();
		cmdIndex = 0;
		
		JScrollPane sp = new JScrollPane(shell);
		add(sp);
		this.interpreter = interpreter;
		shell.addKeyListener(new ShellKeyListener());
	}
	
	public static void main(String args[])
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new JFrame();
					InterpreterPanel interpreter = new InterpreterPanel(null);
					frame.add(interpreter);
					frame.pack();	        
					frame.setVisible(true);
					
					
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		
	}// main end
	
    public void setEditable() {
        shell.setEditable(true);        
    }
    
    public void addNotify () {
        super.addNotify();
        initialize();
    }

    public void initialize() {
    	clearShell();
        appendShell(mainPrompt);
    }
    
    public void clearShell() {
        Runnable doClearShell = new Runnable() {
                public void run() {
                    shell.setText("");
                    shell.setCaretPosition(0);
                    _promptCursor = 0;
                }
            };
        SwingUtilities.invokeLater(doClearShell);
    }
    
    public void setLine(String command)
    {
    	Runnable doSetLine = new Runnable() {
			@Override
			public void run() {
				String text = shell.getText().substring(0, _promptCursor);
				shell.setText(text);
				shell.append(command);
				
			}
		};
		SwingUtilities.invokeLater(doSetLine);
    }
	
	public void appendShell(final String text) {
        Runnable doAppendSh2 = new Runnable() {
                public void run() {
                    shell.append(text);
                    // Scroll down as we generate text.
                    shell.setCaretPosition(shell.getText().length());
                    // To prevent _promptCursor from being
                    // updated before the JTextArea is actually updated,
                    // this needs to be inside the Runnable.
                    _promptCursor += text.length();
                }
            };
        SwingUtilities.invokeLater(doAppendSh2);
    }
	
    private void _evalCommand () {
        String newtext = shell.getText().substring(_promptCursor);
        _promptCursor += newtext.length();
        if (_commandBuffer.length() > 0) {
            _commandBuffer.append("\n");
        }
        _commandBuffer.append(newtext);
        String command = _commandBuffer.toString();
        if (interpreter == null || command.equals("")) {
            appendShell("\n" + mainPrompt);
        } else {
            if (interpreter.isCommandComplete(command)) {
                System.out.println(command);
                // Process it
                appendShell("\n");
                Cursor oldCursor = shell.getCursor();
                shell.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                String result;
                try {
                    result = interpreter.evaluateCommand(command);
                    cmdHistory.add(command.replace('\n', ' '));
                    cmdIndex = cmdHistory.size();
                } catch (RuntimeException e) {
                    // RuntimeException are due to bugs in the expression
                    // evaluation code, so we make the stack trace available.
                    result = "Internal error evaluating expression.";
                    throw e;
                } catch (Exception e) {
                    result = e.getMessage();
                    // NOTE: Not ideal here to print the stack trace, but
                    // if we don't, it will be invisible, which makes
                    // debugging hard.
                    // e.printStackTrace();
                }
                if (result != null) {
                    if (result.trim().equals("")) {
                        appendShell(mainPrompt);
                    } else {
                        appendShell(result + "\n" + mainPrompt);
                        System.out.println(result);
                    }
                } else {
                    // Result is incomplete.
                    // Make the text uneditable to prevent further input
                    // until returnResult() is called.
                    // NOTE: We are assuming this called in the swing thread.
                    setEditable();
                }
                _commandBuffer.setLength(0);
                shell.setCursor(oldCursor);
            } else {
                appendShell("\n" + contPrompt);
            }
        }
    }//evsl cmd end
	
	 private class ShellKeyListener extends KeyAdapter {
	        public void keyTyped (KeyEvent keyEvent) {
	            switch (keyEvent.getKeyCode()) {
	            case KeyEvent.VK_UNDEFINED:
	                if (keyEvent.getKeyChar() == '\b') {
	                    if (shell.getCaretPosition() == _promptCursor) {
	                        keyEvent.consume(); // don't backspace over prompt!
	                    }
	                }
	                break;

	            case KeyEvent.VK_BACK_SPACE:
	                if (shell.getCaretPosition() == _promptCursor) {
	                    keyEvent.consume(); // don't backspace over prompt!
	                }
	                break;
	            default:
	            }
	        }

	        public void keyReleased (KeyEvent keyEvent) {
	            switch (keyEvent.getKeyCode()) {
	            case KeyEvent.VK_BACK_SPACE:
	                if (shell.getCaretPosition() == _promptCursor) {
	                    keyEvent.consume(); // don't backspace over prompt!
	                }
	                break;
	            default:
	            }
	        }

	        public void keyPressed (KeyEvent keyEvent) {
	            // Process keys
	            switch (keyEvent.getKeyCode()) {
	            case KeyEvent.VK_ENTER:
	                keyEvent.consume();
	                _evalCommand();
	                break;
	            case KeyEvent.VK_BACK_SPACE:
	                if (shell.getCaretPosition() <= _promptCursor) {
	                    // FIXME: Consuming the event is useless...
	                    // The backspace still occurs.  Why?  Java bug?
	                    keyEvent.consume(); // don't backspace over prompt!
	                }
	                break;
	            case KeyEvent.VK_LEFT:
	                if (shell.getCaretPosition() == _promptCursor) {
	                    keyEvent.consume();
	                }
	                break;
	            case KeyEvent.VK_UP:
	            	System.out.println(cmdHistory.size() + "idex: "+cmdIndex);
	                if(!cmdHistory.isEmpty())
	                {
	                	if(cmdIndex != 0)
	                		cmdIndex--;
	                	setLine(cmdHistory.get(cmdIndex));
	                }
	            	keyEvent.consume();
	                break;    
	            case KeyEvent.VK_DOWN:
	            	System.out.println(cmdHistory.size() + "idex: "+cmdIndex);
	                if(!cmdHistory.isEmpty())
	                {
	                	if(cmdIndex != cmdHistory.size()-1)
	                	{
	                		cmdIndex++;
	           	            setLine(cmdHistory.get(cmdIndex));
	                	}else
	                		setLine("");
	                	
	                }
	                keyEvent.consume();
	                break;  
	            case KeyEvent.VK_HOME:
	                shell.setCaretPosition(_promptCursor);
	                keyEvent.consume();
	                break;
	            default:
	                switch (keyEvent.getModifiers()) {
	                case InputEvent.CTRL_MASK:
	                    switch (keyEvent.getKeyCode()) {
	                    case KeyEvent.VK_A:
	                        shell.setCaretPosition(_promptCursor);
	                        keyEvent.consume();
	                        break;
	                    case KeyEvent.VK_M:
	                        initialize();
	                        keyEvent.consume();
	                        break;
	                    default:
	                    }
	                    break;
	                default:
	                    // Otherwise we got a regular character.
	                    // Don't consume it, and TextArea will
	                    // take care of displaying it.
	                }
	            }
	        }
	    }//shellkeylistenerend

}
