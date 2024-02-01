package com.covide.crf.dao;

import java.util.List;
import java.util.Map;

import com.covide.crf.dto.CRFGroupItemStd;
import com.covide.crf.dto.CRFItemValuesStd;
import com.covide.crf.dto.CRFSectionsStd;
import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfDateComparison;
import com.covide.crf.dto.CrfDescrpency;
import com.covide.crf.dto.CrfDescrpencyAudit;
import com.covide.crf.dto.CrfDescrpencyLog;
import com.covide.crf.dto.CrfEleCaliculation;
import com.covide.crf.dto.CrfGroup;
import com.covide.crf.dto.CrfGroupDataRows;
import com.covide.crf.dto.CrfGroupElement;
import com.covide.crf.dto.CrfGroupElementData;
import com.covide.crf.dto.CrfItemsStd;
import com.covide.crf.dto.CrfMapplingTable;
import com.covide.crf.dto.CrfMapplingTableColumn;
import com.covide.crf.dto.CrfMapplingTableColumnMap;
import com.covide.crf.dto.CrfMetaData;
import com.covide.crf.dto.CrfMetaDataStd;
import com.covide.crf.dto.CrfRule;
import com.covide.crf.dto.CrfSectionElement;
import com.covide.crf.dto.CrfSectionElementData;
import com.covide.crf.dto.CrfSectionElementDataAudit;
import com.covide.crf.dto.CrfSectionElementInstrumentValue;
import com.covide.crf.dto.DTNAME;
import com.covide.crf.dto.DataSet;
import com.covide.crf.dto.DataSetPhasewiseCrfs;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.ObservationRole;
import com.springmvc.model.ReviewLevel;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StdSubGroupObservationCrfsLog;
import com.springmvc.model.StudyAccessionCrfDescrpency;
import com.springmvc.model.StudyAccessionCrfDescrpencyLog;
import com.springmvc.model.StudyAccessionCrfSectionElementData;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCountReviewLevel;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.SubjectDataEntryDetails;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.model.WorkFlowReviewAudit;

public interface CrfDAO {

	List<CrfMetaData> findAll();

	public boolean saveCrf(CrfMetaData crf, DTNAME dt);

	List<CrfMetaDataStd> findAllStdCrfs(StudyMaster sm);

	void savestdCrf1(CrfMetaDataStd scrf);

	void updatestdCrf(CrfMetaDataStd scrf);

	void updatestdCrf(List<CrfMetaDataStd> stdcrfsUpdate);

	void savestdCrfSections1(CRFSectionsStd s);

	void savestdCrfEle1(CrfItemsStd si);

	void savestdCrfEleVal(List<CRFItemValuesStd> ivlist);

	void savestdCrfGroups1(CRFGroupItemStd sg);

	public List<Crf> findAllCrfs();

	void saveCrf(Crf crf);

	Crf getCrf(Long crfId);

	void updateCrf(Crf crf);

	List<Crf> findAllCrfsWithSubElemens();

	Crf getCrfForView(Long crfId);

	CrfGroup studyGroupFull(Long groupId);

	void saveStudySectionData2(List<CrfSectionElementData> sectionData, SubjectDataEntryDetails sded);

	void saveStudyGroupData(List<CrfGroupElementData> groupoData);

	void updateVpc(VolunteerPeriodCrf vpc);

	Map<String, CrfSectionElementData> studyCrfSectionElementDataList(VolunteerPeriodCrf vpc);

	Map<String, CrfGroupElementData> studyCrfGroupElementDataList(VolunteerPeriodCrf vpc);

	void saveCrfGroupDataRows(List<CrfGroupDataRows> groupoInfo);

	Map<Long, CrfGroupDataRows> crfGroupDataRows(VolunteerPeriodCrf vpc);

	CrfSectionElementData studyCrfSectionElementData(String keyName, VolunteerPeriodCrf vpc, CrfSectionElement ele);

	CrfGroupElementData studyCrfGroupElementData(String keyName, VolunteerPeriodCrf vpc, CrfGroupElement ele);

	com.covide.crf.dto.CrfGroupDataRows studyCrfGroupDataRows(VolunteerPeriodCrf vpc, CrfGroup group);

	void saveStudySectionDataUpdate(List<CrfSectionElementData> sectionDataUpdate);

	void saveStudyGroupDataUpdate(List<CrfGroupElementData> groupoDataUpdate);

	void saveCrfGroupDataRowsUpdate(List<CrfGroupDataRows> groupoInfoUpdate);

	List<CrfSectionElement> sectionElemets(Long id);

	List<CrfGroupElement> groupElemets(Long id);

	CrfSectionElement sectionElement(Long parseLong);

	CrfGroupElement groupElement(Long parseLong);

	String saveCrfRule(CrfRule rule);

	List<CrfRule> findAllCrfRules();

	CrfRule crfRule(Long id);

	void updateCrfRule(CrfRule rule);

	List<Crf> findAllActiveCrfs();

	List<CrfRule> crfRuleWithCrf(Crf crf);

	List<Crf> findAllStudyCrfsWithSubElements(StudyMaster sm);

	void saveCrfRule(List<CrfRule> stdcrfsRulesave);

	List<CrfRule> crfRuleWithCrfAndSubElements(Crf crf);

	List<CrfRule> crfRuleOfSubElements(Crf crf, List<Long> sectionIds);

	List<CrfRule> studyCrfRuleWithCrfAndSubElementsWithSecEleId(Long id, String type);

	List<CrfRule> studyCrfRuleWithCrfAndSubElementsWithGroupEleId(Long id);

	String studyCrfSectionElementData(VolunteerPeriodCrf vpc, CrfSectionElement secEle);

	String studyCrfGroupElementData(VolunteerPeriodCrf vpc, CrfGroupElement groupEle, String string);

	CrfDescrpency studyCrfDescrpencySec(Crf crf, VolunteerPeriodCrf vpc, CrfSectionElementData data);

	CrfDescrpency studyCrfDescrpencyGroup(Crf crf, VolunteerPeriodCrf vpc, CrfGroupElementData data);

	void saveStudyCrfDescrpencyList(List<CrfDescrpency> desc);

	List<CrfDescrpency> userdiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc);

	List<CrfDescrpency> reviewerdiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc);

	List<CrfDescrpency> closeddiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc);

	List<CrfDescrpency> onHolddiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc);

	List<CrfDescrpency> alldiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc);

	List<CrfDescrpency> opendiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc);

	CrfSectionElement studyCrfSectionElement(Long id);

	Crf studyCrf(Long crfId);

	List<CrfDescrpency> alldiscrepencyEleInfo(Crf crf, VolunteerPeriodCrf vpc, String keyName);

	CrfSectionElementData studyCrfSectionElementData(Long dataId);

	boolean saveStudyCrfDescrpency(CrfDescrpency scd);

	CrfGroupElement studyCrfGroupElement(Long id);

	CrfGroupElementData studyCrfGroupElementData(Long dataId);

	List<CrfDescrpency> alldiscrepencyEleInfo(Crf crf, VolunteerPeriodCrf vpc, String keyName, String condition);

	void saveDataSet(DataSet ds);

	List<DataSet> findAllDataSets();

	DataSet dataSet(Long id);

	void updateDataSet(DataSet dataSet);

	DataSet fullDataSetInfo(Long id);

	VolunteerPeriodCrf volunteerPeriodCrf(DataSetPhasewiseCrfs crf, StudyPeriodMaster priod, Volunteer v);

	CrfSectionElementData studyCrfSectionElementData2(VolunteerPeriodCrf vcp, CrfSectionElement ele);

	CrfGroupElementData studyCrfSectionElementData(VolunteerPeriodCrf vcp, CrfGroupElement ele, String keyName);

	Crf crfByName(String value);

	void saveCrfEleCaliculationList(List<CrfEleCaliculation> list);

	List<CrfEleCaliculation> crfEleCaliculationList();

	List<CrfEleCaliculation> crfEleCaliculationList(Crf libCrf);

	List<CrfEleCaliculation> libcrfEleCaliculationList(Crf libCrf);

	CrfEleCaliculation studyCrfEleCaliculation(StudyMaster sm, Long crfId, String resultFiled);

	List<DataSet> findAllDataSets(StudyMaster sm);

	List<CrfDescrpency> allOpendStudydiscrepency(StudyMaster sm, String username);

	CrfDescrpency studyCrfDescrpency(Long id);

	void updateStudyCrfDescrpency(CrfDescrpency scd);

	CrfGroup getCrfGroup(Long id);

	List<CrfDateComparison> CrfDateComparisonAndSubElements(Crf crf);

	List<CrfDateComparison> studyCrfDateComparisonAndSubElements(Crf crf);

	CrfDateComparison byCrfDateComparisonAndSubElementsById(Long dateRuleDbId);

	Crf getStudyCrf(Long crfId);

	void updateStudyCrf(Crf crf);

	List<CrfEleCaliculation> studyCrfEleCaliculation(Long studyId);

	Crf studycrfByName(StudyMaster sm, String value);

	CrfEleCaliculation getCrfEleCaliculation(Long crfId);

	void updateCrfEleCaliculationStatus(CrfEleCaliculation crf);

	List<CrfRule> studyCrfRuleWithCrfAndSubElements(StudyMaster sm);

	List<Crf> findAllStudyCrfsForRules(StudyMaster sm);

	List<CrfSectionElement> studysectionElemets(Long id);

	List<CrfGroupElement> studygroupElemets(Long id);

	CrfRule studycrfRule(Long id);

	void saveCrfDateComparison(CrfDateComparison data);

	List<CrfDateComparison> crfDateComparisonlist();

	CrfDateComparison crfDateComparison(Long id);

	void updateCrfDateComparison(CrfDateComparison rule);

	List<CrfDateComparison> studycrfDateComparisonlist();

	CrfDateComparison studycrfDateComparison(Long id);

	List<CrfDateComparison> studycrfDateComparisonlistall(StudyMaster sm);

	Crf crfForView(Long crfId);

	
	void saveStudyCrfPeriodCrfsList(List<Crf> crfs, StudyMaster sm);


	void saveUpdateCrfData(List<CrfSectionElementData> sectionData, List<CrfSectionElementData> sectionDataUpdate,
			VolunteerPeriodCrf vpc);

	void saveCrfSectionElementDataAuditList(List<CrfSectionElementDataAudit> audit);

	void saveCrfSectionElementDataAudit(CrfSectionElementDataAudit saudit);

	void saveCrfDescrpencyAudit(CrfDescrpencyAudit descAudit);

	Crf checkStudyCrf(String name, Long stdId);

	Crf checkCrf(String name);

	List<Crf> crfsByName(String name);

	void saveStudySectionData1(List<CrfSectionElementData> sectionData);

	List<StdSubGroupObservationCrfs> findAllStdSubGroupObservationCrfs(StudyMaster sm, SubGroupInfo subGroup);

	void updateStdSubGroupObservationCrfsList(List<StdSubGroupObservationCrfs> stdcrfsUpdate,
			List<StdSubGroupObservationCrfsLog> sgblog);

	void saveStdSubGroupObservationCrfsList(List<StdSubGroupObservationCrfs> updates,
			List<StdSubGroupObservationCrfs> stdcrfssave, StudyMaster sm, String userName, StudyDesignStatus sds,
			List<StdSubGroupObservationCrfsLog> logs);

	List<StdSubGroupObservationCrfs> findAllStdSubGroupObservationCrfs(StudyMaster sm, Long subGroupId);

	StdSubGroupObservationCrfs stdSubGroupObservationCrfsWithCrf(Long crfId);

	List<CrfRule> crfRuleWithCrfAndSubElementsWith(Long dbId, Long crfId, boolean b);

	String saveCrfRecord(Crf crf, List<Long> ids);

	List<CrfDescrpency> alldiscrepencyOfElement(CrfSectionElementData data);

	StdSubGroupObservationCrfs saveRevewObsevation(LoginUsers user, Long subGroupInfoId,
			Long stdSubGroupObservationCrfsId, Integer descElementAll, List<Long> dataIds);

	List<SubGroupAnimalsInfoAll> subGroupAnimalsInfoInStudy(Long id);

	List<StdSubGroupObservationCrfs> stdSubGroupObservationCrfsWithAnimal(Long animalId);

	ReviewLevel reviewLevel();

	List<StdSubGroupObservationCrfs> findAllActiveStdSubGroupObservationCrfs(SubGroupInfo sg);

	List<SubGroupAnimalsInfoAll> subGroupAnimalsInfoAll(SubGroupInfo sg);

	List<SubGroupAnimalsInfoCrfDataCount> subGroupAnimalsInfoCrfDataCount(SubGroupInfo sg,
			SubGroupAnimalsInfoAll animal, StdSubGroupObservationCrfs obs);

	List<CrfSectionElementData> crfSectionElementData(SubGroupAnimalsInfoCrfDataCount entry);

	CrfSectionElementData crfSectionElementData(SubGroupAnimalsInfoCrfDataCount entry, CrfSectionElement ele);

	List<SubGroupAnimalsInfoCrfDataCountReviewLevel> subGroupAnimalsInfoCrfDataCountReviewLevels(
			SubGroupAnimalsInfoCrfDataCount entry);

	List<CrfMapplingTableColumn> crfMappingTableColumns(Long id);

	List<CrfMapplingTable> findAllMappingTables();

	CrfMapplingTableColumnMap saveCrfMapplingTableColumnMap(CrfMapplingTableColumnMap map);

	List<CrfSectionElement> sectionElemetsSelectTale(Long id);

	List<CrfMapplingTableColumnMap> allCrfMapplingTableColumnMap();

	String[] crfSectionElementValuesFromCrfMappingTableColumnMap(CrfSectionElement secElement);

	SubGroupAnimalsInfoAll subGroupAnimalsInfoAllById(Long animalId);

	void saveCrfSectionElementInstrumentValue(CrfSectionElementInstrumentValue ins);

	List<CrfSectionElementInstrumentValue> crfSectionElementInstrumentValues(StudyAnimal animal, Crf crf);

	List<StdSubGroupObservationCrfs> stdSubGroupObservationCrfs(SubGroupInfo subGroup);

	Long sectionIdWithCrfId(Long id, String sectionName);

	Long sectionElemntId(Long sectionId, String elementName);

	Crf getCrfForDataEntryView(Long id);

	Crf getCrfForDataEntryView(Long id, StudyMaster sm, Long dataDatesId, StudyAnimal animal, String string,
			String scheduleType);
	
	List<StudyAccessionCrfDescrpency> allStudyAccessionCrfDescrpency(StudyMaster sm, String username);

	StudyAccessionCrfDescrpency studyAccessionCrfDescrpency(Long id);

	void updateStudyAccessionCrfDescrpency(StudyAccessionCrfDescrpency scd, StudyAccessionCrfDescrpencyLog log, CrfSectionElementDataAudit saudit);

	List<StudyAccessionCrfDescrpency> allStudyAccessionCrfDescrepencyOfElement(Long id);

	StudyAccessionCrfSectionElementData studyAccessionCrfSectionElementDataWithId(
			Long studyAccessionCrfSectionElementDataId);

	boolean saveStudyAccessionCrfDescrpency(StudyAccessionCrfDescrpency scd);

	Long crfIdByName(String name);

	void saveWorkFlowReviewAudit(WorkFlowReviewAudit workFlowReviewAudit);

	void updateSubjectDataEntryDetails(SubjectDataEntryDetails sded);

	CrfSectionElementData crfSectionElementDataWithId(Long crfSectionElementDataId);

	List<CrfDescrpency> allStudyCrfDescrpencyOfElement(Long id);

	CrfDescrpency saveCrfDescrpency(CrfDescrpency scd);

	void updateStudyObservatoinCrfDescrpency(CrfDescrpency scd, CrfDescrpencyLog log, CrfDescrpencyAudit descAudit);

	List<String> crfSectionElementValue(CrfSectionElement element);

	List<CrfSectionElementInstrumentValue> crfSectionElementInstrumentValues(Long animalId,
			Long elementId);

	List<Crf> findAllActiveCrfsConfigurationForAcclimatization();

	List<Crf> findAllActiveCrfsConfigurationForObservation();

	List<Crf> findAllActiveCrfsConfigurationForTreatment();

	List<StdSubGroupObservationCrfs> findAllActiveStdSubGroupObservationCrfs(StudyMaster sm, Long subGroupId);

	List<RoleMaster> allRoleMastersOfIds(List<Long> roleList);

	Crf saveIsntrumebtCrf(Crf crf);

	StdSubGroupObservationCrfs stdSubGroupObservationCrfs(Long id);

	void saveObservationRoles(List<ObservationRole> obRoles);


	
}
