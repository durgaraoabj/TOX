package com.covide.crf.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.ui.ModelMap;
import org.xml.sax.SAXException;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfDateComparison;
import com.covide.crf.dto.CrfDescrpency;
import com.covide.crf.dto.CrfDescrpencyAudit;
import com.covide.crf.dto.CrfDescrpencyLog;
import com.covide.crf.dto.CrfEleCaliculation;
import com.covide.crf.dto.CrfGroupDataRows;
import com.covide.crf.dto.CrfGroupElement;
import com.covide.crf.dto.CrfGroupElementData;
import com.covide.crf.dto.CrfMapplingTable;
import com.covide.crf.dto.CrfMapplingTableColumnMap;
import com.covide.crf.dto.CrfRule;
import com.covide.crf.dto.CrfSectionElement;
import com.covide.crf.dto.CrfSectionElementData;
import com.covide.crf.dto.CrfSectionElementDataAudit;
import com.covide.crf.dto.DataSet;
import com.covide.crf.dto.DataSetPhasewiseCrfs;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.ReviewLevel;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAccessionCrfDescrpency;
import com.springmvc.model.StudyAccessionCrfDescrpencyLog;
import com.springmvc.model.StudyAccessionCrfSectionElementData;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.reports.RulesInfoTemp;

public interface CrfService {
	void copyLibCrfToStudy(StudyMaster sm, List<Integer> cids);
	List<Crf> findAllCrfs();

	Crf readCrfExcelFile(FileInputStream fis, String filename) throws IOException;

	void saveCrf(Crf crf);

	Crf getCrf(Long crfId);

	Crf changeCrfStatus(Long crfId, String username);

	
	void copyLibCrfToStudy(StudyMaster sm, String username, List<Long> crfIds, Map<Long, String> type,  
			Map<Long, String> days, Long sibGroupId, List<String> obDeptList, Map<Long, String> window, Map<Long, Integer> windowPeriod);

	List<Crf> findAllCrfsWithSubElements();

	Crf getCrfForView(Long crfId);

	String studyGroupElement(Long groupId, int rowNo);

	String studyCrfDataEntry(Crf crf, VolunteerPeriodCrf vpc, HttpServletRequest request);

	Map<String, String> getStudyCrfData(Crf crf, VolunteerPeriodCrf vpc);

	Map<Long, CrfGroupDataRows> studyCrfGroupDataRows(VolunteerPeriodCrf vpc);

	String studyCrfDataUpdate(Crf crf, VolunteerPeriodCrf vpc, HttpServletRequest request);

	Map<String, String> requiredElementIdInJspStd(Crf crf, VolunteerPeriodCrf vpc, String userRole);

	String requiredGoupElementIdInJsp(Long groupId, int rowNo);

	Map<String, String> requiredElementIdInJspStdUpdate(Crf crf, VolunteerPeriodCrf vpc, String userRole);

	Map<String, String> pattrenIdsAndPattrenStd(Crf crf, VolunteerPeriodCrf vpc);

	Map<String, String> allElementIdsTypesJspStd(Crf crf, VolunteerPeriodCrf vpc);

	Map<String, String> allElementIdsTypesJspStdUpdate(Crf crf, VolunteerPeriodCrf vpc);

	Map<String, String> pattrenIdsAndPattrenStdUpdate(Crf crf, VolunteerPeriodCrf vpc);

	String sectionEleSelect(Long id);

	String otherCrfSectionElements(Long id, int count);
	
	String groupEleSelect(Long id);

	String otherCrfGroupElements(Long id, int count);
	
	String crfRuleElements(int rowId, Long crfId);

	String saveCrfRule(String username, HttpServletRequest request);
	

	List<CrfRule> findAllCrfRules();

	String crfRuleChangeStatus(Long id);

	List<Crf> findAllActiveCrfs();

	Map<String, String> rulesFieldsMap(Crf crf);

	List<RulesInfoTemp> rulesFieldsSecMap(Crf crf, List<CrfRule> rules);

	List<CrfRule> crfRuleWithCrfAndSubElements(Crf crf);

	String studyCrfSecRuelCheck(Long crfId,  Long dbid, String id, String value, String values, StudyMaster sm, String oeles);

	Map<String, String> rulesFieldsGroupMap(Crf crf, List<CrfRule> rules);

	String studyCrfGroupRuelCheck(Long crfId, Long dbid, String id, String value, String values, StudyMaster sm);

	List<String> userdiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc);

	List<String> reviewerdiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc);

	List<String> closeddiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc);

	List<String> onHolddiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc);

	List<String> alldiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc);

	List<String> opendiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc);

	List<CrfDescrpency> alldiscrepency(Crf crf, VolunteerPeriodCrf vpc);

	CrfSectionElement studyCrfSectionElement(Long id);

	Crf studyCrf(Long crfId);

	CrfSectionElementData studyCrfSectionElementData(VolunteerPeriodCrf vpc, CrfSectionElement ele,
			String keyName);

	List<CrfDescrpency> alldiscrepencyEleInfo(Crf crf, VolunteerPeriodCrf vpc, String keyName);

	CrfSectionElementData studyCrfSectionElementData(Long dataId);

	boolean saveStudyCrfDescrpency(CrfDescrpency scd);

	CrfGroupElement studyCrfGroupElement(Long id);

	CrfGroupElementData studyCrfGroupElementData(VolunteerPeriodCrf vpc, CrfGroupElement ele, String keyName);

	CrfGroupElementData studyCrfGroupElementData(Long dataId);

	List<CrfDescrpency> alldiscrepencyEleInfo(Crf crf, VolunteerPeriodCrf vpc, String keyName,
			String condition);

	void saveDataSet(DataSet ds);

	List<DataSet> findAllDataSets();

	DataSet dataSet(Long id);

	void updateDataSet(DataSet dataSet);

	DataSet fullDataSetInfo(Long id);

	VolunteerPeriodCrf volunteerPeriodCrf(DataSetPhasewiseCrfs crf, StudyPeriodMaster phase, Volunteer v);

	CrfSectionElementData studyCrfSectionElementData(VolunteerPeriodCrf vcp, CrfSectionElement ele);

	CrfGroupElementData studyCrfSectionElementData(VolunteerPeriodCrf vcp, CrfGroupElement ele,
			String keyName);

	void uploadCrfCaliculationFile(String path, String username) throws ParserConfigurationException, SAXException, IOException;

	List<CrfEleCaliculation> crfEleCaliculationList();

	Map<String, String> caliculationFieldSec(Crf crf);

	void copyCrfCaliculationFromLib(StudyMaster sm);

	String studyCrfElementCalculationValue(StudyMaster sm, Long crfId, String resultFiled, String fieldAndVales);

	Object findAllDataSets(StudyMaster sm);

	List<CrfDescrpency> allOpendStudydiscrepency(StudyMaster sm, String userName);

	CrfDescrpency studyCrfDescrpency(Long id);

	void updateStudyCrfDescrpency(CrfDescrpency scd);

	String groupEleSelect1(Long id);

	String sectionEleSelect1(Long id);

	String sectionEleSelect2(Long id);

	String groupEleSelect2(Long id);

	String sectionEleSelect3(Long id);

	String sectionEleSelect4(Long id);

	String groupEleSelect3(Long id);

	String groupEleSelect4(Long id);

	String saveCrfDateComparison(String username, HttpServletRequest request);

	List<CrfDateComparison> studyCrfDateComparisonAndSubElements(Crf crf);

	String studyCrfSecDateRuelCheck(Long crfId, Long dateRuleDbId, String values);

	Crf readStudyCrfExcelFile(FileInputStream fis, String filename, Long stdId) throws IOException;


	Crf changeStudyCrfStatus(Long crfId, String username);


	List<CrfEleCaliculation> studyCrfEleCaliculation(Long activeStudyId);

	void uploadStudyCrfCaliculationFile(String path, String username, StudyMaster sm) throws ParserConfigurationException, SAXException, IOException;

	CrfEleCaliculation changeStudyCrfEleCaliculationStatus(Long crfId, String username);

	Object studyCrfRuleWithCrfAndSubElements(StudyMaster sm);

	List<Crf> findAllStudyCrfsForRules(StudyMaster sm);

	String studysectionEleSelect(Long id);

	String studygroupEleSelect(Long id);

	String studycrfRuleElements(StudyMaster sm, int rowId);

	String otherStudyCrfSectionElements(Long id, int count);

	String otherStudyCrfGroupElements(Long id, int count);


	String studycrfRuleChangeStatus(Long id);

	String studysectionEleSelect1(Long id);

	String studygroupEleSelect1(Long id);

	String studysectionEleSelect2(Long id);

	String studygroupEleSelect2(Long id);

	String studysectionEleSelect3(Long id);

	String studygroupEleSelect3(Long id);

	String studygroupEleSelect4(Long id);

	String studysectionEleSelect4(Long id);

	String saveStudyCrfDateComparison(StudyMaster sm, String username, HttpServletRequest request);

	List<CrfDateComparison> crfDateComparisonlist();

	String crfDateComparisonChangeStatus(Long id);

	List<CrfDateComparison> studycrfDateComparisonlist();

	String studycrfDateComparisonChangeStatus(Long id);

	List<CrfDateComparison> studycrfDateComparisonlistall(StudyMaster sm);

	Map<String, String> requiredElementIdInJspStdUpdate(Crf crf, VolunteerPeriodCrf vpc);

	Crf crfForView(Long crfId);


	void saveStudyCrfSectionElementDataAudit(CrfSectionElementDataAudit saudit);

	void saveStudyCrfDescrpencyAudit(CrfDescrpencyAudit descAudit);

	List<Crf> crfsByName(String name);

	StdSubGroupObservationCrfs stdSubGroupObservationCrfsWithCrf(Long crfId);

	List<RulesInfoTemp> rulesFields(Crf crf, List<CrfRule> rules);

	String crfRuelCheck(Long crfId, Long dbid, String id, String value, String values);

	String saveCrfRecord(Crf crf, List<Long> ids);

	List<CrfDescrpency> alldiscrepencyOfElement(CrfSectionElementData data);

	boolean saveCrfDescrpency(Long dataId, String username, Long userId, String comment);

	StdSubGroupObservationCrfs saveRevewObsevation(LoginUsers user, Long subGroupInfoId, Long stdSubGroupObservationCrfsId, Integer descElementAll, List<Long> dataIds);

	ReviewLevel reviewLevel();

	String crfMappingTableColumns(Long id);

	List<CrfMapplingTable> findAllMappingTables();

	CrfMapplingTableColumnMap mapTableToCrfSave(Long crfid, Long secEleId, Long tableId, Long columnId, String userName);

	String crfSectionElementsSelectTable(Long id);

	Object allCrfMapplingTableColumnMap();

	String[] crfSectionElementValuesFromCrfMapplingTableColumnMap(CrfSectionElement secElement);

	Crf getCrfForDataEntryView(Long id);

	Crf getCrfForDataEntryView(Long id, StudyMaster sm, Long studyTreatmentDataDatesId, StudyAnimal animal,
			String string, String type);
	
	List<StudyAccessionCrfDescrpency> allStudyAccessionCrfDescrpency(StudyMaster sm, String username);
	StudyAccessionCrfDescrpency studyAccessionCrfDescrpency(Long id);
	void updateStudyAccessionCrfDescrpency(StudyAccessionCrfDescrpency scd, StudyAccessionCrfDescrpencyLog log, CrfSectionElementDataAudit saudit);
	
	List<StudyAccessionCrfDescrpency> allStudyAccessionCrfDescrepencyOfElement(Long id);
	
	StudyAccessionCrfSectionElementData studyAccessionCrfSectionElementDataWithId(
			Long studyAccessionCrfSectionElementDataId);
	boolean saveAccessionDescrpency(Long accessiondataId, Long accessionuserId, String comment, Long userId);
	
	Long crfIdByName(String string);
	CrfSectionElementData crfSectionElementDataWithId(Long crfSectionElementDataId);
	List<CrfDescrpency> allStudyCrfDescrpencyOfElement(Long crfSectionElementDataId);
	CrfDescrpency saveObservationDescrpency(Long obserctiondataId, Long obserctionuserId, String comment, Long userId);
	void updateStudyObservatoinCrfDescrpency(CrfDescrpency scd, CrfDescrpencyLog log, CrfDescrpencyAudit descAudit);
	List<Crf> findAllActiveCrfsConfigurationForAcclimatization();
	List<Crf> findAllActiveCrfsConfigurationForObservation();
	
	List<Crf> findAllActiveCrfsConfigurationForTreatment();
	List<RoleMaster> allRoleMastersOfIds(List<Long> roleList);


	
	

	
}
