package com.springmvc.service;

import com.springmvc.model.EmployeeMaster;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.LoginUsersLog;

public interface EmployeeService {

	boolean checkEmpIdExistOrNot(String empId);

	boolean saveEmpDetails(EmployeeMaster employeeMaster, String username, Long roleId);

	EmployeeMaster findByEmpId(String empId);

	boolean changeStatus(LoginUsers lu);

	void saveLoginUsersLog(LoginUsersLog lulog);

	boolean updateEmpDetails(EmployeeMaster employeeMaster, String username);

}
