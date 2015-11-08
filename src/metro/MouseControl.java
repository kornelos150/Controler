package metro;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import bluethoothCtrl.BluetoothMock;
import bluethoothCtrl.IBluethooth;

public class MouseControl extends BaseTile {

	private JTextField textField;
	private JLabel lblInitText;
	private JLabel lblPrescalerpx;
	private JLabel lblCms;
	private JButton btnNewButton;
	private JPanel controlPanel;
	
	private final String initMessage = "Choose Starting Point by left clicking on area";
	private final String setMessage = "Initial point chosen";
	private final String inactiveMessage = "Activate component first, in order to control";
	
	private Boolean isPointSetted = false;
	private Point startPoint = new Point(0, 0); 
	private JLabel lblSetvelocity;
	private double prescaler = 0.01;
	
	double translation;
	double rotation;
	
	
	
	@Override
	public double[] update(double[] input) {
		double [] result = new double[2];
		result[0] = this.translation;
		result[1] = this.rotation;
		return result;
	}

	public MouseControl(String name) {
		super(name);

		setType(Type.CONTROL);
		getContent().setLayout(null);
		initComponents();
	}

void initComponents(){
	lblInitText = new JLabel(initMessage);
	lblInitText.setBounds(10, 2, 263, 14);
	getContent().add(lblInitText);
	
	lblPrescalerpx = new JLabel("Prescaler 1px = ");
	lblPrescalerpx.setBounds(10, 243-17, 104, 14);
	getContent().add(lblPrescalerpx);
	
	
	
	textField = new JTextField();
	textField.setBounds(114, 240-17, 37, 20);
	textField.setText(Double.toString(prescaler));
	getContent().add(textField);
	textField.setColumns(10);
	
	lblCms = new JLabel("cm /s");
	lblCms.setBounds(161, 243-17, 46, 14);
	getContent().add(lblCms);
	
	btnNewButton = new JButton("Apply");
	btnNewButton.setBounds(194, 239-17, 79, 23);
	getContent().add(btnNewButton);
	
	controlPanel = new JPanel();
	controlPanel.setBorder(new LineBorder(Color.RED, 2, true));
	controlPanel.setBounds(10, 23, 263, 209-17);
	getContent().add(controlPanel);
	controlPanel.setLayout(null);
	
	lblSetvelocity = new JLabel("T: 1.34 R: 2.45");
	lblSetvelocity.setBounds(65, 47, 87, 14);
	controlPanel.add(lblSetvelocity);
	lblSetvelocity.setVisible(false);
	
	addMouseListeners();
	addApplyBtnListener();
}

private void addMouseListeners()
{
	DrawingPanelListener listener = new DrawingPanelListener();
	controlPanel.addMouseListener(listener);
	controlPanel.addMouseMotionListener(listener);
}

private void addApplyBtnListener()
{
	btnNewButton.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try{
			double tmp = Double.parseDouble(textField.getText());
			prescaler = tmp;
			}catch(NumberFormatException e1)
			{
				e1.printStackTrace();
				System.out.println("Wrong value for prescaler");
			}				
		}
	});
}



@Override
public void activate() {
	// TODO Auto-generated method stub
	super.activate();
}

@Override
public void deactivate() {
	translation = 0.0;
	rotation = 0.0;
	super.deactivate();
}

private double roundTo2Dec(double val)
{
	val = val*100;
	val = Math.round(val);
	return (val /100);
	
}

private void setSpeed(double[] speed)
{
	this.translation = roundTo2Dec(speed[0]);
	this.rotation = roundTo2Dec(speed[1]);
}

class CtrlTask extends TimerTask
{

	@Override
	public void run() {
		
		
	}
	
}


class DrawingPanelListener extends MouseAdapter
{
	Color defaulColor = controlPanel.getBackground();
	
	private double[] calculateSpeed(Point e)
	{
		double [] res = new double[2];
		res[0] = (double)(startPoint.y - e.y) * prescaler;
		res[1] = (double)(e.x - startPoint.x) * prescaler;
		return res;
	}
	
	private void setVelocityLabel(double[] speed, Point e)
	{
		lblSetvelocity.setLocation(e.x, e.y - 14);
		lblSetvelocity.setText(String.format("T:%.2f R:%.2f", speed[0],speed[1]));
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(!isPointSetted)
		{
			if(getActiveFlag())
			{
				startPoint = e.getPoint();
				isPointSetted = true;
				lblSetvelocity.setVisible(true);
				lblInitText.setText(setMessage);
				double[] speed = calculateSpeed(e.getPoint());
				setSpeed(speed);
				setVelocityLabel(speed, e.getPoint());
			}else
			{
				lblInitText.setText(inactiveMessage);
			}
			
		}else
		{
			isPointSetted = false;
			lblSetvelocity.setVisible(false);
			double[] speed = {0.0, 0.0};
			setSpeed(speed);
			lblInitText.setText(initMessage);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		controlPanel.setBackground(Color.WHITE);
	}
	

	@Override
	public void mouseExited(MouseEvent e) {
		controlPanel.setBackground(defaulColor);
		translation = 0.0;
		rotation = 0.0;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(isPointSetted)
		{
			double[] speed = calculateSpeed(e.getPoint());
			setSpeed(speed);
			setVelocityLabel(speed,e.getPoint());
		}
	}
	
}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Info infotst = new Info();
					Timer testTimer = new Timer();
					BaseTile testTile = new MouseControl("Testowy");
					testTile.activate();
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
