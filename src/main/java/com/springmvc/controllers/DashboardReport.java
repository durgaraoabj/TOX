package com.springmvc.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springmvc.dao.UserWiseStudiesAsignDao;
import com.springmvc.model.StudyMaster;
import com.springmvc.reports.StudyProgress;
import com.springmvc.reports.SubjectEnrollmentBySite;
import com.springmvc.reports.SubjectEnrollmentForStudy;
import com.springmvc.service.DashBordService;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;
import com.springmvc.service.UserWiseStudiesAsignService;

@Controller
@RequestMapping("/dashboardReport")
@PropertySource(value = { "classpath:application.properties" })
public class DashboardReport {

	@Autowired
	DashBordService dashBordService;
	
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
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String dashboardPage(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		try {
			String currentStudy = request.getSession().getAttribute("activeStudyNo").toString();
			if(currentStudy!=null && !currentStudy.equals("")) {
				model.addAttribute("PageHedding", "Dash Board");
				model.addAttribute("activeUrl", "/dashboardReport/");
				StudyMaster sm =  studyService.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
				List<SubjectEnrollmentBySite> sebsList = dashBordService.subjectEnrollmentBySiteList(sm);
				model.addAttribute("siteList", sebsList);
				List<SubjectEnrollmentForStudy> sebstudyList = dashBordService.subjectEnrollmentForStudyList(sm);
				model.addAttribute("studyList", sebstudyList);
				List<StudyProgress> spList = dashBordService.studyProgressList(sm);
				model.addAttribute("spList", spList);				
				
				return "dashboardReport.tiles";
			}else {
				redirectAttributes.addFlashAttribute("pageWarning", "Please select study");
				return "redirect:/dashboard/userWiseActiveStudies";
			}
		}catch(NullPointerException npe) {
			if(request.getSession().getAttribute("userRole").toString().equalsIgnoreCase("ADMIN"))
				return "adminDashBoard.tiles";
			else {
				redirectAttributes.addFlashAttribute("pageWarning", "Please select study");
				return "redirect:/dashboard/userWiseActiveStudies";
			}
		}
	}
}
