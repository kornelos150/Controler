package bluethoothCtrl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

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

	private static BluetoothMock instance;
	
	public static BluetoothMock getInstance()
	{
		if(instance == null)
			instance = new BluetoothMock();
		return instance;
	}
	
	
	@Override
	public String setVelocity(double left, double right, int time) {
		
		
		double res[] = new double[2];
		res[0] = left;
		res[1] = right;
		//res[2] = time;
		leftVal = left;
		rightVal = right;
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return GeneralConverter.serializeDbl2Str(res[0], res[1]);
	}

	@Override
	public String getVelocity() {
		
		double res[] = new double[3];
		res[0] = leftVal;
		res[1] = rightVal;
		return GeneralConverter.serializeDbl2Str(res[0], res[1]);
	}

	@Override
	public String setConfig(double P, double D) {
		double res[] = new double[2];
		res[0] = P;
		pVal = P;
		dVal = D;
		res[1] = D;
		return GeneralConverter.serializeDbl2Str(res[0], res[1]);

	}

	@Override
	public String getConfig() {
		double res[] = new double[2];
		res[0] = pVal;
		res[1] = dVal;
		return GeneralConverter.serializeDbl2Str(res[0], res[1]);
	}

	@Override
	public String setTimeout(int timeout) throws SerialPortException {
		this.timeout = timeout;
		return String.valueOf(timeout);
	}
	
	@Override
	public String getTimeout() throws SerialPortException {
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
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return GeneralConverter.serializeInt2Str(res[0], res[1]);
	}

	@Override
	public String getPWM() {
		
		int res[] = new int[2];
		res[0] = leftPWM;
		res[1] = rightPWM;
		return GeneralConverter.serializeInt2Str(res[0], res[1]);
	}	
	
	@Override
	public String setEncoderMeas(int time) throws SerialPortException {
		encoderTimer = time;
		return String.valueOf(time);
	}


	@Override
	public String getEncoderMeas() throws SerialPortException {
		return String.valueOf(encoderTimer);
	}

	@Override
	public String setRegulationTimer(int time) throws SerialPortException {
		regulationTimer = time;
		return String.valueOf(time);
	}


	@Override
	public String getRegulationTimer() throws SerialPortException {
		return String.valueOf(regulationTimer);
	}
	


	@Override
	public String getSetSpeed() throws SerialPortException {
		return GeneralConverter.serializeDbl2Str(leftSetSpeed, rightSetSpeed);
	}
	

	@Override
	public String setMotorDirection(byte bitmap) throws SerialPortException {
		directionBitmap = bitmap;
		return String.valueOf(bitmap);
	}


	@Override
	public String getMotorDirection() throws SerialPortException {
		return String.valueOf(directionBitmap);
	}


	public static void main(String[] args)
	{
	BluetoothMock test = new BluetoothMock();
	System.out.println("Given velocity "+10.4+" "+23.5);	
	String res = test.setVelocity(10.4, 23.5, 0);
	System.out.println("Result velocity "+res);
	}

}
