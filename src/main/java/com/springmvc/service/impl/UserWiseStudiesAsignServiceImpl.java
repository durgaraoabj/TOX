package com.springmvc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springmvc.dao.StudyDao;
import com.springmvc.dao.UserDao;
import com.springmvc.dao.UserWiseStudiesAsignDao;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.UserWiseSitesAsignMaster;
import com.springmvc.model.UserWiseStudiesAsignMaster;
import com.springmvc.service.UserWiseStudiesAsignService;

@Service("userWiseStudiesAsignService")
public class UserWiseStudiesAsignServiceImpl implements UserWiseStudiesAsignService {

	@Autowired
	UserWiseStudiesAsignDao userWiseStudiesAsignDao;
	
	@Autowired
	UserDao userDao;
	@Autowired
	StudyDao studyDao;
	@Override
	public List<UserWiseStudiesAsignMaster> findUserWiseStudies(String username) {
		// TODO Auto-generated method stub
		LoginUsers user = userDao.findByusername(username);
		return userWiseStudiesAsignDao.findByUserWiseList(user);
	}

	@Override
	public List<UserWiseStudiesAsignMaster> findUsersFindByStudy(StudyMaster study) {
		// TODO Auto-generated method stub
		return userWiseStudiesAsignDao.findByStudyWiseList(study);
	}

	@Override
	public List<UserWiseSitesAsignMaster> findByStudyWiseSitesList(StudyMaster study) {
		// TODO Auto-generated method stub
		return userWiseStudiesAsignDao.findByStudyWiseSitesList(study);
	}

	@Override
	public List<UserWiseSitesAsignMaster> findByStudyWiseSitesListByUser(StudyMaster study, LoginUsers users) {
		return userWiseStudiesAsignDao.findByStudyWiseSitesListByUser(study, users);
	}

	@Override
	public List<RoleMaster> getRolesMasterRecordsList() {
		return userWiseStudiesAsignDao.getRoleMasterRecordsList();
	}

	@Override
	public Map<Long, Long> getStudyLevleRolesList(Long studyId) {
		List<UserWiseStudiesAsignMaster> uwsamList = null;
		Map<Long, Long> stlrMap = new HashMap<>();
		try {
			uwsamList = userWiseStudiesAsignDao.getStudyLevleRolesList(studyId);
			if(uwsamList != null && uwsamList.size() > 0) {
				for(UserWiseStudiesAsignMaster uwsam : uwsamList) {
					if(uwsam.getRoleId() != null)
						stlrMap.put(uwsam.getUserId().getId(), uwsam.getRoleId().getId());
					else 
						stlrMap.put(uwsam.getUserId().getId(), 0L);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stlrMap;
	}

	@Override
	public String assingStudy(List<Long> userIds, Map<Long, Long> roleIds, Map<Long, String> comments, Long studyId,
			Long userId) {

			LoginUsers loginUser = userDao.findById(userId);
			StudyMaster study = studyDao.getStudyMasterRecord(studyId);
			List<UserWiseStudiesAsignMaster> userUpdate = new ArrayList<>();
			List<UserWiseStudiesAsignMaster> activeUsers = userWiseStudiesAsignDao.getStudyLevleRolesList(studyId);
			for (UserWiseStudiesAsignMaster user : activeUsers) {
				System.out.println(user.getUserId().getId());
			}
			for (UserWiseStudiesAsignMaster user : activeUsers) {
				if(user.getRoleId() == null) {
					user.setRoleId(user.getUserId().getRole());
				}
				System.out.println(user.getUserId().getId());
				if(user.getRoleId().getId() != 1) {					
					if (userIds.contains(user.getUserId().getId())) {
						boolean flag = false;
						if(user.getStatus() != 'T') {
							user.setStatus('T');
							flag = true;
						}
						Long roleId = roleIds.get(user.getUserId().getId());
						System.out.println(roleId +"  "+user.getRoleId().getId());
						if(roleId != null && roleId != 0 && user.getRoleId().getId() != roleId) {
							RoleMaster role = studyDao.getRoleMasterRecord(roleId);
							user.setRoleId(role);
							flag = true;
						}
						
						if(flag) {
							user.setUpdatedBy(loginUser.getUsername());
							user.setUpdatedOn(new Date());
							user.setUpdateReason(comments.get(userId));					
							userUpdate.add(user);
						}
						
					} else {
						if(user.getStatus() != 'F') {
							userIds.remove(user.getUserId().getId());
							user.setStatus('F');
							user.setUpdatedBy(loginUser.getUsername());
							user.setUpdatedOn(new Date());
							user.setUpdateReason(comments.get(userId));
							userUpdate.add(user);
						}
					}
				}else {
					if(user.getStatus() != 'T') {
						user.setStatus('T');
						userUpdate.add(user);
					}
				}
				userIds.remove(user.getUserId().getId());
			}
			
			List<UserWiseStudiesAsignMaster> usersave = new ArrayList<>();
			for (Long l : userIds) {
				UserWiseStudiesAsignMaster uwsam = new UserWiseStudiesAsignMaster();
				uwsam.setStudyMaster(study);
				uwsam.setUserId(userDao.findById(l));
				uwsam.setCreatedBy(loginUser.getUsername());
				uwsam.setCreatedOn(new Date());
				uwsam.setUpdateReason(comments.get(userId));
				uwsam.setRoleId(studyDao.getRoleMasterRecord(roleIds.get(l)));
				usersave.add(uwsam);
				System.out.println(uwsam.getRoleId().getId()+"\t"+uwsam.getUserId().getId()+"\t"+uwsam.getStudyMaster().getId());
			}

			userWiseStudiesAsignDao.saveStudyAsignStudyCreationTime(usersave);
			
			
			userWiseStudiesAsignDao.updateUserWiseStudiesAsignMaster(userUpdate);
			return "Study Assign Done Successfully";
		
		
		
	}

	@Override
	public UserWiseStudiesAsignMaster userWiseStudiesAsignMaster(Long studyId, Long userId) {
		// TODO Auto-generated method stub
		return userWiseStudiesAsignDao.userWiseStudiesAsignMaster(studyId, userId);
	}
	
	
}
