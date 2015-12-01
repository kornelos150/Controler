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
import jssc.SerialPortException;
import mainApp.GeneralConverter;

public class ConfigTable extends BaseTile{

	private JTable table;
	private JButton btnGetAll;
	private JButton btnSetConfig;
	private JButton btnSetTimeout;
	private JButton btnSetEncoder;
	private JButton btnSetTimer;
	private JButton btnSetRegulation;
	JPanel buttonPanel;
	ActionListener buttonListener;
	
	private final String getAllLabel = "Get All";
	private final String setConfigLabel = "Set Config";
	private final String setTimeoutLabel = "Set Timeout";
	private final String setEncoderLabel = "Set Enc. Meas.";
	private final String setTimerLabel = "Set Timer";
	private final String setRegulationLabel = "Set Regul.";
	
	private final double initPval;
	private final double initDval;
	private final int initInterval;
	private final int initTimeout;
	private final int initEncodetTime;
	private final int initRegulationTime;
	
	private final short TimeoutID = 0;
	private final short EncoderID = 1;
	private final short TimerID = 2;
	private final short RegulationID = 3;
	
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
		initRegulationTime = 150;
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
		
		btnGetAll = addButton(getAllLabel);
		btnSetConfig = addButton(setConfigLabel);
		btnSetTimeout = addButton(setTimeoutLabel);
		btnSetEncoder = addButton(setEncoderLabel);
		btnSetTimer = addButton(setTimerLabel);
		btnSetRegulation = addButton(setRegulationLabel);
	}
	
	private JButton addButton(String name)
	{
		JButton button = new JButton(name);
		buttonPanel.add(button);
		return button;
	}
	
	private void addListener(){
		btnGetAll.addActionListener(buttonListener);
		btnSetConfig.addActionListener(buttonListener);
		btnSetTimeout.addActionListener(buttonListener);
		btnSetEncoder.addActionListener(buttonListener);
		btnSetTimer.addActionListener(buttonListener);
		btnSetRegulation.addActionListener(buttonListener);
	}
	
	private void removeListener()
	{
		btnGetAll.removeActionListener(buttonListener);
		btnSetConfig.removeActionListener(buttonListener);
		btnSetTimeout.removeActionListener(buttonListener);
		btnSetEncoder.removeActionListener(buttonListener);
		btnSetTimer.removeActionListener(buttonListener);
		btnSetRegulation.removeActionListener(buttonListener);
	}

	@Override
	public void getBTControl(IBluethooth bluetooth) {
		this.bluetooth = bluetooth;
	}

	@Override
	public void getTimerControl(Timer timer) {
		System.out.println(timer == null ? "timer to null" : "timer git");
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
		return result;
	}
	private int setTimerValue(short ID)
	{
		if(table.isEditing())
			table.getCellEditor().stopCellEditing();
		int res =0;
		if(ID == TimeoutID)
		{
			res = Integer.parseInt((String)table.getValueAt(2, 3));
		}
		else if(ID == EncoderID)
		{
			res = Integer.parseInt((String)table.getValueAt(3, 3));
		}
		else if(ID == TimerID)
		{
			res = Integer.parseInt((String)table.getValueAt(5, 3));
		}
		else if(ID == RegulationID)
		{
			res = Integer.parseInt((String)table.getValueAt(4, 3));
		}
		return res;
	}
	
	private void getTimerValue(short ID,int value)
	{

		if(ID == TimeoutID)
		{
			table.setValueAt(value, 2, 2);
		}
		else if(ID == EncoderID)
		{
			table.setValueAt(value, 3, 2);
		}
		else if(ID == TimerID)
		{
			table.setValueAt(value, 5, 2);
		}
		else if(ID == RegulationID)
		{
			table.setValueAt(value, 4, 2);
		}
	}
	
	private void getConfigPD(double Pval, double Dval)
	{
		table.setValueAt(Pval, 0, 2);
		table.setValueAt(Dval, 1, 2);
		
	}
	
	class TableModel extends AbstractTableModel
	{
		private final String[] columnNames = {"Parameter", "Component", "Cur. Value", "To Set Val." };
		private Object[][] data = { { "P", "Robot", String.valueOf(initPval), String.valueOf((initPval)) },
				{ "D", "Robot", String.valueOf((initDval)), String.valueOf((initDval)) },
				{ "Timeout", "Robot", String.valueOf((initTimeout)), String.valueOf((initTimeout)) },
				{ "Encoder", "Robot", String.valueOf((initEncodetTime)), String.valueOf((initEncodetTime)) },
				{ "Regulation", "Robot", String.valueOf((initRegulationTime)), String.valueOf((initRegulationTime)) },
				{ "Timer", "Application", String.valueOf((initInterval)), String.valueOf((initInterval)) }};

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
			if(bluetooth != null)
			{
				String tmp;
				if(e.getActionCommand().equals(getAllLabel))
				{
					String PD, timeout_, encoder_, regul_;
					int timer_;
					try {
						PD = bluetooth.getConfig();
						timeout_ = bluetooth.getTimeout();
						encoder_ = bluetooth.getEncoderMeas();
						regul_ = bluetooth.getRegulationTimer();
						timer_ = timer.getDelay();
						
					} catch (SerialPortException e1) {
						PD = "0.0!0.0";
						timeout_ = "0";
						encoder_ = "0";
						regul_ = "0";
						timer_ = 0;
						e1.printStackTrace();
					}
					double[] val = GeneralConverter.deserializeStr2Dbl(PD);
					getConfigPD(val[0], val[1]);
					int res[] = GeneralConverter.deserializeStr2Int(timeout_);
					getTimerValue(TimeoutID, res[0]);
					res = GeneralConverter.deserializeStr2Int(encoder_);
					getTimerValue(EncoderID, res[0]);
					res = GeneralConverter.deserializeStr2Int(regul_);
					getTimerValue(RegulationID, res[0]);
					getTimerValue(TimerID, timer_);
				}else if(e.getActionCommand().equals(setConfigLabel))
				{
					double[] val = setConfigPD();
					
					try {
						bluetooth.setConfig(val[0], val[1]);
						tmp = bluetooth.getConfig();
					} catch (SerialPortException e1) {
						tmp = "0.0!0.0";
						e1.printStackTrace();
					}
					val = GeneralConverter.deserializeStr2Dbl(tmp);
					getConfigPD(val[0], val[1]);
				}else if(e.getActionCommand().equals(setTimeoutLabel))
				{
					int val = setTimerValue(TimeoutID);
					try {
						bluetooth.setTimeout(val);
						tmp = bluetooth.getTimeout();
						
					} catch (SerialPortException e1) {
						tmp = "0";
						e1.printStackTrace();
					}
					int[] res = GeneralConverter.deserializeStr2Int(tmp);
					getTimerValue(TimeoutID, res[0]);
					
				}else if(e.getActionCommand().equals(setEncoderLabel))
				{
					int val = setTimerValue(EncoderID);
					try {
						bluetooth.setEncoderMeas(val);
						tmp = bluetooth.getEncoderMeas();
						
					} catch (SerialPortException e1) {
						tmp = "0";
						e1.printStackTrace();
					}
					int[] res = GeneralConverter.deserializeStr2Int(tmp);
					getTimerValue(EncoderID, res[0]);
					
				}else if(e.getActionCommand().equals(setRegulationLabel))
				{
					int val = setTimerValue(RegulationID);
					try {
						bluetooth.setRegulationTimer(val);
						tmp = bluetooth.getRegulationTimer();
						
					} catch (SerialPortException e1) {
						tmp = "0";
						e1.printStackTrace();
					}
					int[] res = GeneralConverter.deserializeStr2Int(tmp);
					getTimerValue(RegulationID, res[0]);
					
				}
				else if(e.getActionCommand().equals(setTimerLabel))
				{
					timer.setDelay(setTimerValue(TimerID));
					getTimerValue(TimerID, timer.getDelay());
				}
			}else { System.err.println("BT and Tiemr are NULL");}
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Info infotst = new Info();
					BaseTile testTile = new ConfigTable("Testowy");//,infotst);
					testTile.getBTControl(BluetoothMock.getInstance());
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
