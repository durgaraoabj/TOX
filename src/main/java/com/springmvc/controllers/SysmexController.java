package com.springmvc.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.StagoDataDto;
import com.covide.crf.dto.VistrosDataDto;
import com.covide.crf.service.CrfService;
import com.covide.dto.StudyDto;
import com.covide.dto.SysmexDataDto;
import com.covide.enums.StatusMasterCodes;
import com.covide.template.dto.SysmexDataUpdateDto;
import com.covide.template.dto.TestCodesUnitsDto;
import com.covide.template.dto.VistrosDataUpdateDto;
import com.springmvc.dao.StatusDao;
import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.StagoData;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTestCodes;
import com.springmvc.model.SysmexAnimalCode;
import com.springmvc.model.SysmexData;
import com.springmvc.service.ExpermentalDesignService;
import com.springmvc.service.StudyService;
import com.springmvc.util.StagoThread;
import com.springmvc.util.SysmexThread;
import com.springmvc.util.VistrosThread;

@Controller
@RequestMapping("/sysmex")
public class SysmexController {
	@Autowired
	CrfService crfSer;
	@Autowired
	StudyService studyService;
	@Autowired
	private ExpermentalDesignService expermentalDesignService;
	@Autowired
	private Environment environment;
	@Autowired
	private StatusDao statusDao;
	public static Long ipAddressPk = 0l;

	@RequestMapping(value = "/selectInstrument", method = RequestMethod.GET)
	public String selectInstrument(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAttribute("insturments", studyService.instrumentIpAddresses(new StatusMaster(), false));
		return "selectInstrument";
	}

	@RequestMapping(value = "/saveInstrument", method = RequestMethod.POST)
	public String saveInstrument(HttpServletRequest request, ModelMap model,
			@RequestParam("insturmentId") List<Long> insturmentId, RedirectAttributes redirectAttributes) {
		if (studyService.instrumentIpAddressesStatus(insturmentId))
			redirectAttributes.addFlashAttribute("pageMessage", "Data saved Successfully.");
		else
			redirectAttributes.addFlashAttribute("pageError", "Faield to Save Data.");

		return "redirect:/sysmex/selectInstrument";
	}

	@RequestMapping(value = "/readInsturmentDataCapture/{serviceType}", method = RequestMethod.GET)
	public String readInsturmentDataCapture(HttpServletRequest request, ModelMap model,
			@PathVariable("serviceType") String serviceType, RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StatusMaster activeStatus = statusDao.statusMaster(StatusMasterCodes.ACTIVE.toString());
//		List<InstrumentIpAddress> ipAddress = new ArrayList<>();
		List<InstrumentIpAddress> dbipAddress = studyService.instrumentIpAddresses(activeStatus, true);
		List<StudyTestCodes> stagoTestCodes = new ArrayList<>();
		for (InstrumentIpAddress ip : dbipAddress) {
			if (ip.getInstrumentName().equals("STAGO")) {
				stagoTestCodes = studyService.studyInstumentTestCodes(ip.getId(), activeStudyId, null);
			}
		}
		model.addAttribute("stagoTestCodes", stagoTestCodes);
		model.addAttribute("ipAddress", dbipAddress);
		List<StudyMaster> studyList = studyService.allActiveStudys();
		model.addAttribute("studyList", studyList);
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		model.addAttribute("ipAddressPk", ipAddressPk);
		model.addAttribute("studys", studyService.findAll());
		model.addAttribute("serviceType", serviceType);

		ScheduledTasks sc = new ScheduledTasks();
		sc.reportCurrentTime();
		return "readInsturmentDataCapture";

	}

	@RequestMapping(value = "/callInstrumentReport", method = RequestMethod.POST)
	public String callInstrumentReport(HttpServletRequest request, ModelMap model,
			@RequestParam("serviceType") String serviceType, RedirectAttributes redirectAttributes) {
		if (serviceType.equals("SYSMEX"))
			return "redirect:/sysmex/sysmexDataExport";
		else if (serviceType.equals("STAGO"))
			return "redirect:/sysmex/stagoDataExport";
		else
//			if(serviceType.equals("SYSMEX"))
			return "redirect:/sysmex/vistrosDataExport";

	}

	@RequestMapping(value = "/studyStagoTestCodes/{studyId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<StudyTestCodes> studyStagoTestCodes(HttpServletRequest request, ModelMap model,
			@PathVariable("studyId") Long studyId, RedirectAttributes redirectAttributes) {
//		List<InstrumentIpAddress> ipAddress = new ArrayList<>();
		InstrumentIpAddress stagoIp = studyService.instrumentIpAddress("STAGO");
		List<StudyTestCodes> stagoTestCodes = studyService.studyInstumentTestCodes(stagoIp.getId(), studyId, null);
		return stagoTestCodes;
	}

	@RequestMapping(value = "/callSysmexService/{ipaddress}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody TestCodesUnitsDto getConfgiuredCrfs(HttpServletRequest request, ModelMap model,
			@PathVariable("ipaddress") Long ipaddress, RedirectAttributes redirectAttributes) {
		String userName = request.getSession().getAttribute("userName").toString();
//		if (studyId == null)
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(studyId);
		InstrumentIpAddress ip = studyService.instrumentIpAddress("SYSMEX");
		TestCodesUnitsDto dto = new TestCodesUnitsDto();
		try {
			
			stopSysmexService(userName);
			SysmexThread.studyId = studyId;
			SysmexThread.studyService = studyService;
			SysmexThread.port = ip.getPortNo();
			SysmexThread.ipAddress = ip.getIpAddress();
			SysmexThread.study = sm;
			List<SysmexAnimalCode> codes = expermentalDesignService.sysmexAnimalCodes();
			for (SysmexAnimalCode code : codes) {
				SysmexThread.animalCodes.put(code.getCode(), code.getId());
			}
//			if (SysmexThread.testCodes.size() == 0) {
				sysmexHeadderData(ip, studyId);
//			}
			if (SysmexThread.server != null && !SysmexThread.server.isClosed()) {
				SysmexThread.server.close();
			}
			SysmexThread thread = new SysmexThread();
			thread.start();
//			SysmexThread.runningThreads.put(userName, thread);
			dto.setMessage("Connection establish successfully");
//			SocketSysmax.runSysmexServier();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
//		dto.setTestCodes(SysmexThread.testCodes);
//		dto.setSysMaxTestCodesUnitsMap(SysmexThread.sysMaxTestCodesUnitsMap);
//		dto.setSysMaxTestCodesMap(SysmexThread.sysMaxTestCodesMap);
		return dto;
	}

	private void sysmexHeadderData(InstrumentIpAddress ip, Long studyId) {
//		SysmexThread.sysMaxTestCodesMap.put(0, "Study No.");
//		SysmexThread.sysMaxTestCodesMap.put(1, "Animal No");
//		SysmexThread.sysMaxTestCodesMap.put(2, "Run Time");
//		SysmexThread.sysMaxTestCodesUnitsMap.put(0, "");
//		SysmexThread.sysMaxTestCodesUnitsMap.put(1, "");
//		SysmexThread.sysMaxTestCodesUnitsMap.put(2, "");

		List<StudyTestCodes> testCodes = studyService.studyInstumentTestCodes(ip.getId(), studyId, null);
		for (StudyTestCodes tc : testCodes) {
//			SysmexThread.testCodes.put(tc.getTestCode().getTestCode(), tc);
//			SysmexThread.sysMaxTestCodesMap.put(tc.getOrderNo() + 2, tc.getTestCode().getDisPalyTestCode());
//			SysmexThread.sysMaxTestCodesUnitsMap.put(tc.getOrderNo() + 2,
//					tc.getTestCode().getTestCodeUints().getDisplayUnit());
		}
	}

	private void vitrosHeadderData(InstrumentIpAddress ip, Long studyId) {

//		VistrosThread.sysMaxTestCodesMap.put(0, "Study No.");
//		VistrosThread.sysMaxTestCodesMap.put(1, "Animal No");
//		VistrosThread.sysMaxTestCodesMap.put(2, "Run Time");
//		VistrosThread.sysMaxTestCodesUnitsMap.put(0, "");
//		VistrosThread.sysMaxTestCodesUnitsMap.put(1, "");
//		VistrosThread.sysMaxTestCodesUnitsMap.put(2, "");
//		List<StudyTestCodes> testCodes = studyService.studyInstumentTestCodes(ip.getId(), studyId);
//		for (StudyTestCodes tc : testCodes) {
//			VistrosThread.testCodes.put(tc.getTestCode().getTestCode(), tc);
//			VistrosThread.sysMaxTestCodesMap.put(tc.getOrderNo() + 2, tc.getTestCode().getDisPalyTestCode());
//			VistrosThread.instruemntTestCodesMap.put(tc.getOrderNo() + 2, tc.getTestCode().getTestCode());
//			VistrosThread.sysMaxTestCodesUnitsMap.put(tc.getOrderNo() + 2,
//					tc.getTestCode().getTestCodeUints().getDisplayUnit());
//		}
	}

	private void stopSysmexService(String username) {
		// TODO Auto-generated method stub
		try {
			SysmexThread.studyId = 0l;
			SysmexThread.studyService = null;
			SysmexThread.ipAddress = "";
			SysmexThread.testCodes = new TreeMap<>();
			SysmexThread.study = null;
			SysmexThread thread = SysmexThread.runningThreads.get(username);
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

	@RequestMapping(value = "/sysmexDataExport", method = RequestMethod.GET)
	public String sysmexDataExport(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAttribute("studyNumbers", studyService.sysmaxStudyNumbers());
		return "sysmexDataExport";
	}

//	@RequestMapping(value = "/exportSysmexData", method = RequestMethod.GET)
//	public void excelExport(HttpServletRequest request, HttpServletResponse response, ModelMap model,
//			RedirectAttributes redirectAttributes, HttpSession session) {
//		try {
//			
//			File file = studyService.exportSysmexDataToExcel(request);
//
//			response.setContentType("application/sysmex.ms-excel");
//			response.addHeader("Content-Disposition", "attachment; filename=sysmexdata.xlsx");
//			response.setContentLength((int) file.length());
//
//			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
//
//			// Copy bytes from source to destination(outputstream in this example), closes
//			// both streams.
//			FileCopyUtils.copy(inputStream, response.getOutputStream());
//
//			redirectAttributes.addFlashAttribute("pageMessage", "Data Exported Successfully.");
//		} catch (Exception e) {
//			e.printStackTrace();
//			redirectAttributes.addFlashAttribute("pageMessage", "Data Export Faild.");
//		}
//	}
	@RequestMapping(value = "/sysmexStudyRunDates/{studyId}/{insturment}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<String> sysmexResultSelection(@PathVariable("studyId") Long studyId,
			@PathVariable("insturment") String insturment, ModelMap model) throws ParseException {
		return studyService.insturmentResultSelectionDates(studyId, insturment);
	}

	@RequestMapping(value = "/sysmexDataExportTable", method = RequestMethod.POST)
	public String sysmexDataExportPost(@RequestParam("studyNumbers") Long studyNumber, HttpServletRequest request,
			@RequestParam("stDate") String startDate, @RequestParam("sampleType") String sampleType,
			@RequestParam("observation") String observation, ModelMap model) throws ParseException {
//		SysmexDataDto sysmexDataDto = studyService.sysmexDataDtoList(ip,studyNumber, startDate);
//		model.addAttribute("sysmexDataDto",sysmexDataDto);
		List<SysmexData> sysmexDataList = studyService.sysmexDataList(studyNumber, startDate, sampleType, observation);
		SysmexDataDto sysmexDataDto = studyService.sysmexDataDtoList(studyNumber, sysmexDataList, "table", observation);
		model.addAttribute("sysmexDataDto", sysmexDataDto);
//		
		// swami
//		SysmexDataDto sysmexDataDto1 = studyService.sysmexDataDtoTable(studyNumber, sysmexDataList, "table");
//		model.addAttribute("sysmexDataDto1", sysmexDataDto1);
		model.addAttribute("sampleType", sampleType);
//		dto.setHeadding(studyService.headderPart(sysmexDataList));
//		dto.setDataList(studyService.sysmexDataDtoList(sysmexDataList));
		return "/pages/sysmex/sysmexDataExportTable";
	}

	@RequestMapping(value = "/sysmexResultSelection", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody SysmexDataUpdateDto sysmexResultSelection(@RequestParam("studyId") Long studyId,
			@RequestParam("animalNumber") String animalNo, @RequestParam("columnIndex") int columnIndexOrOrderNo,
			@RequestParam("observation") String observation, @RequestParam("sysmexstDate") String sysmexstDate,
			ModelMap model) throws ParseException {
		SysmexDataUpdateDto dto =  studyService.sysmexDataList(studyId, animalNo, columnIndexOrOrderNo, observation, sysmexstDate);
		return dto;
	}

	@RequestMapping(value = "/sysmexResultSelectionSave", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String sysmexResultSelectionSave(@RequestParam("studyId") Long studyId,
			@RequestParam("animalNo") String animalNo, @RequestParam("testName") String testName,
			@RequestParam("columnIndex") int columnIndex, @RequestParam("finalResultId") Long finalResultId,
			@RequestParam("rerunCommnet") String rerunCommnet, @RequestParam("observation") String observation,
			@RequestParam("sysmexstDate") String sysmexstDate, ModelMap model) throws ParseException {
		String s = studyService.sysmexResultSelectionSave(studyId, animalNo, testName, columnIndex, finalResultId,
				rerunCommnet, observation, sysmexstDate);
		return s;
	}

	@RequestMapping(value = "/sysmexDataExport", method = RequestMethod.POST)
	public void sysmexDataExportPost(@RequestParam("studyNumbers") Long studyNumber,
			@RequestParam("stDate") String startDate, @RequestParam("sampleType") String sampleType,
			@RequestParam("observation") String observation, @RequestParam("exportType") String exportType,
			HttpServletResponse response, HttpServletRequest request, ModelMap model,
			RedirectAttributes redirectAttributes) {
		if (exportType.equals("Excel")) {
			try {
				File file = studyService.exportSysmexDataToExcel(request, studyNumber, startDate, sampleType,
						observation);

				response.setContentType("application/sysmex.ms-excel");
				response.addHeader("Content-Disposition", "attachment; filename=sysmexdata.xlsx");
				response.setContentLength((int) file.length());

				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

				// Copy bytes from source to destination(outputstream in this example), closes
				// both streams.
				FileCopyUtils.copy(inputStream, response.getOutputStream());

				redirectAttributes.addFlashAttribute("pageMessage", "Data Exported Successfully.");
			} catch (Exception e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("pageMessage", "Data Export Faild.");
			}
		} else {
			String realPath = request.getSession().getServletContext().getRealPath("/");

			try {
				String fileName = studyService.exportSysmexDataToPdf(request, response, studyNumber, null, realPath,
						startDate, sampleType, observation);
				System.out.println(fileName);
				File file = new File(fileName);
				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=Sysmex.pdf");
				response.setContentLength((int) file.length());
//				redirectAttributes.addFlashAttribute("pageMessage", " Subject barcodes created Successfully.");
//				return outputStream -> {
				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
				FileCopyUtils.copy(inputStream, response.getOutputStream());
//				};
			} catch (Exception e) {
				e.printStackTrace();
//				return null;
			}
		}

	}

	@RequestMapping(value = "/sysmexDataExportPdf", method = RequestMethod.POST)
	public StreamingResponseBody sysmexDataExportPdf(@RequestParam("sysmexStudyId") Long studyId,
			@RequestParam("observation") String observation, @RequestParam("sysmexAnimalNum") String animalNum,
			HttpServletResponse response, HttpServletRequest request, ModelMap model,
			RedirectAttributes redirectAttributes) {

		String realPath = request.getSession().getServletContext().getRealPath("/");

		try {
			String fileName = studyService.exportSysmexDataToPdf(request, response, studyId, animalNum, realPath, null,
					null, observation);
			System.out.println(fileName);
			File file = new File(fileName);
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=Sysmex.pdf");
			response.setContentLength((int) file.length());
//			redirectAttributes.addFlashAttribute("pageMessage", " Subject barcodes created Successfully.");
			return outputStream -> {
				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
				FileCopyUtils.copy(inputStream, response.getOutputStream());
			};
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/callStagoService/{sampleType}/{loatNo}/{test}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody StudyDto callStagoService(HttpServletRequest request, ModelMap model,
			@PathVariable("sampleType") String sampleType, @PathVariable("loatNo") String loatNo,
			@PathVariable("test") String test,
//			@PathVariable("studyId") Long studyId,
			RedirectAttributes redirectAttributes) {
		String userName = request.getSession().getAttribute("userName").toString();

		StudyDto dto = new StudyDto();
//		dto.setStudys(studyService.findAll());
//		Long userId = (Long) request.getSession().getAttribute("userId");
////		if (studyId == null)
//		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
//		StudyMaster study = studyService.findByStudyId(studyId);
//		InstrumentIpAddress ip = studyService.instrumentIpAddress("STAGO");
//		if (StagoThread.testCodes.size() == 0) {
//			stagoHeadderData(ip, studyId, sampleType, loatNo);
//		}
//		dto.setTestCodes(StagoThread.testCodes);
//		dto.setStagoTestCodesMap(StagoThread.stagoTestCodesMap);
//		dto.setStagoTestCodesUnitsMap(StagoThread.stagoTestCodesUnitsMap);
//		StagoThread.study = study;
//		try {
//			stopStagoService(userName);
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		try {
//			
//
//			StagoThread.studyId = studyId;
//			StagoThread.sampleType = sampleType;
//			StagoThread.portName = ip.getComPortNo();
//			StagoThread.test = test;
//			StagoThread.loatNo = loatNo;
//			StagoThread.userId = userId;
//			StagoThread thread = new StagoThread();
//			thread.start();
//			StagoThread.runningThreads.put(userName, thread);
////			SocketSysmax.runSysmexServier();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
		return dto;
	}

	private void stagoHeadderData(InstrumentIpAddress ip, Long studyId, String sampleType, String loatNo) {
//		StagoThread.stagoTestCodesMap.put(0, "Study No.");
//		StagoThread.stagoTestCodesMap.put(1, "Animal No");
//
////		if(sampleType.equals("QC")) {
////			StagoThread.stagoTestCodesMap.put(2, "Lot No : " + loatNo);
////		}else {
////			StagoThread.stagoTestCodesMap.put(2, "Lot No");
////		}
//		StagoThread.stagoTestCodesMap.put(2, "Lot No");
//		StagoThread.stagoTestCodesMap.put(3, "Run Time");
//		StagoThread.stagoTestCodesMap.put(4, "Test(sec)");
//		StagoThread.stagoTestCodesMap.put(5, "Result");
////		StagoThread.stagoTestCodesUnitsMap.put(0, "");
////		StagoThread.stagoTestCodesUnitsMap.put(1, "");
////		StagoThread.stagoTestCodesUnitsMap.put(2, "");
////		if(sampleType.equals("QC")) {
////			StagoThread.stagoTestCodesUnitsMap.put(3, "");	
////		}
//		List<StudyTestCodes> testCodes = studyService.studyInstumentTestCodes(ip.getId(), studyId);
//		for (StudyTestCodes tc : testCodes) {
//			StagoThread.stagoTestCodesOnlyMap.put(tc.getOrderNo(), tc.getTestCode().getTestCode());
//			StagoThread.testCodes.put(tc.getTestCode().getTestCode(), tc);
////			StagoThread.stagoTestCodesMap.put(tc.getOrderNo() + 2, tc.getTestCode().getDisPalyTestCode());
////			StagoThread.stagoTestCodesUnitsMap.put(tc.getOrderNo() + 2,
////					tc.getTestCode().getTestCodeUints().getDisplayUnit());
//		}
//		StagoThread.animalNos = studyService.studyAnimals(studyId);
	}

	@RequestMapping(value = "/stopStagoService", method = RequestMethod.GET)
	public @ResponseBody String stopStagoService(HttpServletRequest request, ModelMap model,
			RedirectAttributes redirectAttributes) {
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		String userName = request.getSession().getAttribute("userName").toString();
		stopStagoService(userName);
		return "Server Stoped Successfully.";
	}

	private void stopStagoService(String userName) {
		// TODO Auto-generated method stub
		try {
			try {
				StagoThread.comPort.closePort();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			StagoThread.portName = "";
			StagoThread.studyId = 0l;
			StagoThread.test = "";
//			Map<Long, StagoThread> threads = StagoThread.runningThreads;
			StagoThread thread = StagoThread.runningThreads.get(userName);
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

	@RequestMapping(value = "/stagoDataExport", method = RequestMethod.GET)
	public String stagoDataExport(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
//		List<StagoDataDto> sysmexDataList = studyService.stagoDataList();
//		model.addAttribute("sysmexDataList", sysmexDataList);
		model.addAttribute("studyNumbers", studyService.stagoStudyNumbers());
		return "stagoDataExport";
	}

	@RequestMapping(value = "/stagoResultSelectionAjax/{studyId}/{testName}/{animalId}/{rowId}/{tdId}/{sampleType}/{observation}/{stagostDate}", method = RequestMethod.GET)
	public String stagoResultSelectionAjax(@PathVariable("studyId") Long studyId,
			@PathVariable("testName") String testName, @PathVariable("animalId") Long animalId,
			@PathVariable("rowId") String rowId, @PathVariable("tdId") String tdId,
			@PathVariable("sampleType") String sampleType, @PathVariable("observation") String observation,
			@PathVariable("stagostDate") String stagostDate, ModelMap model) throws ParseException {
		StudyMaster study = studyService.findByStudyId(studyId);
		StudyAnimal animal = studyService.studyAnimalsById(animalId);
		List<StagoData> list = studyService.stagoDataList(studyId, null, testName, animalId, sampleType, observation,
				stagostDate);
//		com.springmvc.dto.StagoDataDto dto = new com.springmvc.dto.StagoDataDto();
//		dto.setList(list);
		model.addAttribute("eachStagoData", list);
		model.addAttribute("study", study);
		model.addAttribute("animal", animal);
		model.addAttribute("testName", testName);
		model.addAttribute("animalId", animalId);
		model.addAttribute("rowId", rowId);
		model.addAttribute("tdId", tdId);
		model.addAttribute("sampleType", sampleType);
		return "/pages/sysmex/stagoDataUpdatePage";
	}

//	@RequestMapping(value = "/stagoResultSelection", method = RequestMethod.POST, produces = {
//			MediaType.APPLICATION_JSON_VALUE })
//	public @ResponseBody com.springmvc.dto.StagoDataDto stagoResultSelection(@RequestParam("studyId") Long studyId,
//			@RequestParam("animalNo") String animalNo, @RequestParam("testName") String testName,
//			@RequestParam("animalId") Long animalId, ModelMap model) throws ParseException {
//		List<StagoData> list = studyService.stagoDataList(studyId, animalNo, testName, animalId, "");
//		com.springmvc.dto.StagoDataDto dto = new com.springmvc.dto.StagoDataDto();
//		dto.setList(list);
//		return dto;
//	}

	@RequestMapping(value = "/stagoResultSelectionSave", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String stagoResultSelectionSave(@RequestParam("studyId") Long studyId,
			@RequestParam("animalNo") String animalNo, @RequestParam("testName") String testName,
			@RequestParam("finalResultId") Long finalResultId, @RequestParam("animalId") Long animalId,
			@RequestParam("rerunCommnet") String rerunCommnet, @RequestParam("sampleType") String sampleType,
			@RequestParam("observation") String observation, @RequestParam("stagostDate") String stagostDate,
			ModelMap model) throws ParseException {
		String s = studyService.stagoDataSave(studyId, animalNo, testName, finalResultId, animalId, rerunCommnet,
				sampleType, observation, stagostDate);
		return s;
	}

	@RequestMapping(value = "/stagoDataExportTable", method = RequestMethod.POST)
	public String stagoDataExportTable(@RequestParam("studyNumbers") Long studyNumber,
			@RequestParam("stDate") String startDate, @RequestParam("sampleType") String sampleType,
			@RequestParam("observation") String observation, ModelMap model) throws ParseException {
//		SysmexDataDto dto = new SysmexDataDto();
		InstrumentIpAddress ip = studyService.instrumentIpAddress("STAGO");
		List<StudyTestCodes> testCodes = studyService.studyInstumentTestCodes(ip.getId(), studyNumber, observation);
		model.addAttribute("testCodes", testCodes);
		List<StagoDataDto> sysmexDataList = studyService.stagoDataList(studyNumber, startDate, null, sampleType,
				observation);
		if (sysmexDataList.size() > 0) {
			Collections.sort(sysmexDataList);
			SortedMap<Integer, StudyTestCodes> selecteTestCodes = sysmexDataList.get(0).getSelecteTestCodes();
			model.addAttribute("selecteTestCodes", selecteTestCodes);
		}

		model.addAttribute("sysmexDataList", sysmexDataList);
		model.addAttribute("sampleType", sampleType);
		return "/pages/sysmex/stagoDataExportTable";
	}

	@RequestMapping(value = "/stagoDataExport", method = RequestMethod.POST)
	public void stagoDataExport(@RequestParam("studyNumbers") Long studyNumber,
			@RequestParam("stDate") String startDate, @RequestParam("sampleType") String sampleType,
			@RequestParam("exportType") String exportType, @RequestParam("observation") String observation,
			HttpServletResponse response, HttpServletRequest request, ModelMap model,
			RedirectAttributes redirectAttributes) {
		if (exportType.equals("Excel")) {
			try {
				File file = studyService.exportStagoDataToExcel(request, studyNumber, startDate, sampleType,
						observation);
				response.setContentType("application/stago.ms-excel");
				response.addHeader("Content-Disposition", "attachment; filename=stagodata.xlsx");
				response.setContentLength((int) file.length());

				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

				// Copy bytes from source to destination(outputstream in this example), closes
				// both streams.
				FileCopyUtils.copy(inputStream, response.getOutputStream());

				redirectAttributes.addFlashAttribute("pageMessage", "Data Exported Successfully.");
			} catch (Exception e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("pageMessage", "Data Export Faild.");
			}
		} else {
			String realPath = request.getSession().getServletContext().getRealPath("/");

			try {
				List<Long> animalIds = new ArrayList<>();
				String fileName = studyService.exportStagoDataToPdf(request, response, studyNumber, animalIds, realPath,
						startDate, sampleType, observation);
				System.out.println(fileName);
				File file = new File(fileName);
				response.setContentType("application/Stago.pdf");
				response.addHeader("Content-Disposition", "attachment; filename=Stago.pdf");
				response.setContentLength((int) file.length());
//				redirectAttributes.addFlashAttribute("pageMessage", " Subject barcodes created Successfully.");

				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
				FileCopyUtils.copy(inputStream, response.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@RequestMapping(value = "/stagoDataExportPdf", method = RequestMethod.POST)
	public StreamingResponseBody stagoDataExportPdf(@RequestParam("studyId") Long studyId,
			@RequestParam("sampleType") String sampleType, @RequestParam("animalId") Long animalId,
			@RequestParam("startDate") String startDate, @RequestParam("multipleAnimals") List<Long> multipleAnimals,
			@RequestParam("observation") String observation, HttpServletResponse response, HttpServletRequest request,
			ModelMap model, RedirectAttributes redirectAttributes) {

		String realPath = request.getSession().getServletContext().getRealPath("/");

		try {
			List<Long> animalIds = new ArrayList<>();
			if (multipleAnimals == null || multipleAnimals.size() == 0) {
				animalIds.add(animalId);
			} else
				animalIds = multipleAnimals;
			String fileName = studyService.exportStagoDataToPdf(request, response, studyId, animalIds, realPath,
					startDate, sampleType, observation);
			System.out.println(fileName);
			File file = new File(fileName);
			response.setContentType("application/Stago.pdf");
			response.addHeader("Content-Disposition", "attachment; filename=Stago.pdf");
			response.setContentLength((int) file.length());
//			redirectAttributes.addFlashAttribute("pageMessage", " Subject barcodes created Successfully.");
			return outputStream -> {
				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
				FileCopyUtils.copy(inputStream, response.getOutputStream());
			};
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/callVistrosService", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody TestCodesUnitsDto callVistrosService(HttpServletRequest request, ModelMap model,
			RedirectAttributes redirectAttributes) {
//		try {
//			String userName = request.getSession().getAttribute("userName").toString();
//			stopVitrosService(userName);
////			if (studyId == null)
//			Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
//			InstrumentIpAddress ip = studyService.instrumentIpAddress("VITROS");
//			try {
//				VistrosThread.study = studyService.findByStudyId(studyId);
//				VistrosThread.studyId = studyId;
//				VistrosThread.studyService = studyService;
//				if (VistrosThread.testCodes.size() == 0) {
//					vitrosHeadderData(ip, studyId);
//				}
//
//				VistrosThread thread = new VistrosThread();
//				thread.start();
//				VistrosThread.runningThreads.put(userName, thread);
////				SocketSysmax.runSysmexServier();
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//			TestCodesUnitsDto dto = new TestCodesUnitsDto();
////			dto.setTestCodes(VistrosThread.testCodes);
////			dto.setSysMaxTestCodesUnitsMap(VistrosThread.sysMaxTestCodesUnitsMap);
////			dto.setSysMaxTestCodesMap(VistrosThread.sysMaxTestCodesMap);
//			return dto;
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
		return new TestCodesUnitsDto();
	}

	private void stopVitrosService(String userName) {
		// TODO Auto-generated method stub
		try {
			VistrosThread.study = null;
			VistrosThread.studyId = 0l;
			VistrosThread.exit = true;
			VistrosThread thread = VistrosThread.runningThreads.get(userName);
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

	@RequestMapping(value = "/vitrosDataExport", method = RequestMethod.GET)
	public String vistrosDataExport(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAttribute("studys", studyService.allActiveStudys());
		return "vitrosDataExport";
	}

	@RequestMapping(value = "/vitorsDataExportTable", method = RequestMethod.POST)
	public String vistorsDataExportTable(@RequestParam("studyNumbers") Long studyId,
			@RequestParam("stDate") String startDate, @RequestParam("sampleType") String sampleType,
			@RequestParam("observation") String observation, ModelMap model) throws ParseException {
		InstrumentIpAddress ip = studyService.instrumentIpAddress("VITROS");
		VistrosDataDto vistrosDataDto = studyService.vitrosDataForExcel(studyId, ip, startDate, sampleType,
				observation);
		model.addAttribute("vistrosDataDto", vistrosDataDto);
		return "/pages/vistros/vitorsDataExportTable";
	}

	@RequestMapping(value = "/vitrosResultSelection", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody VistrosDataUpdateDto vitrosResultSelection(@RequestParam("studyId") Long studyId,
			@RequestParam("animalNumber") String animalNo, @RequestParam("columnIndex") int columnIndex, ModelMap model)
			throws ParseException {
		return studyService.vitrosDataList(studyId, animalNo, columnIndex);
	}

	@RequestMapping(value = "/vitrosResultSelectionSave", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String vitrosResultSelectionSave(@RequestParam("studyId") Long studyId,
			@RequestParam("animalNo") String animalNo, @RequestParam("testName") String testName,
			@RequestParam("columnIndex") int columnIndex, @RequestParam("finalResultId") Long finalResultId,
			@RequestParam("rerunCommnet") String rerunCommnet, ModelMap model) throws ParseException {
		String s = studyService.vitrosResultSelectionSave(studyId, animalNo, testName, columnIndex, finalResultId,
				rerunCommnet);
		return s;
	}

	@RequestMapping(value = "/vitorsDataExport", method = RequestMethod.POST)
	public void vistorsDataExport(@RequestParam("studyNumbers") Long studyId, @RequestParam("stDate") String startDate,
			@RequestParam("sampleType") String sampleType, @RequestParam("exportType") String exportType,
			@RequestParam("observation") String observation, HttpServletResponse response, HttpServletRequest request,
			ModelMap model, RedirectAttributes redirectAttributes) {
		if (exportType.equals("Excel")) {
			try {
				File file = studyService.exportVistrosDataToExcel(request, studyId, startDate, sampleType, observation);
				response.setContentType("application/vitros.ms-excel");
				response.addHeader("Content-Disposition", "attachment; filename=Vitrosdata.xlsx");
				response.setContentLength((int) file.length());
				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
				// Copy bytes from source to destination(outputstream in this example), closes
				// both streams.
				FileCopyUtils.copy(inputStream, response.getOutputStream());
				redirectAttributes.addFlashAttribute("pageMessage", "Data Exported Successfully.");
			} catch (Exception e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("pageMessage", "Data Export Faild.");
			}
		} else {
			String realPath = request.getSession().getServletContext().getRealPath("/");

			try {
				String fileName = studyService.exportVitroDataToPdf(request, response, null, realPath, studyId,
						startDate, sampleType, observation);
				System.out.println(fileName);
				File file = new File(fileName);
				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=Vitros.pdf");
				response.setContentLength((int) file.length());
//				redirectAttributes.addFlashAttribute("pageMessage", " Subject barcodes created Successfully.");

				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
				FileCopyUtils.copy(inputStream, response.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@RequestMapping(value = "/vistroDataExportPdf", method = RequestMethod.POST)
	public StreamingResponseBody vistroDataExportPdf(@RequestParam("studyId") Long studyId,
			@RequestParam("animalId") String animalId, @RequestParam("observation") String observation,
			HttpServletResponse response, HttpServletRequest request, ModelMap model,
			RedirectAttributes redirectAttributes) {

		String realPath = request.getSession().getServletContext().getRealPath("/");

		try {
			String fileName = studyService.exportVitroDataToPdf(request, response, animalId, realPath, studyId, null,
					null, observation);
			System.out.println(fileName);
			File file = new File(fileName);
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=Sysmex.pdf");
			response.setContentLength((int) file.length());
//			redirectAttributes.addFlashAttribute("pageMessage", " Subject barcodes created Successfully.");
			return outputStream -> {
				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
				FileCopyUtils.copy(inputStream, response.getOutputStream());
			};
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/insturmentDataExport", method = RequestMethod.GET)
	public String dataExport(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StatusMaster activeStatus = statusDao.statusMaster(StatusMasterCodes.ACTIVE.toString());
		List<InstrumentIpAddress> dbipAddress = studyService.instrumentIpAddresses(activeStatus, true);
		model.addAttribute("instruments", dbipAddress);
		List<StudyMaster> studyList = studyService.allActiveStudys();
		model.addAttribute("studyList", studyList);
		return "insturmentDataExport";

	}
}
