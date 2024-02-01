package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.Crf;
import com.covide.crf.service.CrfService;
import com.covide.dto.ExpermentalDto;
import com.covide.dto.SatusAndWorkFlowDetailsDto;
import com.covide.enums.StatusMasterCodes;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.DepartmentMaster;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.WorkFlowStatusDetails;
import com.springmvc.service.ExpermentalDesignService;
import com.springmvc.service.StudyService;

@Controller
@RequestMapping("/expermentalDesign")
@PropertySource(value = { "classpath:application.properties" })
public class ExpermentalDesignConroller {
	@Autowired
    private Environment environment;
	
	@Autowired
	StudyService studyService;
	
	@Autowired
	CrfService crfService;
	@Autowired
	private ExpermentalDesignService expermentalDesignService;
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String expermentalDesign(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		StudyMaster sm =  studyService.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		if(!sm.getStatus().getStatusCode().equals(StatusMasterCodes.IN.toString())) {
			model.addAttribute("study", sm);
			
			List<GroupInfo> gi = expermentalDesignService.studyGroupInfoWithChaild(sm);
			model.addAttribute("group", gi);
			return "expermentalDesignView";
			
//			StudyDesignStatus sds = expermentalDesignService.getStudyDesignStatusRecord(sm.getId());
//			if(sds != null) {
//				if(sds.getCount() < 3) {
//					List<GroupInfo> gi = expermentalDesignService.studyGroupInfoWithChaild(sm);
//					model.addAttribute("group", gi);
//					return "expermentalDesignView";
//				}else {
//					List<GroupInfo> gi = expermentalDesignService.studyGroupInfoWithChaild(sm);
//					Map<Long, String> newGroupMap = expermentalDesignService.getNewGroupDataDetails(gi);
//					model.addAttribute("newGroupMap", newGroupMap);
//					model.addAttribute("group", gi);
//					return "expermentalDesignViewForUpdate";
//				}
//			}else {
//				model.addAttribute("warnMessage", "Observation Design Having Problem. Please contact Administrator.");
//				return "warningPage";
//			}
		}else {
			model.addAttribute("warnMessage", "Study Meta data not avialable.");
			return "warningPage";
		}
		
		
	}
	
	@RequestMapping(value="/createNewExperimentalDesign", method=RequestMethod.POST)
	public String createNewExperimentalDesign(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		StudyMaster sm =  studyService.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		SatusAndWorkFlowDetailsDto sawfdDto = studyService.SatusAndWorkFlowDetailsDto("EDOROD", "EXPD");
		StatusMaster stm = null;
		WorkFlowStatusDetails wfsd = null;
		if(sawfdDto != null) {
			stm = sawfdDto.getSm();
			wfsd = sawfdDto.getWfsd();
		}
		String userName = request.getSession().getAttribute("userName").toString();
		List<GroupInfo> giList = expermentalDesignService.studyGroupInfo(sm);
		List<SubGroupInfo> subGroups = new ArrayList<SubGroupInfo>();
		List<SubGroupAnimalsInfo> animalInfo = new ArrayList<>();
		List<SubGroupAnimalsInfoAll> animalInfoAll = new ArrayList<>();
		SubGroupInfo sgif = null;
		int count =1;
		for(GroupInfo gi : giList) {
			if(sm.isRequiredSubGroup()) {
				for(int i=1; i<= gi.getGroupTest(); i++) {
					sgif = new SubGroupInfo();
					sgif.setStudy(sm);
					sgif.setGroup(gi);
					sgif.setName(request.getParameter("subGroupName"+gi.getId()+"_"+i));
//					sgif.setDose(request.getParameter("subGroupDose"+gi.getId()+"_"+i));
//					sgif.setConc(request.getParameter("subGroupConc"+gi.getId()+"_"+i));
					sgif.setCreatedBy(request.getSession().getAttribute("userName").toString());
					subGroups.add(sgif);
					
					if(gi.getGender().equals("Male") || gi.getGender().equals("Female")) {
						String gender = "Male";
						if(gi.getGender().equals("Female")) gender = "Female";
						SubGroupAnimalsInfo sgaif = new SubGroupAnimalsInfo();
						sgaif.setStudy(sm);
						sgaif.setGroup(gi);
						sgaif.setSubGroup(sgif);
						sgaif.setSequenceNo(count++);
						sgaif.setCount(Integer.parseInt(request.getParameter("subGroupCount"+gi.getId()+"_"+i)));
						sgaif.setGender(gender);
						sgaif.setCreatedBy(request.getSession().getAttribute("userName").toString());
						animalInfo.add(sgaif);
						
					}else {							
						SubGroupAnimalsInfo sgaif1 = new SubGroupAnimalsInfo();
						sgaif1.setStudy(sm);
						sgaif1.setGroup(gi);
						sgaif1.setSubGroup(sgif);
						sgaif1.setSequenceNo(count++);
						sgaif1.setCount(Integer.parseInt(request.getParameter("subGroupCount"+gi.getId()+"_"+i+"_Male")));
						sgaif1.setGender("Male");
						sgaif1.setCreatedBy(request.getSession().getAttribute("userName").toString());
						animalInfo.add(sgaif1);
						
						SubGroupAnimalsInfo sgaif2 = new SubGroupAnimalsInfo();
						sgaif2.setStudy(sm);
						sgaif2.setGroup(gi);
						sgaif2.setSubGroup(sgif);
						sgaif2.setSequenceNo(count++);
						sgaif2.setCount(Integer.parseInt(request.getParameter("subGroupCount"+gi.getId()+"_"+i+"_Female")));
						sgaif2.setGender("Female");
						animalInfo.add(sgaif2);
					}
				}
			}else {
				sgif = new SubGroupInfo();
				sgif.setStudy(sm);
				sgif.setGroup(gi);
				sgif.setName(gi.getGroupName());
//				sgif.setDose(request.getParameter("subGroupDose"+gi.getId()));
//				sgif.setConc(request.getParameter("subGroupConc"+gi.getId()));
				sgif.setCreatedBy(request.getSession().getAttribute("userName").toString());
				subGroups.add(sgif);
				
				if(gi.getGender().equals("Male") || gi.getGender().equals("Female")) {
					String gender = "Male";
					if(gi.getGender().equals("Female")) gender = "Female";
					SubGroupAnimalsInfo sgaif = new SubGroupAnimalsInfo();
					sgaif.setStudy(sm);
					sgaif.setGroup(gi);
					sgaif.setSubGroup(sgif);
					sgaif.setSequenceNo(count++);
					sgaif.setCount(Integer.parseInt(request.getParameter("subGroupCount"+gi.getId())));
					sgaif.setGender(gender);
					sgaif.setCreatedBy(request.getSession().getAttribute("userName").toString());
					animalInfo.add(sgaif);
					
				}else {							
					SubGroupAnimalsInfo sgaif1 = new SubGroupAnimalsInfo();
					sgaif1.setStudy(sm);
					sgaif1.setGroup(gi);
					sgaif1.setSubGroup(sgif);     
					sgaif1.setSequenceNo(count++);
					sgaif1.setCount(Integer.parseInt(request.getParameter("subGroupCount"+gi.getId()+"_Male")));
					sgaif1.setGender("Male");
					sgaif1.setCreatedBy(request.getSession().getAttribute("userName").toString());
					animalInfo.add(sgaif1);
					
					SubGroupAnimalsInfo sgaif2 = new SubGroupAnimalsInfo();
					sgaif2.setStudy(sm);
					sgaif2.setGroup(gi);
					sgaif2.setSubGroup(sgif);
					sgaif2.setSequenceNo(count++);
					sgaif2.setCount(Integer.parseInt(request.getParameter("subGroupCount"+gi.getId()+"_Female")));
					sgaif2.setGender("Female");
					animalInfo.add(sgaif2);
				}
			}
			

		}
		
		sm.setExperimentalDesign("Done");
		sm.setEdDoneBy(userName);
		sm.setEdDoneDate(new Date());
		
		StudyDesignStatus sds = new StudyDesignStatus();
		sds.setCreatedBy(userName);
		sds.setCreatedOn(new Date());
		sds.setStatus(stm);
		sds.setStudyId(sm.getId());
		
		ApplicationAuditDetails apad = new ApplicationAuditDetails();
		apad.setAction("Expermental Design");
		apad.setCreatedBy(userName);
		apad.setCreatedOn(new Date());
		apad.setStudyId(sm);
		apad.setWfsdId(wfsd);
		
		
		String status = expermentalDesignService.saveExpermentalDesign(sm, subGroups, animalInfo, animalInfoAll, sds, apad);
		if(status.trim().equals("success"))
			redirectAttributes.addFlashAttribute("pageMessage", "Experimental Design saved Successfully");
		else
			redirectAttributes.addFlashAttribute("pageError", "Experimental Design Faild to Save");
		return "redirect:/expermentalDesign/";
	}
	@RequestMapping(value="/createExpermentalDesign", method=RequestMethod.POST)
	public String createExpermentalDesign(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		StudyMaster sm =  studyService.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		SatusAndWorkFlowDetailsDto sawfdDto = studyService.SatusAndWorkFlowDetailsDto("EDOROD", "EXPD");
		StatusMaster stm = null;
		WorkFlowStatusDetails wfsd = null;
		if(sawfdDto != null) {
			stm = sawfdDto.getSm();
			wfsd = sawfdDto.getWfsd();
		}
		String userName = request.getSession().getAttribute("userName").toString();
		List<GroupInfo> giList = expermentalDesignService.studyGroupInfo(sm);
		List<SubGroupInfo> subGroups = new ArrayList<SubGroupInfo>();
		List<SubGroupAnimalsInfo> animalInfo = new ArrayList<>();
		List<SubGroupAnimalsInfoAll> animalInfoAll = new ArrayList<>();
		SubGroupInfo sgif = null;
		for(GroupInfo gi : giList) {
			for(int i=1; i<= gi.getGroupTest(); i++) {
				sgif = new SubGroupInfo();
				sgif.setStudy(sm);
				sgif.setGroup(gi);
				sgif.setName(request.getParameter("subGroupName"+gi.getId()+"_"+i));
				sgif.setDose(request.getParameter("subGroupDose"+gi.getId()+"_"+i));
				sgif.setConc(request.getParameter("subGroupConc"+gi.getId()+"_"+i));
				sgif.setCreatedBy(request.getSession().getAttribute("userName").toString());
				subGroups.add(sgif);
				
				if(gi.getGender().equals("Male") || gi.getGender().equals("Female")) {
					String gender = "Male";
					if(gi.getGender().equals("Female")) gender = "Female";
					SubGroupAnimalsInfo sgaif = new SubGroupAnimalsInfo();
					sgaif.setStudy(sm);
					sgaif.setGroup(gi);
					sgaif.setSubGroup(sgif);
					System.out.println("subGroupCount"+gi.getId()+"_"+i);
					String value = request.getParameter("subGroupCount"+gi.getId()+"_"+i);
					sgaif.setCount(Integer.parseInt(request.getParameter("subGroupCount"+gi.getId()+"_"+i)));
					sgaif.setGender(gender);
					sgaif.setFormId(request.getParameter("subGroupFrom"+gi.getId()+"_"+i));
					sgaif.setToId(request.getParameter("subGroupTo"+gi.getId()+"_"+i));
					String[] f = sgaif.getFormId().split("\\/"); 
					sgaif.setFrom(f[1]);
					String[] t = sgaif.getToId().split("\\/");
					sgaif.setTo(t[1]);
					sgaif.setCreatedBy(request.getSession().getAttribute("userName").toString());
					animalInfo.add(sgaif);
					
					int start = Integer.parseInt(sgaif.getFrom());
					int end = Integer.parseInt(sgaif.getTo());
					for(; start<=end; start++) {
						SubGroupAnimalsInfoAll sa = new SubGroupAnimalsInfoAll();
						sa.setSubGroup(sgif);
						sa.setSubGroupAnimalsInfo(sgaif);
						sa.setNo(start);
						if(start<10) {
							sa.setAnimalNo("TOXX/00"+start);
						}else if(start<100) {
							sa.setAnimalNo("TOXX/0"+start);
						}else {
							sa.setAnimalNo("TOXX/"+start);
						}
						animalInfoAll.add(sa);
					}
				}else {							
					SubGroupAnimalsInfo sgaif1 = new SubGroupAnimalsInfo();
					sgaif1.setStudy(sm);
					sgaif1.setGroup(gi);
					sgaif1.setSubGroup(sgif);                              
					sgaif1.setCount(Integer.parseInt(request.getParameter("subGroupCount"+gi.getId()+"_"+i+"_Male")));
					sgaif1.setGender("Male");
					sgaif1.setFormId(request.getParameter("subGroupFrom"+gi.getId()+"_"+i+"_Male"));
					sgaif1.setToId(request.getParameter("subGroupTo"+gi.getId()+"_"+i+"_Male"));
					String[] f = sgaif1.getFormId().split("\\/"); 
					sgaif1.setFrom(f[1]);
					String[] t = sgaif1.getToId().split("\\/");
					sgaif1.setTo(t[1]);
					animalInfo.add(sgaif1);
					int start = Integer.parseInt(sgaif1.getFrom());
					int end = Integer.parseInt(sgaif1.getTo());
					for(; start<=end; start++) {
						SubGroupAnimalsInfoAll sa = new SubGroupAnimalsInfoAll();
						sa.setSubGroup(sgif);
						sa.setSubGroupAnimalsInfo(sgaif1);
						sa.setNo(start);
						if(start<10) {
							sa.setAnimalNo("TOXX/00"+start);
						}else if(start<100) {
							sa.setAnimalNo("TOXX/0"+start);
						}else {
							sa.setAnimalNo("TOXX/"+start);
						}
						animalInfoAll.add(sa);
					}
					
					SubGroupAnimalsInfo sgaif2 = new SubGroupAnimalsInfo();
					sgaif2.setStudy(sm);
					sgaif2.setGroup(gi);
					sgaif2.setSubGroup(sgif);
					sgaif2.setCount(Integer.parseInt(request.getParameter("subGroupCount"+gi.getId()+"_"+i+"_Female")));
					sgaif2.setGender("Female");
					sgaif2.setFormId(request.getParameter("subGroupFrom"+gi.getId()+"_"+i+"_Female"));
					sgaif2.setToId(request.getParameter("subGroupTo"+gi.getId()+"_"+i+"_Female"));
					f = sgaif2.getFormId().split("\\/"); 
					sgaif2.setFrom(f[1]);
					t = sgaif2.getToId().split("\\/");
					sgaif2.setTo(t[1]);
					animalInfo.add(sgaif2);
					
					start = Integer.parseInt(sgaif2.getFrom());
					end = Integer.parseInt(sgaif2.getTo());
					for(; start<=end; start++) {
						SubGroupAnimalsInfoAll sa = new SubGroupAnimalsInfoAll();
						sa.setSubGroup(sgif);
						sa.setSubGroupAnimalsInfo(sgaif2);
						sa.setNo(start);
						sa.setCreatedBy(request.getSession().getAttribute("userName").toString());
						if(start<10) {
							sa.setAnimalNo("TOXX/00"+start);
						}else if(start<100) {
							sa.setAnimalNo("TOXX/0"+start);
						}else {
							sa.setAnimalNo("TOXX/"+start);
						}
						animalInfoAll.add(sa);
					}
				}
			}

		}
		
		sm.setExperimentalDesign("Done");
		sm.setEdDoneBy(userName);
		sm.setEdDoneDate(new Date());
		
		StudyDesignStatus sds = new StudyDesignStatus();
		sds.setCreatedBy(userName);
		sds.setCreatedOn(new Date());
		sds.setStatus(stm);
		sds.setStudyId(sm.getId());
		
		ApplicationAuditDetails apad = new ApplicationAuditDetails();
		apad.setAction("Expermental Design");
		apad.setCreatedBy(userName);
		apad.setCreatedOn(new Date());
		apad.setStudyId(sm);
		apad.setWfsdId(wfsd);
		
		
		String status = expermentalDesignService.saveExpermentalDesign(sm, subGroups, animalInfo, animalInfoAll, sds, apad);
		if(status.trim().equals("success"))
			redirectAttributes.addFlashAttribute("pageMessage", "Experimental Design saved Successfully");
		else
			redirectAttributes.addFlashAttribute("pageError", "Experimental Design Faild to Save");
		return "redirect:/expermentalDesign/";
	}
	
	@RequestMapping(value="/updateExpermentalDesign", method=RequestMethod.POST)
	public String updateExpermentalDesign(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes, @RequestParam("subGroupName")List<String> subNamesList,
			@RequestParam("subGroupDose")List<String> doseList, @RequestParam("subGroupConc")List<String> concList,
			@RequestParam("subGroupCount")List<String> countList,@RequestParam("subGroupFrom")List<String> fromList,
			@RequestParam("subGroupTo")List<String> toList,@RequestParam("newsubGroupName")List<String> newGroupNameList,
			@RequestParam("newsubGroupDose")List<String> newGroupDoseList,@RequestParam("newsubGroupConc")List<String> newGroupConcList,
			@RequestParam("newsubGroupCount")List<String> newGroupCountList,@RequestParam("newsubGroupFrom")List<String> newGroupFromList, 
			@RequestParam("newsubGroupTo")List<String> newGroupToList, @RequestParam("newsubGroupGender")List<String> newGroupgenderList) {
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		String userName = request.getSession().getAttribute("userName").toString();
		String result = expermentalDesignService.updateExpermentalDesignDetails(studyId, userName, studyService, doseList, concList,
				countList, fromList, toList, subNamesList, newGroupNameList,newGroupDoseList,newGroupConcList, newGroupCountList,newGroupFromList,newGroupToList,newGroupgenderList);
		if(result.trim().equals("success"))
			redirectAttributes.addFlashAttribute("pageMessage", "Experimental Design Updated Successfully");
		else
			redirectAttributes.addFlashAttribute("pageError", "Experimental Design Updation Faild. Please Try Again.");
		return "redirect:/expermentalDesign/";
	}
	@RequestMapping(value="/observationConfig2", method=RequestMethod.GET)
	public String observationConfig2(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long subGroupId = Long.parseLong(request.getParameter("sibGroupId").toString());
		SubGroupInfo subGroupInfo = studyService.stdGroupInfo(subGroupId);
		StudyMaster sm =  studyService.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		
		List<Crf> crfList = crfService.findAllActiveCrfs();
		Map<Long, Crf> crfIds = new HashMap<>(); 
		for (Crf crf : crfList) {
			crfIds.put(crf.getId(), crf);
		}
		List<StdSubGroupObservationCrfs> stdcrfs = expermentalDesignService.findAllStdSubGroupObservationCrfs(sm, subGroupId);
		for(StdSubGroupObservationCrfs stdcrf : stdcrfs) {
			crfIds.remove(stdcrf.getCrf().getId());
		}
		crfList = new ArrayList<>();
		for(Map.Entry<Long, Crf> c : crfIds.entrySet()) {
			crfList.add(c.getValue());
		}
		model.addAttribute("sibGroupId", subGroupId);
		model.addAttribute("stdcrfs", crfList);
		
		return "observationConfig2";
	}
	@RequestMapping(value="/observationConfigWithSelection", method=RequestMethod.POST)
	public String observationConfigWithSelection(@RequestParam("crfIds") List<Long> crfids,
			HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long subGroupId = Long.parseLong(request.getParameter("sibGroupId").toString());
//		SubGroupInfo subGroupInfo = studyService.stdGroupInfo(subGroupId);
		StudyMaster sm =  studyService.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		
		List<Crf> crfList = crfService.findAllActiveCrfs();
		Map<Long, Crf> crfIds = new HashMap<>(); 
		for (Crf crf : crfList) {
			if(crfids.contains(crf.getId())) {
				crfIds.put(crf.getId(), crf);
			}
		}
		
		List<StdSubGroupObservationCrfs> stdcrfs = expermentalDesignService.findAllStdSubGroupObservationCrfs(sm, subGroupId);
		for(Map.Entry<Long, Crf> m : crfIds.entrySet()) {
			StdSubGroupObservationCrfs scrf = new StdSubGroupObservationCrfs();
			scrf.setCrf(m.getValue());
			scrf.setActive(false);
			stdcrfs.add(scrf);
		}
		model.addAttribute("sibGroupId", subGroupId);
		model.addAttribute("stdcrfs", stdcrfs);
		
		return "observationConfig2";
	}
	
	
	@RequestMapping(value="/showObservations/{subGroupId}", method=RequestMethod.GET)
	public String showObservations(@PathVariable("subGroupId") Long subGroupId,
			HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		StudyMaster sm =  studyService.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
		SubGroupInfo subGroupInfo = studyService.stdGroupInfo(subGroupId);
		
		try {
			model.addAttribute("configureStatus", subGroupInfo.isCrfConfiguation());
		} catch (Exception e) {
			// TODO: handle exception
			model.addAttribute("configureStatus", false);
		}
		
		List<Crf> crfList = crfService.findAllActiveCrfs();
		Map<Long, Crf> crfIds = new HashMap<>(); 
		for (Crf crf : crfList) {
			crfIds.put(crf.getId(), crf);
		}
		
		model.addAttribute("crfList", crfList);
		List<StdSubGroupObservationCrfs> stdcrfs = expermentalDesignService.findAllStdSubGroupObservationCrfs(sm, subGroupId);
		for(StdSubGroupObservationCrfs stdcrf : stdcrfs) {
			crfIds.remove(stdcrf.getCrf().getId());
		}
		model.addAttribute("crfIds", crfIds);
		for(Map.Entry<Long, Crf> m : crfIds.entrySet()) {
			StdSubGroupObservationCrfs scrf = new StdSubGroupObservationCrfs();
			scrf.setCrf(m.getValue());
			scrf.setActive(false);
			stdcrfs.add(scrf);
		}
		
		model.addAttribute("sibGroupId", subGroupId);
		model.addAttribute("stdcrfs", stdcrfs);
		return "pages/studyDesign/showObservations";
	}
	

	
	@RequestMapping(value="/observationConfig", method=RequestMethod.GET)
	public String observationConfig(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		ExpermentalDto edto = expermentalDesignService.getExpermentalDtoDetails(studyId);
		List<GroupInfo> gi = expermentalDesignService.studyGroupInfoWithChaild(edto.getSm());
		model.addAttribute("study", edto.getSm());
		model.addAttribute("sds", edto.getSds());
		model.addAttribute("group", gi);
		return "observationConfig";
	}
	
	@RequestMapping(value="/createExpermentalDesignEFrom", method=RequestMethod.POST)
	public String createExpermentalDesignEFrom(@RequestParam("studyId") Long studyId, HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		List<Crf> crfList = crfService.findAllActiveCrfsConfigurationForTreatment();
		StudyMaster sm =  studyService.findByStudyId(studyId);
		Long subGroupId = Long.parseLong(request.getParameter("sibGroupId").toString());
		List<StdSubGroupObservationCrfs> stdcrfs = expermentalDesignService.findAllActiveStdSubGroupObservationCrfs(sm, subGroupId);
		Map<Long, StdSubGroupObservationCrfs> sadMap = new HashMap<>();
		stdcrfs.forEach((tcrf) ->{
			sadMap.put(tcrf.getCrf().getId(), tcrf);
		});
		model.addAttribute("sadMap", sadMap);
		model.addAttribute("studyId", studyId);
		model.addAttribute("crfList", crfList);
		model.addAttribute("sibGroupId", subGroupId);
		return "createExpermentalDesignEFrom";
	}
//	@RequestMapping(value="/createExpermentalDesignEFrom", method=RequestMethod.POST)
//	public String createExpermentalDesignEFrom(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
//		StudyMaster sm =  studyService.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
//		StudyDesignStatus sds = expermentalDesignService.getStudyDesignStatusRecord(sm.getId());
//		Long subGroupId = Long.parseLong(request.getParameter("sibGroupId").toString());
//		SubGroupInfo subGroupInfo = studyService.stdGroupInfo(subGroupId);
//		try {
//			model.addAttribute("configureStatus", subGroupInfo.isCrfConfiguation());
//		} catch (Exception e) {
//			// TODO: handle exception
//			model.addAttribute("configureStatus", false);
//		}
//		
//		List<Crf> crfList = crfService.findAllActiveCrfsConfigurationForObservation();
//		Map<Long, Crf> crfIds = new HashMap<>(); 
//		for (Crf crf : crfList) {
//			crfIds.put(crf.getId(), crf);
//		}
//		
//		List<StdSubGroupObservationCrfs> stdcrfs = expermentalDesignService.findAllStdSubGroupObservationCrfs(sm, subGroupId);
//		for(StdSubGroupObservationCrfs stdcrf : stdcrfs) {
//			crfIds.remove(stdcrf.getCrf().getId());
//		}
//		for(Map.Entry<Long, Crf> m : crfIds.entrySet()) {
//			StdSubGroupObservationCrfs scrf = new StdSubGroupObservationCrfs();
//			scrf.setCrf(m.getValue());
//			scrf.setActive(false);
//			stdcrfs.add(scrf);
//		}
//		List<DepartmentMaster> dmList = studyService.getDepartmentMasterList();
//		model.addAttribute("sds", sds);
//		model.addAttribute("dmList", dmList);
//		model.addAttribute("status", sm.getStatus().getStatusDesc());
//		model.addAttribute("sibGroupId", subGroupId);
//		model.addAttribute("stdcrfs", stdcrfs);
//		model.addAttribute("PageHedding", "Config CRF");
//		model.addAttribute("activeUrl", "buildStdyCrf/configureCrfsToStudy");
//		return "createExpermentalDesignEFrom";
//	}
	@RequestMapping(value="/createExpermentalDesignEFromGet/{sibGroupId}", method=RequestMethod.GET)
	public String createExpermentalDesignEFromGet(@PathVariable("sibGroupId") Long subGroupId, HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		StudyMaster sm =  studyService.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
//		Long subGroupId = Long.parseLong(request.getParameter("sibGroupId").toString());
		SubGroupInfo subGroupInfo = studyService.stdGroupInfo(subGroupId);
		try {
			model.addAttribute("configureStatus", subGroupInfo.isCrfConfiguation());
		} catch (Exception e) {
			// TODO: handle exception
			model.addAttribute("configureStatus", false);
		}
		
		List<Crf> crfList = crfService.findAllActiveCrfs();
		Map<Long, Crf> crfIds = new HashMap<>(); 
		for (Crf crf : crfList) {
			crfIds.put(crf.getId(), crf);
		}
		List<DepartmentMaster> dmList = studyService.getDepartmentMasterList();
		List<StdSubGroupObservationCrfs> stdcrfs = expermentalDesignService.findAllStdSubGroupObservationCrfs(sm, subGroupId);
		for(StdSubGroupObservationCrfs stdcrf : stdcrfs) {
			crfIds.remove(stdcrf.getCrf().getId());
		}
		for(Map.Entry<Long, Crf> m : crfIds.entrySet()) {
			StdSubGroupObservationCrfs scrf = new StdSubGroupObservationCrfs();
			scrf.setCrf(m.getValue());
			scrf.setActive(false);
			stdcrfs.add(scrf);
		}
		model.addAttribute("dmList", dmList);
		model.addAttribute("status", sm.getStatus().getStatusDesc());
		model.addAttribute("sibGroupId", subGroupId);
		model.addAttribute("stdcrfs", stdcrfs);
		model.addAttribute("PageHedding", "Config CRF");
		model.addAttribute("activeUrl", "buildStdyCrf/configureCrfsToStudy");
		return "createExpermentalDesignEFrom";
	}
	
	@RequestMapping(value = "/saveconfigureCrfs", method = RequestMethod.POST)
	public String saveconfigureCrfs(ModelMap model,  
			 @RequestParam("studyId")Long studyId, @RequestParam("crfIds")List<Long> crfIds,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long sibGroupId = Long.parseLong(request.getParameter("sibGroupId").toString());
		String username = request.getSession().getAttribute("userName").toString();

		Map<Long, String> type = new HashMap<>();
		Map<Long, String> days = new HashMap<>();
		Map<Long, String> window = new HashMap<>();
		Map<Long, Integer> windowPeriod = new HashMap<>();
		for(Long crfId : crfIds) {
			type.put(crfId, request.getParameter("dayType_"+crfId));
			if(!request.getParameter("dayOrWeek_"+crfId).trim().equals(""))
				days.put(crfId, request.getParameter("dayOrWeek_"+crfId));
			else
				days.put(crfId, "0");
			window.put(crfId, request.getParameter("sign_"+crfId));
			if(!request.getParameter("window_"+crfId).trim().equals(""))
				windowPeriod.put(crfId, Integer.parseInt(request.getParameter("window_"+crfId)));
			else
				windowPeriod.put(crfId, 0);
		}
		
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		try {
			crfService.copyLibCrfToStudy(sm, username, crfIds, type, days, sibGroupId, null, window, windowPeriod);
			redirectAttributes.addFlashAttribute("pageMessage", "Observations Configured Successfully.");
		}catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("pageMessage", "Faild to Configure Observations.");
		}
		
//		http://localhost:9080/TOX/expermentalDesign/observationConfig2?sibGroupId=68
//			return "redirect:/expermentalDesign/observationConfig2?sibGroupId="+sibGroupId;
//		return "redirect:/expermentalDesign/createExpermentalDesignEFrom/"+sibGroupId; 
		return "redirect:/expermentalDesign/createExpermentalDesignEFromGet/"+sibGroupId; 

	}
	
	@RequestMapping(value="/createExpermentalDesignEFrom/{id}", method=RequestMethod.GET)
	public String createExpermentalDesignEFromget(@PathVariable("id") Long subGroupId,
			HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		StudyMaster sm =  studyService.findByStudyId(Long.parseLong(request.getSession().getAttribute("studyDesignId").toString()));
		SubGroupInfo subGroupInfo = studyService.stdGroupInfo(subGroupId);
		try {
			model.addAttribute("configureStatus", subGroupInfo.isCrfConfiguation());
		} catch (Exception e) {
			// TODO: handle exception
			model.addAttribute("configureStatus", false);
		}
		
		List<Crf> crfList = crfService.findAllActiveCrfs();
		Map<Long, Crf> crfIds = new HashMap<>(); 
		for (Crf crf : crfList) {
			crfIds.put(crf.getId(), crf);
		}
		
		List<StdSubGroupObservationCrfs> stdcrfs = expermentalDesignService.findAllStdSubGroupObservationCrfs(sm, subGroupId);
		for(StdSubGroupObservationCrfs stdcrf : stdcrfs) {
			crfIds.remove(stdcrf.getCrf().getId());
		}
		for(Map.Entry<Long, Crf> m : crfIds.entrySet()) {
			StdSubGroupObservationCrfs scrf = new StdSubGroupObservationCrfs();
			scrf.setCrf(m.getValue());
			scrf.setActive(false);
			stdcrfs.add(scrf);
		}
		
		model.addAttribute("sm", sm);
		model.addAttribute("studyId", sm.getId());
		model.addAttribute("status", sm.getStatus().getStatusDesc());
		model.addAttribute("sibGroupId", subGroupId);
		model.addAttribute("stdcrfs", stdcrfs);
//		return "createExpermentalDesignEFrom";
		return "pages/studyDesign/createExpermentalDesignEFrom";
	}
}
