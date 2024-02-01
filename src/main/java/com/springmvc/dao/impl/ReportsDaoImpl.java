package com.springmvc.dao.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.ReportsDao;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;

@Repository("reportsDao")
public class ReportsDaoImpl extends AbstractDao<Integer, Volunteer> implements ReportsDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Volunteer> getVolunteersList(StudyMaster sm) {
		return getSession().createCriteria(Volunteer.class)
				.add(Restrictions.eq("studyMaster", sm)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StudyPeriodMaster> getPeriodsList(StudyMaster sm) {
		return getSession().createCriteria(StudyPeriodMaster.class)
				.add(Restrictions.eq("studyMaster", sm)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VolunteerPeriodCrf> getVolunteerAllPeriodCrfsList(StudyMaster sm) {
		return getSession().createCriteria(VolunteerPeriodCrf.class)
				.add(Restrictions.eq("studyId", sm.getId())).list();
	}

	@Override
	public Volunteer getVolunteerMasterPojo(String volId) {
		return (Volunteer) getSession().createCriteria(Volunteer.class)
				.add(Restrictions.eq("id", Long.parseLong(volId))).uniqueResult();
	}

	@Override
	public StudyPeriodMaster getPeriodMasterPojo(String periodId) {
		return (StudyPeriodMaster) getSession().createCriteria(StudyPeriodMaster.class)
				.add(Restrictions.eq("id", Long.parseLong(periodId))).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VolunteerPeriodCrf> getVolunteerCustomCrfsList(StudyMaster sm, List<Long> periodIds,
			Volunteer volPojo) {
		return getSession().createCriteria(VolunteerPeriodCrf.class)
				.add(Restrictions.eq("studyId", sm.getId()))
				.add(Restrictions.in("period.id", periodIds))
				.add(Restrictions.eq("vol", volPojo)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VolunteerPeriodCrf> getVolunteerCrfsBasedOnVolId(StudyMaster sm, List<Long> volIds,
			StudyPeriodMaster spmPojo) {
		return getSession().createCriteria(VolunteerPeriodCrf.class)
				.add(Restrictions.eq("studyId", sm.getId()))
				.add(Restrictions.in("vol.id", volIds))
				.add(Restrictions.eq("period", spmPojo)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VolunteerPeriodCrf> getVolunteerCrfsBasedOnVolidAndPeriodId(StudyMaster sm, Volunteer volPojo,
			StudyPeriodMaster spmPojo) {
		return getSession().createCriteria(VolunteerPeriodCrf.class)
				.add(Restrictions.eq("studyId", sm.getId()))
				.add(Restrictions.eq("vol", volPojo))
				.add(Restrictions.eq("period", spmPojo)).list();
	}

	@Override
	public VolunteerPeriodCrf getVolunteerPeriodCrfPojo(long studyId, long volId, long periodId, long crfId) {
		return (VolunteerPeriodCrf) getSession().createCriteria(VolunteerPeriodCrf.class)
				.add(Restrictions.eq("studyId", studyId))
				.add(Restrictions.eq("vol.id", volId))
				.add(Restrictions.eq("periodId.id", periodId))
				//.add(Restrictions.eq("stdCrf.id", crfId))
				.uniqueResult();
	}
  
	
}
