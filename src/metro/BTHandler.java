package metro;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import bluethoothCtrl.BluetoothImp;
import bluethoothCtrl.BluetoothMock;
import bluethoothCtrl.IBluethooth;
import jssc.SerialPortException;
import mainApp.GeneralConverter;

public class BTHandler {

	public class Interval 
	{
		private int interval = 300;

		public int getInterval() {
			return interval;
		}

		public void setInterval(int interval) {
			this.interval = interval;
		}
		
	}
	
	private IBluethooth bluetooth;
	
	private ActionList getSpeedObs;
	private BTObserver setSpeedObs;
	private BTObserver setPWMObs;
	private BTObserver configObs;
	
	private Timer timer;
	private Task task;
	private ActionListener tryStartTimer;
	private ActionListener tryStopTimer;
	private final ActionEvent stopTimerEvent = new ActionEvent(this, 1, "stopTimer");
	private final ActionEvent startTimerEvent = new ActionEvent(this, 2, "startTimer");
	private Interval interval;
	
	
	public BTHandler() {
		
		tryStartTimer = new TryStartTimer();
		tryStopTimer = new TryStopTimer();
		
		getSpeedObs = new ActionList(tryStopTimer, tryStartTimer);
		setSpeedObs = null;
		configObs = null;
		interval = new Interval();
		
		timer = new Timer(interval.getInterval(),new Task());
		
//		try {
//			this.bluetooth = BluetoothImp.getInstance();
//		} catch (SerialPortException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		this.bluetooth = BluetoothMock.getInstance();
		
	}
	
	public void activeTile(BaseTile tile)
	{	
		if(tile.isCtrl())
		{
			removeConfigCtrlObs();
			addSetSpeedObs(tile);
			try {
				String tmp = bluetooth.getRegulationTimer();
				if(Integer.parseInt(tmp) == 0)
					bluetooth.setRegulationTimer(interval.getInterval()/2);
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(tile.isConfig())
		{
			removeConfigCtrlObs();
			addConfigObs(tile);
			tile.getBTControl(bluetooth);
			tile.getTimerControl(timer);
			
		}else if(tile.isPWMCtrl())
		{
			removeConfigCtrlObs();
			addSetPWMObs(tile);
		}else
		{
			if(tile.getType() == BaseTile.Type.SPEEDVISIO)
				addGetSpeedObs(tile);
		}
		tile.activate();
	}
	
	public void deactiveTile(BaseTile tile)
	{	
		if(tile.isCtrl())
			setSpeedObs = null;
		else if(tile.isConfig())
			configObs = null;
		else if(tile.getType() == BaseTile.Type.SPEEDVISIO)
			removeGetSpeedObs(tile);
		
		tile.deactivate();
	}
	
	public void deactivateAll(ArrayList<BaseTile> components)
	{
		for(BaseTile item: components)
			deactiveTile(item);
	}
	
	public void deactivateCtrlConf()
	{
		deactiveTile((BaseTile)setSpeedObs);
		deactiveTile((BaseTile)configObs);
		deactiveTile((BaseTile)setPWMObs);
	}
	
	public void hardTimerStop()
	{
		timer.stop();
		sentStop();
	}
	
	public void sentStop()
	{
		try {
			bluetooth.setVelocity(0.0, 0.0, 0);
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void addSetSpeedObs(BTObserver tile)
	{
		setSpeedObs = tile;
		tryStartTimer.actionPerformed(startTimerEvent);
	}
	
	private void addConfigObs(BTObserver tile)
	{
		configObs = tile;
	}
	
	private void addSetPWMObs(BTObserver tile)
	{
		setPWMObs = tile;
		tryStartTimer.actionPerformed(startTimerEvent);
	}
	
	private void removeConfigCtrlObs()
	{
		if(configObs != null)
		{
			configObs.deactivate();
			configObs = null;
			
		}else if(setSpeedObs != null)
		{
			setSpeedObs.deactivate();
			setSpeedObs = null;
			tryStopTimer.actionPerformed(stopTimerEvent);
			try {
				bluetooth.setVelocity(0.0, 0.0, 0);
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(setPWMObs != null)
		{
			setPWMObs.deactivate();
			setPWMObs = null;
			tryStopTimer.actionPerformed(stopTimerEvent);
			try {
				bluetooth.setVelocity(0.0, 0.0, 0);
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private void removeGetSpeedObs(BTObserver tile)
	{
		getSpeedObs.remove(tile);
		
	}
	
	private void addGetSpeedObs(BTObserver tile)
	{
		getSpeedObs.add(tile);
	}
	
	private class Task implements ActionListener
	{
		private int currentVal = 0;
		double[] velocity;
		
		private void iterateCurrentVal()
		{
			if(currentVal != 2)
				currentVal++;
			else
				currentVal = 0;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try{	
				if(setSpeedObs != null && currentVal == 0)
				{
					velocity = setSpeedObs.update(null);
					bluetooth.setVelocity(velocity[0], velocity[1], 0);
					System.out.println("SET Tr: "+velocity[0]+" Rot: "+ velocity[1]);

				}
				if(!getSpeedObs.isEmpty() && currentVal == 1)
				{
					String tmp = bluetooth.getVelocity();
					velocity = GeneralConverter.deserializeStr2Dbl(tmp);
					System.out.println("GET Tr: "+velocity[0]+" Rot: "+ velocity[1]);
					for(BTObserver obs : getSpeedObs)
						obs.update(velocity);
				}
				if(setPWMObs != null && currentVal == 2)
				{
					velocity = setPWMObs.update(null);
					int pwmL = (int)velocity[0];
					int pwmR = (int)velocity[1];
					bluetooth.setPWM(pwmL, pwmR, 0);
					System.out.println("SET PWM LEFT: "+pwmL+" RIGHT: "+ pwmR);

				}
				iterateCurrentVal();
			}catch(SerialPortException serialE)
			{
			}
			
		}
		
	}

	
	private class TryStartTimer implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!timer.isRunning())
				timer.start();
		}
		
	}
	
	private class TryStopTimer implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(getSpeedObs.isEmpty() && setSpeedObs == null && setPWMObs == null)
				timer.stop();
		}
		
	}
	
	private class ActionList extends LinkedList<BTObserver>
	{
		ActionListener goEmptyListener;
		ActionListener goFullListner;
		
		public ActionList(ActionListener goEmptyListener, ActionListener goFullListner) {
			super();
			this.goEmptyListener = goEmptyListener;
			this.goFullListner = goFullListner;
		}

		@Override
		public boolean add(BTObserver e) {
			Boolean result = super.add(e);
			if(this.size() == 1)
				goFullListner.actionPerformed(startTimerEvent);
			return result;
		}

		@Override
		public boolean remove(Object o) {
			Boolean result = super.remove(o);
			if(this.isEmpty())
				goEmptyListener.actionPerformed(stopTimerEvent);
			return result;
		}
		
	}

}
