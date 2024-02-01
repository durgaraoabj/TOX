package com.springmvc.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springmvc.dao.UserDao;
import com.springmvc.dao.impl.ClinicalCodesDaoImple;
import com.springmvc.model.ClinicalCodes;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.TestCodeUnits;
import com.springmvc.model.TestCodeUnitsLog;

@Service("clinicalCodesService")
public class ClinicalCodesServiceImpl {
	@Autowired
	ClinicalCodesDaoImple clinicalCodesDao;
	@Autowired
	UserDao userDao;

	public List<ClinicalCodes> allClinicalCodes() {
		// TODO Auto-generated method stub
		return clinicalCodesDao.allClinicalCodes();
	}

	public boolean mergeClinicalCodes(Long userId, Map<Long, String> oldDescription, Map<String, String> newHeadings,
			Map<String, String> newClinicalCode, Map<String, String> newClinicalSign,
			Map<String, String> newDescription, Map<String, String> newrank) {
		return clinicalCodesDao.mergeClinicalCodes(userId, oldDescription, newHeadings, newClinicalCode,
				newClinicalSign, newDescription, newrank);

	}

	public List<ClinicalCodes> clinicalCodesWithCode(String collectionCodeName, String code) {
		// TODO Auto-generated method stub
		return clinicalCodesDao.clinicalCodesWithCode(collectionCodeName, code);
	}

	public List<ClinicalCodes> allActiveClinicalCodes() {
		// TODO Auto-generated method stub
		return clinicalCodesDao.allActiveClinicalCodes();
	}

	public List<String> animalNumber(String code, Long studyId, String type) {
		return clinicalCodesDao.animalNumber(code, studyId, type);
	}

}
