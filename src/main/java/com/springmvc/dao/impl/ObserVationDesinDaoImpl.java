package com.springmvc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.covide.dto.ObservationScheduleDto;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.ObserVationDesinDao;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;

@Repository("obserVationDesinDao")
public class ObserVationDesinDaoImpl extends AbstractDao<Long, StudyMaster> implements ObserVationDesinDao {

	@SuppressWarnings("unchecked")
	@Override
	public ObservationScheduleDto getObservationScheduleDetails(Long studyId) {
		ObservationScheduleDto obvsdto = null;
		StudyMaster sm = null;
		List<StdSubGroupObservationCrfs> obvList = null;
		List<SubGroupAnimalsInfoAll> sgaList = null;
		List<SubGroupAnimalsInfoCrfDataCount> sgaicdcList = null;
		List<Long> subIds = new ArrayList<>();
		try {
			sm = (StudyMaster) getSession().createCriteria(StudyMaster.class)
					.add(Restrictions.eq("id", studyId)).uniqueResult();
			if(sm != null) {
				obvList = getSession().createCriteria(StdSubGroupObservationCrfs.class)
						.add(Restrictions.eq("study", sm)).list();
				if(obvList != null && obvList.size() > 0) {
					for(StdSubGroupObservationCrfs sgoc : obvList) {
						Hibernate.initialize(sgoc.getSubGroupInfo().getGroup());
						subIds.add(sgoc.getSubGroupInfo().getId());
					}
				}
				sgaList = getSession().createCriteria(SubGroupAnimalsInfoAll.class)
						.add(Restrictions.in("subGroup.id", subIds)).list();
				sgaicdcList = getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
						.add(Restrictions.in("subGroup.id", subIds)).list();
				obvsdto = new ObservationScheduleDto();
				obvsdto.setSm(sm);
				obvsdto.setObvList(obvList);
				obvsdto.setSgaicdcList(sgaicdcList);
				obvsdto.setSgaList(sgaList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obvsdto;
	}

}
