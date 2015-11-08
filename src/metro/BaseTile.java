package metro;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import bluethoothCtrl.BluetoothMock;
import bluethoothCtrl.IBluethooth;

public class BaseTile extends JPanel implements BTObserver{
	
	public enum Type{
		CONTROL, PWM_CONTROL, SPEEDVISIO, SENSORVISIO, OTHER, CONFIG;
	}
	

	private static int side = 300;
	private static Dimension dimension = new Dimension(side, side);
	private static int uppperBarHeight = 25;
	
	private JPanel upperBar;
	private JPanel content;
	private JButton activeButton;
	private JButton exitButton;
	
	private Type type;
	private String name;

	
	private Boolean presentFlag;
	private Boolean activeFlag;
	
	public static Dimension getDimension()
	{
		return dimension;
	}
	
	public static int getSide()
	{
		return side;
	}
	
	public BaseTile(String name)
	{
		this.name = name;
		this.type = Type.OTHER;
		
		setBorder(name);
		setSize();
		setPanels(uppperBarHeight);
		
		activeFlag = false;
		presentFlag = false;
		
		
	}
	
	@Override
	public double[] update(double[] input) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCloseListener(ActionListener closeListener) {
		
		exitButton.addActionListener(closeListener);
		
	}
	
	public void setActiveListener(ActionListener activeListener) {
		
		activeButton.addActionListener(activeListener);
	}
	@Override
	public void activate()
	{
		activeFlag = true;
		activeButton.setBackground(Color.GREEN);
		activeButton.setText("ACTIVE");
	}
	@Override
	public void deactivate()
	{
		activeFlag = false;
		activeButton.setBackground(Color.RED);
		activeButton.setText("DEACTIVE");
	}
	
	private void setBorder(String name)
	{
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		Border fullBorder = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
		setBorder(new TitledBorder(fullBorder, name));
		
	}
	
	private void setSize()
	{
		setSize(getDimension());
		setPreferredSize(getDimension());
		setMaximumSize(getDimension());
		setMinimumSize(getDimension());
	}
	
	private void setPanels(int upperBarHeight)
	{
		setLayout(null);
		upperBar = new JPanel();
		upperBar.setBounds(10, 15, side - 17, upperBarHeight);
		upperBar.setLayout(null);
		upperBar.setBorder(new EtchedBorder());
		add(upperBar);
		
		activeButton = new JButton("DEACTIVE");
		activeButton.setBackground(Color.RED);
		activeButton.setBounds(2, 4, 80, upperBarHeight-8);
		Font toResize = activeButton.getFont().deriveFont(7.0f);
		activeButton.setFont(toResize);
		upperBar.add(activeButton);
		
		exitButton = new JButton();
		Image img = new ImageIcon("resource\\exit.png").getImage() ;  
		Image newimg = img.getScaledInstance( 21, 21, Image.SCALE_SMOOTH ) ;  
		exitButton.setIcon(new ImageIcon( newimg ));
		exitButton.setBounds(260, 4, 17, 17);
		upperBar.add(exitButton);
		
		content = new JPanel();
		content.setBounds(10, upperBarHeight + 17, side - 17, side - upperBarHeight - 25);
		add(content);
	}
	
	
	
	public JPanel getContent() {
		return content;
	}

	public void setContent(JPanel content) {
		this.content = content;
	}

	public Boolean isPresent() {
		return presentFlag;
	}

	public void setPresence(Boolean isPresent) {
		this.presentFlag = isPresent;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public Boolean isCtrlConfType()
	{
		return ( (type == Type.CONTROL) || (type == Type.CONFIG) );
	}
	
	public Boolean isCtrl()
	{
		return (type == Type.CONTROL);
	}
	
	public Boolean isConfig()
	{
		return (type == Type.CONFIG);
	}

	public Boolean getActiveFlag() {
		return activeFlag;
	}


	public static void main(String[] args) {
		
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + s);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Info infotst = new Info();
					BaseTile testTile = new BaseTile("Testowy");//,infotst);
					testTile.upperBar.setVisible(true);
					testTile.getContent().setBackground(Color.GREEN);
					BaseTile testTile2 = new BaseTile("Testowy2");//,infotst);
					BaseTile testTile3 = new BaseTile("Testowy3");//,infotst);
					JFrame testFrame = new JFrame();
					testFrame.setMinimumSize(new Dimension(side + 30, side + 50));
					testFrame.setLayout(new WrapLayout());
					testFrame.add(testTile);
					testFrame.add(testTile2);
					testFrame.add(testTile3);
					testFrame.pack();
					
					testFrame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
	
	}// main end

}
