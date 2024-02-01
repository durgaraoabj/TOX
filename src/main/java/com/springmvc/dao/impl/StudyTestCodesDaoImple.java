package com.springmvc.dao.impl;

import java.util.List;import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.model.StudyTestCodes;

@SuppressWarnings("unchecked")
@Repository
public class StudyTestCodesDaoImple extends AbstractDao<Long, StudyTestCodes>{

	public List<StudyTestCodes> studyObservationInstumentTestCodes(Long observationInturmentConfigurationId, Long instrumentIpAddressId) {
		return createEntityCriteria().add(Restrictions.eq("observationInturmentConfiguration.id", observationInturmentConfigurationId))
				.add(Restrictions.eq("instrument.id", instrumentIpAddressId))
				.add(Restrictions.eq("activeStatus", true)).addOrder(Order.asc("orderNo")).list();
	}

}
