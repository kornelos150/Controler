package mainApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import bluethoothCtrl.BluetoothImp;
import jssc.SerialPortException;
import metro.BaseTile;
import metro.MainFrame;
import metro.WrapLayout;

public class InitFrame extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField textField;
	private JButton setBluethoothConnection;
	private JButton openMetro;
	private JButton openScript;
	private JLabel label;
	private GridLayout gridLayout;
	
	
	private final String BTCommand  = "Connect to BT";
	private final String metroCommand  = "Open Metro";
	private final String scripterCommand  = "Open Scripter";
	
	private metro.MainFrame METRO;
	private scripter.UI SCRIPTER;
	private bluethoothCtrl.IBluethooth BLUETOOTH;
	
	private final int WIDTH = 150;
	private final int HEIGHT = 228;
	
	public InitFrame() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textField = new JTextField("COM4");
		setBluethoothConnection = new JButton(BTCommand);
		openMetro = new JButton(metroCommand);
		openScript = new JButton(scripterCommand);
		label = new JLabel("Disconnected");
		getContentPane().setLayout(new BorderLayout());
		
		setBounds(100, 100, 150, 228);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		gridLayout = new GridLayout(5, 1, 0, 0);
		contentPane.setLayout(gridLayout);

		textField.setColumns(10);
		
		contentPane.add(textField);
		contentPane.add(setBluethoothConnection);
		contentPane.add(openMetro);
		contentPane.add(openScript);
		contentPane.add(label);
		
		openMetro.setEnabled(false);
		openScript.setEnabled(false);
		
		textField.addActionListener(this);
		setBluethoothConnection.addActionListener(this);
		openMetro.addActionListener(this);
		openScript.addActionListener(this);
		
		pack();
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals(BTCommand))
		{
			if(BLUETOOTH == null)
			{
				try {
				
				String port = textField.getText();
				BLUETOOTH = BluetoothImp.getInstance();
				label.setText("Connected");
				openMetro.setEnabled(true);
				openScript.setEnabled(true);
				
				} catch (SerialPortException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}//if end
				
		}//end BTCmd
		else if(cmd.equals(metroCommand))
		{
			if(METRO == null)
				METRO = new metro.MainFrame();
			METRO.setVisible(true);
		}
		else if(cmd.equals(scripterCommand))
		{
			if(SCRIPTER == null)
				SCRIPTER = new scripter.UI();
			SCRIPTER.setVisible(true);
		}
		
	}



	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					(new InitFrame()).setVisible(true);
					
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		
	}

}
