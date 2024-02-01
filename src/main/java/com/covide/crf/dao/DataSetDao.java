package com.covide.crf.dao;

import java.util.List;

import com.covide.crf.service.DatasetService;

public interface DataSetDao {

	List<DatasetService> findAllDataSets();

}
