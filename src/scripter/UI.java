package scripter;

// GUI
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.*;
// Input Stream
import java.io.*;
// Various
import java.util.Scanner;
import javax.swing.border.Border;
import javax.swing.plaf.TabbedPaneUI;

public class UI extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	public final static String NAME = "TextEditor";
	
	private JavaScriptInterpreter interpreter;
	
	private JTabbedPane tabbedPane;
	
	private JTextArea scriptInput;
	private JTextArea scriptOutput;
	private ScriptPanel scriptPanel;
	private InterpreterPanel interpreterPanel;
	
	private JMenuBar menuBar;
	private JMenu menuFile, menuEdit, menuFind;
	private JMenuItem newFile, openFile, saveFile, close, clearInput,
						clearOutput,quickFind,run;
	private JToolBar mainToolbar;
    private JButton newButton, openButton, saveButton, clearButton, quickButton, closeButton, runButton, spaceButton;
        
    private final ImageIcon newIcon = new ImageIcon("resource/new.png");
    private final ImageIcon openIcon = new ImageIcon("resource/open.png");
    private final ImageIcon saveIcon = new ImageIcon("resource/save.png");
    private final ImageIcon closeIcon = new ImageIcon("resource/close.png");
    private final ImageIcon clearIcon = new ImageIcon("resource/clear.png");
    private final ImageIcon searchIcon = new ImageIcon("resource/search.png");
    private final ImageIcon runIcon = new ImageIcon("resource/run.png");
    
    private RunScriptListener runScriptListener;
    
    private void menuInit()
    {
		menuFile = new JMenu("File");
		menuEdit = new JMenu("Edit");
		menuFind = new JMenu("Search");
		
		newFile = new JMenuItem("Load..");
		openFile = new JMenuItem("Open..");
		saveFile = new JMenuItem("Save");
		close = new JMenuItem("Close");
		clearInput = new JMenuItem("Clear Input");
		clearOutput = new JMenuItem("Clear Output");
		quickFind = new JMenuItem("Quick..");
		run = new JMenuItem("Run script");
		
		menuBar = new JMenuBar();
		menuBar.add(menuFile); 
		menuBar.add(menuEdit);
		menuBar.add(menuFind);

        this.setJMenuBar(menuBar);
		
		newFile.addActionListener(this);  // Adding an action listener (so we know when it's been clicked).
		newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK)); // Set a keyboard shortcut
		menuFile.add(newFile); // Adding the file menu
		
		openFile.addActionListener(this);
		openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK)); 
		menuFile.add(openFile); 
		
		saveFile.addActionListener(this);
		saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		menuFile.add(saveFile);
		
		menuFile.addSeparator();
		
		run.addActionListener(runScriptListener);
		run.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		menuFile.add(run);
		
		menuFile.addSeparator();
		
		close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_MASK));
		close.addActionListener(this);
		menuFile.add(close);

		clearInput.addActionListener(this);
		clearInput.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK)); 
		menuEdit.add(clearInput);
		
		clearOutput.addActionListener(this);
		clearOutput.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK)); 
		menuEdit.add(clearOutput);
		
		quickFind.addActionListener(this);
		quickFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		menuFind.add(quickFind);
    }
     
    private void toolBarInit()
    {
    	mainToolbar = new JToolBar();
        this.add(mainToolbar, BorderLayout.NORTH);
        //used to create space between button groups
        Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 50);
        
        newButton = new JButton(newIcon);
        newButton.setToolTipText("New");
        newButton.addActionListener(this);
        mainToolbar.add(newButton);
        mainToolbar.addSeparator();
        
        openButton = new JButton(openIcon);
        openButton.setToolTipText("Open");
        openButton.addActionListener(this);
        mainToolbar.add(openButton);
        mainToolbar.addSeparator();
        
        saveButton = new JButton(saveIcon);
        saveButton.setToolTipText("Save");
        saveButton.addActionListener(this);
        mainToolbar.add(saveButton);
        mainToolbar.addSeparator();
        
        clearButton = new JButton(clearIcon);
        clearButton.setToolTipText("Clear All");
        clearButton.addActionListener(this);
        mainToolbar.add(clearButton);
        mainToolbar.addSeparator();
        
        quickButton = new JButton(searchIcon);
        quickButton.setToolTipText("Quick Search");
        quickButton.addActionListener(this);
        mainToolbar.add(quickButton);
              
        //create space between button groups
        spaceButton = new JButton();
        spaceButton.setBorder(emptyBorder);
        mainToolbar.add(spaceButton);
        
        runButton = new JButton(runIcon);
        runButton.setToolTipText("Run Script");
        runButton.addActionListener(runScriptListener);
        mainToolbar.add(runButton);
        mainToolbar.addSeparator();
        
        closeButton = new JButton(closeIcon);
        closeButton.setToolTipText("Close");
        closeButton.addActionListener(this);
        mainToolbar.add(closeButton);
    }
	
    private void basicInit()
    {
		setSize(700, 500); 
		setTitle("Undefined | " + NAME);
    }
    
    private void scripterPanelInit()
    {
    	scriptInput = new JTextArea("", 0,0);
		scriptInput.setFont(new Font("Century Gothic", Font.BOLD, 12)); 
		scriptOutput = new JTextArea("Init message\n");
		
		scriptPanel = new ScriptPanel(scriptInput, scriptOutput);
		interpreterPanel = new InterpreterPanel(interpreter);
    }

    public UI() {	 
		
    	interpreter = new JavaScriptInterpreter();
    	runScriptListener = new RunScriptListener();
    	
    	getContentPane();
		menuInit();
		basicInit();
		scripterPanelInit();
		interpreter.setOutputScript(scriptOutput);
		
		tabbedPane = new JTabbedPane();
		//tabbedPane = new 
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.add("Interpreter", interpreterPanel);
		tabbedPane.add("Scripting", scriptPanel);
		getContentPane().setLayout(new BorderLayout()); // the BorderLayout bit makes it fill it automatically
		getContentPane().add(tabbedPane);
		
		toolBarInit();		
	}

	public static void main(String[] args) {
        new UI().setVisible(true);
	}
	
	public void actionPerformed (ActionEvent e) {
		// If the source of the event was our "close" option
		if(e.getSource() == close || e.getSource() == closeButton)
			this.dispose(); // dispose all resources and close the application
		
		// If the source was the "new" file option
		else if(e.getSource() == newFile || e.getSource() == newButton) {
			Commands.clear(scriptInput);
		}
		// If the source was the "open" option
		else if(e.getSource() == openFile || e.getSource() == openButton) {
			JFileChooser open = new JFileChooser(); // open up a file chooser (a dialog for the user to browse files to open)
			int option = open.showOpenDialog(this); // get the option that the user selected (approve or cancel)
			
			/*
			 * NOTE: because we are OPENing a file, we call showOpenDialog~
			 * if the user clicked OK, we have "APPROVE_OPTION"
			 * so we want to open the file
			 */
			if(option == JFileChooser.APPROVE_OPTION) {
				Commands.clear(scriptInput); // clear the TextArea before applying the file contents
				try {
					// create a scanner to read the file (getSelectedFile().getPath() will get the path to the file)
					Scanner scan = new Scanner(new FileReader(open.getSelectedFile().getPath()));
					while (scan.hasNext()) // while there's still something to read
						scriptInput.append(scan.nextLine() + "\n"); // append the line to the TextArea
				} catch (Exception ex) { // catch any exceptions, and...
					// ...write to the debug console
					System.out.println(ex.getMessage());
				}
			}
		}
		// If the source of the event was the "save" option
		else if(e.getSource() == saveFile || e.getSource() == saveButton) {
			// Open a file chooser
			JFileChooser fileChoose = new JFileChooser(); 
			// Open the file, only this time we call
			int option = fileChoose.showSaveDialog(this); 
			
			/*
			 * ShowSaveDialog instead of showOpenDialog
			 * if the user clicked OK (and not cancel)
			 */
			if(option == JFileChooser.APPROVE_OPTION) {
				try {
					File file = fileChoose.getSelectedFile();
					// Set the new title of the window
					setTitle(file.getName() + " | " + NAME);
					// Create a buffered writer to write to a file
					BufferedWriter out = new BufferedWriter(new FileWriter(file.getPath()));
					// Write the contents of the TextArea to the file
					out.write(scriptInput.getText()); 
					// Close the file stream
					out.close(); 
				} catch (Exception ex) { // again, catch any exceptions and...
					// ...write to the debug console
					System.out.println(ex.getMessage());
				}
			}
		}
		
		// Clear File (Code)
		if(e.getSource() == clearInput) {
			Commands.clear(scriptInput);
		}
		
		if(e.getSource() == clearOutput || e.getSource() == clearButton) {
			Commands.clear(scriptOutput);
		}
		// Find 
		if(e.getSource() == quickFind || e.getSource() == quickButton) {
			new CommandFind(scriptInput);
		}	

		
	}
		


	class RunScriptListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			try {
				interpreter.runStript(scriptInput.getText(), false);
			} catch (Exception e1) {
				scriptOutput.setText("Error occured");
				e1.printStackTrace();
			}
			
		}
		
	}
}