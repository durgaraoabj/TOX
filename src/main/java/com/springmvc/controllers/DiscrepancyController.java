package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfDescrpency;
import com.covide.crf.dto.CrfDescrpencyAudit;
import com.covide.crf.dto.CrfDescrpencyLog;
import com.covide.crf.dto.CrfSectionElement;
import com.covide.crf.dto.CrfSectionElementData;
import com.covide.crf.dto.CrfSectionElementDataAudit;
import com.covide.crf.service.CrfService;
import com.springmvc.dao.StatusDao;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.StudyAccessionCrfDescrpency;
import com.springmvc.model.StudyAccessionCrfDescrpencyLog;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;

@Controller
@RequestMapping("/discrepancy")
public class DiscrepancyController {

	@Autowired
	StudyService studyService;

	@Autowired
	UserService userService;

	@Autowired
	CrfService crfService;

	@Autowired
	StatusDao statusDao;
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String discrepancy(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("staticData", statusDao.staticDataList());
			String username = request.getSession().getAttribute("userName").toString();
			model.addAllAttributes(redirectAttributes.getFlashAttributes());
			Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
			if (activeStudyId != null && !request.getSession().getAttribute("activeStudyNo").toString().equals("")) {
				StudyMaster sm = studyService.findByStudyId(activeStudyId);
				model.addAttribute("sm", studyService.findByStudyId(activeStudyId));

				List<StudyAccessionCrfDescrpency> accessionDescrpencys = crfService.allStudyAccessionCrfDescrpency(sm,
						username);
				model.addAttribute("accessionDescrpencys", accessionDescrpencys);

				List<CrfDescrpency> list = crfService.allOpendStudydiscrepency(sm, username);
				model.addAttribute("list", list);
				model.addAttribute("PageHedding", "Discrepancy");
				model.addAttribute("activeUrl", "discrepancy/");
				return "discrepancy.tiles";
			} else {
				redirectAttributes.addFlashAttribute("pageWarning",
						"Please Select Study form the List of Studys avilable");
				return "redirect:/dashboard/userWiseActiveStudies";
			}

		} catch (NullPointerException npe) {
			redirectAttributes.addFlashAttribute("pageWarning", "Please Select Study form the List of Studys avilable");
			return "redirect:/dashboard/userWiseActiveStudies";
		}

	}

	@RequestMapping(value = "/discrepancyUpdate/{id}", method = RequestMethod.GET)
	public String createDiscrepencyGroup(
//			@ModelAttribute("comment") String comment,
			@PathVariable("id") Long id, HttpServletRequest request, ModelMap model) {
		List<LoginUsers> allLoginUsers = userService.findAllActiveUsers();
		StudyAccessionCrfDescrpency scd = crfService.studyAccessionCrfDescrpency(id);

//		CrfDescrpency scd = crfService.studyCrfDescrpency(id);
		String eleType = "text";
		String htmlField = "";
		if (scd.getSecElement() != null) {
			eleType = scd.getSecElement().getType();
			if (scd.getSecElement().getType().equals("text") || scd.getSecElement().getType().equals("textArea")
					|| scd.getSecElement().getType().equals("date")
					|| scd.getSecElement().getType().equals("dateAndTime")
					|| scd.getSecElement().getType().equals("non") || scd.getSecElement().getType().equals("staticData")) {
				
			} else {
				String[] values = scd.getSecElement().getResponceType().split("##");
				if (scd.getSecElement().getType().equals("radio")
						|| scd.getSecElement().getType().equals("radioLabel")) {
					htmlField = getRadioButton(values);
				} else if (scd.getSecElement().getType().equals("checkBox")) {
					htmlField = getCheckBoxButton(values);
				} else if (scd.getSecElement().getType().equals("select")
						|| scd.getSecElement().getType().equals("selectTable")) {
					if (scd.getSecElement().getType().equals("selectTable")) {
						values = crfService.crfSectionElementValuesFromCrfMapplingTableColumnMap(scd.getSecElement());

					}

					htmlField = getSelectBoxButton(values);
				}
			}
		} else if (scd.getGroupElement() != null) {
			eleType = scd.getGroupElement().getType();
			if (scd.getGroupElement().getType().equals("text") || scd.getGroupElement().getType().equals("textArea")
					|| scd.getGroupElement().getType().equals("date")
					|| scd.getGroupElement().getType().equals("dateAndTime")
					|| scd.getGroupElement().getType().equals("non")) {
			} else {
				String[] values = scd.getGroupElement().getResponceType().split("##");
				if (scd.getGroupElement().getType().equals("radio")) {
					htmlField = getRadioButton(values);
				} else if (scd.getGroupElement().getType().equals("checkBox")) {
					htmlField = getCheckBoxButton(values);
				} else if (scd.getGroupElement().getType().equals("select")) {
					htmlField = getSelectBoxButton(values);
				}
			}
		}
		model.addAttribute("htmlField", htmlField);
		model.addAttribute("eleType", eleType);
		model.addAttribute("scd", scd);
		model.addAttribute("allLoginUsers", allLoginUsers);
		model.addAttribute("type", "accession");
		return "pages/discrepency/descrepecyUpdateSecPageView";
	}

	@RequestMapping(value = "/objservationDiscrepancyUpdate/{id}", method = RequestMethod.GET)
	public String objservationDiscrepancyUpdate(
//			@ModelAttribute("comment") String comment,
			@PathVariable("id") Long id, HttpServletRequest request, ModelMap model) {
		List<LoginUsers> allLoginUsers = userService.findAllActiveUsers();
		CrfDescrpency scd = crfService.studyCrfDescrpency(id);

//		CrfDescrpency scd = crfService.studyCrfDescrpency(id);
		String eleType = "text";
		String htmlField = "";
		if (scd.getSecElement() != null) {
			eleType = scd.getSecElement().getType();
			if (scd.getSecElement().getType().equals("text") || scd.getSecElement().getType().equals("textArea")
					|| scd.getSecElement().getType().equals("date")
					|| scd.getSecElement().getType().equals("dateAndTime")
					|| scd.getSecElement().getType().equals("non")) {

			} else {
				String[] values = scd.getSecElement().getResponceType().split("##");
				if (scd.getSecElement().getType().equals("radio")
						|| scd.getSecElement().getType().equals("radioLabel")) {
					htmlField = getRadioButton(values);
				} else if (scd.getSecElement().getType().equals("checkBox")) {
					htmlField = getCheckBoxButton(values);
				} else if (scd.getSecElement().getType().equals("select")
						|| scd.getSecElement().getType().equals("selectTable")) {
					if (scd.getSecElement().getType().equals("selectTable")) {
						values = crfService.crfSectionElementValuesFromCrfMapplingTableColumnMap(scd.getSecElement());

					}

					htmlField = getSelectBoxButton(values);
				}
			}
		} else if (scd.getGroupElement() != null) {
			eleType = scd.getGroupElement().getType();
			if (scd.getGroupElement().getType().equals("text") || scd.getGroupElement().getType().equals("textArea")
					|| scd.getGroupElement().getType().equals("date")
					|| scd.getGroupElement().getType().equals("dateAndTime")
					|| scd.getGroupElement().getType().equals("non")) {
			} else {
				String[] values = scd.getGroupElement().getResponceType().split("##");
				if (scd.getGroupElement().getType().equals("radio")) {
					htmlField = getRadioButton(values);
				} else if (scd.getGroupElement().getType().equals("checkBox")) {
					htmlField = getCheckBoxButton(values);
				} else if (scd.getGroupElement().getType().equals("select")) {
					htmlField = getSelectBoxButton(values);
				}
			}
		}
		model.addAttribute("htmlField", htmlField);
		model.addAttribute("eleType", eleType);
		model.addAttribute("scd", scd);
		model.addAttribute("allLoginUsers", allLoginUsers);
		model.addAttribute("type", "accession");
		return "pages/discrepency/obsercationDescrepecyUpdatePageView";
	}

	private String getSelectBoxButton(String[] values) {
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='newValue' id='newValue'>");
		sb.append("<option value='-1'>--Select--</option>");
		for (String s : values) {
			sb.append("<option value='").append(s).append("'>").append(s).append("</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}

	private String getCheckBoxButton(String[] values) {
		StringBuilder sb = new StringBuilder();
		for (String s : values) {
			sb.append("<input type='checkbox' name='newValue' id='newValue' value='").append(s).append("'>").append(s);
		}
		return sb.toString();
	}

	private String getRadioButton(String[] values) {
		StringBuilder sb = new StringBuilder();
		for (String s : values) {
			sb.append("<input type='radio' name='newValue' id='newValue' value='").append(s).append("'>").append(s);
		}
		return sb.toString();
	}

	@RequestMapping(value = "/discrepancyCloseOrUpdate", method = RequestMethod.POST)
	public String discrepancyCloseOrUpdate(@ModelAttribute("id") Long id, @ModelAttribute("newValue") String newValue,
			@ModelAttribute("comment") String comment, @ModelAttribute("userId") Long userId,
			@ModelAttribute("status") String status, @ModelAttribute("eleType") String eleType,
			@ModelAttribute("newValue2") String newValue2, @ModelAttribute("type") String type,
			HttpServletRequest request, RedirectAttributes redirectAttributes, ModelMap model) {
		System.out.println(id);
		String username = request.getSession().getAttribute("userName").toString();
		Long loginUserId = (Long) request.getSession().getAttribute("userId");
		LoginUsers user = userService.findById(loginUserId); // assign to

		if (type.equals("accession")) {
			StudyAccessionCrfDescrpency scd = crfService.studyAccessionCrfDescrpency(id);
			StudyAccessionCrfDescrpencyLog log = new StudyAccessionCrfDescrpencyLog();
			try {
				BeanUtils.copyProperties(log, scd);
			} catch (Exception e) {
				// TODO: handle exceptione
				e.printStackTrace();
			}
//			CrfDescrpency scd = crfService.studyCrfDescrpency(id);
			CrfSectionElementDataAudit saudit = null;
			if (!newValue2.trim().equals("")) {
				saudit = new CrfSectionElementDataAudit();
				saudit.setData(scd.getStydyAccCrfSecEleData());
				saudit.setElement(scd.getStydyAccCrfSecEleData().getElement());
				saudit.setKayName(scd.getStydyAccCrfSecEleData().getKayName());
				saudit.setLoginUsers(user);
				saudit.setCreatedOn(new Date());
				if (eleType.equals("radio") || eleType.equals("checkBox")) {
					saudit.setOldValue(scd.getStydyAccCrfSecEleData().getValue());
					scd.setOldValue(scd.getStydyAccCrfSecEleData().getValue());
					scd.getStydyAccCrfSecEleData().setValue(newValue2);
					scd.setValue(newValue2);
					saudit.setNewValue(newValue2);
				} else {
					saudit.setOldValue(scd.getStydyAccCrfSecEleData().getValue());
					scd.setOldValue(scd.getStydyAccCrfSecEleData().getValue());
					scd.getStydyAccCrfSecEleData().setValue(newValue);
					saudit.setNewValue(newValue);
					scd.setValue(newValue);
				}
//				scd.setTypeOfElemet("section");
			}
			if (userId != null && userId != 0 && userId != -1) {
				LoginUsers assignTo = userService.findById(userId);
				scd.setAssingnedTo(assignTo.getUsername());
//				scd.setTypeOfElemet("assigned");
				if (status.equals("onHold"))
					scd.setStatus(status);

			} else if (status.equals("closed")) {
				scd.setOldStatus(scd.getStatus());
				scd.setStatus(status);
			} else {
				scd.setOldStatus(scd.getStatus());
				scd.setStatus(status);
			}
			scd.setUpdatedBy(username);
			scd.setUpdatedOn(new Date());
			scd.setUpdateReason(comment);

			crfService.updateStudyAccessionCrfDescrpency(scd, log, saudit);
		} else {

		}

		redirectAttributes.addFlashAttribute("pageMessage", "Descrepancy " + status + " Successfully");
		return "redirect:/discrepancy/";
	}

	@RequestMapping(value = "/observationdiscrepancyCloseOrUpdate", method = RequestMethod.POST)
	public String observationdiscrepancyCloseOrUpdate(@ModelAttribute("id") Long id,
			@ModelAttribute("newValue") String newValue, @ModelAttribute("comment") String comment,
			@ModelAttribute("userId") Long userId, @ModelAttribute("status") String status,
			@ModelAttribute("eleType") String eleType, @ModelAttribute("newValue2") String newValue2,
			@ModelAttribute("type") String type, HttpServletRequest request, RedirectAttributes redirectAttributes,
			ModelMap model) {
		System.out.println(id);
		String username = request.getSession().getAttribute("userName").toString();

		CrfDescrpency scd = crfService.studyCrfDescrpency(id);
		CrfDescrpencyLog log = new CrfDescrpencyLog();
		try {
			BeanUtils.copyProperties(log, scd);
			log.setCrfDescrepency(scd);
		} catch (Exception e) {
			// TODO: handle exceptione
			e.printStackTrace();
		}
//			CrfDescrpency scd = crfService.studyCrfDescrpency(id);
		CrfDescrpencyAudit descAudit = null;
		if (!newValue2.trim().equals("")) {
			descAudit = new CrfDescrpencyAudit();
			descAudit.setStdSubGroupObservationCrfs(scd.getStdSubGroupObservationCrfs());
			descAudit.setDesc(scd);
			descAudit.setCrf(scd.getCrf());
			descAudit.setVolPeriodCrf(scd.getVolPeriodCrf());
			descAudit.setKayName(scd.getKayName());
			descAudit.setSecElement(scd.getSecElement());
			descAudit.setSecEleData(scd.getSecEleData());
			descAudit.setCrfRule(scd.getCrfRule());
			descAudit.setStatus(scd.getStatus());
			descAudit.setAssingnedTo(scd.getAssingnedTo());
			descAudit.setRisedBy(scd.getRisedBy());
			descAudit.setOldValue(scd.getOldValue());
			descAudit.setOldStatus(scd.getOldStatus());

			descAudit.setUpdatedBy(username);
			descAudit.setUpdatedOn(new Date());
			if (eleType.equals("radio") || eleType.equals("checkBox")) {
				descAudit.setOldValue(scd.getSecEleData().getValue());
				scd.setOldValue(scd.getSecEleData().getValue());
				scd.getSecEleData().setValue(newValue2);
				scd.setValue(newValue2);
				descAudit.setNewValue(newValue2);
			} else {
				descAudit.setOldValue(scd.getSecEleData().getValue());
				scd.setOldValue(scd.getSecEleData().getValue());
				scd.getSecEleData().setValue(newValue);
				descAudit.setNewValue(newValue);
				scd.setValue(newValue);
			}
//				scd.setTypeOfElemet("section");

//				crfDAO.saveCrfDescrpencyAudit(descAudit);
		}
		if (userId != null && userId != 0 && userId != -1) {
			LoginUsers user = userService.findById(userId);
			scd.setAssingnedTo(user.getUsername());
//				scd.setTypeOfElemet("assigned");
			if (status.equals("onHold"))
				scd.setStatus(status);

		} else if (status.equals("closed")) {
			scd.setOldStatus(scd.getStatus());
			scd.setStatus(status);
		} else {
			scd.setOldStatus(scd.getStatus());
			scd.setStatus(status);
		}
		scd.setUpdatedBy(username);
		scd.setUpdatedOn(new Date());
		scd.setUpdateReason(comment);

		crfService.updateStudyObservatoinCrfDescrpency(scd, log, descAudit);

		redirectAttributes.addFlashAttribute("pageMessage", "Descrepancy " + status + " Successfully");
		return "redirect:/discrepancy/";
	}

	@RequestMapping(value = "/discDataNewSecViewsd/{keyName}/{id}/{crfId}/{vpcId}/{condition}", method = RequestMethod.GET)
	public String discDataNewSecView(@PathVariable("keyName") String keyName, @PathVariable("id") Long id,
			@PathVariable("crfId") Long crfId, @PathVariable("vpcId") Long vpcId,
			@PathVariable("condition") String condition, HttpServletRequest request, ModelMap model) {

		String username = request.getSession().getAttribute("userName").toString();
		VolunteerPeriodCrf vpc = studyService.volunteerPeriodCrf(vpcId);
		Crf crf = crfService.studyCrf(crfId);
		CrfSectionElement ele = crfService.studyCrfSectionElement(id);
		CrfSectionElementData data = crfService.studyCrfSectionElementData(vpc, ele, keyName);
		List<CrfDescrpency> list = crfService.alldiscrepencyEleInfo(crf, vpc, keyName, condition);
		List<LoginUsers> allLoginUsers = userService.findAllActiveUsers();
		model.addAttribute("allLoginUsers", allLoginUsers);

		model.addAttribute("kayName", keyName);
		model.addAttribute("username", username);
		model.addAttribute("vpc", vpc);
		model.addAttribute("crf", crf);
		model.addAttribute("vpc", vpc);
		model.addAttribute("ele", ele);
		model.addAttribute("data", data);
		model.addAttribute("list", list);

		return "pages/reviewer/period/descrepecyCreationPageSecWithCondition";
	}
}
