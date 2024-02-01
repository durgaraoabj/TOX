package com.springmvc.dao.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.AmendmentDao;
import com.springmvc.model.AmendmentDetails;
import com.springmvc.model.AmendmentDetailsLog;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.StudyMaster;

@Repository("amendmentDao")
public class AmendmentDaoImpl extends AbstractDao<Long, AmendmentDetails> implements AmendmentDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<AmendmentDetails> getAmendmentDetailsRecrodsList(Long studyNo) {
		return getSession().createCriteria(AmendmentDetails.class)
				.add(Restrictions.eq("studyId", studyNo))
				.add(Restrictions.ne("status", "Approved")).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AmendmentDetails> getAmendmentRecordsList(List<Long> amdIds) {
		return getSession().createCriteria(AmendmentDetails.class)
				.add(Restrictions.in("id", amdIds)).list();
	}

	@Override
	public StudyMaster getStudyMasterRecrod(Long studyNo) {
		return (StudyMaster) getSession().createCriteria(StudyMaster.class)
				.add(Restrictions.eq("id", studyNo)).uniqueResult();
	}

	@Override
	public boolean saveAmendmentDetails(List<AmendmentDetails> updateAmdList, List<AmendmentDetailsLog> amdLogList,
			ApplicationAuditDetails aad) {
		boolean flag = false;
		boolean updateAmdFlag = false;
		boolean amdLogFlag = false;
		try {
			if(amdLogList.size() > 0) {
				for(AmendmentDetailsLog amdLog : amdLogList) {
					getSession().save(amdLog);
					amdLogFlag = true;
				}
			}else amdLogFlag = true;
			
			if(updateAmdList.size() > 0) {
				for(AmendmentDetails amd : updateAmdList) {
					getSession().update(amd);
					updateAmdFlag = true;
				}
			}else updateAmdFlag = true;
			if(amdLogFlag && updateAmdFlag) {
				long aadNo = (long) getSession().save(aad);
				if(aadNo > 0)
					flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AmendmentDetails> getStudyAmendmentDetailsList(Long studyId) {
		return getSession().createCriteria(AmendmentDetails.class)
				.add(Restrictions.eq("studyId", studyId))
				.add(Restrictions.ne("status", "Approved"))
				.add(Restrictions.ne("status", "Close")).list();
	}

}
