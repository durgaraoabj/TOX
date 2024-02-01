package com.springmvc.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.DepartmentDao;
import com.springmvc.model.DepartmentMaster;

@Repository("departmentDao")
public class DepartmentDaoImpl extends AbstractDao<Long, DepartmentMaster> implements DepartmentDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<DepartmentMaster> findActiveAllDepts() {
		// TODO Auto-generated method stub
		return createEntityCriteria().list();
	}

	@Override
	public DepartmentMaster findById(Long id) {
		// TODO Auto-generated method stub
		return (DepartmentMaster) getSession().get(DepartmentMaster.class, id);
	}

	@Override
	public void saveDepartmentMaster(DepartmentMaster departmentMaster) {
		getSession().save(departmentMaster);
	}
	
	
	

}
