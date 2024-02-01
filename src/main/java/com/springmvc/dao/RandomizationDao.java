package com.springmvc.dao;

import java.util.List;

import com.springmvc.model.IpRequest;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.Randomization;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.StudySite;
import com.springmvc.model.StudySiteSubject;

public interface RandomizationDao {

	List<StudyPeriodMaster> onlyStudyPeriodsWithSubEle(StudyMaster study);

	List<Randomization> randomizationList(StudyMaster study);

	void saveRandomizationList(List<Randomization> list);

	void saveIpRequest(IpRequest ir);

	Randomization randomizationBySubPeriod(int subjectId, StudyPeriodMaster sp);

	List<IpRequest> ipRequestsByStudy(StudyMaster study);

	List<StudySiteSubject> siteWiseSubjects(StudySite site);

	List<IpRequest> ipRequestsBySite(StudySite site);

	StudySiteSubject studySiteSubject(Long subjectId);

	void updateIpRequest(String username, Long id);

	void updateLoginUsers(LoginUsers users);

	IpRequest ipRequests(int subjectno, StudyPeriodMaster sp, StudySite site);

}
