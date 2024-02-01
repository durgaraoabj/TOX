package com.springmvc.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.Crf;
import com.covide.crf.service.CrfService;
import com.covide.dto.ExpermentalDto;
import com.covide.dto.StudyCreationDto;
import com.covide.enums.StudyStatus;
import com.covide.template.dto.StudyMasterDto;
import com.springmvc.dao.UserWiseStudiesAsignDao;
import com.springmvc.dao.impl.GroupingDao;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.SponsorMaster;
import com.springmvc.model.StaticData;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.StudySite;
import com.springmvc.model.StudyStatusAudit;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.SubjectStatus;
import com.springmvc.model.TestCode;
import com.springmvc.model.TestCodeProfile;
import com.springmvc.model.UserWiseSitesAsignMaster;
import com.springmvc.model.UserWiseStudiesAsignMaster;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.service.AcclimatizationService;
import com.springmvc.service.ExpermentalDesignService;
import com.springmvc.service.RoleMasterService;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;
import com.springmvc.service.UserWiseStudiesAsignService;
import com.springmvc.service.impl.ClinicalCodesServiceImpl;

@Controller
@RequestMapping("/administration")
@PropertySource(value = { "classpath:application.properties" })
public class AdministrationController {
	@Autowired
	private Environment environment;

	@Autowired
	StudyService studyService;

	@Autowired
	UserService userService;

	@Autowired
	UserWiseStudiesAsignService userWiseStudiesAsignService;

	@Autowired
	UserWiseStudiesAsignDao userWiseStudiesAsignDao;

	@Autowired
	GroupingDao groupingDao;
	@Autowired
	CrfService crfService;
	@Autowired
	AcclimatizationService accService;
	@Autowired
	ClinicalCodesServiceImpl clinicalCodesService;
	@Autowired
	RoleMasterService roleMasterService; 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String adminHomePage(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		try {
			String currentStudy = request.getSession().getAttribute("activeStudyNo").toString();
			if (currentStudy != null && !currentStudy.equals("")) {
				model.addAttribute("PageHedding", "Administration");
				model.addAttribute("activeUrl", "administration/");
				return "adminDashBoard.tiles";
			} else {
				redirectAttributes.addFlashAttribute("pageWarning", "Please select study");
				return "redirect:/dashboard/userWiseActiveStudies";
			}
		} catch (NullPointerException npe) {
			if (request.getSession().getAttribute("userRole").toString().equalsIgnoreCase("ADMIN"))
				return "adminDashBoard.tiles";
			else {
				redirectAttributes.addFlashAttribute("pageWarning", "Please select study");
				return "redirect:/dashboard/userWiseActiveStudies";
			}
		}
	}

	@RequestMapping(value = "/studies", method = RequestMethod.GET)
	public String allStudies(ModelMap model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		model.addAttribute("PageHedding", "Administration");
		model.addAttribute("activeUrl", "administration/");
		String role = (String) request.getSession().getAttribute("userRole");
		Long userId = (Long) request.getSession().getAttribute("userId");
		List<StudyMaster> allStudies = null;
		if (role != null && role != "") {
			if (role.equals("TFM") || role.equals("SUPERADMIN"))
				allStudies = studyService.findAll();
			else
				allStudies = studyService.getAllStudyDesignStatusStudiesList(userId);
		}
		Map<Long, String> sdMap = studyService.getStudyDirectorsDetails(allStudies);
		model.addAttribute("sdMap", sdMap);
		model.addAttribute("allStudies", allStudies);
		return "studies.tiles";
	}

	@RequestMapping(value = "/changeStudyStatus/{studyid}", method = RequestMethod.GET)
	public String changeStudyStatus(@PathVariable("studyid") Long studyid, ModelMap model) {
		model.addAttribute("PageHedding", "Administration");
		model.addAttribute("activeUrl", "administration/");

		StudyMaster sm = studyService.findByStudyId(studyid);
		model.addAttribute("sm", sm);
		return "changeStudyStatus.tiles";
	}

	@RequestMapping(value = "/changeStudyStatus", method = RequestMethod.POST)
	public String changeStudyStatusSave(HttpServletRequest request, ModelMap model,
			RedirectAttributes redirectAttributes) {
		model.addAttribute("PageHedding", "Administration");
		model.addAttribute("activeUrl", "administration/");
		StudyMaster sm = studyService.findByStudyId(Long.parseLong(request.getParameter("studyId")));
		boolean flag = true;
		List<VolunteerPeriodCrf> list = new ArrayList<>();
		if (request.getParameter("newStatus").equals("Locked")) {
			List<SubjectStatus> ssList = studyService.subjectStatusList(sm);
			Map<Volunteer, List<StudyPeriodMaster>> map = new HashMap<>();
			for (SubjectStatus ss : ssList) {
				List<StudyPeriodMaster> plist = map.get(ss.getVol());
				if (plist == null)
					plist = new ArrayList<>();
				plist.add(ss.getPeriod());
				map.put(ss.getVol(), plist);
			}

			for (Map.Entry<Volunteer, List<StudyPeriodMaster>> m : map.entrySet()) {
				List<StudyPeriodMaster> plist = m.getValue();
				boolean crfflag = true;
				if (plist.size() > 0) {
					for (StudyPeriodMaster sp : plist) {
						List<VolunteerPeriodCrf> list2 = studyService.volunteerPeriodCrfList(sp, m.getKey());
						if (crfflag) {
							for (VolunteerPeriodCrf vpc : list2) {
								if (vpc.getExitCrf().equals("Yes") && !vpc.getCrfStatus().equals("COMPLETED")) {
									crfflag = false;
									list.add(vpc);
								}
							}
						}
					}
				}
			}
			if (list.size() > 0)
				flag = false;
		}
		if (flag) {
			String newStatus = request.getParameter("newStatus");
			StatusMaster status = studyService.getStatusMasterRecord(newStatus);
			StudyStatusAudit audit = new StudyStatusAudit();
			audit.setStudyMaster(sm);
			audit.setOldStatus(sm.getStatus().getStatusDesc());
//			audit.setNewStatus(request.getParameter("newStatus"));
			audit.setNewStatus(newStatus);
			audit.setUpdatedBy(request.getSession().getAttribute("userName").toString());
			audit.setUpdatedOn(new Date());
//			sm.setStudyStatus(request.getParameter("newStatus"));
			sm.setStatus(status);
			studyService.updateStudy(sm);
			studyService.saveStudyStatusAudit(audit);
			redirectAttributes.addFlashAttribute("pageMessage", "Study Status Changed Successfully");
		} else {
			model.addAttribute("pageError", "Bellow Subject Crf Process need to complete");
			model.addAttribute("crfInfo", list);
			return "subjectCrfProcessPending.tiles";
		}
		return "redirect:/administration/studies";
	}

	@RequestMapping(value = "/checkStudyNumber", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String checkStudyNumber(StudyCreationDto studyCreationDto, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		StudyMaster studyMaster = studyService.findByStudyNo(studyCreationDto.getStudyId(), studyCreationDto.getId());
		if (studyMaster == null)
			return "{\"Success\": \"" + false + "\"}";
		else {
			return "{\"Success\": \"" + true + "\"}";
		}

	}

	@RequestMapping(value = "/checkStudyNumber/{studyNo}/{id}", method = RequestMethod.GET)
	public String checkStudyNumber(@PathVariable("studyNo") String studyNo, @PathVariable("id") Long id,
			ModelMap model) {
		String result = "";
		StudyMaster studyMaster = studyService.findByStudyNo(studyNo, id);
		if (studyMaster == null)
			result = "";
		else
			result = "yes";
		model.addAttribute("result", result);
		return "result";
	}

	@RequestMapping(value = "/createStudy", method = RequestMethod.GET)
	public String createStudy(ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		List<LoginUsers> usersList = studyService.getStudyDirectorList();
		Map<String, List<StudyMaster>> smMap = studyService.getUserAssignedStudiesList();
		model.addAttribute("smMap", smMap);
		model.addAttribute("studyMaster", new StudyMaster());
		model.addAttribute("usersList", usersList);
		// model.addAttribute("speciesList", studyService.speciesList());
		model.addAttribute("sponsorList", studyService.sponsorList());
		return "createStudy.tiles";
	}

	@RequestMapping(value = "/createStudy", method = RequestMethod.POST)
	public String createStudy(@ModelAttribute("studyMaster") StudyMaster studyMaster,
			@RequestParam("userVal") Long userId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String username = request.getSession().getAttribute("userName").toString();
		String result = "";
		try {

			LoginUsers primary = studyService.getLoginUsersWithId(studyMaster.getSdUser());
			LoginUsers secondary = studyService.getLoginUsersWithId(studyMaster.getAsdUser());
			List<LoginUsers> userIds = new ArrayList<>();
			userIds.add(primary);
			if (secondary != null)
				userIds.add(secondary);
			studyMaster.setSdUserLable(primary.getUsername());
			if (secondary != null)
				studyMaster.setAsdUserLable(secondary.getUsername());
			result = studyService.saveStudyMasterRecord(studyMaster, username, userId, userIds);
			if (result.equals("success"))
				redirectAttributes.addFlashAttribute("pageMessage", "Study Creation Done Successfully...!");
			else
				redirectAttributes.addFlashAttribute("pageError", "Study Creation Failed. Please Try Again.");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("pageError", "Internal Error. Please Contact Your Administrator.");
		}
		return "redirect:/administration/createStudy";
	}

	@RequestMapping(value = "/studyMetaData", method = RequestMethod.GET)
	public String studyMetaData(ModelMap model, HttpServletRequest request) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		List<StudyMaster> smList = studyService.getStudyMasterListBasedOnLogin(userId);
		model.addAttribute("smList", smList);

		List<StudyMaster> smListView = studyService.getStudyMasterListBasedOnLoginVeiw(userId);
		model.addAttribute("smListView", smListView);
		return "studyMetaData";
	}

	@RequestMapping(value = "/studyDesign", method = RequestMethod.GET)
	public String studyDesign(ModelMap model, HttpServletRequest request) {
		return "redirect:/dashboard/studyDirectorDashBoard";
	}

	@RequestMapping(value = "/metaDataEntry/{studyId}/{studyName}/{type}", method = RequestMethod.GET)
	public String studyMetaData(ModelMap model, @PathVariable("studyId") Long studyId,
			@PathVariable("studyName") String studyName, @PathVariable("type") String type,
			HttpServletRequest request) {
		StudyMaster sm = studyService.getStudyMasterRecord(studyId);
		model.addAttribute("studyId", studyId);
		model.addAttribute("studyName", studyName);
		model.addAttribute("sm", sm);
		model.addAttribute("speciesList", studyService.speciesList());
		model.addAttribute("units", studyService.staticDatas(StudyStatus.WEIGHTUNITS.toString()));
		model.addAttribute("study", sm);
		StudyMasterDto studyDto = groupingDao.studyView(studyId);
		model.addAttribute("studyDto", studyDto);

		// IntrumentAndPerameters
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
		model.addAttribute("allowInstrumentConfiguration", true);

		if (type.equals("entry"))
			return "studyMetaDataEntry";
		else {
			model.addAttribute("type", type);
			return "updatestudyMetaData";
		}
	}

	@RequestMapping(value = "/studyDesign", method = RequestMethod.POST)
	public String studyDesign(ModelMap model, @RequestParam("studyId") Long studyId,
			@RequestParam("actionType") String type, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		return studyDesignPage(studyId, type, model, request, redirectAttributes);
	}

	@RequestMapping(value = "/studyDesignGet/{studyId}/{type}", method = RequestMethod.GET)
	public String studyDesignGet(ModelMap model, @PathVariable("studyId") Long studyId,
			@PathVariable("type") String type, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		return studyDesignPage(studyId, type, model, request, redirectAttributes);
	}

	private String studyDesignPage(Long studyId, String type, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		StudyMaster sm = studyService.getStudyMasterRecord(studyId);
		Long userId = (Long) request.getSession().getAttribute("userId");
		UserWiseStudiesAsignMaster role = userWiseStudiesAsignService.userWiseStudiesAsignMaster(studyId, userId);
		model.addAttribute("roleName", role.getRoleId().getRole());
		request.getSession().setAttribute("studyDesignId", studyId);
		if (sm.getWeightUnits() == null) {
			StaticData sd = new StaticData();
			sd.setId(-1);
			sd.setFieldValue("--Select--");
			sm.setWeightUnits(sd);
//			sm.setWeightUnits(studyService.staticDataByCode(StudyStatus.KG.toString()));
		}
		model.addAttribute("sm", sm);
		if (type.equals("entry")) {
			model.addAttribute("units", studyService.staticDatas(StudyStatus.WEIGHTUNITS.toString()));
			model.addAttribute("studyName", sm.getStudyNo());
			model.addAttribute("speciesList", studyService.speciesList());
			model.addAttribute("observationConfig", false);
			return "studyMetaDataEntry";
		} else {
			
			model.addAttribute("type", type);
			model.addAttribute("observationConfig", true);
			model = acclimatizationFrom(model, request, studyId);
			model = observationConfig(studyId, model, request);

			model = studyService.addIntrumentAndPerameters(studyId, model, request); // instumentConfigPage
			return "updatestudyDesign";
//			return "updatestudyMetaData";
		}
	}

	@Autowired
	private ExpermentalDesignService expermentalDesignService;

	private ModelMap observationConfig(Long studyId, ModelMap model, HttpServletRequest request) {
		ExpermentalDto edto = expermentalDesignService.getExpermentalDtoDetails(studyId);
		List<GroupInfo> gi = expermentalDesignService.studyGroupInfoWithChaild(edto.getSm());
		model.addAttribute("study", edto.getSm());
		model.addAttribute("sds", edto.getSds());
		model.addAttribute("group", gi);
		return model;
	}

	private ModelMap acclimatizationFrom(ModelMap model, HttpServletRequest request, Long studyId) {
		List<Crf> crfList = crfService.findAllActiveCrfsConfigurationForAcclimatization();
		Map<Long, StudyAcclamatizationData> sadMap = accService.getStudyAcclamatizationDataList(studyId);
		model.addAttribute("sadMap", sadMap);
		model.addAttribute("studyId", studyId);
		model.addAttribute("crfList", crfList);
		return model;
	}

	@RequestMapping(value = "/updatemetaData/{studyId}/{studyName}", method = RequestMethod.GET)
	public String updatemetaData(ModelMap model, @PathVariable("studyId") Long studyId,
			@PathVariable("studyName") String studyName, HttpServletRequest request) {
		StudyMaster sm = studyService.getStudyMasterRecord(studyId);
		model.addAttribute("studyId", studyId);
		model.addAttribute("studyName", studyName);
		model.addAttribute("sm", sm);
		return "updatestudyMetaData";
	}

	@RequestMapping(value = "/saveUpdatedMetaDataDetails", method = RequestMethod.POST)
	public String saveUpdatedMetaDataDetails(@ModelAttribute("sm") StudyMaster studyMaster,
			@RequestParam("noOfGroups") Integer groups, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		String username = request.getSession().getAttribute("userName").toString();
		Long userId = (Long) request.getSession().getAttribute("userId");
		String studyNo = studyMaster.getStudyNo();
		Long studyId = studyMaster.getId();
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		List<GroupInfo> existsGroupList = new ArrayList<>();
		List<GroupInfo> newgroupList = new ArrayList<>();
		String result = "";
		Date stDate = null;
		String subgroupRequired = request.getParameter("subgroupRequired");
		if (subgroupRequired != null && subgroupRequired.equalsIgnoreCase("NA"))
			studyMaster.setRequiredSubGroup(false);
		try {
			/*
			 * if (startDate != null && !startDate.equals("")) { stDate =
			 * sdf.parse(startDate); studyMaster.setStartDate(stDate); }
			 */
			studyMaster.setStartDate(studyMaster.getAcclimatizationStarDate());
			GroupInfo gi = null;
			for (int i = 1; i <= groups; i++) {
				String groupId = request.getParameter("gid_" + i);
				Long groupNo = Long.parseLong(groupId);
				gi = new GroupInfo();
				gi.setId(groupNo);
				gi.setGroupName(request.getParameter("groupName" + i));
				if (studyMaster.isRequiredSubGroup())
					gi.setGroupTest(Integer.parseInt(request.getParameter("groupTest" + i)));
				gi.setGender(request.getParameter("groupGender" + i));
				if (Long.parseLong(groupId) != 0) {
					existsGroupList.add(gi);
				} else {
					newgroupList.add(gi);
				}
			}
			result = studyService.updateStudyMetaDataDetails(studyMaster, username, existsGroupList, newgroupList,
					groups);

			if (result.equals("success"))
				redirectAttributes.addFlashAttribute("pageMessage", "Study MetaData Saved Successfully...!");
			else
				redirectAttributes.addFlashAttribute("pageError", "Study MetaData Saving Failed. Please Try Again.");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("pageError", "Internal Error. Please Contact Your Administrator.");
		}
		return "redirect:/administration/studyMetaData";
	}

	@RequestMapping(value = "/saveMetaDataDetails", method = RequestMethod.POST)
	public String saveMetaDataDetails(@ModelAttribute("sm") StudyMaster studyMaster,
			@RequestParam("groupGender") String groupGender, @RequestParam("calculationBased") String calculationBased,
//			@RequestParam("clinpathPerameters") String clinpathPerameters, 
			@RequestParam("doseMgkg") String doseMgkg,

			@RequestParam("acclimatizationStDate") String acclimatizationStDate,
			@RequestParam("acclimatizationEnDate") String acclimatizationEnDate,
			@RequestParam("treatmentStDate") String treatmentStDate,
			@RequestParam("treatmentEnDate") String treatmentEnDate, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		String subgroupRequired = request.getParameter("subgroupRequired");
		String splitStudyByGender = request.getParameter("splitStudyGender");
		String username = request.getSession().getAttribute("userName").toString();
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		List<GroupInfo> groupInfos = new ArrayList<>();
		List<SubGroupInfo> subGroups = new ArrayList<SubGroupInfo>();
		List<SubGroupAnimalsInfo> animalInfo = new ArrayList<>();
//		List<SubGroupAnimalsInfoAll> animalInfoAll = new ArrayList<>();
		StudyMaster sm = studyService.findByStudyId(studyMaster.getId());
//		Long userId = (Long) request.getSession().getAttribute("userId");

		String result = "";
		studyMaster.setAcceptComments(sm.getAcceptComments());
		studyMaster.setAcceptStatus(sm.getAcceptStatus());
		studyMaster.setAcceptedDate(sm.getAcceptedDate());
		studyMaster.setMetaDataBy(username);
		studyMaster.setMetaDataOn(new Date());
		studyMaster.setGender(groupGender);
		if (calculationBased.equalsIgnoreCase("Yes"))
			studyMaster.setCalculationBased(true);
//		if (clinpathPerameters.equalsIgnoreCase(""))
//			studyMaster.setClinpathPerameters(true);
		if (splitStudyByGender == null || splitStudyByGender.trim().equals(""))
			studyMaster.setSplitStudyByGender(false);
		else
			studyMaster.setSplitStudyByGender(true);
		if (subgroupRequired == null || subgroupRequired.trim().equals("") || subgroupRequired.equals("NA"))
			studyMaster.setRequiredSubGroup(true);
		String[] std = studyMaster.getStudyNo().split("\\/");
		String animalPrefix = std[3] + "/";
		if (subgroupRequired != null && subgroupRequired.equalsIgnoreCase("NA"))
			studyMaster.setRequiredSubGroup(false);
		int animalNumber = 0;
		try {
			studyMaster.setStartDate(sdf.parse(acclimatizationStDate));
			studyMaster.setAcclimatizationStarDate(sdf.parse(acclimatizationStDate));
			studyMaster.setAcclimatizationEndDate(sdf.parse(acclimatizationEnDate));
			studyMaster.setTreatmentStarDate(sdf.parse(treatmentStDate));
			studyMaster.setTreatmentEndDate(sdf.parse(treatmentEnDate));
			if (studyMaster.getGender().equals("Both")
					&& (splitStudyByGender == null || splitStudyByGender.trim().equals(""))) {
				studyMaster.setAcclimatizationStarDateFemale(sdf.parse(request.getParameter("acclimatizationStDate2")));
				studyMaster.setAcclimatizationEndDateFemale(sdf.parse(request.getParameter("acclimatizationEnDate2")));
				studyMaster.setTreatmentStarDateFemale(sdf.parse(request.getParameter("treatmentStDate2")));
				studyMaster.setTreatmentEndDateFemale(sdf.parse(request.getParameter("treatmentEnDate2")));
			}

			GroupInfo gi = null;
			int count = 1;
			int groups = studyMaster.getNoOfGroups();
			for (int i = 1; i <= groups; i++) {
				gi = new GroupInfo();
				gi.setGroupNo(i);
				gi.setGroupName(request.getParameter("groupName" + i));

				if (studyMaster.isRequiredSubGroup())
					gi.setGroupTest(Integer.parseInt(request.getParameter("subGroupCount" + i))); // no of subgroups
				if (studyMaster.isRequiredSubGroup()) {
					for (int subgroupNo = 1; subgroupNo <= gi.getGroupTest(); subgroupNo++) {
						SubGroupInfo subGroup = new SubGroupInfo();
						subGroup.setSubGroupNo(subgroupNo);
						subGroup.setStudy(studyMaster);
						subGroup.setGroup(gi);
						if (!groupGender.equals("Both")) {
							subGroup.setName(request.getParameter("subGroupName" + i + "_" + subgroupNo + "_Male"));
						} else
							subGroup.setName(
									request.getParameter("subGroupName" + i + "_" + subgroupNo + "_" + gi.getGender()));
						subGroup.setGender(studyMaster.getGender());
						subGroup.setCreatedBy(request.getSession().getAttribute("userName").toString());
						subGroups.add(subGroup);
						if (!groupGender.equals("Both")) {
							gi.setGender(groupGender);
							SubGroupAnimalsInfo sgaif = new SubGroupAnimalsInfo();
							sgaif.setStudy(studyMaster);
							sgaif.setGroup(gi);
							sgaif.setSubGroup(subGroup);
							sgaif.setSequenceNo(count++);
							sgaif.setCount(Integer.parseInt(
									request.getParameter("subGroupAnimals" + i + "_" + subgroupNo + "_Male")));
							sgaif.setDoseVolume(Double
									.parseDouble(request.getParameter("doseVolume" + i + "_" + subgroupNo + "_Male")));
							sgaif.setConcentration(Double.parseDouble(
									request.getParameter("concentration" + i + "_" + subgroupNo + "_Male")));
							sgaif.setGender("Male");
							sgaif.setCreatedBy(request.getSession().getAttribute("userName").toString());

							sgaif = generateFromToAnimals(sgaif, animalNumber, animalPrefix);
							animalNumber = animalNumber + sgaif.getCount();
							animalInfo.add(sgaif);

							if (request.getParameter("subGroupAnimals" + i + "_" + subgroupNo + "_Female") != null) {
								sgaif = new SubGroupAnimalsInfo();
								sgaif.setStudy(studyMaster);
								sgaif.setGroup(gi);
								sgaif.setSubGroup(subGroup);
								sgaif.setSequenceNo(count++);
								sgaif.setCount(Integer.parseInt(
										request.getParameter("subGroupAnimals" + i + "_" + subgroupNo + "_Female")));
								sgaif.setDoseVolume(Double.parseDouble(
										request.getParameter("doseVolume" + i + "_" + subgroupNo + "_Female")));
								sgaif.setConcentration(Double.parseDouble(
										request.getParameter("concentration" + i + "_" + subgroupNo + "_Female")));
								sgaif.setGender("Female");
								sgaif.setCreatedBy(request.getSession().getAttribute("userName").toString());
								sgaif = generateFromToAnimals(sgaif, animalNumber, animalPrefix);
								animalNumber = animalNumber + sgaif.getCount();
								animalInfo.add(sgaif);
							}
						} else {
							SubGroupAnimalsInfo sgaif = new SubGroupAnimalsInfo();
							sgaif.setStudy(studyMaster);
							sgaif.setGroup(gi);
							sgaif.setSubGroup(subGroup);
							sgaif.setSequenceNo(count++);
							sgaif.setCount(Integer.parseInt(request.getParameter(
									"subGroupAnimals" + i + "_" + subgroupNo + "_" + subGroup.getGender())));
							sgaif.setDoseVolume(Double.parseDouble(request
									.getParameter("doseVolume" + i + "_" + subgroupNo + "_" + subGroup.getGender())));
							sgaif.setConcentration(Double.parseDouble(request.getParameter(
									"concentration" + i + "_" + subgroupNo + "_" + subGroup.getGender())));
							sgaif.setGender(subGroup.getGender());
							sgaif.setCreatedBy(request.getSession().getAttribute("userName").toString());
							sgaif = generateFromToAnimals(sgaif, animalNumber, animalPrefix);
							animalNumber = animalNumber + sgaif.getCount();
							animalInfo.add(sgaif);
						}
					}
				} else {
					SubGroupInfo subGroup = new SubGroupInfo();
					subGroup.setSubGroupNo(i);
					subGroup.setStudy(studyMaster);
					subGroup.setGroup(gi);
					subGroup.setName(gi.getGroupName());
					subGroup.setGender(studyMaster.getGender());
					subGroup.setCreatedBy(request.getSession().getAttribute("userName").toString());
					subGroups.add(subGroup);
					if (groupGender.equals("Both")) {
						SubGroupAnimalsInfo sgaif = new SubGroupAnimalsInfo();
						sgaif.setStudy(studyMaster);
						sgaif.setGroup(gi);
						sgaif.setSubGroup(subGroup);
						sgaif.setSequenceNo(count++);
						sgaif.setCount(1);
						sgaif.setCount(Integer.parseInt(request.getParameter("subGroupAnimals" + i + "_Male")));
						sgaif.setDoseVolume(Double.parseDouble(request.getParameter("doseVolume" + i + "_Male")));
						sgaif.setConcentration(Double.parseDouble(request.getParameter("concentration" + i + "_Male")));
						sgaif.setGender("Male");
						sgaif.setCreatedBy(request.getSession().getAttribute("userName").toString());
						sgaif = generateFromToAnimals(sgaif, animalNumber, animalPrefix);
						animalNumber = animalNumber + sgaif.getCount();
						animalInfo.add(sgaif);

						sgaif = new SubGroupAnimalsInfo();
						sgaif.setStudy(studyMaster);
						sgaif.setGroup(gi);
						sgaif.setSubGroup(subGroup);
						sgaif.setSequenceNo(count++);
						sgaif.setCount(Integer.parseInt(request.getParameter("subGroupAnimals" + i + "_Female")));
						sgaif.setDoseVolume(Double.parseDouble(request.getParameter("doseVolume" + i + "_Female")));
						sgaif.setConcentration(
								Double.parseDouble(request.getParameter("concentration" + i + "_Female")));
						sgaif.setGender("Female");
						sgaif.setCreatedBy(request.getSession().getAttribute("userName").toString());
						sgaif = generateFromToAnimals(sgaif, animalNumber, animalPrefix);
						animalNumber = animalNumber + sgaif.getCount();
						animalInfo.add(sgaif);
					} else {
						SubGroupAnimalsInfo sgaif = new SubGroupAnimalsInfo();
						sgaif.setStudy(studyMaster);
						sgaif.setGroup(gi);
						sgaif.setSubGroup(subGroup);
						sgaif.setSequenceNo(count++);
						sgaif.setCount(Integer
								.parseInt(request.getParameter("subGroupAnimals" + i + "_" + subGroup.getGender())));
						sgaif.setDoseVolume(Double
								.parseDouble(request.getParameter("doseVolume" + i + "_" + subGroup.getGender())));
						sgaif.setConcentration(Double
								.parseDouble(request.getParameter("concentration" + i + "_" + subGroup.getGender())));
						sgaif.setGender(subGroup.getGender());
						sgaif.setCreatedBy(request.getSession().getAttribute("userName").toString());
						sgaif = generateFromToAnimals(sgaif, animalNumber, animalPrefix);
						animalNumber = animalNumber + sgaif.getCount();
						animalInfo.add(sgaif);
					}
				}
//				gi.setGender(request.getParameter("groupGender" + i));
				groupInfos.add(gi);
				studyMaster.setGroupInfo(groupInfos);
			}

			studyMaster.setExperimentalDesign("Done");
			studyMaster.setEdDoneBy(username);
			studyMaster.setEdDoneDate(new Date());
			studyMaster.setStudyStatus("In-Design");
//			SatusAndWorkFlowDetailsDto sawfdDto = studyService.SatusAndWorkFlowDetailsDto("EDOROD", "EXPD");
//			StatusMaster stm = null;
//			WorkFlowStatusDetails wfsd = null;
//			if(sawfdDto != null) {
//				stm = sawfdDto.getSm();
//				wfsd = sawfdDto.getWfsd();
//			}
//			StudyDesignStatus sds = new StudyDesignStatus();
//			sds.setCreatedBy(username);
//			sds.setCreatedOn(new Date());
//			sds.setStatus(stm);
//			sds.setStudyId(studyMaster.getId());
//			
//			ApplicationAuditDetails apad = new ApplicationAuditDetails();
//			apad.setAction("Expermental Design");
//			apad.setCreatedBy(username);
//			apad.setCreatedOn(new Date());
//			apad.setStudyId(studyMaster);
//			apad.setWfsdId(wfsd);

			result = studyService.saveStudyMetaDataDetails(studyMaster, username, groupInfos, subGroups, animalInfo);
//
//// Clinical Pathology Parameters Saving Start Code
//			Date studyDate = null;
//			studyDate = studyMaster.getAcclimatizationStarDate();
//			Enumeration enumeration = request.getParameterNames();
//			Map<String, Object> modelMap = new HashMap<>();
//			while (enumeration.hasMoreElements()) {
////	            String parameterName = (String) enumeration.nextElement();
//				System.out.println(enumeration.nextElement());
////	            modelMap.put(parameterName, request.getParameter(parameterName));
//			}
//
//			String[] addedTcIds = request.getParameterValues("addedTcIds");
//			List<Long> testCodeIds = new ArrayList<>();
//			Map<Long, Integer> tcOrder = new HashMap<>();
//			if (addedTcIds != null && addedTcIds.length > 0) {
//				for (String tcId : addedTcIds) {
//					testCodeIds.add(Long.parseLong(tcId));
//					tcOrder.put(Long.parseLong(tcId), Integer.parseInt(request.getParameter(tcId + "_tcOrder")));
//				}
//			}
//			String[] profileIdsArray = request.getParameterValues("profileIds");
//			List<Long> profileIds = new ArrayList<>();
//			if (profileIdsArray != null && profileIdsArray.length > 0) {
//				for (String profileId : profileIdsArray) {
//					profileIds.add(Long.parseLong(profileId));
//				}
//			}
//
//			boolean result1 = false;
//			try {
//				result1 = groupingDao.saveIntrumentAndPerameters(userId, studyId, studyNo, testCodeIds, tcOrder,
//						profileIds, studyDate);
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//				result1 = false;
//			}
//			// Clinical Pathology Parameters Saving End Code

			if (result.equals("success")) {
				redirectAttributes.addFlashAttribute("pageMessage", "Study MetaData Saved Successfully...!");

				return "redirect:/administration/studyDesignGet/" + studyMaster.getId() + "/udpate";
			} else {
				redirectAttributes.addFlashAttribute("pageError", "Study MetaData Saving Failed. Please Try Again.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("pageError", "Internal Error. Please Contact Your Administrator.");
		}

		return "redirect:/administration/studyDesignGet/" + studyMaster.getId() + "/entry";

	}

	private SubGroupAnimalsInfo generateFromToAnimals(SubGroupAnimalsInfo sgaif, int animalNumber,
			String animalPrefix) {
		int animalNo = animalNumber + 1;
		if (animalNo < 10) {
			sgaif.setFormId(animalPrefix + "00" + animalNo);
		} else if (animalNo < 100) {
			sgaif.setFormId(animalPrefix + "0" + animalNo);
		} else
			sgaif.setFormId(animalPrefix + animalNo);
		sgaif.setFrom(animalNo + "");
		animalNo = animalNumber + sgaif.getCount();
		if (animalNo < 10) {
			sgaif.setToId(animalPrefix + "00" + animalNo);
		} else if (animalNo < 100) {
			sgaif.setToId(animalPrefix + "0" + animalNo);
		} else
			sgaif.setToId(animalPrefix + animalNo);
		sgaif.setTo(animalNo + "");
		return sgaif;
	}

	@RequestMapping(value = "/asignStudy", method = RequestMethod.GET)
	public String asignCurrentStudy(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		String studyNo = request.getSession().getAttribute("activeStudyNo").toString();
		StudyMaster study = studyService.findByStudyNo(studyNo);
		List<UserWiseStudiesAsignMaster> activeUsers = userWiseStudiesAsignService.findUsersFindByStudy(study);
		List<RoleMaster> rolesList = roleMasterService.findAll();
		Map<Long, Long> slrMap = userWiseStudiesAsignService.getStudyLevleRolesList(study.getId());
		List<String> activedUsers = new ArrayList<>();
		for (UserWiseStudiesAsignMaster user : activeUsers) {
			if (user.getStatus() == 'T')
				activedUsers.add(user.getUserId().getUsername());
		}
		model.addAttribute("activedUsers", activedUsers);
		model.addAttribute("slrMap", slrMap);
		List<LoginUsers> allLoginUsers = userService.findAllActiveUsers();
		model.addAttribute("allLoginUsers", allLoginUsers);
		model.addAttribute("study", study);
		model.addAttribute("rolesList", rolesList);
		model.addAttribute("PageHedding", "Administration");
		model.addAttribute("activeUrl", "administration/");
		return "asignStudy.tiles";
	}

	@RequestMapping(value = "/asignStudySave", method = RequestMethod.POST)
	public String asignStudySave(ModelMap model, @RequestParam("userInfo") List<Long> userIds,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
//		Enumeration<String> names = request.getParameterNames();
//		 while(names.hasMoreElements()){
//			 	System.out.println(names.nextElement());
//	    }
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		Long userId = (Long) request.getSession().getAttribute("userId");
		List<Long> allLoginUserIds = new ArrayList<>();

		List<LoginUsers> allLoginUsers = userService.findAllActiveUsers();
		allLoginUsers.forEach((u) -> {
			allLoginUserIds.add(u.getId());
		});

		Map<Long, Long> roleIds = new HashMap<>();
		Map<Long, String> comments = new HashMap<>();
		for (Long uid : allLoginUserIds) {
			System.out.println(request.getParameter("roleName_" + uid) + "  " + request.getParameter(uid + "_comment"));
			if (request.getParameter("roleName_" + uid) != null) {
				roleIds.put(uid, Long.parseLong(request.getParameter("roleName_" + uid)));
			}
			if (request.getParameter(uid + "_comment") != null) {
				comments.put(uid, request.getParameter(uid + "_comment"));
			}
		}

		try {
			String result = userWiseStudiesAsignService.assingStudy(userIds, roleIds, comments, studyId, userId);
			redirectAttributes.addFlashAttribute("pageMessage", result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("pageError", "Internal Error. Please Contact Your Administrator.");
		}
		return "redirect:/administration/asignStudy";
	}

	@RequestMapping(value = "/asignSite", method = RequestMethod.GET)
	public String asignSite(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		String studyNo = request.getSession().getAttribute("activeStudyNo").toString();
		StudyMaster study = studyService.findByStudyNo(studyNo);
		List<UserWiseSitesAsignMaster> activeUsers = userWiseStudiesAsignService.findByStudyWiseSitesList(study);
		List<LoginUsers> list = userService.findAllActiveUsers();
		List<StudySite> sites = studyService.studySite(study);
		for (StudySite site : sites) {
			for (UserWiseSitesAsignMaster user : activeUsers) {
				if (site.getId() == user.getSite().getId() && user.getStatus() == 'T')
					site.getUsersIds().add(user.getUserId().getId());
			}
		}
		model.addAttribute("study", study);
		model.addAttribute("sites", sites);
		model.addAttribute("list", list);
		return "asignSite.tiles";
	}

	@RequestMapping(value = "/asignSiteSave", method = RequestMethod.POST)
	public String asignSiteSave(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String studyNo = request.getSession().getAttribute("activeStudyNo").toString();
		StudyMaster study = studyService.findByStudyNo(studyNo);

		String userIds = request.getParameter("cserIds");
		String[] ids = userIds.split(",");
		List<String> ccids = new ArrayList<>();
		for (String s : ids)
			if (s.trim() != "")
				ccids.add(s.trim());
		Map<Long, List<Long>> map = new HashMap<>();
		for (String s : ccids) {
			String[] s1 = s.split("\\_");
			List<Long> uids = map.get(Long.parseLong(s1[0]));
			if (uids == null)
				uids = new ArrayList<>();
			uids.add(Long.parseLong(s1[1]));
			map.put(Long.parseLong(s1[0]), uids);
		}

		String userIdsRemove = request.getParameter("cserIdsRemove");
		String[] idsRemove = userIdsRemove.split(",");
		List<String> ccidsRemove = new ArrayList<>();
		for (String s : idsRemove)
			if (s.trim() != "")
				ccidsRemove.add(s.trim());
		Map<Long, List<Long>> mapRemove = new HashMap<>();
		for (String s : ccidsRemove) {
			String[] s1 = s.split("\\_");
			List<Long> uids = mapRemove.get(Long.parseLong(s1[0]));
			if (uids == null)
				uids = new ArrayList<>();
			uids.add(Long.parseLong(s1[1]));
			mapRemove.put(Long.parseLong(s1[0]), uids);
		}

		List<UserWiseSitesAsignMaster> userUpdate = new ArrayList<>();
		List<UserWiseSitesAsignMaster> activeUsers = userWiseStudiesAsignService.findByStudyWiseSitesList(study);
		for (UserWiseSitesAsignMaster user : activeUsers) {
			List<Long> cids = map.get(user.getUserId().getId());
			List<Long> cidsmapRemove = mapRemove.get(user.getUserId().getId());

			if (cids == null)
				cids = new ArrayList<>();
			if (cidsmapRemove == null)
				cidsmapRemove = new ArrayList<>();
//			System.out.println(user.getUserId().getUsername()+" : " + user.getSite().getSiteName() + " : " + user.getStatus());
			if (!cids.contains(user.getSite().getId())) {
//				user.setStatus('F');
				if (cidsmapRemove.contains(user.getSite().getId())) {
					user.setStatus('F');
				}
			} else {
				user.setStatus('T');
			}
			System.out.println(
					user.getUserId().getUsername() + " : " + user.getSite().getSiteName() + " : " + user.getStatus());
			user.setUpdatedBy(request.getSession().getAttribute("userName").toString());
			user.setUpdatedOn(new Date());
			user.setUpdateReason(request.getParameter(user.getUserId().getId() + "_comment"));
			userUpdate.add(user);
			cids.remove(user.getSite().getId());
			map.put(user.getUserId().getId(), cids);
		}

		List<StudySite> sites = studyService.studySite(study);
		Map<Long, StudySite> sitemap = new HashMap<>();
		for (StudySite site : sites) {
			sitemap.put(site.getId(), site);
		}
		List<UserWiseSitesAsignMaster> usersave = new ArrayList<>();
		for (Map.Entry<Long, List<Long>> m : map.entrySet()) {
			LoginUsers user = userService.findById(m.getKey());
			for (Long l : m.getValue()) {
				UserWiseSitesAsignMaster uwsam = new UserWiseSitesAsignMaster();
				uwsam.setStudyMaster(study);
				uwsam.setUserId(user);
				uwsam.setSite(sitemap.get(l));
				uwsam.setCreatedBy(request.getSession().getAttribute("userName").toString());
				uwsam.setCreatedOn(new Date());
				uwsam.setUpdateReason(request.getParameter(l + "_comment"));
				usersave.add(uwsam);
			}
		}
		userWiseStudiesAsignDao.saveUserWiseSitesAsignMaster(usersave);
		userWiseStudiesAsignDao.updateUserWiseSitesAsignMaster(userUpdate);
		redirectAttributes.addFlashAttribute("pageMessage", "Sites Assign Done Successfully");
		return "redirect:/administration/asignSite";
	}

	@RequestMapping(value = "/studyView/{studyid}", method = RequestMethod.GET)
	public String studyView(@PathVariable("studyid") Long studyid, ModelMap model) {
		model.addAttribute("PageHedding", "Administration");
		model.addAttribute("activeUrl", "administration/");

		StudyMaster sm = studyService.findByStudyId(studyid);
		model.addAttribute("sm", sm);
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getProperty("dateFormat"));
		model.addAttribute("smdate", sdf.format(sm.getStartDate()));
		return "studyView.tiles";
	}

	/*
	 * @RequestMapping(value="/updateStudy", method=RequestMethod.POST) public
	 * String updateStudy(@Valid StudyMaster sm, BindingResult result,
	 * HttpServletRequest request, RedirectAttributes redirectAttributes) { String
	 * username = request.getSession().getAttribute("userName").toString(); boolean
	 * flag = studyService.updateStudyMaster(sm, username); if(flag) {
	 * redirectAttributes.addFlashAttribute("infoMessage", false);
	 * redirectAttributes.addFlashAttribute("pageMessage", "Study : "
	 * +sm.getStudyNo()+" Created Successfully"); return
	 * "redirect:/administration/studies"; }else {
	 * redirectAttributes.addFlashAttribute("studyMaster", sm);
	 * redirectAttributes.addFlashAttribute("pageError", "Study : " +
	 * sm.getStudyNo()+" Creation failed"); return
	 * "redirect:/administration/createStudy"; } }
	 */
	@RequestMapping(value = "/studyUpdate", method = RequestMethod.GET)
	public String studyUpdate(ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		List<StudyMaster> smList = studyService.getStudyMasterListForUpdation();
		model.addAttribute("smList", smList);
		return "studyUpdateList";
	}

	@RequestMapping(value = "/updateStudy/{studyId}", method = RequestMethod.GET)
	public String studyUpdate(ModelMap model, @PathVariable("studyId") Long studyId) {
		List<LoginUsers> usersList = studyService.getStudyDirectorList();
		StudyMaster sm = studyService.getStudyMasterRecord(studyId);
		Map<String, List<StudyMaster>> smMap = studyService.getUserAssignedStudiesList();
		model.addAttribute("smMap", smMap);
		model.addAttribute("sm", sm);
		model.addAttribute("usersList", usersList);
		return "studyUpdatePage";
	}

	@RequestMapping(value = "/saveUpdateStudy", method = RequestMethod.POST)
	public String saveUpdateStudy(ModelMap model, @ModelAttribute("sm") StudyMaster sm, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		String userName = request.getSession().getAttribute("userName").toString();
		String result = studyService.updateStudyMasterRecord(sm, userName);
		if (result.equals("success")) {
			redirectAttributes.addFlashAttribute("pageMessage",
					"'" + sm.getStudyNo() + "' Updation Done Successfully...!");
		} else if (result.equals("Failed")) {
			redirectAttributes.addFlashAttribute("pageError",
					"'" + sm.getStudyNo() + "' Updation Failed. Pleas Try Again.");
		} else
			redirectAttributes.addFlashAttribute("pageMessage",
					"No Modification Done For " + "'" + sm.getStudyNo() + "'.");

		return "redirect:/administration/studyUpdate";
	}

	@RequestMapping(value = "/rolesCreationPage", method = RequestMethod.GET)
	public String rolesCreationPage(ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		model.addAttribute("rolePojo", new RoleMaster());

		List<RoleMaster> roles = studyService.allRoleMasters();
		model.addAttribute("roles", roles);
		return "roleCreation";
	}

	@RequestMapping(value = "/saveNewRoleMaster", method = RequestMethod.POST)
	public String saveNewRoleMaster(@ModelAttribute("rolePojo") RoleMaster role, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		String username = request.getSession().getAttribute("userName").toString();
		long no = 0;
		try {
			no = studyService.saveRoleMasterRecord(username, role);
			if (no > 0)
				redirectAttributes.addFlashAttribute("pageMessage", "Role Created Sucessfully..!");
			else
				redirectAttributes.addFlashAttribute("pageError", "Role Creation Failed. Please Tray again.");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/administration/rolesCreationPage";
	}

	@RequestMapping(value = "/getCsrfToken", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody CsrfToken getCurrentCsrfToken() {
		// quick-test
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(false);
		if (session == null) {
			return null;
		}
		return (CsrfToken) session
				.getAttribute("org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN");
	}

	@RequestMapping(value = "/createSponsorMaster", method = RequestMethod.GET)
	public String createSponsorMaster(ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		model.addAttribute("sponsor", new SponsorMaster());
		model.addAttribute("sponsorList", studyService.sponsorList());
		return "createSponsorMasterPage";
	}

	@RequestMapping(value = "/saveSponsorMaster", method = RequestMethod.POST)
	public String saveSponsorMaster(@ModelAttribute("sponsorMaster") SponsorMaster sponsorMaster,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String username = request.getSession().getAttribute("userName").toString();
		String result = "";
		try {
			sponsorMaster.setCreatedBy(username);
			sponsorMaster.setCreatedOn(new Date());
			result = studyService.saveSponsorMaster(sponsorMaster);
			if (result.equals("success"))
				redirectAttributes.addFlashAttribute("pageMessage", "Sponsor Master Done Successfully...!");
			else
				redirectAttributes.addFlashAttribute("pageError", "Sponsor Master Failed. Please Try Again.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("pageError", "Internal Error. Please Contact Your Administrator.");
		}
		return "redirect:/administration/createSponsorMaster";
	}

	@RequestMapping(value = "/getSponsorMasterUniqueCheck/{data}", method = RequestMethod.GET)
	public @ResponseBody String getSponsorMasterUniqueCheck(@PathVariable("data") String data,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String msg = "No";
		if (data != null && data != "") {
			SponsorMaster sm = studyService.getSponsorMasterUniqueCheckCode(data);
			if (sm != null) {
				msg = "Yes";
			}
		} else {
			msg = "No";
		}
		return msg;
	}

	@RequestMapping(value = "/aceeptMetdeta", method = RequestMethod.POST)
	public String aceeptMetdeta(@RequestParam("comments") String comments, @RequestParam("studyid") Long studyid,
			HttpServletRequest request, RedirectAttributes redirectAttributes, ModelMap model) {
//		String username = request.getSession().getAttribute("userName").toString();
		StudyMaster sm = studyService.getStudyMasterRecord(studyid);
		String result = "";
		try {
			sm.setStudyStatus("Selected By Director");
			sm.setAcceptComments(comments);
			sm.setAcceptStatus("Accept");
			sm.setAcceptedDate(new Date());
			result = studyService.updateAceeptMetdetaStatus(sm);
			if (result.equals("success"))
				redirectAttributes.addFlashAttribute("pageMessage", "Aceept Done Successfully...!");
			else
				redirectAttributes.addFlashAttribute("pageError", "Aceept Failed. Please Try Again.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("pageError", "Internal Error. Please Contact Your Administrator.");
		}

		return "redirect:/dashboard/studyDirectorDashBoard";
//		return "redirect:/administration/studyMetaData";
	}

	@RequestMapping(value = "/sendStudyFroReivew", method = RequestMethod.POST)
	public String createStudy(@RequestParam("studyId") Long studyId, @RequestParam("commentForReview") String commentForReview, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		String username = request.getSession().getAttribute("userName").toString();
		String result = "";
		try {
			result = studyService.sendStudyFroReivew(studyId, username, commentForReview);
			if (result.equals("success"))
				redirectAttributes.addFlashAttribute("pageMessage", "Study Sent To Reiview Successfully...!");
			else
				redirectAttributes.addFlashAttribute("pageError", "Study Sent To Reiview hasFailed. Please Try Again.");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("pageError", "Internal Error. Please Contact Your Administrator.");
		}
		return "redirect:/dashboard/studyDirectorDashBoard";
	}

	@RequestMapping(value = "/clinicalCodes", method = RequestMethod.GET)
	public String clinicalCodes(ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		model.addAttribute("clinicalCodes", clinicalCodesService.allClinicalCodes());
		return "clinicalCodes";
	}

	@RequestMapping(value = "/clinicalCodesSave", method = RequestMethod.POST)
	public String clinicalCodesSave(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		String[] unitIds = request.getParameterValues("unitId");
		Map<Long, String> oldDescription = new HashMap<>();

		if (unitIds != null) {
			for (String unitId : unitIds) {
				oldDescription.put(Long.parseLong(unitId), request.getParameter(unitId + "_description"));
			}
		}
		String newUnitrows = request.getParameter("newunits");
		Map<String, String> newHeadings = new HashMap<>();
		Map<String, String> newClinicalCode = new HashMap<>();
		Map<String, String> newClinicalSign = new HashMap<>();
		Map<String, String> newDescription = new HashMap<>();
		Map<String, String> newrank = new HashMap<>();

		if (newUnitrows != null && !newUnitrows.trim().equals("")) {
			newUnitrows = newUnitrows.trim();
			String[] newRows = newUnitrows.split("\\,");
			if (newRows != null) {
				for (String newRow : newRows) {
					newHeadings.put(newRow, request.getParameter(newRow + "_newheadding"));
					newClinicalCode.put(newRow, request.getParameter(newRow + "_newclinicalCode"));
					newClinicalSign.put(newRow, request.getParameter(newRow + "_newclinicalSign"));
					newDescription.put(newRow, request.getParameter(newRow + "_newdescription"));
					newrank.put(newRow, request.getParameter(newRow + "_newrank"));
				}
			}
		}
		boolean result = clinicalCodesService.mergeClinicalCodes(userId, oldDescription, newHeadings, newClinicalCode,
				newClinicalSign, newDescription, newrank);
		if (result)
			redirectAttributes.addFlashAttribute("pageMessage", "Data Saved Successfullly...!");
		else
			redirectAttributes.addFlashAttribute("pageError", "Failed to save data. Please Try Again.");
		return "redirect:/administration/clinicalCodes";
	}
}
