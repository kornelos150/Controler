package metro;

import javax.swing.Timer;

import bluethoothCtrl.IBluethooth;

public interface BTObserver {
	
	public double[] update(double[] input);
	public void activate();
	public void deactivate();
	public void getBTControl(IBluethooth bluethooth);
	public void getTimerControl(Timer timer);

}
