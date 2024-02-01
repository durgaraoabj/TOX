package com.springmvc.serialPort;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class demo {
	

	public void connect(String portname) {
		// now we need to serialport
		
		SerialPort SP = new SerialPort(portname);
		
		try {
			SP.openPort();
			SP.setParams(
					
					SerialPort.BAUDRATE_9600,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE
					);
		System.out.println("Start");

		SP.addEventListener((SerialPortEvent event)->{
//				
				System.out.println("RXCHAR :" + event.isRXCHAR());
				System.out.println("isBREAK :" + event.isBREAK());
				System.out.println("isCTS :" + event.isCTS());
				System.out.println("isDSR :" + event.isDSR());
				System.out.println("isRING :" + event.isRING());
				System.out.println("isRXFLAG :" + event.isRXFLAG());
				System.out.println("isTXEMPTY :" + event.isTXEMPTY());
				System.out.println("type :" + event.getEventType());
//				System.out.println("type :" + SerialPort.li);
//				if(event.isRXCHAR()) {
//					printData(port);
//					
//				}\
				String tmp = "";
				if (event.isRXCHAR() && event.getEventValue() > 0) {         
	                try {
	                    tmp += SP.readString();
	                    if (tmp.contains("\n") && tmp.length() > 2){
//	                            if (tmp.contains("$GPGGA") && tmp.length() > 60){
//	                                decompose_GPGGA(tmp);
//	                            } else if (tmp.contains("$GPGLL")&& tmp.length() > 20){
//	                                decompose_GPGLL(tmp);
//	                            } else if (tmp.contains("$GPGSA") && tmp.length() > 14){
//	                                decompose_GPGSA(tmp);
//	                            } else if (tmp.contains("$GPGRS") && tmp.length() > 14){
//	                                int ind = tmp.indexOf("$GPGRS");
//	                                String str = tmp.substring(ind);
//	                                decompose_GPGRS(str);
//	                            }
	                        tmp = "";
	                    }
	                } 
	                catch (SerialPortException ex) {
	                	ex.printStackTrace();
//	                	msgArea.append(ex.toString() + "\n");
	                	} 
	            }

			});
			
			
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void printData(SerialPort port) {
		try {
			System.out.println(port.readHexString());
			System.out.println(port.readBytes());
			System.out.println(port.readString());
			System.out.println(port.readHexStringArray());
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// we need to array of string that hold all the ports
		String portlist[] = SerialPortList.getPortNames();
		
		for(int i =0; i<portlist.length;i++) {
			System.out.println(portlist[i]);
		}
		demo obj = new demo();
		obj.connect("COM7");
		
		

	}

}
