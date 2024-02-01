package com.springmvc.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.service.CrfService;
import com.springmvc.dto.FileInformation;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.service.AcclimatizationService;
import com.springmvc.service.ExpermentalDesignService;
import com.springmvc.service.StudyService;

@Controller
@RequestMapping("/dataExport")
@PropertySource(value = { "classpath:application.properties" })
public class DataPdfExport {
	@Autowired
	private Environment environment;
	@Autowired
	StudyService studyService;
	@Autowired
	CrfService crfService;
	@Autowired
	AcclimatizationService accService;
	@Autowired
	private ExpermentalDesignService expermentalDesignService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String expermentalDesign(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long studyId = Long.parseLong(request.getSession().getAttribute("activeStudyId").toString());
		model.addAttribute("studyId", studyId);
		List<StudyAcclamatizationData> accessionObservations = accService.studyAcclamatizationData(studyId);
		model.addAttribute("accessionObservations", accessionObservations);
		List<StdSubGroupObservationCrfs> unicaTreatmentObservations = expermentalDesignService
				.stdSubGroupObservationCrfs(studyId);
		model.addAttribute("unicaTreatmentObservations", unicaTreatmentObservations);
		return "dataExport";
	}

	@RequestMapping(value = "/treatmentObservatinExport", method = RequestMethod.POST)
	public void treatmentObservatinExport(ModelMap model, @RequestParam("observationId") Long observationId,
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		try {
			FileInformation fi = expermentalDesignService.treatmentObservatinExport(observationId, model, request, redirectAttributes, response);
			System.out.println(fi.getFilePath());
			File file = new File(fi.getFilePath());
			response.setContentType("application/"+fi.getFileName());
			response.addHeader("Content-Disposition", "attachment; filename="+fi.getFileName());
			response.setContentLength((int) file.length());
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			FileCopyUtils.copy(inputStream, response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/acclamatigationObservatinExport", method = RequestMethod.POST)
	public void acclamatigationObservatinExport(ModelMap model, @RequestParam("observationId") Long observationId,
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		try {
			FileInformation fi = expermentalDesignService.acclamatizationObservatinExport(observationId, model, request, redirectAttributes, response);
			System.out.println(fi.getFilePath());
			File file = new File(fi.getFilePath());
			response.setContentType("application/"+fi.getFileName());
			response.addHeader("Content-Disposition", "attachment; filename="+fi.getFileName());
			response.setContentLength((int) file.length());
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			FileCopyUtils.copy(inputStream, response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
