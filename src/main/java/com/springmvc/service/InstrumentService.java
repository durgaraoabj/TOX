package com.springmvc.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

import com.covide.crf.dto.CrfSectionElementInstrumentValue;
import com.covide.dto.StudyDto;
import com.covide.template.dto.TestCodesUnitsDto;
import com.springmvc.model.CongulometerInstrumentValues;
import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.ObservationInturmentConfiguration;
import com.springmvc.model.SysmaxInstrumentValues;

public interface InstrumentService {

	List<CongulometerInstrumentValues> getCongulometerInstrumentValuesWithStudyId(Long studyId);

	List<CrfSectionElementInstrumentValue> getCrfSectionElementInstrumentValueList();

	List<SysmaxInstrumentValues> getSysmaxInstrumentValuesWithStudyId(Long studyId);

	String instrumentDataCaptur(Long studyAcclamatizationDateId, String selecteDate, HttpServletRequest request, ModelMap model, String serviceType);

	String instrumentDataCapturPage(Long studyAcclamatizationDateId, Long studyTreatmentDataDatesId, String selecteDate, HttpServletRequest request,
			ModelMap model, String string);

	InstrumentIpAddress instrumentIpAddress(Long instumentId);

	ObservationInturmentConfiguration observationInturmentConfiguration(Long observationId);

	TestCodesUnitsDto callSysmexService(Long studyId, Long userId, InstrumentIpAddress instrumentIp, Long observationId,
			Long instumentid);

	TestCodesUnitsDto callVitrosService(Long studyId, Long userId, InstrumentIpAddress instrumentIp, Long observationId,
			Long instumentid);

	StudyDto callStagoService(Long studyId, Long userId, InstrumentIpAddress instrumentIp, Long observationId,
			Long instumentid, String sampleType, String loatNo, String test);

}
