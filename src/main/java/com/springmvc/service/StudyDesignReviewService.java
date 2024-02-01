package com.springmvc.service;

public interface StudyDesignReviewService {

	String checReviewProcessDetails(Long studyId);

	String observationDesingSendToReviewProcess(Long studyId, String userName);

}
