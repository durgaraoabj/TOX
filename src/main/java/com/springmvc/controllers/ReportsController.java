package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.covide.crf.dao.CrfDAO;
import com.covide.crf.dto.Crf;
import com.covide.crf.pdf.actions.GenerateAnnotatePdf;
import com.covide.crf.pdf.actions.GenerateSiglePdfAction;
import com.covide.crf.service.CrfService;
import com.springmvc.model.PeriodCrfs;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.service.CrfPdfService;
import com.springmvc.service.ReportsService;
import com.springmvc.service.StudyService;

@Controller
@RequestMapping("/reports")
public class ReportsController {
	
	@Autowired
	StudyService studyService;
	
	@Autowired
	ReportsService reportsService;
	
	@Autowired
	CrfService crfService;
	
	@Autowired
	CrfPdfService crfPdfService;
	
	@Autowired
	CrfDAO crfDAO;
	
	@RequestMapping(value="/pdfReportsSelection", method=RequestMethod.GET)
	public String pdfReportsSelection(ModelMap map, HttpServletRequest request) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		List<Volunteer> volList = reportsService.getVolunteersList(sm);
		List<StudyPeriodMaster> periodList = reportsService.getPeriodsList(sm);
		map.put("volList", volList);
		map.put("periodList", periodList);
		return "pdfReportsSelectionPage.tiles";
	}
    
	@RequestMapping(value="/getPdfList/{volId}/{periodId}", method=RequestMethod.GET)
	public String getpdfList(ModelMap map, HttpServletRequest request, @PathVariable("volId")String volId, @PathVariable("periodId")String periodId) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		List<VolunteerPeriodCrf> volcrfsList = null;
		if(!volId.equals("") && !periodId.equals("")) {
			volcrfsList = reportsService.getVolunteerAllPeriodCrfsList(sm, volId, periodId);
		}
		map.put("crfList", volcrfsList);
		return "pages/reports/pdfCrfsListPage";
	}
	
	@RequestMapping(value="/generateSinglePdf", method=RequestMethod.GET)
	public void generateSinglePdf(ModelMap map, HttpServletRequest request, HttpServletResponse response, @RequestParam("crfsIds")String crfsIds) {
		System.out.println("values are"+crfsIds);
		try {
			List<String> fileNames = new ArrayList<>();
			GenerateSiglePdfAction gspa = new GenerateSiglePdfAction();
			if(!crfsIds.equals("")) {
				if(crfsIds.contains("##")) {
					String[] temp1 = crfsIds.split("##");
					if(temp1.length >0) {
						for(int i=0; i<temp1.length; i++) {
							String val = temp1[i];
							VolunteerPeriodCrf vpc = studyService.volunteerPeriodCrf(Long.parseLong(val));
						    PeriodCrfs pcrf = vpc.getStdCrf();
						    Crf crf = crfService.getCrfForView(pcrf.getCrfId());
							String fileName = gspa.genrerateSinglePdf(response, request, crf, vpc, crfPdfService, crfDAO);
							if(!fileName.equals("")) {
								fileNames.add(fileName);
							}
						}
						
					}
				}else {
					VolunteerPeriodCrf vpc = studyService.volunteerPeriodCrf(Long.parseLong(crfsIds));
				    PeriodCrfs pcrf = vpc.getStdCrf();
				    Crf crf = crfService.getCrfForView(pcrf.getCrfId());
					String fileName = gspa.genrerateSinglePdf(response, request, crf, vpc, crfPdfService, crfDAO);
					if(!fileName.equals("")) {
						fileNames.add(fileName);
					}
				}
			}
			System.out.println("fileNames Length is :"+fileNames.size());
			if(fileNames.size() > 0) {
				gspa.mergePdf(request, response, fileNames);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/annotedCrfPdfReport/{vpcId}", method=RequestMethod.GET)
	public void annotedCrfPdfRoprtGeneration(HttpServletRequest request, HttpServletResponse response, @PathVariable("vpcId")long vpcId) {
		try {
			GenerateAnnotatePdf gap = new GenerateAnnotatePdf();
			VolunteerPeriodCrf vpc = studyService.volunteerPeriodCrf(vpcId);
		    PeriodCrfs pcrf = vpc.getStdCrf();
		    Crf crf = crfService.getCrfForView(pcrf.getCrfId());
			gap.genrerateAnnotatePdf(response, request, crf, vpc, crfPdfService, crfDAO);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	
		}
}
