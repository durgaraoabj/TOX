package com.springmvc.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfGroup;
import com.covide.crf.dto.CrfGroupElement;
import com.covide.crf.dto.CrfSection;
import com.covide.crf.dto.CrfSectionElement;
import com.covide.crf.dto.DataSet;
import com.covide.crf.dto.DataSetPhase;
import com.covide.crf.dto.DataSetPhasewiseCrfs;
import com.covide.crf.service.CrfService;
import com.covide.crf.service.DatasetService;
import com.springmvc.model.PeriodCrfs;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.service.StudyService;

@Controller
@RequestMapping("/extractData")
public class DatasetController {
	
	@Autowired
	StudyService studyService;
	
	@Autowired
	CrfService crfSer;
	
	@Autowired
	DatasetService datasetSer;
	
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String crfHomePage(HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("sm", studyService.findByStudyId(activeStudyId));
		model.addAttribute("datasets", crfSer.findAllDataSets(sm));
		
		model.addAttribute("PageHedding", "Administration");
		model.addAttribute("activeUrl", "administration/");
		return "extractData.tiles";
	}
	
	@RequestMapping(value="/changeStatus/{dataSetId}", method=RequestMethod.GET)
	public String changeStatus(@PathVariable("dataSetId") Long id,
			HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes,
			HttpSession session) {
		DataSet dataSet = crfSer.dataSet(id);
		if(dataSet.getStatus().equals("active"))
			dataSet.setStatus("In-Active");
		else
			dataSet.setStatus("active");
		dataSet.setUpdatedBy(request.getSession().getAttribute("userName").toString());
		dataSet.setUpdatedOn(new Date());
		try {
			crfSer.updateDataSet(dataSet);
			redirectAttributes.addFlashAttribute("pageMessage", dataSet.getName() + " Status Changed Successfully.");
		}catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("pageMessage", dataSet.getName() + "Faild to Change Status.");
		}
		return "redirect:/extractData/";
	}
	
	
	@RequestMapping(value="/datasetCreationPage", method=RequestMethod.GET)
	public String datasetCreationPage(ModelMap model, 
			HttpServletRequest request,
			RedirectAttributes redirectAttributes,
			HttpSession session) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		List<StudyPeriodMaster> phases = studyService.allStudyPeriods(sm);
		model.addAttribute("phases", phases);
		if(model.containsKey("addMoreElemets")) {
			model.addAttribute("name", session.getAttribute("name"));
		}
		
		model.addAttribute("PageHedding", "Extract Data");
		model.addAttribute("activeUrl", "extractData/");
		return "datasetCreationPage.tiles";

	}	
	
	@RequestMapping(value="/datasetCreationPage2", method=RequestMethod.GET)
	public String datasetCreationPage2(ModelMap model, 
			HttpServletRequest request,
			RedirectAttributes redirectAttributes,
			HttpSession session) {
		session.removeAttribute("phaseinfo");
		session.removeAttribute("name");
		session.removeAttribute("desc");
		session.removeAttribute("itemStatus");
		session.removeAttribute("metaDataVersion");
		session.removeAttribute("metaDataVersionName");
		session.removeAttribute("studyODMIdName");
		session.removeAttribute("previousMetaDataVersionName");
		session.removeAttribute("phases");
		session.removeAttribute("crf");
		session.removeAttribute("secchecklist"); 
		session.removeAttribute("gropchecklist");
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		List<StudyPeriodMaster> phases = studyService.allStudyPeriods(sm);
		model.addAttribute("phases", phases);
		if(model.containsKey("addMoreElemets")) {
			model.addAttribute("name", session.getAttribute("name"));
		}
		
		model.addAttribute("PageHedding", "Extract Data");
		model.addAttribute("activeUrl", "extractData/");
		return "datasetCreationPage.tiles";

	}	
	@RequestMapping(value="/crflist/{periodId}", method=RequestMethod.GET)
	public String crflist(@PathVariable("periodId") Long id,ModelMap model, 
			HttpServletRequest request) {
		StudyPeriodMaster sp = studyService.studyPeriodMaster(id);
		List<PeriodCrfs> periodCrf = studyService.periodCrfList(sp);
		model.addAttribute("periodCrf", periodCrf);
		return "pages/study/period/periodCrfsSelect";
	}
	
	@RequestMapping(value="/crfelementsCheckBoxList/{phaseid}/{crfelements}", method=RequestMethod.GET)
	public String crfelementsCheckBoxList(
			@PathVariable("phaseid") Long phaseid,
			@PathVariable("crfelements") Long id,ModelMap model, 
			HttpServletRequest request, HttpSession session) {
		List<DataSetPhaseTemp> temp = (List<DataSetPhaseTemp>) session.getAttribute("phaseinfo");
		Set<Long> secEleIds = new HashSet<>();
		Set<Long> groupEleIds = new HashSet<>();
		
		if(temp != null) {
			for(DataSetPhaseTemp dp : temp) {
				if(dp.getPhaseid().longValue() == phaseid.longValue()) {
					Set<Long> secEleIds2 = dp.getSectionEle().get(id);
					Set<Long> groupEleIds2 = dp.getGroupEle().get(id);
					if(secEleIds2 != null) secEleIds = secEleIds2;
					if(groupEleIds2 != null) groupEleIds = groupEleIds2;
				}
			}
		}
		
		Crf crf = crfSer.getCrfForView(id);
		List<CrfSectionElement> secEleList = new ArrayList<CrfSectionElement>();
		List<CrfGroupElement> groupEleList = new ArrayList<CrfGroupElement>();
		for(CrfSection sec : crf.getSections()) {
			if(sec.getElement() != null || sec.getElement().size() >= 0) {
				for(CrfSectionElement e : sec.getElement()) {
					if(!e.getType().equals("non")) {
						if(secEleIds.contains(e.getId())) 
//							//sswamy e.setSeclectionStatus(true);
						secEleList.add(e);
					}
				}
			}	
			CrfGroup group = sec.getGroup();
			if(group != null) {
				if(group.getElement() != null || group.getElement().size() > 0) {
					for(CrfGroupElement e : group.getElement()) {
						if(!e.getType().equals("non")) {
							if(groupEleIds.contains(e.getId())) 
//								e.setSeclectionStatus(true);
							e.setSection(sec);
							groupEleList.add(e);
						}
					}
				}
			}
		}
		model.addAttribute("crf", crf);
		model.addAttribute("secEleList", secEleList);
		model.addAttribute("groupEleList", groupEleList);
		return "pages/study/crfelementsCheckBoxList";
	}
	
	@RequestMapping(value = "/addMoreElemets", method = RequestMethod.POST)
	public String addMoreElemets(ModelMap model, @RequestParam String crfIds, 
			HttpServletRequest request, RedirectAttributes redirectAttributes,
			HttpSession session) {
		session.setAttribute("name", request.getParameter("name1"));
		session.setAttribute("desc", request.getParameter("desc1"));
		session.setAttribute("itemStatus", request.getParameter("itemStatus1"));
		session.setAttribute("metaDataVersion", request.getParameter("metaDataVersion1"));
		session.setAttribute("metaDataVersionName", request.getParameter("metaDataVersionName1"));
		session.setAttribute("studyODMIdName", request.getParameter("studyODMIdName1"));
		session.setAttribute("previousMetaDataVersionName", request.getParameter("previousMetaDataVersionName1"));
		
		
		List<DataSetPhaseTemp> temp = (List<DataSetPhaseTemp>) session.getAttribute("phaseinfo");
		session.removeAttribute("phaseinfo");
		List<DataSetPhaseTemp> tempphlist =  new ArrayList<>();
		if(temp == null) {
			DataSetPhaseTemp tempph = new DataSetPhaseTemp();
			tempph.setPhaseid(Long.parseLong(request.getParameter("phases1")));
			if(request.getParameter("secchecklist") != null) {
				Map<Long, Set<Long>> secEleMap = new HashMap<>();
				Set<Long> secEle = new HashSet<>();
				try {
				String[] sr = request.getParameter("secchecklist").split(",");
				for(String s : sr) {
					if(!s.trim().equals("")) 
					secEle.add(Long.parseLong(s.trim()));
				}
				secEleMap.put(Long.parseLong(request.getParameter("crf1")), secEle);
				}catch (Exception e) {
					// TODO: handle exception
				}
				tempph.setSectionEle(secEleMap);
			}
			if(request.getParameter("gropchecklist") != null) {
				Map<Long, Set<Long>> gropEleMap = new HashMap<>();
				Set<Long> gropEle = new HashSet<>();
				try {
				String[] sr = request.getParameter("gropchecklist").split(",");
				for(String s : sr) {
					if(!s.trim().equals("")) 
					gropEle.add(Long.parseLong(s.trim()));
				}
				gropEleMap.put(Long.parseLong(request.getParameter("crf1")), gropEle);
				}catch (Exception e) {
					// TODO: handle exception
				}
				tempph.setGroupEle(gropEleMap);
			}
			tempphlist.add(tempph);
			
		}else {
			DataSetPhaseTemp tempph = null;
			for(DataSetPhaseTemp temds : temp) {
				if(temds.getPhaseid().equals(Long.parseLong(request.getParameter("phases1")))) {
					tempph = temds;
				}else tempphlist.add(temds);
			}
			if(tempph == null) {
				tempph = new DataSetPhaseTemp();
				tempph.setPhaseid(Long.parseLong(request.getParameter("phases1")));
				if(request.getParameter("secchecklist") != null) {
					Map<Long, Set<Long>> secEleMap = new HashMap<>();
					Set<Long> secEle = new HashSet<>();
					String[] sr = request.getParameter("secchecklist").split(",");
					for(String s : sr) {
						if(!s.trim().equals("")) 
						secEle.add(Long.parseLong(s.trim()));
					}
					secEleMap.put(Long.parseLong(request.getParameter("crf1")), secEle);
					tempph.setSectionEle(secEleMap);
				}
				if(request.getParameter("gropchecklist") != null) {
					Map<Long, Set<Long>> gropEleMap = new HashMap<>();
					Set<Long> gropEle = new HashSet<>();
					String[] sr = request.getParameter("gropchecklist").split(",");
					for(String s : sr) {
						if(!s.trim().equals("")) 
							gropEle.add(Long.parseLong(s.trim()));
					}
					gropEleMap.put(Long.parseLong(request.getParameter("crf1")), gropEle);				
					tempph.setGroupEle(gropEleMap);
				}
				tempphlist.add(tempph);
			}else {
				Map<Long, Set<Long>> secEleMap = tempph.getSectionEle();
				if(request.getParameter("secchecklist") != null) {
					Set<Long> secEle = new HashSet<>();
					String[] sr = request.getParameter("secchecklist").split(",");
					for(String s : sr) {
						try {
						secEle.add(Long.parseLong(s.trim()));
						}catch (Exception e) {
							// TODO: handle exception
						}
					}
					try {
					secEleMap.put(Long.parseLong(request.getParameter("crf1")), secEle);
					}catch (Exception e) {
						// TODO: handle exception
					}
					tempph.setSectionEle(secEleMap);
				}else	tempph.setSectionEle(new HashMap<>());
				
				Map<Long, Set<Long>> gropEleMap = tempph.getGroupEle();
				if(request.getParameter("gropchecklist") != null) {
					Set<Long> gropEle = new HashSet<>();
					String[] sr = request.getParameter("gropchecklist").split(",");
					for(String s : sr) {
						try {
						gropEle.add(Long.parseLong(s.trim()));
						}catch (Exception e) {
							// TODO: handle exception
						}
					}
					try {
					gropEleMap.put(Long.parseLong(request.getParameter("crf1")), gropEle);
					}catch (Exception e) {
						// TODO: handle exception
					}
					tempph.setGroupEle(gropEleMap);
				}else	tempph.setGroupEle(new HashMap<>());
			}
			tempphlist.add(tempph);
		}
		session.setAttribute("phaseinfo", tempphlist);
		
		
		String details = request.getParameter("phases1");
		details = details + "#" + request.getParameter("crf1");
		details = details + "#" + request.getParameter("secchecklist") + "#" + request.getParameter("gropchecklist");
		String olddetails = (String) session.getAttribute("details");
		if(olddetails != null) olddetails = olddetails + "@" + details;
		else olddetails = details;
		session.setAttribute("details", olddetails);
		session.setAttribute("phases", request.getParameter("phases1"));
		session.setAttribute("crf", request.getParameter("crf1"));
		session.setAttribute("secchecklist", request.getParameter("secchecklist"));
		session.setAttribute("gropchecklist", request.getParameter("gropchecklist"));
		
		redirectAttributes.addFlashAttribute("name", request.getParameter("name1"));
		redirectAttributes.addFlashAttribute("desc", request.getParameter("desc1"));
		redirectAttributes.addFlashAttribute("itemStatus", request.getParameter("itemStatus1"));
		redirectAttributes.addFlashAttribute("metaDataVersion", request.getParameter("metaDataVersion1"));
		redirectAttributes.addFlashAttribute("metaDataVersionName", request.getParameter("metaDataVersionName1"));
		redirectAttributes.addFlashAttribute("studyODMIdName", request.getParameter("studyODMIdName1"));
		redirectAttributes.addFlashAttribute("previousMetaDataVersionName", request.getParameter("previousMetaDataVersionName1"));
		redirectAttributes.addFlashAttribute("phases", request.getParameter("phases1"));
		redirectAttributes.addFlashAttribute("secchecklist", request.getParameter("secchecklist"));		
		redirectAttributes.addFlashAttribute("gropchecklist", request.getParameter("gropchecklist"));
		redirectAttributes.addFlashAttribute("addMoreElemets", "addMoreElemets");
		
		if(request.getParameter("saveOrAdd").equals("saveDataSet")) {
			DataSet ds = new DataSet();
			ds.setName(request.getParameter("name1"));
			ds.setDescription(request.getParameter("desc1"));
			ds.setItemStatus(request.getParameter("itemStatus1"));
			ds.setMetaDataVersion(request.getParameter("metaDataVersion1"));
			ds.setMetaDataVersionName(request.getParameter("metaDataVersionName1"));
			ds.setStudyODMIdName(request.getParameter("studyODMIdName1"));
			ds.setPreviousMetaDataVersionName(request.getParameter("previousMetaDataVersionName1"));
			ds.setCreatedOn(new Date());
			ds.setCreatedBy(request.getSession().getAttribute("userName").toString());
			Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
			StudyMaster sm = studyService.findByStudyId(activeStudyId);
			ds.setStudy(sm);
			
			List<DataSetPhase> phases = new ArrayList<>();
			for(DataSetPhaseTemp dspt : tempphlist) {
				DataSetPhase phase = new  DataSetPhase();
				phase.setDataSetId(ds);
				phase.setPhase(studyService.studyPeriodMaster(dspt.getPhaseid()));
				
				
				Map<Long, DataSetPhasewiseCrfs> map = new HashMap<>();
				Map<Long, Set<Long>> secEleMap = dspt.getSectionEle();
				for(Map.Entry<Long, Set<Long>> m : secEleMap.entrySet()) {
					DataSetPhasewiseCrfs dspwc = new DataSetPhasewiseCrfs();
					dspwc.setPhasesId(phase);
					dspwc.setCrf(crfSer.studyCrf(m.getKey()));
					StringBuilder sb = new StringBuilder();
					boolean flag = true;
					for(Long l : m.getValue()) 
						if(flag) {
							sb.append(l); flag = false;
						}else	sb.append(",").append(l);
					dspwc.setSecEleIds(sb.toString());
					map.put(m.getKey(), dspwc);
				}
				
				Map<Long, Set<Long>> groupEleMap = dspt.getGroupEle();
				for(Map.Entry<Long, Set<Long>> m : groupEleMap.entrySet()) {
					DataSetPhasewiseCrfs dspwc = null;
					if(map.containsKey(m.getKey()))
						dspwc = map.get(m.getKey());
					else dspwc = new DataSetPhasewiseCrfs();
					
					dspwc.setPhasesId(phase);
					dspwc.setCrf(crfSer.studyCrf(m.getKey()));
					StringBuilder sb = new StringBuilder();
					boolean flag = true;
					for(Long l : m.getValue()) 
						if(flag) {
							sb.append(l); flag = false;
						}else	sb.append(",").append(l);
					dspwc.setGroupEleIds(sb.toString());
					map.put(m.getKey(), dspwc);
				}
				
				List<DataSetPhasewiseCrfs> dspwclist = new ArrayList<>();
				for(Map.Entry<Long, DataSetPhasewiseCrfs> m : map.entrySet()) {
					dspwclist.add(m.getValue());
				}
				
				phase.setPhases(dspwclist);
				phases.add(phase);
			}
			ds.setPhases(phases);
			crfSer.saveDataSet(ds);
			redirectAttributes.addFlashAttribute("pageMessage", "Dateset Created Sussfully");
			session.removeAttribute("phaseinfo");
			
			
			
			session.removeAttribute("name");
			session.removeAttribute("desc");
			session.removeAttribute("itemStatus");
			session.removeAttribute("metaDataVersion");
			session.removeAttribute("metaDataVersionName");
			session.removeAttribute("studyODMIdName");
			session.removeAttribute("previousMetaDataVersionName");
			session.removeAttribute("phases");
			session.removeAttribute("crf");
			session.removeAttribute("secchecklist");
			session.removeAttribute("gropchecklist");
			return "redirect:/extractData/";
		}else
			return "redirect:/extractData/datasetCreationPage";
	}
	
	
	@RequestMapping(value="/excelExport/{dataSetId}", method=RequestMethod.GET)
	public void excelExport(@PathVariable("dataSetId") Long id,
			HttpServletRequest request,
			HttpServletResponse response,
			ModelMap model,
			RedirectAttributes redirectAttributes,
			HttpSession session) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy");
		String dateinSting = sdf.format(new  Date());
		DataSet dataSet = crfSer.fullDataSetInfo(id);
		try {
			File file = datasetSer.exportDataToExcel(dataSet, request);
			
			response.setContentType("application/vnd.ms-excel");
	        response.addHeader("Content-Disposition", "attachment; filename="+dataSet.getName()+"_" +dateinSting+".xlsx");
	        response.setContentLength((int)file.length());
	            
	        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
	     
	        //Copy bytes from source to destination(outputstream in this example), closes both streams.
	        FileCopyUtils.copy(inputStream, response.getOutputStream());
	            
	        redirectAttributes.addFlashAttribute("pageMessage", dataSet.getName() + " Exported Successfully.");
		}catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("pageMessage", dataSet.getName() + " Export Faild.");
		}
		
//		return "redirect:/extractData/";
	}
	
}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
