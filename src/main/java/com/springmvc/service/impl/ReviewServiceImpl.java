package com.springmvc.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.covide.crf.dto.AccessionAnimalDataviewDto;
import com.covide.crf.dto.ObservationAnimalDataviewDto;
import com.covide.dto.SatusAndWorkFlowDetailsDto;
import com.covide.dto.StudyDesignReviewDto;
import com.springmvc.dao.ReviewDao;
import com.springmvc.dao.StudyDao;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyMaster;
import com.springmvc.service.ReviewService;

@Service("reviewService")
public class ReviewServiceImpl implements ReviewService {
	
	@Autowired
	ReviewDao reviewDao;
	@Autowired
	StudyDao studyDao;
	@Override
	public StudyDesignReviewDto getStudyDesignReviewDto(Long studyId) {
		return reviewDao.getStudyDesignReviewDto(studyId);
	}

	@Override
	public String obserVationReviewProcessSaving(String username, Long studyId, String type, String comments) {
		String result = "Failed";
		StudyDesignStatus sds = null;
		SatusAndWorkFlowDetailsDto sawfdd = null;
		boolean flag = false;
		StudyMaster sm = null;
		try {
			if(type.equals("Accept"))
				sawfdd = reviewDao.getSatusAndWorkFlowDetailsDtoDetails(studyId, "DINR", "RASD");
			else
				sawfdd = reviewDao.getSatusAndWorkFlowDetailsDtoDetails(studyId, "DINR", "RRSD");
			sds = sawfdd.getSds();
			if(sds != null) {
				sds.setStatus(sawfdd.getSm());
				sds.setUpdatedBy(username);
				sds.setUpdatedOn(new Date());
				sds.setStudyId(sawfdd.getStudy().getId());
				if(type.equals("Reject")) {
				  sds.setCount(3);
				  sm = sawfdd.getStudy();
				  StatusMaster status = reviewDao.getStatusMasterRecord("SU");
				  sm.setStatus(status);
				  sm.setExperimentalDesign("Done");
				}else {
					sds.setCount(2);
				}
				if(!comments.equals("0"))
					sds.setComments(comments);
				
				ApplicationAuditDetails aad = new ApplicationAuditDetails();
				aad.setAction(type);
				aad.setCreatedBy(username);
				aad.setCreatedOn(new Date());
				aad.setStudyId(sawfdd.getStudy());
				aad.setWfsdId(sawfdd.getWfsd());
				flag = reviewDao.updateStudyDesignStatusDetails(sds, aad, sm);
				if(flag)
					result ="success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public AccessionAnimalDataviewDto getAccessionAnimalDataDtoDetails(Long studyId, Long crfId, Long userId, String dateString, String gender, Long studyAcclamatizationDatesId) {
		return reviewDao.getAccessionAnimalDataDtoDetails(studyId, crfId, userId, dateString, gender, studyAcclamatizationDatesId);
	}
	

	
	@Override
	public ObservationAnimalDataviewDto observatoinAnimalDataDtoDetails(Long studyId, Long studyTreatmentDataDatesId, Long stdSubGroupObservationCrfsId, Long userId, 
			boolean export, String gender, String schduleType) {
		return reviewDao.observatoinAnimalDataDtoDetails(studyId, studyTreatmentDataDatesId, stdSubGroupObservationCrfsId, userId, export, gender, schduleType);
	}

	@Override
	public List<StudyAcclamatizationData> getStudyAcclamatizationDataRecordsList(Long studyId) {
		return reviewDao.getStudyAcclamatizationDataRecordsList(studyId);
	}

	@Override
	public String reviewSelectedDataEntryFroms(List<Long> checkedIds, Long userId, String  reviewType, String reviewComment) {
		return reviewDao.reviewSelectedDataEntryFroms(checkedIds, userId, reviewType, reviewComment);
	}
	
	@Override
	public String reviewSelectedObservationDataEntryFroms(List<Long> checkedIds, Long userId, String  reviewType, String reviewComment) {
		return reviewDao.reviewSelectedObservationDataEntryFroms(checkedIds, userId, reviewType, reviewComment);
	}

	@Override
	public String saveRandamizationData(Long redamizationId, Long userId, String reviewType, String reviewComment) {
		// TODO Auto-generated method stub
		return reviewDao.saveRandamizationData(redamizationId, userId, reviewType, reviewComment);
	}
	
	@Override
	public AccessionAnimalDataviewDto getAccessionAnimalDataDtoDetailsView(Long studyAcclamatizationDataId) {
		return reviewDao.getAccessionAnimalDataDtoDetailsView(studyAcclamatizationDataId);
	}
	
	@Override
	public boolean saveRandamizationReview(Long studyId, Long userId, String reviewType, String reviewComment) {
		// TODO Auto-generated method stub
		return reviewDao.saveRandamizationReview(studyId, userId, reviewType, reviewComment);
	}
	
	@Override
	public String studyReviewProcessSaving(String username, Long studyId, String type, String comments) {
		String result = "Failed";
		StudyMaster sm = null;
		try {
			sm = studyDao.findByStudyId(studyId);
			if(type.equals("Approve")) {
				sm.setStudyStatus("Reviewed");
				sm.setApprovalStatus(true);
			}else
				sm.setStudyStatus("Comments Required");
			sm.setReviewComment(comments);
			sm.setReviewedBy(username);
			sm.setReviewedOn(new Date());
			studyDao.updateStudy(sm);
			result = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
