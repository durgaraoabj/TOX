package com.springmvc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.covide.crf.dto.Crf;
import com.covide.enums.StatusMasterCodes;
import com.springmvc.dao.AccessionDao;
import com.springmvc.dao.AcclimatizationDao;
import com.springmvc.dao.StudyDao;
import com.springmvc.model.AmendmentDetails;
import com.springmvc.model.AnimalCage;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAcclamatizationDates;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyCageAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudySubGroupAnimal;
import com.springmvc.model.StudyTreatmentDataDates;
import com.springmvc.service.AcclimatizationService;

@Service("accService")
public class AcclimatizationServiceImpl implements AcclimatizationService {
	@Autowired
	AccessionDao accessionDao;
	@Autowired
	AcclimatizationDao acclimatizationDao;
	@Autowired
	StudyDao studyDao;

	@Override
	public Map<Long, StudyAcclamatizationData> getStudyAcclamatizationDataList(Long studyId) {
		List<StudyAcclamatizationData> sadList = null;
		Map<Long, StudyAcclamatizationData> sadMap = new HashMap<>();
		try {
			sadList = acclimatizationDao.getStudyAcclamatizationDataList(studyId);
			if (sadList != null && sadList.size() > 0) {
				for (StudyAcclamatizationData sad : sadList) {
					sadMap.put(sad.getCrf().getId(), sad);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sadMap;
	}

	@Override
	public String saveAcclimatizationData(Long studyId, List<Long> crfIds, List<Long> crfIds2,
			Map<Long, String> typeMap, Map<Long, String> entryDaysMap, Map<Long, String> windowPeriod,
			Map<Long, Integer> deviationMap, String userName) {
		StudyMaster study = studyDao.findByStudyId(studyId);
		String result = "";
		List<Crf> crfsList = null;
		List<StudyAcclamatizationData> saveList = new ArrayList<>();
		List<StudyAcclamatizationData> sadList = null;
		List<StudyAcclamatizationData> updateList = new ArrayList<>();
		try {
			crfsList = acclimatizationDao.getCrfRecordsList(crfIds);
			List<StudyAcclamatizationData> savedData = acclimatizationDao.getStudyAcclamatizationDataList(studyId);
			Map<Long, StudyAcclamatizationData> savedMap = new HashMap<>();
			for (StudyAcclamatizationData sadd : savedData) {
				savedMap.put(sadd.getCrf().getId(), sadd);
			}
			if (crfsList != null && crfsList.size() > 0) {
				for (Crf crf : crfsList) {
					StudyAcclamatizationData sad = savedMap.get(crf.getId());
					if (sad == null) {
						sad = new StudyAcclamatizationData();
						sad.setCreatedBy(userName);
						sad.setCreatedOn(new Date());
						sad.setCrf(crf);
						sad.setEntryValues(entryDaysMap.get(crf.getId()));
						sad.setDeviationSign(windowPeriod.get(crf.getId()));
						sad.setDeviation(deviationMap.get(crf.getId()));
						sad.setStudy(study);
						sad.setType(typeMap.get(crf.getId()));
						saveList.add(sad);
					} else {
						savedMap.remove(crf.getId());
						if (!sad.getEntryValues().equals(entryDaysMap.get(crf.getId()))
								|| !sad.getDeviationSign().equals(windowPeriod.get(crf.getId()))
								|| sad.getDeviation() != (deviationMap.get(crf.getId()))) {
							sad.setEntryValues(entryDaysMap.get(crf.getId()));
							sad.setDeviationSign(windowPeriod.get(crf.getId()));
							sad.setDeviation(deviationMap.get(crf.getId()));
							sad.setUpdatedBy(userName);
							sad.setUpdatedOn(new Date());
							updateList.add(sad);
						}
					}
				}
			}
			for (Map.Entry<Long, StudyAcclamatizationData> map : savedMap.entrySet()) {
				StudyAcclamatizationData sad = map.getValue();
				sad.setStatus("inactive");
				updateList.add(sad);
			}
			boolean flag = acclimatizationDao.saveStudyAcclamatizationData(saveList, updateList, studyId);
			if (flag)
				result = "success";
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	@Override
	public Map<Long, String> allAnimals(Long studyId) {
		// TODO Auto-generated method stub
		Map<Long, String> map = new HashMap<>();
		List<StudyAnimal> list = acclimatizationDao.allStudyAnimals(studyId);
		for (StudyAnimal studyAnimal : list) {
			map.put(studyAnimal.getId(), studyAnimal.getAnimalNo());
		}
		return map;
	}

	@Override
	public Map<Long, String> allUnCagedAnimals(Long studyId) {
		// TODO Auto-generated method stub

		List<StudyAnimal> list = acclimatizationDao.allUnCagedAnimals(studyId);
		SortedMap<Integer, StudyAnimal> animals = new TreeMap<>();
		for (StudyAnimal studyAnimal : list) {
			animals.put(studyAnimal.getAnimalId(), studyAnimal);
		}
		Map<Long, String> map = new LinkedHashMap<>();
		for (Entry<Integer, StudyAnimal> m : animals.entrySet()) {
			map.put(m.getValue().getId(), m.getValue().getPermanentNo());
		}

		return map;
	}

	@Override
	public String saveAnimalCageing(String cageNameOrNo, List<Long> animalIds, Long studyId, Long userId) {
		// TODO Auto-generated method stub
		try {
			if (animalIds.size() > 0) {
				List<StudyAnimal> allAnimals = accessionDao.studyAnimalsOf(animalIds);
				AnimalCage cage = accessionDao.animalCage(cageNameOrNo);
				List<StudyCageAnimal> cageAnimals = new ArrayList<>();
				for (StudyAnimal ani : allAnimals) {
					StudyCageAnimal sca = new StudyCageAnimal();
					sca.setAnimal(ani);
					sca.setCage(cage);
					sca.setStudy(ani.getStudy());
					cageAnimals.add(sca);
					ani.setCaged(true);
				}
				accessionDao.studyCageAnimals(cageAnimals, allAnimals);
				return "success";
			}
			return "faild";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "faild";
		}
	}

	@Override
	public Map<String, List<StudyCageAnimal>> cagedAnimals(Long studyId) {
		// TODO Auto-generated method stub
		Map<String, List<StudyCageAnimal>> map = new HashMap<>();
		List<StudyCageAnimal> list = acclimatizationDao.allCagedAnimals(studyId);
		for (StudyCageAnimal studyAnimal : list) {
			List<StudyCageAnimal> each = map.get(studyAnimal.getCage().getCageId());
			if (each == null)
				each = new ArrayList<>();
			each.add(studyAnimal);
			map.put(studyAnimal.getCage().getCageId(), each);
		}
		return map;
	}

	@Override
	public Map<Long, List<StudySubGroupAnimal>> studySubGroupAnimal(Long studyId) {
		Map<Long, List<StudySubGroupAnimal>> map = new HashMap<>();
		List<StudySubGroupAnimal> list = acclimatizationDao.studySubGroupAnimal(studyId);
		for (StudySubGroupAnimal studyAnimal : list) {
			List<StudySubGroupAnimal> each = map.get(studyAnimal.getSubgroupId().getId());
			if (each == null)
				each = new ArrayList<>();
			each.add(studyAnimal);
			map.put(studyAnimal.getSubgroupId().getId(), each);
		}
		return map;
	}

	@Override
	public StudyAcclamatizationData studyAcclamatizationData(Long crfId, Long studyId) {
		// TODO Auto-generated method stub
		return acclimatizationDao.studyAcclamatizationData(crfId, studyId);
	}

	@Override
	public StdSubGroupObservationCrfs studyTreatmentData(Long crfId, Long studyId, Long subGroupId) {
		// TODO Auto-generated method stub
		return acclimatizationDao.studyTreatmentData(crfId, studyId, subGroupId);
	}

	@Override
	public StudyAcclamatizationDates studyAcclamatizationDates(Long crfId, Long studyId, Date date, Long userId,
			Boolean genderBase, String gender) {
		// TODO Auto-generated method stub
		return acclimatizationDao.studyAcclamatizationDates(crfId, studyId, date, userId, genderBase, gender);
	}

	@Override
	public StudyTreatmentDataDates studyTreatmentDataDates(Long crfId, Long studyId, Date date, Long userId,
			Long subGropId, Boolean genderBase, String gender) {
		// TODO Auto-generated method stub
		return acclimatizationDao.studyTreatmentDataDates(crfId, studyId, date, userId, subGropId, genderBase, gender);
	}

	@Override
	public boolean changeAcclimatizationConfig(Crf crf, StudyMaster study, String status, String userName) {
		try {
			StudyAcclamatizationData sad = acclimatizationDao.getStudyAcclamatizationDataList(crf.getId(),
					study.getId());
			if (sad == null) {
				sad = new StudyAcclamatizationData();
				sad.setCreatedBy(userName);
				sad.setCreatedOn(new Date());
				sad.setCrf(crf);
				sad.setStudy(study);
				sad.setType("day");

				AmendmentDetails ad = null;
				if (sad.getStudy().getStatus().getStatusCode().equals(StatusMasterCodes.SI.toString())) {
					ad = new AmendmentDetails();
					ad.setAcclamtizationData(sad);
					ad.setAmendmentType("Acclamatization");
					ad.setAction("Observerion Added");
					ad.setCrf(crf);
					ad.setCreatedBy(userName);
					ad.setCreatedOn(new Date());
					ad.setStatus("Open");
					ad.setStudyId(study.getId());
					ad.setStudyName(study.getStudyNo());
					sad.setAmendment(true);
				}
				acclimatizationDao.saveStudyAcclamatizationData(sad, ad);
			} else {
				sad.setStatus(status);
				sad.setUpdatedBy(userName);
				sad.setUpdatedOn(new Date());
				acclimatizationDao.updateStudyAcclamatizationData(sad);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean changeAcclimatizationConfigType(Long crfId, Long studyId, String value, String userName,
			String field) {
		try {
			StudyAcclamatizationData sad = acclimatizationDao.getStudyAcclamatizationDataList(crfId, studyId);
			if (sad != null) {
				sad.setUpdatedBy(userName);
				sad.setUpdatedOn(new Date());
				if (field.equals("type"))
					sad.setType(value);
				else if (field.equals("deviationSign")) {
					if (value.equals("plus"))
						sad.setDeviationSign("+");
					else if (value.equals("mines"))
						sad.setDeviationSign("-");
					else if (value.equals("bouth"))
						sad.setDeviationSign("+/-");
					else
						sad.setDeviationSign(value);
				} else if (field.equals("deviation"))
					sad.setDeviation(Integer.parseInt(value));
				acclimatizationDao.updateStudyAcclamatizationData(sad);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean removeStudyAcclamatizationDatesDetails(Long studyAcclamatizationDatesId, Long userId) {
		// TODO Auto-generated method stub
		return acclimatizationDao.removeStudyAcclamatizationDatesDetails(studyAcclamatizationDatesId, userId);
	}

	@Override
	public boolean updateStudyAcclamatizationDatesDetails(Long studyAcclamatizationDatesId, String value, Long userId) {
		// TODO Auto-generated method stub
		return acclimatizationDao.updateStudyAcclamatizationDatesDetails(studyAcclamatizationDatesId, value, userId);
	}

	@Override
	public List<StudyAcclamatizationDates> studyAcclamatizationDates(Long crfId, Long studyId) {
		// TODO Auto-generated method stub
		return acclimatizationDao.studyAcclamatizationDates(crfId, studyId);
	}

	@Override
	public List<StudyTreatmentDataDates> studyTreatmentnDates(Long crfId, Long studyId, Long subGroupId) {
		// TODO Auto-generated method stub
		return acclimatizationDao.studyTreatmentDataDates(crfId, studyId, subGroupId);
	}

	@Override
	public StudyAcclamatizationDates studyAcclamatizationDatesWithId(Long studyAcclamatizationDatesId) {
		// TODO Auto-generated method stub
		return acclimatizationDao.studyAcclamatizationDatesById(studyAcclamatizationDatesId);
	}

	@Override
	public List<StudyAcclamatizationData> studyAcclamatizationData(Long studyId) {
		// TODO Auto-generated method stub
		return acclimatizationDao.studyAcclamatizationData(studyId);
	}

	
	
}
