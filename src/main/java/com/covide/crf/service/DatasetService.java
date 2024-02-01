package com.covide.crf.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.covide.crf.dto.DataSet;

public interface DatasetService {

	List<DatasetService> findAllDataSets();

	File exportDataToExcel(DataSet dataSet, HttpServletRequest request) throws IOException;

}
