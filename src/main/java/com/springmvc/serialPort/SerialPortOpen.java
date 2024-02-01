package com.springmvc.serialPort;
import com.fazecast.jSerialComm.*; // Serial Port classes

public class SerialPortOpen
{
   public static void main (String[] Args) throws InterruptedException
   {
     SerialPort [] AvailablePorts = SerialPort.getCommPorts();
       
     for (SerialPort s : AvailablePorts) {
         //Open the first Available port
         SerialPort MySerialPort = s;
         
         if(s.getSystemPortName().equals("COM7")) {
        	 readData(MySerialPort);
         }
//         System.out.print(s.getSystemPortName());
//         MySerialPort.openPort(); //open the port
//                                  //Arduino May get reset 
//
//         if (MySerialPort.isOpen())//Check whether port open/not
//               System.out.print(" -> is Open ");
//         else
//            System.out.print(" -> Port not open ");
//
//         MySerialPort.closePort(); //Close the port    	
//         if (MySerialPort.isOpen())
//             System.out.println("--- is Open ");
//         else
//             System.out.println("-- Port not open ");
     }   
   }//end of main()
   
   public static void readData(SerialPort MySerialPort) throws InterruptedException {
	   int BaudRate = 9600;
	   int DataBits = 8;
	   int StopBits = SerialPort.ONE_STOP_BIT;
	   int Parity   = SerialPort.NO_PARITY;

	   
	   MySerialPort.setBaudRate(BaudRate);
	   MySerialPort.setParity(Parity);
	   MySerialPort.setNumDataBits(DataBits);
	   MySerialPort.setNumStopBits(StopBits);
	   MySerialPort.setFlowControl(SerialPort.NO_PARITY);
	   
	   //Sets all serial port parameters at one time
	   MySerialPort.setComPortParameters(BaudRate,
	                                     DataBits,
	                                     StopBits,
	                                       Parity);
	   
	   MySerialPort.setRTS();
	   MySerialPort.setDTR();
	   //Set Read Time outs
//	   MySerialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 
//	                                    1000, 
//	                                       0); 
	   //	   
	   MySerialPort.openPort(); //open the port
	   
	 //Reading from COM port using Java

	   byte[] WriteByte = new byte[1];
	      WriteByte[0] = 65; //send A
	      int bytesTxed  = 0;
	 
	      bytesTxed  = MySerialPort.writeBytes(WriteByte,1);
	 
	      System.out.print(" Bytes Transmitted -> " + bytesTxed );
	      
	   try 
	    {
	       while (true)
	       {
	    	
	    	   Thread.sleep(2000);//Delay added to so Arduino-
               //-can recover after RESET
	         byte[] readBuffer = new byte[100];

	         int numRead = MySerialPort.readBytes(readBuffer,
	                                              readBuffer.length);

	         System.out.print("Read " + numRead + " bytes -");
	         
	         //Convert bytes to String
	         String S = new String(readBuffer); 

	         System.out.println("Received -> "+ S);

	       }
	   } 
	   catch (Exception e) 
	   {

	         e.printStackTrace(); 
	   }

	   MySerialPort.closePort(); //Close the port
   }
}