package mainApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ControlState {

	private static String statePath = "config/state.conf";
	
	private double rightVelocity;
	private double leftVelocity;
	private double pVal;
	private double dVal;
	private double intervlalTimer;
	
	public ControlState() throws IOException
	{
		File config = new File(statePath);
		if(config.isFile())
			readFromFile();		
	}
	
	public double getRightVelocity() {
		return rightVelocity;
	}

	public void setRightVelocity(double rightVelocity) {
		this.rightVelocity = rightVelocity;
	}

	public double getLeftVelocity() {
		return leftVelocity;
	}

	public void setLeftVelocity(double leftVelocity) {
		this.leftVelocity = leftVelocity;
	}

	public double getpVal() {
		return pVal;
	}

	public void setpVal(double pVal) {
		this.pVal = pVal;
	}

	public double getdVal() {
		return dVal;
	}

	public void setdVal(double dVal) {
		this.dVal = dVal;
	}

	public double getIntervlalTimer() {
		return intervlalTimer;
	}

	public void setIntervlalTimer(double intervlalTimer) {
		this.intervlalTimer = intervlalTimer;
	}

	public void write2file()
	{
		
	}

	public void readFromFile() throws IOException
	{
		File config = new File(statePath);
		if(config.isFile())
		{
			BufferedReader reader = new BufferedReader(new FileReader(statePath));
			String line;
			while((line = reader.readLine()) != null)
			{
				
			}
			
		}
	}
	
	private void setVariableFromString(String line)
	{
		String[] tmp = line.split(",");
		if("rightVelocity" == tmp[0])
			rightVelocity = Double.parseDouble(tmp[1]);
		if("leftVelocity" == tmp[0])
			leftVelocity = Double.parseDouble(tmp[1]);
		if("pVal" == tmp[0])
			pVal = Double.parseDouble(tmp[1]);
		if("dVal" == tmp[0])
			dVal = Double.parseDouble(tmp[1]);
		if("intervlalTimer" == tmp[0])
			intervlalTimer = Double.parseDouble(tmp[1]);
		
	}
	public static void main(String[] args) {
		
	}

}
