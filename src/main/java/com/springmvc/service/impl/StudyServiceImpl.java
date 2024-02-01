package com.springmvc.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.covide.crf.dto.CrfDescrpencyAudit;
import com.covide.crf.dto.CrfMetaDataStd;
import com.covide.crf.dto.ObservationClinPathTestCodesDto;
import com.covide.crf.dto.StagoDataDto;
import com.covide.crf.dto.VistrosDataDto;
import com.covide.dto.SysmexDataDto;
import com.covide.enums.StatusMasterCodes;
import com.covide.template.dto.PdfDataDto;
import com.covide.template.dto.PdfHeadderDto;
import com.covide.template.dto.StagoDataAnimalDto;
import com.covide.template.dto.StudyMasterDto;
import com.covide.template.dto.SysmexDataUpdateDto;
import com.covide.template.dto.SysmexDto;
import com.covide.template.dto.SysmexRowiseDto;
import com.covide.template.dto.SysmexTestCodeDataDto;
import com.covide.template.dto.SysmexTestCodePdfData;
import com.covide.template.dto.VistrosDataUpdateDto;
import com.covide.template.dto.VitrosTestCodeDataDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.springmvc.audittrail.AuditToken;
import com.springmvc.controllers.TestWebSocket;
import com.springmvc.dao.AcclimatizationDao;
import com.springmvc.dao.InstrumentDao;
import com.springmvc.dao.StatusDao;
import com.springmvc.dao.StudyDao;
import com.springmvc.dao.UserDao;
import com.springmvc.dao.UserWiseStudiesAsignDao;
import com.springmvc.dao.impl.GroupingDao;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.DepartmentMaster;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.ObservationInturmentConfiguration;
import com.springmvc.model.PeriodCrfs;
import com.springmvc.model.PhysicalWeightBalanceData;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.Species;
import com.springmvc.model.SponsorMaster;
import com.springmvc.model.StagoData;
import com.springmvc.model.StaticData;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyAcclamatizationDates;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyGroup;
import com.springmvc.model.StudyGroupClass;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyMasterLog;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.StudySite;
import com.springmvc.model.StudySitePeriodCrfs;
import com.springmvc.model.StudySiteSubject;
import com.springmvc.model.StudySiteSubjectGroup;
import com.springmvc.model.StudyStatusAudit;
import com.springmvc.model.StudyTestCodes;
import com.springmvc.model.StudyTreatmentDataDates;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.SubjectStatus;
import com.springmvc.model.SysmexData;
import com.springmvc.model.SysmexTestCode;
import com.springmvc.model.SysmexTestCodeData;
import com.springmvc.model.TestCode;
import com.springmvc.model.TestCodeProfile;
import com.springmvc.model.TestCodeProfileParameters;
import com.springmvc.model.UserWiseSitesAsignMaster;
import com.springmvc.model.UserWiseStudiesAsignMaster;
import com.springmvc.model.VitrosData;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.model.VolunteerPeriodCrfStatus;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;

@Service("studyService")
@PropertySource(value = { "classpath:application.properties" })
public class StudyServiceImpl implements StudyService {
	@Autowired
	private Environment environment;
	public static Map<String, Integer> mounthEndDate = new HashedMap();
	static {
		mounthEndDate.put("01", 31);
		mounthEndDate.put("02", 29);
		mounthEndDate.put("03", 31);
		mounthEndDate.put("04", 30);
		mounthEndDate.put("05", 31);
		mounthEndDate.put("06", 30);
		mounthEndDate.put("07", 31);
		mounthEndDate.put("08", 31);
		mounthEndDate.put("09", 30);
		mounthEndDate.put("10", 31);
		mounthEndDate.put("11", 30);
		mounthEndDate.put("12", 31);
	}
	@Autowired
	StudyDao studyDao;

	@Autowired
	StatusDao statusDao;

	@Autowired
	UserDao userDao;

	@Autowired
	UserWiseStudiesAsignDao userWiseStudiesAsignDao;

	@Autowired
	UserService userService;
	@Autowired
	private GroupingDao groupingDao;

	@Override
	public List<StudyMaster> findAll() {
		// TODO Auto-generated method stub
		return studyDao.findAll();
	}

	@Override
	public StudyMaster findByStudyId(Long studyId) {
		// TODO Auto-generated method stub
		return studyDao.findByStudyId(studyId);
	}

	@Override
	public StudyMaster findByStudyNo(String studyNo, Long id) {
		// TODO Auto-generated method stub
		return studyDao.findByStudyNo(studyNo, id);
	}

	@Override
	public StudyMaster findByStudyNo(String studyNo) {
		// TODO Auto-generated method stub
		return studyDao.findByStudyNo(studyNo);
	}

	@Override
	@AuditToken
	public boolean saveStudyMaster(StudyMaster studyMaster, String username) {
		// TODO Auto-generated method stub
		try {
//			studyMaster.setGlobalStatus(statusDao.findById(2));
//			studyMaster.setStudyStatus("Design");
			studyMaster.setCreatedBy(username);
			studyMaster.setCreatedOn(new Date());
			StudyMaster savedStudy = studyDao.saveStudyMaster(studyMaster);
			if (savedStudy != null) {
				List<UserWiseStudiesAsignMaster> userWisePermission = new ArrayList<>();
				if (!username.equals("superadmin")) {
					for (int i = 0; i < 2; i++) {
						UserWiseStudiesAsignMaster userwise = new UserWiseStudiesAsignMaster();
						userwise.setCreatedBy(username);
						userwise.setCreatedOn(new Date());
						userwise.setStudyMaster(savedStudy);
						if (i == 0)
							userwise.setUserId(userDao.findByusername("superadmin"));
						else
							userwise.setUserId(userDao.findByusername(username));
						userWisePermission.add(userwise);
					}
				} else {
					UserWiseStudiesAsignMaster userwise = new UserWiseStudiesAsignMaster();
					userwise.setCreatedBy(username);
					userwise.setCreatedOn(new Date());
					userwise.setStudyMaster(savedStudy);
					userwise.setUserId(userDao.findByusername("superadmin"));
					userWisePermission.add(userwise);

				}
				List<LoginUsers> users = userDao.dataUsersLoginUsers();
				for (LoginUsers user : users) {
					UserWiseStudiesAsignMaster userwise = new UserWiseStudiesAsignMaster();
					userwise.setCreatedBy("superadmin");
					userwise.setCreatedOn(new Date());
					userwise.setStudyMaster(savedStudy);
					userwise.setUserId(user);
					userWisePermission.add(userwise);
				}
				/*
				 * UserWiseStudiesAsignMaster userwise = new UserWiseStudiesAsignMaster();
				 * userwise.setCreatedBy("superadmin"); userwise.setCreatedOn(new Date());
				 * userwise.setStudyMaster(savedStudy);
				 * userwise.setUserId(userDao.findByusername("admin"));
				 * userWisePermission.add(userwise);
				 */
				userWiseStudiesAsignDao.saveStudyAsignStudyCreationTime(userWisePermission);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void updateStudy(StudyMaster sm) {
		studyDao.updateStudy(sm);
	}

	@Override
	public List<StudyPeriodMaster> allStudyPeriods(StudyMaster sm) {
		return studyDao.allStudyPeriods(sm);
	}

	@Override
	public List<StudyPeriodMaster> allStudyPeriodsWithSubEle(StudyMaster sm) {
		return studyDao.allStudyPeriodsWithSubEle(sm);
	}

	@Override
	public List<PeriodCrfs> periodCrfs(Long periodId) {
		return studyDao.periodCrfs(periodId);
	}

	@Override
	public StudyPeriodMaster studyPeriodMaster(Long periodId) {
		// TODO Auto-generated method stub
		return studyDao.studyPeriodMaster(periodId);
	}

	@Override
	public void upatePeriodCrfsList(List<PeriodCrfs> pcrfsupdate) {
		// TODO Auto-generated method stub
		studyDao.upatePeriodCrfsList(pcrfsupdate);
	}

	@Override
	public void savePeriodCrfsList(List<PeriodCrfs> pcrfsSave) {
		// TODO Auto-generated method stub
		studyDao.savePeriodCrfsList(pcrfsSave);
	}

	@Override
	public void upatePeriodCrfs(StudyPeriodMaster sp) {
		// TODO Auto-generated method stub
		studyDao.upatePeriodCrfs(sp);
	}

	@Override
	public void saveVolList(List<Volunteer> vlist) {
		// TODO Auto-generated method stub
		studyDao.saveVolList(vlist);
	}

	@Override
	public void saveVolunteerPeriodCrf(List<VolunteerPeriodCrf> vpclist) {
		studyDao.saveVolunteerPeriodCrf(vpclist);
	}

	@Override
	public List<Volunteer> studyVolunteerList(StudyMaster sm) {
		// TODO Auto-generated method stub
		return studyDao.studyVolunteerList(sm);
	}

	@Override
	public List<Volunteer> studyVolunteerListWithSite(StudyMaster sm, StudySite site) {
		// TODO Auto-generated method stub
		return studyDao.studyVolunteerListWithSite(sm, site);
	}

	@Override
	public List<VolunteerPeriodCrf> volunteerPeriodCrfList(Long peiodId) {
		// TODO Auto-generated method stub
		return studyDao.volunteerPeriodCrfList(peiodId);
	}

	@Override
	public List<VolunteerPeriodCrf> volunteerPeriodCrfList(StudyPeriodMaster sp, Volunteer vol) {
		return studyDao.volunteerPeriodCrfList(sp, vol);
	}

	@Override
	public void saveVolunteerPeriodCrfStatusList(List<VolunteerPeriodCrfStatus> vpclist) {
		studyDao.saveVolunteerPeriodCrfStatusList(vpclist);
	}

	@Override
	public List<VolunteerPeriodCrfStatus> volunteerPeriodCrfStatusList(Long peiodId) {
		// TODO Auto-generated method stub
		return studyDao.volunteerPeriodCrfStatusList(peiodId);
	}

	@Override
	public VolunteerPeriodCrfStatus volunteerPeriodCrfStatus(StudyPeriodMaster sp, Volunteer v) {
		// TODO Auto-generated method stub
		return studyDao.volunteerPeriodCrfStatus(sp, v);
	}

	@Override
	public Volunteer volunteer(Long volId) {
		// TODO Auto-generated method stub
		return studyDao.volunteer(volId);
	}

	@Override
	public VolunteerPeriodCrf volunteerPeriodCrf(Long vpcId) {
		// TODO Auto-generated method stub
		return studyDao.volunteerPeriodCrf(vpcId);
	}

	@Override
	public CrfMetaDataStd crfMetaDataStd(int crfId) {
		// TODO Auto-generated method stub
		return studyDao.crfMetaDataStd(crfId);
	}

	@Override
	public void updateVolunteerPeriodCrfStatus(VolunteerPeriodCrfStatus vpcs) {
		studyDao.updateVolunteerPeriodCrfStatus(vpcs);
	}

	@Override
	public List<PeriodCrfs> periodCrfList(StudyPeriodMaster sp) {
		return studyDao.periodCrfList(sp);
	}

	@Override
	public void updateVolunteerPeriodCrf(VolunteerPeriodCrf vpc) {
		studyDao.updateVolunteerPeriodCrf(vpc);
	}

	@Override
	public String crateGroupClass(StudyGroupClass group) {
		return studyDao.saveGroupClass(group);
	}

	@Override
	public String crateGrop(StudyGroup group) {
		return studyDao.saveGroup(group);
	}

	@Override
	public String crateSite(StudyMaster sm, HttpServletRequest request, String username) {
		int count = studyDao.maxSiteNo(sm);
		StudySite site = new StudySite();
		site.setStudyMaster(sm);

		site.setCreatedBy(username);
		site.setCreatedOn(new Date());
		site.setSiteName(request.getParameter("siteName"));
		site.setProtocalId(request.getParameter("protocalId"));
		site.setSecondaryIDs(request.getParameter("secondaryIDs"));
		site.setPrincipalInvestigator(request.getParameter("principalInvestigator"));
		site.setBriefSummary(request.getParameter("briefSummary"));
		site.setProtocolVerificationIRBApprovalDate(request.getParameter("protocolVerificationIRBApprovalDate"));
		site.setStartDate(request.getParameter("startDate"));
		site.setEndDate(request.getParameter("endDate"));
		site.setSubjects(Integer.parseInt(request.getParameter("subjects")));
		site.setFacilityName(request.getParameter("facilityName"));
		site.setFacilityCity(request.getParameter("facilityCity"));
		site.setFacilityState(request.getParameter("facilityState"));
		site.setFacilityZip(request.getParameter("facilityZip"));
		site.setFacilityCountry(request.getParameter("facilityCountry"));
		site.setFacilityContactName(request.getParameter("facilityContactName"));
		site.setFacilityContactDegree(request.getParameter("facilityContactDegree"));
		site.setFacilityContactPhone(request.getParameter("facilityContactPhone"));
		site.setFacilityContactEmail(request.getParameter("facilityContactEmail"));
		site.setSiteNo(++count);

		List<StudySitePeriodCrfs> list = new ArrayList<>();
		StudySitePeriodCrfs sspc = null;
		List<StudyPeriodMaster> periods = allStudyPeriods(sm);
		for (StudyPeriodMaster sp : periods) {
			List<PeriodCrfs> pcrfs = periodCrfs(sp.getId());
			for (PeriodCrfs pcrf : pcrfs) {
				sspc = new StudySitePeriodCrfs();
				sspc.setStudyMaster(sm);
				sspc.setSite(site);
				sspc.setPeriod(sp);
				sspc.setPeriodId(sp.getId());
				sspc.setPeriodCrf(pcrf);
				sspc.setActive(pcrf.isActive());
				sspc.setCrfName(pcrf.getCrfName());
				sspc.setCrfId(pcrf.getCrfId());
				list.add(sspc);
			}
		}

		String s = studyDao.saveSite(site, list);
		List<UserWiseSitesAsignMaster> usersave = new ArrayList<>();
		UserWiseSitesAsignMaster uwsam = new UserWiseSitesAsignMaster();
		uwsam.setStudyMaster(sm);
		uwsam.setUserId(userService.findByUsername("superadmin"));
		uwsam.setSite(site);
		usersave.add(uwsam);
		LoginUsers lu = null;
		if (!username.equals("superadmin")) {
			lu = userService.findByUsername(username);
			uwsam = new UserWiseSitesAsignMaster();
			uwsam.setStudyMaster(sm);
			uwsam.setUserId(userService.findByUsername(username));
			uwsam.setSite(site);
			usersave.add(uwsam);
		}

		List<LoginUsers> users = userDao.dataUsersLoginUsers();
		for (LoginUsers user : users) {
			boolean flag = true;
			if (lu != null && lu.getUsername() != null) {
				if (user.getUsername().equals(lu.getUsername())) {
					flag = false;
				}
			}
			if (flag) {
				uwsam = new UserWiseSitesAsignMaster();
				uwsam.setStudyMaster(sm);
				uwsam.setUserId(user);
				uwsam.setSite(site);
				usersave.add(uwsam);
			}
		}

		userWiseStudiesAsignDao.saveUserWiseSitesAsignMaster(usersave);

		return s;
	}

	@Override
	public List<StudySite> studySite(StudyMaster sm) {
		return studyDao.studySite(sm);
	}

	@Override
	public List<StudyGroupClass> studyGroupClass(StudyMaster sm) {
		return studyDao.studyGroupClass(sm);
	}

	@Override
	public List<StudyGroup> studyGroup(StudyMaster sm) {
		return studyDao.studyGroup(sm);
	}

	@Override
	public StudySite studySiteId(Long siteId) {
		return studyDao.studySiteId(siteId);
	}

	@Override
	public StudyGroupClass studyGroupClassById(long id) {
		// TODO Auto-generated method stub
		return studyDao.studyGroupClassById(id);
	}

	@Override
	public String createSubject(StudySiteSubject subject, List<Long> ids) {
		List<StudyGroup> gorups = studyDao.studyGroupByIds(ids);
		List<StudySiteSubjectGroup> subjectGroups = new ArrayList<>();
		Volunteer vol = new Volunteer();
		List<VolunteerPeriodCrfStatus> vpcslist = new ArrayList<>();
		List<VolunteerPeriodCrf> vpclist = new ArrayList<>();
//		vol.setVolId(subject.getSubjectId());
		vol.setVolName(subject.getName());
//		vol.setBedNo(subject.getSubjectno()+"");
		vol.setStudyId(subject.getStudyMaster().getId());
		vol.setStudyMaster(subject.getStudyMaster());
		vol.setSite(subject.getSite());
		StudySiteSubjectGroup sg = null;
		for (StudyGroup g : gorups) {
			sg = new StudySiteSubjectGroup();
			sg.setGroupClass(g.getGroupClass());
			sg.setStudyGroup(g);
			sg.setSite(subject.getSite());
			subjectGroups.add(sg);
		}

		StudyPeriodMaster p = studyDao.studyPeriodMaster(subject.getStudyMaster(), "Screening", 1);
		List<PeriodCrfs> pcrfs = periodCrfs(p.getId());
		VolunteerPeriodCrfStatus vpcs = new VolunteerPeriodCrfStatus();
		vpcs.setPeriod(p);
		vpcs.setVol(vol);
		vpcs.setStudyId(subject.getStudyMaster().getId());
		vpcslist.add(vpcs);
		for (PeriodCrfs pcrf : pcrfs) {
			VolunteerPeriodCrf vpc = new VolunteerPeriodCrf();
			vpc.setVol(vol);
			vpc.setStdCrf(pcrf);
			vpc.setPeriod(p);
			vpc.setStudyId(subject.getStudyMaster().getId());
			vpc.setExitCrf(pcrf.getExitCrf());
			vpclist.add(vpc);
		}
		for (VolunteerPeriodCrf vpc : vpclist)
			System.out.println(vpc.getExitCrf());
		String no = studyDao.saveStudySiteSubject(subject, subjectGroups, vol, vpcslist, vpclist);
//		sm.setVolConfiguation(true);
//		sm.updateStudy(sm);
		return no;
	}

	@Override
	public String scheduleSubject(Volunteer vol, StudyPeriodMaster p, String userName, String startDate) {
		List<VolunteerPeriodCrfStatus> vpcslist = new ArrayList<>();
		List<VolunteerPeriodCrf> vpclist = new ArrayList<>();

		List<PeriodCrfs> pcrfs = periodCrfs(p.getId());
		VolunteerPeriodCrfStatus vpcs = new VolunteerPeriodCrfStatus();
		vpcs.setPeriod(p);
		vpcs.setVol(vol);
		vpcs.setStudyId(vol.getStudyMaster().getId());
		vpcs.setStartDate(startDate);
		vpcslist.add(vpcs);
		for (PeriodCrfs pcrf : pcrfs) {
			VolunteerPeriodCrf vpc = new VolunteerPeriodCrf();
			vpc.setVol(vol);
			vpc.setStdCrf(pcrf);
			vpc.setPeriod(p);
			vpc.setStudyId(vol.getStudyMaster().getId());
			vpc.setExitCrf(pcrf.getExitCrf());
			vpclist.add(vpc);
		}
		String no = studyDao.scheduleSubject(vol, vpcslist, vpclist);
		return no;
	}

	@Override
	public void saveSubjectStatus(SubjectStatus ss) {
		studyDao.saveSubjectStatus(ss);
	}

	@Override
	public SubjectStatus subjectStatus(StudyPeriodMaster period, Volunteer vol) {
		return studyDao.subjectStatus(period, vol);
	}

	@Override
	public void saveStudyStatusAudit(StudyStatusAudit audit) {
		studyDao.saveStudyStatusAudit(audit);
	}

	@Override
	public List<SubjectStatus> subjectStatusList(StudyMaster sm) {
		return studyDao.subjectStatusList(sm);
	}

	@Override
	public boolean checkGroupNameExistOrNot(Long groupclass, String groupName) {
		StudyGroup group = studyDao.checkGroupNameExistOrNot(groupclass, groupName);
		if (group != null)
			return true;
		return false;
	}

	@Override
	public boolean checkGroupClassNameExistOrNot(StudyMaster sm, String groupName) {
		StudyGroupClass group = studyDao.checkGroupClassNameExistOrNot(sm, groupName);
		if (group != null)
			return true;
		return false;
	}

	@Override
	public boolean checkSiteNameExistOrNot(StudyMaster sm, String siteName) {
		StudySite group = studyDao.checkSiteNameExistOrNot(sm, siteName);
		if (group != null)
			return true;
		return false;
	}

	@Override
	public List<StudySiteSubject> studySiteSubjectList(StudyMaster sm) {
		return studyDao.studySiteSubjectList(sm);
	}

	@Override
	public StudySiteSubject studySiteSubject(Long id) {
		return studyDao.studySiteSubject(id);
	}

	@Override
	public List<VolunteerPeriodCrfStatus> volunteerPeriodCrfStatus(Volunteer vol) {
		return studyDao.volunteerPeriodCrfStatus(vol);
	}

	@Override
	public List<CrfDescrpencyAudit> studyCrfDescrpencyAudit(Volunteer vol) {
		return studyDao.studyCrfDescrpencyAudit(vol);
	}

	@Override
	public List<SubjectStatus> subjectStatusForVol(Volunteer vol) {
		return studyDao.subjectStatusForVol(vol);
	}

	@Override
	public boolean updateStudyMaster(StudyMaster sm, String username) {
		StudyMaster studyMaster = findByStudyId(sm.getId());
		try {
			StudyMasterLog log = new StudyMasterLog();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(log, sm);
			log.setSm(sm);
			log.setGlobalStatus(sm.getGlobalStatus());
			studyDao.saveStudyLog(log);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		studyMaster.setStudyDesc(sm.getStudyDesc());
		studyMaster.setSubjects(sm.getSubjects());
		studyMaster.setPrincipalInvestigator(sm.getPrincipalInvestigator());
		studyMaster.setStartDate(sm.getStartDate());
		studyDao.updateStudy(sm);
		return true;

	}

	@Override
	public PhysicalWeightBalanceData savePhysicalWeightBalanceData(PhysicalWeightBalanceData obj) {
		// TODO Auto-generated method stub
		PhysicalWeightBalanceData pd = studyDao.savePhysicalWeightBalanceData(obj);

		if (TestWebSocket.emittersMap.size() > 0) {
			try {
				Map<String, String> s = new HashedMap();
				s.put("id", pd.getId().toString());
//				s.put("collected", "");
//				s.put("color", "blue");
				s.put("dateAndTime", pd.getDateAndTime());
				s.put("batchNo", pd.getBatchNo());
				s.put("nozzleNo", pd.getNozzleNo());
				s.put("grossWt", pd.getGrossWt());
				s.put("tareWt", pd.getTareWt());
				s.put("netWt", pd.getNetWt());
				s.put("status", pd.getStatus());
				s.put("dataFrom", pd.getDataFrom());
				s.put("ipAddress", pd.getIpAddress());

				String json = "";
				try {
					json = new ObjectMapper().writeValueAsString(s);
				} catch (JsonProcessingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Map<String, SseEmitter> map = TestWebSocket.emittersMap;
				for (Map.Entry<String, SseEmitter> emitters : map.entrySet()) {
					SseEmitter emitter = emitters.getValue();
					try {
						emitter.send(SseEmitter.event().name("insData").data(json));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						TestWebSocket.emittersMap.remove("superadmin");
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			;
		}
		return pd;
	}

	@Override
	public List<PhysicalWeightBalanceData> physicalWeightBalanceDataList() {
		// TODO Auto-generated method stub
		return studyDao.physicalWeightBalanceDataList();
	}

	@Override
	public SubGroupInfo stdGroupInfo(Long subGroupId) {
		return studyDao.stdGroupInfo(subGroupId);
	}

	@Override
	public String saveStudyMasterRecord(StudyMaster studyMaster, String username, Long userId,
			List<LoginUsers> userIds) {
		String result = "Failed";
		long no = 0;
		try {
			studyMaster.setCreatedBy(username);
			studyMaster.setCreatedOn(new Date());
			List<UserWiseStudiesAsignMaster> list = assingStudyToUsers(userIds, studyMaster, username);
			no = studyDao.saveStudyMasterRecord(studyMaster, list);
			if (no > 0)
				result = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private List<UserWiseStudiesAsignMaster> assingStudyToUsers(List<LoginUsers> userIds, StudyMaster study,
			String userName) {
		List<UserWiseStudiesAsignMaster> usersave = new ArrayList<>();
		for (LoginUsers l : userIds) {
			UserWiseStudiesAsignMaster uwsam = new UserWiseStudiesAsignMaster();
			uwsam.setStudyMaster(study);
			uwsam.setUserId(l);
			uwsam.setCreatedBy(userName);
			uwsam.setCreatedOn(new Date());
			uwsam.setUpdateReason("");
			uwsam.setRoleId(l.getRole());
			usersave.add(uwsam);
		}
		return usersave;
	}

	@Override
	public List<LoginUsers> getStudyDirectorList() {
		return studyDao.getStudyDirectorList();
	}

	@Override
	public Map<String, List<StudyMaster>> getUserAssignedStudiesList() {
		Map<String, List<StudyMaster>> smMap = new HashMap<>();
		List<StudyMaster> smList = null;
		List<StudyMaster> tempList = null;
		LoginUsers user = null;
		List<LoginUsers> usersList = null;
		try {
			smList = studyDao.getUserAssignedStudiesList();
			if (smList != null && smList.size() > 0) {
				usersList = studyDao.getStudyDirectorList();
				if (usersList != null && usersList.size() > 0) {
					for (LoginUsers lu : usersList) {
						for (StudyMaster sm : smList) {
							StudyMaster smPojo = new StudyMaster();
							smPojo.setId(sm.getId());
							smPojo.setStudyNo(sm.getStudyNo());
							smPojo.setStudyDesc(sm.getStudyDesc());
							smPojo.setSdUser(sm.getSdUser());
							smPojo.setAsdUser(sm.getAsdUser());
							smPojo.setStartDate(sm.getStartDate());
							smPojo.setCreatedBy(sm.getCreatedBy());
							smPojo.setCreatedOn(sm.getCreatedOn());
							if (lu.getId() == sm.getSdUser() || lu.getId() == sm.getAsdUser()) {
								if (smMap.containsKey(lu.getUsername())) {
									tempList = smMap.get(lu.getUsername());
									if (lu.getId() == sm.getSdUser())
										smPojo.setUserId(sm.getSdUser());
									else
										smPojo.setUserId(sm.getAsdUser());
									tempList.add(smPojo);
									smMap.put(lu.getUsername(), tempList);
								} else {
									tempList = new ArrayList<>();
									if (lu.getId() == sm.getAsdUser())
										smPojo.setUserId(sm.getAsdUser());
									else
										smPojo.setUserId(sm.getSdUser());
									tempList.add(smPojo);
									smMap.put(lu.getUsername(), tempList);
								}
							}
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smMap;
	}

	@Override
	public List<StudyMaster> getStudyMasterListBasedOnLogin(Long userId) {
		return studyDao.getStudyMasterListBasedOnLogin(userId);
	}

	@Override
	public List<StudyMaster> getStudyMasterListBasedOnLoginVeiw(Long userId) {
		return studyDao.getStudyMasterListBasedOnLoginVeiw(userId);
	}

	@Override
	public StudyMaster getStudyMasterRecord(Long studyId) {
		return studyDao.getStudyMasterRecord(studyId);
	}

	@Override
	public String saveStudyMetaDataDetails(StudyMaster studyMaster, String username, List<GroupInfo> groupInfos,
			List<SubGroupInfo> subGroups, List<SubGroupAnimalsInfo> animalInfo) {
		String result = "Failed";
		boolean flag = false;
		StudyMaster sm = null;
		StudyMasterLog smLog = null;
		try {
			sm = studyDao.getStudyMasterRecord(studyMaster.getId());
			if (sm != null) {
				StudyMaster smPojo = new StudyMaster();
				BeanUtils.copyProperties(smPojo, studyMaster);
//				smPojo.setStudyStatus("Design");
				smPojo.setUpdatedBy(username);
				smPojo.setUpdatedOn(new Date());
				smPojo.setCreatedBy(sm.getCreatedBy());
				smPojo.setCreatedOn(sm.getCreatedOn());
				smPojo.setStudyNo(sm.getStudyNo());
				smPojo.setStudyDesc(sm.getStudyDesc());
				smPojo.setSdUser(sm.getSdUser());
				smPojo.setAsdUser(sm.getAsdUser());
				if (smPojo.isCalculationBased())
					smPojo.setWeightUnits(studyDao.staticDatasById(smPojo.getWeightUnits().getId()));
				smLog = new StudyMasterLog();
				BeanUtils.copyProperties(smLog, sm);
				smLog.setId(null);
				smLog.setCreatedBy(username);
				smLog.setCreatedOn(new Date());
				smLog.setSm(sm);
				flag = studyDao.updateStudyMetaDataDetails(smPojo, smLog, groupInfos, subGroups, animalInfo);

			}
			if (flag) {
				List<UserWiseStudiesAsignMaster> userWisePermission = new ArrayList<>();
				if (!username.equals("superadmin")) {
					StudyMaster study = studyDao.getStudyMasterRecord(1L);
					for (int i = 0; i < 2; i++) {
						UserWiseStudiesAsignMaster userwise = new UserWiseStudiesAsignMaster();
						userwise.setCreatedBy(username);
						userwise.setCreatedOn(new Date());
						if (i == 0) {
							LoginUsers user = userDao.findByusername("superadmin");
							userwise.setUserId(user);
							userwise.setRoleId(user.getRole());
							userwise.setStudyMaster(sm);
						} else {
							LoginUsers user = userDao.findByusername(username);
							userwise.setUserId(user);
							userwise.setRoleId(user.getRole());
							userwise.setStudyMaster(study);
						}
						userWisePermission.add(userwise);
					}
				}
//				else {
//					StudyMaster study = studyDao.getStudyMasterRecord(1L);
//					UserWiseStudiesAsignMaster userwise = new UserWiseStudiesAsignMaster();
//					userwise.setCreatedBy(username);
//					userwise.setCreatedOn(new Date());
//					userwise.setStudyMaster(study);
//					LoginUsers user = userDao.findByusername(username);
//					userwise.setUserId(user);
//					userwise.setRoleId(user.getRole());
//					userWisePermission.add(userwise);
//				}
				if (userWisePermission.size() > 0)
					userWiseStudiesAsignDao.saveStudyAsignStudyCreationTime(userWisePermission);
			}
			if (flag)
				return result = "success";
			else
				return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
	}

	@Override
	public List<StudyMaster> getAllStudyDesignStatusStudiesList(Long userId) {
		return studyDao.getAllStudyDesignStatusStudiesList(userId);
	}

	@Override
	public List<StudyMaster> getStudyMasterListForUpdation() {
		return studyDao.getStudyMasterListForUpdation();
	}

	@Override
	public String updateStudyMasterRecord(StudyMaster sm, String userName) {
		String result = "Failed";
		StudyMaster stdPojo = null;
		boolean flag = false;
		StudyMasterLog smLog = null;
		try {
			stdPojo = studyDao.getStudyMasterRecord(sm.getId());
			if (stdPojo != null) {
				smLog = new StudyMasterLog();
				BeanUtils.copyProperties(smLog, stdPojo);
				smLog.setId(null);
				smLog.setCreatedBy(userName);
				smLog.setCreatedOn(new Date());
				smLog.setSm(stdPojo);

				if (!sm.getStudyNo().equals(stdPojo.getStudyNo())) {
					stdPojo.setStudyNo(sm.getStudyNo());
					flag = true;
				}
				if (!sm.getStudyDesc().equals(stdPojo.getStudyDesc())) {
					stdPojo.setStudyDesc(sm.getStudyDesc());
					flag = true;
				}
				if (sm.getAsdUser() != stdPojo.getAsdUser()) {
					stdPojo.setAsdUser(sm.getAsdUser());
					flag = true;
				}
				if (sm.getSdUser() != stdPojo.getSdUser()) {
					stdPojo.setSdUser(sm.getSdUser());
					flag = true;
				}
			}
			if (flag) {
				boolean updateFlag = studyDao.updateStudyMaterRecord(stdPojo, smLog);
				if (updateFlag)
					result = "success";
			} else
				result = "NoModifications";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public LoginUsers getLoginUsersWithId(Long sdUser) {
		return studyDao.getLoginUsersWithId(sdUser);
	}

	@Override
	public List<StudyMaster> getStudyMasterForSearch(String studynoval, String studyNameval, String sponsorval,
			String statusval, String roleval) {
		return studyDao.getStudyMasterForSearch(studynoval, studyNameval, sponsorval, statusval, roleval);
	}

	@Override
	public com.covide.dto.SatusAndWorkFlowDetailsDto SatusAndWorkFlowDetailsDto(String statusCode, String moduleName) {
		return studyDao.SatusAndWorkFlowDetailsDto(statusCode, moduleName);
	}

	@Override
	public List<DepartmentMaster> getDepartmentMasterList() {
		return studyDao.getDepartmentMasterList();
	}

	@Override
	public com.covide.dto.SatusAndWorkFlowDetailsDto satusAndWorkFlowDetailsDtoForUpdation(Long studyId, String string,
			String string2) {
		return studyDao.getstatusAndWorkFlowDetailsDtoForUpdation(studyId, string, string2);
	}

	@Override
	public StatusMaster getStatusMasterRecord(String newStatus) {
		return studyDao.getStatusMasterRecord(newStatus);
	}

	@Override
	public String updateStudyMetaDataDetails(StudyMaster studyMaster, String username, List<GroupInfo> existsGroupList,
			List<GroupInfo> newgroupList, Integer groups) {
		String result = "Failed";
		boolean flag = false;
		StudyMaster sm = null;
		StudyMasterLog smLog = null;
		try {
			sm = studyDao.getStudyMasterRecord(studyMaster.getId());
			if (sm != null) {
				StudyMaster smPojo = new StudyMaster();
				BeanUtils.copyProperties(smPojo, studyMaster);
//				smPojo.setStudyStatus("Design");
				smPojo.setUpdatedBy(username);
				smPojo.setUpdatedOn(new Date());
				smPojo.setCreatedBy(sm.getCreatedBy());
				smPojo.setCreatedOn(sm.getCreatedOn());
				smPojo.setStudyNo(sm.getStudyNo());
				smPojo.setStudyDesc(sm.getStudyDesc());
				smPojo.setSdUser(sm.getSdUser());
				smPojo.setAsdUser(sm.getAsdUser());
				if (smPojo.isCalculationBased())
					smPojo.setWeightUnits(studyDao.staticDatasById(smPojo.getWeightUnits().getId()));
				smLog = new StudyMasterLog();
				BeanUtils.copyProperties(smLog, sm);
				smLog.setId(null);
				smLog.setCreatedBy(username);
				smLog.setCreatedOn(new Date());
				smLog.setSm(sm);
				flag = studyDao.updateStudyMetaDataRecords(smPojo, smLog, existsGroupList, newgroupList, username,
						groups);
			}
			boolean finalFlag = false;
			if (flag) {
				com.covide.dto.SatusAndWorkFlowDetailsDto sawfd = studyDao
						.getSatusAndWorkFlowDetailsDtoDetails(sm.getId(), "RRSD", "SU");
				StudyDesignStatus sds = null;
				if (sawfd != null) {
					if (sawfd.getSds() != null) {
						sds = sawfd.getSds();
						sds.setStatus(sawfd.getSm());
						sds.setUpdatedBy(username);
						sds.setUpdatedOn(new Date());
					}
					ApplicationAuditDetails aad = new ApplicationAuditDetails();
					aad.setAction("Update");
					aad.setCreatedBy(username);
					aad.setCreatedOn(new Date());
					aad.setStudyId(sm);
					aad.setWfsdId(sawfd.getWfsd());

					finalFlag = studyDao.saveUpdateStdyDesignStatus(sds, aad);
					if (finalFlag)
						result = "success";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	@Override
	public RoleMaster getRoleMasterRecord(Long roleId) {
		return studyDao.getRoleMasterRecord(roleId);
	}

	@Override
	public long saveRoleMasterRecord(String username, RoleMaster role) {
		return studyDao.saveRoleMasterRecord(username, role);
	}

	@Override
	public Map<Long, String> getStudyDirectorsDetails(List<StudyMaster> allStudies) {
		Map<Long, String> sdMap = new HashMap<>();
		Set<Long> sdIds = new HashSet<>();
		List<LoginUsers> usersList = null;
		try {
			if (allStudies.size() > 0) {
				for (StudyMaster sm : allStudies) {
					if (sm.getAsdUser() != null)
						sdIds.add(sm.getAsdUser());
					if (sm.getSdUser() != null)
						sdIds.add(sm.getSdUser());
				}
				if (sdIds.size() > 0)
					usersList = studyDao.getLoginUserRecordsList(sdIds);
				if (usersList != null && usersList.size() > 0) {
					for (LoginUsers user : usersList) {
						sdMap.put(user.getId(), user.getUsername());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdMap;
	}

	@Override
	public StatusMaster statusMaster(String string) {
		// TODO Auto-generated method stub
		return studyDao.statusMaster(string);
	}

	@Override
	public List<StaticData> staticDatas(String fieldName) {

		return studyDao.staticDatas(fieldName);
	}

	@Override
	public StudyDesignStatus studyDesignStatus(Long studyId) {
		// TODO Auto-generated method stub
		return studyDao.studyDesignStatus(studyId);
	}

	@Override
	public List<InstrumentIpAddress> instrumentIpAddresses(StatusMaster activeStatus, boolean withActiveStatus) {
		// TODO Auto-generated method stub

		return studyDao.instrumentIpAddresses(activeStatus.getId(), withActiveStatus);
	}

	@Override
	public InstrumentIpAddress instrumentIpAddress(Long ipaddress) {
		// TODO Auto-generated method stub
		return studyDao.instrumentIpAddress(ipaddress);
	}

	@Override
	public SysmexDto saveSysmexData(SysmexDto dto) {
		return studyDao.saveSysmexData(dto);
	}

	@Override
	public List<SysmexData> sysmexDataList() {
		return studyDao.saveSysmexData();
	}

	@Override
	public SysmexDataDto sysmexDataDtoList(Long studyId, List<SysmexData> sysmexDataList, String exprotType, String observation) {
		StudyMaster study = studyDao.findByStudyId(studyId);
		SysmexDataDto dto = new SysmexDataDto();
		PdfHeadderDto pdfHeasderDto = new PdfHeadderDto();
		pdfHeasderDto.setStudyNumber(study.getStudyNo());
		dto.setHeader(pdfHeasderDto);
		InstrumentIpAddress ip = studyDao.instumentIpAddress("SYSMEX");
		Map<String, StudyTestCodes> testCodes = studyInstumentTestCodesMap(ip.getId(), studyId, observation);
		SortedMap<Integer, StudyTestCodes> heading = new TreeMap<>();
		for (Map.Entry<String, StudyTestCodes> m : testCodes.entrySet()) {
			heading.put(m.getValue().getOrderNo(), m.getValue());
		}
		dto.setHeading(heading);
		dto.setStudy(study);

		Set<String> animals = new HashSet<>();
		Map<String, List<SysmexData>> animalWiseData = new HashMap<>();
		sysmexDataList.stream().forEach((data) -> {
			List<SysmexData> list = animalWiseData.get(data.getAnimalNumber());
			if (list == null)
				list = new ArrayList<>();
			list.add(data);
			animalWiseData.put(data.getAnimalNumber(), list);
			animals.add(data.getAnimalNumber());
		});
		SortedMap<String, SortedMap<Integer, List<SysmexTestCodeData>>> sysmexDataListMap = new TreeMap<>();

		for (Map.Entry<String, List<SysmexData>> mp : animalWiseData.entrySet()) {
			SortedMap<Integer, List<SysmexTestCodeData>> eachSample = new TreeMap<>();
			List<SysmexData> sdlist = mp.getValue();
			System.out.println("Animla : " + mp.getKey() + "\t size : " + sdlist.size());
			System.out.println("");
			for (SysmexData sd : sdlist) {
				List<SysmexTestCodeData> sysmexTestCodeData = sd.getSysmexTestCodeData();
				for (SysmexTestCodeData tc : sysmexTestCodeData) {
					if (tc.getStudyTestCode() != null) {
						List<SysmexTestCodeData> list = eachSample.get(tc.getStudyTestCode().getOrderNo());
						if (list == null)
							list = new ArrayList<>();
						list.add(tc);
						eachSample.put(tc.getStudyTestCode().getOrderNo(), list);
					}
				}
			}
			sysmexDataListMap.put(mp.getKey(), eachSample);
		}
		dto.setSysmexDataListMap(sysmexDataListMap);
//		// animal groups
//		List<String> animalList = new ArrayList<>(animals);
//		Map<String, StudyAnimal> studyAnimals = groupingDao.animilGropInfo(studyId, animalList);
//		Map<String, String> animalGroup = new HashedMap();
//		studyAnimals.forEach((animNO, anim) -> {
//			animalGroup.put(animNO, anim.getGroupInfo().getGroupName());
//		});
//		dto.setStudyAnimalsGroup(animalGroup);
//		Map<String, String> animalTestDoneDate = new HashedMap();
//
//		int temporder = 100;
//		SimpleDateFormat dateTimeSecondsFormat = new SimpleDateFormat(
//				environment.getRequiredProperty("dateTimeSecondsFormat"));
//		SortedMap<String, SortedMap<Integer, List<SysmexTestCodeData>>> dataList = new TreeMap<>();
//
//		SortedMap<String, List<SysmexData>> animalWise = new TreeMap<>();
//		for (SysmexData data : sysmexDataList) {
//			List<SysmexData> list = animalWise.get(data.getAnimal().getPermanentNo());
//			if (list == null)
//				list = new ArrayList<>();
//			list.add(data);
//			animalWise.put(data.getAnimal().getPermanentNo(), list);
//		}
//		for (Entry<String, List<SysmexData>> map : animalWise.entrySet()) {
//			String animla = "";
//			List<SysmexData> lis = map.getValue();
//			SortedMap<Integer, List<SysmexTestCodeData>> eachAniamal = new TreeMap<>();
//			for (SysmexData data : lis) {
//				if (!animalTestDoneDate.containsKey(data.getAnimal().getPermanentNo())) {
//					animla = data.getAnimal().getPermanentNo();
//					animalTestDoneDate.put(data.getAnimalNumber(), dateTimeSecondsFormat.format(data.getStartTime()));
//				}
//				List<SysmexTestCodeData> sysmexTestCodeData = data.getSysmexTestCodeData();
//				for (SysmexTestCodeData sd : sysmexTestCodeData) {
//					if (sd.getStudyTestCode() != null) {
//						List<SysmexTestCodeData> tempResults = eachAniamal.get(sd.getStudyTestCode().getOrderNo());
//						if (tempResults == null)
//							tempResults = new ArrayList<>();
//						tempResults.add(sd);
//						eachAniamal.put(sd.getStudyTestCode().getOrderNo(), tempResults);
//					}
////					else {
////						List<SysmexTestCodeData> tempResults = eachAniamal.get((temporder + 1));
////						if (tempResults == null)
////							tempResults = new ArrayList<>();
////						tempResults.add(sd);
////						eachAniamal.put(temporder++, tempResults); // TOX - order from lib to study level swami
////					}
//				}
//
//			}
//			dataList.put(animla, eachAniamal);
//		}
////		for (SysmexData data : sysmexDataList) {
////			
////			SortedMap<Integer, List<SysmexTestCodeData>> eachAniamal = new TreeMap<>();
////			List<SysmexTestCodeData> sysmexTestCodeData = data.getSysmexTestCodeData();
////			for (SysmexTestCodeData sd : sysmexTestCodeData) {
////				if (sd.getStudyTestCode() != null) {
////					List<SysmexTestCodeData> tempResults = eachAniamal.get(sd.getStudyTestCode().getOrderNo());
////					if (tempResults == null)
////						tempResults = new ArrayList<>();
////					tempResults.add(sd);
////					eachAniamal.put(sd.getStudyTestCode().getOrderNo(), tempResults);
////				} 
//////				else {
//////					List<SysmexTestCodeData> tempResults = eachAniamal.get((temporder + 1));
//////					if (tempResults == null)
//////						tempResults = new ArrayList<>();
//////					tempResults.add(sd);
//////					eachAniamal.put(temporder++, tempResults); // TOX - order from lib to study level swami
//////				}
////			}
////			dataList.put(data.getAnimalNumber(), eachAniamal);
////		}
//		dto.setAnimalTestDoneDate(animalTestDoneDate);
//		dto.setSysmexDataList(dataList);
		return dto;
	}

//	@Override
//	public File exportSysmexDataToExcel(HttpServletRequest request) throws IOException {
//
//		List<SysmexData> sysmexDataList = sysmexDataList();
////		model.addAttribute("sysmexDataList", sysmexDataList);
//		SortedMap<Integer, String> hedding = SysmexThread.sysMaxTestCodesMap;
//		SortedMap<Long, SortedMap<Integer, String>> rows = sysmexDataDtoList(sysmexDataList, "xlsx");
//
//		return exportSysmexDataToExcelCreateExcel(request, hedding, rows);
//
//	}

	private File exportSysmexDataToExcelCreateExcel(HttpServletRequest request, SysmexDataDto sysmexDataDto,
			String startDate) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		String dateinSting = sdf.format(new Date());

		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
		/*
		 * CreationHelper helps us create instances of various things like DataFormat,
		 * Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way
		 */
		CreationHelper createHelper = workbook.getCreationHelper();
		// Create a Sheet
		Sheet sheet = workbook.createSheet("Vitros_" + dateinSting);
		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		DataFormat format = workbook.createDataFormat();
		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		CellStyle numberCellStyle = workbook.createCellStyle();
		numberCellStyle.setDataFormat(format.getFormat("#.###"));

		Cell cell = null;
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MMM-yyyy"));

		int rowCount = 0;
		Row headerRow = null;
		// Create a Row
		int cellNo = 0;
		headerRow = sheet.createRow(rowCount++);
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue("Study");
		cell.setCellStyle(headerCellStyle);

		cell = headerRow.createCell(cellNo++);
		cell.setCellValue(sysmexDataDto.getStudy().getStudyNo());
		cell.setCellStyle(headerCellStyle);

		cellNo = 0;
		headerRow = sheet.createRow(rowCount++);
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue("Test Run Date");
		cell.setCellStyle(headerCellStyle);
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue(startDate);
		cell.setCellStyle(headerCellStyle);

		cellNo = 0;
		headerRow = sheet.createRow(rowCount++);
//		cell = headerRow.createCell(cellNo++);
//		cell.setCellValue("Group");
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue("Animal No.");
//		cell = headerRow.createCell(cellNo++);
//		cell.setCellValue("Test Run Time");
//		cell.setCellStyle(dateCellStyle);

		int cols = sysmexDataDto.getHeading().size() + 1;
		for (Map.Entry<Integer, StudyTestCodes> m : sysmexDataDto.getHeading().entrySet()) {
			cell = headerRow.createCell(cellNo++);
			cell.setCellValue(m.getValue().getTestCode().getDisPalyTestCode());
		}

		cellNo = 0;
		headerRow = sheet.createRow(rowCount++);
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue("");
//		cell = headerRow.createCell(cellNo++);
//		cell.setCellValue("");
//		cell = headerRow.createCell(cellNo++);
//		cell.setCellValue("");
		for (Map.Entry<Integer, StudyTestCodes> m : sysmexDataDto.getHeading().entrySet()) {
			cell = headerRow.createCell(cellNo++);
			cell.setCellValue(m.getValue().getTestCode().getTestCodeUints().getDisplayUnit());
			// cell.setCellStyle(dateCellStyle);
		}

//		sysmexDataDto.sysmexDataListMap
//		private SortedMap<String, SortedMap<Integer, List<SysmexTestCodeData>>> sysmexDataListMap = new TreeMap<>();
		for (Entry<String, SortedMap<Integer, List<SysmexTestCodeData>>> map : sysmexDataDto.getSysmexDataListMap()
				.entrySet()) {
			cellNo = 0;
			headerRow = sheet.createRow(rowCount++);
//			cell = headerRow.createCell(cellNo++);
//			cell.setCellValue(sysmexDataDto.getStudy().getStudyNo());

			cell = headerRow.createCell(cellNo++);
			cell.setCellValue(map.getKey());

			for (Map.Entry<Integer, List<SysmexTestCodeData>> m : map.getValue().entrySet()) {
				cell = headerRow.createCell(cellNo++);
				List<SysmexTestCodeData> lis = m.getValue();
				String finalValue = "";
				for (SysmexTestCodeData ctd : lis) {
					if (ctd.isFinalValue()) {
						finalValue = ctd.getValue();
					}
				}
				cell.setCellValue(finalValue);
				cell.setCellStyle(numberCellStyle);
			}
		}

//		for (Entry<String, SortedMap<Integer, List<SysmexTestCodeData>>> eachAnimal : sysmexDataDto.getSysmexDataList()
//				.entrySet()) {
//			cellNo = 0;
//			headerRow = sheet.createRow(rowCount++);
//			cell = headerRow.createCell(cellNo++);
//			cell.setCellValue(sysmexDataDto.getStudyAnimalsGroup().get(eachAnimal.getKey()));
//			// cell.setCellStyle(dateCellStyle);
//
//			cell = headerRow.createCell(cellNo++);
//			cell.setCellValue(eachAnimal.getKey());
//			System.out.println(eachAnimal.getKey());
////			cell.setCellStyle(dateCellStyle);
//
//			cell = headerRow.createCell(cellNo++);
//			cell.setCellValue(sysmexDataDto.getAnimalTestDoneDate().get(eachAnimal.getKey()));
//			cell.setCellStyle(dateCellStyle);
//
//			for (Map.Entry<Integer, List<SysmexTestCodeData>> m : eachAnimal.getValue().entrySet()) {
//				cell = headerRow.createCell(cellNo++);
//				List<SysmexTestCodeData> lis = m.getValue();
//				String finalValue = "";
//				for (SysmexTestCodeData ctd : lis) {
//					if (ctd.isFinalValue()) {
//						finalValue = ctd.getValue();
//					}
//				}
//				cell.setCellValue(finalValue);
//				cell.setCellStyle(numberCellStyle);
//			}
//		}

		for (int i = 0; i < cols; i++) {
			sheet.autoSizeColumn(i);
		}

		String path = request.getSession().getServletContext().getRealPath("/") + "Sysmex_" + dateinSting;
		System.out.println(path);

		File file = new File(path + ".xlsx");

		if (!file.exists()) {
			file.createNewFile();
		} else {
			file.delete();
			file.createNewFile();
		}

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(path + ".xlsx");
		workbook.write(fileOut);
		fileOut.close();
		// Closing the workbook
		workbook.close();
		return file;
	}

	@Override
	public List<SysmexData> sysmexDataList(Long studyNumber, String startDate, String sampleType, String observation)
			throws ParseException {
		// TODO Auto-generated method stub
		return studyDao.sysmexDataList(studyNumber, startDate, sampleType, observation);
	}

	@Override

	public File exportSysmexDataToExcel(HttpServletRequest request, Long studyNumber, String startDate,
			String sampleType, String observation) throws ParseException, IOException, ParseException {
		List<SysmexData> sysmexDataList = sysmexDataList(studyNumber, startDate, sampleType, observation);
		SysmexDataDto sysmexDataDto = sysmexDataDtoList(studyNumber, sysmexDataList, "xlsx", observation);

		return exportSysmexDataToExcelCreateExcel(request, sysmexDataDto, startDate);
	}

	private String checktheFile(String pdfpath, String fileName) {
		FileOutputStream fos = null;

		try {
			pdfpath = "C:\\instrumenData";
			File file = new File(pdfpath);
			if(!file.exists())
				file.mkdirs();
			pdfpath =  pdfpath + "\\" +fileName;
			System.out.println("File Path : " + pdfpath);
			// read only file with same name already exists
			file = new File(pdfpath);
			file.createNewFile();
			/*
			 * Make sure that if the file exists, it is writable first
			 */
			if (file.exists() && !file.canWrite()) {

				System.out.println("File exists and it is read only, making it writable");
				file.setWritable(true);
			}
			if (!file.canRead())
				file.setReadable(true);
			fos = new FileOutputStream(file);

			System.out.println("File can be overwritten now!");
			return pdfpath;
		} catch (IOException fnfe) {
			fnfe.printStackTrace();

		} finally {

			try {
				if (fos != null)
					fos.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String exportSysmexDataToPdf(HttpServletRequest request, HttpServletResponse response, Long studyId,
			String animalNo, String realPath, String startDate, String sampleType, String observation)
			throws ParseException, IOException, DocumentException {
		Long userId = (Long) request.getSession().getAttribute("userId");
		LoginUsers user = userService.findById(userId);
		analysedBy = user.getUsername();
		String file = checktheFile(realPath, "sysmex.pdf");
		System.out.println(file);
		StudyMaster study = studyDao.findByStudyId(studyId);
		List<SysmexData> sysmexDataList = sysmexDataList(studyId, startDate, sampleType, observation);
//		List<SysmexData> sysmexData = groupingDao.sysmexDataByStudyIdAnimalNo(studyId, animalNo, startDate, sampleType);
//		SysmexDataDto sysmexDataDto = sysmexDataDtoTable(studyId, sysmexData, "table");
		SysmexDataDto sysmexDataDto = sysmexDataDtoList(studyId, sysmexDataList, "pdf", observation);
		List<PdfDataDto> pdfdata = sysmexDataDtoListForPdf(sysmexDataDto, study);
//		List<SysmexData> list = new ArrayList<>();
//		list.add(sysmexData);
//		SysmexDataDto sysmexDataDto = sysmexDataDtoList(studyId, sysmexData, "xlsx");
//		sysmexDataDto.setAnimalId(animalNo);
//		sysmexDataDto.setStudy(study);
//		SortedMap<Integer, SysmexTestCodePdfData> rows = sysmexDataDtoListForPdf(sysmexData);
		InstrumentIpAddress ip = studyDao.instumentIpAddress("SYSMEX");
		exportSysmexDataToPdf(study, request, response, file, pdfdata, user, ip);
		return file;
	}

	private List<PdfDataDto> sysmexDataDtoListForPdf(SysmexDataDto sysmexDataDto, StudyMaster study) {

		List<PdfDataDto> pdfdata = new ArrayList<>();
		SimpleDateFormat dateTimeSecondsFormat = new SimpleDateFormat(
				environment.getRequiredProperty("dateTimeSecondsFormat"));
		for (Map.Entry<String, SortedMap<Integer, List<SysmexTestCodeData>>> map : sysmexDataDto.getSysmexDataListMap()
				.entrySet()) {
			System.out.println("Animal/Sample : " + map.getKey());
			PdfDataDto dto = new PdfDataDto();
			dto.setAnimalName(map.getKey());
			dto.setStudyNumber(study.getStudyNo());
			boolean flag = true;
			List<SysmexTestCodeData> singleAnimal = new ArrayList<>();
			for (Map.Entry<Integer, List<SysmexTestCodeData>> mp : map.getValue().entrySet()) {
				singleAnimal.addAll(mp.getValue());
			}
			SortedMap<Integer, SysmexTestCodeData> withOrder = new TreeMap<>();
			Map<String, SysmexTestCodeData> nameWise = new HashMap<>();
			for (SysmexTestCodeData data : singleAnimal) {
				System.out.println("Order : " + data.getOrderNo());
				if (flag) {
					flag = false;
					if (data.getSysmexData().getAnimal() != null) {
						dto.setGroupName(data.getSysmexData().getAnimal().getGroupInfo().getGroupName());
						dto.setGroupNo(data.getSysmexData().getAnimal().getGroupInfo().getGroupNo());

						dto.setAnimalNo(data.getSysmexData().getAnimal().getAnimalId());
						dto.setPrintDate(dateTimeSecondsFormat.format(new Date()));
					}
					dto.setInstumentName(data.getSysmexData().getInstumentModelNo());
					dto.setSpecies(study.getSpecies().getName());
					dto.setTestRunTime(dateTimeSecondsFormat.format(data.getSysmexData().getStartTime()));
				}
				System.out.println("data.getOrderNo() : " + data.getOrderNo());
				withOrder.put(data.getOrderNo(), data);
				nameWise.put(data.getStudyTestCode().getTestCode().getTestCode(), data);
			}
			SortedMap<Integer, SysmexTestCodePdfData> withOrderDataUnits = new TreeMap<>();
			List<Integer> orders = new ArrayList<>();
			for (Map.Entry<Integer, SysmexTestCodeData> m : withOrder.entrySet()) {
				if (!orders.contains(m.getKey())) {
					SysmexTestCodePdfData pdfrow = new SysmexTestCodePdfData();
					pdfrow.setOrder(m.getKey());
					pdfrow.setAbsolutetc(m.getValue());
					nameWise.remove(m.getValue().getStudyTestCode().getTestCode().getTestCode());

					String s = null;
					for (Map.Entry<String, SysmexTestCodeData> mtc : nameWise.entrySet()) {
						System.out.println(mtc.getValue().getStudyTestCode().getTestCode().getDisPalyTestCode() + " = "
								+ m.getValue().getStudyTestCode().getTestCode().getDisPalyTestCode() + "\t"
								+ mtc.getValue().getStudyTestCode().getTestCode().getTestCode());
						if (mtc.getValue().getStudyTestCode().getTestCode().getDisPalyTestCode()
								.equals(m.getValue().getStudyTestCode().getTestCode().getDisPalyTestCode())
								&& mtc.getValue().getStudyTestCode().getTestCode().getTestCode().contains("%")) {
							pdfrow.setPersentagetc(mtc.getValue());
							orders.add(mtc.getValue().getOrderNo());
							s = mtc.getKey();
							break;
						}
					}
					if (s != null)
						nameWise.remove(s);
					withOrderDataUnits.put(m.getKey(), pdfrow);
				}
			}
			dto.setRowData(withOrderDataUnits);
			pdfdata.add(dto);
		}
		return pdfdata;
	}

	private void exportSysmexDataToPdf(StudyMaster study, HttpServletRequest request, HttpServletResponse response,
			String file, List<PdfDataDto> pdfdata, LoginUsers user, InstrumentIpAddress ip)
			throws IOException, DocumentException {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		String dateinSting = sdf.format(new Date());
		Document document = null;
		document = new Document();
		document.setPageSize(new Rectangle(PageSize.A4));
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
//		document.addTitle("Total Roles List");
//		document.setPageSize(PageSize.A4);
//		document.setMargins(80, 80, 50, 80);
//		document.setMarginMirroring(false);
//		document.open();
		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		com.itextpdf.text.Font heading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12,
				com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font sideheading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10,
				com.itextpdf.text.Font.BOLD);
		PdfPCell cell = null;
		int pagecount = 1;
		for (PdfDataDto pdfData : pdfdata) {
			document.newPage();
			document.setPageCount(pagecount++);

			includeLogo(request, document);
			int tableCount = 1;
			PdfPTable table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			int count = 1;
			Paragraph pa = new Paragraph("VIMTA LABS LTD", heading);
			pa.setAlignment(Element.ALIGN_CENTER);
			cell = new PdfPCell(pa);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setFixedHeight(30f);
//	         cell.setColspan(3);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);

			table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			pa = new Paragraph("Haematology Parameters", heading);
			pa.setAlignment(Element.ALIGN_CENTER);
			cell = new PdfPCell(pa);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setFixedHeight(30f);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);
			PdfHeadderDto hdto = new PdfHeadderDto();
			hdto.setStudyNumber(study.getStudyNo());
			hdto.setGorupNam(pdfData.getGroupName());
			hdto.setAnimalNo(pdfData.getAnimalName());
			try {
				hdto.setTestRunTime(sdf.format(sdf.parse(pdfData.getTestRunTime())));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				hdto.setTestRunTime(pdfData.getTestRunTime());
				e1.printStackTrace();
			}

			hdto.setInstrument(pdfData.getInstumentName());
			hdto.setStudy(study);
			animalHeadderPart(hdto, document, true);

			PdfPTable hstable = null;
//			hstable = new PdfPTable(2);
//			hstable.setWidthPercentage(95);
//			cell = new PdfPCell(new Phrase("Species : " + study.getSpecies().getName(), regular));
//			cell.setBorder(Rectangle.NO_BORDER);
//			hstable.addCell(cell);
//			cell = new PdfPCell(new Phrase("Instrument Name : " + hdto.getInstrument(), regular));
//			cell.setColspan(2);
//			cell.setBorder(Rectangle.NO_BORDER);
//			hstable.addCell(cell);
//			document.add(hstable);

//			document.add(Chunk.NEWLINE);
			table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			cell = new PdfPCell(new Paragraph(""));
			cell.setBorder(Rectangle.TOP);
			table.addCell(cell);
			document.add(table);
//			document.add(Chunk.NEWLINE);

			table = new PdfPTable(6);
			table.setWidthPercentage(95);
			cell = new PdfPCell(new Phrase("Parameter", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("Absolute Value", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("Percentage", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			table.addCell(cell);
			document.add(table);

//			document.add(Chunk.NEWLINE);
			table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			cell = new PdfPCell(new Paragraph(""));
			cell.setBorder(Rectangle.TOP);
			table.addCell(cell);
			document.add(table);
//			document.add(Chunk.NEWLINE);
			System.out.println(hdto.getAnimalNo());
			SortedMap<Integer, SysmexTestCodePdfData> rowData = pdfData.getRowData();
			if (rowData != null) {
				for (Map.Entry<Integer, SysmexTestCodePdfData> mp : rowData.entrySet()) {

					hstable = new PdfPTable(6);
					hstable.setWidthPercentage(95);

//					String absolute = mp.getValue().getAbsolutetc().getStudyTestCode().getTestCode().getDisPalyTestCode()
//							+ "\t\t" + mp.getValue().getAbsolutetc().getValue() + "\t\t["
//							+ mp.getValue().getAbsolutetc().getStudyTestCode().getTestCode().getTestCodeUints().getDisplayUnit()
//							+ "]";
					try {
						System.out.println(mp.getValue());
						System.out.println(mp.getValue().getAbsolutetc());
						System.out.println(mp.getValue().getAbsolutetc().getStudyTestCode());
						System.out.println(mp.getValue().getAbsolutetc().getStudyTestCode().getTestCode());
						System.out.println(
								mp.getValue().getAbsolutetc().getStudyTestCode().getTestCode().getDisPalyTestCode());
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					if (mp.getValue().getAbsolutetc() != null)
						cell = new PdfPCell(new Phrase(
								mp.getValue().getAbsolutetc().getStudyTestCode().getTestCode().getDisPalyTestCode(),
								regular));
					else
						cell = new PdfPCell(new Phrase(""));
					cell.setBorder(Rectangle.NO_BORDER);
					hstable.addCell(cell);

					if (mp.getValue().getAbsolutetc() != null) {
						table = new PdfPTable(2);
						cell = new PdfPCell(new Phrase(mp.getValue().getAbsolutetc().getValue(), regular));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase("[" + mp.getValue().getAbsolutetc().getStudyTestCode()
								.getTestCode().getTestCodeUints().getDisplayUnit() + "]", regular));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);

						cell = new PdfPCell(table);
					} else {
						cell = new PdfPCell(new Phrase(""));
					}
					cell.setBorder(Rectangle.NO_BORDER);
					hstable.addCell(cell);

					cell = new PdfPCell(new Phrase("", regular));
					cell.setBorder(Rectangle.NO_BORDER);
					hstable.addCell(cell);
					if (mp.getValue().getPersentagetc() != null) {
//						absolute = mp.getValue().getPersentagetc().getStudyTestCode().getTestCode().getDisPalyTestCode()
//								+ "\t\t" + mp.getValue().getPersentagetc().getValue() + "\t\t[" + mp.getValue()
//										.getPersentagetc().getStudyTestCode().getTestCode().getTestCodeUints().getDisplayUnit()
//								+ "]";
						table = new PdfPTable(2);
						cell = new PdfPCell(new Phrase(mp.getValue().getPersentagetc().getValue(), regular));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);
						cell = new PdfPCell(new Phrase("[" + mp.getValue().getPersentagetc().getStudyTestCode()
								.getTestCode().getTestCodeUints().getDisplayUnit() + "]", regular));
						cell.setBorder(Rectangle.NO_BORDER);
						table.addCell(cell);

						cell = new PdfPCell(table);
						cell.setBorder(Rectangle.NO_BORDER);
						hstable.addCell(cell);
					} else {
						cell = new PdfPCell(new Phrase("", regular));
						cell.setBorder(Rectangle.NO_BORDER);
						hstable.addCell(cell);
					}

					cell = new PdfPCell(new Phrase("", regular));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(2);
					hstable.addCell(cell);
					document.add(hstable);

				}
			}

			document.add(new Paragraph(""));
//			document.add(Chunk.NEWLINE);
			table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			cell = new PdfPCell(new Paragraph(""));
			cell.setBorder(Rectangle.TOP);
			table.addCell(cell);
			document.add(table);

			table = new PdfPTable(1);
			table.setWidthPercentage(95);
			cell = new PdfPCell(new Phrase("End of the Report ", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);

			document.add(new Paragraph("\n"));
			table = new PdfPTable(1);
			table.setWidthPercentage(95);
//			table.setHorizontalAlignment(Element.ALIGN_LEFT);
//			Paragraph analysedBy = new Paragraph("Analysed By  : " + user.getFullName(), regular);
			Paragraph analysedBy = new Paragraph("Analysed By  : ", regular);
			cell = new PdfPCell(analysedBy);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);

			table = new PdfPTable(1);
//			table.setHorizontalAlignment(Element.ALIGN_LEFT);
//			table.setWidthPercentage(30);
			table.setWidthPercentage(95);
//			Paragraph analysedOn = new Paragraph("Analysed On  : " + sdf.format(new Date()), regular);
			Paragraph analysedOn = new Paragraph("Sign and Date  : ", regular);
			cell = new PdfPCell(analysedOn);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);
			document.add(Chunk.NEWLINE);
		}

		document.close();

	}

//	private String species;
//	private String instrumentName;
	private String analysedBy;

//	private List<PdfDataDto> sysmexDataDtoListForPdf(List<SysmexData> sysmexDataList, StudyMaster study) {
//
//		List<PdfDataDto> pdfdata = new ArrayList<>();
//		SimpleDateFormat dateTimeSecondsFormat = new SimpleDateFormat(
//				environment.getRequiredProperty("dateTimeSecondsFormat"));
//		InstrumentIpAddress ip = studyDao.instumentIpAddress("SYSMEX");
//		// 1.
//		List<StudyTestCodes> tcs = studyInstumentTestCodes(ip.getId(), study.getId());
//		Collections.sort(tcs, new Comparator<StudyTestCodes>() {
//			@Override
//			public int compare(StudyTestCodes o1, StudyTestCodes o2) {
//				return o1.getOrderNo() - o2.getOrderNo();
//			}
//		});
//
//		// 2.
//		Map<Long, StudyAnimal> animalMap = new HashMap<>();
//		// 3.
//		Map<Long, List<SysmexData>> animalData = new HashMap<>();
//		for (SysmexData data : sysmexDataList) {
//			if (!animalMap.containsKey(data.getAnimal().getId())) {
//				animalMap.put(data.getAnimal().getId(), data.getAnimal());
//			}
//			List<SysmexData> dataList = animalData.get(data.getAnimal().getId());
//			if (dataList == null)
//				dataList = new ArrayList<>();
//			dataList.add(data);
//			animalData.put(data.getAnimal().getId(), dataList);
//		}
//
//		// 4.
//		List<StudyAnimal> animalList = new ArrayList<>();
//		animalList.addAll(animalMap.values());
//		Collections.sort(animalList, new Comparator<StudyAnimal>() {
//			@Override
//			public int compare(StudyAnimal o1, StudyAnimal o2) {
//				return o1.getAnimalId() - o2.getAnimalId();
//			}
//		});
//
//		// 5.
////		List<SysmexRowiseDto> sysmexRowiseDtos = new ArrayList<>();
//
//		for (StudyAnimal animal : animalList) {
//			PdfDataDto dto = new PdfDataDto();
//			dto.setStudyNumber(animal.getStudy().getStudyNo());
//			dto.setGroupName(animal.getGroupInfo().getGroupName());
//			dto.setGroupNo(animal.getGroupInfo().getGroupNo());
//			dto.setAnimalName(animal.getPermanentNo());
//			dto.setAnimalNo(animal.getAnimalId());
//			dto.setPrintDate(dateTimeSecondsFormat.format(new Date()));
//
////			private String reporType;
////			private String anaysedBy;
////			private List<String> rowHeadin;
//			boolean dataflag = true;
//
//			System.out.println(animal.getPermanentNo());
//			List<SysmexData> dataList = animalData.get(animal.getId());
//			dto.setTestRunTime(dateTimeSecondsFormat.format(dataList.get(0).getStartTime()));
//
//			// MAP<StudyTestCode-orderNo, List<SysmexTestCodeData>>
//			Map<Integer, List<SysmexTestCodeData>> testcodeWiseData = new HashMap<>();
//			SortedMap<Integer, SysmexTestCodePdfData> mapFinal = new TreeMap<>();
//			for (SysmexData sd : dataList) {
//				if (dataflag) {
//					dto.setInstumentName(sd.getInstumentModelNo());
//					dto.setTestRunTime(dateTimeSecondsFormat.format(sd.getStartTime()));
//					dto.setSpecies(sd.getAnimalCode());
//					dataflag = false;
//				}
//				List<SysmexTestCodeData> tcData = sd.getSysmexTestCodeData();
//				for (SysmexTestCodeData stcd : tcData) {
//					if (stcd.getStudyTestCode() != null) {
//						List<SysmexTestCodeData> list = testcodeWiseData.get(stcd.getStudyTestCode().getOrderNo());
//						if (list == null)
//							list = new ArrayList<>();
//						list.add(stcd);
//						testcodeWiseData.put(stcd.getStudyTestCode().getOrderNo(), list);
//					}
//				}
//			}
//
//			SortedMap<Integer, SysmexTestCodeDataDto> elementValues = new TreeMap<>();
//			SortedMap<String, SysmexTestCodeDataDto> elementValuespersent = new TreeMap<>();
//			for (StudyTestCodes tc : tcs) {
//				System.out.println(tc.getTestCode().getDisPalyTestCode() + "\t" + tc.getOrderNo());
//				SysmexTestCodeDataDto ddto = new SysmexTestCodeDataDto();
//				ddto.setTestCode(tc);
//				ddto.setDisplayTestCode(tc.getTestCode().getDisPalyTestCode());
//				if (tc.getTestCode().getTestCodeUints().getInstumentUnit().equals("%")) {
//					ddto.setTestType("%");
//				} else
//					ddto.setTestType("Actual");
//				ddto.setOrderNo(tc.getOrderNo());
//				List<SysmexTestCodeData> list = testcodeWiseData.get(tc.getOrderNo());
//				if (list != null && list.size() > 0) {
//					if (list.size() > 1) {
//						ddto.setMutipleReuslt(true);
//						String result = "";
//						SysmexTestCodeData stcd = null;
//						boolean flag = false;
//						for (SysmexTestCodeData sdl : list) {
//							if (sdl.isFinalValue()) {
//								result = sdl.getValue();
//								stcd = sdl;
//							}
//						}
//						ddto.setResultToDisplay(result);
//						ddto.setSysmexTestCodeData(stcd);
//
//					} else {
//						ddto.setResultToDisplay("<font color='red'>" + list.get(0).getValue() + "</font>");
//						ddto.setSysmexTestCodeData(list.get(0));
//					}
//
//				}
//				elementValues.put(tc.getOrderNo(), ddto);
//
//				if (tc.getTestCode().getTestCodeUints().getInstumentUnit().equals("%")) {
//					elementValuespersent.put(tc.getTestCode().getDisPalyTestCode(), ddto);
//				}
//			}
//
//			List<Integer> userTcs = new ArrayList<>();
//			for (Entry<Integer, SysmexTestCodeDataDto> map : elementValues.entrySet()) {
//				if (!userTcs.contains(map.getKey())) {
//					SysmexTestCodePdfData pdfd = new SysmexTestCodePdfData();
//					pdfd.setOrder(map.getKey());
//					pdfd.setAbsolutetc(map.getValue().getSysmexTestCodeData());
//					if (!map.getValue().getTestType().equals("%")) {
//						SysmexTestCodeDataDto ddd = elementValuespersent
//								.get(map.getValue().getTestCode().getTestCode().getDisPalyTestCode());
//						if (ddd != null && !userTcs.contains(ddd.getOrderNo())) {
//							pdfd.setPersentagetc(ddd.getSysmexTestCodeData());
//							userTcs.add(ddd.getTestCode().getOrderNo());
//						}
//					}
//					System.out.println("--------------------------------");
//					try {
//						System.out.println(pdfd);
//						System.out.println(pdfd.getAbsolutetc());
//						System.out.println(pdfd.getAbsolutetc().getStudyTestCode());
//						System.out.println(pdfd.getAbsolutetc().getStudyTestCode().getTestCode());
//						System.out.println(pdfd.getAbsolutetc().getStudyTestCode().getTestCode().getDisPalyTestCode());
//						System.out.println("======================================");
//					} catch (Exception e) {
//						// TODO: handle exceptione.
//						e.printStackTrace();
//					}
//					mapFinal.put(map.getKey(), pdfd);
//				}
//			}
//			dto.setRowData(mapFinal);
//			pdfdata.add(dto);
//		}
//		return pdfdata;
//
//	}

	public SysmexData sysmexDataById(Long sysmexDataId) {
		// TODO Auto-generated method stub
		return studyDao.sysmexDataById(sysmexDataId);
	}

	@Override
	public List<StagoDataDto> stagoDataList() {
		return studyDao.stagoData();
	}

	@Override
	public File exportStagoDataToExcel(HttpServletRequest request, Long studyNumber, String startDate,
			String sampleType, String observation) throws ParseException, IOException {
		List<StagoDataDto> sysmexDataList = stagoDataList(studyNumber, startDate, true, sampleType, observation);
		Collections.sort(sysmexDataList);
		return exportStagoDataToExcelCreateExcel(request, sysmexDataList, studyNumber, sampleType, observation);
	}

	@Override
	public List<StagoDataDto> stagoDataList(Long studyNumber, String startDate, Boolean export, String sampleType, String observation)
			throws ParseException {
		// TODO Auto-generated method stub
		return studyDao.stagoDataList(studyNumber, startDate, export, sampleType, observation);
	}

	private File exportStagoDataToExcelCreateExcel(HttpServletRequest request, List<StagoDataDto> sysmexDataList,
			Long studyNumber, String sampleType, String observation) throws IOException {
		StudyMaster study = findByStudyId(studyNumber);
		InstrumentIpAddress ip = studyDao.instumentIpAddress("STAGO");
		List<StudyTestCodes> tcs = studyInstumentTestCodes(ip.getId(), study.getId(), observation);
		Set<String> tcSet = new HashSet<>();
		SortedMap<Integer, StudyTestCodes> selecteTestCodes = new TreeMap<>();
		for (StudyTestCodes tc : tcs) {
			selecteTestCodes.put(tc.getOrderNo(), tc);
			tcSet.add(tc.getTestCode().getTestCode());
		}
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		String dateinSting = sdf.format(new Date());

		// Create a Workbook
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		/*
		 * CreationHelper helps us create instances of various things like DataFormat,
		 * Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way
		 */
		CreationHelper createHelper = workbook.getCreationHelper();

		// Create a Sheet
		Sheet sheet = workbook.createSheet("Sysmex_" + dateinSting);

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		int rowCount = 0;
		// Create a Row
		Row headerRow = null;
		Cell cell = null;
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MMM-yyyy"));

		headerRow = sheet.createRow(rowCount++);
		cell = headerRow.createCell(0);
		cell.setCellValue("Study");
		cell = headerRow.createCell(1);
		cell.setCellValue(study.getStudyNo());

		headerRow = sheet.createRow(rowCount++);
		int cols = 1;
		int colno = 0;

		if (sampleType.equals("Animal") && observation.equals("Treatment")) {
			cell.setCellValue("Group Name.");
			cell = headerRow.createCell(colno++);
		}
		cell = headerRow.createCell(colno++);
		cell.setCellValue("Animal No.");
		if (sampleType.equals("QC")) {
			cell = headerRow.createCell(colno++);
			cell.setCellValue("Lot No.");
			cols++;
		}

		cols = cols + (selecteTestCodes.size() * 3);
		for (Map.Entry<Integer, StudyTestCodes> tc : selecteTestCodes.entrySet()) {
			cell = headerRow.createCell(colno++);
			cell.setCellValue(tc.getValue().getTestCode().getTestCode());
			cell = headerRow.createCell(colno++);
			cell.setCellValue("Run Time");
			cell = headerRow.createCell(colno++);
			cell.setCellValue("Done By");
		}

		for (StagoDataDto sdd : sysmexDataList) {
			colno = 0;
			headerRow = sheet.createRow(rowCount++);

			if (sampleType.equals("Animal") && observation.equals("Treatment")) {
				cell = headerRow.createCell(colno++);
				cell.setCellValue(sdd.getGroupName());
			}
			
			cell = headerRow.createCell(colno++);
			if (sampleType.equals("Animal") && observation.equals("Treatment")) {
				cell.setCellValue(sdd.getAnimal().getPermanentNo());
			}else if (sampleType.equals("Animal")) {
				cell.setCellValue(sdd.getAnimal().getAnimalNo());
			}else if (sampleType.equals("QC")) {
				cell.setCellValue(sdd.getAnimalNo());
				cell = headerRow.createCell(colno++);
				cell.setCellValue(sdd.getLotNo());
			}
		
			for (Map.Entry<Integer, StudyTestCodes> tc : selecteTestCodes.entrySet()) {
				if (tc.getValue().getTestCode().getTestCode().equals("PT")) {
					cell = headerRow.createCell(colno++);
					String result[] = sdd.getPtResult().split("\\/");
					cell.setCellValue(result[0]);
					cell = headerRow.createCell(colno++);
					cell.setCellValue(sdd.getPtResultTime());
					cell = headerRow.createCell(colno++);
					cell.setCellValue(sdd.getPtResultDoneBy());
				} else if (tc.getValue().getTestCode().getTestCode().equals("APTT")) {
					cell = headerRow.createCell(colno++);
					String result[] = sdd.getApttResult().split("\\/");
					cell.setCellValue(result[0]);
//					cell.setCellValue(sdd.getApttResult());
					cell = headerRow.createCell(colno++);
					cell.setCellValue(sdd.getApttResultTime());
					cell = headerRow.createCell(colno++);
					cell.setCellValue(sdd.getApttResultDoneBy());
				} else if (tc.getValue().getTestCode().getTestCode().equals("Fibrinogen")) {
					cell = headerRow.createCell(colno++);
					String result[] = sdd.getFibrinogenResult().split("\\/");
					cell.setCellValue(result[0]);
//					cell.setCellValue(sdd.getFibrinogenResult());
					cell = headerRow.createCell(colno++);
					cell.setCellValue(sdd.getFibrinogenResultTime());
					cell = headerRow.createCell(colno++);
					cell.setCellValue(sdd.getFibrinogenResultDoneBy());
//					cell.setCellStyle(dateCellStyle);
				}
			}
		}

		for (int i = 0; i < cols; i++) {
			sheet.autoSizeColumn(i);
		}

		String path = request.getSession().getServletContext().getRealPath("/") + "Stago_" + dateinSting;
		System.out.println(path);

		File file = new File(path + ".xlsx");

		if (!file.exists()) {
			file.createNewFile();
		} else {
			file.delete();
			file.createNewFile();
		}

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(path + ".xlsx");
		workbook.write(fileOut);
		fileOut.close();
		// Closing the workbook
		workbook.close();
		return file;
	}

	@Override
	public List<StudyMaster> stagoStudyNumbers() {
		return studyDao.allStagoStudyNumbers();
	}

	@Override
	public List<StudyMaster> sysmaxStudyNumbers() {
		return studyDao.allSysmaxStudyNumbers();
	}

	@Override
	public List<SysmexTestCode> sysmexTestCodes(boolean b) {
		// TODO Auto-generated method stub
		return studyDao.sysmexTestCodes(b);
	}

	@Override
	public InstrumentIpAddress instrumentIpAddress() {
		// TODO Auto-generated method stub
		return studyDao.instumentIpAddress();
	}

	@Override
	public List<VistrosDataDto> vistrosDataList(SortedMap<Integer, String> testCodes) {
		return studyDao.vistrosData(testCodes);
	}

	@Override
	public List<VistrosDataDto> vistrosDataList(String studyNumber, String startDate,
			SortedMap<Integer, String> testCodes) throws ParseException {
		// TODO Auto-generated method stub
		return studyDao.vistrosDataList(studyNumber, startDate, testCodes);
	}

	@Override
	public File exportVistrosDataToExcel(HttpServletRequest request, Long studyId, String startDate, String sampleType, String observation)
			throws ParseException, IOException {
		InstrumentIpAddress ip = instrumentIpAddress("VITROS");
		VistrosDataDto vistrosDataDto = vitrosDataForExcel(studyId, ip, startDate, sampleType, observation);
		return exportVistrosDataToExcelCreateExcel(request, vistrosDataDto);
	}

	private File exportVistrosDataToExcelCreateExcel(HttpServletRequest request, VistrosDataDto vitrosDataDto)
			throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		String dateinSting = sdf.format(new Date());
		// Create a Workbook
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
		/*
		 * CreationHelper helps us create instances of various things like DataFormat,
		 * Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way
		 */
		CreationHelper createHelper = workbook.getCreationHelper();
		// Create a Sheet
		Sheet sheet = workbook.createSheet("Vitros_" + dateinSting);
		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		DataFormat format = workbook.createDataFormat();
		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		CellStyle numberCellStyle = workbook.createCellStyle();
		numberCellStyle.setDataFormat(format.getFormat(""));

		Cell cell = null;
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MMM-yyyy"));

		int rowCount = 0;
		Row headerRow = null;
		// Create a Row
		int cellNo = 0;
		headerRow = sheet.createRow(rowCount++);
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue("Study");
		cell.setCellStyle(headerCellStyle);
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue(vitrosDataDto.getStudy().getStudyNo());
		cell.setCellStyle(headerCellStyle);

		cellNo = 0;
		headerRow = sheet.createRow(rowCount++);
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue("Test Date");
		cell.setCellStyle(headerCellStyle);
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue(vitrosDataDto.getTestDate());
		cell.setCellStyle(headerCellStyle);

		// Test perameter row
		cellNo = 0;
		headerRow = sheet.createRow(rowCount++);
		if (vitrosDataDto.getSampleType().equals("Animal")) {
			cell = headerRow.createCell(cellNo++);
			cell.setCellValue("Group");
//			cell.setCellStyle(dateCellStyle);			
		}
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue("Animal No.");
		int cols = vitrosDataDto.getHeading().size() + 1;
		if (vitrosDataDto.getSampleType().equals("Animal")) {
			cols = vitrosDataDto.getHeading().size() + 2;
		}

		for (Map.Entry<Integer, StudyTestCodes> m : vitrosDataDto.getHeading().entrySet()) {
			cell = headerRow.createCell(cellNo++);
			cell.setCellValue(m.getValue().getTestCode().getDisPalyTestCode());
			// cell.setCellStyle(dateCellStyle);
		}

		// units row
		cellNo = 0;
		headerRow = sheet.createRow(rowCount++);
		if (vitrosDataDto.getSampleType().equals("Animal")) {
			cell = headerRow.createCell(cellNo++);
			cell.setCellValue(""); // group no
		}
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue("");/// animal no;
		for (Map.Entry<Integer, StudyTestCodes> m : vitrosDataDto.getHeading().entrySet()) {
			cell = headerRow.createCell(cellNo++);
			cell.setCellValue(m.getValue().getTestCode().getTestCodeUints().getDisplayUnit());
			// cell.setCellStyle(dateCellStyle);
		}

		// data rows
		for (Entry<String, SortedMap<Integer, List<VitrosTestCodeDataDto>>> eachAnimal : vitrosDataDto
				.getAnimalTcDataListMap().entrySet()) {
			cellNo = 0;
			headerRow = sheet.createRow(rowCount++);
			if (vitrosDataDto.getSampleType().equals("Animal")) {
				cell = headerRow.createCell(cellNo++);
				try {
					cell.setCellValue(vitrosDataDto.getStudyAnimalsGroup().get(eachAnimal.getKey()));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					cell.setCellValue("");
				}
			}
			cell = headerRow.createCell(cellNo++);
			cell.setCellValue(eachAnimal.getKey());

			for (Map.Entry<Integer, List<VitrosTestCodeDataDto>> m : eachAnimal.getValue().entrySet()) {
				List<VitrosTestCodeDataDto> lis = m.getValue();
				if (lis != null) {
					for (VitrosTestCodeDataDto ctd : lis) {
						cell = headerRow.createCell(cellNo++);
						cell.setCellType(CellType.STRING);

						List<VitrosData> dataList = ctd.getVitroData();
						boolean flag = true;
						for (VitrosData vd : dataList) {
							if (vd.isFinalize()) {
								System.out.println(vd.getAnimalNo() + "\t" + vd.getTestName() + "\t" + vd.getResult());
								cell.setCellValue(vd.getResult().toString());
								flag = false;
							}
						}

						if (flag) {
							cell.setCellValue("");
						}
					}
				}

//				cell.setCellValue(sb.toString());
//				cell.setCellStyle(numberCellStyle);

			}
		}

		for (int j = 0; j < cols; j++) {
			sheet.autoSizeColumn(j);
		}

		String path = request.getSession().getServletContext().getRealPath("/") + "Vitros_" + dateinSting;
		System.out.println(path);

		File file = new File(path + ".xlsx");

		if (!file.exists()) {
			file.createNewFile();
		} else {
			file.delete();
			file.createNewFile();
		}

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(path + ".xlsx");
		workbook.write(fileOut);
		fileOut.close();
		// Closing the workbook
		workbook.close();
		return file;
	}

	@Override
	public boolean instrumentIpAddressesStatus(List<Long> insturmentId) {
		// TODO Auto-generated method stub
		return studyDao.instrumentIpAddressesStatus(insturmentId);
	}

	@Override
	public List<StagoData> stagoDataList(Long studyId, String animalNo, String testName, Long animalId,
			String sampleType, String observation, String stagostDate)  throws ParseException {
		// TODO Auto-generated method stub
		return studyDao.stagoDataList(studyId, animalNo, testName, animalId, sampleType, observation, stagostDate);
	}

	@Override
	public String stagoDataSave(Long studyId, String animalNo, String testName, Long finalResultId, Long animalId,
			String rerunCommnet, String sampleType, String observation, String stagostDate) throws ParseException {
		// TODO Auto-generated method stub
		List<StagoData> list = stagoDataList(studyId, animalNo, testName, animalId, sampleType, observation, stagostDate);
		StringBuffer sb = new StringBuffer();
		boolean fg = false;
		for (StagoData sd : list) {
			System.out.println(sd.getId() + "\t" + finalResultId);
			if (sd.getId().equals(finalResultId)) {
				if (!sd.isSelectedResult()) {
					sd.setSelectedResult(true);
					sd.setRerunCommnet(rerunCommnet);
				}
				String rel = "<font color='green'>" + sd.getTestResult() + "</font>";
				if (fg) {
					sb.append(",").append(rel);
				} else {
					sb.append(rel);
					fg = true;
				}
			} else {
				if (fg) {
					sb.append(",").append(sd.getTestResult());
				} else {
					sb.append(sd.getTestResult());
					fg = true;
				}
				if (sd.isSelectedResult()) {
					sd.setSelectedResult(false);
					sd.setRerunCommnet(rerunCommnet);
				}
			}

		}
		studyDao.updateStagoDataList(list);

		return "{\"Success\": \"" + true + "\",\"Message\":\"Data Updated successfully\", \"value\":\"" + sb.toString()
				+ "\"}";

	}

	@Override
	public InstrumentIpAddress instrumentIpAddress(String name) {
		// TODO Auto-generated method stub
		return studyDao.instumentIpAddress(name);
	}

	@Override
	public List<StudyTestCodes> studyInstumentTestCodes(Long instumentId, Long studyId, String observation) {
		// TODO Auto-generated method stub
		return studyDao.studyInstumentTestCodes(instumentId, studyId, observation);
	}

	@Override
	public List<StudyMaster> allActiveStudys() {
		// TODO Auto-generated method stub
		return studyDao.allActiveStudys();
	}

	@SuppressWarnings("unused")
	@Override
	public VistrosDataDto vitrosDataForExcel(Long studyId, InstrumentIpAddress ip, String startDate,
			String sampleType, String observation) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
			SimpleDateFormat sdfloc = new SimpleDateFormat(environment.getRequiredProperty("dateTimeSecondsFormat"));
//			SimpleDateFormat sdfloc = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Map<String, StudyTestCodes> testCodes = studyInstumentTestCodesMap(ip.getId(), studyId, observation);
			Collection<StudyTestCodes> tecs = testCodes.values();
			List<StudyTestCodes> tcsList = new ArrayList<>(tecs);
			Collections.sort(tcsList, (tc1, tc2) -> Integer.compare(tc1.getOrderNo(), tc2.getOrderNo()));
			List<String> testcodesset = new ArrayList<>(testCodes.keySet());
			for (Map.Entry<String, StudyTestCodes> e : testCodes.entrySet()) {
				System.out.println(e.getKey() + "----" + e.getValue().getOrderNo());
			}
			StudyMaster study = studyDao.findByStudyId(studyId);

			Date from = null;
			Date to = null;
			startDate = startDate.trim();
			System.out.println(startDate.equals(""));
			System.out.println(!startDate.equals(""));
			if (startDate == null || startDate.equals("")) {
//				String startDateString = null;
//				String endDateString = null;
//				startDateString = "01-" + month + "-" + year + " 00:00:00";
//				boolean leapFalg = false;
//				if (month.equals("02")) {
//					int studyYear = Integer.parseInt(year);
//					if (studyYear % 4 == 0) {
//						if (studyYear % 100 == 0) {
//							if (studyYear % 400 == 0)
//								leapFalg = true;
//						} else
//							leapFalg = true;
//					}
//				}
//				if (leapFalg)
//					endDateString = "29-" + month + "-" + year + " 23:59:59";
//				else
//					endDateString = mounthEndDate.get(month) + "-" + month + "-" + year + " 23:59:59";
//				from = sdfloc.parse(startDateString);
//				System.out.println(sdfloc.format(from));
//				to = sdfloc.parse(endDateString);
			} else {
				SimpleDateFormat dateonly = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
				from = dateonly.parse(startDate);
				String[] st = startDate.split("\\-");
				String s = st[0] + "-" + st[1] + "-" + st[2];
				startDate = s;
				System.out.println(startDate.trim());
				Date fromDate = dateonly.parse(startDate.trim());
				Calendar c = Calendar.getInstance();
				c.setTime(fromDate);
				c.add(Calendar.DATE, 1);
				to = c.getTime();
			}
			System.out.println(from + "\t" + to);
			VistrosDataDto dto = new VistrosDataDto();
			dto.setStudy(study);
			dto.setTestDate(startDate);
			dto.setSampleType(sampleType);
			SortedMap<Integer, StudyTestCodes> heading = new TreeMap<>();
			for (Map.Entry<String, StudyTestCodes> m : testCodes.entrySet()) {
				heading.put(m.getValue().getOrderNo(), m.getValue());
			}
			dto.setHeading(heading);

			List<VitrosData> vitrosRawDataList = studyDao.vitrosRawDataListWithStudyAndDate(studyId, from, to, null,
					testcodesset, sampleType, observation);
			Map<String, SortedMap<Integer, List<VitrosTestCodeDataDto>>> animalTcDataListMap = new HashMap<>();
			Set<String> animals = new HashSet<>();
			Map<String, List<VitrosData>> animalWiseData = new HashMap<>();
			vitrosRawDataList.stream().forEach((data) -> {
				System.out.println(data.getId() + "\t" + data.getAnimalNo() + "\t" + data.getTestName() + "\t"
						+ data.getResult() + "\t" + data.isFinalize());
				List<VitrosData> list = animalWiseData.get(data.getAnimalNo());
				if (list == null)
					list = new ArrayList<>();
				list.add(data);
				animalWiseData.put(data.getAnimalNo(), list);
				animals.add(data.getAnimalNo());
			});
			List<String> animalList = new ArrayList<>(animals);
			Map<String, StudyAnimal> studyAnimals = groupingDao.animilGropInfo(studyId, animalList);
			Map<String, String> animalGroup = new HashedMap();
			studyAnimals.forEach((animNO, anim) -> {
				animalGroup.put(animNO, anim.getGroupInfo().getGroupName());
			});
			dto.setStudyAnimals(studyAnimals);
			dto.setStudyAnimalsGroup(animalGroup);

			SortedMap<String, SortedMap<Integer, List<VitrosTestCodeDataDto>>> vitrosDataListMap = new TreeMap<>();
			for (Map.Entry<String, List<VitrosData>> mp : animalWiseData.entrySet()) {
				SortedMap<Integer, List<VitrosData>> eachorder = new TreeMap<>();
				List<VitrosData> li = mp.getValue();
				for (VitrosData sd : li) {
					System.out.println(sd.getId() + "\t" + sd.getAnimalNo() + "\t" + sd.getTestName() + "\t"
							+ sd.getResult() + "\t" + sd.isFinalize());
					StudyTestCodes tc = testCodes.get(sd.getTestName());
					List<VitrosData> temp = eachorder.get(tc.getOrderNo());
					if (temp == null)
						temp = new ArrayList<>();
					temp.add(sd);
					eachorder.put(tc.getOrderNo(), temp);
				}
				for (StudyTestCodes tc : tcsList) {
					if (!eachorder.containsKey(tc.getOrderNo())) {
						VitrosData vd = new VitrosData();
						vd.setAnimalNo(mp.getKey());
						vd.setTestName(tc.getTestCode().getTestCode());
						List<VitrosData> temp = new ArrayList<>();
						temp.add(vd);
						eachorder.put(tc.getOrderNo(), temp);
					}
				}

				SortedMap<Integer, List<VitrosTestCodeDataDto>> eachSample = new TreeMap<>();
				for (Map.Entry<Integer, List<VitrosData>> sd : eachorder.entrySet()) {
					List<VitrosData> dataList = sd.getValue();
					StudyTestCodes tc = null;
					List<VitrosData> datafList = new ArrayList<>();
					for (int vc = 0; vc < dataList.size(); vc++) {
						VitrosData data = dataList.get(vc);
						if (testCodes.get(data.getTestName()) != null) {
							tc = testCodes.get(data.getTestName());
							data.setAnimal(studyAnimals.get(data.getAnimalNo()));
							System.out.println("tc : " + tc.getTestCode().getTestCode() + "    " + data.getId() + "\t"
									+ data.getAnimalNo() + "\t" + data.getTestName() + "\t" + data.getResult() + "\t"
									+ data.isFinalize());
							datafList.add(data);
						}
					}
					List<VitrosTestCodeDataDto> list = eachSample.get(tc.getOrderNo());
					if (list == null)
						list = new ArrayList<>();
					VitrosTestCodeDataDto vdto = new VitrosTestCodeDataDto();
					vdto.setOrderNo(tc.getOrderNo());
					vdto.setTestCode(tc);
					vdto.setVitroData(datafList);
					list.add(vdto);
					eachSample.put(vdto.getOrderNo(), list);

				}
				vitrosDataListMap.put(mp.getKey(), eachSample);
			}
			dto.setAnimalTcDataListMap(vitrosDataListMap);
			return dto;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Map<String, StudyTestCodes> studyInstumentTestCodesMap(Long instrument, Long studyId, String observation) {
		List<StudyTestCodes> list = studyInstumentTestCodes(instrument, studyId, observation);
		Map<String, StudyTestCodes> testCodes = new HashMap<>();
		System.out
				.println("---------------------------------TEST CODES------------------------------------------------");
		list.stream().forEach((stc) -> {
			testCodes.put(stc.getTestCode().getTestCode(), stc);
			System.out.println(stc.getId() + "\t" + stc.getTestCode().getId() + "\t" + stc.getTestCode().getTestCode()
					+ "\t" + stc.getTestCode().getDisPalyTestCode() + "\t" + stc.getOrderNo());
		});
		System.out
				.println("---------------------------------TEST CODES------------------------------------------------");
		return testCodes;

	}

	@Override
	public String exportVitroDataToPdf(HttpServletRequest request, HttpServletResponse response, String animalId,
			String realPath, Long studyId, String startDate, String sampleType, String observation) throws IOException, DocumentException {
		Long userId = (Long) request.getSession().getAttribute("userId");
//		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		LoginUsers user = userService.findById(userId);
		StudyMaster study = studyDao.findByStudyId(studyId);
		String file = checktheFile(realPath, "vitro.pdf");
		System.out.println(file);
		InstrumentIpAddress ip = instrumentIpAddress("VITROS");
		VistrosDataDto vistrosDataDto = vitrosDataForExcel(studyId, ip, startDate, sampleType, observation);

//		InstrumentIpAddress ip = instrumentIpAddress("VITROS");
//		List<VistrosDataDto> dto = vitroDataDtoListForPdf(study, ip, animalId, startDate, sampleType);
//		Map<String, StudyTestCodes> testCodes = studyInstumentTestCodesMap(ip.getId(), study.getId());
//		List<String> testcodesset = new ArrayList<>(testCodes.keySet());
//		for (Map.Entry<String, StudyTestCodes> e : testCodes.entrySet()) {
//			System.out.println(e.getKey() + "----" + e.getValue().getOrderNo());
//		}
		exportVitrosDataToPdf(vistrosDataDto, animalId, user, study, request, response, file, startDate, sampleType);
		return file;
	}

	private void exportVitrosDataToPdf(VistrosDataDto vistrosDataDto, String animalId, LoginUsers user,
			StudyMaster study, HttpServletRequest request, HttpServletResponse response, String file, String startDate,
			String sampleType) throws DocumentException, MalformedURLException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		String dateinSting = sdf.format(new Date());
		Document document = new Document(new Rectangle(PageSize.A4));
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		for (Map.Entry<String, SortedMap<Integer, List<VitrosTestCodeDataDto>>> map : vistrosDataDto
				.getAnimalTcDataListMap().entrySet()) {
			document.newPage();
			vitrosHeading(document, request);
			vistrosDataDto.getHeader().setStudy(study);
			vistrosDataDto.getHeader().setTestRunTime(startDate);
			vistrosDataDto.getHeader().setAnimalNo(map.getKey());
			SortedMap<Integer, List<VitrosTestCodeDataDto>> mp1 = map.getValue();
			for (Map.Entry<Integer, List<VitrosTestCodeDataDto>> m : mp1.entrySet()) {
				if (m.getValue().size() > 0) {
					List<VitrosData> vitroData = m.getValue().get(0).getVitroData();
					for (VitrosData vd : vitroData) {
						if (vd.isFinalize()) {
							try {
								vistrosDataDto.getHeader().setInstrument(vd.getMachineName());
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							if (vistrosDataDto.getHeader().getInstrument() == null) {
								vistrosDataDto.getHeader().setInstrument("");
							}

							try {
								if (sampleType.equals("Animal")) {
									vistrosDataDto.getHeader()
											.setGorupNam(vd.getAnimal().getGroupInfo().getGroupName());
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							if (vistrosDataDto.getHeader().getGorupNam() == null) {
								vistrosDataDto.getHeader().setGorupNam("");
							}
						}
					}
					break;
				}
			}
			animalHeadderPart(vistrosDataDto.getHeader(), document, true);

			PdfPTable table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			PdfPCell cell = new PdfPCell(new Paragraph(""));
			cell.setBorder(Rectangle.TOP);
			table.addCell(cell);
			document.add(table);
//					document.add(Chunk.NEWLINE);

			PdfPTable hstable = new PdfPTable(3);
			hstable.setWidthPercentage(95);
			cell = new PdfPCell(new Phrase("ASSAY", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase("RESULT", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase("Units", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
			document.add(hstable);

//					document.add(Chunk.NEWLINE);
			table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			cell = new PdfPCell(new Paragraph(""));
			cell.setBorder(Rectangle.TOP);
			table.addCell(cell);
			document.add(table);

			for (Map.Entry<Integer, List<VitrosTestCodeDataDto>> mp : mp1.entrySet()) {
				table = new PdfPTable(3);
				table.setWidthPercentage(95);

				List<VitrosTestCodeDataDto> m = mp.getValue();
				for (VitrosTestCodeDataDto dt : m) {
					List<VitrosData> dataList = dt.getVitroData();
					for (VitrosData vd : dataList) {
						if (vd.isFinalize()) {
							cell = new PdfPCell(
									new Phrase(dt.getTestCode().getTestCode().getDisPalyTestCode(), regular));
							cell.setBorder(Rectangle.NO_BORDER);
							table.addCell(cell);
							cell = new PdfPCell(new Phrase(vd.getResult() + "", regular));
							cell.setBorder(Rectangle.NO_BORDER);
							table.addCell(cell);
							cell = new PdfPCell(new Phrase(
									dt.getTestCode().getTestCode().getTestCodeUints().getDisplayUnit(), regular));
							cell.setBorder(Rectangle.NO_BORDER);
							table.addCell(cell);
							document.add(table);
						}
					}
				}
			}

			table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			cell = new PdfPCell(new Paragraph(""));
			cell.setBorder(Rectangle.TOP);
			table.addCell(cell);
			document.add(table);

			vitrosFotterPart(document, regular);
		}

		document.close();

	}

	@SuppressWarnings("unused")
	private List<VistrosDataDto> vitroDataDtoListForPdf(StudyMaster study, InstrumentIpAddress ip, String animal,
			String startDate, String sampleType) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateTimeSecondsFormat"));
			SimpleDateFormat sdfloc = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Map<String, StudyTestCodes> testCodes = studyInstumentTestCodesMap(ip.getId(), study.getId(), null);
			List<String> testcodesset = new ArrayList<>(testCodes.keySet());
			for (Map.Entry<String, StudyTestCodes> e : testCodes.entrySet()) {
				System.out.println(e.getKey() + "----" + e.getValue().getOrderNo());
			}
			String[] studynosplit = study.getStudyNo().split("\\/");
			String studySuffiex = studynosplit[3];
			String month = studynosplit[1].substring(0, 2);
			String year = "20" + studynosplit[1].substring(02);

			Date from = null;
			Date to = null;
//			startDate = startDate.trim();
//			System.out.println(startDate.equals(""));
//			System.out.println(!startDate.equals(""));
			if (startDate == null || startDate.trim().equals("")) {
//				String startDateString = null;
//				String endDateString = null;
//				startDateString = "01-" + month + "-" + year + " 00:00:00";
//				boolean leapFalg = false;
//				if (month.equals("02")) {
//					int studyYear = Integer.parseInt(year);
//					if (studyYear % 4 == 0) {
//						if (studyYear % 100 == 0) {
//							if (studyYear % 400 == 0)
//								leapFalg = true;
//						} else
//							leapFalg = true;
//					}
//				}
//				if (leapFalg)
//					endDateString = "29-" + month + "-" + year + " 23:59:59";
//				else
//					endDateString = mounthEndDate.get(month) + "-" + month + "-" + year + " 23:59:59";
//				from = sdfloc.parse(startDateString);
//				System.out.println(sdfloc.format(from));
//				to = sdfloc.parse(endDateString);
			} else {
				SimpleDateFormat dateonly = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
				from = dateonly.parse(startDate);
				String[] st = startDate.split("\\-");
				String s = st[0] + "-" + st[1] + "-" + st[2];
				startDate = s;
				System.out.println(startDate.trim());
				Date fromDate = dateonly.parse(startDate.trim());
				Calendar c = Calendar.getInstance();
				c.setTime(fromDate);
				c.add(Calendar.DATE, 1);
				to = c.getTime();
			}

			System.out.println(from + "\t" + to);

			List<VitrosData> vitrosRawDataListTemp = studyDao.vitrosRawDataListWithStudyAndDate(study.getId(), from, to,
					animal, testcodesset, sampleType, null);
			Set<String> animals = new HashSet<>();
			Map<String, List<VitrosData>> animalWise = new HashMap<>();
			vitrosRawDataListTemp.stream().forEach((data) -> {
				List<VitrosData> list = animalWise.get(data.getAnimalNo());
				if (list == null)
					list = new ArrayList<>();
				list.add(data);
				animalWise.put(data.getAnimalNo(), list);
				System.out.println(data.getTestName());
				animals.add(data.getAnimalNo());
			});

			// animal groups
			List<String> animalList = new ArrayList<>(animals);
			Map<String, StudyAnimal> studyAnimals = groupingDao.animilGropInfo(study.getId(), animalList);
			SortedMap<Integer, GroupInfo> groups = new TreeMap<>();
//			List<GroupInfo> studyGrops = groupingDao.groupInfos(study.getId());
//			for(GroupInfo g : studyGrops) {
//				groups.put(g.getGroupNo(), g);
//				
//			}
			SortedMap<Long, List<StudyAnimal>> animalGroup = new TreeMap<>();
			studyAnimals.forEach((animNO, anim) -> {
				groups.put(anim.getGroupInfo().getGroupNo(), anim.getGroupInfo());
				List<StudyAnimal> animalslist = animalGroup.get(anim.getGroupInfo().getId());
				if (animalslist == null)
					animalslist = new ArrayList<>();
				animalslist.add(anim);
				animalGroup.put(anim.getGroupInfo().getId(), animalslist);
			});

			List<VistrosDataDto> dtolist = new ArrayList<>();
			for (Entry<Integer, GroupInfo> group : groups.entrySet()) {
				List<StudyAnimal> animalgroup = animalGroup.get(group.getValue().getId());
				SortedMap<Integer, StudyAnimal> animalMap = new TreeMap<>();
				for (StudyAnimal ani : animalgroup) {
					animalMap.put(ani.getSequnceNo(), ani);
				}
				for (Entry<Integer, StudyAnimal> animaleach : animalMap.entrySet()) {
					List<VitrosData> vitrosRawDataList = animalWise.get(animaleach.getValue().getPermanentNo());
					StudyAnimal studyAnimal = animaleach.getValue();
					String animalNumber = animaleach.getValue().getPermanentNo();

//					List<VitrosData> vitrosRawDataList = studyDao.vitrosRawDataListWithAnimalNo(studyAnimal.getPermanentNo(), from, to, studySuffiex);
					if (vitrosRawDataList.size() > 0) {

						Map<String, List<VitrosData>> animalWiseData = new HashMap<>();
						vitrosRawDataList.stream().forEach((data) -> {
							List<VitrosData> list = animalWiseData.get(data.getAnimalNo());
							if (list == null)
								list = new ArrayList<>();
							list.add(data);
							animalWiseData.put(data.getAnimalNo(), list);
						});
						SortedMap<Integer, StudyTestCodes> heading = new TreeMap<>();
//						StudyAnimal studyAnimal = groupingDao.studyAnimal(study.getId(), animal);

						PdfHeadderDto pdfHeasderDto = new PdfHeadderDto();
						pdfHeasderDto.setStudyNumber(study.getStudyNo());
						pdfHeasderDto.setGorupNam(studyAnimal.getGroupInfo().getGroupName());
						pdfHeasderDto.setAnimalNo(studyAnimal.getAnimalNo());
						VistrosDataDto dto = new VistrosDataDto();
						dto.setHeader(pdfHeasderDto);
						for (Map.Entry<String, StudyTestCodes> m : testCodes.entrySet()) {
							heading.put(m.getValue().getOrderNo(), m.getValue());
							System.out.println(m.getKey() + "\t" + m.getValue().getTestCode());
						}
						dto.setHeading(heading);
						int order = 1;
						Map<String, String> animalTestDoneDate = new HashedMap();
						Map<String, SortedMap<Integer, List<VitrosTestCodeDataDto>>> testCodeInfoList = new TreeMap<>();
						animalWiseData.forEach((animalNo, animalDataList) -> {
//							List<VitrosData> animalDataList = animalDataList;
							if (dto.getAnimalId() == null) {
								dto.setAnimalId(animalNumber);
								dto.setStudy(study);
							}

							SortedMap<String, List<VitrosTestCodeDataDto>> animaltestCodeInfoList = new TreeMap<>();
							animalDataList.stream().forEach((animalData) -> {
//								VitrosData animalData  = animalData;
								if (!animalTestDoneDate.containsKey(animalNo)) {
									if (animalNumber.equals(animalNo)) {
										pdfHeasderDto.setTestRunTime(sdf.format(animalData.getTestDate()));
									}
									animalTestDoneDate.put(animalNo, sdf.format(animalData.getTestDate()));
								}
								List<VitrosData> vitroData = new ArrayList<>();
								vitroData.add(animalData);
								VitrosTestCodeDataDto testcodeInfo = new VitrosTestCodeDataDto();
								testcodeInfo.setVitroData(vitroData);
								testcodeInfo.setTestCode(testCodes.get(animalData.getTestName()));
								try {
									testcodeInfo.setOrderNo(testcodeInfo.getTestCode().getOrderNo());
								} catch (Exception e) {
									// TODO: handle exception
									System.out.println(animalData.getTestName());
									e.printStackTrace();
								}
								List<VitrosTestCodeDataDto> list = animaltestCodeInfoList.get(animalData.getAnimalNo());
								if (list == null)
									list = new ArrayList<>();
								list.add(testcodeInfo);
								animaltestCodeInfoList.put(animalData.getAnimalNo(), list);
							});

							animaltestCodeInfoList.forEach((animalid, aniamlInfoDtoList) -> {
//								List<VitrosTestCodeDataDto> aniamlInfoDtoList = aniamlInfoDtoList
								Map<String, List<VitrosTestCodeDataDto>> animalTestCodeWise = new HashMap<>();
								for (VitrosTestCodeDataDto ctcdto : aniamlInfoDtoList) {
									try {
										List<VitrosTestCodeDataDto> eachTc = animalTestCodeWise
												.get(ctcdto.getTestCode().getTestCode().getTestCode());
										if (eachTc == null)
											eachTc = new ArrayList<>();
										eachTc.add(ctcdto);
										animalTestCodeWise.put(ctcdto.getTestCode().getTestCode().getTestCode(),
												eachTc);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								SortedMap<Integer, List<VitrosTestCodeDataDto>> mp = new TreeMap<>();
								List<VitrosTestCodeDataDto> prefix = new ArrayList<>();
								VitrosTestCodeDataDto c = new VitrosTestCodeDataDto();

								animalTestCodeWise.forEach((key, value) -> {
									mp.put(testCodes.get(key).getOrderNo(), value);
								});

								testCodeInfoList.put(animalid, mp);
								System.out.println("animalid : " + animalid);
							});

						});
						dto.setAnimalTestDoneDate(animalTestDoneDate);
						dto.setAnimalTcData(testCodeInfoList);
						dtolist.add(dto);
					}
				}
			}

			return dtolist;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

//	private void exportVitrosDataToPdf(List<VistrosDataDto> dtolist, String animalNo, LoginUsers user,
//			StudyMaster study, HttpServletRequest request, HttpServletResponse response, String file,
//			List<String> testcodesset) throws IOException, DocumentException {
//		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
//		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
//		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
//		String dateinSting = sdf.format(new Date());
//		Document document = new Document(new Rectangle(PageSize.A4));
//		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
//		document.open();
//		for (VistrosDataDto dto : dtolist) {
//			vitrosHeader(document, request);
////			for (VistrosDataDto dto : dtolist) {
//			animalNo = dto.getAnimalId();
//			dto.getHeader().setStudy(study);
//
//			SortedMap<Integer, List<VitrosTestCodeDataDto>> each = dto.getAnimalTcData().get(animalNo);
//			if (each != null) {
//				for (Map.Entry<Integer, List<VitrosTestCodeDataDto>> mp : each.entrySet()) {
//					List<VitrosTestCodeDataDto> lis = mp.getValue();
//					for (VitrosTestCodeDataDto obj1 : lis) {
//						dto.getHeader().setInstrument(obj1.getVitroData().getMachineName());
//						dto.getHeader().setTestRunTime(sdf.format(obj1.getVitroData().getTestDate()));
//						break;
//					}
//					break;
//				}
//			}
//			animalHeadderPart(dto.getHeader(), document);
////				document.add(Chunk.NEWLINE);
//			PdfPTable table = new PdfPTable(1);
//			table.setWidthPercentage(95f);
//			PdfPCell cell = new PdfPCell(new Paragraph(""));
//			cell.setBorder(Rectangle.TOP);
//			table.addCell(cell);
//			document.add(table);
////				document.add(Chunk.NEWLINE);
//
//			PdfPTable hstable = new PdfPTable(3);
//			hstable.setWidthPercentage(95);
//			cell = new PdfPCell(new Phrase("ASSAY", regular));
//			cell.setBorder(Rectangle.NO_BORDER);
//			hstable.addCell(cell);
//			cell = new PdfPCell(new Phrase("RESULT", regular));
//			cell.setBorder(Rectangle.NO_BORDER);
//			hstable.addCell(cell);
//			cell = new PdfPCell(new Phrase("Units", regular));
//			cell.setBorder(Rectangle.NO_BORDER);
//			hstable.addCell(cell);
//			document.add(hstable);
//
////				document.add(Chunk.NEWLINE);
//			table = new PdfPTable(1);
//			table.setWidthPercentage(95f);
//			cell = new PdfPCell(new Paragraph(""));
//			cell.setBorder(Rectangle.TOP);
//			table.addCell(cell);
//			document.add(table);
////				document.add(Chunk.NEWLINE);
//
////				Map<String, SortedMap<Integer, List<VitrosTestCodeDataDto>>> animalTcData;
//			System.out.println("Animal : " + animalNo);
//
//			if (each != null) {
//				for (Map.Entry<Integer, List<VitrosTestCodeDataDto>> mp : each.entrySet()) {
//					table = new PdfPTable(3);
//					table.setWidthPercentage(95);
//
//					String displayResult = "";
//					if (mp.getValue().size() == 1) {
//						displayResult = mp.getValue().get(0).getVitroData().getResult().toString();
//					} else {
//						List<VitrosTestCodeDataDto> li = mp.getValue();
//						for (VitrosTestCodeDataDto l : li) {
//							if (l.getVitroData().isFinalize())
//								displayResult = l.getVitroData().getResult().toString();
//						}
//					}
//
//					cell = new PdfPCell(
//							new Phrase(mp.getValue().get(0).getTestCode().getTestCode().getDisPalyTestCode(), regular));
//					cell.setBorder(Rectangle.NO_BORDER);
//					table.addCell(cell);
//					cell = new PdfPCell(new Phrase(displayResult, regular));
//					cell.setBorder(Rectangle.NO_BORDER);
//					table.addCell(cell);
//					cell = new PdfPCell(new Phrase(
//							mp.getValue().get(0).getTestCode().getTestCode().getTestCodeUints().getDisplayUnit(),
//							regular));
//					cell.setBorder(Rectangle.NO_BORDER);
//					table.addCell(cell);
////						cell = new PdfPCell(new Phrase("", regular));
////						cell.setBorder(Rectangle.NO_BORDER);
////						table.addCell(cell);
//
//					document.add(table);
//				}
//
//			}
//			vitrosFotterPart(document, regular);
//
//		}
//
//		List<VitrosData> vitros = studyDao.vitrosRawDataListWithStudyAndDate(study.getId(), null, null, "QC", null,
//				null);
//		Map<String, List<VitrosData>> eachQCSample = new HashMap<>();
//		for (VitrosData vt : vitros) {
//			List<VitrosData> li = eachQCSample.get(vt.getAnimalNo());
//			if (li == null) {
//				li = new ArrayList<>();
//			}
//			li.add(vt);
//			eachQCSample.put(vt.getAnimalNo(), li);
//		}
//
//		for (Map.Entry<String, List<VitrosData>> m : eachQCSample.entrySet()) {
//			if (m.getValue().size() > 0) {
//				VitrosData v = m.getValue().get(0);
//				vitrosHeader(document, request);
//				PdfHeadderDto header = new PdfHeadderDto();
//				header.setStudy(study);
//				header.setInstrument(v.getMachineName());
//				header.setTestRunTime(sdf.format(v.getTestDate()));
//				header.setAnimalNo(v.getAnimalNo());
//
//				animalHeadderPart(header, document);
////				document.add(Chunk.NEWLINE);
//				PdfPTable table = new PdfPTable(1);
//				table.setWidthPercentage(95f);
//				PdfPCell cell = new PdfPCell(new Paragraph(""));
//				cell.setBorder(Rectangle.TOP);
//				table.addCell(cell);
//				document.add(table);
////				document.add(Chunk.NEWLINE);
//
//				PdfPTable hstable = new PdfPTable(3);
//				hstable.setWidthPercentage(95);
//				cell = new PdfPCell(new Phrase("ASSAY", regular));
//				cell.setBorder(Rectangle.NO_BORDER);
//				hstable.addCell(cell);
//				cell = new PdfPCell(new Phrase("RESULT", regular));
//				cell.setBorder(Rectangle.NO_BORDER);
//				hstable.addCell(cell);
//				cell = new PdfPCell(new Phrase("Units", regular));
//				cell.setBorder(Rectangle.NO_BORDER);
//				hstable.addCell(cell);
//				document.add(hstable);
//
////				document.add(Chunk.NEWLINE);
//				table = new PdfPTable(1);
//				table.setWidthPercentage(95f);
//				cell = new PdfPCell(new Paragraph(""));
//				cell.setBorder(Rectangle.TOP);
//				table.addCell(cell);
//				document.add(table);
//
//				vitrosFotterPart(document, regular);
//			}
//
//		}
//		document.close();
//
//	}

	private void vitrosFotterPart(Document document, com.itextpdf.text.Font regular) throws DocumentException {

		// TODO Auto-generated method stub
//		document.add(Chunk.NEWLINE);
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		PdfPCell cell = new PdfPCell(new Paragraph(""));
		cell.setBorder(Rectangle.TOP);
		table.addCell(cell);
		document.add(table);
//		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("\n"));
//	}
		table = new PdfPTable(1);
		table.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("End of the Report ", regular));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		document.add(new Paragraph("\n"));
		table = new PdfPTable(1);
		table.setWidthPercentage(95);
//	table.setHorizontalAlignment(Element.ALIGN_LEFT);
//	Paragraph analysedBy = new Paragraph("Analysed By  : " + user.getFullName(), regular);
		Paragraph analysedBy = new Paragraph("Analysed By  : ", regular);
		cell = new PdfPCell(analysedBy);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
//	table.setHorizontalAlignment(Element.ALIGN_LEFT);
//	table.setWidthPercentage(30);
		table.setWidthPercentage(95);
//	Paragraph analysedOn = new Paragraph("Analysed On  : " + sdf.format(new Date()), regular);
		Paragraph analysedOn = new Paragraph("Sign and Date  : ", regular);
		cell = new PdfPCell(analysedOn);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

	}

	private void vitrosHeading(Document document, HttpServletRequest request)
			throws MalformedURLException, IOException, DocumentException {
		document.newPage();
		includeLogo(request, document);

		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		com.itextpdf.text.Font heading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12,
				com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font sideheading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10,
				com.itextpdf.text.Font.BOLD);
		PdfPCell cell = null;

		int tableCount = 1;
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		int count = 1;
		Paragraph pa = new Paragraph("VIMTA LABS LTD", heading);
		pa.setAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(pa);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(30f);
//         cell.setColspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		Phrase p = new Phrase("Clinical Chemistry Parameters", heading);
		cell = new PdfPCell(p);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(30f);
//         cell.setColspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);
	}

	private void includeLogo(HttpServletRequest request, Document document)
			throws MalformedURLException, IOException, DocumentException {
		// TODO Auto-generated method stub
		String logo = request.getServletContext().getRealPath("/static/images/vimta.png");
		Image logoimg = Image.getInstance(logo);
		logoimg.scaleAbsolute(100, 40);
//		logoimg.setAbsolutePosition(100, 100);
		document.add(logoimg);
	}

	@Override
	public SortedMap<Integer, StudyAnimal> studyAnimals(Long studyId) {
		List<StudyAnimal> animals = studyDao.studyAnimals(studyId);
		SortedMap<Integer, StudyAnimal> map = new TreeMap<>();
		for (StudyAnimal animal : animals) {
			map.put(animal.getAnimalId(), animal);
		}
		return map;
	}

	@Override
	public String exportStagoDataToPdf(HttpServletRequest request, HttpServletResponse response, Long studyId,
			List<Long> animalIds, String realPath, String startDate, String sampleType, String observation)
			throws ParseException, IOException, DocumentException {
		Long userId = (Long) request.getSession().getAttribute("userId");
		LoginUsers user = userService.findById(userId);
		analysedBy = user.getUsername();
		String file = checktheFile(realPath, "stago.pdf");
		System.out.println(file);

		Map<String, StagoDataDto> sysmexDataList = stagoDataList(studyId, animalIds, true, startDate, sampleType, observation);
		exportStagoDataToPdf(sysmexDataList, request, response, file, user, studyId, startDate, sampleType, observation);
		return file;
	}

	private Map<String, StagoDataDto> stagoDataList(Long studyId, List<Long> animalIds, Boolean export,
			String startDate, String sampleType, String observation) throws ParseException {
		return studyDao.stagoDataList(studyId, animalIds, true, startDate, sampleType, observation);
	}

	private void exportStagoDataToPdf(Map<String, StagoDataDto> stagoData, HttpServletRequest request,
			HttpServletResponse response, String file, LoginUsers user, Long studyId, String startDate,
			String sampleType, String observation) throws IOException, DocumentException, ParseException {
		StudyMaster study = studyDao.findByStudyId(studyId);
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		SimpleDateFormat sdftime = new SimpleDateFormat(environment.getRequiredProperty("dateTimeSecondsFormat"));
		SimpleDateFormat time = new SimpleDateFormat(environment.getRequiredProperty("timeSecondsFormat"));
		String studyNo = null;
//		String date = sdf.format(new Date());
//		String loatNo = null;
		StagoDataDto singleAnimal = null;
		System.out.println("stagoData  : " + stagoData);
		System.out.println("stagoData.size()  : " + stagoData.size());
		for (Map.Entry<String, StagoDataDto> sdm : stagoData.entrySet()) {
			studyNo = sdm.getValue().getStudy().getStudyNo();
//			if (sdm.getValue().getLotNo() != null && !sdm.getValue().getLotNo().equals("null")
//					&& !sdm.getValue().getLotNo().equals("N/A"))
//				loatNo = sdm.getValue().getLotNo();
			singleAnimal = sdm.getValue();
			break;
		}

		String dateinSting = sdf.format(new Date());
		Document document = new Document(new Rectangle(PageSize.A4));
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		includeLogo(request, document);

//		document.addTitle("Total Roles List");
//		document.setPageSize(PageSize.A4);
//		document.setMargins(80, 80, 50, 80);
//		document.setMarginMirroring(false);
//		document.open();
		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		com.itextpdf.text.Font heading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12,
				com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font sideheading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10,
				com.itextpdf.text.Font.BOLD);
		PdfPCell cell = null;
		PdfPTable table = null;
		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		int count = 1;
		Paragraph pa = new Paragraph("VIMTA LABS LTD", heading);
		pa.setAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(pa);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(30f);
//         cell.setColspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		pa = new Paragraph("Coagulation Parameters", heading);
		pa.setAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(pa);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(30f);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);
//		document.add(new Paragraph("\n\n"));
//		document.add(new Paragraph("\n\n"));
//		document.add(new Paragraph("\n\n"));

		String doneBy = "";
		String doneDate = "";
		for (Map.Entry<String, StagoDataDto> eachAnimal : stagoData.entrySet()) {
//			StagoDataDto dto = eachAnimal.getValue();
			try {
//				doneBy = dto.getPtResultDoneBy();	
				doneDate = startDate;
				break;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

		PdfHeadderDto header = new PdfHeadderDto();
		header.setStudy(study);
		header.setStudyNumber(studyNo);
		header.setTestRunTime(startDate);
		header.setInstrument("ST4");
		for (Map.Entry<String, StagoDataDto> eachAnimal : stagoData.entrySet()) {
			header.setInstrument(eachAnimal.getValue().getInstrumentName());
			break;
		}
		animalHeadderPart(header, document, false);
		document.add(new Paragraph("\n"));
		PdfPTable hstable = null;

		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		cell = new PdfPCell(new Paragraph(""));
		cell.setBorder(Rectangle.TOP);
		table.addCell(cell);
		document.add(table);

		if (sampleType.equals("Animal") && observation.equals("Treatment")) {
			hstable = new PdfPTable(2 + (singleAnimal.getSelecteTestCodes().size() * 2));
			hstable.setWidthPercentage(95);
			cell = new PdfPCell(new Phrase("Group", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase("Animal No", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
		} else if (sampleType.equals("Animal")) {
			System.out.println("singleAnimal : " + singleAnimal);
			System.out.println("singleAnimal.getSelecteTestCodes() : " + singleAnimal.getSelecteTestCodes());
			hstable = new PdfPTable(1 + (singleAnimal.getSelecteTestCodes().size() * 2));
			hstable.setWidthPercentage(95);
			cell = new PdfPCell(new Phrase("Animal No", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
		} else {
			hstable = new PdfPTable(2 + (singleAnimal.getSelecteTestCodes().size() * 2));
			hstable.setWidthPercentage(95);
			cell = new PdfPCell(new Phrase("Lot No", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase("S.No.", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
		}

		for (Map.Entry<Integer, StudyTestCodes> m : singleAnimal.getSelecteTestCodes().entrySet()) {
			cell = new PdfPCell(new Phrase(m.getValue().getTestCode().getTestCode() + "(sec)", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase("Time", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
		}
		document.add(hstable);
		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		cell = new PdfPCell(new Paragraph(""));
		cell.setBorder(Rectangle.TOP);
		table.addCell(cell);
		document.add(table);
//		document.add(Chunk.NEWLINE);
//		document.add(Chunk.NEWLINE);
//		document.add(Chunk.NEWLINE);
//		document.add(new Paragraph("\n"));
		table.setSpacingAfter(20f);
		for (Map.Entry<String, StagoDataDto> eachAnimal : stagoData.entrySet()) {
			if (sampleType.equals("Animal") && observation.equals("Treatment")) {
				hstable = new PdfPTable(2 + (singleAnimal.getSelecteTestCodes().size() * 2));
				hstable.setWidthPercentage(95);
				cell = new PdfPCell(new Phrase(eachAnimal.getValue().getGroupName(), regular));
				cell.setBorder(Rectangle.NO_BORDER);
				hstable.addCell(cell);
				cell = new PdfPCell(new Phrase(eachAnimal.getValue().getAnimal().getPermanentNo(), regular));
				cell.setBorder(Rectangle.NO_BORDER);
				hstable.addCell(cell);
			}else if (sampleType.equals("Animal")) {
				hstable = new PdfPTable(1 + (singleAnimal.getSelecteTestCodes().size() * 2));
				hstable.setWidthPercentage(95);
				cell = new PdfPCell(new Phrase(eachAnimal.getValue().getAnimal().getAnimalNo(), regular));
				cell.setBorder(Rectangle.NO_BORDER);
				hstable.addCell(cell);
			}else {
				hstable = new PdfPTable(2 + (singleAnimal.getSelecteTestCodes().size() * 2));
				hstable.setWidthPercentage(95);
				cell = new PdfPCell(new Phrase(eachAnimal.getValue().getLotNo(), regular));
				cell.setBorder(Rectangle.NO_BORDER);
				hstable.addCell(cell);
				cell = new PdfPCell(new Phrase(""+eachAnimal.getKey(), regular));
				cell.setBorder(Rectangle.NO_BORDER);
				hstable.addCell(cell);
			}

//			cell = new PdfPCell(new Phrase(eachAnimal.getKey(), regular));
//			cell.setBorder(Rectangle.NO_BORDER);
//			hstable.addCell(cell);

			for (Map.Entry<Integer, StudyTestCodes> m : singleAnimal.getSelecteTestCodes().entrySet()) {
				if (m.getValue().getTestCode().getTestCode().equals("PT")) {
					cell = new PdfPCell(new Phrase(eachAnimal.getValue().getPtResult(), regular));
					cell.setBorder(Rectangle.NO_BORDER);
					hstable.addCell(cell);
					if (!eachAnimal.getValue().getPtResultTime().equals(""))
						cell = new PdfPCell(new Phrase(
								time.format(sdftime.parse(eachAnimal.getValue().getPtResultTime())), regular));
					else
						cell = new PdfPCell(new Phrase("", regular));
					cell.setBorder(Rectangle.NO_BORDER);
					hstable.addCell(cell);
				} else if (m.getValue().getTestCode().getTestCode().equals("APTT")) {
					cell = new PdfPCell(new Phrase(eachAnimal.getValue().getApttResult(), regular));
					cell.setBorder(Rectangle.NO_BORDER);
					hstable.addCell(cell);
					if (!eachAnimal.getValue().getApttResultTime().equals(""))
						cell = new PdfPCell(new Phrase(
								time.format(sdftime.parse(eachAnimal.getValue().getApttResultTime())), regular));
					else
						cell = new PdfPCell(new Phrase("", regular));
					cell.setBorder(Rectangle.NO_BORDER);
					hstable.addCell(cell);
				} else if (m.getValue().getTestCode().getTestCode().equals("Fibrinogen")) {
					cell = new PdfPCell(new Phrase(eachAnimal.getValue().getFibrinogenResult(), regular));
					cell.setBorder(Rectangle.NO_BORDER);
					hstable.addCell(cell);
					if (!eachAnimal.getValue().getFibrinogenResultTime().equals(""))
						cell = new PdfPCell(new Phrase(
								time.format(sdftime.parse(eachAnimal.getValue().getFibrinogenResultTime())), regular));
					else
						cell = new PdfPCell(new Phrase("", regular));

					cell.setBorder(Rectangle.NO_BORDER);
					hstable.addCell(cell);
				}
			}
			document.add(hstable);
		}
		table.setSpacingAfter(20f);
//		document.add(new Paragraph("\n"));
//		document.add(Chunk.NEWLINE);
//		document.add(Chunk.NEWLINE);
//		document.add(Chunk.NEWLINE);
		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		cell = new PdfPCell(new Paragraph(""));
		cell.setBorder(Rectangle.TOP);
		table.addCell(cell);
		document.add(table);
//		document.add(new Paragraph("\n"));
//		document.add(new Paragraph("\n\n"));
//		document.add(new Paragraph("\n\n"));

		table = new PdfPTable(1);
		table.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("End of the Report ", regular));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		document.add(new Paragraph("\n"));
		table = new PdfPTable(1);
		table.setWidthPercentage(95);
//		table.setHorizontalAlignment(Element.ALIGN_LEFT);
//		Paragraph analysedBy = new Paragraph("Analysed By  : " + user.getFullName(), regular);
		Paragraph analysedBy = new Paragraph("Analysed By  : ", regular);
		cell = new PdfPCell(analysedBy);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
//		table.setHorizontalAlignment(Element.ALIGN_LEFT);
//		table.setWidthPercentage(30);
		table.setWidthPercentage(95);
//		Paragraph analysedOn = new Paragraph("Analysed On  : " + sdf.format(new Date()), regular);
		Paragraph analysedOn = new Paragraph("Sign and Date  : ", regular);
		cell = new PdfPCell(analysedOn);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		document.close();

	}

	private Map<String, SortedMap<Integer, PdfPCell>> stagoTestPdfResultCells(StagoDataDto singleAnimal,
			com.itextpdf.text.Font regular, Map<Integer, StudyTestCodes> testCodes) throws ParseException {
		// TODO Auto-generated method stub
		SimpleDateFormat sdftime = new SimpleDateFormat(environment.getRequiredProperty("dateTimeSecondsFormat"));
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		SimpleDateFormat time = new SimpleDateFormat(environment.getRequiredProperty("timeSecondsFormat"));
		PdfPCell cell = null;
		Map<String, SortedMap<Integer, PdfPCell>> tcCelles = new HashMap<>();
		if (singleAnimal.getTcs().contains("PT")) {
			SortedMap<Integer, PdfPCell> m = new TreeMap<>();
			String result[] = singleAnimal.getPtResult().split("\\/");
			cell = new PdfPCell(new Phrase(result[0], regular));
			cell.setBorder(Rectangle.NO_BORDER);
			m.put(1, cell);
//			try {
//			cell = new PdfPCell(new Phrase(sdf.format(sdftime.parse(singleAnimal.getPtResultTime())), regular));
//			}catch (Exception e) {
//				// TODO: handle exception
//				cell = new PdfPCell(new Phrase("", regular));
//			}
//			cell.setBorder(Rectangle.NO_BORDER);
//			m.put(2, cell);
			try {
				cell = new PdfPCell(new Phrase(time.format(sdftime.parse(singleAnimal.getPtResultTime())), regular));
			} catch (Exception e) {
				// TODO: handle exception
				cell = new PdfPCell(new Phrase("", regular));
			}

			cell.setBorder(Rectangle.NO_BORDER);
			m.put(2, cell);
//			cell = new PdfPCell(new Phrase(singleAnimal.getPtResultDoneBy(), regular));
//			cell.setBorder(Rectangle.NO_BORDER);
//			m.put(4, cell);
			tcCelles.put("PT", m);
		}
		if (singleAnimal.getTcs().contains("APTT")) {
			SortedMap<Integer, PdfPCell> m = new TreeMap<>();
			String result[] = singleAnimal.getApttResult().split("\\/");
			cell = new PdfPCell(new Phrase(result[0], regular));
			cell.setBorder(Rectangle.NO_BORDER);
			m.put(1, cell);
//			try {
//				cell = new PdfPCell(new Phrase(sdf.format(sdftime.parse(singleAnimal.getApttResultTime())), regular));
//				}catch (Exception e) {
//					// TODO: handle exception
//					cell = new PdfPCell(new Phrase("", regular));
//				}
//			cell.setBorder(Rectangle.NO_BORDER);
//			m.put(2, cell);
			try {
				cell = new PdfPCell(new Phrase(time.format(sdftime.parse(singleAnimal.getApttResultTime())), regular));
			} catch (Exception e) {
				// TODO: handle exception
				cell = new PdfPCell(new Phrase("", regular));
			}

			cell.setBorder(Rectangle.NO_BORDER);
			m.put(2, cell);
//			cell = new PdfPCell(new Phrase(singleAnimal.getApttResultDoneBy(), regular));
//			cell.setBorder(Rectangle.NO_BORDER);
//			m.put(4, cell);
			tcCelles.put("APTT", m);
		}
		if (singleAnimal.getTcs().contains("FIBRINOGEN")) {
			SortedMap<Integer, PdfPCell> m = new TreeMap<>();
			String result[] = singleAnimal.getFibrinogenResult().split("\\/");
			cell = new PdfPCell(new Phrase(result[0], regular));
			cell.setBorder(Rectangle.NO_BORDER);
			m.put(1, cell);
//			cell = new PdfPCell(new Phrase(sdf.format(sdftime.parse(singleAnimal.getFibrinogenResultTime())), regular));
//			cell.setBorder(Rectangle.NO_BORDER);
//			m.put(2, cell);
			cell = new PdfPCell(
					new Phrase(time.format(sdftime.parse(singleAnimal.getFibrinogenResultTime())), regular));
			cell.setBorder(Rectangle.NO_BORDER);
			m.put(2, cell);
//			cell = new PdfPCell(new Phrase(singleAnimal.getFibrinogenResultDoneBy(), regular));
//			cell.setBorder(Rectangle.NO_BORDER);
//			m.put(4, cell);
			tcCelles.put("FIBRINOGEN", m);
		}
		return tcCelles;
	}

	private void animalHeadderPart(PdfHeadderDto header, Document document, boolean animal ) throws DocumentException {
		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		PdfPTable hstable = null;
		PdfPCell cell = null;
		hstable = new PdfPTable(2);
		hstable.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("Study : " + header.getStudy().getStudyNo(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Test Run Date : " + header.getTestRunTime(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_RIGHT);
		hstable.addCell(cell);

		document.add(hstable);
		if (!header.getGorupNam().equals("") || !header.getInstrument().equals("")) {
			hstable = new PdfPTable(2);
			hstable.setWidthPercentage(95);

			cell = new PdfPCell(new Phrase("Group : " + header.getGorupNam(), regular));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setVerticalAlignment(Element.ALIGN_LEFT);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase("Insturment Id : " + header.getInstrument(), regular));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setVerticalAlignment(Element.ALIGN_LEFT);
			hstable.addCell(cell);
			document.add(hstable);
		}
		if (!header.getAnimalNo().equals("")) {
			cell = new PdfPCell(new Phrase("Species : " + header.getStudy().getSpecies().getName(), regular));

		} else {
			cell = new PdfPCell(new Phrase("Species : ", regular)); // Remove "Species " in pojo
		}

		if (header.getStudy().getSpecies() != null || !header.getAnimalNo().equals("")) {
			hstable = new PdfPTable(2);
			hstable.setWidthPercentage(95);
			cell = new PdfPCell(new Phrase("Species : " + header.getStudy().getSpecies().getName(), regular));

			cell.setBorder(Rectangle.NO_BORDER);
			cell.setVerticalAlignment(Element.ALIGN_LEFT);
			hstable.addCell(cell);
			if(animal)
				cell = new PdfPCell(new Phrase("Animal Id : " + header.getAnimalNo(), regular));
			else
				cell = new PdfPCell(new Phrase("", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setVerticalAlignment(Element.ALIGN_LEFT);
			hstable.addCell(cell);
			document.add(hstable);
		}
		
//		document.add(Chunk.NEWLINE);
	}

	@Override
	public VistrosDataUpdateDto vitrosDataList(Long studyId, String animalNo, int columnIndex) {
		// TODO Auto-generated method stub

		return studyDao.vitrosDataList(studyId, animalNo, columnIndex);
	}

	@Override
	public SysmexDataUpdateDto sysmexDataList(Long studyId, String animalNo, int columnIndexOrOrderNo, String observation, String stagostDate)  throws ParseException{
		// TODO Auto-generated method stub

		return studyDao.sysmexDataList(studyId, animalNo, columnIndexOrOrderNo, observation, stagostDate);
	}

	@Override
	public String vitrosResultSelectionSave(Long studyId, String animalNo, String testName, int columnIndex,
			Long finalResultId, String rerunCommnet) {
		List<VitrosData> list = studyDao.vitrosDataListData(studyId, animalNo, testName);
//		List<VitrosDataLog>
		StringBuffer sb = new StringBuffer();
		boolean fg = false;
		for (VitrosData sd : list) {
			if (sd.getId().equals(finalResultId)) {
				if (!sd.isFinalize()) {
					sd.setRerunCommnet(rerunCommnet);
					sd.setFinalize(true);
				}

				String rel = "<font color='green'>" + sd.getResult() + "</font>";
				if (fg) {
					sb.append(",").append(rel);
				} else {
					sb.append(rel);
					fg = true;
				}
			} else {
				if (fg) {
					sb.append(",").append(sd.getResult());
				} else {
					sb.append(sd.getResult());
					fg = true;
				}
				if (sd.isFinalize()) {
					sd.setRerunCommnet(rerunCommnet);
					sd.setFinalize(false);
				}
			}
		}
		studyDao.updateVitrosDataList(list);

		return "{\"Success\": \"" + true + "\",\"Message\":\"Data Updated successfully\", \"value\":\"" + sb.toString()
				+ "\"}";
	}

	@Override
	public String sysmexResultSelectionSave(Long studyId, String animalNo, String testName, int columnIndex,
			Long finalResultId, String rerunCommnet, String observation, String stagostDate) throws ParseException{
		List<SysmexTestCodeData> list = studyDao.sysmexDataListData(studyId, animalNo, testName, observation, stagostDate);
		StringBuffer sb = new StringBuffer();
		boolean fg = false;
		for (SysmexTestCodeData sd : list) {
			System.out.println(sd.getId() + "\t" + finalResultId + "\t" + (sd.getId().equals(finalResultId)));
			if (sd.getId().equals(finalResultId)) {
				if (!sd.isFinalValue()) {
					sd.setRerunCommnet(rerunCommnet);
					sd.setFinalValue(true);
				}
				String rel = "<font color='green'>" + sd.getValue() + "</font>";
				if (fg) {
					sb.append(",").append(rel);
				} else {
					sb.append(rel);
					fg = true;
				}
			} else {
				if (fg) {
					sb.append(",").append(sd.getValue());
				} else {
					sb.append(sd.getValue());
					fg = true;
				}
				if (sd.isFinalValue()) {
					sd.setRerunCommnet(rerunCommnet);
					sd.setFinalValue(false);
				}
			}
		}
		studyDao.updateSysmexDataList(list);

		return "{\"Success\": \"" + true + "\",\"Message\":\"Data Updated successfully\", \"value\":\"" + sb.toString()
				+ "\"}";
	}

	@Override
	public List<VitrosData> onlineVitrosData() {
		// TODO Auto-generated method stub
		return studyDao.onlineVitrosData();
	}

	@Override
	public void updateAsOnline(List<VitrosData> data) {
		// TODO Auto-generated method stub
		studyDao.updateAsOnline(data);
	}

	@Override
	public List<StagoDataAnimalDto> studyStagoData(Long studyNumber, String startDate, boolean exprot,
			String sampleType, List<StudyTestCodes> testCodes) throws ParseException {
		// TODO Auto-generated method stub
		SortedMap<Integer, StudyTestCodes> sortedTcs = new TreeMap<>();
		for (StudyTestCodes tc : testCodes) {
			sortedTcs.put(tc.getOrderNo(), tc);
		}

		SimpleDateFormat sdftime = new SimpleDateFormat(environment.getRequiredProperty("dateTimeSecondsFormat"));

		SortedMap<Long, List<StagoData>> animalWiseData = new TreeMap<>();
		SortedMap<Long, StudyAnimal> animals = new TreeMap<>();
		List<StagoData> data = studyDao.studyStagoData(studyNumber, startDate, exprot, sampleType);
		for (StagoData sd : data) {
//			if(exprot ) {
//				List<StagoData> sdl = animalWiseData.get(sd.getStudyAnimal().getId());
//				if(sdl == null) sdl = new ArrayList<>();
//				if(sd.isSelectedResult()) {
//					sdl.add(sd);
//					animalWiseData.put(sd.getStudyAnimal().getId(), sdl);
//				}
//			}else {
			List<StagoData> sdl = animalWiseData.get(sd.getStudyAnimal().getId());
			if (sdl == null)
				sdl = new ArrayList<>();
			sdl.add(sd);
			animalWiseData.put(sd.getStudyAnimal().getId(), sdl);
//			}
			animals.put(sd.getStudyAnimal().getId(), sd.getStudyAnimal());
		}
		SortedMap<Integer, GroupInfo> groups = new TreeMap<>();
		SortedMap<Long, SortedMap<Integer, StudyAnimal>> gorupsAnimals = new TreeMap<>();
		for (Entry<Long, StudyAnimal> ani : animals.entrySet()) {
			groups.put(ani.getValue().getGroupInfo().getGroupNo(), ani.getValue().getGroupInfo());
			SortedMap<Integer, StudyAnimal> gorupsAnimal = gorupsAnimals.get(ani.getValue().getGroupInfo().getId());
			if (gorupsAnimal == null)
				gorupsAnimal = new TreeMap<>();
			gorupsAnimal.put(ani.getValue().getAnimalId(), ani.getValue());
			gorupsAnimals.put(ani.getValue().getGroupInfo().getId(), gorupsAnimal);
		}

		List<StagoDataAnimalDto> rows = new ArrayList<>();
		for (Entry<Integer, GroupInfo> group : groups.entrySet()) {
			SortedMap<Integer, StudyAnimal> gorupsAnimal = gorupsAnimals.get(group.getValue().getId());
			for (Entry<Integer, StudyAnimal> animal : gorupsAnimal.entrySet()) {
				StagoDataAnimalDto dto = new StagoDataAnimalDto();
				dto.setGroupName(group.getValue().getGroupName());
				dto.setGroupNo(group.getValue().getGroupNo());
				dto.setAnimalName(animal.getValue().getPermanentNo());
				dto.setAnimalNo(animal.getValue().getAnimalId());
				SortedMap<Integer, String> receivedTimes = new TreeMap<>();

				List<StagoData> animalData = animalWiseData.get(animal.getValue().getId());
				int orderCount = 1;
				SortedMap<Integer, String> tcdata = new TreeMap<>();
				String lotNo = null;
				for (StagoData sd : animalData) {
					String value = sd.getTestResult();
					if (sd.isSelectedResult())
						value = "<font color='green'>" + value + "</font>";
					if (tcdata.containsKey(sd.getTestCode().getOrderNo())) {
						tcdata.put(sd.getTestCode().getOrderNo(),
								tcdata.get(sd.getTestCode().getOrderNo()) + "," + value);
					} else {
						tcdata.put(sd.getTestCode().getOrderNo(), value);
					}
					if (lotNo == null && sd.getLotNo() != null && !sd.getLotNo().equals("")) {
						lotNo = sd.getLotNo();
					}
					if (!receivedTimes.containsKey(sd.getTestCode().getOrderNo())) {
						receivedTimes.put(sd.getTestCode().getOrderNo(), sdftime.format(sd.getReceivedTime()));
					}
				}
				if (lotNo == null)
					lotNo = "";
				SortedMap<Integer, String> tcdataDispay = new TreeMap<>();

				tcdataDispay.put(orderCount++, dto.getGroupName());
				tcdataDispay.put(orderCount++, dto.getAnimalName());
				if (!sampleType.equals("Animal")) {
					tcdataDispay.put(orderCount++, lotNo);
				}

				for (Entry<Integer, StudyTestCodes> tc : sortedTcs.entrySet()) {
					tcdataDispay.put(orderCount++, tcdata.get(tc.getKey()));
					tcdataDispay.put(orderCount++, receivedTimes.get(tc.getKey()));
				}
//				for(Entry<Integer, String> entry : tcdata.entrySet()) {
//					tcdataDispay.put(orderCount++, entry.getValue());
//					tcdataDispay.put(orderCount++, receivedTimes.get(entry.getKey()));
//				}
				dto.setReceivedTimes(receivedTimes);
				dto.setTcdataDispay(tcdataDispay);
				rows.add(dto);
			}
		}
		return rows;
	}

//	/*
//	  (non-Javadoc)
//	  @see com.springmvc.service.StudyService#sysmexDataDtoTable(java.lang.Long,  java.util.List, java.lang.String) 
//		1. All test Study test codes with order by order no. [ORDER , STUDY_TESTCODES] 
//		2. Study Animal <Animal PK, STUDY_ANIMAL>
//	    3. each animal all the data <Animal PK , SYSMEX_DATA> 
//		4. List of animala of order by AnimalId(1,2,3,..) 
//		5. SysmexRowiseDto(StudyMaster study, GroupInfo group, StudyAnimal animal, String runTime, SortedMap<Integer,SysmexTestCodeDataDto> elementValues) 
//		   elementValues = <StudyTestCodes-orderNO, SysmexTestCodeDataDto> 
//		   SysmexTestCodeDataDto(StudyTestCodes testCode, String resultToDisplay, boolean mutipleReuslt)
//	 
//	result = SysmexDataDto -> List<SysmexRowiseDto> sysmexRowiseDtos;
//	 */
	@Override
	public SysmexDataDto sysmexDataDtoTable(Long studyId, List<SysmexData> sysmexDataList, String string) {
		// TODO Auto-generated method stub
		try {
			SysmexDataDto sddto = new SysmexDataDto();

			SimpleDateFormat dateTimeSecondsFormat = new SimpleDateFormat(
					environment.getRequiredProperty("dateTimeSecondsFormat"));
			InstrumentIpAddress ip = studyDao.instumentIpAddress("SYSMEX");
			// 1.
			List<StudyTestCodes> tcs = studyInstumentTestCodes(ip.getId(), studyId, null);
			Collections.sort(tcs, new Comparator<StudyTestCodes>() {
				@Override
				public int compare(StudyTestCodes o1, StudyTestCodes o2) {
					return o1.getOrderNo() - o2.getOrderNo();
				}
			});
			sddto.setTestCodes(tcs);
			// 2.
			Map<Long, StudyAnimal> animalMap = new HashMap<>();
			// 3.
			Map<Long, List<SysmexData>> animalData = new HashMap<>();
			for (SysmexData data : sysmexDataList) {
				if (!animalMap.containsKey(data.getAnimal().getId())) {
					animalMap.put(data.getAnimal().getId(), data.getAnimal());
				}
				List<SysmexData> dataList = animalData.get(data.getAnimal().getId());
				if (dataList == null)
					dataList = new ArrayList<>();
				dataList.add(data);
				animalData.put(data.getAnimal().getId(), dataList);
			}

			// 4.
			List<StudyAnimal> animalList = new ArrayList<>();
			animalList.addAll(animalMap.values());
			Collections.sort(animalList, new Comparator<StudyAnimal>() {
				@Override
				public int compare(StudyAnimal o1, StudyAnimal o2) {
					return o1.getAnimalId() - o2.getAnimalId();
				}
			});

			// 5.
			List<SysmexRowiseDto> sysmexRowiseDtos = new ArrayList<>();
			for (StudyAnimal animal : animalList) {
				SysmexRowiseDto dto = new SysmexRowiseDto();
				dto.setStudy(animal.getStudy());
				dto.setGroup(animal.getGroupInfo());
				dto.setAnimal(animal);
				System.out.println(animal.getPermanentNo());
				List<SysmexData> dataList = animalData.get(animal.getId());
				dto.setRunTime(dateTimeSecondsFormat.format(dataList.get(0).getStartTime()));

				// MAP<StudyTestCode-orderNo, List<SysmexTestCodeData>>
				Map<Integer, List<SysmexTestCodeData>> testcodeWiseData = new HashMap<>();
				for (SysmexData sd : dataList) {
					List<SysmexTestCodeData> tcData = sd.getSysmexTestCodeData();
					for (SysmexTestCodeData stcd : tcData) {
						if (stcd.getStudyTestCode() != null) {
							List<SysmexTestCodeData> list = testcodeWiseData.get(stcd.getStudyTestCode().getOrderNo());
							if (list == null)
								list = new ArrayList<>();
							list.add(stcd);
							testcodeWiseData.put(stcd.getStudyTestCode().getOrderNo(), list);
						}
					}
				}

				SortedMap<Integer, SysmexTestCodeDataDto> elementValues = new TreeMap<>();
				for (StudyTestCodes tc : tcs) {
					System.out.println(tc.getTestCode().getDisPalyTestCode() + "\t" + tc.getOrderNo());
					SysmexTestCodeDataDto ddto = new SysmexTestCodeDataDto();
					ddto.setTestCode(tc);
					List<SysmexTestCodeData> list = testcodeWiseData.get(tc.getOrderNo());
					if (list != null && list.size() > 0) {
						if (list.size() > 1) {
							ddto.setMutipleReuslt(true);
							StringBuffer sb = new StringBuffer();
							boolean flag = false;
							for (SysmexTestCodeData sdl : list) {
								if (flag) {
									if (sdl.isFinalValue())
										sb.append(",").append("<font color='red'>" + sdl.getValue() + "</font>");
									else
										sb.append(",").append(sdl.getValue());
								} else {
									if (sdl.isFinalValue())
										sb.append("<font color='red'>" + sdl.getValue() + "</font>");
									else
										sb.append(sdl.getValue());
									flag = true;
								}
							}
							ddto.setResultToDisplay(sb.toString());
						} else {
							ddto.setResultToDisplay("<font color='red'>" + list.get(0).getValue() + "</font>");
						}

					}
					elementValues.put(tc.getOrderNo(), ddto);

				}
				dto.setElementValues(elementValues);
				sysmexRowiseDtos.add(dto);
			}
			sddto.setSysmexRowiseDtos(sysmexRowiseDtos);
			return sddto;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new SysmexDataDto();
		}

	}

	@Override
	public boolean studyMetaDataConfigured(Long studyId) {
		// TODO Auto-generated method stub
		return studyDao.studyMetaDataConfigured(studyId);
	}

	@Override
	public List<Species> speciesList() {
		// TODO Auto-generated method stub
		return studyDao.speciesList();
	}

	@Override
	public String saveSponsorMaster(SponsorMaster sponsorMaster) {
		return studyDao.saveSponsorMaster(sponsorMaster);
	}

	@Override
	public SponsorMaster getSponsorMasterUniqueCheckCode(String data) {
		return studyDao.getSponsorMasterUniqueCheckCode(data);
	}

	@Override
	public Object sponsorList() {
		return studyDao.sponsorList();
	}

	@Override
	public List<String> insturmentResultSelectionDates(Long studyId, String insturment) {
		// TODO Auto-generated method stub
		return studyDao.insturmentResultSelectionDates(studyId, insturment);
	}

	@Override
	public StudyAnimal studyAnimalsById(Long animalId) {
		// TODO Auto-generated method stub
		return studyDao.studyAnimalsById(animalId);
	}

	@Override
	public String updateAceeptMetdetaStatus(StudyMaster sm) {
		return studyDao.updateAceeptMetdetaStatus(sm);
	}

	@Override
	public List<StudyMaster> allNewActiveStudysOfStudyDirector(Long userId) {
		// TODO Auto-generated method stub
		return studyDao.allNewActiveStudysOfStudyDirector(userId);
	}

	@Override
	public List<StudyMaster> allActiveStudysOfStudyDirector(Long userId) {
		// TODO Auto-generated method stub
		return studyDao.allActiveStudysOfStudyDirector(userId);
	}

	@Override
	public List<RoleMaster> allRoleMasters() {
		// TODO Auto-generated method stub
		return studyDao.allRoleMasters();
	}

	@Override
	public ModelMap addIntrumentAndPerameters(Long studyId, ModelMap model, HttpServletRequest request) {
		if (studyMetaDataConfigured(studyId)) {
			StudyMaster study = findByStudyId(studyId);
			if (study.isIntrumentConfuguraiton()) {
				StudyMasterDto studyDto = groupingDao.studyView(studyId);
				model.addAttribute("studyDto", studyDto);
//				return "/pages/grouping/studyView";
				model.addAttribute("pageMessage", "Instrument configureation has completed.");
				model.addAttribute("instumentConfigPage", "clinpahtPeramsView");
				return model;
			} else {
				model.addAttribute("study", study);

				Map<String, List<TestCode>> instumentTestCodes = groupingDao.instrumentWiseTestCodes();
				Map<String, String> instrumentNames = new HashMap<>();
				for (Map.Entry<String, List<TestCode>> m : instumentTestCodes.entrySet()) {
					List<TestCode> list = m.getValue();
					for (TestCode tc : list)
						instrumentNames.put(tc.getMethodType(), tc.getInstrument());
				}
				model.addAttribute("instrumentNames", instrumentNames);
				model.addAttribute("instumentTestCodes", instumentTestCodes);
				List<TestCodeProfile> profiles = groupingDao.allActiveProfiles();
				Map<String, List<TestCodeProfile>> profileMaptemp = new HashMap<>();
				profiles.stream().forEach((profile) -> {
					List<TestCodeProfile> list = profileMaptemp.get(profile.getInsturment().getInstrumentName());
					if (list == null)
						list = new ArrayList<>();
					list.add(profile);
					profileMaptemp.put(profile.getInsturment().getInstrumentName(), list);
				});
				Map<String, List<TestCodeProfile>> profileMap = new HashMap<>();
				List<InstrumentIpAddress> ipAddress = groupingDao.allActiveInsturments();
				for (InstrumentIpAddress ins : ipAddress) {
					profileMap.put(ins.getInstrumentName(), profileMaptemp.get(ins.getInstrumentName()));
				}
				model.addAttribute("profiles", profiles);
				model.addAttribute("profileMap", profileMap);
				model.addAttribute("allowInstrumentConfiguration", true);
			}

		} else {
			model.addAttribute("page", "Study Metadata not done.");
			model.addAttribute("allowInstrumentConfiguration", true);
		}
		model.addAttribute("instumentConfigPage", "addIntrumentAndPerameters");
		return model;
	}

	@Override
	public StaticData staticDataByCode(String code) {
		// TODO Auto-generated method stub
		return studyDao.staticDataByCode(code);
	}

	@Autowired
	AcclimatizationDao acclimatizationDao;
	@Autowired
	InstrumentDao instrumentDao;

	@Override
	public ModelMap addIntrumentAndPerametersForStudyAcclamatizationDates(Long observationDatesId,
			ModelMap model, HttpServletRequest request, String obserVationFor) {
		StudyAcclamatizationDates studyacc = null;
		StudyTreatmentDataDates studyTreetment = null;
		Long studyId = null;
		if(obserVationFor.equals("Acclimatization")) {
			studyacc = acclimatizationDao
					.studyAcclamatizationDatesById(observationDatesId);		
			studyId = studyacc.getStudy().getId();
		}else {
			studyTreetment = acclimatizationDao.studyTreatmentDataDatesById(observationDatesId);
			studyId = studyTreetment.getStudy().getId();
		}


		
		if (studyMetaDataConfigured(studyId)) {
			ObservationInturmentConfiguration observationInturmentConfiguration = instrumentDao
					.observationInturmentConfiguration(observationDatesId, obserVationFor );

			StatusMaster activeStatus = statusDao.statusMaster(StatusMasterCodes.ACTIVE.toString());
			SortedMap<Integer, InstrumentIpAddress> instumentMap = groupingDao.activeInstruments(activeStatus);
			
			Map<Long, List<TestCode>> insturmentTestCodes = new HashMap<>();
			Map<Long, List<StudyTestCodes>> observationInsturmentTestCodes = new HashMap<>();
			for (Map.Entry<Integer, InstrumentIpAddress> insturments : instumentMap.entrySet()) {
				List<TestCode> tcs = groupingDao.testCodes(true, insturments.getValue().getInstrumentName());
				List<StudyTestCodes> testCode = new ArrayList<>();
				if(observationInturmentConfiguration != null)
					testCode = instrumentDao.studyTestCodesOfObservation(
						observationInturmentConfiguration.getId(), insturments.getValue().getId());
				List<Long> studyTestCodeIds = new ArrayList<>();
				for(StudyTestCodes std : testCode)
					studyTestCodeIds.add(std.getTestCode().getId());
				
				List<TestCode> requiredTestCodes = new ArrayList<>();
				for(TestCode tc : tcs)
					if(!studyTestCodeIds.contains(tc.getId()))
						requiredTestCodes.add(tc);
				insturmentTestCodes.put(insturments.getValue().getId(), requiredTestCodes);
				observationInsturmentTestCodes.put(insturments.getValue().getId(), testCode);
			}
			
			model.addAttribute("observationDatesId", observationDatesId);
			if(observationInturmentConfiguration != null)
				model.addAttribute("observationInturmentConfigurationId", observationInturmentConfiguration.getId());
			else
				model.addAttribute("observationInturmentConfigurationId", 0l);
			model.addAttribute("instumentMap", instumentMap);
			model.addAttribute("insturmentTestCodes", insturmentTestCodes);
			model.addAttribute("observationInsturmentTestCodes", observationInsturmentTestCodes);

			List<TestCodeProfile> profiles = groupingDao.allActiveProfiles();
			Map<Long, List<TestCodeProfile>> profileMaptemp = new HashMap<>();
			profiles.stream().forEach((profile) -> {
				List<TestCodeProfile> list = profileMaptemp.get(profile.getInsturment().getId());
				if (list == null)
					list = new ArrayList<>();
				list.add(profile);
				profileMaptemp.put(profile.getInsturment().getId(), list);
			});

			model.addAttribute("profileMaptemp", profileMaptemp);
			model.addAttribute("allowInstrumentConfiguration", true);
		}else {
			model.addAttribute("page", "Study Metadata not done.");
			model.addAttribute("allowInstrumentConfiguration", true);
		}
		return model;
	}
	@Override
	public List<ObservationClinPathTestCodesDto> dbIntrumentAndPerametersForStudyAcclamatizationDates(
			Long studyAcclamatizationDatesId) {
		List<ObservationClinPathTestCodesDto> dtos = new ArrayList<>();
//		ObservationInturmentConfiguration observationInturmentConfiguration = instrumentDao
//				.observationInturmentConfiguration(studyAcclamatizationDatesId);
//		if (observationInturmentConfiguration != null) {
//			Map<String, List<TestCode>> instumentTestCodes = groupingDao.instrumentWiseTestCodes();
//			Map<String, String> instrumentNames = new HashMap<>();
//			for (Map.Entry<String, List<TestCode>> m : instumentTestCodes.entrySet()) {
//				List<TestCode> list = m.getValue();
//				for (TestCode tc : list)
//					instrumentNames.put(tc.getMethodType(), tc.getInstrument());
//			}
//			List<StudyTestCodes> testCode = instrumentDao
//					.studyTestCodesOfObservation(observationInturmentConfiguration.getId());
//			for (Map.Entry<String, String> map : instrumentNames.entrySet()) {
//				ObservationClinPathTestCodesDto dto = new ObservationClinPathTestCodesDto();
//				dto.setInsturment(map.getValue());
//				dto.setOpration(map.getKey());
//				List<StudyTestCodesDto> tcs = new ArrayList<>();
//				for (StudyTestCodes tc : testCode) {
//					if (tc.getInstrument().getInstrumentName().equals(map.getValue())) {
//						tcs.add(new StudyTestCodesDto(tc.getTestCode(), tc.getOrderNo(), tc.getInstrument()));
//					}
//				}
//				dto.setTestCodes(tcs);
//				dtos.add(dto);
//			}
//		}
		return dtos;
	}

	@Override
	public List<TestCodeProfileParameters> testCodeProfileParametersList(Long profileId) {
		// TODO Auto-generated method stub
		return groupingDao.testCodeProfileParameters(profileId);
	}

	@Override
	public List<StudyAnimal> studyAnimals(String gender, boolean randamized) {
		// TODO Auto-generated method stub
		return studyDao.studyAnimals(gender, randamized);
	}

	@Override
	public String sendStudyFroReivew(Long studyId, String username, String commentForReview) {
		try {
			studyDao.sendStudyFroReivew(studyId, username, commentForReview);
			return "success";
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "Fail";
	}

	@Override
	public List<StudyMaster> allReivewActiveStudys() {
		// TODO Auto-generated method stub
		return studyDao.allReivewActiveStudys();
	}

	
	
}
