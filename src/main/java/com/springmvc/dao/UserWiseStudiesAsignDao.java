package com.springmvc.dao;

import java.util.List;

import com.springmvc.model.LoginUsers;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.UserWiseSitesAsignMaster;
import com.springmvc.model.UserWiseStudiesAsignMaster;

public interface UserWiseStudiesAsignDao {

	List<UserWiseStudiesAsignMaster> findByUserWiseList(LoginUsers user);

	void saveStudyAsignStudyCreationTime(List<UserWiseStudiesAsignMaster> userWisePermission);

	List<UserWiseStudiesAsignMaster> findByStudyWiseList(StudyMaster study);

	void updateUserWiseStudiesAsignMaster(List<UserWiseStudiesAsignMaster> userUpdate);

	List<UserWiseSitesAsignMaster> findByStudyWiseSitesList(StudyMaster study);

	void saveUserWiseSitesAsignMaster(List<UserWiseSitesAsignMaster> usersave);

	void updateUserWiseSitesAsignMaster(List<UserWiseSitesAsignMaster> userUpdate);

	List<UserWiseSitesAsignMaster> findByStudyWiseSitesListByUser(StudyMaster study, LoginUsers users);

	List<RoleMaster> getRoleMasterRecordsList();

	List<UserWiseStudiesAsignMaster> getStudyLevleRolesList(Long studyId);

	UserWiseStudiesAsignMaster userWiseStudiesAsignMaster(Long studyId, Long userId);

}
