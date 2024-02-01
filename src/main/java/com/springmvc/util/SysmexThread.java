package com.springmvc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.covide.template.dto.SysmexDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springmvc.controllers.SysmexWebSocket;
import com.springmvc.dao.StudyDao;
import com.springmvc.model.ObservationInturmentConfiguration;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTestCodes;
import com.springmvc.model.SysmexData;
import com.springmvc.model.SysmexRawData;
import com.springmvc.model.SysmexTestCodeData;
import com.springmvc.model.TestCode;
import com.springmvc.service.StudyService;

public class SysmexThread extends Thread {
	@Autowired
	private Environment environment;
	public static Long studyId;
	public static StudyMaster study;
	
	public static Map<Long, SysmexThread> runningThreads = new HashMap<>();

//	StudyService studyService;
	public static StudyService studyService;
	public static StudyDao studyDao;
	public static Map<String, Integer> map = new HashMap<>();
	// static ServerSocket variable
	public static ServerSocket server;
	public static String ipAddress = "localhost";
	// socket server port on which it will listen
	public static int port = 8002;

	public static Map<Long, List<StudyTestCodes>> observationWiseTestCodes = new HashMap<>();
	public static Map<Long, SortedMap<String, StudyTestCodes>> testCodes = new TreeMap<>();
	public static Map<Long, SortedMap<Integer, String>> sysMaxTestCodesMap = new TreeMap<>();
	public static Map<Long, SortedMap<Integer, String>> sysMaxTestCodesUnitsMap = new TreeMap<>();
	public static Map<String, Long> animalCodes = new HashMap<>();

	private ObservationInturmentConfiguration observationInturmentConfiguration;
	private Long sysmaxDtaMaxId = 0l;
	private Long sysmaxRawDtaMaxId = 0l;
	private Long sysmexTestCodeDataMaxId = 0l;
	@SuppressWarnings("unlikely-arg-type")
	public void run() {
		try {
			/*
			 * //create the socket server object with ip InetAddress
			 * inetAddress=InetAddress.getByName("10.1.23.172"); server = new
			 * ServerSocket(); //Binding the SocketAddress with inetAddress and port
			 * SocketAddress endPoint=new InetSocketAddress(inetAddress, port); //bind()
			 * method the ServerSocket to the specified socket address
			 * server.bind(endPoint);
			 */
			InetAddress inetAddress = InetAddress.getByName(ipAddress);
			if (server != null && server.isBound()) {
				server.close();
			}
			server = new ServerSocket();
			// Binding the SocketAddress with inetAddress and port
			SocketAddress endPoint = new InetSocketAddress(inetAddress, port);
			// bind() method the ServerSocket to the specified socket address
			server.bind(endPoint);
			/*
			 * //create the socket server object with out ip server = new
			 * ServerSocket(port);
			 */

			SysmexDao dao = new SysmexDao();
			sysmaxDtaMaxId = dao.sysmexDataMaxId();
			sysmaxRawDtaMaxId = dao.sysmaxRawDtaMaxId();
			sysmexTestCodeDataMaxId = dao.sysmexTestCodeDataMaxId();
			// keep listens indefinitely until receives 'exit' call or program terminates
			while (true) {
				System.out.println("Waiting for the client request");
				// creating socket and waiting for client connection
				Socket socket = server.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//	            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				System.out.println(in);
//	            while(in.ready()) {
//			    	System.out.println(in.readLine());
//			    }
//	            String line =  in.lines().collect(Collectors.joining());
				List<String> data = new ArrayList<>();
				StringBuffer line = new StringBuffer();
				line.append(in.readLine());
				data.add(line.toString());
				System.out.println("Start : " + line);
				int length = 0;

				while (!line.equals("")) {
					length = length + line.length();
					String s = in.readLine();
					System.out.println("INLine : " + s);
					line.append(s);
					data.add(s);
					if (s.trim().equals("L|1|N")) {
						SysmexDto dto  = saveSysmexData(data, line.toString(), SysmexThread.studyId);
						dto.getSysData().setStudy(SysmexThread.study);
						pushToClient(dto);
						line = new StringBuffer();
						data = new ArrayList<>();
					}
				}
				System.out.println("All : " + line);
				System.out.println(in);

				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				// write object to Socket
				oos.writeObject("Hi Sysmex we have received Data successfully. status code : " + 200);
				// close resources
				in.close();
				oos.close();
				socket.close();
				// terminate the server if client sends exit request

			}

		}catch (java.net.SocketException e) {
			// TODO: handle exception
			e.printStackTrace();
			
		} catch (IOException ioe) {
			// TODO: handle exception
			ioe.printStackTrace();
		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
		} finally {
//			try {
//				server.close();
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
		}

	}
	
	private SysmexDto saveSysmexData(List<String> data, String text, Long studyId) {
		boolean fg = true;
		SysmexDto dto = new SysmexDto();
		SimpleDateFormat dateTimeSecondsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SysmexRawData rawData = new SysmexRawData();
		rawData.setId(++sysmaxRawDtaMaxId);
		rawData.setRawData(text);

		SysmexData sysData = new SysmexData();
		rawData.setSysmexData(sysData);
		sysData.setObservationInturmentConfiguration(observationInturmentConfiguration);
		sysData.setId(++sysmaxDtaMaxId);
		List<SysmexTestCodeData> perameters = new ArrayList<>();
		System.out.println("-------------------------------------------------------------------------" + data.size());
		for (String sl : data) {
			System.out.println(sl);
		}
		System.out.println("-------------------------------------------------------------------------");
		boolean flag = false;
		Date startTime = null;
		Date endTime = null;
		SortedMap<String, StudyTestCodes> tcs = testCodes.get(observationInturmentConfiguration.getId());
		for (Map.Entry<String, StudyTestCodes> s : tcs.entrySet()) {
			System.out.println(s.getKey() + "\t" + s.getValue().getTestCode().getTestCode());
		}
		for (int i = 0; i < data.size(); i++) {
			String line = data.get(i);
			if (i < data.size() - 2) {
				if (flag) {
					SysmexTestCodeData sysmexTestCodeData = new SysmexTestCodeData();
					sysmexTestCodeData.setId(++sysmexTestCodeDataMaxId); 
					sysmexTestCodeData.setSysmexData(sysData);
					String[] st = line.split("\\|");
					sysmexTestCodeData.setCode(st[0]);
					sysmexTestCodeData.setSeqNo(Integer.parseInt(st[1]));

					String[] s = st[2].split("\\^1");
					sysmexTestCodeData.setTestCode(s[0].replaceAll("\\^", "").trim());
					sysmexTestCodeData.setStudyTestCode(tcs.get(sysmexTestCodeData.getTestCode()));
					if (sysmexTestCodeData.getStudyTestCode() != null) {
						sysmexTestCodeData.setOrderNo(sysmexTestCodeData.getStudyTestCode().getOrderNo());
					}
//					sysmexTestCodeData.setTestCodeNo(SysmexTestCodeNos.tcAndNo.get(sysmexTestCodeData.getTestCode()));
					sysmexTestCodeData.setValue(st[3]);
					sysmexTestCodeData.setUnits(st[4]);
					sysmexTestCodeData.setCode1(st[5]);
					sysmexTestCodeData.setCode2(st[6]);
					String date = st[st.length - 1];
					String ddmmyy = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + " "
							+ date.substring(8, 10) + ":" + date.substring(10, 12) + ":" + date.substring(12, 14);
					try {
						System.out.println("Run time : " + ddmmyy + " \t" + dateTimeSecondsFormat.parse(ddmmyy));
						sysmexTestCodeData.setRunTime(dateTimeSecondsFormat.parse(ddmmyy));

						if (startTime == null) {
							startTime = sysmexTestCodeData.getRunTime();
							endTime = sysmexTestCodeData.getRunTime();
						} else {
							if (startTime.getTime() > sysmexTestCodeData.getRunTime().getTime())
								startTime = sysmexTestCodeData.getRunTime();
							if (endTime.getTime() < sysmexTestCodeData.getRunTime().getTime())
								endTime = sysmexTestCodeData.getRunTime();
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					perameters.add(sysmexTestCodeData);
				} else {
					switch (i) {
					case 0:
						try {
							String st = line.replaceAll(" ", ",");
							String[] ss = st.split("\\,");
							for (int j = 1; j < ss.length; j++) {
								String s = ss[j];
								if (!s.trim().equals("")) {
									sysData.setInstumentModelNo(s.substring(0, s.indexOf('^')));
									s = s.substring(s.indexOf('^') + 1);
									sysData.setInstumentYear(s.substring(0, s.indexOf('^')));
									s = s.substring(s.indexOf('^') + 1);
									sysData.setInstrumentSno(s.substring(0, s.indexOf('^')));
									s = s.substring(s.indexOf('^') + 1);
									s = s.replaceAll("\\^", ""); /// not working
									sysData.setInstrumentReferenceNo(s.substring(0, s.indexOf('|')));
									s = s.substring(s.indexOf('|') + 1);
									s = s.replaceAll("\\|", "");
									sysData.setInstrumentRemainNo(s);
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						break;
					case 1:
						break;
					case 2:
						break;
					case 3:
						try {
							String s = line;
							if (s.contains("QC-")) {
								sysData.setTestRunType("QC");
							} else if (s.contains("BACKGROUNDCHECK")) {
								sysData.setTestRunType("BACKGROUNDCHECK");
							} else if (s.contains("CAL-")) {
								sysData.setTestRunType("CAL");
							} else if (s.contains("SEN-")) {
								sysData.setTestRunType("SEN");
							}
							if (line.contains("O|1||^^")) {
								s = line.replace("O|1||^^", "").trim();
							} else if (line.contains("QC")) {
								s = line.substring(line.indexOf("QC-"));

							}

							sysData.setAnimalNumber(s.substring(0, s.indexOf('^')));
							s = s.substring(s.indexOf('^') + 1);
//							sysData.setGender(s.substring(0, s.indexOf('^')));
							s = s.substring(s.indexOf('^') + 1);
							sysData.setAnimalCode(s.substring(0, s.indexOf('|')));
							s = s.substring(1);
							s = s.replaceAll("\\^^^^", ",");
//							String[] perameters = s.split("\\,");

						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case 4:
						try {
							String s = "";
							if (line.contains("FC|1||")) {
								s = line.replace("FC|1||", "").trim();
							} else {
								s = line.replace("C|1||", "").trim();
							}

//							String s = line.replace("C|1||", "").trim();
							// swami exception
							// java.lang.StringIndexOutOfBoundsException: String index out of range: -1
							if (!s.equals("")) {
								if (s.contains("(") && s.contains(")")) {
									sysData.setStudyNumber(s.substring(0, s.indexOf('(')));
									sysData.setTimePoint(s.substring(s.indexOf('(')));
								} else {
									sysData.setStudyNumber(s);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						flag = true;
						break;
					}
				}
			} else {
				if (fg) {
					sysData.setStartTime(startTime);
					sysData.setEndTime(endTime);
					dto.setObservationInturmentConfiguration(observationInturmentConfiguration);
					dto.setSysData(sysData);
					dto.setSysmexRawData(rawData);
					dto.setSysmexTestCodeDatas(perameters);
					dto = saveSysmexDataInDB(dto);
					fg = false;
				}

			}

		}
		return dto;
	}

	private SysmexDto saveSysmexDataInDB(SysmexDto dto) {
		// TODO Auto-generated method stub
		try {
			SysmexDao dao = new SysmexDao();
			dao.saveSysmexDto(dto);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return dto;
	}

	private void pushToClient(SysmexDto dto) {
		try {
			
			SortedMap<String, StudyTestCodes> tecs = testCodes.get(observationInturmentConfiguration.getId());
			SimpleDateFormat dateTimeSecondsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuffer sb = new StringBuffer();
			sb.append("<tr>");
			sb.append("<th>").append(dto.getSysData().getStudy().getStudyNo()).append("</th>");
			sb.append("<th>").append(dto.getSysData().getAnimalNumber()).append("</th>");
//			sb.append("<th>").append(dto.getSysData().getGender()).append("</th>");
			sb.append("<th>").append(dateTimeSecondsFormat.format(dto.getSysData().getStartTime())).append("</th>");
			SortedMap<Integer, SysmexTestCodeData> testCodesValues = new TreeMap<>();
			List<SysmexTestCodeData> sysmexTestCodeDatas = dto.getSysmexTestCodeDatas();
			for (SysmexTestCodeData sd : sysmexTestCodeDatas) {
				if (sd.getUnits() != null && !sd.getUnits().trim().equals("")) {
					if (tecs.containsKey(sd.getTestCode())) {
						sb.append("<td id=\"" + sd.getOrderNo() + 2 + "\">" + sd.getValue() + "</td>");
						testCodesValues.put(sd.getOrderNo() + 2, sd);
//					sb.append("<td>").append(sd.getTestCode()+"  "+sd.getValue()).append("</td>");
					}
				}
			}

////			List<SysmexTestCodeData> sysmexTestCodeResult = dto.getSysmexTestCodeDatas();
//			for(Map.Entry<Integer, String> mp : SysmexThread.sysMaxTestCodesMap.entrySet()) {
////				testCodesValues.put(mp.getKey(), testCodesValues.get(mp.getKey()));
//				if(testCodesValues.get(mp.getKey()) != null) {
//					sb.append("<td id=\""+mp.getKey()+"\">"+testCodesValues.get(mp.getKey()).getValue()+"</td>");
//				}
//				
//			}
			sb.append("</tr>");
			System.out.println(sb.toString());
			Map<String, String> s = new HashedMap();
			s.put("id", "1");
//			s.put("collected", "");
//			s.put("color", "blue");
			s.put("status", "200");
			s.put("dataFrom", sb.toString());
			s.put("ipAddress", ipAddress + "/" + port);

			String json = "";
			try {
				json = new ObjectMapper().writeValueAsString(s);
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Map<String, SseEmitter> map = SysmexWebSocket.sysmexEmittersMap;
			for (Map.Entry<String, SseEmitter> emitters : map.entrySet()) {
				System.out.println(emitters.getKey());
				SseEmitter emitter = emitters.getValue();
				try {
					emitter.send(SseEmitter.event().name("sysmesinsData").data(json));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
//					SysmexWebSocket.sysmexEmittersMap.remove("superadmin");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public SysmexThread(ObservationInturmentConfiguration observationInturmentConfiguration) {
		super();
		this.observationInturmentConfiguration = observationInturmentConfiguration;
	}

	public SysmexThread() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
