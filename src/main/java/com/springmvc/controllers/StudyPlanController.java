package com.springmvc.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springmvc.model.StudyPlanFilesDetails;
import com.springmvc.service.StudyPlanService;

@RequestMapping("/stuydPlan")
@Controller
public class StudyPlanController {
	
	
	@Autowired
	StudyPlanService studyPlanService;
	
	@RequestMapping(value="/studyPlanUpload", method=RequestMethod.GET)
	public String studyPlanUpload(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		model.addAttribute("studyId", studyId);
		return "studyPlanUpload";
	}
	
	@RequestMapping(value="/saveStudyPlan", method=RequestMethod.POST)
	public String saveStudyPlan(ModelMap model, @RequestParam("file") MultipartFile file, @RequestParam("studyId")Long studyId, 
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String username = request.getSession().getAttribute("userName").toString();
		StudyPlanFilesDetails spfd = studyPlanService.getStudyPlanFilesDetailsRecord(studyId);
		if(spfd != null) {
			redirectAttributes.addFlashAttribute("pageError", "Study Plan Already Uploaded For This Study.");
		}else {
			String result = studyPlanService.saveStudyPlanFilesDetails(username, file, studyId);
			if(result.equals("success"))
				redirectAttributes.addFlashAttribute("pageMessage", "Study Plan Uploaded Successfully....!");
			else redirectAttributes.addFlashAttribute("pageError", "Study Plan Uploading Failed. Please Try Again.");
		}
		return "redirect:/stuydPlan/studyPlanUpload";
	}
	
	@RequestMapping(value="/viewStudyPlan", method=RequestMethod.GET)
	public void viewStudyPlan(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		String result = studyPlanService.generateStudyPlanReport(studyId, request, response);
		System.out.println(result);
	}


}
