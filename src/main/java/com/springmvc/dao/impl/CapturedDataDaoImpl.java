package com.springmvc.dao.impl;

import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.CapturedDataDao;
import com.springmvc.model.StudyMaster;

@Repository("capturedDataDao")
public class CapturedDataDaoImpl extends AbstractDao<Long, StudyMaster> implements CapturedDataDao {
	
	

}
