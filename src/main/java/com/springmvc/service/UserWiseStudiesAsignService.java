package com.springmvc.service;

import java.util.List;
import java.util.Map;

import com.springmvc.model.LoginUsers;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.UserWiseSitesAsignMaster;
import com.springmvc.model.UserWiseStudiesAsignMaster;

public interface UserWiseStudiesAsignService {

	List<UserWiseStudiesAsignMaster> findUserWiseStudies(String username);

	List<UserWiseStudiesAsignMaster> findUsersFindByStudy(StudyMaster study);

	List<UserWiseSitesAsignMaster> findByStudyWiseSitesList(StudyMaster study);

	List<UserWiseSitesAsignMaster> findByStudyWiseSitesListByUser(StudyMaster study, LoginUsers users);

	List<RoleMaster> getRolesMasterRecordsList();

	Map<Long, Long> getStudyLevleRolesList(Long studyId);

	String assingStudy(List<Long> userIds, Map<Long, Long> roleIds, Map<Long, String> comments, Long studyId,
			Long userId);

	UserWiseStudiesAsignMaster userWiseStudiesAsignMaster(Long studyId, Long userId);


}
