
package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.CrfDescrpency;
import com.covide.crf.dto.CrfSectionElementData;
import com.covide.crf.service.CrfService;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAccessionCrfDescrpency;
import com.springmvc.model.StudyAccessionCrfSectionElementData;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTreatmentDataDates;
import com.springmvc.service.ExpermentalDesignService;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;
import com.springmvc.util.ObservationData;

@Controller
@RequestMapping("/studyReview")
@PropertySource(value = { "classpath:application.properties" })
public class StudyReviewController {
	@Autowired
	private Environment environment;
	@Autowired
	StudyService studyService;

	@Autowired
	CrfService crfService;
	@Autowired
	UserService userService;
	@Autowired
	private ExpermentalDesignService expermentalDesignService;

	@RequestMapping(value = "/animalCrfDataReviewView", method = RequestMethod.POST)
	public String animalCrfDataReviewView(HttpServletRequest request, ModelMap model,
			RedirectAttributes redirectAttributes) {
		StudyMaster sm = studyService
				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		Long userId = (Long) request.getSession().getAttribute("userId");
		LoginUsers user = userService.findById(userId);
		RoleMaster role = user.getRole();
		if (role.getRole().equals("ADMIN") || role.getRole().equals("SUPERADMIN") || role.getRole().equals("QA")
				|| role.getRole().equals("Study Director")) {
			model.addAttribute("acess", "yes");
		} else {
			model.addAttribute("acess", "no");
		}
		Long groupId = Long.parseLong(request.getParameter("groupId").toString());
		Long subGroupId = Long.parseLong(request.getParameter("subGroupId").toString());
		Long subGroupInfoId = Long.parseLong(request.getParameter("subGroupInfoId").toString()); // subject
		Long stdSubGroupObservationCrfsId = Long
				.parseLong(request.getParameter("stdSubGroupObservationCrfsId").toString()); // observationId
		String review = request.getParameter("review");
		ObservationData data = expermentalDesignService.observationData(subGroupInfoId, stdSubGroupObservationCrfsId,
				user);
		model.addAttribute("pageMessage", data.getMessage());
		model.addAttribute("data", data);
		model.addAttribute("subGroupInfoId", subGroupInfoId);
		model.addAttribute("stdSubGroupObservationCrfsId", stdSubGroupObservationCrfsId);
		if (review.equals("review"))
			return "animalCrfDataReviewView";
		return "animalCrfDataReviewViewOnly";
	}

	@RequestMapping(value = "/animalCrfDataReviewViewRedirect/{subGroupInfoId}/{stdSubGroupObservationCrfsId}", method = RequestMethod.GET)
	public String animalCrfDataReviewViewRedirect(@PathVariable("subGroupInfoId") Long subGroupInfoId,
			@PathVariable("stdSubGroupObservationCrfsId") Long stdSubGroupObservationCrfsId, HttpServletRequest request,
			ModelMap model, RedirectAttributes redirectAttributes) {
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
		model.addAttribute("pageMessage", data.getMessage());
		model.addAttribute("data", data);
		model.addAttribute("subGroupInfoId", subGroupInfoId);
		model.addAttribute("stdSubGroupObservationCrfsId", stdSubGroupObservationCrfsId);
		return "animalCrfDataReviewView";
	}

	@RequestMapping(value = "/discDataNew/{id}", method = RequestMethod.GET)
	public String discDataNew(@PathVariable("id") Long studyAccessionCrfSectionElementDataId,
			HttpServletRequest request, ModelMap model) {

		String username = request.getSession().getAttribute("userName").toString();
		StudyAccessionCrfSectionElementData data = crfService
				.studyAccessionCrfSectionElementDataWithId(studyAccessionCrfSectionElementDataId);

		List<StudyAccessionCrfDescrpency> list = crfService
				.allStudyAccessionCrfDescrepencyOfElement(studyAccessionCrfSectionElementDataId);
		boolean flag = true;
		for (StudyAccessionCrfDescrpency sacd : list) {
			System.out.println(sacd.getStatus());
			if (!sacd.getStatus().equals("closed")) {
				flag = false;
				break;
			}
		}
		if (flag) {
			List<LoginUsers> allLoginUsers = userService.findAllActiveUsers();
			model.addAttribute("allLoginUsers", allLoginUsers);
			model.addAttribute("createDiscrepency", true);
		} else {
			model.addAttribute("createDiscrepency", false);
		}

		model.addAttribute("username", username);
		model.addAttribute("data", data);
		model.addAttribute("list", list);

		return "pages/reviewer/period/descrepecyCreationPage";
	}

	@RequestMapping(value = "/boservatoinDiscrepencyData/{crfSectionElementDataId}/{status}", method = RequestMethod.GET)
	public String boservatoinDiscrepencyData(@PathVariable("crfSectionElementDataId") Long crfSectionElementDataId, @PathVariable("status") boolean status, 
			HttpServletRequest request, ModelMap model) {
		String username = request.getSession().getAttribute("userName").toString();
		CrfSectionElementData data = crfService.crfSectionElementDataWithId(crfSectionElementDataId);

		List<CrfDescrpency> list = crfService.allStudyCrfDescrpencyOfElement(crfSectionElementDataId);
		boolean flag = true;
		for (CrfDescrpency sacd : list) {
			System.out.println(sacd.getStatus());
			if (!sacd.getStatus().equals("closed")) {
				flag = false;
				break;
			}
		}
		if (flag) {
			List<LoginUsers> allLoginUsers = userService.findAllActiveUsers();
			model.addAttribute("allLoginUsers", allLoginUsers);
			model.addAttribute("createDiscrepency", true);
		} else {
			model.addAttribute("createDiscrepency", false);
		}

		model.addAttribute("username", username);
		model.addAttribute("data", data);
		model.addAttribute("list", list);
		model.addAttribute("status", status);
		return "pages/reviewer/period/observationDescrepecyCreationPage";
	}
//	StudyAccessionCrfSectionElementData

	@RequestMapping(value = "/createAccessionDiscrepency", method = RequestMethod.POST)
	public String createDiscrepency(@ModelAttribute("studyAcclamatizationDatesId") Long studyAcclamatizationDatesId, @ModelAttribute("accessioncrfId") Long crfId,
			@ModelAttribute("accessionstuydId") Long studyId, @ModelAttribute("accessiondataId") Long accessiondataId,
			@ModelAttribute("accessionuserId") Long accessionuserId, @ModelAttribute("accessioncomment") String comment,
			@ModelAttribute("gender") String gender,@ModelAttribute("dateString") String dateString,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		boolean flag = crfService.saveAccessionDescrpency(accessiondataId, accessionuserId, comment, userId);

		if (flag)
			redirectAttributes.addFlashAttribute("pageMessage", "Discrepency saved successfully.");
		else {
			redirectAttributes.addFlashAttribute("pageError", "Faild to save Discrepency.");
		}
		return "redirect:/studyReview/reviewAccessionForm/" + crfId + "/" + studyId+"/"+studyAcclamatizationDatesId+"/"+gender+"/"+dateString;
	}

	@RequestMapping(value = "/createObservationDiscrepency", method = RequestMethod.POST)
	public String createObservationDiscrepency(@ModelAttribute("obserctioncrfId") Long crfId,
			@ModelAttribute("schduleType") String schduleType,
			@ModelAttribute("seletedGender") String gender,
			@ModelAttribute("obserctionstuydId") Long studyId,
			@ModelAttribute("obserctiondataId") Long obserctiondataId,
			@ModelAttribute("obserctionuserId") Long obserctionuserId,
			@ModelAttribute("obserctioncomment") String comment, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		CrfDescrpency scd = crfService.saveObservationDescrpency(obserctiondataId, obserctionuserId, comment, userId);

		if (scd.isSaveFlag())
			redirectAttributes.addFlashAttribute("pageMessage", "Discrepency saved successfully.");
		else {
			redirectAttributes.addFlashAttribute("pageError", "Faild to save Discrepency.");
		}
		if(gender == null || gender.equals("")) gender = "gender";
		if(schduleType.equals("scheduled")) {
			StudyTreatmentDataDates std = scd.getSecEleData().getSubjectDataEntryDetails().getStudyTreatmentDataDates();
			return "redirect:/studyReview/reviewObservationForm/"+schduleType+"/"+std.getId()+"/"+scd.getStdSubGroupObservationCrfs().getId()+"/review/"+gender;
		}else {
			return "redirect:/studyReview/reviewObservationForm/"+schduleType+"/0/"+scd.getStdSubGroupObservationCrfs().getId()+"/review/"+gender;
		}

	}

	@RequestMapping(value = "/createDiscrepency", method = RequestMethod.POST)
	public String createDiscrepency(@ModelAttribute("dataId") Long dataId, @ModelAttribute("username") String username,
			@ModelAttribute("userId") Long userId, @ModelAttribute("comment") String comment,
			@ModelAttribute("subGroupInfoId") Long subGroupInfoId,
			@ModelAttribute("stdSubGroupObservationCrfsId") Long stdSubGroupObservationCrfsId,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		boolean flag = crfService.saveCrfDescrpency(dataId, username, userId, comment);

		if (flag)
			redirectAttributes.addFlashAttribute("pageMessage", "Discrepency saved successfully.");
		else {
			redirectAttributes.addFlashAttribute("pageError", "Faild to save Discrepency.");
		}
		System.out.println(
				"/studyReview/animalCrfDataReviewViewRedirect/" + subGroupInfoId + "/" + stdSubGroupObservationCrfsId);
		return "redirect:/studyReview/animalCrfDataReviewViewRedirect/" + subGroupInfoId + "/"
				+ stdSubGroupObservationCrfsId;
	}

	@RequestMapping(value = "/approveObservation", method = RequestMethod.POST)
	public String approveObservation(@ModelAttribute("subGroupInfoId") Long subGroupInfoId,
			@ModelAttribute("stdSubGroupObservationCrfsId") Long stdSubGroupObservationCrfsId,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Integer descElementAll = null;
		if (request.getParameter("descElementAll") != null)
			descElementAll = Integer.parseInt(request.getParameter("descElementAll").toString());
		String[] descElement = request.getParameterValues("descElement");
		List<Long> dataIds = new ArrayList<>();
		if (descElement != null) {
			for (String s : descElement) {
				dataIds.add(Long.parseLong(s));
			}
		}
		Long userId = (Long) request.getSession().getAttribute("userId");
		LoginUsers user = userService.findById(userId);

		StdSubGroupObservationCrfs stdSubGroupObservationCrfs = crfService.saveRevewObsevation(user, subGroupInfoId,
				stdSubGroupObservationCrfsId, descElementAll, dataIds);
		if (stdSubGroupObservationCrfs != null)
			redirectAttributes.addFlashAttribute("pageMessage", "Observation Approved successfully.");
		else {
			redirectAttributes.addFlashAttribute("pageError", "Approval already done.");
		}
		System.out.println(
				"/studyReview/animalCrfDataReviewViewRedirect/" + subGroupInfoId + "/" + stdSubGroupObservationCrfsId);
		
//		return "redirect:/studyReview/animalCrfDataReviewViewRedirect/" + subGroupInfoId + "/"
//				+ stdSubGroupObservationCrfsId;
		return "redirect:/studyReview/reviewAccessionAnimalsDataCalender";
	}

	@RequestMapping(value = "/allAnimalCrfDataReviewView", method = RequestMethod.POST)
	public String allAnimalCrfDataReviewView(HttpServletRequest request, ModelMap model,
			RedirectAttributes redirectAttributes) {
		StudyMaster sm = studyService
				.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		Long userId = (Long) request.getSession().getAttribute("userId");
		LoginUsers user = userService.findById(userId);
		RoleMaster role = user.getRole();
		if (role.getRole().equals("ADMIN") || role.getRole().equals("SUPERADMIN") || role.getRole().equals("QA")
				|| role.getRole().equals("Study Director")) {
			model.addAttribute("acess", "yes");
		} else {
			model.addAttribute("acess", "no");
		}
		Long groupId = Long.parseLong(request.getParameter("groupId").toString());
		Long subGroupId = Long.parseLong(request.getParameter("subGroupId").toString());

		Long subGroupInfoId = Long.parseLong(request.getParameter("subGroupInfoId").toString()); // subject
		Long stdSubGroupObservationCrfsId = Long
				.parseLong(request.getParameter("stdSubGroupObservationCrfsId").toString()); // observationId
		String review = request.getParameter("review");
		ObservationData data = expermentalDesignService.observationDataAllAnimal(subGroupInfoId,
				stdSubGroupObservationCrfsId, user, groupId, subGroupId);
		model.addAttribute("pageMessage", data.getMessage());
		model.addAttribute("data", data);
		model.addAttribute("subGroupInfoId", subGroupInfoId);
		model.addAttribute("stdSubGroupObservationCrfsId", stdSubGroupObservationCrfsId);
		model.addAttribute("sm", sm);
		if (review.equals("review"))
			return "animalCrfDataReviewViewAllAnimal";
		return "animalCrfDataReviewViewOnlyAllAnimal";
	}

	@RequestMapping(value = "/createDiscrepencyNew", method = RequestMethod.POST)
	public String createDiscrepencyNew(@ModelAttribute("dataId") Long dataId,
			@ModelAttribute("username") String username, @ModelAttribute("userId") Long userId,
			@ModelAttribute("comment") String comment, @ModelAttribute("subGroupInfoId") Long subGroupInfoId,
			@ModelAttribute("stdSubGroupObservationCrfsId") Long stdSubGroupObservationCrfsId,
			@ModelAttribute("groupid") Long groupid, @ModelAttribute("subGroupId") Long subGroupId,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		boolean flag = crfService.saveCrfDescrpency(dataId, username, userId, comment);

		if (flag)
			redirectAttributes.addFlashAttribute("pageMessage", "Discrepency saved successfully.");
		else {
			redirectAttributes.addFlashAttribute("pageError", "Faild to save Discrepency.");
		}
		System.out.println("/studyReview/animalCrfDataReviewViewRedirectNew/" + subGroupInfoId + "/"
				+ stdSubGroupObservationCrfsId + "/" + groupid + "/" + subGroupId);
		return "redirect:/studyReview/animalCrfDataReviewViewRedirectNew/" + subGroupInfoId + "/"
				+ stdSubGroupObservationCrfsId + "/" + groupid + "/" + subGroupId;
	}

	@RequestMapping(value = "/approveObservationNew", method = RequestMethod.POST)
	public String approveObservationNew(@ModelAttribute("subGroupInfoId") Long subGroupInfoId,
			@ModelAttribute("stdSubGroupObservationCrfsId") Long stdSubGroupObservationCrfsId,
			@ModelAttribute("groupid") Long groupid, @ModelAttribute("subGroupId") Long subGroupId,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Integer descElementAll = null;
		if (request.getParameter("descElementAll") != null)
			descElementAll = Integer.parseInt(request.getParameter("descElementAll").toString());
		String[] descElement = request.getParameterValues("descElement");
		List<Long> dataIds = new ArrayList<>();
		if (descElement != null) {
			for (String s : descElement) {
				dataIds.add(Long.parseLong(s));
			}
		}
		Long userId = (Long) request.getSession().getAttribute("userId");
		LoginUsers user = userService.findById(userId);

		StdSubGroupObservationCrfs stdSubGroupObservationCrfs = crfService.saveRevewObsevation(user, subGroupInfoId,
				stdSubGroupObservationCrfsId, descElementAll, dataIds);
		if (stdSubGroupObservationCrfs != null)
			redirectAttributes.addFlashAttribute("pageMessage", "Observation Approved successfully.");
		else {
			redirectAttributes.addFlashAttribute("pageError", "Approval already done.");
		}
		System.out.println("/studyReview/animalCrfDataReviewViewRedirectNew/" + subGroupInfoId + "/"
				+ stdSubGroupObservationCrfsId + "/" + groupid + "/" + subGroupId);
		return "redirect:/studyReview/animalCrfDataReviewViewRedirectNew/" + subGroupInfoId + "/"
				+ stdSubGroupObservationCrfsId + "/" + groupid + "/" + subGroupId;
	}

	@RequestMapping(value = "/animalCrfDataReviewViewRedirectNew/{subGroupInfoId}/{stdSubGroupObservationCrfsId}/{groupid}/{subGroupId}", method = RequestMethod.GET)
	public String animalCrfDataReviewViewRedirectNew(@PathVariable("subGroupInfoId") Long subGroupInfoId,
			@PathVariable("stdSubGroupObservationCrfsId") Long stdSubGroupObservationCrfsId,
			@PathVariable("groupid") Long groupid, @PathVariable("subGroupId") Long subGroupId,
			HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		LoginUsers user = userService.findById(userId);
		RoleMaster role = user.getRole();
		if (role.getRole().equals("ADMIN") || role.getRole().equals("SUPERADMIN") || role.getRole().equals("QA")
				|| role.getRole().equals("Study Director")) {
			model.addAttribute("acess", "yes");
		} else {
			model.addAttribute("acess", "no");
		}
		ObservationData data = expermentalDesignService.observationDataAllAnimal(subGroupInfoId,
				stdSubGroupObservationCrfsId, user, groupid, subGroupId);
		model.addAttribute("pageMessage", data.getMessage());
		model.addAttribute("data", data);
		model.addAttribute("subGroupInfoId", subGroupInfoId);
		model.addAttribute("stdSubGroupObservationCrfsId", stdSubGroupObservationCrfsId);
		return "animalCrfDataReviewViewAllAnimal";
	}

}
