package com.springmvc.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.StatusDao;
import com.springmvc.model.Species;
import com.springmvc.model.StaticData;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.WorkFlow;
import com.springmvc.model.WorkFlowReviewStages;

@Repository("statusDao")
public class StatusDaoImpl extends AbstractDao<Long, StatusMaster> implements StatusDao {

	@Override
	public StatusMaster findById(long statusId) {
		// TODO Auto-generated method stub
		return getByKey(statusId);
	}

	@Override
	public StatusMaster statusMaster(String string) {
		// TODO Auto-generated method stub
		return (StatusMaster) getSession().createCriteria(StatusMaster.class)
				.add(Restrictions.eq("statusCode", string)).uniqueResult();
	}

	@Override
	public StaticData staticData(String fieldName, String code) {
		// TODO Auto-generated method stub
		return (StaticData) getSession().createCriteria(StaticData.class)
				.add(Restrictions.eq("fieldName", fieldName))
				.add(Restrictions.eq("code", code)).uniqueResult();
	}

	@Override
	public List<StatusMaster> fiendAll() {
		// TODO Auto-generated method stub
		return createEntityCriteria().list();
	}

	@Override
	public StatusMaster saveStatusMaster(StatusMaster statusMaster) {
		// TODO Auto-generated method stub
		getSession().save(statusMaster);
		return statusMaster;
	}

	@Override
	public void saveStaticData(StaticData staticData) {
		// TODO Auto-generated method stub
		StaticData sd = (StaticData) getSession().createCriteria(StaticData.class).add(Restrictions.eq("code", staticData.getCode())).uniqueResult();
		if(sd == null)
			getSession().save(staticData);
	}

	@Override
	public void saveSpecies(Species species) {
		Species sd = (Species) getSession().createCriteria(Species.class).add(Restrictions.eq("code", species.getCode())).uniqueResult();
		if(sd == null)
			getSession().save(species);
	}

	@Override
	public WorkFlow saveWorkFlow(WorkFlow workFlow) {
		// TODO Auto-generated method stub
		WorkFlow sd = (WorkFlow) getSession().createCriteria(WorkFlow.class).add(Restrictions.eq("name", workFlow.getName()))
				.add(Restrictions.eq("workFlowCode", workFlow.getWorkFlowCode())).uniqueResult();
		if(sd == null) {
			getSession().save(workFlow);
			return workFlow;
		}else
			return sd;
	}

	@Override
	public void saveworkFlowReviewStages(WorkFlowReviewStages workFlowReviewStages) {
		// TODO Auto-generated method stub
		Criteria cri = getSession().createCriteria(WorkFlowReviewStages.class)
				.add(Restrictions.eq("fromRole", workFlowReviewStages.getFromRole()))
				.add(Restrictions.eq("workFlow", workFlowReviewStages.getWorkFlow()));
		if(workFlowReviewStages.getToRole() != null) {
			cri.add(Restrictions.eq("toRole", workFlowReviewStages.getToRole()));
		}
		WorkFlowReviewStages sd = (WorkFlowReviewStages) cri.uniqueResult();
		if(sd == null)
			getSession().save(workFlowReviewStages);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StaticData> staticDataList() {
		// TODO Auto-generated method stub
		return getSession().createCriteria(StaticData.class)
				.list();
	}

	
	
}
