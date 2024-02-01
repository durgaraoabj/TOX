package com.springmvc.dao.impl;

import java.util.List;

import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.covide.dto.SatusAndWorkFlowDetailsDto;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.StudyDesignReviewDao;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.WorkFlowStatusDetails;

@Repository("studyDesignReviewDao")
public class StudyDesignReviewDaoImpl extends AbstractDao<Long, StudyMaster> implements StudyDesignReviewDao {

	@SuppressWarnings("unchecked")
	@Override
	public String checReviewProcessDetails(Long studyId) {
		String result = "Stop";
		List<Long> subIds = null;
		List<StdSubGroupObservationCrfs> ssgocList = null;
		try {
			subIds = getSession().createCriteria(SubGroupInfo.class)
					.add(Restrictions.eq("study.id", studyId))
					.setProjection(Projections.property("id")).list();
			if(subIds != null && subIds.size() > 0) {
				for(Long subId : subIds) {
					ssgocList = getSession().createCriteria(StdSubGroupObservationCrfs.class)
							.add(Restrictions.eq("study.id", studyId))
							.add(Restrictions.eq("subGroupInfo.id", subId)).list();
					if(ssgocList == null || ssgocList.size() == 0) {
						result = "Stop";
						break;
					}else result ="Proceed";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public StudyDesignStatus getStudyStatusDesignStatusRecord(Long studyId) {
		return (StudyDesignStatus) getSession().createCriteria(StudyDesignStatus.class)
				.add(Restrictions.eq("studyId", studyId)).uniqueResult();
	}

	
	@Override
	public boolean updateStudyDesingRecord(StudyDesignStatus sds, ApplicationAuditDetails apad) {
		boolean flag = false;
		long no = 0;
		try {
			getSession().update(sds);
			no = (long) getSession().save(apad);
			if(no > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}

	@Override
	public SatusAndWorkFlowDetailsDto getSatusAndWorkFlowDetailsDtoDetails(String statusCode, Long studyId) {
		SatusAndWorkFlowDetailsDto sawfdd = null;
		WorkFlowStatusDetails wfsd = null;
		StatusMaster fromStatus = null;
		StatusMaster toStatus = null;
		StudyMaster study = null;
		try {
			study = (StudyMaster) getSession().createCriteria(StudyMaster.class)
					.add(Restrictions.eq("id", studyId)).uniqueResult();
			
			fromStatus = (StatusMaster) getSession().createCriteria(StatusMaster.class)
					.add(Restrictions.eq("statusCode", "EDOROD")).uniqueResult();
			
			toStatus = (StatusMaster) getSession().createCriteria(StatusMaster.class)
					.add(Restrictions.eq("statusCode", statusCode)).uniqueResult();
			
			wfsd = (WorkFlowStatusDetails) getSession().createCriteria(WorkFlowStatusDetails.class)
					.add(Restrictions.eq("fromStatus", fromStatus))
					.add(Restrictions.eq("toStatus", toStatus)).uniqueResult();
			if(toStatus != null) {
				sawfdd = new SatusAndWorkFlowDetailsDto();
				sawfdd.setSm(toStatus);
				sawfdd.setWfsd(wfsd);
				sawfdd.setStudy(study);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sawfdd;
	}

}
