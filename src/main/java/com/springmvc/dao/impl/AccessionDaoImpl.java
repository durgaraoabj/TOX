package com.springmvc.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import com.covide.crf.dao.CrfDAO;
import com.covide.crf.dto.AccessionAnimalDataviewDto;
import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfGroup;
import com.covide.crf.dto.CrfGroupElement;
import com.covide.crf.dto.CrfMapplingTableColumnMap;
import com.covide.crf.dto.CrfRule;
import com.covide.crf.dto.CrfSection;
import com.covide.crf.dto.CrfSectionElement;
import com.covide.crf.dto.CrfSectionElementData;
import com.covide.crf.dto.CrfSectionElementDataAudit;
import com.covide.crf.dto.CrfSectionElementValue;
import com.covide.dto.AccessionDataEntryDto;
import com.covide.enums.StatusMasterCodes;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.AccessionDao;
import com.springmvc.dao.StatusDao;
import com.springmvc.dao.StudyDao;
import com.springmvc.dao.UserDao;
import com.springmvc.model.AccessionAnimalDataDto;
import com.springmvc.model.AccessionAnimalsDataEntryDetails;
import com.springmvc.model.AnimalCage;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.ObservationRole;
import com.springmvc.model.RadamizationAllDataReview;
import com.springmvc.model.RadomizationReviewDto;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyAccessionAnimals;
import com.springmvc.model.StudyAccessionCrfDescrpency;
import com.springmvc.model.StudyAccessionCrfSectionElementData;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAcclamatizationDates;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyCageAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.WorkFlow;
import com.springmvc.model.WorkFlowReviewAudit;
import com.springmvc.model.WorkFlowReviewStages;

@Repository("accessionDao")
@PropertySource(value = { "classpath:application.properties" })
@SuppressWarnings("unchecked")
public class AccessionDaoImpl extends AbstractDao<Long, StudyMaster> implements AccessionDao {
	@Autowired
	private Environment environment;
	@Autowired
	private CrfDAO crfDao;
	@Autowired
	private StudyDao studyDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private StatusDao statuDao;

	@Override
	public Long getMaxRecordNo(Long studyId) {
		Long maxId = 0L;
		Long maxRecNo = 0L;
		StudyAccessionAnimals saa = null;
		try {
			maxRecNo = (Long) getSession().createCriteria(StudyAccessionAnimals.class)
					.setProjection(Projections.max("id")).uniqueResult();
			if (maxRecNo != null && maxRecNo > 0) {
				saa = (StudyAccessionAnimals) getSession().createCriteria(StudyAccessionAnimals.class)
						.add(Restrictions.eq("id", maxRecNo)).uniqueResult();
				if (saa != null) {
					maxId = Long.parseLong(saa.getAnimalsTo());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maxId;
	}

	@Override
	public String saveStudyAccessionAnimalsData(List<StudyAccessionAnimals> saveList) {
		String result = "Failed";
		long saveNo = 0;
		try {
			for (StudyAccessionAnimals saa : saveList) {
				saveNo = (long) getSession().save(saa);
				if (saveNo > 0)
					result = "success";

			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	@Override
	public List<StudyAccessionAnimals> getStudyAccessionAnimalsList(Long studyId) {
		return getSession().createCriteria(StudyAccessionAnimals.class).add(Restrictions.eq("study.id", studyId))
				.list();
	}

	@Override
	public List<StudyAcclamatizationData> getStudyAcclamatizationDataRecordsList(Long studyId, Long userRoleId) {
		Criteria cric = getSession().createCriteria(ObservationRole.class)
				.setProjection(Projections.property("crf.id"));
		if (userRoleId != null) {
			cric.add(Restrictions.eq("roleMaster.id", userRoleId));
		}
		List<Long> roleBasedCrfsIds = cric.list();
		Criteria cri = getSession().createCriteria(StudyAcclamatizationData.class)
				.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("status", "active"));
		List<StudyAcclamatizationData> list = new ArrayList<>();
		if (roleBasedCrfsIds.size() > 0) {
			cri.add(Restrictions.in("crf.id", roleBasedCrfsIds));
			list = cri.list();
		}

		list.forEach((obj) -> {
			obj.setAcclamatizationDates(studyAcclamatizationDatesListByDataId(obj.getId()));
		});
		return list;
	}

	private List<StudyAcclamatizationDates> studyAcclamatizationDatesListByDataId(Long id) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(StudyAcclamatizationDates.class)
				.add(Restrictions.eq("studyAcclamatizationData.id", id)).add(Restrictions.eq("activeStatus", true))
				.list();
	}

	@Override
	public AccessionDataEntryDto getAccessionDataEntryDtoDetails(Long studyId, Long crfId) {
		AccessionDataEntryDto adDto = null;
		StudyAcclamatizationData sad = null;
		Crf crf = null;
		StudyMaster sm = null;
		AccessionAnimalsDataEntryDetails saaed = null;
		int entrySize = 0;
		StudyAccessionAnimals saaPojo = null;
		List<StudyAccessionAnimals> saaList = null;
		int genderSize = 1;
		try {
			sm = (StudyMaster) getSession().createCriteria(StudyMaster.class).add(Restrictions.eq("id", studyId))
					.uniqueResult();

			sad = (StudyAcclamatizationData) getSession().createCriteria(StudyAcclamatizationData.class)
					.add(Restrictions.eq("studyId", studyId)).add(Restrictions.eq("crf.id", crfId))
					.add(Restrictions.eq("status", "active")).uniqueResult();

			crf = (Crf) getSession().createCriteria(Crf.class).add(Restrictions.eq("id", crfId)).uniqueResult();
			Hibernate.initialize(crf.getSections());
			Collections.sort(crf.getSections());
			for (CrfSection sec : crf.getSections()) {
				Hibernate.initialize(sec.getElement());
				if (sec.getElement() == null || sec.getElement().size() == 0) {
				} else {
					for (CrfSectionElement ele : sec.getElement()) {
						if (ele.getType().equals("selectTable")) {
							List<CrfSectionElementValue> evalues = new ArrayList<>();
							CrfMapplingTableColumnMap colMap = crfMapplingTableColumnMap(ele);
							String sql = "Select " + colMap.getMappingColumn().getCloumnName() + " from "
									+ colMap.getMappingTable().getTableName();
							SQLQuery query = getSession().createSQLQuery(sql);
							query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
							List results = query.list();
							for (Object object : results) {
								Map<String, String> row = (Map<String, String>) object;
								for (Map.Entry<String, String> m : row.entrySet()) {
									CrfSectionElementValue mv = new CrfSectionElementValue();
									mv.setValue(m.getValue());
									evalues.add(mv);
								}

							}
							ele.setElementValues(evalues);
						} else
							Hibernate.initialize(ele.getElementValues());
					}
				}
				Hibernate.initialize(sec.getGroup());
				CrfGroup group = sec.getGroup();
				if (group != null) {
					Hibernate.initialize(group.getElement());
					if (group.getElement() == null || group.getElement().size() == 0) {
					} else {
						for (CrfGroupElement ele : group.getElement()) {
							Hibernate.initialize(ele.getElementValues());
						}
					}
				}
			}
			Long maxId = (Long) getSession().createCriteria(AccessionAnimalsDataEntryDetails.class)
					.add(Restrictions.eq("crfId", crfId)).add(Restrictions.eq("studyId", studyId))
					.add(Restrictions.eq("entryType", "scheduled")).setProjection(Projections.max("id")).uniqueResult();
			if (maxId != null) {
				saaed = (AccessionAnimalsDataEntryDetails) getSession()
						.createCriteria(AccessionAnimalsDataEntryDetails.class).add(Restrictions.eq("id", maxId))
						.uniqueResult();
			}
			Long count = (Long) getSession().createCriteria(AccessionAnimalsDataEntryDetails.class)
					.add(Restrictions.eq("crfId", crfId)).add(Restrictions.eq("studyId", studyId))
					.add(Restrictions.eq("entryType", "scheduled")).setProjection(Projections.count("id"))
					.uniqueResult();
			if (count == null || count == 0)
				entrySize = 0;
			else
				entrySize = Integer.parseInt(count + "");

			saaList = getSession().createCriteria(StudyAccessionAnimals.class).add(Restrictions.eq("study.id", studyId))
					.list();
			if (saaList.size() > 0) {
				saaPojo = saaList.get(0);
				genderSize = saaList.size();
			}

			adDto = new AccessionDataEntryDto();
			adDto.setSad(sad);
			adDto.setCrf(crf);
			adDto.setSm(sm);
			adDto.setAaded(saaed);
			adDto.setEntrySize(entrySize);
			adDto.setSaaPojo(saaPojo);
			adDto.setGenderSize(genderSize);
			adDto.setSaaList(saaList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return adDto;
	}

	private CrfMapplingTableColumnMap crfMapplingTableColumnMap(CrfSectionElement ele) {
		CrfMapplingTableColumnMap map = (CrfMapplingTableColumnMap) getSession()
				.createCriteria(CrfMapplingTableColumnMap.class).add(Restrictions.eq("element", ele)).uniqueResult();
		return map;
	}

	@Override
	public StudyAccessionCrfDescrpency studyAccessCrfDescrpencySec(AccessionAnimalsDataEntryDetails animalData, Crf crf,
			Object object, StudyAccessionCrfSectionElementData data) {
		StudyAccessionCrfDescrpency desc = (StudyAccessionCrfDescrpency) getSession()
				.createCriteria(StudyAccessionCrfDescrpency.class).add(Restrictions.eq("crfId", crf.getId()))
				.add(Restrictions.eq("accAnimalDataEntryDetails", animalData))
				.add(Restrictions.eq("stydyAccCrfSecEleData", data)).add(Restrictions.eq("status", "open"))
				.uniqueResult();
		return desc;
	}

	@Override
	public boolean saveStudyAccessionAnimalsFormDetails(AccessionAnimalsDataEntryDetails accaded,
			List<StudyAccessionCrfSectionElementData> sectionData, List<StudyAccessionCrfSectionElementData> secList,
			Crf crf, LoginUsers user, boolean b, List<Long> secIdList, Long studyId) {
		StatusMaster activeStatus = statuDao.statusMaster("ACTIVE");
		boolean flag = false;
		long accanimalNo = 0;
		boolean sectionFlag = false;
		boolean rulesFlag = false;
		StudyMaster study = studyDao.findByStudyId(studyId);
		List<StudyAccessionCrfSectionElementData> sacseDataList = new ArrayList<>();
		try {
			accanimalNo = (long) getSession().save(accaded);
			for (StudyAccessionCrfSectionElementData sacsed : sectionData) {

				sacsed.setStudyAccAnimal(accaded);
				long sacsedNo = (long) getSession().save(sacsed);
				sacseDataList.add(sacsed);
				CrfSectionElementDataAudit saudit = new CrfSectionElementDataAudit();
//				saudit.setDataCount(animalData);
				saudit.setStudy(study);
				saudit.setData(sacsed);
				saudit.setElement(sacsed.getElement());
				saudit.setKayName(sacsed.getKayName());
				saudit.setNewValue(sacsed.getValue());
				saudit.setLoginUsers(user);
				saudit.setCreatedOn(new Date());
				long sauditNo = (long) getSession().save(saudit);

				if (sacsedNo > 0 && sauditNo > 0)
					sectionFlag = true;
			}
			Map<String, StudyAccessionCrfDescrpency> map = new HashMap<>();
			List<CrfRule> rules = crfRuleOfSubElements(crf, secIdList);
			if (rules.size() > 0) {
				Map<String, StudyAccessionCrfSectionElementData> secData = new HashMap<>();
				for (StudyAccessionCrfSectionElementData s : sacseDataList)
					secData.put(s.getKayName(), s);
				for (CrfRule rule : rules) {
					if (rule.getSecEle() != null) {
						StudyAccessionCrfSectionElementData data = secData
								.get(rule.getSecEle().getId() + "_" + rule.getSecEle().getName());
						StudyAccessionCrfDescrpency scd = studyAccessCrfDescrpencySec(accaded, crf, null, data);
						if (scd == null) {
//							sdsdsd
							scd = new StudyAccessionCrfDescrpency();
							scd.setValue(data.getValue());
							scd.setCrf(crf);
							scd.setAccAnimalDataEntryDetails(accaded);
							scd.setStudy(study);
							scd.setKayName(data.getKayName());
							scd.setSecElement(data.getElement());
							scd.setStydyAccCrfSecEleData(data);
							scd.setCrfRule(rule);
							scd.setCreatedBy(user.getUsername());
							scd.setCreatedOn(new Date());
							if (map.get(data.getKayName()) == null) {
								getSession().save(scd);
								rulesFlag = true;
							}
						}
						rulesFlag = true;
						map.put(data.getKayName(), scd);
					}
					rulesFlag = true;
				}
			} else
				rulesFlag = true;
			if (accanimalNo > 0 && sectionFlag && rulesFlag) {
				flag = true;
				WorkFlow accessionWorkFlow = userDao.accessionWorkFlow("ACCESSION", activeStatus);
				WorkFlowReviewStages workFlowReviewStages = userDao.workFlowReviewStages(user, accessionWorkFlow);
				if (workFlowReviewStages != null) {
					WorkFlowReviewAudit workFlowReviewAudit = new WorkFlowReviewAudit();
					workFlowReviewAudit.setAccessionAnimalsDataEntryDetailsId(accaded);
					workFlowReviewAudit.setReviewState(workFlowReviewStages);
					workFlowReviewAudit.setRole(workFlowReviewStages.getFromRole());
					workFlowReviewAudit.setUser(user);
					workFlowReviewAudit.setDateOfActivity(new Date());
					getSession().save(workFlowReviewAudit);
					accaded.setCurrentReviewRole(workFlowReviewStages.getToRole());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}

	@Override
	public List<CrfRule> crfRuleOfSubElements(Crf crf, List<Long> sectionIdsList) {
		if (sectionIdsList.size() > 0) {
			for (Long l : sectionIdsList) {
				System.out.println(l);
			}
			List<CrfRule> rule = getSession().createCriteria(CrfRule.class).add(Restrictions.eq("crf", crf))
					.add(Restrictions.in("secEle.id", sectionIdsList)).add(Restrictions.eq("active", true)).list();
			for (CrfRule r : rule) {
				Hibernate.initialize(r.getCrf());
				Hibernate.initialize(r.getSecEle());
				Hibernate.initialize(r.getGroupEle());
				Hibernate.initialize(r.getOtherCrf());
			}
			return rule;
		} else
			return new ArrayList<>();
	}

	@Override
	public AccessionAnimalDataviewDto getAccessionAnimalDataviewDtoDetails(Long crfId, Long studyId) {
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

			aadedList = getSession().createCriteria(AccessionAnimalsDataEntryDetails.class)
					.add(Restrictions.eq("crfId", crfId)).add(Restrictions.eq("study.id", studyId)).list();

			crf = (Crf) getSession().createCriteria(Crf.class).add(Restrictions.eq("id", crfId)).uniqueResult();
			Hibernate.initialize(crf.getSections());
			if (aadedList != null && aadedList.size() > 0) {
				for (AccessionAnimalsDataEntryDetails aaded : aadedList) {
					secDataList = getSession().createCriteria(StudyAccessionCrfSectionElementData.class)
							.add(Restrictions.eq("studyAccAnimal", aaded)).list();

					Map<Long, StudyAccessionCrfSectionElementData> elementData = new HashMap<>();
					if (secDataList != null && secDataList.size() > 0) {
						for (StudyAccessionCrfSectionElementData scsed : secDataList) {
							if (!scsed.getElement().getType().equals("non")) {
								elementData.put(scsed.getElement().getId(), scsed);

								Long red = (Long) getSession().createCriteria(StudyAccessionCrfDescrpency.class)
										.add(Restrictions.eq("stydyAccCrfSecEleData", scsed))
										.add(Restrictions.or(Restrictions.eq("status", "open"),
												Restrictions.eq("status", "onHold")))
										.setProjection(Projections.rowCount()).uniqueResult();
								scsed.setDiscripency(red.intValue());

								Long red2 = (Long) getSession().createCriteria(StudyAccessionCrfDescrpency.class)
										.add(Restrictions.eq("stydyAccCrfSecEleData", scsed))
										.setProjection(Projections.rowCount()).uniqueResult();
								scsed.setDiscripencyClose(red2.intValue());
							}
						}
					}
					AccessionAnimalDataDto data = new AccessionAnimalDataDto();
					data.setId(aaded.getId());
					data.setDeviationMessage(aaded.getDeviation());
					data.setUserName(aaded.getCreatedBy());
					data.setDate(aaded.getCreatedOn());
					data.setUserName(aaded.getCreatedBy());
					data.setAnimalno(aaded.getAnimal().getSequnceNo() + "");
					data.setAnimalId(aaded.getAnimalTempId() + "");
					Long red = (Long) getSession().createCriteria(StudyAccessionCrfDescrpency.class)
							.add(Restrictions.eq("accAnimalDataEntryDetails", aaded))
							.add(Restrictions.eq("status", "closed")).setProjection(Projections.rowCount())
							.uniqueResult();

					Long all = (Long) getSession().createCriteria(StudyAccessionCrfDescrpency.class)
							.add(Restrictions.eq("accAnimalDataEntryDetails", aaded))
							.setProjection(Projections.rowCount()).uniqueResult();
					if (all == red) {
						data.setDiscrepencyClosed(0);
					} else {
						Long diff = all - red;
						data.setDiscrepencyClosed(diff.intValue());
						totoal += diff.intValue();
					}

					data.setElementData(elementData);
					animalData.add(data);

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

	@Override
	public String saveStudyAccessionAnimals(List<StudyAccessionAnimals> animalsmeataData, List<StudyAnimal> animals) {
		// TODO Auto-generated method stub
		animals.forEach((animal) -> {
			getSession().save(animal);
		});
		animalsmeataData.forEach((animal) -> {
			getSession().save(animal);
		});
		return "success";
	}

	@Override
	public List<StudyAnimal> studyAnimalsGenderBase(Long studyId, String string) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", studyId))
				.add(Restrictions.eq("gender", string)).list();
	}

	@Override
	public StudyAnimal nextAnimalForAccessionData(Long studyAcclamatizationDateId, Long studyAcclamatizationDataId, Long studyId, Long crfId,
			String gender, int noOfEntry, String selecteDate) {
		try {

			
			StudyAcclamatizationDates date = null;
			if(studyAcclamatizationDateId != null)
				date = studyAcclamatizationDates(studyAcclamatizationDateId);
			else {
				SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
				Date fromDate = sdf.parse(selecteDate);
				Calendar c = Calendar.getInstance();
				c.setTime(fromDate);
				c.add(Calendar.DATE, 1);
//				Date toDate = sdf.parse(sdf.format(c.getTime()));
				StudyAcclamatizationData data = studyAcclamatizationDataById(studyAcclamatizationDataId);
					date = (StudyAcclamatizationDates) getSession()
						.createCriteria(StudyAcclamatizationDates.class)
						.add(Restrictions.eq("studyAcclamatizationData.id", data.getId()))
						.add(Restrictions.eq("activeStatus", true)).add(Restrictions.eq("gender", gender))
						.add(Restrictions.eq("accDate", fromDate))
	//					.add(Restrictions.eq("accDate", fromDate))
						.uniqueResult();
				if (!data.getStudy().getGender().equals("Both")) {
					date = (StudyAcclamatizationDates) getSession().createCriteria(StudyAcclamatizationDates.class)
							.add(Restrictions.eq("studyAcclamatizationData.id", data.getId()))
							.add(Restrictions.eq("activeStatus", true)).add(Restrictions.eq("accDate", fromDate))
	//						.add(Restrictions.eq("accDate", fromDate))
							.uniqueResult();
				}
			}
			Long count = (Long) getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", studyId))
					.add(Restrictions.eq("gender", gender)).setProjection(Projections.count("id")).uniqueResult();
			for (int i = 1; i <= date.getNoOfEntry(); i++) {
				if (noOfEntry != 0) {
					i = noOfEntry;
				}
				List<Long> animalIds = getSession().createCriteria(AccessionAnimalsDataEntryDetails.class)
						.add(Restrictions.eq("studyAcclamatizationDates.id", date.getId()))
						.add(Restrictions.eq("entryType", "scheduled")).add(Restrictions.eq("gender", gender))
						.add(Restrictions.eq("noOfEntry", i)).setProjection(Projections.property("animal.id")).list();
				if (animalIds.size() < count) {
					Criteria cri = getSession().createCriteria(StudyAnimal.class)
							.add(Restrictions.eq("study.id", studyId))
//							.add(Restrictions.eq("accessionDataEntryStatus", false))
							.add(Restrictions.eq("gender", gender)).setProjection(Projections.min("sequnceNo"));
					if (animalIds.size() > 0)
						cri.add(Restrictions.not(Restrictions.in("id", animalIds)));
					int sequnceNo = 0;
					try {
						sequnceNo = (int) cri.uniqueResult();
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (sequnceNo != 0) {
						StudyAnimal animal = (StudyAnimal) getSession().createCriteria(StudyAnimal.class)
								.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("gender", gender))
								.add(Restrictions.eq("sequnceNo", sequnceNo)).uniqueResult();
						if (animal != null)
							animal.setNoOfEntry(i);
						return animal;
					}
					break;
				}
				if (noOfEntry != 0) {
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	

	private StudyAcclamatizationData studyAcclamatizationDataById(Long studyAcclamatizationDataId) {
		// TODO Auto-generated method stub
		return (StudyAcclamatizationData) getSession().get(StudyAcclamatizationData.class, studyAcclamatizationDataId);
	}

	@Override
	public StudyAcclamatizationData studyAcclamatizationData(Long crf, Long sm) {
		return (StudyAcclamatizationData) getSession().createCriteria(StudyAcclamatizationData.class)
				.add(Restrictions.eq("crf.id", crf)).add(Restrictions.eq("study.id", sm)).uniqueResult();
	}

	@Override
	public List<StudyAnimal> allStudyAnimals(Long studyId) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", studyId)).list();
	}

	@Override
	public StudyAnimal studyAnimal(Long animalId) {
		// TODO Auto-generated method stub
		if(animalId != null)
			return (StudyAnimal) getSession().get(StudyAnimal.class, animalId);
		else
			return null;
	}

	@Override
	public List<Long> studyActiveAnimalIds(StatusMaster activeStatus, Long studyId) {
		// TODO Auto-generated method stub
		try {
			return getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", studyId))
					.add(Restrictions.eq("animalStatus", activeStatus)).setProjection(Projections.property("id"))
					.list();
		} catch (Exception e) {
			// TODO: handle exception
			return new ArrayList<>();
		}
	}

	@Override
	public List<AccessionAnimalsDataEntryDetails> approvedAccessionAnimalsDataEntryDetails(Crf crf,
			List<Long> studyAnimalIds, StatusMaster approveStatus) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(AccessionAnimalsDataEntryDetails.class).add(Restrictions.eq("crf", crf))
//				.add(Restrictions.eq("status", approveStatus))
				.add(Restrictions.in("animal.id", studyAnimalIds)).list();
	}

	@Override
	public List<AccessionAnimalsDataEntryDetails> accessionAnimalsDataEntryDetails(Crf crf, List<Long> studyAnimalIds,
			StatusMaster approveStatus) {
		Criteria cri = getSession().createCriteria(AccessionAnimalsDataEntryDetails.class)
				.add(Restrictions.eq("crf", crf)).add(Restrictions.in("animal.id", studyAnimalIds));
		if (approveStatus != null)
			cri.add(Restrictions.eq("status", approveStatus));
		List<AccessionAnimalsDataEntryDetails> list = cri.list();
		return list;
	}

	@Override
	public List<AccessionAnimalsDataEntryDetails> accessionAnimalsDataEntryDetails(Crf crf, List<Long> studyAnimalIds,
			StatusMaster approveStatus, StatusMaster sentToReview) {
		List<AccessionAnimalsDataEntryDetails> list = getSession()
				.createCriteria(AccessionAnimalsDataEntryDetails.class).add(Restrictions.eq("crf", crf))
				.add(Restrictions.eq("status", approveStatus))
//				.add(Restrictions.or(Restrictions.eq("status", approveStatus), Restrictions.eq("status", sentToReview)))
				.add(Restrictions.in("animal.id", studyAnimalIds)).list();
		return list;
	}
	@Override
	public StudyAccessionCrfSectionElementData weightStudyAccessionCrfSectionElementData(
			AccessionAnimalsDataEntryDetails each, String sectionName, String elementName) {
		// TODO Auto-generated method stub
		Long sectionId = crfDao.sectionIdWithCrfId(each.getCrf().getId(), sectionName);
		Long elementId = crfDao.sectionElemntId(sectionId, elementName);
		StudyAccessionCrfSectionElementData data =  (StudyAccessionCrfSectionElementData) getSession()
				.createCriteria(StudyAccessionCrfSectionElementData.class).add(Restrictions.eq("studyAccAnimal", each))
				.add(Restrictions.eq("element.id", elementId)).uniqueResult();
		System.out.println(data.getId()+"\t"+data.getValue());
		return data;
	}

	@Override
	public List<AnimalCage> allAnimalCage(int i) {
		List<AnimalCage> cages = getSession().createCriteria(AnimalCage.class).list();
		if (cages.size() < i) {
			int count = i - cages.size();
			for (int start = 1; start <= count; start++) {
				AnimalCage cage = new AnimalCage();
				getSession().save(cage);
				cage.setName("CAGE " + cage.getId());
				cage.setNo(cage.getId().intValue());
				cage.setCageId(cage.getName());
			}
		}

		return getSession().createCriteria(AnimalCage.class).list();
	}

	@Override
	public AnimalCage animalCage(String cageName) {
		AnimalCage cage = null;
		try {
			cage = (AnimalCage) getSession().createCriteria(AnimalCage.class).add(Restrictions.eq("cageId", cageName))
					.uniqueResult();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (cage == null) {
			cage = new AnimalCage();
			cage.setCageId(cageName);
			cage.setName(cageName);
			getSession().save(cage);
			cage.setNo(cage.getId().intValue());
		}
		return cage;
	}

	@Override
	public void studyCageAnimals(List<StudyCageAnimal> cageAnimals) {
		// TODO Auto-generated method stub
		cageAnimals.forEach((ca) -> {
			getSession().save(ca);
		});
	}

	@Override
	public int studyCageList(Long studyId) {
		try {
			Long size = (Long) getSession().createCriteria(StudyCageAnimal.class)
					.add(Restrictions.eq("study.id", studyId)).setProjection(Projections.rowCount()).uniqueResult();
			return size.intValue();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	@Override
	public List<StudyAnimal> studyAnimalsOf(List<Long> animalIds) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(StudyAnimal.class).add(Restrictions.in("id", animalIds)).list();
	}

	@Override
	public void studyCageAnimals(List<StudyCageAnimal> cageAnimals, List<StudyAnimal> allAnimals) {
		// TODO Auto-generated method stub
		cageAnimals.forEach((ca) -> {
			getSession().save(ca);
		});
		allAnimals.forEach((ca) -> {
			getSession().merge(ca);
		});
	}

	@Override
	public RadomizationReviewDto radomizationReviewDto(Long studyId) {
		// TODO Auto-generated method stub
		RadomizationReviewDto rrt = new RadomizationReviewDto();
		List<RadomizationReviewDto> rrdList = getSession().createCriteria(RadomizationReviewDto.class)
				.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("viewData", true)).list();
		for (RadomizationReviewDto rrd : rrdList) {
			Hibernate.initialize(rrd.getCurrentReviewRole());
			Hibernate.initialize(rrd.getStudy());
			Hibernate.initialize(rrd.getReviewStatus());
			Hibernate.initialize(rrd.getRandamizaitonSheetMale());
			Hibernate.initialize(rrd.getRandamizaitonSheetFemale());
			Hibernate.initialize(rrd.getRandamizaitonSheetMaleAscedning());
			Hibernate.initialize(rrd.getRandamizaitonSheetFemaleAscedning());
			Hibernate.initialize(rrd.getRandamizaitonSheetMaleGruoup());
			List<RadamizationAllDataReview> list = rrd.getRandamizaitonSheetMaleGruoup();
			for (RadamizationAllDataReview rr : list) {
				Hibernate.initialize(rr.getAnimals());
			}
			Hibernate.initialize(rrd.getRandamizaitonSheetFemaleGruoup());
			list = rrd.getRandamizaitonSheetFemaleGruoup();
			for (RadamizationAllDataReview rr : list) {
				Hibernate.initialize(rr.getAnimals());
			}
		}
		if (rrdList.size() == 1)
			return rrdList.get(0);
		else {
			boolean flag = true;
			for (RadomizationReviewDto rrd : rrdList) {
				if (flag) {
					rrt.setErrorCode(rrd.getErrorCode());
					rrt.setErrorMessage(rrd.getErrorMessage());
					rrt.setStudy(rrd.getStudy());
					rrt.setGender("Both");
					rrt.setViewData(rrd.isViewData());

					flag = false;
				}
				if (rrd.getGender().equals("Male")) {
					rrt.setRandamizaitonSheetMale(rrd.getRandamizaitonSheetMale());
					rrt.setRandamizaitonSheetMaleAscedning(rrd.getRandamizaitonSheetMaleAscedning());
					rrt.setRandamizaitonSheetMaleGruoup(rrd.getRandamizaitonSheetMaleGruoup());

					rrt.setReviewStatus(rrd.getReviewStatus());
					rrt.setApprovedBy(rrd.getApprovedBy());
					rrt.setApprovedOn(rrd.getApprovedOn());
					rrt.setRejectedBy(rrd.getRejectedBy());
					rrt.setRejectedOn(rrd.getRejectedOn());
					rrt.setReviewComment(rrd.getReviewComment());
					rrt.setCurrentReviewRole(rrd.getCurrentReviewRole());
				} else {
					rrt.setRandamizaitonSheetFemale(rrd.getRandamizaitonSheetFemale());
					rrt.setRandamizaitonSheetFemaleAscedning(rrd.getRandamizaitonSheetFemaleAscedning());
					rrt.setRandamizaitonSheetFemaleGruoup(rrd.getRandamizaitonSheetFemaleGruoup());

					rrt.setReviewStatus(rrd.getReviewStatus());
					rrt.setApprovedBy(rrd.getApprovedBy());
					rrt.setApprovedOn(rrd.getApprovedOn());
					rrt.setRejectedBy(rrd.getRejectedBy());
					rrt.setRejectedOn(rrd.getRejectedOn());
					rrt.setReviewComment(rrd.getReviewComment());
					rrt.setCurrentReviewRole(rrd.getCurrentReviewRole());
				}
			}
		}
		return rrt;
	}

	@Override
	public RadomizationReviewDto radomizationReviewDto(Long studyId, Long userId, String gender) {
		LoginUsers user = (LoginUsers) getSession().get(LoginUsers.class, userId);
		RadomizationReviewDto rrd = (RadomizationReviewDto) getSession().createCriteria(RadomizationReviewDto.class)
				.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("viewData", true)).add(Restrictions.eq("gender", gender)).uniqueResult();
		Hibernate.initialize(rrd.getCurrentReviewRole());
		Hibernate.initialize(rrd.getStudy());
		Hibernate.initialize(rrd.getReviewStatus());
		Hibernate.initialize(rrd.getRandamizaitonSheetMale());
		Hibernate.initialize(rrd.getRandamizaitonSheetFemale());
		Hibernate.initialize(rrd.getRandamizaitonSheetMaleAscedning());
		Hibernate.initialize(rrd.getRandamizaitonSheetFemaleAscedning());
		Hibernate.initialize(rrd.getRandamizaitonSheetMaleGruoup());
		List<RadamizationAllDataReview> list = rrd.getRandamizaitonSheetMaleGruoup();
		for (RadamizationAllDataReview rr : list) {
			Hibernate.initialize(rr.getAnimals());
		}
		Hibernate.initialize(rrd.getRandamizaitonSheetFemaleGruoup());
		list = rrd.getRandamizaitonSheetFemaleGruoup();
		for (RadamizationAllDataReview rr : list) {
			Hibernate.initialize(rr.getAnimals());
		}

		if (rrd.getReviewStatus().getStatusCode().equals(StatusMasterCodes.SENDTORIVEW.toString())
				|| rrd.getReviewStatus().getStatusCode().equals(StatusMasterCodes.INREVIEW.toString())) {
			rrd.setReviewed(true);
			rrd.setDisplayMessage("Radamization is In Review.");
		} else if (rrd.getReviewStatus().getStatusCode().equals(StatusMasterCodes.REVIEWED.toString())) {
			rrd.setDisplayMessage("Radamization has Reviewed.");
		} else if (rrd.getReviewStatus().getStatusCode().equals(StatusMasterCodes.REJECTED.toString())) {
			rrd.setDisplayMessage("Radamization has Rejected.");
		}
		if (rrd.isReviewed()) {
			if (rrd.getCurrentReviewRole().getId() == user.getRole().getId()) {
				rrd.setAllowToReview(true);
			} else {
				rrd.setDisplayMessage("Radamization sent to next level.");
			}
		}
		return rrd;
	}

	@Override
	public Long noOfAvailableStudyAnimalsCount(Long studyId) {
		// TODO Auto-generated method stub
		StatusMaster activeStatus = statuDao.statusMaster(StatusMasterCodes.ACTIVE.toString());
		try {
			return (Long) getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", studyId))
					.add(Restrictions.eq("animalStatus", activeStatus)).setProjection(Projections.rowCount())
					.uniqueResult();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0l;
		}
	}

	@Override
	public StudyAccessionCrfSectionElementData studyAccessionCrfSectionElementWithId(Long eleId) {
		// TODO Auto-generated method stub
		return (StudyAccessionCrfSectionElementData) getSession().get(StudyAccessionCrfSectionElementData.class, eleId);
	}

	@Override
	public List<CrfSectionElementDataAudit> crfSectionElementDataAudit(Long id, String activityType) {
		// TODO Auto-generated method stub
		Criteria cri = getSession().createCriteria(CrfSectionElementDataAudit.class);
		if (activityType.equals("observation")) {
			cri.add(Restrictions.eq("animalObsercationElementData.id", id)).list();
		} else if (activityType.equals("acesstion")) {
			cri.add(Restrictions.eq("data.id", id)).list();
		}
		return cri.list();
	}

	@Override
	public String accessionActivityElementUpdate(StudyAccessionCrfSectionElementData element,
			CrfSectionElementData obsercationElement, CrfSectionElementDataAudit saudit) {
		try {
			if (element != null)
				getSession().merge(element);
			if (obsercationElement != null)
				getSession().merge(obsercationElement);
			getSession().save(saudit);
			return saudit.getNewValue();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public CrfSectionElementData crfSectionElementWithId(Long eleId) {
		return (CrfSectionElementData) getSession().get(CrfSectionElementData.class, eleId);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public StudyAcclamatizationDates studyAcclamatizationDatesOfCurrentDate(StudyMaster study, Long id, Date fromDate, String gender)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		if (fromDate != null)
			fromDate = sdf.parse(sdf.format(new Date()));

		Calendar c = Calendar.getInstance();
		c.setTime(fromDate);
		c.add(Calendar.DATE, 1);
		Date toDate = sdf.parse(sdf.format(c.getTime()));
		Criteria cri = getSession()
				.createCriteria(StudyAcclamatizationDates.class).add(Restrictions.eq("studyAcclamatizationData.id", id))
				.add(Restrictions.eq("activeStatus", true))
//				.add(Restrictions.between("accDate", fromDate, toDate))
				.add(Restrictions.eq("accDate", fromDate));
		if(study.getGender().equals("Both")) {
			cri.add(Restrictions.eq("gender", gender));
		}
		StudyAcclamatizationDates date = (StudyAcclamatizationDates) cri.uniqueResult();
		if (date == null) {
			date = (StudyAcclamatizationDates) getSession().createCriteria(StudyAcclamatizationDates.class)
					.add(Restrictions.eq("studyAcclamatizationData.id", id)).add(Restrictions.eq("activeStatus", true))
//						.add(Restrictions.between("accDate", fromDate, toDate))
					.add(Restrictions.eq("accDate", fromDate)).uniqueResult();
		}
		return date;
	}

	@Override
	public Map<Integer, SubGroupInfo> studyAllSubgroups(StudyMaster study) {
		// TODO Auto-generated method stub
		int count = 1;
		List<SubGroupInfo> list = getSession().createCriteria(SubGroupInfo.class)
				.add(Restrictions.eq("study.id", study.getId())).addOrder(Order.asc("id")).list();
		Map<Integer, SubGroupInfo> subGroupOrder = new HashMap<>();
		for (SubGroupInfo sb : list) {
			sb.setSubGroupNo(count);
			subGroupOrder.put(count++, sb);
		}
		return subGroupOrder;
	}

	@Override
	public StudyAcclamatizationDates studyAcclamatizationDates(Long studyAcclamatizationDatesId) {
		// TODO Auto-generated method stub
		return (StudyAcclamatizationDates) getSession().get(StudyAcclamatizationDates.class, studyAcclamatizationDatesId);
	}

	
}
