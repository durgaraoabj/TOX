package com.springmvc.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import com.covide.crf.dto.CrfDescrpencyAudit;
import com.covide.crf.dto.CrfMetaDataStd;
import com.covide.crf.dto.StagoDataDto;
import com.covide.crf.dto.VistrosDataDto;
import com.covide.dto.SatusAndWorkFlowDetailsDto;
import com.covide.template.dto.SysmexDataUpdateDto;
import com.covide.template.dto.SysmexDto;
import com.covide.template.dto.VistrosDataUpdateDto;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.DepartmentMaster;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.ObservationInturmentConfiguration;
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
import com.springmvc.model.StudyMasterLog;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.StudySite;
import com.springmvc.model.StudySitePeriodCrfs;
import com.springmvc.model.StudySiteSubject;
import com.springmvc.model.StudySiteSubjectGroup;
import com.springmvc.model.StudyStatusAudit;
import com.springmvc.model.StudyTestCodes;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.SubjectStatus;
import com.springmvc.model.SysmexData;
import com.springmvc.model.SysmexTestCode;
import com.springmvc.model.SysmexTestCodeData;
import com.springmvc.model.UserWiseStudiesAsignMaster;
import com.springmvc.model.VitrosData;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.model.VolunteerPeriodCrfStatus;

public interface StudyDao {

	List<StudyMaster> findAll();

	StudyMaster findByStudyId(Long studyId);

	StudyMaster findByStudyNo(String studyNo);

	StudyMaster findByStudyNo(String studyNo, Long id);

	StudyMaster saveStudyMaster(StudyMaster studyMaster);

	void updateStudy(StudyMaster sm);

	List<StudyPeriodMaster> allStudyPeriods(StudyMaster sm);

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

	List<VolunteerPeriodCrf> volunteerPeriodCrfList(StudyPeriodMaster sp, Volunteer vol);

	VolunteerPeriodCrfStatus volunteerPeriodCrfStatus(StudyPeriodMaster sp, Volunteer v);

	Volunteer volunteer(Long volId);

	VolunteerPeriodCrf volunteerPeriodCrf(Long vpcId);

	CrfMetaDataStd crfMetaDataStd(int crfId);

	void updateVolunteerPeriodCrfStatus(VolunteerPeriodCrfStatus vpcs);

	List<PeriodCrfs> periodCrfList(StudyPeriodMaster sp);

	void updateVolunteerPeriodCrf(VolunteerPeriodCrf vpc);

	List<StudyPeriodMaster> allStudyPeriodsWithSubEle(StudyMaster sm);

	String saveSite(StudySite site, List<StudySitePeriodCrfs> list);

	int maxSiteNo(StudyMaster sm);

	List<StudySite> studySite(StudyMaster sm);

	List<StudyGroup> studyGroup(StudyMaster sm);

	StudySite studySiteId(Long siteId);

	List<Volunteer> studyVolunteerListWithSite(StudyMaster sm, StudySite site);

	String saveGroup(StudyGroup group);

	StudyGroup studyGroupById(Long parseInt);

	List<StudyGroupClass> studyGroupClass(StudyMaster sm);

	String saveGroupClass(StudyGroupClass group);

	StudyGroupClass studyGroupClassById(long id);

	List<StudyGroup> studyGroupByIds(List<Long> ids);

	StudyPeriodMaster studyPeriodMaster(StudyMaster studyMaster, String string, int i);

	String saveStudySiteSubject(StudySiteSubject subject, List<StudySiteSubjectGroup> subjectGroups, Volunteer vol,
			List<VolunteerPeriodCrfStatus> vpcslist, List<VolunteerPeriodCrf> vpclist);

	String scheduleSubject(Volunteer vol, List<VolunteerPeriodCrfStatus> vpcslist, List<VolunteerPeriodCrf> vpclist);

	void saveSubjectStatus(SubjectStatus ss);

	SubjectStatus subjectStatus(StudyPeriodMaster period, Volunteer vol);

	void saveStudyStatusAudit(StudyStatusAudit audit);

	List<SubjectStatus> subjectStatusList(StudyMaster sm);

	StudyGroup checkGroupNameExistOrNot(Long groupclass, String groupName);

	StudyGroupClass checkGroupClassNameExistOrNot(StudyMaster sm, String groupName);

	StudySite checkSiteNameExistOrNot(StudyMaster sm, String siteName);

	List<StudySiteSubject> studySiteSubjects(StudySite ss);

	List<VolunteerPeriodCrfStatus> volunteerPeriodCrfStatusList(List<StudyPeriodMaster> plist);

	List<VolunteerPeriodCrfStatus> volunteerPeriodCrfStatus(Volunteer vol);

	List<SubjectStatus> subjectStatusList(StudySite ss);

	List<StudySiteSubject> studySiteSubjectList(StudyMaster sm);

	StudySiteSubject studySiteSubject(Long id);

	List<CrfDescrpencyAudit> studyCrfDescrpencyAudit(Volunteer vol);

	List<SubjectStatus> subjectStatusForVol(Volunteer vol);

	void saveStudyLog(StudyMasterLog log);

	PhysicalWeightBalanceData savePhysicalWeightBalanceData(PhysicalWeightBalanceData obj);

	List<PhysicalWeightBalanceData> physicalWeightBalanceDataList();

	SubGroupInfo stdGroupInfo(Long subGroupId);

	long saveStudyMasterRecord(StudyMaster studyMaster, List<UserWiseStudiesAsignMaster> list);

	List<LoginUsers> getStudyDirectorList();

	LoginUsers getEmployeeMasterRecord(Long userId);

	List<StudyMaster> getUserAssignedStudiesList();

	List<StudyMaster> getStudyMasterListBasedOnLogin(Long userId);

	StudyMaster getStudyMasterRecord(Long studyId);

	boolean updateStudyMetaDataDetails(StudyMaster studyMaster, StudyMasterLog smLog, List<GroupInfo> groupInfos,
			List<SubGroupInfo> subGroups, List<SubGroupAnimalsInfo> animalInfo);

	List<StudyMaster> getAllStudyDesignStatusStudiesList(Long userId);

	List<StudyMaster> getStudyMasterListForUpdation();

	boolean updateStudyMaterRecord(StudyMaster stdPojo, StudyMasterLog smLog);

	LoginUsers getLoginUsersWithId(Long sdUser);

	List<StudyMaster> getStudyMasterForSearch(String studynoval, String studyNameval, String sponsorval,
			String statusval, String roleval);

	SatusAndWorkFlowDetailsDto SatusAndWorkFlowDetailsDto(String statusCode, String moduleName);

	List<DepartmentMaster> getDepartmentMasterList();

	com.covide.dto.SatusAndWorkFlowDetailsDto getstatusAndWorkFlowDetailsDtoForUpdation(Long studyId, String string,
			String string2);

	StatusMaster getStatusMasterRecord(String newStatus);

	boolean updateStudyMetaDataRecords(StudyMaster smPojo, StudyMasterLog smLog, List<GroupInfo> existsGroupList,
			List<GroupInfo> newgroupList, String username, Integer groups);

	com.covide.dto.SatusAndWorkFlowDetailsDto getSatusAndWorkFlowDetailsDtoDetails(Long id, String string,
			String string2);

	boolean saveUpdateStdyDesignStatus(StudyDesignStatus sds, ApplicationAuditDetails aad);

	RoleMaster getRoleMasterRecord(Long roleId);

	long saveRoleMasterRecord(String username, RoleMaster role);

	List<LoginUsers> getLoginUserRecordsList(Set<Long> sdIds);

	StatusMaster statusMaster(String string);

	List<StaticData> staticDatas(String fieldName);

	StaticData staticDatasById(long id);

	StudyDesignStatus studyDesignStatus(Long studyId);

	List<StudyMaster> getStudyMasterListBasedOnLoginVeiw(Long userId);

	List<InstrumentIpAddress> instrumentIpAddresses(Long activeStatusId, boolean withActiveStatus);

	InstrumentIpAddress instrumentIpAddress(Long ipaddress);

	SysmexDto saveSysmexData(SysmexDto dto);

	List<SysmexData> saveSysmexData();

	List<SysmexData> sysmexDataList(Long studyNumber, String startDate, String sampleType, String observation)
			throws ParseException;

	List<StagoDataDto> stagoData();

	List<StagoDataDto> stagoDataList(Long studyNumber, String startDate, Boolean export, String sampleType,
			String observation) throws ParseException;

	List<StudyMaster> allStagoStudyNumbers();

	List<StudyMaster> allSysmaxStudyNumbers();

	List<SysmexTestCode> sysmexTestCodes(boolean b);

	InstrumentIpAddress instumentIpAddress();

	SysmexData sysmexDataById(Long sysmexDataId);

	List<VistrosDataDto> vistrosData(SortedMap<Integer, String> testCodes);

	List<VistrosDataDto> vistrosDataList(String studyNumber, String startDate, SortedMap<Integer, String> testCodes)
			throws ParseException;

	boolean instrumentIpAddressesStatus(List<Long> insturmentId);

	List<StagoData> stagoDataList(Long studyId, String animalNo, String testName, Long animalId, String sampleType,
			String observation, String stagostDate) throws ParseException;

	void updateStagoDataList(List<StagoData> list);

	InstrumentIpAddress instumentIpAddress(String name);

	List<StudyTestCodes> studyInstumentTestCodes(Long id, Long studyId, String observation);

	List<StudyMaster> allActiveStudys();

	List<VitrosData> vitrosRawDataList(String studySuffiex, Date from, Date to, String animal,
			List<String> testcodesset);

	List<VitrosData> vitrosRawDataListWithAnimalNo(String animalNo, Date from, Date to, String studySuffiex);

	List<StudyAnimal> studyAnimals(Long studyId);

	Map<String, StagoDataDto> stagoDataList(Long studyId, List<Long> animalIds, Boolean export, String startDate,
			String sampleType, String observation) throws ParseException;

	VistrosDataUpdateDto vitrosDataList(Long studyId, String animalNo, int columnIndex);

	List<VitrosData> vitrosDataListData(Long studyId, String animalNo, String testName);

	void updateVitrosDataList(List<VitrosData> list);

	SysmexDataUpdateDto sysmexDataList(Long studyId, String animalNo, int columnIndex, String observation, String stagostDate) throws ParseException;

	List<SysmexTestCodeData> sysmexDataListData(Long studyId, String animalNo, List<StudyTestCodes> tc, String observation, String startDate) throws ParseException;

	List<SysmexTestCodeData> sysmexDataListData(Long studyId, String animalNo, String testName, String observation, String startDate) throws ParseException;

	void updateSysmexDataList(List<SysmexTestCodeData> list);

	List<VitrosData> onlineVitrosData();

	void updateAsOnline(List<VitrosData> data);

	List<StagoData> studyStagoData(Long studyNumber, String startDate, boolean b, String sampleType)
			throws ParseException;

	boolean studyMetaDataConfigured(Long studyId);

	List<Species> speciesList();

	String saveSponsorMaster(SponsorMaster sponsorMaster);

	SponsorMaster getSponsorMasterUniqueCheckCode(String data);

	Object sponsorList();

	List<String> insturmentResultSelectionDates(Long studyId, String insturment);

	List<VitrosData> vitrosRawDataListWithStudyAndDate(Long studyId, Date from, Date to, String object,
			List<String> testcodesset, String sampleType, String observation);

	StudyAnimal studyAnimalsById(Long animalId);

	String updateAceeptMetdetaStatus(StudyMaster sm);

	List<StudyMaster> allNewActiveStudysOfStudyDirector(Long userId);

	List<StudyMaster> allActiveStudysOfStudyDirector(Long userId);

	List<RoleMaster> allRoleMasters();

	/*
	 * get Static Data with Code
	 */
	StaticData staticDataByCode(String code);

	void updateAnimal(StudyAnimal animal);

	List<SubGroupInfo> allSubGroupInfo(Long id);

	int maxAnimalPerminentNo(Long id);

	List<StudyAnimal> studyAnimals(String gender, boolean randamized);

	List<StudyTestCodes> studyObservationInstumentTestCodes(
			ObservationInturmentConfiguration observationInturmentConfiguration, Long id, Long studyId);

	void sendStudyFroReivew(Long studyId, String username, String commentForReview);

	List<StudyMaster> allReivewActiveStudys();

}
