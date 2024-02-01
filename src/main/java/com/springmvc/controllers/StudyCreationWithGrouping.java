package com.springmvc.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.InsturmentParemtersDataDto;
import com.covide.crf.dto.ObservationClinPathTestCodesDto;
import com.covide.dto.StudyCreationDto;
import com.covide.template.dto.StudyMasterDto;
import com.covide.template.dto.TestCodeProfileDto;
import com.covide.template.dto.TestCodeProfileParametersDto;
import com.springmvc.dao.impl.GroupingDao;
import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.TestCode;
import com.springmvc.model.TestCodeProfile;
import com.springmvc.model.TestCodeProfileParameters;
import com.springmvc.service.AcclimatizationService;
import com.springmvc.service.StudyService;

@Controller
@RequestMapping("/grouping")
@PropertySource(value = { "classpath:application.properties" })
public class StudyCreationWithGrouping {
	@Autowired
	private Environment environment;
	@Autowired
	private GroupingDao groupingDao;
	@Autowired
	private StudyService studyService;
	@Autowired
	AcclimatizationService acclimatizationService;

	@RequestMapping(value = "/testCodeUnits", method = RequestMethod.GET)
	public String testCodeUnits(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		model.addAttribute("testCodeUnits", groupingDao.testCodeUnits());
		return "testCodeUnits";
	}

	@RequestMapping(value = "/checkUintName", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String checkUintName(@RequestParam("unit") String unit) {
		return groupingDao.checkUintName(unit);
	}

	@RequestMapping(value = "/mergeTestCodeUnits", method = RequestMethod.POST)
	public String mergeTestCodeUnits(ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		String[] unitIds = request.getParameterValues("unitId");
		Map<Long, String> oldUnits = new HashMap<>();
		Map<Long, String> oldUnitsTcs = new HashMap<>();
		if (unitIds != null) {
			for (String unitId : unitIds) {
				oldUnits.put(Long.parseLong(unitId), request.getParameter(unitId + "_displayUnit"));
				oldUnitsTcs.put(Long.parseLong(unitId), request.getParameter(unitId + "_tcUnit"));
			}
		}
		String newUnitrows = request.getParameter("newunits");
		Map<String, String> newUnits = new HashMap<>();
		if (newUnitrows != null && !newUnitrows.trim().equals("")) {
			newUnitrows = newUnitrows.trim();
			String[] newRows = newUnitrows.split("\\,");
			if (newRows != null) {
				for (String newRow : newRows) {
					newUnits.put(request.getParameter(newRow + "_newunit"),
							request.getParameter(newRow + "_newdispalyUnit"));
				}
			}
		}

		boolean result = groupingDao.mergeTestCodeUnits(userId, oldUnits, oldUnitsTcs, newUnits);
		if (result)
			redirectAttributes.addFlashAttribute("pageMessage", "Data Saved Successfullly...!");
		else
			redirectAttributes.addFlashAttribute("pageError", "Failed to save data. Please Try Again.");
		return "redirect:/grouping/testCodeUnits";
	}

	@RequestMapping(value = "/testCodes", method = RequestMethod.GET)
	public String testCodes(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		String instument = request.getParameter("instument");
		if (instument != null && !instument.trim().equals("")) {
			model.addAttribute("testCodes", groupingDao.testCodes(instument));
			model.addAttribute("testCodesUnits", groupingDao.testCodeUnits(true));
			model.addAttribute("instument", instument);
		} else
			model.addAttribute("instument", -1);
//		model.addAttribute("testCodes", groupingDao.testCodes());
//		model.addAttribute("testCodesUnits", groupingDao.testCodeUnits(true));
		return "testCodes";
	}

	@RequestMapping(value = "/testCodes/{instument}", method = RequestMethod.GET)
	public String testCodes(@PathVariable("instument") String instument, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		model.addAttribute("testCodes", groupingDao.testCodes(instument));
		model.addAttribute("testCodesUnits", groupingDao.testCodeUnits(true));
		model.addAttribute("instument", instument);
		return "testCodes";
	}

	@RequestMapping(value = "/checkTestCodeName", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String checkTestCodeName(@RequestParam("testCode") String testCode, String type) {
		return groupingDao.checkTestCodeName(testCode, type);
	}

	@RequestMapping(value = "/mergeTestCodes", method = RequestMethod.POST)
	public String mergeTestCodes(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		String instument = request.getParameter("instument");
		String[] testCodeIds = request.getParameterValues("testCodeIds");
		Map<Long, String> oldTestCodes = new HashMap<>();
		Map<Long, String> oldDispalyTestCodes = new HashMap<>();
		Map<Long, String> oldOrders = new HashMap<>();
		Map<Long, Long> oldUnits = new HashMap<>();
		if (testCodeIds != null) {
			for (String tcId : testCodeIds) {
				oldTestCodes.put(Long.parseLong(tcId), request.getParameter(tcId + "_testCode"));
				oldDispalyTestCodes.put(Long.parseLong(tcId), request.getParameter(tcId + "_dispalyTestCode"));
				oldOrders.put(Long.parseLong(tcId), request.getParameter(tcId + "_order"));
				oldUnits.put(Long.parseLong(tcId), Long.parseLong(request.getParameter(tcId + "_type")));
			}
		}
		String newtcrows = request.getParameter("newtcrows");
		Map<Integer, String> newTestCodes = new HashMap<>();
		Map<Integer, String> newDispalyTestCodes = new HashMap<>();
		Map<Integer, String> newOrders = new HashMap<>();
		Map<Integer, Long> newUnits = new HashMap<>();
		if (newtcrows != null && !newtcrows.trim().equals("")) {
			newtcrows = newtcrows.trim();
			String[] newRows = newtcrows.split("\\,");
			if (newRows != null) {
				for (String newRow : newRows) {
					newTestCodes.put(Integer.parseInt(newRow), request.getParameter(newRow + "_newTestCode"));
					newDispalyTestCodes.put(Integer.parseInt(newRow),
							request.getParameter(newRow + "_newDispalyTestCode"));
					newOrders.put(Integer.parseInt(newRow), request.getParameter(newRow + "_newOrder"));
					newUnits.put(Integer.parseInt(newRow), Long.parseLong(request.getParameter(newRow + "_newType")));
				}
			}
		}

		boolean result = groupingDao.mergeTestCodes(userId, oldTestCodes, oldDispalyTestCodes, oldOrders, oldUnits,
				newTestCodes, newDispalyTestCodes, newOrders, newUnits, instument);
		if (result)
			redirectAttributes.addFlashAttribute("pageMessage", "Data Saved Successfullly...!");
		else
			redirectAttributes.addFlashAttribute("pageError", "Failed to save data. Please Try Again.");
		return "redirect:/grouping/testCodes/" + instument;
	}

	@RequestMapping(value = "/observatoinIntrumentAndPerameters/{studyAcclamatizationDatesId}/{obserVationFor}", method = RequestMethod.GET)
	public String intrumentAndPerameters(@PathVariable("studyAcclamatizationDatesId") Long studyAcclamatizationDatesId, @PathVariable("obserVationFor") String obserVationFor,
			ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		model.addAttribute("obserVationFor", obserVationFor);
		model = studyService.addIntrumentAndPerametersForStudyAcclamatizationDates(studyAcclamatizationDatesId, model,
				request, obserVationFor);
//		return (String) model.get("instumentConfigPage");
		return "pages/grouping/addIntrumentAndPerameters";
	}

	
	
	@RequestMapping(value = "/dbIntrumentAndPerametersForStudyAcclamatizationDates/{studyAcclamatizationDatesId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<ObservationClinPathTestCodesDto> studyStagoTestCodes(
			@PathVariable("studyAcclamatizationDatesId") Long studyAcclamatizationDatesId) {
		List<ObservationClinPathTestCodesDto> obj = studyService
				.dbIntrumentAndPerametersForStudyAcclamatizationDates(studyAcclamatizationDatesId);
		return obj;
	}

	@RequestMapping(value = "/intrumentAndPerameters/{studyId}", method = RequestMethod.GET)
	public String studyAndGroupingViewPost(@PathVariable("studyId") Long studyId, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		model = studyService.addIntrumentAndPerameters(studyId, model, request);
		return (String) model.get("instumentConfigPage");
	}

	@RequestMapping(value = "/intrumentAndPerameters", method = RequestMethod.POST)
	public String addIntrumentAndPerameters(@RequestParam("studyId") Long studyId, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
//		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		model = studyService.addIntrumentAndPerameters(studyId, model, request);
		return (String) model.get("instumentConfigPage");
	}

	@RequestMapping(value = "/studyAndGroupingView/{studyId}", method = RequestMethod.GET)
	public String studyAndGroupingView(@PathVariable("studyId") Long studyId, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		StudyMasterDto studyDto = groupingDao.studyView(studyId);
		model.addAttribute("studyDto", studyDto);
//		return "/pages/grouping/studyView";
		model.addAttribute("pageMessage", "Instrument configureation has completed.");
		return "studyAndGroupingView";
	}

//	study creation for only with instrument  Configuration
	@RequestMapping(value = "/createStudyAndGrouping", method = RequestMethod.GET)
	public String createStudyAndGrouping(ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		Map<String, List<TestCode>> instumentTestCodes = groupingDao.instrumentWiseTestCodes();
		Map<String, String> instrumentNames = new HashMap<>();
		for (Map.Entry<String, List<TestCode>> m : instumentTestCodes.entrySet()) {
			List<TestCode> list = m.getValue();
			for (TestCode tc : list)
				instrumentNames.put(tc.getMethodType(), tc.getInstrument());
		}
		model.addAttribute("instrumentNames", instrumentNames);
		model.addAttribute("instumentTestCodes", instumentTestCodes);
		List<TestCodeProfile> profiles = groupingDao.allActiveProfiles();
		Map<String, List<TestCodeProfile>> profileMaptemp = new HashMap<>();
		profiles.stream().forEach((profile) -> {
			List<TestCodeProfile> list = profileMaptemp.get(profile.getInsturment().getInstrumentName());
			if (list == null)
				list = new ArrayList<>();
			list.add(profile);
			profileMaptemp.put(profile.getInsturment().getInstrumentName(), list);
		});
		Map<String, List<TestCodeProfile>> profileMap = new HashMap<>();
		List<InstrumentIpAddress> ipAddress = groupingDao.allActiveInsturments();
		for (InstrumentIpAddress ins : ipAddress) {
			profileMap.put(ins.getInstrumentName(), profileMaptemp.get(ins.getInstrumentName()));
		}
		model.addAttribute("profiles", profiles);
		model.addAttribute("profileMap", profileMap);
		return "createStudyAndGrouping";
	}

	@RequestMapping(value = "/saveIntrumentAndPerameters", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String saveIntrumentAndPerameters( InsturmentParemtersDataDto insturmentParemtersDataDto,
			@RequestParam("studyAcclamatizationDatesId") Long observationDatesId, 
			@RequestParam("observationFor") String observationFor,ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
//		StudyAcclamatizationDates studyacc = acclimatizationService.studyAcclamatizationDatesWithId(studyAcclamatizationDatesId);

		Long userId = (Long) request.getSession().getAttribute("userId");
		String studyNo = request.getParameter("studyNo");
		Date studyDate = null;
		try {
			studyDate = dateFormat.parse(request.getParameter("startDate"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
			studyDate = new Date();
		}
		Enumeration enumeration = request.getParameterNames();
		Map<String, Object> modelMap = new HashMap<>();
		while (enumeration.hasMoreElements()) {
//            String parameterName = (String) enumeration.nextElement();
			String s = (String) enumeration.nextElement();
			System.out.println(s + " : " +request.getParameter(s));
			
//            modelMap.put(parameterName, request.getParameter(parameterName));
		}

		List<Long> testCodeIds = new ArrayList<>();
		String addedTcIdsTemp = request.getParameter("addedTcIds");
		String[] addedTcIds =  addedTcIdsTemp.split("\\,");
		Map<Long, Integer> tcOrder = new HashMap<>();
		if (addedTcIds != null && addedTcIds.length > 0) {
			for (String tcId : addedTcIds) {
				testCodeIds.add(Long.parseLong(tcId));
				tcOrder.put(Long.parseLong(tcId), Integer.parseInt(request.getParameter(tcId + "_tcOrder")));
			}
		}
		String profileIdsArrayTemp = request.getParameter("profileIds");
		String[] profileIdsArray = profileIdsArrayTemp.split("\\,");
		List<Long> profileIds = new ArrayList<>();
		if (profileIdsArray != null && profileIdsArray.length > 0) {
			for (String profileId : profileIdsArray) {
				profileIds.add(Long.parseLong(profileId));
			}
		}

		boolean result = false;
		try {
			result = groupingDao.saveIntrumentAndPerameters(userId, observationDatesId, studyNo, testCodeIds,
					tcOrder, profileIds, studyDate, observationFor);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result = false;
		}
		if (result)
			return "{\"Success\": \"" + true + "\",\"Message\":\"Data Saved Successfullly...!\"}";
		else
			return "{\"Success\": \"" + false + "\",\"Message\":\"Failed to save data. Please Try Again.\"}";

////		return "redirect:/grouping/intrumentAndPerameters/"+studyId;
//		return "redirect:/administration/studyDesignGet/"+studyAcclamatizationDatesId+"/update";

	}

	@RequestMapping(value = "/createStudy", method = RequestMethod.POST)
	public String createStudy(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		Long userId = (Long) request.getSession().getAttribute("userId");

		String studyNo = request.getParameter("studyNo");
		Date studyDate = null;
		try {
			studyDate = dateFormat.parse(request.getParameter("startDate"));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			studyDate = new Date();
		}
		String groupRows = request.getParameter("groupRows");
		List<Integer> rows = new ArrayList<>();
		Map<Integer, String> groupNames = new HashMap<>();
		Map<Integer, String> genders = new HashMap<>();
		Map<Integer, Integer> froms = new HashMap<>();
		Map<Integer, Integer> tos = new HashMap<>();
		if (groupRows != null && !groupRows.trim().equals("")) {
			groupRows = groupRows.trim();
			String[] newRows = groupRows.split("\\,");
			if (newRows != null) {
				for (String newRow : newRows) {
					rows.add(Integer.parseInt(newRow));
					groupNames.put(Integer.parseInt(newRow), request.getParameter(newRow + "_groupName"));
					genders.put(Integer.parseInt(newRow), request.getParameter(newRow + "_gender"));
					froms.put(Integer.parseInt(newRow), Integer.parseInt(request.getParameter(newRow + "_from")));
					tos.put(Integer.parseInt(newRow), Integer.parseInt(request.getParameter(newRow + "_to")));
				}
			}
		}
		Enumeration enumeration = request.getParameterNames();
		Map<String, Object> modelMap = new HashMap<>();
		while (enumeration.hasMoreElements()) {
//            String parameterName = (String) enumeration.nextElement();
			System.out.println(enumeration.nextElement());
//            modelMap.put(parameterName, request.getParameter(parameterName));
		}

		String[] addedTcIds = request.getParameterValues("addedTcIds");
		List<Long> testCodeIds = new ArrayList<>();
		Map<Long, Integer> tcOrder = new HashMap<>();
		if (addedTcIds != null && addedTcIds.length > 0) {
			for (String tcId : addedTcIds) {
				testCodeIds.add(Long.parseLong(tcId));
				tcOrder.put(Long.parseLong(tcId), Integer.parseInt(request.getParameter(tcId + "_tcOrder")));
			}
		}
		String[] profileIdsArray = request.getParameterValues("profileIds");
		List<Long> profileIds = new ArrayList<>();
		if (profileIdsArray != null && profileIdsArray.length > 0) {
			for (String profileId : profileIdsArray) {
				profileIds.add(Long.parseLong(profileId));
			}
		}

		boolean result = false;
		try {
			result = groupingDao.createStudy(userId, studyNo, rows, groupNames, genders, froms, tos, testCodeIds,
					tcOrder, profileIds, studyDate);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result = false;
		}
		if (result)
			redirectAttributes.addFlashAttribute("pageMessage", "Data Saved Successfullly...!");
		else
			redirectAttributes.addFlashAttribute("pageError", "Failed to save data. Please Try Again.");
		return "redirect:/grouping/createStudyAndGrouping";
	}

	@RequestMapping(value = "/testCodesProfiles", method = RequestMethod.GET)
	public String testCodesProfiles(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		List<TestCodeProfile> profiles = groupingDao.allTestCodeProfiles();
		List<TestCodeProfileDto> testCodesProfiles = new ArrayList<>();
		profiles.forEach((profile) -> {
			testCodesProfiles
					.add(new TestCodeProfileDto(profile.getId(), profile.getProfileName(), profile.getInsturment(),
							profile.getCreatedBy(), sdf.format(profile.getCreatedOn()), profile.isActiveStatus()));
		});
		model.addAttribute("testCodesProfiles", testCodesProfiles);
		model.addAttribute("intrumentIpAddress", groupingDao.allActiveInsturments());
		return "testCodesProfiles";
	}

	@RequestMapping(value = "/testCodesProfileView/{profileId}", method = RequestMethod.GET)
	public String testCodesProfileView(@PathVariable("profileId") Long profileId, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		TestCodeProfile profile = groupingDao.testCodeProfileById(profileId);
		List<TestCodeProfileParameters> parameters = groupingDao.testCodeProfileParameters(profileId);
		model.addAttribute("profile", profile);
		model.addAttribute("testcodes", parameters);
		return "/pages/grouping/profileParameters";
	}

	@RequestMapping(value = "/checkProfileName", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String checkProfileName(StudyCreationDto studyCreationDto, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		TestCodeProfile profile = groupingDao.testCodeProfileByStudyNo(studyCreationDto.getStudyId());
		if (profile == null)
			return "{\"Success\": \"" + false + "\"}";
		else {
			return "{\"Success\": \"" + true + "\"}";
		}

	}

	@RequestMapping(value = "/insturmentSelection/{insturmentId}", method = RequestMethod.GET)
	public String insturmentSelection(@PathVariable("insturmentId") Long insturmentId, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		Map<String, List<TestCode>> instumentTestCodes = groupingDao.insturmentTestCodes(insturmentId);
		Map<String, String> instrumentNames = new HashMap<>();
		for (Map.Entry<String, List<TestCode>> m : instumentTestCodes.entrySet()) {
			List<TestCode> list = m.getValue();
			for (TestCode tc : list)
				instrumentNames.put(tc.getMethodType(), tc.getInstrument());
		}
		model.addAttribute("instrumentNames", instrumentNames);
		model.addAttribute("instumentTestCodes", instumentTestCodes);
		return "/pages/grouping/insturmentSelection";
	}

	@RequestMapping(value = "/testCodesProfilesSave", method = RequestMethod.POST)
	public String testCodesProfilesSave(@RequestParam("instrument") Long instrument,
			@RequestParam("profileName") String profileName, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		String studyNo = request.getParameter("studyNo");

//		Enumeration enumeration = request.getParameterNames();
//		Map<String, Object> modelMap = new HashMap<>();
//		while (enumeration.hasMoreElements()) {
//			System.out.println(enumeration.nextElement());
//		}
		String addedTcIds = request.getParameter("addedTcIds");
		List<Long> testCodeIds = new ArrayList<>();
		Map<Long, Integer> tcOrder = new HashMap<>();
		if (addedTcIds != null && !addedTcIds.trim().equals("")) {
			addedTcIds = addedTcIds.trim();
			String[] tcIds = addedTcIds.split("\\,");
			if (tcIds != null) {
				for (String tcId : tcIds) {
					testCodeIds.add(Long.parseLong(tcId));
					tcOrder.put(Long.parseLong(tcId), Integer.parseInt(request.getParameter(tcId + "_tcOrder")));
				}
			}
		}
		boolean result = false;
		try {
			result = groupingDao.testCodesProfilesSave(userId, studyNo, instrument, profileName, testCodeIds, tcOrder);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result = false;
		}
		if (result)
			redirectAttributes.addFlashAttribute("pageMessage", "Data Saved Successfullly...!");
		else
			redirectAttributes.addFlashAttribute("pageError", "Failed to save data. Please Try Again.");
		return "redirect:/grouping/testCodesProfiles";
	}

	@RequestMapping(value = "/changeProfileStatus", method = RequestMethod.POST)
	public String changeProfileStatus(@RequestParam("profileId") List<Long> profileId, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		boolean result = false;
		try {
			result = groupingDao.changeProfileStatus(userId, profileId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result = false;
		}
		if (result)
			redirectAttributes.addFlashAttribute("pageMessage", "Data Saved Successfullly...!");
		else
			redirectAttributes.addFlashAttribute("pageError", "Failed to save data. Please Try Again.");
		return "redirect:/grouping/testCodesProfiles";
	}

	@RequestMapping(value = "/profileData/{profileId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody TestCodeProfileDto profileData(@PathVariable("profileId") Long profileId) {
		TestCodeProfile data = groupingDao.profileData(profileId);
		TestCodeProfileDto dto = new TestCodeProfileDto();
		dto.setProfileId(profileId);
		List<TestCodeProfileParameters> parameters = data.getParameters();

		for (TestCodeProfileParameters tp : parameters) {
			dto.getParameters().add(new TestCodeProfileParametersDto(tp.getTestCode().getId(), tp.getOrderNo()));
		}

		return dto;
	}
	
	
	@RequestMapping(value = "/sanderedParameterList/{standerdParamId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<TestCodeProfileParameters> sanderedParameterList(
			@PathVariable("standerdParamId") Long standerdParamId) {
		List<TestCodeProfileParameters> obj = studyService
				.testCodeProfileParametersList(standerdParamId);
		return obj;
	}
}
