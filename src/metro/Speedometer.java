package metro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;

import bluethoothCtrl.BluetoothMock;
import bluethoothCtrl.IBluethooth;

public class Speedometer extends BaseTile{

	private static int TRANS = 0;
	private static int ROT = 1;
	private static int RIGHT = 0;
	private static int LEFT = 1;
	
	private JPanel panel;
	private JPanel panelButtons;
	private JPanel unitPanel;
	private SpeedPanel speedPanel;
	private JLabel lblSpeedUnit;
	private JLabel lblForm;
	private JLabel lblShowUnit;
	private JRadioButton rdbtnCmh;
	private JRadioButton rdbtnMs;
	private JRadioButton rdbtnKmh;
	private ButtonGroup unitGroup;
	private JRadioButton rdbtnTR;
	private JRadioButton rdbtnRL;
	private ButtonGroup formGroup;
	
	
	private double velocity[];
	private String formText[];
	
	private double multiplier = 1.0;
	
	@Override
	public double[] update(double[] input) {
		unitChanger(input);
		formChanger(input);
		velocity = input;
		speedPanel.repaint();
		return input;
	}



	public Speedometer(String name) {
		super(name);
		setType(Type.SPEEDVISIO);
		getContent().setLayout(null);

		speedPanelInit();
		buttonPanelInit();
		
		velocity = new double[2];
		velocity[TRANS] = 0.0;
		velocity[ROT] = 0.0;
		
		formText = new String[2];
		formText[TRANS] = "Trans.";
		formText[ROT] = "Ratat.";
		
		speedPanel.repaint();
		
	}
	
	private void unitChanger(double[] vel)
	{
		vel[0] = multiplier * vel[0];
		vel[1] = multiplier * vel[1];

	}
	
	private void formChanger(double[] vel)
	{
		//TO DO

	}
	private void buttonPanelInit()
	{
		panelButtons = new JPanel();
		panelButtons.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		panelButtons.setBounds(196, 11, 77, 228);
		getContent().add(panelButtons);
		panelButtons.setLayout(null);
		
		lblSpeedUnit = new JLabel("Speed Unit");
		lblSpeedUnit.setBounds(10, 11, 67, 14);
		panelButtons.add(lblSpeedUnit);
		
		ActionListener unitListener = new UnitListener();
		
		rdbtnCmh = new JRadioButton("cm/s");
		rdbtnCmh.setBounds(6, 37, 65, 23);
		rdbtnCmh.setSelected(true);
		rdbtnCmh.addActionListener(unitListener);
		panelButtons.add(rdbtnCmh);
		
		rdbtnMs = new JRadioButton("m/s");
		rdbtnMs.setBounds(6, 63, 65, 23);
		rdbtnMs.addActionListener(unitListener);
		panelButtons.add(rdbtnMs);
		
		rdbtnKmh = new JRadioButton("km/h");
		rdbtnKmh.setBounds(6, 89, 65, 23);
		rdbtnKmh.addActionListener(unitListener);
		panelButtons.add(rdbtnKmh);
		
		unitGroup = new ButtonGroup();
		unitGroup.add(rdbtnCmh);
		unitGroup.add(rdbtnKmh);
		unitGroup.add(rdbtnMs);
		
		lblForm = new JLabel("Form");
		lblForm.setBounds(10, 130, 67, 14);
		panelButtons.add(lblForm);
		
		rdbtnTR = new JRadioButton("T/R");
		rdbtnTR.setBounds(6, 156, 65, 23);
		rdbtnTR.setSelected(true);
		panelButtons.add(rdbtnTR);
		
		rdbtnRL = new JRadioButton("L/R");
		rdbtnRL.setBounds(6, 182, 65, 23);
		panelButtons.add(rdbtnRL);
		
		formGroup = new ButtonGroup();
		formGroup.add(rdbtnTR);
		formGroup.add(rdbtnRL);
	}
	
	private void speedPanelInit()
	{
		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		panel.setBounds(10, 11, 176, 228);
		panel.setLayout(null);
		getContent().add(panel);
		
		unitPanel = new JPanel();
		unitPanel.setBounds(60, 178, 60, 39);
		unitPanel.setLayout(new BorderLayout());
		panel.add(unitPanel);
		
		lblShowUnit = new JLabel("<html><h1><center>cm/s</center></h1></html>");
		unitPanel.add(lblShowUnit,BorderLayout.CENTER);
		
		speedPanel = new SpeedPanel();
		panel.add(speedPanel);
	}
	
	private void  setShowUnit(String unitText)
	{
		lblShowUnit.setText(String.format("<html><h1><center>%s</center></h1></html>", unitText));
	}
	
	public void drawCenteredString(String s, int w, int h, Graphics g, int Yoffset) {
	    FontMetrics fm = g.getFontMetrics();
	    int x = (w - fm.stringWidth(s)) / 2;
	    int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2) + Yoffset;
	    g.drawString(s, x, y);
	  }
	
	
	public class SpeedPanel extends JPanel
	{
		private int middle;
		private final int thickness = 10;
		private Color defaultColor = getBackground();
		
		private Font font = new Font("Courier New", Font.BOLD, 20);
		
		public void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
			  x = x-(r/2);
			  y = y-(r/2);
			  g.fillOval(x,y,r,r);
			}
		
		public SpeedPanel()
		{
			setBounds(10, 11, 156, 156);
			middle = getWidth()/2;
			
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D)g;
			
			g.setColor(Color.GRAY);
			drawCenteredCircle(g2, middle, middle, 2*middle);
			g.setColor(Color.white);
			drawCenteredCircle(g2, middle, middle, 2*middle-thickness);
			g.setFont(font);
			g.setColor(Color.BLACK);
			
			drawCenteredString(formText[0], getWidth(), getHeight(), g2, -40);
			drawCenteredString(Double.toString(velocity[0]), getWidth(), getHeight(), g2, -20);
			drawCenteredString(formText[1], getWidth(), getHeight(), g2, 20);
			drawCenteredString(Double.toString(velocity[1]), getWidth(), getHeight(), g2, 40);

		}
		
		
	}
	

	private class UnitListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(rdbtnCmh.isSelected())
			{
				multiplier = 1.0;
				setShowUnit(rdbtnCmh.getText());
				
			}else if(rdbtnKmh.isSelected())
			{
				multiplier = 0.036;
				setShowUnit(rdbtnKmh.getText());
				
			}else if(rdbtnMs.isSelected())
			{
				multiplier = 0.01;
				setShowUnit(rdbtnMs.getText());
			}	
			unitChanger(velocity);
			speedPanel.repaint();
		}
		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Info infotst = new Info();
					BaseTile testTile = new Speedometer("Testowy");//,infotst);
					System.out.println("SIZE: "+ testTile.getContent().getSize());
					JFrame testFrame = new JFrame();
					int side = BaseTile.getSide();
					testFrame.setMinimumSize(new Dimension(side + 30, side + 50));
					testFrame.setLayout(new WrapLayout());
					testFrame.add(testTile);
					testFrame.pack();
					
					testFrame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});

	}

}
