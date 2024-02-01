package com.springmvc.dao;

import java.util.List;

import com.covide.crf.dto.CrfSectionElementInstrumentValue;
import com.springmvc.model.CongulometerInstrumentValues;
import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.ObservationInturmentConfiguration;
import com.springmvc.model.StudyTestCodes;
import com.springmvc.model.SysmaxInstrumentValues;

public interface InstrumentDao {

	public List<CongulometerInstrumentValues> getCongulometerInstrumentValuesWithStudyId(Long studyId);

	public List<CrfSectionElementInstrumentValue> getCrfSectionElementInstrumentValueList();

	public List<SysmaxInstrumentValues> getSysmaxInstrumentValuesWithStudyId(Long studyId);

	public ObservationInturmentConfiguration observationInturmentConfiguration(Long studyAcclamatizationDatesId, String obserVationFor);

	public void saveInstrumentIpAddressData(InstrumentIpAddress instrumentIpAddress);

	public List<StudyTestCodes> studyTestCodesOfObservation(Long id, Long insturmentId);
	

}
