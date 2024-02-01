package com.springmvc.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.Crf;
import com.covide.enums.StatusMasterCodes;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyCageAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudySubGroupAnimal;
import com.springmvc.service.AcclimatizationService;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;

@Controller
@RequestMapping("/cageDesign")
public class CageController {
	@Autowired
	UserService userService;
	@Autowired
	AcclimatizationService accService;
	@Autowired
	StudyService studyService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String cageDesign(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster study = studyService.findByStudyId(studyId);
		if (study.getRadamizationStatus() != null && (study.getRadamizationStatus().getStatusCode().equals(StatusMasterCodes.REVIEWED.toString()) 
				|| study.getRadamizationStatus().getStatusCode().equals(StatusMasterCodes.APPROVED.toString()))) { 
			Map<Long, String> animals = accService.allUnCagedAnimals(studyId);
			model.addAttribute("animals", animals);
		} else {
			model.addAttribute("pageError", "Randamization not done.");
			model.addAttribute("animals", new HashMap<>());
		}

		Map<String, List<StudyCageAnimal>> cagedAnimals = accService.cagedAnimals(studyId);
		model.addAttribute("cagedAnimals", cagedAnimals);
		return "cageDesign";
	}

	@RequestMapping(value = "/saveCageDesign", method = RequestMethod.POST)
	public String saveAnimalCageing(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes,
			@RequestParam("cageNameOrNo") String cageNameOrNo, @RequestParam("animalIds") List<Long> animalIds) {

		try {
			Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
			Long userId = (Long) request.getSession().getAttribute("userId");
			String status = accService.saveAnimalCageing(cageNameOrNo, animalIds, studyId, userId);
			if (status.equals("success"))
				redirectAttributes.addFlashAttribute("pageMessage", "Data Saved Successfully.");
			else
				redirectAttributes.addFlashAttribute("pageMessage", "Data Saving Failed. Please Try Again.");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("pageMessage", "Data Saving Failed. Please Try Again.");
		}
		return "redirect:/cageDesign/";
	}

	@RequestMapping(value = "/animalGroup", method = RequestMethod.GET)
	public String animalGroup(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
//		StudyMaster study = studyService.findByStudyId(studyId);
//		if(study.getRadamizationStatus().getStatusCode().equals("REVIEWED") || study.getRadamizationStatus().getStatusCode().equals("APPROVED")) {
//			Map<Long, String> animals = accService.allUnCagedAnimals(studyId);
//			model.addAttribute("animals", animals);
//		}else {
//			model.addAttribute("pageError", "Randamization not done.");
//			model.addAttribute("animals", new HashMap<>());
//		}

//		Map<String, List<StudyCageAnimal>> cagedAnimals = accService.cagedAnimals(studyId);
//		model.addAttribute("cagedAnimals", cagedAnimals);

		Map<Long, List<StudySubGroupAnimal>> subGroupAnimals = accService.studySubGroupAnimal(studyId);
		model.addAttribute("subGroupAnimals", subGroupAnimals);
		return "animalGroup";
	}
}
