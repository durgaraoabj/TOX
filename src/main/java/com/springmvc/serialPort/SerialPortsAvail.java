package com.springmvc.serialPort;

import com.fazecast.jSerialComm.*;

public class SerialPortsAvail {
	public static void main(String[] args) {
		SerialPort[] availablePorts = SerialPort.getCommPorts();

		// use the for loop to print the available serial ports
		for (SerialPort s : availablePorts) {
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println("\t  SerialPort : " + s.toString());
			System.out.println("\t  SerialNumber : " + s.getSerialNumber());
			System.out.println("\t  port number in COMxx format  : " + s.getSystemPortName());
			System.out.println("\t  Port Description as reported by the device : " + s.getDescriptivePortName());
			System.out.println("\t  Port OS path : " + s.getSystemPortPath());
			System.out.println("\t  Location of the Virtual COM port on the USB Hub : " + s.getSystemPortPath());
			System.out.println("_________________________________________________________________________________________________");
		}
	}
}
