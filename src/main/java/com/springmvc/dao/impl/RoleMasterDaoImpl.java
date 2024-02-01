package com.springmvc.dao.impl;

import java.util.List;

import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.RoleMasterDao;
import com.springmvc.model.RoleMaster;
@SuppressWarnings("unchecked")
@Repository("roleMasterDao")
public class RoleMasterDaoImpl extends AbstractDao<Long, RoleMaster> implements RoleMasterDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleMaster> findAll() {
		// TODO Auto-generated method stub
		return createEntityCriteria().list();
	}

	@Override
	public RoleMaster findById(long id) {
		// TODO Auto-generated method stub
		return (RoleMaster) createEntityCriteria().add(Restrictions.eq("id", id)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleMaster> findAllActiveRolesExceptSuperAdmin() {
		// TODO Auto-generated method stub
		return createEntityCriteria()
				.add(Restrictions.eq("status", 'T'))
				.add(Restrictions.ne("role", "SUPERADMIN")).list();
	}

	
	@Override
	public List<RoleMaster> rolesByIds(List<Long> roleIds) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(RoleMaster.class).add(Restrictions.in("id", roleIds)).list();
	}

	@Override
	public RoleMaster roleIdByRoleName(String roleCode) {
		return (RoleMaster) createEntityCriteria().add(Restrictions.eq("role", roleCode)).uniqueResult();
	}

	@Override
	public RoleMaster saveRoleMaster(RoleMaster roleMaster) {
		// TODO Auto-generated method stub
		getSession().save(roleMaster);
		return roleMaster;
	}

	
}
