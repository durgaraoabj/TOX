package com.springmvc.dao;

import java.util.List;

import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;

public interface ReportsDao {

	List<Volunteer> getVolunteersList(StudyMaster sm);

	List<StudyPeriodMaster> getPeriodsList(StudyMaster sm);

	List<VolunteerPeriodCrf> getVolunteerAllPeriodCrfsList(StudyMaster sm);

	Volunteer getVolunteerMasterPojo(String volId);

	StudyPeriodMaster getPeriodMasterPojo(String periodId);

	List<VolunteerPeriodCrf> getVolunteerCustomCrfsList(StudyMaster sm, List<Long> periodIds, Volunteer volPojo);

	
	List<VolunteerPeriodCrf> getVolunteerCrfsBasedOnVolidAndPeriodId(StudyMaster sm, Volunteer volPojo,
			StudyPeriodMaster spmPojo);

	List<VolunteerPeriodCrf> getVolunteerCrfsBasedOnVolId(StudyMaster sm, List<Long> volIds, StudyPeriodMaster spmPojo);

	VolunteerPeriodCrf getVolunteerPeriodCrfPojo(long studyId, long volId, long periodId, long crfId);

}
