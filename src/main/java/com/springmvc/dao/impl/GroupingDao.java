package com.springmvc.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.covide.enums.StatusMasterCodes;
import com.covide.template.dto.StudyMasterDto;
import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.AccessionDao;
import com.springmvc.dao.AcclimatizationDao;
import com.springmvc.dao.ExpermentalDesignDao;
import com.springmvc.dao.InstrumentDao;
import com.springmvc.dao.StatusDao;
import com.springmvc.dao.StudyDao;
import com.springmvc.dao.UserDao;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.ObservationInturmentConfiguration;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyAcclamatizationDates;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTestCodes;
import com.springmvc.model.StudyTreatmentDataDates;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.SysmexData;
import com.springmvc.model.SysmexTestCodeData;
import com.springmvc.model.TestCode;
import com.springmvc.model.TestCodeLog;
import com.springmvc.model.TestCodeProfile;
import com.springmvc.model.TestCodeProfileLog;
import com.springmvc.model.TestCodeProfileParameters;
import com.springmvc.model.TestCodeUnits;
import com.springmvc.model.TestCodeUnitsLog;
import com.springmvc.model.UserWiseStudiesAsignMaster;

@Repository("groupingDao")
@PropertySource(value = { "classpath:application.properties" })
@SuppressWarnings("unchecked")
public class GroupingDao extends AbstractDao<Long, TestCodeUnits> {
	@Autowired
	private Environment environment;
	@Autowired
	private UserDao userDao;
	@Autowired
	private StatusDao statusDao;
	@Autowired
	private StudyDao studyDao;
	@Autowired
	private ExpermentalDesignDao expermentalDesignDao;
	@Autowired
	private AccessionDao accessionDao;
	@Autowired
	private AcclimatizationDao acclimatizationDao;
	@Autowired
	private InstrumentDao instrumentDao;

	public List<TestCodeUnits> testCodeUnits() {
		return getSession().createCriteria(TestCodeUnits.class).list();
	}

	public String checkUintName(String unit) {
		TestCodeUnits testcodeUnit = (TestCodeUnits) getSession().createCriteria(TestCodeUnits.class)
				.add(Restrictions.eq("instumentUnit", unit)).uniqueResult();
		if (testcodeUnit != null) {
			return "Valied";
		} else
			return "In-Valied";
	}

	public boolean mergeTestCodeUnits(Long userId, Map<Long, String> oldUnits, Map<Long, String> oldUnitsTcs,
			Map<String, String> newUnits) {
		try {
			LoginUsers user = userDao.findById(userId);
			List<TestCodeUnits> testCodeUnits = testCodeUnits();
			testCodeUnits.forEach((tcu) -> {
				boolean flag = false;
				System.out.println(oldUnits);
				System.out.println(oldUnitsTcs);
				System.out.println(tcu.getId());
				if (oldUnits.containsKey(tcu.getId()) && (!oldUnits.get(tcu.getId()).equals(tcu.getDisplayUnit())
						|| !oldUnitsTcs.get(tcu.getId()).equals(tcu.getInstumentUnit()))) {
					System.out.println(tcu);
					try {
						saveTestCodeUnitsLog(tcu);
					} catch (Exception e) {
						e.printStackTrace();
					}
					tcu.setActiveStatus(true);
					tcu.setDisplayUnit(oldUnits.get(tcu.getId()));
					tcu.setInstumentUnit(oldUnitsTcs.get(tcu.getId()));
					flag = true;
					System.out.println(tcu);

				} else if (!oldUnits.containsKey(tcu.getId())) {
					if (tcu.isActiveStatus()) {
						try {
							saveTestCodeUnitsLog(tcu);
						} catch (Exception e) {
							e.printStackTrace();
						}
						tcu.setActiveStatus(false);
						flag = true;
					}
				} else if (oldUnits.containsKey(tcu.getId()) && !tcu.isActiveStatus()) {
					try {
						saveTestCodeUnitsLog(tcu);
					} catch (Exception e) {
						e.printStackTrace();
					}
					tcu.setActiveStatus(true);
					flag = true;
				}
				if (flag) {
					tcu.setUpdatedBy(user.getUsername());
					tcu.setUpdatedOn(new Date());
					getSession().merge(tcu);
				}
			});
			newUnits.forEach((instumentUnit, dispalyUnit) -> {
				TestCodeUnits testCodeUnit = new TestCodeUnits();
				testCodeUnit.setInstumentUnit(instumentUnit);
				testCodeUnit.setDisplayUnit(dispalyUnit);
				testCodeUnit.setCreatedBy(user.getUsername());
				testCodeUnit.setCreatedOn(new Date());
				System.out.println(testCodeUnit);
				getSession().save(testCodeUnit);
			});
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private void saveTestCodeUnitsLog(TestCodeUnits tcu) throws IllegalAccessException, InvocationTargetException {
		TestCodeUnitsLog log = new TestCodeUnitsLog();
		BeanUtils.copyProperties(log, tcu);
		log.setId(null);
		log.setTestCodeUnits(tcu);
		getSession().save(log);
	}

	public List<TestCode> testCodes() {
		return getSession().createCriteria(TestCode.class).list();
	}

	public String checkTestCodeName(String testCode, String type) {
		TestCode testcode = (TestCode) getSession().createCriteria(TestCode.class)
				.add(Restrictions.eq("testCode", testCode)).add(Restrictions.eq("type", testCode)).uniqueResult();
		if (testcode != null) {
			return "Valied";
		} else
			return "In-Valied";
	}

	public List<TestCodeUnits> testCodeUnits(boolean b) {
		return getSession().createCriteria(TestCodeUnits.class).add(Restrictions.eq("activeStatus", b)).list();
	}

	public boolean mergeTestCodes(Long userId, Map<Long, String> oldTestCodes, Map<Long, String> oldDispalyTestCodes,
			Map<Long, String> oldOrders, Map<Long, Long> oldUnits, Map<Integer, String> newTestCodes,
			Map<Integer, String> newDispalyTestCodes, Map<Integer, String> newOrders, Map<Integer, Long> newUnits,
			String instument) {
		try {
			LoginUsers user = userDao.findById(userId);
			List<TestCodeUnits> testCodeUnits = testCodeUnits(true);
			Map<Long, TestCodeUnits> unitsMap = new HashMap<>();
			testCodeUnits.forEach((unit) -> {
				unitsMap.put(unit.getId(), unit);
			});
			List<TestCode> testCodes = activeInsturmenttestCodes(instument, null);
			testCodes.forEach((tc) -> {
				boolean flag = false;
				if (oldTestCodes.containsKey(tc.getId()) && (!oldTestCodes.get(tc.getId()).equals(tc.getTestCode())
						|| !oldDispalyTestCodes.get(tc.getId()).equals(tc.getDisPalyTestCode())
						|| !oldOrders.get(tc.getId()).equals(tc.getOrderNo() + "")
						|| !oldUnits.get(tc.getId()).equals(tc.getTestCodeUints().getId()))) {
					System.out.println(tc);
					try {
						saveTestCodeLog(tc);
					} catch (Exception e) {
						e.printStackTrace();
					}
					tc.setActiveStatus(true);
					tc.setTestCode(oldTestCodes.get(tc.getId()));
					tc.setDisPalyTestCode(oldDispalyTestCodes.get(tc.getId()));
					tc.setOrderNo(Integer.parseInt(oldOrders.get(tc.getId())));
					tc.setTestCodeUints(unitsMap.get(oldUnits.get(tc.getId())));
					flag = true;
					System.out.println(tc);

				} else if (!oldTestCodes.containsKey(tc.getId())) {
					if (tc.isActiveStatus()) {
						try {
							saveTestCodeLog(tc);
						} catch (Exception e) {
							e.printStackTrace();
						}
						tc.setActiveStatus(false);
						flag = true;
					}
				} else if (oldTestCodes.containsKey(tc.getId()) && !tc.isActiveStatus()) {
					try {
						saveTestCodeLog(tc);
					} catch (Exception e) {
						e.printStackTrace();
					}
					tc.setActiveStatus(true);
					flag = true;
				}
				if (flag) {
					tc.setUpdatedBy(user.getUsername());
					tc.setUpdatedOn(new Date());
//					getSession().merge(tc);
				}
			});
			newTestCodes.forEach((rowNo, testCode) -> {
				TestCode tc = new TestCode();
				tc.setTestCode(newTestCodes.get(rowNo));
				tc.setDisPalyTestCode(newDispalyTestCodes.get(rowNo));
				tc.setOrderNo(Integer.parseInt(newOrders.get(rowNo)));
				tc.setTestCodeUints(unitsMap.get(newUnits.get(rowNo)));
				tc.setCreatedBy(user.getUsername());
				tc.setCreatedOn(new Date());
				tc.setInstrument(instument);
				if (tc.getInstrument().equals("STAGO")) {
					tc.setMethodType("Coagulation");
				} else if (tc.getInstrument().equals("SYSMEX")) {
					tc.setMethodType("Hematology");
				} else if (tc.getInstrument().equals("VITROS")) {
					tc.setMethodType("Clinical Chemistry");
				}

				System.out.println(tc.getInstrument());
				getSession().save(tc);
			});
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private void saveTestCodeLog(TestCode tc) throws IllegalAccessException, InvocationTargetException {
		TestCodeLog log = new TestCodeLog();
		BeanUtils.copyProperties(log, tc);
		log.setId(null);
		log.setTestCodeId(tc);
		getSession().save(log);
	}

	public List<TestCode> testCodes(String instument) {
		return getSession().createCriteria(TestCode.class).add(Restrictions.eq("instrument", instument))
				.addOrder(Order.asc("orderNo")).list();
	}

	public List<TestCode> activeInsturmenttestCodes(String instument, Boolean status) {
		Criteria cri = getSession().createCriteria(TestCode.class).add(Restrictions.eq("instrument", instument))
				.addOrder(Order.asc("orderNo"));
		if (status != null)
			cri.add(Restrictions.eq("activeStatus", status));
		return cri.list();
	}

	public List<TestCode> testCodes(boolean status, String insturment) {
		return getSession().createCriteria(TestCode.class).add(Restrictions.eq("activeStatus", status))
				.add(Restrictions.eq("instrument", insturment)).addOrder(Order.asc("orderNo")).list();
	}

	public Map<String, List<TestCode>> instrumentWiseTestCodes() {
//		StatusMaster activeStatus = statusDao.statusMaster(StatusMasterCodes.ACTIVE.toString());
//		Map<String, InstrumentIpAddress> instumentMap = activeInstruments(activeStatus);
		Map<String, List<TestCode>> map = new HashMap<>();
//		for(Map.Entry<String, InstrumentIpAddress> insturments : instumentMap.entrySet()) {
//			map.put(insturments.getKey(), testCodes(true, insturments.getKey()));
//		}
		return map;
	}

	public boolean createStudy(Long userId, String studyNo, List<Integer> gorupRows, Map<Integer, String> groupNames,
			Map<Integer, String> genders, Map<Integer, Integer> froms, Map<Integer, Integer> tos,
			List<Long> testCodeIds, Map<Long, Integer> tcOrder, List<Long> profileIdsd, Date studyDate) {
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		for (Long id : profileIdsd) {
			if (flag) {
				sb.append(",").append(id);
			} else {
				sb.append(id);
				flag = true;
			}
		}
		LoginUsers user = userDao.findById(userId);
		StatusMaster activeStatus = statusDao.statusMaster(StatusMasterCodes.ACTIVE.toString());
		StatusMaster status = (StatusMaster) getSession().createCriteria(StatusMaster.class)
				.add(Restrictions.eq("statusCode", "SI")).uniqueResult();
		String[] std = studyNo.split("\\/");
		String animalPrefix = std[3] + "/";
		StudyMaster studyMaster = new StudyMaster();
		studyMaster.setProfileIds(sb.toString());
		studyMaster.setCreatedBy(user.getUsername());
		studyMaster.setCreatedOn(new Date());
		studyMaster.setStudyNo(studyNo);
		studyMaster.setStudyDesc(studyNo);
		studyMaster.setStartDate(studyDate);
		studyMaster.setStatus(status);
		getSession().save(studyMaster);

		int groupNo = 1;
		int sequenceNo = 1;
		int animalSequenceNo = 1;
		Map<String, GroupInfo> groups = new HashMap<>();
		Map<String, SubGroupInfo> subgroups = new HashMap<>();
		List<SubGroupAnimalsInfo> subGroupAnimalInfoList = new ArrayList<>();
		List<StudyAnimal> animals = new ArrayList<>();
		List<StudyTestCodes> studyTestCodes = new ArrayList<>();
		for (Integer row : gorupRows) {
			String gender = "";
			if (genders.get(row).equals("M"))
				gender = "Male";
			else if (genders.get(row).equals("F"))
				gender = "Female";

			// studt group info and animal info
			GroupInfo group = groups.get(groupNames.get(row));
			if (group == null) {
				group = new GroupInfo();
				group.setGroupName(groupNames.get(row));
				group.setStudy(studyMaster);
				group.setGroupNo(groupNo++);
//				private int noOfAnimals = 0;;
				group.setGender(gender);
				group.setCreatedBy(user.getCreatedBy());
				getSession().save(group);
				groups.put(groupNames.get(row), group);
			} else {
				if (!group.getGender().equals(gender))
					group.setGender("Both");
			}
			SubGroupInfo sgroup = new SubGroupInfo();
			sgroup.setName(group.getGroupName());
			sgroup.setSubGroupNo(group.getGroupNo());
			sgroup.setStudy(studyMaster);
			sgroup.setGroup(group);
			sgroup.setGender(gender);
			getSession().save(sgroup);
			subgroups.put(group.getGroupName() + group.getGender(), sgroup);

			SubGroupAnimalsInfo subGroupAnimalInfo = new SubGroupAnimalsInfo();
			subGroupAnimalInfo.setStudy(studyMaster);
			subGroupAnimalInfo.setGroup(group);
			subGroupAnimalInfo.setSubGroup(sgroup);
			subGroupAnimalInfo.setSequenceNo(sequenceNo++);
			subGroupAnimalInfo.setGender(gender);
			subGroupAnimalInfo.setFrom(froms.get(row) + "");
			subGroupAnimalInfo.setTo(tos.get(row) + "");
			subGroupAnimalInfo.setCount(tos.get(row) - froms.get(row));
			getSession().save(subGroupAnimalInfo);
			subGroupAnimalInfoList.add(subGroupAnimalInfo);
			for (int start = froms.get(row); start <= tos.get(row); start++) {
				StudyAnimal animal = new StudyAnimal();
				if (start < 10) {
					animal.setAnimalNo(animalPrefix + "00" + start);
				} else if (start < 100) {
					animal.setAnimalNo(animalPrefix + "0" + start);
				} else
					animal.setAnimalNo(animalPrefix + start);
				animal.setStudy(studyMaster);
				animal.setPermanentNo(animal.getAnimalNo());
				animal.setAnimalId(start);
				animal.setGenderCode(genders.get(row));
				animal.setGender(gender);
				animal.setSequnceNo(animalSequenceNo++);
				animal.setAnimalStatus(activeStatus);
				animal.setCreatedBy(user.getUsername());
				animal.setGroupInfo(group);
				animal.setSubGrop(subGroupAnimalInfo);
				getSession().save(animal);
				animals.add(animal);
			}
		}

//		Map<String, InstrumentIpAddress> instumentMap = activeInstruments(activeStatus);
//		List<TestCode> testcodes = (List<TestCode>) getSession().createCriteria(TestCode.class)
//				.add(Restrictions.in("id", testCodeIds)).list();
//		for (TestCode tc : testcodes) {
//			StudyTestCodes stc = new StudyTestCodes();
//			stc.setStudy(studyMaster);
//			stc.setTestCode(tc);
//			stc.setInstrument(instumentMap.get(tc.getInstrument()));
//			stc.setCreatedBy(user.getUsername());
//			stc.setOrderNo(tcOrder.get(tc.getId()));
//			getSession().save(stc);
//		}
		UserWiseStudiesAsignMaster uwsam = new UserWiseStudiesAsignMaster();
		uwsam.setStudyMaster(studyMaster);
		uwsam.setUserId(user);
		uwsam.setCreatedBy(user.getUsername());
		uwsam.setCreatedOn(new Date());
		uwsam.setUpdateReason("");
		uwsam.setRoleId(user.getRole());
		getSession().save(uwsam);
		if (!user.getUsername().equals("superadmin")) {
			LoginUsers superadmin = userDao.findByusername("superadmin");
			uwsam = new UserWiseStudiesAsignMaster();
			uwsam.setStudyMaster(studyMaster);
			uwsam.setUserId(superadmin);
			uwsam.setCreatedBy(superadmin.getUsername());
			uwsam.setCreatedOn(new Date());
			uwsam.setUpdateReason("");
			uwsam.setRoleId(superadmin.getRole());
			getSession().save(uwsam);
		}
//		studyMaster, groups, subgroups, subGroupAnimalInfoList, animals, studyTestCodes
		return true;
	}

	private List<TestCodeProfileParameters> testCodeProfileParametersByProfileIds(List<Long> profileIds) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(TestCodeProfileParameters.class)
				.add(Restrictions.in("testCodeProfile.id", profileIds)).list();
	}

	private List<TestCodeProfile> testCodeProfileByIds(List<Long> profileIds) {
		return getSession().createCriteria(TestCodeProfile.class).add(Restrictions.in("profileId", profileIds))
				.addOrder(Order.asc("profileId")).list();
	}

	public SortedMap<Integer, InstrumentIpAddress> activeInstruments(StatusMaster activeStatus) {
		List<InstrumentIpAddress> insturments = allActiveInsturments();
		SortedMap<Integer, InstrumentIpAddress> instumentMap = new TreeMap<>();
		for (InstrumentIpAddress ins : insturments) {
			instumentMap.put(ins.getOrderNo(), ins);
		}
		return instumentMap;
	}

	private SortedMap<String, InstrumentIpAddress> activeInstrumentsNameWise(StatusMaster activeStatus) {
		List<InstrumentIpAddress> insturments = allActiveInsturments();
		SortedMap<String, InstrumentIpAddress> instumentMap = new TreeMap<>();
		for (InstrumentIpAddress ins : insturments) {
			instumentMap.put(ins.getInstrumentName(), ins);
		}
		return instumentMap;
	}

	public List<TestCodeProfile> allActiveProfiles() {
		// TODO Auto-generated method stub
		List<TestCodeProfile> profiles = getSession().createCriteria(TestCodeProfile.class)
				.add(Restrictions.eq("activeStatus", true)).list();
		return profiles;
	}

	public List<InstrumentIpAddress> allActiveInsturments() {
		StatusMaster activeStatus = statusDao.statusMaster(StatusMasterCodes.ACTIVE.toString());
		List<InstrumentIpAddress> list = getSession().createCriteria(InstrumentIpAddress.class)
				.add(Restrictions.eq("activeStatus", activeStatus)).addOrder(Order.asc("orderNo")).list();
		return list;
	}

	public Map<String, List<TestCode>> insturmentTestCodes(Long insturmentId) {
		InstrumentIpAddress ins = instrumentIpAddress(insturmentId);
		List<TestCode> list = getSession().createCriteria(TestCode.class)
				.add(Restrictions.eq("instrument", ins.getInstrumentName())).add(Restrictions.eq("activeStatus", true))
				.list();
		Map<String, List<TestCode>> map = new HashMap<>();
		map.put(ins.getInstrumentName(), list);
		return map;
	}

	private InstrumentIpAddress instrumentIpAddress(Long insturmentId) {
		return (InstrumentIpAddress) getSession().get(InstrumentIpAddress.class, insturmentId);
	}

	public boolean testCodesProfilesSave(Long userId, String studyNo, Long instrumentId, String profileName,
			List<Long> testCodeIds, Map<Long, Integer> tcOrder) {
		try {
			LoginUsers user = userDao.findById(userId);
			StatusMaster activeStatus = statusDao.statusMaster(StatusMasterCodes.ACTIVE.toString());
			InstrumentIpAddress instrument = instrumentIpAddress(instrumentId);
			TestCodeProfile profile = new TestCodeProfile();
			profile.setInsturment(instrument);
			profile.setProfileName(profileName);
			profile.setCreatedBy(user.getUsername());
			getSession().save(profile);

			List<TestCode> testcodes = (List<TestCode>) getSession().createCriteria(TestCode.class)
					.add(Restrictions.in("id", testCodeIds)).list();
			for (TestCode tc : testcodes) {
				TestCodeProfileParameters stc = new TestCodeProfileParameters();
				stc.setTestCodeProfile(profile);
				stc.setTestCode(tc);
				stc.setOrderNo(tcOrder.get(tc.getId()));
				getSession().save(stc);
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public TestCodeProfile testCodeProfileByStudyNo(String profileName) {
		TestCodeProfile profile = (TestCodeProfile) getSession().createCriteria(TestCodeProfile.class)
				.add(Restrictions.eq("profileName", profileName)).uniqueResult();
		return profile;
	}

	public TestCodeProfile testCodeProfileById(Long profileId) {
		// TODO Auto-generated method stub
		return (TestCodeProfile) getSession().get(TestCodeProfile.class, profileId);
	};

	public List<TestCodeProfileParameters> testCodeProfileParameters(Long profileId) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(TestCodeProfileParameters.class)
				.add(Restrictions.eq("testCodeProfile.id", profileId)).list();
	}

	public StudyMasterDto studyView(Long studyId) {
		StudyMasterDto dto = new StudyMasterDto();
		StudyMaster study = studyDao.findByStudyId(studyId);
		dto.setStudId(studyId);
		dto.setStudyNo(study.getStudyNo());
		List<StudyAnimal> animals = accessionDao.allStudyAnimals(studyId);
		for (StudyAnimal animal : animals) {
			System.out.println(animal.getAnimalId() + "\t" + animal.getPermanentNo());
		}
		// Sort the list based on age
		Collections.sort(animals, new Comparator<StudyAnimal>() {
			public int compare(StudyAnimal p1, StudyAnimal p2) {
				return p1.getAnimalId() - p2.getAnimalId();
			}
		});
		for (StudyAnimal animal : animals) {
			System.out.println(animal.getAnimalId() + "\t" + animal.getPermanentNo());
		}
		dto.setAnimals(animals);

		List<SubGroupAnimalsInfo> groups = expermentalDesignDao.allSubGroupAnimalsInfos(studyId);
		dto.setGroups(groups);
		List<StudyTestCodes> testCodes = studyTestCodes(studyId);
		for (StudyTestCodes tc : testCodes) {
			List<StudyTestCodes> tcs = dto.getInsturmentViewTestCodes().get(tc.getInstrument().getInstrumentName());
			if (tcs == null)
				tcs = new ArrayList<>();
			tcs.add(tc);
			dto.getInsturmentViewTestCodes().put(tc.getInstrument().getInstrumentName(), tcs);
		}

		Map<String, List<StudyTestCodes>> insturmentViewTestCodesTemp = dto.getInsturmentViewTestCodes();
		dto.setInsturmentViewTestCodes(new HashMap<>());
		for (Map.Entry<String, List<StudyTestCodes>> m : insturmentViewTestCodesTemp.entrySet()) {
			List<StudyTestCodes> list = m.getValue();
			// sort by orderNo
			Collections.sort(list, new Comparator<StudyTestCodes>() {
				@Override
				public int compare(StudyTestCodes o1, StudyTestCodes o2) {
					return o1.getOrderNo() - o2.getOrderNo();
				}
			});
			// dto.getInsturmentViewTestCodes().put(m.getKey(), list);
		}
		Map<String, List<StudyTestCodes>> insturmentViewTestCodes = new LinkedHashMap<>();
		List<InstrumentIpAddress> insturments = allActiveInsturments();
		for (InstrumentIpAddress ins : insturments) {
			insturmentViewTestCodes.put(ins.getInstrumentName(),
					insturmentViewTestCodesTemp.get(ins.getInstrumentName()));
		}
		dto.setInsturmentViewTestCodes(insturmentViewTestCodes);
		return dto;

	}

	private List<StudyTestCodes> studyTestCodes(Long studyId) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(StudyTestCodes.class).add(Restrictions.eq("study.id", studyId)).list();
	}

	public Map<String, StudyAnimal> animilGropInfo(Long studyId, List<String> animalList) {
		Map<String, StudyAnimal> map = new HashMap<>();
		if (animalList.size() > 0) {
			List<StudyAnimal> list = getSession().createCriteria(StudyAnimal.class)
					.add(Restrictions.eq("study.id", studyId)).add(Restrictions.in("animalNo", animalList)).list();
			list.stream().forEach((animal) -> {
				map.put(animal.getPermanentNo(), animal);
			});
		}
		return map;
	}

	public StudyAnimal studyAnimal(Long studyId, String animalNo) {
		// TODO Auto-generated method stub
		return (StudyAnimal) getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", studyId))
				.add(Restrictions.eq("permanentNo", animalNo)).uniqueResult();
	}

	public List<SysmexData> sysmexDataByStudyIdAnimalNo(Long studyId, String animalNo, String startDate,
			String sampleType) throws ParseException {
		// TODO Auto-generated method stub
		InstrumentIpAddress ip = studyDao.instumentIpAddress("SYSMEX");
		List<StudyTestCodes> tcs = studyDao.studyInstumentTestCodes(ip.getId(), studyId, null);
		Criteria cri = getSession().createCriteria(SysmexData.class).add(Restrictions.eq("study.id", studyId));
		if (animalNo != null)
			cri.add(Restrictions.eq("animalNumber", animalNo)).list();
		else if (startDate != null && !startDate.trim().equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
			System.out.println(startDate.trim());
			String[] st = startDate.split("\\-");
			String s = st[0] + "-" + st[1] + "-" + st[2];
			startDate = s;
			System.out.println(startDate.trim());
			Date fromDate = sdf.parse(startDate.trim());
			Calendar c = Calendar.getInstance();
			c.setTime(fromDate);
			c.add(Calendar.DATE, 1);
			Date toDate = sdf.parse(sdf.format(c.getTime()));
			System.out.println(fromDate + "\t" + toDate);
			cri.add(Restrictions.between("startTime", fromDate, toDate));
		}
		if (sampleType != null && !sampleType.equals("Both")) {
			cri.add(Restrictions.eq("testRunType", sampleType));
		}
		List<SysmexData> result = cri.list();

		SortedMap<String, List<SysmexData>> animalWise = new TreeMap<>();
		for (SysmexData data : result) {
			data.setSysmexTestCodeData(sysmexTestCodeDataForAnimal(data, null));
			List<SysmexData> lis = animalWise.get(data.getAnimal().getPermanentNo());
			if (lis == null)
				lis = new ArrayList<>();
			lis.add(data);
			animalWise.put(data.getAnimal().getPermanentNo(), lis);
		}
		for (Map.Entry<String, List<SysmexData>> temp : animalWise.entrySet()) {
			System.out.println(temp.getKey());

			checkSysmexFinalResult(temp.getValue(), tcs);
		}
		return result;
	}

	public List<SysmexTestCodeData> sysmexTestCodeDataForAnimal(SysmexData data, List<Long> tcIds) {
		// TODO Auto-generated method stub
		Criteria cri = getSession().createCriteria(SysmexTestCodeData.class)
				.add(Restrictions.eq("sysmexData.id", data.getId()));
		if (tcIds != null && tcIds.size() > 0)
			cri.add(Restrictions.in("studyTestCode.id", tcIds));
		List<SysmexTestCodeData> list = cri.list();
		return list;
	}

	public List<SysmexData> checkSysmexFinalResult(List<SysmexData> dataList, List<StudyTestCodes> tcs) {
		// MAP<StudyTestCode-orderNo, List<SysmexTestCodeData>>
		Map<Integer, List<SysmexTestCodeData>> testcodeWiseData = new HashMap<>();
		for (SysmexData sd : dataList) {
			List<SysmexTestCodeData> tcData = sd.getSysmexTestCodeData();
			for (SysmexTestCodeData stcd : tcData) {
				if (stcd.getStudyTestCode() != null) {
					List<SysmexTestCodeData> list = testcodeWiseData.get(stcd.getStudyTestCode().getOrderNo());
					if (list == null)
						list = new ArrayList<>();
					list.add(stcd);
					testcodeWiseData.put(stcd.getStudyTestCode().getOrderNo(), list);
				}
			}
		}

		for (StudyTestCodes tc : tcs) {
			System.out.println(tc.getTestCode().getDisPalyTestCode() + "\t" + tc.getOrderNo());
			List<SysmexTestCodeData> list = testcodeWiseData.get(tc.getOrderNo());
			if (list != null && list.size() > 0) {
				if (list.size() > 1) {
					boolean flag = false;
					for (SysmexTestCodeData sdl : list) {
						if (flag && sdl.isFinalValue()) {
							sdl.setFinalValue(false);
						} else if (sdl.isFinalValue()) {
							flag = true;
						}
					}
				} else {
					list.get(0).setFinalValue(true);
				}
			}

		}
		return dataList;
	}

	public boolean changeProfileStatus(Long userId, List<Long> profileId) {
		try {
			LoginUsers user = userDao.findById(userId);
			List<TestCodeProfile> list = allTestCodeProfiles();
			list.stream().forEach((profile) -> {
				if (profileId.contains(profile.getId())) {
					if (!profile.isActiveStatus()) {
						profile.setActiveStatus(true);
						profile.setUpdatedBy(user.getUsername());
						profile.setUpdatedOn(new Date());
						saveTestCodeProfileLog(profile);
					}
				} else {
					if (profile.isActiveStatus()) {
						profile.setActiveStatus(false);
						profile.setUpdatedBy(user.getUsername());
						profile.setUpdatedOn(new Date());
						saveTestCodeProfileLog(profile);
					}
				}
			});
			return true;
		} catch (Exception e) {
			// TODO: handele exception
			e.printStackTrace();
		}
		return false;
	}

	private void saveTestCodeProfileLog(TestCodeProfile profile) {
		// TODO Auto-generated method stub
		TestCodeProfileLog log = new TestCodeProfileLog();
		try {
			BeanUtils.copyProperties(log, profile);
			log.setTestCodeProfile(profile);
			getSession().save(profile);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<TestCodeProfile> allTestCodeProfiles() {
		// TODO Auto-generated method stub
		return getSession().createCriteria(TestCodeProfile.class).list();
	}

	public TestCodeProfile profileData(Long profileId) {
		TestCodeProfile profile = testCodeProfileById(profileId);
		if (profile != null) {
			profile.setParameters(profileParameters(profile.getId()));
		}
		return profile;
	}

	private List<TestCodeProfileParameters> profileParameters(Long profileId) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(TestCodeProfileParameters.class)
				.add(Restrictions.eq("testCodeProfile.id", profileId)).addOrder(Order.asc("orderNo")).list();
	}

	public List<StudyAnimal> allStudyAnimas(Long id) {
		return getSession().createCriteria(StudyAnimal.class).add(Restrictions.eq("study.id", id)).list();
	}

	public boolean saveIntrumentAndPerameters(Long userId, Long observationDatesId, String studyNo,
			List<Long> testCodeIds, Map<Long, Integer> tcOrder, List<Long> profileIds, Date studyDate, String observationFor) {
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		for (Long id : profileIds) {
			testCodeIds.add(id);
			if (flag) {
				sb.append(",").append(id);
			} else {
				sb.append(id);
				flag = true;
			}
		}
		LoginUsers user = userDao.findById(userId);
		StatusMaster activeStatus = statusDao.statusMaster(StatusMasterCodes.ACTIVE.toString());
		StudyAcclamatizationDates studyAcclamatizationDates = null;
		StudyTreatmentDataDates studyTreatmentDataDates = null;
		StudyMaster studyMaster = null;
		if(observationFor.equals("Acclimatization")) {
			studyAcclamatizationDates = acclimatizationDao
					.studyAcclamatizationDatesById(observationDatesId);
			studyMaster = studyAcclamatizationDates.getStudy();
		}else {
			studyTreatmentDataDates = acclimatizationDao
					.studyTreatmentDataDatesById(observationDatesId);
			studyMaster = studyTreatmentDataDates.getStudy();
		}
		
//		studyMaster.setIntrumentConfuguraiton(true);
//		studyMaster.setIntrumentConfuguredBy(user.getId());
//		studyMaster.setIntrumentConfuguredOn(new Date());

		ObservationInturmentConfiguration observationInturmentConfiguration = instrumentDao
				.observationInturmentConfiguration(observationDatesId, observationFor);
		if (observationInturmentConfiguration == null) {
			observationInturmentConfiguration = new ObservationInturmentConfiguration();
			observationInturmentConfiguration.setStudy(studyMaster);
			observationInturmentConfiguration.setStudyAcclamatizationDates(studyAcclamatizationDates);
			observationInturmentConfiguration.setStudyTreatmentDataDates(studyTreatmentDataDates);
			getSession().save(observationInturmentConfiguration);
		}

		Map<String, InstrumentIpAddress> instumentMap = activeInstrumentsNameWise(activeStatus);
		List<TestCode> testcodes = (List<TestCode>) getSession().createCriteria(TestCode.class)
				.add(Restrictions.in("id", testCodeIds)).list();
		Set<Long> ids = new HashSet<>();
		for(Map.Entry<String, InstrumentIpAddress> in : instumentMap.entrySet())
			System.out.println(in.getKey());
		if (observationInturmentConfiguration != null) {
			for (TestCode tc : testcodes) {
				System.out.println(tc.getInstrument());
				StudyTestCodes stc = null;
				stc = (StudyTestCodes) getSession().createCriteria(StudyTestCodes.class)
						.add(Restrictions.eq("observationInturmentConfiguration.id",
								observationInturmentConfiguration.getId()))
						.add(Restrictions.eq("instrument.id", instumentMap.get(tc.getInstrument()).getId()))
						.add(Restrictions.eq("testCode.id", tc.getId())).uniqueResult();
				if (stc != null) {
					if (!stc.isActiveStatus() || stc.getOrderNo() != tcOrder.get(tc.getId())) {
						stc.setActiveStatus(true);
						stc.setOrderNo(tcOrder.get(tc.getId()));
						tc.setUpdatedBy(user.getUsername());
						tc.setUpdatedOn(new Date());
					}
					ids.add(stc.getTestCode().getId());
				} else {
					stc = new StudyTestCodes();
					stc.setObservationInturmentConfiguration(observationInturmentConfiguration);
					stc.setStudy(studyMaster);
					stc.setTestCode(tc);
					stc.setInstrument(instumentMap.get(tc.getInstrument()));
					stc.setCreatedBy(user.getUsername());
					stc.setOrderNo(tcOrder.get(tc.getId()));
					getSession().save(stc);
					ids.add(stc.getTestCode().getId());
				}
			}
			List<StudyTestCodes> stcs = getSession().createCriteria(StudyTestCodes.class)
					.add(Restrictions.eq("observationInturmentConfiguration.id",
							observationInturmentConfiguration.getId()))
					.add(Restrictions.not(Restrictions.in("testCode.id", ids))).list();
			for (StudyTestCodes tc : stcs) {
				tc.setActiveStatus(false);
				tc.setUpdatedBy(user.getUsername());
				tc.setUpdatedOn(new Date());
			}
		}

//		studyMaster, groups, subgroups, subGroupAnimalInfoList, animals, studyTestCodes
		return true;
	}

	public List<GroupInfo> groupInfos(Long id) {
		// TODO Auto-generated method stub
		return getSession().createCriteria(GroupInfo.class).add(Restrictions.eq("study.id", id)).list();
	}
}