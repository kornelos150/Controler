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

	private JTextField textFieldTrans;
	private JTextField textFieldRot;
	private JLabel lblInitText;
	private JLabel lblPrescaleTrans;
	private JLabel lblPrescaleRot;
	private JLabel lblCmsTrans;
	private JLabel lblCmsRot;
	private JButton btnNewButtonTrans;
	private JButton btnNewButtonRot;
	private JPanel controlPanel;
	
	private final String initMessage = "Choose Starting Point by left clicking on area";
	private final String setMessage = "Initial point chosen";
	private final String inactiveMessage = "Activate component first, in order to control";
	
	private Boolean isPointSetted = false;
	private Point startPoint = new Point(0, 0); 
	private JLabel lblSetvelocity;
	private double prescalerTrans = 0.01;
	private double prescalerRot = 0.01;
	
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
	
	lblPrescaleTrans = new JLabel("Prescaler TR 1px = ");
	lblPrescaleTrans.setBounds(0, 243-41, 115, 14);
	getContent().add(lblPrescaleTrans);
	
	lblCmsRot = new JLabel("Prescaler RO 1px = ");
	lblCmsRot.setBounds(0, 243-17, 115, 14);
	getContent().add(lblCmsRot);
	
	textFieldTrans = new JTextField();
	textFieldTrans.setBounds(116, 243-43, 37, 20);
	textFieldTrans.setText(Double.toString(prescalerTrans));
	getContent().add(textFieldTrans);
	textFieldTrans.setColumns(10);
	
	lblCmsTrans = new JLabel("cm /s");
	lblCmsTrans.setBounds(161, 243-43, 46, 14);
	getContent().add(lblCmsTrans);
	
	btnNewButtonTrans = new JButton("Apply");
	btnNewButtonTrans.setBounds(194, 243-43, 79, 23);
	getContent().add(btnNewButtonTrans);
	
	textFieldRot = new JTextField();
	textFieldRot.setBounds(117, 243-17, 37, 20);
	textFieldRot.setText(Double.toString(prescalerRot));
	getContent().add(textFieldRot);
	textFieldRot.setColumns(10);
	
	lblCmsRot = new JLabel("cm /s");
	lblCmsRot.setBounds(161, 243-17, 46, 14);
	getContent().add(lblCmsRot);
	
	btnNewButtonRot = new JButton("Apply");
	btnNewButtonRot.setBounds(194, 243-17, 79, 23);
	getContent().add(btnNewButtonRot);
	
	controlPanel = new JPanel();
	controlPanel.setBorder(new LineBorder(Color.RED, 2, true));
	controlPanel.setBounds(10, 23, 263, 205-31);
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
	btnNewButtonTrans.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try{
			double tmp = Double.parseDouble(textFieldTrans.getText());
			prescalerTrans = tmp;
			}catch(NumberFormatException e1)
			{
				e1.printStackTrace();
				System.out.println("Wrong value for prescaler");
			}				
		}
	});
	
	btnNewButtonRot.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try{
			double tmp = Double.parseDouble(textFieldRot.getText());
			prescalerRot = tmp;
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
		res[0] = (double)(startPoint.y - e.y) * prescalerTrans;
		res[1] = (double)(e.x - startPoint.x) * prescalerRot;
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
