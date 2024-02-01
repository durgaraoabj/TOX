package com.springmvc.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.covide.crf.dao.CrfDAO;
import com.covide.crf.dto.Crf;
import com.covide.crf.pdf.actions.PdfGeneratingAction;
import com.covide.crf.service.CrfService;
import com.springmvc.model.PeriodCrfs;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.service.CrfPdfService;
import com.springmvc.service.StudyService;

@Controller
@RequestMapping("/pdfview")
@PropertySource(value = { "classpath:application.properties" })
public class CRFPDFViewController {
	
	@Autowired
	StudyService studyService;
	
	@Autowired
	CrfService crfService;
	
	@Autowired
	CrfPdfService crfPdfService;
	
	@Autowired
	CrfDAO crfDAO;
	
	@RequestMapping(value="/crfPdfView/{vpcId}", method=RequestMethod.GET)
	public void crfPdfView(@PathVariable("vpcId")Long vpcId, HttpServletRequest request,  HttpServletResponse response, ModelMap model) {
		PdfGeneratingAction pga = new PdfGeneratingAction();
		try {
			//Crf crf  =  crfService.getCrfForView(id);
			VolunteerPeriodCrf vpc = studyService.volunteerPeriodCrf(vpcId);
		    PeriodCrfs pcrf = vpc.getStdCrf();
		    Crf crf = crfService.getCrfForView(pcrf.getCrfId());
			pga.generatePdf(response, request, crf, vpc, crfPdfService, crfDAO);
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}
}
