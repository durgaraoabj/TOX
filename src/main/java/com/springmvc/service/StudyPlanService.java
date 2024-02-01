package com.springmvc.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.springmvc.model.StudyPlanFilesDetails;

public interface StudyPlanService {

	StudyPlanFilesDetails getStudyPlanFilesDetailsRecord(Long studyId);

	String saveStudyPlanFilesDetails(String username, MultipartFile file, Long studyId);

	String generateStudyPlanReport(Long studyId, HttpServletRequest request, HttpServletResponse response);

}
