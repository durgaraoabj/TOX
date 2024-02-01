package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springmvc.model.GroupInfo;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.service.CRFAuditService;
import com.springmvc.service.ExpermentalDesignService;
import com.springmvc.service.StudyAuditService;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;
import com.springmvc.util.ObservationData;

@Controller
@RequestMapping("/crfAudit")
public class CRFAuditController {
	
	@Autowired
	CRFAuditService  crfAuditService;
	
	@Autowired
	StudyService studyService;
	
	@Autowired
	StudyAuditService studyAuditService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private ExpermentalDesignService expermentalDesignService;
	
	@RequestMapping(value="/crfauditObjservations", method=RequestMethod.GET)
	public String crfauditObjservations(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		StudyMaster sm =  crfAuditService.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		List<GroupInfo> giList = crfAuditService.studyGroupInfoWithChaildReview(sm);
		model.addAttribute("study", sm);
		model.addAttribute("group", giList);
		return "crfauditObjservations";
	}
	
	@RequestMapping(value="/subgroupSubjectsAllReviewsAudit", method=RequestMethod.POST)
	public String subgroupSubjectsAllReviewsAudit(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		StudyMaster sm =  studyService.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		System.out.println(request.getParameter("subGroupId1"));
		Long id = Long.parseLong(request.getParameter("subGroupId1").toString());
		StudyMaster study = studyService.findByStudyNo(sm.getStudyNo());
		List<SubGroupAnimalsInfoCrfDataCount> sgaindcList=studyAuditService.getSubGroupAnimalsInfoCrfDataCountWithStudyIdAndSubId(study,id);
		List<SubGroupAnimalsInfoCrfDataCount> sgaindcListData=new ArrayList<>();
		List<String> animalno=new ArrayList<>();
		String groupn="";
		String subgroupn="";
		
		Long groupnid = null;
		Long subgroupnid = null;
		Long subjectgroupnid = null;
		for(SubGroupAnimalsInfoCrfDataCount sga:sgaindcList) {
				if(!animalno.contains(sga.getCrf().getObservationName())) {
					animalno.add(sga.getCrf().getObservationName());
					sgaindcListData.add(sga);
					groupn=sga.getGroup().getGroupName();
					subgroupn=sga.getSubGroup().getName();
					
					groupnid=sga.getGroup().getId();
					subgroupnid=sga.getSubGroup().getId();
					subjectgroupnid=sga.getSubGroupAnimalsInfoAll().getId();
				   }
		}
		model.addAttribute("groupname", groupn);
		model.addAttribute("subgroup", subgroupn);
		model.addAttribute("groupid", groupnid);
		model.addAttribute("subgroupnid", subgroupnid);
		model.addAttribute("subjectgroupnid", subjectgroupnid);
		
		model.addAttribute("study", study);
		model.addAttribute("sgaindcListData", sgaindcListData);
		return "subgroupSubjectsAuditAllReviews";
	}
	@RequestMapping(value="/allAnimalCrfDataReviewViewAudit", method=RequestMethod.POST)
	public String allAnimalCrfDataReviewViewAudit(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		StudyMaster sm =  studyService.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		Long userId = (Long) request.getSession().getAttribute("userId");
		LoginUsers user = userService.findById(userId);
		RoleMaster role = user.getRole();
		if(role.getRole().equals("ADMIN") || role.getRole().equals("SUPERADMIN") || role.getRole().equals("QA") || role.getRole().equals("Study Director") ) {
			model.addAttribute("acess", "yes");
		}else {
			model.addAttribute("acess", "no");
		}
		Long groupId = Long.parseLong(request.getParameter("groupId").toString());
		Long subGroupId = Long.parseLong(request.getParameter("subGroupId").toString());
		
		Long subGroupInfoId = Long.parseLong(request.getParameter("subGroupInfoId").toString()); //subject 
		Long stdSubGroupObservationCrfsId = Long.parseLong(request.getParameter("stdSubGroupObservationCrfsId").toString()); //observationId
		String review = request.getParameter("review");
		ObservationData data = expermentalDesignService.observationDataAllAnimal(subGroupInfoId, stdSubGroupObservationCrfsId, user,groupId,subGroupId);
		model.addAttribute("pageMessage", data.getMessage());
		model.addAttribute("data", data);
		model.addAttribute("subGroupInfoId", subGroupInfoId);
		model.addAttribute("stdSubGroupObservationCrfsId", stdSubGroupObservationCrfsId);
		if(review.equals("review"))		
			return "animalCrfDataReviewViewAllAnimalAudit";
		return "animalCrfDataReviewViewOnlyAllAnimalAudit";
	}
	
}
