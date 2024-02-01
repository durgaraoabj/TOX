package com.springmvc.dao;

import java.util.List;

import com.springmvc.model.DepartmentMaster;

public interface DepartmentDao {

	List<DepartmentMaster> findActiveAllDepts();
	
	/*
	 * get DepartmentMaster with PK 
	 */
	DepartmentMaster findById(Long i);

	/*
	 * Save Department Master
	 */
	void saveDepartmentMaster(DepartmentMaster departmentMaster);

}
