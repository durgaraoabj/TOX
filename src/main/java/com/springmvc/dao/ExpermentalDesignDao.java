package com.springmvc.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfDescrpency;
import com.covide.crf.dto.CrfSectionElement;
import com.covide.crf.dto.CrfSectionElementData;
import com.covide.dto.CrfDataEntryDto;
import com.covide.dto.CrfFormulaDto;
import com.covide.dto.ExpermentalDto;
import com.springmvc.model.AmendmentDetails;
import com.springmvc.model.AnimalCage;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.DepartmentMaster;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyCageAnimal;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTreatmentDataDates;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.SubjectDataEntryDetails;
import com.springmvc.model.SysmexAnimalCode;
import com.springmvc.util.ClinicalObservationRecordTimePoint;
import com.springmvc.util.DailyClinicalObservation;
import com.springmvc.util.DetailedClinicalObservations;
import com.springmvc.util.ExprementalData;
import com.springmvc.util.MotorActivityDataOfIndividualAnimal;
import com.springmvc.util.NeurobehavioralObservationsofIndividual;
import com.springmvc.util.ObservationData;
import com.springmvc.util.OphthalmologicalExamination;
import com.springmvc.util.RecordforMortalityMorbidity;
import com.springmvc.util.SensoryReactivityOfIndividual;

public interface ExpermentalDesignDao {

	List<GroupInfo> studyGroupInfo(StudyMaster sm);

	String saveExpermentalDesign(StudyMaster sm, List<SubGroupInfo> subGroups, 
			List<SubGroupAnimalsInfo> animalInfo, List<SubGroupAnimalsInfoAll> animalInfoAll, 
			StudyDesignStatus sds, ApplicationAuditDetails apad);
	
	List<SubGroupInfo> studySubGroupInfo(StudyMaster sm);

	List<GroupInfo> studyGroupInfoWithChaild(StudyMaster sm);

	public List<SubGroupAnimalsInfo> subGroupAnimalsInfo(SubGroupInfo sgi);
	
	public List<SubGroupInfo> studySubGroupInfo(GroupInfo gi);
	
	public List<Crf> findAllCrfsOfsubGroup(StudyMaster sm, Long subGroupId);
	public List<Crf> findAllActiveCrfsOfsubGroup(StudyMaster sm, Long subGroupId);

	SubGroupInfo subGroupInfoById(StudyMaster sm, Long sibGroupId);

	Crf studyCrf(StudyMaster sm, Long subGroupId, Long crfId);

	SubGroupAnimalsInfo subGroupAnimalsInfo(Long subGroupInfoId);

	int subGroupAnimalsInfoCrfDataCount(SubGroupAnimalsInfoAll animalInfo, StdSubGroupObservationCrfs crf);

	SubGroupAnimalsInfoCrfDataCount saveSubGroupAnimalsInfoCrfDataCount(SubGroupAnimalsInfoAll sgaicdc, StdSubGroupObservationCrfs crf, String userName,
			String deviationMessage, String frequntlyMessage);

	CrfDescrpency studyCrfDescrpencySec(StdSubGroupObservationCrfs animalData, Crf crf,
			SubGroupAnimalsInfo animalInfo, CrfSectionElementData data);

	String studyCrfSectionElementData(SubGroupAnimalsInfoCrfDataCount animalData, CrfSectionElement secEle);

	ObservationData observationData(Long subGroupInfoId, Long crfId, LoginUsers user);

	SubGroupInfo subGroupInfoAllById(StudyMaster sm, Long gorupId, Long id);

	SubGroupAnimalsInfoAll subGroupAnimalsInfoAll(Long subGroupInfoId);


	int subGroupAnimalsInfoCrfDataCountForDuplicate(SubGroupAnimalsInfoAll animal, StdSubGroupObservationCrfs crf, Long sgoupId);

	Map<String, String> weightWithNameValue(Long crfid, Long subGroupAnimalsInfoAllId, String userName);

//	List<GroupInfo> studyGroupInfoWithChaildReview(StudyMaster sm);

	SubGroupInfo subGroupInfoAllByIdReview(StudyMaster sm, Long id);

	List<ExprementalData> subGroupAnimalsInfoWithStudy(StudyMaster sm);

	Map<String, List<StdSubGroupObservationCrfs>> subGroupAnimalsInfoWithStudyCalender(StudyMaster sm);

	ObservationData allObservationData1(StudyMaster sm);

	DepartmentMaster getDepartmentMasterRecord(Long long1);

	StudyDesignStatus getStudyDesignStatusRecord(Long studyId);

	List<SubGroupInfo> getSubGroupInfoList(StudyMaster sm);

	List<SubGroupAnimalsInfoAll> getSubGroupAnimalsInfoAllRecordsList(SubGroupAnimalsInfo sgai);

	String updateExpermentalDesign(List<SubGroupInfo> updateSubList, List<SubGroupAnimalsInfo> updateAnimalList,
			List<SubGroupAnimalsInfoAll> updateanimalInfoAll, StudyDesignStatus sds, ApplicationAuditDetails apad, 
			Map<Long, List<String>> newGsNameMap, Map<Long, List<String>> newSgDoseMap,
			Map<Long, List<String>> newSgConcMap, Map<Long, List<Integer>> newSgCountMap, Map<Long, List<String>> newFromMap,
			Map<Long, List<String>> newToMap, Map<Long, List<String>> newGenderMap, String userName, StudyMaster sm);

	ExpermentalDto getExpermentalDtoDetails(Long studyId);

	List<SubGroupInfo> getSubGroupRecrodsList(GroupInfo g);

	GroupInfo getGroupInfoRecord(Long id);

	boolean saveNewSubGroupDetails(Map<Long, List<String>> newGsNameMap, Map<Long, List<String>> newSgDoseMap,
			Map<Long, List<String>> newSgConcMap, Map<Long, List<Integer>> newSgCountMap, Map<Long, List<String>> newFromMap,
			Map<Long, List<String>> newToMap, Map<Long, List<String>> newGenderMap, String userName, StudyMaster sm);

	List<StdSubGroupObservationCrfs> getSubGroupObservations(Long id, Long subGroupId);

	List<SubGroupAnimalsInfoAll> getsSbGroupAnimalsInfoAll(Long animalId);

	CrfDataEntryDto getCrfDataEntryDetails(Long studyId, Long animalId, Long crfId, Long subGroupId);

	List<SubjectDataEntryDetails> getSubjectDataEntryDetailsList(Set<Long> crfIds, Long id, Long subgroupId);

	List<SubGroupAnimalsInfoAll> getSubGroupAnimalsInfoAllList(Long subGroupId);

	String saveorUpdateSubjectDataEntryDetails(List<StdSubGroupObservationCrfs> updateSdedList,
			List<StdSubGroupObservationCrfs> newSdedList);

	List<Crf> getCrfRecordsList(Set<Long> crfIds);

	SubjectDataEntryDetails getSubjectDataEntryDetailsRecord(SubGroupAnimalsInfoAll animalInfoAll, Long crfId);

	boolean updateSubjectDataEntryDetailsRecord(SubjectDataEntryDetails sded);

	ObservationData observationDataAllAnimal(Long subGroupInfoId, Long stdSubGroupObservationCrfsId, LoginUsers user,
			Long groupId, Long subGroupId);

	List<AmendmentDetails> getAmendmentDetailsRecordsList(Long studyId);

	SubjectDataEntryDetails getSubjectDataEntryDetailsRecord(StdSubGroupObservationCrfs stdCrf, Long studyId, SubGroupAnimalsInfoAll animal, Long subGroupId);

	CrfFormulaDto getFrormulaDataofCurrentCrf(Long id, Long studyId);

	List<SubGroupAnimalsInfoAll> getSubGroupAnimalsInfoAllRecordsList(Long subGroupId);

	List<GroupInfo> studyGroupInfoWithSubGroup(StudyMaster sm);

	List<SubGroupAnimalsInfo> allSubGroupAnimalsInfos(Long studyId);

	Long subGroupId(Long groupId);

	StdSubGroupObservationCrfs stdSubGroupObservationCrfsWithId(Long stdSubGroupObservationCrfsId);

	StudyCageAnimal nextAnimalForObservationData(Long stdSubGroupObservationCrfsId, Long cageId, Long studyId, Long subGroupId);

	List<AnimalCage> animalCages(Long studyId);

	List<StudyCageAnimal> studyCageAnimal(Long studyId, Long cageId);

	StudyAnimal studyAnimalWithId(Long animalId);

	StudyCageAnimal studyCageAnimalWithId(Long studyCageAnimalId);

	SubGroupAnimalsInfo subGroupAnimalsInfo(Long id, SubGroupInfo subGroupInfo, String gender);

	List<StdSubGroupObservationCrfs> stdSubGroupObservationCrfs(List<Long> crfIds, Long id, Long subGroupId);

	Map<Long, DepartmentMaster> separtmentMasters(List<Long> deptIds);

	boolean checkDataEntryEligible(StudyMaster sm);

	AnimalCage cages(Long cageId);

	List<AnimalCage> groupAnimalCages(Long studyId, Long subGroupId);

	List<SysmexAnimalCode> sysmexAnimalCodes();

	StdSubGroupObservationCrfs getStudyTreatmentConfigList(Long id, Long id2, Long subGroupId);

	void saveStudyTreatmentData(StdSubGroupObservationCrfs sad);

	void updateStudyTreatmentData(StdSubGroupObservationCrfs sad);

	boolean updateStudyTreatmentDataDatesDetails(Long studyTreatmentDataDatesId, String value, Long userId);

	boolean removeStudyTreatmentDataDatesDetails(Long studyTreatmentDataDatesId, Long userId);

	List<StdSubGroupObservationCrfs> stdSubGroupObservationCrf(Long studyId, Long userRoleId);

	StudyAnimal nextAnimalForTreatmentData(Long studyTreatmentDataDatesId, Long stdSubGroupObservationCrfsId, Long studyId, Long crfId,
			String seletedGender, int noOfEntry, String selecteDate);

	List<StudyAnimal> studyAnimalWithGender(Long studyId, String gender);

	StudyTreatmentDataDates studyTreatmentDataDates(Long id, boolean b, Date fromDate, String seletedGender);

	List<StudyAnimal> studyAnimal(Long studyId);

	StudyTreatmentDataDates studyTreatmentDataDates(Long studyTreatmentDataDatesId);

	List<SubGroupInfo> studySubGroupInfo(Long groupId);

	Long subGrouId(Long groupId);

	List<SubjectDataEntryDetails> unscheduelTreatmentData(StudyMaster sm);

	List<StdSubGroupObservationCrfs> stdSubGroupObservationCrfs(Long studyId);

	StdSubGroupObservationCrfs stdSubGroupObservationCrfsById(Long observationId);

	List<String> allDaysOfClinicalObservations(Long crfSectionElementId, Long stdSubGroupObservationCrfsId);

	DailyClinicalObservation dailyClinicalObservation(CrfSectionElement dayElement, CrfSectionElement valueElement,
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs, List<StudyAnimal> animals);

	StudyAcclamatizationData studyAcclamatizationData(Long observationId);

	DailyClinicalObservation acclamatiztionDailyClinicalObservation(CrfSectionElement dayElement,
			CrfSectionElement valueElement, StudyAcclamatizationData studyAcclamatizationData,
			List<StudyAnimal> animals);

	Map<Integer, ClinicalObservationRecordTimePoint> clinicalObservationRecordTimePoint(CrfSectionElement timePointElement,
			CrfSectionElement observationElement, CrfSectionElement timeHrElement, StdSubGroupObservationCrfs stdSubGroupObservationCrfs, 
			List<StudyAnimal> animals);

	List<RecordforMortalityMorbidity> recordforMortalityMorbidity(CrfSectionElement mortalityElement,
			CrfSectionElement morbidityElement, CrfSectionElement mortalitySelect1Element,
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs);

	List<RecordforMortalityMorbidity> acclamatizationrecordforMortalityMorbidity(CrfSectionElement mortalityElement,
			CrfSectionElement morbidityElement, CrfSectionElement mortalitySelect1Element,
			StudyAcclamatizationData studyAcclamatizationData);

	DetailedClinicalObservations detailedClinicalObservation(CrfSectionElement valueElement,
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs, List<StudyAnimal> animals);

	DetailedClinicalObservations acclamatizationDetailedClinicalObservation(CrfSectionElement valueElement,
			StudyAcclamatizationData studyAcclamatizationData, List<StudyAnimal> animals);

	OphthalmologicalExamination acclamatizationOphthalmologicalExamination(CrfSectionElement leftEyeElement,
			CrfSectionElement rightEyeElement, StudyAcclamatizationData studyAcclamatizationData,
			List<StudyAnimal> animals);

	OphthalmologicalExamination ophthalmologicalExamination(CrfSectionElement leftEyeElement,
			CrfSectionElement rightEyeElement, StdSubGroupObservationCrfs stdSubGroupObservationCrfs,
			List<StudyAnimal> animals);

	List<NeurobehavioralObservationsofIndividual> neurobehavioralObservationsofIndividual(CrfSectionElement posture,
			CrfSectionElement convulsion, CrfSectionElement handiling, CrfSectionElement easeOfRemove,
			CrfSectionElement palpebral, CrfSectionElement lacrimation, CrfSectionElement eyeExamination,
			CrfSectionElement piloerection, CrfSectionElement salivation, CrfSectionElement mobility,
			CrfSectionElement gait, CrfSectionElement respiration, CrfSectionElement arousal,
			CrfSectionElement clonicMovement, CrfSectionElement tonicMovement, CrfSectionElement stereotypy,
			CrfSectionElement bizzareBehaviour, CrfSectionElement vocalisationNo, CrfSectionElement noofRears,
			CrfSectionElement urinePoolsNo, CrfSectionElement faecalBolusNo,
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs, List<StudyAnimal> animals);

	List<SensoryReactivityOfIndividual> sensoryReactivityOfIndividual(CrfSectionElement approachResponse,
			CrfSectionElement touchResponse, CrfSectionElement clickResponse, CrfSectionElement tailPinchResponse,
			CrfSectionElement pupilResponse, CrfSectionElement airRightingReflex, StdSubGroupObservationCrfs stdSubGroupObservationCrfs, List<StudyAnimal> animals);

	MotorActivityDataOfIndividualAnimal motorActivityDataOfIndividualAnimal(CrfSectionElement total,
			CrfSectionElement ambulatory, CrfSectionElement stereotypic,
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs, List<StudyAnimal> animals);

	
	
	
	
}
