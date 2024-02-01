package com.springmvc.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.ObservationAnimalDataviewDto;
import com.covide.dto.CrfDataEntryDto;
import com.covide.dto.ExpermentalDto;
import com.itextpdf.text.DocumentException;
import com.springmvc.dto.FileInformation;
import com.springmvc.model.AnimalCage;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyCageAnimal;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTreatmentDataDates;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.SubjectDataEntryDetails;
import com.springmvc.model.SysmexAnimalCode;
import com.springmvc.util.ExprementalData;
import com.springmvc.util.ObservationData;

public interface ExpermentalDesignService {

	List<GroupInfo> studyGroupInfo(StudyMaster sm);

	String saveExpermentalDesign(StudyMaster sm, List<SubGroupInfo> subGroups, List<SubGroupAnimalsInfo> animalInfo,
			List<SubGroupAnimalsInfoAll> animalInfoAll, StudyDesignStatus sds, ApplicationAuditDetails apad);

	List<SubGroupInfo> studySubGroupInfo(StudyMaster sm);

	List<GroupInfo> studyGroupInfoWithChaild(StudyMaster sm);

	public List<SubGroupAnimalsInfo> subGroupAnimalsInfo(SubGroupInfo sgi);

	public List<SubGroupInfo> studySubGroupInfo(GroupInfo gi);

	List<Crf> findAllCrfsOfsubGroup(StudyMaster sm, Long id);

	SubGroupInfo subGroupInfoById(StudyMaster sm, Long id);

	Crf studyCrf(StudyMaster sm, Long subGroupId, Long crfId);

	Map<String, String> allElementIdsTypesJspStd(Crf crf);

	Map<String, String> requiredElementIdInJspStd(Crf crf, String userRole);

	Map<String, String> pattrenIdsAndPattrenStd(Crf crf);

	SubjectDataEntryDetails studyCrfDataEntry(Long studyCageAnimalId, Long subGroupInfoAllId, Long groupId,
			Long subGroupId, Long subGroupInfoId, Long crfId, Long stdSubGroupObservationCrfsId, Long userId,
			StudyMaster sm, String discrebencyFields, String deviationMessage, String frequntlyMessage,
			HttpServletRequest request, String type);

	ObservationData observationData(Long subGroupInfoId, Long crfId, LoginUsers user);

	SubGroupInfo subGroupInfoAllById(StudyMaster sm, Long id, Long gorupId);

	File exportDataToExcel(ObservationAnimalDataviewDto data, HttpServletRequest request) throws Exception;

	SubGroupAnimalsInfoAll subGroupAnimalsInfoAll(Long subGroupInfoId);

	int subGroupAnimalsInfoCrfDataCount(SubGroupAnimalsInfoAll animal, StdSubGroupObservationCrfs crf, Long sgoupId);

	Map<String, String> weightWithNameValue(Long crfid, Long subGroupAnimalsInfoAllId, String userName);

	List<StdSubGroupObservationCrfs> findAllStdSubGroupObservationCrfs(StudyMaster sm, Long subGroupId);

//	List<GroupInfo> studyGroupInfoWithChaildReview(StudyMaster sm);

	SubGroupInfo subGroupInfoAllByIdReview(StudyMaster sm, Long id);

	List<SubGroupAnimalsInfoAll> subGroupAnimalsInfoInStudy(Long id);

	String stdSubGroupObservationCrfsWithAnimal(Long animalId);

	String crfElements(Long crfId);

	List<ExprementalData> expressionData(StudyMaster sm);

	Map<String, List<StdSubGroupObservationCrfs>> expressionDataCalender(StudyMaster sm);

	File exportDataToExcelExport(StudyMaster sm, HttpServletRequest request);

	ObservationData allObservationData1(StudyMaster sm);

	String saveInstrumentWeight(Long animalId, Long crf, Long crfSecEle, String weight, String userName);

	Map<String, String> crfSectionElementInstrumentValues(StudyAnimal animal, Crf crf);
	Map<Long, Map<String, String>> crfSectionElementInstrumentValues(List<StudyAnimal> animals, Crf crf);
	StudyDesignStatus getStudyDesignStatusRecord(Long studyId);

	List<SubGroupInfo> getSubGroupInfoList(StudyMaster sm);

	List<SubGroupAnimalsInfoAll> getSubGroupAnimalsInfoAllRecordsList(SubGroupAnimalsInfo sgai);

	String updateExpermentalDesignDetails(Long studyId, String userName, StudyService studyService,
			List<String> doseList, List<String> concList, List<String> countList, List<String> fromList,
			List<String> toList, List<String> subNamesList, List<String> newGroupNameList,
			List<String> newGroupDoseList, List<String> newGroupConcList, List<String> newGroupCountList,
			List<String> newGroupFromList, List<String> newGroupToList, List<String> newGroupgenderList);

	ExpermentalDto getExpermentalDtoDetails(Long studyId);

	Map<Long, String> getNewGroupDataDetails(List<GroupInfo> gi);

	List<StdSubGroupObservationCrfs> getSubGroupObservations(Long id, Long groupId, Long subGroupId);

	List<SubGroupAnimalsInfoAll> getsSbGroupAnimalsInfoAll(Long animalId);

	CrfDataEntryDto getCrfDataEntryDetails(Long studyId, Long animalId, Long crfId, Long subGroupId);

	ObservationData observationDataAllAnimal(Long subGroupInfoId, Long stdSubGroupObservationCrfsId, LoginUsers user,
			Long groupId, Long subGroupId);

	SubjectDataEntryDetails getSubjectDataEntryDetailsRecord(StdSubGroupObservationCrfs stdCrf, Long studyId,
			SubGroupAnimalsInfoAll animal, Long subGroupId);

	List<String> getFrormulaDataofCurrentCrf(Long id, Long studyId);

	List<SubGroupAnimalsInfoAll> getSubGroupAnimalsInfoAllRecords(List<SubjectDataEntryDetails> sdedList);

	List<SubGroupAnimalsInfoAll> getSubGroupAnimalsInfoAllRecordsList(Long subGroupId);

	List<GroupInfo> studyGroupInfoWithSubGroup(StudyMaster sm);

	StudyCageAnimal nextAnimalForObservationData(Long studyId, Long crfId, Long subGroupId, Long cageId);

	List<AnimalCage> animalCages(Long studyId);

	List<StudyCageAnimal> studyCageAnimal(Long studyId, Long cageId);

	StdSubGroupObservationCrfs stdSubGroupObservationCrfsWithId(Long stdSubGroupObservationCrfsId);

	boolean checkDataEntryEligible(StudyMaster sm);

	AnimalCage cages(Long cageId);

	List<AnimalCage> groupAnimalCages(Long id, Long subGroupId);

	List<SysmexAnimalCode> sysmexAnimalCodes();

	boolean changeTreatmentConfig(Crf crf, StudyMaster study, String status, String userName, Long subGroupId);

	boolean changeTreatmentConfigType(Long crfId, Long studyId, String value, String userName, String field, Long subGroupId);

	boolean updateStudyTreatmentDataDatesDetails(Long studyTreatmentDataDatesId, String value, Long userId);

	boolean removeStudyTreatmentDataDatesDetails(Long studyTreatmentDataDatesId, Long userId);

	List<StdSubGroupObservationCrfs> stdSubGroupObservationCrfs(Long studyId, Long userRoleId);

	Map<String, List<StdSubGroupObservationCrfs>> expressionTreatmentDataCalender(
			List<StdSubGroupObservationCrfs> treatmentList, StudyMaster sm);

	StudyAnimal nextAnimalForTreatmentData(Long studyTreatmentDataDatesId, Long stdSubGroupObservationCrfsId, Long studyId, Long crfId,
			String seletedGender, int noOfEntry,String selecteDate);

	List<StudyAnimal> studyAnimalWithGender(Long studyId, String seletedGender);

	SubjectDataEntryDetails studyTreatmentDataEntry(Long stdSubGroupObservationCrfsId,Long studyTreatmentDataDatesId, Long crfId, Long subGroupId,
			String seletedGender, String type, int noOfEntry, Long userId, StudyMaster sm, Long animalId,
			String selecteDate, String discrebencyFields, String deviationMessage, String frequntlyMessage,
			HttpServletRequest request, String type2)  throws ParseException;

	List<StudyAnimal> studyAnimal(Long studyId);

	List<StdSubGroupObservationCrfs> findAllActiveStdSubGroupObservationCrfs(StudyMaster sm, Long subGroupId);

	/*
	 * Day wise each treatment gender based list
	 */
	Map<String, List<StudyTreatmentDataDates>> expressionTreatmentDataCalenderForGenderSpecific(
			List<StdSubGroupObservationCrfs> treatmentList, StudyMaster sm);

	List<SubGroupInfo> studySubGroupInfo(Long groupId);

	Long subGrouId(Long groupId);

	Map<String, Map<Long, StdSubGroupObservationCrfs>> unscheduelTreatmentData(StudyMaster sm);

	List<StdSubGroupObservationCrfs> stdSubGroupObservationCrfs(Long studyId);

	FileInformation treatmentObservatinExport(Long observationId, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes, HttpServletResponse response) throws IOException, DocumentException, ParseException ;

	FileInformation acclamatizationObservatinExport(Long observationId, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes, HttpServletResponse response) throws IOException, DocumentException, ParseException ;
	

}
