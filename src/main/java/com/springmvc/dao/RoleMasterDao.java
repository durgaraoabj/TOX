package com.springmvc.dao;

import java.util.List;

import com.springmvc.model.RoleMaster;

public interface RoleMasterDao {

	List<RoleMaster> findAll();

	RoleMaster findById(long roleId);

	List<RoleMaster> findAllActiveRolesExceptSuperAdmin();
	/*
	 * To get all roles with role Id's
	 */
	List<RoleMaster> rolesByIds(List<Long> roleIds);

	/*
	 * get Role master with Role name
	 */
	RoleMaster roleIdByRoleName(String string);

	/*
	 * Save Role master data
	 */
	RoleMaster saveRoleMaster(RoleMaster roleMaster);

}
