package com.springmvc.dao;

import java.util.List;

import com.covide.dto.RadomizationDto;
import com.covide.dto.RandomizationGenerationDto;
import com.springmvc.model.AccessionAnimalsDataEntryDetails;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.RadomizationReviewDto;
import com.springmvc.model.RandamizationDto;
import com.springmvc.model.RandomizationDetails;
import com.springmvc.model.StudyAccessionCrfSectionElementData;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupInfo;

public interface AnimalRandomizationDao {

	RadomizationDto getRadomizationDtoDetails(Long studyId);

	List<AccessionAnimalsDataEntryDetails> getAccessionAnimalsDataEntryDetailsList(Long crfId, Long studyId, Long animalId, int animalNo);

	RandomizationGenerationDto RandomizationGenerationDtoDetails(Long studyId, Long crfId);

	StudyAccessionCrfSectionElementData getStudyAccessionCrfSectionElementDataRecord(Long studyId, int animalId, Long crfId,
			Long secelId);

	List<GroupInfo> getGroupInfo(Long studyId);

	List<SubGroupAnimalsInfo> getSubGroupAnimalsInfoRecordsList(List<Long> groupIds, Long studyId);

	StudyMaster getStudyMasterRecord(Long studyId);

	List<SubGroupAnimalsInfo> getSubGroupAnimalsInfoRecordsList(Long studyId);

	SubGroupInfo getSubGroupInfoRecord(Long subgroupId);

	boolean saveRadomizationDetails(List<SubGroupAnimalsInfo> updateList, List<RandomizationDetails> radList);

	List<RandomizationDetails> getRandomizationDetailsRecordsList(Long studyId);

	String saveRadomization(List<StudyAnimal> animalsList, List<SubGroupAnimalsInfo> subGroup,
			List<RandomizationDetails> radList,StudyMaster study);

	String sendRandamizationToReview(RadomizationReviewDto reviewrdmDto, Long studyId, Long userId);

	RandamizationDto generatedRandamization(StudyMaster study, String gender);

	List<StudyAnimal> studyAnimal(Long id);

	boolean saverandomizationSendToReview(StudyMaster study, List<RandamizationDto> dtoList, Long userId);

	List<StudyAnimal> studyAnimalWithGender(Long id, String gender);

	int stuyAnimalSize(StudyMaster study, String gender);

	
}
