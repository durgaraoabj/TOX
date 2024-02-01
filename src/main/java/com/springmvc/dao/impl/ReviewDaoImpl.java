package com.springmvc.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import com.covide.crf.dto.AccessionAnimalDataviewDto;
import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfDescrpency;
import com.covide.crf.dto.CrfSection;
import com.covide.crf.dto.CrfSectionElement;
import com.covide.crf.dto.CrfSectionElementData;
import com.covide.crf.dto.CrfSectionElementDataAudit;
import com.covide.crf.dto.ObservationAnimalDataviewDto;
import com.covide.dto.RadomizationDto;
import com.covide.dto.SatusAndWorkFlowDetailsDto;
import com.covide.dto.StudyDesignReviewDto;
import com.covide.enums.StatusMasterCodes;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.AccessionDao;
import com.springmvc.dao.ReviewDao;
import com.springmvc.dao.StatusDao;
import com.springmvc.dao.StudyDao;
import com.springmvc.dao.UserDao;
import com.springmvc.model.AccessionAnimalDataDto;
import com.springmvc.model.AccessionAnimalsDataEntryDetails;
import com.springmvc.model.AnimalCage;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.DepartmentMaster;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.ObservationAnimalDataDto;
import com.springmvc.model.RadamizationAllDataReview;
import com.springmvc.model.RadomizationReviewDto;
import com.springmvc.model.RandamizationDto;
import com.springmvc.model.RandamizationGroupAnimalDto;
import com.springmvc.model.RandamizationGroupDto;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAccessionCrfDescrpency;
import com.springmvc.model.StudyAccessionCrfSectionElementData;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAcclamatizationDates;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyCageAnimal;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTreatmentDataDates;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.SubjectDataEntryDetails;
import com.springmvc.model.WorkFlow;
import com.springmvc.model.WorkFlowReviewAudit;
import com.springmvc.model.WorkFlowReviewStages;
import com.springmvc.model.WorkFlowStatusDetails;
import com.springmvc.model.dummy.RadamizationAllData;
import com.springmvc.service.AnimalRandomizationService;

@Repository("reviewDao")
@SuppressWarnings("unchecked")
@PropertySource(value = { "classpath:application.properties" })
public class ReviewDaoImpl extends AbstractDao<Long, StudyMaster> implements ReviewDao {
	@Autowired
	private Environment environment;
	@Autowired
	StatusDao statusDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	AnimalRandomizationService animalRadService;
	@Autowired
	AccessionDao accessionDao;
	@Autowired
	private StudyDao studyDao;

	@Override
	public StudyDesignReviewDto getStudyDesignReviewDto(Long studyId) {
		StudyDesignReviewDto sdrd = null;
		StudyMaster sm = null;
		List<GroupInfo> giList = null;
		StudyDesignStatus sds = null;
		try {
			sds = (StudyDesignStatus) getSession().createCriteria(StudyDesignStatus.class)
					.add(Restrictions.eq("studyId", studyId)).uniqueResult();
			sm = (StudyMaster) getSession().createCriteria(StudyMaster.class).add(Restrictions.eq("id", studyId))
					.uniqueResult();
			if (sds != null) {
				if (sds.getCount() == 2 && sds.getStatus().getStatusCode().equals(StatusMasterCodes.DINR.toString())) {
					giList = getSession().createCriteria(GroupInfo.class).add(Restrictions.eq("study", sm)).list();
					if (giList != null && giList.size() > 0) {
						for (GroupInfo gi : giList) {
							List<SubGroupInfo> sgifList = studySubGroupInfo(gi);
							for (SubGroupInfo sgi : sgifList) {
								List<StdSubGroupObservationCrfs> ssgocList = getSession()
										.createCriteria(StdSubGroupObservationCrfs.class)
										.add(Restrictions.eq("subGroupInfo", sgi)).list();
								sgi.setSsgocList(ssgocList);
								List<SubGroupAnimalsInfo> animalInfo = subGroupAnimalsInfo(sgi);
								sgi.setAnimalInfo(animalInfo);
							}
							gi.setSubGroupInfo(sgifList);
						}
					}
				}
			}
			sdrd = new StudyDesignReviewDto();
			sdrd.setGi(giList);
			sdrd.setSm(sm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdrd;
	}

	public List<SubGroupInfo> studySubGroupInfo(GroupInfo gi) {
		return getSession().createCriteria(SubGroupInfo.class).add(Restrictions.eq("group", gi)).list();
	}

	public List<SubGroupAnimalsInfo> subGroupAnimalsInfo(SubGroupInfo sgi) {
		return getSession().createCriteria(SubGroupAnimalsInfo.class).add(Restrictions.eq("subGroup", sgi)).list();
	}

	@Override
	public SatusAndWorkFlowDetailsDto getSatusAndWorkFlowDetailsDtoDetails(Long studyId, String fromStatus,
			String toStatus) {
		SatusAndWorkFlowDetailsDto sawfdd = null;
		StudyMaster sm = null;
		StatusMaster fStatus = null;
		StatusMaster tStatus = null;
		WorkFlowStatusDetails wfsd = null;
		StudyDesignStatus sds = null;
		try {
			sm = (StudyMaster) getSession().createCriteria(StudyMaster.class).add(Restrictions.eq("id", studyId))
					.uniqueResult();

			fStatus = (StatusMaster) getSession().createCriteria(StatusMaster.class)
					.add(Restrictions.eq("statusCode", fromStatus)).uniqueResult();

			tStatus = (StatusMaster) getSession().createCriteria(StatusMaster.class)
					.add(Restrictions.eq("statusCode", toStatus)).uniqueResult();

			wfsd = (WorkFlowStatusDetails) getSession().createCriteria(WorkFlowStatusDetails.class)
					.add(Restrictions.eq("fromStatus", fStatus)).add(Restrictions.eq("toStatus", tStatus))
					.uniqueResult();

			sds = (StudyDesignStatus) getSession().createCriteria(StudyDesignStatus.class)
					.add(Restrictions.eq("studyId", studyId)).uniqueResult();

			sawfdd = new SatusAndWorkFlowDetailsDto();
			sawfdd.setSm(tStatus);
			sawfdd.setStudy(sm);
			sawfdd.setWfsd(wfsd);
			sawfdd.setSds(sds);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sawfdd;
	}

	@Override
	public boolean updateStudyDesignStatusDetails(StudyDesignStatus sds, ApplicationAuditDetails aad, StudyMaster sm) {
		boolean flag = false;
		long no = 0;
		try {
			getSession().update(sds);
			no = (long) getSession().save(aad);
			if (no > 0) {
				if (sm != null) {
					getSession().update(sm);
					flag = true;
				} else
					flag = true;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}

	@Override
	public StatusMaster getStatusMasterRecord(String statusCode) {
		return (StatusMaster) getSession().createCriteria(StatusMaster.class)
				.add(Restrictions.eq("statusCode", statusCode)).uniqueResult();
	}

	@Override
	public AccessionAnimalDataviewDto getAccessionAnimalDataDtoDetails(Long studyId, Long crfId, Long userId,
			String dateString, String gender, Long studyAcclamatizationDatesId) {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		LoginUsers user = userDao.findById(userId);
		AccessionAnimalDataviewDto aadDto = null;
		StudyMaster sm = null;
		List<StudyAccessionCrfSectionElementData> secDataList = null;
		List<AccessionAnimalsDataEntryDetails> aadedList = null;
		List<AccessionAnimalDataDto> animalData = new ArrayList<>();
		Crf crf = null;
		int totoal = 0;
		try {
			sm = (StudyMaster) getSession().createCriteria(StudyMaster.class).add(Restrictions.eq("id", studyId))
					.uniqueResult();
			StudyAcclamatizationDates studyAcclamatizationDates = null;
			if (studyAcclamatizationDatesId == null) {
				StudyAcclamatizationData studyAcclamatizationData = accessionDao.studyAcclamatizationData(crfId,
						studyId);
				Date accDate = null;
				if (dateString != null) {
					accDate = sdf.parse(dateString);
				} else {
					accDate = sdf.parse(sdf.format(new Date()));
				}
				studyAcclamatizationDates = accessionDao.studyAcclamatizationDatesOfCurrentDate(sm,
						studyAcclamatizationData.getId(), accDate, gender);
				aadedList = getSession().createCriteria(AccessionAnimalsDataEntryDetails.class)
						.add(Restrictions.eq("studyAcclamatizationDates.id", studyAcclamatizationDates.getId())).list();
			} else {
//				studyAcclamatizationDates = accessionDao.studyAcclamatizationDates(studyAcclamatizationDatesId);
				aadedList = getSession().createCriteria(AccessionAnimalsDataEntryDetails.class)
						.add(Restrictions.eq("gender", gender))
						.add(Restrictions.eq("studyAcclamatizationDates.id", studyAcclamatizationDatesId)).list();
			}

			if (true) {
				Date fromDate = sdf.parse(dateString);
				Calendar c = Calendar.getInstance();
				c.setTime(fromDate);
				c.add(Calendar.DATE, 1);
				Date toDate = sdf.parse(sdf.format(c.getTime()));
				aadedList.addAll(getSession().createCriteria(AccessionAnimalsDataEntryDetails.class)
						.add(Restrictions.eq("entryType", "unscheduled")).add(Restrictions.eq("gender", gender))
						.add(Restrictions.between("createdOn", fromDate, toDate))
						.add(Restrictions.isNull("studyAcclamatizationDates")).list());
			}
			crf = (Crf) getSession().createCriteria(Crf.class).add(Restrictions.eq("id", crfId)).uniqueResult();
			Hibernate.initialize(crf.getSections());
			if (aadedList != null && aadedList.size() > 0) {
				for (AccessionAnimalsDataEntryDetails aaded : aadedList) {
					secDataList = getSession().createCriteria(StudyAccessionCrfSectionElementData.class)
							.add(Restrictions.eq("studyAccAnimal", aaded)).list();

					Map<Long, StudyAccessionCrfSectionElementData> elementData = new HashMap<>();
					int opend = 0;
					int closed = 0;
					if (secDataList != null && secDataList.size() > 0) {
						for (StudyAccessionCrfSectionElementData scsed : secDataList) {
							if (!scsed.getElement().getType().equals("non")) {
								// for the view of all audits in review page
								scsed = accessionCrfSectionElementDataAudit(scsed);
								elementData.put(scsed.getElement().getId(), scsed);
								Long close = 0l, open = 0l;
								try {
									close = (Long) getSession().createCriteria(StudyAccessionCrfDescrpency.class)
											.add(Restrictions.eq("stydyAccCrfSecEleData", scsed))
											.add(Restrictions.eq("status", "closed"))
											.setProjection(Projections.rowCount()).uniqueResult();
								} catch (Exception e) {
									// TODO: handle exceptione
									e.printStackTrace();
									close = 0l;
								}
								try {
									open = (Long) getSession().createCriteria(StudyAccessionCrfDescrpency.class)
											.add(Restrictions.eq("stydyAccCrfSecEleData", scsed))
											.add(Restrictions.ne("status", "closed"))
											.setProjection(Projections.rowCount()).uniqueResult();
								} catch (Exception e) {
									// TODO: handle exceptione
									e.printStackTrace();
									open = 0l;
								}
								scsed.setDiscripency(open.intValue());
								scsed.setDiscripencyClose(close.intValue());
								opend += open.intValue();
								closed += close.intValue();
							}
						}
					}
					AccessionAnimalDataDto data = new AccessionAnimalDataDto();
					data.setId(aaded.getId());
					data.setDeviationMessage(aaded.getDeviation());
					data.setUserName(aaded.getCreatedBy());
					data.setDate(aaded.getCreatedOn());
					data.setEntryType(aaded.getEntryType());
					data.setAnimalId(aaded.getAnimalTempId() + "");
					data.setGender(aaded.getAnimal().getGender());
					data.setGenderCode(aaded.getAnimal().getGenderCode());
					data.setStatus(aaded.getStatus().getStatusDesc());
					data.setStatusCode(aaded.getStatus().getStatusCode());
					if (aaded.getStatus() != null && !aaded.getStatus().equals("")) {
						System.out.println(aaded.getStatus().getStatusCode());
						if (aaded.getStatus().getStatusCode().equals(StatusMasterCodes.APPROVED.toString())
								|| aaded.getStatus().getStatusCode().equals(StatusMasterCodes.REJECTED.toString()))
							data.setReviewed(true);
						if (!data.isReviewed()) {
							if (aaded.getCurrentReviewRole() != null
									&& aaded.getCurrentReviewRole().getId() == user.getRole().getId()) {
								data.setAllowToReview(true);
							}
						}
					}

					data.setDiscrepencyOpend(opend);
					data.setDiscrepencyClosed(closed);
					data.setElementData(elementData);
					animalData.add(data);
					totoal += opend;
				}
			}

			Map<Long, CrfSectionElement> elements = new HashMap<>();
			for (CrfSection sec : crf.getSections()) {
				for (CrfSectionElement ele : sec.getElement()) {
					if (!ele.getType().equals("non")) {
						elements.put(ele.getId(), ele);
					}
				}
			}
			aadDto = new AccessionAnimalDataviewDto();
			aadDto.setElements(elements);
			aadDto.setCrf(crf);
			aadDto.setAnimalData(animalData);
			aadDto.setTotoalDescNotDone(totoal);
			aadDto.setStudy(sm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aadDto;
	}

	private StudyAccessionCrfSectionElementData accessionCrfSectionElementDataAudit(
			StudyAccessionCrfSectionElementData scsed) {
//		if (scsed.isDataUpdate() && !scsed.isSelectedDataUpdate()) {
		if (scsed.isDataUpdate()) {
			List<CrfSectionElementDataAudit> audits = getSession().createCriteria(CrfSectionElementDataAudit.class)
					.add(Restrictions.eq("data.id", scsed.getId())).addOrder(Order.asc("createdOn")).list();
			if (audits.size() > 1) {
				audits.remove(0);
				scsed.setAudit(audits);
			}
		}
		return scsed;
	}

	@Override
	public ObservationAnimalDataviewDto observatoinAnimalDataDtoDetails(Long studyId, Long studyTreatmentDataDatesId,
			Long stdSubGroupObservationCrfsId, Long userId, boolean export, String gender, String schduleType) {
		StatusMaster approveStatus = statusDao.statusMaster(StatusMasterCodes.APPROVED.toString());
		LoginUsers user = userDao.findById(userId);
		ObservationAnimalDataviewDto aadDto = null;
		StudyMaster sm = null;
		List<CrfSectionElementData> secDataList = null;
		List<SubjectDataEntryDetails> aadedList = null;
		List<ObservationAnimalDataDto> animalData = new ArrayList<>();

		Crf crf = null;
		int totoal = 0;
		try {
			sm = (StudyMaster) getSession().createCriteria(StudyMaster.class).add(Restrictions.eq("id", studyId))
					.uniqueResult();
			if (studyTreatmentDataDatesId != null) {
				StudyTreatmentDataDates date = (StudyTreatmentDataDates) getSession().get(StudyTreatmentDataDates.class,
						studyTreatmentDataDatesId);
				crf = date.getStdSubGroupObservationCrfs().getCrf();
				Criteria cri = getSession().createCriteria(SubjectDataEntryDetails.class)
						.add(Restrictions.eq("studyTreatmentDataDates.id", studyTreatmentDataDatesId))
						.add(Restrictions.eq("study.id", studyId));
				if (gender != null && !gender.equals(""))
					cri.add(Restrictions.eq("gender", gender));
				if (export) {
					cri.add(Restrictions.eq("status", approveStatus));
				}
				aadedList = cri.list();
			} else {
				StdSubGroupObservationCrfs stdSubGroupObservationCrfs = (StdSubGroupObservationCrfs) getSession()
						.get(StdSubGroupObservationCrfs.class, stdSubGroupObservationCrfsId);
				crf = stdSubGroupObservationCrfs.getCrf();
				Criteria cri = getSession().createCriteria(SubjectDataEntryDetails.class)
						.add(Restrictions.eq("observationCrf.id", stdSubGroupObservationCrfsId))
						.add(Restrictions.eq("entryType", schduleType))
						.add(Restrictions.eq("study.id", studyId));
				if (gender != null && !gender.equals(""))
					cri.add(Restrictions.eq("gender", gender));
				if (export) {
					cri.add(Restrictions.eq("status", approveStatus));
				}
				aadedList = cri.list();
			}

			if (aadedList != null && aadedList.size() > 0) {
				for (SubjectDataEntryDetails aaded : aadedList) {
					Hibernate.initialize(aaded.getGroup());
					Hibernate.initialize(aaded.getSubGroupAnaimal());
					Hibernate.initialize(aaded.getObservationCrf());
					Hibernate.initialize(aaded.getEntredby());
					Hibernate.initialize(aaded.getObservationCrf());
					Hibernate.initialize(aaded.getStatus());
					Hibernate.initialize(aaded.getApprovedBy());
					Hibernate.initialize(aaded.getCurrentReviewRole());
					secDataList = getSession().createCriteria(CrfSectionElementData.class)
							.add(Restrictions.eq("subjectDataEntryDetails", aaded)).list();

					Map<Long, CrfSectionElementData> elementData = new HashMap<>();
					int mopend = 0;
					int mclosed = 0;
					if (secDataList != null && secDataList.size() > 0) {
						for (CrfSectionElementData scsed : secDataList) {
							if (!scsed.getElement().getType().equals("non")) {
								elementData.put(scsed.getElement().getId(), scsed);
								Long close = 0l, open = 0l;
								try {
									close = (Long) getSession().createCriteria(CrfDescrpency.class)
											.add(Restrictions.eq("secEleData", scsed))
											.add(Restrictions.eq("status", "closed"))
											.setProjection(Projections.rowCount()).uniqueResult();
								} catch (Exception e) {
									// TODO: handle exceptione
									e.printStackTrace();
									close = 0l;
								}
								try {
									open = (Long) getSession().createCriteria(CrfDescrpency.class)
											.add(Restrictions.eq("secEleData", scsed))
											.add(Restrictions.ne("status", "closed"))
											.setProjection(Projections.rowCount()).uniqueResult();
								} catch (Exception e) {
									// TODO: handle exceptione
									e.printStackTrace();
									open = 0l;
								}
								scsed.setDiscripency(open.intValue());
								scsed.setDiscripencyClose(close.intValue());
								mopend += open.intValue();
								mclosed += close.intValue();
							}
						}
					}
					ObservationAnimalDataDto data = new ObservationAnimalDataDto();
					data.setId(aaded.getId());
					data.setDeviationMessage(aaded.getDeviation());
					data.setUserName(aaded.getCreatedBy());
					data.setDate(aaded.getCreatedOn());
					data.setEntryType(aaded.getEntryType());
					data.setAnimalId(aaded.getAnimal().getPermanentNo() + "");
					data.setGender(aaded.getAnimal().getGender());
					data.setGenderCode(aaded.getAnimal().getGenderCode());
					data.setStatus(aaded.getStatus().getStatusDesc());
					data.setStatusCode(aaded.getStatus().getStatusCode());
					if (aaded.getStatus() != null && !aaded.getStatus().equals("")) {
						System.out.println(aaded.getStatus().getStatusCode());
						if (aaded.getStatus().getStatusCode().equals(StatusMasterCodes.APPROVED.toString())
								|| aaded.getStatus().getStatusCode().equals(StatusMasterCodes.REJECTED.toString()))
							data.setReviewed(true);
						if (!data.isReviewed()) {
							if (aaded.getCurrentReviewRole() != null
									&& aaded.getCurrentReviewRole().getId() == user.getRole().getId()) {
								data.setAllowToReview(true);
							}
						}
					}
					System.out.println(data.getId() + "/t" + data.isAllowToReview());
					data.setDiscrepencyOpend(mopend);
					data.setDiscrepencyClosed(mclosed);
					data.setElementData(elementData);
					animalData.add(data);
					totoal += mopend;
				}
			}

			Hibernate.initialize(crf.getSections());

			Map<Long, CrfSectionElement> elements = new HashMap<>();
			for (CrfSection sec : crf.getSections()) {
				for (CrfSectionElement ele : sec.getElement()) {
					if (!ele.getType().equals("non")) {
						elements.put(ele.getId(), ele);
					}
				}
			}
			aadDto = new ObservationAnimalDataviewDto();
			aadDto.setElements(elements);
			aadDto.setCrf(crf);
			aadDto.setAnimalData(animalData);
			aadDto.setTotoalDescNotDone(totoal);
			aadDto.setStudy(sm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aadDto;
	}

	@Override
	public List<StudyAcclamatizationData> getStudyAcclamatizationDataRecordsList(Long studyId) {
		return getSession().createCriteria(StudyAcclamatizationData.class).add(Restrictions.eq("study.id", studyId))
				.list();
	}

	@Override
	public String reviewSelectedDataEntryFroms(List<Long> checkedIds, Long userId, String reviewType,
			String reviewComment) {
		String result = "Failed";
		LoginUsers user = userDao.findById(userId);
		StatusMaster approveStatus = statusDao.statusMaster(StatusMasterCodes.APPROVED.toString());
		StatusMaster rejectStatus = statusDao.statusMaster("REJECTED");
		StatusMaster activeStatus = statusDao.statusMaster("ACTIVE");
		StatusMaster sendToReview = statusDao.statusMaster("Sent to Review");
		boolean flag = false;
		List<AccessionAnimalsDataEntryDetails> aadedList = null;

		try {
			aadedList = getSession().createCriteria(AccessionAnimalsDataEntryDetails.class)
					.add(Restrictions.in("id", checkedIds)).list();
			Map<Long, List<AccessionAnimalsDataEntryDetails>> animalWise = new HashMap<>();
			if (aadedList != null && aadedList.size() > 0) {

				for (AccessionAnimalsDataEntryDetails aaded : aadedList) {
					WorkFlow accessionWorkFlow = userDao.accessionWorkFlow("ACCESSION", activeStatus);
					WorkFlowReviewStages workFlowReviewStages = userDao.workFlowReviewStages(user, accessionWorkFlow);
					List<WorkFlowReviewAudit> reviewAudit = userDao.workFlowReviewAudit(aaded, user);
					if (reviewType.equals("Approve")) {
						if (workFlowReviewStages != null) {
							if (workFlowReviewStages.getToRole() != null) {
								WorkFlowReviewAudit workFlowReviewAudit = new WorkFlowReviewAudit();
								workFlowReviewAudit.setAccessionAnimalsDataEntryDetailsId(aaded);
								workFlowReviewAudit.setReviewState(workFlowReviewStages);
								workFlowReviewAudit.setRole(workFlowReviewStages.getFromRole());
								workFlowReviewAudit.setUser(user);
								workFlowReviewAudit.setDateOfActivity(new Date());
								getSession().save(workFlowReviewAudit);
								aaded.setCurrentReviewRole(workFlowReviewStages.getToRole());
							} else {
								aaded.setStatus(approveStatus);
								aaded.setApprovedBy(user.getUsername());
								aaded.setApprovedOn(new Date());
//								getSession().update(aaded);
							}
						} else {
							aaded.setStatus(approveStatus);
							aaded.setApprovedBy(user.getUsername());
							aaded.setApprovedOn(new Date());
						}

						if (aaded.getCrf().getName()
								.equals(environment.getRequiredProperty("accessionAnimalWeightFrom"))) {
							List<AccessionAnimalsDataEntryDetails> templist = animalWise.get(aaded.getAnimal().getId());
							if (templist == null)
								templist = new ArrayList<>();
							templist.add(aaded);
							animalWise.put(aaded.getAnimal().getId(), templist);
						}

					} else if (reviewType.equals("Reject")) {
						aaded.setStatus(rejectStatus);
						aaded.setRejectedBy(user.getUsername());
						aaded.setRejectedOn(new Date());
					} else if (reviewType.equals("Send To Review")) {

						if (workFlowReviewStages != null) {
							WorkFlowReviewAudit workFlowReviewAudit = new WorkFlowReviewAudit();
							workFlowReviewAudit.setAccessionAnimalsDataEntryDetailsId(aaded);
							workFlowReviewAudit.setReviewState(workFlowReviewStages);
							workFlowReviewAudit.setRole(workFlowReviewStages.getFromRole());
							workFlowReviewAudit.setUser(user);
							workFlowReviewAudit.setDateOfActivity(new Date());
							getSession().save(workFlowReviewAudit);
							aaded.setCurrentReviewRole(workFlowReviewStages.getToRole());
							aaded.setStatus(sendToReview);
							aaded.setSendBy(user.getUsername());
							aaded.setSentOn(new Date());
						}
					}
					if (!reviewType.equals("Send To Review")) {
						aaded.setReviewComment(reviewComment);
						for (WorkFlowReviewAudit au : reviewAudit)
							au.setInTheFlow(flag);
					}
					flag = true;
				}
			} else
				flag = true;
			if (flag) {
				if (animalWise.size() > 0) {
					for (Map.Entry<Long, List<AccessionAnimalsDataEntryDetails>> entry : animalWise.entrySet()) {
						List<AccessionAnimalsDataEntryDetails> list = entry.getValue();
						Collections.sort(list);

						AccessionAnimalsDataEntryDetails aaded = list.get(list.size() - 1);
						aaded.getAnimal()
								.setActivityData(accessionDao.weightStudyAccessionCrfSectionElementData(aaded,
										environment.getRequiredProperty("accessionAnimalWeightSectionName"),
										environment.getRequiredProperty("accessionAnimalWeightSectionElementName")));
						aaded.getAnimal().setWeight(Double.parseDouble(aaded.getAnimal().getActivityData().getValue()));
						getSession().merge(aaded.getAnimal());
					}
				}
				result = "success";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	@Override
	public String reviewSelectedObservationDataEntryFroms(List<Long> checkedIds, Long userId, String reviewType,
			String reviewComment) {
		String result = "Failed";
		LoginUsers user = userDao.findById(userId);
		StatusMaster approveStatus = statusDao.statusMaster(StatusMasterCodes.APPROVED.toString());
		StatusMaster rejectStatus = statusDao.statusMaster("REJECTED");
		StatusMaster activeStatus = statusDao.statusMaster("ACTIVE");
		StatusMaster sendToReview = statusDao.statusMaster("Sent to Review");
		boolean flag = false;
		List<SubjectDataEntryDetails> aadedList = null;
		try {
			aadedList = getSession().createCriteria(SubjectDataEntryDetails.class)
					.add(Restrictions.in("id", checkedIds)).list();
			if (aadedList != null && aadedList.size() > 0) {
				for (SubjectDataEntryDetails aaded : aadedList) {
					WorkFlow accessionWorkFlow = userDao.accessionWorkFlow("OBSERCVATION", activeStatus);
					WorkFlowReviewStages workFlowReviewStages = userDao.workFlowReviewStages(user, accessionWorkFlow);
					List<WorkFlowReviewAudit> reviewAudit = userDao.observationWorkFlowReviewAudit(aaded.getId(), user);
					if (reviewType.equals("Approve")) {
						if (workFlowReviewStages != null) {
							if (workFlowReviewStages.getToRole() != null) {
								WorkFlowReviewAudit workFlowReviewAudit = new WorkFlowReviewAudit();
								workFlowReviewAudit.setSubjectDataEntryDetails(aaded);
								workFlowReviewAudit.setReviewState(workFlowReviewStages);
								workFlowReviewAudit.setRole(workFlowReviewStages.getFromRole());
								workFlowReviewAudit.setUser(user);
								workFlowReviewAudit.setDateOfActivity(new Date());
								getSession().save(workFlowReviewAudit);
								aaded.setCurrentReviewRole(workFlowReviewStages.getToRole());
							} else {
								aaded.setStatus(approveStatus);
								aaded.setApprovedBy(user);
								aaded.setApprovedOn(new Date());
//								getSession().update(aaded);
							}
						} else {
							aaded.setStatus(approveStatus);
							aaded.setApprovedBy(user);
							aaded.setApprovedOn(new Date());
						}
					} else if (reviewType.equals("Reject")) {
						aaded.setStatus(rejectStatus);
						aaded.setRejectedBy(user.getUsername());
						aaded.setRejectedOn(new Date());
					} else if (reviewType.equals("Send To Review")) {

						if (workFlowReviewStages != null) {
							WorkFlowReviewAudit workFlowReviewAudit = new WorkFlowReviewAudit();
							workFlowReviewAudit.setSubjectDataEntryDetails(aaded);
							workFlowReviewAudit.setReviewState(workFlowReviewStages);
							workFlowReviewAudit.setRole(workFlowReviewStages.getFromRole());
							workFlowReviewAudit.setUser(user);
							workFlowReviewAudit.setDateOfActivity(new Date());
							getSession().save(workFlowReviewAudit);
							aaded.setCurrentReviewRole(workFlowReviewStages.getToRole());
							aaded.setStatus(sendToReview);
							aaded.setSendBy(user.getUsername());
							aaded.setSentOn(new Date());
						}
					}
					if (!reviewType.equals("Send To Review")) {
						aaded.setReviewComment(reviewComment);
						for (WorkFlowReviewAudit au : reviewAudit)
							au.setInTheFlow(flag);
					}
					flag = true;
				}
			} else
				flag = true;
			if (flag)
				result = "success";

		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	@Override
	public String saveRandamizationData(Long redamizationId, Long userId, String reviewType, String reviewComment) {
		String result = "Failed";
		LoginUsers user = userDao.findById(userId);
		StatusMaster approveStatus = statusDao.statusMaster(StatusMasterCodes.APPROVED.toString());
		StatusMaster rejectStatus = statusDao.statusMaster("REJECTED");
		StatusMaster activeStatus = statusDao.statusMaster("ACTIVE");
		boolean flag = false;
		try {
			RadomizationReviewDto randamization = (RadomizationReviewDto) getSession().get(RadomizationReviewDto.class,
					redamizationId);

			WorkFlow accessionWorkFlow = userDao.accessionWorkFlow("RANDAMIZATION", activeStatus);
			WorkFlowReviewStages workFlowReviewStages = userDao.workFlowReviewStages(user, accessionWorkFlow);
			List<WorkFlowReviewAudit> reviewAudit = userDao.readamizationWorkFlowReviewAudit(randamization.getId(),
					user);
			if (reviewType.equals("Approve")) {
				if (workFlowReviewStages != null) {
					if (workFlowReviewStages.getToRole() != null) {
						WorkFlowReviewAudit workFlowReviewAudit = new WorkFlowReviewAudit();
						workFlowReviewAudit.setReamdamizationReivew(randamization);
						workFlowReviewAudit.setReviewState(workFlowReviewStages);
						workFlowReviewAudit.setRole(workFlowReviewStages.getFromRole());
						workFlowReviewAudit.setUser(user);
						workFlowReviewAudit.setDateOfActivity(new Date());
						getSession().save(workFlowReviewAudit);
						randamization.setCurrentReviewRole(workFlowReviewStages.getToRole());
					} else {
						if (randamization.getGender().equals("Both")) {
							randamization.getStudy().setMaleRandamizationReviewStatus(true);
							randamization.getStudy().setFemaleRandamizationReviewStatus(true);
							randamization.getStudy().setRandamizattionStatus(true);
						} else if (randamization.getGender().equals("Male")) {
							randamization.getStudy().setMaleRandamizationReviewStatus(true);
							if (randamization.getStudy().isFemaleRandamizationReviewStatus())
								randamization.getStudy().setRandamizattionStatus(true);
						} else if (randamization.getGender().equals("Female")) {
							randamization.getStudy().setFemaleRandamizationReviewStatus(true);
							if (randamization.getStudy().isMaleRandamizationReviewStatus())
								randamization.getStudy().setRandamizattionStatus(true);
						}
						randamization.getStudy().setRadamizationStatus(approveStatus);
						randamization.setReviewStatus(approveStatus);
						randamization.setApprovedBy(user.getUsername());
						randamization.setApprovedOn(new Date());
						result = saveRandamizationToTables(randamization, user, approveStatus, rejectStatus);
//								getSession().update(aaded);
					}
				} else {
					if (randamization.getGender().equals("Both")) {
						randamization.getStudy().setMaleRandamizationReviewStatus(true);
						randamization.getStudy().setFemaleRandamizationReviewStatus(true);
						randamization.getStudy().setRandamizattionStatus(true);
					} else if (randamization.getGender().equals("Male")) {
						randamization.getStudy().setMaleRandamizationReviewStatus(true);
						if (randamization.getStudy().isFemaleRandamizationReviewStatus())
							randamization.getStudy().setRandamizattionStatus(true);
					} else if (randamization.getGender().equals("Female")) {
						randamization.getStudy().setFemaleRandamizationReviewStatus(true);
						if (randamization.getStudy().isMaleRandamizationReviewStatus())
							randamization.getStudy().setRandamizattionStatus(true);
					}

					randamization.setReviewStatus(approveStatus);
					randamization.setApprovedBy(user.getUsername());
					randamization.setApprovedOn(new Date());
					result = saveRandamizationToTables(randamization, user, approveStatus, rejectStatus);
				}
			} else if (reviewType.equals("Reject")) {
				if (randamization.getGender().equals("Both")) {
					randamization.getStudy().setMaleRandamizationStatus(false);
					randamization.getStudy().setFemaleRandamizationStatus(false);
				} else if (randamization.getGender().equals("Male")) {
					randamization.getStudy().setMaleRandamizationStatus(false);
				} else if (randamization.getGender().equals("Female")) {
					randamization.getStudy().setFemaleRandamizationStatus(false);
				}
				randamization.getStudy().setRadamizationStatus(rejectStatus);
				randamization.setReviewStatus(rejectStatus);
				randamization.setRejectedBy(user.getUsername());
				randamization.setRejectedOn(new Date());
			}
			randamization.setReviewComment(reviewComment);
			for (WorkFlowReviewAudit au : reviewAudit)
				au.setInTheFlow(flag);

			result = "success";

		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	private String saveRandamizationToTables(RadomizationReviewDto randamization, LoginUsers user,
			StatusMaster approveStatus, StatusMaster rejectStatus)
			throws IllegalAccessException, InvocationTargetException {
		// TODO Auto-generated method stub
		RadomizationDto reviewrdmDto = new RadomizationDto();
		if (randamization.getRandamizaitonSheetMale() != null) {
			RadamizationAllData randamizaitonSheetMale = new RadamizationAllData();
			BeanUtils.copyProperties(randamizaitonSheetMale, randamization.getRandamizaitonSheetMale());
			reviewrdmDto.setRandamizaitonSheetMale(randamizaitonSheetMale);
		}
		if (randamization.getRandamizaitonSheetFemale() != null) {
			RadamizationAllData randamizaitonSheetFemale = new RadamizationAllData();
			BeanUtils.copyProperties(randamizaitonSheetFemale, randamization.getRandamizaitonSheetFemale());
			reviewrdmDto.setRandamizaitonSheetFemale(randamizaitonSheetFemale);
		}

		if (randamization.getRandamizaitonSheetMaleAscedning() != null) {
			RadamizationAllData randamizaitonSheetMaleAscedning = new RadamizationAllData();
			BeanUtils.copyProperties(randamizaitonSheetMaleAscedning,
					randamization.getRandamizaitonSheetMaleAscedning());
			reviewrdmDto.setRandamizaitonSheetMaleAscedning(randamizaitonSheetMaleAscedning);
		}
		if (randamization.getRandamizaitonSheetFemaleAscedning() != null) {
			RadamizationAllData randamizaitonSheetFemaleAscedning = new RadamizationAllData();
			BeanUtils.copyProperties(randamizaitonSheetFemaleAscedning,
					randamization.getRandamizaitonSheetFemaleAscedning());
			reviewrdmDto.setRandamizaitonSheetFemaleAscedning(randamizaitonSheetFemaleAscedning);
		}
		if (randamization.getRandamizaitonSheetMaleGruoup() != null
				&& randamization.getRandamizaitonSheetMaleGruoup().size() > 0) {
			List<RadamizationAllData> randamizaitonSheetMaleGruoup = new ArrayList<>();
			List<RadamizationAllDataReview> randamizaitonSheetMaleGruoupold = randamization
					.getRandamizaitonSheetMaleGruoup();
			for (RadamizationAllDataReview rad : randamizaitonSheetMaleGruoupold) {
				RadamizationAllData radw = new RadamizationAllData();
				BeanUtils.copyProperties(radw, rad);
				randamizaitonSheetMaleGruoup.add(radw);
			}
			reviewrdmDto.setRandamizaitonSheetMaleGruoup(randamizaitonSheetMaleGruoup);
		}

		if (randamization.getRandamizaitonSheetFemaleGruoup() != null
				&& randamization.getRandamizaitonSheetFemaleGruoup().size() > 0) {
			List<RadamizationAllData> randamizaitonSheetFemaleGruoup = new ArrayList<>();
			List<RadamizationAllDataReview> randamizaitonSheetFemaleGruoupold = randamization
					.getRandamizaitonSheetFemaleGruoup();
			for (RadamizationAllDataReview rad : randamizaitonSheetFemaleGruoupold) {
				RadamizationAllData radw = new RadamizationAllData();
				BeanUtils.copyProperties(radw, rad);
				randamizaitonSheetFemaleGruoup.add(radw);
			}
			reviewrdmDto.setRandamizaitonSheetFemaleGruoup(randamizaitonSheetFemaleGruoup);
		}
		return animalRadService.saveRandamization(reviewrdmDto, randamization.getStudy().getId(), user.getId(),
				rejectStatus, rejectStatus);
	}

	@Override
	public AccessionAnimalDataviewDto getAccessionAnimalDataDtoDetailsView(Long studyAcclamatizationDataId) {
		AccessionAnimalDataviewDto aadDto = null;
		StudyMaster sm = null;
		List<StudyAccessionCrfSectionElementData> secDataList = null;
		List<AccessionAnimalsDataEntryDetails> aadedList = null;
		List<AccessionAnimalDataDto> animalData = new ArrayList<>();
		Crf crf = null;
		int totoal = 0;
		try {
			StudyAcclamatizationData studyAcclamatizationData = (StudyAcclamatizationData) getSession()
					.get(StudyAcclamatizationData.class, studyAcclamatizationDataId);
			sm = studyAcclamatizationData.getStudy();
			aadedList = getSession().createCriteria(AccessionAnimalsDataEntryDetails.class)
					.add(Restrictions.eq("studyAcclamatizationData.id", studyAcclamatizationDataId)).list();
			crf = studyAcclamatizationData.getCrf();
			Hibernate.initialize(crf.getSections());
			if (aadedList != null && aadedList.size() > 0) {
				for (AccessionAnimalsDataEntryDetails aaded : aadedList) {
					secDataList = getSession().createCriteria(StudyAccessionCrfSectionElementData.class)
							.add(Restrictions.eq("studyAccAnimal", aaded)).list();

					Map<Long, StudyAccessionCrfSectionElementData> elementData = new HashMap<>();
					int opend = 0;
					int closed = 0;
					if (secDataList != null && secDataList.size() > 0) {
						for (StudyAccessionCrfSectionElementData scsed : secDataList) {
							if (!scsed.getElement().getType().equals("non")) {
								// for the view of all audits in review page
								scsed = accessionCrfSectionElementDataAudit(scsed);
								elementData.put(scsed.getElement().getId(), scsed);
								Long close = 0l, open = 0l;
								try {
									close = (Long) getSession().createCriteria(StudyAccessionCrfDescrpency.class)
											.add(Restrictions.eq("stydyAccCrfSecEleData", scsed))
											.add(Restrictions.eq("status", "closed"))
											.setProjection(Projections.rowCount()).uniqueResult();
								} catch (Exception e) {
									// TODO: handle exceptione
									e.printStackTrace();
									close = 0l;
								}
								try {
									open = (Long) getSession().createCriteria(StudyAccessionCrfDescrpency.class)
											.add(Restrictions.eq("stydyAccCrfSecEleData", scsed))
											.add(Restrictions.ne("status", "closed"))
											.setProjection(Projections.rowCount()).uniqueResult();
								} catch (Exception e) {
									// TODO: handle exceptione
									e.printStackTrace();
									open = 0l;
								}
								scsed.setDiscripency(open.intValue());
								scsed.setDiscripencyClose(close.intValue());
								opend += open.intValue();
								closed += close.intValue();
							}
						}
					}
					AccessionAnimalDataDto data = new AccessionAnimalDataDto();
					data.setId(aaded.getId());
					data.setDeviationMessage(aaded.getDeviation());
					data.setUserName(aaded.getCreatedBy());
					data.setDate(aaded.getCreatedOn());
					data.setEntryType(aaded.getEntryType());
					data.setAnimalId(aaded.getAnimalTempId() + "");
					data.setGender(aaded.getAnimal().getGender());
					data.setGenderCode(aaded.getAnimal().getGenderCode());
					data.setStatus(aaded.getStatus().getStatusDesc());
					data.setStatusCode(aaded.getStatus().getStatusCode());
					if (aaded.getStatus() != null && !aaded.getStatus().equals("")) {
						System.out.println(aaded.getStatus().getStatusCode());
						if (aaded.getStatus().getStatusCode().equals(StatusMasterCodes.APPROVED.toString())
								|| aaded.getStatus().getStatusCode().equals(StatusMasterCodes.REJECTED.toString()))
							data.setReviewed(true);
						if (!data.isReviewed()) {
//							if (aaded.getCurrentReviewRole() != null
//									&& aaded.getCurrentReviewRole().getId() == user.getRole().getId()) {
							data.setAllowToReview(true);
//							}
						}
					}

					data.setDiscrepencyOpend(opend);
					data.setDiscrepencyClosed(closed);
					data.setElementData(elementData);
					animalData.add(data);
					totoal += opend;
				}
			}

			Map<Long, CrfSectionElement> elements = new HashMap<>();
			for (CrfSection sec : crf.getSections()) {
				for (CrfSectionElement ele : sec.getElement()) {
					if (!ele.getType().equals("non")) {
						elements.put(ele.getId(), ele);
					}
				}
			}
			aadDto = new AccessionAnimalDataviewDto();
			aadDto.setStudy(sm);
			aadDto.setElements(elements);
			aadDto.setCrf(crf);
			aadDto.setAnimalData(animalData);
			aadDto.setTotoalDescNotDone(totoal);
			aadDto.setStudy(sm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aadDto;
	}

	@Override
	public boolean saveRandamizationReview(Long studyId, Long userId, String reviewType, String reviewComment) {
		// TODO Auto-generated method stub
		boolean result = false;
		LoginUsers user = userDao.findById(userId);
		StudyMaster study = studyDao.findByStudyId(studyId);
		StatusMaster approveStatus = statusDao.statusMaster(StatusMasterCodes.APPROVED.toString());
		StatusMaster rejectStatus = statusDao.statusMaster("REJECTED");
		StatusMaster activeStatus = statusDao.statusMaster("ACTIVE");
		StatusMaster sendtorivew = statusDao.statusMaster(StatusMasterCodes.SENDTORIVEW.toString());
		boolean flag = false;
		try {
			List<RandamizationDto> dtoList = getSession().createCriteria(RandamizationDto.class)
					.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("activeStatus", true))
					.add(Restrictions.eq("approvalStatus.id", sendtorivew.getId())).list();

			for (RandamizationDto randamization : dtoList) {

				WorkFlow accessionWorkFlow = userDao.accessionWorkFlow("RANDAMIZATION", activeStatus);
				WorkFlowReviewStages workFlowReviewStages = userDao.workFlowReviewStages(user, accessionWorkFlow);
				List<WorkFlowReviewAudit> reviewAudit = userDao
						.readamizationDtoWorkFlowReviewAudit(randamization.getId(), user);
				if (reviewType.equals("Approve")) {
					if (workFlowReviewStages != null) {
						if (workFlowReviewStages.getToRole() != null) {
							WorkFlowReviewAudit workFlowReviewAudit = new WorkFlowReviewAudit();
							workFlowReviewAudit.setRandamizationDto(randamization);
							workFlowReviewAudit.setReviewState(workFlowReviewStages);
							workFlowReviewAudit.setRole(workFlowReviewStages.getFromRole());
							workFlowReviewAudit.setUser(user);
							workFlowReviewAudit.setDateOfActivity(new Date());
							getSession().save(workFlowReviewAudit);
//							randamization.setCurrentReviewRole(workFlowReviewStages.getToRole());
						} else {
							if (randamization.getGender().equals("Male")) {
								randamization.getStudy().setMaleRandamizationReviewStatus(true);
								if (randamization.getStudy().isFemaleRandamizationReviewStatus())
									randamization.getStudy().setRandamizattionStatus(true);
							} else {
								randamization.getStudy().setFemaleRandamizationReviewStatus(true);
								if (randamization.getStudy().isMaleRandamizationReviewStatus())
									randamization.getStudy().setRandamizattionStatus(true);
							}
							randamization.getStudy().setRadamizationStatus(approveStatus);

							randamization.setApprovalStatus(approveStatus);
							randamization.setReviwedBy(user);
							randamization.setReviwedOn(new Date());
							randamization.setReviewComment(reviewComment);

							result = saveRandamizationReviewData(randamization, user, approveStatus, study);
//									getSession().update(aaded);
						}
					} else {
						if (randamization.getGender().equals("Male")) {
							randamization.getStudy().setMaleRandamizationReviewStatus(true);
							if (randamization.getStudy().isFemaleRandamizationReviewStatus())
								randamization.getStudy().setRandamizattionStatus(true);
						} else if (randamization.getGender().equals("Female")) {
							randamization.getStudy().setFemaleRandamizationReviewStatus(true);
							if (randamization.getStudy().isMaleRandamizationReviewStatus())
								randamization.getStudy().setRandamizattionStatus(true);
						}

						randamization.setApprovalStatus(approveStatus);
						randamization.setReviwedBy(user);
						randamization.setReviwedOn(new Date());
						randamization.setReviewComment(reviewComment);
						result = saveRandamizationReviewData(randamization, user, approveStatus, study);
					}
					if (!study.getGender().equals("Both")) {
						randamization.getStudy().setRadamizationStatus(approveStatus);

					} else if (study.getGender().equals("Both") && study.isSplitStudyByGender()) {
						if (randamization.getGender().equals("Male")) {
							if (study.isFemaleRandamizationReviewStatus())
								study.setRadamizationStatus(approveStatus);
							study.setMaleRandamizationReviewStatus(true);
						} else if (randamization.getGender().equals("Female")) {
							if (study.isMaleRandamizationReviewStatus())
								study.setRadamizationStatus(approveStatus);
							study.setFemaleRandamizationReviewStatus(true);
						}
					} else {
						randamization.getStudy().setRadamizationStatus(approveStatus);
					}
				} else if (reviewType.equals("Reject")) {
					if (randamization.getGender().equals("Male")) {
						randamization.getStudy().setMaleRandamizationStatus(false);
					} else if (randamization.getGender().equals("Female")) {
						randamization.getStudy().setFemaleRandamizationStatus(false);
					}
					randamization.setApprovalStatus(rejectStatus);
					randamization.setActiveStatus(false);
					randamization.setReviwedBy(user);
					randamization.setReviwedOn(new Date());
					randamization.setReviewComment(reviewComment);
					if (!study.getGender().equals("Both")) {
						randamization.getStudy().setRadamizationStatus(rejectStatus);

					} else if (study.getGender().equals("Both") && study.isSplitStudyByGender()) {
						if (randamization.getGender().equals("Male")) {
							if (study.isFemaleRandamizationReviewStatus())
								study.setRadamizationStatus(rejectStatus);
							study.setMaleRandamizationReviewStatus(true);
							study.setMaleRandamizationStatus(false);
						} else if (randamization.getGender().equals("Female")) {
							if (study.isMaleRandamizationReviewStatus())
								study.setRadamizationStatus(rejectStatus);
							study.setFemaleRandamizationReviewStatus(true);
							study.setFemaleRandamizationStatus(false);
						}
					} else {
						randamization.getStudy().setRadamizationStatus(rejectStatus);
					}

				}

				for (WorkFlowReviewAudit au : reviewAudit)
					au.setInTheFlow(flag);
			}

			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	private boolean saveRandamizationReviewData(RandamizationDto dto, LoginUsers user, StatusMaster approveStatus,
			StudyMaster study) {
		// TODO Auto-generated method stub
		List<RandamizationGroupDto> randamizationGroupDto = getSession().createCriteria(RandamizationGroupDto.class)
				.add(Restrictions.eq("randamizationDto.id", dto.getId())).list();
		dto.setRandamizationGroupDto(randamizationGroupDto);
		for (RandamizationGroupDto gdto : randamizationGroupDto) {
			List<RandamizationGroupAnimalDto> randamizationGroupAnimalDto = getSession()
					.createCriteria(RandamizationGroupAnimalDto.class)
					.add(Restrictions.eq("randamizationGroupDto.id", gdto.getId())).list();
			gdto.setRandamizationGroupAnimalDto(randamizationGroupAnimalDto);
			for (RandamizationGroupAnimalDto gadto : randamizationGroupAnimalDto) {
				StudyAnimal animal = gadto.getAnimal();
				animal.setPermanentNo(gadto.getAnimalNo());
				animal.setSubGropInfo(gadto.getSubGroup());
				animal.setGroupInfo(gadto.getSubGroup().getGroup());
				animal.setAnimalId(gadto.getOrder());
				getSession().merge(animal);
			}
		}
		return true;
	}
}
