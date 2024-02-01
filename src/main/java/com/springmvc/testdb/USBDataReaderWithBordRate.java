package com.springmvc.testdb;

import java.io.InputStream;

import com.fazecast.jSerialComm.SerialPort;

public class USBDataReaderWithBordRate {
    public static void main(String[] args) {
        SerialPort serialPort = SerialPort.getCommPort("COM7"); // Replace "COMx" with your actual port name
//        serialPort.setBaudRate(9600);
        serialPort.openPort();

        InputStream inputStream = serialPort.getInputStream();

        // Read data from the serial port
        try {
            while (true) {
                int data = inputStream.read();
                System.out.print((char) data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        serialPort.closePort();
    }
}