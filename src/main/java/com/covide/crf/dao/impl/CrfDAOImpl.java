package com.covide.crf.dao.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.covide.crf.dao.CrfDAO;
import com.covide.crf.dto.CRFGroupItem;
import com.covide.crf.dto.CRFGroupItemStd;
import com.covide.crf.dto.CRFItemValuesStd;
import com.covide.crf.dto.CRFSections;
import com.covide.crf.dto.CRFSectionsStd;
import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfDateComparison;
import com.covide.crf.dto.CrfDescrpency;
import com.covide.crf.dto.CrfDescrpencyAudit;
import com.covide.crf.dto.CrfDescrpencyLog;
import com.covide.crf.dto.CrfEleCaliculation;
import com.covide.crf.dto.CrfGroup;
import com.covide.crf.dto.CrfGroupDataRows;
import com.covide.crf.dto.CrfGroupElement;
import com.covide.crf.dto.CrfGroupElementData;
import com.covide.crf.dto.CrfGroupElementValue;
import com.covide.crf.dto.CrfItems;
import com.covide.crf.dto.CrfItemsStd;
import com.covide.crf.dto.CrfMapplingTable;
import com.covide.crf.dto.CrfMapplingTableColumn;
import com.covide.crf.dto.CrfMapplingTableColumnMap;
import com.covide.crf.dto.CrfMetaData;
import com.covide.crf.dto.CrfMetaDataStd;
import com.covide.crf.dto.CrfRule;
import com.covide.crf.dto.CrfRuleWithOther;
import com.covide.crf.dto.CrfSection;
import com.covide.crf.dto.CrfSectionElement;
import com.covide.crf.dto.CrfSectionElementData;
import com.covide.crf.dto.CrfSectionElementDataAudit;
import com.covide.crf.dto.CrfSectionElementInstrumentValue;
import com.covide.crf.dto.CrfSectionElementValue;
import com.covide.crf.dto.DTNAME;
import com.covide.crf.dto.DataSet;
import com.covide.crf.dto.DataSetPhase;
import com.covide.crf.dto.DataSetPhasewiseCrfs;
import com.covide.enums.StatusMasterCodes;
import com.mysql.fabric.xmlrpc.base.Array;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.RoleMasterDao;
import com.springmvc.model.AmendmentDetails;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.ObservationReviewLevel;
import com.springmvc.model.ObservationRole;
import com.springmvc.model.PeriodCrfs;
import com.springmvc.model.ReviewLevel;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StdSubGroupObservationCrfsLog;
import com.springmvc.model.StudyAccessionCrfDescrpency;
import com.springmvc.model.StudyAccessionCrfDescrpencyLog;
import com.springmvc.model.StudyAccessionCrfSectionElementData;
import com.springmvc.model.StudyAcclamatizationDates;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.StudyTreatmentDataDates;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCountReviewLevel;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.SubjectDataEntryDetails;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.model.WorkFlowReviewAudit;

@Repository("crfDAO")
@SuppressWarnings("unchecked")
public class CrfDAOImpl extends AbstractDao<Long, Crf> implements CrfDAO {
	@Autowired
	RoleMasterDao roleDao;

	@Override
	public List<Crf> findAllCrfs() {
		List<Crf> ml = getSession().createCriteria(Crf.class).list();
		ml.stream().forEach((crf) -> {
			crf.setObservationRoles(
					getSession().createCriteria(ObservationRole.class).add(Restrictions.eq("crf", crf)).list());
		});
		return ml;
	}

//	@Override
//	public EmployeeMaster getEmployeeByEmpId(String empId) {
//		// TODO Auto-generated method stub
//		return (EmployeeMaster) createEntityCriteria().add(Restrictions.eq("empId", empId)).uniqueResult();
//	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfMetaData> findAll() {
		List<CrfMetaData> ml = getSession().createCriteria(CrfMetaData.class).list();
		for (CrfMetaData m : ml) {
			Hibernate.initialize(m.getSections());
			for (CRFSections s : m.getSections()) {
				Hibernate.initialize(s.getItemList());
				for (CrfItems i : s.getItemList()) {
					Hibernate.initialize(i.getItemResponceValues());
				}
			}
			Hibernate.initialize(m.getGroups());
			for (CRFGroupItem g : m.getGroups()) {
				Hibernate.initialize(g.getItemList());
				for (CrfItems i : g.getItemList()) {
					Hibernate.initialize(i.getItemResponceValues());
				}
			}
		}
		return ml;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfMetaDataStd> findAllStdCrfs(StudyMaster sm) {
		// TODO Auto-generated method stub
		List<CrfMetaDataStd> list = getSession().createCriteria(CrfMetaDataStd.class).add(Restrictions.eq("std", sm))
				.list();
		return list;
	}

	@Override
	public void savestdCrf1(CrfMetaDataStd scrf) {
		getSession().save(scrf);
	}

	@Override
	public void updatestdCrf(CrfMetaDataStd scrf) {
		getSession().update(scrf);
	}

	@Override
	public void updatestdCrf(List<CrfMetaDataStd> stdcrfsUpdate) {
		// TODO Auto-generated method stub
		for (CrfMetaDataStd std : stdcrfsUpdate)
			getSession().update(std);
	}

	@Override
	public void savestdCrfSections1(CRFSectionsStd std) {
		// TODO Auto-generated method stub
		getSession().save(std);
	}

	@Override
	public void savestdCrfEle1(CrfItemsStd si) {
		// TODO Auto-generated method stub
		getSession().save(si);
	}

	@Override
	public void savestdCrfEleVal(List<CRFItemValuesStd> ivlist) {
		// TODO Auto-generated method stub
		for (CRFItemValuesStd std : ivlist)
			getSession().save(std);
	}

	@Override
	public void savestdCrfGroups1(CRFGroupItemStd sg) {
		getSession().save(sg);
	}

	@Override
	public boolean saveCrf(CrfMetaData crf, DTNAME dt) {
		// TODO Auto-generated method stub
		try {
			getSession().save(dt);
			getSession().save(crf);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void saveCrf(Crf crf) {
		getSession().save(crf);
		for (CrfSection sec : crf.getSections()) {
			System.out.println(sec.getName());
			getSession().save(sec);
			for (CrfSectionElement ele : sec.getElement()) {
				ele.setSection(sec);
				getSession().save(ele);
				List<CrfSectionElementValue> elevl = ele.getElementValues();
				if (elevl != null)
					for (CrfSectionElementValue elev : elevl) {
						getSession().save(elev);
					}
			}
			CrfGroup group = sec.getGroup();
			if (group != null) {
				for (CrfGroupElement ele : group.getElement()) {
					ele.setGroup(group);
					getSession().save(ele);
					List<CrfGroupElementValue> elementValues = ele.getElementValues();
					if (elementValues != null)
						for (CrfGroupElementValue elev : elementValues) {
							getSession().save(elev);
						}
				}
				sec.setGroup(group);
			}
		}

	}

	@Override
	public Crf getCrf(Long crfId) {
		return getByKey(crfId);
	}

	@Override
	public void updateCrf(Crf crf) {
		// TODO Auto-generated method stub
		update(crf);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Crf> findAllStudyCrfsWithSubElements(StudyMaster sm) {
		List<Crf> stdCrfs = getSession().createCriteria(Crf.class).add(Restrictions.eq("std", sm))
				.add(Restrictions.eq("active", true)).add(Restrictions.eq("crfFrom", "Library")).list();
		for (Crf crf : stdCrfs) {
			Hibernate.initialize(crf.getSections());
			Collections.sort(crf.getSections());
			for (CrfSection sec : crf.getSections()) {
				Hibernate.initialize(sec.getElement());
				if (sec.getElement() == null || sec.getElement().size() == 0) {
				} else {
					for (CrfSectionElement ele : sec.getElement()) {
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
		}
		return stdCrfs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Crf> findAllCrfsWithSubElemens() {
		List<Crf> ml = getSession().createCriteria(Crf.class).list();
		for (Crf crf : ml) {
			Hibernate.initialize(crf.getSections());
			for (CrfSection sec : crf.getSections()) {
				Hibernate.initialize(sec.getElement());
				if (sec.getElement() == null || sec.getElement().size() == 0) {
					List<CrfSectionElement> element = getSession().createCriteria(CrfSectionElement.class)
							.add(Restrictions.eq("section", sec)).list();
					sec.setElement(element);
					for (CrfSectionElement ele : sec.getElement()) {
						Hibernate.initialize(ele.getElementValues());
						if (ele.getElementValues() == null || ele.getElementValues().size() == 0) {
							List<CrfSectionElementValue> v = getSession().createCriteria(CrfSectionElementValue.class)
									.add(Restrictions.eq("sectionElement", ele)).list();
							ele.setElementValues(v);
						}
					}
				} else {
					for (CrfSectionElement ele : sec.getElement()) {
						Hibernate.initialize(ele.getElementValues());
					}
				}
				Hibernate.initialize(sec.getGroup());
				CrfGroup group = sec.getGroup();
				if (group != null) {
					Hibernate.initialize(group.getElement());
					if (group.getElement() == null || group.getElement().size() == 0) {
						List<CrfGroupElement> geles = getSession().createCriteria(CrfGroupElement.class)
								.add(Restrictions.eq("group", group)).list();
						group.setElement(geles);
						for (CrfGroupElement ge : group.getElement()) {
							List<CrfGroupElementValue> elementValues = getSession()
									.createCriteria(CrfGroupElementValue.class).add(Restrictions.eq("groupElement", ge))
									.list();
							ge.setElementValues(elementValues);
						}
					} else {
						for (CrfGroupElement ele : group.getElement()) {
							Hibernate.initialize(ele.getElementValues());
						}
					}
				}
			}
		}

		return ml;
	}

	@Override
	public Crf studyCrf(Long crfId) {
		Crf crf = (Crf) getSession().get(Crf.class, crfId);
		return crf;
	}

	@Override
	public Crf getCrfForView(Long crfId) {
		Crf crf = (Crf) getSession().get(Crf.class, crfId);
		Hibernate.initialize(crf.getSections());
		Collections.sort(crf.getSections());
		try {
			for (CrfSection sec : crf.getSections()) {
				Hibernate.initialize(sec.getElement());
				if (sec.getElement() == null || sec.getElement().size() == 0) {
				} else {
					for (CrfSectionElement ele : sec.getElement()) {
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
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return crf;
	}

	@Override
	public Crf getCrfForDataEntryView(Long crfId) {
		Crf crf = (Crf) getSession().createCriteria(Crf.class).add(Restrictions.eq("id", crfId)).uniqueResult();
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

		return crf;
	}

	@Override
	public Crf getCrfForDataEntryView(Long crfId, StudyMaster sm, Long dataDatesId, StudyAnimal animal,
			String activityType, String scheduleType) {
		Crf crf = (Crf) getSession().createCriteria(Crf.class).add(Restrictions.eq("id", crfId)).uniqueResult();
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

		if (crf.getType().equalsIgnoreCase("IN-LIFE OBSERVATIONS")
				|| crf.getType().equalsIgnoreCase("TERMINAL OBSERVATIONS")) {
			if (crf.getSubType().equalsIgnoreCase("Clinical Observations")) {
				if (crf.getObservationName().equalsIgnoreCase("Daily Clinical Observations")) {
					for (CrfSection sec : crf.getSections()) {
						for (CrfSectionElement ele : sec.getElement()) {
							if (ele.getName().equalsIgnoreCase("day")) {
								if (activityType.equals("Treatment") && scheduleType.equals("scheduled")) {
									Date startDate = sm.getTreatmentStarDate();
									StudyTreatmentDataDates date = (StudyTreatmentDataDates) getSession()
											.get(StudyTreatmentDataDates.class, dataDatesId);
									// Convert java.util.Date to java.time.LocalDate
							        LocalDate localDate1 = startDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
							        LocalDate localDate2 = date.getAccDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

							        // Calculate the period between two LocalDate objects
							        Period period = Period.between(localDate1, localDate2);

							        // Extract the total number of days from the period
							        ele.setValues(period.getDays()+"");
									
								}else if (activityType.equals("Acclamatization") && scheduleType.equals("scheduled")) {
									Date startDate = sm.getAcclimatizationStarDate();
									StudyAcclamatizationDates date = (StudyAcclamatizationDates) getSession()
											.get(StudyAcclamatizationDates.class, dataDatesId);
									// Convert java.util.Date to java.time.LocalDate
							        LocalDate localDate1 = startDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
							        LocalDate localDate2 = date.getAccDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

							        // Calculate the period between two LocalDate objects
							        Period period = Period.between(localDate1, localDate2);

							        // Extract the total number of days from the period
							        ele.setValues(period.getDays()+"");
								}
								
							}
						}
					}
				}
			}
		}
		return crf;
	}

	@Override
	public CrfGroup studyGroupFull(Long groupId) {
		CrfGroup group = (CrfGroup) getSession().get(CrfGroup.class, groupId);
		Hibernate.initialize(group.getElement());
		for (CrfGroupElement ele : group.getElement()) {
			Hibernate.initialize(ele.getElementValues());
		}
		return group;
	}

	@Override
	public void saveStudySectionData1(List<CrfSectionElementData> sectionData) {
		for (CrfSectionElementData data : sectionData)
			getSession().save(data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveStudySectionData2(List<CrfSectionElementData> sectionData, SubjectDataEntryDetails sded) {
		getSession().save(sded);
		for (CrfSectionElementData data : sectionData) {
			getSession().save(data);
		}
	}

	@Override
	public void saveStudyGroupData(List<CrfGroupElementData> groupoData) {
		for (CrfGroupElementData data : groupoData)
			getSession().save(data);
	}

	@Override
	public void saveCrfGroupDataRows(List<CrfGroupDataRows> groupoInfo) {
		for (CrfGroupDataRows groupin : groupoInfo)
			getSession().save(groupin);
	}

	@Override
	public void updateVpc(VolunteerPeriodCrf vpc) {
		getSession().update(vpc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, CrfSectionElementData> studyCrfSectionElementDataList(VolunteerPeriodCrf vpc) {
		Map<String, CrfSectionElementData> map = new HashMap<>();
		List<CrfSectionElementData> list = getSession().createCriteria(CrfSectionElementData.class)
				.add(Restrictions.eq("volPeriodCrf", vpc)).list();
		for (CrfSectionElementData d : list) {
			Hibernate.initialize(d.getElement());
			map.put(d.getKayName(), d);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, CrfGroupElementData> studyCrfGroupElementDataList(VolunteerPeriodCrf vpc) {
		Map<String, CrfGroupElementData> map = new HashMap<>();
		List<CrfGroupElementData> list = getSession().createCriteria(CrfGroupElementData.class)
				.add(Restrictions.eq("volPeriodCrf", vpc)).list();
		for (CrfGroupElementData d : list) {
			Hibernate.initialize(d.getElement());
			map.put(d.getKayName(), d);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, CrfGroupDataRows> crfGroupDataRows(VolunteerPeriodCrf vpc) {
		// TODO Auto-generated method stub
		Map<Long, CrfGroupDataRows> map = new HashMap<>();
		List<CrfGroupDataRows> list = getSession().createCriteria(CrfGroupDataRows.class)
				.add(Restrictions.eq("volPeriodCrf", vpc)).list();
		for (CrfGroupDataRows d : list) {
			Hibernate.initialize(d.getGroup());
			map.put(d.getGroup().getId(), d);
		}
		return map;
	}

	@Override
	public CrfSectionElementData studyCrfSectionElementData(String keyName, VolunteerPeriodCrf vpc,
			CrfSectionElement ele) {
		CrfSectionElementData data = (CrfSectionElementData) getSession().createCriteria(CrfSectionElementData.class)
				.add(Restrictions.eq("volPeriodCrf", vpc)).add(Restrictions.eq("element", ele))
				.add(Restrictions.eq("kayName", keyName)).uniqueResult();
		return data;
	}

	@Override
	public CrfGroupElementData studyCrfGroupElementData(String keyName, VolunteerPeriodCrf vpc, CrfGroupElement ele) {
		CrfGroupElementData data = (CrfGroupElementData) getSession().createCriteria(CrfGroupElementData.class)
				.add(Restrictions.eq("volPeriodCrf", vpc)).add(Restrictions.eq("element", ele))
				.add(Restrictions.eq("kayName", keyName)).uniqueResult();
		return data;
	}

	@Override
	public CrfGroupDataRows studyCrfGroupDataRows(VolunteerPeriodCrf vpc, CrfGroup group) {
		CrfGroupDataRows row = (CrfGroupDataRows) getSession().createCriteria(CrfGroupDataRows.class)
				.add(Restrictions.eq("volPeriodCrf", vpc)).add(Restrictions.eq("group", group)).uniqueResult();
		return row;
	}

	@Override
	public void saveStudySectionDataUpdate(List<CrfSectionElementData> sectionDataUpdate) {
		for (CrfSectionElementData data : sectionDataUpdate)
			getSession().update(data);
	}

	@Override
	public void saveStudyGroupDataUpdate(List<CrfGroupElementData> groupoDataUpdate) {
		for (CrfGroupElementData data : groupoDataUpdate)
			getSession().update(data);
	}

	@Override
	public void saveCrfGroupDataRowsUpdate(List<CrfGroupDataRows> groupoInfoUpdate) {
		for (CrfGroupDataRows data : groupoInfoUpdate)
			getSession().update(data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfSectionElement> sectionElemets(Long id) {
		List<CrfSectionElement> list = new ArrayList<>();
		Crf crf = (Crf) getSession().get(Crf.class, id);
		Hibernate.initialize(crf.getSections());
		for (CrfSection sec : crf.getSections()) {
			List<CrfSectionElement> eles = getSession().createCriteria(CrfSectionElement.class)
					.add(Restrictions.eq("section", sec)).add(Restrictions.eq("typeOfTime", "weight")).list();
			if (eles.size() > 0)
				list.addAll(eles);
// 			Hibernate.initialize(sec.getElement());
//			list.addAll(sec.getElement());
		}
		return list;
	}

	@Override
	public List<CrfGroupElement> groupElemets(Long id) {
		List<CrfGroupElement> list = new ArrayList<CrfGroupElement>();
		Crf crf = (Crf) getSession().get(Crf.class, id);
		Hibernate.initialize(crf.getSections());
		for (CrfSection sec : crf.getSections()) {
			Hibernate.initialize(sec.getGroup());
			CrfGroup group = sec.getGroup();
			if (group != null) {
				Hibernate.initialize(group.getElement());
				if (group.getElement() != null || group.getElement().size() > 0) {
					list.addAll(group.getElement());
				}
			}
		}
		return list;
	}

	@Override
	public CrfSectionElement sectionElement(Long id) {
		return (CrfSectionElement) getSession().get(CrfSectionElement.class, id);
	}

	@Override
	public CrfGroupElement groupElement(Long id) {
		return (CrfGroupElement) getSession().get(CrfGroupElement.class, id);
	}

	@Override
	public String saveCrfRule(CrfRule rule) {
		try {
			getSession().save(rule);
			for (CrfRuleWithOther o : rule.getOtherCrf()) {
				getSession().save(o);
			}
			return rule.getName();
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfRule> findAllCrfRules() {
		List<CrfRule> list = getSession().createCriteria(CrfRule.class).list();
		for (CrfRule r : list) {
			Hibernate.initialize(r.getCrf());
			Hibernate.initialize(r.getSecEle());
			Hibernate.initialize(r.getGroupEle());
			Hibernate.initialize(r.getOtherCrf());
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfRule> crfRuleWithCrfAndSubElements(Crf crf) {
		List<CrfRule> rule = getSession().createCriteria(CrfRule.class).add(Restrictions.eq("crf", crf))
				.add(Restrictions.eq("active", true)).list();
		for (CrfRule r : rule) {
			Hibernate.initialize(r.getCrf());
			Hibernate.initialize(r.getSecEle());
			Hibernate.initialize(r.getGroupEle());
			Hibernate.initialize(r.getOtherCrf());
		}
		return rule;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfRule> crfRuleOfSubElements(Crf crf, List<Long> sectionIds) {
		System.out.println(sectionIds.size());
		if (sectionIds.size() > 0) {
			for (Long l : sectionIds) {
				System.out.println(l);
			}
			List<CrfRule> rule = getSession().createCriteria(CrfRule.class).add(Restrictions.eq("crf", crf))
					.add(Restrictions.in("secEle.id", sectionIds)).add(Restrictions.eq("active", true)).list();
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
	public CrfRule crfRule(Long id) {
		return (CrfRule) getSession().get(CrfRule.class, id);
	}

	@Override
	public void updateCrfRule(CrfRule rule) {
		getSession().update(rule);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Crf> findAllActiveCrfs() {
		List<Crf> list = getSession().createCriteria(Crf.class).add(Restrictions.eq("active", true)).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfRule> crfRuleWithCrf(Crf crf) {
		List<CrfRule> rule = getSession().createCriteria(CrfRule.class).add(Restrictions.eq("crf", crf))
				.add(Restrictions.eq("active", true)).list();
		return rule;
	}

	@Override
	public void saveCrfRule(List<CrfRule> stdcrfsRulesave) {
		for (CrfRule rule : stdcrfsRulesave) {
			getSession().save(rule);
			for (CrfRuleWithOther o : rule.getOtherCrf()) {
				getSession().save(o);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfRule> studyCrfRuleWithCrfAndSubElementsWithSecEleId(Long id, String type) {
		Criteria cri = getSession().createCriteria(CrfRule.class).add(Restrictions.eq("active", true));
		if (type.equals("section")) {
			cri.add(Restrictions.eq("secEle.id", id));
		} else
			cri.add(Restrictions.eq("groupEle.id", id));
		List<CrfRule> rule = cri.list();
		for (CrfRule r : rule) {
			Hibernate.initialize(r.getCrf());
			Hibernate.initialize(r.getSecEle());
			Hibernate.initialize(r.getGroupEle());
			Hibernate.initialize(r.getOtherCrf());
		}
		return rule;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfRule> studyCrfRuleWithCrfAndSubElementsWithGroupEleId(Long id) {
		List<CrfRule> rule = getSession().createCriteria(CrfRule.class).add(Restrictions.eq("groupEle.id", id)).list();
		for (CrfRule r : rule) {
			Hibernate.initialize(r.getCrf());
			Hibernate.initialize(r.getSecEle());
			Hibernate.initialize(r.getGroupEle());
			Hibernate.initialize(r.getOtherCrf());
		}
		return rule;
	}

	@Override
	public String studyCrfSectionElementData(VolunteerPeriodCrf vpc, CrfSectionElement secEle) {
		CrfSectionElementData data = (CrfSectionElementData) getSession().createCriteria(CrfSectionElementData.class)
				.add(Restrictions.eq("volPeriodCrf", vpc)).add(Restrictions.eq("element", secEle)).uniqueResult();
		if (data != null)
			return data.getValue();
		else
			return null;
	}

	@Override
	public String studyCrfGroupElementData(VolunteerPeriodCrf vpc, CrfGroupElement groupEle, String kayName) {
		CrfGroupElementData data = (CrfGroupElementData) getSession().createCriteria(CrfGroupElementData.class)
				.add(Restrictions.eq("volPeriodCrf", vpc)).add(Restrictions.eq("element", groupEle))
				.add(Restrictions.eq("kayName", kayName)).uniqueResult();
		if (data != null)
			return data.getValue();
		else
			return null;
	}

	@Override
	public CrfDescrpency studyCrfDescrpencySec(Crf crf, VolunteerPeriodCrf vpc, CrfSectionElementData data) {
		CrfDescrpency desc = (CrfDescrpency) getSession().createCriteria(CrfDescrpency.class)
				.add(Restrictions.eq("crf", crf)).add(Restrictions.eq("volPeriodCrf", vpc))
				.add(Restrictions.eq("secEleData", data)).add(Restrictions.eq("status", "open")).uniqueResult();
		return desc;
	}

	@Override
	public CrfDescrpency studyCrfDescrpencyGroup(Crf crf, VolunteerPeriodCrf vpc, CrfGroupElementData data) {
		CrfDescrpency desc = (CrfDescrpency) getSession().createCriteria(CrfDescrpency.class)
				.add(Restrictions.eq("crf", crf)).add(Restrictions.eq("volPeriodCrf", vpc))
				.add(Restrictions.eq("groupEleData", data)).add(Restrictions.eq("status", "open")).uniqueResult();
		return desc;
	}

	@Override
	public void saveStudyCrfDescrpencyList(List<CrfDescrpency> desc) {
		for (CrfDescrpency d : desc)
			getSession().save(d);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfDescrpency> userdiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc) {
		List<CrfDescrpency> desc = getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("crf", crf))
				.add(Restrictions.eq("volPeriodCrf", vpc)).add(Restrictions.eq("risedBy", "user")).list();
		return desc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfDescrpency> reviewerdiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc) {
		List<CrfDescrpency> desc = getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("crf", crf))
				.add(Restrictions.eq("volPeriodCrf", vpc)).add(Restrictions.eq("risedBy", "reviewer")).list();
		return desc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfDescrpency> closeddiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc) {
		List<CrfDescrpency> desc = getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("crf", crf))
				.add(Restrictions.eq("volPeriodCrf", vpc)).add(Restrictions.eq("status", "closed")).list();
		return desc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfDescrpency> onHolddiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc) {
		List<CrfDescrpency> desc = getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("crf", crf))
				.add(Restrictions.eq("volPeriodCrf", vpc)).add(Restrictions.eq("status", "onHold")).list();
		return desc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfDescrpency> alldiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc) {
		List<CrfDescrpency> desc = getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("crf", crf))
				.add(Restrictions.eq("volPeriodCrf", vpc)).list();
		return desc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfDescrpency> alldiscrepencyEleInfo(Crf crf, VolunteerPeriodCrf vpc, String keyName) {
		List<CrfDescrpency> desc = getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("crf", crf))
				.add(Restrictions.eq("volPeriodCrf", vpc)).add(Restrictions.eq("kayName", keyName)).list();
		return desc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfDescrpency> opendiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc) {
		List<CrfDescrpency> desc = getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("crf", crf))
				.add(Restrictions.eq("volPeriodCrf", vpc)).add(Restrictions.eq("status", "open")).list();
		return desc;
	}

	@Override
	public CrfSectionElement studyCrfSectionElement(Long id) {
		return (CrfSectionElement) getSession().get(CrfSectionElement.class, id);
	}

	@Override
	public CrfSectionElementData studyCrfSectionElementData(Long dataId) {
		CrfSectionElementData data = (CrfSectionElementData) getSession().get(CrfSectionElementData.class, dataId);
		return data;
	}

	@Override
	public boolean saveStudyCrfDescrpency(CrfDescrpency scd) {
		try {
			getSession().save(scd);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public CrfGroupElement studyCrfGroupElement(Long id) {
		return (CrfGroupElement) getSession().get(CrfGroupElement.class, id);
	}

	@Override
	public CrfGroupElementData studyCrfGroupElementData(Long dataId) {
		return (CrfGroupElementData) getSession().get(CrfGroupElementData.class, dataId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfDescrpency> alldiscrepencyEleInfo(Crf crf, VolunteerPeriodCrf vpc, String keyName,
			String condition) {
		List<CrfDescrpency> desc = null;
		if (condition.equals("user") || condition.equals("reviewer")) {
			desc = getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("crf", crf))
					.add(Restrictions.eq("volPeriodCrf", vpc)).add(Restrictions.eq("kayName", keyName))
					.add(Restrictions.eq("risedBy", condition)).list();
		} else if (condition.equals("onHold") || condition.equals("open") || condition.equals("closed")) {
			desc = getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("crf", crf))
					.add(Restrictions.eq("volPeriodCrf", vpc)).add(Restrictions.eq("kayName", keyName))
					.add(Restrictions.eq("status", condition)).list();
		} else if (condition.equals("all")) {
			desc = getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("crf", crf))
					.add(Restrictions.eq("volPeriodCrf", vpc)).add(Restrictions.eq("kayName", keyName)).list();
		}
		return desc;
	}

	@Override
	public void saveDataSet(DataSet ds) {
		getSession().save(ds);
		for (DataSetPhase phase : ds.getPhases()) {
			getSession().save(phase);
			for (DataSetPhasewiseCrfs eles : phase.getPhases())
				getSession().save(eles);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataSet> findAllDataSets() {
		List<DataSet> list = getSession().createCriteria(DataSet.class).list();
		for (DataSet ds : list) {
			Hibernate.initialize(ds.getPhases());
			for (DataSetPhase ph : ds.getPhases()) {
				Hibernate.initialize(ph.getPhases());
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataSet> findAllDataSets(StudyMaster sm) {
		List<DataSet> list = getSession().createCriteria(DataSet.class).add(Restrictions.eq("study", sm)).list();
		for (DataSet ds : list) {
			Hibernate.initialize(ds.getPhases());
			for (DataSetPhase ph : ds.getPhases()) {
				Hibernate.initialize(ph.getPhases());
			}
		}
		return list;
	}

	@Override
	public DataSet dataSet(Long id) {
		return (DataSet) getSession().get(DataSet.class, id);
	}

	@Override
	public void updateDataSet(DataSet dataSet) {
		getSession().update(dataSet);
	}

	@Override
	public DataSet fullDataSetInfo(Long id) {
		DataSet ds = (DataSet) getSession().get(DataSet.class, id);
		Hibernate.initialize(ds.getPhases());
		for (DataSetPhase dsp : ds.getPhases())
			Hibernate.initialize(dsp.getPhases());
		return ds;
	}

	@Override
	public VolunteerPeriodCrf volunteerPeriodCrf(DataSetPhasewiseCrfs crf, StudyPeriodMaster period, Volunteer v) {
		PeriodCrfs pc = (PeriodCrfs) getSession().createCriteria(PeriodCrfs.class)
				.add(Restrictions.eq("period", period)).add(Restrictions.eq("crfId", crf.getCrf().getId()))
				.uniqueResult();
		VolunteerPeriodCrf vpc = (VolunteerPeriodCrf) getSession().createCriteria(VolunteerPeriodCrf.class)
				.add(Restrictions.eq("stdCrf", pc)).add(Restrictions.eq("period", period))
				.add(Restrictions.eq("vol", v)).uniqueResult();
		return vpc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CrfSectionElementData studyCrfSectionElementData2(VolunteerPeriodCrf vcp, CrfSectionElement ele) {
		List<CrfSectionElementData> data = getSession().createCriteria(CrfSectionElementData.class)
				.add(Restrictions.eq("volPeriodCrf", vcp)).add(Restrictions.eq("element", ele)).list();
		if (data != null && data.size() > 0)
			return data.get(0);
		else
			return null;
	}

	@Override
	public CrfGroupElementData studyCrfSectionElementData(VolunteerPeriodCrf vcp, CrfGroupElement ele, String keyName) {
		try {
			CrfGroupElementData data = (CrfGroupElementData) getSession().createCriteria(CrfGroupElementData.class)
					.add(Restrictions.eq("volPeriodCrf", vcp)).add(Restrictions.eq("element", ele))
					.add(Restrictions.eq("kayName", keyName)).uniqueResult();
			return data;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Crf crfByName(String value) {
		Crf crf = (Crf) getSession().createCriteria(Crf.class).add(Restrictions.eq("name", value))
				.add(Restrictions.eq("active", true)).uniqueResult();
		return crf;
	}

	@Override
	public void saveCrfEleCaliculationList(List<CrfEleCaliculation> list) {
		for (CrfEleCaliculation cec : list) {
			getSession().save(cec);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfEleCaliculation> crfEleCaliculationList() {
		return getSession().createCriteria(CrfEleCaliculation.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfEleCaliculation> libcrfEleCaliculationList(Crf crf) {
		List<CrfEleCaliculation> elecal = getSession().createCriteria(CrfEleCaliculation.class)
				.add(Restrictions.eq("crf", crf)).add(Restrictions.eq("status", "active")).list();
		return elecal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfEleCaliculation> crfEleCaliculationList(Crf crf) {
		List<CrfEleCaliculation> elecal = getSession().createCriteria(CrfEleCaliculation.class)
				.add(Restrictions.eq("crf", crf)).add(Restrictions.eq("status", "active")).list();
		return elecal;
	}

	@Override
	public CrfEleCaliculation studyCrfEleCaliculation(StudyMaster sm, Long crfId, String resultFiled) {
		Crf crf = (Crf) getSession().get(Crf.class, crfId);
		CrfEleCaliculation scec = (CrfEleCaliculation) getSession().createCriteria(CrfEleCaliculation.class)
				.add(Restrictions.eq("crf", crf)).add(Restrictions.eq("resultField", resultFiled))
				.add(Restrictions.eq("status", "active")).uniqueResult();
		return scec;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfDescrpency> allOpendStudydiscrepency(StudyMaster sm, String username) {
		List<CrfDescrpency> list = new ArrayList<>();
		List<CrfDescrpency> l = getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("study", sm))
//				.add(Restrictions.ne("status", "closed"))
				.add(Restrictions.or(Restrictions.eq("assingnedTo", username), Restrictions.eq("assingnedTo", "all")))
				.list();
		if (l.size() > 0) {
			for (CrfDescrpency cd : l) {
				Hibernate.initialize(cd.getStdSubGroupObservationCrfs());
				Hibernate.initialize(cd.getSubGroupAnimalsInfo());
				Hibernate.initialize(cd.getDataCount());
				Hibernate.initialize(cd.getCrf());
				Hibernate.initialize(cd.getVolPeriodCrf());
				Hibernate.initialize(cd.getSecEleData());
				Hibernate.initialize(cd.getSecEleData().getSubjectDataEntryDetails().getAnimal());
				Hibernate.initialize(cd.getSecElement());
				Hibernate.initialize(cd.getGroupElement());
				Hibernate.initialize(cd.getGroupEleData());
				Hibernate.initialize(cd.getCrfRule());
			}
			list.addAll(l);
		}
		return list;
	}

	@Override
	public List<StudyAccessionCrfDescrpency> allStudyAccessionCrfDescrpency(StudyMaster sm, String username) {
		List<StudyAccessionCrfDescrpency> list = new ArrayList<>();
		List<StudyAccessionCrfDescrpency> l = getSession().createCriteria(StudyAccessionCrfDescrpency.class)
				.add(Restrictions.eq("study", sm))
//				.add(Restrictions.ne("status", "closed"))
				.add(Restrictions.or(Restrictions.eq("assingnedTo", username), Restrictions.eq("assingnedTo", "all")))
				.list();
		if (l != null && l.size() > 0) {
			list.addAll(l);
		}
		return list;
	}

	@Override
	public StudyAccessionCrfDescrpency studyAccessionCrfDescrpency(Long id) {
		// TODO Auto-generated method stub
		return (StudyAccessionCrfDescrpency) getSession().createCriteria(StudyAccessionCrfDescrpency.class)
				.add(Restrictions.eq("id", id)).uniqueResult();
	}

	@Override
	public CrfDescrpency studyCrfDescrpency(Long id) {
		CrfDescrpency d = (CrfDescrpency) getSession().createCriteria(CrfDescrpency.class)
				.add(Restrictions.eq("id", id)).uniqueResult();
		if (d != null) {
			Hibernate.initialize(d.getStdSubGroupObservationCrfs());
			Hibernate.initialize(d.getSubGroupAnimalsInfo());
			Hibernate.initialize(d.getDataCount());
			Hibernate.initialize(d.getCrf());
			Hibernate.initialize(d.getSecElement());
			Hibernate.initialize(d.getSecEleData());
			Hibernate.initialize(d.getGroupElement());
			Hibernate.initialize(d.getGroupEleData());
			Hibernate.initialize(d.getCrfRule());
		}
		return d;
	}

	@Override
	public void updateStudyCrfDescrpency(CrfDescrpency scd) {
		getSession().update(scd);
		if (scd.getTypeOfElemet().equals("section")) {
			System.out.println(scd.getSecEleData().getId());
			System.out.println(scd.getSecEleData().getValue());
			getSession().update(scd.getSecEleData());
		} else if (scd.getTypeOfElemet().equals("group")) {
			getSession().update(scd.getGroupEleData());
		}
	}

	@Override
	public CrfGroup getCrfGroup(Long id) {
		CrfGroup group = (CrfGroup) getSession().createCriteria(CrfGroup.class)
				.add(Restrictions.eq("sectionId", id.intValue())).uniqueResult();
		Hibernate.initialize(group.getElement());
		List<CrfGroupElement> element = group.getElement();
		for (CrfGroupElement g : element)
			Hibernate.initialize(g.getElementValues());

		return group;
	}

	@Override
	public void saveCrfDateComparison(CrfDateComparison data) {
		getSession().save(data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfDateComparison> crfDateComparisonlist() {
		List<CrfDateComparison> rule = getSession().createCriteria(CrfDateComparison.class).list();
		return rule;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfDateComparison> CrfDateComparisonAndSubElements(Crf crf) {
		List<CrfDateComparison> rule = getSession().createCriteria(CrfDateComparison.class)
				.add(Restrictions.eq("sourcrf", crf)).add(Restrictions.eq("status", "active")).list();
		for (CrfDateComparison r : rule) {
			Hibernate.initialize(r.getSecEle());
			Hibernate.initialize(r.getSecEle1());
			Hibernate.initialize(r.getSecEle2());
			Hibernate.initialize(r.getSecEle3());
			Hibernate.initialize(r.getSecEle4());
			Hibernate.initialize(r.getGroupEle());
			Hibernate.initialize(r.getGroupEle1());
			Hibernate.initialize(r.getGroupEle2());
			Hibernate.initialize(r.getGroupEle3());
			Hibernate.initialize(r.getGroupEle4());
			Hibernate.initialize(r.getCrf1());
			Hibernate.initialize(r.getCrf2());
			Hibernate.initialize(r.getCrf3());
			Hibernate.initialize(r.getCrf4());
		}
		return rule;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfDateComparison> studyCrfDateComparisonAndSubElements(Crf crf) {
		List<CrfDateComparison> rule = getSession().createCriteria(CrfDateComparison.class)
				.add(Restrictions.eq("sourcrf", crf)).add(Restrictions.eq("status", "active")).list();
		for (CrfDateComparison r : rule) {
			Hibernate.initialize(r.getSecEle());
			Hibernate.initialize(r.getSecEle1());
			Hibernate.initialize(r.getSecEle2());
			Hibernate.initialize(r.getSecEle3());
			Hibernate.initialize(r.getSecEle4());
			Hibernate.initialize(r.getGroupEle());
			Hibernate.initialize(r.getGroupEle1());
			Hibernate.initialize(r.getGroupEle2());
			Hibernate.initialize(r.getGroupEle3());
			Hibernate.initialize(r.getGroupEle4());
			Hibernate.initialize(r.getCrf1());
			Hibernate.initialize(r.getCrf2());
			Hibernate.initialize(r.getCrf3());
			Hibernate.initialize(r.getCrf4());
		}
		return rule;
	}

	@Override
	public CrfDateComparison byCrfDateComparisonAndSubElementsById(Long dateRuleDbId) {
		CrfDateComparison r = (CrfDateComparison) getSession().get(CrfDateComparison.class, dateRuleDbId);
		Hibernate.initialize(r.getSecEle());
		Hibernate.initialize(r.getSecEle1());
		Hibernate.initialize(r.getSecEle2());
		Hibernate.initialize(r.getSecEle3());
		Hibernate.initialize(r.getSecEle4());
		Hibernate.initialize(r.getGroupEle());
		Hibernate.initialize(r.getGroupEle1());
		Hibernate.initialize(r.getGroupEle2());
		Hibernate.initialize(r.getGroupEle3());
		Hibernate.initialize(r.getGroupEle4());
		Hibernate.initialize(r.getCrf1());
		Hibernate.initialize(r.getCrf2());
		Hibernate.initialize(r.getCrf3());
		Hibernate.initialize(r.getCrf4());
		return r;
	}

	@Override
	public Crf getStudyCrf(Long crfId) {
		return (Crf) getSession().get(Crf.class, crfId);
	}

	@Override
	public void updateStudyCrf(Crf crf) {
		getSession().update(crf);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfEleCaliculation> studyCrfEleCaliculation(Long studyId) {
		return getSession().createCriteria(CrfEleCaliculation.class).add(Restrictions.eq("crfFrom", "Study"))
				.add(Restrictions.eq("studyId", studyId)).list();
	}

	@Override
	public Crf studycrfByName(StudyMaster sm, String value) {
		Crf crf = (Crf) getSession().createCriteria(Crf.class).add(Restrictions.eq("std", sm))
				.add(Restrictions.eq("name", value)).add(Restrictions.eq("active", true)).uniqueResult();
		return crf;
	}

	@Override
	public CrfEleCaliculation getCrfEleCaliculation(Long crfId) {
		return (CrfEleCaliculation) getSession().get(CrfEleCaliculation.class, crfId);
	}

	@Override
	public void updateCrfEleCaliculationStatus(CrfEleCaliculation crf) {
		getSession().update(crf);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfRule> studyCrfRuleWithCrfAndSubElements(StudyMaster sm) {
		List<CrfRule> rule = getSession().createCriteria(CrfRule.class).add(Restrictions.eq("study", sm))
				.add(Restrictions.eq("crfFrom", "Study")).list();
		for (CrfRule r : rule) {
			Hibernate.initialize(r.getCrf());
			Hibernate.initialize(r.getSecEle());
			Hibernate.initialize(r.getGroupEle());
			Hibernate.initialize(r.getOtherCrf());
		}
		return rule;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Crf> findAllStudyCrfsForRules(StudyMaster sm) {
		return getSession().createCriteria(Crf.class).add(Restrictions.eq("std", sm))
				.add(Restrictions.eq("active", true)).list();
	}

	@Override
	public List<CrfSectionElement> studysectionElemets(Long id) {
		List<CrfSectionElement> list = new ArrayList<>();
		Crf crf = (Crf) getSession().get(Crf.class, id);
		Hibernate.initialize(crf.getSections());
		for (CrfSection sec : crf.getSections()) {
			Hibernate.initialize(sec.getElement());
			list.addAll(sec.getElement());
		}
		return list;
	}

	@Override
	public List<CrfGroupElement> studygroupElemets(Long id) {
		List<CrfGroupElement> list = new ArrayList<CrfGroupElement>();
		Crf crf = (Crf) getSession().get(Crf.class, id);
		Hibernate.initialize(crf.getSections());
		for (CrfSection sec : crf.getSections()) {
			Hibernate.initialize(sec.getGroup());
			CrfGroup group = sec.getGroup();
			if (group != null) {
				Hibernate.initialize(group.getElement());
				if (group.getElement() != null || group.getElement().size() > 0) {
					list.addAll(group.getElement());
				}
			}
		}
		return list;
	}

	@Override
	public CrfRule studycrfRule(Long id) {
		return (CrfRule) getSession().get(CrfRule.class, id);
	}

	@Override
	public CrfDateComparison crfDateComparison(Long id) {
		return (CrfDateComparison) getSession().get(CrfDateComparison.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfDateComparison> studycrfDateComparisonlist() {
		List<CrfDateComparison> rule = getSession().createCriteria(CrfDateComparison.class).list();
		return rule;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfDateComparison> studycrfDateComparisonlistall(StudyMaster sm) {
		List<CrfDateComparison> rule = getSession().createCriteria(CrfDateComparison.class)
				.add(Restrictions.eq("sm", sm)).list();
		return rule;
	}

	@Override
	public CrfDateComparison studycrfDateComparison(Long id) {
		return (CrfDateComparison) getSession().get(CrfDateComparison.class, id);
	}

	@Override
	public void updateCrfDateComparison(CrfDateComparison rule) {
		getSession().update(rule);
	}

	@Override
	public Crf crfForView(Long crfId) {
		Crf crf = (Crf) getSession().get(Crf.class, crfId);
		Hibernate.initialize(crf.getSections());
		Collections.sort(crf.getSections());
		for (CrfSection sec : crf.getSections()) {
			Hibernate.initialize(sec.getElement());
			if (sec.getElement() == null || sec.getElement().size() == 0) {
			} else {
				for (CrfSectionElement ele : sec.getElement()) {
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

		return crf;
	}

	@Override
	public void saveStudyCrfPeriodCrfsList(List<Crf> crfs, StudyMaster sm) {
		for (Crf crf : crfs) {
			getSession().save(crf);
			for (CrfSection sec : crf.getSections()) {
				System.out.println(sec.getName());
				getSession().save(sec);
				for (CrfSectionElement ele : sec.getElement()) {
					ele.setSection(sec);
					getSession().save(ele);
					List<CrfSectionElementValue> elevl = ele.getElementValues();
					if (elevl != null)
						for (CrfSectionElementValue elev : elevl) {
							getSession().save(elev);
						}
				}
				CrfGroup group = sec.getGroup();
				if (group != null) {
					for (CrfGroupElement ele : group.getElement()) {
						ele.setGroup(group);
						getSession().save(ele);
						List<CrfGroupElementValue> elementValues = ele.getElementValues();
						if (elementValues != null)
							for (CrfGroupElementValue elev : elementValues) {
								getSession().save(elev);
							}
					}
					sec.setGroup(group);
				}
			}
		}

		@SuppressWarnings("unchecked")
		List<StudyPeriodMaster> splist = getSession().createCriteria(StudyPeriodMaster.class)
				.add(Restrictions.eq("studyMaster", sm)).list();
		Collections.sort(splist);

		List<PeriodCrfs> pclist = new ArrayList<>();
		for (StudyPeriodMaster sp : splist) {
			if (sp.getType().equals("Period")) {
				for (Crf crf : crfs) {
					PeriodCrfs pc = new PeriodCrfs();
					pc.setPeriod(sp);
					pc.setPeriodId(sp.getId());
					pc.setCrfName(crf.getName());
					pc.setCrfId(crf.getId());
					getSession().save(pc);
					pclist.add(pc);
				}
			}
		}
	}

	@Override
	public void saveUpdateCrfData(List<CrfSectionElementData> sectionData,
			List<CrfSectionElementData> sectionDataUpdate, VolunteerPeriodCrf vpc) {
		for (CrfSectionElementData data : sectionData)
			getSession().save(data);
		for (CrfSectionElementData data : sectionDataUpdate)
			getSession().update(data);
		getSession().update(vpc);
	}

	@Override
	public void saveCrfSectionElementDataAuditList(List<CrfSectionElementDataAudit> audit) {
		for (CrfSectionElementDataAudit studyCrfSectionElementDataAudit : audit) {
			getSession().save(studyCrfSectionElementDataAudit);
		}

	}

	@Override
	public void saveCrfSectionElementDataAudit(CrfSectionElementDataAudit saudit) {
		// TODO Auto-generated method stub
		getSession().save(saudit);
	}

	@Override
	public void saveCrfDescrpencyAudit(CrfDescrpencyAudit descAudit) {
		getSession().save(descAudit);
	}

	@Override
	public Crf checkStudyCrf(String name, Long stdId) {
		StudyMaster sm = (StudyMaster) getSession().get(StudyMaster.class, stdId);
		return (Crf) getSession().createCriteria(Crf.class).add(Restrictions.eq("std", sm))
				.add(Restrictions.eq("name", name)).uniqueResult();
	}

	@Override
	public Crf checkCrf(String name) {
		return (Crf) getSession().createCriteria(Crf.class).add(Restrictions.eq("name", name))
				.add(Restrictions.eq("active", true)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Crf> crfsByName(String name) {
		return getSession().createCriteria(Crf.class).add(Restrictions.eq("name", name)).list();
	}

	@Override
	public List<StdSubGroupObservationCrfs> findAllStdSubGroupObservationCrfs(StudyMaster sm, SubGroupInfo subGroup) {
		return getSession().createCriteria(StdSubGroupObservationCrfs.class).add(Restrictions.eq("study", sm))
				.add(Restrictions.eq("subGroupInfo", subGroup)).list();
	}

	@Override
	public void updateStdSubGroupObservationCrfsList(List<StdSubGroupObservationCrfs> stdcrfsUpdate,
			List<StdSubGroupObservationCrfsLog> sgblog) {
		for (StdSubGroupObservationCrfsLog scrflog : sgblog)
			getSession().save(scrflog);
		for (StdSubGroupObservationCrfs scrf : stdcrfsUpdate)
			getSession().update(scrf);
	}

	@Override
	public void saveStdSubGroupObservationCrfsList(List<StdSubGroupObservationCrfs> updates,
			List<StdSubGroupObservationCrfs> stdcrfssave, StudyMaster sm, String userName, StudyDesignStatus sds,
			List<StdSubGroupObservationCrfsLog> logs) {
		for (StdSubGroupObservationCrfs scrf : updates) {
			getSession().merge(scrf);
		}
		for (StdSubGroupObservationCrfs scrf : stdcrfssave) {
			getSession().save(scrf);
			if (sm.getStatus().getStatusCode().equals(StatusMasterCodes.SI.toString())) {
				AmendmentDetails ad = new AmendmentDetails();
				ad.setAction("Observerion Added");
				ad.setCrf(scrf.getCrf());
				ad.setCreatedBy(userName);
				ad.setCreatedOn(new Date());
				ad.setStatus("Open");
				ad.setStudyId(sm.getId());
				ad.setStudyName(sm.getStudyNo());
				ad.setObvId(scrf);
				getSession().save(ad);
			}
		}
	}

	@Override
	public List<StdSubGroupObservationCrfs> findAllStdSubGroupObservationCrfs(StudyMaster sm, Long subGroupId) {
		List<StdSubGroupObservationCrfs> stdCrfs = getSession().createCriteria(StdSubGroupObservationCrfs.class)
				.add(Restrictions.eq("study", sm)).add(Restrictions.eq("subGroupInfo.id", subGroupId)).list();
		return stdCrfs;
	}

	@Override
	public List<StdSubGroupObservationCrfs> findAllActiveStdSubGroupObservationCrfs(StudyMaster sm, Long subGroupId) {
		List<StdSubGroupObservationCrfs> stdCrfs = getSession().createCriteria(StdSubGroupObservationCrfs.class)
//				.add(Restrictions.eq("study", sm))
				.add(Restrictions.eq("subGroupInfo.id", subGroupId)).add(Restrictions.eq("active", true)).list();
		return stdCrfs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StdSubGroupObservationCrfs stdSubGroupObservationCrfsWithCrf(Long crfId) {
		StdSubGroupObservationCrfs stdcrf = (StdSubGroupObservationCrfs) getSession()
				.get(StdSubGroupObservationCrfs.class, crfId);
//		Crf crf = stdcrf.getCrf();
//		Crf crf = (Crf) getSession().get(Crf.class, crfId);
		Hibernate.initialize(stdcrf.getCrf().getSections());
		Hibernate.initialize(stdcrf.getSubGroupInfo());
		Hibernate.initialize(stdcrf.getSubGroupInfo().getGroup());
		Collections.sort(stdcrf.getCrf().getSections());
		for (CrfSection sec : stdcrf.getCrf().getSections()) {
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
		return stdcrf;
	}

	private CrfMapplingTableColumnMap crfMapplingTableColumnMap(CrfSectionElement ele) {
		CrfMapplingTableColumnMap map = (CrfMapplingTableColumnMap) getSession()
				.createCriteria(CrfMapplingTableColumnMap.class).add(Restrictions.eq("element", ele)).uniqueResult();
		return map;
	}

	@Override
	public List<CrfRule> crfRuleWithCrfAndSubElementsWith(Long elementId, Long crfId, boolean group) {
		Criteria cri = getSession().createCriteria(CrfRule.class).add(Restrictions.eq("crf.id", crfId))
				.add(Restrictions.eq("active", true));
		if (group)
			cri.add(Restrictions.eq("groupEle.id", elementId));
		else
			cri.add(Restrictions.eq("secEle.id", elementId));
		List<CrfRule> rule = cri.list();
		for (CrfRule r : rule) {
			Hibernate.initialize(r.getCrf());
			Hibernate.initialize(r.getSecEle());
			Hibernate.initialize(r.getGroupEle());
			Hibernate.initialize(r.getOtherCrf());
		}
		return rule;
	}

	@Override
	public String saveCrfRecord(Crf crf, List<Long> roleIds) {
		String result = "Failed";
		long crfId = 0;
		try {
			crfId = (long) getSession().save(crf);
			for (CrfSection sec : crf.getSections()) {
				System.out.println(sec.getName());
				getSession().save(sec);
				for (CrfSectionElement ele : sec.getElement()) {
					ele.setSection(sec);
					getSession().save(ele);
					List<CrfSectionElementValue> elevl = ele.getElementValues();
					if (elevl != null)
						for (CrfSectionElementValue elev : elevl) {
							getSession().save(elev);
						}
				}
				CrfGroup group = sec.getGroup();
				if (group != null) {
					for (CrfGroupElement ele : group.getElement()) {
						ele.setGroup(group);
						getSession().save(ele);
						List<CrfGroupElementValue> elementValues = ele.getElementValues();
						if (elementValues != null)
							for (CrfGroupElementValue elev : elementValues) {
								getSession().save(elev);
							}
					}
					sec.setGroup(group);
				}
			}
			if (crfId > 0) {
				List<RoleMaster> roles = roleDao.rolesByIds(roleIds);
				for (RoleMaster role : roles) {
					ObservationRole obRole = new ObservationRole(crf, role, crf.getCreatedBy());
					getSession().save(obRole);
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
	public List<CrfDescrpency> alldiscrepencyOfElement(CrfSectionElementData data) {
		List<CrfDescrpency> desc = getSession().createCriteria(CrfDescrpency.class)
				.add(Restrictions.eq("secEleData", data)).list();
		return desc;
	}

	public SubGroupAnimalsInfoAll subGroupAnimalsInfoAll(Long subGroupInfoId) {
		SubGroupAnimalsInfoAll sgai = (SubGroupAnimalsInfoAll) getSession().get(SubGroupAnimalsInfoAll.class,
				subGroupInfoId);

		Hibernate.initialize(sgai.getSubGroup());
		Hibernate.initialize(sgai.getSubGroup().getGroup());
		Hibernate.initialize(sgai.getSubGroupAnimalsInfo());
		Hibernate.initialize(sgai.getSubGroup().getStudy());
		return sgai;
	}

	public Crf studyCrf(StudyMaster sm, Long subGroupIdd, Long stdSubGroupObservationCrfsId) {
		StdSubGroupObservationCrfs stdSubGroupObsCrfs = (StdSubGroupObservationCrfs) getSession()
				.get(StdSubGroupObservationCrfs.class, stdSubGroupObservationCrfsId);
		Crf crf = stdSubGroupObsCrfs.getCrf();
		Hibernate.initialize(crf.getSections());
//		Hibernate.initialize(crf.getSubGroupInfo());
//		Hibernate.initialize(crf.getSubGroupInfo().getGroup());
		Collections.sort(crf.getSections());
		for (CrfSection sec : crf.getSections()) {
			Hibernate.initialize(sec.getElement());
			if (sec.getElement() == null || sec.getElement().size() == 0) {
			} else {
				for (CrfSectionElement ele : sec.getElement()) {
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
		return crf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StdSubGroupObservationCrfs saveRevewObsevation(LoginUsers user, Long subGroupInfoId,
			Long stdSubGroupObservationCrfsId, Integer descElementAll, List<Long> dataIds) {
		try {

			SubGroupAnimalsInfoAll subGroupAnimalsInfoAll = subGroupAnimalsInfoAll(subGroupInfoId);
			SubGroupAnimalsInfo subGroupAnimalsInfo = subGroupAnimalsInfoAll.getSubGroupAnimalsInfo();
//			Crf crf = studyCrf(subGroupAnimalsInfo.getStudy(), subGroupAnimalsInfo.getSubGroup().getId(), stdSubGroupObservationCrfsId);
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs = (StdSubGroupObservationCrfs) getSession()
					.get(StdSubGroupObservationCrfs.class, stdSubGroupObservationCrfsId);
			ObservationReviewLevel orl = null;
			try {
				orl = (ObservationReviewLevel) getSession().createCriteria(ObservationReviewLevel.class)
						.add(Restrictions.eq("subGroupObservationCrf", stdSubGroupObservationCrfs))
						.add(Restrictions.eq("user", user)).uniqueResult();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (orl == null) {
				orl = new ObservationReviewLevel();
				orl.setSubGroupObservationCrf(stdSubGroupObservationCrfs);
				orl.setStudy(subGroupAnimalsInfo.getStudy());
				orl.setCrf(stdSubGroupObservationCrfs.getCrf());
				orl.setGroup(subGroupAnimalsInfo.getGroup());
				orl.setSubGroup(stdSubGroupObservationCrfs.getSubGroupInfo());
				orl.setSubGroupAnimalsInfo(subGroupAnimalsInfo);
				orl.setSubGroupAnimalsInfoAll(subGroupAnimalsInfoAll);
				orl.setUser(user);
				if (user.getReviewLevel() != 100) {
					orl.setUserLevel(user.getReviewLevel());
				} else {
					int max = 0;
					try {
						max = (int) getSession().createCriteria(ObservationReviewLevel.class)
								.add(Restrictions.eq("subGroupObservationCrf.id", stdSubGroupObservationCrfs))
								.setProjection(Projections.max("userLevel")).uniqueResult();
					} catch (Exception e) {
						// TODO: handle exception
					}
					orl.setUserLevel(++max);
				}
				getSession().save(orl);
			}

			List<SubGroupAnimalsInfoCrfDataCount> crfData = null;
			if (dataIds.size() > 0) {
				crfData = getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
						.add(Restrictions.in("id", dataIds)).list();
			}
			for (SubGroupAnimalsInfoCrfDataCount s : crfData) {
				SubGroupAnimalsInfoCrfDataCountReviewLevel sl = null;
				try {
					sl = (SubGroupAnimalsInfoCrfDataCountReviewLevel) getSession()
							.createCriteria(SubGroupAnimalsInfoCrfDataCountReviewLevel.class)
							.add(Restrictions.eq("dataCount", s)).add(Restrictions.eq("user", user)).uniqueResult();
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (sl == null) {
					sl = new SubGroupAnimalsInfoCrfDataCountReviewLevel();
					sl.setSubGroupObservationCrf(stdSubGroupObservationCrfs);
					sl.setStudy(subGroupAnimalsInfo.getStudy());
					sl.setCrf(stdSubGroupObservationCrfs.getCrf());
					sl.setGroup(subGroupAnimalsInfo.getGroup());
					sl.setSubGroup(stdSubGroupObservationCrfs.getSubGroupInfo());
					sl.setSubGroupAnimalsInfo(subGroupAnimalsInfo);
					sl.setSubGroupAnimalsInfoAll(subGroupAnimalsInfoAll);
					sl.setUser(user);
					sl.setDataCount(s);
					sl.setObsLevel(orl);
					sl.setUserLevel(orl.getUserLevel());
					getSession().save(sl);
				}
//				Long size = (Long) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
//						.add(Restrictions.eq("crf", stdSubGroupObservationCrfs))
//						.add(Restrictions.eq("subGroupAnimalsInfoAll", subGroupAnimalsInfoAll))
//						.setProjection(Projections.rowCount()).uniqueResult();
				Long size2 = (Long) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCountReviewLevel.class)
						.add(Restrictions.eq("subGroupObservationCrf", stdSubGroupObservationCrfs))
						.add(Restrictions.eq("dataCount", s))
//						.add(Restrictions.eq("user", user))
						.setProjection(Projections.rowCount()).uniqueResult();
//				if(size == size2) {

				ReviewLevel rl = (ReviewLevel) getSession().createCriteria(ReviewLevel.class)
						.add(Restrictions.eq("status", "Active")).uniqueResult();
				if (rl != null) {
					if (rl.getObservationApprovelLevel() == size2) {
//							if(orl.getUserLevel() == size2) {
						stdSubGroupObservationCrfs.setReviewStatus(true);
						orl.setReviewFlag(false);
						s.setCrfStatus("REVIEWED");
					}
				}
//				}
			}
//			Long size = (Long) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
//					.add(Restrictions.eq("crf", stdSubGroupObservationCrfs))
//					.add(Restrictions.eq("subGroupAnimalsInfoAll", subGroupAnimalsInfoAll))
//					.setProjection(Projections.rowCount()).uniqueResult();
//			Long size2 = (Long) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCountReviewLevel.class)
//					.add(Restrictions.eq("subGroupObservationCrf", stdSubGroupObservationCrfs))
//					.add(Restrictions.eq("user", user))
//					.setProjection(Projections.rowCount()).uniqueResult();
//			if(size == size2) {
//				orl.setReviewFlag(false);
//				ReviewLevel rl =  (ReviewLevel) getSession().createCriteria(ReviewLevel.class)
//						.add(Restrictions.eq("status", "Active")).uniqueResult();
//				if(orl.getUserLevel() == rl.getObservationApprovelLevel()) {
//					stdSubGroupObservationCrfs.setReviewStatus(true);
//					for(SubGroupAnimalsInfoCrfDataCount s : crfData) {
//						s.setCrfStatus("REVIEWED");
//					}
//				}				
//			}

			return stdSubGroupObservationCrfs;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<SubGroupAnimalsInfoAll> subGroupAnimalsInfoInStudy(Long stdId) {

		List<Long> ids = getSession().createCriteria(SubGroupAnimalsInfo.class).add(Restrictions.eq("study.id", stdId))
				.setProjection(Projections.property("id")).list();
		if (ids.size() > 0) {
			return getSession().createCriteria(SubGroupAnimalsInfoAll.class)
					.add(Restrictions.in("subGroupAnimalsInfo.id", ids)).list();
		} else
			return new ArrayList<>();
	}

	@Override
	public List<StdSubGroupObservationCrfs> stdSubGroupObservationCrfsWithAnimal(Long animalId) {
		List<StdSubGroupObservationCrfs> crfs = getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
				.add(Restrictions.eq("subGroupAnimalsInfoAll.id", animalId)).setProjection(Projections.property("crf"))
				.list();
		return crfs;
	}

	@Override
	public ReviewLevel reviewLevel() {
		// TODO Auto-generated method stub
		return (ReviewLevel) getSession().createCriteria(ReviewLevel.class).add(Restrictions.eq("status", "Active"))
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StdSubGroupObservationCrfs> findAllActiveStdSubGroupObservationCrfs(SubGroupInfo sg) {
		List<StdSubGroupObservationCrfs> crfs = getSession().createCriteria(StdSubGroupObservationCrfs.class)
				.add(Restrictions.eq("subGroupInfo", sg)).add(Restrictions.eq("active", true)).list();
		for (StdSubGroupObservationCrfs obj : crfs) {
			Hibernate.initialize(obj.getCrf().getSections());
			Collections.sort(obj.getCrf().getSections());
			for (CrfSection sec : obj.getCrf().getSections()) {
				Hibernate.initialize(sec.getElement());
				if (sec.getElement() == null || sec.getElement().size() == 0) {
				} else {
					for (CrfSectionElement ele : sec.getElement()) {
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

		}
		return crfs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubGroupAnimalsInfoAll> subGroupAnimalsInfoAll(SubGroupInfo sg) {
		List<SubGroupAnimalsInfoAll> crfs = getSession().createCriteria(SubGroupAnimalsInfoAll.class)
				.add(Restrictions.eq("subGroup", sg)).list();
		return crfs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubGroupAnimalsInfoCrfDataCount> subGroupAnimalsInfoCrfDataCount(SubGroupInfo sg,
			SubGroupAnimalsInfoAll animal, StdSubGroupObservationCrfs obs) {
		List<SubGroupAnimalsInfoCrfDataCount> crfs = getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
				.add(Restrictions.eq("subGroup", sg)).add(Restrictions.eq("subGroupAnimalsInfoAll", animal))
				.add(Restrictions.eq("crf", obs)).list();
		return crfs;
	}

	@Override
	public List<CrfSectionElementData> crfSectionElementData(SubGroupAnimalsInfoCrfDataCount entry) {
		List<CrfSectionElementData> data = getSession().createCriteria(CrfSectionElementData.class)
				.add(Restrictions.eq("dataCount", entry)).list();
		return data;
	}

	@Override
	public CrfSectionElementData crfSectionElementData(SubGroupAnimalsInfoCrfDataCount entry, CrfSectionElement ele) {
		// TODO Auto-generated method stub
		return (CrfSectionElementData) getSession().createCriteria(CrfSectionElementData.class)
				.add(Restrictions.eq("dataCount", entry)).add(Restrictions.eq("element", ele)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubGroupAnimalsInfoCrfDataCountReviewLevel> subGroupAnimalsInfoCrfDataCountReviewLevels(
			SubGroupAnimalsInfoCrfDataCount entry) {
		List<SubGroupAnimalsInfoCrfDataCountReviewLevel> data = getSession()
				.createCriteria(SubGroupAnimalsInfoCrfDataCountReviewLevel.class)
				.add(Restrictions.eq("dataCount", entry)).list();
		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfMapplingTable> findAllMappingTables() {
		List<CrfMapplingTable> list = getSession().createCriteria(CrfMapplingTable.class).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfMapplingTableColumn> crfMappingTableColumns(Long id) {
		List<CrfMapplingTableColumn> list = getSession().createCriteria(CrfMapplingTableColumn.class)
				.add(Restrictions.eq("mappingTable.id", id)).list();
		return list;
	}

	@Override
	public CrfMapplingTableColumnMap saveCrfMapplingTableColumnMap(CrfMapplingTableColumnMap map) {
		getSession().save(map);
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfSectionElement> sectionElemetsSelectTale(Long id) {
		List<Long> mapedIds = getSession().createCriteria(CrfMapplingTableColumnMap.class)
				.setProjection(Projections.property("element.id")).list();
		List<CrfSectionElement> list = new ArrayList<>();
		Crf crf = (Crf) getSession().get(Crf.class, id);
		Hibernate.initialize(crf.getSections());
		for (CrfSection sec : crf.getSections()) {
			Criteria cri = getSession().createCriteria(CrfSectionElement.class).add(Restrictions.eq("section", sec))
					.add(Restrictions.eq("type", "selectTable"));
			if (mapedIds.size() > 0)
				cri.add(Restrictions.not(Restrictions.in("id", mapedIds)));
			List<CrfSectionElement> eles = cri.list();
			list.addAll(eles);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfMapplingTableColumnMap> allCrfMapplingTableColumnMap() {
		List<CrfMapplingTableColumnMap> list = getSession().createCriteria(CrfMapplingTableColumnMap.class).list();
		return list;
	}

	@Override
	public String[] crfSectionElementValuesFromCrfMappingTableColumnMap(CrfSectionElement ele) {
		CrfMapplingTableColumnMap colMap = crfMapplingTableColumnMap(ele);
		String sql = "Select " + colMap.getMappingColumn().getCloumnName() + " from "
				+ colMap.getMappingTable().getTableName();
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List results = query.list();
		List<String> st = new ArrayList<>();
		for (Object object : results) {
			Map<String, String> row = (Map<String, String>) object;
			for (Map.Entry<String, String> m : row.entrySet()) {
				st.add(m.getValue());
			}
		}
		String[] s = new String[st.size()];
		for (int i = 0; i < st.size(); i++) {
			s[i] = st.get(i);
		}
		return s;
	}

	@Override
	public SubGroupAnimalsInfoAll subGroupAnimalsInfoAllById(Long animalId) {
		return (SubGroupAnimalsInfoAll) getSession().get(SubGroupAnimalsInfoAll.class, animalId);
	}

	private CrfSectionElementInstrumentValue crfSectionElementInstrumentValue(StudyAnimal animal, Crf crf,
			CrfSectionElement ele) {
		try {
			return (CrfSectionElementInstrumentValue) getSession()
					.createCriteria(CrfSectionElementInstrumentValue.class).add(Restrictions.eq("animal", animal))
					.add(Restrictions.eq("crf", crf)).add(Restrictions.eq("element", ele))
					.add(Restrictions.eq("weightStatus", true)).uniqueResult();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void saveCrfSectionElementInstrumentValue(CrfSectionElementInstrumentValue ins) {
		CrfSectionElementInstrumentValue old = crfSectionElementInstrumentValue(ins.getAnimal(), ins.getCrf(),
				ins.getElement());
		if (old != null) {
			old.setWeightStatus(false);
			old.setUpdatedBy(ins.getCreatedBy());
			old.setUpdatedOn(new Date());
		}
		getSession().save(ins);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfSectionElementInstrumentValue> crfSectionElementInstrumentValues(StudyAnimal animal, Crf crf) {
		List<CrfSectionElementInstrumentValue> list = getSession()
				.createCriteria(CrfSectionElementInstrumentValue.class).add(Restrictions.eq("animal", animal))
				.add(Restrictions.eq("crf", crf)).add(Restrictions.eq("weightStatus", true)).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StdSubGroupObservationCrfs> stdSubGroupObservationCrfs(SubGroupInfo subGroup) {
		return getSession().createCriteria(StdSubGroupObservationCrfs.class)
				.add(Restrictions.eq("subGroupInfo", subGroup)).list();
	}

	@Override
	public Long sectionIdWithCrfId(Long id, String sectionName) {
		// TODO Auto-generated method stub
		return (Long) getSession().createCriteria(CrfSection.class).add(Restrictions.eq("crf.id", id))
				.add(Restrictions.eq("name", sectionName)).setProjection(Projections.property("id")).uniqueResult();
	}

	@Override
	public Long sectionElemntId(Long sectionId, String elementName) {
		return (Long) getSession().createCriteria(CrfSectionElement.class).add(Restrictions.eq("section.id", sectionId))
				.add(Restrictions.eq("name", elementName)).setProjection(Projections.property("id")).uniqueResult();
	}

	@Override
	public void updateStudyAccessionCrfDescrpency(StudyAccessionCrfDescrpency scd, StudyAccessionCrfDescrpencyLog log,
			CrfSectionElementDataAudit saudit) {
		// TODO Auto-generated method stub
		getSession().merge(scd.getStydyAccCrfSecEleData());
		System.out.println(scd.getStydyAccCrfSecEleData().getValue());
		getSession().merge(scd);
		getSession().save(log);
		if (saudit != null)
			getSession().save(saudit);
	}

	@Override
	public void updateStudyObservatoinCrfDescrpency(CrfDescrpency scd, CrfDescrpencyLog log,
			CrfDescrpencyAudit descAudit) {
		// TODO Auto-generated method stub
		getSession().merge(scd.getSecEleData());
		System.out.println(scd.getSecEleData().getValue());
		getSession().merge(scd);
		getSession().save(log);
		if (descAudit != null)
			getSession().save(descAudit);
	}

	@Override
	public List<StudyAccessionCrfDescrpency> allStudyAccessionCrfDescrepencyOfElement(Long id) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(StudyAccessionCrfDescrpency.class)
				.add(Restrictions.eq("stydyAccCrfSecEleData.id", id)).list();
	}

	@Override
	public StudyAccessionCrfSectionElementData studyAccessionCrfSectionElementDataWithId(
			Long studyAccessionCrfSectionElementDataId) {
		// TODO Auto-generated method stub
		return (StudyAccessionCrfSectionElementData) getSession()
				.createCriteria(StudyAccessionCrfSectionElementData.class)
				.add(Restrictions.eq("id", studyAccessionCrfSectionElementDataId)).uniqueResult();
	}

	@Override
	public boolean saveStudyAccessionCrfDescrpency(StudyAccessionCrfDescrpency scd) {
		try {
			getSession().save(scd);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Long crfIdByName(String name) {
		try {
			return (Long) getSession().createCriteria(Crf.class).add(Restrictions.eq("observationName", name))
					.setProjection(Projections.property("id")).uniqueResult();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void saveWorkFlowReviewAudit(WorkFlowReviewAudit workFlowReviewAudit) {
		// TODO Auto-generated method stub
		getSession().save(workFlowReviewAudit);
	}

	@Override
	public void updateSubjectDataEntryDetails(SubjectDataEntryDetails sded) {
		// TODO Auto-generated method stub
		getSession().merge(sded);
	}

	@Override
	public CrfSectionElementData crfSectionElementDataWithId(Long crfSectionElementDataId) {
		CrfSectionElementData obj = (CrfSectionElementData) getSession().createCriteria(CrfSectionElementData.class)
				.add(Restrictions.eq("id", crfSectionElementDataId)).uniqueResult();
		if (obj != null) {
			Hibernate.initialize(obj.getSubjectDataEntryDetails().getStudy());
			Hibernate.initialize(obj.getSubjectDataEntryDetails().getAnimal());
			Hibernate.initialize(obj.getSubjectDataEntryDetails().getObservationCrf());
			Hibernate.initialize(obj.getSubjectDataEntryDetails().getObservationCrf().getCrf());
		}
		return obj;
	}

	@Override
	public List<CrfDescrpency> allStudyCrfDescrpencyOfElement(Long id) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(CrfDescrpency.class).add(Restrictions.eq("secEleData.id", id)).list();
	}

	@Override
	public CrfDescrpency saveCrfDescrpency(CrfDescrpency scd) {
		// TODO Auto-generated method stub
		try {
			getSession().save(scd);
			scd.setSaveFlag(true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return scd;
	}

	@Override
	public List<String> crfSectionElementValue(CrfSectionElement ele) {
		if (!ele.getType().equals("selectTable")) {
			return getSession().createCriteria(CrfSectionElementValue.class)
					.add(Restrictions.eq("sectionElement.id", ele.getId())).setProjection(Projections.property("value"))
					.list();
		} else {
			List<String> evalues = new ArrayList<>();
			CrfMapplingTableColumnMap colMap = crfMapplingTableColumnMap(ele);
			String sql = "Select " + colMap.getMappingColumn().getCloumnName() + " from "
					+ colMap.getMappingTable().getTableName();
			SQLQuery query = getSession().createSQLQuery(sql);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List results = query.list();
			for (Object object : results) {
				Map<String, String> row = (Map<String, String>) object;
				for (Map.Entry<String, String> m : row.entrySet()) {
					evalues.add(m.getValue());
				}
			}
			return evalues;
		}

	}

	@Override
	public List<CrfSectionElementInstrumentValue> crfSectionElementInstrumentValues(Long animalId, Long elementId) {
		return getSession().createCriteria(CrfSectionElementInstrumentValue.class)
				.add(Restrictions.eq("animal.id", animalId)).add(Restrictions.eq("element.id", elementId)).list();
	}

	@Override
	public List<Crf> findAllActiveCrfsConfigurationForAcclimatization() {
		return getSession().createCriteria(Crf.class).add(Restrictions.eq("active", true))
				.add(Restrictions.or(Restrictions.eq("configurationFor", "Acclimatization"),
						Restrictions.eq("configurationFor", "Both")))
				.list();
	}

	@Override
	public List<Crf> findAllActiveCrfsConfigurationForObservation() {
		return getSession().createCriteria(Crf.class).add(Restrictions.eq("active", true))
				.add(Restrictions.ne("configurationFor", "Acclimatization")).list();
	}

	@Override
	public List<Crf> findAllActiveCrfsConfigurationForTreatment() {
		return getSession().createCriteria(Crf.class).add(Restrictions.eq("active", true))
				.add(Restrictions.eq("configurationFor", "Treatment")).list();
	}

	@Override
	public List<RoleMaster> allRoleMastersOfIds(List<Long> roleList) {
		return getSession().createCriteria(RoleMaster.class).add(Restrictions.in("id", roleList)).list();
	}

	@Override
	public Crf saveIsntrumebtCrf(Crf crf) {
		// TODO Auto-generated method stub
		Crf dbCrf = (Crf) getSession().createCriteria(Crf.class).add(Restrictions.eq("type", "CP")).uniqueResult();
		if (dbCrf == null) {
			getSession().save(crf);
			return crf;
		} else
			return dbCrf;
	}

	@Override
	public StdSubGroupObservationCrfs stdSubGroupObservationCrfs(Long id) {
		// TODO Auto-generated method stub
		return (StdSubGroupObservationCrfs) getSession().get(StdSubGroupObservationCrfs.class, id);
	}

	@Override
	public void saveObservationRoles(List<ObservationRole> obRoles) {
		// TODO Auto-generated method stub
		for (ObservationRole obr : obRoles) {
			getSession().save(obr);
		}
	}

}
