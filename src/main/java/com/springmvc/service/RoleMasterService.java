package com.springmvc.service;

import java.util.List;

import com.springmvc.model.RoleMaster;

public interface RoleMasterService {

	List<RoleMaster> findAll();

	RoleMaster findById(Long id);

	List<RoleMaster> findAllAciveRoleExceptSuperAdmin();

}
