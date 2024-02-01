package com.springmvc.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.UserDao;
import com.springmvc.model.ClinicalCodes;
import com.springmvc.model.ClinicalCodesLog;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.StudyAnimal;

@Repository("clinicalCodesDao")
@PropertySource(value = { "classpath:application.properties" })
@SuppressWarnings("unchecked")
public class ClinicalCodesDaoImple extends AbstractDao<Long, ClinicalCodes> {
	@Autowired
	UserDao userDao;

	public List<ClinicalCodes> allClinicalCodes() {
		// TODO Auto-generated method stub
		return createEntityCriteria().list();
	}

	public boolean mergeClinicalCodes(Long userId, Map<Long, String> oldDescription, Map<String, String> newHeadings,
			Map<String, String> newClinicalCode, Map<String, String> newClinicalSign,
			Map<String, String> newDescription, Map<String, String> newrank) {
		try {
			LoginUsers user = userDao.findById(userId);
			List<ClinicalCodes> testCodeUnits = allClinicalCodes();
			testCodeUnits.forEach((code) -> {
				boolean flag = false;
				System.out.println(oldDescription);
				if (oldDescription.containsKey(code.getId())
						&& (!oldDescription.get(code.getId()).equals(code.getDescription()))) {
					try {
						saveClinicalCodesLog(code);
					} catch (Exception e) {
						e.printStackTrace();
					}
					code.setActiveStatus(true);
					code.setDescription(oldDescription.get(code.getId()));
					flag = true;

				} else if (!oldDescription.containsKey(code.getId())) {
					if (code.isActiveStatus()) {
						try {
							saveClinicalCodesLog(code);
						} catch (Exception e) {
							e.printStackTrace();
						}
						code.setActiveStatus(false);
						flag = true;
					}
				} else if (oldDescription.containsKey(code.getId()) && !code.isActiveStatus()) {
					try {
						saveClinicalCodesLog(code);
					} catch (Exception e) {
						e.printStackTrace();
					}
					code.setActiveStatus(true);
					flag = true;
				}
				if (flag) {
					code.setUpdatedBy(user.getUsername());
					code.setUpdatedOn(new Date());
					getSession().merge(code);
				}
			});
			newHeadings.forEach((key, value) -> {
				ClinicalCodes newCode = new ClinicalCodes();
				newCode.setHeadding(value);
				newCode.setSubHeadding(value);
				newCode.setClinicalCode(newClinicalCode.get(key));
				newCode.setClinicalSign(newClinicalSign.get(key));
				newCode.setDescription(newDescription.get(key));
				newCode.setRank(newrank.get(key));
				persist(newCode);
			});
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private void saveClinicalCodesLog(ClinicalCodes tcu) throws IllegalAccessException, InvocationTargetException {
		ClinicalCodesLog log = new ClinicalCodesLog();
		BeanUtils.copyProperties(log, tcu);
		log.setId(null);
		log.setClinicalCodes(tcu);
		getSession().save(log);
	}

	public List<ClinicalCodes> allActiveClinicalCodes() {
		// TODO Auto-generated method stub
		return createEntityCriteria().add(Restrictions.eq("activeStatus", true)).list();
	}

	public List<ClinicalCodes> clinicalCodesWithCode(String collectionCodeName, String code) {
		// TODO Auto-generated method stub
		if (collectionCodeName.equalsIgnoreCase("Clinical Sign") || collectionCodeName.equalsIgnoreCase("Home Cage Observation")
				|| collectionCodeName.equalsIgnoreCase("Handling Observations")
				|| collectionCodeName.equalsIgnoreCase("Open Field Observations"))
			return createEntityCriteria().add(Restrictions.eq("headding", collectionCodeName))
					.add(Restrictions.eq("activeStatus", true))
					.add(Restrictions.ilike("clinicalCode", code, MatchMode.ANYWHERE)).list();
		else
			return createEntityCriteria().add(Restrictions.ilike("clinicalCode", code, MatchMode.ANYWHERE)).list();
	}

	public List<ClinicalCodes> getClinicalCodes(List<String> value) {
		// TODO Auto-generated method stub
		List<Long> ids = new ArrayList<>();
		for (String s : value) {
			if (!ids.contains(Long.parseLong(s)))
				ids.add(Long.parseLong(s));
		}
		return createEntityCriteria().add(Restrictions.in("id", ids)).list();
	}

	public List<String> animalNumber(String code, Long studyId, String type) {
		Criteria cri = getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", studyId));
		if (type.equals("Acclamatization")) {
			cri.setProjection(Projections.property("animalNo"));
			cri.add(Restrictions.ilike("animalNo", code, MatchMode.ANYWHERE));
		} else if (type.equals("Treatment")) {
			cri.setProjection(Projections.property("permanentNo"));
			cri.add(Restrictions.ilike("permanentNo", code, MatchMode.ANYWHERE));
		}
		List<String> list = cri.list();
		list.add("NILL");
		return list;
	}
}