package metro;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.peer.ButtonPeer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;

import bluethoothCtrl.BluetoothMock;
import bluethoothCtrl.IBluethooth;

public class ConfigTable extends BaseTile{

	private JTable table;
	JButton btnGetConfig;
	JButton btnSetConfig;
	JButton btnGetTimeout;
	JButton btnSetTimeout;
	JButton btnGetEncoder;
	JButton btnSetEncoder;
	JButton btnSetTimer;
	JPanel buttonPanel;
	ActionListener buttonListener;
	
	private final String getConfigLabel = "Get Config";
	private final String setConfigLabel = "Set Config";
	private final String getTimeoutLabel = "Get Timeout";
	private final String setTimeoutLabel = "Set Timeout";
	private final String getEncoderLabel = "Get Enc. Meas.";
	private final String setEncoderLabel = "Set Enc. Meas.";
	private final String setTimerLabel = "Set Timer";
	
	private final double initPval;
	private final double initDval;
	private final int initInterval;
	private final int initTimeout;
	private final int initEncodetTime;
	
	private IBluethooth bluetooth;
	private Timer timer;
	
	public ConfigTable(String name) {
		super(name);
		setType(Type.CONFIG);
		componentInit();
		initPval = 0.5;
		initDval = 0.5;
		initInterval = 500;
		initTimeout = 0;
		initEncodetTime = 60;
		table.setModel(new TableModel());
	}
	
	private void componentInit()
	{
		getContent().setLayout(null);
		
		table = new JTable();
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 11, 263, 132);
		table.setFillsViewportHeight(true);
		getContent().add(scrollPane);
		
		buttonListener = new UnivarsalListener();
		
		buttonPanel = new JPanel(new GridLayout(4,2));
		buttonPanel.setBounds(10, 154, 263, 85);
		getContent().add(buttonPanel);
		
		addButton(btnGetConfig, getConfigLabel);
		addButton(btnSetConfig, setConfigLabel);
		addButton(btnGetTimeout, getTimeoutLabel);
		addButton(btnSetTimeout, setTimeoutLabel);
		addButton(btnGetEncoder, getEncoderLabel);
		addButton(btnSetEncoder, setEncoderLabel);
		addButton(btnSetTimer, setTimerLabel);
	}
	
	private void addButton(JButton button,String name)
	{
		button = new JButton(name);
		buttonPanel.add(button);
	}
	
	private void addListener(){
		btnGetConfig.addActionListener(buttonListener);
		btnSetConfig.addActionListener(buttonListener);
		btnGetTimeout.addActionListener(buttonListener);
		btnSetTimeout.addActionListener(buttonListener);
		btnGetEncoder.addActionListener(buttonListener);
		btnSetEncoder.addActionListener(buttonListener);
		btnSetTimer.addActionListener(buttonListener);
	}
	
	private void removeListener()
	{
		btnGetConfig.removeActionListener(buttonListener);
		btnSetConfig.removeActionListener(buttonListener);
		btnGetTimeout.removeActionListener(buttonListener);
		btnSetTimeout.removeActionListener(buttonListener);
		btnGetEncoder.removeActionListener(buttonListener);
		btnSetEncoder.removeActionListener(buttonListener);
		btnSetTimer.removeActionListener(buttonListener);
	}

	@Override
	public void getBTControl(IBluethooth bluetooth) {
		this.bluetooth = bluetooth;
	}

	@Override
	public void getTimerControl(Timer timer) {
		this.timer = timer;
	}
	
	

	@Override
	public void activate() {
		addListener();
		super.activate();
	}

	@Override
	public void deactivate() {
		removeListener();
		super.deactivate();
	}

	private double[] setConfigPD()
	{
		if(table.isEditing())
			table.getCellEditor().stopCellEditing();
		double result[] = new double[2];
		result[0] = Double.parseDouble((String)table.getValueAt(0, 3));
		result[1] = Double.parseDouble((String)table.getValueAt(1, 3));
		getConfigPD(result[0], result[1]);
		return result;
	}
	private int setTimerInterval()
	{
		if(table.isEditing())
			table.getCellEditor().stopCellEditing();
		int res = Integer.parseInt((String)table.getValueAt(2, 3));
		table.setValueAt(res, 2, 2);
		return res;
	}
	
	private void getConfigPD(double Pval, double Dval)
	{
		table.setValueAt(Pval, 0, 2);
		table.setValueAt(Dval, 1, 2);
		
	}

	class TableModel extends AbstractTableModel
	{
		private final String[] columnNames = {"Parameter", "Component", "Cur. Value", "To Set Val." };
		private Object[][] data = { { "P", "Robot", initPval, initPval },
				{ "D", "Robot", initDval, initDval },
				{ "Timer", "Application", initInterval, initInterval },
				{ "Timeout", "Robot", initTimeout, initTimeout },
				{ "Encoder", "Robot", initEncodetTime, initEncodetTime }};

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}
		
		@Override
		public String getColumnName(int arg0) {
			return columnNames[arg0];
		}

		@Override
		public int getRowCount() {
			return data.length;
		}

		@Override
		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return (columnIndex == (columnNames.length-1));
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			data[rowIndex][columnIndex] = aValue;
			fireTableCellUpdated(rowIndex, columnIndex);
		}
		
		
	}
	
	public class UnivarsalListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.equals(getConfigLabel))
			{
				
			}else if(e.equals(setConfigLabel))
			{
				
			}else if(e.equals(getTimeoutLabel))
			{
				
			}else if(e.equals(setTimeoutLabel))
			{
				
			}else if(e.equals(getEncoderLabel))
			{
				
			}else if(e.equals(setEncoderLabel))
			{
				
			}else if(e.equals(setTimerLabel))
			{
				
			}
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Info infotst = new Info();
					BaseTile testTile = new ConfigTable("Testowy");//,infotst);
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
