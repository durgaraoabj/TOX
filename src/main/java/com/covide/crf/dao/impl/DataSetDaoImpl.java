package com.covide.crf.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.covide.crf.dao.DataSetDao;
import com.covide.crf.dto.DataSet;
import com.covide.crf.service.DatasetService;
import com.springmvc.abstractdao.AbstractDao;

@Repository("datasetDAO")
public class DataSetDaoImpl extends AbstractDao<Long, DataSet> implements DataSetDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<DatasetService> findAllDataSets() {
		return getSession().createCriteria(DataSet.class).list();
	}

}
