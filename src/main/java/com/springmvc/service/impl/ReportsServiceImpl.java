package com.springmvc.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springmvc.dao.ReportsDao;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.service.ReportsService;

@Service("reportsService")
public class ReportsServiceImpl implements ReportsService {

	@Autowired
	ReportsDao reportsDao;
	
	@Override
	public List<Volunteer> getVolunteersList(StudyMaster sm) {
		return reportsDao.getVolunteersList(sm);
	}

	@Override
	public List<StudyPeriodMaster> getPeriodsList(StudyMaster sm) {
		return reportsDao.getPeriodsList(sm);
	}

	@Override
	public List<VolunteerPeriodCrf> getVolunteerAllPeriodCrfsList(StudyMaster sm, String volId, String periodId) {
		List<VolunteerPeriodCrf> volcrfsList = null;
		Volunteer volPojo = null;
		StudyPeriodMaster spmPojo = null;
		if(volId.equals("All") && periodId.equals("All")) {
			volcrfsList = reportsDao.getVolunteerAllPeriodCrfsList(sm);
		}else if(volId.equals("All") && !periodId.equals("All")) {
			spmPojo = reportsDao.getPeriodMasterPojo(periodId);
			List<Volunteer> volList = reportsDao.getVolunteersList(sm);
			List<Long> volIds = new ArrayList<>();
			if(volList.size() > 0) {
				for(Volunteer vol : volList) {
					volIds.add(vol.getId());
				}
			}
			if(volIds.size() > 0)
		       volcrfsList = reportsDao.getVolunteerCrfsBasedOnVolId(sm, volIds, spmPojo);
		}else if(!volId.equals("All") && periodId.equals("All")) {
			volPojo = reportsDao.getVolunteerMasterPojo(volId);
			List<StudyPeriodMaster> periodList = reportsDao.getPeriodsList(sm);
			List<Long> periodIds = new ArrayList<>();
			if(periodList.size() > 0) {
				for(StudyPeriodMaster spm : periodList) {
					periodIds.add(spm.getId());
				}
			}
			if(periodIds.size() > 0)
			  volcrfsList = reportsDao.getVolunteerCustomCrfsList(sm, periodIds, volPojo);
        }else if(!volId.equals("All") && !periodId.equals("All")){
			volPojo = reportsDao.getVolunteerMasterPojo(volId);
			spmPojo = reportsDao.getPeriodMasterPojo(periodId);
			volcrfsList = reportsDao.getVolunteerCrfsBasedOnVolidAndPeriodId(sm, volPojo, spmPojo);
		}
		return volcrfsList;
	}

	@Override
	public VolunteerPeriodCrf getVolunteerPeriodCrfPojo(String[] temp2) {
		long studyId = Long.parseLong(temp2[0]);
		long volId =  Long.parseLong(temp2[1]);
		long periodId = Long.parseLong(temp2[2]);
		long crfId = Long.parseLong(temp2[3]);
		
		return reportsDao.getVolunteerPeriodCrfPojo(studyId, volId, periodId, crfId);
	}

}
