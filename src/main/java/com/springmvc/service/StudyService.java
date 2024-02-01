package com.springmvc.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

import com.covide.crf.dto.CrfDescrpencyAudit;
import com.covide.crf.dto.CrfMetaDataStd;
import com.covide.crf.dto.ObservationClinPathTestCodesDto;
import com.covide.crf.dto.StagoDataDto;
import com.covide.crf.dto.VistrosDataDto;
import com.covide.dto.SatusAndWorkFlowDetailsDto;
import com.covide.dto.SysmexDataDto;
import com.covide.template.dto.StagoDataAnimalDto;
import com.covide.template.dto.SysmexDataUpdateDto;
import com.covide.template.dto.SysmexDto;
import com.covide.template.dto.VistrosDataUpdateDto;
import com.itextpdf.text.DocumentException;
import com.springmvc.model.DepartmentMaster;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.PeriodCrfs;
import com.springmvc.model.PhysicalWeightBalanceData;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.Species;
import com.springmvc.model.SponsorMaster;
import com.springmvc.model.StagoData;
import com.springmvc.model.StaticData;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyGroup;
import com.springmvc.model.StudyGroupClass;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.StudySite;
import com.springmvc.model.StudySiteSubject;
import com.springmvc.model.StudyStatusAudit;
import com.springmvc.model.StudyTestCodes;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.SubjectStatus;
import com.springmvc.model.SysmexData;
import com.springmvc.model.SysmexTestCode;
import com.springmvc.model.TestCodeProfileParameters;
import com.springmvc.model.VitrosData;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.model.VolunteerPeriodCrfStatus;

public interface StudyService {

	List<StudyMaster> findAll();

	StudyMaster findByStudyId(Long studyId);

	StudyMaster findByStudyNo(String studyNo);

	StudyMaster findByStudyNo(String studyNo, Long id);

	boolean saveStudyMaster(StudyMaster studyMaster, String username);

	void updateStudy(StudyMaster sm);

	List<StudyPeriodMaster> allStudyPeriods(StudyMaster sm);

	List<StudyPeriodMaster> allStudyPeriodsWithSubEle(StudyMaster sm);

	List<PeriodCrfs> periodCrfs(Long periodId);

	StudyPeriodMaster studyPeriodMaster(Long periodId);

	void upatePeriodCrfsList(List<PeriodCrfs> pcrfsupdate);

	void savePeriodCrfsList(List<PeriodCrfs> pcrfsSave);

	void upatePeriodCrfs(StudyPeriodMaster sp);

	void saveVolList(List<Volunteer> vlist);

	void saveVolunteerPeriodCrf(List<VolunteerPeriodCrf> vpclist);

	List<Volunteer> studyVolunteerList(StudyMaster sm);

	List<VolunteerPeriodCrf> volunteerPeriodCrfList(Long peiodId);

	void saveVolunteerPeriodCrfStatusList(List<VolunteerPeriodCrfStatus> vpclist);

	List<VolunteerPeriodCrfStatus> volunteerPeriodCrfStatusList(Long peiodId);

	List<VolunteerPeriodCrf> volunteerPeriodCrfList(StudyPeriodMaster sp, Volunteer v);

	VolunteerPeriodCrfStatus volunteerPeriodCrfStatus(StudyPeriodMaster sp, Volunteer v);

	Volunteer volunteer(Long volId);

	VolunteerPeriodCrf volunteerPeriodCrf(Long vpcId);

	CrfMetaDataStd crfMetaDataStd(int crfId);

	void updateVolunteerPeriodCrfStatus(VolunteerPeriodCrfStatus vpcs);

	List<PeriodCrfs> periodCrfList(StudyPeriodMaster sp);

	void updateVolunteerPeriodCrf(VolunteerPeriodCrf vpc);

	String crateSite(StudyMaster sm, HttpServletRequest request, String username);

	List<StudySite> studySite(StudyMaster activeStudy);

	StudySite studySiteId(Long siteId);

	List<Volunteer> studyVolunteerListWithSite(StudyMaster sm, StudySite site);

	List<StudyGroup> studyGroup(StudyMaster sm);

	String crateGrop(StudyGroup gorup);

	List<StudyGroupClass> studyGroupClass(StudyMaster sm);

	String crateGroupClass(StudyGroupClass group);

	StudyGroupClass studyGroupClassById(long parseLong);

	String createSubject(StudySiteSubject subject, List<Long> groups);

	String scheduleSubject(Volunteer vol, StudyPeriodMaster sp, String userName, String startDate);

	void saveSubjectStatus(SubjectStatus ss);

	SubjectStatus subjectStatus(StudyPeriodMaster period, Volunteer vol);

	void saveStudyStatusAudit(StudyStatusAudit audit);

	List<SubjectStatus> subjectStatusList(StudyMaster sm);

	boolean checkGroupNameExistOrNot(Long groupclass, String groupName);

	boolean checkGroupClassNameExistOrNot(StudyMaster sm, String groupName);

	boolean checkSiteNameExistOrNot(StudyMaster sm, String siteName);

	List<StudySiteSubject> studySiteSubjectList(StudyMaster sm);

	StudySiteSubject studySiteSubject(Long id);

	List<VolunteerPeriodCrfStatus> volunteerPeriodCrfStatus(Volunteer vol);

	List<CrfDescrpencyAudit> studyCrfDescrpencyAudit(Volunteer vol);

	List<SubjectStatus> subjectStatusForVol(Volunteer vol);

	boolean updateStudyMaster(StudyMaster sm, String username);

	PhysicalWeightBalanceData savePhysicalWeightBalanceData(PhysicalWeightBalanceData obj);

	List<PhysicalWeightBalanceData> physicalWeightBalanceDataList();

	SubGroupInfo stdGroupInfo(Long subGroupId);

	String saveStudyMasterRecord(StudyMaster studyMaster, String username, Long userId, List<LoginUsers> userIds);

	List<LoginUsers> getStudyDirectorList();

	Map<String, List<StudyMaster>> getUserAssignedStudiesList();

	List<StudyMaster> getStudyMasterListBasedOnLogin(Long userId);

	StudyMaster getStudyMasterRecord(Long studyId);

	String saveStudyMetaDataDetails(StudyMaster studyMaster, String username, List<GroupInfo> groupInfos,
			List<SubGroupInfo> subGroups, List<SubGroupAnimalsInfo> animalInfo);

	List<StudyMaster> getAllStudyDesignStatusStudiesList(Long userId);

	List<StudyMaster> getStudyMasterListForUpdation();

	String updateStudyMasterRecord(StudyMaster sm, String userName);

	LoginUsers getLoginUsersWithId(Long sdUser);

	List<StudyMaster> getStudyMasterForSearch(String studynoval, String studyNameval, String sponsorval,
			String statusval, String roleval);

	SatusAndWorkFlowDetailsDto SatusAndWorkFlowDetailsDto(String string, String string2);

	List<DepartmentMaster> getDepartmentMasterList();

	com.covide.dto.SatusAndWorkFlowDetailsDto satusAndWorkFlowDetailsDtoForUpdation(Long studyId, String string,
			String string2);

	StatusMaster getStatusMasterRecord(String newStatus);

	String updateStudyMetaDataDetails(StudyMaster studyMaster, String username, List<GroupInfo> existsGroupList,
			List<GroupInfo> newgroupList, Integer groups);

	RoleMaster getRoleMasterRecord(Long long1);

	long saveRoleMasterRecord(String username, RoleMaster role);

	Map<Long, String> getStudyDirectorsDetails(List<StudyMaster> allStudies);

	StatusMaster statusMaster(String string);

	List<StaticData> staticDatas(String fieldName);

	StudyDesignStatus studyDesignStatus(Long studyId);

	List<StudyMaster> getStudyMasterListBasedOnLoginVeiw(Long userId);

	List<InstrumentIpAddress> instrumentIpAddresses(StatusMaster activeStatus, boolean withActiveStatus);

	InstrumentIpAddress instrumentIpAddress(Long ipaddress);

	SysmexDto saveSysmexData(SysmexDto dto);

	List<SysmexData> sysmexDataList();

	SysmexDataDto sysmexDataDtoList(Long studyId, List<SysmexData> sysmexDataList, String exprotType,
			String observation);

//	File exportSysmexDataToExcel(HttpServletRequest request) throws IOException, ParseException;

	List<SysmexData> sysmexDataList(Long studyNumber, String startDate, String sampleType, String observation)
			throws ParseException;

	File exportSysmexDataToExcel(HttpServletRequest request, Long studyNumber, String startDate, String sampleType,
			String observation) throws ParseException, IOException, ParseException;

	List<StagoDataDto> stagoDataList();

	Object stagoStudyNumbers();

	File exportStagoDataToExcel(HttpServletRequest request, Long studyNumber, String startDate, String sampleType,
			String observation) throws ParseException, IOException;

	List<StagoDataDto> stagoDataList(Long studyNumber, String startDate, Boolean export, String sampleType,
			String observation) throws ParseException;

	Object sysmaxStudyNumbers();

	List<SysmexTestCode> sysmexTestCodes(boolean b);

	InstrumentIpAddress instrumentIpAddress();

	String exportSysmexDataToPdf(HttpServletRequest request, HttpServletResponse response, Long sysmexDataId,
			String animalNo, String realPath, String startDate, String sampleType, String observation)
			throws ParseException, IOException, DocumentException;

	List<VistrosDataDto> vistrosDataList(SortedMap<Integer, String> testCodes);

	List<VistrosDataDto> vistrosDataList(String studyNumber, String startDate, SortedMap<Integer, String> testCodes)
			throws ParseException;

	File exportVistrosDataToExcel(HttpServletRequest request, Long studyId, String startDate, String sampleType,
			String observation) throws ParseException, IOException;

	boolean instrumentIpAddressesStatus(List<Long> insturmentId);

	List<StagoData> stagoDataList(Long studyId, String animalNo, String testName, Long animalId, String sampleType,
			String observation, String stagostDate) throws ParseException;

	String stagoDataSave(Long studyId, String animalNo, String testName, Long finalResultId, Long animalId,
			String rerunCommnet, String sampleType, String observation, String stagostDate) throws ParseException;

	InstrumentIpAddress instrumentIpAddress(String string);

	List<StudyTestCodes> studyInstumentTestCodes(Long id, Long studyId, String observation);

	List<StudyMaster> allActiveStudys();

	VistrosDataDto vitrosDataForExcel(Long studyId, InstrumentIpAddress ip, String startDate, String sampleType,
			String observation);

	String exportVitroDataToPdf(HttpServletRequest request, HttpServletResponse response, String animalId,
			String realPath, Long studyId, String startDate, String sampleType, String observation)
			throws IOException, DocumentException;

	String exportStagoDataToPdf(HttpServletRequest request, HttpServletResponse response, Long studyId,
			List<Long> animalNo, String realPath, String startDate, String sampleType, String observation)
			throws ParseException, IOException, DocumentException;

	SortedMap<Integer, StudyAnimal> studyAnimals(Long studyId);

	VistrosDataUpdateDto vitrosDataList(Long studyId, String animalNo, int columnIndex);

	String vitrosResultSelectionSave(Long studyId, String animalNo, String testName, int columnIndex,
			Long finalResultId, String rerunCommnet);

	SysmexDataUpdateDto sysmexDataList(Long studyId, String animalNo, int columnIndex, String observation, String stagostDate) throws ParseException;

	String sysmexResultSelectionSave(Long studyId, String animalNo, String testName, int columnIndex,
			Long finalResultId, String rerunCommnet, String observation, String stagostDate) throws ParseException;

	List<VitrosData> onlineVitrosData();

	void updateAsOnline(List<VitrosData> data);

	List<StagoDataAnimalDto> studyStagoData(Long studyNumber, String startDate, boolean b, String sampleType,
			List<StudyTestCodes> testCodes) throws ParseException;

	SysmexDataDto sysmexDataDtoTable(Long studyNumber, List<SysmexData> sysmexDataList, String string);

	boolean studyMetaDataConfigured(Long studyId);

	List<Species> speciesList();

	String saveSponsorMaster(SponsorMaster sponsorMaster);

	SponsorMaster getSponsorMasterUniqueCheckCode(String data);

	Object sponsorList();

	List<String> insturmentResultSelectionDates(Long studyId, String insturment);

	StudyAnimal studyAnimalsById(Long animalId);

	String updateAceeptMetdetaStatus(StudyMaster sm);

	List<StudyMaster> allNewActiveStudysOfStudyDirector(Long id);

	List<StudyMaster> allActiveStudysOfStudyDirector(Long userId);

	List<RoleMaster> allRoleMasters();

	ModelMap addIntrumentAndPerameters(Long studyId, ModelMap model, HttpServletRequest request);

	/*
	 * get Static Data with Code
	 */
	StaticData staticDataByCode(String code);

	ModelMap addIntrumentAndPerametersForStudyAcclamatizationDates(Long studyAcclamatizationDatesId, ModelMap model,
			HttpServletRequest request, String obserVationFor);

	List<ObservationClinPathTestCodesDto> dbIntrumentAndPerametersForStudyAcclamatizationDates(
			Long studyAcclamatizationDatesId);

	List<TestCodeProfileParameters> testCodeProfileParametersList(Long standerdParamId);

	List<StudyAnimal> studyAnimals(String gender, boolean randamized);

	String sendStudyFroReivew(Long studyId, String username, String commentForReview);

	List<StudyMaster> allReivewActiveStudys();

}
