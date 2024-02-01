package com.springmvc.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.AccessionAnimalDataviewDto;
import com.covide.crf.dto.Crf;
import com.covide.crf.dto.ObservationAnimalDataviewDto;
import com.covide.crf.service.CrfService;
import com.covide.dto.ExpermentalDto;
import com.covide.dto.StudyDesignReviewDto;
import com.covide.enums.StatusMasterCodes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.itextpdf.text.pdf.events.IndexEvents.Entry;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.RadomizationReviewDto;
import com.springmvc.model.RandamizationDto;
import com.springmvc.model.ReviewLevel;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAcclamatizationDates;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTreatmentDataDates;
import com.springmvc.model.SubjectDataEntryDetails;
import com.springmvc.service.AccessionService;
import com.springmvc.service.AcclimatizationService;
import com.springmvc.service.AnimalRandomizationService;
import com.springmvc.service.ExpermentalDesignService;
import com.springmvc.service.ReviewService;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;
import com.springmvc.util.CEvents;

@Controller
@RequestMapping("/studyReview")
public class ReviewController {

	@Autowired
	ReviewService reviewService;
	@Autowired
	UserService userService;

	@Autowired
	private Environment environment;
	@Autowired
	AnimalRandomizationService animalRadService;
	@Autowired
	CrfService crfService;
	@Autowired
	StudyService studyService;
	@Autowired
	AccessionService accessionService;
	@Autowired
	private ExpermentalDesignService expermentalDesignService;

	@Autowired
	AcclimatizationService accService;
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String reviewHomePage(ModelMap model) {
		return "reviewHomePage";
	}

	@RequestMapping(value = "/designReview", method = RequestMethod.GET)
	public String designReview(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		List<StudyMaster> reviewStudys = studyService.allReivewActiveStudys();
		model.addAttribute("reviewStudys", reviewStudys);
		return "studyReviewPage";
	}
	@RequestMapping(value = "/reivewStudy", method = RequestMethod.POST)
	public String reivewStudy(@RequestParam("studyId") Long studyId, @RequestParam("reviewComment") String reviewComment,
			@RequestParam("reviewType") String reviewType,
			ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String username = request.getSession().getAttribute("userName").toString();
		String result = reviewService.studyReviewProcessSaving(username, studyId, reviewType, reviewComment);
		if (result.equals("success")) {
			if (reviewType.equals("Accept"))
				redirectAttributes.addFlashAttribute("pageMessage", "Observation Design Accepted.");
			else
				redirectAttributes.addFlashAttribute("pageMessage", "Observation Design Rejection Done.");

		} else
			redirectAttributes.addFlashAttribute("pageError",
					"Observation Design Review Process Failed. Please Try Again.");
		return "redirect:/studyReview/designReview";
//		return "studyReview/designReview";
	}
	
	@RequestMapping(value = "/studyReviewView/{studyId}", method = RequestMethod.GET)
	public String studyReviewView(@PathVariable("studyId") Long studyId, ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		model.addAttribute("sm", studyService.findByStudyId(studyId));
		model = acclimatizationFrom(model, request, studyId);
		model = observationConfig(studyId, model, request);
		model = studyService.addIntrumentAndPerameters(studyId, model, request); // instumentConfigPage
		return "pages/reviewer/studyReviewView";
	}
	
	private ModelMap acclimatizationFrom(ModelMap model, HttpServletRequest request, Long studyId) {
		List<Crf> crfList = crfService.findAllActiveCrfsConfigurationForAcclimatization();
		Map<Long, StudyAcclamatizationData> sadMap = accService.getStudyAcclamatizationDataList(studyId);
		model.addAttribute("sadMap", sadMap);
		model.addAttribute("studyId", studyId);
		model.addAttribute("crfList", crfList);
		return model;
	}
	
	private ModelMap observationConfig(Long studyId, ModelMap model, HttpServletRequest request) {
		ExpermentalDto edto = expermentalDesignService.getExpermentalDtoDetails(studyId);
		List<GroupInfo> gi = expermentalDesignService.studyGroupInfoWithChaild(edto.getSm());
		model.addAttribute("study", edto.getSm());
		model.addAttribute("sds", edto.getSds());
		model.addAttribute("group", gi);
		return model;
	}
//	@RequestMapping(value = "/designReview", method = RequestMethod.GET)
//	public String designReview(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
//		model.addAllAttributes(redirectAttributes.getFlashAttributes());
//		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
//		StudyDesignReviewDto sdrdto = reviewService.getStudyDesignReviewDto(studyId);
//		model.addAttribute("study", sdrdto.getSm());
//		model.addAttribute("group", sdrdto.getGi());
//		model.addAttribute("sm", sdrdto.getSm());
//		return "designReviewPage";
//	}

	@RequestMapping(value = "/obserVationReviewProcess", method = RequestMethod.POST)
	public String obserVationReviewProcess(ModelMap model, @RequestParam("studyId") Long studyId,
			@RequestParam("type") String type, @RequestParam("comments") String comments, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		String username = request.getSession().getAttribute("userName").toString();
		String result = reviewService.obserVationReviewProcessSaving(username, studyId, type, comments);
		if (result.equals("success")) {
			if (type.equals("Accept"))
				redirectAttributes.addFlashAttribute("pageMessage", "Observation Design Accepted.");
			else
				redirectAttributes.addFlashAttribute("pageMessage", "Observation Design Rejection Done.");

		} else
			redirectAttributes.addFlashAttribute("pageError",
					"Observation Design Review Process Failed. Please Try Again.");
		return "redirect:/studyReview/designReview";

	}

	@RequestMapping(value = "/reviewAccessionAnimalsData", method = RequestMethod.GET)
	public String viewDataEntryForms(ModelMap model, HttpServletRequest request) {
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		List<StudyAcclamatizationData> sadList = reviewService.getStudyAcclamatizationDataRecordsList(studyId);
		model.addAttribute("sadList", sadList);
		model.addAttribute("studyId", studyId);
		return "reviewDataEntryForms";
	}

	@RequestMapping(value = "/reviewAccessionAnimalsDataCalender", method = RequestMethod.GET)
	public String reviewAccessionAnimalsDataCalender(ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws JsonProcessingException {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		StudyMaster sm = studyService
				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		ReviewLevel reviewLevel = crfService.reviewLevel();
		model.addAttribute("reviewLevel", reviewLevel);

		List<StudyAcclamatizationData> sadList = accessionService.getStudyAcclamatizationDataRecordsList(sm.getId(),
				null);
		Map<String, List<StudyAcclamatizationDates>> expAcclamatizationData = accessionService
				.expressionAcclamatizationDataCalender(sadList, sm);
		
//		model.addAttribute("expAcclamatizationData", expAcclamatizationData);

		List<StdSubGroupObservationCrfs> treatmentList = accessionService.stdSubGroupObservationCrfs(sm.getId(), null);
//		Map<String, List<StdSubGroupObservationCrfs>> expData = expermentalDesignService.expressionDataCalender(sm);
		Map<String, List<StudyTreatmentDataDates>> expData = expermentalDesignService
				.expressionTreatmentDataCalenderForGenderSpecific(treatmentList, sm);
		
		Map<String, Map<Long, StdSubGroupObservationCrfs>> unscheduelTreatmentData = expermentalDesignService.unscheduelTreatmentData(sm);
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
//					e = new CEvents(ssgbc.getId().intValue());
					if (ssgbc.getDeviationSign().equals("+/-")) {
						e.setWindowPre(ssgbc.getDeviation());
						e.setWindowPost(ssgbc.getDeviation());
					} else if (ssgbc.getDeviationSign().equals("+")) {
						e.setWindowPost(ssgbc.getDeviation());
					} else if (ssgbc.getDeviationSign().equals("-")) {
						e.setWindowPre(ssgbc.getDeviation());
					}
//					e = new CEvents(ssgbc.getId().intValue());
					e.setTitle(ssgbcd.getDisplayGender() + "-> Acclamatization : " + ssgbc.getCrf().getPrefix());
					e.setStart(m.getKey());
					e.setDivId(e.getId() + "_" + m.getKey());
					e.setGroupId("Acclamatization");
					e.setColor(ssgbc.getColor());
					e.setGender(ssgbcd.getDisplayGender());
					e.setCrfId(ssgbc.getCrf().getId() + "");
					e.setStudyId(sm.getId() + "");
					e.setAction("Acclamatization");
					System.out.println(e.getStart());
					e.setPrimaryKey(ssgbc.getId());
					e.setStudyAcclamatizationDatesId(ssgbcd.getId());
					es.add(e);
//					acclamatizationMap.put(ssgbc.getId(), e);
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
					e = new CEvents(keyCount);
//					e = new CEvents(ssgbc.getId().intValue());
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
					e.setSubGroupId(ssgbc.getSubGroupInfo().getId() + "");
					e.setColor(ssgbc.getColor());
					e.setGender(ssgbcd.getDisplayGender());
					e.setCrfId(ssgbc.getCrf().getId() + "");
					e.setStudyId(sm.getId() + "");
					e.setAction("Treatment");
					System.out.println(e.getStart());
					e.setPrimaryKey(ssgbcd.getId());
					e.setStudyTreatmentDataDatesId(ssgbcd.getId());
					es.add(e);
//					treatmentMap.put(ssgbc.getId(), e);
					treatmentMap.put(keyCount++, e);
				}
			}
		}
		model.addAttribute("treatmentMap", treatmentMap);
		
		Map<Integer, CEvents> unschudelTreatmentMap = new HashMap<>();
		for (Map.Entry<String, Map<Long, StdSubGroupObservationCrfs>> m : unscheduelTreatmentData.entrySet()) {
			Map<Long, StdSubGroupObservationCrfs> map = m.getValue();
				for(Map.Entry<Long, StdSubGroupObservationCrfs> ssgbcd : map.entrySet()){
					StdSubGroupObservationCrfs ssgbc = ssgbcd.getValue();
					e = new CEvents(keyCount);
//					
					e.setTitle("Group : "
							+ ssgbc.getSubGroupInfo().getGroup().getGroupName() + ". Sub Group : "
							+ ssgbc.getSubGroupInfo().getName() + ". Observation : " + ssgbc.getCrf().getPrefix());
					e.setStart(m.getKey());
					e.setDivId(e.getId() + "_" + m.getKey());
					e.setGroupId("unscheduledTreatment");
					e.setSubGroupId(ssgbc.getSubGroupInfo().getId() + "");
					e.setColor("Yellow");
					e.setGender("");
					e.setCrfId(ssgbc.getCrf().getId() + "");
					e.setStudyId(sm.getId() + "");
					e.setAction("Treatment");
					System.out.println(e.getStart());
					e.setPrimaryKey(ssgbc.getId());
					e.setSchduleType("unscheduled");
					es.add(e);
//					treatmentMap.put(ssgbc.getId(), e);
					unschudelTreatmentMap.put(keyCount++, e);
				}
		}
		model.addAttribute("unSchudeltreatmentMap", unschudelTreatmentMap);

		Gson gson = new Gson();
		String json = gson.toJson(es);
		model.addAttribute("jsonvalues", json);

		model.addAttribute("currentDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return "reviewDataEntryFormsCalender";
	}

	@RequestMapping(value = "/reviewAccessionForm/{crfId}/{studyId}/{studyAcclamatizationDatesId}/{gender}/{dateString}", method = RequestMethod.GET)
	public String reviewAccessionAnimalsData(ModelMap model, @PathVariable("crfId") Long crfId,
			@PathVariable("studyId") Long studyId,@PathVariable("studyAcclamatizationDatesId") Long studyAcclamatizationDatesId,
			@PathVariable("gender") String gender,@PathVariable("dateString") String dateString, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		AccessionAnimalDataviewDto aadDto = reviewService.getAccessionAnimalDataDtoDetails(studyId, crfId, userId, dateString,
				gender, studyAcclamatizationDatesId);
		model.addAttribute("aadDto", aadDto);
		model.addAttribute("crfId", crfId);
		model.addAttribute("dateString", dateString);
		model.addAttribute("gender", gender);
		model.addAttribute("studyAcclamatizationDatesId", studyAcclamatizationDatesId);
		return "accessionAnimalDataEntryReview";
	}

	@RequestMapping(value = "/reviewAccessionForm", method = RequestMethod.POST)
	public String reviewAccessionForm(ModelMap model, @RequestParam("crfId") Long crfId,
			@RequestParam("studyId") Long studyId, @RequestParam("type") String type,
			@RequestParam("dateString") String dateString, @RequestParam("seletedGender") String gender, @RequestParam("studyAcclamatizationDatesId") Long studyAcclamatizationDatesId,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		AccessionAnimalDataviewDto aadDto = reviewService.getAccessionAnimalDataDtoDetails(studyId, crfId, userId,
				dateString, gender, studyAcclamatizationDatesId);
		model.addAttribute("aadDto", aadDto);
		model.addAttribute("crfId", crfId);
		model.addAttribute("dateString", dateString);
		model.addAttribute("gender", gender);
		model.addAttribute("studyAcclamatizationDatesId", studyAcclamatizationDatesId);
		return "accessionAnimalDataEntryReview";
	}
	@RequestMapping(value = "/reviewObservationForm/{schduleType}/{studyTreatmentDataDatesId}/{stdSubGroupObservationCrfsId}/{type}/{seletedGender}", method = RequestMethod.GET)
	public String reviewObservationFormGet(ModelMap model,
			@PathVariable("schduleType") String schduleType,
			@PathVariable("studyTreatmentDataDatesId") Long studyTreatmentDataDatesId,
			@PathVariable("stdSubGroupObservationCrfsId") Long stdSubGroupObservationCrfsId,
			@PathVariable("type") String type, @PathVariable("seletedGender") String gender, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
	
		if(!schduleType.equals("scheduled")) {
			studyTreatmentDataDatesId = null;
		}
		if(gender != null && gender.equals("gender"))
			gender = null;
		
		Long userId = (Long) request.getSession().getAttribute("userId");
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		model.addAttribute("studyTreatmentDataDatesId", studyTreatmentDataDatesId);
		model.addAttribute("stdSubGroupObservationCrfsId", stdSubGroupObservationCrfsId);
		model.addAttribute("type", type);
		model.addAttribute("seletedGender", gender);
		return reviewObservationForm(model, userId, studyId, studyTreatmentDataDatesId, stdSubGroupObservationCrfsId, type, gender, request,
				redirectAttributes, schduleType);
	}
	@RequestMapping(value = "/reviewObservationForm", method = RequestMethod.POST)
	public String reviewObservationFormPost(ModelMap model,
			@RequestParam("schduleType") String schduleType,
			@RequestParam("stdSubGroupObservationCrfsId") Long stdSubGroupObservationCrfsId,
			@RequestParam("type") String type, @RequestParam("seletedGender") String gender, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		Long studyTreatmentDataDatesId = null;
		if(schduleType.equals("scheduled")) 
			studyTreatmentDataDatesId = Long.parseLong(request.getParameter("studyTreatmentDataDatesId"));
	
		
		Long userId = (Long) request.getSession().getAttribute("userId");
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		model.addAttribute("schduleType", schduleType);
		model.addAttribute("studyTreatmentDataDatesId", studyTreatmentDataDatesId);
		model.addAttribute("stdSubGroupObservationCrfsId", stdSubGroupObservationCrfsId);
		model.addAttribute("type", type);
		model.addAttribute("seletedGender", gender);
		return reviewObservationForm(model, userId, studyId, studyTreatmentDataDatesId, stdSubGroupObservationCrfsId, type, gender, request,
				redirectAttributes, schduleType);
		
	}

	/*
	 * common logic for get and post call of observation data entry view. To display
	 * gender specific of the selected day subgroup observation data
	 */
	private String reviewObservationForm(ModelMap model, Long userId, Long studyId, Long studyTreatmentDataDatesId, Long stdSubGroupObservationCrfsId,
			String type, String gender, HttpServletRequest request, RedirectAttributes redirectAttributes, String schduleType) {
		ObservationAnimalDataviewDto aadDto = reviewService.observatoinAnimalDataDtoDetails(studyId,
				studyTreatmentDataDatesId, stdSubGroupObservationCrfsId, userId, false, gender, schduleType);
		model.addAttribute("aadDto", aadDto);
		model.addAttribute("crfId", stdSubGroupObservationCrfsId);

		if (type.equals("review"))
			return "observationAnimalDataEntryReview";
		else
			return "observationAnimalDataEntryView";
	}

	@RequestMapping(value = "/saveApprovedData", method = RequestMethod.POST)
	public String saveApprovedData(ModelMap model, HttpServletRequest request, @RequestParam("stuydId") Long studyId,
			@RequestParam("chkckedIds") List<Long> checkedIds, @RequestParam("crfId") Long crfId,
			@RequestParam("reviewType") String reviewType, @RequestParam("reviewComment") String reviewComment,
			@RequestParam("studyAcclamatizationDatesId") Long studyAcclamatizationDatesId,
			@RequestParam("gender") String gender,@RequestParam("dateString") String dateString,
			RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		String result = reviewService.reviewSelectedDataEntryFroms(checkedIds, userId, reviewType, reviewComment);
		if (!reviewType.equals("Send To Review")) {
			if (result.equals("success"))
				redirectAttributes.addFlashAttribute("pageMessage", "Data Approving Process Done. Successfully....!");
			else
				redirectAttributes.addFlashAttribute("pageError", "Data Approving Process Failed. Please Try Again.");
//			return "redirect:/studyReview/reviewAccessionForm/" + crfId + "/" + studyId;
			return "redirect:/studyReview/reviewAccessionAnimalsDataCalender";
		} else {
			if (result.equals("success"))
				redirectAttributes.addFlashAttribute("pageMessage", "Data Sent to review. Successfully....!");
			else
				redirectAttributes.addFlashAttribute("pageError", "Failed to sent data to reivew. Please Try Again.");
//			return "redirect:/accession/viewFormData/" + crfId + "/" + studyId;
			return "redirect:/studyReview/reviewAccessionForm/" + crfId + "/" + studyId+"/"+studyAcclamatizationDatesId+"/"+gender+"/"+dateString;

		}
	}

	@RequestMapping(value = "/saveApprovedObservationData", method = RequestMethod.POST)
	public String saveApprovedObservationData(ModelMap model, HttpServletRequest request,
			@RequestParam("schduleType") String schduleType, 
			@RequestParam("studyTreatmentDataDatesId") Long studyTreatmentDataDatesId, 
			@RequestParam("stdSubGroupObservationCrfsId") Long stdSubGroupObservationCrfsId,
			@RequestParam("type") String type, @RequestParam("seletedGender") String gender,
			@RequestParam("stuydId") Long studyId, @RequestParam("chkckedIds") List<Long> checkedIds,
			@RequestParam("crfId") Long crfId, @RequestParam("reviewType") String reviewType,
			@RequestParam("reviewComment") String reviewComment, RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		String result = reviewService.reviewSelectedObservationDataEntryFroms(checkedIds, userId, reviewType,
				reviewComment);
		if (!reviewType.equals("Send To Review")) {
			if (result.equals("success"))
				redirectAttributes.addFlashAttribute("pageMessage", "Data Approving Process Done. Successfully....!");
			else
				redirectAttributes.addFlashAttribute("pageError", "Data Approving Process Failed. Please Try Again.");
			if(studyTreatmentDataDatesId == null) studyTreatmentDataDatesId = 0l;
			if(gender == null || gender.equals("")) gender = "gender";
			System.out.println("redirect:/studyReview/reviewObservationForm/"+schduleType+"/"+studyTreatmentDataDatesId+"/"+stdSubGroupObservationCrfsId+"/" + type + "/"+gender);
			return "redirect:/studyReview/reviewObservationForm/"+schduleType+"/"+studyTreatmentDataDatesId+"/"+stdSubGroupObservationCrfsId+"/" + type + "/"+gender;
		} else {
			if (result.equals("success"))
				redirectAttributes.addFlashAttribute("pageMessage", "Data Sent to review. Successfully....!");
			else
				redirectAttributes.addFlashAttribute("pageError", "Failed to sent data to reivew. Please Try Again.");
			return "redirect:/studyReview/reviewObservationForm/"+studyTreatmentDataDatesId+"/"+stdSubGroupObservationCrfsId+"/" + type + "/"+gender;
		}
	}

	@RequestMapping(value = "/signcheck/{newpwd}", method = RequestMethod.GET)
	public @ResponseBody String signcheck(@PathVariable("newpwd") String newpwd, HttpServletRequest request,
			ModelMap model) {
		if (BCrypt.checkpw(newpwd,
				userService.findByUsername(request.getSession().getAttribute("userName").toString()).getTranPassword()))
			return "yes";
		else
			return "no";
	}

	@RequestMapping(value = "/randamizationData", method = RequestMethod.GET)
	public String randamizationData(HttpServletRequest request, ModelMap model) {
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster study = studyService.findByStudyId(studyId);
		model.addAttribute("study", study);
		boolean fg = true;
		if (!study.getStatus().getStatusCode().equals(StatusMasterCodes.IN.toString())) {
			if (study.getRadamizationStatus() == null
					|| study.getRadamizationStatus().getStatusCode().equals(StatusMasterCodes.REJECTED.toString())) {
				String generatedFor = "";
				if (study.getGender().equals("Both") && study.isSplitStudyByGender()) {
					if (study.isMaleRandamizationReviewStatus() && study.isFemaleRandamizationReviewStatus()) {
						fg = false;
					}
					String msg = "";
					if (study.isMaleRandamizationStatus()) {
						generatedFor = "Male";
						model.addAttribute("tempRandamization", generatedFor);
						RandamizationDto dto = animalRadService.generatedRandamization(study, "Male");
						request.getSession().setAttribute("tempmaledto", dto);
						msg = "Male Animals :" + dto.getApprovalStatus().getStatusDesc();
					}
					if (study.isFemaleRandamizationStatus()) {
						generatedFor = "Female";
						model.addAttribute("tempRandamization", generatedFor);
						RandamizationDto dto = animalRadService.generatedRandamization(study, "Female");
						request.getSession().setAttribute("tempfemaledto", dto);
						if (msg.equals(""))
							msg = "Female Animals :" + dto.getApprovalStatus().getStatusDesc();
						else
							msg = msg + "<br/>" + "Female Animals :" + dto.getApprovalStatus().getStatusDesc();
					}
					if (!msg.equals("")) {
						model.addAttribute("pageMessage", msg);
					}
				}

				if (fg)
					model.addAttribute("allowtoReview", false);
				else
					model.addAttribute("allowtoReview", true);
				return "animalRandomizationViewReviewPage";
			} else {

				if (study.getGender().equals("Both")) {
					String msg = "";
					RandamizationDto dto = animalRadService.generatedRandamization(study, "Male");
					if(dto != null) {
						request.getSession().setAttribute("tempmaledto", dto);
						msg = "Male Animals :" + dto.getApprovalStatus().getStatusDesc();
						model.addAttribute("tempRandamization", "Male");
					}
					RandamizationDto dto1 = animalRadService.generatedRandamization(study, "Female");
					if(dto1 != null) {
						request.getSession().setAttribute("tempfemaledto", dto1);
						msg = msg + "<br/>" + "Female Animals :" + dto1.getApprovalStatus().getStatusDesc();
						model.addAttribute("tempRandamization", "Female");
					}
					if(dto != null && dto1 != null)
						model.addAttribute("tempRandamization", "Both");
					model.addAttribute("pageMessage", msg);

				} else {
					model.addAttribute("tempRandamization", study.getGender());
					RandamizationDto dto = animalRadService.generatedRandamization(study, study.getGender());
					if (study.getGender().equals("Male"))
						request.getSession().setAttribute("tempmaledto", dto);
					else
						request.getSession().setAttribute("tempfemaledto", dto);
					model.addAttribute("pageMessage",
							study.getGender() + " Animals :" + dto.getApprovalStatus().getStatusDesc());
				}
				if(study.getRadamizationStatus().getStatusCode().equals(StatusMasterCodes.SENDTORIVEW.toString())) {
					model.addAttribute("allowtoReview", true);
				}else
					model.addAttribute("allowtoReview", false);
				return "animalRandomizationViewReviewPage";
			}

		} else {
			model.addAttribute("pageMessage", "Study Meta data not avilable.");
			return "animalRandomizationViewReviewPage";
		}
	}

	@RequestMapping(value = "/saveRandamizationData", method = RequestMethod.POST)
	public String saveRandamizationData(ModelMap model, HttpServletRequest request, 
			@RequestParam("reviewType") String reviewType,
			@RequestParam("reviewComment") String reviewComment, RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		boolean result = reviewService.saveRandamizationReview(studyId, userId, reviewType, reviewComment);
		if (result)
			redirectAttributes.addFlashAttribute("pageMessage",
					"Randamization Approving Process Done. Successfully....!");
		else
			redirectAttributes.addFlashAttribute("pageError",
					"Randamization Approving Process Failed. Please Try Again.");
		return "redirect:/studyReview/randamizationData";
	}

	@RequestMapping(value = "/randamizationDataOld", method = RequestMethod.GET)
	public String randamizationDataOld(HttpServletRequest request, ModelMap model) {
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster study = studyService.findByStudyId(studyId);
		Long userId = (Long) request.getSession().getAttribute("userId");
		if (study.getRadamizationStatus() != null) {
			String gender = study.getRandamizationDoneNow();
			RadomizationReviewDto rdmDto = animalRadService.getRadomizationReviewDtoDetails(studyId, userId, gender); // gender
																														// need
																														// to
																														// review
			model.addAttribute("rdmDto", rdmDto);
			if (gender.equals("Female") && study.isMaleRandamizationReviewStatus()) {
				RadomizationReviewDto rdmDtoOld = animalRadService.getRadomizationReviewDtoDetails(studyId, userId,
						"Male");// Male need to display
				model.addAttribute("rdmDtoOld", rdmDtoOld);
			} else if (gender.equals("Both") && study.isFemaleRandamizationReviewStatus()) {
				RadomizationReviewDto rdmDtoOld = animalRadService.getRadomizationReviewDtoDetails(studyId, userId,
						"Female");// Female need to display
				model.addAttribute("rdmDtoOld", rdmDtoOld);
			}

			if (study.getRadamizationStatus().getStatusCode().equals(StatusMasterCodes.REVIEWED.toString())
					|| study.getRadamizationStatus().getStatusCode().equals(StatusMasterCodes.APPROVED.toString()))
				model.addAttribute("pageMessage", "Randomization has Reviewd .");
			else
				model.addAttribute("pageMessage", "Randomization is In Review .");
			model.addAttribute("rdmDto", rdmDto);
			return "animalRandomizationReview";
		} else {
			model.addAttribute("pageMessage", "Randomization is not Done .");

		}
		return "animalRandomizationReview";
	}

	@RequestMapping(value = "/saveRandamizationDataOld", method = RequestMethod.POST)
	public String saveRandamizationDataOld(ModelMap model, HttpServletRequest request,
			@RequestParam("redamizationId") Long redamizationId, @RequestParam("reviewType") String reviewType,
			@RequestParam("reviewComment") String reviewComment, RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		String result = reviewService.saveRandamizationData(redamizationId, userId, reviewType, reviewComment);
		if (result.equals("success"))
			redirectAttributes.addFlashAttribute("pageMessage",
					"Randamization Approving Process Done. Successfully....!");
		else
			redirectAttributes.addFlashAttribute("pageError",
					"Randamization Approving Process Failed. Please Try Again.");
		return "redirect:/studyReview/randamizationData";
	}

}
