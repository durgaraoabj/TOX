package com.springmvc.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.covide.enums.DepartmentMasterCodes;
import com.covide.enums.RoleMasterRoles;
import com.covide.enums.StudyStatus;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.DepartmentDao;
import com.springmvc.dao.EmployeeDao;
import com.springmvc.dao.RoleMasterDao;
import com.springmvc.dao.UserDao;
import com.springmvc.model.AccessionAnimalsDataEntryDetails;
import com.springmvc.model.AddressMaster;
import com.springmvc.model.DepartmentMaster;
import com.springmvc.model.EmergencyContMaster;
import com.springmvc.model.EmpJobMaster;
import com.springmvc.model.EmployeeMaster;
import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.RolesWiseModules;
import com.springmvc.model.StaticData;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudySite;
import com.springmvc.model.SysmexAnimalCode;
import com.springmvc.model.ToxUser;
import com.springmvc.model.UserWiseSitesAsignMaster;
import com.springmvc.model.UserWiseStudiesAsignMaster;
import com.springmvc.model.WorkFlow;
import com.springmvc.model.WorkFlowReviewAudit;
import com.springmvc.model.WorkFlowReviewStages;

@Repository("userDao")
public class UserDaoImpl extends AbstractDao<Long, LoginUsers> implements UserDao {
	static final Logger logger = Logger.getLogger(UserDaoImpl.class);

	@Override
	public LoginUsers findById(Long id) {
		LoginUsers user = (LoginUsers) getSession().createCriteria(LoginUsers.class).add(Restrictions.eq("id", id))
				.uniqueResult();
		Hibernate.initialize(user.getRole());
		return user;
	}

	@Override
	public LoginUsers findByActiveusername(String username) {
		
		logger.info("Username : {}" + username);
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("username", username));
		crit.add(Restrictions.eq("status", "Active"));
		LoginUsers user = (LoginUsers) crit.uniqueResult();
		if (user != null) {
			Hibernate.initialize(user.getRole());
			Hibernate.initialize(user.getActiveSite());
		}
		return user;
	}

	public static boolean codes = true;

	@Override
	public LoginUsers findByusername(String username) {
		logger.info("Username : {}" + username);
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("username", username));
		LoginUsers user = (LoginUsers) crit.uniqueResult();
		if (user != null) {
			Hibernate.initialize(user.getRole());
			Hibernate.initialize(user.getActiveSite());
		}
		if (codes) {
			Long count = (Long) getSession().createCriteria(SysmexAnimalCode.class)
					.setProjection(Projections.rowCount()).uniqueResult();
			if (count == 0l) {
				for (int i = 1; i <= 199; i++) {
					SysmexAnimalCode ne = new SysmexAnimalCode();
					getSession().save(ne);
					if (i < 10)
						ne.setCode("00" + i);
					else if (i < 100)
						ne.setCode("0" + i);
					else
						ne.setCode("" + i);
				}
				codes = false;

			}
		}

		return user;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LoginUsers> findAllUsers() {
		/*
		 * Criteria criteria = createEntityCriteria().addOrder(Order.asc("firstName"));
		 * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid
		 * duplicates.
		 */ List<LoginUsers> users = createEntityCriteria().list();
		return users;
	}

	@Override
	public void updateStudy(LoginUsers user) {
		// TODO Auto-generated method stub
		update(user);
	}

	@Override
	public void generateLoginDetails(LoginUsers loginUsers, long id) {
		// TODO Auto-generated method stub
		Query query = getSession()
				.createQuery("update EmployeeMaster set loginCredantials = :flag" + " where id = :id");
		query.setParameter("flag", true);
		query.setParameter("id", id);
		query.executeUpdate();
		getSession().save(loginUsers);

		ToxUser tuser = new ToxUser();
		tuser.setUserName(loginUsers.getUsername());
		tuser.setPassword(loginUsers.getUsername() + "@123");
		tuser.setLoginUserId(loginUsers);
		getSession().save(tuser);
		RoleMaster role = loginUsers.getRole();
		if (role.getRole().equals("Data Manager")) {
//			List<StudyMaster> smlist = new ArrayList<>();
			List<StudyMaster> smlist0 = getSession().createCriteria(StudyMaster.class).list();
			for (StudyMaster study : smlist0) {
				UserWiseStudiesAsignMaster uwsam = null;
				uwsam = (UserWiseStudiesAsignMaster) getSession().createCriteria(UserWiseStudiesAsignMaster.class)
						.add(Restrictions.eq("userId", loginUsers)).add(Restrictions.eq("studyMaster", study))
						.uniqueResult();
				if (uwsam == null) {
					uwsam = new UserWiseStudiesAsignMaster();
					uwsam.setStudyMaster(study);
					uwsam.setUserId(loginUsers);
					getSession().save(uwsam);
				}

				List<StudySite> sites = getSession().createCriteria(StudySite.class)
						.add(Restrictions.eq("studyMaster", study)).list();
				for (StudySite ss : sites) {
					UserWiseSitesAsignMaster uwsiam = null;
					uwsiam = (UserWiseSitesAsignMaster) getSession().createCriteria(UserWiseSitesAsignMaster.class)
							.add(Restrictions.eq("userId", loginUsers)).add(Restrictions.eq("site", ss)).uniqueResult();
					if (uwsiam == null)
						uwsiam = new UserWiseSitesAsignMaster();
					uwsiam.setStudyMaster(study);
					uwsiam.setUserId(loginUsers);
					uwsiam.setSite(ss);
					getSession().save(uwsiam);
				}
			}
		}
	}

	@Override
	public void updateLoginCredentials(LoginUsers checkLoginUser) {
		// TODO Auto-generated method stub
		getSession().update(checkLoginUser);
		RoleMaster role = checkLoginUser.getRole();
		if (role.getRole().equals("Data Manager")) {
//			List<StudyMaster> smlist = new ArrayList<>();
			List<StudyMaster> smlist0 = getSession().createCriteria(StudyMaster.class).list();
			for (StudyMaster study : smlist0) {
				UserWiseStudiesAsignMaster uwsam = null;
				uwsam = (UserWiseStudiesAsignMaster) getSession().createCriteria(UserWiseStudiesAsignMaster.class)
						.add(Restrictions.eq("userId", checkLoginUser)).add(Restrictions.eq("studyMaster", study))
						.uniqueResult();
				if (uwsam == null) {
					uwsam = new UserWiseStudiesAsignMaster();
					uwsam.setStudyMaster(study);
					uwsam.setUserId(checkLoginUser);
					getSession().save(uwsam);
				}

				List<StudySite> sites = getSession().createCriteria(StudySite.class)
						.add(Restrictions.eq("studyMaster", study)).list();
				for (StudySite ss : sites) {
					UserWiseSitesAsignMaster uwsiam = null;
					uwsiam = (UserWiseSitesAsignMaster) getSession().createCriteria(UserWiseSitesAsignMaster.class)
							.add(Restrictions.eq("userId", checkLoginUser)).add(Restrictions.eq("site", ss))
							.uniqueResult();
					if (uwsiam == null)
						uwsiam = new UserWiseSitesAsignMaster();
					uwsiam.setStudyMaster(study);
					uwsiam.setUserId(checkLoginUser);
					uwsiam.setSite(ss);
					getSession().save(uwsiam);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LoginUsers> findAllActiveUsers() {
		// TODO Auto-generated method stub
		return createEntityCriteria().add(Restrictions.eq("accountNotLock", true))
				.add(Restrictions.eq("accountNotDisable", true)).add(Restrictions.ge("accountexprie", new Date()))
				.add(Restrictions.eq("status", "Active")).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LoginUsers> dataUsersLoginUsers() {
		List<LoginUsers> list = new ArrayList<LoginUsers>();
		RoleMaster rm = (RoleMaster) getSession().createCriteria(RoleMaster.class)
				.add(Restrictions.eq("role", "Data Manager")).uniqueResult();
		List<LoginUsers> list1 = createEntityCriteria().add(Restrictions.eq("role", rm)).list();
		list.addAll(list1);

//		rm = (RoleMaster) getSession().createCriteria(RoleMaster.class).add(Restrictions.eq("role", "Data Manager")).uniqueResult();
//		List<LoginUsers> list2 = createEntityCriteria().add(Restrictions.eq("role", rm)).list();
//		list.addAll(list2);

		return list;
	}

	@Override
	public UserWiseStudiesAsignMaster getUserWiseSitesAsignMasterRecord(Long studyId, Long userId) {
		UserWiseStudiesAsignMaster uwsam = null;
		try {
			uwsam = (UserWiseStudiesAsignMaster) getSession().createCriteria(UserWiseStudiesAsignMaster.class)
					.add(Restrictions.eq("studyMaster.id", studyId)).add(Restrictions.eq("userId.id", userId))
					.add(Restrictions.eq("status", 'T')).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uwsam;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RolesWiseModules> getApplicationMenus(LoginUsers users) {
		return getSession().createCriteria(RolesWiseModules.class).add(Restrictions.eq("role", users.getRole()))
				.add(Restrictions.eq("status", "active")).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RolesWiseModules> getApplicationMenusBasedOnStudy(RoleMaster role) {
		return getSession().createCriteria(RolesWiseModules.class).add(Restrictions.eq("role", role))
				.add(Restrictions.eq("status", "active")).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RolesWiseModules> getRolesWiseModulesList() {
		return getSession().createCriteria(RolesWiseModules.class).list();
	}

	@Override
	public WorkFlow accessionWorkFlow(String string, StatusMaster activeStatus) {
		// TODO Auto-generated method stub
		return (WorkFlow) getSession().createCriteria(WorkFlow.class).add(Restrictions.eq("workFlowCode", string))
				.add(Restrictions.eq("activeStatus", activeStatus)).uniqueResult();
	}

	@Override
	public WorkFlowReviewStages workFlowReviewStages(LoginUsers user, WorkFlow accessionWorkFlow) {
		return (WorkFlowReviewStages) getSession().createCriteria(WorkFlowReviewStages.class)
				.add(Restrictions.eq("fromRole", user.getRole())).add(Restrictions.eq("workFlow", accessionWorkFlow))
				.add(Restrictions.eq("activeStatus", true)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkFlowReviewAudit> workFlowReviewAudit(AccessionAnimalsDataEntryDetails aaded, LoginUsers user) {
		return getSession().createCriteria(WorkFlowReviewAudit.class)
				.add(Restrictions.eq("accessionAnimalsDataEntryDetailsId.id", aaded.getId()))
				.add(Restrictions.eq("inTheFlow", true)).list();
	}

	@Override
	public List<WorkFlowReviewAudit> readamizationWorkFlowReviewAudit(Long id, LoginUsers user) {
		return getSession().createCriteria(WorkFlowReviewAudit.class)
				.add(Restrictions.eq("reamdamizationReivew.id", id)).add(Restrictions.eq("inTheFlow", true)).list();
	}

	@Override
	public List<WorkFlowReviewAudit> observationWorkFlowReviewAudit(Long id, LoginUsers user) {
		return getSession().createCriteria(WorkFlowReviewAudit.class)
				.add(Restrictions.eq("subjectDataEntryDetails.id", id)).add(Restrictions.eq("inTheFlow", true)).list();
	}


	private void instrumentIpAddress(StatusMaster activeStatus) {
		Long count = null;
		try {

			count = (Long) getSession().createCriteria(InstrumentIpAddress.class)
					.add(Restrictions.eq("activeStatus", activeStatus)).setProjection(Projections.rowCount())
					.uniqueResult();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (count == null || count == 0l) {
			InstrumentIpAddress instument = new InstrumentIpAddress("10.1.23.172", 8002, activeStatus);
			getSession().save(instument);
			instument = new InstrumentIpAddress("10.1.23.173", 8002, activeStatus);
			getSession().save(instument);
		}
	}

	@Override
	public RoleMaster roleMaster(String role) {
		// TODO Auto-generated method stub
		return (RoleMaster) getSession().createCriteria(RoleMaster.class).add(Restrictions.eq("role", role))
				.add(Restrictions.eq("status", 'T')).uniqueResult();
	}


	private void staticDataTable(StatusMaster activeStatus) {
		Long count = null;
		try {
			count = (Long) getSession().createCriteria(StaticData.class).setProjection(Projections.rowCount())
					.uniqueResult();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (count == null || count == 0l) {
			List<StaticData> list = new ArrayList<StaticData>();
			// study
			list.add(new StaticData(StudyStatus.ACTITYORCRF.toString(),
					"Appendix 2: Motor Activity Data of Individual Animal  ANNEXURE 4",
					StudyStatus.ACTIVITYWEIGHTFROM.toString()));
			list.add(new StaticData(StudyStatus.ACTITYORCRF.toString(), "total", StudyStatus.TOTAL.toString()));
			list.add(new StaticData(StudyStatus.ACTITYORCRF.toString(), "ambulatory",
					StudyStatus.AMBULATORY.toString()));
			list.add(new StaticData(StudyStatus.WEIGHTUNITS.toString(), "Kilogram", StudyStatus.KG.toString()));
			list.add(new StaticData(StudyStatus.WEIGHTUNITS.toString(), "Gram", StudyStatus.GR.toString()));
			for (StaticData fsd : list)
				getSession().save(fsd);
		}
	}

	@Override
	public List<WorkFlowReviewAudit> readamizationDtoWorkFlowReviewAudit(Long id, LoginUsers user) {
		return getSession().createCriteria(WorkFlowReviewAudit.class)
				.add(Restrictions.eq("randamizationDto.id", id)).add(Restrictions.eq("inTheFlow", true)).list();
	}	
}
