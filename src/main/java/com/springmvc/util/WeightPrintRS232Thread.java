package com.springmvc.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazecast.jSerialComm.SerialPort;
import com.springmvc.controllers.TestWebSocket;
import com.springmvc.model.PhysicalWeightBalanceData;
import com.springmvc.service.StudyService;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
public class WeightPrintRS232Thread extends Thread{
	public static StudyService studyService;
	public static Map<String, Integer> map = new HashMap<>();
	public static String ip = "";
	public static int port = 0;
	public static String portName = "";
	
	
	public void run() {
		System.out.println("******************************************instrument data satrt");
		SerialPort[] availablePorts = SerialPort.getCommPorts();
		for(SerialPort mySerialPort : availablePorts) {
			System.out.println(mySerialPort.toString());
			System.out.println(mySerialPort.getSystemPortName());
			System.out.println(mySerialPort.getDescriptivePortName());
		}
		System.out.println("******************************************instrument data satrt from USB");
		for(SerialPort mySerialPort : availablePorts) {
			if(mySerialPort.getSystemPortName().equals(WeightPrintRS232Thread.portName)) {
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
//						            System.out.print("Read " + numRead + " bytes -");
						// Convert bytes to String
						String s = new String(readBuffer, "UTF-8");
						if(!s.trim().equals("")) {
							System.out.println(s);
							String[] sp = s.split("\\+");
							s = sp[1].trim();
							try {
								Map<String, String> m = new HashedMap();
								m.put("id", "200");
//								s.put("collected", "");
//								s.put("color", "blue");
								m.put("dateAndTime", "200");
								m.put("batchNo", "200");
								m.put("nozzleNo", "200");
								m.put("grossWt", "200");
								m.put("tareWt", "200");
								m.put("netWt", s.trim());
								m.put("status", "200");
								m.put("dataFrom", "200");
								m.put("ipAddress", "200");

								String json = "";
								try {
									json = new ObjectMapper().writeValueAsString(m);
								} catch (JsonProcessingException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								Map<String, SseEmitter> map = TestWebSocket.emittersMap;
								for(Map.Entry<String, SseEmitter> emitters:  map.entrySet()) {
									SseEmitter emitter = emitters.getValue();
									try {
										System.out.println(json);
										emitter.send(SseEmitter.event().name("insData").data(json));
									} catch (IOException e) {
										// TODO Auto-generated catch block
										TestWebSocket.emittersMap.remove("superadmin");
									}
								}						
							}catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							};
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				mySerialPort.closePort(); // Close the port	
			}
		}
	}
	
	/*public void run() {
		try {
			map.put(ip, 1);
			
			SerialPort[] ports = SerialPort.getCommPorts();
			
			System.out.println("Select a port:");
			int i = 1;
			int portNo = 0;
			System.out.println("-------------------------------Covide--------------------------------");
			for (SerialPort port : ports) {
				System.out.println(i + ": " + port.getSystemPortName());
				if(port.getSystemPortName().equals(portName)) {
					portNo = i;
				}
				i++;
			}
			System.out.println(portNo);
			SerialPort comPort = SerialPort.getCommPorts()[portNo];
			System.out.println(comPort.getInputStream());
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
			      
			      PhysicalWeightBalanceData obj = null;
			        InputStream is = null;
			        BufferedReader bfReader = null;
			        try {
			            is = new ByteArrayInputStream(readBuffer);
			            bfReader = new BufferedReader(new InputStreamReader(is));
			            String temp = null;
			            while((temp = bfReader.readLine()) != null){
			            	Thread.sleep(200);
			                System.out.println(temp);
				    	     String[] sa = temp.split(" : ");
								if(sa[0].trim().equals("Date & Time")) {
									obj = new PhysicalWeightBalanceData();
									obj.setDateAndTime(sa[1]);
									obj.setDataFrom("RS232");
									obj.setIpAddress(ip);
								}
								if(sa.length > 1 && sa[0].trim().equals("Batch No"))	obj.setBatchNo(sa[1]);
								if(sa.length > 1 && sa[0].trim().equals("Nozzle No"))	obj.setNozzleNo(sa[1]);
								if(sa.length > 1 && sa[0].trim().equals("Gross Wt"))	obj.setGrossWt(sa[1]);
								if(sa.length > 1 && sa[0].trim().equals("Tare Wt"))	obj.setTareWt(sa[1]);
								if(sa.length > 1 && sa[0].trim().equals("Net Wt"))	obj.setNetWt(sa[1]);
								if(sa[0].trim().equals("Status")) {
									obj.setStatus(sa[1]);
									obj = studyService.savePhysicalWeightBalanceData(obj);
									obj = null;
								}
								
			            }
			        } catch (IOException e) {
			            e.printStackTrace();
			        } finally {
			            try{
			                if(is != null) is.close();
			            } catch (Exception ex){
			                 ex.printStackTrace();
			            }
			        }
			      InputStream inputStream =   comPort.getInputStream();
			      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			      
			      while(reader.ready()) {
			    	     String line = reader.readLine();
			    	     
						System.out.println("-------------/////////////////");
						
			    	     System.out.println(line);
			    	     System.out.println("-------------/////////////////++++++++++");
//			    	     WeightRead.weight.setText(line);
			    	}
			      System.out.println("################################################################################################################");
			   }
			} catch (Exception e) { 
				e.printStackTrace(); 
				}
			comPort.closePort();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}*/
}
