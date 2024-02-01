package com.springmvc.serialPort;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RS232CommunicationExample {
    public static void main(String[] args) {
        String portName = "COM7"; // Replace with your COM port name
        int baudRate = 9600; // Replace with your baud rate
        byte ackChar = 0x06; // Replace with your AckChar

        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            if (portIdentifier.isCurrentlyOwned()) {
                System.err.println("Error: Port is currently in use");
                return;
            }

            CommPort commPort = portIdentifier.open(RS232CommunicationExample.class.getName(), 2000);
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                InputStream inputStream = serialPort.getInputStream();
                OutputStream outputStream = serialPort.getOutputStream();

                while (true) {
                    int data = inputStream.read();
                    if (data != -1) {
                        if (data == ackChar) {
                            // Send acknowledgment if needed
                            // outputStream.write(ackChar);
                        } else {
                            // Process received data
                            System.out.println("Received: " + data);
                        }
                    }
                }
            } else {
                System.err.println("Error: Only serial ports are handled by this example.");
            }
        } catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | IOException e) {
            e.printStackTrace();
        }
    }
}