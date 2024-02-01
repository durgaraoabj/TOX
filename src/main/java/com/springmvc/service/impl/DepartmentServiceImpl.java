package com.springmvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springmvc.dao.DepartmentDao;
import com.springmvc.model.DepartmentMaster;
import com.springmvc.service.DepartmentService;

@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService {
	
	@Autowired
	DepartmentDao departmentDao;

	@Override
	public List<DepartmentMaster> getActiveAllDepts() {
		// TODO Auto-generated method stub
		return departmentDao.findActiveAllDepts();
	}

}
