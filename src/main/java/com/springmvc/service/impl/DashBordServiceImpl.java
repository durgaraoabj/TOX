package com.springmvc.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.springmvc.dao.StudyDao;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.StudySite;
import com.springmvc.model.StudySiteSubject;
import com.springmvc.model.SubjectStatus;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrfStatus;
import com.springmvc.reports.StudyProgress;
import com.springmvc.reports.SubjectEnrollmentBySite;
import com.springmvc.reports.SubjectEnrollmentForStudy;
import com.springmvc.service.DashBordService;

@Service("dashBordService")
@PropertySource(value = { "classpath:application.properties" })
public class DashBordServiceImpl implements DashBordService{

	@Autowired
	StudyDao studyDao;
	@Autowired
    private Environment environment;
	@Override
	public List<SubjectEnrollmentBySite> subjectEnrollmentBySiteList(StudyMaster sm) {
		List<SubjectEnrollmentBySite> list = new ArrayList<SubjectEnrollmentBySite>();
		List<StudySite> sites = studyDao.studySite(sm);
		for(StudySite ss : sites) {
			
			int size  = ss.getSubjects();
			SubjectEnrollmentBySite sebs = new SubjectEnrollmentBySite();
			sebs.setSiteName(ss.getSiteName());
			sebs.setTotalSubjects(size);
			List<StudySiteSubject> subjets = studyDao.studySiteSubjects(ss);
			sebs.setEnrolled(subjets.size());
			sebs.setPersentage((100*sebs.getEnrolled())/size);
			
			List<Long> v = new ArrayList<>();
			List<SubjectStatus> sslist = studyDao.subjectStatusList(ss);
			for (SubjectStatus sub : sslist) {
				if(sub.getWithDraw().equals("Yes")) {
					if(!v.contains(sub.getVol().getId()))
						v.add(sub.getVol().getId());
				}
			}
			sebs.setWithDron(v.size());
			list.add(sebs);
		}
		return list;
	}
	@Override
	public List<SubjectEnrollmentForStudy> subjectEnrollmentForStudyList(StudyMaster sm) {
		List<SubjectEnrollmentForStudy> list = new ArrayList<SubjectEnrollmentForStudy>();
		SubjectEnrollmentForStudy sebs = new SubjectEnrollmentForStudy();
		sebs.setStudyNo(sm.getStudyNo());
		int size  = 0;
		int enrolled = 0;
		List<StudySite> sites = studyDao.studySite(sm);
		for(StudySite ss : sites) {
			size  += ss.getSubjects();
			List<StudySiteSubject> subjets = studyDao.studySiteSubjects(ss);
			enrolled += subjets.size();
		}
		sebs.setTotalSubjects(size);
		sebs.setEnrolled(enrolled);
		if(size > 0 ) {
			sebs.setPersentage((100*enrolled)/size);
		}else {
			sebs.setPersentage(100);
		}
		list.add(sebs);
		return list;
	}
	@Override
	public List<StudyProgress> studyProgressList(StudyMaster sm) {
		List<StudyProgress> list = new ArrayList<StudyProgress>();
		List<StudySite> sites = studyDao.studySite(sm);
		int totalSubjects  = 0;
		for(StudySite ss : sites) {
			totalSubjects  += ss.getSubjects();
		}
		int enrolled = 0;
		List<Volunteer> volList = studyDao.studyVolunteerList(sm);
		enrolled = volList.size();
		
		StudyProgress sp = new StudyProgress();
		sp.setTotalSubjects(totalSubjects);
		sp.setName("scheduled");
		sp.setEnrolled(enrolled);
		if(totalSubjects > 0)
			sp.setPersentage((100*enrolled)/totalSubjects);
		list.add(sp);

		
		int dataEntryNotStarted = 0;
		int dataEntryStarted = 0;
		int inReview = 0;
		int completed = 0;
//		NOT STARTED, IN PROGRESS, IN REVIEW, COMPLETED
		List<StudyPeriodMaster> plist = studyDao.allStudyPeriods(sm);
		for(Volunteer vol : volList) {
			int dataEntryNotStarted1 = 0;
			boolean dataEntryStarted1 = false;
			int inReview1 = 0;
			int completed1 = 0;
			List<VolunteerPeriodCrfStatus> vpslist = studyDao.volunteerPeriodCrfStatus(vol);
			for (VolunteerPeriodCrfStatus vps : vpslist) {
				if(vps.getStatus().equals("NOT STARTED"))
					dataEntryNotStarted1 ++;
			}
			for (VolunteerPeriodCrfStatus vps : vpslist) {
				if(vps.getStatus().equals("IN PROGRESS") || vps.getStatus().equals("IN REVIEW")  || vps.getStatus().equals("COMPLETED"))
					dataEntryStarted1 = true;
			}
			for (VolunteerPeriodCrfStatus vps : vpslist) {
				if(vps.getStatus().equals("IN REVIEW"))
					inReview1 ++;
			}
			for (VolunteerPeriodCrfStatus vps : vpslist) {
				if(vps.getStatus().equals("COMPLETED"))
					completed1 ++;
			}
			if(dataEntryNotStarted1 == vpslist.size()) 
				dataEntryNotStarted ++;
//			if(dataEntryStarted1) dataEntryStarted++;
			if(inReview1 == vpslist.size()) 
				inReview ++;
			if(completed1 == vpslist.size()) {
				completed ++;
			}
			if(dataEntryStarted1 || inReview1 == vpslist.size() || completed1 == vpslist.size())
				dataEntryStarted ++;
		}
		

		sp = new StudyProgress();
		sp.setTotalSubjects(totalSubjects);
		sp.setName("data entry not started");
		sp.setEnrolled(dataEntryNotStarted);
		if(totalSubjects > 0)
			sp.setPersentage((100*dataEntryNotStarted)/totalSubjects);
		list.add(sp);
		sp = new StudyProgress();
		sp.setTotalSubjects(totalSubjects);
		sp.setName("data entry started");
		sp.setEnrolled(dataEntryStarted);
		if(totalSubjects > 0)
			sp.setPersentage((100*dataEntryStarted)/totalSubjects);
		list.add(sp);
		sp = new StudyProgress();
		sp.setTotalSubjects(totalSubjects);
		sp.setName("in review");
		sp.setEnrolled(inReview);
		if(totalSubjects > 0)
			sp.setPersentage((100*inReview)/totalSubjects);
		list.add(sp);
		sp = new StudyProgress();
		sp.setTotalSubjects(totalSubjects);
		sp.setName("completed");
		sp.setEnrolled(completed);
		if(totalSubjects > 0)
			sp.setPersentage((100*completed)/totalSubjects);
		list.add(sp);
		return list;
	}
	
	
}
