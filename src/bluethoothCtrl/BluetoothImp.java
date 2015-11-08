package bluethoothCtrl;

import jssc.SerialPort;
import jssc.SerialPortException;
import mainApp.GeneralConverter;

public class BluetoothImp implements IBluethooth{

	private SerialPort port;
	private final int TIMEOUT = 500;
	
	private static BluetoothImp instance;
	
	public static BluetoothImp getInstance() throws SerialPortException
	{
		if(instance == null)
			instance = new BluetoothImp();
		return instance;
	}
	
	
	private BluetoothImp() throws SerialPortException {
		
		port = new SerialPort("COM4");
		port.openPort();
		port.setParams(9600, 8, 1, 0);
		
	}
	
	private byte[] prepareMessage()
	{
		byte[] message = new byte[10];
		for(int i=0; i<message.length; ++i)
			message[i] = 0;
		message[0] = 'B';
		message[4] = 'M';
		message[9] = 'E';
		
		return message;
	}
	
	private String decodeMessage(byte[] message)
	{
		return String.format("%d.%d!%d.%d", message[2],message[3],message[5],message[6]);
	}
	
	private String decodeMessageWrkd(byte[] message)
	{
		byte[] tmp = new byte[2];
		tmp[0] = message[2];
		tmp[1] = message[3];
		double left = GeneralConverter.Byte2Double(tmp);
		tmp[0] = message[4];
		tmp[1] = message[5];
		double right = GeneralConverter.Byte2Double(tmp);
		return GeneralConverter.serializeDbl2Str(left, right);
	}
	
	private String decodeMessageTransformWrkd(byte[] message)
	{
		byte[] tmp = new byte[2];
		tmp[0] = message[2];
		tmp[1] = message[3];
		double left = GeneralConverter.Byte2Double(tmp);
		tmp[0] = message[4];
		tmp[1] = message[5];
		double right = GeneralConverter.Byte2Double(tmp);
		double TR[] = GeneralConverter.LertRight2TranRot(left, right);
		return GeneralConverter.serializeDbl2Str(TR[0], TR[1]);
	}
	
	private String decodeIntMessageWrkd(byte[] message)
	{
		byte[] tmp = new byte[2];
		tmp[0] = message[2];
		tmp[1] = message[3];
		int left = GeneralConverter.byte2int(tmp);
		tmp[0] = message[4];
		tmp[1] = message[5];
		int right = GeneralConverter.byte2int(tmp);
		return GeneralConverter.serializeInt2Str(left, right);
	}
	
	@Override
	public String setVelocity(double translation, double rotation, int time) throws SerialPortException {
		byte[] message = prepareMessage();
		double[] vel_LR = GeneralConverter.TranRot2LeftRight(translation, rotation);
		byte[] left = GeneralConverter.Double2Byte(vel_LR[0]);
		byte[] right = GeneralConverter.Double2Byte(vel_LR[1]);
		byte[] timeBytes = GeneralConverter.int2byte(time);
		message[1] = 1;
		message[2] = left[0];
		message[3] = left[1];
		message[5] = right[0];
		message[6] = right[1];
		message[7] = timeBytes[0];
		message[8] = timeBytes[1];
		printMessage(message);
		
		port.writeBytes(message);
		byte[] response = port.readBytes(10);
		
		printMessage(response);
		return decodeMessageTransformWrkd(response);
		
	}


	@Override
	public String getVelocity() throws SerialPortException  {
		byte[] message = prepareMessage();
		message[1] = 2;
		printMessage(message);
		
		port.writeBytes(message);
		byte[] response = port.readBytes(10);
		
		printMessage(response);
		return decodeMessageTransformWrkd(response);
	}


	@Override
	public String setConfig(double P, double D)  throws SerialPortException {
		byte[] message = prepareMessage();
		byte[] left = GeneralConverter.Double2Byte(P);
		byte[] right = GeneralConverter.Double2Byte(D);
		message[1] = 3;
		message[2] = left[0];
		message[3] = left[1];
		message[5] = right[0];
		message[6] = right[1];
		
		printMessage(message);
		port.writeBytes(message);
		byte[] response = port.readBytes(10);
		
		printMessage(response);
		return decodeMessageWrkd(response);
	}


	@Override
	public String getConfig() throws SerialPortException  {
		byte[] message = prepareMessage();
		message[1] = 4;
		printMessage(message);
		port.writeBytes(message);
		byte[] response = port.readBytes(10);
		
		printMessage(response);
		return decodeMessageWrkd(response);
	}
	
	@Override
	public String setTimeout(int timeout) throws SerialPortException {
		byte[] message = prepareMessage();
		message[1] = 5;
		byte[] timeBytes = GeneralConverter.int2byte(timeout);
		message[2] = timeBytes[0];
		message[3] = timeBytes[1];
		
		port.writeBytes(message);
		byte[] response = port.readBytes(10);
		
		printMessage(response);
		byte[] tmp = new byte[2];
		tmp[0] = response[2];
		tmp[1] = response[3];
		return String.valueOf(GeneralConverter.byte2int(tmp));
	}

	@Override
	public String getTimeout() throws SerialPortException {
		byte[] message = prepareMessage();
		message[1] = 6;

		port.writeBytes(message);
		byte[] response = port.readBytes(10);
		
		printMessage(response);
		byte[] tmp = new byte[2];
		tmp[0] = response[2];
		tmp[1] = response[3];
		return String.valueOf(GeneralConverter.byte2int(tmp));
	}

	@Override
	public String setPWM(int left, int right, int time) throws SerialPortException {
		byte[] message = prepareMessage();
		byte[] leftBytes = GeneralConverter.int2byte(left);
		byte[] rightBytes = GeneralConverter.int2byte(right);
		byte[] timeBytes = GeneralConverter.int2byte(time);
		message[1] = 7;
		message[2] = leftBytes[0];
		message[3] = leftBytes[1];
		message[5] = rightBytes[0];
		message[6] = rightBytes[1];
		message[7] = timeBytes[0];
		message[8] = timeBytes[1];
		printMessage(message);
		
		port.writeBytes(message);
		byte[] response = port.readBytes(10);
		
		printMessage(response);
		return decodeIntMessageWrkd(response);
		
	}


	@Override
	public String getPWM() throws SerialPortException  {
		byte[] message = prepareMessage();
		message[1] = 8;
		printMessage(message);
		
		port.writeBytes(message);
		byte[] response = port.readBytes(10);
		
		printMessage(response);
		return decodeIntMessageWrkd(response);
	}
		
	@Override
	public String setEncoderMeas(int time) throws SerialPortException {
		byte[] message = prepareMessage();
		message[1] = 9;
		byte[] timeBytes = GeneralConverter.int2byte(time);
		message[2] = timeBytes[0];
		message[3] = timeBytes[1];
		
		port.writeBytes(message);
		byte[] response = port.readBytes(10);
		
		printMessage(response);
		byte[] tmp = new byte[2];
		tmp[0] = response[2];
		tmp[1] = response[3];
		return String.valueOf(GeneralConverter.byte2int(tmp));
	}


	@Override
	public String getEncoderMeas() throws SerialPortException {
		byte[] message = prepareMessage();
		message[1] = 10;

		port.writeBytes(message);
		byte[] response = port.readBytes(10);
		
		printMessage(response);
		byte[] tmp = new byte[2];
		tmp[0] = response[2];
		tmp[1] = response[3];
		return String.valueOf(GeneralConverter.byte2int(tmp));
	}


	@Override
	public String setControlFlag(byte flag) throws SerialPortException {
		byte[] message = prepareMessage();
		message[1] = 11;
		message[2] = flag;
		
		printMessage(message);
		
		port.writeBytes(message);
		byte[] response = port.readBytes(10);
		
		printMessage(response);
		return String.valueOf(response[2]);
		
	}


	@Override
	public String getSetSpeed() throws SerialPortException {
		byte[] message = prepareMessage();
		message[1] = 12;
		printMessage(message);
		port.writeBytes(message);
		byte[] response = port.readBytes(10);
		
		printMessage(response);
		return decodeMessageTransformWrkd(response);
	}


	@Override
	protected void finalize() throws Throwable {
		port.closePort();
		super.finalize();
	}


	private void printMessage(byte[] bytes)
	{
		for(byte b: bytes)
			System.out.print(b+",");
		System.out.println("\n");
	}
	
	public static void main(String[] args) {
		try{
		BluetoothImp test = new BluetoothImp();
		
		String s = test.setVelocity(5.09, 1.52, 0);
		System.out.println(s);
		
		test.port.closePort();
		
		}catch(SerialPortException exc)
		{
			System.err.println(exc.getExceptionType());
			System.err.println("Invalid virtual port COM");
		}
		
	}

}
