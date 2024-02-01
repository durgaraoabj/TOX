package com.springmvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springmvc.dao.CRFAuditDao;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.service.CRFAuditService;
import com.springmvc.util.ObservationData;

@Service("crfAuditService")
public class CRFAuditServiceImpl implements CRFAuditService {
	
	@Autowired
	CRFAuditDao crfAuditDao;

	@Override
	public StudyMaster findByStudyId(long studyId) {
		return crfAuditDao.findByStudyId(studyId);
	}

	@Override
	public List<GroupInfo> studyGroupInfoWithChaildReview(StudyMaster sm) {
		return crfAuditDao.studyGroupInfoWithChaildReview(sm);
	}

	@Override
	public SubGroupInfo subGroupInfoAllByIdReview(StudyMaster sm, Long id) {
		return crfAuditDao.subGroupInfoAllByIdReview(sm, id);
	}

	@Override
	public LoginUsers findById(Long id) {
		return crfAuditDao.findById(id);
	}

	@Override
	public ObservationData observationData(Long subGroupInfoId, Long stdSubGroupObservationCrfsId, LoginUsers user) {
		
		return crfAuditDao.observationData(subGroupInfoId,stdSubGroupObservationCrfsId,user);
	}
	

}
