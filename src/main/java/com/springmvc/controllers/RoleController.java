package com.springmvc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springmvc.model.RoleMaster;
import com.springmvc.service.RoleMasterService;

@Controller
@RequestMapping("/userRole")
public class RoleController {

	@Autowired
	RoleMasterService roleMasterService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String getAllActiveRoles() {
		List<RoleMaster> allRoles = roleMasterService.findAll();
		return null;
	}
	
}
