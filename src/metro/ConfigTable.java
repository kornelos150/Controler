package metro;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import bluethoothCtrl.BluetoothMock;
import bluethoothCtrl.IBluethooth;

public class ConfigTable extends BaseTile implements ConfigTile{

	private JTable table;
	JButton btnGetConfig;
	JButton btnSetConfig;
	JButton btnSetTimer;
	
	private final double initPval;
	private final double initDval;
	private final int initInterval;
	private final int initTimeout;
	
	public ConfigTable(String name) {
		super(name);
		setType(Type.CONFIG);
		componentInit();
		initPval = 0.5;
		initDval = 0.5;
		initInterval = 500;
		initTimeout = 700;
		table.setModel(new TableModel());
	}
	
	private void componentInit()
	{
		getContent().setLayout(null);
		
		table = new JTable();
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 11, 263, 152);
		table.setFillsViewportHeight(true);
		getContent().add(scrollPane);
		
		btnGetConfig = new JButton("Get Config");
		btnGetConfig.setBounds(164, 186, 109, 23);
		getContent().add(btnGetConfig);
		
		btnSetConfig = new JButton("Set Config");
		btnSetConfig.setBounds(10, 186, 109, 23);
		getContent().add(btnSetConfig);
		
		btnSetTimer = new JButton("Set Timer");
		btnSetTimer.setBounds(10, 216, 109, 23);
		getContent().add(btnSetTimer);
	}
	
	@Override
	public void addGetConfigListener(ActionListener listener)
	{
		btnGetConfig.addActionListener(listener);
	}
	@Override
	public void removeGetConfigListener(ActionListener listener)
	{
		btnGetConfig.removeActionListener(listener);
	}
	@Override
	public void addSetConfigListener(ActionListener listener)
	{
		btnSetConfig.addActionListener(listener);
	}
	@Override
	public void removeSetConfigListener(ActionListener listener)
	{
		btnSetConfig.removeActionListener(listener);
	}
	
	@Override
	public void addSetTimerListener(ActionListener listener)
	{
		btnSetTimer.addActionListener(listener);
	}
	
	@Override
	public void removeSetTimerListener(ActionListener listener)
	{
		btnSetTimer.removeActionListener(listener);
	}
	
	
	
	@Override
	public void getTimeout(int timeout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double[] setTimeout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addSetTimeoutListener(ActionListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSetTimeoutListener(ActionListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double[] setConfigPD()
	{
		if(table.isEditing())
			table.getCellEditor().stopCellEditing();
		double result[] = new double[2];
		result[0] = Double.parseDouble((String)table.getValueAt(0, 3));
		result[1] = Double.parseDouble((String)table.getValueAt(1, 3));
		getConfigPD(result[0], result[1]);
		return result;
	}
	@Override
	public int setTimerInterval()
	{
		if(table.isEditing())
			table.getCellEditor().stopCellEditing();
		int res = Integer.parseInt((String)table.getValueAt(2, 3));
		table.setValueAt(res, 2, 2);
		return res;
	}
	
	@Override
	public void getConfigPD(double Pval, double Dval)
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
				{ "Timeout", "Robot", initDval, initDval }};

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
