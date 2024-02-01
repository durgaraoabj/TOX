package com.springmvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springmvc.dao.PincodeDao;
import com.springmvc.model.PinCodeMaster;

@Controller
@RequestMapping("/pincode")
public class PincodeController {

	@Autowired
	PincodeDao pincodeDao;
	//, consumes={"application/json"}
	@RequestMapping(value="/{pincode}", method=RequestMethod.GET, produces={"application/json"})
	public @ResponseBody PinCodeMaster getPincode(@PathVariable("pincode") Long pincode) {
		PinCodeMaster pin = pincodeDao.findByPinCodeId(pincode);
		return pin;
	}
	
	
}
