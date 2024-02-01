package com.springmvc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.covide.crf.dto.Crf;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.CRFAuditDao;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.util.ObservationData;

@Repository("crfAuditDao")
public class CRFAuditDaoImpl extends AbstractDao<Long, StudyMaster> implements CRFAuditDao {

	@Override
	public StudyMaster findByStudyId(long studyId) {
		StudyMaster sm = getByKey(studyId);
		Hibernate.initialize(sm.getStudyAnalytes());
		return sm;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GroupInfo> studyGroupInfoWithChaildReview(StudyMaster sm) {
		List<GroupInfo> giList = getSession().createCriteria(GroupInfo.class).add(Restrictions.eq("study", sm)).list();
		for (GroupInfo gi : giList) {
			try {
				Long c = (Long) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
						.add(Restrictions.eq("group", gi)).add(Restrictions.eq("crfStatus", "IN REVIEW"))
						.setProjection(Projections.rowCount()).uniqueResult();
				if (c == 0) {
					c = (Long) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
							.add(Restrictions.eq("group", gi)).add(Restrictions.eq("crfStatus", "REVIEWED"))
							.setProjection(Projections.rowCount()).uniqueResult();
					if (c > 0)
						gi.setReviewStatus("REVIEWED");
				} else
					gi.setNoOfGroupsNeedToReview(c.intValue());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			List<SubGroupInfo> sgifList = studySubGroupInfo(gi);
			for (SubGroupInfo sgi : sgifList) {
				try {
					Long c = (Long) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
							.add(Restrictions.eq("subGroup", sgi)).add(Restrictions.eq("crfStatus", "IN REVIEW"))
							.setProjection(Projections.rowCount()).uniqueResult();
					if (c == 0) {
						c = (Long) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
								.add(Restrictions.eq("subGroup", sgi)).add(Restrictions.eq("crfStatus", "REVIEWED"))
								.setProjection(Projections.rowCount()).uniqueResult();
						if (c > 0)
							sgi.setReviewStatus("REVIEWED");
					} else
						sgi.setNoOfSubGroupsNeedToReview(c.intValue());
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				List<SubGroupAnimalsInfo> animalInfo = subGroupAnimalsInfo(sgi);
				sgi.setAnimalInfo(animalInfo);
			}
			gi.setSubGroupInfo(sgifList);
		}
		return giList;
	}
	@SuppressWarnings("unchecked")
	public List<SubGroupAnimalsInfo> subGroupAnimalsInfo(SubGroupInfo sgi) {
		return getSession().createCriteria(SubGroupAnimalsInfo.class).add(Restrictions.eq("subGroup", sgi)).list();
	}

	@SuppressWarnings("unchecked")
	public List<SubGroupInfo> studySubGroupInfo(GroupInfo gi) {
		return getSession().createCriteria(SubGroupInfo.class).add(Restrictions.eq("group", gi)).list();
	}

	@Override
	public SubGroupInfo subGroupInfoAllByIdReview(StudyMaster sm, Long sibGroupId) {
		// TODO Auto-generated method stub
		SubGroupInfo sg = (SubGroupInfo) getSession().get(SubGroupInfo.class, sibGroupId);
		Hibernate.initialize(sg.getGroup());
		List<SubGroupAnimalsInfoAll> animalInfo = subGroupAnimalsInfoAllReview(sg);
		List<SubGroupAnimalsInfoAll> animalInfo1 = findAllSubjectsStdSubGroupObservationCrfs(sg, animalInfo, sm);
//		List<StdSubGroupObservationCrfs> observations = findAllActiveStdSubGroupObservationCrfs(sm, sibGroupId);
//		for(SubGroupAnimalsInfoAll sgai : animalInfo) {
//			//24
//			sgai.setObservation(observations);
//		}
		sg.setAnimalInfoAll(animalInfo1);
		return sg;

	}
	@SuppressWarnings("unchecked")
	public List<SubGroupAnimalsInfoAll> subGroupAnimalsInfoAllReview(SubGroupInfo sgi) {
		List<SubGroupAnimalsInfoAll> list = getSession().createCriteria(SubGroupAnimalsInfoAll.class)
				.add(Restrictions.eq("subGroup", sgi)).list();
		for (SubGroupAnimalsInfoAll s : list) {
			try {
				Long c = (Long) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
						.add(Restrictions.eq("subGroupAnimalsInfoAll", s))
						.add(Restrictions.eq("crfStatus", "IN REVIEW")).setProjection(Projections.rowCount())
						.uniqueResult();
				if (c == 0) {
					c = (Long) getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
							.add(Restrictions.eq("subGroupAnimalsInfoAll", s))
							.add(Restrictions.eq("crfStatus", "REVIEWED")).setProjection(Projections.rowCount())
							.uniqueResult();
					if (c > 0) {
						s.setReviewStatus("REVIEWED");
						s.setNoOfObservationsNeedToReview(-1);
					}
				} else
					s.setNoOfObservationsNeedToReview(c.intValue());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return list;
	}
	@SuppressWarnings("unchecked")
	private List<SubGroupAnimalsInfoAll> findAllSubjectsStdSubGroupObservationCrfs(SubGroupInfo sg,
			List<SubGroupAnimalsInfoAll> animalInfo, StudyMaster sm) {
		List<SubGroupAnimalsInfoAll> animals = new ArrayList<>();
		for (SubGroupAnimalsInfoAll ani : animalInfo) {
			List<StdSubGroupObservationCrfs> stdCrfs = getSession().createCriteria(StdSubGroupObservationCrfs.class)
					.add(Restrictions.eq("study", sm))
					.add(Restrictions.eq("subGroupInfo.id", ani.getSubGroup().getId()))
					.add(Restrictions.eq("active", true)).list();
			List<StdSubGroupObservationCrfs> stdCrfs2 = new ArrayList<>();
			for (StdSubGroupObservationCrfs ssoc : stdCrfs) {
				StdSubGroupObservationCrfs ssoc2 = new StdSubGroupObservationCrfs();
				ssoc2.setId(ssoc.getId());
				ssoc2.setObservationName(ssoc.getObservationName());
				try {
					List<SubGroupAnimalsInfoCrfDataCount> c = getSession()
							.createCriteria(SubGroupAnimalsInfoCrfDataCount.class).add(Restrictions.eq("crf", ssoc))
							.add(Restrictions.eq("subGroupAnimalsInfoAll", ani))
							.add(Restrictions.eq("crfStatus", "IN REVIEW")).list();
					if (c.size() == 0) {
						c = getSession().createCriteria(SubGroupAnimalsInfoCrfDataCount.class)
								.add(Restrictions.eq("crf", ssoc)).add(Restrictions.eq("subGroupAnimalsInfoAll", ani))
								.add(Restrictions.eq("crfStatus", "REVIEWED")).list();
						if (c.size() > 0) {
							ssoc2.setNeedToReview(-1);
							ssoc2.setNeedTore("-1");
//							System.out.println(ssoc.getNeedToReview());
							ssoc2.setDataCrfs(c);
						}
					} else {
						ssoc2.setNeedToReview(c.size());
						ssoc2.setDataCrfs(c);
					}
					stdCrfs2.add(ssoc2);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				System.out.println(ssoc.getNeedToReview());
			}
			for (StdSubGroupObservationCrfs ssoc : stdCrfs2) {
				System.out.println(ssoc.getId() + " - " + ssoc.getNeedToReview() + "  " + ssoc.getNeedTore());
			}
			ani.setObservation(stdCrfs2);
			animals.add(ani);
		}
		for (SubGroupAnimalsInfoAll ani : animals) {
			List<StdSubGroupObservationCrfs> stdCrfs2 = ani.getObservation();
			for (StdSubGroupObservationCrfs ssoc : stdCrfs2) {
				System.out.println(ssoc.getId() + " - " + ssoc.getNeedToReview() + "  " + ssoc.getNeedTore());
			}
		}
		return animals;
	}

	@Override
	public LoginUsers findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObservationData observationData(Long subGroupInfoId, Long stdSubGroupObservationCrfsId, LoginUsers user) {
		// TODO Auto-generated method stub
		return null;
	}

}
