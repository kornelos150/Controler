package scripter;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import metro.MainFrame;

public class ScriptPanel extends JPanel {

	private JTextArea input;
	private JTextArea output;
	private JSplitPane pane;
	
	
	public ScriptPanel(JTextArea input, JTextArea output) {
		super(new BorderLayout());
		//input = new JTextArea("", 20, 120);
		this.input = input;
        JScrollPane upperPane = new JScrollPane(input);
       // output = new JTextArea("", 20, 40);
        this.output = output;
        JScrollPane lowerPane = new JScrollPane(output);
        pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperPane, lowerPane);
        pane.setDividerLocation(0.5);    
        add(pane, BorderLayout.CENTER);
		
        
		output.setEditable(false);
	}
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame testFrame = new JFrame();
					ScriptPanel test = new ScriptPanel(new JTextArea("", 20, 120),new JTextArea("", 20, 40));
					testFrame.add(test);
					
					testFrame.pack();
					testFrame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		
	}

}
