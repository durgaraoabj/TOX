package com.springmvc.service;

import java.util.List;
import java.util.Map;

import com.springmvc.model.ApplicationSideMenuLinks;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.RolesWiseModules;

public interface RolesWiseMenuService {

	Map<String, Map<String, List<RolesWiseModules>>> getRolesWiseModulesRecordsList(Long roleId);

	List<RoleMaster> getRolesMasterRecords();

	Map<String, List<ApplicationSideMenuLinks>> getApplicationSideMenusList(Long roleId);

	RoleMaster getRoleMasterRecordBasedOnId(Long roleId);

	boolean saveRoleBasedLinks(RoleMaster rolePojo, List<Long> linkIds, String userName);

	boolean inactiveApplicationSideLink(RoleMaster rolePojo, Long linkId, String userName);

	void generateRoleConfiguredLinksInPdf(Map<String, Map<String, List<RolesWiseModules>>> menusMap);

}
