package com.springmvc.dao;

import java.util.List;

import com.covide.crf.dto.CRFGroupItemStd;
import com.covide.crf.dto.CRFItemValuesStd;
import com.covide.crf.dto.CRFSectionsStd;
import com.covide.crf.dto.CrfItemsStd;
import com.covide.crf.dto.CrfMetaData;
import com.covide.crf.dto.CrfMetaDataStd;
import com.covide.crf.dto.DTNAME;
import com.springmvc.model.EmployeeMaster;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.LoginUsersLog;
import com.springmvc.model.ReviewLevel;
import com.springmvc.model.StudyMaster;

public interface EmployeeDao {

	EmployeeMaster getEmployeeByEmpId(String empId);

	boolean saveEmpDetails(EmployeeMaster employeeMaster, Long roleId);

	void saveCrf(CrfMetaData crf, DTNAME dt);

	public List<CrfMetaData> findAllCrfs();
	public List<CrfMetaData> findAllCrfsAll();

	List<CrfMetaDataStd> findAllStdCrfs(StudyMaster sm);

	public void savestdCrf1(CrfMetaDataStd std);
	public void savestdCrfSections1(CRFSectionsStd std);
	public void savestdCrfSections(List<CRFSectionsStd> crf);
	public void savestdCrfGroups1(CRFGroupItemStd crf);
	public void savestdCrfGroups(List<CRFGroupItemStd> crf);
	public void savestdCrfEle1(CrfItemsStd crf);
	public void savestdCrfEle(List<CrfItemsStd> crf);
	public void savestdCrfEleVal(List<CRFItemValuesStd> crf);
	public void savestdCrf(List<CrfMetaDataStd> crf);

	void updatestdCrf(List<CrfMetaDataStd> stdcrfsUpdate);

	void updatestdCrf(CrfMetaDataStd scrf);

	boolean changeStatus(LoginUsers lu);

	void saveLoginUsersLog(LoginUsersLog lulog);

	boolean updateEmpDetails(EmployeeMaster employeeMaster);

	ReviewLevel reviewLevel();

	boolean saveReviewLevel(int observationApprovelLevel, String userName);
}
