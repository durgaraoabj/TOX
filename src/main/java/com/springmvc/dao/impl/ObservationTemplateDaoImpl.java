package com.springmvc.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.covide.template.dto.TemplateAuditTrailDto;
import com.covide.template.dto.TemplateCommentsDto;
import com.covide.template.dto.TemplateFilesDto;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.ObservationTemplateDao;
import com.springmvc.model.ObserVationTemplates;
import com.springmvc.model.StudyLevelObservationTemplateData;
import com.springmvc.model.StudyLevelObservationTemplateDataLog;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.TemplateFileAuditTrail;
import com.springmvc.model.TemplateFileAuditTrailLog;

@Repository("observationTemplateDao")
public class ObservationTemplateDaoImpl extends AbstractDao<Long, ObserVationTemplates> implements ObservationTemplateDao {

	
	@Override
	public TemplateFilesDto getTemplateFilesRecords(Long studyId, Long groupId, Long subGroupId, Long subgroupAnimalId,
			Long templateId) {
		TemplateFilesDto tfdto = null;
		ObserVationTemplates obvt = null;
		StudyLevelObservationTemplateData slotdata = null;
		try {
			slotdata = (StudyLevelObservationTemplateData) getSession().createCriteria(StudyLevelObservationTemplateData.class)
					.add(Restrictions.eq("studyId", studyId))
					.add(Restrictions.eq("groupId", groupId))
					.add(Restrictions.eq("subGroupId", subGroupId))
					.add(Restrictions.eq("animalId", subgroupAnimalId))
					.add(Restrictions.eq("obstData.id", templateId)).uniqueResult();
			if(slotdata == null) {
				obvt = (ObserVationTemplates) getSession().createCriteria(ObserVationTemplates.class)
						.add(Restrictions.eq("obstd.id", templateId)).uniqueResult();
			}
			if(slotdata != null || obvt != null)
				tfdto = new TemplateFilesDto();
				tfdto.setObvt(obvt);
				tfdto.setSlotdata(slotdata);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tfdto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TemplateFileAuditTrail> getDraftTemplateAuditRecord(List<String> positions,
			StudyLevelObservationTemplateData slotdata) {
		return getSession().createCriteria(TemplateFileAuditTrail.class)
				.add(Restrictions.eq("slotd", slotdata))
				.add(Restrictions.in("position", positions))
				.list();
	}

	@Override
	public boolean saveTemplateAuditTrailLogRecords(List<TemplateFileAuditTrailLog> tfatLogList) {
		boolean flag = false;
		long logNo = 0;
		try {
			for(TemplateFileAuditTrailLog tafatLog : tfatLogList) {
				logNo = (long) getSession().save(tafatLog);
				if(logNo > 0)
					flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}

	@Override
	public boolean updateTemplateAuditTrailRecords(List<TemplateFileAuditTrail> updatList) {
		boolean flag = false;
		try {
			for(TemplateFileAuditTrail tfat : updatList) {
				getSession().update(tfat);
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}

	@Override
	public int saveTemplateAuditTrailRecordList(List<TemplateFileAuditTrail> datList) {
		int no = 0;
		long tfatNo = 0;
		try {
			for(TemplateFileAuditTrail tfaut : datList) {
				tfatNo = (Long) getSession().save(tfaut);
				if(tfatNo > 0)
					no = Integer.parseInt(tfatNo+"");
				else no = Integer.parseInt(tfatNo+"");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return no;
		}
		return no;
	}

	@Override
	public long saveTemplateLogRecord(StudyLevelObservationTemplateDataLog slotdLog) {
		return (long) getSession().save(slotdLog);
	}

	@Override
	public boolean updateTemplateRecord(StudyLevelObservationTemplateData slotdata) {
		boolean flag = false;
		try {
			getSession().update(slotdata);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}

	@Override
	public long saveStudyLevelObservationTemplateDataRecord(StudyLevelObservationTemplateData slotdata) {
		return (long) getSession().save(slotdata);
	}

	@Override
	public TemplateFilesDto getTemplateFileDetails(Long studyId, Long groupId, Long subGroupId, Long subGroupanimalId,
			Long templateId) {
		TemplateFilesDto tfdto = null;
		ObserVationTemplates obvt = null;
		StudyLevelObservationTemplateData slotdata = null;
		SubGroupAnimalsInfoAll sgaia = null;
		String fileStatus = "";
		try {
			slotdata = (StudyLevelObservationTemplateData) getSession().createCriteria(StudyLevelObservationTemplateData.class)
					.add(Restrictions.eq("studyId", studyId))
					.add(Restrictions.eq("groupId", groupId))
					.add(Restrictions.eq("subGroupId", subGroupId))
					.add(Restrictions.eq("animalId", subGroupanimalId))
					.add(Restrictions.eq("obstData.id", templateId)).uniqueResult();
			if(slotdata == null) {
				obvt = (ObserVationTemplates) getSession().createCriteria(ObserVationTemplates.class)
						.add(Restrictions.eq("obstd.id", templateId)).uniqueResult();
				if(obvt != null)
					fileStatus = "assigned";
			}else fileStatus = slotdata.getStatus();
			sgaia = (SubGroupAnimalsInfoAll) getSession().createCriteria(SubGroupAnimalsInfoAll.class)
			.add(Restrictions.eq("id", subGroupanimalId)).uniqueResult();
			if(sgaia != null) {
				Hibernate.initialize(sgaia.getSubGroup().getStudy());
				Hibernate.initialize(sgaia.getSubGroup().getGroup());
			}
			if(slotdata != null || obvt != null)
				tfdto = new TemplateFilesDto();
				tfdto.setObvt(obvt);
				tfdto.setSlotdata(slotdata);
				tfdto.setSgaia(sgaia);
				tfdto.setFileStatus(fileStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tfdto;
	}

	@Override
	public boolean completeReviewForTemplate(Long templateId, String statusStr, String comments, String userName) {
		boolean flag = false;
		StudyLevelObservationTemplateData slotdata = null;
		try {
			slotdata = (StudyLevelObservationTemplateData) getSession().createCriteria(StudyLevelObservationTemplateData.class)
					.add(Restrictions.eq("id", templateId)).uniqueResult();
			if(slotdata != null) {
				slotdata.setStatus(statusStr);
				if(statusStr.equals("reviewed"))
					slotdata.setReviewComments(comments);
				else slotdata.setReAssignComments(comments);
				slotdata.setUpdatedBy(userName);
				slotdata.setUpdatedOn(new Date());
				getSession().update(slotdata);
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TemplateAuditTrailDto getTemplateAuditTrailData(Long templateId) {
		TemplateAuditTrailDto tempAuditDto = null;
		List<TemplateFileAuditTrail> tempAuditList = null;
		StudyLevelObservationTemplateData fileDetails = null;
		List<TemplateFileAuditTrailLog> tempAuditListLog = null;
		try {
			fileDetails = (StudyLevelObservationTemplateData) getSession().createCriteria(StudyLevelObservationTemplateData.class)
					.add(Restrictions.eq("id", templateId)).uniqueResult();
			if(fileDetails != null) {
				tempAuditList = getSession().createCriteria(TemplateFileAuditTrail.class)
						.add(Restrictions.eq("slotd", fileDetails)).list();
				
				tempAuditListLog = getSession().createCriteria(TemplateFileAuditTrailLog.class)
						.add(Restrictions.eq("slotd", fileDetails)).list();
				
				tempAuditDto = new TemplateAuditTrailDto();
				tempAuditDto.setFileDetails(fileDetails);
				tempAuditDto.setTempAuditList(tempAuditList);
				tempAuditDto.setTempAuditListLog(tempAuditListLog);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempAuditDto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TemplateCommentsDto getTemplateCommentsData(Long templateId) {
		TemplateCommentsDto tcdto = null;
		StudyLevelObservationTemplateData slotdata = null;
		List<StudyLevelObservationTemplateDataLog> slotLogList = null;
		try {
			slotdata = (StudyLevelObservationTemplateData) getSession().createCriteria(StudyLevelObservationTemplateData.class)
					.add(Restrictions.eq("id", templateId)).uniqueResult();
			slotLogList = getSession().createCriteria(StudyLevelObservationTemplateDataLog.class)
					.add(Restrictions.eq("slotd.id", templateId)).list();
			if((slotdata != null) || (slotLogList != null && slotLogList.size() > 0)){
				tcdto = new TemplateCommentsDto();
				tcdto.setSlotdata(slotdata);
				tcdto.setSlotLogList(slotLogList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return tcdto;
		}
		return tcdto;
	}

}
