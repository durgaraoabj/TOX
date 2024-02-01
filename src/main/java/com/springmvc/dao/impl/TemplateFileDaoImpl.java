package com.springmvc.dao.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.covide.crf.dto.Crf;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.RoleMasterDao;
import com.springmvc.dao.TemplateFileDao;
import com.springmvc.model.ObserVationTemplates;
import com.springmvc.model.ObservationRole;
import com.springmvc.model.ObservationTemplateData;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.StatusMaster;

@Repository("templateFileDao")
public class TemplateFileDaoImpl extends AbstractDao<Long, StatusMaster> implements TemplateFileDao {
	@Autowired
	RoleMasterDao roleDao;
	@Override
	public boolean saveObservationTemplateData(ObservationTemplateData obtd, ObserVationTemplates obvt, Crf crf, List<Long> roleIds) {
		boolean flag = false;
		long obtdNo = 0;
		long obvtNo = 0;
		try {
			obtdNo = (long) getSession().save(obtd);
			if(obtdNo > 0) {
				obvt.setObstd(obtd);
				obvtNo = (long) getSession().save(obvt);
				if(obvtNo > 0) {
					crf.setTemplete(obtd);
					if(crf.getId() == null)
						getSession().save(crf);
					else
						getSession().merge(crf);
					List<RoleMaster> roles = roleDao.rolesByIds(roleIds);
					for(RoleMaster role : roles) {
						getSession().save(new ObservationRole(crf, role, crf.getCreatedBy()));
					}
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}

	@Override
	public Crf getCrfRecord(String obserName, String type) {
		if(type.equals("observation")) {
			return (Crf) getSession().createCriteria(Crf.class)
					.add(Restrictions.eq("observationName", obserName))
					.add(Restrictions.eq("active", true)).uniqueResult();
		}else if(type.equals("prefix")) {
			return (Crf) getSession().createCriteria(Crf.class)
					.add(Restrictions.eq("prefix", obserName))
					.add(Restrictions.eq("active", true)).uniqueResult();
		}else {
			return null;
		}
		
	}
	
	

}
