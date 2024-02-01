package com.springmvc.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springmvc.audittrail.AuditToken;
import com.springmvc.dao.EmployeeDao;
import com.springmvc.model.EmployeeMaster;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.LoginUsersLog;
import com.springmvc.service.EmployeeService;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EmployeeDao employeeDao;
	
	@Override
	public boolean checkEmpIdExistOrNot(String empId) {
		// TODO Auto-generated method stub
		EmployeeMaster emp = employeeDao.getEmployeeByEmpId(empId);
		if(emp!=null)
			return true;
		return false;
	}

	@Override
	@AuditToken
	public boolean saveEmpDetails(EmployeeMaster employeeMaster, String username, Long roleId) {
		// TODO Auto-generated method stub
		employeeMaster.setCreatedBy(username);
		employeeMaster.setCreatedOn(new Date());
		//employeeMaster.getAddressMaster().setCreatedBy(username);
		//employeeMaster.getAddressMaster().setCreatedOn(new Date());
		//employeeMaster.getEmerContMaster().setCreatedBy(username);
		//employeeMaster.getEmerContMaster().setCreatedOn(new Date());
		employeeMaster.setCreatedBy(username);
		employeeMaster.setCreatedOn(new Date());
		boolean flag = employeeDao.saveEmpDetails(employeeMaster, roleId);
		if(flag)
			return true;
		return false;
	}

	
	@Override
	@AuditToken
	public boolean updateEmpDetails(EmployeeMaster employeeMaster, String username) {
		// TODO Auto-generated method stub
		
		employeeMaster.setUpdatedBy(username);
		employeeMaster.setUpdatedOn(new Date());
		employeeMaster.getAddressMaster().setUpdatedBy(username);
		employeeMaster.getAddressMaster().setUpdatedOn(new Date());
		employeeMaster.getEmerContMaster().setUpdatedBy(username);
		employeeMaster.getEmerContMaster().setUpdatedOn(new Date());
		employeeMaster.getJobMaster().setUpdatedBy(username);
		employeeMaster.getJobMaster().setUpdatedOn(new Date());
		boolean flag = employeeDao.updateEmpDetails(employeeMaster);
		if(flag)
			return true;
		return false;
	}
	@Override
	public EmployeeMaster findByEmpId(String empId) {
		// TODO Auto-generated method stub
		return employeeDao.getEmployeeByEmpId(empId);
	}

	@Override
	public boolean changeStatus(LoginUsers lu) {
		return employeeDao.changeStatus(lu) ;
	}

	@Override
	public void saveLoginUsersLog(LoginUsersLog lulog) {
		// TODO Auto-generated method stub
		employeeDao.saveLoginUsersLog(lulog);
	}

	
}
