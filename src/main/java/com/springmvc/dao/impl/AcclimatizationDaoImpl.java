package com.springmvc.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.covide.crf.dao.CrfDAO;
import com.covide.crf.dto.Crf;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.AcclimatizationDao;
import com.springmvc.dao.StudyDao;
import com.springmvc.dao.UserDao;
import com.springmvc.model.AmendmentDetails;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAcclamatizationDates;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyCageAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudySubGroupAnimal;
import com.springmvc.model.StudyTreatmentDataDates;

@Repository("acclimatizationDao")
@SuppressWarnings("unchecked")
@PropertySource(value = { "classpath:application.properties" })
public class AcclimatizationDaoImpl extends AbstractDao<Long, StudyAcclamatizationData> implements AcclimatizationDao {
	@Autowired
	private Environment environment;
	@Autowired
	UserDao userDao;
	@Autowired
	CrfDAO crfDao;
	@Autowired
	StudyDao studyDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<StudyAcclamatizationData> getStudyAcclamatizationDataList(Long studyId) {
		return getSession().createCriteria(StudyAcclamatizationData.class).add(Restrictions.eq("study.id", studyId))
				.add(Restrictions.eq("status", "active")).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Crf> getCrfRecordsList(List<Long> crfIds) {
		return getSession().createCriteria(Crf.class).add(Restrictions.in("id", crfIds)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StudyAcclamatizationData> getStudyAcclamatizationDataRecords(List<Long> unchkIds, Long studyId) {
		return getSession().createCriteria(StudyAcclamatizationData.class).add(Restrictions.eq("study.id", studyId))
				.add(Restrictions.in("crf.id", unchkIds)).list();
	}

	@Override
	public boolean saveStudyAcclamatizationData(List<StudyAcclamatizationData> saveList,
			List<StudyAcclamatizationData> updateList, Long studyId) {
		boolean flag = false;
		boolean saveFlag = false;
		boolean updateFlag = false;
		try {
			if (saveList.size() > 0) {
				for (StudyAcclamatizationData sad : saveList) {
					StudyAcclamatizationData sadPojo = (StudyAcclamatizationData) getSession()
							.createCriteria(StudyAcclamatizationData.class).add(Restrictions.eq("study.id", studyId))
							.add(Restrictions.eq("crf", sad.getCrf())).uniqueResult();
					if (sadPojo != null) {
						Long sadId = sadPojo.getId();
						BeanUtils.copyProperties(sad, sadPojo);
						sadPojo.setId(sadId);
						getSession().merge(sadPojo);
						saveFlag = true;
					} else {
						getSession().save(sad);
						saveFlag = true;
					}
				}
			} else
				saveFlag = true;
			if (updateList.size() > 0) {
				for (StudyAcclamatizationData sad : updateList) {
					getSession().merge(sad);
					updateFlag = true;
				}
			} else
				updateFlag = true;
			if (updateFlag && saveFlag)
				flag = true;

		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}

	@Override
	public List<StudyAnimal> allStudyAnimals(Long studyId) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", studyId)).list();
	}

	@Override
	public List<StudyAnimal> allUnCagedAnimals(Long studyId) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", studyId))
				.add(Restrictions.eq("caged", false)).list();
	}

	@Override
	public List<StudyCageAnimal> allCagedAnimals(Long studyId) {
		List<StudyCageAnimal> cagedAniamals = (List<StudyCageAnimal>) getSession().createCriteria(StudyCageAnimal.class)
				.add(Restrictions.eq("study.id", studyId)).list();
		return cagedAniamals;
	}

	@Override
	public List<StudySubGroupAnimal> studySubGroupAnimal(Long studyId) {
		List<StudySubGroupAnimal> cagedAniamals = (List<StudySubGroupAnimal>) getSession()
				.createCriteria(StudyCageAnimal.class).add(Restrictions.eq("study.id", studyId)).list();
		return cagedAniamals;
	}

	@Override
	public StudyAcclamatizationData studyAcclamatizationData(Long crfId, Long studyId) {
		StudyAcclamatizationData sd = getStudyAcclamatizationDataList(crfId, studyId);
		if (sd != null)
			sd.setAcclamatizationDates(studyAcclamatizationDates(sd.getId()));
		return sd;
	}

	@Override
	public StdSubGroupObservationCrfs studyTreatmentData(Long crfId, Long studyId, Long subGroupId) {
		StdSubGroupObservationCrfs sd = getStudyTreatmentDataList(crfId, studyId, subGroupId);
		if (sd != null)
			sd.setTreatmentDates(studyTreatmentDates(sd.getId()));
		return sd;
	}

	private List<StudyAcclamatizationDates> studyAcclamatizationDates(Long id) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		List<StudyAcclamatizationDates> list = getSession().createCriteria(StudyAcclamatizationDates.class)
				.add(Restrictions.eq("studyAcclamatizationData.id", id)).list();
		for (StudyAcclamatizationDates li : list) {
			li.setDateString(sdf.format(li.getAccDate()));
		}
		return list;
	}

	private List<StudyTreatmentDataDates> studyTreatmentDates(Long id) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		List<StudyTreatmentDataDates> list = getSession().createCriteria(StudyTreatmentDataDates.class)
				.add(Restrictions.eq("stdSubGroupObservationCrfs.id", id)).list();
		for (StudyTreatmentDataDates li : list) {
			li.setDateString(sdf.format(li.getAccDate()));
		}
		return list;
	}

	@Override
	public StudyAcclamatizationDates studyAcclamatizationDates(Long crfId, Long studyId, Date date, Long userId,
			Boolean genderBase, String gender) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		LoginUsers user = userDao.findById(userId);
		Crf crf = crfDao.getCrf(crfId);
		StudyMaster study = studyDao.findByStudyId(studyId);
		String stDate = sdf.format(study.getAcclimatizationStarDate());
		long timeDiff;
		long daysDiff;
		try {
			timeDiff = date.getTime() - sdf.parse(stDate).getTime();
			daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			daysDiff = 0l;
		}

		int days = (int) daysDiff + 1;
		StudyAcclamatizationData sad = studyAcclamatizationData(crfId, studyId);
		Criteria cri = getSession().createCriteria(StudyAcclamatizationDates.class)
				.add(Restrictions.eq("crf.id", crfId)).add(Restrictions.eq("study.id", studyId))
				.add(Restrictions.eq("dayNo", days));
		if (genderBase) {
			cri.add(Restrictions.eq("genderBased", true));
			cri.add(Restrictions.eq("gender", gender));
		} else
			cri.add(Restrictions.eq("genderBased", false));
//		StudyAcclamatizationDates dates = (StudyAcclamatizationDates) getSession().createCriteria(StudyAcclamatizationDates.class)
//				.add(Restrictions.eq("crf.id", crfId))
//				.add(Restrictions.eq("study.id", studyId))
//				.add(Restrictions.eq("dayNo", days)).uniqueResult();
		StudyAcclamatizationDates dates = (StudyAcclamatizationDates) cri.uniqueResult();
		if (dates == null) {
			dates = new StudyAcclamatizationDates();
			dates.setCrf(crf);
			dates.setStudyAcclamatizationData(sad);
			dates.setStudy(study);
			dates.setActiveStatus(true);
			dates.setAccDate(date);
			dates.setCreatedBy(user.getUsername());
			dates.setCreatedOn(new Date());
			dates.setDayNo(days);
			if (genderBase) {
				dates.setGenderBased(true);
				dates.setGender(gender);
			}
			getSession().save(dates);
		} else {
			if (!dates.isActiveStatus()) {
				dates.setActiveStatus(true);
				dates.setUpdatedBy(user.getUsername());
				dates.setUpdatedOn(new Date());
			}
		}
		dates.setDateString(sdf.format(dates.getAccDate()));
		return dates;
	}

	@Override
	public StudyTreatmentDataDates studyTreatmentDataDates(Long crfId, Long studyId, Date date, Long userId,
			Long subGroupId, Boolean genderBase, String gender) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		LoginUsers user = userDao.findById(userId);
		Crf crf = crfDao.getCrf(crfId);
		StudyMaster study = studyDao.findByStudyId(studyId);
		String stDate = sdf.format(study.getTreatmentStarDate());
		long timeDiff;
		long daysDiff;
		try {
			timeDiff = date.getTime() - sdf.parse(stDate).getTime();
			daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			daysDiff = 0l;
		}

		int days = (int) daysDiff + 1;
		StdSubGroupObservationCrfs sad = studyTreatmentData(crfId, studyId, subGroupId);
		Criteria cri = getSession().createCriteria(StudyTreatmentDataDates.class).add(Restrictions.eq("crf.id", crfId))
				.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("dayNo", days));
		if (genderBase) {
			cri.add(Restrictions.eq("genderBased", true));
			cri.add(Restrictions.eq("gender", gender));
		} else
			cri.add(Restrictions.eq("genderBased", false));
//		StudyTreatmentDataDates dates = (StudyTreatmentDataDates) getSession().createCriteria(StudyTreatmentDataDates.class)
//				.add(Restrictions.eq("crf.id", crfId))
//				.add(Restrictions.eq("study.id", studyId))
//				.add(Restrictions.eq("dayNo", days)).uniqueResult();
		StudyTreatmentDataDates dates = (StudyTreatmentDataDates) cri.uniqueResult();

		if (dates == null) {
			dates = new StudyTreatmentDataDates();
			dates.setCrf(crf);
			dates.setStdSubGroupObservationCrfs(sad);
			dates.setStudy(study);
			dates.setActiveStatus(true);
			dates.setAccDate(date);
			dates.setCreatedBy(user.getUsername());
			dates.setCreatedOn(new Date());
			dates.setDayNo(days);
			if (genderBase) {
				dates.setGenderBased(true);
				dates.setGender(gender);
			}
			getSession().save(dates);
		} else {
			if (!dates.isActiveStatus()) {
				dates.setActiveStatus(true);
				dates.setUpdatedBy(user.getUsername());
				dates.setUpdatedOn(new Date());
			}
		}

		dates.setDateString(sdf.format(dates.getAccDate()));

		return dates;
	}

	@Override
	public StudyAcclamatizationData getStudyAcclamatizationDataList(Long crfId, Long studyId) {
		return (StudyAcclamatizationData) getSession().createCriteria(StudyAcclamatizationData.class)
				.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("crf.id", crfId)).uniqueResult();
	}

	@Override
	public StdSubGroupObservationCrfs getStudyTreatmentDataList(Long crfId, Long studyId, Long subGroupId) {
		return (StdSubGroupObservationCrfs) getSession().createCriteria(StdSubGroupObservationCrfs.class)
				.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("subGroupInfo.id", subGroupId))
				.add(Restrictions.eq("crf.id", crfId)).uniqueResult();
	}

	@Override
	public void saveStudyAcclamatizationData(StudyAcclamatizationData sad, AmendmentDetails ad) {
		// TODO Auto-generated method stub
		getSession().save(sad);
		if (ad != null)
			getSession().save(ad);

	}

	@Override
	public void updateStudyAcclamatizationData(StudyAcclamatizationData sad) {
		// TODO Auto-generated method stub
		getSession().merge(sad);
	}

	@Override
	public boolean removeStudyAcclamatizationDatesDetails(Long studyAcclamatizationDatesId, Long userId) {
		// TODO Auto-generated method stub
		StudyAcclamatizationDates sd = studyAcclamatizationDatesById(studyAcclamatizationDatesId);
		sd.setActiveStatus(false);
		sd.setUpdatedBy(userDao.findById(userId).getUsername());
		sd.setUpdatedOn(new Date());
		return true;
	}

	

	@Override
	public boolean updateStudyAcclamatizationDatesDetails(Long studyAcclamatizationDatesId, String value, Long userId) {
		StudyAcclamatizationDates sd = studyAcclamatizationDatesById(studyAcclamatizationDatesId);
		sd.setNoOfEntry(Integer.parseInt(value));
		sd.setUpdatedBy(userDao.findById(userId).getUsername());
		sd.setUpdatedOn(new Date());
		return true;
	}

	@Override
	public List<StudyAcclamatizationDates> studyAcclamatizationDates(Long crfId, Long studyId) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		List<StudyAcclamatizationDates> list = getSession().createCriteria(StudyAcclamatizationDates.class)
				.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("crf.id", crfId))
				.add(Restrictions.eq("activeStatus", true)).list();
		for (StudyAcclamatizationDates li : list) {
			li.setDateString(sdf.format(li.getAccDate()));
		}
		return list;
	}

	@Override
	public List<StudyTreatmentDataDates> studyTreatmentDataDates(Long crfId, Long studyId, Long subGroupId) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		StdSubGroupObservationCrfs sg = getStudyTreatmentDataList(crfId, studyId, subGroupId);
		List<StudyTreatmentDataDates> list = getSession().createCriteria(StudyTreatmentDataDates.class)
				.add(Restrictions.eq("stdSubGroupObservationCrfs.id", sg.getId()))
				.add(Restrictions.eq("activeStatus", true)).list();
		for (StudyTreatmentDataDates li : list) {
			li.setDateString(sdf.format(li.getAccDate()));
		}
		return list;
	}

	

	@Override
	public StudyAcclamatizationDates studyAcclamatizationDatesById(Long id) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		StudyAcclamatizationDates sad = (StudyAcclamatizationDates) getSession().get(StudyAcclamatizationDates.class,
				id);
		sad.setDateString(sdf.format(sad.getAccDate()));
		return sad;
	}

	@Override
	public StudyTreatmentDataDates studyTreatmentDataDatesById(Long id) {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		StudyTreatmentDataDates sad = (StudyTreatmentDataDates) getSession().get(StudyTreatmentDataDates.class,
				id);
		sad.setDateString(sdf.format(sad.getAccDate()));
		return sad;
	}

	@Override
	public List<StudyAcclamatizationData> studyAcclamatizationData(Long studyId) {
		// TODO Auto-generated method stub
		return createEntityCriteria().add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("status", "active")).list();
	}
	
	
}
