package bluethoothCtrl;

import jssc.SerialPortException;

public interface IBluethooth {
	
	//public String setVelocity(double left, double right, int time);
	public String setVelocity(double translation, double rotation, int time) throws SerialPortException;
	public String setVelocityWait(double translation, double rotation, int time) throws SerialPortException;
	public String getVelocity() throws SerialPortException;
	public String setConfig(double P, double D) throws SerialPortException;
	public String getConfig() throws SerialPortException;
	public String setTimeout(int timeout) throws SerialPortException;
	public String getTimeout() throws SerialPortException;
	public String setPWM(int left, int right, int time) throws SerialPortException;
	public String setPWMWait(int left, int right, int time) throws SerialPortException;
	public String getPWM() throws SerialPortException;
	public String setEncoderMeas(int time) throws SerialPortException;
	public String getEncoderMeas() throws SerialPortException;
	public String setRegulationTimer(int time) throws SerialPortException;
	public String getRegulationTimer() throws SerialPortException;
	public String setMotorDirection(byte bitmap) throws SerialPortException;
	public String getMotorDirection() throws SerialPortException;
	public String getSetSpeed() throws SerialPortException;
	public String getEncoderTicks() throws SerialPortException;

}
