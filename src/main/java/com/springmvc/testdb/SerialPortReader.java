package com.springmvc.testdb;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class SerialPortReader {
	 public static void main(String[] args) {
	        String portName = "COM7"; // Replace with your port name
	        int baudRate = 9600;

	        try {
	            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
	            SerialPort serialPort = (SerialPort) portIdentifier.open("SerialPortReader", 2000);
	            serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

	            InputStream inputStream = serialPort.getInputStream();
	            OutputStream outputStream = serialPort.getOutputStream();

	            // Handshake: Sender sends a handshake message
	            String handshakeMessage = "ACKACK";
	            byte[] handshakeBytes = handshakeMessage.getBytes();
	            outputStream.write(handshakeBytes);
	            outputStream.flush();

	            // Wait for handshake response
	            byte[] responseBytes = new byte[handshakeBytes.length];
	            int bytesRead = 0;
	            while (bytesRead < handshakeBytes.length) {
	                int read = inputStream.read(responseBytes, bytesRead, handshakeBytes.length - bytesRead);
	                if (read == -1) {
	                    System.out.println("Handshake response not received.");
	                    return;
	                }
	                bytesRead += read;
	            }
	            String responseMessage = new String(responseBytes);
	            if (!responseMessage.equals(handshakeMessage)) {
	                System.out.println("Handshake response mismatch.");
	                return;
	            }

	            System.out.println("Handshake successful.");

	            // Now, you can start sending/receiving data

	            inputStream.close();
	            outputStream.close();
	            serialPort.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}