package com.springmvc.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazecast.jSerialComm.SerialPort;
import com.springmvc.controllers.StagoWebSocket;
import com.springmvc.controllers.VistrosWebSocket;
import com.springmvc.dao.StudyDao;
import com.springmvc.model.ObservationInturmentConfiguration;
import com.springmvc.model.StagoData;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTestCodes;
import com.springmvc.model.SysmexTestCodeData;
import com.springmvc.model.VitrosData;
import com.springmvc.service.StudyService;

public class VistrosThread extends Thread {
	public static Long studyId = 0l;
	public static StudyMaster study;
	public static String test = "";
//	public static StudyService studyService;
	public static StudyDao studyDao;
	public static Map<Long, VistrosThread> runningThreads = new HashMap<>();
	public static boolean exit = false;
	public static Map<Long, SortedMap<Integer, String>> instruemntTestCodesMap = new TreeMap<>();
	public static Map<String, Long> animalCodes = new HashMap<>();
	
	public static SortedMap<String, StudyAnimal> animalNos = new TreeMap<>();
	public static SortedMap<String, StudyAnimal> animalPerminentNos = new TreeMap<>();
	public static Map<Long, List<StudyTestCodes>> observationWiseTestCodes = new HashMap<>();
	public static Map<Long, SortedMap<String, StudyTestCodes>> testCodes = new TreeMap<>();
	public static Map<Long, SortedMap<Integer, String>> testCodesMap = new TreeMap<>();
	public static Map<Long, SortedMap<Integer, String>> testCodesUnitsMap = new TreeMap<>();
	
	private ObservationInturmentConfiguration observationInturmentConfiguration;
	@SuppressWarnings("static-access")
	public void run() {
		while(true) {
			displayVitosData();
			try {
				this.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			if(exit)
//				break;
		}
	}
	
	public void displayVitosData() {
		SimpleDateFormat dateTimeSecondsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("dddddddddddddd");
		List<VitrosData> data = studyDao.onlineVitrosData();
		Map<String, Map<String, List<VitrosData>>> animalWise = new HashMap<>();
		for(VitrosData vd : data) {
			vd.setObservationInturmentConfiguration(observationInturmentConfiguration);
			Map<String, List<VitrosData>> dateWise = animalWise.get(vd.getAnimalNo());
			if(dateWise == null) dateWise = new HashMap<>();
			List<VitrosData> dataList = dateWise.get(dateTimeSecondsFormat.format(vd.getTestDate()));
			if(dataList == null) dataList = new ArrayList<>();
			dataList.add(vd);
			dateWise.put(dateTimeSecondsFormat.format(vd.getTestDate()), dataList);

			if(observationInturmentConfiguration.getStudyAcclamatizationDates() != null) {
				vd.setAnimal(animalNos.get(vd.getAnimalNo()));
				if(vd.getAnimal() != null) {
					vd.setAnimalNo(vd.getAnimal().getAnimalNo());			
				}
			}else if(observationInturmentConfiguration.getStudyTreatmentDataDates() != null) {
				vd.setAnimal(animalPerminentNos.get(vd.getAnimalNo()));
				if(vd.getAnimal() != null) {
					vd.setAnimalNo(vd.getAnimal().getPermanentNo());			
				}
			}
			animalWise.put(vd.getAnimalNo(), dateWise);
		}
		
		
		StringBuffer sb = new StringBuffer();
		for(Map.Entry<String, Map<String, List<VitrosData>>> mp : animalWise.entrySet()) {
			Map<String, List<VitrosData>> dateWise = mp.getValue();
			for(Map.Entry<String, List<VitrosData>> date : dateWise.entrySet()) {
				sb.append("<tr>");
				sb.append("<th>").append(study.getStudyNo()).append("</th>");
				sb.append("<th>").append(mp.getKey()).append("</th>");
				sb.append("<th>").append(date.getKey()).append("</th>");
				List<VitrosData> cds = date.getValue();
				Map<String, VitrosData> dataMap = new HashMap<>();
				for(VitrosData vd : cds) {
					dataMap.put(vd.getTestName(), vd);
				}
				SortedMap<Integer, String> orderTestCodesMap = VistrosThread.instruemntTestCodesMap.get(observationInturmentConfiguration.getId());
				for(Entry<Integer, String> m : orderTestCodesMap.entrySet()) {
					if(dataMap.get(m.getValue()) != null) {
						sb.append("<td id=\""+m.getKey()+2+"\">"+dataMap.get(m.getValue()).getResult()+"</td>");
					}else
						sb.append("<td id=\""+m.getKey()+2+"\"></td>");
				}
				sb.append("</tr>");	
			}
		}

		studyDao.updateAsOnline(data);
		System.out.println(sb.toString());
		Map<String, String> m = new HashedMap();
		m.put("id", "200");
//		s.put("collected", "");
//		s.put("color", "blue");
		m.put("status", "200");
		m.put("dataFrom", sb.toString());
		m.put("portNo", "200");
		String json = "";
		try {
			json = new ObjectMapper().writeValueAsString(m);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Map<String, SseEmitter> map = VistrosWebSocket.stagoEmittersMap;
		for (Map.Entry<String, SseEmitter> emitters : map.entrySet()) {
			SseEmitter emitter = emitters.getValue();
			try {
				System.out.println(json);
				emitter.send(SseEmitter.event().name("vistrosData").data(json));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				VistrosWebSocket.stagoEmittersMap.remove("superadmin");
			}
		}
	}

	public VistrosThread() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VistrosThread(ObservationInturmentConfiguration observationInturmentConfiguration) {
		super();
		this.observationInturmentConfiguration = observationInturmentConfiguration;
	}
	
	
}
