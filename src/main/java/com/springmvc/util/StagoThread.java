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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazecast.jSerialComm.SerialPort;
import com.springmvc.controllers.StagoWebSocket;
import com.springmvc.model.ObservationInturmentConfiguration;
import com.springmvc.model.StagoData;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTestCodes;
import com.springmvc.model.SysmexTestCodeData;
import com.springmvc.service.StudyService;

public class StagoThread extends Thread {
	public static Long studyId = 0l;
	public static String sampleType = "";
	public static String portName = "";
	public static String test = "";
	public static String loatNo = "";
	public static Long userId;
	public static StudyService studyService;
	public static Map<Long, StagoThread> runningThreads = new HashMap<>();
//	public static String ip = "";
//	public static int port = 0;
	
	public static SortedMap<Integer, StudyAnimal> animalNos = new TreeMap<>();
	public static SortedMap<Integer, StudyAnimal> animalPerminentNos = new TreeMap<>();
	public static Map<Long, SortedMap<Integer, String>> testCodesOnlyMap = new TreeMap<>();
	
	public static Map<Long, List<StudyTestCodes>> observationWiseTestCodes = new HashMap<>();
	public static Map<Long, SortedMap<String, StudyTestCodes>> testCodes = new TreeMap<>();
	public static Map<Long, SortedMap<Integer, String>> testCodesMap = new TreeMap<>();
	public static Map<Long, SortedMap<Integer, String>> testCodesUnitsMap = new TreeMap<>();
	private ObservationInturmentConfiguration observationInturmentConfiguration;
	public static StudyMaster study = null;
	public static SerialPort comPort = null;
	public static boolean callConnection = false;
	public void run() {
		
		SerialPort[] ports = SerialPort.getCommPorts();
		System.out.println("Select a port:");
		int i = 1;
//		String portName = DBConnection.userPortName;
		comPort = null;
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
							System.out.println("Line : " + temp);
							List<String> each = splitSampleData(temp);
//							String[] each = temp.split("          ");
							
							Map<String, String> subjectTestValueMap = new HashMap<>(); 
							System.out.println("Each subject ----- ");
							for(String e : each) {
								if(e.contains("S") || e.contains("R") || e.contains("F")) {
									String sub = e.trim();
									String[] subData = sub.split(" ");
									List<String> eachData = new ArrayList<>();
									for(String as : subData) {
										if(!as.trim().equals("")) {
											eachData.add(as);
										}
									}
									subjectTestValueMap.put(eachData.get(0), eachData.get(1));
								}
								System.out.println(e);
							}
							
							List<StagoData> insDataList = new ArrayList<>();
							String data = temp.substring(1);
							StagoData sd = new StagoData();
							sd.setSampleType(StagoThread.sampleType);
							if(sd.getSampleType().equals("QC")) {
								sd.setLotNo(StagoThread.loatNo);
							}
							sd.setObservationInturmentConfiguration(observationInturmentConfiguration);
//							sd.setStudyNumber("");
							sd.setTestName(StagoThread.test);
							sd.setTestCode(StagoThread.testCodes.get(observationInturmentConfiguration.getId()).get(StagoThread.test));
							sd.setStdId(StagoThread.studyId + "");
							sd.setData(temp);
							for(Map.Entry<String, String> mp : subjectTestValueMap.entrySet()) {
								if(mp.getKey().contains("ST")) {
									sd.setInstrument(mp.getKey());
								}else {
									if(mp.getKey().contains("S")) {
										insDataList.add(eachResutl(sd, mp.getKey().replace("S", ""), mp.getValue()));
									}else if(mp.getKey().contains("R")) {
										insDataList.add(eachResutl(sd, mp.getKey().replace("R", ""), mp.getValue()));
									}else if(mp.getKey().contains("F")) {
										insDataList.add(eachResutl(sd, mp.getKey().replace("F", ""), mp.getValue()));
									}
								}
									
							}

							
							saveStagoDataInDB(insDataList, studyId);
							System.out.println(temp);
							try {

								SimpleDateFormat dateTimeSecondsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								StringBuffer sb = new StringBuffer();
								for(StagoData sdata : insDataList) {
									sb.append("<tr>");
									try {
										sb.append("<td>").append(study.getStudyNo()).append("</td>");
									}catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
										sb.append("<td>").append(studyId).append("</td>");
									}
									if(sdata.getStudyAnimal() != null) {
										if(observationInturmentConfiguration.getStudyAcclamatizationDates() != null)
											sb.append("<td>").append(sdata.getStudyAnimal().getAnimalNo()).append("</td>");
										else if(observationInturmentConfiguration.getStudyTreatmentDataDates() != null)
											sb.append("<td>").append(sdata.getStudyAnimal().getPermanentNo()).append("</td>");
										else
											sb.append("<td>").append(sdata.getAnimalNo()).append("</td>");
									}else
										sb.append("<td>").append(sdata.getAnimalNo()).append("</td>");
//									else sb.append("<td>").append(sdata.getLotNo()).append("</td>");
									if(sdata.getSampleType().equals("QC")) {
										sb.append("<td>").append("QC").append("</td>");
									}else {
										sb.append("<td>Animal</td>");
									}
									sb.append("<td>").append(dateTimeSecondsFormat.format(sdata.getReceivedTime())).append("</td>");
//									SortedMap<String, StudyTestCodes> testCodes = new TreeMap<>();
									sb.append("<td>").append(sdata.getTestCode().getTestCode().getDisPalyTestCode()).append("</td>");
									sb.append("<td>").append(sdata.getTestResult()).append("</td>");
//									for(Entry<Integer, String> tc : stagoTestCodesOnlyMap.entrySet()) {
//										if(test.equals(tc.getValue())) {
//											sb.append("<td>").append(sdata.getTestResult()).append("</td>");
//										}else {
//											sb.append("<td></td>");
//										}
//									}
//									if(test.equals("PT")) {
//										sb.append("<td>").append(sdata.getTestResult()).append("</td>");
//										sb.append("<td></td>");
//									}else if(test.equals("APTT")) {
//										sb.append("<td></td>");
//										sb.append("<td>").append(sdata.getTestResult()).append("</td>");
//									}else {
//										sb.append("<td></td>");
//										sb.append("<td></td>");
//									}
									
									
									sb.append("</tr>");
								}
							
								
								Map<String, String> m = new HashedMap();
								m.put("id", "200");
//								s.put("collected", "");
//								s.put("color", "blue");
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
								Map<String, SseEmitter> map = StagoWebSocket.stagoEmittersMap;
								for (Map.Entry<String, SseEmitter> emitters : map.entrySet()) {
									SseEmitter emitter = emitters.getValue();
									try {
										System.out.println(json);
										System.out.println(SseEmitter.event().name("stagoinsData"));
										emitter.send(SseEmitter.event().name("stagoinsData").data(json));
									} catch (IOException e) {
										e.printStackTrace();
										// TODO Auto-generated catch block
										StagoWebSocket.stagoEmittersMap.remove("superadmin");
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							;
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
//		comPort.closePort();

	}

	private List<String> splitSampleData(String temp) {
		// TODO Auto-generated method stub
		try {
			String[] eachAnimal = temp.split("        ");
			List<String> arr = new ArrayList<>();
			for(String animal : eachAnimal) {
				if(animal != null && !animal.trim().equals("")) {
					System.out.println(animal.trim());
					arr.add(animal.trim());
				}
			}
//			String[] split1 = temp.split("  ");
//			int index = 0;
//			for(String sp : split1) {
//				if(sp != null && !sp.trim().equals("")) { 
//					if(sp.contains("ST")) {
//						StringBuilder sb = new StringBuilder();
//						int count = 0, c = 0;
//						boolean flag = true;
//						while(count < 5) {
//							if(!split1[index+c].trim().equals("")) {
//								if(flag) {
//									sb.append(split1[index+c]);
//									flag = false;
//								}else
//									sb.append(" ").append(split1[index+c]);
//								count ++;
//							}
//							c++;
//						}
//						index = index+c;
//						if(sb.length() > 0) {
//							arr.add(sb.toString());
//						}
//					}else if(sp.contains("S") || sp.contains("R") || sp.contains("F")) {
//						StringBuilder sb = new StringBuilder();
//						int count = 0, c = 0;
//						boolean flag = true;
//						while(count < 4) {
//							if(!split1[index+c].trim().equals("") ) {
//								if(flag) {
//									sb.append(split1[index+c]);
//									flag = false;
//								}else
//									sb.append(" ").append(split1[index+c]);
//								count ++;
//							}
//							c++;
//						}
//						index = index+c;
//						if(sb.length() > 0) {
//							System.out.println(sb.toString());
//							arr.add(sb.toString());
//						}
//					}else 
//						index ++;
//				}else
//					index ++;
//			}
			return arr;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ArrayList<>();
		}

	}

	private String saveStagoDataInDB(List<StagoData> insDataList, Long studyId) {
		try {
			StagoDao dao = new StagoDao();
//			dao.getconnection();
			return dao.saveStagoData(insDataList, studyId);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "";
	}

	private StagoData eachResutl(StagoData sd, String currentAnimal, String value) {
		StagoData data = new StagoData();
		data.setStudy(study);
		data.setStdId(sd.getStdId());
		data.setSampleType(sd.getSampleType());
		data.setObservationInturmentConfiguration(sd.getObservationInturmentConfiguration());
		data.setLotNo(sd.getLotNo());
		try {
			if(sd.getSampleType().equals("Animal")) {
				if(observationInturmentConfiguration.getStudyAcclamatizationDates() != null) {
					data.setStudyAnimal(animalNos.get(Integer.parseInt(currentAnimal)));
				}else if(observationInturmentConfiguration.getStudyTreatmentDataDates() != null) {
					data.setStudyAnimal(animalPerminentNos.get(Integer.parseInt(currentAnimal)));
				}	
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		data.setTestCode(testCodes.get(observationInturmentConfiguration.getId()).get(sd.getTestName()));
		data.setTestName(sd.getTestName());
		data.setData(sd.getData());
		data.setInstrument(sd.getInstrument());
		data.setTar(sd.getTar());
		data.setDuriation(sd.getDuriation());
		data.setAfterDuriation(sd.getAfterDuriation());
		data.setNoOfAnimals(sd.getNoOfAnimals());
		data.setAnimalNo(Integer.parseInt(currentAnimal)+"");
		data.setTestResult(value);
		data.setReceivedTime(sd.getReceivedTime());
		return data;
	}

	public StagoThread() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StagoThread(ObservationInturmentConfiguration observationInturmentConfiguration) {
		super();
		this.observationInturmentConfiguration = observationInturmentConfiguration;
	}

	
}
