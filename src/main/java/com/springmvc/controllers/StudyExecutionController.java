
package com.springmvc.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfRule;
import com.covide.crf.dto.ObservationAnimalDataviewDto;
import com.covide.crf.service.CrfService;
import com.covide.dto.StudyAccessionCrfSectionElementDataUpdateDto;
import com.covide.dto.StudyCrfSectionElementDataDto;
import com.covide.enums.StatusMasterCodes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.springmvc.dao.impl.ClinicalCodesDaoImple;
import com.springmvc.dto.StdSubGroupObservationCrfsDto;
import com.springmvc.dto.StudyAnimalDto;
import com.springmvc.interceptor.OneTimeInitializer;
import com.springmvc.model.ClinicalCodes;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.ReviewLevel;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAcclamatizationDates;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTreatmentDataDates;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.SubjectDataEntryDetails;
import com.springmvc.reports.RulesInfoTemp;
import com.springmvc.service.AccessionService;
import com.springmvc.service.ExpermentalDesignService;
import com.springmvc.service.InstrumentService;
import com.springmvc.service.ReviewService;
import com.springmvc.service.StudyAuditService;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;
import com.springmvc.service.impl.ClinicalCodesServiceImpl;
import com.springmvc.util.CEvents;
import com.springmvc.util.CalendarEl;
import com.springmvc.util.ExprementalData;
import com.springmvc.util.ObservationData;

@Controller
@RequestMapping("/studyExecution")
@PropertySource(value = { "classpath:application.properties" })
public class StudyExecutionController {
	@Autowired
	private Environment environment;
	@Autowired
	StudyService studyService;
	@Autowired
	ReviewService reviewService;
	@Autowired
	CrfService crfService;
	@Autowired
	UserService userService;
	@Autowired
	private ExpermentalDesignService expermentalDesignService;
	@Autowired
	StudyAuditService studyAuditService;
	@Autowired
	AccessionService accessionService;

	@Autowired
	private InstrumentService instrumentService;
	
	@Autowired
	ClinicalCodesServiceImpl clinicalCodesService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String expermentalDesign(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		StudyMaster sm = studyService
				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
//		StudyDesignStatus sds = expermentalDesignService.getStudyDesignStatusRecord(sm.getId());
//		if (sds.getStatus().getStatusCode().equals("DINR")) {
//			model.addAttribute("subjectMatrixMessage", "Expermental Desing in Review");
//			return "studyDesignMessagePage";
//		} else if (sds.getStatus().getStatusCode().equals("EDOROD")) {
//			model.addAttribute("subjectMatrixMessage", "Expermental (or) Observation in Desing Stage");
//			return "studyDesignMessagePage";
//		} else if (sds.getStatus().getStatusCode().equals("RASD")) {
		if (sm.getStatus().getStatusCode().equals("SD"))
			model.addAttribute("pageMsg", "");
		else
			model.addAttribute("pageMsg", "Study in \"Design\" State.");
		if (expermentalDesignService.checkDataEntryEligible(sm)) {
			List<GroupInfo> giList = expermentalDesignService.studyGroupInfoWithChaild(sm);
			model.addAttribute("study", sm);
			model.addAttribute("group", giList);
			return "studyExecution";
		} else {
			model.addAttribute("subjectMatrixMessage", "Expermental (or) Observation Process Not Started.");
			return "studyDesignMessagePage";
		}
	}

	@RequestMapping(value = "/subGroups/{groupId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<SubGroupInfo> subGroups(@PathVariable("groupId") Long groupId, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		return expermentalDesignService.studySubGroupInfo(groupId);
	}

	@RequestMapping(value = "/groupObservations/{groupId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<StdSubGroupObservationCrfsDto> groupObservations(@PathVariable("groupId") Long groupId,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		List<StdSubGroupObservationCrfs> list = expermentalDesignService.findAllActiveStdSubGroupObservationCrfs(null,
				expermentalDesignService.subGrouId(groupId));
		List<StdSubGroupObservationCrfsDto> result = new ArrayList<>();
		for (StdSubGroupObservationCrfs obj : list) {
			StdSubGroupObservationCrfsDto dto = new StdSubGroupObservationCrfsDto();
			dto.setId(obj.getId());
			dto.setType(obj.getCrf().getType());
			dto.setSubType(obj.getCrf().getSubType());
			dto.setPrefix(obj.getCrf().getPrefix());
			dto.setObservationName(obj.getCrf().getObservationName());
			result.add(dto);
		}
		return result;
	}

	@RequestMapping(value = "/subGroupObservations/{subGroupId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<StdSubGroupObservationCrfsDto> subGroupObservations(
			@PathVariable("subGroupId") Long subGroupId, HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		List<StdSubGroupObservationCrfs> list = expermentalDesignService.findAllActiveStdSubGroupObservationCrfs(null,
				subGroupId);
		List<StdSubGroupObservationCrfsDto> result = new ArrayList<>();
		for (StdSubGroupObservationCrfs obj : list) {
			StdSubGroupObservationCrfsDto dto = new StdSubGroupObservationCrfsDto();
			dto.setId(obj.getId());
			dto.setType(obj.getCrf().getType());
			dto.setSubType(obj.getCrf().getSubType());
			dto.setPrefix(obj.getCrf().getPrefix());
			dto.setObservationName(obj.getCrf().getObservationName());
			result.add(dto);
		}
		return result;

	}

	@RequestMapping(value = "/animals/{stdSubGroupObservationCrfsId}/{gender}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<StudyAnimalDto> animals(
			@PathVariable("stdSubGroupObservationCrfsId") Long stdSubGroupObservationCrfsId,
			@PathVariable("gender") String gender, HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		List<StudyAnimal> animals = studyService.studyAnimals(gender, true);
		List<StudyAnimalDto> result = new ArrayList<>();
		for (StudyAnimal animal : animals) {
			StudyAnimalDto dto = new StudyAnimalDto();
			dto.setId(animal.getId());
			dto.setAnimalNo(animal.getPermanentNo());
			dto.setAnimalId(animal.getAnimalId());
			dto.setAccessionNo(dto.getAnimalNo());
			result.add(dto);
		}
		return result;

	}

	@RequestMapping(value = "/reviewObjservations", method = RequestMethod.GET)
	public String reviewObjservations(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		StudyMaster sm = studyService
				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		if (expermentalDesignService.checkDataEntryEligible(sm)) {
			List<GroupInfo> giList = expermentalDesignService.studyGroupInfoWithChaild(sm);
			model.addAttribute("study", sm);
			model.addAttribute("group", giList);
			return "reviewObjservations";
		} else {
			model.addAttribute("subjectMatrixMessage", "Expermental (or) Observation Process Not Started.");
			return "reviewObjservations";
		}

	}

	@RequestMapping(value = "/subgroupSubjects", method = RequestMethod.POST)
	public String subgroupSubjects(HttpServletRequest request, ModelMap model,
			@RequestParam("subGroupId") Long subGroupId, @RequestParam("type") String type,
			RedirectAttributes redirectAttributes) {
		if (subGroupId == null) {
			if (type.equals("scheduled")) {
				return "redirect:/studyExecution/viewExpermantalInCalender";
			} else {
				return "redirect:/accession/viewDataEntryForms";
			}
		}
		StudyMaster sm = studyService
				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		/*
		 * System.out.println(request.getParameter("subGroupId")); Long id =
		 * Long.parseLong(request.getParameter("subGroupId").toString());
		 */
//		List<AnimalCage> cages = expermentalDesignService.animalCages(sm.getId());
//		List<AnimalCage> cages = expermentalDesignService.groupAnimalCages(sm.getId(), subGroupId);
//		model.addAttribute("cages", cages);
		List<StdSubGroupObservationCrfs> ssgocList = expermentalDesignService.getSubGroupObservations(sm.getId(), null,
				subGroupId);
		SubGroupInfo sgi = expermentalDesignService.subGroupInfoAllById(sm, subGroupId, null);
		model.addAttribute("subjects", sgi);
		model.addAttribute("obvList", ssgocList);
//		return "subgroupSubjects";
		return "subGroupObservations";
	}

	@RequestMapping(value = "/subgroupSubjectsReview", method = RequestMethod.POST)
	public String subgroupSubjectsReview(HttpServletRequest request, ModelMap model,
			@RequestParam("groupId") Long groupId, @RequestParam("subGroupId") Long subGroupId,
			RedirectAttributes redirectAttributes) {
		StudyMaster sm = studyService
				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		System.out.println(request.getParameter("subGroupId"));
		List<StdSubGroupObservationCrfs> ssgocList = expermentalDesignService.getSubGroupObservations(sm.getId(),
				groupId, subGroupId);
		SubGroupInfo sgi = expermentalDesignService.subGroupInfoAllById(sm, subGroupId, groupId);
		model.addAttribute("subjects", sgi);
		model.addAttribute("obvList", ssgocList);
		return "subgroupSubjectsReview";

	}

	public String subgroupSubjects(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		StudyMaster sm = studyService
				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		System.out.println(request.getParameter("subGroupId"));
		Long id = Long.parseLong(request.getParameter("subGroupId").toString());
		SubGroupInfo sgi = expermentalDesignService.subGroupInfoAllById(sm, id, null);
		model.addAttribute("subjects", sgi);
		return "subgroupSubjects";
	}

	@RequestMapping(value = "/subgroupSubjects/{id}", method = RequestMethod.GET)
	public String subgroupSubjects(@PathVariable("id") Long id, HttpServletRequest request, ModelMap model,
			RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		StudyMaster sm = studyService
				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
//		SubGroupInfo sgi = expermentalDesignService.subGroupInfoById(sm, id);
		SubGroupInfo sgi = expermentalDesignService.subGroupInfoAllById(sm, id, null);
		model.addAttribute("subjects", sgi);
		return "subgroupSubjects";
	}

	@RequestMapping(value = "/weightData/{crfid}/{subGroupAnimalsInfoAllId}", method = RequestMethod.POST)
	public String weightData(@PathVariable("crfid") Long crfid,
			@PathVariable("subGroupAnimalsInfoAllId") Long subGroupAnimalsInfoAllId, HttpServletRequest request,
			ModelMap model, RedirectAttributes redirectAttributes) {
		System.out.println(request.getParameter("crfid"));
		System.out.println(request.getParameter("subGroupAnimalsInfoAllId"));

		String userName = request.getSession().getAttribute("userName").toString();
		Map<String, String> nameValues = expermentalDesignService.weightWithNameValue(crfid, subGroupAnimalsInfoAllId,
				userName);
		model.addAttribute("nameValues", nameValues);
		return "crfElementValuesFromWeightMechion";
	}

	@RequestMapping(value = "/animalCrfDataEntryGet/{animalId}/{crfId}/{sgoupId}", method = RequestMethod.GET)
	public String crfDataEntryGet(@PathVariable("animalId") Long animalId, @PathVariable("crfId") Long crfId,
			@PathVariable("sgoupId") Long sgoupId, HttpServletRequest request, ModelMap model,
			RedirectAttributes redirectAttributes) {
		String userRole = (String) request.getSession().getAttribute("userRole");
		String userName = request.getSession().getAttribute("userName").toString();
		StudyMaster sm = studyService
				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
//		Long groupId = Long.parseLong(request.getParameter("groupId").toString());

		SubGroupAnimalsInfoAll animal = expermentalDesignService.subGroupAnimalsInfoAll(animalId);
		Long groupId = animal.getSubGroup().getGroup().getId();
//		Long subGroupId = animal.getSubGroup().getId();
		model.addAttribute("animal", animal);
		StdSubGroupObservationCrfs stdCrf = crfService.stdSubGroupObservationCrfsWithCrf(crfId);
		Crf crf = stdCrf.getCrf();
//		StudyCrf crf = expermentalDesignService.studyCrf(sm, subGroupId, crfId);

		int sgaicdc = expermentalDesignService.subGroupAnimalsInfoCrfDataCount(animal, stdCrf, sgoupId);
		if (sgaicdc > 0)
			model.addAttribute("duplicateData", "yes");
		else
			model.addAttribute("duplicateData", "no");
		model.addAttribute("stdSubGroupObservationCrfsId", stdCrf.getId());
		model.addAttribute("sm", sm); // study object
		model.addAttribute("stdSubGroupObsCrf", stdCrf);
		model.addAttribute("crf", stdCrf.getCrf());
		model.addAttribute("groupId", groupId);
		model.addAttribute("subGroupId", sgoupId);
		model.addAttribute("subGroupInfoId", animalId);
		model.addAttribute("crfId", crfId);

		Date d = sm.getStartDate();
		String[] ds = stdCrf.getDays().split("\\,");
		List<String> listDates = new ArrayList<>();

		Date studyDate = sm.getStartDate();
		Date sysDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		Long noOfDays = null;
		try {
			Date date1 = sdf.parse(sdf.format(studyDate));
			Date date2 = sdf.parse(sdf.format(sysDate));
			long diff = date2.getTime() - date1.getTime();
			noOfDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
//		    noOfDays ++;
		} catch (Exception e) {
			e.printStackTrace();
			noOfDays = 0l;
		}
		if (stdCrf.getDayType().equals("day")) {
			model.addAttribute("daysCount", noOfDays);
		} else {
			if (noOfDays % 7 == 0) {
				noOfDays = noOfDays / 7;
			} else if (noOfDays >= 0)
				noOfDays = (noOfDays / 7) + 1;
			else
				noOfDays = (noOfDays / 7) - 1;
			model.addAttribute("daysCount", noOfDays);
		}

		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < ds.length; i++) {
			String dcount = ds[i];
			if (stdCrf.getDayType().equals("day")) {
				boolean dayWise = true;
				// CHECKING DATE LIKE 2-3 OR 2-8. IF ANY EXCEPTION WE WONT CONSIDER THIS DAYS
				String[] days = null;
				int start = 0;
				int end = 0;
				if (dcount.contains("-")) {
					try {
						days = dcount.split("\\-");
						start = Integer.parseInt(days[0].trim());
						end = Integer.parseInt(days[1].trim());
					} catch (Exception e) {
						// TODO: handle exception
						dayWise = false;
					}
				}

				// ABOVE CONFITION SUCESS WE ARE GOINT TO PROCESS DAYS FROM-TO
				if (dcount.contains("-") && dayWise) {
					while (start <= end) {
						cal.setTime(d);
						cal.add(Calendar.DAY_OF_MONTH, start);
						listDates.add(sdf.format(cal.getTime()));
						start++;
					}
				} else {
					cal.setTime(d);
					cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(dcount));
					listDates.add(sdf.format(cal.getTime()));

				}
			} else if (stdCrf.getDayType().equals("week")) {
//				dcount   2-6, 2 5 
//				Date studyDate = sm.getStartDate();
				boolean weekWise = true;
				if (dcount.contains("-")) {
					try {
						String[] days = dcount.split("\\-");
						int start = Integer.parseInt(days[0].trim());
						int end = Integer.parseInt(days[1].trim());
					} catch (Exception e) {
						// TODO: handle exception
						weekWise = false;
					}
				}

				if (dcount.contains("-") && weekWise) {
					String[] days = dcount.split("\\-");
					int start = Integer.parseInt(days[0].trim());
					int end = Integer.parseInt(days[1].trim());
					while (start <= end) {
						int day = start;
						for (int c = (day - 1) * 7; c < day * 7; c++) {
							cal.setTime(studyDate);
							cal.add(Calendar.DAY_OF_MONTH, c);
							listDates.add(sdf.format(cal.getTime()));
						}
						start++;
					}
				} else {
					int days = Integer.parseInt(dcount);
					if (days > -1) {
						if (days == 0)
							days = 1;
						for (int c = (days - 1) * 7; c < days * 7; c++) {
							cal.setTime(studyDate);
							cal.add(Calendar.DAY_OF_MONTH, c);
							listDates.add(sdf.format(cal.getTime()));
						}
					} else {
						days = -(days);
						int c = days * 7;
						for (int j = 1; j <= 7; j++) {
							System.out.println(c);
							cal.setTime(studyDate);
							cal.add(Calendar.DAY_OF_MONTH, -(c));
							listDates.add(sdf.format(cal.getTime()));
							c--;
							System.out.println(listDates);
						}

//						for(int c = (days-1)*7; c < days*7 ; c++) {
//							System.out.println(c);
//							cal.setTime(studyDate);
//							cal.add(Calendar.DAY_OF_MONTH, -(c));  
//							listDates.add(sdf.format(cal.getTime()));
//						}
					}
				}
			}

		}
		System.out.println(listDates);
		String[] dates = new String[listDates.size()];
		for (int i = 0; i < listDates.size(); i++) {
			dates[i] = listDates.get(i);
			System.out.println(listDates.get(i));
		}
		model.addAttribute("dates", dates);
		model.addAttribute("sysDate", sdf.format(new Date()));

		Map<String, String> caliculationFieldSec = new HashMap<String, String>();
		caliculationFieldSec = crfService.caliculationFieldSec(crf);
		model.addAttribute("caliculationFieldSec", caliculationFieldSec);

		Map<String, String> allElementIdsTypesJspStd = new HashMap<String, String>(); // key-id and value is type
		// requied field element id-key in list
		Map<String, String> requiredElementIdInJsp = new HashMap<String, String>(); // key-id and value is type
		Map<String, String> pattrenIdsAndPattren = new HashMap<String, String>(); // key-id and value is type@pattren

		allElementIdsTypesJspStd = expermentalDesignService.allElementIdsTypesJspStd(crf);
		requiredElementIdInJsp = expermentalDesignService.requiredElementIdInJspStd(crf, userRole);
		pattrenIdsAndPattren = expermentalDesignService.pattrenIdsAndPattrenStd(crf);
		model.addAttribute("requiredElementIdInJsp", requiredElementIdInJsp);
		model.addAttribute("allElementIdsTypesJspStd", allElementIdsTypesJspStd);
		model.addAttribute("pattrenIdsAndPattren", pattrenIdsAndPattren);

		// E-Form rules filed
		List<CrfRule> rules = crfService.crfRuleWithCrfAndSubElements(crf);
		List<RulesInfoTemp> rulesFieldAll = crfService.rulesFields(crf, rules);
		model.addAttribute("rulesFieldAll", rulesFieldAll);

		model.addAttribute("autoFillCrfData", "no");
		return "animalCrfDataEntry";
	}

	private String crfDataEntrypage(Long cageId, Long groupId, Long subGroupId, Long stdSubGroupObservationCrfsId,
			String type, HttpServletRequest request, ModelMap model) {
		System.out.println(
				cageId + "\t" + groupId + "\t" + subGroupId + "\t" + stdSubGroupObservationCrfsId + "\t" + type);

		String userRole = (String) request.getSession().getAttribute("userRole");
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(studyId);
		StdSubGroupObservationCrfs stdSubGroupObservationCrfs = expermentalDesignService
				.stdSubGroupObservationCrfsWithId(stdSubGroupObservationCrfsId);
		List<StudyAnimal> animals = null;

		animals = expermentalDesignService.studyAnimal(studyId);

//		StudyDesignStatus sds = studyService.studyDesignStatus(studyId);
		Crf crf = crfService.getCrfForDataEntryView(stdSubGroupObservationCrfs.getCrf().getId());

		List<String> frEleList = expermentalDesignService.getFrormulaDataofCurrentCrf(crf.getId(), studyId);

		if (sm.getStatus().getStatusCode().equals(StatusMasterCodes.SI.toString())) {
			model.addAttribute("noOfEntry", 0);

			model.addAttribute("animal", null);
			model.addAttribute("duplicateData", "no");
			model.addAttribute("stdSubGroupObservationCrfsId", stdSubGroupObservationCrfs.getId());
			model.addAttribute("sm", sm); // study object
			model.addAttribute("stdSubGroupObsCrf", stdSubGroupObservationCrfs);
			model.addAttribute("crf", crf);
			model.addAttribute("groupId", groupId);
			model.addAttribute("subGroupId", subGroupId);
			model.addAttribute("crfId", stdSubGroupObservationCrfs.getCrf().getId());

			SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
			model.addAttribute("frEleList", frEleList);
			model.addAttribute("date", sm.getStartDate());
			model.addAttribute("sysDate", sdf.format(new Date()));

			Map<String, String> caliculationFieldSec = new HashMap<String, String>();
			caliculationFieldSec = crfService.caliculationFieldSec(crf);
			model.addAttribute("caliculationFieldSec", caliculationFieldSec);

			Map<String, String> allElementIdsTypesJspStd = new HashMap<String, String>(); // key-id and value is
																							// type
			// requied field element id-key in list
			Map<String, String> requiredElementIdInJsp = new HashMap<String, String>(); // key-id and value is type
			Map<String, String> pattrenIdsAndPattren = new HashMap<String, String>(); // key-id and value is
																						// type@pattren

			allElementIdsTypesJspStd = expermentalDesignService.allElementIdsTypesJspStd(crf);
			requiredElementIdInJsp = expermentalDesignService.requiredElementIdInJspStd(crf, userRole);
			pattrenIdsAndPattren = expermentalDesignService.pattrenIdsAndPattrenStd(crf);
			model.addAttribute("requiredElementIdInJsp", requiredElementIdInJsp);
			model.addAttribute("allElementIdsTypesJspStd", allElementIdsTypesJspStd);
			model.addAttribute("pattrenIdsAndPattren", pattrenIdsAndPattren);

			// E-Form rules filed
			List<CrfRule> rules = crfService.crfRuleWithCrfAndSubElements(crf);
			List<RulesInfoTemp> rulesFieldAll = crfService.rulesFields(crf, rules);
			model.addAttribute("rulesFieldAll", rulesFieldAll);

			if (type.equalsIgnoreCase("Unscheduled")) {
				model.addAttribute("animalList", animals);
			} else {
//					model.addAttribute("weightData",
//							expermentalDesignService.crfSectionElementInstrumentValues(animal.getAnimal(), crf));
			}

			model.addAttribute("autoFillCrfData", "no");
			model.addAttribute("type", type);
			model.addAttribute("animalList", animals);
			return "animalCrfDataEntry";
		} else {
			if (!sm.getGlobalStatus().getStatusCode().equals(StatusMasterCodes.SI.toString()))
				model.addAttribute("formMessage", "Data Entry Process Done only Study In 'In-Progress' Stage.");
			else
				model.addAttribute("formMessage",
						"Experimantal Desing and Observation Desing Review Process Not Completed. Please Complete Review Process.");
			return "dataEntryMessage";
		}
	}

	@RequestMapping(value = "/animalCrfDataEntry", method = RequestMethod.POST)
	public String animalCrfDataEntry(HttpServletRequest request, @RequestParam("cageId") Long cageId,
			@RequestParam("groupId") Long groupId, @RequestParam("subGroupId") Long subGroupId,
			@RequestParam("subGroupInfoId") Long animalId, @RequestParam("crfId") Long stdSubGroupObservationCrfsId,
			@RequestParam("type") String type, ModelMap model, RedirectAttributes redirectAttributes) {
		return crfDataEntrypage(cageId, groupId, subGroupId, stdSubGroupObservationCrfsId, type, request, model);
	}

	private String crfDataEntrypage(Long stdSubGroupObservationCrfsId, String type, HttpServletRequest request,
			ModelMap model) {
		System.out.println(stdSubGroupObservationCrfsId + "\t" + type);

		String userRole = (String) request.getSession().getAttribute("userRole");
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(studyId);
		StdSubGroupObservationCrfs stdSubGroupObservationCrfs = expermentalDesignService
				.stdSubGroupObservationCrfsWithId(stdSubGroupObservationCrfsId);
		List<StudyAnimal> animals = null;

		animals = expermentalDesignService.studyAnimal(studyId);

//		StudyDesignStatus sds = studyService.studyDesignStatus(studyId);
		Crf crf = crfService.getCrfForDataEntryView(stdSubGroupObservationCrfs.getCrf().getId());

		List<String> frEleList = expermentalDesignService.getFrormulaDataofCurrentCrf(crf.getId(), studyId);

		if (sm.getStatus().getStatusCode().equals(StatusMasterCodes.SI.toString())) {
			model.addAttribute("noOfEntry", 0);

			model.addAttribute("animal", null);
			model.addAttribute("duplicateData", "no");
			model.addAttribute("stdSubGroupObservationCrfsId", stdSubGroupObservationCrfs.getId());
			model.addAttribute("sm", sm); // study object
			model.addAttribute("stdSubGroupObsCrf", stdSubGroupObservationCrfs);
			model.addAttribute("crf", crf);
			model.addAttribute("crfId", stdSubGroupObservationCrfs.getCrf().getId());

			SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
			model.addAttribute("frEleList", frEleList);

			Map<String, String> caliculationFieldSec = new HashMap<String, String>();
			caliculationFieldSec = crfService.caliculationFieldSec(crf);
			model.addAttribute("caliculationFieldSec", caliculationFieldSec);

			Map<String, String> allElementIdsTypesJspStd = new HashMap<String, String>(); // key-id and value is
																							// type
			// requied field element id-key in list
			Map<String, String> requiredElementIdInJsp = new HashMap<String, String>(); // key-id and value is type
			Map<String, String> pattrenIdsAndPattren = new HashMap<String, String>(); // key-id and value is
																						// type@pattren

			allElementIdsTypesJspStd = expermentalDesignService.allElementIdsTypesJspStd(crf);
			requiredElementIdInJsp = expermentalDesignService.requiredElementIdInJspStd(crf, userRole);
			pattrenIdsAndPattren = expermentalDesignService.pattrenIdsAndPattrenStd(crf);
			model.addAttribute("requiredElementIdInJsp", requiredElementIdInJsp);
			model.addAttribute("allElementIdsTypesJspStd", allElementIdsTypesJspStd);
			model.addAttribute("pattrenIdsAndPattren", pattrenIdsAndPattren);

			// E-Form rules filed
			List<CrfRule> rules = crfService.crfRuleWithCrfAndSubElements(crf);
			List<RulesInfoTemp> rulesFieldAll = crfService.rulesFields(crf, rules);
			model.addAttribute("rulesFieldAll", rulesFieldAll);

			if (type.equalsIgnoreCase("Unscheduled")) {
				model.addAttribute("animalList", animals);
			} else {
//					model.addAttribute("weightData",
//							expermentalDesignService.crfSectionElementInstrumentValues(animal.getAnimal(), crf));
			}

			model.addAttribute("autoFillCrfData", "no");
			model.addAttribute("type", type);
			model.addAttribute("animalList", animals);
			return "animalCrfDataEntry";
		} else {
			if (!sm.getGlobalStatus().getStatusCode().equals(StatusMasterCodes.SI.toString()))
				model.addAttribute("formMessage", "Data Entry Process Done only Study In 'In-Progress' Stage.");
			else
				model.addAttribute("formMessage",
						"Experimantal Desing and Observation Desing Review Process Not Completed. Please Complete Review Process.");
			return "dataEntryMessage";
		}
	}

	@RequestMapping(value = "/treatmentAnimalCrfDataEntry", method = RequestMethod.POST)
	public String treatmentAnimalCrfDataEntry(HttpServletRequest request, @RequestParam("clinPaht") boolean clinPaht,
			@RequestParam("studyTreatmentDataDatesId") Long studyTreatmentDataDatesId,
			@RequestParam("stdSubGroupObservationCrfsId") Long stdSubGroupObservationCrfsId,
			@RequestParam("crfId") Long crfId, @RequestParam("subGroupId") Long subGroupId,
			@RequestParam("seletedGender") String seletedGender, @RequestParam("type") String type,
			@RequestParam("selecteDate") String selecteDate, ModelMap model, RedirectAttributes redirectAttributes) {
		if (!clinPaht)
			return treatmentAnimalCrfDataEntry(studyTreatmentDataDatesId, stdSubGroupObservationCrfsId, crfId,
					subGroupId, seletedGender, type, request, model, 0, selecteDate);
		else
			return instrumentService.instrumentDataCapturPage(null, studyTreatmentDataDatesId, selecteDate, request,
					model, "NA");

	}

//	redirectAttributes.addFlashAttribute("stdSubGroupObservationCrfsId", stdSubGroupObservationCrfsId);
//	redirectAttributes.addFlashAttribute("crfId", crfId);
//	redirectAttributes.addFlashAttribute("subGroupId", subGroupId);
//	redirectAttributes.addFlashAttribute("seletedGender", seletedGender);
//	redirectAttributes.addFlashAttribute("type", type);
//	redirectAttributes.addFlashAttribute("noOfEntry", noOfEntry);
//	redirectAttributes.addFlashAttribute("selecteDate", selecteDate);
//	

	@RequestMapping(value = "/unschudelTreatmentAnimalCrfDataEntry/{stdSubGroupObservationCrfsId}/{gender}", method = RequestMethod.GET)
	public String treatmentAnimalCrfDataEntry(ModelMap model,
			@PathVariable("stdSubGroupObservationCrfsId") Long stdSubGroupObservationCrfsId,
			@PathVariable("gender") String gender, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		return treatmentAnimalCrfDataEntry(null, stdSubGroupObservationCrfsId, null, null, gender, "unscheduled",
				request, model, 0, null);
	}

	@RequestMapping(value = "/treatmentAnimalCrfDataEntry", method = RequestMethod.GET)
	public String treatmentAnimalCrfDataEntry(ModelMap model,
//			@PathVariable("stdSubGroupObservationCrfsId") Long stdSubGroupObservationCrfsId,
//			@PathVariable("crfId") Long crfId, @PathVariable("subGroupId") Long subGroupId,
//			@PathVariable("seletedGender") String seletedGender, @PathVariable("type") String type,
//			@PathVariable("noOfEntry") int noOfEntry,@RequestParam("selecteDate") String selecteDate,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Map<String, ?> attri = redirectAttributes.getFlashAttributes();
		Long stdSubGroupObservationCrfsId = (Long) model.get("stdSubGroupObservationCrfsId");
		Long studyTreatmentDataDatesId = (Long) model.get("studyTreatmentDataDatesId");
		Long crfId = (Long) model.get("crfId");
		Long subGroupId = (Long) model.get("subGroupId");
		String seletedGender = (String) model.get("seletedGender");
		String type = (String) model.get("type");
		int noOfEntry = 0;
		try {
			noOfEntry = (int) model.get("noOfEntry");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			noOfEntry = 0;
		}

		String selecteDate = (String) model.get("selecteDate");
		return treatmentAnimalCrfDataEntry(studyTreatmentDataDatesId, stdSubGroupObservationCrfsId, crfId, subGroupId,
				seletedGender, type, request, model, noOfEntry, selecteDate);
	}

	private String treatmentAnimalCrfDataEntry(Long studyTreatmentDataDatesId, Long stdSubGroupObservationCrfsId,
			Long crfId, Long subGroupId, String seletedGender, String type, HttpServletRequest request, ModelMap model,
			int noOfEntry, String selecteDate) {
		System.out.println(crfId + "\t" + subGroupId + "\t" + seletedGender + "\t" + type + "\t" + noOfEntry);
		model.addAttribute("studyTreatmentDataDatesId", studyTreatmentDataDatesId);
		model.addAttribute("stdSubGroupObservationCrfsId", stdSubGroupObservationCrfsId);
		model.addAttribute("crfId", crfId);
		model.addAttribute("subGroupId", subGroupId);
		model.addAttribute("seletedGender", seletedGender);
		model.addAttribute("type", type);

		model.addAttribute("selecteDate", selecteDate);

		String userRole = (String) request.getSession().getAttribute("userRole");
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(studyId);
		StdSubGroupObservationCrfs stdSubGroupObservationCrfs = expermentalDesignService
				.stdSubGroupObservationCrfsWithId(stdSubGroupObservationCrfsId);
//		List<StudyAnimal>  animals= new ArrayList<>();
		StudyAnimal animal = null;
		if (!type.equals("unscheduled") && !stdSubGroupObservationCrfs.getCrf().getName().equals("Record for Mortality Morbidity"))
			animal = expermentalDesignService.nextAnimalForTreatmentData(studyTreatmentDataDatesId,
					stdSubGroupObservationCrfsId, studyId, crfId, seletedGender, noOfEntry, selecteDate);
		if (animal == null && !type.equalsIgnoreCase("unscheduled") && !stdSubGroupObservationCrfs.getCrf().getName().equals("Record for Mortality Morbidity")) {
			model.addAttribute("formMessage", "Form Data Entry Completed.");
			return "dataEntryMessage";
		}
//		else if(type.equalsIgnoreCase("unscheduled")){
//			animals = expermentalDesignService.studyAnimalWithGender(studyId, seletedGender);
//		}
		else if (animal != null) {
			noOfEntry = animal.getNoOfEntry();
			model.addAttribute("noOfEntry", noOfEntry);
		}

		Crf crf = crfService.getCrfForDataEntryView(stdSubGroupObservationCrfs.getCrf().getId(), sm,
				studyTreatmentDataDatesId, animal, "Treatment", type);

		List<String> frEleList = expermentalDesignService.getFrormulaDataofCurrentCrf(crf.getId(), studyId);
		boolean entryFlag = true;
		System.out.println(sm.getStatus());
		System.out.println(sm.getStatus().getId());
		System.out.println(sm.getStatus().getStatusCode());
		if (sm.getStatus().getStatusCode().equals(StatusMasterCodes.SI.toString())) {

			model.addAttribute("duplicateData", "no");
			model.addAttribute("stdSubGroupObservationCrfsId", stdSubGroupObservationCrfs.getId());
			model.addAttribute("sm", sm); // study object
			model.addAttribute("stdSubGroupObsCrf", stdSubGroupObservationCrfs);
			model.addAttribute("crf", crf);
			model.addAttribute("subGroupId", subGroupId);
			model.addAttribute("crfId", stdSubGroupObservationCrfs.getCrf().getId());

			SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
			model.addAttribute("frEleList", frEleList);
			model.addAttribute("date", sm.getStartDate());
			model.addAttribute("sysDate", sdf.format(new Date()));

			Map<String, String> caliculationFieldSec = new HashMap<String, String>();
			caliculationFieldSec = crfService.caliculationFieldSec(crf);
			model.addAttribute("caliculationFieldSec", caliculationFieldSec);

			Map<String, String> allElementIdsTypesJspStd = new HashMap<String, String>(); // key-id and value is
																							// type
			// requied field element id-key in list
			Map<String, String> requiredElementIdInJsp = new HashMap<String, String>(); // key-id and value is type
			Map<String, String> pattrenIdsAndPattren = new HashMap<String, String>(); // key-id and value is
																						// type@pattren

			allElementIdsTypesJspStd = expermentalDesignService.allElementIdsTypesJspStd(crf);
			requiredElementIdInJsp = expermentalDesignService.requiredElementIdInJspStd(crf, userRole);
			pattrenIdsAndPattren = expermentalDesignService.pattrenIdsAndPattrenStd(crf);
			model.addAttribute("requiredElementIdInJsp", requiredElementIdInJsp);
			model.addAttribute("allElementIdsTypesJspStd", allElementIdsTypesJspStd);
			model.addAttribute("pattrenIdsAndPattren", pattrenIdsAndPattren);

			// E-Form rules filed
			List<CrfRule> rules = crfService.crfRuleWithCrfAndSubElements(crf);
			List<RulesInfoTemp> rulesFieldAll = crfService.rulesFields(crf, rules);
			model.addAttribute("rulesFieldAll", rulesFieldAll);

			if (type.equals("unscheduled")) {
//					model.addAttribute("animalList", animals);
//					model.addAttribute("animalId", 0L);
				return "/pages/studyDesign/animalCrfDataEntry";
			} else {
				if(!stdSubGroupObservationCrfs.getCrf().getName().equals("Record for Mortality Morbidity")) {
					model.addAttribute("animal", animal);
					model.addAttribute("animalId", animal.getId());
				}
//					model.addAttribute("weightData",expermentalDesignService.crfSectionElementInstrumentValues(animal.getAnimal(), crf));
			}
//				Map<Long, Map<String, String>>  animalWeightDatas = expermentalDesignService.crfSectionElementInstrumentValues(animals, crf);
//				model.addAttribute("animalWeightDatas", animalWeightDatas);
//				
			model.addAttribute("autoFillCrfData", "no");
			model.addAttribute("type", type);
			return "animalCrfDataEntry";
		} else {
//				if (!sm.getGlobalStatus().getStatusCode().equals(StatusMasterCodes.SI.toString())) 
			model.addAttribute("formMessage", "Data Entry Process Done only Study In 'In-Progress' Stage.");
//				else
//					model.addAttribute("formMessage",
//							"Experimantal Desing and Observation Desing Review Process Not Completed. Please Complete Review Process.");
			return "dataEntryMessage";
		}
	}

	@RequestMapping(value = "/clinicalCodesWithCode/{collectionCodeName}/{code}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<ClinicalCodes> clinicalCodesWithCode(HttpServletRequest request, ModelMap model,
			@PathVariable("collectionCodeName") String collectionCodeName, @PathVariable("code") String code) {
		Long studyId = Long.parseLong(request.getSession().getAttribute("activeStudyId").toString());
		return clinicalCodesService.clinicalCodesWithCode(collectionCodeName, code);
	}
	
	@RequestMapping(value = "/clinicalCodes", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody Map<Long, ClinicalCodes> clinicalCodes(HttpServletRequest request, ModelMap model) {
		List<ClinicalCodes> list =  clinicalCodesService.allActiveClinicalCodes();
		Map<Long, ClinicalCodes> result = new HashMap<>();
		for(ClinicalCodes cc : list) {
			result.put(cc.getId(), cc);
		}
		return result;
	}
	@RequestMapping(value = "/animalNumber/{code}/{type}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody List<String> animalNumber(@PathVariable("code") String code, @PathVariable("type") String type, HttpServletRequest request, ModelMap model) {
		Long studyId = Long.parseLong(request.getSession().getAttribute("activeStudyId").toString());
		return clinicalCodesService.animalNumber(code, studyId, type);
	}

	
//		
//		<input type="hidden" name="groupId" id="groupId1" value="0"/>
//		<input type="hidden" name="subGroupInfoId" id="subGroupInfoId1" value="0"/>
//		
	@RequestMapping(value = "/animalCrfDataEntryUrl", method = RequestMethod.GET)
	public String crfDataEntry(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		Map<String, ?> map = (Map<String, ?>) redirectAttributes.getFlashAttributes();
		for (Entry<String, ?> m : map.entrySet()) {
			System.out.println(m.getKey() + "  " + (String) m.getValue());
		}

//		 @RequestParam("cageId") Long cageId,
//			@RequestParam("groupId") Long groupId, @RequestParam("subGroupId") Long subGroupId,
//			@RequestParam("subGroupInfoId") Long animalId, @RequestParam("crfId") Long stdSubGroupObservationCrfsId,
//			@RequestParam("type") String type, 
//		return crfDataEntrypage(cageId, groupId, subGroupId, stdSubGroupObservationCrfsId, type, request,
//				model);
		return null;
	}

	@RequestMapping(value = "/studyCrfSecEleDateRuelCheck/{crfId}/{dateRuleDbId}/{values}", method = RequestMethod.GET)
	public @ResponseBody String studyCrfSecEleDateRuelCheck(@PathVariable("crfId") Long crfId,
//			@PathVariable("vpcId") Long vpcId,
			@PathVariable("dateRuleDbId") Long dateRuleDbId, @PathVariable("values") String values) {
		try {
			return crfService.studyCrfSecDateRuelCheck(crfId, dateRuleDbId, values);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@RequestMapping(value = "/treatmentAnimalCrfDataSave", method = RequestMethod.POST)
	public String treatmentAnimalCrfDataSave(HttpServletRequest request, ModelMap model,
			RedirectAttributes redirectAttributes,
			@RequestParam("stdSubGroupObservationCrfsId") Long stdSubGroupObservationCrfsId,
			@RequestParam("seletedGender") String seletedGender, @RequestParam("type") String type,
			@RequestParam("animalId") Long animalId) {
		Long studyTreatmentDataDatesId = null, crfId = null, subGroupId = null;
		int noOfEntry = 0;
		String selecteDate = null;
		String discrebencyFields = null;
		String deviationMessage = null;
		String frequntlyMessage = null;
		try {
			studyTreatmentDataDatesId = Long.parseLong(request.getParameter("studyTreatmentDataDatesId"));
		} catch (Exception e) {
		}
		try {
			crfId = Long.parseLong(request.getParameter("crfId"));
		} catch (Exception e) {
		}
		try {
			subGroupId = Long.parseLong(request.getParameter("subGroupId"));
			noOfEntry = Integer.parseInt(request.getParameter("noOfEntry"));
			selecteDate = request.getParameter("selecteDate");
			discrebencyFields = request.getParameter("discrebencyFields");
			deviationMessage = request.getParameter("deviationMessage");
			frequntlyMessage = request.getParameter("frequntlyMessage");
		} catch (Exception e) {
		}

		StudyMaster sm = studyService
				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));

		Long userId = (Long) request.getSession().getAttribute("userId");
		try {
			System.out.println(request.getParameter("animalId"));
			SubjectDataEntryDetails status = expermentalDesignService.studyTreatmentDataEntry(
					stdSubGroupObservationCrfsId, studyTreatmentDataDatesId, crfId, subGroupId, seletedGender, type,
					noOfEntry, userId, sm, animalId, selecteDate, discrebencyFields, deviationMessage, frequntlyMessage,
					request, type);
//			SubjectDataEntryDetails status = expermentalDesignService.studyCrfDataEntry(studyCageAnimalId,
//					subGroupInfoAllId, groupId, subGroupId, subGroupInfoId, crfId, stdSubGroupObservationCrfsId, userId,
//					sm, discrebencyFields, deviationMessage, frequntlyMessage, request, type);
			if (status != null) {
				model.addAttribute("pageMessage", "Crf Saved Successfully.");
			} else {
				model.addAttribute("pageMessage", "Faild to save Crf Data.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		redirectAttributes.addFlashAttribute("studyTreatmentDataDatesId", studyTreatmentDataDatesId);
		redirectAttributes.addFlashAttribute("stdSubGroupObservationCrfsId", stdSubGroupObservationCrfsId);
		redirectAttributes.addFlashAttribute("crfId", crfId);
		redirectAttributes.addFlashAttribute("subGroupId", subGroupId);
		redirectAttributes.addFlashAttribute("seletedGender", seletedGender);
		redirectAttributes.addFlashAttribute("type", type);
		redirectAttributes.addFlashAttribute("noOfEntry", noOfEntry);
		redirectAttributes.addFlashAttribute("selecteDate", selecteDate);

		model.addAttribute("stdSubGroupObservationCrfsId", stdSubGroupObservationCrfsId);
		model.addAttribute("crfId", crfId);
		model.addAttribute("subGroupId", subGroupId);
		model.addAttribute("seletedGender", seletedGender);
		model.addAttribute("type", type);
		model.addAttribute("noOfEntry", noOfEntry);
		model.addAttribute("selecteDate", selecteDate);
		if (!type.equals("unscheduled"))
			return "redirect:/studyExecution/treatmentAnimalCrfDataEntry";
		else
			return "redirect:/studyExecution/";
//		@RequestMapping(value = "/treatmentAnimalCrfDataEntry/{stdSubGroupObservationCrfsId}/{crfId}/{subGroupId}/{seletedGender}/{type}/{noOfEntry}", method = RequestMethod.GET)
//		return "redirect:/studyExecution/viewExpermantalInCalender";
	}

	@RequestMapping(value = "/animalCrfDataView", method = RequestMethod.POST)
	public String crfDataView(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		StudyMaster sm = studyService
				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		Long groupId = Long.parseLong(request.getParameter("groupId").toString());
		Long subGroupId = Long.parseLong(request.getParameter("subGroupId").toString());
		Long subGroupInfoId = Long.parseLong(request.getParameter("subGroupInfoId").toString()); // subject
		Long stdSubGroupObservationCrfsId = Long.parseLong(request.getParameter("crfId").toString()); // observationId
		Long userId = (Long) request.getSession().getAttribute("userId");
		LoginUsers user = userService.findById(userId);
		RoleMaster role = user.getRole();
		if (role.getRole().equals("ADMIN") || role.getRole().equals("SUPERADMIN") || role.getRole().equals("QA")
				|| role.getRole().equals("Study Director")) {
			model.addAttribute("acess", "yes");
		} else {
			model.addAttribute("acess", "no");
		}
		ObservationData data = expermentalDesignService.observationData(subGroupInfoId, stdSubGroupObservationCrfsId,
				user);
		model.addAttribute("data", data);
		return "animalCrfDataView";
	}

//	@RequestMapping(value = "/animalCrfDataExport", method = RequestMethod.POST)
//	public void animalCrfDataExport(HttpServletRequest request, HttpServletResponse response, ModelMap model,
//			RedirectAttributes redirectAttributes, HttpSession session) {
//		StudyMaster sm = studyService
//				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
//		Long groupId = Long.parseLong(request.getParameter("groupId").toString());
//		Long subGroupId = Long.parseLong(request.getParameter("subGroupId").toString());
//		Long subGroupInfoId = Long.parseLong(request.getParameter("subGroupInfoId").toString()); // subject
//		Long stdSubGroupObservationCrfsId = Long.parseLong(request.getParameter("crfId").toString()); // observationId
//		Long userId = (Long) request.getSession().getAttribute("userId");
//		LoginUsers user = userService.findById(userId);
//		RoleMaster role = user.getRole();
//		if (role.getRole().equals("ADMIN") || role.getRole().equals("SUPERADMIN") || role.getRole().equals("QA")
//				|| role.getRole().equals("Study Director")) {
//			model.addAttribute("acess", "yes");
//		} else {
//			model.addAttribute("acess", "no");
//		}
//		ObservationData data = expermentalDesignService.observationData(subGroupInfoId, stdSubGroupObservationCrfsId,
//				user);
//
//		model.addAttribute("data", data);
//
//		SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy");
//		String dateinSting = sdf.format(new Date());
//		try {
//			File file = expermentalDesignService.exportDataToExcel(data, request);
//
//			response.setContentType("application/vnd.ms-excel");
//			// response.addHeader("Content-Disposition", "attachment;
//			// filename="+data.getAnimalAll().getAnimalNo()+"_"+data.getSubGroup()+".xlsx");
//			response.addHeader("Content-Disposition", "attachment; filename=Export Data.xlsx");
//			response.setContentLength((int) file.length());
//
//			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
//
//			// Copy bytes from source to destination(outputstream in this example), closes
//			// both streams.
//			FileCopyUtils.copy(inputStream, response.getOutputStream());
//
//			redirectAttributes.addFlashAttribute("pageMessage",
//					data.getAnimalAll().getAnimalNo() + " Exported Successfully.");
//		} catch (Exception e) {
//			e.printStackTrace();
//			redirectAttributes.addFlashAttribute("pageMessage", data.getAnimalAll().getAnimalNo() + " Export Faild.");
//		}
//
////		return "redirect:/extractData/";
//	}

	@RequestMapping(value = "/viewExpermantalInfo", method = RequestMethod.GET)
	public String viewExpermantalInfo(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		StudyMaster sm = studyService
				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		ObservationData data = expermentalDesignService.allObservationData1(sm);
		List<ExprementalData> expData = expermentalDesignService.expressionData(sm);
		int max = 1;
		for (ExprementalData d : expData) {
			if (d.getMax() > max) {
				max = d.getMax();
			}
		}
		for (ExprementalData d : expData) {
			if (d.getMax() > max) {
				int i = max - d.getMax();
				List<String> days = d.getDays();
				for (; i < d.getMax(); i++) {
					days.add("");
				}
				d.setDays(days);
			}
		}
		model.addAttribute("max", max);
		model.addAttribute("expData", expData);
		model.addAttribute("study", sm);
		return "viewExpermantalInfo";
	}

//	@RequestMapping(value = "/exportExpermantalInfo", method = RequestMethod.GET)
//	public void exportExpermantalInfo(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model,
//			HttpServletResponse response, HttpSession session) {
//		model.addAllAttributes(redirectAttributes.getFlashAttributes());
//		StudyMaster sm = studyService
//				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
////		List<ExprementalDataExport> expData = expermentalDesignService.exprementalDataExport(sm); 
////		int max = 1;
////		for(ExprementalData d : expData) {
////			if(d.getMax() > max) {
////				max = d.getMax();
////			}
////		}
////		for(ExprementalData d : expData) {
////			if(d.getMax() > max) {
////				int i = max - d.getMax();
////				List<String> days = d.getDays();
////				for(; i<d.getMax(); i++) {
////					days.add("");
////				}
////				d.setDays(days);
////			}
////		}
////		model.addAttribute("max", max);
////		model.addAttribute("expData", expData);
////		model.addAttribute("study", sm);
//
//		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
//		try {
//			File file = expermentalDesignService.exportDataToExcelExport(sm, request);
//
//			response.setContentType("application/vnd.ms-excel");
//			response.addHeader("Content-Disposition",
//					"attachment; filename=" + sm.getStudyNo() + "_" + sdf.format(new Date()) + ".xlsx");
//			response.setContentLength((int) file.length());
//
//			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
//
//			// Copy bytes from source to destination(outputstream in this example), closes
//			// both streams.
//			FileCopyUtils.copy(inputStream, response.getOutputStream());
//
//			redirectAttributes.addFlashAttribute("pageMessage", sm.getStudyNo() + " Data Exported Successfully.");
//		} catch (Exception e) {
//			e.printStackTrace();
//			redirectAttributes.addFlashAttribute("pageMessage", sm.getStudyNo() + " data Export Faild.");
//		}
//	}

	@RequestMapping(value = "/viewExpermantalInCalender", method = RequestMethod.GET)
	public String viewExpermantalInCalender(HttpServletRequest request, RedirectAttributes redirectAttributes,
			Model model) throws JsonProcessingException {
		Long userRoleId = (Long) request.getSession().getAttribute("userRoleId");
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		StudyMaster sm = studyService
				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		ReviewLevel reviewLevel = crfService.reviewLevel();
		model.addAttribute("reviewLevel", reviewLevel);

		List<StudyAcclamatizationData> sadList = accessionService.getStudyAcclamatizationDataRecordsList(sm.getId(),
				userRoleId);
		Map<String, List<StudyAcclamatizationDates>> expAcclamatizationData = accessionService
				.expressionAcclamatizationDataCalender(sadList, sm);
		model.addAttribute("expAcclamatizationData", expAcclamatizationData);

		List<StdSubGroupObservationCrfs> treatmentList = accessionService.stdSubGroupObservationCrfs(sm.getId(),
				userRoleId);
//		Map<String, List<StdSubGroupObservationCrfs>> expData = expermentalDesignService.expressionDataCalender(sm);
		Map<String, List<StudyTreatmentDataDates>> expData = expermentalDesignService
				.expressionTreatmentDataCalenderForGenderSpecific(treatmentList, sm);
		model.addAttribute("expData", expData);
		model.addAttribute("study", sm);

		List<CEvents> es = new ArrayList<>();
		CEvents e = null;
		int keyCount = 1;
		Map<Integer, CEvents> acclamatizationMap = new HashMap<>();
		for (Map.Entry<String, List<StudyAcclamatizationDates>> m : expAcclamatizationData.entrySet()) {
			List<StudyAcclamatizationDates> list = m.getValue();
			if (list.size() > 0) {
				for (StudyAcclamatizationDates ssgbcd : list) {
					StudyAcclamatizationData ssgbc = ssgbcd.getStudyAcclamatizationData();
					e = new CEvents(keyCount);
					if (ssgbc.getDeviationSign().equals("+/-")) {
						e.setWindowPre(ssgbc.getDeviation());
						e.setWindowPost(ssgbc.getDeviation());
					} else if (ssgbc.getDeviationSign().equals("+")) {
						e.setWindowPost(ssgbc.getDeviation());
					} else if (ssgbc.getDeviationSign().equals("-")) {
						e.setWindowPre(ssgbc.getDeviation());
					}

					e.setTitle(ssgbcd.getDisplayGender() + "->" + e.getId() + " Acclamatization : "
							+ ssgbc.getCrf().getPrefix());
					e.setStart(m.getKey());
					e.setDivId(e.getId() + "_" + m.getKey());
					e.setGroupId("Acclamatization");
					if (ssgbc.getCrf().getPrefix().equalsIgnoreCase("Clin Path")) {
						e.setColor("Blue");
						e.setClinPaht(true);
					} else
						e.setColor(ssgbc.getColor());
					e.setGender(ssgbcd.getDisplayGender());
					e.setCrfId(ssgbc.getCrf().getId() + "");
					e.setStudyId(sm.getId() + "");
					e.setAction("Acclamatization");
					System.out.println(e.getStart());
					e.setPrimaryKey(ssgbc.getId());
					e.setStudyAcclamatizationDatesId(ssgbcd.getId());
					es.add(e);
					acclamatizationMap.put(keyCount++, e);
				}
			}
		}
		model.addAttribute("acclamatizationMap", acclamatizationMap);
		keyCount = 1;
		Map<Integer, CEvents> treatmentMap = new HashMap<>();
		for (Map.Entry<String, List<StudyTreatmentDataDates>> m : expData.entrySet()) {
			List<StudyTreatmentDataDates> list = m.getValue();
			if (list.size() > 0) {
				for (StudyTreatmentDataDates ssgbcd : list) {
					StdSubGroupObservationCrfs ssgbc = ssgbcd.getStdSubGroupObservationCrfs();
//					e = new CEvents(ssgbc.getId().intValue());
					e = new CEvents(keyCount);
					if (ssgbc.getWindowSign().equals("+/-")) {
						e.setWindowPre(ssgbc.getWindow());
						e.setWindowPost(ssgbc.getWindow());
					} else if (ssgbc.getWindowSign().equals("+")) {
						e.setWindowPost(ssgbc.getWindow());
					} else if (ssgbc.getWindowSign().equals("-")) {
						e.setWindowPre(ssgbc.getWindow());
					}
					e.setTitle(ssgbcd.getDisplayGender() + "-> Group : "
							+ ssgbc.getSubGroupInfo().getGroup().getGroupName() + ". Sub Group : "
							+ ssgbc.getSubGroupInfo().getName() + ". Observation : " + ssgbc.getCrf().getPrefix());
					e.setStart(m.getKey());
					e.setDivId(e.getId() + "_" + m.getKey());
					e.setGroupId("Treatment");
					if (ssgbc.getCrf().getPrefix().equals("Clin Path")) {
						e.setColor("Blue");
						e.setClinPaht(true);
					} else
						e.setColor(ssgbc.getColor());
					e.setSubGroupId(ssgbc.getSubGroupInfo().getId() + "");
					e.setColor(ssgbc.getColor());
					e.setGender(ssgbcd.getDisplayGender());
					e.setCrfId(ssgbc.getCrf().getId() + "");
					e.setStudyId(sm.getId() + "");
					e.setAction("Treatment");
					e.setPrimaryKey(ssgbc.getId());
					System.out.println(e.getStart());
					e.setStudyTreatmentDataDatesId(ssgbcd.getId());
					es.add(e);
					treatmentMap.put(keyCount++, e);
				}
			}
		}
		model.addAttribute("treatmentMap", treatmentMap);
		Gson gson = new Gson();
		String json = gson.toJson(es);
		model.addAttribute("jsonvalues", json);

		model.addAttribute("currentDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return "viewExpermantalInCalender";
	}

	@RequestMapping(value = "/viewExpermantalInCalenderview", method = RequestMethod.GET)
	public @ResponseBody String viewExpermantalInCalenderview(HttpServletRequest request,
			RedirectAttributes redirectAttributes, Model model) throws JsonProcessingException {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
//		StudyMaster sm =  studyService.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
//		List<ExprementalData> expData = expermentalDesignService.expressionData(sm); 
//		int max = 1;
//		for(ExprementalData d : expData) {
//			if(d.getMax() > max) {
//				max = d.getMax();
//			}
//		}
//		for(ExprementalData d : expData) {
//			if(d.getMax() > max) {
//				int i = max - d.getMax();
//				List<String> days = d.getDays();
//				for(; i<d.getMax(); i++) {
//					days.add("");
//				}
//				d.setDays(days);
//			}
//		}
//		model.addAttribute("max", max);
//		model.addAttribute("expData", expData);
//		model.addAttribute("study", sm);

		CalendarEl cal = new CalendarEl();
		List<CEvents> es = new ArrayList<>();
		CEvents e = new CEvents(1);

		e.setTitle("All Day Event");
		e.setStart("2022-04-01");
		es.add(e);
		e = new CEvents(2);
		e.setTitle("Long Event");
		e.setStart("2022-04-07");
//		e.setEnd("2022-04-10");
		es.add(e);
		e = new CEvents(3);
//		e.setGroupId("999");
		e.setTitle("Repeating Event");
		e.setStart("2022-04-09T16:00:00");
		es.add(e);
		e = new CEvents(4);
//		e.setGroupId("999");
		e.setTitle("Repeating Event");
		e.setStart("2022-04-16T16:00:00");
		es.add(e);
		e = new CEvents(5);
		e.setTitle("Conference");
		e.setStart("2022-04-11");
//		e.setEnd("2022-04-13");
		es.add(e);
		e = new CEvents(6);
		e.setTitle("Meeting");
		e.setStart("2022-04-12T10:30:00");
//		e.setEnd("2022-04-12T12:30:00");
		es.add(e);
		e = new CEvents(7);
		e.setTitle("Lunch");
		e.setStart("2022-04-12T12:00:00");
		es.add(e);
		e = new CEvents(8);
		e.setTitle("Lunch");
		e.setStart("2022-04-12T14:00:00");
		es.add(e);
		e = new CEvents(9);
		e.setTitle("Lunch");
		e.setStart("2022-04-12T17:00:00");
		es.add(e);
		e = new CEvents(10);
		e.setTitle("Dinner");
		e.setStart("2022-04-12T20:00:00");
		es.add(e);

		e = new CEvents(11);
		e.setTitle("Birthday Party");
		e.setStart("2022-04-13T07:00:00");
		es.add(e);
		e = new CEvents(12);
		e.setTitle("Click for Google");
//		e.setUrl("http://google.com/");
		e.setStart("2022-04-28");
		es.add(e);
		cal.setEvents(es);

//		List l = new ArrayList();
//		CEvents c = new CEvents(1);
//		 c.setId(1);
//		 c.setStart("2022-04-28");
//		 c.setEnd("2022-04-29");
//		 c.setTitle("Task in Progress");
//		 
//		 CEvents d = new CEvents(2);
//		 c.setId(2);
//		 c.setStart("2022-04-26");
//		 c.setEnd("2022-05-28");
//		 c.setTitle("Task in Progress");
//		 
//		 l.add(c);
//		l.add(d);

		Gson gson = new Gson();
		String json = gson.toJson(es);
//		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//		String json = ow.writeValueAsString(es);
//		model.addAttribute("jsonvalues", json);
//		model.addAttribute("calendarEl", cal);
		return json;
	}

	@RequestMapping(value = "/subgroupSubjectsAllReviews", method = RequestMethod.POST)
	public String subgroupSubjectsAllReviews(HttpServletRequest request, ModelMap model,
			RedirectAttributes redirectAttributes) {
		StudyMaster sm = studyService
				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		System.out.println(request.getParameter("subGroupId1"));
		Long id = Long.parseLong(request.getParameter("subGroupId1").toString());
		StudyMaster study = studyService.findByStudyNo(sm.getStudyNo());
		List<SubGroupAnimalsInfoCrfDataCount> sgaindcList = studyAuditService
				.getSubGroupAnimalsInfoCrfDataCountWithStudyIdAndSubId(study, id);
		List<SubGroupAnimalsInfoCrfDataCount> sgaindcListData = new ArrayList<>();
		List<String> animalno = new ArrayList<>();
		String groupn = "";
		String subgroupn = "";

		Long groupnid = null;
		Long subgroupnid = null;
		Long subjectgroupnid = null;
		for (SubGroupAnimalsInfoCrfDataCount sga : sgaindcList) {
			if (!animalno.contains(sga.getCrf().getObservationName())) {
				animalno.add(sga.getCrf().getObservationName());
				sgaindcListData.add(sga);
				groupn = sga.getGroup().getGroupName();
				subgroupn = sga.getSubGroup().getName();

				groupnid = sga.getGroup().getId();
				subgroupnid = sga.getSubGroup().getId();
				subjectgroupnid = sga.getSubGroupAnimalsInfoAll().getId();
			}
		}
		model.addAttribute("groupname", groupn);
		model.addAttribute("subgroup", subgroupn);
		model.addAttribute("groupid", groupnid);
		model.addAttribute("subgroupnid", subgroupnid);
		model.addAttribute("subjectgroupnid", subjectgroupnid);

		model.addAttribute("study", study);
		model.addAttribute("sgaindcListData", sgaindcListData);
		return "subgroupSubjectsAllReviews";
	}

	@RequestMapping(value = "/reviewObservationForm/{studyTreatmentDataDatesId}/{stdSubGroupObservationCrfsId}/{type}/{gender}", method = RequestMethod.GET)
	public String reviewObservationForm(ModelMap model,
			@PathVariable("studyTreatmentDataDatesId") Long studyTreatmentDataDatesId,
			@PathVariable("stdSubGroupObservationCrfsId") Long stdSubGroupObservationCrfsId,
			@PathVariable("type") String type, @PathVariable("gender") String gender, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		ObservationAnimalDataviewDto aadDto = reviewService.observatoinAnimalDataDtoDetails(studyId,
				studyTreatmentDataDatesId, stdSubGroupObservationCrfsId, userId, false, gender, type);
		model.addAttribute("aadDto", aadDto);
		model.addAttribute("crfId", stdSubGroupObservationCrfsId);
		if (type.equals("review"))
			return "observationAnimalDataEntryReview";
		else
			return "observationAnimalDataEntryView";
	}

	@RequestMapping(value = "/obsercatoinActivityUpdateView/{eleId}/{activityType}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody StudyCrfSectionElementDataDto accessionActivityUpdateView(ModelMap model,
			@PathVariable("eleId") Long eleId, @PathVariable("activityType") String activityType,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		return accessionService.observationActivityUpdateView(eleId, activityType);
	}

	@RequestMapping(value = "/observationActivityElementUpdate", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String accessionActivityUpdate(StudyAccessionCrfSectionElementDataUpdateDto updateEleData,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		return accessionService.accessionActivityElementUpdate(updateEleData, userId, "observation");
	}

	@RequestMapping(value = "/animalCrfDataExportTwo", method = RequestMethod.POST)
	public void animalCrfDataExportTwo(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			RedirectAttributes redirectAttributes, HttpSession session) {
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
//		StudyMaster sm = studyService.findByStudyId(studyId);
		Long stdSubGroupObservationCrfsId = Long.parseLong(request.getParameter("crfId").toString()); // observationId
		Long studyTreatmentDataDatesId = Long.parseLong(request.getParameter("studyTreatmentDataDatesId").toString()); // observationId
		Long userId = (Long) request.getSession().getAttribute("userId");
		LoginUsers user = userService.findById(userId);
		RoleMaster role = user.getRole();
		if (role.getRole().equals("ADMIN") || role.getRole().equals("SUPERADMIN") || role.getRole().equals("QA")
				|| role.getRole().equals("Study Director")) {
			model.addAttribute("acess", "yes");
		} else {
			model.addAttribute("acess", "no");
		}
		String gender = request.getParameter("gender");
		ObservationAnimalDataviewDto data = reviewService.observatoinAnimalDataDtoDetails(studyId,
				studyTreatmentDataDatesId, stdSubGroupObservationCrfsId, userId, true, gender, "scheduled");
//		ObservationData data = expermentalDesignService.observationDataAllAnimal(subGroupInfoId,
//				stdSubGroupObservationCrfsId, user, groupId, subGroupId);

		model.addAttribute("data", data);

		SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy");
		String dateinSting = sdf.format(new Date());
		try {
			File file = expermentalDesignService.exportDataToExcel(data, request);

			response.setContentType("application/vnd.ms-excel");
			// response.addHeader("Content-Disposition", "attachment;
			// filename="+data.getAnimalAll().getAnimalNo()+"_"+data.getSubGroup()+".xlsx");
			response.addHeader("Content-Disposition", "attachment; filename=Export Data.xlsx");
			response.setContentLength((int) file.length());

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			// Copy bytes from source to destination(outputstream in this example), closes
			// both streams.
			FileCopyUtils.copy(inputStream, response.getOutputStream());

//			redirectAttributes.addFlashAttribute("pageMessage",
//					data.getAnimalAll().getAnimalNo() + " Exported Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
//			redirectAttributes.addFlashAttribute("pageMessage", data.getAnimalAll().getAnimalNo() + " Export Faild.");
		}

//		return "redirect:/extractData/";
	}

}
