package metro;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import bluethoothCtrl.BluetoothMock;
import bluethoothCtrl.IBluethooth;

public class KeysControl extends BaseTile {

	private JPanel keysPanel;
	private JProgressBar progressBar;
	private JLabel lblMaxSpeed;
	private JLabel lblSetSpeedTranslation;
	private JPanel drawPanel;
	private JPanel infoPanel;
	
	
	public KeysControl(String name) {
		super(name);
		getContent().setLayout(null);
		
		componentInit();

	}
	
	private void componentInit()
	{
		keysPanel = new JPanel();
		keysPanel.setBounds(0, 0, 283, 188);
		getContent().add(keysPanel);
		keysPanel.setLayout(null);
		
		progressBar = new JProgressBar();
		progressBar.setValue(50);
		progressBar.setOrientation(SwingConstants.VERTICAL);
		progressBar.setBounds(256, 11, 17, 166);
		keysPanel.add(progressBar);
		
		lblMaxSpeed = new JLabel("Max speed: ");
		lblMaxSpeed.setBounds(134, 11, 112, 14);
		keysPanel.add(lblMaxSpeed);
		
		drawPanel = new DrawPanel();
		drawPanel.setBounds(10, 34, 236, 143);
		drawPanel.setBorder(new LineBorder(Color.BLACK));
		keysPanel.add(drawPanel);
		
		infoPanel = new JPanel();
		infoPanel.setBounds(0, 199, 283, 40);
		getContent().add(infoPanel);
		infoPanel.setLayout(null);
		
		lblSetSpeedTranslation = new JLabel("Set speed: Translation: Rotation: [cm/s]");
		lblSetSpeedTranslation.setBounds(10, 11, 263, 18);
		infoPanel.add(lblSetSpeedTranslation);
	}
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Info infotst = new Info();
					BaseTile testTile = new KeysControl("Testowy");//,infotst);
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
	
	class DrawPanel extends JPanel
	{
		private final int squareSide = 40;
		
		private Rectangle up;
		private Rectangle down;
		private Rectangle left;
		private Rectangle right;
		private Rectangle speedUp;
		private Rectangle speedDown;
		private Rectangle stop;
		
		public DrawPanel()
		{
			setLayout(null);
			JLabel lblSpeedUp = new JLabel("Speed Up");
			lblSpeedUp.setBounds(162, 24, 70, 14);
			add(lblSpeedUp);
			
			JLabel lblSpeedDown = new JLabel("Speed down");
			lblSpeedDown.setBounds(162, 88, 70, 14);
			add(lblSpeedDown);
			
			up = new Rectangle(68, 11, squareSide, squareSide);
			left = new Rectangle(27, 52, squareSide, squareSide);
			down = new Rectangle(68, 52, squareSide, squareSide);
			right = new Rectangle(109, 52, squareSide, squareSide);
			
			stop = new Rectangle(27, 103, 122, 29);
			speedUp = new Rectangle(159, 45, 67, 23);
			speedDown = new Rectangle(159, 109, 67, 23);
			repaint();
			
		}
		
		void drawww(Graphics2D g, Rectangle r)
		{
			g.setColor(Color.CYAN);
			g.fill(r);
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setFont(getFont().deriveFont(23.0f));
			super.paintComponent(g);
			g2.setColor(Color.red);
			g2.fill(up);
			
			drawww(g2, down);
			
			g2.setColor(Color.black);
			g2.drawString("UP", 10, 30);
		}
		
	}//class Draw Panel end
	
	class KeyListener extends KeyAdapter
	{

		@Override
		public void keyPressed(KeyEvent arg) {
			switch(arg.getKeyCode())
			{
			case KeyEvent.VK_UP:
				
			break;
			case KeyEvent.VK_DOWN:
				
			break;
			case KeyEvent.VK_LEFT:
				
			break;
			case KeyEvent.VK_RIGHT:
				
			break;
			case KeyEvent.VK_SPACE:
				
			break;
			case KeyEvent.VK_W:
				
			break;
			case KeyEvent.VK_S:
				
			break;
			}	
		}

		@Override
		public void keyReleased(KeyEvent arg) {
			switch(arg.getKeyCode())
			{
			case KeyEvent.VK_UP:
				
			break;
			case KeyEvent.VK_DOWN:
				
			break;
			case KeyEvent.VK_LEFT:
				
			break;
			case KeyEvent.VK_RIGHT:
				
			break;
			case KeyEvent.VK_SPACE:
				
			break;
			case KeyEvent.VK_W:
				
			break;
			case KeyEvent.VK_S:
				
			break;
			}	
		}
		
	}

}// clss KeysControl end
