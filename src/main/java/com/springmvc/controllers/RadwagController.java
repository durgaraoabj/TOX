package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.Crf;
import com.springmvc.model.PhysicalWeightBalanceData;
import com.springmvc.service.StudyService;
import com.springmvc.util.WeightPrintRS232Thread;
import com.springmvc.util.WeightPrintThread;

@Controller
@RequestMapping("/radwag")
@PropertySource(value = { "classpath:application.properties" })
public class RadwagController {

	public static int count=0;
	public static PhysicalWeightBalanceData currentData = null;
	@Autowired
	StudyService studyService;
	@Autowired
    private Environment environment;
	@RequestMapping(value="/dataLocation", method=RequestMethod.GET)
	public String dataLocation(HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		model.addAttribute("datavalue", "0");
		model.addAttribute("PageHedding", "Physical Balance");
		model.addAttribute("activeUrl", "radwag/dataLocation");
		model.addAttribute("mainUrl", environment.getRequiredProperty("mainUrl"));
		return "dataLocation.tiles";

	}
	
	@RequestMapping(value="/dataLocationSave", method=RequestMethod.POST)
	public String dataLocationSave(HttpServletRequest request, 
			ModelMap model, @RequestParam("insturment") int insturment, @RequestParam("insturment2") int insturment2, @RequestParam("portName") String portName,
			RedirectAttributes redirectAttributes) {
		
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		if(insturment != -1) {
			List<String> ips = new ArrayList<>();
			for(Map.Entry<String, Integer> m : WeightPrintThread.map.entrySet()) {
				ips.add(m.getKey());
			}
			for(String ip : ips) {
				while(WeightPrintThread.map.get(ip) != 0) {
					WeightPrintThread.map.put(ip, 2);
				 }
			}
				
			switch (insturment) {
				case 1:
					try {
						WeightPrintThread thread = new WeightPrintThread();
						thread.studyService = studyService;
						thread.ip = "192.168.0.159";
						thread.port = 4001;
						thread.start();
	//					 getinstrumentData("192.168.0.159", 4001);
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					break;
				case 2:
					try {
						WeightPrintThread thread = new WeightPrintThread();
						thread.studyService = studyService;
						thread.ip = "192.168.0.55";
						thread.port = 4001;
						thread.start();
	//					 getinstrumentData("192.168.0.159", 4001);
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					break;
				default:
					break;
			}
		}else if(insturment2 != -1) {
			List<String> ips = new ArrayList<>();
			for(Map.Entry<String, Integer> m : WeightPrintThread.map.entrySet()) {
				ips.add(m.getKey());
			}
			for(String ip : ips) {
				while(WeightPrintThread.map.get(ip) != 0) {
					WeightPrintThread.map.put(ip, 2);
				 }
			}
				
			switch (insturment2) {
				case 1:
					try {
						WeightPrintRS232Thread thread = new WeightPrintRS232Thread();
						WeightPrintRS232Thread.studyService = studyService;
						WeightPrintRS232Thread.ip = "192.168.0.159";
						WeightPrintRS232Thread.port = 4001;
						WeightPrintRS232Thread.portName = portName;
						thread.start();
	//					 getinstrumentData("192.168.0.159", 4001);
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					break;
				default:
					break;
			}
		}
		
		model.addAttribute("datavalue", "1");
		model.addAttribute("PageHedding", "Physical Balance");
		model.addAttribute("activeUrl", "radwag/dataLocation");
		return "dataLocation.tiles";

	}

//	private void getinstrumentData(String ip, int port) {
//		map.put(ip, 1);
//		try {
//			Socket socket = new Socket(ip, port);
//			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			System.out.println("Input: " + input.readLine());
//			StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters  
//			String line;  
//			PhysicalWeightBalanceData obj = null;
//			while((line=input.readLine())!=null)  
//			{  
//				String[] sa = line.split(" : ");
//				if(sa[0].trim().equals("Date & Time")) {
//					obj = new PhysicalWeightBalanceData();
//					obj.setDateAndTime(sa[1]);
//					obj.setDataFrom("Lan");
//					obj.setIpAddress(ip);
//				}
//				if(sa.length > 1 && sa[0].trim().equals("Batch No"))	obj.setBatchNo(sa[1]);
//				if(sa.length > 1 && sa[0].trim().equals("Nozzle No"))	obj.setNozzleNo(sa[1]);
//				if(sa.length > 1 && sa[0].trim().equals("Gross Wt"))	obj.setGrossWt(sa[1]);
//				if(sa.length > 1 && sa[0].trim().equals("Tare Wt"))	obj.setTareWt(sa[1]);
//				if(sa.length > 1 && sa[0].trim().equals("Net Wt"))	obj.setNetWt(sa[1]);
//				if(sa[0].trim().equals("Status")) {
//					obj.setStatus(sa[1]);
//					obj = studyService.savePhysicalWeightBalanceData(obj);
//					obj = null;
//				}
//				
//				System.out.println(line);
//				sb.append(line);      //appends line to string buffer  
//				sb.append("\n");     //line feed   
//				if(map.get(ip) == 2) {
//					map.put(ip, 0);
//					break;
//				}
//			}  
//			
//			System.out.println(sb.toString());
//			socket.close();
//			}catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//				 map.put("192.168.0.159", 0);
//			}
//	}
	
	
	@RequestMapping(value="/viewInstrumentData", method=RequestMethod.GET)
	public String viewInstrumentData(HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		
		List<PhysicalWeightBalanceData> list = studyService.physicalWeightBalanceDataList();
		
		model.addAttribute("PageHedding", "Physical Balance");
		model.addAttribute("activeUrl", "radwag/dataLocation");
		model.addAttribute("list", list);
		return "physicalWeightBalanceDataView.tiles";

	}
	
	
	@RequestMapping(value="/currentData/{count}", method=RequestMethod.GET)
	public String viewCrf(@PathVariable("count") int count1, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if(RadwagController.count == count1) {
			model.addAttribute("result", count1 );
			return "result";
		}else {
			model.addAttribute("curentData", RadwagController.currentData );
			return "pages/physicalBalance/radwagCurrentData";
		}
		
	}
}
