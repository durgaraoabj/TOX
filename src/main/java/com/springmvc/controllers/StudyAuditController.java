package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springmvc.model.GroupInfo;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;
import com.springmvc.service.StudyAuditService;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;


@Controller
@RequestMapping("/studyAudit")
@PropertySource(value = { "classpath:application.properties" })
public class StudyAuditController {
	@Autowired
    private Environment environment;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	StudyService studyService;
	
	
	@Autowired
	StudyAuditService studyAuditService;
	
	@RequestMapping(value="/studyViewData", method=RequestMethod.GET)
	public String stdClinicalHome(HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		String studyNo = request.getSession().getAttribute("activeStudyNo").toString();
		StudyMaster study = studyService.findByStudyNo(studyNo);
		List<GroupInfo> group=studyAuditService.getGroupInfoForStudy(study);
		List<StdSubGroupObservationCrfs> ssgocList=studyAuditService.getStdSubGroupObservationCrfsWithStudy(study);
		
		model.addAttribute("study", study);
		model.addAttribute("group", group);
		model.addAttribute("ssgocList", ssgocList);
		
		return "studyView.tiles";
	}
	@RequestMapping(value="/crfAuditData", method=RequestMethod.GET)
	public String crfAuditData(HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		String studyNo = request.getSession().getAttribute("activeStudyNo").toString();
		StudyMaster study = studyService.findByStudyNo(studyNo);
		List<SubGroupAnimalsInfoCrfDataCount> sgaindcList=studyAuditService.getSubGroupAnimalsInfoCrfDataCountWithStudyId(study);
		List<SubGroupAnimalsInfoCrfDataCount> sgaindcListData=new ArrayList<>();
		List<String> animalno=new ArrayList<>();
		for(SubGroupAnimalsInfoCrfDataCount sga:sgaindcList) {
			if(animalno.size()>0) {
				if(!animalno.contains(sga.getSubGroupAnimalsInfoAll().getAnimalNo()+":"+sga.getCrf().getObservationName())) {
					animalno.add(sga.getSubGroupAnimalsInfoAll().getAnimalNo()+":"+sga.getCrf().getObservationName());
					sgaindcListData.add(sga);
				   }
			}else {
				   animalno.add(sga.getSubGroupAnimalsInfoAll().getAnimalNo()+":"+sga.getCrf().getObservationName());
				   sgaindcListData.add(sga);
			}
			
		}
		
		model.addAttribute("study", study);
		model.addAttribute("sgaindcListData", sgaindcListData);
		
		return "crfAuditData.tiles";
	}
	
	@RequestMapping(value="/serachData" , method=RequestMethod.GET)
	public String  serachData(HttpServletRequest request,ModelMap model,RedirectAttributes redirectAttributes  ) {
		
		String username = request.getSession().getAttribute("userName").toString();
		
		return "searchPage";
		
	}
	/*@RequestMapping(value="/studySearchData" , method=RequestMethod.POST)
	public String  studySearchData(HttpServletRequest request,ModelMap model,RedirectAttributes redirectAttributes,
			@RequestParam ("studynoval") String studynoval ,@RequestParam ("studyNameval") String studyNameval ,
			@RequestParam ("sponsorval") String sponsorval,@RequestParam ("statusval") String statusval,
			@RequestParam ("roleval") String roleval) {
		
		List<StudyMaster> sml=studyService.getStudyMasterForSearch(studynoval,studyNameval,sponsorval,statusval,roleval);
		model.addAttribute("sml" ,sml);
			
		return "studySearchData";
		
	}*/
	
	@RequestMapping(value="/studySearchData/{studynovalid}/{studyNamevalid}/{sponsorvalid}/{statusvalid}/{rolevalid}" , method=RequestMethod.GET)
	public String  studySearchData(HttpServletRequest request,ModelMap model,RedirectAttributes redirectAttributes,
			@PathVariable("studynovalid") String studynovalid ,@PathVariable ("studyNamevalid") String studyNamevalid ,
			@PathVariable ("sponsorvalid") String sponsorvalid,@PathVariable ("statusvalid") String statusvalid,
			@PathVariable ("rolevalid") String rolevalid) {
		
		List<StudyMaster> sml=studyService.getStudyMasterForSearch(studynovalid,studyNamevalid,sponsorvalid,statusvalid,rolevalid);
		model.addAttribute("sml" ,sml);
		
		return "pages/studyaudit/studySearchListData";
		
	}
	
}
