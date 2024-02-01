import com.fazecast.jSerialComm.*; // Serial Port classes

public class SerialPortOpen {
	public static void main(String[] Args) {
		SerialPort[] AvailablePorts = SerialPort.getCommPorts();
		SerialPort mySerialPort = AvailablePorts[0];
		System.out.println(mySerialPort.toString());
		System.out.println(mySerialPort.getSystemPortName());
		System.out.println(mySerialPort.getDescriptivePortName());
		mySerialPort.openPort();
		
		
		// Reading from COM port using Java
		try {
			System.out.println("Read data from USB : " + mySerialPort.getSystemPortName());
			while (true) {
				
				byte[] readBuffer = new byte[100];
				int numRead = mySerialPort.readBytes(readBuffer, readBuffer.length);
//				            System.out.print("Read " + numRead + " bytes -");
				// Convert bytes to String
				String s = new String(readBuffer, "UTF-8");
				if(!s.trim().equals(""))
					System.out.println(s);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mySerialPort.closePort(); // Close the port


	}// end of main()
}