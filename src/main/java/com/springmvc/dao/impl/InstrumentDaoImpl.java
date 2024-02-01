package com.springmvc.dao.impl;

import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.covide.crf.dto.CrfSectionElementInstrumentValue;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.InstrumentDao;
import com.springmvc.model.CongulometerInstrumentValues;
import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.ObservationInturmentConfiguration;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyTestCodes;
import com.springmvc.model.SysmaxInstrumentValues;

@Repository("instrumentDao")
public class InstrumentDaoImpl extends AbstractDao<Long, CongulometerInstrumentValues> implements InstrumentDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<CongulometerInstrumentValues> getCongulometerInstrumentValuesWithStudyId(Long studyId) {

		List<CongulometerInstrumentValues> list = null;
		list = getSession().createCriteria(CongulometerInstrumentValues.class).add(Restrictions.eq("studyId", studyId))
				.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfSectionElementInstrumentValue> getCrfSectionElementInstrumentValueList() {
		List<CrfSectionElementInstrumentValue> list = null;
		list = getSession().createCriteria(CrfSectionElementInstrumentValue.class).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SysmaxInstrumentValues> getSysmaxInstrumentValuesWithStudyId(Long studyId) {
		List<SysmaxInstrumentValues> list = null;
		list = getSession().createCriteria(SysmaxInstrumentValues.class).add(Restrictions.eq("studyId", studyId))
				.list();

		return list;
	}

	@Override
	public ObservationInturmentConfiguration observationInturmentConfiguration(Long observationId, String obserVationFor) {
		// TODO Auto-generated method stub
		Criteria cri = getSession().createCriteria(ObservationInturmentConfiguration.class);
		if(obserVationFor.equals("Acclimatization"))
			cri.add(Restrictions.eq("studyAcclamatizationDates.id", observationId));
		else
			cri.add(Restrictions.eq("studyTreatmentDataDates.id", observationId));
		return (ObservationInturmentConfiguration) cri.uniqueResult();
	}

	@Override
	public void saveInstrumentIpAddressData(InstrumentIpAddress instrumentIpAddress) {
		// TODO Auto-generated method stub
		InstrumentIpAddress ins = (InstrumentIpAddress) getSession().createCriteria(InstrumentIpAddress.class)
				.add(Restrictions.eq("instrumentName", instrumentIpAddress.getInstrumentName())).uniqueResult();
		if (ins == null)
			getSession().save(instrumentIpAddress);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StudyTestCodes> studyTestCodesOfObservation(Long id, Long insturmentId) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(StudyTestCodes.class)
				.add(Restrictions.eq("observationInturmentConfiguration.id", id))
				.add(Restrictions.eq("instrument.id", insturmentId)).add(Restrictions.eq("activeStatus", true))
				.list();
	}

}
