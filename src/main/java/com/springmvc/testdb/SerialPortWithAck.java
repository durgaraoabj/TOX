package com.springmvc.testdb;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class SerialPortWithAck {
    public static void main(String[] args) {
        String portName = "COM7"; // Replace with your port name
        int baudRate = 9600;

        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            SerialPort serialPort = (SerialPort) portIdentifier.open("SerialPortWithAck", 2000);
            serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            InputStream inputStream = serialPort.getInputStream();
            OutputStream outputStream = serialPort.getOutputStream();
            byte[] ackBytes = "ACK".getBytes();
            outputStream.write(ackBytes);
            outputStream.flush();
            
            while (true) {
                int data = inputStream.read();
//                if (data == -1) {
//                   System.out.println("  End of stream");
//                    break;
//                }
                
                System.out.print((char) data);

                // Send an acknowledgment back
                
            }

//            inputStream.close();
//            outputStream.close();
//            serialPort.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



