package com.springmvc.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

import com.covide.dto.FinalRandomizationDto;
import com.covide.dto.RadomizationDto;
import com.covide.dto.RandomizationGenerationDto;
import com.springmvc.model.RadomizationReviewDto;
import com.springmvc.model.RandamizationDto;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyMaster;

public interface AnimalRandomizationService {

	RadomizationDto getRadomizationDtoDetails(Long studyId);

	RandomizationGenerationDto getRadomizationDetails(Long studyId, String userName);

	FinalRandomizationDto generateFinalRandomizationData(Long studyId, HttpServletRequest request);

	String savesavegenerateGropRandomizationDataDetails(Long studyId, String userName, HttpServletRequest request);

	String saveRandamization(RadomizationDto rdmDto, Long studyId, Long userId, StatusMaster approveStatus, StatusMaster rejectStatus);

	String sendRandamizationToReview(RadomizationReviewDto reviewrdmDto, Long studyId, Long userId);

	RadomizationReviewDto getRadomizationReviewDtoDetails(Long studyId);

	RadomizationReviewDto getRadomizationReviewDtoDetails(Long studyId, Long userId, String gender);

	RadomizationDto generateRandamization(Long studyId);

	RandamizationDto generatedRandamization(StudyMaster study, String string);

	String newRandamization(StudyMaster study, ModelMap model, HttpServletRequest request, String generatedFor);

	boolean saverandomizationSendToReview(StudyMaster study, List<RandamizationDto> dtoList, Long userId);

}
