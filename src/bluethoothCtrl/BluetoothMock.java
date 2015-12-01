package bluethoothCtrl;

import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;

import javax.management.timer.TimerMBean;

import jssc.SerialPortException;
import mainApp.GeneralConverter;

public class BluetoothMock implements IBluethooth {

	private double rightVal;
	private double leftVal;
	private double pVal =3.0;
	private double dVal =0.2;
	private double leftS = 10.0;
	private double middleS = 10.0;
	private double righttS = 10.0;
	private double battery = 0.7;
	private int timeout =0;
	private int leftPWM = 0;
	private int rightPWM = 0;
	private double leftSetSpeed = 2.0;
	private double rightSetSpeed = 2.0;
	private byte controlFlag = 0;
	private int encoderTimer = 10000;
	private int regulationTimer = 10000;
	private byte directionBitmap = 0;
	private int leftEncoderTicks = 3000;
	private int rightEncoderTicks = 3000;

	private static BluetoothMock instance;
	
	public static BluetoothMock getInstance()
	{
		if(instance == null)
			instance = new BluetoothMock();
		return instance;
	}
	
	private void printFloat(double first, double second)
	{
		System.out.println(String.format("first: %.2f second: %.2f", first, second));
	}
	private void printInt(int first, int second)
	{
		System.out.println(String.format("first: %d second: %d", first, second));
	}
	
	
	@Override
	public String setVelocity(double left, double right, int time) {
		
		
		double res[] = new double[2];
		res[0] = left;
		res[1] = right;
		//res[2] = time;
		leftVal = left;
		rightVal = right;
		printFloat(left, right);
		return GeneralConverter.serializeDbl2Str(res[0], res[1]);
	}
	
	@Override
	public String setVelocityWait(double left, double right, int time) {
		
		double res[] = new double[2];
		res[0] = left;
		res[1] = right;
		//res[2] = time;
		leftVal = left;
		rightVal = right;
		if(time != 0)
		{
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		printFloat(left, right);
		return GeneralConverter.serializeDbl2Str(res[0], res[1]);
	}

	@Override
	public String getVelocity() {
		
		double res[] = new double[3];
		res[0] = leftVal;
		res[1] = rightVal;
		printFloat(leftVal, rightVal);
		return GeneralConverter.serializeDbl2Str(res[0], res[1]);
	}

	@Override
	public String setConfig(double P, double D) {
		double res[] = new double[2];
		res[0] = P;
		pVal = P;
		dVal = D;
		res[1] = D;
		printFloat(P, D);
		return GeneralConverter.serializeDbl2Str(res[0], res[1]);

	}

	@Override
	public String getConfig() {
		double res[] = new double[2];
		res[0] = pVal;
		res[1] = dVal;
		printFloat(pVal, dVal);
		return GeneralConverter.serializeDbl2Str(res[0], res[1]);
	}

	@Override
	public String setTimeout(int timeout) throws SerialPortException {
		this.timeout = timeout;
		printInt(timeout, 0);
		return String.valueOf(timeout);
	}
	
	@Override
	public String getTimeout() throws SerialPortException {
		printInt(timeout, 0);
		return String.valueOf(timeout);
	}
	
	@Override
	public String setPWM(int left, int right, int time) {
		
		
		int res[] = new int[2];
		res[0] = left;
		res[1] = right;
		//res[2] = time;
		leftPWM = left;
		rightPWM = right;
		printInt(left, right);
		return GeneralConverter.serializeInt2Str(res[0], res[1]);
	}
	
	@Override
	public String setPWMWait(int left, int right, int time) {
		
		
		int res[] = new int[2];
		res[0] = left;
		res[1] = right;
		//res[2] = time;
		leftPWM = left;
		rightPWM = right;
		if(time != 0)
		{
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		printInt(left, right);
		return GeneralConverter.serializeInt2Str(res[0], res[1]);
	}
	

	@Override
	public String getPWM() {
		
		int res[] = new int[2];
		res[0] = leftPWM;
		res[1] = rightPWM;
		printInt(leftPWM, rightPWM);
		return GeneralConverter.serializeInt2Str(res[0], res[1]);
	}	
	
	@Override
	public String setEncoderMeas(int time) throws SerialPortException {
		encoderTimer = time;
		printInt(time, 0);
		return String.valueOf(time);
	}


	@Override
	public String getEncoderMeas() throws SerialPortException {
		printInt(encoderTimer, 0);
		return String.valueOf(encoderTimer);
	}

	@Override
	public String setRegulationTimer(int time) throws SerialPortException {
		regulationTimer = time;
		printInt(time, 0);
		return String.valueOf(time);
	}


	@Override
	public String getRegulationTimer() throws SerialPortException {
		printInt(regulationTimer, 0);
		return String.valueOf(regulationTimer);
	}
	


	@Override
	public String getSetSpeed() throws SerialPortException {
		printFloat(leftSetSpeed, rightSetSpeed);
		return GeneralConverter.serializeDbl2Str(leftSetSpeed, rightSetSpeed);
	}
	

	@Override
	public String setMotorDirection(byte bitmap) throws SerialPortException {
		directionBitmap = bitmap;
		printInt((int)bitmap, 0);
		return String.valueOf(bitmap);
	}


	@Override
	public String getMotorDirection() throws SerialPortException {
		printInt((int)directionBitmap, 0);
		return String.valueOf(directionBitmap);
	}

	@Override
	public String getEncoderTicks() throws SerialPortException {
		int res[] = new int[2];
		res[0] = leftEncoderTicks;
		res[1] = rightEncoderTicks;
		return GeneralConverter.serializeInt2Str(res[0], res[1]);
	}

	public static void main(String[] args)
	{
	BluetoothMock test = new BluetoothMock();
	System.out.println("Given velocity "+10.4+" "+23.5);	
	String res = test.setVelocity(10.4, 23.5, 0);
	System.out.println("Result velocity "+res);
	
	}

}
