import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import com.fazecast.jSerialComm.SerialPort;

public class Radwag {
	public static void main(String[] args) throws UnknownHostException, IOException {
//		thowRS232();
//		trowLan();
	}

	private static void thowRS232() {
		// TODO Auto-generated method stub

			
			SerialPort[] ports = SerialPort.getCommPorts();
			System.out.println("Select a port:");
			int i = 1;
			String portName = "COM4"; 
			int portNo = 0;
			SerialPort comPort = null;
			for (SerialPort port : ports) {
				System.out.println(i + ": " + port.getSystemPortName());
				if (port.getSystemPortName().equals(portName)) {
					comPort = port;
					System.out.println("Conneted to  : " + i + ": " + port.getSystemPortName());
				}
				i++;
			}			
			System.out.println(portNo);
//			SerialPort comPort = SerialPort.getCommPorts()[portNo];
//			System.out.println(comPort.getInputStream());
			comPort.openPort();
			try {
			   while (true)
			   {
			      while (comPort.bytesAvailable() <= 0) {
//			         System.out.println(comPort.bytesAvailable());
			    	  Thread.sleep(20);
			      }
			      Thread.sleep(200);
			      byte[] readBuffer = new byte[comPort.bytesAvailable()];
			      
			      int numRead = comPort.readBytes(readBuffer, readBuffer.length);
			      String s = new String(readBuffer);
			      Thread.sleep(200);
			      System.out.println("==============================================================================================================");
			      System.out.println(s);  
//			      weight.setText(s);
//			      WeightRead.weight.setText(s);
//			      Thread.sleep(200);
			      
			   
			      InputStream inputStream =   comPort.getInputStream();
			      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			      while(reader.ready()) {
			    	     String line = reader.readLine();
			    	     System.out.println(line);
//			    	     WeightRead.weight.setText(line);
			    	}
			      System.out.println("################################################################################################################");
			   }
			} catch (Exception e) { e.printStackTrace(); }
			comPort.closePort();
		
	}

	private static void trowLan() {
		try {
			Socket socket = new Socket("192.168.0.159", 4001);
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Input: " + input.readLine());
			StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters  
			String line;  
			while((line=input.readLine())!=null)  
			{  
				System.out.println(line);
				sb.append(line);      //appends line to string buffer  
				sb.append("\n");     //line feed   
			}  
			
			System.out.println(sb.toString());
			socket.close();
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	}
	
	
}
