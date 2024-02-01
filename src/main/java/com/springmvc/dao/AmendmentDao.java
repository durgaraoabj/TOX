package com.springmvc.dao;

import java.util.List;

import com.springmvc.model.AmendmentDetails;
import com.springmvc.model.AmendmentDetailsLog;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.StudyMaster;

public interface AmendmentDao {

	List<AmendmentDetails> getAmendmentDetailsRecrodsList(Long studyNo);

	List<AmendmentDetails> getAmendmentRecordsList(List<Long> amdIds);

	StudyMaster getStudyMasterRecrod(Long studyNo);

	boolean saveAmendmentDetails(List<AmendmentDetails> updateAmdList, List<AmendmentDetailsLog> amdLogList,
			ApplicationAuditDetails aad);

	List<AmendmentDetails> getStudyAmendmentDetailsList(Long studyId);

}
