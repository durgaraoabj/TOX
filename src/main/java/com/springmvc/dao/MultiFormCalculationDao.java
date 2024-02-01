package com.springmvc.dao;

import java.util.List;
import java.util.Map;

import com.covide.crf.dto.Crf;

public interface MultiFormCalculationDao {

	List<Crf> getCrfsRecordsList();

	Crf getCrfRecord(Long crfId);

	Map<String, String> getFormulaDataElements(Long crfId);

	String getActualElementValue(String crfKeyName, String eleName, Long studyId, Long animalId);

}
