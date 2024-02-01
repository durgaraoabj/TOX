package com.springmvc.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.RolesWiseMenusDao;
import com.springmvc.model.ApplicationSideMenuLinks;
import com.springmvc.model.ApplictionSideMenus;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.RolesWiseModules;

@Repository("rolesWiseMenusDao")
public class RolesWiseMenusDaoImpl extends AbstractDao<Long, ApplictionSideMenus> implements RolesWiseMenusDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<RolesWiseModules> getRolesWiseModulesRecordsList(Long roleId) {
		return getSession().createCriteria(RolesWiseModules.class)
				.add(Restrictions.eq("role.id", roleId))
				.add(Restrictions.eq("status", "active")).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleMaster> getRolesMasterRecords() {
		return getSession().createCriteria(RoleMaster.class).list();
	}

	@Override
	public RoleMaster getRoleMasterRecord(Long roleId) {
		return (RoleMaster) getSession().createCriteria(RoleMaster.class)
				.add(Restrictions.eq("id", roleId)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ApplicationSideMenuLinks> getApplicationSideMenusList(Long roleId) {
		List<ApplicationSideMenuLinks> apsList = null;
		try {
			List<Long> rwmIds = getSession().createCriteria(RolesWiseModules.class)
					.add(Restrictions.eq("role.id", roleId))
					.add(Restrictions.eq("status", "active"))
					.setProjection(Projections.property("appsideMenu.id")).list();
			if(rwmIds != null && rwmIds.size() > 0) {
				apsList = getSession().createCriteria(ApplicationSideMenuLinks.class)
						.add(Restrictions.not(Restrictions.in("id", rwmIds)))
						.add(Restrictions.eq("display", 'T'))
						.list();
			}else {
				apsList = getSession().createCriteria(ApplicationSideMenuLinks.class)
						.add(Restrictions.eq("display", 'T')).list();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return apsList; 
	}

	@Override
	public RoleMaster getRoleMasterRecordBasedOnId(Long roleId) {
		return (RoleMaster) getSession().createCriteria(RoleMaster.class)
				.add(Restrictions.eq("id", roleId)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean saveRoleBasedLinks(RoleMaster rolePojo, List<Long> linkIds, String userName) {
		boolean flag = false;
		try {
			List<ApplicationSideMenuLinks> apsmList = getSession().createCriteria(ApplicationSideMenuLinks.class)
					.add(Restrictions.in("id", linkIds))
					.add(Restrictions.eq("display", 'T'))
					.list();
			if(apsmList != null && apsmList.size() > 0) {
				for(ApplicationSideMenuLinks asm : apsmList) {
					RolesWiseModules rwm = (RolesWiseModules) getSession().createCriteria(RolesWiseModules.class)
							.add(Restrictions.eq("appsideMenu", asm))
							.add(Restrictions.eq("role", rolePojo)).uniqueResult();
					if(rwm != null) {
						rwm.setStatus("active");
						getSession().update(rwm);
						flag = true;
					}else {
						RolesWiseModules rwmPojo = new RolesWiseModules();
						rwmPojo.setAppsideMenu(asm);
						rwmPojo.setCreatedBy(userName);
						rwmPojo.setCreatedOn(new Date());
						rwmPojo.setRole(rolePojo);
						rwmPojo.setStatus("active");
						long no = (long) getSession().save(rwmPojo);
						if(no > 0)
							flag = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean inactiveApplicationSideLink(RoleMaster rolePojo, Long linkId, String userName) {
		boolean flag = false;
		try {
			RolesWiseModules rwm = (RolesWiseModules) getSession().createCriteria(RolesWiseModules.class)
					.add(Restrictions.eq("appsideMenu.id", linkId))
					.add(Restrictions.eq("role", rolePojo)).uniqueResult();
			if(rwm != null) {
				rwm.setStatus("inactive");
				getSession().update(rwm);
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	@Override
	public List<ApplicationSideMenuLinks> allApplicationSideMenuLinks() {
		// TODO Auto-generated method stub
		return getSession().createCriteria(ApplicationSideMenuLinks.class).list();
	}

	@Override
	public void saveApplicationSideMenu(ApplictionSideMenus applictionSideMenus) {
		// TODO Auto-generated method stub
		getSession().save(applictionSideMenus);
	}

	@Override
	public List<ApplictionSideMenus> allApplicationSideMenus() {
		// TODO Auto-generated method stub
		return getSession().createCriteria(ApplictionSideMenus.class).list();
	}

	@Override
	public ApplictionSideMenus applictionSideMenusByCode(String code) {
		// TODO Auto-generated method stub
		return (ApplictionSideMenus) getSession().createCriteria(ApplictionSideMenus.class).add(Restrictions.eq("code", code)).uniqueResult();
	}

	@Override
	public List<ApplicationSideMenuLinks> allApplicationSideMenuLinksOfSideMenu(Long id) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(ApplicationSideMenuLinks.class).add(Restrictions.eq("appsideMenu.id", id)).list();
	}

	@Override
	public void saveApplicationSideMenuLink(ApplicationSideMenuLinks applicationSideMenuLinks) {
		// TODO Auto-generated method stub
		getSession().save(applicationSideMenuLinks);
	}

	
}
