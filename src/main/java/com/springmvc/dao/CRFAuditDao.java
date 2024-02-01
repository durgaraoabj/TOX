package com.springmvc.dao;

import java.util.List;

import com.springmvc.model.GroupInfo;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.util.ObservationData;

public interface CRFAuditDao {

	StudyMaster findByStudyId(long studyId);

	List<GroupInfo> studyGroupInfoWithChaildReview(StudyMaster sm);

	SubGroupInfo subGroupInfoAllByIdReview(StudyMaster sm, Long id);

	LoginUsers findById(Long id);

	ObservationData observationData(Long subGroupInfoId, Long stdSubGroupObservationCrfsId, LoginUsers user);

}
