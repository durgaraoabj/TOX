package com.springmvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springmvc.dao.RandomizationDao;
import com.springmvc.model.IpRequest;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.Randomization;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.StudySite;
import com.springmvc.model.StudySiteSubject;
import com.springmvc.service.RandomizationService;

@Service("randomizationService")
public class RandomizationServiceImpl implements RandomizationService {

	@Autowired
	RandomizationDao randomizationDao;
	
	@Override
	public List<StudyPeriodMaster> onlyStudyPeriodsWithSubEle(StudyMaster study) {
		return randomizationDao.onlyStudyPeriodsWithSubEle(study);
	}

	@Override
	public List<Randomization> randomizationList(StudyMaster study) {
		return randomizationDao.randomizationList(study);
	}

	@Override
	public void saveRandomizationList(List<Randomization> list) {
		randomizationDao.saveRandomizationList(list);
		
	}

	@Override
	public void saveIpRequest(IpRequest ir) {
		randomizationDao.saveIpRequest(ir);
	}

	@Override
	public Randomization randomizationBySubPeriod(int subjectId, StudyPeriodMaster sp) {
		return randomizationDao.randomizationBySubPeriod(subjectId,sp);
	}

	@Override
	public List<IpRequest> ipRequestsByStudy(StudyMaster study) {
		return randomizationDao.ipRequestsByStudy(study);
	}

	@Override
	public List<StudySiteSubject> siteWiseSubjects(StudySite site) {
		return randomizationDao.siteWiseSubjects(site);
	}

	@Override
	public List<IpRequest> ipRequestsBySite(StudySite site) {
		return randomizationDao.ipRequestsBySite(site);
	}

	@Override
	public StudySiteSubject studySiteSubject(Long subjectId) {
		return randomizationDao.studySiteSubject(subjectId);
	}

	@Override
	public void updateIpRequest(String username, Long id) {
		randomizationDao.updateIpRequest(username, id);
	}

	@Override
	public void updateLoginUsers(LoginUsers users) {
		randomizationDao.updateLoginUsers(users);
	}

	@Override
	public IpRequest ipRequests(int subjectno, StudyPeriodMaster sp, StudySite site) {
		return randomizationDao.ipRequests(subjectno, sp, site);
	}
	
	
}
