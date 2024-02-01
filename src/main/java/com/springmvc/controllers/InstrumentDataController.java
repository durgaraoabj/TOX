package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.CrfSectionElementInstrumentValue;
import com.covide.dto.StagoDto;
import com.covide.dto.SysmaxDto;
import com.springmvc.model.CongulometerInstrumentValues;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SysmaxInstrumentValues;
import com.springmvc.service.InstrumentService;
import com.springmvc.service.StudyService;

@Controller
@RequestMapping("/instrument")
public class InstrumentDataController {
	
	
	@Autowired
	InstrumentService instrumentService;
	
	@Autowired
	StudyService studyService;
	
	@RequestMapping(value="/coagulometerData", method=RequestMethod.GET)
	public String coagulometerData(HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		
		List<StudyMaster> studyList = studyService.findAll();
		
		model.addAttribute("studyList", studyList);
		return "coagulometerData";
		
	}
	
	@RequestMapping(value="/coagulometerStudyData/{studyId}", method=RequestMethod.GET)
	public String coagulometerStudyData(@PathVariable("studyId") Long studyId,HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		
		   List<CongulometerInstrumentValues> civList=instrumentService.getCongulometerInstrumentValuesWithStudyId(studyId);
		   List<StagoDto> stagodata=new ArrayList<>();
		   
			List<String> sub=new ArrayList<>();
		   for(CongulometerInstrumentValues civ:civList) {
			   if(!sub.contains(civ.getAnimalSerialId())) {
				   sub.add(civ.getAnimalSerialId());  
			   }
			  
		   }
		   for(String s:sub) {
			   StagoDto sg=null;
			   sg=new StagoDto();
		       for(CongulometerInstrumentValues civ:civList) {
			    if(s.equals(civ.getAnimalSerialId())) {
			    	sg.setAnimalSerialId(civ.getAnimalSerialId());
			    	if(civ.getType().equals("PT")) {
			    		sg.setPtVal(civ.getWeight());
			    	}
			    	if(civ.getType().equals("APTT")) {
			    		sg.setApttVal(civ.getWeight());
			    	}
			    }
			  }
		       stagodata.add(sg);
		   }
		   model.addAttribute("civList", civList);
		   model.addAttribute("stagodata", stagodata);
		
		return "pages/instrument/coagulometerList";
		
	}
	@RequestMapping(value="/sartoriusData", method=RequestMethod.GET)
	public String sartoriusData(HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		
		List<StudyMaster> studyList = studyService.findAll();
		
		model.addAttribute("studyList", studyList);
		return "sartoriusData";
		
	}
	@RequestMapping(value="/sartoriusFullData/{studyId}", method=RequestMethod.GET)
	public String sartoriusFullData(@PathVariable("studyId") Long studyId,HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		
		   List<CrfSectionElementInstrumentValue> cseivList=instrumentService.getCrfSectionElementInstrumentValueList();
		   model.addAttribute("cseivList", cseivList);
		
		return "pages/instrument/sartoriusList";
		
	}
	
	@RequestMapping(value="/sysmaxData", method=RequestMethod.GET)
	public String sysmaxData(HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		
		List<StudyMaster> studyList = studyService.findAll();
		
		model.addAttribute("studyList", studyList);
		return "sysmaxData";
		
	}
	
	@RequestMapping(value="/sysmaxStudyData/{studyId}", method=RequestMethod.GET)
	public String sysmaxStudyData(@PathVariable("studyId") Long studyId,HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		
		   List<SysmaxInstrumentValues> civList=instrumentService.getSysmaxInstrumentValuesWithStudyId(studyId);
		   List<SysmaxDto> sdlist=new ArrayList<>();
		   
			List<String> sub=new ArrayList<>();
		   for(SysmaxInstrumentValues siv:civList) {
			   if(!sub.contains(siv.getAnimalSerialId())) {
				   sub.add(siv.getAnimalSerialId());  
			   }
			  
		   }
		   for(String s:sub) {
			   SysmaxDto sd=null;
			   sd=new SysmaxDto();
		       for(SysmaxInstrumentValues siv:civList) {
			    if(s.equals(siv.getAnimalSerialId())) {
			    	sd.setAnimalSerialId(siv.getAnimalSerialId());
			    	if(siv.getType().equals("WBC")) {
			    		sd.setWbc(siv.getValue());
			    	}
			    	if(siv.getType().equals("RBC")) {
			    		sd.setRcb(siv.getValue());
			    	}
			    	if(siv.getType().equals("HGB")) {
			    		sd.setHgb(siv.getValue());
			    	}
			    	if(siv.getType().equals("HCT")) {
			    		sd.setHct(siv.getValue());
			    	}
			    	
			    }
			  }
		       sdlist.add(sd);
		   }
		   model.addAttribute("civList", civList);
		   model.addAttribute("sdlist", sdlist);
		
		return "pages/instrument/sysmaxList";
		
	}
	
	@RequestMapping(value="/insturmenConfiguration", method=RequestMethod.GET)
	public String insturmenConfiguration(HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		
		List<StudyMaster> studyList = studyService.findAll();
		
		model.addAttribute("studyList", studyList);
		return "coagulometerData";
		
	}
}
