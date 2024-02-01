package com.springmvc.dao;

import com.covide.dto.SatusAndWorkFlowDetailsDto;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.StudyDesignStatus;

public interface StudyDesignReviewDao {

	String checReviewProcessDetails(Long studyId);

	StudyDesignStatus getStudyStatusDesignStatusRecord(Long studyId);

	boolean updateStudyDesingRecord(StudyDesignStatus sds, ApplicationAuditDetails apad);

	SatusAndWorkFlowDetailsDto getSatusAndWorkFlowDetailsDtoDetails(String string, Long studyId);

}
