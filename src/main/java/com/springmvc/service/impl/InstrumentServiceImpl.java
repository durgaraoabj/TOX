package com.springmvc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.Instrument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.covide.crf.dto.CrfSectionElementInstrumentValue;
import com.covide.dto.StudyDto;
import com.covide.enums.StatusMasterCodes;
import com.covide.template.dto.TestCodesUnitsDto;
import com.springmvc.controllers.ScheduledTasks;
import com.springmvc.dao.InstrumentDao;
import com.springmvc.dao.StatusDao;
import com.springmvc.dao.StudyDao;
import com.springmvc.dao.impl.InstrumentIpAddressDaoImpl;
import com.springmvc.dao.impl.ObservationInturmentConfigurationDaoImpl;
import com.springmvc.dao.impl.StudyAcclamatizationDatesDaoImpl;
import com.springmvc.dao.impl.StudyTestCodesDaoImple;
import com.springmvc.dao.impl.StudyTestCodesImpl;
import com.springmvc.model.CongulometerInstrumentValues;
import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.ObservationInturmentConfiguration;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTestCodes;
import com.springmvc.model.SysmaxInstrumentValues;
import com.springmvc.model.SysmexAnimalCode;
import com.springmvc.service.ExpermentalDesignService;
import com.springmvc.service.InstrumentService;
import com.springmvc.util.StagoThread;
import com.springmvc.util.SysmexThread;
import com.springmvc.util.VistrosThread;

@Service("instrumentService")
public class InstrumentServiceImpl implements InstrumentService {
	public static Long ipAddressPk = 0l;
	@Autowired
	private InstrumentDao instrumentDao;
	
	@Autowired
	private InstrumentIpAddressDaoImpl instrumentIpAddressDao;
	@Autowired
	private StatusDao statusDao;
	@Autowired
	StudyDao studyDao;
	@Autowired
	StudyAcclamatizationDatesDaoImpl studyAcclamatizationDatesDao;
	@Autowired
	ObservationInturmentConfigurationDaoImpl observationInturmentConfigurationDao;
	@Autowired
	StudyTestCodesImpl studyTestCodesDao;

	@Autowired
	private ExpermentalDesignService expermentalDesignService;
	@Override
	public List<CongulometerInstrumentValues> getCongulometerInstrumentValuesWithStudyId(Long studyId) {
		// TODO Auto-generated method stub
		return instrumentDao.getCongulometerInstrumentValuesWithStudyId(studyId);
	}

	@Override
	public List<CrfSectionElementInstrumentValue> getCrfSectionElementInstrumentValueList() {
		return instrumentDao.getCrfSectionElementInstrumentValueList();
	}

	@Override
	public List<SysmaxInstrumentValues> getSysmaxInstrumentValuesWithStudyId(Long studyId) {
		return instrumentDao.getSysmaxInstrumentValuesWithStudyId(studyId);
	}

	@Override
	public String instrumentDataCaptur(Long studyAcclamatizationDateId, String selecteDate, HttpServletRequest request,
			ModelMap model, String serviceType) {
		// TODO Auto-generated method stub
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StatusMaster activeStatus = statusDao.statusMaster(StatusMasterCodes.ACTIVE.toString());
//		List<InstrumentIpAddress> ipAddress = new ArrayList<>();
		List<InstrumentIpAddress> dbipAddress = studyDao.instrumentIpAddresses(activeStatus.getId(), true);
		List<StudyTestCodes> stagoTestCodes = new ArrayList<>();
		for (InstrumentIpAddress ip : dbipAddress) {
			if (ip.getInstrumentName().equals("STAGO")) {
				stagoTestCodes = studyDao.studyInstumentTestCodes(ip.getId(), activeStudyId, null);
			}
		}
		model.addAttribute("stagoTestCodes", stagoTestCodes);
		model.addAttribute("ipAddress", dbipAddress);
		List<StudyMaster> studyList = studyDao.allActiveStudys();
		model.addAttribute("studyList", studyList);
		StudyMaster sm = studyDao.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		model.addAttribute("ipAddressPk", ipAddressPk);
		model.addAttribute("studys", studyDao.findAll());
		model.addAttribute("serviceType", serviceType);

		ScheduledTasks sc = new ScheduledTasks();
		sc.reportCurrentTime();
		return "readInsturmentDataCapture";
	}

	@Override
	public String instrumentDataCapturPage(Long studyAcclamatizationDateId, Long studyTreatmentDataDatesId, String selecteDate,
			HttpServletRequest request, ModelMap model, String serviceType) {
		ObservationInturmentConfiguration observationInturmentConfiguration = observationInturmentConfigurationDao
				.observationInturmentConfigurationOfstudyAcclamatizationDates(studyAcclamatizationDateId, studyTreatmentDataDatesId);
		List<InstrumentIpAddress> instruments = studyTestCodesDao
				.instrumentIpAddressesOfObservation(observationInturmentConfiguration.getId());
		model.addAttribute("instruments", instruments);
		model.addAttribute("observation", observationInturmentConfiguration);
		
		InstrumentIpAddress ipaddress = instrumentIpAddressDao.instrumentIpAddressByName("STAGO");
		List<StudyTestCodes> testCodes = studyTestCodesDao.testCodesOfInstrumentAndObservation(observationInturmentConfiguration, ipaddress);
		model.addAttribute("stagoTestCodes", testCodes);
//		ScheduledTasks sc = new ScheduledTasks();
//		sc.reportCurrentTime();
		return "readInsturmentDataCapturePage";
	}

	@Override
	public InstrumentIpAddress instrumentIpAddress(Long instumentId) {
		// TODO Auto-generated method stub
		return instrumentIpAddressDao.getByKey(instumentId);
	}

	@Override
	public ObservationInturmentConfiguration observationInturmentConfiguration(Long observationId) {
		// TODO Auto-generated method stub
		return observationInturmentConfigurationDao.getByKey(observationId);
	}

	@Override
	public TestCodesUnitsDto callSysmexService(Long studyId, Long userId, InstrumentIpAddress ip, Long observationId,
			Long instumentid) {
		if(instumentid > 0) {
//			kill remain instrument services
		}

		TestCodesUnitsDto dto = new TestCodesUnitsDto();

		// TODO Auto-generated method stub
		ObservationInturmentConfiguration observationInturmentConfiguration = observationInturmentConfiguration(observationId);	
		try {
			boolean flag = stopSysmexService(userId);
			SysmexThread.studyId = studyId;
//			SysmexThread.studyService = studyService;
			SysmexThread.studyDao = studyDao;
			SysmexThread.port = ip.getPortNo();
			SysmexThread.ipAddress = ip.getIpAddress();
			SysmexThread.study = observationInturmentConfiguration.getStudy();
			List<SysmexAnimalCode> codes = expermentalDesignService.sysmexAnimalCodes();
			for(SysmexAnimalCode code : codes) {
				SysmexThread.animalCodes.put(code.getCode(), code.getId());
			}
//			if(SysmexThread.testCodes.get(observationId) == null) {
				sysmexHeadderData(ip, studyId, observationInturmentConfiguration);
//			}
			if (SysmexThread.server != null && !SysmexThread.server.isClosed()) {
				SysmexThread.server.close();
			}
			SysmexThread thread = new SysmexThread(observationInturmentConfiguration);
			thread.start();
			SysmexThread.runningThreads.put(userId, thread);
			dto.setMessage("Connection establish Succussfully");
//			SocketSysmax.runSysmexServier();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		dto.setTestCodes(SysmexThread.testCodes);
		dto.setTestCodesUnitsMap(SysmexThread.sysMaxTestCodesUnitsMap);
		dto.setTestCodesMap(SysmexThread.sysMaxTestCodesMap);
		return dto;
	}
	
	

	@Override
	public TestCodesUnitsDto callVitrosService(Long studyId, Long userId, InstrumentIpAddress ip,
			Long observationId, Long instumentid) {
		if(instumentid > 0) {
//			kill remain instrument services
		}
		// TODO Auto-generated method stub
		ObservationInturmentConfiguration observationInturmentConfiguration = observationInturmentConfiguration(observationId);	
		try {
			stopVitrosService(userId);
			try {
				VistrosThread.study = observationInturmentConfiguration.getStudy();
				VistrosThread.studyId = studyId;
				VistrosThread.studyDao = studyDao;
//				if (VistrosThread.testCodes.size() == 0) {
					vitrosHeadderData(ip, studyId, observationInturmentConfiguration);
//				}

				VistrosThread thread = new VistrosThread(observationInturmentConfiguration);
				thread.start();
				VistrosThread.runningThreads.put(userId, thread);
//				SocketSysmax.runSysmexServier();
				TestCodesUnitsDto dto = new TestCodesUnitsDto();
//				dto.setTestCodes(VistrosThread.testCodes);
				dto.setTestCodesUnitsMap(VistrosThread.testCodesUnitsMap);
				dto.setTestCodesMap(VistrosThread.testCodesMap);
				dto.setMessage("Connection establish Succussfully");
				return dto;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new TestCodesUnitsDto();
	}

	
	@Override
	public StudyDto callStagoService(Long studyId, Long userId, InstrumentIpAddress ip, Long observationId,
			Long instumentid, String sampleType, String loatNo, String test) {
		if(instumentid > 0) {
//			kill remain instrument services
		}
		// TODO Auto-generated method stub
		ObservationInturmentConfiguration observationInturmentConfiguration = observationInturmentConfiguration(observationId);	
		
		StudyDto dto = new StudyDto();
		dto.setStudys(studyDao.findAll());

//		if (StagoThread.testCodes.size() == 0) {
			stagoHeadderData(ip, studyId, sampleType, loatNo, observationInturmentConfiguration);
//		}
//		dto.setTestCodes(StagoThread.testCodes.get(observationId));
		dto.setStagoTestCodesMap(StagoThread.testCodesMap.get(observationId));
		dto.setStagoTestCodesUnitsMap(StagoThread.testCodesUnitsMap.get(observationId));
		StagoThread.study = observationInturmentConfiguration.getStudy();
		
		try {
			stopStagoService(userId);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			StagoThread.studyId = studyId;
			StagoThread.sampleType = sampleType;
			StagoThread.portName = ip.getComPortNo();
			StagoThread.test = test;
			StagoThread.loatNo = loatNo;
			StagoThread.userId = userId;
			StagoThread thread = new StagoThread(observationInturmentConfiguration);
			thread.start();
			StagoThread.runningThreads.put(userId, thread);
//			SocketSysmax.runSysmexServier();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dto;
	}

	private void stagoHeadderData(InstrumentIpAddress ip, Long studyId, String sampleType, String loatNo, ObservationInturmentConfiguration observationInturmentConfiguration) {
		SortedMap<String, StudyTestCodes> tcs = new TreeMap<>(); 
		SortedMap<Integer, String> testCodesMap = new TreeMap<>();
		SortedMap<Integer, String> testCodesOnlyMap = new TreeMap<>();
		SortedMap<Integer, String> testCodesUnitsMap = new TreeMap<>();
		testCodesMap.put(0, "Study No.");
		testCodesMap.put(1, "Animal No");
		testCodesMap.put(2, "Lot No");
		testCodesMap.put(3, "Run Time");
		testCodesMap.put(4, "Test(sec)");
		testCodesMap.put(5, "Result");
		List<StudyTestCodes> testCodes = studyDao.studyObservationInstumentTestCodes(observationInturmentConfiguration, ip.getId(), studyId);
		for (StudyTestCodes tc : testCodes) {
			testCodesOnlyMap.put(tc.getOrderNo(), tc.getTestCode().getTestCode());
			testCodesMap.put(tc.getOrderNo(), tc.getTestCode().getDisPalyTestCode());
			tcs.put(tc.getTestCode().getTestCode(), tc);
			testCodesUnitsMap.put(tc.getOrderNo() + 2,
					tc.getTestCode().getTestCodeUints().getDisplayUnit());
		}
		
		List<StudyAnimal> animals = studyDao.studyAnimals(studyId);
		SortedMap<Integer, StudyAnimal> accessionNumbersMap = new TreeMap<>();
		SortedMap<Integer, StudyAnimal> treatmentAnimalNosMap = new TreeMap<>();
		for (StudyAnimal animal : animals) {
			if(observationInturmentConfiguration.getStudyAcclamatizationDates() != null)
				accessionNumbersMap.put(animal.getAnimalId(), animal);
			else if(observationInturmentConfiguration.getStudyTreatmentDataDates() != null)
				treatmentAnimalNosMap.put(animal.getSequnceNo(), animal);
		}
		StagoThread.animalNos = accessionNumbersMap;
		StagoThread.animalPerminentNos = treatmentAnimalNosMap;
		StagoThread.testCodes.put(observationInturmentConfiguration.getId(), tcs);
		StagoThread.testCodesMap.put(observationInturmentConfiguration.getId(), testCodesMap);
		StagoThread.testCodesOnlyMap.put(observationInturmentConfiguration.getId(), testCodesOnlyMap);
	}
	private void vitrosHeadderData(InstrumentIpAddress ip, Long studyId, ObservationInturmentConfiguration observationInturmentConfiguration) {
		SortedMap<String, StudyTestCodes> tcs = new TreeMap<>(); 
		SortedMap<Integer, String> testCodesMap = new TreeMap<>();
		SortedMap<Integer, String> testCodesUnitsMap = new TreeMap<>();
		SortedMap<Integer, String> instruemntTestCodesMap = new TreeMap<>();
		testCodesMap.put(0, "Study No.");
		testCodesMap.put(1, "Animal No");
		testCodesMap.put(2, "Run Time");
		testCodesUnitsMap.put(0, "");
		testCodesUnitsMap.put(1, "");
		testCodesUnitsMap.put(2, "");
		List<StudyTestCodes> testCodes = studyDao.studyObservationInstumentTestCodes(observationInturmentConfiguration, ip.getId(), studyId);
		for (StudyTestCodes tc : testCodes) {
			tcs.put(tc.getTestCode().getTestCode(), tc);
			testCodesMap.put(tc.getOrderNo() + 2, tc.getTestCode().getDisPalyTestCode());
			instruemntTestCodesMap.put(tc.getOrderNo() + 2, tc.getTestCode().getTestCode());
			testCodesUnitsMap.put(tc.getOrderNo() + 2,
					tc.getTestCode().getTestCodeUints().getDisplayUnit());
		}
		
		List<StudyAnimal> animals = studyDao.studyAnimals(studyId);
		SortedMap<String, StudyAnimal> accessionNumbersMap = new TreeMap<>();
		SortedMap<String, StudyAnimal> treatmentAnimalNosMap = new TreeMap<>();
		for (StudyAnimal animal : animals) {
			if(observationInturmentConfiguration.getStudyAcclamatizationDates() != null)
				accessionNumbersMap.put(animal.getAnimalNo(), animal);
			else if(observationInturmentConfiguration.getStudyTreatmentDataDates() != null) {
				System.out.println(animal.getId());
				if(animal.getPermanentNo() != null)
					treatmentAnimalNosMap.put(animal.getPermanentNo(), animal);
			}
		}
		VistrosThread.animalNos = accessionNumbersMap;
		VistrosThread.animalPerminentNos = treatmentAnimalNosMap;
		VistrosThread.observationWiseTestCodes.put(observationInturmentConfiguration.getId(), testCodes);
		VistrosThread.testCodes.put(observationInturmentConfiguration.getId(), tcs);
		VistrosThread.testCodesMap.put(observationInturmentConfiguration.getId(), testCodesMap);
		VistrosThread.testCodesUnitsMap.put(observationInturmentConfiguration.getId(), testCodesUnitsMap);
		VistrosThread.instruemntTestCodesMap.put(observationInturmentConfiguration.getId(), instruemntTestCodesMap);
	}
	
	private void stopVitrosService(Long userId) {
		// TODO Auto-generated method stub
		try {
			VistrosThread.study = null;
			VistrosThread.studyId = 0l;
			VistrosThread.exit = true;
			VistrosThread thread = VistrosThread.runningThreads.get(userId);
			if (thread != null) {
				if (thread.isAlive())
					thread.interrupt();
				thread = null;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private void sysmexHeadderData(InstrumentIpAddress ip, Long studyId, ObservationInturmentConfiguration observationInturmentConfiguration) {
		
		SortedMap<String, StudyTestCodes> tcs = new TreeMap<>(); 
		SortedMap<Integer, String> testCodesMap = new TreeMap<>();
		SortedMap<Integer, String> testCodesUnitsMap = new TreeMap<>();
		
		testCodesMap.put(0, "Study No.");
		testCodesMap.put(1, "Animal No");
		testCodesMap.put(2, "Run Time");
		testCodesUnitsMap.put(0, "");
		testCodesUnitsMap.put(1, "");
		testCodesUnitsMap.put(2, "");

		
		List<StudyTestCodes> testCodes = studyDao.studyObservationInstumentTestCodes(observationInturmentConfiguration, ip.getId(), studyId);		
//		List<StudyTestCodes> testCodes = studyDao.studyInstumentTestCodes(ip.getId(), studyId);
		if(testCodes.size() > 0) {	
			for (StudyTestCodes tc : testCodes) {
				tcs.put(tc.getTestCode().getTestCode(), tc);
				testCodesMap.put(tc.getOrderNo() + 2, tc.getTestCode().getDisPalyTestCode());
				testCodesUnitsMap.put(tc.getOrderNo() + 2,
						tc.getTestCode().getTestCodeUints().getDisplayUnit());
			}			
		}
		
		SysmexThread.observationWiseTestCodes.put(observationInturmentConfiguration.getId(), testCodes);
		SysmexThread.testCodes.put(observationInturmentConfiguration.getId(), tcs);
		SysmexThread.sysMaxTestCodesMap.put(observationInturmentConfiguration.getId(), testCodesMap);
		SysmexThread.sysMaxTestCodesUnitsMap.put(observationInturmentConfiguration.getId(), testCodesUnitsMap);
	}
	private boolean stopSysmexService(Long userId) {
		// TODO Auto-generated method stub
		try {
			SysmexThread.studyId = 0l;
			SysmexThread.studyService = null;
			SysmexThread.ipAddress = "";
			SysmexThread.testCodes = new TreeMap<>();
			SysmexThread.study = null;
			SysmexThread thread = SysmexThread.runningThreads.get(userId);
			if (thread != null) {
				if (thread.isAlive()) {
					thread.interrupt();
//					return true;
				}
				thread = null;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	
	private void stopStagoService(Long userId) {
		// TODO Auto-generated method stub
		try {
			try {
				StagoThread.comPort.closePort();
			}catch (Exception e) {
				// TODO: handle exception
//				e.printStackTrace();
			}
			StagoThread.portName = "";
			StagoThread.studyId = 0l;
			StagoThread.test = "";
//			Map<Long, StagoThread> threads = StagoThread.runningThreads;
			StagoThread thread = StagoThread.runningThreads.get(userId);
			if (thread != null) {
				if (thread.isAlive())
					thread.interrupt();
				thread = null;
			}

		} catch (Exception e) {
			// TODO: handle exception
//			e.printStackTrace();
		}
	}
}
