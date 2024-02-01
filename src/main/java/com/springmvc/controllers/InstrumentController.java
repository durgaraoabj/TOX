package com.springmvc.controllers;

import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.dto.StudyDto;
import com.covide.template.dto.TestCodesUnitsDto;
import com.springmvc.dao.impl.ObservationInturmentConfigurationDaoImpl;
import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.ObservationInturmentConfiguration;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SysmexAnimalCode;
import com.springmvc.service.InstrumentService;
import com.springmvc.service.StudyService;
import com.springmvc.util.StagoThread;
import com.springmvc.util.SysmexThread;

@Controller
@RequestMapping("/instrumentData")
public class InstrumentController {
	@Autowired
	InstrumentService instrumentService;
	
	@Autowired
	StudyService studyService;
	
//	Map<String>
/**
 * 
 * @param request
 * @param instumentId - need to create service for the instrument id
 * @param observationId - take instrument parameters from the observation 
 * @param instumentid - stop the send result from the service to user  
 * @param model
 * @param redirectAttributes
 * @return acknolege ment to client 
 */
	@RequestMapping(value="/callInstument/{instumentId}/{observationId}/{instumentid}", method=RequestMethod.GET, produces= {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody TestCodesUnitsDto callInstument(HttpServletRequest request, 
			@PathVariable("instumentId") Long instumentId,@PathVariable("observationId") Long observationId,@PathVariable("instumentid") Long instumentid,
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		Long userId = (Long) request.getSession().getAttribute("userId");
		
		InstrumentIpAddress instrumentIp = instrumentService.instrumentIpAddress(instumentId);
		if(instrumentIp.getInstrumentName().equalsIgnoreCase("VITROS")) {
			TestCodesUnitsDto dto = instrumentService.callVitrosService(studyId, userId, instrumentIp, observationId, instumentid);
			dto.setInstrumentName(instrumentIp.getInstrumentName());
			return dto;
		}else if(instrumentIp.getInstrumentName().equalsIgnoreCase("SYSMEX")) {
			TestCodesUnitsDto dto = instrumentService.callSysmexService(studyId, userId, instrumentIp, observationId, instumentid);
			dto.setInstrumentName(instrumentIp.getInstrumentName());
			return dto;
		}
		return null;
	}
	
	
	@RequestMapping(value = "/callInstumentStagoSerivce/{instumentId}/{observationId}/{instumentid}/{sampleType}/{loatNo}/{test}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody StudyDto callStagoService(HttpServletRequest request, ModelMap model,
			@PathVariable("instumentId") Long instumentId,@PathVariable("observationId") Long observationId,@PathVariable("instumentid") Long instumentid,
			@PathVariable("sampleType") String sampleType, @PathVariable("loatNo") String loatNo,
			@PathVariable("test") String test,
//			@PathVariable("studyId") Long studyId,
			RedirectAttributes redirectAttributes) {
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		Long userId = (Long) request.getSession().getAttribute("userId");
		InstrumentIpAddress instrumentIp = instrumentService.instrumentIpAddress(instumentId);
		StudyDto dto = instrumentService.callStagoService(studyId, userId, instrumentIp, observationId, instumentid,sampleType, loatNo, test);
		return dto;
		
	}
	
}
