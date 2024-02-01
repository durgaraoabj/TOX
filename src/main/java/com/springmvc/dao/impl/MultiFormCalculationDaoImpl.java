package com.springmvc.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfGroup;
import com.covide.crf.dto.CrfGroupElement;
import com.covide.crf.dto.CrfSection;
import com.covide.crf.dto.CrfSectionElement;
import com.covide.crf.dto.CrfSectionElementData;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.MultiFormCalculationDao;
import com.springmvc.model.StudyMaster;

@Repository("multiFormCalculationDao")
public class MultiFormCalculationDaoImpl extends AbstractDao<Long, StudyMaster> implements MultiFormCalculationDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Crf> getCrfsRecordsList() {
		return getSession().createCriteria(Crf.class).list();
	}

	@Override
	public Crf getCrfRecord(Long crfId) {
		Crf crf = null;
		try {
			crf = (Crf) getSession().createCriteria(Crf.class)
					.add(Restrictions.eq("id", crfId)).uniqueResult();
			if(crf != null) {
				Hibernate.initialize(crf.getSections());
				Collections.sort(crf.getSections());
				for(CrfSection sec : crf.getSections()) {
					Hibernate.initialize(sec.getElement());
					if(sec.getElement() == null || sec.getElement().size() == 0) {
					}else {
						for(CrfSectionElement ele : sec.getElement()) {
							Hibernate.initialize(ele.getElementValues());
						}
					}
					Hibernate.initialize(sec.getGroup());
					CrfGroup group = sec.getGroup();
					if(group != null) {
						Hibernate.initialize(group.getElement());
						if(group.getElement() == null || group.getElement().size()==0) {
						}else {
							for(CrfGroupElement ele : group.getElement()) {
								Hibernate.initialize(ele.getElementValues());
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return crf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getFormulaDataElements(Long crfId) {
		Map<String, String> formulaMap = new HashMap<>();
		List<CrfSectionElement> eleList = null;
		List<Long> secIds = null;
		try {
			secIds =  getSession().createCriteria(CrfSection.class)
					.add(Restrictions.eq("crf.id", crfId))
					.setProjection(Projections.property("id")).list();
			if(secIds != null && secIds.size() > 0) {
				eleList = getSession().createCriteria(CrfSectionElement.class)
						.add(Restrictions.in("section.id", secIds)).list();
			}
			if(eleList != null && eleList.size() > 0) {
				for(CrfSectionElement ele : eleList) {
					if(!ele.getFormula().equals("") && !ele.getFormula().equals("No")) {
						formulaMap.put(ele.getId()+"_"+ele.getName(), ele.getFormula());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formulaMap;
	}

	@Override
	public String getActualElementValue(String crfKeyName, String eleName, Long studyId, Long animalId) {
		String value ="";
	    long crfId = 0;
	    long elementId =0;
	    long secId =0;
		try {
			crfId = (long) getSession().createCriteria(Crf.class)
					.add(Restrictions.eq("crfCode", crfKeyName))
					.setProjection(Projections.property("id")).uniqueResult();
			if(crfId != 0) {
				
				secId =  (long) getSession().createCriteria(CrfSection.class)
						.add(Restrictions.eq("crf.id", crfId))
						.setProjection(Projections.property("id")).uniqueResult();
				if(secId != 0) {
					elementId = (long) getSession().createCriteria(CrfSectionElement.class)
							.add(Restrictions.eq("name", eleName))
							.add(Restrictions.eq("section.id", secId))
							.setProjection(Projections.property("id")).uniqueResult();
				}
				if(elementId != 0) {
					value = (String) getSession().createCriteria(CrfSectionElementData.class)
							.add(Restrictions.eq("element.id", elementId))/*
							.add(Restrictions.eq("study.id", studyId))
							.add(Restrictions.eq("subGroupAnimalsInfoAll.id", animalId))*/
							.setProjection(Projections.property("value")).uniqueResult();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

}
