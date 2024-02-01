package com.springmvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springmvc.dao.RoleMasterDao;
import com.springmvc.model.RoleMaster;
import com.springmvc.service.RoleMasterService;

@Service("roleMasterService")
public class RoleMasterServiceImpl implements RoleMasterService {

	@Autowired
	private RoleMasterDao roleMasterDao;
	
	@Override
	public List<RoleMaster> findAll() {
		// TODO Auto-generated method stub
		return roleMasterDao.findAll();
	}

	@Override
	public RoleMaster findById(Long id) {
		// TODO Auto-generated method stub
		return roleMasterDao.findById(id);
	}

	@Override
	public List<RoleMaster> findAllAciveRoleExceptSuperAdmin() {
		// TODO Auto-generated method stub
		return roleMasterDao.findAllActiveRolesExceptSuperAdmin();
	}


}
