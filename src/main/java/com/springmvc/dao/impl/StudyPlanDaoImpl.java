package com.springmvc.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.StudyPlanDao;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPlanFilesDetails;

@Repository("studyPlanDao")
public class StudyPlanDaoImpl extends AbstractDao<Long, StudyMaster> implements StudyPlanDao {

	@Override
	public StudyPlanFilesDetails getStudyPlanFilesDetailsRecord(Long studyId) {
		return (StudyPlanFilesDetails) getSession().createCriteria(StudyPlanFilesDetails.class)
				.add(Restrictions.eq("studyId", studyId)).uniqueResult();
	}

	@Override
	public long saveStudyPlanFilesDetails(StudyPlanFilesDetails spfd) {
		return (long) getSession().save(spfd);
	}

}
