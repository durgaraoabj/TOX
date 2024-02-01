package com.springmvc.dao.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.StudyAuditDao;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;

@Repository("StudyAuditDao")
@PropertySource(value = { "classpath:application.properties" })
public class StudyAuditDaoImpl extends AbstractDao<Long, StudyMaster> implements StudyAuditDao {
	
	@Autowired
    private Environment environment;

	@SuppressWarnings("unchecked")
	@Override
	public List<GroupInfo> getGroupInfoForStudy(StudyMaster study) {
		List<GroupInfo> groupd = getSession().createCriteria(GroupInfo.class)
				.add(Restrictions.eq("study", study)).list();
		return groupd;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StdSubGroupObservationCrfs> getStdSubGroupObservationCrfsWithStudy(StudyMaster study) {
		List<StdSubGroupObservationCrfs> ssgocl =null;
		   ssgocl= getSession().createCriteria(StdSubGroupObservationCrfs.class)
				.add(Restrictions.eq("study", study)).list();
		   for(StdSubGroupObservationCrfs ssgoc:ssgocl) {
			   Hibernate.initialize(ssgoc.getSubGroupInfo().getGroup());
		   }
		return ssgocl;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubGroupAnimalsInfoCrfDataCount> getSubGroupAnimalsInfoCrfDataCountWithStudyId(StudyMaster study) {
		
		List<SubGroupAnimalsInfoCrfDataCount>  sgaicdcl=null;
		sgaicdcl=getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
		.add(Restrictions.eq("studyid", study.getId()))
		.addOrder(Order.desc("id")).list();
		for(SubGroupAnimalsInfoCrfDataCount cc:sgaicdcl) {
			   Hibernate.initialize(cc.getSubGroup());
			   Hibernate.initialize(cc.getSubGroup().getGroup());
		   }
		return sgaicdcl;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubGroupAnimalsInfoCrfDataCount> getSubGroupAnimalsInfoCrfDataCountWithStudyIdAndSubId(
			StudyMaster study, Long id) {
		List<SubGroupAnimalsInfoCrfDataCount> groupd = getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
				.add(Restrictions.eq("studyid", study.getId()))
				.add(Restrictions.eq("subGroup.id", id)).list();
		for(SubGroupAnimalsInfoCrfDataCount cc:groupd) {
			   Hibernate.initialize(cc.getGroup());
			   Hibernate.initialize(cc.getSubGroup());
		   }
		
		return groupd;
	}
	
	
	
}
