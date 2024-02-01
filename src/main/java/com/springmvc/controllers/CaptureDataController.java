package com.springmvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springmvc.service.CaptureDataService;

@RequestMapping("/captureData")
public class CaptureDataController {
	
	@Autowired
	CaptureDataService captureDataService;

}
