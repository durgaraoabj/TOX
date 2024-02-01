package com.springmvc.dao;

import java.util.List;

import com.springmvc.model.AccessionAnimalsDataEntryDetails;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.RolesWiseModules;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.SubjectDataEntryDetails;
import com.springmvc.model.UserWiseSitesAsignMaster;
import com.springmvc.model.UserWiseStudiesAsignMaster;
import com.springmvc.model.WorkFlow;
import com.springmvc.model.WorkFlowReviewAudit;
import com.springmvc.model.WorkFlowReviewStages;



public interface UserDao {

	LoginUsers findById(Long id);
    
	LoginUsers findByusername(String sso);
               
    List<LoginUsers> findAllUsers();

	void updateStudy(LoginUsers user);

	void generateLoginDetails(LoginUsers loginUsers, long id);

	void updateLoginCredentials(LoginUsers checkLoginUser);

	List<LoginUsers> findAllActiveUsers();

	List<LoginUsers> dataUsersLoginUsers();

	LoginUsers findByActiveusername(String username);

	UserWiseStudiesAsignMaster getUserWiseSitesAsignMasterRecord(Long studyId, Long userId);

	List<RolesWiseModules> getApplicationMenus(LoginUsers users);

	List<RolesWiseModules> getApplicationMenusBasedOnStudy(RoleMaster role);

	List<RolesWiseModules> getRolesWiseModulesList();


	WorkFlow accessionWorkFlow(String string, StatusMaster activeStatus);

	WorkFlowReviewStages workFlowReviewStages(LoginUsers user, WorkFlow accessionWorkFlow);

	List<WorkFlowReviewAudit> workFlowReviewAudit(AccessionAnimalsDataEntryDetails aaded, LoginUsers user);

	List<WorkFlowReviewAudit> readamizationWorkFlowReviewAudit(Long id, LoginUsers user);

	List<WorkFlowReviewAudit> observationWorkFlowReviewAudit(Long aaded, LoginUsers user);

	RoleMaster roleMaster(String role);

	List<WorkFlowReviewAudit> readamizationDtoWorkFlowReviewAudit(Long id, LoginUsers user);
}
