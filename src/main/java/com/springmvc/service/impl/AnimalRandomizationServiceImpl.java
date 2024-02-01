package com.springmvc.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.covide.crf.dao.CrfDAO;
import com.covide.crf.dto.Crf;
import com.covide.dto.FinalRandomizationDto;
import com.covide.dto.FromsStatusDto;
import com.covide.dto.RadomizationDto;
import com.covide.dto.RandomizationGenerationDto;
import com.covide.dto.StudyGroupSubGroupDetails;
import com.covide.enums.StatusMasterCodes;
import com.springmvc.dao.AccessionDao;
import com.springmvc.dao.AnimalRandomizationDao;
import com.springmvc.dao.ExpermentalDesignDao;
import com.springmvc.dao.StatusDao;
import com.springmvc.dao.StudyDao;
import com.springmvc.dao.UserDao;
import com.springmvc.model.AccessionAnimalsDataEntryDetails;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.RadomizationReviewDto;
import com.springmvc.model.RandamizationAssigndDto;
import com.springmvc.model.RandamizationDto;
import com.springmvc.model.RandamizationGroupAnimalDto;
import com.springmvc.model.RandamizationGroupDto;
import com.springmvc.model.RandomizationDetails;
import com.springmvc.model.StaticData;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyAccessionAnimals;
import com.springmvc.model.StudyAccessionCrfSectionElementData;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.dummy.RadamizationAllData;
import com.springmvc.service.AnimalRandomizationService;

@Service("animalRandomizationService")
@PropertySource(value = { "classpath:application.properties" })
public class AnimalRandomizationServiceImpl implements AnimalRandomizationService {
	@Autowired
	private Environment environment;
	@Autowired
	AnimalRandomizationDao animalRandDao;
	@Autowired
	private ExpermentalDesignDao expermentalDesignDao;
	@Autowired
	StudyDao studyDao;
	@Autowired
	StatusDao statusDao;
	@Autowired
	CrfDAO crfDao;
	@Autowired
	AccessionDao accessionDao;
	@Autowired
	UserDao userDao;
	private Map<Long, GroupInfo> groups = new HashMap<>(); // groupId(key) , Group (value);
	private Map<Long, Integer> assignedAnimalsToSubGroup = new HashMap<>();
	public static int animalNo = 1;

	@Override
	public RadomizationReviewDto getRadomizationReviewDtoDetails(Long studyId) {
//		StatusMaster senttoReview = statusDao.statusMaster("Sent to Review");
//		StatusMaster inReview = statusDao.statusMaster("INREVIEW");
//		StatusMaster reviewed = statusDao.statusMaster("Sent to Review");
		return accessionDao.radomizationReviewDto(studyId);

	}

	@Override
	public RadomizationReviewDto getRadomizationReviewDtoDetails(Long studyId, Long userId, String gender) {
		return accessionDao.radomizationReviewDto(studyId, userId, gender);
	}

	@Override
	public RadomizationDto getRadomizationDtoDetails(Long studyId) {
//		generateRandamization(studyId);
		RadomizationDto dto = new RadomizationDto();
		StudyMaster study = studyDao.findByStudyId(studyId);
		dto.setStudy(study);

		StatusMaster activeStatus = statusDao.statusMaster(StatusMasterCodes.ACTIVE.toString());
		StatusMaster senttoReview = statusDao.statusMaster("Sent to Review");
		StatusMaster approveStatus = statusDao.statusMaster(StatusMasterCodes.APPROVED.toString());
		StaticData activityWeightForm = statusDao.staticData(StatusMasterCodes.ACTITYORCRF.toString(),
				StatusMasterCodes.ACTIVITYWEIGHTFROM.toString());
		StaticData activityWeightSection = statusDao.staticData(StatusMasterCodes.ACTITYORCRF.toString(),
				StatusMasterCodes.AMBULATORY.toString());
		StaticData activityWeightField = statusDao.staticData(StatusMasterCodes.ACTITYORCRF.toString(),
				StatusMasterCodes.TOTAL.toString());
		Crf crf = crfDao.checkCrf(activityWeightForm.getFieldValue());

		List<SubGroupAnimalsInfo> subGroupAnimalsInfo = expermentalDesignDao.allSubGroupAnimalsInfos(studyId);

		Map<String, Map<Integer, SubGroupAnimalsInfo>> genderWiseGroupsWithOrder = new HashMap<>();
		int noOfAnimals = 0;
		for (SubGroupAnimalsInfo sub : subGroupAnimalsInfo) {
			Map<Integer, SubGroupAnimalsInfo> subGrops = genderWiseGroupsWithOrder.get(sub.getGender());
			if (subGrops == null)
				subGrops = new HashMap<>();
			subGrops.put(sub.getSequenceNo(), sub);
			genderWiseGroupsWithOrder.put(sub.getGender(), subGrops);

			noOfAnimals += sub.getCount();
			groups.put(sub.getGroup().getId(), sub.getGroup());

			System.out.println(
					sub.getSequenceNo() + "\t" + sub.getGroup().getGroupNo() + "\t" + sub.getGroup().getGroupName());
			assignedAnimalsToSubGroup.put(sub.getId(), 0);
		}

		List<Long> studyAnimalIds = accessionDao.studyActiveAnimalIds(activeStatus, studyId); // Live(Active) Animals
																								// Id's
		// check required animal and accesion animal size
		if (studyAnimalIds.size() >= noOfAnimals) {
			List<AccessionAnimalsDataEntryDetails> dataEntered = accessionDao.accessionAnimalsDataEntryDetails(crf,
					studyAnimalIds, approveStatus, senttoReview);
//				List<AccessionAnimalsDataEntryDetails> dataEntered = accessionDao.accessionAnimalsDataEntryDetails(crf,
//						studyAnimalIds, approveStatus);													// Data Entered  Animals with CRF mapping
			List<AccessionAnimalsDataEntryDetails> approved = new ArrayList<>(); // Reviewed Animals with CRF mapping
			dataEntered.forEach((each) -> {
				if (each.getStatus().getStatusCode().equals(StatusMasterCodes.APPROVED.toString()))
					approved.add(each);
			});

			// check required animal and data entered animal size
			if (dataEntered.size() >= noOfAnimals) {
				// check required animal and data reviewed animal size
				if (approved.size() >= noOfAnimals) {
					Map<String, List<StudyAnimal>> genderBasedAnimals = new HashMap<>(); // Gender based animals list
																							// and there Weights
					approved.forEach((each) -> {
						StudyAnimal animal = each.getAnimal();
						animal.setAnimalDataMeataData(each);
						animal.setActivityData(accessionDao.weightStudyAccessionCrfSectionElementData(each,
								activityWeightSection.getFieldValue(), activityWeightField.getFieldValue()));
						System.out.println(
								"animal.getActivityData().getValue() : " + animal.getActivityData().getValue());
						animal.setWeight(Double.parseDouble(animal.getActivityData().getValue()));
						List<StudyAnimal> tempValue = genderBasedAnimals.get(animal.getGenderCode());
						if (tempValue == null) {
							tempValue = new ArrayList<>();
						}
						tempValue.add(animal);
						System.out.println(animal.getGenderCode() + "\t" + animal.getAnimalNo());
						genderBasedAnimals.put(animal.getGenderCode(), tempValue);
					});
					System.out.println();
					List<RadamizationAllData> dataList = new ArrayList<>(); // GENDER BASED ANIMALS WEIGHT
					List<RadamizationAllData> dataOrderList = new ArrayList<>(); // GENDER BASED ANIMALS WEIGHT Order
					for (Map.Entry<String, List<StudyAnimal>> map : genderBasedAnimals.entrySet()) {
						String gender = map.getKey();
						List<StudyAnimal> animals = map.getValue();

						dataList.add(new RadamizationAllData(study, gender, animals));
						List<StudyAnimal> clones = new ArrayList<>(animals);
						Collections.sort(clones);
						clones.forEach((an) -> {
							System.out.println(an.getAnimalNo() + "\t" + an.getWeight());
						});
						dataOrderList.add(new RadamizationAllData(study, gender, clones));
					}

					// to show difference weights
					List<RadamizationAllData> dataOrderListAll = new ArrayList<>(dataOrderList);
					int allmale = 0; // total available male animals
					int allfemale = 0;// total available female animals
					for (RadamizationAllData rad : dataOrderListAll) {
						if (rad.getGender().equals("M"))
							allmale = rad.getAnimals().size();
						else
							allfemale = rad.getAnimals().size();
					}

					// Based on mean remove animals weight not in rage
					dataOrderList = meanBasedRemoceAnimalIfWeightNotInRage(dataOrderList);
					int male = 0; // total available male animals
					int female = 0;// total available female animals
					for (RadamizationAllData rad : dataOrderList) {
						if (rad.getGender().equals("M"))
							male = rad.getAnimals().size();
						else
							female = rad.getAnimals().size();
					}

					male = allmale - male;
					female = allfemale - female;
					// animals not lessthan 0 we have sufficient animals
					if (male >= 0 && female >= 0) {
						dto = generateRandamizationTableAllByGender(dto, dataList);
						dto = generateRandamizationTableAllByGenderAssending(dto, dataOrderList);
						dto = randamizatoinGenderBasedToGroup1stPahse(dto, genderWiseGroupsWithOrder, dataOrderList,
								dataList, dataOrderList, study, "");
						return dto;
					} else {
//							male or female animals required
						dto.setErrorCode(4); // error Message : In sufficient male or female animals required
						// caliculate no of males and femails required
						dto.setErrorMessage("In sufficient male or female animals required");

						for (RadamizationAllData rad : dataOrderListAll) {
							if (rad.getGender().equals("M"))
								dto.setRandamizaitonSheetMaleGruoupAll(rad);
							else
								dto.setRandamizaitonSheetFemaleGruoupAll(rad);
						}
						for (RadamizationAllData rad : dataOrderList) {
							if (rad.getGender().equals("M"))
								dto.setRandamizaitonSheetMaleGruoupAvialble(rad);
							else
								dto.setRandamizaitonSheetFemaleGruoupAvialble(rad);
						}
					}
				} else {
					// accession data approval no done for required no of animals
					dto.setErrorCode(3); // error Message : accession data approval no done for required no of animals
					dto.setErrorMessage("Accession data approval not done for required no of animals :" + noOfAnimals);
				}
			} else {
				// accession data entry no done for required no of animals
				dto.setErrorCode(2); // error Message : accession data entry no done for required no of animals
				dto.setErrorMessage("Accession data entry not done for required no of animals");
			}
		} else {
			// accession no done for required no of animals
			dto.setErrorCode(1); // error Message : accession no done for required no of animals
			dto.setErrorMessage("Accession no done for required no of animals");
		}

		return dto;
	}

	int maleGroups = 0;
	int feMaleGroups = 0;

	@Override
	public RadomizationDto generateRandamization(Long studyId) {
		RadomizationDto dto = new RadomizationDto();
		StudyMaster study = studyDao.findByStudyId(studyId);
		dto.setStudy(study);
		String gender = study.getGender();
		if (study.getGender().equals("Both") && study.isSplitStudyByGender()) {
			if (!study.isMaleRandamizationStatus() && !study.isFemaleRandamizationStatus()) {
				Date maleStartDate = study.getAcclimatizationStarDate();
				Date femleStartDate = study.getAcclimatizationStarDateFemale();
				int days = maleStartDate.compareTo(femleStartDate);
				if (days < 0) {
					gender = "Male";
				} else if (days == 0) {
					gender = "Both";
				} else
					gender = "Female";
			} else if (!study.isMaleRandamizationStatus() || !study.isMaleRandamizationReviewStatus()) {
				gender = "Male";
			} else if (!study.isFemaleRandamizationStatus() || !study.isFemaleRandamizationReviewStatus()) {
				gender = "Female";
			}
		}
		dto.setGender(gender);
		// 1. All the animal with sequency no order
		List<StudyAnimal> animalsList = accessionDao.allStudyAnimals(studyId);
		System.out.println(animalsList.size());
		SortedMap<Integer, StudyAnimal> maleAnimalsMap = new TreeMap<>();
		SortedMap<Integer, StudyAnimal> femaleAnimalsMap = new TreeMap<>();

		List<StudyAnimal> maleAnimals = new ArrayList<>();
		List<StudyAnimal> femaleAanimals = new ArrayList<>();
		animalsList.forEach((animal) -> {
			if (animal.getGender().equals("Male")) {
				maleAnimals.add(animal);
				maleAnimalsMap.put(animal.getSequnceNo(), animal);
			} else {
				femaleAnimalsMap.put(animal.getSequnceNo(), animal);
				femaleAanimals.add(animal);
			}
			System.out.println(animal.getGender() + "\t" + animal.getAnimalNo());
		});
		dto.setMaleAnimals(maleAnimals);
		dto.setFemaleAnimals(femaleAanimals);

		Map<Integer, SubGroupInfo> subGroupOrde = accessionDao.studyAllSubgroups(study);
		maleGroups = subGroupOrde.size();
		feMaleGroups = subGroupOrde.size();

		System.out.println(maleAnimalsMap.size());
		System.out.println(femaleAnimalsMap.size());
		if ((maleAnimalsMap.size() + femaleAnimalsMap.size()) >= study.getSubjects()) {
			List<SubGroupAnimalsInfo> subGroupAnimalsInfo = expermentalDesignDao.allSubGroupAnimalsInfos(studyId);
			Map<String, Map<Integer, SubGroupAnimalsInfo>> genderWiseGroupsWithOrder = new HashMap<>();
			Map<String, Integer> totalAnimalForAllSubGroups = new HashMap<>();
			if (gender.equals("Both")) {
				totalAnimalForAllSubGroups.put("Male", 0);
				totalAnimalForAllSubGroups.put("Female", 0);
			} else if (gender.equals("Female")) {
				totalAnimalForAllSubGroups.put("Female", 0);
			} else if (gender.equals("Male")) {
				totalAnimalForAllSubGroups.put("Male", 0);
			}

			int noOfAnimals = 0;
			for (SubGroupAnimalsInfo sub : subGroupAnimalsInfo) {
				if (sub.getGender().equals(gender) || gender.equals("Both")) {
					int start = Integer.parseInt(sub.getFrom());
					int end = Integer.parseInt(sub.getTo());
					for (; start <= end; start++) {
						sub.getNumbers().add(start);
					}
					Map<Integer, SubGroupAnimalsInfo> subGrops = genderWiseGroupsWithOrder.get(sub.getGender());
					if (subGrops == null)
						subGrops = new HashMap<>();
					subGrops.put(sub.getSequenceNo(), sub);
					genderWiseGroupsWithOrder.put(sub.getGender(), subGrops);

					noOfAnimals += sub.getCount();
					groups.put(sub.getGroup().getId(), sub.getGroup());

					System.out.println(sub.getSequenceNo() + "\t" + sub.getGroup().getGroupNo() + "\t"
							+ sub.getGroup().getGroupName());
					assignedAnimalsToSubGroup.put(sub.getId(), 0);

					totalAnimalForAllSubGroups.put(sub.getGender(),
							totalAnimalForAllSubGroups.get(sub.getGender()) + sub.getCount());
				}

			}
			dto.setGenderWiseGroupsWithOrder(genderWiseGroupsWithOrder);

			if (gender.equals("Both")) {
				if (maleAnimalsMap.size() >= totalAnimalForAllSubGroups.get("Male")
						&& femaleAnimalsMap.size() >= totalAnimalForAllSubGroups.get("Female")) {
					dto = generateGenderRamdamizationBoth(dto, gender, maleAnimalsMap, totalAnimalForAllSubGroups,
							femaleAnimalsMap);
				} else if (maleAnimalsMap.size() < totalAnimalForAllSubGroups.get("Male")) {
					dto.setErrorCode(2); // error Message : accession no done for required no of animals
					dto.setErrorMessage("Required " + (totalAnimalForAllSubGroups.get("Male") - maleAnimalsMap.size())
							+ " Male Animal");
				} else if (femaleAnimalsMap.size() < totalAnimalForAllSubGroups.get("Female")) {
					dto.setErrorCode(2); // error Message : accession no done for required no of animals
					dto.setErrorMessage("Required "
							+ (totalAnimalForAllSubGroups.get("Female") - femaleAnimalsMap.size()) + " Female Animal");
				} else {
					dto.setErrorCode(2); // error Message : accession no done for required no of animals
					dto.setErrorMessage("Required " + (totalAnimalForAllSubGroups.get("Male") - maleAnimalsMap.size())
							+ " Male Animal and Required "
							+ (totalAnimalForAllSubGroups.get("Female") - femaleAnimalsMap.size()) + " Female Animal");
				}
			} else if (gender.equals("Female")) {
				if (femaleAnimalsMap.size() >= totalAnimalForAllSubGroups.get("Female")) {
					dto = generateGenderRamdamizationFemale(dto, gender, maleAnimalsMap, totalAnimalForAllSubGroups,
							femaleAnimalsMap);
				} else if (femaleAnimalsMap.size() < totalAnimalForAllSubGroups.get("Female")) {
					dto.setErrorCode(2); // error Message : accession no done for required no of animals
					dto.setErrorMessage("Required "
							+ (totalAnimalForAllSubGroups.get("Female") - femaleAnimalsMap.size()) + " Female Animal");
				}
			} else if (gender.equals("Male")) {
				if (maleAnimalsMap.size() >= totalAnimalForAllSubGroups.get("Male")) {
					dto = generateGenderRamdamizationMale(dto, gender, maleAnimalsMap, totalAnimalForAllSubGroups,
							femaleAnimalsMap);
				} else if (maleAnimalsMap.size() < totalAnimalForAllSubGroups.get("Male")) {
					dto.setErrorCode(2); // error Message : accession no done for required no of animals
					dto.setErrorMessage("Required " + (totalAnimalForAllSubGroups.get("Male") - maleAnimalsMap.size())
							+ " Male Animal");
				}
			}
		} else {
			dto.setErrorCode(1); // error Message : accession no done for required no of animals
			dto.setErrorMessage("Accession no done for required no of animals");
		}

		return dto;
	}

	private RadomizationDto generateGenderRamdamizationBoth(RadomizationDto dto, String gender,
			SortedMap<Integer, StudyAnimal> maleAnimalsMap, Map<String, Integer> totalAnimalForAllSubGroups,
			SortedMap<Integer, StudyAnimal> femaleAnimalsMap) {
		StaticData activityWeightForm = statusDao.staticData(StatusMasterCodes.ACTITYORCRF.toString(),
				StatusMasterCodes.ACTIVITYWEIGHTFROM.toString());
		// TODO Auto-generated method stub
		Crf crf = crfDao.checkCrf(activityWeightForm.getFieldValue());
		dto = meanWithWeigths(maleAnimalsMap, crf, totalAnimalForAllSubGroups, "Male", dto);
		dto = meanWithWeigths(femaleAnimalsMap, crf, totalAnimalForAllSubGroups, "Female", dto);
		dto.setRandamizaitonSheetMale(randamizaitonSheet(dto.getMaleAnimals(), "M"));
		dto.setRandamizaitonSheetFemale(randamizaitonSheet(dto.getFemaleAnimals(), "F"));
		dto.setRandamizaitonSheetMaleAscedning(randamizaitonSheetAssending(dto.getRandamizaitonSheetMale()));
		dto.setRandamizaitonSheetFemaleAscedning(randamizaitonSheetAssending(dto.getRandamizaitonSheetFemale()));
		if (dto.getRandamizaitonSheetMaleAscedning().getAnimals().size() >= totalAnimalForAllSubGroups.get("Male")
				&& dto.getRandamizaitonSheetFemaleAscedning().getAnimals().size() >= totalAnimalForAllSubGroups
						.get("Female")) {
			dto = randamizatoinGenderBasedToGroup1stPahse(dto, gender);
			return dto;
		} else if (dto.getRandamizaitonSheetMaleAscedning().getAnimals().size() < totalAnimalForAllSubGroups
				.get("Male")) {
			dto.setErrorCode(2); // error Message : accession no done for required no of animals
			dto.setErrorMessage("Required " + (totalAnimalForAllSubGroups.get("Male")
					- dto.getRandamizaitonSheetMaleAscedning().getAnimals().size()) + " Male Animal");
		} else if (dto.getRandamizaitonSheetFemaleAscedning().getAnimals().size() < totalAnimalForAllSubGroups
				.get("Female")) {
			dto.setErrorCode(2); // error Message : accession no done for required no of animals
			dto.setErrorMessage(
					"Required "
							+ (totalAnimalForAllSubGroups.get("Female")
									- dto.getRandamizaitonSheetFemaleAscedning().getAnimals().size())
							+ " Female Animal");
		} else {
			dto.setErrorCode(2); // error Message : accession no done for required no of animals
			dto.setErrorMessage(
					"Required "
							+ (totalAnimalForAllSubGroups.get("Male")
									- dto.getRandamizaitonSheetMaleAscedning().getAnimals().size())
							+ " Male Animal and Required "
							+ (totalAnimalForAllSubGroups.get("Female")
									- dto.getRandamizaitonSheetFemaleAscedning().getAnimals().size())
							+ " Female Animal");
		}
		return dto;
	}

	private RadomizationDto generateGenderRamdamizationFemale(RadomizationDto dto, String gender,
			SortedMap<Integer, StudyAnimal> maleAnimalsMap, Map<String, Integer> totalAnimalForAllSubGroups,
			SortedMap<Integer, StudyAnimal> femaleAnimalsMap) {
		StaticData activityWeightForm = statusDao.staticData(StatusMasterCodes.ACTITYORCRF.toString(),
				StatusMasterCodes.ACTIVITYWEIGHTFROM.toString());
		// TODO Auto-generated method stub
		Crf crf = crfDao.checkCrf(activityWeightForm.getFieldValue());
		dto = meanWithWeigths(femaleAnimalsMap, crf, totalAnimalForAllSubGroups, "Female", dto);
		if (dto.getFemaleAnimals().size() > 0) {
			dto.setRandamizaitonSheetFemale(randamizaitonSheet(dto.getFemaleAnimals(), "F"));
			dto.setRandamizaitonSheetFemaleAscedning(randamizaitonSheetAssending(dto.getRandamizaitonSheetFemale()));
			if (dto.getRandamizaitonSheetFemaleAscedning().getAnimals().size() >= totalAnimalForAllSubGroups
					.get("Female")) {
				dto = randamizatoinGenderBasedToGroup1stPahse(dto, gender);
				return dto;
			} else if (dto.getRandamizaitonSheetFemaleAscedning().getAnimals().size() < totalAnimalForAllSubGroups
					.get("Female")) {
				dto.setErrorCode(2); // error Message : accession no done for required no of animals
				dto.setErrorMessage(
						"Required "
								+ (totalAnimalForAllSubGroups.get("Female")
										- dto.getRandamizaitonSheetFemaleAscedning().getAnimals().size())
								+ " Female Animal");
			}
		} else {
			dto.setErrorCode(2); // error Message : accession no done for required no of animals
			dto.setErrorMessage("Required " + (totalAnimalForAllSubGroups.get("Female")) + " Female Animal");
		}

		return dto;
	}

	private RadomizationDto generateGenderRamdamizationMale(RadomizationDto dto, String gender,
			SortedMap<Integer, StudyAnimal> maleAnimalsMap, Map<String, Integer> totalAnimalForAllSubGroups,
			SortedMap<Integer, StudyAnimal> femaleAnimalsMap) {
//		StaticData activityWeightForm = statusDao.staticData(StatusMasterCodes.ACTITYORCRF.toString(),
//				StatusMasterCodes.ACTIVITYWEIGHTFROM.toString());
		// TODO Auto-generated method stub
		Crf crf = crfDao.checkCrf(environment.getRequiredProperty("accessionAnimalWeightFrom").toString());
		dto = meanWithWeigths(maleAnimalsMap, crf, totalAnimalForAllSubGroups, "Male", dto);
		if (dto.getMaleAnimals().size() > 0) {
			dto.setRandamizaitonSheetMale(randamizaitonSheet(dto.getMaleAnimals(), "M"));
			dto.setRandamizaitonSheetMaleAscedning(randamizaitonSheetAssending(dto.getRandamizaitonSheetMale()));
			if (dto.getRandamizaitonSheetMaleAscedning().getAnimals().size() >= totalAnimalForAllSubGroups
					.get("Male")) {
				dto = randamizatoinGenderBasedToGroup1stPahse(dto, gender);
				return dto;
			} else if (dto.getRandamizaitonSheetMaleAscedning().getAnimals().size() < totalAnimalForAllSubGroups
					.get("Male")) {
				dto.setErrorCode(2); // error Message : accession no done for required no of animals
				dto.setErrorMessage(
						"Required "
								+ (totalAnimalForAllSubGroups.get("Male")
										- dto.getRandamizaitonSheetMaleAscedning().getAnimals().size())
								+ " Male Animal");
			}
		} else {
			dto.setErrorCode(2); // error Message : accession no done for required no of animals
			dto.setErrorMessage("Required " + totalAnimalForAllSubGroups.get("Male") + " Male Animal");
		}

		return dto;
	}

	private RadomizationDto randamizatoinGenderBasedToGroup1stPahse(RadomizationDto dto, String gender) {
		if (gender.equals("Both")) {
			skip = 0;
			RadamizationAllData genderBasedAnimalGroups = randamizatoinGenderBasedToGroup2ndPahse(
					dto.getRandamizaitonSheetMaleAscedning(), dto.getGenderWiseGroupsWithOrder().get("Male"));
			dto = generateRandamizationTableView(dto, genderBasedAnimalGroups, gender);
			skip = 0;
			genderBasedAnimalGroups = randamizatoinGenderBasedToGroup2ndPahse(
					dto.getRandamizaitonSheetFemaleAscedning(), dto.getGenderWiseGroupsWithOrder().get("Female"));
			dto = generateRandamizationTableView(dto, genderBasedAnimalGroups, gender);
			return dto;
		} else if (gender.equals("Female")) {
			skip = 0;
			RadamizationAllData genderBasedAnimalGroups = randamizatoinGenderBasedToGroup2ndPahse(
					dto.getRandamizaitonSheetFemaleAscedning(), dto.getGenderWiseGroupsWithOrder().get("Female"));
			dto = generateRandamizationTableView(dto, genderBasedAnimalGroups, gender);
			return dto;
		} else {
			skip = 0;
			RadamizationAllData genderBasedAnimalGroups = randamizatoinGenderBasedToGroup2ndPahse(
					dto.getRandamizaitonSheetMaleAscedning(), dto.getGenderWiseGroupsWithOrder().get("Male"));
			dto = generateRandamizationTableView(dto, genderBasedAnimalGroups, gender);
			return dto;
		}

	}

	private RadamizationAllData randamizaitonSheetAssending(RadamizationAllData sheet) {
		List<StudyAnimal> animalsNew = new ArrayList<>();
		List<StudyAnimal> animals = sheet.getAnimals();
		for (StudyAnimal aniamal : animals) {
			System.out.println(aniamal.getWeight() + "\t" + sheet.getMaximum() + "\t" + aniamal.getWeight() + "\t"
					+ sheet.getMinimum());
			if (aniamal.getWeight() <= sheet.getMaximum() && aniamal.getWeight() >= sheet.getMinimum()) {
				animalsNew.add(aniamal);
			}
		}
		sheet.setAnimals(animalsNew);
		return sheet;
	}

	private RadamizationAllData randamizaitonSheet(List<StudyAnimal> animals, String genderCode) {
		RadamizationAllData sheet = new RadamizationAllData();
		sheet.setGender(genderCode);
		Double total = 0d;
		Double min = 0d;
		Double max = 0d;
		Map<Long, StudyAnimal> map = new HashMap<>();
		boolean flag = false;
		for (StudyAnimal animal : animals) {
			map.put(animal.getId(), animal);
			total += animal.getWeight();
			if (flag) {
				if (min > animal.getWeight())
					min = animal.getWeight();
				if (max < animal.getWeight())
					max = animal.getWeight();
			} else {
				min = animal.getWeight();
				max = min;
				flag = true;
			}
		}
		sheet.setMinimum(min);
		sheet.setMaximum(max);
		sheet.setMean(new BigDecimal(total / animals.size()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//				sheet.setMean(total / animals.size());
		Double persent = ((sheet.getMean() * 20) / 100);
		sheet.setStart(new BigDecimal(sheet.getMean() - persent).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//				sheet.setStart(sheet.getMean() - persent);
		sheet.setEnd(new BigDecimal(sheet.getMean() + persent).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//				sheet.setEnd(sheet.getMean() + persent);
		System.out.println("start : " + sheet.getStart());
		System.out.println("end : " + sheet.getEnd());
		for (StudyAnimal animal : animals) {
			System.out.println(sheet.getStart() + "\t" + animal.getWeight() + "\t" + sheet.getEnd());
			if (animal.getWeight() < sheet.getStart() || animal.getWeight() > sheet.getEnd()) {
				System.out.println(animal.getWeight());
				map.remove(animal.getId());
//						animals.remove(animal);
			}
		}
		animals = new ArrayList<>();
		for (Map.Entry<Long, StudyAnimal> m : map.entrySet()) {
			animals.add(m.getValue());
		}
		sheet.setAnimals(animals);
		return sheet;

	}

	private RadomizationDto meanWithWeigths(SortedMap<Integer, StudyAnimal> animalsMap, Crf crf,
			Map<String, Integer> totalAnimalForAllSubGroups, String gender, RadomizationDto dto) {
		List<StudyAnimal> list = new ArrayList<>();
//		StaticData activityWeightForm = statusDao.staticData(StatusMasterCodes.ACTITYORCRF.toString(),
//				StatusMasterCodes.ACTIVITYWEIGHTFROM.toString());
//		StaticData activityWeightSection = statusDao.staticData(StatusMasterCodes.ACTITYORCRF.toString(),
//				StatusMasterCodes.AMBULATORY.toString());
//		StaticData activityWeightField = statusDao.staticData(StatusMasterCodes.ACTITYORCRF.toString(),
//				StatusMasterCodes.TOTAL.toString());
//		StatusMaster approveStatus = statusDao.statusMaster(StatusMasterCodes.APPROVED.toString());
		if (animalsMap.size() > 0) {
			System.out.println(animalsMap.size());
			List<Long> studyAnimalIds = new ArrayList<>();
			animalsMap.forEach((k, v) -> {
				studyAnimalIds.add(v.getId());
				System.out.println(v.getGender() + "\t" + v.getAnimalNo());
			});
			List<AccessionAnimalsDataEntryDetails> dataEntered = accessionDao.accessionAnimalsDataEntryDetails(crf,
					studyAnimalIds, null);
			System.out.println("Total : " + dataEntered.size());
			Map<Long, List<AccessionAnimalsDataEntryDetails>> map = new HashMap<>();
			dataEntered.forEach((l) -> {
				List<AccessionAnimalsDataEntryDetails> li = map.get(l.getAnimal().getId());
				if (li == null)
					li = new ArrayList<>();
				li.add(l);
				map.put(l.getAnimal().getId(), li);
			});

			List<AccessionAnimalsDataEntryDetails> approved = new ArrayList<>();
			for (Map.Entry<Long, List<AccessionAnimalsDataEntryDetails>> m : map.entrySet()) {
				List<AccessionAnimalsDataEntryDetails> withStatus = m.getValue();
				List<AccessionAnimalsDataEntryDetails> smap = new ArrayList<>();
				for (AccessionAnimalsDataEntryDetails each : withStatus) {
					if (each.getStatus().getStatusCode().equals(StatusMasterCodes.APPROVED.toString()))
						smap.add(each);
				}
				Collections.sort(smap);
				if (smap.size() > 0)
					approved.add(smap.get(0));
			}
			System.out.println("approved : " + dataEntered.size());
			// check required animal and data entered animal size
			if (dataEntered.size() >= totalAnimalForAllSubGroups.get(gender)) {
				// check required animal and data reviewed animal size
				if (approved.size() >= totalAnimalForAllSubGroups.get(gender)) {
					// and there Weights
					approved.forEach((each) -> {
						System.out.println(each.getId() + "\t" + each.getAnimal().getAnimalId());
						StudyAnimal animal = each.getAnimal();
						if (animal.getAnimalDataMeataData() == null) {
							animal.setAnimalDataMeataData(each);
//							animal.setActivityData(accessionDao.weightStudyAccessionCrfSectionElementData(each,
//							environment.getRequiredProperty("accessionAnimalWeightSectionName"),
//							environment.getRequiredProperty("accessionAnimalWeightSectionElementName")));
//					System.out.println(
//							"animal.getActivityData().getValue() : " + animal.getActivityData().getValue());
//					animal.setWeight(Double.parseDouble(animal.getActivityData().getValue()));
							studyDao.updateAnimal(animal);
						}

						list.add(animal);
					});

				} else {
					// accession data approval no done for required no of animals
					dto.setErrorCode(4); // error Message : accession data approval no done for required no of animals
					dto.setErrorMessage("Accession data approval not done for required no of animals :"
							+ totalAnimalForAllSubGroups.get(gender));
				}
			} else {
				// accession data entry no done for required no of animals
				dto.setErrorCode(3); // error Message : accession data entry no done for required no of animals
				dto.setErrorMessage("Accession data entry not done for required no of animals");
			}
		}
		if (gender.equals("Male"))
			dto.setMaleAnimals(list);
		else
			dto.setFemaleAnimals(list);
		return dto;
	}

	private RadomizationDto generateRandamizationTableAllByGenderAssending(RadomizationDto dto,
			List<RadamizationAllData> dataOrderList) {

		// all the animals with ascending
		for (int index = 0; index < dataOrderList.size(); index++) {
			RadamizationAllData rad = dataOrderList.get(index);
			if (rad.getGender().equals("M")) {
				dto.setRandamizaitonSheetMaleAscedning(rad);
			} else {
				dto.setRandamizaitonSheetFemaleAscedning(rad);
			}
		}
		return dto;
	}

	private RadomizationDto generateRandamizationTableAllByGender(RadomizationDto dto,
			List<RadamizationAllData> dataList) {
		// all the animals with out ascending
		for (RadamizationAllData rad : dataList) {
			System.out.println(rad.getGender());
			if (rad.getGender().equals("M"))
				dto.setRandamizaitonSheetMale(rad);
			else
				dto.setRandamizaitonSheetFemale(rad);
		}
		return dto;
	}

	private RadomizationDto randamizatoinGenderBasedToGroup1stPahse(RadomizationDto dto,
			Map<String, Map<Integer, SubGroupAnimalsInfo>> genderWiseGroupsWithOrder,
			List<RadamizationAllData> dataOrderList, List<RadamizationAllData> dataList,
			List<RadamizationAllData> dataOrderList2, StudyMaster study, String gender) {
		for (RadamizationAllData genderBase : dataOrderList) {
			if (genderBase.getGender().equals("M")) {
				RadamizationAllData genderBasedAnimalGroups = randamizatoinGenderBasedToGroup2ndPahse(genderBase,
						genderWiseGroupsWithOrder.get("Male"));
				dto = generateRandamizationTableView(dto, genderBasedAnimalGroups, gender);
//				dto.setRandamizaitonSheetFemaleGruoup(genderBasedAnimalGroups);
			} else {
				RadamizationAllData genderBasedAnimalGroups = randamizatoinGenderBasedToGroup2ndPahse(genderBase,
						genderWiseGroupsWithOrder.get("Female"));
				dto = generateRandamizationTableView(dto, genderBasedAnimalGroups, gender);
			}
		}
		return dto;
	}

	private RadomizationDto generateRandamizationTableView(RadomizationDto dto, RadamizationAllData rad,
			String gender) {
		List<RadamizationAllData> radamizationSheetsGenderGroupWise = new ArrayList<>();

		Map<Long, List<StudyAnimal>> groupBased = new HashMap<>();
		Map<Integer, Long> groupBasedOnNo = new HashMap<>();
		List<StudyAnimal> animals = rad.getAnimals();
		animals = generateAnimalNo(animals, dto.getStudy());
		for (StudyAnimal animal : animals) {
			System.out.println(animal.getGroupInfo());
			if (animal.getGroupInfo() == null)
				System.out.println("asdf");
			try {
				List<StudyAnimal> groupAnimals = groupBased.get(animal.getGroupInfo().getId());
				if (groupAnimals == null)
					groupAnimals = new ArrayList<>();
				groupAnimals.add(animal);
				groupBased.put(animal.getGroupInfo().getId(), groupAnimals);
				groupBasedOnNo.put(animal.getGroupInfo().getGroupNo(), animal.getGroupInfo().getId());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

		Set<Long> rowSetting = new HashSet<>();
		for (Map.Entry<Integer, Long> mp : groupBasedOnNo.entrySet()) {
			Long key = mp.getValue();
			RadamizationAllData radtemp = new RadamizationAllData();
			List<StudyAnimal> animalss = groupBased.get(mp.getValue());

			if (!rowSetting.contains(key)) {
				for (StudyAnimal sa : animalss) {
					sa.setGroupCount(animalss.size());
					rowSetting.add(key);
					break;
				}
			}
			radtemp.setAnimals(animalss);
			radtemp.setGroupMean(new BigDecimal(calculateMeanOfAnimals(animalss)).setScale(2, BigDecimal.ROUND_HALF_UP)
					.doubleValue());
//				radtemp.setGroupMean(calculateMeanOfAnimals(map.getValue()));
			radtemp.setGrouoSD(new BigDecimal(caluclateStandaredDeviationOfAnimals(animalss, radtemp.getGroupMean()))
					.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//				radtemp.setGrouoSD(caluclateStandaredDeviationOfAnimals(map.getValue()));
			radtemp.setGroup(groups.get(key));
			radtemp.setGorupNo(radtemp.getGroup().getGroupNo());
			radamizationSheetsGenderGroupWise.add(radtemp);
		}
		/*
		 * for (Map.Entry<Long, List<StudyAnimal>> map : groupBased.entrySet()) {
		 * RadamizationAllData radtemp = new RadamizationAllData(); List<StudyAnimal>
		 * animalss = map.getValue();
		 * 
		 * if (!rowSetting.contains(map.getKey())) { for (StudyAnimal sa : animalss) {
		 * sa.setGroupCount(animalss.size()); rowSetting.add(map.getKey()); break; } }
		 * radtemp.setAnimals(animalss); radtemp.setGroupMean(new
		 * BigDecimal(calculateMeanOfAnimals(map.getValue())) .setScale(2,
		 * BigDecimal.ROUND_HALF_UP).doubleValue()); //
		 * radtemp.setGroupMean(calculateMeanOfAnimals(map.getValue()));
		 * radtemp.setGrouoSD(new
		 * BigDecimal(caluclateStandaredDeviationOfAnimals(map.getValue(),
		 * radtemp.getGroupMean())) .setScale(2,
		 * BigDecimal.ROUND_HALF_UP).doubleValue()); //
		 * radtemp.setGrouoSD(caluclateStandaredDeviationOfAnimals(map.getValue()));
		 * radtemp.setGroup(groups.get(map.getKey()));
		 * radtemp.setGorupNo(radtemp.getGroup().getGroupNo());
		 * radamizationSheetsGenderGroupWise.add(radtemp); }
		 */

		if (rad.getGender().equals("M"))
			dto.setRandamizaitonSheetMaleGruoup(radamizationSheetsGenderGroupWise);
		else
			dto.setRandamizaitonSheetFemaleGruoup(radamizationSheetsGenderGroupWise);
		return dto;
	}

	SortedMap<Integer, SubGroupAnimalsInfo> orderBasedSubGroupsClone = new TreeMap<>();
	SortedMap<Integer, SubGroupAnimalsInfo> orderBasedSubGroups = new TreeMap<>();
	private int subGroupSize;
	int skip = 0;

	private Map<Long, Integer> animalRepeat = new HashMap<>();

	private RadamizationAllData randamizatoinGenderBasedToGroup2ndPahse(RadamizationAllData genderBase,
			Map<Integer, SubGroupAnimalsInfo> orderBasedSubGroups) {
		Map<Integer, SubGroupAnimalsInfo> temp = new TreeMap<>();
		int count = 0;
		for (Map.Entry<Integer, SubGroupAnimalsInfo> sg : orderBasedSubGroups.entrySet()) {
			System.out.println(
					sg.getKey() + "\t" + sg.getValue().getSubGroup().getGender() + "\t" + sg.getValue().getGender());
			temp.put(++count, sg.getValue());
		}
		orderBasedSubGroups = temp;
		for (Map.Entry<Integer, SubGroupAnimalsInfo> sg : orderBasedSubGroups.entrySet()) {
			System.out.println(
					sg.getKey() + "\t" + sg.getValue().getSubGroup().getGender() + "\t" + sg.getValue().getGender());
		}

		List<StudyAnimal> result = new ArrayList<>();

		orderBasedSubGroupsClone = new TreeMap<>(orderBasedSubGroups);
		orderBasedSubGroups = new TreeMap<>(orderBasedSubGroups);
		// TODO Auto-generated method stub
		List<StudyAnimal> animals = genderBase.getAnimals();
		Collections.sort(animals);

		subGroupSize = orderBasedSubGroups.size();
		for (int i = 0; i < animals.size();) {
			StudyAnimal animal = animals.get(i);
//			if(skip>=7)
//				System.out.println(skip);
			System.out.println(animal.getAnimalNo() + "\t" + animal.getWeight());
//			if(animal.getAnimalNo().equalsIgnoreCase("VPCD/0822/Y2-1563")) {
//				System.out.println("erro");
//			}
			if (orderBasedSubGroupsClone.size() == 0) {
				orderBasedSubGroupsClone.putAll(orderBasedSubGroups);
				skip++;
				if (skip == subGroupSize) {
					skip = 0;
				}
			}
			animal = assigntoGroup(animal, orderBasedSubGroupsClone);
			if (animal.getGroupInfo() != null) {
				result.add(animal);
				i++;
			} else {
				if (animal.getGender().equals("Female")) {
					if (animalRepeat.containsKey(animal.getId())) {
						if (animalRepeat.get(animal.getId()) == maleGroups) {
							i++;
						}
					} else {
						animalRepeat.put(animal.getId(), 0);
					}
				} else {
					if (animalRepeat.containsKey(animal.getId())) {
						if (animalRepeat.get(animal.getId()) == feMaleGroups) {
							i++;
						}
					} else {
						animalRepeat.put(animal.getId(), 0);
					}
				}
			}
			orderBasedSubGroupsClone.remove(animal.getIndex());
		}
		genderBase.setAnimals(result);
		return genderBase;
	}

	private StudyAnimal assigntoGroup(StudyAnimal animal, Map<Integer, SubGroupAnimalsInfo> orderBasedSubGroups) {

		SubGroupAnimalsInfo skipdGroup = null;
		if (skip >= 1) {
			skipdGroup = orderBasedSubGroups.get(skip);
			orderBasedSubGroups.remove(skip);
		}
		int index = 0;
		for (Map.Entry<Integer, SubGroupAnimalsInfo> subgroup : orderBasedSubGroupsClone.entrySet()) {
			if (index == 0)
				index = subgroup.getKey();
			System.out.println(subgroup.getValue().getSubGroup().getSubGroupNo() + "\t"
					+ subgroup.getValue().getSubGroup().getName());
			int count = assignedAnimalsToSubGroup.get(subgroup.getValue().getId());
			if (count < subgroup.getValue().getCount()) {
				animal.setGroupInfo(subgroup.getValue().getGroup());
				animal.setSubGrop(subgroup.getValue());
				assignedAnimalsToSubGroup.put(subgroup.getValue().getId(), ++count);
				animal.setIndex(subgroup.getKey());
				studyDao.updateAnimal(animal);
				return animal;
			}
		}

		if (skipdGroup != null) {
			int count = assignedAnimalsToSubGroup.get(skipdGroup.getId());
			if (count < skipdGroup.getCount()) {
				System.out
						.println(skipdGroup.getSubGroup().getSubGroupNo() + "\t" + skipdGroup.getSubGroup().getName());
				animal.setGroupInfo(skipdGroup.getGroup());
				animal.setSubGrop(skipdGroup);
				assignedAnimalsToSubGroup.put(skipdGroup.getId(), ++count);
				studyDao.updateAnimal(animal);
				skip++;
				return animal;
			}
			animal.setIndex(index);
			return animal;
		} else {
			animal.setIndex(index);
			return animal;
		}

	}

	private Double calculateMeanOfAnimals(List<StudyAnimal> dataList) {
		double sum = 0.0;
		for (StudyAnimal num : dataList) {
			sum += num.getWeight();
		}
		double mean = sum / dataList.size();
		return mean;
	}

	private Double caluclateStandaredDeviationOfAnimals(List<StudyAnimal> dataList, Double mean) {
		Double sd = 0.00;
		try {
			double standardDeviation = 0.0;
			for (StudyAnimal num : dataList) {
				System.out.println(num.getWeight() - mean);
				System.out.println(Math.pow(num.getWeight() - mean, 2));
				standardDeviation += Math.pow(num.getWeight() - mean, 2);
			}

			sd = Math.sqrt(standardDeviation / (dataList.size() - 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sd;
	}

//	private RadamizationAllData generateAnimalNo(RadamizationAllData rad, StudyMaster study) {
//		List<StudyAnimal> animals = rad.getAnimals();
//		for(StudyAnimal animal : animals) {
//			String no = animalNo + "";
//			if (animalNo < 10)
//				no = "00" + animalNo;
//			else if (animalNo < 100)
//				no = "0" + animalNo;
//			no = study.getStudyNo().substring(study.getStudyNo().length() - 4, study.getStudyNo().length()) + "/" + no;
//			animal.setPermanentNo(no);
//			animal.setAnimalId(animalNo);
//			animalNo++;
//			if(study.isCalculationBased()) {
//				double doseAmount = study.getDoseVolume()/study.getConcentration();
//				if(study.getWeightUnits().getCode().equals(StatusMasterCodes.GR.toString()))
//					animal.setDoseAmount(animal.getWeight()*(doseAmount/1000));
//				else
//					animal.setDoseAmount(animal.getWeight()*doseAmount);
//			}
//		}
//		rad.setAnimals(animals);
//		return rad;
//	}

	private List<StudyAnimal> generateAnimalNo(List<StudyAnimal> animals, StudyMaster study) {
		SortedMap<Integer, List<StudyAnimal>> map = new TreeMap<>();
		animals.forEach((ani) -> {
			if (ani.getGroupInfo() != null) {
				List<StudyAnimal> ania = map.get(ani.getGroupInfo().getGroupNo());
				if (ania == null)
					ania = new ArrayList<>();
				ania.add(ani);
				map.put(ani.getGroupInfo().getGroupNo(), ania);
			}
		});
		List<StudyAnimal> animalsWithPeriminentNo = new ArrayList<>();
		for (Map.Entry<Integer, List<StudyAnimal>> m : map.entrySet()) {
			List<StudyAnimal> anis = m.getValue();
			for (StudyAnimal animal : anis) {
				System.out.println(animal.getSubGrop().getNumbers());
				if (animal.getSubGrop().getNumbers().size() > 0) {
					int animalNo = animal.getSubGrop().getNumbers().get(0);
					animal.getSubGrop().getNumbers().remove(0);
					System.out.println(animalNo + "\t" + animal.getSubGrop().getNumbers());
					String no = animalNo + "";
					if (animalNo < 10)
						no = "00" + animalNo;
					else if (animalNo < 100)
						no = "0" + animalNo;
					no = study.getStudyNo().substring(study.getStudyNo().length() - 4, study.getStudyNo().length())
							+ "/" + no;
					animal.setPermanentNo(no);
					animal.setAnimalId(animalNo);
					animalNo++;
					if (study.isCalculationBased()) {
						double doseAmount = animal.getSubGrop().getConcentration()
								/ animal.getSubGrop().getDoseVolume();
						if (study.getWeightUnits().getCode().equals(StatusMasterCodes.GR.toString()))
							animal.setDoseAmount(new BigDecimal(animal.getWeight() * (doseAmount / 1000))
									.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//							animal.setDoseAmount(animal.getWeight()*(doseAmount/1000));
						else
							animal.setDoseAmount(new BigDecimal(animal.getWeight() * doseAmount)
									.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//							animal.setDoseAmount(animal.getWeight()*doseAmount);
					}
					System.out.println(animal.getAnimalNo() + "\t" + animal.getPermanentNo());
					studyDao.updateAnimal(animal);
					animalsWithPeriminentNo.add(animal);
				}

			}

		}
		return animalsWithPeriminentNo;
	}

	private List<RadamizationAllData> meanBasedRemoceAnimalIfWeightNotInRage(List<RadamizationAllData> dataOrderList) {
		for (RadamizationAllData sheet : dataOrderList) {
			Double total = 0d;
			Double min = 0d;
			Double max = 0d;
			List<StudyAnimal> animals = sheet.getAnimals();
			Map<Long, StudyAnimal> map = new HashMap<>();
			boolean flag = false;
			for (StudyAnimal animal : animals) {
				map.put(animal.getId(), animal);
				total += animal.getWeight();
				if (flag) {
					if (min > animal.getWeight())
						min = animal.getWeight();
					if (max < animal.getWeight())
						max = animal.getWeight();
				} else {
					min = animal.getWeight();
					max = min;
					flag = true;
				}
			}
			sheet.setMinimum(min);
			sheet.setMaximum(max);
			sheet.setMean(new BigDecimal(total / animals.size()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//			sheet.setMean(total / animals.size());
			Double persent = ((sheet.getMean() * 20) / 100);
			sheet.setStart(
					new BigDecimal(sheet.getMean() - persent).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//			sheet.setStart(sheet.getMean() - persent);
			sheet.setEnd(new BigDecimal(sheet.getMean() + persent).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//			sheet.setEnd(sheet.getMean() + persent);
			System.out.println("start : " + sheet.getStart());
			System.out.println("end : " + sheet.getEnd());
			for (StudyAnimal animal : animals) {
				if (animal.getWeight() < sheet.getStart() || animal.getWeight() > sheet.getEnd()) {
					System.out.println(animal.getWeight());
					map.remove(animal.getId());
//					animals.remove(animal);
				}
			}
			animals = new ArrayList<>();
			for (Map.Entry<Long, StudyAnimal> m : map.entrySet()) {
				animals.add(m.getValue());
			}
			sheet.setAnimals(animals);
		}
		boolean flag = false;
		for (RadamizationAllData sheet : dataOrderList) {
			List<StudyAnimal> animalsNew = new ArrayList<>();
			List<StudyAnimal> animals = sheet.getAnimals();
			for (StudyAnimal aniamal : animals) {
				System.out.println(aniamal.getWeight() + "\t" + sheet.getMaximum() + "\t" + aniamal.getWeight() + "\t"
						+ sheet.getMinimum());
				if (aniamal.getWeight() <= sheet.getMaximum() && aniamal.getWeight() >= sheet.getMinimum()) {
					animalsNew.add(aniamal);
				}
			}
			sheet.setAnimals(animalsNew);
			if (animalsNew.size() != animals.size()) {
				flag = true;
			}
		}
		if (flag)
			return meanBasedRemoceAnimalIfWeightNotInRage(dataOrderList);
		else
			return dataOrderList;
	}

	private List<StudyAnimal> withOutSkipGroup(Map<Integer, SubGroupAnimalsInfo> subGroupWithNo,
			List<StudyAnimal> animals) {
		int index = 0;
		for (StudyAnimal animal : animals) {
			List<SubGroupAnimalsInfo> list = new ArrayList<>();
			for (Map.Entry<Integer, SubGroupAnimalsInfo> m : subGroupWithNo.entrySet())
				list.add(m.getValue());
			animal = addtoGroup(list, animal, index);
			index = animal.getIndex();
		}
		return animals;
	}

	private StudyAnimal addtoGroup(List<SubGroupAnimalsInfo> subGroupWithNo, StudyAnimal animal, int index) {
		int startindex = index;
		while (true) {
			if (subGroupWithNo.size() != 0) {
				SubGroupAnimalsInfo subgroup = subGroupWithNo.get(index);
				int count = assignedAnimalsToSubGroup.get(subgroup.getId());
				if (subgroup.getGender().equals(animal.getGender()) && count < subgroup.getCount()) {
					animal.setGroupInfo(subgroup.getGroup());
					animal.setSubGrop(subgroup);
					assignedAnimalsToSubGroup.put(subgroup.getId(), ++count);
					animal.setIndex(index);
					subGroupWithNo.remove(index);
					return animal;
				}
				index++;
				if (index == subGroupWithNo.size()) {
					index = 0;
				}
				if (index == startindex) {
					animal.setIndex(index);
					return animal;
				}
			} else {
				animal.setIndex(index);
				return animal;
			}
		}
	}

	int skipGrupNo = 1;
	SubGroupAnimalsInfo skipedGroup = null;
	Map<Long, Boolean> groupsStatus = new HashMap<>();

	private List<StudyAnimal> withSkipGroup(Map<Integer, SubGroupAnimalsInfo> subGroupWithNo,
			List<StudyAnimal> animals) {
		groupsStatus = new HashMap<>();
		skipedGroup = null;
		skipGrupNo = 1;
		resetGroupStatus(subGroupWithNo);
		skipedGroup = subGroupWithNo.get(skipedGroup);
		subGroupWithNo.remove(skipedGroup);

		int index = 0;
		for (StudyAnimal animal : animals) {
			List<SubGroupAnimalsInfo> list = new ArrayList<>();
			for (Map.Entry<Integer, SubGroupAnimalsInfo> m : subGroupWithNo.entrySet())
				list.add(m.getValue());
			animal = addtoGroup(list, animal, index);
			if (animal.getGroupInfo() == null) {
				int count = assignedAnimalsToSubGroup.get(skipedGroup.getId());
				if (skipedGroup.getGender().equals(animal.getGender()) && count < skipedGroup.getCount()) {
					animal.setGroupInfo(skipedGroup.getGroup());
					animal.setSubGrop(skipedGroup);
					assignedAnimalsToSubGroup.put(skipedGroup.getId(), ++count);
					animal.setIndex(index);
					subGroupWithNo.remove(index);
				}
			}
			if (animal.getGroupInfo() != null) {
				groupsStatus.put(animal.getSubGrop().getId(), true);
			}
			index = animal.getIndex();
			boolean flag = true;
			for (Map.Entry<Long, Boolean> m : groupsStatus.entrySet()) {
				if (!m.getValue())
					flag = false;
			}
			if (flag) {
				resetGroupStatus(subGroupWithNo);
				skipGrupNo++;
				if (skipGrupNo == subGroupWithNo.size())
					skipGrupNo = 0;
			}
		}
		return animals;
	}

	private void resetGroupStatus(Map<Integer, SubGroupAnimalsInfo> subGroupWithNo) {
		for (Map.Entry<Integer, SubGroupAnimalsInfo> m : subGroupWithNo.entrySet()) {
			groupsStatus.put(m.getValue().getSubGroup().getId(), false);
		}
	}

	/*
	 * private StudyAnimal addGroupOrSubgroup(Map<Integer, SubGroupAnimalsInfo>
	 * subGroupWithNo, StudyAnimal animal, int index, int startindex, int repeat) {
	 * 
	 * if ((index - 1) == subGroupWithNo.size()) { index = 1; } SubGroupAnimalsInfo
	 * subgroup = subGroupWithNo.get(index++); int count =
	 * assignedAnimalsToSubGroup.get(subgroup.getId()); //
	 * System.out.println(subgroup.getGender()+"   "+ animal.getGender()+"  " +
	 * count+" "+subgroup.getCount()); if
	 * (subgroup.getGender().equals(animal.getGender()) && count <
	 * subgroup.getCount()) { animal.setGroupInfo(subgroup.getGroup());
	 * animal.setSubGrop(subgroup); assignedAnimalsToSubGroup.put(subgroup.getId(),
	 * ++count); startIndex = index; if (animal.getGroupInfo() == null) {
	 * System.out.println(animal.getId()); ; } return animal; } else { if (index ==
	 * startindex) { if (repeat == 1) { return addGroupOrSubgroup(subGroupWithNo,
	 * animal, 1, startindex, repeat++); } else return animal; } else if ((index -
	 * 1) == subGroupWithNo.size()) { // scipGroup++; // if (scipGroup >
	 * subGroupWithNo.size()) // scipGroup = 0; index = 1; } return
	 * addGroupOrSubgroup(subGroupWithNo, animal, index, startindex, repeat); }
	 * 
	 * }
	 */

	/*
	 * private StudyAnimal addGroupOrSubgroup(Map<Integer, SubGroupAnimalsInfo>
	 * subGroupWithNo, StudyAnimal animal, int index, int startindex, int repeat) {
	 * if ((index - 1) == subGroupWithNo.size()) { index = 1; scipGroup++; if
	 * (scipGroup > subGroupWithNo.size()) scipGroup = 0; } SubGroupAnimalsInfo
	 * subgroup = subGroupWithNo.get(index++); if (subgroup == null)
	 * System.out.println("asdf"); if (subgroup.getSequenceNo() != scipGroup) { int
	 * count = assignedAnimalsToSubGroup.get(subgroup.getId()); //
	 * System.out.println(subgroup.getGender()+"   "+ animal.getGender()+"  " +
	 * count+" "+subgroup.getCount()); if
	 * (subgroup.getGender().equals(animal.getGender()) && count <
	 * subgroup.getCount()) { animal.setGroupInfo(subgroup.getGroup());
	 * animal.setSubGrop(subgroup); assignedAnimalsToSubGroup.put(subgroup.getId(),
	 * ++count); startIndex = index; if (animal.getGroupInfo() == null) {
	 * System.out.println(animal.getId()); ; } return animal; } else { if (index ==
	 * startindex) { if(repeat == 1) { return addGroupOrSubgroup(subGroupWithNo,
	 * animal, 1, startindex, repeat++); }else return animal; } else if ((index - 1)
	 * == subGroupWithNo.size()) { scipGroup++; if (scipGroup >
	 * subGroupWithNo.size()) scipGroup = 0; index = 1; } return
	 * addGroupOrSubgroup(subGroupWithNo, animal, index, startindex, repeat); } }
	 * else { if (index == startindex) { if(repeat == 1) { return
	 * addGroupOrSubgroup(subGroupWithNo, animal, 1, startindex, repeat++); }else
	 * return animal; } else if ((index - 1) == subGroupWithNo.size()) {
	 * scipGroup++; if (scipGroup > subGroupWithNo.size()) scipGroup = 0; index = 1;
	 * } return addGroupOrSubgroup(subGroupWithNo, animal, index, startindex,
	 * repeat); }
	 * 
	 * }
	 */

//	private StudyAnimal addGroupOrSubgroup(Map<Integer, SubGroupAnimalsInfo> subGroupWithNo, StudyAnimal animal,
//			int index, int startindex) {
//		System.out.println(animal.getAnimalNo());
//		SubGroupAnimalsInfo subgroup = subGroupWithNo.get(index++);
//		if (subgroup != null) {
//			int count = assignedAnimalsToSubGroup.get(subgroup.getId());
////			System.out.println(subgroup.getGender()+"   "+ animal.getGender()+"  " + count+" "+subgroup.getCount());
//			if (subgroup.getGender().equals(animal.getGender()) && count < subgroup.getCount()) {
//				animal.setGroupInfo(subgroup.getGroup());
//				animal.setSubGrop(subgroup);
//				assignedAnimalsToSubGroup.put(subgroup.getId(), ++count);
//				startIndex = index;
//				if (animal.getGroupInfo() == null) {
//					System.out.println(animal.getId());
//					;
//				}
//				return animal;
//			} else {
//				if (index == startindex) {
//					return animal;
//				} else if ((index - 1) == subGroupWithNo.size()) {
//					index = 1;
//				}
//				return addGroupOrSubgroup(subGroupWithNo, animal, index, startindex);
//			}
//		} else
//			return animal;
//
//	}

	private FromsStatusDto getFormStatusDtoDetails(StudyAccessionAnimals saa, StudyAcclamatizationData sad,
			int animalId, String status, FromsStatusDto fsDto) {
		Map<Long, String> crfMap = null;
		Map<Long, Map<String, String>> animalsMap = null;
		if (fsDto != null) {
			crfMap = fsDto.getCrfMap();
			animalsMap = fsDto.getAnimalsMap();
		} else {
			fsDto = new FromsStatusDto();
			crfMap = new HashMap<>();
			animalsMap = new HashMap<>();
		}
		try {
			crfMap.put(sad.getCrf().getId(), sad.getCrf().getName());
			Map<String, String> tempMap = new HashMap<>();
			String animalStr = saa.getPrefix() + "-";
			if (animalId <= 9)
				animalStr = animalStr + "000" + animalId;
			else if (animalId > 9 && animalId <= 99)
				animalStr = animalStr + "00" + animalId;
			else if (animalId > 99 && animalId <= 999)
				animalStr = animalStr + "0" + animalId;
			else
				animalStr = animalStr + animalId;
			if (saa.getGender().equals("Male"))
				animalStr = animalStr + "M";
			else
				animalStr = animalStr + "F";
			tempMap.put(animalStr, status);
			animalsMap.put(sad.getCrf().getId(), tempMap);

			fsDto.setAnimalsMap(animalsMap);
			fsDto.setCrfMap(crfMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fsDto;
	}

	@Override
	public RandomizationGenerationDto getRadomizationDetails(Long studyId, String userName) {
		RandomizationGenerationDto rdmgDto = null;
		List<StudyAccessionAnimals> saaList = null;
		List<StudyGroupSubGroupDetails> sgsgdList = null;
		StudyMaster sm = null;
		Long crfId = 2L;
		Map<String, Double> actMap = new HashMap<>();
		List<Double> dataList = new ArrayList<>();
		Double mean = 0.00;
		Double plustwentyVal = 0.00;
		Double minusTewentyVal = 0.00;
		Double minVal = 0.00;
		Double maxVal = 0.00;
		int totalAnimals = 0;
		// Group
		String groupingMsg = "";
		String reviewMsg = "";
		String result = "NotDone";
		boolean reviewFlag = true;
		try {
			rdmgDto = animalRandDao.RandomizationGenerationDtoDetails(studyId, crfId);
			if (rdmgDto != null) {
				sm = rdmgDto.getSm();
				saaList = rdmgDto.getSaaList();
				Long secelId = rdmgDto.getSeceleId();
				if (saaList != null && saaList.size() > 0) {
					for (StudyAccessionAnimals saa : saaList) {
						totalAnimals = saa.getTotalAnimals();
						if (rdmgDto.getTotalAnimals() == 0)
							rdmgDto.setTotalAnimals(totalAnimals);
						int animalNo = Integer.parseInt(saa.getNoOfAnimals());
						int startNo = Integer.parseInt(saa.getAnimalsFrom());
						for (int i = 0; i < animalNo; i++) {
							int animalId = startNo + i;
							StudyAccessionCrfSectionElementData aaded = animalRandDao
									.getStudyAccessionCrfSectionElementDataRecord(studyId, animalId, crfId, secelId);
							String animalStr = generateAnimalId(animalId, saa);
							if (aaded != null) {
								if (aaded.getStudyAccAnimal().getStatus() == null
										|| aaded.getStudyAccAnimal().getStatus().equals("")) {
									reviewMsg = animalStr + " Review Not Approved";
									reviewFlag = false;
								}
								actMap.put(animalStr, Double.parseDouble(aaded.getValue()));
								dataList.add(Double.parseDouble(aaded.getValue()));
							} else {
								reviewMsg = animalStr + "Data Entry Not Done";
								reviewFlag = false;
								/*
								 * actMap.put(animalStr, 0.00); dataList.add(0.00);
								 */
							}
						}
					}
				}
			}
			mean = calculateMean(dataList);
			BigDecimal bd = new BigDecimal(mean).setScale(2, RoundingMode.HALF_UP);
			mean = bd.doubleValue();
			maxVal = Collections.max(dataList);

			minVal = Collections.min(dataList);
			Double calVal = ((mean / 100) * 20);
			plustwentyVal = mean + calVal;
			BigDecimal bd2 = new BigDecimal(plustwentyVal).setScale(2, RoundingMode.HALF_UP);
			plustwentyVal = bd2.doubleValue();
			minusTewentyVal = mean - calVal;
			BigDecimal bd3 = new BigDecimal(minusTewentyVal).setScale(2, RoundingMode.HALF_UP);
			minusTewentyVal = bd3.doubleValue();
			rdmgDto.setActMap(actMap);
			rdmgDto.setMean(mean);
			rdmgDto.setMaxVal(maxVal);
			rdmgDto.setMinVal(minVal);
			rdmgDto.setPlustwentyVal(plustwentyVal);
			rdmgDto.setMinusTewentyVal(minusTewentyVal);

			// Grouping
			if (reviewFlag) {
				List<GroupInfo> groupList = animalRandDao.getGroupInfo(studyId);
				if (groupList != null && groupList.size() > 0) {
					int noOfGroups = groupList.size();
					Map<Long, GroupInfo> groupMap = new HashMap<>();
					Map<Long, Integer> subgroupMap = new HashMap<>();
					List<Long> groupIds = new ArrayList<>();
					List<String> genderList = new ArrayList<>();
					for (GroupInfo group : groupList) {
						groupMap.put(group.getId(), group);
						subgroupMap.put(group.getId(), group.getGroupTest());
						groupIds.add(group.getId());
						if (group.getGender().equals("Both")) {
							if (!genderList.contains("Male"))
								genderList.add("Male");
							if (!genderList.contains("Female"))
								genderList.add("Female");
						} else if (group.getGender().equals("Male")) {
							if (!genderList.contains("Male"))
								genderList.add("Male");
						} else if (group.getGender().equals("Female")) {
							if (!genderList.contains("Female"))
								genderList.add("Female");
						}
					}
					List<SubGroupAnimalsInfo> subgroupList = animalRandDao.getSubGroupAnimalsInfoRecordsList(groupIds,
							studyId);
					Map<Long, List<SubGroupAnimalsInfo>> subgMap = new HashMap<>();
					if (subgroupList != null && subgroupList.size() > 0) {
						List<SubGroupAnimalsInfo> tempList = null;
						for (SubGroupAnimalsInfo sgai : subgroupList) {
							if (subgMap.containsKey(sgai.getGroup().getId())) {
								tempList = subgMap.get(sgai.getGroup().getId());
								tempList.add(sgai);
								subgMap.put(sgai.getGroup().getId(), tempList);
							} else {
								tempList = new ArrayList<>();
								tempList.add(sgai);
								subgMap.put(sgai.getGroup().getId(), tempList);
							}
						}
						// Separate Male and Female Animals
						Map<String, Double> maleMap = new TreeMap<String, Double>();
						Map<String, Double> femaleMap = new TreeMap<String, Double>();
						List<String> maleIdsStrList = new ArrayList<>();
						List<String> femaleIdsStrList = new ArrayList<>();
						List<Double> maleData = new ArrayList<>();
						List<Double> femaleData = new ArrayList<>();

						for (Map.Entry<String, Double> map : actMap.entrySet()) {
							String animalStr = map.getKey();
							Double value = map.getValue();
							if (animalStr.charAt(animalStr.length() - 1) == 'M')
								maleMap.put(animalStr, value);
							else
								femaleMap.put(animalStr, value);
						}
						for (Map.Entry<String, Double> mMap : maleMap.entrySet()) {
							if (mMap.getValue() >= minusTewentyVal && mMap.getValue() <= plustwentyVal) {
								maleIdsStrList.add(mMap.getKey());
								maleData.add(mMap.getValue());
							}
						}
						for (Map.Entry<String, Double> fMap : femaleMap.entrySet()) {
							if (fMap.getValue() >= minusTewentyVal && fMap.getValue() <= plustwentyVal) {
								femaleIdsStrList.add(fMap.getKey());
								femaleData.add(fMap.getValue());
							}
						}
						if (totalAnimals != 0) {
							int filteredTotalAnimals = (maleData.size() + femaleData.size());
							int ganimalNo = filteredTotalAnimals / noOfGroups;
//					        int reminder = filteredTotalAnimals - (noOfGroups * ganimalNo);
							if (ganimalNo != 0) {
								boolean maleFlag = false;
								boolean femaleFlag = false;
								if (maleMap.size() > 0)
									maleFlag = true;
								if (femaleMap.size() > 0)
									femaleFlag = true;

								// Design Conditions Checking
								boolean flag = true;
								List<String> msgList = new ArrayList<>();
								if (maleFlag && femaleFlag) {
									String message = checkAnimalsGroupingBeforeSatisifactions(groupMap, totalAnimals,
											subgMap);
									if (!message.trim().equals("")) {
										flag = false;
										msgList.add(message);
									}
								} else if (maleFlag && femaleFlag == false) {
									if (!genderList.contains("Female")) {
										String message = checkAnimalsGroupingBeforeSatisifactions(groupMap,
												totalAnimals, subgMap);
										if (!message.trim().equals("")) {
											flag = false;
											msgList.add(message);
										}
									} else
										groupingMsg = "Female Animals Not Aaliable to generate Randomization.";
								} else if (maleFlag == false && femaleFlag) {
									if (!genderList.contains("Male")) {
										String message = checkAnimalsGroupingBeforeSatisifactions(groupMap,
												totalAnimals, subgMap);
										if (!message.trim().equals("")) {
											flag = false;
											msgList.add(message);
										}
									} else
										groupingMsg = "Male Animals Not Aaliable to generate Randomization.";
								}
								if (flag) {
									List<Double> tempMaleDataList = new ArrayList<>();
									List<Double> tempFemaleDataList = new ArrayList<>();
									List<String> tempMaleIdsStrList = new ArrayList<>();
									List<String> tempFemaleIdsStrList = new ArrayList<>();
									tempMaleDataList.addAll(maleData);
									tempFemaleDataList.addAll(femaleData);
									tempMaleIdsStrList.addAll(maleIdsStrList);
									tempFemaleIdsStrList.addAll(femaleIdsStrList);
									sgsgdList = new ArrayList<>();
									boolean subgFlag = true;
									int sno = 1;
									for (Map.Entry<Long, GroupInfo> grMap : groupMap.entrySet()) {
										GroupInfo groupInfo = grMap.getValue();
										List<SubGroupAnimalsInfo> subgList = subgMap.get(groupInfo.getId());
										if (subgList != null) {
											Map<String, List<SubGroupAnimalsInfo>> genderSubgMap = new HashMap<>();
											List<SubGroupAnimalsInfo> gsubList = null;
											for (SubGroupAnimalsInfo sgai : subgList) {
												if (genderSubgMap.containsKey(sgai.getGender())) {
													gsubList = genderSubgMap.get(sgai.getGender());
													gsubList.add(sgai);
													genderSubgMap.put(sgai.getGender(), gsubList);
												} else {
													gsubList = new ArrayList<>();
													gsubList.add(sgai);
													genderSubgMap.put(sgai.getGender(), gsubList);
												}
											}
											if (genderSubgMap.size() > 0) {
												for (Map.Entry<String, List<SubGroupAnimalsInfo>> fmap : genderSubgMap
														.entrySet()) {
													String gender = fmap.getKey();
													List<SubGroupAnimalsInfo> sgaiList = fmap.getValue();
													for (SubGroupAnimalsInfo sga : sgaiList) {
														if (gender.equals("Male")) {
															if (sga.getCount() > tempMaleDataList.size()) {
																groupingMsg = sga.getGroup().getGroupName() + "-->"
																		+ sga.getSubGroup().getName() + " Gender :"
																		+ sga.getGender()
																		+ " Animal Count is more than accession animals count";
																subgFlag = false;
																sgsgdList.clear();
																result = "NotDone";
																break;
															}
														} else if (gender.equals("Female")) {
															if (sga.getCount() > tempFemaleDataList.size()) {
																groupingMsg = sga.getGroup().getGroupName() + "-->"
																		+ sga.getSubGroup().getName() + " Gender :"
																		+ sga.getGender()
																		+ " Animal Count is more than accession animals count";
																subgFlag = false;
																sgsgdList.clear();
																result = "NotDone";
																break;
															}
														}
														if (subgFlag) {
															for (int j = 0; j < sga.getCount(); j++) {
																StudyGroupSubGroupDetails sgsgd = new StudyGroupSubGroupDetails();
																String studyNo = sm.getStudyNo();
																String str = studyNo.substring(studyNo.length() - 4,
																		studyNo.length());
																if (gender.equals("Male")) {
																	sgsgd.setGender(gender);
																	sgsgd.setTempAnimalId(tempMaleIdsStrList.get(0));
																	sgsgd.setWeight(tempMaleDataList.get(0));
																	tempMaleIdsStrList.remove(0);
																	tempMaleDataList.remove(0);

																} else {
																	sgsgd.setGender(gender);
																	sgsgd.setTempAnimalId(tempFemaleIdsStrList.get(0));
																	sgsgd.setWeight(tempFemaleDataList.get(0));
																	tempFemaleIdsStrList.remove(0);
																	tempFemaleDataList.remove(0);
																}
																if (sno <= 9)
																	sgsgd.setPerminentNo(str + "/00" + sno);
																else if (sno > 9 && sno <= 99)
																	sgsgd.setPerminentNo(str + "/0" + sno);
																else
																	sgsgd.setPerminentNo(str + "/" + sno);

																sgsgd.setGroupId(groupInfo);
																sgsgd.setStudyId(sm);
																sgsgd.setSubgroupId(sga.getSubGroup());
																sgsgd.setSubgaInfo(sga);
																sgsgdList.add(sgsgd);
																sno++;
															}
														}
													}
												}
											}
										}
									}
//					        		boolean sgsgdFlag = animalRandDao.saveStudyGroupSubGroupDetails(sgsgdList);
									if (sgsgdList.size() > 0) {
										result = "Done";
									}
								} else {
									if (msgList.size() > 0) {
										for (String st : msgList) {
											if (groupingMsg.equals(""))
												groupingMsg = st;
											else
												groupingMsg = groupingMsg + "<br/><br/>" + st;
										}
									}
								}
							} else {
								groupingMsg = "Defining Group Number is not suitable for Randomization.";
							}
						}
					} else
						groupingMsg = "Experimental Design Not Done. For This Study";

				} else
					groupingMsg = "StudyMetaData Entry Not Done.";
			} else
				groupingMsg = reviewMsg;

			/*
			 * if(result.equals("Done")) { sgsgList =
			 * animalRandDao.getStudyGroupSubGroupDetailsRecordsList(studyId); }
			 */
			rdmgDto.setGroupingMsg(groupingMsg);
			rdmgDto.setResult(result);
			rdmgDto.setSgsgList(sgsgdList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rdmgDto;
	}

	private String checkAnimalsGroupingBeforeSatisifactions(Map<Long, GroupInfo> groupMap, int totalAnimals,
			Map<Long, List<SubGroupAnimalsInfo>> subgMap) {
		String message = "";
		int disAnimalsTotal = 0;
		try {
			for (Map.Entry<Long, GroupInfo> gmap : groupMap.entrySet()) {
				GroupInfo g = gmap.getValue();
				int groupDisAnimalTotal = 0;
				List<SubGroupAnimalsInfo> subgList = subgMap.get(gmap.getKey());
				if (subgList != null) {
					for (SubGroupAnimalsInfo sgai : subgList) {
						disAnimalsTotal = disAnimalsTotal + sgai.getCount();
						groupDisAnimalTotal = groupDisAnimalTotal + sgai.getCount();
					}
				}
				if (groupDisAnimalTotal > totalAnimals) {
					message = g.getGroupName() + " Having Animals and Randomization Animals Are Not Sufficent";
				}
			}
			if (disAnimalsTotal > totalAnimals) {
				message = " Groups defined animals count is more than accession animals count.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	private Double calculateMean(List<Double> dataList) {
		Double mean = 0.00;
		double sum = 0.00;
		try {
			for (int i = 0; i < dataList.size(); i++) {
				sum += dataList.get(i);
			}
			mean = sum / (float) dataList.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mean;
	}

	private String generateAnimalId(int animalId, StudyAccessionAnimals saa) {
		String animalStr = saa.getPrefix() + "-";
		try {
			if (animalId <= 9)
				animalStr = animalStr + "000" + animalId;
			else if (animalId > 9 && animalId <= 99)
				animalStr = animalStr + "00" + animalId;
			else if (animalId > 99 && animalId <= 999)
				animalStr = animalStr + "0" + animalId;
			else
				animalStr = animalStr + animalId;
			if (saa.getGender().equals("Male"))
				animalStr = animalStr + "M";
			else
				animalStr = animalStr + "F";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return animalStr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FinalRandomizationDto generateFinalRandomizationData(Long studyId, HttpServletRequest request) {
		List<StudyGroupSubGroupDetails> sgsgList = null;
		Map<Long, List<StudyGroupSubGroupDetails>> sgsdMap = new HashMap<>();
		Map<Long, Double> meanMap = new HashMap<>();
		Map<Long, Double> sdMap = new HashMap<>();
		FinalRandomizationDto frDto = null;
		StudyMaster sm = null;
		List<RandomizationDetails> radList = null;
		boolean subFlag = true;
		try {
			sm = animalRandDao.getStudyMasterRecord(studyId);
			radList = animalRandDao.getRandomizationDetailsRecordsList(studyId);
			if (radList != null && radList.size() > 0) {
				sgsgList = new ArrayList<>();
				for (RandomizationDetails rad : radList) {
					StudyGroupSubGroupDetails sgsd = new StudyGroupSubGroupDetails();
					sgsd.setGroupId(rad.getGroupId());
					sgsd.setPerminentNo(rad.getPermanentId());
					sgsd.setStudyId(rad.getStudyId());
					sgsd.setSubgaInfo(rad.getSubanimalInfo());
					sgsd.setSubgroupId(rad.getSubgroupId());
					sgsd.setTempAnimalId(rad.getAnimalTempId());
					sgsd.setWeight(rad.getWeight());
					sgsgList.add(sgsd);
				}
				subFlag = false;
			} else {
				sgsgList = (List<StudyGroupSubGroupDetails>) request.getSession().getAttribute("randmizedData");

			}
			List<StudyGroupSubGroupDetails> tempList = null;
			if (sgsgList != null && sgsgList.size() > 0) {
				for (StudyGroupSubGroupDetails sgsd : sgsgList) {
					if (sgsdMap.containsKey(sgsd.getGroupId().getId())) {
						tempList = sgsdMap.get(sgsd.getGroupId().getId());
						tempList.add(sgsd);
						sgsdMap.put(sgsd.getGroupId().getId(), tempList);
					} else {
						tempList = new ArrayList<>();
						tempList.add(sgsd);
						sgsdMap.put(sgsd.getGroupId().getId(), tempList);
					}
				}
			}
			if (sgsdMap.size() > 0) {
				for (Map.Entry<Long, List<StudyGroupSubGroupDetails>> map : sgsdMap.entrySet()) {
					List<StudyGroupSubGroupDetails> sgsList = map.getValue();
					if (sgsList != null && sgsList.size() > 0) {
						List<Double> dataList = new ArrayList<>();
						for (StudyGroupSubGroupDetails sgsgd : sgsList) {
							dataList.add(sgsgd.getWeight());
						}
						Double mean = 0.00;
						Double sd = 0.00;
						if (sgsList.size() > 0) {

							mean = calculateMean(dataList);
							BigDecimal bdm = new BigDecimal(mean).setScale(2, RoundingMode.HALF_UP);
							mean = bdm.doubleValue();
							sd = caluclateStandaredDeviation(dataList);
							BigDecimal bds = new BigDecimal(sd).setScale(2, RoundingMode.HALF_UP);
							sd = bds.doubleValue();
						}
						meanMap.put(map.getKey(), mean);
						sdMap.put(map.getKey(), sd);
					}

				}
			}
			frDto = new FinalRandomizationDto();
			frDto.setMeanMap(meanMap);
			frDto.setSdMap(sdMap);
			frDto.setSgsgList(sgsgList);
			frDto.setSgsgdMap(sgsdMap);
			frDto.setSm(sm);
			frDto.setSubmitFlag(subFlag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return frDto;
	}

	private Double caluclateStandaredDeviation(List<Double> dataList) {
		Double sd = 0.00;
		try {
			double sum = 0.0, standardDeviation = 0.0;
			int length = dataList.size();
			for (double num : dataList) {
				sum += num;
			}
			double mean = sum / length;
			for (double num : dataList) {
				standardDeviation += Math.pow(num - mean, 2);
			}
			sd = Math.sqrt(standardDeviation / length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sd;
	}

	@Override
	public String sendRandamizationToReview(RadomizationReviewDto reviewrdmDto, Long studyId, Long userId) {
		return animalRandDao.sendRandamizationToReview(reviewrdmDto, studyId, userId);
	}

	@Override
	public String saveRandamization(RadomizationDto rdmDto, Long studyId, Long userId, StatusMaster approveStatus,
			StatusMaster rejectStatus) {
		LoginUsers user = userDao.findById(userId);
		StudyMaster study = studyDao.findByStudyId(studyId);
		List<RadamizationAllData> randamizaitonSheetMaleGruoup = rdmDto.getRandamizaitonSheetMaleGruoup();
		List<RadamizationAllData> randamizaitonSheetFemaleGruoup = rdmDto.getRandamizaitonSheetFemaleGruoup();

		List<RandomizationDetails> radList = new ArrayList<>();
		List<SubGroupAnimalsInfo> subGroup = new ArrayList<>();
		List<StudyAnimal> animalsList = new ArrayList<>();
		if (randamizaitonSheetMaleGruoup != null) {
			randamizaitonSheetMaleGruoup.forEach((rd) -> {
				Map<Long, SubGroupAnimalsInfo> animalSubGroupInfo = new HashMap<>();
				Map<Long, List<StudyAnimal>> animalSubGroupAnimals = new HashMap<>();
				List<StudyAnimal> animals = rd.getAnimals();
				animals.forEach((animal) -> {
					RandomizationDetails rzd = new RandomizationDetails();
					rzd.setCreatedBy(user.getUsername());
					rzd.setGroupId(animal.getGroupInfo());
					rzd.setMean(rd.getGroupMean());
					rzd.setPermanentId(animal.getPermanentNo());
					rzd.setStanderedDeviation(rd.getGrouoSD());
					rzd.setStudyId(rd.getStudy());
					rzd.setWeight(animal.getWeight());
					rzd.setSubgroupId(animal.getSubGrop().getSubGroup());
					rzd.setSubanimalInfo(animal.getSubGrop());
					rzd.setAnimalTempId(animal.getAnimalNo());
					radList.add(rzd);
					animalSubGroupInfo.put(animal.getSubGrop().getId(), animal.getSubGrop());

					List<StudyAnimal> sgAnimals = animalSubGroupAnimals.get(animal.getSubGrop().getId());
					if (sgAnimals == null) {
						sgAnimals = new ArrayList<>();
					}
					sgAnimals.add(animal);
					animalSubGroupAnimals.put(animal.getSubGrop().getId(), sgAnimals);
					animalsList.add(animal);
				});

				animalSubGroupInfo.forEach((k, v) -> {
					List<StudyAnimal> animals1 = animalSubGroupAnimals.get(k);
					SortedMap<Integer, StudyAnimal> map = new TreeMap<>();
					animals1.forEach((ani) -> {
						map.put(ani.getAnimalId(), ani);
					});
					StudyAnimal from = map.get(map.firstKey());
					StudyAnimal to = map.get(map.lastKey());
					v.setFormId(from.getPermanentNo());
					v.setToId(to.getPermanentNo());
					v.setFrom(from.getAnimalId() + "");
					v.setTo(to.getAnimalId() + "");
					subGroup.add(v);
				});
			});
		}

		if (randamizaitonSheetFemaleGruoup != null) {
			randamizaitonSheetFemaleGruoup.forEach((rd) -> {
				Map<Long, SubGroupAnimalsInfo> animalSubGroupInfo = new HashMap<>();
				Map<Long, List<StudyAnimal>> animalSubGroupAnimals = new HashMap<>();
				List<StudyAnimal> animals = rd.getAnimals();
				animals.forEach((animal) -> {
					System.out.println(rd.getGroupMean());
					RandomizationDetails rzd = new RandomizationDetails();
					rzd.setCreatedBy(user.getUsername());
					rzd.setGroupId(animal.getGroupInfo());
					rzd.setMean(rd.getGroupMean());
					rzd.setPermanentId(animal.getPermanentNo());
					rzd.setStanderedDeviation(rd.getGrouoSD());
					rzd.setStudyId(rd.getStudy());
					rzd.setWeight(animal.getWeight());
					rzd.setSubgroupId(animal.getSubGrop().getSubGroup());
					rzd.setSubanimalInfo(animal.getSubGrop());
					rzd.setAnimalTempId(animal.getAnimalNo());
					radList.add(rzd);
					animalSubGroupInfo.put(animal.getSubGrop().getId(), animal.getSubGrop());

					List<StudyAnimal> sgAnimals = animalSubGroupAnimals.get(animal.getSubGrop().getId());
					if (sgAnimals == null) {
						sgAnimals = new ArrayList<>();
					}
					sgAnimals.add(animal);
					animalSubGroupAnimals.put(animal.getSubGrop().getId(), sgAnimals);
					animalsList.add(animal);
				});

				animalSubGroupInfo.forEach((k, v) -> {
					List<StudyAnimal> animals1 = animalSubGroupAnimals.get(k);
					SortedMap<Integer, StudyAnimal> map = new TreeMap<>();
					Integer min = null;
					Integer max = null;
					for (StudyAnimal ani : animals1) {
						map.put(ani.getAnimalId(), ani);
						if (min == null)
							min = ani.getAnimalId();
						else {
							if (min > ani.getAnimalId())
								min = ani.getAnimalId();
						}
						if (max == null)
							max = ani.getAnimalId();
						else {
							if (max < ani.getAnimalId())
								max = ani.getAnimalId();
						}
					}
					;
					StudyAnimal from = map.get(min);
					StudyAnimal to = map.get(max);
					v.setFormId(from.getPermanentNo());
					v.setToId(to.getPermanentNo());
					v.setFrom(from.getAnimalId() + "");
					v.setTo(to.getAnimalId() + "");
					subGroup.add(v);
				});
			});
		}

		return animalRandDao.saveRadomization(animalsList, subGroup, radList, study);
	}

	@Override
	public String savesavegenerateGropRandomizationDataDetails(Long studyId, String userName,
			HttpServletRequest request) {
		String result = "Failed";
		FinalRandomizationDto frmd = null;
		List<SubGroupAnimalsInfo> sgaiList = null;
//		List<SubGroupAnimalsInfoAll> sgiAllList = new ArrayList<>();
		List<RandomizationDetails> radList = new ArrayList<>();
		List<SubGroupAnimalsInfo> updateList = new ArrayList<>();
		Map<Long, List<StudyGroupSubGroupDetails>> sgsgdMap = null;
		Map<Long, Double> meanMap = null;
		Map<Long, Double> sdMap = null;
		try {
			sgaiList = animalRandDao.getSubGroupAnimalsInfoRecordsList(studyId);
			frmd = (FinalRandomizationDto) request.getSession().getAttribute("randmizedData");
			if (frmd != null) {
				sgsgdMap = frmd.getSgsgdMap();
				meanMap = frmd.getMeanMap();
				sdMap = frmd.getSdMap();
				if (sgsgdMap != null && sgsgdMap.size() > 0) {
					Map<Long, Map<String, List<SubGroupAnimalsInfo>>> subMap = new HashMap<>();
					Map<String, List<SubGroupAnimalsInfo>> subgenderMap = null;
					List<SubGroupAnimalsInfo> gaiTempList = null;
					if (sgaiList != null && sgaiList.size() > 0) {
						for (SubGroupAnimalsInfo gai : sgaiList) {
							if (subMap.containsKey(gai.getGroup().getId())) {
								subgenderMap = subMap.get(gai.getGroup().getId());
								if (subgenderMap.containsKey(gai.getGender())) {
									gaiTempList = subgenderMap.get(gai.getGender());
									gaiTempList.add(gai);
									subgenderMap.put(gai.getGender(), gaiTempList);
									subMap.put(gai.getGroup().getId(), subgenderMap);
								} else {
									gaiTempList = new ArrayList<>();
									gaiTempList.add(gai);
									subgenderMap.put(gai.getGender(), gaiTempList);
									subMap.put(gai.getGroup().getId(), subgenderMap);
								}
							} else {
								subgenderMap = new HashMap<>();
								gaiTempList = new ArrayList<>();
								gaiTempList.add(gai);
								subgenderMap.put(gai.getGender(), gaiTempList);
								subMap.put(gai.getGroup().getId(), subgenderMap);
							}
						}
					}
					if (subMap.size() > 0) {
						Map<Long, Map<String, List<StudyGroupSubGroupDetails>>> sgsgenderMap = new HashMap<>();
						Map<String, List<StudyGroupSubGroupDetails>> sgsInMap = null;
						List<StudyGroupSubGroupDetails> sgsgTempList = null;
						for (Map.Entry<Long, List<StudyGroupSubGroupDetails>> map : sgsgdMap.entrySet()) {
							Long groupId = map.getKey();
							List<StudyGroupSubGroupDetails> sgdList = map.getValue();
							if (sgdList != null && sgdList.size() > 0) {
								for (StudyGroupSubGroupDetails sgsgd : sgdList) {
									if (sgsgenderMap.containsKey(groupId)) {
										sgsInMap = sgsgenderMap.get(groupId);
										if (sgsInMap.containsKey(sgsgd.getGender())) {
											sgsgTempList = sgsInMap.get(sgsgd.getGender());
											sgsgTempList.add(sgsgd);
											sgsInMap.put(sgsgd.getGender(), sgsgTempList);
											sgsgenderMap.put(groupId, sgsInMap);
										} else {
											sgsgTempList = new ArrayList<>();
											sgsgTempList.add(sgsgd);
											sgsInMap.put(sgsgd.getGender(), sgsgTempList);
											sgsgenderMap.put(groupId, sgsInMap);
										}
									} else {
										sgsInMap = new HashMap<>();
										sgsgTempList = new ArrayList<>();
										sgsgTempList.add(sgsgd);
										sgsInMap.put(sgsgd.getGender(), sgsgTempList);
										sgsgenderMap.put(groupId, sgsInMap);
									}
								}
							}
						}
						for (Map.Entry<Long, Map<String, List<SubGroupAnimalsInfo>>> firstMap : subMap.entrySet()) {
							Long gId = firstMap.getKey();
							Map<String, List<SubGroupAnimalsInfo>> sgaMap = firstMap.getValue();
							Map<String, List<StudyGroupSubGroupDetails>> sgdMp = sgsgenderMap.get(gId);
							for (Map.Entry<String, List<SubGroupAnimalsInfo>> sgainfMap : sgaMap.entrySet()) {
								String gender = sgainfMap.getKey();
								List<SubGroupAnimalsInfo> sgainList = sgainfMap.getValue();
								List<StudyGroupSubGroupDetails> sgsgdtList = sgdMp.get(gender);
								if (sgainList != null && sgsgdtList != null) {
									for (SubGroupAnimalsInfo sg : sgainList) {
										int count = sg.getCount();
										for (int i = 0; i < count; i++) {
											StudyGroupSubGroupDetails sgd = sgsgdtList.get(i);
											String prNo = sgd.getPerminentNo();
											String[] tempArr = prNo.split("\\/");

											RandomizationDetails rzd = new RandomizationDetails();
											rzd.setCreatedOn(new Date());
											rzd.setGroupId(sgd.getGroupId());
											rzd.setMean(meanMap.get(sgd.getGroupId().getId()));
											rzd.setPermanentId(prNo);
											rzd.setStanderedDeviation(sdMap.get(sgd.getGroupId().getId()));
											rzd.setStudyId(sgd.getStudyId());
											rzd.setWeight(sgd.getWeight());
											rzd.setSubgroupId(sgd.getSubgroupId());
											rzd.setSubanimalInfo(sgd.getSubgaInfo());
											rzd.setAnimalTempId(sgd.getTempAnimalId());
											radList.add(rzd);
											if (i == 0) {
												sg.setFormId(sgd.getPerminentNo());
												if (tempArr.length > 0) {
													sg.setFrom(tempArr[1]);
												}
											}
											if (i == (count - 1)) {
												sg.setToId(sgd.getPerminentNo());
												if (tempArr.length > 0) {
													sg.setTo(tempArr[1]);
												}
											}
										}
										updateList.add(sg);
										int n = 0;
										for (int j = 0; j < count; j++) {
											sgsgdtList.remove(n);
										}

									}
								}
							}
						}
					}
				}
			}
			boolean flag = animalRandDao.saveRadomizationDetails(updateList, radList);
			if (flag)
				result = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public RandamizationDto generatedRandamization(StudyMaster study, String gender) {
		// TODO Auto-generated method stub
		return animalRandDao.generatedRandamization(study, gender);
	}
	public static int maxAnimalNo;
	@Override
	public String newRandamization(StudyMaster study, ModelMap model, HttpServletRequest request, String gender) {
		System.out.println(gender);
//		String gender = study.getGender();
//		if (study.getGender().equals("Both") && !study.isSplitStudyByGender()) {
//			if (!study.isMaleRandamizationStatus() && !study.isFemaleRandamizationStatus()) {
//				Date maleStartDate = study.getAcclimatizationStarDate();
//				Date femleStartDate = study.getAcclimatizationStarDateFemale();
//				int days = maleStartDate.compareTo(femleStartDate);
//				if (days < 0) {
//					gender = "Male";
//				} else if (days == 0) {
//					gender = "Both";
//				} else
//					gender = "Female";
//			} else if (!study.isMaleRandamizationStatus() || !study.isMaleRandamizationReviewStatus()) {
//				gender = "Male";
//			} else if (!study.isFemaleRandamizationStatus() || !study.isFemaleRandamizationReviewStatus()) {
//				gender = "Female";
//			}
//		}
//		else {
//			gender = "Both";
//		}
//		if(gender.equals(generatedFor)) {
		if(true) {
			request.getSession().setAttribute("randamizationGender", gender);
			List<StudyAnimal> animals = animalRandDao.studyAnimalWithGender(study.getId(), gender);
			int stuyAnimalSize = animalRandDao.stuyAnimalSize(study, gender);
			if (stuyAnimalSize <= animals.size()) {
				List<StudyAnimal> maleAnimals = new ArrayList<>();
				List<StudyAnimal> femaleAnimals = new ArrayList<>();
				for (StudyAnimal animal : animals) {
					if (animal.getGender().equals("Male"))
						maleAnimals.add(animal);
					else
						femaleAnimals.add(animal);
				}
				int maleAnimalSize = 0;
				int femaleAnimalSize = 0;
				List<SubGroupInfo> subGroups = studyDao.allSubGroupInfo(study.getId());
				for (SubGroupInfo sgf : subGroups) {
					List<SubGroupAnimalsInfo> subGroupAnimalsInfoList = sgf.getAnimalInfo();
					for (SubGroupAnimalsInfo subGroupAnimalsInfo : subGroupAnimalsInfoList) {
						if (subGroupAnimalsInfo.getGender().equals("Male"))
							maleAnimalSize += subGroupAnimalsInfo.getCount();
						else
							femaleAnimalSize += subGroupAnimalsInfo.getCount();
					}
				}
				if (gender.equals("Male")) {
					if (maleAnimalSize > maleAnimals.size()) {
						return "1- Required " + study.getSubjects() + " Male Animals. Found :" + maleAnimals.size();
					}
				} else if (gender.equals("Female")) {
					if (femaleAnimalSize > femaleAnimals.size()) {
						return "1- Required " + study.getSubjects() + " Female Animals. Found :" + femaleAnimals.size();
					}
				} else {
					if (maleAnimalSize > maleAnimals.size()) {
						return "1- Required " + maleAnimalSize + " Male Animals. Found :" + maleAnimals.size();
					} else if (femaleAnimalSize > femaleAnimals.size()) {
						return "1- Required " + femaleAnimalSize + " Female Animals. Found :" + femaleAnimals.size();
					}
				}
				maleAnimals = new ArrayList<>();
				femaleAnimals = new ArrayList<>();
				for (StudyAnimal animal : animals) {
					if (animal.getGender().equals("Male") && animal.getWeight() != null)
						maleAnimals.add(animal);
					else if (animal.getGender().equals("Female") && animal.getWeight() != null)
						femaleAnimals.add(animal);
				}
				if (gender.equals("Male")) {
					if (maleAnimalSize > maleAnimals.size()) {
						return "1- Required " + study.getSubjects() + " Male Animals. Found :" + maleAnimals.size()
								+ " Weights";
					}
				} else if (gender.equals("Female")) {
					if (femaleAnimalSize > femaleAnimals.size()) {
						return "1- Required " + study.getSubjects() + " Female Animals. Found :" + femaleAnimals.size()
								+ " Weights";
					}
				} else {
					if (maleAnimalSize > maleAnimals.size()) {
						return "1- Required " + maleAnimalSize + " Male Animals. Found :" + maleAnimals.size()
								+ " Weights";
					} else if (femaleAnimalSize > femaleAnimals.size()) {
						return "1- Required " + femaleAnimalSize + " Female Animals. Found :" + femaleAnimals.size()
								+ " Weights";
					}
				}
				maxAnimalNo = studyDao.maxAnimalPerminentNo(study.getId());
				if (gender.equals("Male")) {
					return randamizationCheck(model, request, study, maleAnimals, "Male", maleAnimalSize, subGroups,maxAnimalNo);
				} else if (gender.equals("Female")) {
					return randamizationCheck(model, request, study, femaleAnimals, "Female", femaleAnimalSize, subGroups, maxAnimalNo);
				} else {
					String maleresult = randamizationCheck(model, request, study, maleAnimals, "Male", maleAnimalSize,
							subGroups, 1);
					
					String femaleresult = randamizationCheck(model, request, study, femaleAnimals, "Female",
							femaleAnimalSize, subGroups, maxAnimalNo);
					StringBuilder sb = new StringBuilder();
					boolean st = false;
					if (maleresult.indexOf(0) == '1') {
						sb.append(maleresult.substring(1));
						st = true;
					}
					if (femaleresult.indexOf(0) == '1') {
						sb.append(femaleresult.substring(1));
						st = true;
					}
					if (st)
						return "1-" + sb.toString();
					else
						return "0-Randamization Done Successfully";
				}

			} else {
				return "1- Required " + study.getSubjects() + " Animals. Found :" + animals.size();
			}
		}else {
			return "2";
		}
		
	}

	private String randamizationCheck(ModelMap model, HttpServletRequest request, StudyMaster study,
			List<StudyAnimal> animals, String gender, int requiredAnimalSize, List<SubGroupInfo> subGroups,
			int maxAnimalNo) {
		RandamizationDto dto = new RandamizationDto();
		dto.setGender(gender);
		dto.setAnimals(animals);

		List<RandamizationAssigndDto> randamizationAssigndDto = new ArrayList<>();
		Collections.sort(animals);
		int order = 1;
		Double weight = 0.0;
		boolean flag = false;
		Double min = 0.0;
		Double max = 0.0;
		for (StudyAnimal animal : animals) {
			RandamizationAssigndDto assdto = new RandamizationAssigndDto();
			assdto.setRandamizationDto(dto);
			assdto.setAnimal(animal);
//			assdto.setOrder(order++);
			randamizationAssigndDto.add(assdto);
			weight += animal.getWeight();
			if (flag) {
				if (min > animal.getWeight())
					min = animal.getWeight();
				if (max < animal.getWeight())
					max = animal.getWeight();
			} else {
				min = animal.getWeight();
				max = min;
				flag = true;
			}
		}
		dto.setRandamizationAssigndDto(randamizationAssigndDto);
		dto.setMean(new BigDecimal(weight / animals.size()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		double persent = ((dto.getMean() * 20) / 100);
		dto.setMaxMean(new BigDecimal(dto.getMean() + persent).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		dto.setMinMean(new BigDecimal(dto.getMean() - persent).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		dto.setMinWeight(min);
		dto.setMaxWeight(max);

		System.out.println("start : " + dto.getMinMean());
		System.out.println("end : " + dto.getMaxMean());

		List<StudyAnimal> animalsTemp = new ArrayList<>();
		for (StudyAnimal animal : animals) {
			System.out.println(dto.getMinMean() + "\t" + animal.getWeight() + "\t" + dto.getMaxMean());
			if (!(animal.getWeight() < dto.getMinMean() || animal.getWeight() > dto.getMaxMean())) {
				System.out.println(animal.getWeight());
				animalsTemp.add(animal);
			}
		}
		if (requiredAnimalSize <= animalsTemp.size()) {
			List<RandamizationGroupDto> randamizationGroupDto = assignAnimalsToSubgroup(dto, gender, animalsTemp,
					subGroups, maxAnimalNo);
			Collections.sort(randamizationGroupDto);
			dto.setRandamizationGroupDto(randamizationGroupDto);

			if (gender.equals("Male")) {
				request.getSession().setAttribute("maledto", dto);
			} else
				request.getSession().setAttribute("femaledto", dto);

			return "0-Randamization Done Successfully";
		} else {
			return "1- Required " + study.getSubjects() + " " + gender + " Animals Weigth With in ragne "
					+ dto.getMinMean() + " to  " + dto.getMaxMean() + ". Found :" + animals.size();
		}
	}
	
	private List<RandamizationGroupDto> assignAnimalsToSubgroup(RandamizationDto mdto, String gender,
			List<StudyAnimal> animalsTemp, List<SubGroupInfo> subGroups, int maxAnimalNo) {
		Collections.sort(animalsTemp);
		int maxSubGroupNo = 0;
		Map<Integer, SubGroupInfo> subGroupsMap = new TreeMap<>();
		Map<Long, Integer> subGroupIds = new TreeMap<>();
		Map<Long, Integer> subGroupAnimalCount = new TreeMap<>();
		for (SubGroupInfo sgf : subGroups) {
			List<SubGroupAnimalsInfo> subGroupAnimalsInfoList = sgf.getAnimalInfo();
			for (SubGroupAnimalsInfo subGroupAnimalsInfo : subGroupAnimalsInfoList) {
				if (subGroupAnimalsInfo.getGender().equals(gender)) {
					subGroupIds.put(sgf.getId(), subGroupAnimalsInfo.getCount());
					subGroupsMap.put(sgf.getSubGroupNo(), sgf);
					if (maxSubGroupNo < sgf.getSubGroupNo())
						maxSubGroupNo = sgf.getSubGroupNo();
					subGroupAnimalCount.put(sgf.getId(), 1);
				}
			}
		}

		List<SubGroupInfo> list = new ArrayList<>();
		for (SubGroupInfo sgf : subGroups) {
			if (subGroupIds.containsKey(sgf.getId()))
				list.add(sgf);
		}
		Collections.sort(list);
		int skipGroupNo = -1;
		int animalCount = 0;
		int subGroupsSize = subGroupsMap.size();

		Map<Long, List<RandamizationGroupAnimalDto>> subGroupAnimals = new HashMap<>();
		while (subGroupsSize > 0) {
			if (animalCount < animalsTemp.size()) {
				if(subGroups.size() > 1) {
					skipGroupNo++;
					if (skipGroupNo > maxSubGroupNo)
						skipGroupNo = 1;
				}
				for (Map.Entry<Integer, SubGroupInfo> sg : subGroupsMap.entrySet()) {
					if (skipGroupNo != sg.getKey() && subGroupIds.get(sg.getValue().getId()) > 0) {
						if (animalCount < animalsTemp.size()) {
							RandamizationGroupAnimalDto dto = new RandamizationGroupAnimalDto();
							dto.setAnimal(animalsTemp.get(animalCount++));
//							dto.setOrder(subGroupAnimalCount.get(sg.getValue().getId()));
							
							dto.setSubGroup(sg.getValue());
							List<RandamizationGroupAnimalDto> lis = subGroupAnimals.get(sg.getValue().getId());
							if (lis == null)
								lis = new ArrayList<>();
							lis.add(dto);
							subGroupAnimals.put(sg.getValue().getId(), lis);

							subGroupAnimalCount.put(sg.getValue().getId(), dto.getOrder() + 1);
							subGroupIds.put(sg.getValue().getId(), (subGroupIds.get(sg.getValue().getId()) - 1));
							if (subGroupIds.get(sg.getValue().getId()) == 0)
								subGroupsSize--;
						}
					}
				}
			}

		}

		List<RandamizationGroupDto> rdtoList = new ArrayList<>();
		int animalNo = maxAnimalNo;
		SortedMap<Integer, SubGroupInfo> subgroupMap = new TreeMap<>();
		for (SubGroupInfo sgf : subGroups) {
			subgroupMap.put(sgf.getSubGroupNo(), sgf);
		}
		for(Map.Entry<Integer,SubGroupInfo> sg : subgroupMap.entrySet()) {
			SubGroupInfo sgf = sg.getValue();
			RandamizationGroupDto rdto = new RandamizationGroupDto();
			rdto.setRandamizationDto(mdto);
			rdto.setSubGroupInfo(sgf);
			Double weigth = 0.0d;
//			private Double mean;
//			private Double sd;
			List<RandamizationGroupAnimalDto> randamizationGroupAnimalDto = subGroupAnimals.get(sgf.getId());
			for (RandamizationGroupAnimalDto li : randamizationGroupAnimalDto) {
				li.setRandamizationGroupDto(rdto);
				String no = animalNo + "";
				if (animalNo < 10)
					no = "00" + animalNo;
				else if (animalNo < 100)
					no = "0" + animalNo;
				no = li.getAnimal().getStudy().getStudyNo().substring(
						li.getAnimal().getStudy().getStudyNo().length() - 4,
						li.getAnimal().getStudy().getStudyNo().length()) + "/" + no;
				li.setAnimalNo(no);
				li.setOrder(animalNo);
				animalNo++;
				weigth += li.getAnimal().getWeight();
			}
			rdto.setMean(new BigDecimal(weigth / randamizationGroupAnimalDto.size())
					.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			int n = randamizationGroupAnimalDto.size();
			Double meu = rdto.getMean();
			Double sum = 0.0;
			for (RandamizationGroupAnimalDto li : randamizationGroupAnimalDto) {
				sum += ((li.getAnimal().getWeight() - meu) * (li.getAnimal().getWeight() - meu));
			}
			rdto.setSd(Math.sqrt(sum / n));
			rdto.setRandamizationGroupAnimalDto(randamizationGroupAnimalDto);
			rdto.setSubGroupAnimalCount(randamizationGroupAnimalDto.size());
			rdtoList.add(rdto);
		}
		AnimalRandomizationServiceImpl.maxAnimalNo = animalNo;
		return rdtoList;
	}
	
	@Override
	public boolean saverandomizationSendToReview(StudyMaster study, List<RandamizationDto> dtoList, Long userId) {
		// TODO Auto-generated method stub
		return animalRandDao.saverandomizationSendToReview(study, dtoList, userId);
	}
}
