package com.springmvc.service;

import java.util.List;

import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;

public interface ReportsService {

	List<Volunteer> getVolunteersList(StudyMaster sm);

	List<StudyPeriodMaster> getPeriodsList(StudyMaster sm);

	List<VolunteerPeriodCrf> getVolunteerAllPeriodCrfsList(StudyMaster sm, String volId, String periodId);

	VolunteerPeriodCrf getVolunteerPeriodCrfPojo(String[] temp2);

}
