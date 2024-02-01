package com.springmvc.service;

import java.util.List;

import com.springmvc.model.LoginUsers;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.RolesWiseModules;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudySite;
import com.springmvc.model.UserWiseStudiesAsignMaster;
import com.springmvc.model.dummy.LoginFieldDummyForm;

public interface UserService {

	LoginUsers findByUsername(String username);

	LoginUsers findById(Long id);

	List<LoginUsers> findAllUsers();

	boolean isUserSSOUnique(Long id, String sso);

	StudyMaster changeStudy(Long studyId, String username);

	boolean generateLoginInfo(LoginFieldDummyForm loginFieldDummyForm, String username);

	LoginFieldDummyForm checkDuplicate(String empId);

	boolean updateLoginInfo(LoginUsers checkLoginUser, LoginFieldDummyForm loginFieldDummyForm, String username);

	List<LoginUsers> findAllActiveUsers();

	void updateLoginInfo(LoginUsers user);

	StudySite changeSite(Long siteId, String username);

	boolean updateLoginPassword(LoginUsers checkLoginUser, String password);

	LoginUsers findByActiveUsername(String username);

	UserWiseStudiesAsignMaster getUserWiseSitesAsignMasterRecord(Long studyId, Long userId);

	List<RolesWiseModules> getApplicationMenus(LoginUsers users);

	List<RolesWiseModules> getApplicationMenusBasedOnStudy(RoleMaster role);

	List<RolesWiseModules> getRolesWiseModulesList();


}
