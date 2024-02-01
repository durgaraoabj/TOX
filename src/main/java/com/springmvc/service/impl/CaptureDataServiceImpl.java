package com.springmvc.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springmvc.dao.CapturedDataDao;
import com.springmvc.service.CaptureDataService;

@Service("captureDataService")
public class CaptureDataServiceImpl implements CaptureDataService {
	
	@Autowired
	CapturedDataDao captureDataDao;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String capturedData(ModelMap model, HttpServletRequest request) {
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		Long siteId = (long) request.getSession().getAttribute("siteId");
		if(studyId != null && siteId != null) {
			
		}
		
		return "";
	}

}
