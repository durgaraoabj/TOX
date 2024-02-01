package com.springmvc.dao.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.UserWiseStudiesAsignDao;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.UserWiseSitesAsignMaster;
import com.springmvc.model.UserWiseStudiesAsignMaster;

@Repository("userWiseStudiesAsignDao")
public class UserWiseStudiesAsignDaoImpl extends AbstractDao<Long, UserWiseStudiesAsignMaster>
		implements UserWiseStudiesAsignDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<UserWiseStudiesAsignMaster> findByUserWiseList(LoginUsers user) {
		// TODO Auto-generated method stub
		return createEntityCriteria().add(Restrictions.eq("userId", user)).list();
	}

	@Override
	public void saveStudyAsignStudyCreationTime(List<UserWiseStudiesAsignMaster> userWisePermission) {
		// TODO Auto-generated method stub
		for (UserWiseStudiesAsignMaster uwsam : userWisePermission) {
			getSession().save(uwsam);
//			System.out.println(uwsam.getRoleId().getId()+"\t"+uwsam.getUserId().getId()+"\t"+uwsam.getStudyMaster().getId()+"\t"+uwsam.getId());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserWiseStudiesAsignMaster> findByStudyWiseList(StudyMaster study) {
		// TODO Auto-generated method stub
		List<UserWiseStudiesAsignMaster> list = createEntityCriteria().add(Restrictions.eq("studyMaster", study))
				.list();
		for (UserWiseStudiesAsignMaster user : list) {
			Hibernate.initialize(user);
		}
		return list;
	}

	@Override
	public void updateUserWiseStudiesAsignMaster(List<UserWiseStudiesAsignMaster> userUpdate) {
		for (UserWiseStudiesAsignMaster userWiseStudiesAsignMaster : userUpdate) {
			getSession().update(userWiseStudiesAsignMaster);
		}
	}

	@Override
	public List<UserWiseSitesAsignMaster> findByStudyWiseSitesList(StudyMaster study) {
		List<UserWiseSitesAsignMaster> list = getSession().createCriteria(UserWiseSitesAsignMaster.class)
				.add(Restrictions.eq("studyMaster", study)).list();
		for (UserWiseSitesAsignMaster user : list) {
			Hibernate.initialize(user);
			Hibernate.initialize(user.getStudyMaster());
			Hibernate.initialize(user.getSite());
		}
		return list;
	}

	@Override
	public void saveUserWiseSitesAsignMaster(List<UserWiseSitesAsignMaster> usersave) {
		for (UserWiseSitesAsignMaster userWiseSitesAsignMaster : usersave) {
			getSession().save(userWiseSitesAsignMaster);
		}
	}

	@Override
	public void updateUserWiseSitesAsignMaster(List<UserWiseSitesAsignMaster> userUpdate) {
		for (UserWiseSitesAsignMaster userWiseSitesAsignMaster : userUpdate) {
			getSession().update(userWiseSitesAsignMaster);
		}
	}

	@Override
	public List<UserWiseSitesAsignMaster> findByStudyWiseSitesListByUser(StudyMaster study, LoginUsers users) {
		List<UserWiseSitesAsignMaster> list = getSession().createCriteria(UserWiseSitesAsignMaster.class)
				.add(Restrictions.eq("studyMaster", study)).add(Restrictions.eq("userId", users)).list();
		for (UserWiseSitesAsignMaster user : list) {
			Hibernate.initialize(user);
			Hibernate.initialize(user.getStudyMaster());
			Hibernate.initialize(user.getSite());
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleMaster> getRoleMasterRecordsList() {
		return getSession().createCriteria(RoleMaster.class).add(Restrictions.ne("id", 1l)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserWiseStudiesAsignMaster> getStudyLevleRolesList(Long studyId) {
		return getSession().createCriteria(UserWiseStudiesAsignMaster.class)
				.add(Restrictions.eq("studyMaster.id", studyId)).list();
	}

	@Override
	public UserWiseStudiesAsignMaster userWiseStudiesAsignMaster(Long studyId, Long userId) {
		// TODO Auto-generated method stub
		return (UserWiseStudiesAsignMaster) createEntityCriteria().add(Restrictions.eq("studyMaster.id", studyId))
				.add(Restrictions.eq("userId.id", userId)).add(Restrictions.eq("status", 'T')).uniqueResult();
	}

}
