package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.springmvc.model.StudyMaster;

@RestController
public class RESTContorller {
	

	
	//-------------------Create a User--------------------------------------------------------
	
	@RequestMapping(value = "/user/", method = RequestMethod.POST)
	public String createUser(	UriComponentsBuilder ucBuilder) {
		System.out.println("Creating User " );

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/study/clinical/{stdClinicalHome}").buildAndExpand("stdClinicalHome").toUri());
		return "asfasdfasdfasdfasdfsadf";
	}



}
