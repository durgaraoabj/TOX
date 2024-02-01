package com.springmvc.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springmvc.model.AmendmentDetails;
import com.springmvc.service.AmendmentService;

@Controller
@RequestMapping("/amandment")
public class AmendmentController {
	
	@Autowired
	AmendmentService amendmentService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String amendmentPage(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long studyNo = (Long) request.getSession().getAttribute("activeStudyId");
		List<AmendmentDetails> amdList = amendmentService.getAmendmentDetailsRecrodsList(studyNo);
		model.addAttribute("amdList", amdList);
		return "amendmentPage";
	}
	
	@RequestMapping(value="/saveAmandmentDetails", method=RequestMethod.POST)
	public String saveAmandmentDetails(ModelMap model, HttpServletRequest request, @RequestParam("amdDetails")List<String> amdDetails, RedirectAttributes redirectAttributes) {
		Long studyNo = (Long) request.getSession().getAttribute("activeStudyId");
		String userName = request.getSession().getAttribute("userName").toString();
		String result = amendmentService.saveAmandmentDetails(userName, amdDetails, studyNo);
		if(result.equals("success"))
			redirectAttributes.addFlashAttribute("pageMessage", "Amendment Details Saved Successfully...!");
		else redirectAttributes.addFlashAttribute("pageError", "Amendment Details Saving Failed. Please Try Again.");
		return "redirect:/amandment/";
	}
	
	
	@RequestMapping(value="/checkStudyAmandments/{studyId}", method=RequestMethod.GET)
	public String checkStudyAmandments(ModelMap model, @PathVariable("studyId")Long studyId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		List<AmendmentDetails> admList = amendmentService.getStudyAmendmentDetailsList(studyId);
		if(admList.size() > 0) 
			model.addAttribute("amendmentMessage", "Un-Approved (OR) Un-Closed Amendments Exists For This Study.");
		else model.addAttribute("amendmentMessage", "");
		return "pages/amendment/amendmentMessagePage";
	}

}
