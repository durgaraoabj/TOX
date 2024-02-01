package com.springmvc.dao.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfDescrpency;
import com.covide.crf.dto.CrfGroup;
import com.covide.crf.dto.CrfGroupElement;
import com.covide.crf.dto.CrfMapplingTableColumnMap;
import com.covide.crf.dto.CrfSection;
import com.covide.crf.dto.CrfSectionElement;
import com.covide.crf.dto.CrfSectionElementData;
import com.covide.crf.dto.CrfSectionElementValue;
import com.covide.crf.dto.CrfSectionElementWeightData;
import com.covide.dto.CrfDataEntryDto;
import com.covide.dto.CrfFormulaDto;
import com.covide.dto.ExpermentalDto;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.ExpermentalDesignDao;
import com.springmvc.dao.UserDao;
import com.springmvc.model.AccessionAnimalsDataEntryDetails;
import com.springmvc.model.AmendmentDetails;
import com.springmvc.model.AnimalCage;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.ClinicalCodes;
import com.springmvc.model.DepartmentMaster;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.ObservationReviewLevel;
import com.springmvc.model.ObservationRole;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAccessionCrfSectionElementData;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyCageAnimal;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTreatmentDataDates;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCountReviewLevel;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.SubjectDataEntryDetails;
import com.springmvc.model.SysmexAnimalCode;
import com.springmvc.util.AnimalObservationData;
import com.springmvc.util.ClinicalObservationRecordTimePoint;
import com.springmvc.util.ClinicalObservationRecordTimePointElements;
import com.springmvc.util.DailyClinicalObservation;
import com.springmvc.util.DetailedClinicalObservations;
import com.springmvc.util.DetailedClinicalObservationsAnimlas;
import com.springmvc.util.ExprementalData;
import com.springmvc.util.MotorActivityDataOfIndividualAnimal;
import com.springmvc.util.MotorActivityDataOfIndividualAnimalData;
import com.springmvc.util.NeurobehavioralObservationsofIndividual;
import com.springmvc.util.NeurobehavioralObservationsofIndividualAnimal;
import com.springmvc.util.ObservationData;
import com.springmvc.util.OphthalmologicalExamination;
import com.springmvc.util.OphthalmologicalExaminationAnimlas;
import com.springmvc.util.RecordforMortalityMorbidity;
import com.springmvc.util.SensoryReactivityOfIndividual;
import com.springmvc.util.SensoryReactivityOfIndividualData;

@Repository("expermentalDesignDao")
@PropertySource(value = { "classpath:application.properties" })
@SuppressWarnings("unchecked")
public class ExpermentalDesignDaoImpl extends AbstractDao<Long, GroupInfo> implements ExpermentalDesignDao {

	@Autowired
	private Environment environment;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ClinicalCodesDaoImple clinicalCodesDao;

	@Override
	public List<GroupInfo> studyGroupInfo(StudyMaster sm) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(GroupInfo.class).add(Restrictions.eq("study", sm)).list();
	}

	@Override
	public List<GroupInfo> studyGroupInfoWithSubGroup(StudyMaster sm) {
		// TODO Auto-generated method stub
		List<GroupInfo> list = getSession().createCriteria(GroupInfo.class).add(Restrictions.eq("study", sm)).list();
		for (GroupInfo gi : list) {
			gi.setSubGroupInfo(
					getSession().createCriteria(SubGroupAnimalsInfo.class).add(Restrictions.eq("group", gi)).list());
		}
		return list;
	}

	@Override
	public String saveExpermentalDesign(StudyMaster sm, List<SubGroupInfo> subGroups,
			List<SubGroupAnimalsInfo> animalInfo, List<SubGroupAnimalsInfoAll> animalInfoAll, StudyDesignStatus sds,
			ApplicationAuditDetails apad) {
		try {
			for (SubGroupInfo sg : subGroups)
				getSession().save(sg);
			for (SubGroupAnimalsInfo sg : animalInfo)
				getSession().save(sg);
			for (SubGroupAnimalsInfoAll sa : animalInfoAll) {
				getSession().save(sa);
			}
			getSession().save(sds);
			getSession().save(apad);
			getSession().merge(sm);
			return "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "fail";
		}

	}

	@Override
	public List<SubGroupInfo> studySubGroupInfo(StudyMaster sm) {
		return getSession().createCriteria(SubGroupAnimalsInfo.class).add(Restrictions.eq("study", sm)).list();
	}

	@Override
	public List<GroupInfo> studyGroupInfoWithChaild(StudyMaster sm) {
		List<GroupInfo> giList = getSession().createCriteria(GroupInfo.class).add(Restrictions.eq("study", sm)).list();
		for (GroupInfo gi : giList) {
			List<SubGroupInfo> sgifList = studySubGroupInfo(gi);
			for (SubGroupInfo sgi : sgifList) {
				List<SubGroupAnimalsInfo> animalInfo = subGroupAnimalsInfo(sgi);
				sgi.setAnimalInfo(animalInfo);
				if (checkcheckDataEntryEligibleWithSubGroup(sgi)) {
					if (sm.isRequiredSubGroup())
						sgi.setDataEntry(true);
					else
						gi.setDataEntry(true);
				}
			}
			gi.setSubGroupInfo(sgifList);
		}
		return giList;
	}

//	@Override
//	public List<GroupInfo> studyGroupInfoWithChaildReview(StudyMaster sm) {
//		List<GroupInfo> giList = getSession().createCriteria(GroupInfo.class).add(Restrictions.eq("study", sm)).list();
//		for (GroupInfo gi : giList) {
//			List<SubGroupInfo> sgifList = studySubGroupInfo(gi);
//			for (SubGroupInfo sgi : sgifList) {
//				List<SubGroupAnimalsInfo> animalInfo = subGroupAnimalsInfo(sgi);
//				sgi.setAnimalInfo(animalInfo);
//				if(checkcheckDataEntryEligibleWithSubGroup(sgi)) {
//					if(sm.isRequiredSubGroup())
//						sgi.setDataEntry(true);
//					else
//						gi.setDataEntry(true);
//				}
//			}
//			gi.setSubGroupInfo(sgifList);
//		}
//		return giList;
//	}

	public List<SubGroupAnimalsInfo> subGroupAnimalsInfo(SubGroupInfo sgi) {
		return getSession().createCriteria(SubGroupAnimalsInfo.class).add(Restrictions.eq("subGroup", sgi)).list();
	}

	public List<SubGroupInfo> studySubGroupInfo(GroupInfo gi) {
		return getSession().createCriteria(SubGroupInfo.class).add(Restrictions.eq("group", gi))
				.add(Restrictions.eq("status", "active")).list();
	}

	@Override
	public List<Crf> findAllCrfsOfsubGroup(StudyMaster sm, Long subGroupId) {
		List<Crf> stdCrfs = getSession().createCriteria(Crf.class).add(Restrictions.eq("std", sm))
				.add(Restrictions.eq("subGroupInfo.id", subGroupId)).add(Restrictions.eq("crfFrom", "Library")).list();
		return stdCrfs;
	}

	@Override
	public List<Crf> findAllActiveCrfsOfsubGroup(StudyMaster sm, Long subGroupId) {
		List<Crf> stdCrfs = getSession().createCriteria(Crf.class).add(Restrictions.eq("std", sm))
				.add(Restrictions.eq("subGroupInfo.id", subGroupId)).add(Restrictions.eq("active", true))
				.add(Restrictions.eq("crfFrom", "Library")).list();
		return stdCrfs;
	}

	@Override
	public SubGroupInfo subGroupInfoById(StudyMaster sm, Long sibGroupId) {
		// TODO Auto-generated method stub
		SubGroupInfo sg = (SubGroupInfo) getSession().get(SubGroupInfo.class, sibGroupId);
		Hibernate.initialize(sg.getGroup());
		List<SubGroupAnimalsInfo> animalInfo = subGroupAnimalsInfo(sg);
//		List<Crf> observations = findAllCrfsOfsubGroup(sm, sibGroupId);
//		for (SubGroupAnimalsInfo sgai : animalInfo) {
//			sgai.setObservation(observations);
//		}
		sg.setAnimalInfo(animalInfo);
		return sg;
	}

	@Override
	public Crf studyCrf(StudyMaster sm, Long subGroupIdd, Long stdSubGroupObservationCrfsId) {
		StdSubGroupObservationCrfs stdSubGroupObsCrfs = (StdSubGroupObservationCrfs) getSession()
				.get(StdSubGroupObservationCrfs.class, stdSubGroupObservationCrfsId);
		Crf crf = stdSubGroupObsCrfs.getCrf();
		Hibernate.initialize(crf.getSections());
//		Hibernate.initialize(crf.getSubGroupInfo());
//		Hibernate.initialize(crf.getSubGroupInfo().getGroup());
		Collections.sort(crf.getSections());
		for (CrfSection sec : crf.getSections()) {
			Hibernate.initialize(sec.getElement());
			if (sec.getElement() == null || sec.getElement().size() == 0) {
			} else {
				for (CrfSectionElement ele : sec.getElement()) {
					Hibernate.initialize(ele.getElementValues());
				}
			}
			Hibernate.initialize(sec.getGroup());
			CrfGroup group = sec.getGroup();
			if (group != null) {
				Hibernate.initialize(group.getElement());
				if (group.getElement() == null || group.getElement().size() == 0) {
				} else {
					for (CrfGroupElement ele : group.getElement()) {
						Hibernate.initialize(ele.getElementValues());
					}
				}
			}
		}
		return crf;
	}

	@Override
	public SubGroupAnimalsInfo subGroupAnimalsInfo(Long subGroupInfoId) {
		SubGroupAnimalsInfo sgai = (SubGroupAnimalsInfo) getSession().get(SubGroupAnimalsInfo.class, subGroupInfoId);
		Hibernate.initialize(sgai.getSubGroup());
		Hibernate.initialize(sgai.getGroup());
		Hibernate.initialize(sgai.getStudy());
		return sgai;
	}

	@Override
	public SubGroupAnimalsInfoAll subGroupAnimalsInfoAll(Long subGroupInfoId) {
		SubGroupAnimalsInfoAll sgai = (SubGroupAnimalsInfoAll) getSession().get(SubGroupAnimalsInfoAll.class,
				subGroupInfoId);

		Hibernate.initialize(sgai.getSubGroup());
		Hibernate.initialize(sgai.getSubGroup().getGroup());
		Hibernate.initialize(sgai.getSubGroupAnimalsInfo());
		Hibernate.initialize(sgai.getSubGroup().getStudy());
		return sgai;
	}

	@Override
	public int subGroupAnimalsInfoCrfDataCount(SubGroupAnimalsInfoAll animalInfo, StdSubGroupObservationCrfs crf) {
		int max = 0;
		try {
			max = (int) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
					.add(Restrictions.eq("subGroupAnimalsInfoAll", animalInfo)).add(Restrictions.eq("crf", crf))
					.setProjection(Projections.max("dataEntryCount")).uniqueResult();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ++max;
	}

	@Override
	public int subGroupAnimalsInfoCrfDataCountForDuplicate(SubGroupAnimalsInfoAll animalInfo,
			StdSubGroupObservationCrfs crf, Long sgoupId) {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getProperty("dateFormat"));
		String date = sdf.format(new Date());
		Long max = 0l;
		try {
			max = (Long) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
					.add(Restrictions.eq("subGroupAnimalsInfoAll", animalInfo)).add(Restrictions.eq("crf", crf))
					.add(Restrictions.eq("subGroup.id", sgoupId)).add(Restrictions.eq("entryDate", date))
					.setProjection(Projections.rowCount()).uniqueResult();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return max.intValue();
	}

	@Override
	public SubGroupAnimalsInfoCrfDataCount saveSubGroupAnimalsInfoCrfDataCount(SubGroupAnimalsInfoAll animalInfo,
			StdSubGroupObservationCrfs crf, String username, String deviationMessage, String frequntlyMessage) {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getProperty("dateFormat"));
		String date = sdf.format(new Date());

		SubGroupAnimalsInfoCrfDataCount sgaicdc = new SubGroupAnimalsInfoCrfDataCount();
		sgaicdc.setDeviationMessage(deviationMessage);
		sgaicdc.setFrequntlyMessage(frequntlyMessage);
		sgaicdc.setCreatedBy(username);
		sgaicdc.setGroup(animalInfo.getSubGroup().getGroup());
		sgaicdc.setSubGroup(animalInfo.getSubGroup());
		sgaicdc.setSubGroupAnimalsInfo(animalInfo.getSubGroupAnimalsInfo());
		sgaicdc.setStudyid(animalInfo.getSubGroup().getStudy().getId());

		sgaicdc.setSubGroupAnimalsInfoAll(animalInfo);
		sgaicdc.setCrf(crf);
		sgaicdc.setEntryDate(date);
		sgaicdc.setDataEntryCount(subGroupAnimalsInfoCrfDataCount(animalInfo, crf));

		Date studyDate = crf.getStudy().getStartDate();
		Date sysDate = new Date();
		Long noOfDays = null;
		try {
			Date date1 = sdf.parse(sdf.format(studyDate));
			Date date2 = sdf.parse(sdf.format(sysDate));
			long diff = date2.getTime() - date1.getTime();
			noOfDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			noOfDays++;
		} catch (Exception e) {
			e.printStackTrace();
			noOfDays = 1l;
		}
		if (crf.getDayType().equals("week")) {
			if (noOfDays % 7 > 0) {
				noOfDays = (noOfDays / 7) + 1;
			} else
				noOfDays = noOfDays / 7;
		}
		sgaicdc.setDayWeekCount(noOfDays.intValue());

		getSession().save(sgaicdc);

		return sgaicdc;
	}

	@Override
	public CrfDescrpency studyCrfDescrpencySec(StdSubGroupObservationCrfs animalData, Crf crf,
			SubGroupAnimalsInfo animalInfo, CrfSectionElementData data) {
		CrfDescrpency desc = (CrfDescrpency) getSession().createCriteria(CrfDescrpency.class)
				.add(Restrictions.eq("crf", crf)).add(Restrictions.eq("stdSubGroupObservationCrfs", animalData))
				.add(Restrictions.eq("secEleData", data)).add(Restrictions.eq("status", "open")).uniqueResult();
		return desc;
	}

	@Override
	public String studyCrfSectionElementData(SubGroupAnimalsInfoCrfDataCount animalData, CrfSectionElement secEle) {
		CrfSectionElementData data = (CrfSectionElementData) getSession().createCriteria(CrfSectionElementData.class)
				.add(Restrictions.eq("dataCount", animalData)).add(Restrictions.eq("element", secEle)).uniqueResult();
		if (data != null)
			return data.getValue();
		else
			return null;
	}

	@Override
	public ObservationData allObservationData1(StudyMaster sm) {
//		int totoal = 0;
//		List<GroupInfo> groups = studyGroupInfo(sm);
//		for(GroupInfo g : groups) {
//			List<SubGroupInfo> subGroupInfos = studySubGroupInfo(g);
//			for(SubGroupInfo subg : subGroupInfos) {
//				List<SubGroupAnimalsInfoAll> subGroupAnimalsInfoAll = subGroupAnimalsInfoAll(subg);
//				List<StdSubGroupObservationCrfs> oblservations = findAllStdSubGroupObservationCrfs(sm, subg.getId());
//				for(SubGroupAnimalsInfoAll ani : subGroupAnimalsInfoAll) {
//					for(StdSubGroupObservationCrfs scrf : oblservations) {
//						Crf crf = studyCrf(sm, subg.getId(), scrf.getId());
//					}
//					
//				}
//			}
//		}
//		
//
//		SubGroupAnimalsInfo subGroupAnimalsInfo = subGroupAnimalsInfoAll.getSubGroupAnimalsInfo();
//		
//		StdSubGroupObservationCrfs stdSubGroupObservationCrfs = (StdSubGroupObservationCrfs) getSession()
//				.get(StdSubGroupObservationCrfs.class, stdSubGroupObservationCrfsId);
//		List<SubGroupAnimalsInfoCrfDataCount> entiress = getSession()
//				.createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
//				.add(Restrictions.eq("crf", stdSubGroupObservationCrfs))
//				.add(Restrictions.eq("subGroupAnimalsInfoAll", subGroupAnimalsInfoAll)).list();
//
//		Map<Long, CrfSectionElement> elements = new HashMap<>();
//		for (CrfSection sec : crf.getSections()) {
//			for (CrfSectionElement ele : sec.getElement()) {
//				if (!ele.getType().equals("non")) {
//					elements.put(ele.getId(), ele);
//				}
//			}
//		}
//		ob.setElements(elements);
//
////		List<SubGroupAnimalsInfoCrfDataCount> dcs = subGroupAnimalsInfoCrfDataCount(subGroupAnimalsInfoAll, stdSubGroupObservationCrfsId);
//
//		List<AnimalObservationData> animalData = new ArrayList<>();
//		for (SubGroupAnimalsInfoCrfDataCount dc : entiress) {
//			List<SubGroupAnimalsInfoCrfDataCountReviewLevel> li = getSession()
//					.createCriteria(SubGroupAnimalsInfoCrfDataCountReviewLevel.class)
//					.add(Restrictions.eq("dataCount", dc)).add(Restrictions.eq("user", user)).list();
//			SubGroupAnimalsInfoCrfDataCountReviewLevel l = null;
//			if (li.size() > 0)
//				l = li.get(li.size() - 1);
////					l = (SubGroupAnimalsInfoCrfDataCountReviewLevel) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCountReviewLevel.class)
////					.add(Restrictions.eq("dataCount", dc))
////					.add(Restrictions.eq("user", user)).uniqueResult();
//
//			List<CrfSectionElementData> crfData = getSession().createCriteria(CrfSectionElementData.class)
//					.add(Restrictions.eq("dataCount", dc)).list();
//			AnimalObservationData data = new AnimalObservationData();
//			data.setId(dc.getId());
//			if (l != null)
//				data.setReviewed(true);
//			data.setDeviationMessage(dc.getDeviationMessage());
//			data.setFrequntlyMessage(dc.getFrequntlyMessage());
//			data.setUserName(dc.getCreatedBy());
//			data.setDate(dc.getCreatedOn());
//			data.setCount(dc.getDataEntryCount());
//			Map<Long, CrfSectionElementData> elementData = new HashMap<>();
//			for (CrfSectionElementData crfdata : crfData) {
//				if (!crfdata.getElement().getType().equals("non")) {
//					elementData.put(crfdata.getElement().getId(), crfdata);
//					try {
//						Long red = (Long) getSession().createCriteria(CrfDescrpency.class)
//								.add(Restrictions.eq("secEleData", crfdata))
//								.add(Restrictions.or(Restrictions.eq("status", "open"),
//										Restrictions.eq("status", "onHold")))
//								.setProjection(Projections.rowCount()).uniqueResult();
//						crfdata.setDiscripency(red.intValue());
//						if (red > 0)
//							System.out.println(crfdata.getId() + "--------" + red);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					try {
//						Long red = (Long) getSession().createCriteria(CrfDescrpency.class)
//								.add(Restrictions.eq("secEleData", crfdata)).setProjection(Projections.rowCount())
//								.uniqueResult();
//						crfdata.setDiscripencyClose(red.intValue());
//						if (red > 0)
//							System.out.println(crfdata.getId() + "--------" + red);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//			try {
//				Long red = (Long) getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("dataCount", dc))
//						.add(Restrictions.eq("status", "closed")).setProjection(Projections.rowCount()).uniqueResult();
//				Long all = (Long) getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("dataCount", dc))
//						.setProjection(Projections.rowCount()).uniqueResult();
//				if (all == red) {
//					data.setDiscrepencyClosed(0);
//				} else {
//					Long diff = all - red;
//					data.setDiscrepencyClosed(diff.intValue());
//					totoal += diff.intValue();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			data.setElementData(elementData);
//			data.setDayOrWeek(dc.getDayWeekCount());
//
//			animalData.add(data);
//		}
//		ob.setTotoalDescNotDone(totoal);
//		ob.setAnimalData(animalData);
//		return ob;
		return null;
	}

	@Override
	public ObservationData observationData(Long subGroupInfoId, Long stdSubGroupObservationCrfsId, LoginUsers user) {
		int totoal = 0;
		SubGroupAnimalsInfoAll subGroupAnimalsInfoAll = subGroupAnimalsInfoAll(subGroupInfoId);

		SubGroupAnimalsInfo subGroupAnimalsInfo = subGroupAnimalsInfoAll.getSubGroupAnimalsInfo();
		Crf crf = studyCrf(subGroupAnimalsInfo.getStudy(), subGroupAnimalsInfo.getSubGroup().getId(),
				stdSubGroupObservationCrfsId);
		StdSubGroupObservationCrfs stdSubGroupObservationCrfs = (StdSubGroupObservationCrfs) getSession()
				.get(StdSubGroupObservationCrfs.class, stdSubGroupObservationCrfsId);
		List<SubGroupAnimalsInfoCrfDataCount> entiress = getSession()
				.createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
				.add(Restrictions.eq("crf", stdSubGroupObservationCrfs))
				.add(Restrictions.eq("subGroupAnimalsInfoAll", subGroupAnimalsInfoAll)).list();

		ObservationData ob = new ObservationData();
		int i = 0;
		for (SubGroupAnimalsInfoCrfDataCount sgadc : entiress) {
			ObservationReviewLevel orl = null;
			try {
				orl = (ObservationReviewLevel) getSession().createCriteria(ObservationReviewLevel.class)
						.add(Restrictions.eq("subGroupObservationCrf", stdSubGroupObservationCrfs))
						.add(Restrictions.eq("subGroupAnimalsInfoCrfDataCount", sgadc))
						.add(Restrictions.eq("user", user)).uniqueResult();
				if (orl != null && !orl.isReviewFlag()) {
					sgadc.setUserReview(true);
					i++;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		if (i == entiress.size()) {
			ob.setMessage("Review Completed for the user : '" + user.getUsername() + "'.");
			ob.setAllowDataEntry(false);
		}

//		if(!stdSubGroupObservationCrfs.isReviewStatus()) {
//			ReviewLevel rl =  (ReviewLevel) getSession().createCriteria(ReviewLevel.class)
//				.add(Restrictions.eq("status", "Active")).uniqueResult();
//			int max = 0;
//			try {
//				max = (int) getSession().createCriteria(ObservationReviewLevel.class)
//					.add(Restrictions.eq("subGroupObservationCrf", stdSubGroupObservationCrfs))
//					.add(Restrictions.eq("subGroupAnimalsInfoAll", subGroupAnimalsInfoAll))
//					.setProjection(Projections.max("userLevel")).uniqueResult();
//			}catch (Exception e) {
//				// TODO: handle exception
//			}
//			if(max < rl.getObservationApprovelLevel()) {
//				if(++max == user.getReviewLevel() || user.getReviewLevel() == 100) {
//					ob.setAllowDataEntry(true);
//					stdSubGroupObservationCrfs.setNeedToReview(rl.getObservationApprovelLevel() - max);
//				}
//			}
//		}else {
//			ob.setMessage("Review Completed.");
//			ob.setAllowDataEntry(false);
//		}

		ob.setStudy(subGroupAnimalsInfo.getStudy());
		ob.setGroup(subGroupAnimalsInfo.getGroup());
		ob.setSubGroup(subGroupAnimalsInfo.getSubGroup());
		ob.setAnimal(subGroupAnimalsInfo);
		ob.setAnimalAll(subGroupAnimalsInfoAll);
		ob.setCrfOrObservation(crf);
		Map<Long, CrfSectionElement> elements = new HashMap<>();
		for (CrfSection sec : crf.getSections()) {
			for (CrfSectionElement ele : sec.getElement()) {
				if (!ele.getType().equals("non")) {
					elements.put(ele.getId(), ele);
				}
			}
		}
		ob.setElements(elements);

//		List<SubGroupAnimalsInfoCrfDataCount> dcs = subGroupAnimalsInfoCrfDataCount(subGroupAnimalsInfoAll, stdSubGroupObservationCrfsId);

		List<AnimalObservationData> animalData = new ArrayList<>();
		for (SubGroupAnimalsInfoCrfDataCount dc : entiress) {
			List<SubGroupAnimalsInfoCrfDataCountReviewLevel> li = getSession()
					.createCriteria(SubGroupAnimalsInfoCrfDataCountReviewLevel.class)
					.add(Restrictions.eq("dataCount", dc)).add(Restrictions.eq("user", user)).list();
			SubGroupAnimalsInfoCrfDataCountReviewLevel l = null;
			if (li.size() > 0)
				l = li.get(li.size() - 1);
//					l = (SubGroupAnimalsInfoCrfDataCountReviewLevel) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCountReviewLevel.class)
//					.add(Restrictions.eq("dataCount", dc))
//					.add(Restrictions.eq("user", user)).uniqueResult();

			List<CrfSectionElementData> crfData = getSession().createCriteria(CrfSectionElementData.class)
					.add(Restrictions.eq("dataCount", dc)).list();
			AnimalObservationData data = new AnimalObservationData();
			data.setId(dc.getId());
			if (l != null)
				data.setReviewed(true);
			data.setDeviationMessage(dc.getDeviationMessage());
			data.setFrequntlyMessage(dc.getFrequntlyMessage());
			data.setUserName(dc.getCreatedBy());
			data.setDate(dc.getCreatedOn());
			data.setCount(dc.getDataEntryCount());
			Map<Long, CrfSectionElementData> elementData = new HashMap<>();
			for (CrfSectionElementData crfdata : crfData) {
				if (!crfdata.getElement().getType().equals("non")) {
					elementData.put(crfdata.getElement().getId(), crfdata);
					try {
						Long red = (Long) getSession().createCriteria(CrfDescrpency.class)
								.add(Restrictions.eq("secEleData", crfdata))
								.add(Restrictions.or(Restrictions.eq("status", "open"),
										Restrictions.eq("status", "onHold")))
								.setProjection(Projections.rowCount()).uniqueResult();
						crfdata.setDiscripency(red.intValue());
						if (red > 0)
							System.out.println(crfdata.getId() + "--------" + red);
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Long red = (Long) getSession().createCriteria(CrfDescrpency.class)
								.add(Restrictions.eq("secEleData", crfdata)).setProjection(Projections.rowCount())
								.uniqueResult();
						crfdata.setDiscripencyClose(red.intValue());
						if (red > 0)
							System.out.println(crfdata.getId() + "--------" + red);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			try {
				Long red = (Long) getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("dataCount", dc))
						.add(Restrictions.eq("status", "closed")).setProjection(Projections.rowCount()).uniqueResult();
				Long all = (Long) getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("dataCount", dc))
						.setProjection(Projections.rowCount()).uniqueResult();
				if (all == red) {
					data.setDiscrepencyClosed(0);
				} else {
					Long diff = all - red;
					data.setDiscrepencyClosed(diff.intValue());
					totoal += diff.intValue();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			data.setElementData(elementData);
			data.setDayOrWeek(dc.getDayWeekCount());

			animalData.add(data);
		}
		ob.setTotoalDescNotDone(totoal);
		ob.setAnimalData(animalData);
		return ob;
	}

	private List<SubGroupAnimalsInfoCrfDataCount> subGroupAnimalsInfoCrfDataCount(
			SubGroupAnimalsInfoAll subGroupAnimalsInfo, Long stdSubGroupObservationCrfsId) {
		List<SubGroupAnimalsInfoCrfDataCount> dcs = getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
				.add(Restrictions.eq("subGroupAnimalsInfoAll", subGroupAnimalsInfo))
				.add(Restrictions.eq("crf.id", stdSubGroupObservationCrfsId)).list();
		return dcs;
	}

	@Override
	public SubGroupInfo subGroupInfoAllById(StudyMaster sm, Long gorupId, Long sibGroupId) {
		// TODO Auto-generated method stub
		SubGroupInfo sg = (SubGroupInfo) getSession().get(SubGroupInfo.class, sibGroupId);
		Hibernate.initialize(sg.getGroup());
		List<SubGroupAnimalsInfoAll> animalInfo = subGroupAnimalsInfoAll(sg);
		List<StdSubGroupObservationCrfs> observations = findAllActiveStdSubGroupObservationCrfs(sm, sibGroupId);
		for (SubGroupAnimalsInfoAll sgai : animalInfo) {
			sgai.setObservation(observations);
		}
		sg.setAnimalInfoAll(animalInfo);
		return sg;
	}

	@Override
	public SubGroupInfo subGroupInfoAllByIdReview(StudyMaster sm, Long sibGroupId) {
		// TODO Auto-generated method stub
		SubGroupInfo sg = (SubGroupInfo) getSession().get(SubGroupInfo.class, sibGroupId);
		Hibernate.initialize(sg.getGroup());
		List<SubGroupAnimalsInfoAll> animalInfo = subGroupAnimalsInfoAllReview(sg);
		List<SubGroupAnimalsInfoAll> animalInfo1 = findAllSubjectsStdSubGroupObservationCrfs(sg, animalInfo, sm);
//		List<StdSubGroupObservationCrfs> observations = findAllActiveStdSubGroupObservationCrfs(sm, sibGroupId);
//		for(SubGroupAnimalsInfoAll sgai : animalInfo) {
//			//24
//			sgai.setObservation(observations);
//		}
		sg.setAnimalInfoAll(animalInfo1);
		return sg;

	}

	private List<SubGroupAnimalsInfoAll> findAllSubjectsStdSubGroupObservationCrfs(SubGroupInfo sg,
			List<SubGroupAnimalsInfoAll> animalInfo, StudyMaster sm) {
		List<SubGroupAnimalsInfoAll> animals = new ArrayList<>();
		for (SubGroupAnimalsInfoAll ani : animalInfo) {
			List<StdSubGroupObservationCrfs> stdCrfs = getSession().createCriteria(StdSubGroupObservationCrfs.class)
					.add(Restrictions.eq("study", sm))
					.add(Restrictions.eq("subGroupInfo.id", ani.getSubGroup().getId()))
					.add(Restrictions.eq("active", true)).list();
			List<StdSubGroupObservationCrfs> stdCrfs2 = new ArrayList<>();
			for (StdSubGroupObservationCrfs ssoc : stdCrfs) {
				StdSubGroupObservationCrfs ssoc2 = new StdSubGroupObservationCrfs();
				ssoc2.setId(ssoc.getId());
				ssoc2.setObservationName(ssoc.getObservationName());
				try {
					List<SubGroupAnimalsInfoCrfDataCount> c = getSession()
							.createCriteria(SubGroupAnimalsInfoCrfDataCount.class).add(Restrictions.eq("crf", ssoc))
							.add(Restrictions.eq("subGroupAnimalsInfoAll", ani))
							.add(Restrictions.eq("crfStatus", "IN REVIEW")).list();
					if (c.size() == 0) {
						c = getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
								.add(Restrictions.eq("crf", ssoc)).add(Restrictions.eq("subGroupAnimalsInfoAll", ani))
								.add(Restrictions.eq("crfStatus", "REVIEWED")).list();
						if (c.size() > 0) {
							ssoc2.setNeedToReview(-1);
							ssoc2.setNeedTore("-1");
//							System.out.println(ssoc.getNeedToReview());
							ssoc2.setDataCrfs(c);
						}
					} else {
						ssoc2.setNeedToReview(c.size());
						ssoc2.setDataCrfs(c);
					}
					stdCrfs2.add(ssoc2);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				System.out.println(ssoc.getNeedToReview());
			}
			for (StdSubGroupObservationCrfs ssoc : stdCrfs2) {
				System.out.println(ssoc.getId() + " - " + ssoc.getNeedToReview() + "  " + ssoc.getNeedTore());
			}
			ani.setObservation(stdCrfs2);
			animals.add(ani);
		}
		for (SubGroupAnimalsInfoAll ani : animals) {
			List<StdSubGroupObservationCrfs> stdCrfs2 = ani.getObservation();
			for (StdSubGroupObservationCrfs ssoc : stdCrfs2) {
				System.out.println(ssoc.getId() + " - " + ssoc.getNeedToReview() + "  " + ssoc.getNeedTore());
			}
		}
		return animals;
	}

	private List<StdSubGroupObservationCrfs> findAllStdSubGroupObservationCrfs(StudyMaster sm, Long sibGroupId) {
		List<StdSubGroupObservationCrfs> stdCrfs = getSession().createCriteria(StdSubGroupObservationCrfs.class)
				.add(Restrictions.eq("study", sm)).add(Restrictions.eq("subGroupInfo.id", sibGroupId))
				.add(Restrictions.eq("active", true)).list();
		return stdCrfs;
	}

	private List<StdSubGroupObservationCrfs> findAllActiveStdSubGroupObservationCrfs(StudyMaster sm, Long sibGroupId) {

		List<StdSubGroupObservationCrfs> stdCrfs = getSession().createCriteria(StdSubGroupObservationCrfs.class)
				.add(Restrictions.eq("study", sm)).add(Restrictions.eq("subGroupInfo.id", sibGroupId))
				.add(Restrictions.eq("active", true)).list();
		for (StdSubGroupObservationCrfs s : stdCrfs) {
			try {
				List<SubGroupAnimalsInfoCrfDataCount> c = getSession()
						.createCriteria(SubGroupAnimalsInfoCrfDataCount.class).add(Restrictions.eq("crf", s))
						.add(Restrictions.eq("crfStatus", "IN REVIEW")).list();
				if (c.size() == 0) {
//					c = (Long) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
//							.add(Restrictions.eq("crf", s))
//							.add(Restrictions.eq("crfStatus", "REVIEWED")).setProjection(Projections.rowCount()).uniqueResult();
//					if(c > 0) {
//						s.setReviewStatus("REVIEWED");
//						s.setNoOfObservationsNeedToReview(-1);
//					}
				} else {
					s.setNeedToReview(c.size());
					s.setDataCrfs(c);

//					s.setNoOfObservationsNeedToReview(c.intValue());
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

		return stdCrfs;
	}

	public List<SubGroupAnimalsInfoAll> subGroupAnimalsInfoAll(SubGroupInfo sgi) {
		return getSession().createCriteria(SubGroupAnimalsInfoAll.class).add(Restrictions.eq("subGroup", sgi)).list();
	}

	public List<SubGroupAnimalsInfoAll> subGroupAnimalsInfoAllReview(SubGroupInfo sgi) {
		List<SubGroupAnimalsInfoAll> list = getSession().createCriteria(SubGroupAnimalsInfoAll.class)
				.add(Restrictions.eq("subGroup", sgi)).list();
		for (SubGroupAnimalsInfoAll s : list) {
			try {
				Long c = (Long) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
						.add(Restrictions.eq("subGroupAnimalsInfoAll", s))
						.add(Restrictions.eq("crfStatus", "IN REVIEW")).setProjection(Projections.rowCount())
						.uniqueResult();
				if (c == 0) {
					c = (Long) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
							.add(Restrictions.eq("subGroupAnimalsInfoAll", s))
							.add(Restrictions.eq("crfStatus", "REVIEWED")).setProjection(Projections.rowCount())
							.uniqueResult();
					if (c > 0) {
						s.setReviewStatus("REVIEWED");
						s.setNoOfObservationsNeedToReview(-1);
					}
				} else
					s.setNoOfObservationsNeedToReview(c.intValue());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public Map<String, String> weightWithNameValue(Long crfid, Long subGroupAnimalsInfoAllId, String userName) {
		Map<String, String> result = new HashMap<>();
		List<Long> sectionIds = getSession().createCriteria(CrfSection.class).add(Restrictions.eq("crf.id", crfid))
				.setProjection(Projections.property("id")).list();
		if (sectionIds.size() > 0) {
			List<Long> sectionEleIds = getSession().createCriteria(CrfSectionElement.class)
					.add(Restrictions.in("section.id", sectionIds)).setProjection(Projections.property("id")).list();
			if (sectionEleIds.size() > 0) {
				List<CrfSectionElementWeightData> values = getSession()
						.createCriteria(CrfSectionElementWeightData.class)
						.add(Restrictions.eq("animalInfoAll.id", subGroupAnimalsInfoAllId))
						.add(Restrictions.in("sectionElement.id", sectionEleIds))
						.add(Restrictions.eq("userName", userName)).add(Restrictions.eq("activeStatus", true))
						.addOrder(Order.asc("id")).list();
				for (CrfSectionElementWeightData sw : values) {
					result.put(sw.getSectionElement().getId() + "_" + sw.getSectionElement().getName(), sw.getWeight());
				}
			}

		}
		return result;
	}

	@Override
	public List<ExprementalData> subGroupAnimalsInfoWithStudy(StudyMaster sm) {
		int max = 0;
		Date d = sm.getStartDate();
		Date studyDate = sm.getStartDate();
//		Date sysDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		Calendar cal = Calendar.getInstance();

		List<SubGroupAnimalsInfo> list = getSession().createCriteria(SubGroupAnimalsInfo.class)
				.add(Restrictions.eq("study", sm)).list();
		List<ExprementalData> data = new ArrayList<>();
		for (SubGroupAnimalsInfo s : list) {
			List<StdSubGroupObservationCrfs> obs = getSession().createCriteria(StdSubGroupObservationCrfs.class)
					.add(Restrictions.eq("subGroupInfo", s.getSubGroup())).add(Restrictions.eq("active", true)).list();
			for (StdSubGroupObservationCrfs ob : obs) {
				ExprementalData ed = new ExprementalData();
				ed.setGroupId(s.getGroup().getId());
				ed.setGroupName(s.getGroup().getGroupName());
				ed.setSubgroupId(s.getSubGroup().getId());
				ed.setSubGroupName(s.getSubGroup().getName());
				ed.setGender(s.getGender());
				ed.setFrom(s.getFormId());
				ed.setTo(s.getToId());
				ed.setObserVationId(ob.getId());
				ed.setObserVationName(ob.getObservationName());
				ed.setDayOrWeek(ob.getDayType());

				String[] ds = ob.getDays().split("\\,");
				List<String> listDates = new ArrayList<>();
				for (int i = 0; i < ds.length; i++) {
					String dcount = ds[i];
					if (ob.getDayType().equals("day")) {
						boolean dayWise = true;
						// CHECKING DATE LIKE 2-3 OR 2-8. IF ANY EXCEPTION WE WONT CONSIDER THIS DAYS
						String[] days = null;
						int start = 0;
						int end = 0;
						if (dcount.contains("-")) {
							try {
								days = dcount.split("\\-");
								start = Integer.parseInt(days[0].trim());
								end = Integer.parseInt(days[1].trim());
							} catch (Exception e) {
								// TODO: handle exception
								dayWise = false;
							}
						}

						// ABOVE CONFITION SUCESS WE ARE GOINT TO PROCESS DAYS FROM-TO
						if (dcount.contains("-") && dayWise) {
							while (start <= end) {
								cal.setTime(d);
								cal.add(Calendar.DAY_OF_MONTH, start);
								listDates.add(sdf.format(cal.getTime()));
								start++;
							}
						} else {
							cal.setTime(d);
							cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(dcount));
							listDates.add(sdf.format(cal.getTime()));

						}
					} else if (ob.getDayType().equals("week")) {
//						dcount   2-6, 2 5 
//						Date studyDate = sm.getStartDate();
						boolean weekWise = true;
						if (dcount.contains("-")) {
							try {
								String[] days = dcount.split("\\-");
								int start = Integer.parseInt(days[0].trim());
								int end = Integer.parseInt(days[1].trim());
							} catch (Exception e) {
								// TODO: handle exception
								weekWise = false;
							}
						}
						List<String> tempDates = new ArrayList<>();
						if (dcount.contains("-") && weekWise) {
							String[] days = dcount.split("\\-");
							int start = Integer.parseInt(days[0].trim());
							int end = Integer.parseInt(days[1].trim());
							while (start <= end) {
								int day = start;
								for (int c = (day - 1) * 7; c < day * 7; c++) {
									cal.setTime(studyDate);
									cal.add(Calendar.DAY_OF_MONTH, c);
									tempDates.add(sdf.format(cal.getTime()));
								}
								start++;
							}
						} else {
							int days = Integer.parseInt(dcount);
							if (days > -1) {
								if (days == 0)
									days = 1;
								for (int c = (days - 1) * 7; c < days * 7; c++) {
									cal.setTime(studyDate);
									cal.add(Calendar.DAY_OF_MONTH, c);
									tempDates.add(sdf.format(cal.getTime()));
								}
							} else {
								days = -(days);
								int c = days * 7;
								for (int j = 1; j <= 7; j++) {
									System.out.println(c);
									cal.setTime(studyDate);
									cal.add(Calendar.DAY_OF_MONTH, -(c));
									tempDates.add(sdf.format(cal.getTime()));
									c--;
									System.out.println(listDates);
								}
							}
						}
						if (tempDates.size() > 1) {
							String datef = tempDates.get(0) + " to " + tempDates.get(tempDates.size() - 1);
							listDates.add(datef);
						} else
							listDates.addAll(tempDates);
					}

				}
				System.out.println(listDates);
				ed.setDays(listDates);
				if (max < listDates.size())
					max = listDates.size();
				ed.setMax(max);
				data.add(ed);
			}
		}
		return data;
	}

	@Override
	public Map<String, List<StdSubGroupObservationCrfs>> subGroupAnimalsInfoWithStudyCalender(StudyMaster sm) {
		int max = 0;
		Date d = sm.getAcclimatizationStarDate();
		Date studyDate = sm.getAcclimatizationStarDate();

		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		Calendar cal = Calendar.getInstance();

		List<SubGroupAnimalsInfo> list = getSession().createCriteria(SubGroupAnimalsInfo.class)
				.add(Restrictions.eq("study", sm)).list();
		Map<String, List<StdSubGroupObservationCrfs>> map = new HashMap<>();
		for (SubGroupAnimalsInfo s : list) {
			List<SubGroupAnimalsInfoAll> sall = getSession().createCriteria(SubGroupAnimalsInfoAll.class)
					.add(Restrictions.eq("subGroupAnimalsInfo", s)).list();
//			for(SubGroupAnimalsInfoAll sa : sall) {
			List<StdSubGroupObservationCrfs> obs = getSession().createCriteria(StdSubGroupObservationCrfs.class)
					.add(Restrictions.eq("subGroupInfo.id", s.getSubGroup().getId()))
					.add(Restrictions.eq("active", true)).list();
			for (StdSubGroupObservationCrfs ob : obs) {
				Hibernate.initialize(ob.getCrf());
				Hibernate.initialize(ob.getStudy());
				Hibernate.initialize(ob.getSubGroupInfo());
				System.out.println(ob.getSubGroupInfo().getId());
				Hibernate.initialize(ob.getSubGroupInfo().getGroup());
				String[] ds = ob.getDays().split("\\,");
				List<String> listDates = new ArrayList<>();
				for (int i = 0; i < ds.length; i++) {
					String dcount = ds[i];
					if (ob.getDayType().equals("day")) {
						boolean dayWise = true;
						// CHECKING DATE LIKE 2-3 OR 2-8. IF ANY EXCEPTION WE WONT CONSIDER THIS DAYS
						String[] days = null;
						int start = 0;
						int end = 0;
						if (dcount.contains("-")) {
							try {
								days = dcount.split("\\-");
								start = Integer.parseInt(days[0].trim());
								end = Integer.parseInt(days[1].trim());
							} catch (Exception e) {
								// TODO: handle exception
								dayWise = false;
							}
						}

						// ABOVE CONFITION SUCESS WE ARE GOINT TO PROCESS DAYS FROM-TO
						if (dcount.contains("-") && dayWise) {
							while (start <= end) {
								cal.setTime(d);
								cal.add(Calendar.DAY_OF_MONTH, start);
								listDates.add(sdf.format(cal.getTime()));
								start++;
							}
						} else {
							cal.setTime(d);
							cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(dcount));
							listDates.add(sdf.format(cal.getTime()));

						}
					} else if (ob.getDayType().equals("week")) {
//							dcount   2-6, 2 5 
//							Date studyDate = sm.getStartDate();
						boolean weekWise = true;
						if (dcount.contains("-")) {
							try {
								String[] days = dcount.split("\\-");
								int start = Integer.parseInt(days[0].trim());
								int end = Integer.parseInt(days[1].trim());
							} catch (Exception e) {
								// TODO: handle exception
								weekWise = false;
							}
						}
						List<String> tempDates = new ArrayList<>();
						if (dcount.contains("-") && weekWise) {
							String[] days = dcount.split("\\-");
							int start = Integer.parseInt(days[0].trim());
							int end = Integer.parseInt(days[1].trim());
							while (start <= end) {
								int day = start;
								for (int c = (day - 1) * 7; c < day * 7; c++) {
									cal.setTime(studyDate);
									cal.add(Calendar.DAY_OF_MONTH, c);
									tempDates.add(sdf.format(cal.getTime()));
								}
								start++;
							}
						} else {
							int days = Integer.parseInt(dcount);
							if (days > -1) {
								if (days == 0)
									days = 1;
								for (int c = (days - 1) * 7; c < days * 7; c++) {
									cal.setTime(studyDate);
									cal.add(Calendar.DAY_OF_MONTH, c);
									tempDates.add(sdf.format(cal.getTime()));
								}
							} else {
								days = -(days);
								int c = days * 7;
								for (int j = 1; j <= 7; j++) {
									System.out.println(c);
									cal.setTime(studyDate);
									cal.add(Calendar.DAY_OF_MONTH, -(c));
									tempDates.add(sdf.format(cal.getTime()));
									c--;
									System.out.println(listDates);
								}
							}
						}
						if (tempDates.size() > 1) {
							String datef = tempDates.get(0) + " to " + tempDates.get(tempDates.size() - 1);
							listDates.add(datef);
						} else
							listDates.addAll(tempDates);
					}

				}
				System.out.println(listDates);
				for (String sd : listDates) {
					List<StdSubGroupObservationCrfs> listOfObj = map.get(sd);
					if (listOfObj == null)
						listOfObj = new ArrayList<>();
					listOfObj.add(ob);
					map.put(sd, listOfObj);
				}

//				}
			}

		}
		Date sysDate = new Date();
		String sysdate = sdf.format(sysDate);
		Map<String, List<StdSubGroupObservationCrfs>> map2 = new HashMap<>();
//		date wise observations
		for (Map.Entry<String, List<StdSubGroupObservationCrfs>> m : map.entrySet()) {

			List<StdSubGroupObservationCrfs> list2 = new ArrayList<>();
			List<StdSubGroupObservationCrfs> list1 = m.getValue();
			for (StdSubGroupObservationCrfs sobj1 : list1) {
				StdSubGroupObservationCrfs cdata = new StdSubGroupObservationCrfs();
				cdata.setId(sobj1.getId());
				cdata.setSubGroupInfo(sobj1.getSubGroupInfo());
				cdata.setObservationName(sobj1.getObservationName());
				cdata.setCrf(sobj1.getCrf());
				cdata.setStudy(sobj1.getStudy());
				boolean started = false;
				int count = 0;
				int rcount = 0;
				SubGroupInfo subGroup = sobj1.getSubGroupInfo();

				System.out.println(subGroup.getGroup().getGroupName() + ":" + subGroup.getName() + " "
						+ sobj1.getObservationName() + "  " + m.getKey());
				System.out.println(subGroup.getId());
				List<SubGroupAnimalsInfoAll> animals = getSession().createCriteria(SubGroupAnimalsInfoAll.class)
						.add(Restrictions.eq("subGroup", subGroup)).list();
				List<SubGroupAnimalsInfoAll> canimals = new ArrayList<>();
				int animalDataCount = 0;
				for (SubGroupAnimalsInfoAll animal : animals) {
					SubGroupAnimalsInfoAll canimal = new SubGroupAnimalsInfoAll();
					canimal.setId(animal.getId());
					canimal.setAnimalNo(animal.getAnimalNo());
					canimal.setReviewStatus(animal.getReviewStatus());
					List<SubGroupAnimalsInfoCrfDataCount> entrys = getSession()
							.createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
							.add(Restrictions.eq("subGroupAnimalsInfoAll", animal))
							.add(Restrictions.eq("subGroup", subGroup)).add(Restrictions.eq("crf", sobj1))
							.add(Restrictions.eq("entryDate", m.getKey()))

							.list();
					if (entrys.size() > 0) {
						started = true;
						count += entrys.size();
						int i = 0;
						for (SubGroupAnimalsInfoCrfDataCount s : entrys) {
							if (s.getCrfStatus() != null) {
								canimal.setReviewStatus(s.getCrfStatus());
								if (s.getCrfStatus().equals("REVIEWED"))
									rcount++;
							}
							if (++i == entrys.size()) {
								List<SubGroupAnimalsInfoCrfDataCountReviewLevel> livels = getSession()
										.createCriteria(SubGroupAnimalsInfoCrfDataCountReviewLevel.class)
										.add(Restrictions.eq("dataCount", s)).list();
								for (SubGroupAnimalsInfoCrfDataCountReviewLevel l : livels) {
									l.setDate(sdf.format(l.getCreatedOn()));
								}
								canimal.setLivels(livels);
							}
						}
						animalDataCount++;
					}

					canimals.add(canimal);
				}
				if (started) {
					cdata.setColor("Blue");
					if (animalDataCount == animals.size()) {
						cdata.setColor("#FFC300");
						if (count == rcount) {
							cdata.setColor("Green");
						}
					}
				}
				cdata.setAnimals(canimals);
				list2.add(cdata);
			}
			map2.put(m.getKey(), list2);
		}
		return map2;
	}

	@Override
	public DepartmentMaster getDepartmentMasterRecord(Long deptId) {
		return (DepartmentMaster) getSession().createCriteria(DepartmentMaster.class).add(Restrictions.eq("id", deptId))
				.uniqueResult();
	}

	@Override
	public StudyDesignStatus getStudyDesignStatusRecord(Long studyId) {
		return (StudyDesignStatus) getSession().createCriteria(StudyDesignStatus.class)
				.add(Restrictions.eq("studyId", studyId)).uniqueResult();
	}

	@Override
	public List<SubGroupInfo> getSubGroupInfoList(StudyMaster sm) {
		List<SubGroupInfo> sgList = null;
		List<Long> groupIds = null;
		try {
			groupIds = getSession().createCriteria(GroupInfo.class).add(Restrictions.eq("study", sm))
					.setProjection(Projections.property("id")).list();
			if (groupIds != null && groupIds.size() > 0) {
				sgList = getSession().createCriteria(SubGroupInfo.class).add(Restrictions.in("group.id", groupIds))
						.list();
				if (sgList != null && sgList.size() > 0) {
					for (SubGroupInfo sgi : sgList) {
						List<SubGroupAnimalsInfo> animalInfo = subGroupAnimalsInfo(sgi);
						sgi.setAnimalInfo(animalInfo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sgList;
	}

	@Override
	public List<SubGroupAnimalsInfoAll> getSubGroupAnimalsInfoAllRecordsList(SubGroupAnimalsInfo sgai) {
		return getSession().createCriteria(SubGroupAnimalsInfoAll.class)
				.add(Restrictions.eq("subGroupAnimalsInfo", sgai)).list();
	}

	@Override
	public String updateExpermentalDesign(List<SubGroupInfo> updateSubList, List<SubGroupAnimalsInfo> updateAnimalList,
			List<SubGroupAnimalsInfoAll> updateanimalInfoAll, StudyDesignStatus sds, ApplicationAuditDetails apad,
			Map<Long, List<String>> newGsNameMap, Map<Long, List<String>> newSgDoseMap,
			Map<Long, List<String>> newSgConcMap, Map<Long, List<Integer>> newSgCountMap,
			Map<Long, List<String>> newFromMap, Map<Long, List<String>> newToMap, Map<Long, List<String>> newGenderMap,
			String userName, StudyMaster sm) {
		String result = "Failed";
		boolean subFlag = false;
		boolean subAnimalFlag = false;
		boolean subAllAnimalFlag = false;
		boolean sdsFlag = false;
		long apadNo = 0;
		boolean flag = false;
		try {
			if (updateSubList.size() > 0) {
				for (SubGroupInfo sg : updateSubList) {
					getSession().update(sg);
					subFlag = true;
				}
			} else
				subFlag = true;

			if (updateAnimalList.size() > 0) {
				for (SubGroupAnimalsInfo sga : updateAnimalList) {
					getSession().update(sga);
					subAnimalFlag = true;
				}
			} else
				subAnimalFlag = true;

			if (updateanimalInfoAll.size() > 0) {
				for (SubGroupAnimalsInfoAll sgaall : updateanimalInfoAll) {
					getSession().update(sgaall);
					subAllAnimalFlag = true;
				}
			} else
				subAllAnimalFlag = true;

			int count = 0;
			long gid = 0;
			if (newGsNameMap.size() > 0) {
				for (Map.Entry<Long, List<String>> map : newGsNameMap.entrySet()) {
					Long key = map.getKey();
					if (gid != key) {
						gid = map.getKey();
						count = 0;
					}
					List<String> strList = map.getValue();
					for (int j = 0; j < strList.size(); j++) {
						List<String> doseList = newSgDoseMap.get(key);
						List<String> concList = newSgConcMap.get(key);
						List<Integer> countList = newSgCountMap.get(key);
						List<String> fromStrList = newFromMap.get(key);
						List<String> toStrList = newToMap.get(key);
						List<String> genderList = newGenderMap.get(key);

						String sgName = strList.get(j);
						SubGroupInfo newSgi = new SubGroupInfo();
						newSgi.setConc(concList.get(j));
						newSgi.setCreatedBy(userName);
						newSgi.setCreatedOn(new Date());
						newSgi.setDose(doseList.get(j));
						GroupInfo gi = (GroupInfo) getSession().createCriteria(GroupInfo.class)
								.add(Restrictions.eq("id", key)).uniqueResult();
						newSgi.setGroup(gi);
						newSgi.setName(sgName);
						newSgi.setCrfConfiguation(false);
						newSgi.setStudy(sm);
						getSession().save(newSgi);

						if (gi.getGender().equals("Both")) {
							for (int t = 0; t < 2; t++) {
								saveSubGroupAnimalInfDetails(countList, userName, fromStrList, genderList, newSgi, gi,
										sm, toStrList, count);
								count++;
							}
						} else {
							saveSubGroupAnimalInfDetails(countList, userName, fromStrList, genderList, newSgi, gi, sm,
									toStrList, j);
						}
					}
				}
				flag = true;
			} else
				flag = true;
			if (subFlag && subAnimalFlag && subAllAnimalFlag && flag) {
				getSession().update(sds);
				sdsFlag = true;
				apadNo = (long) getSession().save(apad);
				if (sdsFlag && apadNo > 0) {
					result = "success";
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	private void saveSubGroupAnimalInfDetails(List<Integer> countList, String userName, List<String> fromStrList,
			List<String> genderList, SubGroupInfo newSgi, GroupInfo gi, StudyMaster sm, List<String> toStrList, int k) {
		try {
//			for(int k=0; k<countList.size(); k++) {
			SubGroupAnimalsInfo sgai = new SubGroupAnimalsInfo();
			sgai.setCount(countList.get(k));
			sgai.setCreatedBy(userName);
			sgai.setCreatedOn(new Date());
			String fromStr = fromStrList.get(k);
			String[] arr = fromStr.split("\\/");
			sgai.setFormId(fromStr);
			sgai.setFrom(arr[1]);
			sgai.setGender(genderList.get(k));
			sgai.setGroup(gi);
			sgai.setStudy(sm);
			sgai.setSubGroup(newSgi);
			String toStr = toStrList.get(k);
			String[] toArr = toStr.split("\\/");
			sgai.setTo(toArr[1]);
			sgai.setToId(toStr);
			getSession().save(sgai);

			int start = Integer.parseInt(arr[1]);
			int end = Integer.parseInt(toArr[1]);
			for (; start <= end; start++) {
				SubGroupAnimalsInfoAll sgaia = new SubGroupAnimalsInfoAll();
				sgaia.setSubGroup(newSgi);
				sgaia.setSubGroupAnimalsInfo(sgai);
				sgaia.setNo(start);
				sgaia.setCreatedBy(userName);
				if (start < 10) {
					sgaia.setAnimalNo("TOXX/00" + start);
				} else if (start < 100) {
					sgaia.setAnimalNo("TOXX/0" + start);
				} else {
					sgaia.setAnimalNo("TOXX/" + start);
				}
				getSession().save(sgaia);
			}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ExpermentalDto getExpermentalDtoDetails(Long studyId) {
		ExpermentalDto edto = null;
		StudyMaster sm = null;
		StudyDesignStatus sds = null;
		try {
			sm = (StudyMaster) getSession().createCriteria(StudyMaster.class).add(Restrictions.eq("id", studyId))
					.uniqueResult();

			sds = (StudyDesignStatus) getSession().createCriteria(StudyDesignStatus.class)
					.add(Restrictions.eq("studyId", studyId)).uniqueResult();

			edto = new ExpermentalDto();
			edto.setSds(sds);
			edto.setSm(sm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return edto;
	}

	@Override
	public List<SubGroupInfo> getSubGroupRecrodsList(GroupInfo g) {
		return getSession().createCriteria(SubGroupInfo.class).add(Restrictions.eq("group", g)).list();
	}

	@Override
	public GroupInfo getGroupInfoRecord(Long id) {
		return (GroupInfo) getSession().createCriteria(GroupInfo.class).add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public boolean saveNewSubGroupDetails(Map<Long, List<String>> newGsNameMap, Map<Long, List<String>> newSgDoseMap,
			Map<Long, List<String>> newSgConcMap, Map<Long, List<Integer>> newSgCountMap,
			Map<Long, List<String>> newFromMap, Map<Long, List<String>> newToMap, Map<Long, List<String>> newGenderMap,
			String userName, StudyMaster sm) {
		boolean flag = false;
		try {
			if (newGsNameMap.size() > 0) {
				for (Map.Entry<Long, List<String>> map : newGsNameMap.entrySet()) {
					Long key = map.getKey();
					List<String> strList = map.getValue();
					for (int j = 0; j < strList.size(); j++) {
						List<String> doseList = newSgDoseMap.get(key);
						List<String> concList = newSgConcMap.get(key);
						List<Integer> countList = newSgCountMap.get(key);
						List<String> fromStrList = newFromMap.get(key);
						List<String> toStrList = newToMap.get(key);
						List<String> genderList = newGenderMap.get(key);

						String sgName = strList.get(j);
						SubGroupInfo newSgi = new SubGroupInfo();
						newSgi.setConc(concList.get(j));
						newSgi.setCreatedBy(userName);
						newSgi.setCreatedOn(new Date());
						newSgi.setDose(doseList.get(j));
						GroupInfo gi = (GroupInfo) getSession().createCriteria(GroupInfo.class)
								.add(Restrictions.eq("id", key)).uniqueResult();
						newSgi.setGroup(gi);
						newSgi.setName(sgName);
						newSgi.setCrfConfiguation(false);
						newSgi.setStudy(sm);
						getSession().save(newSgi);

						SubGroupAnimalsInfo sgai = new SubGroupAnimalsInfo();
						sgai.setCount(countList.get(j));
						sgai.setCreatedBy(userName);
						sgai.setCreatedOn(new Date());
						String fromStr = fromStrList.get(j);
						String[] arr = fromStr.split("\\/");
						sgai.setFormId(fromStr);
						sgai.setFrom(arr[1]);
						sgai.setGender(genderList.get(j));
						sgai.setGroup(gi);
						sgai.setStudy(sm);
						sgai.setSubGroup(newSgi);
						String toStr = toStrList.get(j);
						String[] toArr = toStr.split("\\/");
						sgai.setTo(toStr);
						sgai.setToId(toArr[1]);
						getSession().save(sgai);

						int start = Integer.parseInt(sgai.getFrom());
						int end = Integer.parseInt(sgai.getTo());
						for (; start <= end; start++) {
							SubGroupAnimalsInfoAll sgaia = new SubGroupAnimalsInfoAll();
							sgaia.setSubGroup(newSgi);
							sgaia.setSubGroupAnimalsInfo(sgai);
							sgaia.setNo(start);
							sgaia.setCreatedBy(userName);
							if (start < 10) {
								sgaia.setAnimalNo("TOXX/00" + start);
							} else if (start < 100) {
								sgaia.setAnimalNo("TOXX/0" + start);
							} else {
								sgaia.setAnimalNo("TOXX/" + start);
							}
							getSession().save(sgaia);
						}

					}

				}
				flag = true;
			} else
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}

	@Override
	public List<StdSubGroupObservationCrfs> getSubGroupObservations(Long studyId, Long subGroupId) {
		return (List<StdSubGroupObservationCrfs>) getSession().createCriteria(StdSubGroupObservationCrfs.class)
				.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("subGroupInfo.id", subGroupId)).list();
	}

	@Override
	public List<SubGroupAnimalsInfoAll> getsSbGroupAnimalsInfoAll(Long subGroupInfoId) {
		List<SubGroupAnimalsInfoAll> sgaiList = getSession().createCriteria(SubGroupAnimalsInfoAll.class)
				.add(Restrictions.eq("subGroupAnimalsInfo", subGroupInfoId)).list();
		if (sgaiList != null && sgaiList.size() > 0) {
			for (SubGroupAnimalsInfoAll sgai : sgaiList) {
				Hibernate.initialize(sgai.getSubGroup());
				Hibernate.initialize(sgai.getSubGroup().getGroup());
				Hibernate.initialize(sgai.getSubGroupAnimalsInfo());
				Hibernate.initialize(sgai.getSubGroup().getStudy());
			}
		}
		return sgaiList;
	}

	@Override
	public CrfDataEntryDto getCrfDataEntryDetails(Long studyId, Long animalId, Long crfId, Long subGroupId) {
		CrfDataEntryDto cdeDto = null;
		StudyMaster sm = null;
//		SubGroupAnimalsInfoAll animal = null;
		StdSubGroupObservationCrfs stdcrf = null;
		StudyDesignStatus sds = null;
		List<SubjectDataEntryDetails> sdedList = null;
		List<Long> animalIds = null;
		try {
			sm = (StudyMaster) getSession().createCriteria(StudyMaster.class).add(Restrictions.eq("id", studyId))
					.uniqueResult();

			sds = (StudyDesignStatus) getSession().createCriteria(StudyDesignStatus.class)
					.add(Restrictions.eq("studyId", studyId)).uniqueResult();

			/*
			 * Long minId = (Long) getSession().createCriteria(SubGroupAnimalsInfoAll.class)
			 * .add(Restrictions.eq("subGroupAnimalsInfo.id", animalId))
			 * .setProjection(Projections.min("id")).uniqueResult(); if(minId != null) {
			 * animal = (SubGroupAnimalsInfoAll)
			 * getSession().createCriteria(SubGroupAnimalsInfoAll.class)
			 * .add(Restrictions.eq("id", minId)).uniqueResult();
			 * 
			 * Hibernate.initialize(animal.getSubGroup());
			 * Hibernate.initialize(animal.getSubGroup().getGroup());
			 * Hibernate.initialize(animal.getSubGroupAnimalsInfo());
			 * Hibernate.initialize(animal.getSubGroup().getStudy()); }
			 */

			/*
			 * animalIds = getSession().createCriteria(SubGroupAnimalsInfoAll.class)
			 * .add(Restrictions.eq("subGroup.id", subGroupId))
			 * .setProjection(Projections.property("id")).list();
			 */

			sdedList = getSession().createCriteria(SubjectDataEntryDetails.class).add(Restrictions.eq("crfId", crfId))
					.add(Restrictions.eq("subgroupId", subGroupId)).add(Restrictions.eq("studyId", sm.getId())).list();

			Crf crf = (Crf) getSession().createCriteria(Crf.class).add(Restrictions.eq("id", crfId)).uniqueResult();

			stdcrf = (StdSubGroupObservationCrfs) getSession().createCriteria(StdSubGroupObservationCrfs.class)
					.add(Restrictions.eq("crf", crf)).add(Restrictions.eq("study", sm))
					.add(Restrictions.eq("subGroupInfo.id", subGroupId)).uniqueResult();
			Hibernate.initialize(stdcrf.getCrf().getSections());
			Hibernate.initialize(stdcrf.getSubGroupInfo());
			Hibernate.initialize(stdcrf.getSubGroupInfo().getGroup());
			Collections.sort(stdcrf.getCrf().getSections());
			for (CrfSection sec : stdcrf.getCrf().getSections()) {
				Hibernate.initialize(sec.getElement());
				if (sec.getElement() == null || sec.getElement().size() == 0) {
				} else {
					for (CrfSectionElement ele : sec.getElement()) {
						if (ele.getType().equals("selectTable")) {
							List<CrfSectionElementValue> evalues = new ArrayList<>();
							CrfMapplingTableColumnMap colMap = crfMapplingTableColumnMap(ele);
							String sql = "Select " + colMap.getMappingColumn().getCloumnName() + " from "
									+ colMap.getMappingTable().getTableName();
							SQLQuery query = getSession().createSQLQuery(sql);
							query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
							List results = query.list();
							for (Object object : results) {
								Map<String, String> row = (Map<String, String>) object;
								for (Map.Entry<String, String> m : row.entrySet()) {
									CrfSectionElementValue mv = new CrfSectionElementValue();
									mv.setValue(m.getValue());
									evalues.add(mv);
								}

							}
							ele.setElementValues(evalues);
						} else
							Hibernate.initialize(ele.getElementValues());
					}
				}
				Hibernate.initialize(sec.getGroup());
				CrfGroup group = sec.getGroup();
				if (group != null) {
					Hibernate.initialize(group.getElement());
					if (group.getElement() == null || group.getElement().size() == 0) {
					} else {
						for (CrfGroupElement ele : group.getElement()) {
							Hibernate.initialize(ele.getElementValues());
						}
					}
				}
			}
			cdeDto = new CrfDataEntryDto();
//			cdeDto.setAnimal(animal);
			cdeDto.setSm(sm);
			cdeDto.setStdCrf(stdcrf);
			cdeDto.setSds(sds);
			cdeDto.setSdedList(sdedList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cdeDto;
	}

	private CrfMapplingTableColumnMap crfMapplingTableColumnMap(CrfSectionElement ele) {
		CrfMapplingTableColumnMap map = (CrfMapplingTableColumnMap) getSession()
				.createCriteria(CrfMapplingTableColumnMap.class).add(Restrictions.eq("element", ele)).uniqueResult();
		return map;
	}

	@Override
	public List<SubjectDataEntryDetails> getSubjectDataEntryDetailsList(Set<Long> crfIds, Long studyId,
			Long subGroupId) {
		return getSession().createCriteria(SubjectDataEntryDetails.class).add(Restrictions.eq("study.id", studyId))
				.add(Restrictions.eq("subGroup.id", subGroupId)).add(Restrictions.in("observationCrf.id", crfIds))
				.list();
	}

	@Override
	public List<SubGroupAnimalsInfoAll> getSubGroupAnimalsInfoAllList(Long subGroupId) {
		return getSession().createCriteria(SubGroupAnimalsInfoAll.class).add(Restrictions.eq("subGroup.id", subGroupId))
				.list();
	}

	@Override
	public String saveorUpdateSubjectDataEntryDetails(List<StdSubGroupObservationCrfs> updateSdedList,
			List<StdSubGroupObservationCrfs> newSdedList) {
		boolean upsdedFlag = false;
		boolean newSdedFlag = false;
		String result = "Failed";
		try {
			for (StdSubGroupObservationCrfs sded : updateSdedList) {
				getSession().update(sded);
			}
			for (StdSubGroupObservationCrfs newsded : newSdedList) {
				getSession().save(newsded);
			}
			result = "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed";
		}
		return result;
	}

	@Override
	public List<Crf> getCrfRecordsList(Set<Long> crfIds) {
		return getSession().createCriteria(Crf.class).add(Restrictions.in("id", crfIds)).list();
	}

	@Override
	public SubjectDataEntryDetails getSubjectDataEntryDetailsRecord(SubGroupAnimalsInfoAll animalInfoAll, Long crfId) {
		return (SubjectDataEntryDetails) getSession().createCriteria(SubjectDataEntryDetails.class)
				.add(Restrictions.eq("animalId", animalInfoAll)).add(Restrictions.eq("crfId", crfId)).uniqueResult();
	}

	@Override
	public boolean updateSubjectDataEntryDetailsRecord(SubjectDataEntryDetails sded) {
		boolean flag = false;
		try {
			getSession().update(sded);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;

	}

	@Override
	public ObservationData observationDataAllAnimal(Long subGroupInfoId, Long stdSubGroupObservationCrfsId,
			LoginUsers user, Long groupId, Long subGroupId) {
		int totoal = 0;
		SubGroupAnimalsInfoAll subGroupAnimalsInfoAll = subGroupAnimalsInfoAll(subGroupInfoId);

		SubGroupAnimalsInfo subGroupAnimalsInfo = subGroupAnimalsInfoAll.getSubGroupAnimalsInfo();
		Crf crf = studyCrf(subGroupAnimalsInfo.getStudy(), subGroupAnimalsInfo.getSubGroup().getId(),
				stdSubGroupObservationCrfsId);
		StdSubGroupObservationCrfs stdSubGroupObservationCrfs = (StdSubGroupObservationCrfs) getSession()
				.get(StdSubGroupObservationCrfs.class, stdSubGroupObservationCrfsId);
		List<SubGroupAnimalsInfoCrfDataCount> entiress = getSession()
				.createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
				.add(Restrictions.eq("crf", stdSubGroupObservationCrfs)).add(Restrictions.eq("group.id", groupId))
				.add(Restrictions.eq("subGroup.id", subGroupId)).list();

		// .add(Restrictions.eq("subGroupAnimalsInfoAll",
		// subGroupAnimalsInfoAll)).list();

		ObservationData ob = new ObservationData();
		int i = 0;
		for (SubGroupAnimalsInfoCrfDataCount sgadc : entiress) {
			ObservationReviewLevel orl = null;
			try {
				orl = (ObservationReviewLevel) getSession().createCriteria(ObservationReviewLevel.class)
						.add(Restrictions.eq("subGroupObservationCrf", stdSubGroupObservationCrfs))
						.add(Restrictions.eq("subGroupAnimalsInfoCrfDataCount", sgadc))
						.add(Restrictions.eq("user", user)).uniqueResult();
				if (orl != null && !orl.isReviewFlag()) {
					sgadc.setUserReview(true);
					i++;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		if (i == entiress.size()) {
			ob.setMessage("Review Completed for the user : '" + user.getUsername() + "'.");
			ob.setAllowDataEntry(false);
		}

//		if(!stdSubGroupObservationCrfs.isReviewStatus()) {
//			ReviewLevel rl =  (ReviewLevel) getSession().createCriteria(ReviewLevel.class)
//				.add(Restrictions.eq("status", "Active")).uniqueResult();
//			int max = 0;
//			try {
//				max = (int) getSession().createCriteria(ObservationReviewLevel.class)
//					.add(Restrictions.eq("subGroupObservationCrf", stdSubGroupObservationCrfs))
//					.add(Restrictions.eq("subGroupAnimalsInfoAll", subGroupAnimalsInfoAll))
//					.setProjection(Projections.max("userLevel")).uniqueResult();
//			}catch (Exception e) {
//				// TODO: handle exception
//			}
//			if(max < rl.getObservationApprovelLevel()) {
//				if(++max == user.getReviewLevel() || user.getReviewLevel() == 100) {
//					ob.setAllowDataEntry(true);
//					stdSubGroupObservationCrfs.setNeedToReview(rl.getObservationApprovelLevel() - max);
//				}
//			}
//		}else {
//			ob.setMessage("Review Completed.");
//			ob.setAllowDataEntry(false);
//		}

		ob.setStudy(subGroupAnimalsInfo.getStudy());
		ob.setGroup(subGroupAnimalsInfo.getGroup());
		ob.setSubGroup(subGroupAnimalsInfo.getSubGroup());
		ob.setAnimal(subGroupAnimalsInfo);
		ob.setAnimalAll(subGroupAnimalsInfoAll);
		ob.setCrfOrObservation(crf);
		Map<Long, CrfSectionElement> elements = new HashMap<>();
		for (CrfSection sec : crf.getSections()) {
			for (CrfSectionElement ele : sec.getElement()) {
				if (!ele.getType().equals("non")) {
					elements.put(ele.getId(), ele);
				}
			}
		}
		ob.setElements(elements);

//		List<SubGroupAnimalsInfoCrfDataCount> dcs = subGroupAnimalsInfoCrfDataCount(subGroupAnimalsInfoAll, stdSubGroupObservationCrfsId);

		List<AnimalObservationData> animalData = new ArrayList<>();
		for (SubGroupAnimalsInfoCrfDataCount dc : entiress) {
			List<SubGroupAnimalsInfoCrfDataCountReviewLevel> li = getSession()
					.createCriteria(SubGroupAnimalsInfoCrfDataCountReviewLevel.class)
					.add(Restrictions.eq("dataCount", dc)).add(Restrictions.eq("user", user)).list();
			SubGroupAnimalsInfoCrfDataCountReviewLevel l = null;
			if (li.size() > 0)
				l = li.get(li.size() - 1);
//					l = (SubGroupAnimalsInfoCrfDataCountReviewLevel) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCountReviewLevel.class)
//					.add(Restrictions.eq("dataCount", dc))
//					.add(Restrictions.eq("user", user)).uniqueResult();

			List<CrfSectionElementData> crfData = getSession().createCriteria(CrfSectionElementData.class)
					.add(Restrictions.eq("dataCount", dc)).list();
			AnimalObservationData data = new AnimalObservationData();
			data.setId(dc.getId());
			if (l != null)
				data.setReviewed(true);
			data.setDeviationMessage(dc.getDeviationMessage());
			data.setFrequntlyMessage(dc.getFrequntlyMessage());
			data.setUserName(dc.getCreatedBy());
			data.setDate(dc.getCreatedOn());
			data.setCount(dc.getDataEntryCount());

			// adding for all animal review crf list buchi
			data.setAnimalno(dc.getSubGroupAnimalsInfoAll().getAnimalNo());
			data.setAnimalId(dc.getSubGroupAnimalsInfoAll().getId());
			Map<Long, CrfSectionElementData> elementData = new HashMap<>();
			for (CrfSectionElementData crfdata : crfData) {
				if (!crfdata.getElement().getType().equals("non")) {
					elementData.put(crfdata.getElement().getId(), crfdata);
					try {
						Long red = (Long) getSession().createCriteria(CrfDescrpency.class)
								.add(Restrictions.eq("secEleData", crfdata))
								.add(Restrictions.or(Restrictions.eq("status", "open"),
										Restrictions.eq("status", "onHold")))
								.setProjection(Projections.rowCount()).uniqueResult();
						crfdata.setDiscripency(red.intValue());
						if (red > 0)
							System.out.println(crfdata.getId() + "--------" + red);
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Long red = (Long) getSession().createCriteria(CrfDescrpency.class)
								.add(Restrictions.eq("secEleData", crfdata)).setProjection(Projections.rowCount())
								.uniqueResult();
						crfdata.setDiscripencyClose(red.intValue());
						if (red > 0)
							System.out.println(crfdata.getId() + "--------" + red);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			try {
				Long red = (Long) getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("dataCount", dc))
						.add(Restrictions.eq("status", "closed")).setProjection(Projections.rowCount()).uniqueResult();
				Long all = (Long) getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("dataCount", dc))
						.setProjection(Projections.rowCount()).uniqueResult();
				if (all == red) {
					data.setDiscrepencyClosed(0);
				} else {
					Long diff = all - red;
					data.setDiscrepencyClosed(diff.intValue());
					totoal += diff.intValue();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			data.setElementData(elementData);
			data.setDayOrWeek(dc.getDayWeekCount());

			animalData.add(data);
		}
		ob.setTotoalDescNotDone(totoal);
		ob.setAnimalData(animalData);
		return ob;
	}

	@Override
	public List<AmendmentDetails> getAmendmentDetailsRecordsList(Long studyId) {
		return getSession().createCriteria(AmendmentDetails.class).add(Restrictions.eq("studyId", studyId))
				.add(Restrictions.eq("status", "Approved")).list();
	}

	@Override
	public SubjectDataEntryDetails getSubjectDataEntryDetailsRecord(StdSubGroupObservationCrfs stdCrf, Long studyId,
			SubGroupAnimalsInfoAll animal, Long subGroupId) {
		return (SubjectDataEntryDetails) getSession().createCriteria(SubjectDataEntryDetails.class)
				.add(Restrictions.eq("animalId", animal)).add(Restrictions.eq("studyId", studyId))
				.add(Restrictions.eq("subgroupId", subGroupId)).add(Restrictions.eq("crfId", stdCrf.getCrf().getId()))
				.uniqueResult();
	}

	@Override
	public CrfFormulaDto getFrormulaDataofCurrentCrf(Long crfId, Long studyId) {
		CrfFormulaDto cfDto = null;
		List<Long> secIds = null;
		List<CrfSectionElement> secList = null;
		List<String> formulaList = null;
		try {
			secIds = getSession().createCriteria(CrfSection.class).add(Restrictions.eq("crf.id", crfId))
					.setProjection(Projections.property("id")).list();
			if (secIds != null && secIds.size() > 0) {
				formulaList = getSession().createCriteria(CrfSectionElement.class)
						.add(Restrictions.in("section.id", secIds)).add(Restrictions.ne("formula", "No"))
						.setProjection(Projections.property("formula")).list();

				secList = getSession().createCriteria(CrfSectionElement.class)
						.add(Restrictions.in("section.id", secIds)).list();
			}
			cfDto = new CrfFormulaDto();
			cfDto.setSecList(secList);
			cfDto.setFormulsList(formulaList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cfDto;
	}

	@Override
	public List<SubGroupAnimalsInfoAll> getSubGroupAnimalsInfoAllRecordsList(Long subGroupId) {
		return getSession().createCriteria(SubGroupAnimalsInfoAll.class).add(Restrictions.eq("subGroup.id", subGroupId))
				.list();
	}

	@Override
	public List<SubGroupAnimalsInfo> allSubGroupAnimalsInfos(Long studyId) {
		return getSession().createCriteria(SubGroupAnimalsInfo.class).add(Restrictions.eq("study.id", studyId)).list();
	}

	@Override
	public Long subGroupId(Long groupId) {
		// TODO Auto-generated method stub
		return (Long) getSession().createCriteria(SubGroupInfo.class).add(Restrictions.eq("group.id", groupId))
				.setProjection(Projections.property("id")).uniqueResult();
	}

	@Override
	public StdSubGroupObservationCrfs stdSubGroupObservationCrfsWithId(Long stdSubGroupObservationCrfsId) {
		// TODO Auto-generated method stub
		return (StdSubGroupObservationCrfs) getSession().get(StdSubGroupObservationCrfs.class,
				stdSubGroupObservationCrfsId);
	}

	@Override
	public StudyCageAnimal nextAnimalForObservationData(Long stdSubGroupObservationCrfsId, Long cageId, Long studyId,
			Long subGroupId) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
			Date fromDate = sdf.parse(sdf.format(new Date()));
			Calendar c = Calendar.getInstance();
			c.setTime(fromDate);
			c.add(Calendar.DATE, 1);
			Date toDate = sdf.parse(sdf.format(c.getTime()));

			StdSubGroupObservationCrfs stdSubGroupObservationCrfs = stdSubGroupObservationCrfsWithId(
					stdSubGroupObservationCrfsId);
			System.out.println(stdSubGroupObservationCrfs.getSubGroupInfo().getId());

			AnimalCage cage = (AnimalCage) getSession().get(AnimalCage.class, cageId);
			List<StudyCageAnimal> studyCageAnimals = getSession().createCriteria(StudyCageAnimal.class)
					.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("cage", cage)).list();
			SortedMap<Integer, StudyCageAnimal> mapStudyCageAnimals = new TreeMap<>();
			List<Long> animalIds = new ArrayList<>();
			studyCageAnimals.forEach((sa) -> {

				StringBuilder sb = new StringBuilder();
				sb.append("cageId : " + cageId).append("\tCage Name : " + cage.getName()).append("\n");
				sb.append("StudyCageAnimal Id : " + sa.getId());
				sb.append("animal id : " + sa.getAnimal().getId() + "\tGender : " + sa.getAnimal().getGender()).append(
						"\tname : " + sa.getAnimal().getAnimalNo() + "\tno : " + sa.getAnimal().getPermanentNo())
						.append("\n");
				sb.append("SubjgroupId : " + subGroupId).append("\n");
				sb.append("SubGroupAnimalsInfo id : " + sa.getAnimal().getSubGrop().getId()).append("\n");
				sb.append("animala SubjgroupId : " + sa.getAnimal().getSubGrop().getSubGroup().getId()).append("\n");
//				System.out.println(sb.toString());
				System.out.println(sa.getAnimal().getSubGrop().getSubGroup());
				System.out.println(sa.getAnimal().getSubGrop().getSubGroup().getId());
				System.out.println(subGroupId);
				System.out.println((sa.getAnimal().getSubGrop().getSubGroup().getId() == subGroupId));
				System.out.println((sa.getAnimal().getSubGrop().getSubGroup().getId().equals(subGroupId)));
				System.out.println(sa.getAnimal().getSubGrop().getSubGroup() != null
						&& (sa.getAnimal().getSubGrop().getSubGroup().getId().equals(subGroupId)));
				if (sa.getAnimal().getSubGrop().getSubGroup() != null
						&& (sa.getAnimal().getSubGrop().getSubGroup().getId().equals(subGroupId))) {
					mapStudyCageAnimals.put(sa.getAnimal().getAnimalId(), sa);
					animalIds.add(sa.getAnimal().getId());
				}
			});

			List<SubjectDataEntryDetails> list = getSession().createCriteria(SubjectDataEntryDetails.class)
					.add(Restrictions.eq("observationCrf.id", stdSubGroupObservationCrfs.getId()))
					.add(Restrictions.eq("cage", cage)).add(Restrictions.in("animal.id", animalIds))
					.add(Restrictions.between("entredOn", fromDate, toDate)).list();
			SortedMap<Long, StudyCageAnimal> mapEnteredStudyCageAnimals = new TreeMap<>();
			list.forEach((sa) -> {
				mapEnteredStudyCageAnimals.put(sa.getStudyAnimalCage().getId(), sa.getStudyAnimalCage());
			});

			for (Map.Entry<Integer, StudyCageAnimal> map : mapStudyCageAnimals.entrySet()) {
				if (!mapEnteredStudyCageAnimals.containsKey(map.getValue().getId())) {
					return map.getValue();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<AnimalCage> animalCages(Long studyId) {
		// TODO Auto-generated method stub
		List<AnimalCage> cages = getSession().createCriteria(StudyCageAnimal.class)
				.add(Restrictions.eq("study.id", studyId))
				.setProjection(Projections.distinct(Projections.property("cage"))).list();
		return cages;
	}

	@Override
	public List<StudyCageAnimal> studyCageAnimal(Long studyId, Long cageId) {
		// TODO Auto-generated method stub
		Criteria cri = getSession().createCriteria(StudyCageAnimal.class).add(Restrictions.eq("study.id", studyId));
		if (cageId > 0)
			cri.add(Restrictions.eq("cage.id", cageId));
		return cri.list();
	}

	@Override
	public List<StudyAnimal> studyAnimalWithGender(Long studyId, String gender) {
		// TODO Auto-generated method stub
		Criteria cri = getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", studyId));
		if (!gender.equals("gender"))
			cri.add(Restrictions.eq("gender", gender));
		return cri.list();
	}

	@Override
	public StudyAnimal studyAnimalWithId(Long animalId) {
		// TODO Auto-generated method stub
		return (StudyAnimal) getSession().get(StudyAnimal.class, animalId);
	}

	@Override
	public StudyCageAnimal studyCageAnimalWithId(Long studyCageAnimalId) {
		// TODO Auto-generated method stub
		return (StudyCageAnimal) getSession().get(StudyCageAnimal.class, studyCageAnimalId);
	}

	@Override
	public SubGroupAnimalsInfo subGroupAnimalsInfo(Long studyId, SubGroupInfo subGroupInfo, String gender) {
		// TODO Auto-generated method stub
		return (SubGroupAnimalsInfo) getSession().createCriteria(SubGroupAnimalsInfo.class)
				.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("subGroup.id", subGroupInfo.getId()))
				.add(Restrictions.eq("gender", gender)).uniqueResult();
	}

	@Override
	public List<StdSubGroupObservationCrfs> stdSubGroupObservationCrfs(List<Long> crfIds, Long study, Long subGroupId) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(StdSubGroupObservationCrfs.class).add(Restrictions.eq("study.id", study))
				.add(Restrictions.eq("subGroupInfo.id", subGroupId))
//				.add(Restrictions.in("crf.id", crfIds))
				.list();
	}

	@Override
	public Map<Long, DepartmentMaster> separtmentMasters(List<Long> deptIds) {
		// TODO Auto-generated method stub
		List<DepartmentMaster> list = getSession().createCriteria(DepartmentMaster.class)
				.add(Restrictions.in("id", deptIds)).list();
		Map<Long, DepartmentMaster> map = new HashMap<>();
		list.forEach((l) -> {
			map.put(l.getId(), l);
		});
		return map;
	}

	@Override
	public boolean checkDataEntryEligible(StudyMaster sm) {
		try {
			Long count = (Long) getSession().createCriteria(StdSubGroupObservationCrfs.class)
					.add(Restrictions.eq("study.id", sm.getId())).add(Restrictions.eq("active", true))
					.add(Restrictions.eq("reviewStatus", true)).setProjection(Projections.rowCount()).uniqueResult();
			if (count > 0)
				return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	private boolean checkcheckDataEntryEligibleWithSubGroup(SubGroupInfo sgi) {
		try {
			Long count = (Long) getSession().createCriteria(StdSubGroupObservationCrfs.class)
					.add(Restrictions.eq("subGroupInfo.id", sgi.getId())).add(Restrictions.eq("active", true))
					.add(Restrictions.eq("reviewStatus", true)).setProjection(Projections.rowCount()).uniqueResult();
			if (count > 0)
				return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public AnimalCage cages(Long cageId) {
		// TODO Auto-generated method stub
		return (AnimalCage) getSession().get(AnimalCage.class, cageId);
	}

	@Override
	public List<AnimalCage> groupAnimalCages(Long studyId, Long subGroupId) {
//		List<SubGroupAnimalsInfoAll> subGroupAnimalInfoAll = getSession().createCriteria(SubGroupAnimalsInfoAll.class)
//				.add(Restrictions.eq("subGroup.id", subGroupId))
		List<AnimalCage> cages = getSession().createCriteria(StudyCageAnimal.class)
				.add(Restrictions.eq("study.id", studyId))
				.setProjection(Projections.distinct(Projections.property("cage"))).list();
		return cages;
	}

	@Override
	public List<SysmexAnimalCode> sysmexAnimalCodes() {
		// TODO Auto-generated method stub
		return getSession().createCriteria(SysmexAnimalCode.class).list();
	}

	@Override
	public StdSubGroupObservationCrfs getStudyTreatmentConfigList(Long crfId, Long studyId, Long subGroupId) {
		return (StdSubGroupObservationCrfs) getSession().createCriteria(StdSubGroupObservationCrfs.class)
				.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("subGroupInfo.id", subGroupId))
				.add(Restrictions.eq("crf.id", crfId)).uniqueResult();
	}

	@Override
	public void saveStudyTreatmentData(StdSubGroupObservationCrfs sad) {
		// TODO Auto-generated method stub
		getSession().save(sad);
	}

	@Override
	public void updateStudyTreatmentData(StdSubGroupObservationCrfs sad) {
		// TODO Auto-generated method stub
		getSession().merge(sad);
	}

	@Override
	public boolean updateStudyTreatmentDataDatesDetails(Long id, String value, Long userId) {
		StudyTreatmentDataDates sd = studyTreatmentDataDatesById(id);
		sd.setNoOfEntry(Integer.parseInt(value));
		sd.setUpdatedBy(userDao.findById(userId).getUsername());
		sd.setUpdatedOn(new Date());
		return true;
	}

	private StudyTreatmentDataDates studyTreatmentDataDatesById(Long id) {
		return (StudyTreatmentDataDates) getSession().get(StudyTreatmentDataDates.class, id);
	}

	@Override
	public boolean removeStudyTreatmentDataDatesDetails(Long studyTreatmentDataDatesId, Long userId) {
		StudyTreatmentDataDates sd = studyTreatmentDataDatesById(studyTreatmentDataDatesId);
		sd.setActiveStatus(false);
		sd.setUpdatedBy(userDao.findById(userId).getUsername());
		sd.setUpdatedOn(new Date());
		return true;
	}

	@Override
	public List<StdSubGroupObservationCrfs> stdSubGroupObservationCrf(Long studyId, Long userRoleId) {
		Criteria cric = getSession().createCriteria(ObservationRole.class)
				.setProjection(Projections.property("crf.id"));
		if (userRoleId != null) {
			cric.add(Restrictions.eq("roleMaster.id", userRoleId));
		}
		List<Long> roleBasedCrfsIds = cric.list();
		Criteria cri = getSession().createCriteria(StdSubGroupObservationCrfs.class)
				.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("active", true));
		List<StdSubGroupObservationCrfs> list;
		if (roleBasedCrfsIds.size() > 0) {
			cri.add(Restrictions.in("crf.id", roleBasedCrfsIds));
			list = cri.list();
		} else
			list = new ArrayList<>();
		list.forEach((obj) -> {
			obj.setTreatmentDates(studyTreatmentDatesListByDataId(obj.getId()));
		});
		return list;
	}

	private List<StudyTreatmentDataDates> studyTreatmentDatesListByDataId(Long id) {
		return getSession().createCriteria(StudyTreatmentDataDates.class)
				.add(Restrictions.eq("stdSubGroupObservationCrfs.id", id)).add(Restrictions.eq("activeStatus", true))
				.list();
	}

	@Override
	public StudyAnimal nextAnimalForTreatmentData(Long studyTreatmentDataDatesId, Long stdSubGroupObservationCrfsId,
			Long studyId, Long crfId, String gender, int noOfEntry, String selecteDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
			Date fromDate = sdf.parse(selecteDate);
			Calendar c = Calendar.getInstance();
			c.setTime(fromDate);
			c.add(Calendar.DATE, 1);
			Date toDate = sdf.parse(sdf.format(c.getTime()));

//			SimpleDateFormat dateTimeSecondsFormat = new SimpleDateFormat(environment.getRequiredProperty("dateTimeSecondsFormat"));

			System.out.println(sdf.format(fromDate) + " to " + sdf.format(toDate));
			StdSubGroupObservationCrfs data = stdSubGroupObservationCrfsWithId(stdSubGroupObservationCrfsId);
			StudyTreatmentDataDates date = null;
			if (studyTreatmentDataDatesId != null) {
				date = (StudyTreatmentDataDates) getSession().get(StudyTreatmentDataDates.class,
						studyTreatmentDataDatesId);
			} else {
				date = (StudyTreatmentDataDates) getSession().createCriteria(StudyTreatmentDataDates.class)
						.add(Restrictions.eq("stdSubGroupObservationCrfs.id", data.getId()))
						.add(Restrictions.eq("activeStatus", true)).add(Restrictions.eq("gender", gender))
						.add(Restrictions.eq("accDate", fromDate))
//						.add(Restrictions.between("accDate", fromDate, toDate))
						.uniqueResult();
			}

			Long count = (Long) getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", studyId))
					.add(Restrictions.eq("gender", gender))
					.add(Restrictions.eq("subGropInfo.id", data.getSubGroupInfo().getId()))
					.setProjection(Projections.count("id")).uniqueResult();
			for (int i = 1; i <= date.getNoOfEntry(); i++) {
				if (noOfEntry != 0) {
					i = noOfEntry;
				}
				List<Long> animalIds = getSession().createCriteria(SubjectDataEntryDetails.class)
						.add(Restrictions.eq("observationCrf.id", data.getId()))
						.add(Restrictions.eq("studyTreatmentDataDates.id", date.getId()))
						.add(Restrictions.eq("entryType", "scheduled")).add(Restrictions.eq("gender", gender))
						.add(Restrictions.eq("noOfEntry", i)).setProjection(Projections.property("animal.id")).list();
				if (animalIds.size() < count) {
					Criteria cri = getSession().createCriteria(StudyAnimal.class)
							.add(Restrictions.eq("study.id", studyId))
							.add(Restrictions.eq("subGropInfo.id", data.getSubGroupInfo().getId()))
//							.add(Restrictions.eq("accessionDataEntryStatus", false))
							.add(Restrictions.eq("gender", gender)).setProjection(Projections.min("animalId"));
					if (animalIds.size() > 0)
						cri.add(Restrictions.not(Restrictions.in("id", animalIds)));
					int animalId = 0;
					try {
						animalId = (int) cri.uniqueResult();
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (animalId != 0) {
						StudyAnimal animal = (StudyAnimal) getSession().createCriteria(StudyAnimal.class)
								.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("gender", gender))
								.add(Restrictions.eq("subGropInfo.id", data.getSubGroupInfo().getId()))
								.add(Restrictions.eq("animalId", animalId)).uniqueResult();
						if (animal != null)
							animal.setNoOfEntry(i);
						return animal;
					}
					break;
				}
				if (noOfEntry != 0) {
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public StudyTreatmentDataDates studyTreatmentDataDates(Long stdSubGroupObservationCrfsId, boolean activeStatus,
			Date fromDate, String seletedGender) {
		// TODO Auto-generated method stub
		return (StudyTreatmentDataDates) getSession().createCriteria(StudyTreatmentDataDates.class)
				.add(Restrictions.eq("stdSubGroupObservationCrfs.id", stdSubGroupObservationCrfsId))
				.add(Restrictions.eq("activeStatus", activeStatus)).add(Restrictions.eq("gender", seletedGender))

				.add(Restrictions.eq("accDate", fromDate))
//				.add(Restrictions.between("accDate", fromDate, toDate))
				.uniqueResult();
	}

	@Override
	public List<StudyAnimal> studyAnimal(Long studyId) {
		Criteria cri = getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", studyId));
//		cri.add(Restrictions.eq("gender", gender));
		return cri.list();
	}

	@Override
	public StudyTreatmentDataDates studyTreatmentDataDates(Long studyTreatmentDataDatesId) {
		// TODO Auto-generated method stub
		return (StudyTreatmentDataDates) getSession().get(StudyTreatmentDataDates.class, studyTreatmentDataDatesId);
	}

	@Override
	public List<SubGroupInfo> studySubGroupInfo(Long groupId) {
		return getSession().createCriteria(SubGroupInfo.class).add(Restrictions.eq("group.id", groupId))
				.add(Restrictions.eq("status", "active")).list();
	}

	@Override
	public Long subGrouId(Long groupId) {
		// TODO Auto-generated method stub
		return (Long) getSession().createCriteria(SubGroupInfo.class).add(Restrictions.eq("group.id", groupId))
				.setProjection(Projections.property("id")).uniqueResult();
	}

	@Override
	public List<SubjectDataEntryDetails> unscheduelTreatmentData(StudyMaster sm) {
		// TODO Auto-generated method stub
		List<SubjectDataEntryDetails> list = getSession().createCriteria(SubjectDataEntryDetails.class)
				.add(Restrictions.eq("study.id", sm.getId())).add(Restrictions.eq("entryType", "unscheduled")).list();
		list.forEach((obj) -> {
			Hibernate.initialize(obj.getObservationCrf());
		});
		return list;
	}

	@Override
	public List<StdSubGroupObservationCrfs> stdSubGroupObservationCrfs(Long studyId) {
		// TODO Auto-generated method stub
		List<Long> crfIds = new ArrayList<>();
		List<StdSubGroupObservationCrfs> result = new ArrayList<>();
		List<StdSubGroupObservationCrfs> list = getSession().createCriteria(StdSubGroupObservationCrfs.class)
				.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("active", true)).list();
		for (StdSubGroupObservationCrfs obj : list) {
			if (!crfIds.contains(obj.getCrf().getId())) {
				crfIds.add(obj.getCrf().getId());
				result.add(obj);
			}
		}
		return result;
	}

	@Override
	public StdSubGroupObservationCrfs stdSubGroupObservationCrfsById(Long observationId) {
		// TODO Auto-generated method stub
		StdSubGroupObservationCrfs obj = (StdSubGroupObservationCrfs) getSession().get(StdSubGroupObservationCrfs.class,
				observationId);
		Hibernate.initialize(obj.getCrf().getSections());
		List<CrfSection> secs = obj.getCrf().getSections();
		for (CrfSection sec : secs) {
			Hibernate.initialize(sec.getElement());
		}
		return obj;
	}

	@Override
	public StudyAcclamatizationData studyAcclamatizationData(Long observationId) {
		StudyAcclamatizationData obj = (StudyAcclamatizationData) getSession().get(StudyAcclamatizationData.class,
				observationId);
		Hibernate.initialize(obj.getCrf().getSections());
		List<CrfSection> secs = obj.getCrf().getSections();
		for (CrfSection sec : secs) {
			Hibernate.initialize(sec.getElement());
		}
		return obj;
	}

	@Override
	public List<String> allDaysOfClinicalObservations(Long crfSectionElementId, Long stdSubGroupObservationCrfsId) {
		// TODO Auto-generated method stub
		List<Long> ids = getSession().createCriteria(SubjectDataEntryDetails.class)
				.add(Restrictions.eq("observationCrf.id", stdSubGroupObservationCrfsId))
				.setProjection(Projections.property("id")).list();
		if (ids.size() > 0) {
			List<String> days = getSession().createCriteria(CrfSectionElementData.class)
					.add(Restrictions.in("subjectDataEntryDetails.id", ids))
					.add(Restrictions.eq("", crfSectionElementId))
					.setProjection(Projections.distinct(Projections.property("value"))).list();
			return days;
		} else
			return new ArrayList<>();
	}

	@Override
	public DailyClinicalObservation dailyClinicalObservation(CrfSectionElement dayElement,
			CrfSectionElement valueElement, StdSubGroupObservationCrfs stdSubGroupObservationCrfs,
			List<StudyAnimal> animals) {
		List<String> days = new ArrayList<>();
		List<Long> ids = getSession().createCriteria(SubjectDataEntryDetails.class)
				.add(Restrictions.eq("observationCrf.id", stdSubGroupObservationCrfs.getId()))
				.setProjection(Projections.property("id")).list();
		if (ids.size() > 0) {
			days = getSession().createCriteria(CrfSectionElementData.class)
					.add(Restrictions.in("subjectDataEntryDetails.id", ids))
					.add(Restrictions.eq("element.id", dayElement.getId()))
					.setProjection(Projections.distinct(Projections.property("value"))).list();
		}
//		List<String> days = this.allDaysOfClinicalObservations(dayElement.getId(), stdSubGroupObservationCrfs.getId());
		DailyClinicalObservation dco = new DailyClinicalObservation();
		dco.setStudy(stdSubGroupObservationCrfs.getStudy());
		Set<Integer> allDays = new HashSet<>();
		for (String day : days)
			allDays.add(Integer.parseInt(day));
		dco.setAllDays(allDays);
		Map<Integer, Long> animalOrderMap = new TreeMap<>();
		Map<Long, StudyAnimal> animalMap = new TreeMap<>();
		Map<Long, Map<Integer, String>> eachAnimalData = new TreeMap<>();
		for (StudyAnimal animal : animals) {
			Map<Integer, String> map = new TreeMap<>();
			for (String day : days) {
				System.out.println("animal : " + animal.getId() + "\tday : " + day);
				List<Long> data = getSession().createCriteria(CrfSectionElementData.class)
						.add(Restrictions.eq("animal.id", animal.getId()))
						.add(Restrictions.eq("crf.id", stdSubGroupObservationCrfs.getCrf().getId()))
						.add(Restrictions.in("subjectDataEntryDetails.id", ids))
						.add(Restrictions.eq("element.id", dayElement.getId())).add(Restrictions.eq("value", day))
						.setProjection(Projections.property("subjectDataEntryDetails.id")).list();
				if (data.size() > 0) {
					List<String> value = getSession().createCriteria(CrfSectionElementData.class)
							.add(Restrictions.in("subjectDataEntryDetails.id", data))
							.add(Restrictions.eq("element.id", valueElement.getId()))
							.add(Restrictions.eq("animal.id", animal.getId()))
							.setProjection(Projections.distinct(Projections.property("value"))).list();
					if (value.size() > 0) {
						List<ClinicalCodes> codes = clinicalCodesDao.getClinicalCodes(value);
						StringBuilder sb = new StringBuilder();
						boolean fg = true;
						for (ClinicalCodes code : codes) {
							if (fg) {
								sb.append(code.getClinicalCode());
								fg = false;
							} else
								sb.append(",").append(code.getClinicalCode());
						}
						map.put(Integer.parseInt(day), sb.toString());
					} else
						map.put(Integer.parseInt(day), "");
				} else
					map.put(Integer.parseInt(day), "");
			}
			animalOrderMap.put(animal.getAnimalId(), animal.getId());
			animalMap.put(animal.getId(), animal);
			eachAnimalData.put(animal.getId(), map);
		}
		dco.setAnimalOderMap(animalOrderMap);
		dco.setEachAnimalData(eachAnimalData);
		dco.setAnimalMap(animalMap);
		return dco;
	}

	@Override
	public DailyClinicalObservation acclamatiztionDailyClinicalObservation(CrfSectionElement dayElement,
			CrfSectionElement valueElement, StudyAcclamatizationData studyAcclamatizationData,
			List<StudyAnimal> animals) {
		List<String> days = new ArrayList<>();
		List<Long> ids = getSession().createCriteria(AccessionAnimalsDataEntryDetails.class)
				.add(Restrictions.eq("studyAcclamatizationData.id", studyAcclamatizationData.getId()))
				.setProjection(Projections.property("id")).list();
		if (ids.size() > 0) {
			days = getSession().createCriteria(StudyAccessionCrfSectionElementData.class)
					.add(Restrictions.in("studyAccAnimal.id", ids))
					.add(Restrictions.eq("element.id", dayElement.getId()))
					.setProjection(Projections.distinct(Projections.property("value"))).list();
		}
//		List<String> days = this.allDaysOfClinicalObservations(dayElement.getId(), stdSubGroupObservationCrfs.getId());
		DailyClinicalObservation dco = new DailyClinicalObservation();
		dco.setStudy(studyAcclamatizationData.getStudy());
		Set<Integer> allDays = new HashSet<>();
		for (String day : days)
			allDays.add(Integer.parseInt(day));
		dco.setAllDays(allDays);
		Map<Integer, Long> animalOrderMap = new TreeMap<>();
		Map<Long, StudyAnimal> animalMap = new TreeMap<>();
		Map<Long, Map<Integer, String>> eachAnimalData = new TreeMap<>();
		for (StudyAnimal animal : animals) {
			Map<Integer, String> map = new TreeMap<>();
			for (String day : days) {
				System.out.println("animal : " + animal.getId() + "\tday : " + day);
				List<Long> data = getSession().createCriteria(StudyAccessionCrfSectionElementData.class)
						.add(Restrictions.eq("animal.id", animal.getId()))
						.add(Restrictions.eq("crf.id", studyAcclamatizationData.getCrf().getId()))
						.add(Restrictions.in("studyAccAnimal.id", ids))
						.add(Restrictions.eq("element.id", dayElement.getId())).add(Restrictions.eq("value", day))
						.setProjection(Projections.property("studyAccAnimal.id")).list();
				if (data.size() > 0) {
					List<String> value = getSession().createCriteria(StudyAccessionCrfSectionElementData.class)
							.add(Restrictions.in("studyAccAnimal.id", data))
							.add(Restrictions.eq("element.id", valueElement.getId()))
							.add(Restrictions.eq("animal.id", animal.getId()))
							.setProjection(Projections.distinct(Projections.property("value"))).list();
					if (value.size() > 0) {
						List<ClinicalCodes> codes = clinicalCodesDao.getClinicalCodes(value);
						StringBuilder sb = new StringBuilder();
						boolean fg = true;
						for (ClinicalCodes code : codes) {
							if (fg) {
								sb.append(code.getClinicalCode());
								fg = false;
							} else
								sb.append(",").append(code.getClinicalCode());
						}
						map.put(Integer.parseInt(day), sb.toString());
					} else
						map.put(Integer.parseInt(day), "");
				} else
					map.put(Integer.parseInt(day), "");
			}
			animalOrderMap.put(animal.getSequnceNo(), animal.getId());
			animalMap.put(animal.getId(), animal);
			eachAnimalData.put(animal.getId(), map);
		}
		dco.setAnimalOderMap(animalOrderMap);
		dco.setEachAnimalData(eachAnimalData);
		dco.setAnimalMap(animalMap);
		return dco;
	}

	@Override
	public Map<Integer, ClinicalObservationRecordTimePoint> clinicalObservationRecordTimePoint(
			CrfSectionElement timePointElement, CrfSectionElement observationElement, CrfSectionElement timeHrElement,
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs, List<StudyAnimal> animals) {

		List<StudyTreatmentDataDates> treatmentDates = getSession().createCriteria(StudyTreatmentDataDates.class)
				.add(Restrictions.eq("stdSubGroupObservationCrfs.id", stdSubGroupObservationCrfs.getId())).list();
		Map<Integer, ClinicalObservationRecordTimePoint> mapResult = new HashMap<>();
		for (StudyTreatmentDataDates date : treatmentDates) {
			List<String> timePoints = new ArrayList<>();
			List<Long> ids = getSession().createCriteria(SubjectDataEntryDetails.class)
					.add(Restrictions.eq("studyTreatmentDataDates.id", date.getId()))
					.setProjection(Projections.property("id")).list();
			if (ids.size() > 0) {
				timePoints = getSession().createCriteria(CrfSectionElementData.class)
						.add(Restrictions.in("subjectDataEntryDetails.id", ids))
						.add(Restrictions.eq("element.id", timePointElement.getId()))
						.setProjection(Projections.distinct(Projections.property("value"))).list();
			}
			if (timePoints.size() > 0) {
				ClinicalObservationRecordTimePoint obj = new ClinicalObservationRecordTimePoint();
				obj.setStudy(stdSubGroupObservationCrfs.getStudy());
				Set<String> allTimePoints = new HashSet<>();
				for (String timePoint : timePoints)
					allTimePoints.add(timePoint);
				obj.setAllTimePoitns(allTimePoints);
				Map<Integer, Long> animalOderMap = new TreeMap<>();
				Map<Long, Map<String, ClinicalObservationRecordTimePointElements>> eachAnimalData = new TreeMap<>();
				Map<Long, StudyAnimal> animalMap = new TreeMap<>();
				for (StudyAnimal animal : animals) {
					Map<String, ClinicalObservationRecordTimePointElements> map = new TreeMap<>();
					for (String timePoint : timePoints) {
						System.out.println("animal : " + animal.getId() + "\tday : " + timePoint);
						List<Long> data = getSession().createCriteria(CrfSectionElementData.class)
								.add(Restrictions.eq("animal.id", animal.getId()))
								.add(Restrictions.eq("crf.id", stdSubGroupObservationCrfs.getCrf().getId()))
								.add(Restrictions.in("subjectDataEntryDetails.id", ids))
								.add(Restrictions.eq("element.id", timePointElement.getId()))
								.add(Restrictions.eq("value", timePoint))
								.setProjection(Projections.property("subjectDataEntryDetails.id")).list();
						if (data.size() > 0) {
							String observationValue = "";
							List<String> value = getSession().createCriteria(CrfSectionElementData.class)
									.add(Restrictions.in("subjectDataEntryDetails.id", data))
									.add(Restrictions.eq("element.id", observationElement.getId()))
									.add(Restrictions.eq("animal.id", animal.getId()))
									.setProjection(Projections.distinct(Projections.property("value"))).list();
							if (value.size() > 0) {
								List<ClinicalCodes> codes = clinicalCodesDao.getClinicalCodes(value);
								StringBuilder sb = new StringBuilder();
								boolean fg = true;
								for (ClinicalCodes code : codes) {
									if (fg) {
										sb.append(code.getClinicalCode());
										fg = false;
									} else
										sb.append(",").append(code.getClinicalCode());
								}
								observationValue = sb.toString();
							}
							String timeInHr = "";
							value = getSession().createCriteria(CrfSectionElementData.class)
									.add(Restrictions.in("subjectDataEntryDetails.id", data))
									.add(Restrictions.eq("element.id", timeHrElement.getId()))
									.add(Restrictions.eq("animal.id", animal.getId()))
									.setProjection(Projections.distinct(Projections.property("value"))).list();
							if (value.size() > 0) {
								StringBuilder sb = new StringBuilder();
								boolean fg = true;
								for (String va : value) {
									if (fg) {
										sb.append(va);
										fg = false;
									} else
										sb.append(",").append(va);
								}
								timeInHr = sb.toString();
							}
							map.put(timePoint,
									new ClinicalObservationRecordTimePointElements(observationValue, timeInHr));
						} else {
							map.put(timePoint, new ClinicalObservationRecordTimePointElements("", ""));
						}

					}
					animalOderMap.put(animal.getAnimalId(), animal.getId());
					animalMap.put(animal.getId(), animal);
					eachAnimalData.put(animal.getId(), map);
				}
				obj.setAnimalOderMap(animalOderMap);
				obj.setAnimalMap(animalMap);
				obj.setEachAnimalData(eachAnimalData);
				Date startDate = date.getStudy().getTreatmentStarDate();
				// Convert java.util.Date to java.time.LocalDate
				LocalDate localDate1 = startDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
				LocalDate localDate2 = date.getAccDate().toInstant().atZone(java.time.ZoneId.systemDefault())
						.toLocalDate();
				Period period = Period.between(localDate1, localDate2);
				mapResult.put(period.getDays(), obj);
			}
		}
		return mapResult;
	}

	@Override
	public List<RecordforMortalityMorbidity> recordforMortalityMorbidity(CrfSectionElement mortalityElement,
			CrfSectionElement morbidityElement, CrfSectionElement mortalitySelect1Element,
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs) {
		// TODO Auto-generated method stub
		List<Long> ids = getSession().createCriteria(StdSubGroupObservationCrfs.class)
				.add(Restrictions.eq("crf.id", stdSubGroupObservationCrfs.getCrf().getId()))
				.setProjection(Projections.property("id")).list();
		List<SubjectDataEntryDetails> listb = getSession().createCriteria(SubjectDataEntryDetails.class)
				.add(Restrictions.in("observationCrf.id", ids)).list();

		SimpleDateFormat dateOnly = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		SimpleDateFormat timeOnly = new SimpleDateFormat("HH:mm");
		Map<String, List<SubjectDataEntryDetails>> map = new HashMap<>();
		Map<String, List<Long>> mapIds = new HashMap<>();
		Map<Long, SubjectDataEntryDetails> subjectDataEntryDetailsIdsMap = new HashMap<>();
		for (SubjectDataEntryDetails sdd : listb) {
			String date = dateOnly.format(sdd.getCreatedOn());
			List<SubjectDataEntryDetails> temp1 = map.get(date);
			List<Long> tempids = mapIds.get(date);
			if (temp1 == null) {
				temp1 = new ArrayList<>();
				tempids = new ArrayList<>();
			}
			temp1.add(sdd);
			tempids.add(sdd.getId());
			map.put(date, temp1);
			mapIds.put(date, tempids);
			subjectDataEntryDetailsIdsMap.put(sdd.getId(), sdd);
		}
		List<RecordforMortalityMorbidity> result3 = new ArrayList<>();
		if (mapIds.size() > 0) {
			for (Map.Entry<String, List<Long>> tempIds : mapIds.entrySet()) {
				System.out.println(tempIds.getKey() + "--" + tempIds.getValue().size());
				List<CrfSectionElementData> result = getSession().createCriteria(CrfSectionElementData.class)
						.add(Restrictions.in("subjectDataEntryDetails.id", tempIds.getValue())).list();
				Map<Long, List<CrfSectionElementData>> map2 = new HashMap<>();
				for (CrfSectionElementData data : result) {
					List<CrfSectionElementData> list2 = map2.get(data.getSubjectDataEntryDetails().getId());
					if (list2 == null)
						list2 = new ArrayList<>();
					list2.add(data);
					map2.put(data.getSubjectDataEntryDetails().getId(), list2);
				}

				System.out.println(mortalitySelect1Element.getId());
				System.out.println(mortalityElement.getId());
				System.out.println(morbidityElement.getId());
				while (map2.size() > 0) {
					RecordforMortalityMorbidity o = new RecordforMortalityMorbidity();
					o.setDate(tempIds.getKey());
					Map<Long, List<CrfSectionElementData>> tmepMap = new HashMap<>();
					for (Map.Entry<Long, List<CrfSectionElementData>> map3 : map2.entrySet()) {
						List<CrfSectionElementData> list3 = map3.getValue();
						boolean moring = false;
						boolean evening = false;
						boolean afternoon = false;
						for (CrfSectionElementData data : list3) {
							System.out.println(data.toString());
							if (data.getElement().getId().equals(mortalitySelect1Element.getId())) {
								if (data.getValue().equals("Morning")) {
									moring = true;
								} else if (data.getValue().equals("Afternoon")) {
									afternoon = true;
								} else if (data.getValue().equals("Evening")) {
									evening = true;
								}
							}
						}

						if (moring || evening || afternoon) {
							boolean flg = false;
							if (moring && !o.getMorningMortality().equals("")) {
								flg = true;
							} else if ((evening || afternoon) && !o.getEveninngMortality().equals("")) {
								flg = true;
							}
							if (flg) {
								tmepMap.put(map3.getKey(), list3);
							} else {
								for (CrfSectionElementData data : list3) {
									System.out.println(data.getElement().getId() + " - " + mortalityElement.getId()
											+ " - " + morbidityElement.getId());
									if (data.getElement().getId() == mortalityElement.getId()
											|| data.getElement().getId() == morbidityElement.getId()) {
										if (moring) {
											boolean temp = false;
											if (data.getElement().getId() == mortalityElement.getId()) {
												if (o.getMorningMortality().equals("")) {
													o.setMorningMortality(data.getValue());
													temp = true;
												}
											} else if (data.getElement().getId() == morbidityElement.getId()) {
												if (o.getMorningMorblility().equals("")) {
													o.setMorningMorblility(data.getValue());
													temp = true;
												}
											}
											if (temp) {
												SubjectDataEntryDetails sdd = subjectDataEntryDetailsIdsMap
														.get(map3.getKey());
												o.setMorningTime(timeOnly.format(sdd.getCreatedOn()));
												o.setMorningUserName(sdd.getEntredby().getUsername());
											}
										} else if (evening || afternoon) {
											boolean temp = false;
											if (data.getElement().getId() == mortalityElement.getId()) {
												if (o.getEveninngMortality().equals("")) {
													o.setEveninngMortality(data.getValue());
													temp = true;
												}
											} else if (data.getElement().getId() == morbidityElement.getId()) {
												if (o.getEveninngMorblility().equals("")) {
													o.setEveninngMorblility(data.getValue());
													temp = true;
												}
											}
											if (temp) {
												SubjectDataEntryDetails sdd = subjectDataEntryDetailsIdsMap
														.get(map3.getKey());
												o.setEveninngTime(timeOnly.format(sdd.getCreatedOn()));
												o.setEveninngUserName(sdd.getEntredby().getUsername());
											}
										}
									}
								}
							}
						}
					}
					result3.add(o);
					map2 = new HashMap<>(tmepMap);
				}
			}
		}
		return result3;
	}

	@Override
	public List<RecordforMortalityMorbidity> acclamatizationrecordforMortalityMorbidity(
			CrfSectionElement mortalityElement, CrfSectionElement morbidityElement,
			CrfSectionElement mortalitySelect1Element, StudyAcclamatizationData studyAcclamatizationData) {
		List<Long> ids = getSession().createCriteria(StudyAcclamatizationData.class)
				.add(Restrictions.eq("crf.id", studyAcclamatizationData.getCrf().getId()))
				.setProjection(Projections.property("id")).list();
		List<AccessionAnimalsDataEntryDetails> listb = getSession()
				.createCriteria(AccessionAnimalsDataEntryDetails.class)
				.add(Restrictions.in("studyAcclamatizationData.id", ids)).list();

		SimpleDateFormat dateOnly = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		SimpleDateFormat timeOnly = new SimpleDateFormat("HH:mm");
		Map<String, List<AccessionAnimalsDataEntryDetails>> map = new HashMap<>();
		Map<String, List<Long>> mapIds = new HashMap<>();
		Map<Long, AccessionAnimalsDataEntryDetails> subjectDataEntryDetailsIdsMap = new HashMap<>();
		for (AccessionAnimalsDataEntryDetails sdd : listb) {
			String date = dateOnly.format(sdd.getCreatedOn());
			List<AccessionAnimalsDataEntryDetails> temp1 = map.get(date);
			List<Long> tempids = mapIds.get(date);
			if (temp1 == null) {
				temp1 = new ArrayList<>();
				tempids = new ArrayList<>();
			}
			temp1.add(sdd);
			tempids.add(sdd.getId());
			map.put(date, temp1);
			mapIds.put(date, tempids);
			subjectDataEntryDetailsIdsMap.put(sdd.getId(), sdd);
		}
		List<RecordforMortalityMorbidity> result3 = new ArrayList<>();
		if (mapIds.size() > 0) {
			for (Map.Entry<String, List<Long>> tempIds : mapIds.entrySet()) {
				System.out.println(tempIds.getKey() + "--" + tempIds.getValue().size());
				List<StudyAccessionCrfSectionElementData> result = getSession()
						.createCriteria(StudyAccessionCrfSectionElementData.class)
						.add(Restrictions.in("studyAccAnimal.id", tempIds.getValue())).list();
				Map<Long, List<StudyAccessionCrfSectionElementData>> map2 = new HashMap<>();
				for (StudyAccessionCrfSectionElementData data : result) {
					List<StudyAccessionCrfSectionElementData> list2 = map2.get(data.getStudyAccAnimal().getId());
					if (list2 == null)
						list2 = new ArrayList<>();
					list2.add(data);
					map2.put(data.getStudyAccAnimal().getId(), list2);
				}

				System.out.println(mortalitySelect1Element.getId());
				System.out.println(mortalityElement.getId());
				System.out.println(morbidityElement.getId());
				while (map2.size() > 0) {
					RecordforMortalityMorbidity o = new RecordforMortalityMorbidity();
					o.setDate(tempIds.getKey());
					Map<Long, List<StudyAccessionCrfSectionElementData>> tmepMap = new HashMap<>();
					for (Map.Entry<Long, List<StudyAccessionCrfSectionElementData>> map3 : map2.entrySet()) {
						List<StudyAccessionCrfSectionElementData> list3 = map3.getValue();
						boolean moring = false;
						boolean evening = false;
						boolean afternoon = false;
						for (StudyAccessionCrfSectionElementData data : list3) {
							System.out.println(data.toString());
							if (data.getElement().getId().equals(mortalitySelect1Element.getId())) {
								if (data.getValue().equals("Morning")) {
									moring = true;
								} else if (data.getValue().equals("Afternoon")) {
									afternoon = true;
								} else if (data.getValue().equals("Evening")) {
									evening = true;
								}
							}
						}

						if (moring || evening || afternoon) {
							boolean flg = false;
							if (moring && !o.getMorningMortality().equals("")) {
								flg = true;
							} else if ((evening || afternoon) && !o.getEveninngMortality().equals("")) {
								flg = true;
							}
							if (flg) {
								tmepMap.put(map3.getKey(), list3);
							} else {
								for (StudyAccessionCrfSectionElementData data : list3) {
									System.out.println(data.getElement().getId() + " - " + mortalityElement.getId()
											+ " - " + morbidityElement.getId());
									if (data.getElement().getId() == mortalityElement.getId()
											|| data.getElement().getId() == morbidityElement.getId()) {
										if (moring) {
											boolean temp = false;
											if (data.getElement().getId() == mortalityElement.getId()) {
												if (o.getMorningMortality().equals("")) {
													o.setMorningMortality(data.getValue());
													temp = true;
												}
											} else if (data.getElement().getId() == morbidityElement.getId()) {
												if (o.getMorningMorblility().equals("")) {
													o.setMorningMorblility(data.getValue());
													temp = true;
												}
											}
											if (temp) {
												AccessionAnimalsDataEntryDetails sdd = subjectDataEntryDetailsIdsMap
														.get(map3.getKey());
												o.setMorningTime(timeOnly.format(sdd.getCreatedOn()));
												o.setMorningUserName(sdd.getCreatedBy());
											}
										} else if (evening || afternoon) {
											boolean temp = false;
											if (data.getElement().getId() == mortalityElement.getId()) {
												if (o.getEveninngMortality().equals("")) {
													o.setEveninngMortality(data.getValue());
													temp = true;
												}
											} else if (data.getElement().getId() == morbidityElement.getId()) {
												if (o.getEveninngMorblility().equals("")) {
													o.setEveninngMorblility(data.getValue());
													temp = true;
												}
											}
											if (temp) {
												AccessionAnimalsDataEntryDetails sdd = subjectDataEntryDetailsIdsMap
														.get(map3.getKey());
												o.setEveninngTime(timeOnly.format(sdd.getCreatedOn()));
												o.setEveninngUserName(sdd.getCreatedBy());
											}
										}
									}
								}
							}
						}
					}
					result3.add(o);
					map2 = new HashMap<>(tmepMap);
				}
			}
		}
		return result3;
	}

	@Override
	public DetailedClinicalObservations detailedClinicalObservation(CrfSectionElement valueElement,
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs, List<StudyAnimal> animals) {
		List<Long> animalIds = new ArrayList<>();
		Map<Long, StudyAnimal> animalMap = new HashMap<>();
		for (StudyAnimal animal : animals) {
			animalMap.put(animal.getId(), animal);
			animalIds.add(animal.getId());
		}

		Map<Long, List<SubjectDataEntryDetails>> animalWise = new HashMap<>();
		List<SubjectDataEntryDetails> list = getSession().createCriteria(SubjectDataEntryDetails.class)
				.add(Restrictions.in("animal.id", animalIds)).list();
		for (SubjectDataEntryDetails sd : list) {
			List<SubjectDataEntryDetails> list1 = animalWise.get(sd.getAnimal().getId());
			if (list1 == null)
				list1 = new ArrayList<>();
			list1.add(sd);
			animalWise.put(sd.getAnimal().getId(), list1);
		}

		DetailedClinicalObservations dco = new DetailedClinicalObservations();
		dco.setStudy(stdSubGroupObservationCrfs.getStudy());
		dco.setType("Treatment");
		List<DetailedClinicalObservationsAnimlas> eachAnimal = new ArrayList<>();
		for (Map.Entry<Long, List<SubjectDataEntryDetails>> mp : animalWise.entrySet()) {
			DetailedClinicalObservationsAnimlas dcoa = new DetailedClinicalObservationsAnimlas();
			dcoa.setType(dco.getType());
			dcoa.setAnimal(animalMap.get(mp.getKey()));
			List<Long> ids = new ArrayList<>();
			List<SubjectDataEntryDetails> li = mp.getValue();
			for (SubjectDataEntryDetails l : li) {
				ids.add(l.getId());
			}

			List<String> value = getSession().createCriteria(CrfSectionElementData.class)
					.add(Restrictions.in("subjectDataEntryDetails.id", ids))
					.add(Restrictions.eq("element.id", valueElement.getId()))
					.add(Restrictions.eq("animal.id", mp.getKey()))
					.setProjection(Projections.distinct(Projections.property("value"))).list();
			if (value.size() > 0) {
				List<ClinicalCodes> codes = clinicalCodesDao.getClinicalCodes(value);
				StringBuilder sb = new StringBuilder();
				boolean fg = true;
				for (ClinicalCodes code : codes) {
					if (fg) {
						sb.append(code.getClinicalCode());
						fg = false;
					} else
						sb.append(",").append(code.getClinicalCode());
				}
				dcoa.setValue(sb.toString());
			}
			eachAnimal.add(dcoa);
		}
		Collections.sort(eachAnimal);
		int count = 1;
		for (DetailedClinicalObservationsAnimlas dcoa : eachAnimal) {
			dcoa.setSno(count++);
		}
		dco.setEachAnimal(eachAnimal);
		return dco;
	}

	@Override
	public DetailedClinicalObservations acclamatizationDetailedClinicalObservation(CrfSectionElement valueElement,
			StudyAcclamatizationData studyAcclamatizationData, List<StudyAnimal> animals) {
		List<Long> animalIds = new ArrayList<>();
		Map<Long, StudyAnimal> animalMap = new HashMap<>();
		for (StudyAnimal animal : animals) {
			animalMap.put(animal.getId(), animal);
			animalIds.add(animal.getId());
		}

		Map<Long, List<AccessionAnimalsDataEntryDetails>> animalWise = new HashMap<>();
		List<AccessionAnimalsDataEntryDetails> list = getSession()
				.createCriteria(AccessionAnimalsDataEntryDetails.class).add(Restrictions.in("animal.id", animalIds))
				.list();
		for (AccessionAnimalsDataEntryDetails sd : list) {
			List<AccessionAnimalsDataEntryDetails> list1 = animalWise.get(sd.getAnimal().getId());
			if (list1 == null)
				list1 = new ArrayList<>();
			list1.add(sd);
			animalWise.put(sd.getAnimal().getId(), list1);
		}

		DetailedClinicalObservations dco = new DetailedClinicalObservations();
		dco.setStudy(studyAcclamatizationData.getStudy());
		dco.setType("Acclamatization");
		List<DetailedClinicalObservationsAnimlas> eachAnimal = new ArrayList<>();
		for (Map.Entry<Long, List<AccessionAnimalsDataEntryDetails>> mp : animalWise.entrySet()) {
			DetailedClinicalObservationsAnimlas dcoa = new DetailedClinicalObservationsAnimlas();
			dcoa.setType(dco.getType());
			dcoa.setAnimal(animalMap.get(mp.getKey()));
			List<Long> ids = new ArrayList<>();
			List<AccessionAnimalsDataEntryDetails> li = mp.getValue();
			for (AccessionAnimalsDataEntryDetails l : li) {
				ids.add(l.getId());
			}

			List<String> value = getSession().createCriteria(StudyAccessionCrfSectionElementData.class)
					.add(Restrictions.in("studyAccAnimal.id", ids))
					.add(Restrictions.eq("element.id", valueElement.getId()))
					.add(Restrictions.eq("animal.id", mp.getKey()))
					.setProjection(Projections.distinct(Projections.property("value"))).list();
			if (value.size() > 0) {
				List<ClinicalCodes> codes = clinicalCodesDao.getClinicalCodes(value);
				StringBuilder sb = new StringBuilder();
				boolean fg = true;
				for (ClinicalCodes code : codes) {
					if (fg) {
						sb.append(code.getClinicalCode());
						fg = false;
					} else
						sb.append(",").append(code.getClinicalCode());
				}
				dcoa.setValue(sb.toString());
			}
			eachAnimal.add(dcoa);
		}
		Collections.sort(eachAnimal);
		int count = 1;
		for (DetailedClinicalObservationsAnimlas dcoa : eachAnimal) {
			dcoa.setSno(count++);
		}
		dco.setEachAnimal(eachAnimal);
		return dco;
	}

	@Override
	public OphthalmologicalExamination acclamatizationOphthalmologicalExamination(CrfSectionElement leftEyeElement,
			CrfSectionElement rigthEyeElement, StudyAcclamatizationData studyAcclamatizationData,
			List<StudyAnimal> animals) {
		List<Long> animalIds = new ArrayList<>();
		Map<Long, StudyAnimal> animalMap = new HashMap<>();
		for (StudyAnimal animal : animals) {
			animalMap.put(animal.getId(), animal);
			animalIds.add(animal.getId());
		}

		Map<Long, List<AccessionAnimalsDataEntryDetails>> animalWise = new HashMap<>();
		List<AccessionAnimalsDataEntryDetails> list = getSession()
				.createCriteria(AccessionAnimalsDataEntryDetails.class).add(Restrictions.in("animal.id", animalIds))
				.list();
		for (AccessionAnimalsDataEntryDetails sd : list) {
			List<AccessionAnimalsDataEntryDetails> list1 = animalWise.get(sd.getAnimal().getId());
			if (list1 == null)
				list1 = new ArrayList<>();
			list1.add(sd);
			animalWise.put(sd.getAnimal().getId(), list1);
		}

		OphthalmologicalExamination dco = new OphthalmologicalExamination();
		dco.setStudy(studyAcclamatizationData.getStudy());
		dco.setType("Acclamatization");
		List<OphthalmologicalExaminationAnimlas> eachAnimal = new ArrayList<>();
		for (Map.Entry<Long, List<AccessionAnimalsDataEntryDetails>> mp : animalWise.entrySet()) {
			OphthalmologicalExaminationAnimlas dcoa = new OphthalmologicalExaminationAnimlas();
			dcoa.setType(dco.getType());
			dcoa.setAnimal(animalMap.get(mp.getKey()));
			List<Long> ids = new ArrayList<>();
			List<AccessionAnimalsDataEntryDetails> li = mp.getValue();
			for (AccessionAnimalsDataEntryDetails l : li) {
				ids.add(l.getId());
			}

			List<String> value = getSession().createCriteria(StudyAccessionCrfSectionElementData.class)
					.add(Restrictions.in("studyAccAnimal.id", ids))
					.add(Restrictions.eq("element.id", leftEyeElement.getId()))
					.add(Restrictions.eq("animal.id", mp.getKey()))
					.setProjection(Projections.distinct(Projections.property("value"))).list();
			if (value.size() > 0) {
				List<ClinicalCodes> codes = clinicalCodesDao.getClinicalCodes(value);
				StringBuilder sb = new StringBuilder();
				boolean fg = true;
				for (ClinicalCodes code : codes) {
					if (fg) {
						sb.append(code.getClinicalCode());
						fg = false;
					} else
						sb.append(",").append(code.getClinicalCode());
				}
				dcoa.setLeftEye(sb.toString());
			}

			value = getSession().createCriteria(StudyAccessionCrfSectionElementData.class)
					.add(Restrictions.in("studyAccAnimal.id", ids))
					.add(Restrictions.eq("element.id", rigthEyeElement.getId()))
					.add(Restrictions.eq("animal.id", mp.getKey()))
					.setProjection(Projections.distinct(Projections.property("value"))).list();
			if (value.size() > 0) {
				List<ClinicalCodes> codes = clinicalCodesDao.getClinicalCodes(value);
				StringBuilder sb = new StringBuilder();
				boolean fg = true;
				for (ClinicalCodes code : codes) {
					if (fg) {
						sb.append(code.getClinicalCode());
						fg = false;
					} else
						sb.append(",").append(code.getClinicalCode());
				}
				dcoa.setRightEye(sb.toString());
			}
			eachAnimal.add(dcoa);
		}
		Collections.sort(eachAnimal);
		int count = 1;
		for (OphthalmologicalExaminationAnimlas dcoa : eachAnimal) {
			dcoa.setSno(count++);
		}
		dco.setEachAnimal(eachAnimal);
		return dco;
	}

	@Override
	public OphthalmologicalExamination ophthalmologicalExamination(CrfSectionElement leftEyeElement,
			CrfSectionElement rigthEyeElement, StdSubGroupObservationCrfs stdSubGroupObservationCrfs,
			List<StudyAnimal> animals) {
		List<Long> animalIds = new ArrayList<>();
		Map<Long, StudyAnimal> animalMap = new HashMap<>();
		for (StudyAnimal animal : animals) {
			animalMap.put(animal.getId(), animal);
			animalIds.add(animal.getId());
		}

		Map<Long, List<SubjectDataEntryDetails>> animalWise = new HashMap<>();
		List<SubjectDataEntryDetails> list = getSession().createCriteria(SubjectDataEntryDetails.class)
				.add(Restrictions.in("animal.id", animalIds)).list();
		for (SubjectDataEntryDetails sd : list) {
			List<SubjectDataEntryDetails> list1 = animalWise.get(sd.getAnimal().getId());
			if (list1 == null)
				list1 = new ArrayList<>();
			list1.add(sd);
			animalWise.put(sd.getAnimal().getId(), list1);
		}

		OphthalmologicalExamination dco = new OphthalmologicalExamination();
		dco.setStudy(stdSubGroupObservationCrfs.getStudy());
		dco.setType("Treatment");
		List<OphthalmologicalExaminationAnimlas> eachAnimal = new ArrayList<>();
		for (Map.Entry<Long, List<SubjectDataEntryDetails>> mp : animalWise.entrySet()) {
			OphthalmologicalExaminationAnimlas dcoa = new OphthalmologicalExaminationAnimlas();
			dcoa.setType(dco.getType());
			dcoa.setAnimal(animalMap.get(mp.getKey()));
			List<Long> ids = new ArrayList<>();
			List<SubjectDataEntryDetails> li = mp.getValue();
			for (SubjectDataEntryDetails l : li) {
				ids.add(l.getId());
			}

			List<String> value = getSession().createCriteria(CrfSectionElementData.class)
					.add(Restrictions.in("subjectDataEntryDetails.id", ids))
					.add(Restrictions.eq("element.id", leftEyeElement.getId()))
					.add(Restrictions.eq("animal.id", mp.getKey()))
					.setProjection(Projections.distinct(Projections.property("value"))).list();
			if (value.size() > 0) {
				List<ClinicalCodes> codes = clinicalCodesDao.getClinicalCodes(value);
				StringBuilder sb = new StringBuilder();
				boolean fg = true;
				for (ClinicalCodes code : codes) {
					if (fg) {
						sb.append(code.getClinicalCode());
						fg = false;
					} else
						sb.append(",").append(code.getClinicalCode());
				}
				dcoa.setLeftEye(sb.toString());
			}
			value = getSession().createCriteria(CrfSectionElementData.class)
					.add(Restrictions.in("subjectDataEntryDetails.id", ids))
					.add(Restrictions.eq("element.id", rigthEyeElement.getId()))
					.add(Restrictions.eq("animal.id", mp.getKey()))
					.setProjection(Projections.distinct(Projections.property("value"))).list();
			if (value.size() > 0) {
				List<ClinicalCodes> codes = clinicalCodesDao.getClinicalCodes(value);
				StringBuilder sb = new StringBuilder();
				boolean fg = true;
				for (ClinicalCodes code : codes) {
					if (fg) {
						sb.append(code.getClinicalCode());
						fg = false;
					} else
						sb.append(",").append(code.getClinicalCode());
				}
				dcoa.setRightEye(sb.toString());
			}
			eachAnimal.add(dcoa);
		}
		Collections.sort(eachAnimal);
		int count = 1;
		for (OphthalmologicalExaminationAnimlas dcoa : eachAnimal) {
			dcoa.setSno(count++);
		}
		dco.setEachAnimal(eachAnimal);
		return dco;
	}

	@Override
	public List<NeurobehavioralObservationsofIndividual> neurobehavioralObservationsofIndividual(
			CrfSectionElement posture, CrfSectionElement convulsion, CrfSectionElement handiling,
			CrfSectionElement easeOfRemove, CrfSectionElement palpebral, CrfSectionElement lacrimation,
			CrfSectionElement eyeExamination, CrfSectionElement piloerection, CrfSectionElement salivation,
			CrfSectionElement mobility, CrfSectionElement gait, CrfSectionElement respiration,
			CrfSectionElement arousal, CrfSectionElement clonicMovement, CrfSectionElement tonicMovement,
			CrfSectionElement stereotypy, CrfSectionElement bizzareBehaviour, CrfSectionElement vocalisationNo,
			CrfSectionElement noofRears, CrfSectionElement urinePoolsNo, CrfSectionElement faecalBolusNo,
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs, List<StudyAnimal> animals) {
		List<NeurobehavioralObservationsofIndividual> result = new ArrayList<>();
		List<SubGroupInfo> groups = getSession().createCriteria(SubGroupInfo.class)
				.add(Restrictions.eq("study.id", stdSubGroupObservationCrfs.getStudy().getId())).list();
		for (SubGroupInfo sgroup : groups) {
			NeurobehavioralObservationsofIndividual obj = new NeurobehavioralObservationsofIndividual();
			obj.setStudy(stdSubGroupObservationCrfs.getStudy());
			obj.setType("Treatment");
			obj.setGroupNumber(sgroup.getSubGroupNo());
			List<StudyAnimal> groupAnimals = getSession().createCriteria(StudyAnimal.class)
					.add(Restrictions.eq("subGropInfo.id", sgroup.getId())).list();

			Set<Integer> animalNos = new HashSet<>();
			List<Long> animalIds = new ArrayList<>();
			Map<Long, StudyAnimal> animalMap = new HashMap<>();
			for (StudyAnimal animal : groupAnimals) {
				animalNos.add(animal.getAnimalId());
				animalMap.put(animal.getId(), animal);
				animalIds.add(animal.getId());
			}
			obj.setAnimalNumbers(animalNos);

			List<CrfSection> sections = stdSubGroupObservationCrfs.getCrf().getSections();
			Map<String, CrfSection> eachSection = new HashMap<>();
			for (CrfSection section : sections) {
				eachSection.put(section.getName(), section);
			}

			Map<Integer, NeurobehavioralObservationsofIndividualAnimal> mp = new TreeMap<>();
			mp.put(1, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, posture, "Posture"));
			mp.put(2, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, convulsion, "Convulsions"));
			obj.setHomeCageObservations(mp);

			mp = new TreeMap<>();
			mp.put(1, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, easeOfRemove,
					"Ease Of Removeal From the Cage"));
			mp.put(2,
					getneurobehavioralObservationsofIndividualAnimal(groupAnimals, handiling, "Handiling Reactivity"));
			mp.put(3, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, palpebral, "Palpebral Closure"));
			mp.put(4, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, lacrimation, "Lacrimation"));
			mp.put(5,
					getneurobehavioralObservationsofIndividualAnimal(groupAnimals, eyeExamination, "Eye Examination"));
			mp.put(6, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, piloerection, "Piloerection"));
			mp.put(7, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, salivation, "Salivation"));
			obj.setHandlingObservations(mp);

			mp = new TreeMap<>();
			mp.put(1, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, gait, "Gait"));
			mp.put(2, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, mobility, "Mobility"));
			mp.put(3, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, arousal, "Arousal"));
			mp.put(4, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, respiration, "Respiration"));
			mp.put(5, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, tonicMovement, "Tonic Movement"));
			mp.put(6,
					getneurobehavioralObservationsofIndividualAnimal(groupAnimals, clonicMovement, "Clonic Movement"));
			mp.put(7, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, stereotypy, "Stereotypy"));
			mp.put(8, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, bizzareBehaviour,
					"Bizzare Behaviour"));
			mp.put(9, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, noofRears, "N° of Rears"));
			mp.put(10, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, vocalisationNo,
					"Vocalisation (N°)"));
			mp.put(11, getneurobehavioralObservationsofIndividualAnimal(groupAnimals, urinePoolsNo, "UrinePools (N°)"));
			mp.put(12,
					getneurobehavioralObservationsofIndividualAnimal(groupAnimals, faecalBolusNo, "FaecalBolus (N°)"));
			obj.setOpenFiledObservations(mp);

			result.add(obj);
		}

		return result;
	}

	@Override
	public List<SensoryReactivityOfIndividual> sensoryReactivityOfIndividual(CrfSectionElement approachResponse,
			CrfSectionElement touchResponse, CrfSectionElement clickResponse, CrfSectionElement tailPinchResponse,
			CrfSectionElement pupilResponse, CrfSectionElement airRightingReflex,
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs, List<StudyAnimal> animals) {
		List<SensoryReactivityOfIndividual> result = new ArrayList<>();
		List<SubGroupInfo> groups = getSession().createCriteria(SubGroupInfo.class)
				.add(Restrictions.eq("study.id", stdSubGroupObservationCrfs.getStudy().getId())).list();
		for (SubGroupInfo sgroup : groups) {
			SensoryReactivityOfIndividual obj = new SensoryReactivityOfIndividual();
			obj.setStudy(stdSubGroupObservationCrfs.getStudy());
			obj.setType("Treatment");
			obj.setGroupNumber(sgroup.getSubGroupNo());
			List<StudyAnimal> groupAnimals = getSession().createCriteria(StudyAnimal.class)
					.add(Restrictions.eq("subGropInfo.id", sgroup.getId())).list();

			Set<Integer> animalNos = new HashSet<>();
			List<Long> animalIds = new ArrayList<>();
			Map<Long, StudyAnimal> animalMap = new HashMap<>();
			for (StudyAnimal animal : groupAnimals) {
				animalNos.add(animal.getAnimalId());
				animalMap.put(animal.getId(), animal);
				animalIds.add(animal.getId());
			}
			obj.setAnimalNumbers(animalNos);

			List<CrfSection> sections = stdSubGroupObservationCrfs.getCrf().getSections();
			Map<String, CrfSection> eachSection = new HashMap<>();
			for (CrfSection section : sections) {
				eachSection.put(section.getName(), section);
			}

			Map<Integer, SensoryReactivityOfIndividualData> mp = new TreeMap<>();
			SensoryReactivityOfIndividualData chielOdbj = new SensoryReactivityOfIndividualData();
			Map<Integer, String> animalvalue = new HashMap<>();
			for (StudyAnimal animal : groupAnimals) {
				chielOdbj.setApproachResponse(getSectionElemntData(animal, approachResponse));
				chielOdbj.setTouchResponse(getSectionElemntData(animal, touchResponse));
				chielOdbj.setClickResponse(getSectionElemntData(animal, clickResponse));
				chielOdbj.setTailPinchResponse(getSectionElemntData(animal, tailPinchResponse));
				chielOdbj.setPupilResponse(getSectionElemntData(animal, pupilResponse));
				chielOdbj.setAirRightingReflex(getSectionElemntData(animal, airRightingReflex));
				mp.put(animal.getAnimalId(), chielOdbj);
			}
			obj.setData(mp);
			result.add(obj);
		}

		return result;
	}

	private String getSectionElemntData(StudyAnimal animal, CrfSectionElement ele) {
		if (ele != null) {
			List<String> value = getSession().createCriteria(CrfSectionElementData.class)
					.add(Restrictions.eq("animal.id", animal.getId()))
					.add(Restrictions.eq("element.id", ele.getId()))
					.setProjection(Projections.distinct(Projections.property("value"))).list();
			if (value.size() > 0) {
				List<ClinicalCodes> codes = clinicalCodesDao.getClinicalCodes(value);
				StringBuilder sb = new StringBuilder();
				boolean fg = true;
				for (ClinicalCodes code : codes) {
					if (fg) {
						sb.append(code.getClinicalCode());
						fg = false;
					} else
						sb.append(",").append(code.getClinicalCode());
				}
				return sb.toString();
			} else {
				return "";
			}
		} else
			return "";
	}

	private NeurobehavioralObservationsofIndividualAnimal getneurobehavioralObservationsofIndividualAnimal(
			List<StudyAnimal> groupAnimals, CrfSectionElement posture, String lable) {
		NeurobehavioralObservationsofIndividualAnimal chielOdbj = new NeurobehavioralObservationsofIndividualAnimal();
		chielOdbj.setPerameter(lable);
		Map<Integer, String> animalvalue = new HashMap<>();
		for (StudyAnimal animal : groupAnimals) {
			if (posture != null) {
				List<String> value = getSession().createCriteria(CrfSectionElementData.class)
						.add(Restrictions.eq("animal.id", animal.getId()))
						.add(Restrictions.eq("element.id", posture.getId()))
						.setProjection(Projections.distinct(Projections.property("value"))).list();
				if (value.size() > 0) {
					List<ClinicalCodes> codes = clinicalCodesDao.getClinicalCodes(value);
					StringBuilder sb = new StringBuilder();
					boolean fg = true;
					for (ClinicalCodes code : codes) {
						if (fg) {
							sb.append(code.getClinicalCode());
							fg = false;
						} else
							sb.append(",").append(code.getClinicalCode());
					}
					animalvalue.put(animal.getAnimalId(), sb.toString());
				} else {
					animalvalue.put(animal.getAnimalId(), "");
				}
			} else
				animalvalue.put(animal.getAnimalId(), "");
		}
		chielOdbj.setAnimalvalue(animalvalue);
		return chielOdbj;
	}
	
	
	

	@Override
	public MotorActivityDataOfIndividualAnimal motorActivityDataOfIndividualAnimal(CrfSectionElement total,
			CrfSectionElement ambulatory, CrfSectionElement stereotypic,
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs, List<StudyAnimal> animals) {
	
		MotorActivityDataOfIndividualAnimal obj = new MotorActivityDataOfIndividualAnimal();
		Map<Integer, MotorActivityDataOfIndividualAnimalData> mp = new TreeMap<>();
		boolean flag = true;
		List<SubGroupInfo> groups = getSession().createCriteria(SubGroupInfo.class)
				.add(Restrictions.eq("study.id", stdSubGroupObservationCrfs.getStudy().getId())).list();
		for (SubGroupInfo sgroup : groups) {
			if(flag) {
				obj.setStudy(stdSubGroupObservationCrfs.getStudy());
				obj.setType("Treatment");
				flag = false;
			}
			List<StudyAnimal> groupAnimals = getSession().createCriteria(StudyAnimal.class)
					.add(Restrictions.eq("subGropInfo.id", sgroup.getId())).list();
			MotorActivityDataOfIndividualAnimalData chielOdbj = null;
			for (StudyAnimal animal : groupAnimals) {
				chielOdbj = new MotorActivityDataOfIndividualAnimalData();
				chielOdbj.setAnimalNo(animal.getAnimalId()+"");
				chielOdbj.setGender(animal.getGender());
				chielOdbj.setTotal(getSectionElemntData(animal, total));
				chielOdbj.setAmbulatory(getSectionElemntData(animal, ambulatory));
				chielOdbj.setStereotypic(getSectionElemntData(animal, stereotypic));
				mp.put(animal.getAnimalId(), chielOdbj);
			}
			
		}
		obj.setData(mp);
		return obj;
	}
	
}
