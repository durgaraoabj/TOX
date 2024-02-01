package com.springmvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springmvc.dao.StatusDao;
import com.springmvc.dao.StudyAuditDao;
import com.springmvc.dao.StudyDao;
import com.springmvc.dao.UserDao;
import com.springmvc.dao.UserWiseStudiesAsignDao;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;
import com.springmvc.service.StudyAuditService;
import com.springmvc.service.UserService;

@Service("studyAuditService")
public class StudyAuditServiceImpl implements StudyAuditService {

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
	StudyAuditDao studyAuditDao;

	@Override
	public List<GroupInfo> getGroupInfoForStudy(StudyMaster study) {
		// TODO Auto-generated method stub
		return studyAuditDao.getGroupInfoForStudy(study);
	}

	@Override
	public List<StdSubGroupObservationCrfs> getStdSubGroupObservationCrfsWithStudy(StudyMaster study) {
		return studyAuditDao.getStdSubGroupObservationCrfsWithStudy(study);
	}

	@Override
	public List<SubGroupAnimalsInfoCrfDataCount> getSubGroupAnimalsInfoCrfDataCountWithStudyId(StudyMaster study) {
		return studyAuditDao.getSubGroupAnimalsInfoCrfDataCountWithStudyId(study);
	}

	@Override
	public List<SubGroupAnimalsInfoCrfDataCount> getSubGroupAnimalsInfoCrfDataCountWithStudyIdAndSubId(
			StudyMaster study, Long id) {
		return studyAuditDao.getSubGroupAnimalsInfoCrfDataCountWithStudyIdAndSubId(study,id);
	}
	
	

	
	
}
