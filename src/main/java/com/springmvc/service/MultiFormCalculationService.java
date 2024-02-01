package com.springmvc.service;

import java.util.List;
import java.util.Map;

import com.covide.crf.dto.Crf;

public interface MultiFormCalculationService {

	List<Crf> getCrfsRecordsList();

	Crf getCrfRecord(Long crfId);

	Map<String, String> getgetFormulaCalculationData(Long crfId, Long animalId, Long studyId);

	Map<String, String> getFormulaCalculationDataForCurrentForm(Long crfId, List<String> dataString, Long studyId);

}
