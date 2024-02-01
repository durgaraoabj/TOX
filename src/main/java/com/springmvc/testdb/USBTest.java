package com.springmvc.testdb;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazecast.jSerialComm.SerialPort;
import com.springmvc.controllers.StagoWebSocket;
import com.springmvc.model.StagoData;
import com.springmvc.util.StagoThread;

public class USBTest {
	public static void main(String[] args) {
		SerialPort[] ports = SerialPort.getCommPorts();
		System.out.println("Select a port:");
		int i = 1;
//		String portName = DBConnection.userPortName;
		SerialPort comPort = null;
		String portName = "COM7";
		for (SerialPort port : ports) {
			System.out.println(i + ": " + port.getSystemPortName());
			
		}
		for (SerialPort port : ports) {
			System.out.println(i + ": " + port.getSystemPortName());
			if (port.getSystemPortName().equals(portName)) {
				comPort = port;
				System.out.println("Conneted to  : " + i + ": " + port.getSystemPortName());
			}
			i++;
		}
		// System.out.println(comPort.getInputStream());
		try {
			comPort.openPort();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		
//		comPort.setComPortParameters(9600, SerialPort.ONE_STOP_BIT, 8, SerialPort.NO_PARITY);
		try {
			while (true) {
				
				while (comPort.bytesAvailable() <= 0) {
//		         System.out.println(comPort.bytesAvailable());
					Thread.sleep(20);
				}
				Thread.sleep(200);
				byte[] readBuffer = new byte[comPort.bytesAvailable()];

				int numRead = comPort.readBytes(readBuffer, readBuffer.length);
				String s = new String(readBuffer);
				Thread.sleep(200);
				System.out.println(
						"==============================================================================================================");
				System.out.println("Data received for insturmetn ::::::::");
				System.out.println(s);

				InputStream is = null;
				BufferedReader bfReader = null;
				try {
					is = new ByteArrayInputStream(readBuffer);
					bfReader = new BufferedReader(new InputStreamReader(is));
					String temp = null;
					while ((temp = bfReader.readLine()) != null) {
						Thread.sleep(200);
						if (!temp.trim().equals("")) {
							System.out.println(temp);
							
						}

					}
				} catch (IOException e) {
					String message = "";

					for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
						message = message + System.lineSeparator() + stackTraceElement.toString();
					}
					StagoThread thread = new StagoThread();
					thread.start();
					e.printStackTrace();
				} finally {
					try {
						if (is != null)
							is.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				System.out.println(
						"################################################################################################################");
			}
		} catch (Exception e) {
			String message = "";

			for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
				message = message + System.lineSeparator() + stackTraceElement.toString();
			}
			e.printStackTrace();
		}
		System.out.println("Connection closed");
		comPort.closePort();
	}
}
