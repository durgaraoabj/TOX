package com.springmvc.controllers;

import java.util.ArrayList;
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

import com.covide.crf.dto.CRFGroupItem;
import com.covide.crf.dto.CRFGroupItemStd;
import com.covide.crf.dto.CRFItemValues;
import com.covide.crf.dto.CRFItemValuesStd;
import com.covide.crf.dto.CRFSections;
import com.covide.crf.dto.CRFSectionsStd;
import com.covide.crf.dto.CrfItems;
import com.covide.crf.dto.CrfItemsStd;
import com.covide.crf.dto.CrfMetaData;
import com.covide.crf.dto.CrfMetaDataStd;
import com.covide.crf.service.CrfService;
import com.springmvc.dao.EmployeeDao;
import com.springmvc.model.PeriodCrfs;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.model.VolunteerPeriodCrfStatus;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;
import com.springmvc.service.UserWiseStudiesAsignService;

@Controller
@RequestMapping("/study")
public class BuildStudyController {

	@Autowired
	StudyService studyService;

	@Autowired
	UserService userService;

	@Autowired
	UserWiseStudiesAsignService userWiseStudiesAsignService;

	@Autowired
	CrfService crfService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String adminHomePage(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			String currentStudy = request.getSession().getAttribute("activeStudyNo").toString();
			if (currentStudy != null && !currentStudy.equals(""))
				return "studyDashBoard.tiles";
			else {
				redirectAttributes.addFlashAttribute("pageWarning", "Please select study");
				return "redirect:/dashboard/userWiseActiveStudies";
			}
		} catch (NullPointerException npe) {
			redirectAttributes.addFlashAttribute("pageWarning", "Please select study");
			return "redirect:/dashboard/userWiseActiveStudies";
		}
	}

	@Autowired
	EmployeeDao employeeDao;

	@RequestMapping(value = "/configureCrfs", method = RequestMethod.GET)
	public String configureCrfs(ModelMap model, HttpServletRequest request) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		try {
			model.addAttribute("configureStatus", sm.isCrfConfiguation());
		} catch (Exception e) {
			// TODO: handle exception
			model.addAttribute("configureStatus", false);
		}
		List<CrfMetaData> crfs = employeeDao.findAllCrfs();
		System.out.println("crfs.size(): " + crfs.size());
		model.addAttribute("crfs", crfs);

		List<Long> stdcrfids = new ArrayList<>();
		List<CrfMetaDataStd> stdcrfs = employeeDao.findAllStdCrfs(sm);
		for (CrfMetaDataStd crfMetaDataStd : stdcrfs) {
			if (crfMetaDataStd.isActive()) {
				stdcrfids.add(crfMetaDataStd.getCrfmetaId());
			}
		}
		for (CrfMetaData crfMetaData : crfs) {
			if (stdcrfids.contains(crfMetaData.getId())) {
				crfMetaData.setConfigure(true);
			}
		}
		model.addAttribute("status", sm.getStatus().getStatusDesc());
		model.addAttribute("stdcrfids", stdcrfids);
//		List<StudyMaster> allStudies = studyService.findAll();
//		model.addAttribute("allStudies", allStudies);
		return "configureCrfs.tiles";
	}

	@RequestMapping(value = "/saveconfigureCrfs", method = RequestMethod.POST)
	public String saveconfigureCrfs(ModelMap model, @RequestParam String crfIds, HttpServletRequest request) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		String[] ids = crfIds.split(",");
		List<Integer> cids = new ArrayList<>();
		for (String s : ids)
			if (s.trim() != "")
				cids.add(Integer.parseInt(s.trim()));
		
		crfService.copyLibCrfToStudy(sm, cids);
		
//		List<CrfMetaData> crfs = employeeDao.findAllCrfsAll();
//		
//
//		List<CrfMetaDataStd> stdcrfs = employeeDao.findAllStdCrfs(sm);
//		List<CrfMetaDataStd> stdcrfsUpdate = new ArrayList<>();
//
//		System.out.println(cids);
//
//		List<Integer> cidsrec = new ArrayList<>();
//		for (CrfMetaDataStd stdc : stdcrfs) {
//			System.out.println(stdc.getCrfmetaId());
//			System.out.println(stdc.isActive());
//			if (cids.contains(stdc.getCrfmetaId()) && stdc.isActive()) {
//				// no need to update
//				if (!stdc.isActive()) {
//					// update required
//					stdc.setActive(true);
//					stdcrfsUpdate.add(stdc);
//				}
//			} else {
//				// update required
//				stdc.setActive(false);
//				stdcrfsUpdate.add(stdc);
//			}
//
//			cidsrec.add(stdc.getCrfmetaId());
//		}
//
//		List<CrfMetaDataStd> stdcrfssave = new ArrayList<>();
//		for (CrfMetaData crf : crfs) {
//			if (cids.contains(crf.getId()) && !cidsrec.contains(crf.getId())) {
//				stdcrfssave.add(copyCrf(crf, sm));
//			}
//		}
////		employeeDao.savestdCrf(stdcrfssave);
//		stdcrfsUpdate.addAll(stdcrfssave);
//		employeeDao.updatestdCrf(stdcrfsUpdate);

		return "redirect:/study/configureCrfs";
	}

	@RequestMapping(value = "/configureCrfsMarkComplete", method = RequestMethod.POST)
	public String configureCrfsMarkComplete(ModelMap model, HttpServletRequest request) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		sm.setCrfConfiguation(true);
//		sm.setCrfPeriodConfiguation(true);
		studyService.updateStudy(sm);

		model.addAttribute("configureStatus", true);
		List<CrfMetaData> crfs = employeeDao.findAllCrfs();
		System.out.println("crfs.size(): " + crfs.size());
		model.addAttribute("crfs", crfs);

		List<Long> stdcrfids = new ArrayList<>();
		List<CrfMetaDataStd> stdcrfs = employeeDao.findAllStdCrfs(sm);
		for (CrfMetaDataStd crfMetaDataStd : stdcrfs) {
			if (crfMetaDataStd.isActive()) {
				stdcrfids.add(crfMetaDataStd.getCrfmetaId());
			}
		}
		for (CrfMetaData crfMetaData : crfs) {
			if (stdcrfids.contains(crfMetaData.getId())) {
				crfMetaData.setConfigure(true);
			}
		}
		model.addAttribute("stdcrfids", stdcrfids);
//		List<StudyMaster> allStudies = studyService.findAll();
//		model.addAttribute("allStudies", allStudies);
		return "configureCrfs.tiles";
	}

	private CrfMetaDataStd copyCrf(CrfMetaData crf, StudyMaster sm) {
		CrfMetaDataStd scrf = new CrfMetaDataStd();
		scrf.setStd(sm);
		scrf.setCrfmetaId(crf.getId());
		scrf.setCode(crf.getCode());
		scrf.setCrfName(crf.getCrfName());
		scrf.setCrfDesc(crf.getCrfDesc());
		scrf.setGender(crf.getGender());
		scrf.setVersion(crf.getVersion());
		scrf.setOrderNo(crf.getOrderNo());
		scrf.setStringCellValue(crf.getStringCellValue());
		employeeDao.savestdCrf1(scrf);
		scrf.setSections(getSectionStd(crf.getSections(), scrf));
		for(CRFSectionsStd s : scrf.getSections()) {
			for(CrfItemsStd i : s.getItemList()) {
				System.out.println(i);
				List<CRFItemValuesStd> irvl = i.getItemResponceValues();			
			}
		}
		employeeDao.updatestdCrf(scrf);
		scrf.setGroups(copyGroupStd(crf.getGroups(), scrf));
		employeeDao.updatestdCrf(scrf);
		return scrf;
	}

	private List<CRFGroupItemStd> copyGroupStd(List<CRFGroupItem> groups, CrfMetaDataStd scrf) {
		// TODO Auto-generated method stub
		List<CRFGroupItemStd> sglist = new ArrayList<>();
		for (CRFGroupItem g : groups) {
			if (g.isActive()) {
				CRFGroupItemStd sg = new CRFGroupItemStd();
				sg.setCrfId(scrf);
				sg.setGroupId(g.getId());
				sg.setGroupName(g.getGroupName());
				sg.setGroupDesc(g.getGroupDesc());
				List<CrfItems> itemList = g.getItemList();
				List<CrfItemsStd> sigl = new ArrayList<>();
				for (CrfItems i : itemList) {
					if (tempitems != null && tempitems.containsKey(i.getId())) {
						CrfItemsStd si = tempitems.get(i.getId());
						si.setGroup(sg);
						sigl.add(si);
					}
				}
				sg.setItemList(sigl);
				sg.setMaxColumns(g.getMaxColumns());
				sg.setDisplayRow(g.getDisplayRow());
				sg.setMaxRow(g.getMaxRow());
				sg.setDataSetTechName(g.getDataSetTechName());
				sg.setDataSetTechValue(g.getDataSetTechValue());
				sg.setStatusUpdate(g.getStatusUpdate());
				sg.setCreatedOn(g.getCreatedOn());
				sg.setUpdatedOn(g.getUpdatedOn());
				sg.setLabel_values(g.getLabel_values());
				sg.setFontSize(g.getFontSize());
				sg.setFontStyle(g.getFontStyle());
				sg.setTextStyle(g.getTextStyle());
				sg.setNoOfRowsDataContains(g.getNoOfRowsDataContains());
				sg.setChangeStatsu(g.getChangeStatsu());
				sg.setGroupValueSizes(g.getGroupValueSizes());
				sg.setTempId(g.getTempId());
				sglist.add(sg);
				employeeDao.savestdCrfGroups1(sg);
			}
		}

//		employeeDao.savestdCrfGroups(sglist);
		return sglist;
	}

	private List<CRFSectionsStd> getSectionStd(List<CRFSections> sections, CrfMetaDataStd scrf) {
		// TODO Auto-generated method stub
		List<CRFSectionsStd> ssections = new ArrayList<>();
		for (CRFSections sec : sections) {
			if (sec.isActive()) {
				CRFSectionsStd s = new CRFSectionsStd();
				s.setCrfId(scrf);
				s.setCrfSectionId(sec.getId());
				s.setSectionName(sec.getSectionName());
				s.setSectionDesc(sec.getSectionDesc());
				s.setMaxRows(sec.getMaxRows());
				s.setMaxColumns(sec.getMaxColumns());
				s.setUserRole(sec.getUserRole());
				s.setGender(sec.getGender());
				s.setOrderNo(sec.getOrderNo());
				s.setContainsGroup(sec.getContainsGroup());

				s.setUserRoleUpdateReason(sec.getUserRoleUpdateReason());
				s.setDataSetTechName(sec.getDataSetTechName());
				s.setDataSetTechValue(sec.getDataSetTechValue());
				s.setStatusUpdate(sec.getStatusUpdate());
				s.setCreatedOn(sec.getCreatedOn());
				s.setUpdatedOn(sec.getUpdatedOn());
				employeeDao.savestdCrfSections1(s);
				s.setItemList(copyItemList(sec.getItemList(), scrf, s));
			}
		}
//		employeeDao.savestdCrfSections(ssections);
		return ssections;
	}

	private Map<Long, CrfItemsStd> tempitems;

	private List<CrfItemsStd> copyItemList(List<CrfItems> itemList, CrfMetaDataStd scrf, CRFSectionsStd s) {
		List<CrfItemsStd> sitems = new ArrayList<>();
		tempitems = new HashMap<>();
		for (CrfItems i : itemList) {
			if (i.isActive()) {
				CrfItemsStd si = new CrfItemsStd();
				si.setCrfItemId(i.getCrfId().getId());
				si.setCrfId(scrf);
				si.setSection(s);
				tempitems.put(i.getId(), si);
				si.setItemName(i.getItemName());
				si.setItemDesc(i.getItemDesc());
				si.setItemDescRight(i.getItemDescRight());
				si.setItemType(i.getItemType());
				si.setResponseValue(i.getResponseValue());
				si.setResponseType(i.getResponseType());
				si.setRowNumber(i.getRowNumber());
				si.setColumnNo(i.getColumnNo());
				si.setValidation(i.getValidation());
				si.setPattern(i.getPattern());

				si.setCount(i.getCount());
				si.setDataSetTechName(i.getDataSetTechName());
				si.setDataSetTechValue(i.getDataSetTechValue());
				si.setType(i.getType());
				si.setIeTestCode(i.getIeTestCode());
				si.setHeader(i.getHeader());
				si.setSubheader(i.getSubheader());
				si.setIsSelected(i.getIsSelected());
				si.setItem_oid(i.getItem_oid());
				si.setStatusUpdate(i.getStatusUpdate());
				si.setCreatedOn(i.getCreatedOn());
				si.setUpdatedOn(i.getUpdatedOn());
				si.setChangeStatsu(i.getChangeStatsu());
				si.setGropItemName(i.getGropItemName());
				si.setTextStyle(i.getTextStyle());
				si.setFontStyle(i.getFontStyle());
				si.setFontSize(i.getFontSize());
				si.setUnderLine(i.getUnderLine());
				si.setPrintDesc(i.getPrintDesc());
				si.setPrintContent(i.getPrintContent());
				si.setDescCellWidth(i.getDescCellWidth());
				si.setValueWithImage(i.getValueWithImage());
				si.setValueCellWidth(i.getValueCellWidth());
				si.setCellHeight(i.getCellHeight());
				si.setCheckAlign(i.getCheckAlign());
				sitems.add(si);
				employeeDao.savestdCrfEle1(si);
				si.setItemResponceValues(copyitemResponceValues(i.getItemResponceValues(), si));
			}
		}
		return sitems;
	}

	private List<CRFItemValuesStd> copyitemResponceValues(List<CRFItemValues> itemResponceValues, CrfItemsStd si) {
		// TODO Auto-generated method stub
		List<CRFItemValuesStd> ivlist = new ArrayList<>();
		for (CRFItemValues v : itemResponceValues) {
			CRFItemValuesStd iv = new CRFItemValuesStd();
			iv.setCrfItemValueId(v);
			iv.setElemenstValue(v.getElemenstValue());
			iv.setCrfItem(si);
			ivlist.add(iv);
		}
		employeeDao.savestdCrfEleVal(ivlist);
		return ivlist;
	}

	@RequestMapping(value = "/configureCrfsToPeriod", method = RequestMethod.GET)
	public String configureCrfsToPeriod(ModelMap model, HttpServletRequest request) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		
		List<StudyPeriodMaster> periods = studyService.allStudyPeriods(sm);
		model.addAttribute("periods", periods);

		return "configureCrfsToPeriod.tiles";
	}

//	@RequestMapping(value = "/configureCrfsPeriod", method = RequestMethod.POST)
//	public String configureCrfsPeriod(ModelMap model, @RequestParam Long periodId, HttpServletRequest request) {
//		if (periodId == null)
//			periodId = pid;
//		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
//		StudyMaster sm = studyService.findByStudyId(activeStudyId);
//		model.addAttribute("stdcrfConfiguation", sm.isCrfConfiguation());
//
////		sm.setCrfConfiguation(true);
////		studyService.updateStudy(sm);
//		StudyPeriodMaster sp = studyService.studyPeriodMaster(periodId);
//		model.addAttribute("crfConfiguation", sp.isCrfConfiguation());
//		model.addAttribute("periodName", sp.getName());
//
//		List<CrfMetaDataStd> stdcrfs = employeeDao.findAllStdCrfs(sm);
//		List<Integer> temp = new ArrayList<>();
//		List<PeriodCrfs> pcrfs = studyService.periodCrfs(periodId);
//		for (PeriodCrfs pc : pcrfs) {
//			if (pc.isActive())
//				temp.add(pc.getCrfId());
//		}
//
//		List<CrfMetaDataStd> stdcrfsp = new ArrayList<>();
//		for (CrfMetaDataStd stdcrf : stdcrfs) {
//			if (temp.contains(stdcrf.getId()))
//				stdcrf.setPeriodCrfstatus(true);
//			else
//				stdcrf.setPeriodCrfstatus(false);
//			stdcrfsp.add(stdcrf);
//		}
//
//		model.addAttribute("periodId", periodId);
//		model.addAttribute("stdcrfsp", stdcrfsp);
//		return "configureCrfsPeriod.tiles";
//	}

	public static Long pid;

//	@RequestMapping(value = "/configureCrfsPeriodSave", method = RequestMethod.POST)
//	public String configureCrfsPeriodSave(ModelMap model, @RequestParam Long periodId, @RequestParam String crfIds,
//			HttpServletRequest request) {
//		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
//		StudyMaster sm = studyService.findByStudyId(activeStudyId);
//		if (!sm.isCrfConfiguation()) {
//			sm.setCrfConfiguation(true);
//			studyService.updateStudy(sm);
//		}
//		StudyPeriodMaster sp = studyService.studyPeriodMaster(periodId);
//
//		List<CrfMetaDataStd> stdcrfs = employeeDao.findAllStdCrfs(sm);
//		String[] ids = crfIds.split(",");
//		List<Integer> cids = new ArrayList<>();
//		for (String s : ids)
//			if (s.trim() != "")
//				cids.add(Integer.parseInt(s.trim()));
//
//		List<Integer> temp = new ArrayList<>();
//		List<PeriodCrfs> pcrfs = studyService.periodCrfs(periodId);
//		List<PeriodCrfs> pcrfsupdate = new ArrayList<>();
//		for (PeriodCrfs pc : pcrfs) {
//			if (cids.contains(pc.getCrfId())) {
//				if (!pc.isActive()) {
//					pc.setActive(true);
//					pcrfsupdate.add(pc);
//				}
//			} else {
//				pc.setActive(false);
//				pcrfsupdate.add(pc);
//			}
//			temp.add(pc.getCrfId());
//		}
//
//		List<PeriodCrfs> pcrfsSave = new ArrayList<>();
//		for (CrfMetaDataStd stdcrf : stdcrfs) {
//			if (cids.contains(stdcrf.getId()) && !temp.contains(stdcrf.getId())) {
//				// create PeriodCrfs
//				PeriodCrfs pcrf = new PeriodCrfs();
//				pcrf.setPeriod(sp);
//				pcrf.setPeriodId(sp.getId());
//				pcrf.setCrfName(stdcrf.getCrfName());
//				pcrf.setCrfId(stdcrf.getId());
//				pcrfsSave.add(pcrf);
//			}
//		}
//
//		studyService.upatePeriodCrfsList(pcrfsupdate);
//		studyService.savePeriodCrfsList(pcrfsSave);
//
//		temp = new ArrayList<>();
//		pcrfs = studyService.periodCrfs(periodId);
//		for (PeriodCrfs pc : pcrfs) {
//			if (pc.isActive())
//				temp.add(pc.getCrfId());
//		}
//
//		List<CrfMetaDataStd> stdcrfsp = new ArrayList<>();
//		for (CrfMetaDataStd stdcrf : stdcrfs) {
//			if (temp.contains(stdcrf.getId()))
//				stdcrf.setPeriodCrfstatus(true);
//			else
//				stdcrf.setPeriodCrfstatus(false);
//			stdcrfsp.add(stdcrf);
//		}
//
//		model.addAttribute("periodId", periodId);
//		model.addAttribute("stdcrfsp", stdcrfsp);
//		return "configureCrfsPeriod.tiles";
//	}

//	@RequestMapping(value = "/configureCrfsMarkCompletePeriod", method = RequestMethod.POST)
//	public String configureCrfsMarkCompletePeriod(ModelMap model, @RequestParam Long periodId,
//			HttpServletRequest request) {
//		if (periodId == null)
//			periodId = pid;
//		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
//		StudyMaster sm = studyService.findByStudyId(activeStudyId);
//		model.addAttribute("stdcrfConfiguation", sm.isCrfConfiguation());
////		sm.setCrfConfiguation(true);
////		studyService.updateStudy(sm);
//		StudyPeriodMaster sp = studyService.studyPeriodMaster(periodId);
//		sp.setCrfConfiguation(true);
//		studyService.upatePeriodCrfs(sp);
//		model.addAttribute("crfConfiguation", true);
//		model.addAttribute("periodName", sp.getName());
//		List<CrfMetaDataStd> stdcrfs = employeeDao.findAllStdCrfs(sm);
//		List<Integer> temp = new ArrayList<>();
//		List<PeriodCrfs> pcrfs = studyService.periodCrfs(periodId);
//		for (PeriodCrfs pc : pcrfs) {
//			if (pc.isActive())
//				temp.add(pc.getCrfId());
//		}
//
//		List<CrfMetaDataStd> stdcrfsp = new ArrayList<>();
//		for (CrfMetaDataStd stdcrf : stdcrfs) {
//			if (temp.contains(stdcrf.getId()))
//				stdcrf.setPeriodCrfstatus(true);
//			else
//				stdcrf.setPeriodCrfstatus(false);
//			stdcrfsp.add(stdcrf);
//		}
//
//		model.addAttribute("periodId", periodId);
//		model.addAttribute("stdcrfsp", stdcrfsp);
//		return "configureCrfsPeriod.tiles";
//	}

	@RequestMapping(value = "/volunteerCreation", method = RequestMethod.GET)
	public String volunteerCreation(ModelMap model, HttpServletRequest request) {

		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		try {
			model.addAttribute("configureStatus", sm.isCrfConfiguation());
		} catch (Exception e) {
			model.addAttribute("configureStatus", false);
		}

		if (sm.isVolConfiguation()) {
			List<Volunteer> vlist = studyService.studyVolunteerList(sm);
			model.addAttribute("vlist", vlist);
			return "volunteerCreationDone.tiles";
		} else
			return "volunteerCreation.tiles";

	}

	@RequestMapping(value = "/volunteerCreationSave", method = RequestMethod.POST)
	public String volunteerCreationSave(ModelMap model, @RequestParam List<Integer> bedNo,
			@RequestParam List<String> volId, @RequestParam List<String> volName, HttpServletRequest request) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);

		List<Volunteer> vlist = new ArrayList<>();
		for (int i = 0; i < bedNo.size(); i++) {
			Volunteer vol = new Volunteer();
			vol.setVolId(volId.get(i));
			vol.setVolName(volName.get(i));
			vol.setBedNo(bedNo.get(i) + "");
			vol.setStudyId(sm.getId());
			vlist.add(vol);
		}
		studyService.saveVolList(vlist);

		List<StudyPeriodMaster> plist = studyService.allStudyPeriods(sm);
		List<VolunteerPeriodCrf> vpclist = new ArrayList<>();
		List<VolunteerPeriodCrfStatus> vpcslist = new ArrayList<>();


		for (StudyPeriodMaster p : plist) {
			List<PeriodCrfs> pcrfs = studyService.periodCrfs(p.getId());
			for (Volunteer v : vlist) {
				VolunteerPeriodCrfStatus vpcs = new VolunteerPeriodCrfStatus();
				vpcs.setPeriod(p);
				vpcs.setVol(v);
				vpcs.setStudyId(sm.getId());
				vpcslist.add(vpcs);
				for (PeriodCrfs pcrf : pcrfs) {
					VolunteerPeriodCrf vpc = new VolunteerPeriodCrf();
					vpc.setVol(v);
					vpc.setStdCrf(pcrf);
					vpc.setPeriod(p);
					vpc.setStudyId(sm.getId());
					vpc.setExitCrf(pcrf.getExitCrf());
					vpclist.add(vpc);
				}
			}
		}
		studyService.saveVolunteerPeriodCrfStatusList(vpcslist);
		studyService.saveVolunteerPeriodCrf(vpclist);
		sm.setVolConfiguation(true);
		studyService.updateStudy(sm);

		model.addAttribute("study", sm);
		System.out.println(sm);
		try {
			model.addAttribute("configureStatus", sm.isCrfConfiguation());
		} catch (Exception e) {
			model.addAttribute("configureStatus", false);
		}
		return "redirect:/study/volunteerCreation";
	}
}
