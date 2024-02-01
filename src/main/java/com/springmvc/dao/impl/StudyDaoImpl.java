package com.springmvc.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.covide.crf.dto.CRFGroupItemStd;
import com.covide.crf.dto.CRFSectionsStd;
import com.covide.crf.dto.CrfDescrpencyAudit;
import com.covide.crf.dto.CrfItemsStd;
import com.covide.crf.dto.CrfMetaDataStd;
import com.covide.crf.dto.StagoDataDto;
import com.covide.crf.dto.VistrosDataDto;
import com.covide.dto.SatusAndWorkFlowDetailsDto;
import com.covide.enums.StatusMasterCodes;
import com.covide.template.dto.SysmexDataUpdateDto;
import com.covide.template.dto.SysmexDto;
import com.covide.template.dto.SysmexTestCodeDataDto;
import com.covide.template.dto.VistrosDataUpdateDto;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.controllers.RadwagController;
import com.springmvc.dao.StatusDao;
import com.springmvc.dao.StudyDao;
import com.springmvc.dao.UserDao;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.DepartmentMaster;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.ObservationInturmentConfiguration;
import com.springmvc.model.PeriodCrfs;
import com.springmvc.model.PhysicalWeightBalanceData;
import com.springmvc.model.RandamizationDto;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.Species;
import com.springmvc.model.SponsorMaster;
import com.springmvc.model.StagoData;
import com.springmvc.model.StaticData;
import com.springmvc.model.StatusMaster;
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
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.SubgroupInfoLog;
import com.springmvc.model.SubjectStatus;
import com.springmvc.model.SysmexData;
import com.springmvc.model.SysmexTestCode;
import com.springmvc.model.SysmexTestCodeData;
import com.springmvc.model.UserWiseStudiesAsignMaster;
import com.springmvc.model.VitrosData;
import com.springmvc.model.VitrosDataFromExe;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.model.VolunteerPeriodCrfStatus;
import com.springmvc.model.WorkFlowStatusDetails;
import com.springmvc.util.VistrosThread;

@Repository("studyDao")
@PropertySource(value = { "classpath:application.properties" })
@SuppressWarnings("unchecked")
public class StudyDaoImpl extends AbstractDao<Long, StudyMaster> implements StudyDao {
	@Autowired
	private Environment environment;
	@Autowired
	private StatusDao statusDao;
	@Autowired
	private StudyTestCodesDaoImple studyTestCodesDao;
	static boolean firstStudy = true;
	@Autowired
	private ObservationInturmentConfigurationDaoImpl observationInturmentConfigurationDao;
	@Autowired
	private UserDao userDao;
	@Override
	public List<StudyMaster> findAll() {
		List<StudyMaster> smList = null;
		StatusMaster status = null;
		try {
			status = (StatusMaster) getSession().createCriteria(StatusMaster.class)
					.add(Restrictions.eq("statusCode", "SU")).uniqueResult();
			if (status != null) {
				smList = getSession().createCriteria(StudyMaster.class).add(Restrictions.ne("status", status)).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smList;
	}

	@Override
	public StudyMaster findByStudyId(Long studyId) {
		// TODO Auto-generated method stub
		StudyMaster sm = getByKey(studyId);
		Hibernate.initialize(sm.getStudyAnalytes());
		return sm;
	}

	@Override
	public StudyMaster findByStudyNo(String studyNo, Long id) {
		// TODO Auto-generated method stub
		if (id != 0)
			return (StudyMaster) createEntityCriteria().add(Restrictions.eq("studyNo", studyNo)).uniqueResult();
		else
			return (StudyMaster) createEntityCriteria().add(Restrictions.ne("id", id))
					.add(Restrictions.eq("studyNo", studyNo)).uniqueResult();
	}

	@Override
	public StudyMaster findByStudyNo(String studyNo) {
		// TODO Auto-generated method stub
		return (StudyMaster) createEntityCriteria().add(Restrictions.eq("studyNo", studyNo)).uniqueResult();

	}

	@Override
	public StudyMaster saveStudyMaster(StudyMaster studyMaster) {
		StatusMaster sm = null;
		try {
			sm = (StatusMaster) getSession().createCriteria(StatusMaster.class).add(Restrictions.eq("statusCode", "IN"))
					.uniqueResult();
			studyMaster.setStatus(sm);
			getSession().save(studyMaster);
			for (GroupInfo gf : studyMaster.getGroupInfo()) {
				gf.setStudy(studyMaster);
				getSession().save(gf);
			}
			return studyMaster;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void updateStudy(StudyMaster study) {
		getSession().update(study);
	}

	@Override
	public List<StudyPeriodMaster> allStudyPeriods(StudyMaster sm) {
		List<StudyPeriodMaster> periods = getSession().createCriteria(StudyPeriodMaster.class)
				.add(Restrictions.eq("studyMaster", sm)).list();
		return periods;
	}

	@Override
	public List<StudyPeriodMaster> allStudyPeriodsWithSubEle(StudyMaster sm) {
		List<StudyPeriodMaster> periods = getSession().createCriteria(StudyPeriodMaster.class)
				.add(Restrictions.eq("studyMaster", sm)).list();
		Collections.sort(periods);

		return periods;
	}

	@Override
	public List<PeriodCrfs> periodCrfs(Long periodId) {
		List<PeriodCrfs> list = getSession().createCriteria(PeriodCrfs.class).add(Restrictions.eq("periodId", periodId))
				.list();
		return list;
	}

	@Override
	public StudyPeriodMaster studyPeriodMaster(Long periodId) {
		// TODO Auto-generated method stub
		StudyPeriodMaster sp = (StudyPeriodMaster) getSession().get(StudyPeriodMaster.class, periodId);
		return sp;
	}

	@Override
	public void upatePeriodCrfsList(List<PeriodCrfs> pcrfsupdate) {
		// TODO Auto-generated method stub
		for (PeriodCrfs pc : pcrfsupdate)
			getSession().update(pc);
	}

	@Override
	public void savePeriodCrfsList(List<PeriodCrfs> pcrfsSave) {
		// TODO Auto-generated method stub
		for (PeriodCrfs pc : pcrfsSave)
			getSession().save(pc);
	}

	@Override
	public void upatePeriodCrfs(StudyPeriodMaster sp) {
		// TODO Auto-generated method stub
		getSession().update(sp);
	}

	@Override
	public void saveVolList(List<Volunteer> vlist) {
		// TODO Auto-generated method stub
		for (Volunteer v : vlist) {
			getSession().save(v);
		}
	}

	@Override
	public void saveVolunteerPeriodCrf(List<VolunteerPeriodCrf> vpclist) {
		for (VolunteerPeriodCrf vpc : vpclist) {
			System.out.println(vpc.getExitCrf());
			getSession().save(vpc);
		}
	}

	@Override
	public List<Volunteer> studyVolunteerList(StudyMaster sm) {
		List<Volunteer> vl = getSession().createCriteria(Volunteer.class).add(Restrictions.eq("studyId", sm.getId()))
				.list();
		for (Volunteer volunteer : vl) {
			Hibernate.initialize(volunteer.getSite());
		}
		return vl;
	}

	@Override
	public List<Volunteer> studyVolunteerListWithSite(StudyMaster sm, StudySite site) {
		List<Volunteer> vl = getSession().createCriteria(Volunteer.class).add(Restrictions.eq("studyMaster", sm))
				.add(Restrictions.eq("site", site)).list();
		return vl;
	}

	@Override
	public List<VolunteerPeriodCrf> volunteerPeriodCrfList(Long peiodId) {
		// TODO Auto-generated method stub
		List<VolunteerPeriodCrf> vpcl = getSession().createCriteria(VolunteerPeriodCrf.class)
				.add(Restrictions.eq("period.id", peiodId)).list();
		for (VolunteerPeriodCrf vpc : vpcl) {
			Hibernate.initialize(vpc.getVol());
			Hibernate.initialize(vpc.getPeriod());
		}
		return vpcl;
	}

	@Override
	public List<VolunteerPeriodCrf> volunteerPeriodCrfList(StudyPeriodMaster sp, Volunteer vol) {
		// TODO Auto-generated method stub
		List<VolunteerPeriodCrf> vpcl = getSession().createCriteria(VolunteerPeriodCrf.class)
				.add(Restrictions.eq("period", sp)).add(Restrictions.eq("vol", vol)).list();
		for (VolunteerPeriodCrf vpc : vpcl) {
			Hibernate.initialize(vpc.getVol());
			Hibernate.initialize(vpc.getPeriod());
			Hibernate.initialize(vpc.getStdCrf());
		}

		return vpcl;
	}

	@Override
	public void saveVolunteerPeriodCrfStatusList(List<VolunteerPeriodCrfStatus> vpclist) {
		// TODO Auto-generated method stub
		for (VolunteerPeriodCrfStatus vpc : vpclist) {
			getSession().save(vpc);
		}
	}

	@Override
	public List<VolunteerPeriodCrfStatus> volunteerPeriodCrfStatusList(Long peiodId) {
		// TODO Auto-generated method stub
		List<VolunteerPeriodCrfStatus> vpcl = getSession().createCriteria(VolunteerPeriodCrfStatus.class)
				.add(Restrictions.eq("period.id", peiodId)).list();
		for (VolunteerPeriodCrfStatus vpc : vpcl) {
			Hibernate.initialize(vpc.getVol());
		}
		return vpcl;
	}

	@Override
	public VolunteerPeriodCrfStatus volunteerPeriodCrfStatus(StudyPeriodMaster sp, Volunteer v) {
		// TODO Auto-generated method stub
		return (VolunteerPeriodCrfStatus) getSession().createCriteria(VolunteerPeriodCrfStatus.class)
				.add(Restrictions.eq("period", sp)).add(Restrictions.eq("vol", v)).uniqueResult();
	}

	@Override
	public Volunteer volunteer(Long volId) {
		// TODO Auto-generated method stub
		return (Volunteer) getSession().get(Volunteer.class, volId);
	}

	@Override
	public VolunteerPeriodCrf volunteerPeriodCrf(Long vpcId) {
		VolunteerPeriodCrf vpc = (VolunteerPeriodCrf) getSession().get(VolunteerPeriodCrf.class, vpcId);
		Hibernate.initialize(vpc.getVol());
		Hibernate.initialize(vpc.getPeriod());
		Hibernate.initialize(vpc.getStdCrf());
		return vpc;
	}

	@Override
	public CrfMetaDataStd crfMetaDataStd(int crfId) {
		CrfMetaDataStd crf = (CrfMetaDataStd) getSession().get(CrfMetaDataStd.class, crfId);
		Hibernate.initialize(crf.getStd());
		Hibernate.initialize(crf.getSections());
		Hibernate.initialize(crf.getGroups());
//		List<CRFSectionsStd> sl =  getSession().createCriteria(CRFSectionsStd.class)
//				.add(Restrictions.eq("crfId", crf)).list();
//		crf.setSections(sl);
		for (CRFSectionsStd s : crf.getSections()) {
//			List<CrfItemsStd> il =  getSession().createCriteria(CrfItemsStd.class)
//					.add(Restrictions.eq("section", s)).list();
//			s.setItemList(il);
			Hibernate.initialize(s.getItemList());
			for (CrfItemsStd it : s.getItemList()) {
//				List<CRFItemValuesStd> irvl =  getSession().createCriteria(CRFItemValuesStd.class)
//						.add(Restrictions.eq("crfItem", it)).list();
//				it.setItemResponceValues(irvl);
				Hibernate.initialize(it.getItemResponceValues());
			}
		}
		for (CRFGroupItemStd g : crf.getGroups()) {
			Hibernate.initialize(g.getItemList());
			for (CrfItemsStd it : g.getItemList()) {
				Hibernate.initialize(it.getItemResponceValues());
			}
		}
		return crf;
	}

	@Override
	public void updateVolunteerPeriodCrfStatus(VolunteerPeriodCrfStatus vpcs) {
		getSession().update(vpcs);
	}

	@Override
	public List<PeriodCrfs> periodCrfList(StudyPeriodMaster sp) {
		List<PeriodCrfs> list = getSession().createCriteria(PeriodCrfs.class).add(Restrictions.eq("period", sp))
				.add(Restrictions.eq("active", true)).list();
		return list;
	}

	@Override
	public void updateVolunteerPeriodCrf(VolunteerPeriodCrf vpc) {
		getSession().update(vpc);
	}

	@Override
	public String saveSite(StudySite site, List<StudySitePeriodCrfs> list) {
		try {
			getSession().save(site);
			for (StudySitePeriodCrfs crf : list)
				getSession().save(crf);
			return site.getSiteName();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int maxSiteNo(StudyMaster sm) {
		int siteNo = 0;
		try {
			siteNo = (int) getSession().createCriteria(StudySite.class).setProjection(Projections.max("siteNo"))
					.add(Restrictions.eq("studyMaster", sm)).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return siteNo;
	}

	@Override
	public List<StudySite> studySite(StudyMaster sm) {
		return getSession().createCriteria(StudySite.class).add(Restrictions.eq("studyMaster", sm)).list();
	}

	@Override
	public List<StudyGroupClass> studyGroupClass(StudyMaster sm) {
		return getSession().createCriteria(StudyGroupClass.class).add(Restrictions.eq("studyMaster", sm)).list();
	}

	@Override
	public List<StudyGroup> studyGroup(StudyMaster sm) {
		List<StudyGroup> gl = getSession().createCriteria(StudyGroup.class).add(Restrictions.eq("studyMaster", sm))
				.list();
		for (StudyGroup sg : gl)
			Hibernate.initialize(sg.getGroupClass());
		return gl;
	}

	@Override
	public StudySite studySiteId(Long siteId) {
		return (StudySite) getSession().get(StudySite.class, siteId);
	}

	@Override
	public String saveGroupClass(StudyGroupClass group) {
		int groupNo = 0;
		try {
			groupNo = (int) getSession().createCriteria(StudyGroupClass.class).setProjection(Projections.max("groupNo"))
					.add(Restrictions.eq("studyMaster", group.getStudyMaster())).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			group.setGroupNo(++groupNo);
			getSession().save(group);
			return group.getGroupName();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public StudyGroupClass studyGroupClassById(long id) {
		// TODO Auto-generated method stub
		return (StudyGroupClass) getSession().get(StudyGroupClass.class, id);
	}

	@Override
	public String saveGroup(StudyGroup group) {
		int groupNo = 0;
		try {
			groupNo = (int) getSession().createCriteria(StudyGroup.class).setProjection(Projections.max("groupNo"))
					.add(Restrictions.eq("studyMaster", group.getStudyMaster())).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			group.setGroupNo(++groupNo);
			getSession().save(group);
			return group.getGroupName();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public StudyGroup studyGroupById(Long parseInt) {
		return (StudyGroup) getSession().get(StudyGroup.class, parseInt);
	}

	@Override
	public List<StudyGroup> studyGroupByIds(List<Long> ids) {
		List<StudyGroup> group = getSession().createCriteria(StudyGroup.class).add(Restrictions.in("id", ids)).list();
		for (StudyGroup g : group)
			Hibernate.initialize(g.getGroupClass());
		return group;
	}

	@Override
	public StudyPeriodMaster studyPeriodMaster(StudyMaster studyMaster, String type, int periodNo) {
		return (StudyPeriodMaster) getSession().createCriteria(StudyPeriodMaster.class)
				.add(Restrictions.eq("studyMaster", studyMaster)).add(Restrictions.eq("type", type))
				.add(Restrictions.eq("periodNo", periodNo)).uniqueResult();
	}

	@Override
	public String saveStudySiteSubject(StudySiteSubject subject, List<StudySiteSubjectGroup> subjectGroups,
			Volunteer vol, List<VolunteerPeriodCrfStatus> vpcslist, List<VolunteerPeriodCrf> vpclist) {
		int maxSub = 0;
		try {
			maxSub = (int) getSession().createCriteria(StudySiteSubject.class)
					.setProjection(Projections.max("subjectno"))
					.add(Restrictions.eq("studyMaster", subject.getStudyMaster())).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			subject.setSubjectno(++maxSub);
			String no = maxSub + "";
			if (maxSub < 10)
				no = "00" + no;
			else if (maxSub < 100)
				no = "0" + no;

			StudySiteSubjectGroup group = subjectGroups.get(0);
			String groupName = group.getStudyGroup().getGroupName();
			String siteNo = group.getSite().getSiteName();
			String subjectNo = siteNo + "-" + no;
			if (groupName.equals("cUTI")) {
				subjectNo = "I1-" + siteNo + "-" + no;
			} else if (groupName.equals("cIRTI")) {
				subjectNo = "I2-" + siteNo + "-" + no;
			} else if (groupName.equals("cIAI")) {
				subjectNo = "I3-" + siteNo + "-" + no;
			}
			subject.setSubjectId(subjectNo);
//			if(maxSub < 10)	subject.setSubjectId("S0"+maxSub);
//			else 	subject.setSubjectId("S"+maxSub);
			getSession().save(subject);
			for (StudySiteSubjectGroup sssg : subjectGroups) {
				sssg.setSubject(subject);
				getSession().save(sssg);
			}
			vol.setVolId(subject.getSubjectId());
			vol.setBedNo(subject.getSubjectno() + "");
			getSession().save(vol);
			subject.setVol(vol);
			saveVolunteerPeriodCrfStatusList(vpcslist);
			saveVolunteerPeriodCrf(vpclist);
			return subject.getSubjectId();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public String scheduleSubject(Volunteer vol, List<VolunteerPeriodCrfStatus> vpcslist,
			List<VolunteerPeriodCrf> vpclist) {
		try {
			saveVolunteerPeriodCrfStatusList(vpcslist);
			saveVolunteerPeriodCrf(vpclist);
			return vol.getVolId();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public void saveSubjectStatus(SubjectStatus ss) {
		getSession().save(ss);
	}

	@Override
	public SubjectStatus subjectStatus(StudyPeriodMaster period, Volunteer vol) {
		SubjectStatus ss = (SubjectStatus) getSession().createCriteria(SubjectStatus.class)
				.add(Restrictions.eq("period", period)).add(Restrictions.eq("vol", vol)).uniqueResult();
		if (ss != null) {
			Hibernate.initialize(ss.getVol());
			Hibernate.initialize(ss.getPeriod());
			Hibernate.initialize(ss.getStudy());
		}
		return ss;
	}

	@Override
	public void saveStudyStatusAudit(StudyStatusAudit audit) {
		getSession().save(audit);
	}

	@Override
	public List<SubjectStatus> subjectStatusList(StudyMaster sm) {
		List<SubjectStatus> list = getSession().createCriteria(SubjectStatus.class).add(Restrictions.eq("study", sm))
				.list();
		for (SubjectStatus ss : list) {
			Hibernate.initialize(ss.getVol());
			Hibernate.initialize(ss.getPeriod());
			Hibernate.initialize(ss.getStudy());
		}
		return list;
	}

	@Override
	public StudyGroup checkGroupNameExistOrNot(Long groupclass, String groupName) {
		return (StudyGroup) getSession().createCriteria(StudyGroup.class)
				.add(Restrictions.eq("groupClass.id", groupclass)).add(Restrictions.eq("groupName", groupName))
				.uniqueResult();

	}

	@Override
	public StudyGroupClass checkGroupClassNameExistOrNot(StudyMaster sm, String groupName) {
		return (StudyGroupClass) getSession().createCriteria(StudyGroupClass.class)
				.add(Restrictions.eq("studyMaster", sm)).add(Restrictions.eq("groupName", groupName)).uniqueResult();
	}

	@Override
	public StudySite checkSiteNameExistOrNot(StudyMaster sm, String siteName) {
		return (StudySite) getSession().createCriteria(StudySite.class).add(Restrictions.eq("studyMaster", sm))
				.add(Restrictions.eq("siteName", siteName)).uniqueResult();
	}

	@Override
	public List<StudySiteSubject> studySiteSubjects(StudySite ss) {
		return getSession().createCriteria(StudySiteSubject.class).add(Restrictions.eq("site", ss)).list();
	}

	@Override
	public List<VolunteerPeriodCrfStatus> volunteerPeriodCrfStatusList(List<StudyPeriodMaster> plist) {
		List<VolunteerPeriodCrfStatus> list = new ArrayList<VolunteerPeriodCrfStatus>();
		for (StudyPeriodMaster sp : plist) {
			List<VolunteerPeriodCrfStatus> list2 = getSession().createCriteria(VolunteerPeriodCrfStatus.class)
					.add(Restrictions.eq("period", sp)).list();
			list.addAll(list2);
		}
		return list;
	}

	@Override
	public List<VolunteerPeriodCrfStatus> volunteerPeriodCrfStatus(Volunteer vol) {
		List<VolunteerPeriodCrfStatus> list2 = getSession().createCriteria(VolunteerPeriodCrfStatus.class)
				.add(Restrictions.eq("vol", vol)).list();
		for (VolunteerPeriodCrfStatus vpscs : list2) {
			Hibernate.initialize(vpscs.getPeriod());
		}
		return list2;
	}

	@Override
	public List<SubjectStatus> subjectStatusList(StudySite ss) {
		return getSession().createCriteria(SubjectStatus.class).add(Restrictions.eq("site", ss)).list();
	}

	@Override
	public List<StudySiteSubject> studySiteSubjectList(StudyMaster sm) {
		return getSession().createCriteria(StudySiteSubject.class).add(Restrictions.eq("studyMaster", sm)).list();
	}

	@Override
	public StudySiteSubject studySiteSubject(Long id) {
		StudySiteSubject sss = (StudySiteSubject) getSession().get(StudySiteSubject.class, id);
		List<StudySiteSubjectGroup> sg = getSession().createCriteria(StudySiteSubjectGroup.class)
				.add(Restrictions.eq("subject", sss)).list();
		sss.setStudyGroup(sg.get(0).getStudyGroup());
		sss.setGroupClass(sg.get(0).getGroupClass());
		return sss;
	}

	@Override
	public List<CrfDescrpencyAudit> studyCrfDescrpencyAudit(Volunteer vol) {
		List<CrfDescrpencyAudit> list = getSession().createCriteria(CrfDescrpencyAudit.class)
				.add(Restrictions.eq("vol", vol)).list();
		for (CrfDescrpencyAudit sa : list) {
			Hibernate.initialize(sa.getSecElement());
			Hibernate.initialize(sa.getGroupElement());
			Hibernate.initialize(sa.getSecEleData());
			Hibernate.initialize(sa.getGroupEleData());
//			Hibernate.initialize();
//			Hibernate.initialize();
		}
		return list;
	}

	@Override
	public List<SubjectStatus> subjectStatusForVol(Volunteer vol) {
		List<SubjectStatus> list = getSession().createCriteria(SubjectStatus.class).add(Restrictions.eq("vol", vol))
				.list();
		for (SubjectStatus ss : list) {
			Hibernate.initialize(ss.getPeriod());
			Hibernate.initialize(ss.getStudy());
			Hibernate.initialize(ss.getSite());
		}
		return list;
	}

	@Override
	public void saveStudyLog(StudyMasterLog log) {
		getSession().save(log);
	}

	@Override
	public PhysicalWeightBalanceData savePhysicalWeightBalanceData(PhysicalWeightBalanceData obj) {
		try {
			getSession().save(obj);
			RadwagController.currentData = obj;
			RadwagController.count++;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return obj;
	}

	@Override
	public List<PhysicalWeightBalanceData> physicalWeightBalanceDataList() {
		sysncronizeData();
		return getSession().createCriteria(PhysicalWeightBalanceData.class)
//				.add(Restrictions.gt("id", 457l))
				.add(Restrictions.eq("dataFrom", "Offline")).list();
	}

	private void sysncronizeData() {
		List<PhysicalWeightBalanceData> list = new ArrayList<>();
		String userName, password, url, driver;

		userName = environment.getRequiredProperty("jdbc.maria.username");
		password = environment.getRequiredProperty("jdbc.maria.password");
		url = environment.getRequiredProperty("jdbc.maria.url");
		;
		driver = environment.getRequiredProperty("jdbc.maria.driverClassName");
		String ip = environment.getRequiredProperty("jdbc.maria.ip");
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, userName, password);
			Statement st = con.createStatement();
			// date, tareWt
			String sql = "select id,JSON_REPORT,send_status from rep_diff_weighments where send_status='0'";
			PreparedStatement p = con.prepareStatement(sql);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getLong("id"));
				String json = rs.getString("JSON_REPORT");
				String s = json.substring(1, json.length() - 1);
				String[] array = s.split("},");
				int send_status = rs.getInt("send_status");
				for (String o : array) {
					String o2 = o.substring(1, o.length());
					String[] array2 = o2.split(",");
					if (array2.length > 1) {
						String[] array3 = array2[0].split(":");
						String name = array3[1].replaceAll("\"", "");
						if (name.equals("printout")) {
							try {
								Long id = rs.getLong("id");
								if (id >= 110) {
									System.out.print(rs.getString("id") + "\t");
									System.out.println(array2[1]);
									String[] testa = array2[1].split("\":\"");
									String test = testa[1];
									test = test.replaceAll("\"", "");
									test = test.replace('\\', ' ');
									test = test.replaceAll(" r n", "___");
									System.out.println(test);
									String[] results = test.split("___");
									Map<String, String> mp = new HashMap<>();
									for (String r : results) {
										String[] rr = r.split(":");
										if (rr.length > 1) {
											StringBuilder sb = new StringBuilder();
											sb.append(rr[1].trim());
											if (rr[0].trim().equals("Date & Time"))
												sb.append(":").append(rr[2].trim()).append(":").append(rr[3].trim());

											System.out.println(rr[0].trim() + "  -   " + sb.toString());
											mp.put(rr[0].trim(), sb.toString());
										}
									}
									if (mp.get("Gross Wt") != null) {
										PhysicalWeightBalanceData pd = new PhysicalWeightBalanceData();
										pd.setId(id);
										pd.setBatchNo(mp.get("Batch No"));
										pd.setGrossWt(mp.get("Gross Wt"));
										pd.setNetWt(mp.get("Net Wt"));
										pd.setNozzleNo(mp.get("Nozzle No"));
										pd.setStatus(mp.get("Status"));
										pd.setTareWt(mp.get("Tare Wt"));
										pd.setDateAndTime(mp.get("Date & Time"));
										pd.setDataFrom("Offline");
										pd.setIpAddress(ip);
										pd.setSend_status(send_status);
										list.add(pd);
									}
								}

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
					}

				}
			}
			System.out.println("Connection is successful");

			try {
				con.setAutoCommit(false);
				for (PhysicalWeightBalanceData physicalWeightBalanceData : list) {
					Long id = physicalWeightBalanceData.getId();
					physicalWeightBalanceData.setId(0l);
					PhysicalWeightBalanceData data = null;
					try {
						data = (PhysicalWeightBalanceData) getSession().createCriteria(PhysicalWeightBalanceData.class)
								.add(Restrictions.eq("dateAndTime", physicalWeightBalanceData.getDateAndTime()))
								.add(Restrictions.eq("ipAddress", ip))
								.add(Restrictions.eq("batchNo", physicalWeightBalanceData.getBatchNo()))
								.add(Restrictions.eq("grossWt", physicalWeightBalanceData.getGrossWt()))
								.add(Restrictions.eq("tareWt", physicalWeightBalanceData.getTareWt()))
								.add(Restrictions.eq("netWt", physicalWeightBalanceData.getNetWt()))
								.add(Restrictions.eq("nozzleNo", physicalWeightBalanceData.getNozzleNo()))
								.add(Restrictions.eq("status", physicalWeightBalanceData.getStatus())).uniqueResult();
						if (data == null)
							getSession().save(physicalWeightBalanceData);

						sql = "update rep_diff_weighments set send_status='1' where id=?";
						p = con.prepareStatement(sql);
						p.setLong(1, id);
						p.executeUpdate();

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
				con.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public SubGroupInfo stdGroupInfo(Long subGroupId) {
		// TODO Auto-generated method stub
		return (SubGroupInfo) getSession().get(SubGroupInfo.class, subGroupId);
	}

	@Override
	public long saveStudyMasterRecord(StudyMaster studyMaster, List<UserWiseStudiesAsignMaster> list) {
		long no = 0;
		StatusMaster sm = null;
		try {
			sm = (StatusMaster) getSession().createCriteria(StatusMaster.class).add(Restrictions.eq("statusCode", "IN"))
					.uniqueResult();
			studyMaster.setStatus(sm);
			no = (long) getSession().save(studyMaster);
			list.forEach((l) -> {
				getSession().save(l);
			});

//			if(firstStudy) {
//				int count = (int) getSession().createCriteria(StudyMaster.class).setProjection(Projections.rowCount()).uniqueResult();
//				if(count == 1) {
//					LoginUsers user = (LoginUsers) getSession().createCriteria(LoginUsers.class).add(Restrictions.eq("username", studyMaster.getCreatedBy())).uniqueResult();
//					user.setActiveStudy(studyMaster);
//				}
//				firstStudy = false;
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return no;
	}

	@Override
	public List<LoginUsers> getStudyDirectorList() {
		List<LoginUsers> usersList = null;
		RoleMaster role = null;
		try {
			role = (RoleMaster) getSession().createCriteria(RoleMaster.class).add(Restrictions.eq("role", "SD"))
					.uniqueResult();
			if (role != null) {
				usersList = getSession().createCriteria(LoginUsers.class).add(Restrictions.eq("role", role)).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usersList;
	}

	@Override
	public LoginUsers getEmployeeMasterRecord(Long userId) {
		return (LoginUsers) getSession().createCriteria(LoginUsers.class).add(Restrictions.eq("id", userId))
				.uniqueResult();
	}

	@Override
	public List<StudyMaster> getUserAssignedStudiesList() {
		RoleMaster role = null;
		List<StudyMaster> smList = null;
		List<Long> ids = null;
		try {
			role = (RoleMaster) getSession().createCriteria(RoleMaster.class).add(Restrictions.eq("role", "SD"))
					.uniqueResult();
			if (role != null) {
				ids = getSession().createCriteria(LoginUsers.class).add(Restrictions.eq("role", role))
						.setProjection(Projections.property("id")).list();
				if (ids.size() > 0) {
					smList = getSession().createCriteria(StudyMaster.class).add(Restrictions.in("sdUser", ids)).list();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smList;
	}

	@Override
	public List<StudyMaster> getStudyMasterListBasedOnLogin(Long userId) {
//		LoginUsers user = gets.findById(userId);
		List<StudyMaster> smList = null;
		List<String> strList = new ArrayList<>();
		List<Long> statusIds = null;
		try {
			strList.add("IN");
//				strList.add("SU");
//				strList.add("SI");
			statusIds = getSession().createCriteria(StatusMaster.class).add(Restrictions.in("statusCode", strList))
					.setProjection(Projections.property("id")).list();
			Criteria cri = getSession().createCriteria(StudyMaster.class).add(Restrictions.in("status.id", statusIds));
			Criterion cr1 = Restrictions.eq("sdUser", userId);
			Criterion cr2 = Restrictions.eq("asdUser", userId);
			smList = cri.add(Restrictions.or(cr1, cr2)).list();
			for (StudyMaster sm : smList) {
				sm.setStatusCode(sm.getStatus().getStatusCode());
				sm.setDirctore(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return smList;
	}

	@Override
	public List<StudyMaster> getStudyMasterListBasedOnLoginVeiw(Long userId) {
		LoginUsers user = (LoginUsers) getSession().get(LoginUsers.class, userId);
		RoleMaster role = user.getRole();
		List<StudyMaster> smList = null;
		List<String> strList = new ArrayList<>();
		List<Long> statusIds = null;
		strList.add("IN");
		statusIds = getSession().createCriteria(StatusMaster.class).add(Restrictions.in("statusCode", strList))
				.setProjection(Projections.property("id")).list();
		Criteria cri = getSession().createCriteria(StudyMaster.class)
				.add(Restrictions.not(Restrictions.in("status.id", statusIds)));
		if (role.getRole().equals("SD")) {
			try {
				Criterion cr1 = Restrictions.eq("sdUser", userId);
				Criterion cr2 = Restrictions.eq("asdUser", userId);
				smList = cri.add(Restrictions.or(cr1, cr2)).list();
				for (StudyMaster sm : smList) {
					sm.setStatusCode(sm.getStatus().getStatusCode());
					System.out.println(userId + "-" + sm.getSdUser() + "      " + userId + "-" + sm.getAsdUser());
					System.out.println(userId == sm.getSdUser());
					System.out.println(userId == sm.getAsdUser());
					if ((userId == sm.getSdUser() || userId == sm.getAsdUser())
							&& sm.getStatus().getStatusCode().equals(StatusMasterCodes.SD.toString()))
						sm.setDirctore(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				smList = cri.list();
				for (StudyMaster sm : smList) {
					sm.setStatusCode(sm.getStatus().getStatusCode());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return smList;
	}

	@Override
	public StudyMaster getStudyMasterRecord(Long studyId) {
		StudyMaster sm = null;
		List<GroupInfo> giList = null;
		try {
			sm = (StudyMaster) getSession().createCriteria(StudyMaster.class).add(Restrictions.eq("id", studyId))
					.uniqueResult();
			if (sm != null) {
				giList = getSession().createCriteria(GroupInfo.class).add(Restrictions.eq("study", sm)).list();
				sm.setGroupInfo(giList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sm;

	}

	@Override
	public boolean updateStudyMetaDataDetails(StudyMaster studyMaster, StudyMasterLog smLog, List<GroupInfo> groupInfos,
			List<SubGroupInfo> subGroups, List<SubGroupAnimalsInfo> animalInfo) {
		boolean flag = false;
		long no = 0;
		StatusMaster sm = null;
//		try {
		no = (long) getSession().save(smLog);
		if (no > 0) {
			sm = (StatusMaster) getSession().createCriteria(StatusMaster.class).add(Restrictions.eq("statusCode", "SD"))
					.uniqueResult();
			studyMaster.setStatus(sm);
			getSession().merge(studyMaster);
			flag = true;
		}
		if (flag) {
			for (GroupInfo gf : groupInfos) {
				gf.setStudy(studyMaster);
				getSession().save(gf);
			}

			for (SubGroupInfo gf : subGroups) {
				gf.setStudy(studyMaster);
				getSession().save(gf);
			}

			for (SubGroupAnimalsInfo gf : animalInfo) {
				gf.setStudy(studyMaster);
				getSession().save(gf);
			}
		}

//		} catch (Exception e) {
//			e.printStackTrace();
//			return flag;
//		}
		return flag;
	}

	@Override
	public List<StudyMaster> getAllStudyDesignStatusStudiesList(Long userId) {
		List<StudyMaster> smList = null;
		List<String> strList = new ArrayList<>();
		List<Long> stdIds = null;
		List<Long> statusIds = null;
		try {
			strList.add("SD");
			strList.add("SI");
			strList.add("IN");
			statusIds = getSession().createCriteria(StatusMaster.class).add(Restrictions.in("statusCode", strList))
					.setProjection(Projections.property("id")).list();
			stdIds = getSession().createCriteria(UserWiseStudiesAsignMaster.class)
					.add(Restrictions.eq("userId.id", userId)).setProjection(Projections.property("studyMaster.id"))
					.list();
			if (stdIds.size() > 0) {
				smList = getSession().createCriteria(StudyMaster.class).add(Restrictions.in("status.id", statusIds))
						.add(Restrictions.in("id", stdIds)).list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return smList;
	}

	@Override
	public List<StudyMaster> getStudyMasterListForUpdation() {
		List<StudyMaster> smList = null;
		List<String> strList = new ArrayList<>();
		try {
			StatusMaster status = (StatusMaster) getSession().createCriteria(StatusMaster.class)
					.add(Restrictions.eq("statusCode", "In")).uniqueResult();
//			strList.add("Initiate");
//			strList.add("Design");
//			strList.add("Inprogress");
			smList = getSession().createCriteria(StudyMaster.class).add(Restrictions.eq("status.id", status.getId()))
					.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smList;
	}

	@Override
	public boolean updateStudyMaterRecord(StudyMaster stdPojo, StudyMasterLog smLog) {
		boolean flag = false;
		long no = 0;
		try {
			no = (long) getSession().save(smLog);
			if (no > 0) {
				getSession().update(stdPojo);
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}

	@Override
	public LoginUsers getLoginUsersWithId(Long sdUser) {
		return (LoginUsers) getSession().createCriteria(LoginUsers.class).add(Restrictions.eq("id", sdUser))
				.uniqueResult();
	}

	@Override
	public List<StudyMaster> getStudyMasterForSearch(String studynoval, String studyNameval, String sponsorval,
			String statusval, String roleval) {
		List<StudyMaster> studyl = null;
		if (!studynoval.equals("-1") && studyNameval.equals("-1") && sponsorval.equals("-1") && statusval.equals("-1")
				&& roleval.equals("-1")) {
			studyl = (List<StudyMaster>) getSession().createCriteria(StudyMaster.class)
					.add(Restrictions.eq("studyNo", studynoval)).list();
		}
		if (studynoval.equals("-1") && !studyNameval.equals("-1") && sponsorval.equals("-1") && statusval.equals("-1")
				&& roleval.equals("-1")) {
			studyl = (List<StudyMaster>) getSession().createCriteria(StudyMaster.class)
					.add(Restrictions.eq("studyDesc", studyNameval)).list();
		}
		if (studynoval.equals("-1") && studyNameval.equals("-1") && !sponsorval.equals("-1") && statusval.equals("-1")
				&& roleval.equals("-1")) {
			studyl = (List<StudyMaster>) getSession().createCriteria(StudyMaster.class)
					.add(Restrictions.eq("sponsorname", sponsorval)).list();
		}
		if (studynoval.equals("-1") && studyNameval.equals("-1") && sponsorval.equals("-1") && !statusval.equals("-1")
				&& roleval.equals("-1")) {
			StatusMaster sm = getStatusMasterRecord(statusval);
			studyl = (List<StudyMaster>) getSession().createCriteria(StudyMaster.class)
					.add(Restrictions.eq("status", sm)).list();
		}
		if (studynoval.equals("-1") && studyNameval.equals("-1") && sponsorval.equals("-1") && statusval.equals("-1")
				&& !roleval.equals("-1")) {
			studyl = (List<StudyMaster>) getSession().createCriteria(StudyMaster.class)
					.add(Restrictions.eq("studyNo", roleval)).list();
		}

		return studyl;
	}

	@Override
	public com.covide.dto.SatusAndWorkFlowDetailsDto SatusAndWorkFlowDetailsDto(String statusCode, String moduleName) {
		com.covide.dto.SatusAndWorkFlowDetailsDto sawfd = null;
		StatusMaster sm = null;
		WorkFlowStatusDetails wfsd = null;
		try {
			sm = (StatusMaster) getSession().createCriteria(StatusMaster.class)
					.add(Restrictions.eq("statusCode", statusCode)).uniqueResult();
			wfsd = (WorkFlowStatusDetails) getSession().createCriteria(WorkFlowStatusDetails.class)
					.add(Restrictions.eq("module", moduleName)).uniqueResult();
			if (sm != null) {
				sawfd = new com.covide.dto.SatusAndWorkFlowDetailsDto();
				sawfd.setSm(sm);
				sawfd.setWfsd(wfsd);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sawfd;
	}

	@Override
	public List<DepartmentMaster> getDepartmentMasterList() {
		return getSession().createCriteria(DepartmentMaster.class).list();
	}

	@Override
	public com.covide.dto.SatusAndWorkFlowDetailsDto getstatusAndWorkFlowDetailsDtoForUpdation(Long studyId,
			String fromStatus, String toStatus) {
		SatusAndWorkFlowDetailsDto sawfdd = null;
		StudyMaster sm = null;
		StatusMaster fStatus = null;
		StatusMaster tStatus = null;
		WorkFlowStatusDetails wfsd = null;
		StudyDesignStatus sds = null;
		try {
			sm = (StudyMaster) getSession().createCriteria(StudyMaster.class).add(Restrictions.eq("id", studyId))
					.uniqueResult();

			fStatus = (StatusMaster) getSession().createCriteria(StatusMaster.class)
					.add(Restrictions.eq("statusCode", fromStatus)).uniqueResult();

			tStatus = (StatusMaster) getSession().createCriteria(StatusMaster.class)
					.add(Restrictions.eq("statusCode", toStatus)).uniqueResult();

			wfsd = (WorkFlowStatusDetails) getSession().createCriteria(WorkFlowStatusDetails.class)
					.add(Restrictions.eq("fromStatus", fStatus)).add(Restrictions.eq("toStatus", tStatus))
					.uniqueResult();

			sds = (StudyDesignStatus) getSession().createCriteria(StudyDesignStatus.class)
					.add(Restrictions.eq("studyId", studyId)).uniqueResult();

			sawfdd = new SatusAndWorkFlowDetailsDto();
			sawfdd.setSm(tStatus);
			sawfdd.setStudy(sm);
			sawfdd.setWfsd(wfsd);
			sawfdd.setSds(sds);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sawfdd;
	}

	@Override
	public StatusMaster getStatusMasterRecord(String newStatus) {
		StatusMaster sm = null;
		try {
			if (newStatus.equals("Design")) {
				sm = (StatusMaster) getSession().createCriteria(StatusMaster.class)
						.add(Restrictions.eq("statusCode", "SD")).uniqueResult();
			} else if (newStatus.equals("Inprogress")) {
				sm = (StatusMaster) getSession().createCriteria(StatusMaster.class)
						.add(Restrictions.eq("statusCode", "SI")).uniqueResult();
			} else if (newStatus.equals("Frozen")) {
				sm = (StatusMaster) getSession().createCriteria(StatusMaster.class)
						.add(Restrictions.eq("statusCode", "SF")).uniqueResult();
			} else if (newStatus.equals("Locked")) {
				sm = (StatusMaster) getSession().createCriteria(StatusMaster.class)
						.add(Restrictions.eq("statusCode", "SL")).uniqueResult();
			} else if (newStatus.equals("Cancel")) {
				sm = (StatusMaster) getSession().createCriteria(StatusMaster.class)
						.add(Restrictions.eq("statusCode", "SC")).uniqueResult();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sm;
	}

	@Override
	public boolean updateStudyMetaDataRecords(StudyMaster smPojo, StudyMasterLog smLog, List<GroupInfo> existsGroupList,
			List<GroupInfo> newgroupList, String userName, Integer groups) {
		boolean flag = false;
		long no = 0;
		StatusMaster sm = null;
		Map<Long, SubGroupInfo> subgMap = new HashMap<>();
		List<Long> ids = new ArrayList<>();
		try {
			for (GroupInfo gf : newgroupList) {
				gf.setId(null);
				gf.setStudy(smPojo);
				getSession().save(gf);
			}
			for (GroupInfo gf2 : existsGroupList) {
				GroupInfo gi = (GroupInfo) getSession().createCriteria(GroupInfo.class)
						.add(Restrictions.eq("id", gf2.getId())).uniqueResult();

				List<SubGroupInfo> sgiList = getSession().createCriteria(SubGroupInfo.class)
						.add(Restrictions.eq("group", gi)).add(Restrictions.eq("study", smPojo)).list();
				int count = 0;
				if (sgiList.size() > 0)
					count = sgiList.size() - gi.getGroupTest();
				if (count != 0) {
					for (SubGroupInfo sgi : sgiList) {
						ids.add(sgi.getId());
						subgMap.put(sgi.getId(), sgi);
					}
					Collections.sort(ids, Collections.reverseOrder());
					for (int i = count; i <= count;) {
						long subId = ids.get(i);
						SubGroupInfo sgi = subgMap.get(subId);
						SubgroupInfoLog sgiLog = new SubgroupInfoLog();
						BeanUtils.copyProperties(sgi, sgiLog);
						sgiLog.setId(null);
						sgiLog.setSgi(sgi);
						sgiLog.setUpdatedBy(userName);
						sgiLog.setUpdatedOn(new Date());
						long sgiLogNo = (long) getSession().save(sgiLog);
						if (sgiLogNo > 0) {
							sgi.setStatus("inactive");
							getSession().update(sgi);
						}
						if (i != 0)
							i--;
					}
				}
				if (gi != null) {
					BeanUtils.copyProperties(gf2, gi);
					gi.setStudy(smPojo);
					getSession().update(gi);
				}
			}
			no = (long) getSession().save(smLog);
			if (no > 0) {
				sm = (StatusMaster) getSession().createCriteria(StatusMaster.class)
						.add(Restrictions.eq("statusCode", "SD")).uniqueResult();
				smPojo.setStatus(sm);
				getSession().merge(smPojo);
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}

	@Override
	public com.covide.dto.SatusAndWorkFlowDetailsDto getSatusAndWorkFlowDetailsDtoDetails(Long studyId,
			String fromStatus, String toStatus) {
		SatusAndWorkFlowDetailsDto sawfdd = null;
		StudyMaster sm = null;
		StatusMaster fStatus = null;
		StatusMaster tStatus = null;
		WorkFlowStatusDetails wfsd = null;
		StudyDesignStatus sds = null;
		try {
			sm = (StudyMaster) getSession().createCriteria(StudyMaster.class).add(Restrictions.eq("id", studyId))
					.uniqueResult();

			fStatus = (StatusMaster) getSession().createCriteria(StatusMaster.class)
					.add(Restrictions.eq("statusCode", fromStatus)).uniqueResult();

			tStatus = (StatusMaster) getSession().createCriteria(StatusMaster.class)
					.add(Restrictions.eq("statusCode", toStatus)).uniqueResult();

			wfsd = (WorkFlowStatusDetails) getSession().createCriteria(WorkFlowStatusDetails.class)
					.add(Restrictions.eq("fromStatus", fStatus)).add(Restrictions.eq("toStatus", tStatus))
					.uniqueResult();

			sds = (StudyDesignStatus) getSession().createCriteria(StudyDesignStatus.class)
					.add(Restrictions.eq("studyId", studyId)).uniqueResult();

			sawfdd = new SatusAndWorkFlowDetailsDto();
			sawfdd.setSm(tStatus);
			sawfdd.setStudy(sm);
			sawfdd.setWfsd(wfsd);
			sawfdd.setSds(sds);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sawfdd;

	}

	@Override
	public boolean saveUpdateStdyDesignStatus(StudyDesignStatus sds, ApplicationAuditDetails aad) {
		boolean flag = false;
		boolean finalFlag = false;
		long aadNo = 0;
		try {
			if (sds != null) {
				getSession().update(sds);
				flag = true;
			} else
				flag = true;
			aadNo = (long) getSession().save(aad);
			if (flag && aadNo > 0)
				finalFlag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return finalFlag;
		}
		return finalFlag;
	}

	@Override
	public RoleMaster getRoleMasterRecord(Long roleId) {
		return (RoleMaster) getSession().createCriteria(RoleMaster.class).add(Restrictions.eq("id", roleId))
				.uniqueResult();
	}

	@Override
	public long saveRoleMasterRecord(String username, RoleMaster role) {
		long no = 0;
		try {
			role.setCreatedBy(username);
			role.setCreatedOn(new Date());
			role.setStatus('T');
			no = (long) getSession().save(role);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return no;
	}

	@Override
	public List<LoginUsers> getLoginUserRecordsList(Set<Long> sdIds) {
		return getSession().createCriteria(LoginUsers.class).add(Restrictions.in("id", sdIds)).list();
	}

	@Override
	public StatusMaster statusMaster(String string) {
		return (StatusMaster) getSession().createCriteria(StatusMaster.class).add(Restrictions.eq("statusCode", string))
				.uniqueResult();
	}

	@Override
	public List<StaticData> staticDatas(String fieldName) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(StaticData.class).add(Restrictions.eq("fieldName", fieldName)).list();
	}

	@Override
	public StaticData staticDatasById(long id) {
		// TODO Auto-generated method stub
		return (StaticData) getSession().get(StaticData.class, id);
	}

	@Override
	public StudyDesignStatus studyDesignStatus(Long studyId) {
		// TODO Auto-generated method stub
		return (StudyDesignStatus) getSession().createCriteria(StudyDesignStatus.class)
				.add(Restrictions.eq("studyId", studyId)).uniqueResult();
	}

	@Override
	public List<InstrumentIpAddress> instrumentIpAddresses(Long activeStatusId, boolean withActiveStatus) {
		// TODO Auto-generated method stub
		Criteria cri = getSession().createCriteria(InstrumentIpAddress.class);
		if (withActiveStatus)
			cri.add(Restrictions.eq("activeStatus.id", activeStatusId));
		cri.addOrder(Order.asc("orderNo"));
		return cri.list();
	}

	@Override
	public InstrumentIpAddress instrumentIpAddress(Long ipaddress) {
		// TODO Auto-generated method stub
		return (InstrumentIpAddress) getSession().get(InstrumentIpAddress.class, ipaddress);
	}

	@Override
	public InstrumentIpAddress instumentIpAddress() {
		return (InstrumentIpAddress) getSession().createCriteria(InstrumentIpAddress.class)
				.add(Restrictions.eq("configuredIp", true)).uniqueResult();
	}

	@Override
	public InstrumentIpAddress instumentIpAddress(String insturmentName) {
		StatusMaster activeStatus = statusDao.statusMaster(StatusMasterCodes.ACTIVE.toString());
		return (InstrumentIpAddress) getSession().createCriteria(InstrumentIpAddress.class)
				.add(Restrictions.eq("instrumentName", insturmentName))
				.add(Restrictions.eq("activeStatus", activeStatus)).uniqueResult();
	}

	@Override
	public SysmexDto saveSysmexData(SysmexDto dto) {
		try {
			getSession().save(dto.getSysData());
			getSession().save(dto.getSysmexRawData());
			List<SysmexTestCodeData> sysmexTestCodeDatas = dto.getSysmexTestCodeDatas();
			for (SysmexTestCodeData tc : sysmexTestCodeDatas) {
				getSession().save(tc);
			}
			return dto;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<SysmexData> saveSysmexData() {
		// TODO Auto-generated method stub
		List<SysmexData> result = getSession().createCriteria(SysmexData.class).list();
		for (SysmexData data : result) {
			data.setSysmexTestCodeData(sysmexTestCodeDataForAnimal(data, null));
		}
		return result;
	}

	public List<SysmexTestCodeData> sysmexTestCodeDataForAnimal(SysmexData data, List<Long> tcIds) {
		// TODO Auto-generated method stub
		Criteria cri = getSession().createCriteria(SysmexTestCodeData.class)
				.add(Restrictions.eq("sysmexData.id", data.getId()));
		if (tcIds != null && tcIds.size() > 0)
			cri.add(Restrictions.in("studyTestCode.id", tcIds));
		List<SysmexTestCodeData> list = cri.list();
		for (SysmexTestCodeData stcd : list) {
			Hibernate.initialize(stcd.getSysmexData());
			Hibernate.initialize(stcd.getStudyTestCode());
		}

		return list;
	}

	@Override
	public List<SysmexData> sysmexDataList(Long studyNumber, String startDate, String sampleType, String observation)
			throws ParseException {
		List<Long> obs = new ArrayList<>();
		if(observation != null)
			obs = observationInturmentConfigurationDao.observationInturmentConfiguration(studyNumber, observation);
		InstrumentIpAddress ip = instumentIpAddress("SYSMEX");
		List<StudyTestCodes> tcs = studyInstumentTestCodes(ip.getId(), studyNumber, observation);
//		List<Long> tcIds = getSession().createCriteria(StudyTestCodes.class)
//				.add(Restrictions.eq("study.id", studyNumber)).setProjection(Projections.property("id")).list();
		Criteria cri = getSession().createCriteria(SysmexData.class).add(Restrictions.eq("study.id", studyNumber));
		if (startDate != null && !startDate.trim().equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
			System.out.println(startDate.trim());
			String[] st = startDate.split("\\-");
			String s = st[0] + "-" + st[1] + "-" + st[2];
			startDate = s;
			System.out.println(startDate.trim());
			Date fromDate = sdf.parse(startDate.trim());
			Calendar c = Calendar.getInstance();
			c.setTime(fromDate);
			c.add(Calendar.DATE, 1);
			Date toDate = sdf.parse(sdf.format(c.getTime()));
			System.out.println(fromDate + "\t" + toDate);
			cri.add(Restrictions.between("startTime", fromDate, toDate));
		}
		if (!sampleType.equals("All")) {
			if (sampleType.equals("QC")) {
				cri.add(Restrictions.or(Restrictions.isNull("animal"), Restrictions.like("testRunType", "QC%")));
			} else if (sampleType.equals("Animal")) {
				cri.add(Restrictions.eq("testRunType", sampleType));
			} else if (sampleType.equals("Other")) {
				cri.add(Restrictions.or(Restrictions.ne("testRunType", "Animal"), Restrictions.or(
						Restrictions.isNull("animal"), Restrictions.ne("", Restrictions.like("testRunType", "QC%")))));
			}
		}
		cri.addOrder(Order.asc("animalNumber"));
		if(obs.size() > 0) {
			cri.add(Restrictions.in("observationInturmentConfiguration.id", obs));
		}
		List<SysmexData> result = cri.list();

		SortedMap<String, List<SysmexData>> animalWise = new TreeMap<>();
		for (SysmexData data : result) {
			Hibernate.initialize(data.getObservationInturmentConfiguration());
			Hibernate.initialize(data.getAnimal());
			data.setSysmexTestCodeData(sysmexTestCodeDataForAnimal(data, null));
			List<SysmexData> lis = animalWise.get(data.getAnimalNumber());
			if (lis == null)
				lis = new ArrayList<>();
			lis.add(data);
			animalWise.put(data.getAnimalNumber(), lis);
		}
		for (Map.Entry<String, List<SysmexData>> temp : animalWise.entrySet()) {
			System.out.println(temp.getKey());

			checkSysmexFinalResult(temp.getValue(), tcs);
		}
		return result;
	}

	public List<SysmexData> checkSysmexFinalResult(List<SysmexData> dataList, List<StudyTestCodes> tcs) {
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

		for (StudyTestCodes tc : tcs) {
			System.out.println(tc.getTestCode().getDisPalyTestCode() + "\t" + tc.getOrderNo());
			List<SysmexTestCodeData> list = testcodeWiseData.get(tc.getOrderNo());
			if (list != null && list.size() > 0) {
				if (list.size() > 1) {
					boolean flag = false;
					for (SysmexTestCodeData sdl : list) {
						if (flag && sdl.isFinalValue()) {
							sdl.setFinalValue(false);
						} else if (sdl.isFinalValue()) {
							flag = true;
						}
					}
				} else {
					list.get(0).setFinalValue(true);
				}
			}

		}
		return dataList;
	}

	@Override
	public List<StagoDataDto> stagoData() {

		List<StagoData> lists = getSession().createCriteria(StagoData.class).list();
		Map<String, List<StagoData>> studyMap = new HashMap<>();
		for (StagoData data : lists) {
			List<StagoData> li = studyMap.get(data.getStudyNumber());
			if (li == null)
				li = new ArrayList<>();
			li.add(data);
			studyMap.put(data.getStudyNumber(), li);
		}

		Map<String, Map<String, List<StagoData>>> mapp = new HashMap<>();
		for (Map.Entry<String, List<StagoData>> m : studyMap.entrySet()) {
			Map<String, List<StagoData>> map = mapp.get(m.getKey());
			if (map == null) {
				map = new HashMap<>();
			}
			List<StagoData> list = m.getValue();
			for (StagoData data : list) {
				List<StagoData> li = map.get(data.getAnimalNo());
				if (li == null)
					li = new ArrayList<>();
				li.add(data);
				map.put(data.getAnimalNo(), li);
			}
			mapp.put(m.getKey(), map);
		}

		List<StagoDataDto> result = new ArrayList<>();
		for (Map.Entry<String, Map<String, List<StagoData>>> mm : mapp.entrySet()) {
			Map<String, List<StagoData>> map = mm.getValue();

			for (Map.Entry<String, List<StagoData>> m : map.entrySet()) {
				String no = m.getKey();
				List<StagoData> list = m.getValue();
				StagoDataDto dto = new StagoDataDto();
				dto.setAnimalNo(no);
				for (StagoData sd : list) {
					String rel = sd.getTestResult();
					if (sd.isSelectedResult()) {
						rel = "<font color='green'>" + sd.getTestResult() + "</font>";
					}
					if (dto.getStudy() == null) {
						dto.setStudy(sd.getStudy());
						dto.setId(sd.getStudyNumber() + "_" + dto.getAnimalNo());
					}
					if (sd.getTestName().equals("PT")) {
						if (dto.getPtResult() == null)
							dto.setPtResult(rel);
						else {
							dto.setPtResult(dto.getPtResult() + "," + rel);
							dto.setMultipleResult(true);
						}
					} else if (sd.getTestName().equals("APTT")) {
						if (dto.getApttResult() == null)
							dto.setApttResult(rel);
						else {
							dto.setApttResult(dto.getPtResult() + "," + rel);
							dto.setMultipleResult(true);
						}
					} else if (sd.getTestName().equals("Fibrinogen")) {
						if (dto.getFibrinogenResult() == null)
							dto.setFibrinogenResult(rel);
						else {
							dto.setFibrinogenResult(dto.getPtResult() + "," + rel);
							dto.setMultipleResult(true);
						}
					}
				}
				result.add(dto);
			}
		}

		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public List<StagoDataDto> stagoDataList(Long studyNumber, String startDate, Boolean export, String sampleType, String observation)
			throws ParseException {
		List<Long> obs = new ArrayList<>();
		if(observation != null)
			obs = observationInturmentConfigurationDao.observationInturmentConfiguration(studyNumber, observation);
		SimpleDateFormat sdftime = new SimpleDateFormat(environment.getRequiredProperty("dateTimeSecondsFormat"));
		StudyMaster study = findByStudyId(studyNumber);
		Criteria cri = getSession().createCriteria(StagoData.class).add(Restrictions.eq("study.id", study.getId()));
		if (!sampleType.equals("Both")) {
			cri.add(Restrictions.eq("sampleType", sampleType));
		}
		
		if (startDate != null && !startDate.trim().equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
			System.out.println(startDate.trim());
			String[] st = startDate.split("\\-");
			String s = st[0] + "-" + st[1] + "-" + st[2];
			startDate = s;
			System.out.println(startDate.trim());
			Date fromDate = sdf.parse(startDate.trim());
			Calendar c = Calendar.getInstance();
			c.setTime(fromDate);
			c.add(Calendar.DATE, 1);
			Date toDate = sdf.parse(sdf.format(c.getTime()));
			System.out.println(fromDate + "\t" + toDate);
			cri.add(Restrictions.between("receivedTime", fromDate, toDate));
		}
		
		List<StagoData> lists = new ArrayList<>();
		if(obs.size() > 0) {
			cri.add(Restrictions.in("observationInturmentConfiguration.id", obs));
		} 
		lists = cri.list();
		Map<String, String> animalLotNo = new HashMap<>();
		Map<String, List<StagoData>> map = new HashMap<>();
		for (StagoData data : lists) {
			Hibernate.initialize(data.getObservationInturmentConfiguration());
			Hibernate.initialize(data.getStudy());
			List<StagoData> li = map.get(data.getAnimalNo());
			if (li == null)
				li = new ArrayList<>();
			li.add(data);
			map.put(data.getAnimalNo(), li);
			animalLotNo.put(data.getAnimalNo(), data.getLotNo());
		}

		List<StagoDataDto> result = new ArrayList<>();
		Map<String, Boolean> tcflag = new HashMap<>();
		for (Map.Entry<String, List<StagoData>> m : map.entrySet()) {

			List<StagoData> list = m.getValue();
			StagoDataDto dto = new StagoDataDto();
			dto.setAnimalNo(m.getKey());
			dto.setLotNo(animalLotNo.get(m.getKey()));

			StagoData single = list.get(0);

			if (single.getObservationInturmentConfiguration().getStudyTreatmentDataDates() != null) {
				dto.setGroupName(single.getObservationInturmentConfiguration().getStudyTreatmentDataDates()
						.getStdSubGroupObservationCrfs().getSubGroupInfo().getGroup().getGroupName());
				dto.setGroupNumber(single.getObservationInturmentConfiguration().getStudyTreatmentDataDates()
						.getStdSubGroupObservationCrfs().getSubGroupInfo().getGroup().getGroupNo());
			}
			if (single.getStudyAnimal() != null) {
				dto.setAnimalSeqno(single.getStudyAnimal().getSequnceNo());
				dto.setAnimalId(single.getStudyAnimal().getId());
			}else {
				dto.setAnimalSeqno(Integer.parseInt(single.getAnimalNo()));
			}

			for (StagoData sd : list) {
				System.out.println(sd.getAnimalNo() + "\t" + sd.getTestName() + "\t" + sd.getTestResult());
				String sdani = null;
				if (sd.getStudyAnimal() != null) {
					sdani = sd.getStudyAnimal().getPermanentNo() + "_" + sd.getTestName();
				} else {
					sdani = sd.getLotNo();
				}
				if (!tcflag.containsKey(sdani) && sd.isSelectedResult()) {
					tcflag.put(sdani, true);
				} else if (tcflag.containsKey(sdani) && (tcflag.get(sdani) == true) && sd.isSelectedResult()) {
//					sd.setSelectedResult(false);
				}

//				if(!flagg && sd.isSelectedResult()) {
//					flagg = true;
//				}else if(flagg && sd.isSelectedResult())
//					sd.setSelectedResult(false);
				dto.setAnimal(sd.getStudyAnimal());
				if (dto.getStudy() == null) {
					dto.setStudy(sd.getStudy());
					dto.setId(sd.getId() + "," + dto.getAnimalNo());
					dto.setRowId(sd.getId() + "_" + dto.getAnimalNo());
				}
//				String rel = sd.getTestResult() + "/" + sd.getId();
				String rel = sd.getTestResult();
				if (sd.isSelectedResult() && export == null) {
					rel = "<font color='green'>" + sd.getTestResult() + "</font>";
				}
				if (sd.getTestName().equals("PT")) {
					if (export != null) {
						if (sd.isSelectedResult()) {
							dto.setPtResult(rel);
							dto.setPtResultDoneBy(sd.getActivityDoneBy().getUsername());
							dto.setPtResultTime(sdftime.format(sd.getReceivedTime()));
						}

					} else {
						if (dto.getPtResult().trim().equals("")) {
							dto.setPtResult(rel);
							dto.setPtResultDoneBy(sd.getActivityDoneBy().getUsername());
							dto.setPtResultTime(sdftime.format(sd.getReceivedTime()));
						} else {
							dto.setPtResult(dto.getPtResult() + "," + rel);
							dto.setMultipleResult(true);
						}
					}

				} else if (sd.getTestName().equals("APTT")) {
					if (export != null) {
						if (sd.isSelectedResult()) {
							dto.setApttResult(rel);
							dto.setApttResultDoneBy(sd.getActivityDoneBy().getUsername());
							dto.setApttResultTime(sdftime.format(sd.getReceivedTime()));
						}
					} else {
						if (dto.getApttResult().trim().equals("")) {
							dto.setApttResult(rel);
							dto.setApttResultDoneBy(sd.getActivityDoneBy().getUsername());
							dto.setApttResultTime(sdftime.format(sd.getReceivedTime()));
						} else {
							dto.setApttResult(dto.getApttResult() + "," + rel);
							dto.setMultipleResult(true);
						}
					}
				} else if (sd.getTestName().equals("Fibrinogen")) {
					if (export != null) {
						if (sd.isSelectedResult()) {
							dto.setFibrinogenResult(rel);
							dto.setFibrinogenResultDoneBy(sd.getActivityDoneBy().getUsername());
							dto.setFibrinogenResultTime(sdftime.format(sd.getReceivedTime()));
						}
					} else {
						if (dto.getFibrinogenResult().trim().equals("")) {
							dto.setFibrinogenResult(rel);
							dto.setFibrinogenResultDoneBy(sd.getActivityDoneBy().getUsername());
							dto.setFibrinogenResultTime(sdftime.format(sd.getReceivedTime()));
						} else {
							dto.setFibrinogenResult(dto.getFibrinogenResult() + "," + rel);
							dto.setMultipleResult(true);
						}
					}
				}
			}
			result.add(dto);
		}
		Collections.sort(result, new Comparator<StagoDataDto>() {
			@Override
			public int compare(StagoDataDto p1, StagoDataDto p2) {
				return p1.getAnimalNo().compareTo(p2.getAnimalNo());
			}
		});
		return result;
	}

	@Override
	public Map<String, StagoDataDto> stagoDataList(Long studyId, List<Long> animalIds, Boolean export, String startDate,
			String sampleType, String observation) throws ParseException {
		List<Long> obs = new ArrayList<>();
		if(observation != null)
			obs = observationInturmentConfigurationDao.observationInturmentConfiguration(studyId, observation);
		SimpleDateFormat sdftime = new SimpleDateFormat(environment.getRequiredProperty("dateTimeSecondsFormat"));
		StudyMaster study = findByStudyId(studyId);
		InstrumentIpAddress ip = instumentIpAddress("STAGO");
		List<StudyTestCodes> tcs = studyInstumentTestCodes(ip.getId(), studyId, observation);
		Set<String> tcSet = new HashSet<>();
		SortedMap<Integer, StudyTestCodes> selecteTestCodes = new TreeMap<>();
		for (StudyTestCodes tc : tcs) {
			selecteTestCodes.put(tc.getOrderNo(), tc);
			tcSet.add(tc.getTestCode().getTestCode());
		}
		
		Criteria cri = getSession().createCriteria(StagoData.class);
		if (studyId != null && study != null)
			cri.add(Restrictions.eq("study.id", study.getId()));
		List<Long> ids = new ArrayList<>(animalIds);
		animalIds.clear();
		for (Long id : ids) {
			if (id != null)
				animalIds.add(id);
		}
		 
		if (animalIds.size() > 0) {
			cri.add(Restrictions.in("studyAnimal.id", animalIds));
		} else {
			if (!sampleType.equals("Both")) {
				cri.add(Restrictions.eq("sampleType", sampleType));
			}
			if (startDate != null && !startDate.trim().equals("")) {
				SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
				System.out.println(startDate.trim());
				String[] st = startDate.split("\\-");
				String s = st[0] + "-" + st[1] + "-" + st[2];
				startDate = s;
				System.out.println(startDate.trim());
				Date fromDate = sdf.parse(startDate.trim());
				Calendar c = Calendar.getInstance();
				c.setTime(fromDate);
				c.add(Calendar.DATE, 1);
				Date toDate = sdf.parse(sdf.format(c.getTime()));
				System.out.println(fromDate + "\t" + toDate);
				cri.add(Restrictions.between("receivedTime", fromDate, toDate));
			}
		}

		cri.add(Restrictions.eq("selectedResult", true));
		if(obs.size() > 0) {
			cri.add(Restrictions.in("observationInturmentConfiguration.id", obs));
		}
		List<StagoData> lists = cri.list();
		System.out.println("lists : " + lists);
		System.out.println("lists.size() :" + lists.size());
		Map<String, List<StagoData>> map = new HashMap<>();
		for (StagoData data : lists) {
			if (data.getSampleType().equals("QC")) {
				String animalNo = data.getAnimalNo();
				List<StagoData> li = map.get(animalNo);
				if (li == null)
					li = new ArrayList<>();
				li.add(data);
				map.put(animalNo, li);
			}else if (data.getStudyAnimal() != null) {
				String animalNo = data.getStudyAnimal().getAnimalNo();
				if(!observation.equals("Acclimatization")) {
					animalNo = data.getStudyAnimal().getPermanentNo();
				}
				List<StagoData> li = map.get(animalNo);
				if (li == null)
					li = new ArrayList<>();
				li.add(data);
				map.put(animalNo, li);
			} 

		}
		Map<String, StagoDataDto> result = new TreeMap<>();
		Map<String, Boolean> tcflag = new HashMap<>();
		for (Map.Entry<String, List<StagoData>> m : map.entrySet()) {
			List<StagoData> list = m.getValue();
			StagoDataDto dto = new StagoDataDto();
			dto.setAnimalNo(m.getKey());
//			dto.setTcs(tcSet);
			dto.setSelecteTestCodes(selecteTestCodes);
			StagoData single = list.get(0);
			if (single.getStudyAnimal() != null) {
				if(!observation.equals("Acclimatization")) {
					dto.setGroupName(single.getStudyAnimal().getGroupInfo().getGroupName());
					dto.setGroupNumber(single.getStudyAnimal().getGroupInfo().getGroupNo());
//					dto.setAnimalSeqno(single.getStudyAnimal().getSequnceNo());
				}
			}

			
			for (StagoData sd : list) {
				if (dto.getStudy() == null) {
					dto.setStudy(sd.getStudy());
					dto.setId(sd.getId() + "," + dto.getAnimalNo());
					dto.setRowId(sd.getId() + "_" + dto.getAnimalNo());
				}
				if(sampleType.equals("Animal")) {
					dto.setAnimal(sd.getStudyAnimal());
				}
				String rel = sd.getTestResult();
				if (sd.isSelectedResult() && export == null) {
					rel = "<font color='green'>" + sd.getTestResult() + "</font>";
				}
				if (sd.getTestName().equals("PT")) {
					if (export != null && sd.isSelectedResult()) {
						dto.setPtResult(rel);
						dto.setPtResultDoneBy(sd.getActivityDoneBy().getUsername());
						dto.setPtResultTime(sdftime.format(sd.getReceivedTime()));
					} else {
						if (dto.getPtResult().trim().equals("")) {
							dto.setPtResult(rel);
							dto.setPtResultDoneBy(sd.getActivityDoneBy().getUsername());
							dto.setPtResultTime(sdftime.format(sd.getReceivedTime()));
						} else {
							dto.setPtResult(dto.getPtResult() + "," + rel);
							dto.setMultipleResult(true);
						}
					}

				} else if (sd.getTestName().equals("APTT")) {
					if (export != null && sd.isSelectedResult()) {
						dto.setApttResult(rel);
						dto.setApttResultDoneBy(sd.getActivityDoneBy().getUsername());
						dto.setApttResultTime(sdftime.format(sd.getReceivedTime()));
					} else {
						if (dto.getApttResult().trim().equals("")) {
							dto.setApttResult(rel);
							dto.setApttResultDoneBy(sd.getActivityDoneBy().getUsername());
							dto.setApttResultTime(sdftime.format(sd.getReceivedTime()));
						} else {
							dto.setApttResult(dto.getApttResult() + "," + rel);
							dto.setMultipleResult(true);
						}
					}
				} else if (sd.getTestName().equals("Fibrinogen")) {
					if (export != null && sd.isSelectedResult()) {
						dto.setFibrinogenResult(rel);
						dto.setFibrinogenResultDoneBy(sd.getActivityDoneBy().getUsername());
						dto.setFibrinogenResultTime(sdftime.format(sd.getReceivedTime()));
					} else {
						if (dto.getFibrinogenResult().trim().equals("")) {
							dto.setFibrinogenResult(rel);
							dto.setFibrinogenResultDoneBy(sd.getActivityDoneBy().getUsername());
							dto.setFibrinogenResultTime(sdftime.format(sd.getReceivedTime()));
						} else {
							dto.setFibrinogenResult(dto.getFibrinogenResult() + "," + rel);
							dto.setMultipleResult(true);
						}
					}
				}
			}
			result.put(m.getKey(), dto);
		}

		return result;
	}

	@Override
	public List<StudyMaster> allSysmaxStudyNumbers() {
		List<String> studyNumbers = getSession().createCriteria(SysmexData.class)
				.setProjection(Projections.distinct(Projections.property("studyNumber"))).list();
		if (studyNumbers.size() > 0) {
			return studyMasterWithStudyNumbers(studyNumbers);
		} else
			return new ArrayList<>();
	}

	private List<StudyMaster> studyMasterWithStudyNumbers(List<String> studyNumbers) {
		List<StudyMaster> studys = getSession().createCriteria(StudyMaster.class)
				.add(Restrictions.in("studyNo", studyNumbers)).list();
		return studys;
	}

	@Override
	public List<StudyMaster> allStagoStudyNumbers() {
		List<String> studyNumbers = getSession().createCriteria(StagoData.class)
				.setProjection(Projections.distinct(Projections.property("studyNumber"))).list();
		if (studyNumbers.size() > 0) {
			return studyMasterWithStudyNumbers(studyNumbers);
		} else
			return new ArrayList<>();
	}

	@Override
	public List<SysmexTestCode> sysmexTestCodes(boolean b) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(SysmexTestCode.class).add(Restrictions.eq("activeStatus", b))
				.addOrder(Order.asc("orderNo")).list();
	}

	@Override
	public SysmexData sysmexDataById(Long sysmexDataId) {
		// TODO Auto-generated method stub
		SysmexData data = (SysmexData) getSession().get(SysmexData.class, sysmexDataId);
		data.setSysmexTestCodeData(sysmexTestCodeDataForAnimal(data, null));
		return data;
	}

	@Override
	public List<VistrosDataDto> vistrosData(SortedMap<Integer, String> testCodes) {
//		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateTimeFormat"));
//		List<String> nos = getSession().createCriteria(VistrosData.class)
//				.setProjection(Projections.property("animalNo")).list();
//		nos = new ArrayList<>(new HashSet<>(nos));
//		List<VistrosDataDto> result = new ArrayList<>();
//
//		Map<String, List<VistrosData>> map = new HashMap<>();
//		List<VistrosData> lists = getSession().createCriteria(VistrosData.class).list();
//		for (VistrosData vd : lists) {
//			List<VistrosData> li = map.get(vd.getAnimalNo());
//			if (li == null)
//				li = new ArrayList<>();
//			li.add(vd);
//			map.put(vd.getAnimalNo(), li);
//		}
//		List<Long> ids = new ArrayList<>();
//		for (Map.Entry<String, List<VistrosData>> ma : map.entrySet()) {
//			String animal = ma.getKey();
//			List<VistrosData> list = ma.getValue();
//			VistrosData no = list.get(0);
//			VistrosDataDto dto = new VistrosDataDto();
//			dto.setId(no.getId());
//			dto.setStudy(no.getStudy());
//			dto.setAnimalNo(no.getAnimalNo());
//			dto.setTestDate(sdf.format(no.getTestDate()));
//			dto.setMachineName(no.getMachineName());
//			Map<String, String> mp = new HashMap<>();
//			for (VistrosData vd : list) {
//				if (!ids.contains(vd.getId())) {
//					if (mp.get(vd.getTestName()) == null)
//						mp.put(vd.getTestName(), vd.getResult().toString());
//					else
//						mp.put(vd.getTestName(), mp.get(vd.getTestName()) + "," + vd.getResult().toString());
//					ids.add(vd.getId());
//				}
//
//			}
//
//			SortedMap<Integer, String> testNameResult = new TreeMap<>();
//			for (Map.Entry<Integer, String> m : testCodes.entrySet()) {
//				if (mp.containsKey(m.getValue())) {
////					if(testNameResult.get(m.getKey()) != null)
////						testNameResult.put(m.getKey(), testNameResult.get(m.getKey())+","+mp.get(m.getValue()));
////					else
//					testNameResult.put(m.getKey(), mp.get(m.getValue()));
//				} else {
////					if(testNameResult.get(m.getKey()) == null)
//					testNameResult.put(m.getKey(), "");
//				}
//			}
//			dto.setTestNameResult(testNameResult);
//			result.add(dto);
//		}
//
//		return result;
		return null;
	}

	@Override
	public List<VistrosDataDto> vistrosDataList(String studyNumber, String startDate,
			SortedMap<Integer, String> testCodes) throws ParseException {
//		SimpleDateFormat sdf2 = new SimpleDateFormat(environment.getRequiredProperty("dateTimeFormat"));
//		StudyMaster study = findByStudyNo(studyNumber);
//		Criteria cri = getSession().createCriteria(VitrosData.class);
//		if (studyNumber != null && !studyNumber.equals("") && !studyNumber.equals("-1") && study != null)
//			cri.add(Restrictions.eq("study.id", study.getId()));
//		if (startDate != null && !startDate.trim().equals("")) {
//			SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
//			System.out.println(startDate.trim());
//			String[] st = startDate.split("\\-");
//			String s = st[0] + "-" + st[1] + "-" + st[2];
//			startDate = s;
//			System.out.println(startDate.trim());
//			Date fromDate = sdf.parse(startDate.trim());
//			Calendar c = Calendar.getInstance();
//			c.setTime(fromDate);
//			c.add(Calendar.DATE, 1);
//			Date toDate = sdf.parse(sdf.format(c.getTime()));
//			System.out.println(fromDate + "\t" + toDate);
//			cri.add(Restrictions.between("testDate", fromDate, toDate));
//		}
//		List<VitrosData> lists = cri.list();
//		Map<String, List<VitrosData>> map = new HashMap<>();
//		for (VitrosData data : lists) {
//			List<VitrosData> li = map.get(data.getAnimalNo());
//			if (li == null)
//				li = new ArrayList<>();
//			li.add(data);
//			map.put(data.getAnimalNo(), li);
//		}
//		List<VistrosDataDto> result = new ArrayList<>();
//		for (Map.Entry<String, List<VitrosData>> m : map.entrySet()) {
//			List<VitrosData> list = m.getValue();
//			VitrosData no = list.get(0);
//			VistrosDataDto dto = new VistrosDataDto();
//			dto.setId(no.getId());
//			dto.setStudy(no.getStudy());
//			dto.setAnimalNo(no.getAnimalNo());
////			dto.setTestDate(sdf2.format(no.getTestDate()));
//			dto.setMachineName(no.getMachineName());
//			Map<String, String> mp = new HashMap<>();
//			for (VistrosData vd : list) {
//				if (mp.get(vd.getTestName()) == null)
//					mp.put(vd.getTestName(), vd.getResult().toString());
//				else
//					mp.put(vd.getTestName(), mp.get(vd.getTestName()) + "," + vd.getResult().toString());
////				mp.put(vd.getTestName(), vd.getResult().toString());
//			}
//			SortedMap<Integer, String> testNameResult = new TreeMap<>();
//			for (Map.Entry<Integer, String> mpr : testCodes.entrySet()) {
//				if (mp.containsKey(mpr.getValue())) {
//					testNameResult.put(mpr.getKey(), mp.get(mpr.getValue()));
//				} else {
//					testNameResult.put(mpr.getKey(), "");
//				}
//			}
//			dto.setTestNameResult(testNameResult);
//			result.add(dto);
//		}
//		return result;
		return null;
	}

	@Override
	public boolean instrumentIpAddressesStatus(List<Long> insturmentId) {
		// TODO Auto-generated method stub
		try {

			StatusMaster active = statusDao.statusMaster(StatusMasterCodes.ACTIVE.toString());
			StatusMaster inactive = statusDao.statusMaster(StatusMasterCodes.INACTIVE.toString());
			List<InstrumentIpAddress> list = instrumentIpAddresses(null, false);
			list.forEach((ins) -> {
				if (insturmentId.contains(ins.getId())) {
					ins.setActiveStatus(active);
				} else {
					ins.setActiveStatus(inactive);
				}
			});
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<StagoData> stagoDataList(Long studyId, String animalNo, String testName, Long animalId,
			String sampleType, String observation, String startDate) throws ParseException {
		// TODO Auto-generated method stub
		List<Long> obs = new ArrayList<>();
		if(observation != null)
			obs = observationInturmentConfigurationDao.observationInturmentConfiguration(studyId, observation);
		Criteria cri =  getSession().createCriteria(StagoData.class).add(Restrictions.eq("study.id", studyId))
				.add(Restrictions.eq("studyAnimal.id", animalId)).add(Restrictions.eq("testName", testName))
				.add(Restrictions.eq("sampleType", sampleType));
		if (startDate != null && !startDate.trim().equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
			System.out.println(startDate.trim());
			String[] st = startDate.split("\\-");
			String s = st[0] + "-" + st[1] + "-" + st[2];
			startDate = s;
			System.out.println(startDate.trim());
			Date fromDate = sdf.parse(startDate.trim());
			Calendar c = Calendar.getInstance();
			c.setTime(fromDate);
			c.add(Calendar.DATE, 1);
			Date toDate = sdf.parse(sdf.format(c.getTime()));
			System.out.println(fromDate + "\t" + toDate);
			cri.add(Restrictions.between("receivedTime", fromDate, toDate));
		}
		if(obs.size() > 0)
			cri.add(Restrictions.in("observationInturmentConfiguration.id", obs));
		return  cri.list();
	}

	@Override
	public void updateStagoDataList(List<StagoData> list) {
		// TODO Auto-generated method stub
		for (StagoData sd : list)
			getSession().merge(sd);

	}

	@Override
	public List<StudyTestCodes> studyInstumentTestCodes(Long instumentId, Long studyId, String observation) {
		List<Long> obser = new ArrayList<>();
		if(observation != null)
			obser = observationInturmentConfigurationDao.observationInturmentConfiguration(studyId, observation);
		Criteria cri = getSession().createCriteria(StudyTestCodes.class).add(Restrictions.eq("instrument.id", instumentId))
				.add(Restrictions.eq("study.id", studyId))
				.add(Restrictions.eq("activeStatus", true))
				.setProjection(Projections.property("testCode.id"))
				.addOrder(Order.asc("orderNo"));
		if(obser.size() > 0)
			cri.add(Restrictions.in("observationInturmentConfiguration.id", obser));
		List<Long> testCodeIds =  cri.list();
		Set<Long> tcs = new HashSet<>(testCodeIds);
		testCodeIds = new ArrayList<>(tcs);
		if(testCodeIds.size() > 0 )
			return getSession().createCriteria(StudyTestCodes.class).add(Restrictions.eq("instrument.id", instumentId))
					.add(Restrictions.eq("study.id", studyId))
					.add(Restrictions.in("observationInturmentConfiguration.id", obser))
					.add(Restrictions.eq("activeStatus", true))
					.add(Restrictions.in("testCode.id", testCodeIds))
					.addOrder(Order.asc("orderNo")).list();
		else return new ArrayList<>();
	}

	@Override
	public List<StudyMaster> allActiveStudys() {
		// TODO Auto-generated method stub
		return getSession().createCriteria(StudyMaster.class).list();
	}

	@Override
	public List<VitrosData> vitrosRawDataList(String studySuffiex, Date from, Date to, String animal,
			List<String> testcodesset) {
		// TODO Auto-generated method stub
		Criteria cri = getSession().createCriteria(VitrosData.class);
		if (animal == null) {
			if (from != null)
				cri.add(Restrictions.between("testDate", from, to));
			cri.add(Restrictions.like("animalNo", studySuffiex + "%"));
		} else {
			cri.add(Restrictions.eq("animalNo", animal));
		}
		if (testcodesset != null && testcodesset.size() > 0) {
			cri.add(Restrictions.in("testName", testcodesset));
		}
		List<VitrosData> list = cri.list();
		checkVistroFinalResult(list);
		return list;
	}

	@Override
	public List<VitrosData> vitrosRawDataListWithStudyAndDate(Long studyId, Date from, Date to, String animal,
			List<String> testcodesset, String sampleType, String observation) {
		List<Long> obs = new ArrayList<>();
		if(observation != null)
			obs = observationInturmentConfigurationDao.observationInturmentConfiguration(studyId, observation);

		Criteria cri = getSession().createCriteria(VitrosData.class).add(Restrictions.eq("study.id", studyId));
		if (from != null)
			cri.add(Restrictions.between("testDate", from, to));
//		if (animal != null) {			
//			cri.add(Restrictions.like("animalNo", animal + "%"));
//		} 
//		else {
//			cri.add(Restrictions.eq("animalNo", animal));
//		}
		if (sampleType != null) {
			if (sampleType.equals("QC"))
				cri.add(Restrictions.like("animalNo", "QC%"));
			else
				cri.add(Restrictions.not(Restrictions.like("animalNo", "QC%")));
		}
		if (testcodesset != null && testcodesset.size() > 0) {
			cri.add(Restrictions.in("testName", testcodesset));
		}
		if(obs.size() > 0) {
			cri.add(Restrictions.in("observationInturmentConfiguration.id", obs));
		}
		List<VitrosData> list = cri.list();
		checkVistroFinalResult(list);
		return list;
	}

	private void checkVistroFinalResult(List<VitrosData> list) {
		// TODO Auto-generated method stub
		Map<String, Map<String, List<VitrosData>>> map = new HashMap<>();
		list.stream().forEach((o) -> {
			Map<String, List<VitrosData>> animalWise = map.get(o.getAnimalNo());
			if (animalWise == null) {
				animalWise = new HashMap<>();
			}
			List<VitrosData> testResult = animalWise.get(o.getTestName());
			if (testResult == null) {
				testResult = new ArrayList<>();
			}
			testResult.add(o);
			animalWise.put(o.getTestName(), testResult);
			map.put(o.getAnimalNo(), animalWise);
		});

		map.forEach((animalNo, testCodeResult) -> {
			testCodeResult.forEach((testcode, results) -> {
				System.out.println(animalNo + "\t" + testcode + "\t" + results.size());
				boolean finalResult = false;
				for (VitrosData result : results) {
					;
					if (finalResult && result.isFinalize()) {
						result.setFinalize(false);
					} else if (result.isFinalize()) {
						finalResult = true;
					}
				}
				if (!finalResult) {
					results.get(0).setFinalize(true);
				}
			});
		});
	}

	@Override
	public List<VitrosData> vitrosRawDataListWithAnimalNo(String animalNo, Date from, Date to, String studySuffiex) {
		Criteria cri = getSession().createCriteria(VitrosData.class)
				.add(Restrictions.like("animalNo", studySuffiex + "%"));
		if (from != null)
			cri.add(Restrictions.between("testDate", from, to));
		if (animalNo != null)
			cri.add(Restrictions.eq("animalNo", animalNo));
		return cri.list();
	}

	@Override
	public List<StudyAnimal> studyAnimals(Long studyId) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", studyId)).list();
	}

	@Override
	public VistrosDataUpdateDto vitrosDataList(Long studyId, String animalNo, int columnIndex) {
		List<StudyTestCodes> tcs = studyTestCode(studyId, columnIndex, "VITROS", null);
		StudyTestCodes tc = tcs.get(0);
		List<VitrosData> list = vitrosDataListData(studyId, animalNo, tc.getTestCode().getTestCode());
		VistrosDataUpdateDto dto = new VistrosDataUpdateDto();
		dto.setStudyNumber(tc.getStudy().getStudyNo());
		dto.setTestName(tc.getTestCode().getTestCode());
		dto.setResult(list);
		return dto;
	}

	@Override
	public SysmexDataUpdateDto sysmexDataList(Long studyId, String animalNo, int columnIndexOrOrderNo, String observation, String stagostDate) throws ParseException {
		List<StudyTestCodes> tcs = studyTestCode(studyId, columnIndexOrOrderNo, "SYSMEX", observation);
		StudyTestCodes tc = tcs.get(0);
		List<SysmexTestCodeData> list = sysmexDataListData(studyId, animalNo, tcs, observation, stagostDate);
		List<SysmexTestCodeDataDto> result = new ArrayList<>();
		for(SysmexTestCodeData data :  list) {
			SysmexTestCodeDataDto ddto = new SysmexTestCodeDataDto();
			ddto.setFinalValue(data.isFinalValue());
			ddto.setId(data.getId());
			ddto.setValue(data.getValue());
			ddto.setDisplayRunTime(data.getDisplayRunTime());
			result.add(ddto);
		}
		SysmexDataUpdateDto dto = new SysmexDataUpdateDto();
		dto.setStudyNumber(tc.getStudy().getStudyNo());
		dto.setTestName(tc.getTestCode().getTestCode());
//		dto.setResult(list);
		dto.setResultDto(result);
		return dto;
	}

	@Override
	public List<SysmexTestCodeData> sysmexDataListData(Long studyId, String animalNo, List<StudyTestCodes> tcs, String observation, String startDate) throws ParseException {
		// TODO Auto-generated method stub
		List<Long> tcIds = new ArrayList<>();
		for(StudyTestCodes tc : tcs) {
			tcIds.add(tc.getId());
		}
		SimpleDateFormat sdf = new SimpleDateFormat();
		List<Long> sysDataIds = sysmexDataIds(studyId, animalNo, observation, startDate);
		Criteria cri = getSession().createCriteria(SysmexTestCodeData.class);
		if (sysDataIds.size() > 0) {
			cri.add(Restrictions.in("sysmexData.id", sysDataIds));
		}
		cri.add(Restrictions.in("studyTestCode.id", tcIds));
		List<SysmexTestCodeData> list = cri.list();
		list.forEach((obj) -> {
			if (obj.getRunTime() != null)
				obj.setDisplayRunTime(sdf.format(obj.getRunTime()));
		});
		return list;
	}

	private List<Long> sysmexDataIds(Long studyId, String animalNo, String observation, String startDate) throws ParseException {
		// TODO Auto-generated method stub
		List<Long> obs = new ArrayList<>();
		if(observation != null)
			obs = observationInturmentConfigurationDao.observationInturmentConfiguration(studyId, observation);
		
		Criteria cri = getSession().createCriteria(SysmexData.class).add(Restrictions.eq("study.id", studyId))
				.add(Restrictions.eq("animalNumber", animalNo)).setProjection(Projections.property("id"));
		if(obs.size() > 0) {
			cri.add(Restrictions.in("observationInturmentConfiguration.id", obs));
		}
		if (startDate != null && !startDate.trim().equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
			System.out.println(startDate.trim());
			String[] st = startDate.split("\\-");
			String s = st[0] + "-" + st[1] + "-" + st[2];
			startDate = s;
			System.out.println(startDate.trim());
			Date fromDate = sdf.parse(startDate.trim());
			Calendar c = Calendar.getInstance();
			c.setTime(fromDate);
			c.add(Calendar.DATE, 1);
			Date toDate = sdf.parse(sdf.format(c.getTime()));
			System.out.println(fromDate + "\t" + toDate);
			cri.add(Restrictions.between("startTime", fromDate, toDate));
		}
		return cri.list();
	}

	@Override
	public List<VitrosData> vitrosDataListData(Long studyId, String animalNo, String testName) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(VitrosData.class)
//				.add(Restrictions.eq("study.id", studyId))
				.add(Restrictions.eq("animalNo", animalNo)).add(Restrictions.eq("testName", testName)).list();
	}

	@Override
	public List<SysmexTestCodeData> sysmexDataListData(Long studyId, String animalNo, String testName, String observation, String stagostDate) throws ParseException {
		// TODO Auto-generated method stubd

		List<Long> sysDataIds = sysmexDataIds(studyId, animalNo, observation, stagostDate);
		Criteria cri = getSession().createCriteria(SysmexTestCodeData.class);
		if (sysDataIds.size() > 0) {
			cri.add(Restrictions.in("sysmexData.id", sysDataIds));
		}
		cri.add(Restrictions.eq("testCode", testName));
		return cri.list();
	}

	private List<StudyTestCodes> studyTestCode(Long studyId, int columnIndexOrOrderNo, String instumentName, String observation) {
		List<Long> obs = new ArrayList<>();
		if(observation != null)
			obs = observationInturmentConfigurationDao.observationInturmentConfiguration(studyId, observation);
		InstrumentIpAddress instrument = instumentIpAddress(instumentName);
		Criteria cri = getSession().createCriteria(StudyTestCodes.class)
				.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("instrument", instrument))
				.add(Restrictions.eq("orderNo", columnIndexOrOrderNo));
		if(obs.size() > 0)
			cri.add(Restrictions.in("observationInturmentConfiguration.id", obs));
				
		return cri.list();
	}

	@Override
	public void updateVitrosDataList(List<VitrosData> list) {
		// TODO Auto-generated method stub
		for (VitrosData sd : list)
			getSession().merge(sd);

	}

	@Override
	public void updateSysmexDataList(List<SysmexTestCodeData> list) {
		// TODO Auto-generated method stub
		for (SysmexTestCodeData sd : list)
			getSession().merge(sd);

	}

	@Override
	public List<VitrosData> onlineVitrosData() {
		// TODO Auto-generated method stub
		List<VitrosDataFromExe> list = getSession().createCriteria(VitrosDataFromExe.class)
				.add(Restrictions.eq("status", true)).list();
		List<VitrosData> result = new ArrayList<>();
		for (VitrosDataFromExe vdfe : list) {
			VitrosData vd = new VitrosData();
			vd.setAnimalNo(vdfe.getPatientId());
			vd.setTestDate(vdfe.getTestDate());
			vd.setMachineName(vdfe.getMachineName());
			vd.setTestName(vdfe.getTestName());
			vd.setResult(vdfe.getResult());
			vd.setStartDateTime(vdfe.getStartDateTime());
			vd.setEndDateTime(vdfe.getEndDateTime());
			vd.setVitrosDataFromExe(vdfe);
			result.add(vd);
		}
		return result;
	}

	@Override
	public void updateAsOnline(List<VitrosData> data) {
		for (VitrosData o : data) {
			o.setStudy(VistrosThread.study);
//			o.setOnlineData(true);
			getSession().merge(o);
			o.getVitrosDataFromExe().setStatus(false);
			getSession().merge(o.getVitrosDataFromExe());
		}
	}

	@Override
	public List<StagoData> studyStagoData(Long studyNumber, String startDate, boolean export, String sampleType)
			throws ParseException {
		SimpleDateFormat sdftime = new SimpleDateFormat(environment.getRequiredProperty("dateTimeSecondsFormat"));
		StudyMaster study = findByStudyId(studyNumber);
		Criteria cri = getSession().createCriteria(StagoData.class);
		if (studyNumber != null && !studyNumber.equals("") && !studyNumber.equals("-1") && study != null)
			cri.add(Restrictions.eq("study.id", study.getId()));
		if (!sampleType.equals("Both")) {
			cri.add(Restrictions.eq("sampleType", sampleType));
		}

		if (startDate != null && !startDate.trim().equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
			System.out.println(startDate.trim());
			String[] st = startDate.split("\\-");
			String s = st[0] + "-" + st[1] + "-" + st[2];
			startDate = s;
			System.out.println(startDate.trim());
			Date fromDate = sdf.parse(startDate.trim());
			Calendar c = Calendar.getInstance();
			c.setTime(fromDate);
			c.add(Calendar.DATE, 1);
			Date toDate = sdf.parse(sdf.format(c.getTime()));
			System.out.println(fromDate + "\t" + toDate);
			cri.add(Restrictions.between("receivedTime", fromDate, toDate));
		}
		List<StagoData> lists = cri.list();
		return lists;
	}

	@Override
	public boolean studyMetaDataConfigured(Long studyId) {
		// TODO Auto-generated method stub
		StudyMaster sm = findByStudyId(studyId);
		if (sm.getStatus().getStatusCode().equals(StatusMasterCodes.IN.toString()))
			return false;
		else
			return true;
	}

	@Override
	public List<Species> speciesList() {
		// TODO Auto-generated method stub
		return getSession().createCriteria(Species.class).list();
	}

	@Override
	public String saveSponsorMaster(SponsorMaster sponsorMaster) {
		String msg = "error";
		try {
			getSession().save(sponsorMaster);
			msg = "success";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}

	@Override
	public SponsorMaster getSponsorMasterUniqueCheckCode(String data) {
		SponsorMaster sm = null;
		sm = (SponsorMaster) getSession().createCriteria(SponsorMaster.class).add(Restrictions.eq("code", data))
				.uniqueResult();
		return sm;
	}

	@Override
	public Object sponsorList() {
		return getSession().createCriteria(SponsorMaster.class).list();
	}

	public List<String> insturmentResultSelectionDates(Long studyId, String insturment) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdFormat = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		List<Date> dates = null;
		if (insturment.equalsIgnoreCase("Sysmex")) {
			dates = getSession().createCriteria(SysmexData.class).add(Restrictions.eq("study.id", studyId))
					.setProjection(Projections.distinct(Projections.property("startTime"))).list();
		} else if (insturment.equalsIgnoreCase("Stago")) {
			dates = getSession().createCriteria(StagoData.class).add(Restrictions.eq("study.id", studyId))
					.setProjection(Projections.distinct(Projections.property("receivedTime"))).list();
		} else if (insturment.equalsIgnoreCase("VITROS")) {
			dates = getSession().createCriteria(VitrosData.class).add(Restrictions.eq("study.id", studyId))
					.setProjection(Projections.distinct(Projections.property("testDate"))).list();
		}
		Set<String> set = new HashSet<>();
		for (Date d : dates) {
			set.add(sdFormat.format(d));
		}
		return new ArrayList<>(set);
	}

	@Override
	public StudyAnimal studyAnimalsById(Long animalId) {
		// TODO Auto-generated method stub
		if(animalId != null)
			return (StudyAnimal) getSession().get(StudyAnimal.class, animalId);
		return null;
	}

	@Override
	public String updateAceeptMetdetaStatus(StudyMaster sm) {
		String msg = "error";
		try {
			getSession().merge(sm);
			msg = "success";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}

	@Override
	public List<StudyMaster> allNewActiveStudysOfStudyDirector(Long userId) {
		// TODO Auto-generated method stub
		StatusMaster st = statusDao.statusMaster("IN");

		Criteria cri = getSession().createCriteria(StudyMaster.class).add(Restrictions.eq("sdUser", userId))
				.add(Restrictions.ne("acceptStatus", "Accept"));
		if (st != null) {
			cri.add(Restrictions.eq("status", st));
		}
		return cri.list();
	}

	@Override
	public List<StudyMaster> allActiveStudysOfStudyDirector(Long userId) {
		Criteria cri = getSession().createCriteria(StudyMaster.class).add(Restrictions.eq("sdUser", userId))
				.add(Restrictions.eq("acceptStatus", "Accept"));
		return cri.list();
	}

	@Override
	public List<RoleMaster> allRoleMasters() {
		// TODO Auto-generated method stub
		return getSession().createCriteria(RoleMaster.class).list();
	}

	@Override
	public StaticData staticDataByCode(String code) {
		// TODO Auto-generated method stub
		return (StaticData) getSession().createCriteria(StaticData.class).add(Restrictions.eq("code", code))
				.uniqueResult();
	}

	@Override
	public void updateAnimal(StudyAnimal animal) {
		// TODO Auto-generated method stub
		getSession().merge(animal);
	}

	@Override
	public List<SubGroupInfo> allSubGroupInfo(Long studyId) {
		// TODO Auto-generated method stub
		List<SubGroupInfo> list = getSession().createCriteria(SubGroupInfo.class)
				.add(Restrictions.eq("study.id", studyId)).list();
		for (SubGroupInfo sgf : list) {
			List<SubGroupAnimalsInfo> info = getSession().createCriteria(SubGroupAnimalsInfo.class)
					.add(Restrictions.eq("subGroup.id", sgf.getId())).list();
			sgf.setAnimalInfo(info);
		}
		return list;
	}

	@Override
	public int maxAnimalPerminentNo(Long id) {
		int count = 1;
		try {
			count = (int) getSession().createCriteria(RandamizationDto.class).add(Restrictions.eq("study.id", id))
					.add(Restrictions.eq("activeStatus", true)).setProjection(Projections.max("maxAnimalNo"))
					.uniqueResult();
			return (count + 1);
		} catch (Exception e) {
			// TODO: handle exception
			return 1;
		}
	}

	@Override
	public List<StudyAnimal> studyAnimals(String gender, boolean randamized) {
		// TODO Auto-generated method stub
		Criteria cri = getSession().createCriteria(StudyAnimal.class);
		if (!gender.equals("gender")) {
			cri.add(Restrictions.eq("gender", gender));
		}
		if (randamized) {
			cri.add(Restrictions.isNotNull("subGropInfo"));
		}
		return cri.list();
	}

	@Override
	public List<StudyTestCodes> studyObservationInstumentTestCodes(
			ObservationInturmentConfiguration observationInturmentConfiguration, Long instrumentIpAddress,
			Long studyId) {
		return studyTestCodesDao.studyObservationInstumentTestCodes(observationInturmentConfiguration.getId(),
				instrumentIpAddress);
	}

	@Override
	public void sendStudyFroReivew(Long studyId, String username, String commentForReview) {
		StudyMaster study = getByKey(studyId);
		study.setStudyStatus("In-Review");
		study.setSentToReviewOn(new Date());
		study.setSentToReviewComment(commentForReview);
	}

	@Override
	public List<StudyMaster> allReivewActiveStudys() {
		List<StudyMaster> result = createEntityCriteria().add(Restrictions.or(Restrictions.eq("studyStatus", "In-Review"), Restrictions.eq("studyStatus", "Comments Required"))).list();
		return result;
	}


}
