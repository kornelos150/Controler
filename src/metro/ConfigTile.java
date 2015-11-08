package metro;

import java.awt.event.ActionListener;

public interface ConfigTile {
	
	public void getConfigPD(double Pval, double Dval);
	public double[] setConfigPD();
	public void getTimeout(int timeout);
	public double[] setTimeout();
	public int setTimerInterval();
	
	public void addGetConfigListener(ActionListener listener);
	public void removeGetConfigListener(ActionListener listener);
	public void addSetConfigListener(ActionListener listener);
	public void removeSetConfigListener(ActionListener listener);
	public void addSetTimerListener(ActionListener listener);
	public void removeSetTimerListener(ActionListener listener);
	public void addSetTimeoutListener(ActionListener listener);
	public void removeSetTimeoutListener(ActionListener listener);

}
