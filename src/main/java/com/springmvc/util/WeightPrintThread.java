package com.springmvc.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.springmvc.model.PhysicalWeightBalanceData;
import com.springmvc.service.StudyService;

public class WeightPrintThread extends Thread{
	public static StudyService studyService;
	public static Map<String, Integer> map = new HashMap<>();
	public static String  ip= "";
	public static int port = 0;
	
	public void run() {
		map.put(ip, 1);
		try {
			Socket socket = new Socket(ip, port);
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Input: " + input.readLine());
			StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters  
			String line;  
			PhysicalWeightBalanceData obj = null;
			while((line=input.readLine())!=null)  
			{  
				String[] sa = line.split(" : ");
				if(sa[0].trim().equals("Date & Time")) {
					obj = new PhysicalWeightBalanceData();
					obj.setDateAndTime(sa[1]);
					obj.setDataFrom("Lan");
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
				
				System.out.println(line);
				sb.append(line);      //appends line to string buffer  
				sb.append("\n");     //line feed   
				if(map.get(ip) == 2) {
					map.put(ip, 0);
					break;
				}
			}  
			
			System.out.println(sb.toString());
			socket.close();
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				 map.put(ip, 0);
			}
	}
	
}
