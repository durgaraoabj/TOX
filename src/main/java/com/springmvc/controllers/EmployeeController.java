package com.springmvc.controllers;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springmvc.model.AddressMaster;
import com.springmvc.model.DepartmentMaster;
import com.springmvc.model.EmergencyContMaster;
import com.springmvc.model.EmpJobMaster;
import com.springmvc.model.EmployeeMaster;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.LoginUsersLog;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.dummy.LoginFieldDummyForm;
import com.springmvc.service.DepartmentService;
import com.springmvc.service.EmployeeService;
import com.springmvc.service.RoleMasterService;
import com.springmvc.service.UserService;

@Controller
@RequestMapping("/administration/employee")
public class EmployeeController {

	@Autowired
	UserService userService;
	
	@Autowired
	DepartmentService departmentService;
	
	@Autowired
	RoleMasterService roleMasterService;
	
	@Autowired
	EmployeeService employeeService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String getAllUsers(ModelMap model,RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		List<LoginUsers> allUsers = userService.findAllUsers();
		model.addAttribute("PageHedding", "All Users");
		model.addAttribute("activeUrl", "administration/employee/");
		model.addAttribute("allUsers", allUsers);
		return "allUsers.tiles";
	}	
	@RequestMapping(value="/changeStatus/{empId}")
	public String changeStatus(@PathVariable("empId") Long empId,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		LoginUsers lu = userService.findById(empId);
		LoginUsersLog lulog = new LoginUsersLog();
		try {
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(lulog, lu);
			lulog.setLoginUserOld(lu);
			employeeService.saveLoginUsersLog(lulog);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(lu.getStatus().equals("Active"))
			lu.setStatus("In-Active");
		else
			lu.setStatus("Active");
		lu.setUpdatedBy(request.getSession().getAttribute("userName").toString());
		lu.setUpdatedOn(new Date());
		boolean flag = employeeService.changeStatus(lu);
		if(flag) 
			redirectAttributes.addFlashAttribute("pageMessage", "Employe "+lu.getUsername()+" Status Updated Successfully");
		else
			redirectAttributes.addFlashAttribute("pageError", "Employe "+lu.getUsername()+" Status Updation Failed");
		return "redirect:/administration/employee/";
	} 
	
	@RequestMapping(value="/empId/{empId}")
	public @ResponseBody String checkEmpIdExistOrNot(@PathVariable("empId") String empId) {
		boolean flag = employeeService.checkEmpIdExistOrNot(empId);
		if(flag)
			return "'"+empId+"' already exist, please check.";
		else
			return "";
	} 
	
	/*@RequestMapping(value="/createEmployee", method=RequestMethod.GET)
	public String createUser(ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		List<DepartmentMaster> deptList = departmentService.getActiveAllDepts();
		model.addAttribute("deptList", deptList);
		if(!model.containsKey("employeeMaster")) {
			EmployeeMaster employeeMaster = new EmployeeMaster();
			employeeMaster.setAddressMaster(new AddressMaster());
			employeeMaster.setEmerContMaster(new EmergencyContMaster());
			employeeMaster.setJobMaster(new EmpJobMaster());
			model.addAttribute("employeeMaster", employeeMaster);
		}
		
		model.addAttribute("PageHedding", "Create User Login");
		model.addAttribute("activeUrl", "administration/employee/createEmployee");
		return "createUser.tiles";
	}*/
	@RequestMapping(value="/createEmployee", method=RequestMethod.GET)
	public String createUser(ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		List<RoleMaster> roles = roleMasterService.findAllAciveRoleExceptSuperAdmin();
		model.addAttribute("roles", roles);
		if(!model.containsKey("employeeMaster")) {
			EmployeeMaster employeeMaster = new EmployeeMaster();
			/*EmployeeMaster employeeMaster = new EmployeeMaster();
			employeeMaster.setAddressMaster(new AddressMaster());
			employeeMaster.setEmerContMaster(new EmergencyContMaster());*/
//			employeeMaster.setJobMaster(new EmpJobMaster());
			model.addAttribute("employeeMaster", employeeMaster);
		}
		
		model.addAttribute("PageHedding", "Create User Login");
		model.addAttribute("activeUrl", "administration/employee/createEmployee");
		return "createUser.tiles";
	}
	/*@RequestMapping(value="/updateEmployee/{empId}")
	public String updateEmployee(@PathVariable("empId") String empId, ModelMap model, RedirectAttributes redirectAttributes) {
		
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		List<DepartmentMaster> deptList = departmentService.getActiveAllDepts();
		model.addAttribute("deptList", deptList);
		if(!model.containsKey("employeeMaster")) {
			EmployeeMaster lu = employeeService.findByEmpId(empId);
			model.addAttribute("employeeMaster", lu);
		}
		
		model.addAttribute("PageHedding", "Update User Login");
		model.addAttribute("activeUrl", "administration/employee/updateEmployee/"+empId);
		return "updateUser.tiles";
	}*/
	@RequestMapping(value="/updateEmployee/{empId}")
	public String updateEmployee(@PathVariable("empId") String empId, ModelMap model, RedirectAttributes redirectAttributes) {
		
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		List<DepartmentMaster> deptList = departmentService.getActiveAllDepts();
		model.addAttribute("deptList", deptList);
		/*if(!model.containsKey("employeeMaster")) {
			EmployeeMaster lu = employeeService.findByEmpId(empId);
			model.addAttribute("employeeMaster", lu);
		}*/
		
		model.addAttribute("PageHedding", "Update User Login");
		model.addAttribute("activeUrl", "administration/employee/updateEmployee/"+empId);
		return "updateUser.tiles";
	}
	
	/*@RequestMapping(value="/createEmployee", method=RequestMethod.POST)
	public String createUser(@Valid EmployeeMaster employeeMaster, BindingResult result, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("employeeMaster", employeeMaster);
			redirectAttributes.addFlashAttribute("pageError", "Invalid information entered");
			return "redirect:/administration/employee/createEmployee";
		}
		boolean checkduplicate = employeeService.checkEmpIdExistOrNot(employeeMaster.getEmpId());
		if(!checkduplicate) {
			String username = request.getSession().getAttribute("userName").toString();
			boolean flag = employeeService.saveEmpDetails(employeeMaster, username);
			if(flag) {
				redirectAttributes.addFlashAttribute("pageMessage", "Employe Created Successfully");
				return "redirect:/administration/employee/createEmployee";
			}else {
				redirectAttributes.addFlashAttribute("employeeMaster", employeeMaster);
				redirectAttributes.addFlashAttribute("pageError", "Employe saving failed..");
				return "redirect:/administration/employee/createEmployee";
			}
		}else {
			redirectAttributes.addFlashAttribute("employeeMaster", employeeMaster);
			redirectAttributes.addFlashAttribute("pageError", "Employe details already exist");
			return "redirect:/administration/employee/createEmployee";
		}
	}*/
	@RequestMapping(value="/createEmployee", method=RequestMethod.POST)
	public String createUser(@ModelAttribute("employeeMaster") EmployeeMaster employeeMaster, @RequestParam("roleVal")Long roleId, BindingResult result, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("employeeMaster", employeeMaster);
			redirectAttributes.addFlashAttribute("pageError", "Invalid information entered");
			return "redirect:/administration/employee/createEmployee";
		}
		boolean checkduplicate = employeeService.checkEmpIdExistOrNot(employeeMaster.getEmpId());
		if(!checkduplicate) {
			String username = request.getSession().getAttribute("userName").toString();
			boolean flag = employeeService.saveEmpDetails(employeeMaster, username, roleId);
			if(flag) {
				redirectAttributes.addFlashAttribute("pageMessage", "Employe Created Successfully");
				return "redirect:/administration/employee/createEmployee";
			}else {
				redirectAttributes.addFlashAttribute("employeeMaster", employeeMaster);
				redirectAttributes.addFlashAttribute("pageError", "Employe saving failed..");
				return "redirect:/administration/employee/createEmployee";
			}
		}else {
			redirectAttributes.addFlashAttribute("employeeMaster", employeeMaster);
			redirectAttributes.addFlashAttribute("pageError", "Employe details already exist");
			return "redirect:/administration/employee/createEmployee";
		}
	}
	
	/*@RequestMapping(value="/updateEmployee", method=RequestMethod.POST)
	public String updateEmployee(@Valid EmployeeMaster employeeMaster, BindingResult result, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("employeeMaster", employeeMaster);
			redirectAttributes.addFlashAttribute("pageError", "Invalid information entered");
			return "redirect:/administration/employee/updateEmployee/"+employeeMaster.getEmailId();
		}
			String username = request.getSession().getAttribute("userName").toString();
			boolean flag = employeeService.updateEmpDetails(employeeMaster, username);
			if(flag) {
				redirectAttributes.addFlashAttribute("pageMessage", "Employe Updated Successfully");
				return "redirect:/administration/employee/";
			}else {
				redirectAttributes.addFlashAttribute("employeeMaster", employeeMaster);
				redirectAttributes.addFlashAttribute("pageError", "Employe Updation failed..");
				return "redirect:/administration/employee/updateEmployee/"+employeeMaster.getEmailId();
			}
	}*/
	@RequestMapping(value="/updateEmployee", method=RequestMethod.POST)
	public String updateEmployee(@Valid EmployeeMaster employeeMaster, BindingResult result, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("employeeMaster", employeeMaster);
			redirectAttributes.addFlashAttribute("pageError", "Invalid information entered");
			return "redirect:/administration/employee/updateEmployee/"+employeeMaster.getEmailId();
		}
			String username = request.getSession().getAttribute("userName").toString();
			boolean flag = employeeService.updateEmpDetails(employeeMaster, username);
			if(flag) {
				redirectAttributes.addFlashAttribute("pageMessage", "Employe Updated Successfully");
				return "redirect:/administration/employee/";
			}else {
				redirectAttributes.addFlashAttribute("employeeMaster", employeeMaster);
				redirectAttributes.addFlashAttribute("pageError", "Employe Updation failed..");
				return "redirect:/administration/employee/updateEmployee/"+employeeMaster.getEmailId();
			}
	}
	@RequestMapping(value="/loginCredentials", method=RequestMethod.GET)
	public String generateLoginCredentials(RedirectAttributes redirectAttributes, ModelMap model) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		return "loginCredentials.tiles";
	}
	
	
	@RequestMapping(value="/loginCredentials/{empId}", method=RequestMethod.GET)
	public String generateLoginCredentials(@PathVariable("empId") String empId, RedirectAttributes redirectAttributes, ModelMap model) {
		if(empId!=null && !empId.equals("")) {
			List<RoleMaster> allRoles = roleMasterService.findAllAciveRoleExceptSuperAdmin();
			LoginFieldDummyForm loginFieldDummyForm = userService.checkDuplicate(empId);
			model.addAttribute("allRoles", allRoles);
			if(loginFieldDummyForm==null) {
				EmployeeMaster employeeMaster = employeeService.findByEmpId(empId);
				model.addAttribute("employeeMaster", employeeMaster);
				model.addAttribute("loginFieldDummyForm", new LoginFieldDummyForm());
				model.addAttribute("createType", true);
			}else {
				model.addAttribute("loginFieldDummyForm", loginFieldDummyForm);
				model.addAttribute("createType", false);
			}
			return "pages/administration/users/login_credential_emp_id_entry_form";
		}else {
			redirectAttributes.addFlashAttribute("pageError", "Employee ID : "+ empId +" is required.");
			return "redirect:/administration/employee/loginCredentials";
		}
	}
	
	@RequestMapping(value="/loginCredentials", method=RequestMethod.POST)
	public String generateLoginCredentials(@Valid LoginFieldDummyForm loginFieldDummyForm, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		System.out.println("Emp ID : "+loginFieldDummyForm.getEmpId());
		LoginUsers checkLoginUser = userService.findByUsername(loginFieldDummyForm.getEmpId());
		if(checkLoginUser == null) {
			String username = request.getSession().getAttribute("userName").toString();
			boolean flag = userService.generateLoginInfo(loginFieldDummyForm, username);
			if(flag) {
				redirectAttributes.addFlashAttribute("pageMessage", "Emp ID : "+loginFieldDummyForm.getEmpId()+" success");
			}else {
				redirectAttributes.addFlashAttribute("pageError", "Emp ID : "+loginFieldDummyForm.getEmpId()+" failed");
			}
		}else {
			redirectAttributes.addFlashAttribute("pageError", "Emp ID : "+loginFieldDummyForm.getEmpId()+" login details already exist.");
		}
		return "redirect:/administration/employee/loginCredentials";
	}
	
	@RequestMapping(value="/updateLoginCredentials", method=RequestMethod.POST)
	public String updateLoginCredentials(@Valid LoginFieldDummyForm loginFieldDummyForm, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		System.out.println("Emp ID : "+loginFieldDummyForm.getEmpId());
		LoginUsers checkLoginUser = userService.findByUsername(loginFieldDummyForm.getEmpId());
		if(checkLoginUser != null) {
			String username = request.getSession().getAttribute("userName").toString();
			boolean flag = userService.updateLoginInfo(checkLoginUser, loginFieldDummyForm, username);
			if(flag) {
				redirectAttributes.addFlashAttribute("pageMessage", "Emp ID : "+loginFieldDummyForm.getEmpId()+" success");
			}else {
				redirectAttributes.addFlashAttribute("pageError", "Emp ID : "+loginFieldDummyForm.getEmpId()+" failed");
			}
		}else {
			redirectAttributes.addFlashAttribute("pageError", "Emp ID : "+loginFieldDummyForm.getEmpId()+" login details not exist.");
		}
		return "redirect:/administration/employee/loginCredentials";
	}
	@RequestMapping(value="/loginCredentials/changePassword", method=RequestMethod.GET)
	public String changePassword(RedirectAttributes redirectAttributes, ModelMap model) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		return "changePassword.tiles";
	}
	@RequestMapping(value="/loginCredentials/updatePassword", method=RequestMethod.POST)
	public String updatePassword( RedirectAttributes redirectAttributes, HttpServletRequest request) {
		String userName = request.getSession().getAttribute("userName").toString();
		
		LoginUsers checkLoginUser = userService.findByUsername(userName);
		String password = request.getParameter("pws1").trim();
		if(checkLoginUser != null) {
			boolean flag = userService.updateLoginPassword(checkLoginUser, password);
			if(flag) {
				redirectAttributes.addFlashAttribute("pageMessage", "Password Change Successuflly");
			}else {
				redirectAttributes.addFlashAttribute("pageError", "Failed To Change Password");
			}
		}else {
			redirectAttributes.addFlashAttribute("pageError", " login details not exist.");
		}
		return "redirect:/administration/employee/loginCredentials/changePassword";
	}
}
