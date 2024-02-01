package com.springmvc.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.covide.dto.SatusAndWorkFlowDetailsDto;
import com.springmvc.dao.StudyDesignReviewDao;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.service.StudyDesignReviewService;

@Service("studyDesignReviewService")
public class StudyDesignReviewServiceImpl implements StudyDesignReviewService {
	
	@Autowired
	StudyDesignReviewDao stdReviewDao;

	@Override
	public String checReviewProcessDetails(Long studyId) {
		return stdReviewDao.checReviewProcessDetails(studyId);
	}

	@Override
	public String observationDesingSendToReviewProcess(Long studyId, String userName) {
		String result ="Failed";
		StudyDesignStatus sds = null;
		SatusAndWorkFlowDetailsDto swfddto = null;
		boolean flag = false;
		try {
			 sds = stdReviewDao.getStudyStatusDesignStatusRecord(studyId);
			 if(sds != null) {
				 swfddto = stdReviewDao.getSatusAndWorkFlowDetailsDtoDetails("DINR", studyId);
				 if(swfddto != null) {
					 sds.setCount(2);
					 sds.setStatus(swfddto.getSm());
					 sds.setUpdatedBy(userName);
					 sds.setUpdatedOn(new Date());
					 
					 ApplicationAuditDetails apad = new ApplicationAuditDetails();
					 apad.setAction("Review");
					 apad.setWfsdId(swfddto.getWfsd());
					 apad.setStudyId(swfddto.getStudy());
					 apad.setCreatedBy(userName);
					 apad.setCreatedOn(new Date());
					 flag = stdReviewDao.updateStudyDesingRecord(sds, apad);
					 if(flag)
						 result = "success";
				 }
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
