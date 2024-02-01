package com.springmvc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springmvc.dao.RolesWiseMenusDao;
import com.springmvc.model.ApplicationSideMenuLinks;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.RolesWiseModules;
import com.springmvc.service.RolesWiseMenuService;

@Service("rolesWiseMenuService")
public class RolesWiseMenuServiceImpl implements RolesWiseMenuService {

	@Autowired
	RolesWiseMenusDao rolesWiseMenusDao;

	@Override
	public Map<String, Map<String, List<RolesWiseModules>>> getRolesWiseModulesRecordsList(Long roleId) {
		Map<String, Map<String, List<RolesWiseModules>>> rwmMap = new HashMap<>();
		List<RolesWiseModules> rwmList = null;
		List<RolesWiseModules> list = null;
		Map<String, List<RolesWiseModules>> map = null;
		try {
			RoleMaster rolePojo = rolesWiseMenusDao.getRoleMasterRecord(roleId);
			if (rolePojo != null) {
				rwmList = rolesWiseMenusDao.getRolesWiseModulesRecordsList(roleId);
				if (rwmList != null && rwmList.size() > 0) {
					for (RolesWiseModules rwm : rwmList) {
						if (rwmMap.containsKey(rwm.getRole().getRole())) {
							map = rwmMap.get(rwm.getRole().getRole());
							list = map.get(rwm.getAppsideMenu().getAppsideMenu().getName());
							if (list == null)
								list = new ArrayList<>();
							list.add(rwm);
							map.put(rwm.getAppsideMenu().getAppsideMenu().getName(), list);
							rwmMap.put(rwm.getRole().getRole(), map);
						} else {
							map = new HashMap<>();
							list = new ArrayList<>();
							list.add(rwm);
							map.put(rwm.getAppsideMenu().getAppsideMenu().getName(), list);
							rwmMap.put(rwm.getRole().getRole(), map);
						}
					}
				} else {
					rwmMap.put(rolePojo.getRole(), map);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rwmMap;
	}

	@Override
	public List<RoleMaster> getRolesMasterRecords() {
		return rolesWiseMenusDao.getRolesMasterRecords();
	}

	@Override
	public Map<String, List<ApplicationSideMenuLinks>> getApplicationSideMenusList(Long roleId) {
		Map<String, List<ApplicationSideMenuLinks>> map = new HashMap<>();
		List<ApplicationSideMenuLinks> apsList = null;
		List<ApplicationSideMenuLinks> list = null;
		try {
			apsList = rolesWiseMenusDao.getApplicationSideMenusList(roleId);
			if (apsList != null && apsList.size() > 0) {
				for (ApplicationSideMenuLinks aps : apsList) {

					if (map.containsKey(aps.getAppsideMenu().getName())) {
						list = map.get(aps.getAppsideMenu().getName());
						list.add(aps);
						map.put(aps.getAppsideMenu().getName(), list);
					} else {
						list = new ArrayList<>();
						list.add(aps);
						map.put(aps.getAppsideMenu().getName(), list);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	@Override
	public RoleMaster getRoleMasterRecordBasedOnId(Long roleId) {
		return rolesWiseMenusDao.getRoleMasterRecordBasedOnId(roleId);
	}

	@Override
	public boolean saveRoleBasedLinks(RoleMaster rolePojo, List<Long> linkIds, String userName) {
		return rolesWiseMenusDao.saveRoleBasedLinks(rolePojo, linkIds, userName);
	}

	@Override
	public boolean inactiveApplicationSideLink(RoleMaster rolePojo, Long linkId, String userName) {
		return rolesWiseMenusDao.inactiveApplicationSideLink(rolePojo, linkId, userName);
	}

	@Override
	public void generateRoleConfiguredLinksInPdf(Map<String, Map<String, List<RolesWiseModules>>> menusMap) {

	}

}
