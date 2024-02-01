package com.springmvc.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.ObservationInturmentConfiguration;
import com.springmvc.model.StudyTestCodes;

@SuppressWarnings("unchecked")
@Repository
public class StudyTestCodesImpl extends AbstractDao<Long, StudyTestCodes> {
	@Autowired
	InstrumentIpAddressDaoImpl instrumentIpAddressDao;
	public List<InstrumentIpAddress> instrumentIpAddressesOfObservation(Long observationInturmentConfigurationId) {
		// TODO Auto-generated method stub
		List<Long> instrumentIds =  createEntityCriteria()
				.add(Restrictions.eq("observationInturmentConfiguration.id", observationInturmentConfigurationId))
				.setProjection(Projections.property("instrument.id")).list();
		if(instrumentIds.size() > 0) {
			return instrumentIpAddressDao.instrumentIpAddressesOfListOfIds(new ArrayList<>(new HashSet<>(instrumentIds)));
		}else
			return new ArrayList<>();
	}
	public List<StudyTestCodes> testCodesOfInstrumentAndObservation(
			ObservationInturmentConfiguration observationInturmentConfiguration, InstrumentIpAddress ipaddress) {
		// TODO Auto-generated method stub
		return createEntityCriteria().add(Restrictions.eq("observationInturmentConfiguration.id", observationInturmentConfiguration.getId()))
				.add(Restrictions.eq("instrument.id", ipaddress.getId())).list();
	}

}
