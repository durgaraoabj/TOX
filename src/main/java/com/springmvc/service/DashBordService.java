package com.springmvc.service;

import java.util.List;

import com.springmvc.model.StudyMaster;
import com.springmvc.reports.StudyProgress;
import com.springmvc.reports.SubjectEnrollmentBySite;
import com.springmvc.reports.SubjectEnrollmentForStudy;

public interface DashBordService {

	List<SubjectEnrollmentBySite> subjectEnrollmentBySiteList(StudyMaster sm);

	List<SubjectEnrollmentForStudy> subjectEnrollmentForStudyList(StudyMaster sm);

	List<StudyProgress> studyProgressList(StudyMaster sm);

}
