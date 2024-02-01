package com.springmvc.dao;

import java.util.List;

import com.springmvc.model.ApplicationSideMenuLinks;
import com.springmvc.model.ApplictionSideMenus;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.RolesWiseModules;

public interface RolesWiseMenusDao {

	List<RolesWiseModules> getRolesWiseModulesRecordsList(Long roleId);

	List<RoleMaster> getRolesMasterRecords();

	RoleMaster getRoleMasterRecord(Long roleId);

	List<ApplicationSideMenuLinks> getApplicationSideMenusList(Long roleId);

	RoleMaster getRoleMasterRecordBasedOnId(Long roleId);

	boolean saveRoleBasedLinks(RoleMaster rolePojo, List<Long> linkIds, String userName);

	boolean inactiveApplicationSideLink(RoleMaster rolePojo, Long linkId, String userName);

	/*
	 * To get All Side menu Links codes
	 */
	List<ApplicationSideMenuLinks> allApplicationSideMenuLinks();

	/*
	 * Save ApplictionSideMenus
	 */
	void saveApplicationSideMenu(ApplictionSideMenus applictionSideMenus);

	/*
	 * To get All Side menu codes
	 */
	List<ApplictionSideMenus> allApplicationSideMenus();

	/*
	 * To get Side menu with menu code
	 */
	ApplictionSideMenus applictionSideMenusByCode(String code);

	/*
	 * To get All Side menu Links codes of side menu id
	 */
	List<ApplicationSideMenuLinks> allApplicationSideMenuLinksOfSideMenu(Long id);

	/*
	 * Save ApplicationSideMenuLinks
	 */
	void saveApplicationSideMenuLink(ApplicationSideMenuLinks applicationSideMenuLinks);

}
