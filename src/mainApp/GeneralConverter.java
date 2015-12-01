package mainApp;

import java.nio.charset.StandardCharsets;

public class GeneralConverter {
	
	private static final double robotWidth = 1;
	
	public static double[] TranRot2LeftRight(double translation, double rotation)
	{
		double[] result = new double[2] ;
		result[0] = translation + rotation;
		result[1] = (translation - rotation)/robotWidth;
		return result;
	}
	
	public static double[] LertRight2TranRot(double left,double right)
	{
		double[] result = new double[2] ;
		result[0] = (left + right)/2;
		result[1] = (left - right)/2;
		return result;
	}
	
	public static byte[] Str2Byte(String string)
	{
		return string.getBytes(StandardCharsets.US_ASCII);
	}
	
	public static String Byte2Str(byte[] bytes)
	{
		return new String(bytes,StandardCharsets.US_ASCII);
	}
	
	public static byte[] Double2Byte( double val)
	{
		String tmp[] = String.format("%.2f", val).replaceAll(",",".").split("\\.");
		byte[] result = new byte[2];
		result[0] = Byte.valueOf(tmp[0]);
		if(val < 0 && val > -1)
			tmp[1] = "-"+tmp[1];
		result[1] = Byte.valueOf(tmp[1]);
		return result;
	}
	
	public static double Byte2Double( byte[] val)
	{
		if(val[0] > -1)
			return val[0] + val[1]/100.0;
		else
			return val[0] - val[1]/100.0;
	}
	
	public static String serializeDbl2Str(double first,double second)
	{
		String result = String.format("%.2f!%.2f", first,second).replaceAll(",",".");
		return result;
	}
	
	public static double[] deserializeStr2Dbl(String text)
	{
		String tmp[] = text.split("!");
		double[] res = new double[2];
		for(int i=0; i<tmp.length; i++)
			res[i] = Double.parseDouble(tmp[i]);
		return res;
	}
	
	public static String serializeInt2Str(int first,int second)
	{
		String result = String.format("%d!%d", first,second);
		return result;
	}
	
	public static int[] deserializeStr2Int(String text)
	{
		String tmp[] = text.split("!");
		int[] res = new int[2];
		for(int i=0; i<tmp.length; i++)
			res[i] = Integer.parseInt(tmp[i]);
		return res;
	}
	
	public static int byte2int(byte[] bytes)
	{
		int high = bytes[1] >= 0 ? bytes[1] : 256 + bytes[1];
		int low = bytes[0] >= 0 ? bytes[0] : 256 + bytes[0];

		return low | (high << 8);
	}
	
	public static byte[] int2byte(int value)
	{
		byte[] result = new byte[2];
		result[0] = (byte) (value & 0xFF);
		result[1] = (byte) ((value >> 8) & 0xFF);
		return result;
	}
	
	private static void printBytes(byte[] bytes)
	{
		for(byte i: bytes)
			System.out.print(i + " ");
		System.out.println();
	}
	
	
	public static void main(String args[])
	{
//		String x = "abcd";
//		printBytes(Str2Byte(x));
	
		double a = -0.345;
		byte [] res = Double2Byte(a);
		printBytes(res);
		
		double da = Byte2Double(res);
		System.out.println(da);
		
		a = 2.05;
		res = Double2Byte(a);
		printBytes(res);
		
		da = Byte2Double(res);
		System.out.println(da);
		
		int t = 1723;
		byte[] res2 = int2byte(t);
		printBytes(res2);
		
		int afterT = byte2int(res2);
		System.out.println(afterT);
			
	}

}
