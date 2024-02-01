package com.springmvc.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.Crf;
import com.covide.crf.service.CrfService;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAcclamatizationDates;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTreatmentDataDates;
import com.springmvc.service.AcclimatizationService;
import com.springmvc.service.ExpermentalDesignService;
import com.springmvc.service.StudyService;

@Controller
@RequestMapping("/acclimatization")
@PropertySource(value = { "classpath:application.properties" })
public class AcclimatizationController {
	@Autowired
	private Environment environment;
	@Autowired
	private StudyService studyService;
	@Autowired
	CrfService srfService;
	@Autowired
	AcclimatizationService accService;
	@Autowired
	private ExpermentalDesignService expermentalDesignService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String acclimatization(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
//		SimpleDateFormat sdf  = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
//		StudyMaster study = studyService.findByStudyId(studyId);
//		model.addAttribute("acclimatizationStart", sdf.format(study.getAcclimatizationStarDate()));
//		model.addAttribute("acclimatizationEnd", sdf.format(study.getAcclimatizationEndDate()));
//		long timeDiff = study.getAcclimatizationEndDate().getTime() - study.getAcclimatizationStarDate().getTime();
//        long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
//        int days =  (int) daysDiff/30;
//        if(daysDiff%30 > 0)
//        	days++;
//        if(days > 1)
//        	model.addAttribute("noOfMounths", days);
//        else
//        	model.addAttribute("noOfMounths", 2);
		List<Crf> crfList = srfService.findAllActiveCrfsConfigurationForAcclimatization();
		Map<Long, StudyAcclamatizationData> sadMap = accService.getStudyAcclamatizationDataList(studyId);
		model.addAttribute("sadMap", sadMap);
		model.addAttribute("studyId", studyId);
		model.addAttribute("crfList", crfList);
		return "acclimatizationPage";
	}

	@RequestMapping(value = "/saveAcclimatizationData", method = RequestMethod.POST)
	public String saveAcclimatizationData(ModelMap model, @RequestParam("studyId") Long studyId,
			@RequestParam("crfIds") List<Long> crfIds,
//			@RequestParam("typeVals")List<String>typeList, @RequestParam("daysOrWeeksVals")String daysDataList, @RequestParam("windowPeriod")String windowPeriod,
//			@RequestParam("ucheckedIds")List<Long>unchkIds, 
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String userName = request.getSession().getAttribute("userName").toString();
		Map<Long, String> typeVals = new HashMap<>();
		Map<Long, String> daysOrWeeksVals = new HashMap<>();
		Map<Long, String> windowPeriod = new HashMap<>();
		Map<Long, Integer> windowMap = new HashMap<>();
		for (Long crfId : crfIds) {
			typeVals.put(crfId, request.getParameter("dayOrWeekRadio_" + crfId));
			if (!request.getParameter("dayOrWeek_" + crfId).trim().equals(""))
				daysOrWeeksVals.put(crfId, request.getParameter("dayOrWeek_" + crfId));
			else
				daysOrWeeksVals.put(crfId, "0");
			windowPeriod.put(crfId, request.getParameter("sign_" + crfId));
			if (!request.getParameter("window_" + crfId).trim().equals(""))
				windowMap.put(crfId, Integer.parseInt(request.getParameter("window_" + crfId)));
			else
				windowMap.put(crfId, 0);
		}
		String result = accService.saveAcclimatizationData(studyId, crfIds, crfIds, typeVals, daysOrWeeksVals,
				windowPeriod, windowMap, userName);
		if (result.equals("success"))
			redirectAttributes.addFlashAttribute("pageMessage",
					"Acclimatization Configuration Done. Successfullly...!");
		else
			redirectAttributes.addFlashAttribute("pageError",
					"Acclimatization Configuration Failed. Please Try Again.");
		return "redirect:/acclimatization/";
	}

	@RequestMapping(value = "/changeAcclimatizationConfig/{studyId}/{crfId}/{status}/{activity}/{subGroupId}", method = RequestMethod.GET)
	public String changeAcclimatizationConfig(@PathVariable("studyId") Long studyId, @PathVariable("crfId") Long crfId,
			@PathVariable("status") String status, @PathVariable("activity") String activity,
			@PathVariable("subGroupId") Long subGroupId, HttpServletRequest request, ModelMap model) {
		String userName = request.getSession().getAttribute("userName").toString();
		studyId = (Long) request.getSession().getAttribute("studyDesignId");
		StudyMaster study = studyService.findByStudyId(studyId);
		Crf crf = srfService.crfForView(crfId);
		if (activity.equals("Acclimatization")) {
			if (accService.changeAcclimatizationConfig(crf, study, status, userName))
				return "success";
			else
				return null;
		} else if (activity.equals("Treatment")) {
			if (expermentalDesignService.changeTreatmentConfig(crf, study, status, userName, subGroupId))
				return "success";
			else
				return null;
		}
		return null;
	}

	@RequestMapping(value = "/changeAcclimatizationConfigValue/{studyId}/{crfId}/{value}/{activity}/{field}/{subGroupId}", method = RequestMethod.GET)
	public String changeAcclimatizationConfigValue(@PathVariable("studyId") Long studyId,
			@PathVariable("crfId") Long crfId, @PathVariable("value") String value,
			@PathVariable("activity") String activity, @PathVariable("field") String field,
			@PathVariable("subGroupId") Long subGroupId, HttpServletRequest request, ModelMap model) {
		String userName = request.getSession().getAttribute("userName").toString();
		studyId = (Long) request.getSession().getAttribute("studyDesignId");
		if (activity.equals("Acclimatization")) {
			if (accService.changeAcclimatizationConfigType(crfId, studyId, value, userName, field))
				return "success";
			else
				return null;
		} else if (activity.equals("Treatment")) {
			if (expermentalDesignService.changeTreatmentConfigType(crfId, studyId, value, userName, field, subGroupId))
				return "success";
			else
				return null;
		}
		return null;
	}

	@RequestMapping(value = "/observationConfig/{studyId}/{crfId}/{crfFrom}/{activity}/{subGroupId}", method = RequestMethod.GET)
	public String createDiscrepencyGroup(@PathVariable("studyId") Long studyId, @PathVariable("crfId") Long crfId,
			@PathVariable("crfFrom") String crfFrom, @PathVariable("activity") String activity,
			@PathVariable("subGroupId") Long subGroupId, HttpServletRequest request, ModelMap model) {
		observationConfig(studyId, crfId, crfFrom, activity, subGroupId, request, model);
		return "pages/acclimatization/observationDatesConfig";

	}
	
	@RequestMapping(value = "/observationConfigView/{studyId}/{crfId}/{crfFrom}/{activity}/{subGroupId}", method = RequestMethod.GET)
	public String observationConfigView(@PathVariable("studyId") Long studyId, @PathVariable("crfId") Long crfId,
			@PathVariable("crfFrom") String crfFrom, @PathVariable("activity") String activity,
			@PathVariable("subGroupId") Long subGroupId, HttpServletRequest request, ModelMap model) {
		observationConfig(studyId, crfId, crfFrom, activity, subGroupId, request, model);
		return "pages/reviewer/observationDatesConfigView";

	}

	private void observationConfig(Long studyId, Long crfId, String crfFrom, String activity, Long subGroupId,
			HttpServletRequest request, ModelMap model) {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		StudyMaster study = studyService.findByStudyId(studyId);
		Crf crf = srfService.crfForView(crfId);
		model.addAttribute("crf", crf);
		model.addAttribute("study", study);
		model.addAttribute("obserVationFor", activity);
		model.addAttribute("subGroupId", subGroupId);
		if (activity.equals("Acclimatization")) {
			model.addAttribute("acclimatizationStart", sdf.format(study.getAcclimatizationStarDate()));
			model.addAttribute("acclimatizationEnd", sdf.format(study.getAcclimatizationEndDate()));

			long timeDiff = study.getAcclimatizationEndDate().getTime() - study.getAcclimatizationStarDate().getTime();
			long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
			int days = (int) daysDiff / 30;
			if (daysDiff % 30 > 0)
				days++;
			if (days > 1)
				model.addAttribute("noOfMounths", days);
			else
				model.addAttribute("noOfMounths", 2);
//		if (crfFrom.equals("study")) {
//			model.addAttribute("acclimatizationDates", accService.studyAcclamatizationData(crfId, studyId));
//		}
			model.addAttribute("observationDates", accService.studyAcclamatizationDates(crfId, studyId));

			if (study.getGender().equals("Both") && !study.isSplitStudyByGender()) {
				model.addAttribute("femaleAcclimatizationStart", sdf.format(study.getAcclimatizationStarDateFemale()));
				model.addAttribute("femaleAcclimatizationEnd", sdf.format(study.getAcclimatizationEndDateFemale()));

				timeDiff = study.getAcclimatizationEndDateFemale().getTime()
						- study.getAcclimatizationStarDateFemale().getTime();
				daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
				days = (int) daysDiff / 30;
				if (daysDiff % 30 > 0)
					days++;
				if (days > 1)
					model.addAttribute("femalNoOfMounths", days);
				else
					model.addAttribute("femalNoOfMounths", 2);
			}
//		return "pages/acclimatization/observationDatesConfig";
		} else if (activity.equals("Treatment")) {
			model.addAttribute("acclimatizationStart", sdf.format(study.getTreatmentStarDate()));
			model.addAttribute("acclimatizationEnd", sdf.format(study.getTreatmentEndDate()));
			long timeDiff = study.getTreatmentEndDate().getTime() - study.getTreatmentStarDate().getTime();
			long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
			int days = (int) daysDiff / 30;
			if (daysDiff % 30 > 0)
				days++;
			if (days > 1)
				model.addAttribute("noOfMounths", days);
			else
				model.addAttribute("noOfMounths", 2);
//		if (crfFrom.equals("study")) {
//			model.addAttribute("acclimatizationDates", accService.studyTreatmentData(crfId, studyId, subGroupId));
//		}
			model.addAttribute("observationDates", accService.studyTreatmentnDates(crfId, studyId, subGroupId));

			if (study.getGender().equals("Both") && !study.isSplitStudyByGender()) {
				model.addAttribute("femaleAcclimatizationStart", sdf.format(study.getTreatmentStarDateFemale()));
				model.addAttribute("femaleAcclimatizationEnd", sdf.format(study.getTreatmentEndDateFemale()));

				timeDiff = study.getTreatmentEndDateFemale().getTime() - study.getTreatmentStarDateFemale().getTime();
				daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
				days = (int) daysDiff / 30;
				if (daysDiff % 30 > 0)
					days++;
				if (days > 1)
					model.addAttribute("femalNoOfMounths", days);
				else
					model.addAttribute("femalNoOfMounths", 2);
			}
//		return "pages/acclimatization/treatmentObservationDatesConfig";
		}
	}

//	.observationDateDetails(java.lang.Long,
//			java.lang.String,
//			java.lang.String,
//			java.lang.Long,
//			java.lang.Boolean,
//			java.lang.Long,
//			javax.servlet.http.HttpServletRequest,org.springframework.ui.ModelMap) throws java.text.ParseException]

	@RequestMapping(value = "/observationDateDetails/{studyId}/{crfId}/{activity}/{selectedDate}/{subGroupId}/{genderBase}/{gender}", method = RequestMethod.GET)
	public @ResponseBody String observationDateDetails(@PathVariable("studyId") Long studyId,
			@PathVariable("crfId") Long crfId, @PathVariable("activity") String activity,
			@PathVariable("selectedDate") String selectedDate, @PathVariable("subGroupId") Long subGroupId,
			@PathVariable("genderBase") Boolean genderBase, @PathVariable("gender") String gender,
			HttpServletRequest request, ModelMap model) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		Long userId = (Long) request.getSession().getAttribute("userId");
		studyId = (Long) request.getSession().getAttribute("studyDesignId");
		StudyMaster study = studyService.findByStudyId(studyId);
		Crf crf = srfService.crfForView(crfId);
		if (activity.equals("Acclimatization")) {
			StudyAcclamatizationDates sad = accService.studyAcclamatizationDates(crfId, studyId,
					sdf.parse(selectedDate), userId, genderBase, gender);
			StringBuffer sb = new StringBuffer();
			sb.append("<tr id=\"" + sad.getId() + "\">");
			if (sad.isGenderBased()) {
				sb.append("<td><input type=\"hidden\" name=\"gender_" + sad.getId() + "\" id=\"gender_" + sad.getId()
						+ " value=\"" + sad.getGender() + "\" \"/>" + sad.getGender() + "</td>");
			}
			sb.append("<td>" + sad.getDayNo() + "</td>");
			sb.append("<td>" + sdf.format(sad.getAccDate()) + "</td>");
			if (crf.getType().equals("CP")) {
				sb.append("<td><input type=\"number\" id=\"vlaue_" + sad.getId()
						+ "\" class=\"form-control\" disabled=\"disabled\" /></td>");
			} else if (sad.getNoOfEntry() > 0)
				sb.append("<td><input type=\"number\" id=\"vlaue_" + sad.getId()
						+ "\" class=\"form-control\" onchange=\"saveStudyAcclamatizationDatesDetails('" + sad.getId()
						+ "', this.value, '" + activity + "')\" value=\"" + sad.getNoOfEntry() + "\"/></td>");
			else
				sb.append("<td><input type=\"number\" id=\"vlaue_" + sad.getId()
						+ "\" class=\"form-control\" onchange=\"saveStudyAcclamatizationDatesDetails('" + sad.getId()
						+ "', this.value, '" + activity + "')\" /></td>");
			if (sad.isGenderBased()) {
				sb.append(
						"<td><input type=\"button\" value=\"Remove\" class=\"btn btn-new\" onclick=\"removeStudyAcclamatizationDatesDetails('"
								+ sad.getId() + "', '" + sad.getDayNo() + "', '" + sad.getDateString() + "', '"
								+ sad.getGender() + "')\"/></td>");
			} else {
				sb.append(
						"<td><input type=\"button\" value=\"Remove\" class=\"btn btn-new\" onclick=\"removeStudyAcclamatizationDatesDetails('"
								+ sad.getId() + "', '" + sad.getDayNo() + "', '" + sad.getDateString()
								+ "', 'gender')\"/></td>");
			}
			if (crf.getType().equals("CP")) {
				sb.append("<td id=\"buttonTdId_" + sad.getId() + "\">");
				sb.append("<i class=\"fas fa-plus\" onclick=\"parameterConfig('" + sad.getId() + "', 'buttonTdId_"
						+ sad.getId() + "', '" + activity + "')\"></i></td>");
				sb.append("<tr id=\"" + sad.getId() + "_clinpath\" style=\"display: none\">");
				sb.append("<td colspan=\"6\" id=\"" + sad.getId() + "_clinpath_td\"></tr/>");
			}
			sb.append("</tr>");
			return sb.toString();
		} else if (activity.equals("Treatment")) {
			StudyTreatmentDataDates sad = accService.studyTreatmentDataDates(crfId, studyId, sdf.parse(selectedDate),
					userId, subGroupId, genderBase, gender);
			StringBuffer sb = new StringBuffer();
			sb.append("<tr id=\"" + sad.getDayNo() + "\">");
			if (sad.isGenderBased()) {
				sb.append("<td><input type=\"hidden\" name=\"gender_" + sad.getId() + "\" id=\"gender_" + sad.getId()
						+ " value=\"" + sad.getGender() + "\" \"/>" + sad.getGender() + "</td>");
			}
			sb.append("<td>" + sad.getDayNo() + "</td>");
			sb.append("<td>" + sdf.format(sad.getAccDate()) + "</td>");
			if (crf.getType().equals("CP")) {
				sb.append("<td><input type=\"number\" id=\"vlaue_" + sad.getId()
						+ "\" class=\"form-control\" disabled=\"disabled\" /></td>");
			} else if (sad.getNoOfEntry() > 0)
				sb.append("<td><input type=\"number\" id=\"vlaue_" + sad.getId()
						+ "\" class=\"form-control\" onchange=\"saveStudyAcclamatizationDatesDetails('" + sad.getId()
						+ "', this.value, '" + activity + "')\" value=\"" + sad.getNoOfEntry() + "\"/></td>");
			else
				sb.append("<td><input type=\"number\" id=\"vlaue_" + sad.getId()
						+ "\"  class=\"form-control\" onchange=\"saveStudyAcclamatizationDatesDetails('" + sad.getId()
						+ "', this.value, '" + activity + "')\" /></td>");
			if (sad.isGenderBased()) {
				sb.append(
						"<td><input type=\"button\" value=\"Remove\" class=\"btn btn-new\" onclick=\"removeStudyAcclamatizationDatesDetails('"
								+ sad.getId() + "', '" + sad.getDayNo() + "', '" + sad.getDateString() + "', '"
								+ sad.getGender() + "')\"/></td>");
			} else {
				sb.append(
						"<td><input type=\"button\" value=\"Remove\" class=\"btn btn-new\" onclick=\"removeStudyAcclamatizationDatesDetails('"
								+ sad.getId() + "', '" + sad.getDayNo() + "', '" + sad.getDateString()
								+ "', 'gender')\"/></td>");
			}
			if (crf.getType().equals("CP")) {
				sb.append("<td id=\"buttonTdId_" + sad.getId() + "\">");
				sb.append("<i class=\"fas fa-plus\" onclick=\"parameterConfig('" + sad.getId() + "', 'buttonTdId_"
						+ sad.getId() + "', '" + activity + "')\"></i></td>");

//				sb.append(
//						"<td><input type=\"button\" value=\"Configure\" class=\"btn btn-new\"  style=\"width:100px;padding:0px\" onclick=\"parameterConfig('"
//								+ sad.getId() + "', '" + sad.getDayNo() + "', '" + sad.getDateString()
//								+ "', '"+study.getGender()+"')\"/></td>");
				sb.append("<tr id=\"" + sad.getId() + "_clinpath\" style=\"display: none\">");
				sb.append("<td colspan=\"6\" id=\"" + sad.getId() + "_clinpath_td\"></tr/>");
			}
			return sb.toString();
		}
		model.addAttribute("crf", crf);
		model.addAttribute("study", study);
		return "";
	}

	@RequestMapping(value = "/removeStudyAcclamatizationDatesDetails/{observationDateId}/{activity}", method = RequestMethod.GET)
	public String removeStudyAcclamatizationDatesDetails(@PathVariable("observationDateId") Long observationDateId,
			@PathVariable("activity") String activity, HttpServletRequest request, ModelMap model) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		if (activity.equals("Acclimatization")) {
			if (accService.removeStudyAcclamatizationDatesDetails(observationDateId, userId))
				return "success";
			else
				return null;
		} else if (activity.equals("Treatment")) {
			if (expermentalDesignService.removeStudyTreatmentDataDatesDetails(observationDateId, userId))
				return "success";
			else
				return null;
		}
		return null;
	}

	@RequestMapping(value = "/updateStudyAcclamatizationDatesDetails/{observationDateId}/{value}/{activity}", method = RequestMethod.GET)
	public String updateStudyAcclamatizationDatesDetails(@PathVariable("observationDateId") Long observationDateId,
			@PathVariable("activity") String activity, @PathVariable("value") String value, HttpServletRequest request,
			ModelMap model) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		if (activity.equals("Acclimatization")) {
			if (accService.updateStudyAcclamatizationDatesDetails(observationDateId, value, userId))
				return "success";
			else
				return null;
		} else if (activity.equals("Treatment")) {
			if (expermentalDesignService.updateStudyTreatmentDataDatesDetails(observationDateId, value, userId))
				return "success";
			else
				return null;
		}
		return null;
	}
}
