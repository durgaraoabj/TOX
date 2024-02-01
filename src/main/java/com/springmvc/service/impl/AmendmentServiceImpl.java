package com.springmvc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springmvc.dao.AmendmentDao;
import com.springmvc.model.AmendmentDetails;
import com.springmvc.model.AmendmentDetailsLog;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.StudyMaster;
import com.springmvc.service.AmendmentService;

@Service("amendmentService")
public class AmendmentServiceImpl implements AmendmentService {

	@Autowired
	AmendmentDao amendmentDao;

	@Override
	public List<AmendmentDetails> getAmendmentDetailsRecrodsList(Long studyNo) {
		return amendmentDao.getAmendmentDetailsRecrodsList(studyNo) ;
	}

	@Override
	public String saveAmandmentDetails(String userName, List<String> amdDetails, Long studyNo) {
		String result = "Failed";
		List<Long> amdIds = new ArrayList<>();
		Map<Long, String> amdMap = new HashMap<>();
		StudyMaster sm = null;
		try {
			sm = amendmentDao.getStudyMasterRecrod(studyNo);
			if(amdDetails.size() > 0) {
				for(String st : amdDetails) {
					String[] temp = st.split("\\##");
					amdIds.add(Long.parseLong(temp[0]));
					amdMap.put(Long.parseLong(temp[0]), temp[1]);
				}
			}
			List<AmendmentDetails> amdList = amendmentDao.getAmendmentRecordsList(amdIds);
			List<AmendmentDetails> updateAmdList = new ArrayList<>();
			List<AmendmentDetailsLog> amdLogList = new ArrayList<>();
			ApplicationAuditDetails aad = null;
			if(amdList != null && amdList.size() > 0) {
				for(AmendmentDetails amd : amdList) {
					
					AmendmentDetailsLog amdlog = new AmendmentDetailsLog();
					BeanUtils.copyProperties(amd, amdlog);
					amdlog.setId(null);
					amdlog.setUpdatedBy(userName);
					amdlog.setUpdatedOn(new Date());
					amdlog.setAmendmentId(amd);
					amdLogList.add(amdlog);
					
					amd.setStatus(amdMap.get(amd.getId()));
					updateAmdList.add(amd);
				}
			    aad = new ApplicationAuditDetails();
				aad.setAction("AmendMent Updated");
				aad.setCreatedBy(userName);
				aad.setCreatedOn(new Date());
				aad.setStudyId(sm);
			}
			boolean flag = amendmentDao.saveAmendmentDetails(updateAmdList, amdLogList, aad);
			if(flag)
				result ="success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<AmendmentDetails> getStudyAmendmentDetailsList(Long studyId) {
		return amendmentDao.getStudyAmendmentDetailsList(studyId);
	}
	
}
