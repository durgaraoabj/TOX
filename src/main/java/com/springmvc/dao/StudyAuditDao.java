package com.springmvc.dao;

import java.util.List;

import com.springmvc.model.GroupInfo;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;

public interface StudyAuditDao {

	List<GroupInfo> getGroupInfoForStudy(StudyMaster study);

	List<StdSubGroupObservationCrfs> getStdSubGroupObservationCrfsWithStudy(StudyMaster study);

	List<SubGroupAnimalsInfoCrfDataCount> getSubGroupAnimalsInfoCrfDataCountWithStudyId(StudyMaster study);

	List<SubGroupAnimalsInfoCrfDataCount> getSubGroupAnimalsInfoCrfDataCountWithStudyIdAndSubId(StudyMaster study,
			Long id);

	
	
}
