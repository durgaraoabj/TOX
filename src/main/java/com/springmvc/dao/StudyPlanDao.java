package com.springmvc.dao;

import com.springmvc.model.StudyPlanFilesDetails;

public interface StudyPlanDao {

	StudyPlanFilesDetails getStudyPlanFilesDetailsRecord(Long studyId);

	long saveStudyPlanFilesDetails(StudyPlanFilesDetails spfd);

}
