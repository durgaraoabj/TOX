package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springmvc.dao.EmployeeDao;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.model.VolunteerPeriodCrfStatus;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;
import com.springmvc.service.UserWiseStudiesAsignService;

@Controller
@RequestMapping("/period")
public class PeriodManagementController {
	@Autowired
	StudyService studyService;

	@Autowired
	UserService userService;

	@Autowired
	UserWiseStudiesAsignService userWiseStudiesAsignService;
	
	@Autowired
	EmployeeDao employeeDao;
	
	@RequestMapping(value = "/periodManagement", method = RequestMethod.GET)
	public String volunteerCreation(ModelMap model, HttpServletRequest request) {
		
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm =  studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		try {
			model.addAttribute("configureStatus", sm.isCrfConfiguation());
		}catch (Exception e) {
			model.addAttribute("configureStatus", false);
		}
		
		if(sm.isVolConfiguation()) {
			List<StudyPeriodMaster> plist= studyService.allStudyPeriods(sm);
			
			model.addAttribute("plist", plist);
			return "periodManagement.tiles";
		}else
			return "buildStudyNotDose.tiles";
	}
	
	
	
	
	
	@RequestMapping(value="/viewSubjectInfo/{peiodId}", method=RequestMethod.GET)
	public String viewSubjectInfo(@PathVariable("peiodId") Long peiodId, 
			HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm =  studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		StudyPeriodMaster period = studyService.studyPeriodMaster(peiodId);
		model.addAttribute("period", period);
		List<VolunteerPeriodCrfStatus> vpcsl  = studyService.volunteerPeriodCrfStatusList(peiodId);
		model.addAttribute("vpcsl", vpcsl);
		
		
		String username = request.getSession().getAttribute("userName").toString();
		
		List<VolunteerPeriodCrf> vpcl  = studyService.volunteerPeriodCrfList(peiodId);
		
		List<Long> volid = new ArrayList<>();
		List<VolunteerPeriodCrf> vpcl2  = new ArrayList<VolunteerPeriodCrf>();
		for(VolunteerPeriodCrf vpc : vpcl) {
			if(!volid.contains(vpc.getVol().getId())) {
				vpcl2.add(vpc);
			}
		}
		model.addAttribute("vpcl", vpcl2);
		
		List<StudyPeriodMaster> plist= studyService.allStudyPeriods(sm);
		
		model.addAttribute("plist", plist);
		return "periodManagementVollist.tiles";
	}
	
	
	@RequestMapping(value = "/subjectMatrix", method = RequestMethod.GET)
	public String subjectMatrix(ModelMap model, HttpServletRequest request) {
		
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm =  studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		if(sm.isVolConfiguation()) {
			List<Volunteer> vlist = new ArrayList<>(); 
			List<Volunteer> volList = studyService.studyVolunteerList(sm);
			List<StudyPeriodMaster> plist= studyService.allStudyPeriods(sm);
			Map<Long, Volunteer> volMap = new HashMap<>();
			
			for(Volunteer v : volList) {
				volMap.put(v.getId(), v);
//				v.setPeriods(plist);
				
				for(StudyPeriodMaster sp: plist) {
					VolunteerPeriodCrfStatus vpcs = studyService.volunteerPeriodCrfStatus(sp, v);
					sp.setVpcs(vpcs);
					v.getPeriods().add(sp);					
//					List<VolunteerPeriodCrf> vpcl  = studyService.volunteerPeriodCrfList(sp, v);
				}
				vlist.add(v);
			}
			
			model.addAttribute("PageHedding", "Study");
			model.addAttribute("activeUrl", "study/");
			model.addAttribute("plist", plist);
			model.addAttribute("vlist", vlist);
			return "subjectMatrix.tiles";
		}else
			return "buildStudyNotDose.tiles";
	}
}
