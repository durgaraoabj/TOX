package com.springmvc.dao.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.RandomizationDao;
import com.springmvc.model.IpRequest;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.Randomization;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.StudySite;
import com.springmvc.model.StudySiteSubject;


@Repository("randomizationDao")	
public class RandomizationDaoImpl extends AbstractDao<Long, Randomization> implements RandomizationDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<StudyPeriodMaster> onlyStudyPeriodsWithSubEle(StudyMaster sm) {
		List<StudyPeriodMaster> periods = getSession().createCriteria(StudyPeriodMaster.class)
				.add(Restrictions.eq("studyMaster", sm))
				.add(Restrictions.eq("type", "Period")).list();
		Collections.sort(periods);
		return periods;
	}

	@Override
	public List<Randomization> randomizationList(StudyMaster study) {
		@SuppressWarnings("unchecked")
		List<Randomization> list = getSession().createCriteria(Randomization.class)
		.add(Restrictions.eq("std", study))
		.list();
		for(Randomization r : list) {
			Hibernate.initialize(r.getStd());
			Hibernate.initialize(r.getPeriod());
		}
		return list;
	}

	@Override
	public void saveRandomizationList(List<Randomization> list) {
		for(Randomization r : list)
			getSession().save(r);
	}

	@Override
	public void saveIpRequest(IpRequest ir) {
		getSession().save(ir);
	}

	@Override
	public Randomization randomizationBySubPeriod(int subjectId, StudyPeriodMaster sp) {
		return (Randomization) getSession().createCriteria(Randomization.class)
				.add(Restrictions.eq("period", sp))
				.add(Restrictions.eq("subject", subjectId)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IpRequest> ipRequestsByStudy(StudyMaster study) {
		List<IpRequest> list =  getSession().createCriteria(IpRequest.class)
				.add(Restrictions.eq("std", study)).list();
		for(IpRequest r : list) Hibernate.initialize(r.getSite());
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IpRequest> ipRequestsBySite(StudySite site) {
		List<IpRequest> list =  getSession().createCriteria(IpRequest.class)
				.add(Restrictions.eq("site", site)).list();
		for(IpRequest r : list) Hibernate.initialize(r.getSite());
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StudySiteSubject> siteWiseSubjects(StudySite site) {
		return getSession().createCriteria(StudySiteSubject.class)
				.add(Restrictions.eq("site", site)).list();
	}


	@Override
	public StudySiteSubject studySiteSubject(Long subjectId) {
		StudySiteSubject sss =  (StudySiteSubject) getSession().get(StudySiteSubject.class, subjectId);
		Hibernate.initialize(sss.getSite());
		return sss;
	}

	@Override
	public void updateIpRequest(String username, Long id) {
		IpRequest ipr = (IpRequest) getSession().get(IpRequest.class, id);
		ipr.setUpdatedBy(username);
		ipr.setUpdatedOn(new Date());
		ipr.setStatus("AVAILABLE");
		getSession().update(ipr);
	}

	@Override
	public void updateLoginUsers(LoginUsers users) {
		getSession().update(users);
	}

	@Override
	public IpRequest ipRequests(int subjectno, StudyPeriodMaster sp, StudySite site) {
		return (IpRequest) getSession().createCriteria(IpRequest.class)
				.add(Restrictions.eq("period", sp))
				.add(Restrictions.eq("site", site))
				.add(Restrictions.eq("subject", subjectno)).uniqueResult();
	}
	
	
}
