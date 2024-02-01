package com.springmvc.dao.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.model.ObservationInturmentConfiguration;

@Repository
public class ObservationInturmentConfigurationDaoImpl extends AbstractDao<Long, ObservationInturmentConfiguration> {
	@Autowired
	private Environment environment;

	public ObservationInturmentConfiguration observationInturmentConfigurationOfstudyAcclamatizationDates(
			Long studyAcclamatizationDateId, Long studyTreatmentDataDatesId) {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		ObservationInturmentConfiguration obj = null;
		if(studyAcclamatizationDateId != null) {
			obj = (ObservationInturmentConfiguration) createEntityCriteria()
					.add(Restrictions.eq("studyAcclamatizationDates.id", studyAcclamatizationDateId)).uniqueResult();
			if (obj != null)
				obj.getStudyAcclamatizationDates()
						.setDateString(sdf.format(obj.getStudyAcclamatizationDates().getAccDate()));			
		}else {
			obj = (ObservationInturmentConfiguration) createEntityCriteria()
					.add(Restrictions.eq("studyTreatmentDataDates.id", studyTreatmentDataDatesId)).uniqueResult();
			if (obj != null)
				obj.getStudyTreatmentDataDates()
						.setDateString(sdf.format(obj.getStudyTreatmentDataDates().getAccDate()));
		}

		return obj;
	}

	public List<Long> observationInturmentConfiguration(Long studyId,String observation) {
		Criteria cri = createEntityCriteria().add(Restrictions.eq("study.id", studyId))
				.setProjection(Projections.property("id"));
		if(observation.equals("Acclimatization")) {
			cri.add(Restrictions.isNotNull("studyAcclamatizationDates"));
		}else {
			cri.add(Restrictions.isNotNull("studyTreatmentDataDates"));
		}
		// TODO Auto-generated method stub
		return cri.list();
	}

}
