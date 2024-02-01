package com.springmvc.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.covide.crf.dto.CRFGroupItem;
import com.covide.crf.dto.CRFGroupItemStd;
import com.covide.crf.dto.CRFItemValuesStd;
import com.covide.crf.dto.CRFSections;
import com.covide.crf.dto.CRFSectionsStd;
import com.covide.crf.dto.CrfItems;
import com.covide.crf.dto.CrfItemsStd;
import com.covide.crf.dto.CrfMetaData;
import com.covide.crf.dto.CrfMetaDataStd;
import com.covide.crf.dto.DTNAME;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.EmployeeDao;
import com.springmvc.dao.StatusDao;
import com.springmvc.dao.StudyDao;
import com.springmvc.model.AddressMasterLog;
import com.springmvc.model.EmergencyContMasterLog;
import com.springmvc.model.EmpJobMasterLog;
import com.springmvc.model.EmployeeMaster;
import com.springmvc.model.EmployeeMasterLog;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.LoginUsersLog;
import com.springmvc.model.ReviewLevel;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.StudyMaster;

@Repository("employeeDao")
public class EmployeeDaoImpl extends AbstractDao<Long, EmployeeMaster> implements EmployeeDao {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	StatusDao statusDao;
	@Autowired
	StudyDao studyDao;

	@Override
	public EmployeeMaster getEmployeeByEmpId(String empId) {
		// TODO Auto-generated method stub
		return (EmployeeMaster) createEntityCriteria().add(Restrictions.eq("empId", empId)).uniqueResult();
	}

	@Override
	public boolean saveEmpDetails(EmployeeMaster employeeMaster, Long roleId) {
		// TODO Auto-generated method stub
		RoleMaster role = null;
		try {
			StudyMaster sm = studyDao.findByStudyNo("default_study");
			
			//getSession().save(employeeMaster.getAddressMaster());
			//getSession().save(employeeMaster.getEmerContMaster());
//			employeeMaster.setRole((RoleMaster)getSession().get(RoleMaster.class, employeeMaster.getRole().getId()));
			role = (RoleMaster) getSession().createCriteria(RoleMaster.class)
					.add(Restrictions.eq("id", roleId)).uniqueResult();
			employeeMaster.setRole(role);
			getSession().save(employeeMaster);
			
			
			LoginUsers loginUsers = new LoginUsers();
			loginUsers.setActiveStudy(sm);
			loginUsers.setCreatedBy(employeeMaster.getCreatedBy());
			loginUsers.setCreatedOn(new Date());
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, 1); // to get previous year add -1
			Date nextYear = cal.getTime();
			loginUsers.setAccountexprie(nextYear);
			loginUsers.setAccountNotDisable(true);
			loginUsers.setAccountNotLock(true);
			String fullName = employeeMaster.getFirstName()+" "+employeeMaster.getMiddleName()+" "+employeeMaster.getLastName();
			loginUsers.setFullName(fullName);
			loginUsers.setUsername(employeeMaster.getEmpId());
			loginUsers.setRole(role);
			loginUsers.setPassword(passwordEncoder.encode(loginUsers.getUsername()+"@123"));
			if(role.isTranPassword())
				loginUsers.setTranPassword(passwordEncoder.encode(loginUsers.getUsername()+"@123456"));
			System.out.println(loginUsers.toString());
			getSession().save(loginUsers);
			/*@SuppressWarnings("unchecked")
			List<StudyMaster> smlist0 = getSession().createCriteria(StudyMaster.class).list();
			for(StudyMaster study : smlist0) {
				UserWiseStudiesAsignMaster uwsam = null;
				uwsam = (UserWiseStudiesAsignMaster) getSession().createCriteria(UserWiseStudiesAsignMaster.class)
						.add(Restrictions.eq("userId", loginUsers))
						.add(Restrictions.eq("studyMaster", study)).uniqueResult();
				if(uwsam == null) {
					uwsam = new UserWiseStudiesAsignMaster();
					uwsam.setStudyMaster(study);
					uwsam.setUserId(loginUsers);
					getSession().save(uwsam);
				}
				
				List<StudySite> sites = getSession().createCriteria(StudySite.class)
						.add(Restrictions.eq("studyMaster", study))
						.list();
				for(StudySite ss : sites) {
					UserWiseSitesAsignMaster uwsiam = null;
					uwsiam = (UserWiseSitesAsignMaster) getSession().createCriteria(UserWiseSitesAsignMaster.class)
							.add(Restrictions.eq("userId", loginUsers))
							.add(Restrictions.eq("site", ss)).uniqueResult();
					if(uwsiam == null)
						uwsiam = new UserWiseSitesAsignMaster();
					uwsiam.setStudyMaster(study);
					uwsiam.setUserId(loginUsers);
					uwsiam.setSite(ss);
					getSession().save(uwsiam);
				}
			}*/
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public boolean updateEmpDetails(EmployeeMaster employeeMaster) {
		// TODO Auto-generated method stub
		try {
			EmployeeMaster em = getEmployeeByEmpId(employeeMaster.getEmpId());
			
			try {
				EmployeeMasterLog emlog = new EmployeeMasterLog();
				AddressMasterLog amlog = new AddressMasterLog();
				BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
				BeanUtils.copyProperties(amlog, em.getAddressMaster());
				amlog.setId(0L);
				EmpJobMasterLog ejmlog = new EmpJobMasterLog();
				BeanUtils.copyProperties(ejmlog, em.getJobMaster());
				ejmlog.setId(0L);
				EmergencyContMasterLog emclog = new EmergencyContMasterLog();
				BeanUtils.copyProperties(emclog, em.getEmerContMaster());
				emclog.setId(0L);
				emlog.setEmailId(em.getEmailId());
				emlog.setFirstName(em.getFirstName());
				emlog.setMiddleName(em.getMiddleName());
				emlog.setLastName(em.getLastName());
				emlog.setBirthDate(em.getBirthDate());
				emlog.setGender(em.getGender());
				emlog.setMaritalStatus(em.getMaritalStatus());
				emlog.setMobileNo(em.getMobileNo());
				emlog.setAltMobileNo(em.getAltMobileNo());
				emlog.setPhoneNo(em.getPhoneNo());
				emlog.setEmailId(em.getEmailId());
				emlog.setFaxNo(em.getFaxNo());
				emlog.setAddressMaster(amlog);
				emlog.setLoginCredantials(em.isLoginCredantials());
				emlog.setJobMaster(ejmlog);
				emlog.setEmerContMaster(emclog);
				
				getSession().save(amlog);
				getSession().save(ejmlog);
				getSession().save(emclog);
				getSession().save(emlog);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			employeeMaster.setId(em.getId());
			employeeMaster.getAddressMaster().setId(em.getAddressMaster().getId());
			employeeMaster.getEmerContMaster().setId(em.getEmerContMaster().getId());
			employeeMaster.getJobMaster().setId(em.getJobMaster().getId());
			
			employeeMaster.getAddressMaster().setCreatedBy(em.getAddressMaster().getCreatedBy());
			employeeMaster.getAddressMaster().setCreatedOn(em.getAddressMaster().getCreatedOn());
			employeeMaster.getEmerContMaster().setCreatedBy(em.getEmerContMaster().getCreatedBy());
			employeeMaster.getEmerContMaster().setCreatedOn(em.getEmerContMaster().getCreatedOn());
			employeeMaster.getJobMaster().setCreatedBy(em.getJobMaster().getCreatedBy());
			employeeMaster.getJobMaster().setCreatedOn(em.getJobMaster().getCreatedOn());
			System.out.println(employeeMaster.getAddressMaster().getId());
			System.out.println(employeeMaster.getEmerContMaster().getId());
			System.out.println(employeeMaster.getJobMaster().getId());
			
			getSession().merge(employeeMaster.getAddressMaster());
			getSession().merge(employeeMaster.getEmerContMaster());
			getSession().merge(employeeMaster.getJobMaster());
			getSession().merge(employeeMaster);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	

	@Override
	public void saveCrf(CrfMetaData crf, DTNAME dt) {
		try {
			getSession().save(dt);
			getSession().save(crf);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CrfMetaData> findAllCrfs() {
		return getSession().createCriteria(CrfMetaData.class).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CrfMetaData> findAllCrfsAll() {
		List<CrfMetaData> ml  = getSession().createCriteria(CrfMetaData.class).list();
		
		for(CrfMetaData m : ml) {
			Hibernate.initialize(m.getSections());
			for(CRFSections s: m.getSections()) {
				Hibernate.initialize(s.getItemList());
				for(CrfItems i : s.getItemList()) {
					Hibernate.initialize(i.getItemResponceValues());
				}
			}
			Hibernate.initialize(m.getGroups());
			for(CRFGroupItem g : m.getGroups()) {
				Hibernate.initialize(g.getItemList());
				for(CrfItems i : g.getItemList()) {
					Hibernate.initialize(i.getItemResponceValues());
				}
			}
		}
		return ml;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CrfMetaDataStd> findAllStdCrfs(StudyMaster sm) {
		List<CrfMetaDataStd> list = getSession().createCriteria(CrfMetaDataStd.class)
				.add(Restrictions.eq("std", sm))
				.list();
		return list;
	}

	@Override
	public void savestdCrf1(CrfMetaDataStd std) {
			getSession().save(std);
	}
	
	@Override
	public void savestdCrfSections1(CRFSectionsStd std) {
		getSession().save(std);
	}
	@Override
	public void savestdCrfSections(List<CRFSectionsStd> crf) {
		for(CRFSectionsStd std : crf)
			getSession().save(std);
	}
	
	@Override
	public void savestdCrfGroups1(CRFGroupItemStd crf) {
		getSession().save(crf);
	}
	@Override
	public void savestdCrfGroups(List<CRFGroupItemStd> crf) {
		for(CRFGroupItemStd std : crf)
			getSession().save(std);
	}
	
	@Override
	public void savestdCrfEle1(CrfItemsStd crf) {
		getSession().save(crf);
	}
	
	@Override
	public void savestdCrfEle(List<CrfItemsStd> crf) {
		for(CrfItemsStd std : crf)
			getSession().save(std);
	}
	
	@Override
	public void savestdCrfEleVal(List<CRFItemValuesStd> crf) {
		for(CRFItemValuesStd std : crf)
			getSession().save(std);
	}
	
	@Override
	public void savestdCrf(List<CrfMetaDataStd> crf) {
			for(CrfMetaDataStd std : crf)
				getSession().save(std);
	}


	
	@Override
	public void updatestdCrf(CrfMetaDataStd scrf) {
		// TODO Auto-generated method stub
		getSession().update(scrf);
	}

	@Override
	public void updatestdCrf(List<CrfMetaDataStd> stdcrfsUpdate) {
		// TODO Auto-generated method stub
			for(CrfMetaDataStd std : stdcrfsUpdate)
				getSession().update(std);
	}

	@Override
	public boolean changeStatus(LoginUsers lu) {
		try {
			getSession().update(lu);
			return true;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void saveLoginUsersLog(LoginUsersLog lulog) {
		// TODO Auto-generated method stub
		getSession().save(lulog);
	}

	@Override
	public ReviewLevel reviewLevel() {
		ReviewLevel rl = null;
		try {
			rl = (ReviewLevel) getSession().createCriteria(ReviewLevel.class)
					.add(Restrictions.eq("status", "Active")).uniqueResult();	
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if(rl == null)
			rl = new ReviewLevel();
		return rl;
	}

	@Override
	public boolean saveReviewLevel(int observationApprovelLevel, String userName) {
		try{
			ReviewLevel oldRL = reviewLevel();
			if(oldRL.getObservationApprovelLevel() != 0) {
				oldRL.setStatus("In-Active");
				oldRL.setUpdatedBy(userName);
				oldRL.setUpdatedOn(new Date());
			}
			ReviewLevel rl =  new ReviewLevel();
			rl.setObservationApprovelLevel(observationApprovelLevel);
			rl.setCreatedBy(userName);
			rl.setCreatedOn(new Date());
			getSession().save(rl);
			
			return true;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	
	
}
