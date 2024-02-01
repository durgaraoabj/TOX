package com.springmvc.service;

import java.util.List;

import com.springmvc.model.AmendmentDetails;

public interface AmendmentService {

	List<AmendmentDetails> getAmendmentDetailsRecrodsList(Long studyNo);

	String saveAmandmentDetails(String userName, List<String> amdDetails, Long studyNo);

	List<AmendmentDetails> getStudyAmendmentDetailsList(Long studyId);

}
