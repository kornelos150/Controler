package metro;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import bluethoothCtrl.BluetoothImp;
import bluethoothCtrl.BluetoothMock;
import bluethoothCtrl.IBluethooth;

public class MainFrame extends JFrame {
	
	private JMenuBar menuBar;
	private JMenu compMenu;
	private JMenu fileMenu;
	private JMenuItem addAll;
	private JMenuItem removeAll;
	
	private JMenuItem load;
	private JMenuItem save;
	private JMenuItem saveAs;
	private JMenuItem scriptAcivator;
	
	private BaseTile activeCtrlConf;
	
	private BTHandler btHandler;
	
	private class ComponentPair{
		public BaseTile component;
		public JCheckBoxMenuItem menuItem;
		
		public ComponentPair(BaseTile component)
		{
			this.component = component;
			menuItem = new JCheckBoxMenuItem(component.getName());
			MainFrame.this.compMenu.add(menuItem);
			
			menuItem.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(menuItem.isSelected())
						MainFrame.this.add(component);
					else
					{
						MainFrame.this.remove(component);
						btHandler.deactiveTile(component);
					}
					
					MainFrame.this.pack();
				}
			});
			
			this.component.setCloseListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					MainFrame.this.remove(component);
					btHandler.deactiveTile(component);
					MainFrame.this.pack();
					menuItem.setSelected(false);
				}
			});
			
			this.component.setActiveListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(component.getActiveFlag())
						btHandler.deactiveTile(component);
					else
						btHandler.activeTile(component);
				}
			});
		}
		
		
		
	}
	private ArrayList<ComponentPair> components = new ArrayList<>();
	
	public void addComponents()
	{
		addTile(new MouseControl("Mouse Controller"));
		addTile(new ConfigTable("Configuration"));
		addTile(new Speedometer("Speedometer"));
		
		addRemoveAllInit();
	}
		
	public MainFrame(){
		
		setLayout();
		menuInit();
		setWindowsListeners();
		btHandler = new BTHandler();
		
		addComponents();
			
	}
	
	private void setWindowsListeners()
	{
		WindowAdapter windowListener = new CustomWindowListener();
		addWindowListener(windowListener);
		addWindowFocusListener(windowListener);
	}
	
	private void setLayout()
	{
		setMinimumSize(new Dimension(BaseTile.getSide() + 30, BaseTile.getSide() + 50));
		setLayout(new WrapLayout());
		pack();
	}
	
	private void menuInit()
	{
		menuBar = new JMenuBar();
			
		compMenu = new JMenu("Components");		
		menuBar.add(compMenu);
		
		menuBar.setVisible(true);
		setJMenuBar(menuBar);
	}
	
	private void addRemoveAllInit()
	{
		compMenu.addSeparator();
		addAll = new JMenuItem("Add all");
		removeAll = new JMenuItem("Remove all");
		compMenu.add(addAll);
		compMenu.add(removeAll);
		addAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(ComponentPair item: components)
				{
					item.menuItem.setSelected(true);
					MainFrame.this.add(item.component);
				}
				MainFrame.this.pack();
			}
		});
		removeAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(ComponentPair item: components)
				{
					item.menuItem.setSelected(false);
					MainFrame.this.remove(item.component);
					btHandler.deactiveTile(item.component);
				}
				MainFrame.this.pack();
				
			}
		});
	}//addremove finish
	
	private ArrayList<BaseTile> getActiveComponents()
	{
		ArrayList<BaseTile> activeComponents = new ArrayList<>();
		for(ComponentPair item: components)
			if(item.component.getActiveFlag())
				activeComponents.add(item.component);
		return activeComponents;
	}
	
	
	public void addTile(BaseTile tile)
	{
		components.add(new ComponentPair(tile));
	}
	
	private class CustomWindowListener extends WindowAdapter
	{

		@Override
		public void windowClosing(WindowEvent e) {
			btHandler.deactivateAll(getActiveComponents());
			btHandler.hardTimerStop();
			MainFrame.this.setVisible(false);
		}

		@Override
		public void windowLostFocus(WindowEvent arg0) {
			btHandler.deactivateAll(getActiveComponents());
			btHandler.hardTimerStop();
			super.windowLostFocus(arg0);
		}
		
		
		
		
		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame testFrame = new MainFrame();

					
					testFrame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		
	}

}
