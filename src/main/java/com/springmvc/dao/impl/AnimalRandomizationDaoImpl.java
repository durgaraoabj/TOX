package com.springmvc.dao.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.covide.crf.dto.CrfSection;
import com.covide.crf.dto.CrfSectionElement;
import com.covide.dto.RadomizationDto;
import com.covide.dto.RandomizationGenerationDto;
import com.covide.enums.StatusMasterCodes;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.AnimalRandomizationDao;
import com.springmvc.dao.StatusDao;
import com.springmvc.dao.StudyDao;
import com.springmvc.dao.UserDao;
import com.springmvc.model.AccessionAnimalsDataEntryDetails;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.RadamizationAllDataReview;
import com.springmvc.model.RadomizationReviewDto;
import com.springmvc.model.RandamizationAssigndDto;
import com.springmvc.model.RandamizationDto;
import com.springmvc.model.RandamizationGroupAnimalDto;
import com.springmvc.model.RandamizationGroupDto;
import com.springmvc.model.RandomizationDetails;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyAccessionAnimals;
import com.springmvc.model.StudyAccessionCrfSectionElementData;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.WorkFlow;
import com.springmvc.model.WorkFlowReviewAudit;
import com.springmvc.model.WorkFlowReviewStages;

@Repository("animalRandomizationDao")
public class AnimalRandomizationDaoImpl extends AbstractDao<Long, StudyMaster> implements AnimalRandomizationDao {

	@Autowired
	private StudyDao studyDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private StatusDao statuDao;

	@SuppressWarnings("unchecked")
	@Override
	public RadomizationDto getRadomizationDtoDetails(Long studyId) {
		RadomizationDto rmDto = null;
		List<StudyAcclamatizationData> sacdList = null;
		List<StudyAccessionAnimals> saaList = null;
		try {
			sacdList = getSession().createCriteria(StudyAcclamatizationData.class)
					.add(Restrictions.eq("study.id", studyId)).list();
			saaList = getSession().createCriteria(StudyAccessionAnimals.class).add(Restrictions.eq("study.id", studyId))
					.list();
			rmDto = new RadomizationDto();
//			rmDto.setSaaList(saaList);  //swami
//			rmDto.setSacdList(sacdList);  //swami
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rmDto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AccessionAnimalsDataEntryDetails> getAccessionAnimalsDataEntryDetailsList(Long crfId, Long studyId,
			Long animalId, int animalNo) {
		return getSession().createCriteria(AccessionAnimalsDataEntryDetails.class).add(Restrictions.eq("crf.id", crfId))
				.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("studyAccAnimal.id", animalId))
				.add(Restrictions.eq("animal.id", animalNo)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public RandomizationGenerationDto RandomizationGenerationDtoDetails(Long studyId, Long crfId) {
		RandomizationGenerationDto rdmgDto = null;
		List<StudyAccessionAnimals> saaList = null;
		List<Long> formIds = null;
		StudyMaster sm = null;
		List<Long> sectionIds = null;
		Long seceleId = null;
		try {
			sm = (StudyMaster) getSession().createCriteria(StudyMaster.class).add(Restrictions.eq("id", studyId))
					.uniqueResult();

			saaList = getSession().createCriteria(StudyAccessionAnimals.class).add(Restrictions.eq("study.id", studyId))
					.list();

			formIds = getSession().createCriteria(StudyAcclamatizationData.class)
					.add(Restrictions.eq("study.id", studyId)).setProjection(Projections.property("crf.id")).list();

			if (formIds != null && formIds.size() > 0) {
				sectionIds = getSession().createCriteria(CrfSection.class).add(Restrictions.eq("crf.id", crfId))
						.setProjection(Projections.property("id")).list();
			}
			if (sectionIds != null && sectionIds.size() > 0) {

				seceleId = (Long) getSession().createCriteria(CrfSectionElement.class)
						.add(Restrictions.in("section.id", sectionIds)).add(Restrictions.eq("typeOfTime", "weight"))
						.setProjection(Projections.property("id")).uniqueResult();
			}

			rdmgDto = new RandomizationGenerationDto();
			rdmgDto.setSaaList(saaList);
			rdmgDto.setSm(sm);
			rdmgDto.setSeceleId(seceleId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rdmgDto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StudyAccessionCrfSectionElementData getStudyAccessionCrfSectionElementDataRecord(Long studyId, int animalId,
			Long crfId, Long secelId) {
		StudyAccessionCrfSectionElementData aaded = null;
		AccessionAnimalsDataEntryDetails acaded = null;
		List<Long> acaedIds = null;
		List<Long> acadedIds = null;
		Long acadedId = 0L;
		try {

			acadedIds = getSession().createCriteria(AccessionAnimalsDataEntryDetails.class)
					.add(Restrictions.eq("crf.id", crfId)).add(Restrictions.eq("animalId", animalId))
					.add(Restrictions.eq("study.id", studyId)).setProjection(Projections.property("id")).list();

			if (acadedIds != null && acadedIds.size() > 0) {
				acadedId = Collections.max(acadedIds);
			}

			acaded = (AccessionAnimalsDataEntryDetails) getSession()
					.createCriteria(AccessionAnimalsDataEntryDetails.class).add(Restrictions.eq("id", acadedId))
					.uniqueResult();
			if (acaded != null) {
				acaedIds = getSession().createCriteria(StudyAccessionCrfSectionElementData.class)
						.add(Restrictions.eq("element.id", secelId)).add(Restrictions.eq("crf.id", crfId))
						.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("studyAccAnimal", acaded))
						.setProjection(Projections.property("id")).list();
			}
			if (acaedIds != null && acaedIds.size() > 0) {
				Long maxNo = Collections.max(acaedIds);
				aaded = (StudyAccessionCrfSectionElementData) getSession()
						.createCriteria(StudyAccessionCrfSectionElementData.class).add(Restrictions.eq("id", maxNo))
						.uniqueResult();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aaded;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GroupInfo> getGroupInfo(Long studyId) {
		return getSession().createCriteria(GroupInfo.class).add(Restrictions.eq("study.id", studyId)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubGroupAnimalsInfo> getSubGroupAnimalsInfoRecordsList(List<Long> groupIds, Long studyId) {
		return getSession().createCriteria(SubGroupAnimalsInfo.class).add(Restrictions.in("group.id", groupIds))
				.add(Restrictions.eq("study.id", studyId)).list();
	}

	@Override
	public StudyMaster getStudyMasterRecord(Long studyId) {
		return (StudyMaster) getSession().createCriteria(StudyMaster.class).add(Restrictions.eq("id", studyId))
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubGroupAnimalsInfo> getSubGroupAnimalsInfoRecordsList(Long studyId) {
		return getSession().createCriteria(SubGroupAnimalsInfo.class).add(Restrictions.eq("study.id", studyId)).list();
	}

	@Override
	public SubGroupInfo getSubGroupInfoRecord(Long subgroupId) {
		return (SubGroupInfo) getSession().createCriteria(SubGroupInfo.class).add(Restrictions.eq("id", subgroupId))
				.uniqueResult();
	}

	@Override
	public boolean saveRadomizationDetails(List<SubGroupAnimalsInfo> updateList, List<RandomizationDetails> radList) {
		boolean flag = false;
		boolean updateFlag = false;
		boolean rzgFlag = false;
		try {
			if (updateList.size() > 0) {
				for (SubGroupAnimalsInfo sai : updateList) {
					getSession().update(sai);
					updateFlag = true;
				}
			} else
				updateFlag = true;

			if (radList.size() > 0) {
				for (RandomizationDetails rzd : radList) {
					getSession().save(rzd);

					SubGroupAnimalsInfoAll sgall = new SubGroupAnimalsInfoAll();
					sgall.setAnimalNo(rzd.getPermanentId());
					String prNo = rzd.getPermanentId();
					String[] tempArr = prNo.split("\\/");
//					sgall.setCreatedBy(rzd.getCreated_by());
					sgall.setCreatedOn(new Date());
					if (tempArr.length > 0)
						sgall.setNo(Integer.parseInt(tempArr[1]));
					sgall.setSubGroup(rzd.getSubgroupId());
					sgall.setSubGroupAnimalsInfo(rzd.getSubanimalInfo());
					sgall.setRazdId(rzd);
					getSession().save(sgall);
					rzgFlag = true;
				}
			} else
				rzgFlag = true;
			if (updateFlag && rzgFlag) {
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
	public List<RandomizationDetails> getRandomizationDetailsRecordsList(Long studyId) {
		return getSession().createCriteria(RandomizationDetails.class).add(Restrictions.eq("studyId.id", studyId))
				.list();
	}

	@Override
	public String saveRadomization(List<StudyAnimal> animalsList, List<SubGroupAnimalsInfo> subGroup,
			List<RandomizationDetails> radList, StudyMaster study) {
		try {
			animalsList.forEach((ani) -> {
				getSession().merge(ani);
			});
			subGroup.forEach((sg) -> {
				getSession().merge(sg);
			});
			radList.forEach((rad) -> {
				getSession().save(rad);
			});
			StatusMaster sentToRevew = statuDao.statusMaster(StatusMasterCodes.SENDTORIVEW.toString());
			study.setRadamizationStatus(sentToRevew);
			return "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "fail";
		}

	}

	@Override
	public String sendRandamizationToReview(RadomizationReviewDto reviewrdmDto, Long studyId, Long userId) {
		try {
			RadomizationReviewDto old = (RadomizationReviewDto) getSession().createCriteria(RadomizationReviewDto.class)
					.add(Restrictions.eq("study.id", studyId)).add(Restrictions.eq("viewData", true)).uniqueResult();
//			if(old != null) {
//				old.setViewData(false);
//			}
			LoginUsers user = userDao.findById(userId);
			StudyMaster study = studyDao.findByStudyId(studyId);
			study.setRandamizationDoneNow(reviewrdmDto.getGender());
			StatusMaster activeStatus = statuDao.statusMaster(StatusMasterCodes.ACTIVE.toString());
			StatusMaster sentToReview = statuDao.statusMaster(StatusMasterCodes.SENDTORIVEW.toString());

			if (reviewrdmDto.getGender().equals("Both") || reviewrdmDto.getGender().equals("")) {
				study.setMaleRandamizationStatus(true);
				study.setFemaleRandamizationStatus(true);
			} else if (reviewrdmDto.getGender().equals("Male")) {
				study.setMaleRandamizationStatus(true);
			} else if (reviewrdmDto.getGender().equals("Female")) {
				study.setFemaleRandamizationStatus(true);
			}
			reviewrdmDto.setStudy(study);
			reviewrdmDto.setReviewStatus(sentToReview);
			reviewrdmDto.setCreatedBy(user.getUsername());
			reviewrdmDto.setCreatedOn(new Date());
			if (reviewrdmDto.getRandamizaitonSheetMale() != null) {
				getSession().save(reviewrdmDto.getRandamizaitonSheetMale());
			}
			if (reviewrdmDto.getRandamizaitonSheetFemale() != null)
				getSession().save(reviewrdmDto.getRandamizaitonSheetFemale());

			if (reviewrdmDto.getRandamizaitonSheetMaleAscedning() != null)
				getSession().save(reviewrdmDto.getRandamizaitonSheetMaleAscedning());
			if (reviewrdmDto.getRandamizaitonSheetFemaleAscedning() != null)
				getSession().save(reviewrdmDto.getRandamizaitonSheetFemaleAscedning());

			List<RadamizationAllDataReview> randamizaitonSheetMaleGruoup = reviewrdmDto
					.getRandamizaitonSheetMaleGruoup();
			if (randamizaitonSheetMaleGruoup != null) {
				for (RadamizationAllDataReview radr : randamizaitonSheetMaleGruoup) {
					getSession().save(radr);
				}
			}
			reviewrdmDto.setRandamizaitonSheetMaleGruoup(randamizaitonSheetMaleGruoup);
			List<RadamizationAllDataReview> randamizaitonSheetFemaleGruoup = reviewrdmDto
					.getRandamizaitonSheetFemaleGruoup();
			if (randamizaitonSheetFemaleGruoup != null) {
				for (RadamizationAllDataReview radr : randamizaitonSheetFemaleGruoup) {
					getSession().save(radr);
				}
			}
			reviewrdmDto.setRandamizaitonSheetFemaleGruoup(randamizaitonSheetFemaleGruoup);

			getSession().save(reviewrdmDto);

			WorkFlow accessionWorkFlow = userDao.accessionWorkFlow("RANDAMIZATION", activeStatus);
			WorkFlowReviewStages workFlowReviewStages = userDao.workFlowReviewStages(user, accessionWorkFlow);
			if (workFlowReviewStages != null) {
				WorkFlowReviewAudit workFlowReviewAudit = new WorkFlowReviewAudit();
				workFlowReviewAudit.setStudy(study);
				workFlowReviewAudit.setReamdamizationReivew(reviewrdmDto);
				workFlowReviewAudit.setReviewState(workFlowReviewStages);
				workFlowReviewAudit.setRole(workFlowReviewStages.getFromRole());
				workFlowReviewAudit.setUser(user);
				workFlowReviewAudit.setDateOfActivity(new Date());
				getSession().save(workFlowReviewAudit);
				reviewrdmDto.setCurrentReviewRole(workFlowReviewStages.getToRole());
			}
			study.setRadamizationStatus(sentToReview);
			study.setRadamizationDoneBy(user.getUsername());
			study.setRadamizationDate(new Date());
			return "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "error";
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public RandamizationDto generatedRandamization(StudyMaster study, String gender) {
		// TODO Auto-generated method stub
		RandamizationDto dto = (RandamizationDto) getSession().createCriteria(RandamizationDto.class)
				.add(Restrictions.eq("study.id", study.getId())).add(Restrictions.eq("gender", gender))
				.add(Restrictions.eq("activeStatus", true)).uniqueResult();
		if (dto != null) {
			dto.setAnimals(getSession().createCriteria(StudyAnimal.class)
					.add(Restrictions.eq("study.id", study.getId())).list());
			List<RandamizationAssigndDto> randamizationAssigndDto = getSession()
					.createCriteria(RandamizationAssigndDto.class)
					.add(Restrictions.eq("randamizationDto.id", dto.getId())).list();
			Collections.sort(randamizationAssigndDto);
			dto.setRandamizationAssigndDto(randamizationAssigndDto);
			List<RandamizationGroupDto> randamizationGroupDto = getSession().createCriteria(RandamizationGroupDto.class)
					.add(Restrictions.eq("randamizationDto.id", dto.getId())).list();
			Collections.sort(randamizationGroupDto);
			for (RandamizationGroupDto gdto : randamizationGroupDto) {
				List<RandamizationGroupAnimalDto> randamizationGroupAnimalDto = getSession()
						.createCriteria(RandamizationGroupAnimalDto.class)
						.add(Restrictions.eq("randamizationGroupDto.id", gdto.getId())).list();
				Collections.sort(randamizationGroupAnimalDto);
				gdto.setRandamizationGroupAnimalDto(randamizationGroupAnimalDto);
			}
			dto.setRandamizationGroupDto(randamizationGroupDto);
		}
		return dto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StudyAnimal> studyAnimal(Long id) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", id)).list();
	}

	@Override
	public boolean saverandomizationSendToReview(StudyMaster study, List<RandamizationDto> dtoList, Long userId) {
		// TODO Auto-generated method stub
		LoginUsers user = (LoginUsers) getSession().get(LoginUsers.class, userId);
		for (RandamizationDto dto : dtoList) {
			dto.setStudy(study);
			getSession().save(dto);
			List<RandamizationAssigndDto> randamizationAssigndDto = dto.getRandamizationAssigndDto();
			for (RandamizationAssigndDto adto : randamizationAssigndDto) {
				adto.setRandamizationDto(dto);
				dto.setCreatedBy(user.getUsername());
				getSession().save(adto);
			}
			List<RandamizationGroupDto> randamizationGroupDto = dto.getRandamizationGroupDto();
			for (RandamizationGroupDto gdto : randamizationGroupDto) {
				gdto.setRandamizationDto(dto);
				getSession().save(gdto);
				List<RandamizationGroupAnimalDto> randamizationGroupAnimalDto = gdto.getRandamizationGroupAnimalDto();
				for (RandamizationGroupAnimalDto gadto : randamizationGroupAnimalDto) {
					gadto.setRandamizationGroupDto(gdto);
					getSession().save(gadto);
//					StudyAnimal animal = gadto.getAnimal();
//					animal.setPermanentNo(gadto.getAnimalNo());
//					animal.setSubGropInfo(gadto.getSubGroup());
//					animal.setGroupInfo(gadto.getSubGroup().getGroup());
//					getSession().merge(animal);
				}
			}
		}
		for (RandamizationDto dto : dtoList) {
			List<RandamizationGroupDto> randamizationGroupDto = dto.getRandamizationGroupDto();
			for (RandamizationGroupDto gdto : randamizationGroupDto) {
				List<RandamizationGroupAnimalDto> randamizationGroupAnimalDto = gdto.getRandamizationGroupAnimalDto();
				for (RandamizationGroupAnimalDto gadto : randamizationGroupAnimalDto) {
					System.out.println(gadto.getOrder());
				}
			}
		}
		getSession().merge(study);
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StudyAnimal> studyAnimalWithGender(Long id, String gender) {
		// TODO Auto-generated method stub
		if (gender.equals("Both"))
			return getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", id)).list();
		else
			return getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", id))
					.add(Restrictions.eq("gender", gender)).list();
	}

	@Override
	public int stuyAnimalSize(StudyMaster study, String gender) {
		// TODO Auto-generated method stub
		if (gender.equals("Both"))
			return study.getSubjects();
		else {
			Long count =  (Long) getSession().createCriteria(SubGroupAnimalsInfo.class)
					.add(Restrictions.eq("study.id", study.getId())).add(Restrictions.eq("gender", gender))
					.setProjection(Projections.sum("count")).uniqueResult();
			return count.intValue();
		}
	}
}
