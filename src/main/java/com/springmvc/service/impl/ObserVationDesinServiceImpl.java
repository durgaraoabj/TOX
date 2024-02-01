package com.springmvc.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.covide.dto.ObservationScheduleDto;
import com.springmvc.dao.ObserVationDesinDao;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;
import com.springmvc.service.ObserVationDesinService;

@Service("obserVationDesinService")
public class ObserVationDesinServiceImpl implements ObserVationDesinService {
	
	@Autowired
	ObserVationDesinDao obvdDao;

	@Override
	public ObservationScheduleDto getObservationScheduleDetails(Long studyId) {
		ObservationScheduleDto  obvsdto = null;
		List<StdSubGroupObservationCrfs> obvList = null;
		List<SubGroupAnimalsInfoAll> sgaList = null;
		List<SubGroupAnimalsInfoCrfDataCount> sgaicdcList = null;
		Map<Long, StdSubGroupObservationCrfs> obsvMap = new HashMap<>();
		Map<Long, List<SubGroupAnimalsInfoAll>> animalMap = new HashMap<>();
		Map<Long, List<SubGroupAnimalsInfoCrfDataCount>> map2 = new HashMap<>();
		Map<Long, List<SubGroupAnimalsInfoAll>> fanimalMap = new HashMap<>();
		StudyMaster sm = null;
		try {
			obvsdto = obvdDao.getObservationScheduleDetails(studyId);
			if(obvsdto != null) {
				sm = obvsdto.getSm();
				obvList = obvsdto.getObvList();
				sgaList = obvsdto.getSgaList();
				sgaicdcList = obvsdto.getSgaicdcList();
				if(sgaList != null && sgaList.size() > 0) {
					List<SubGroupAnimalsInfoAll> tempList = null;
					for(SubGroupAnimalsInfoAll sgai : sgaList) {
						if(animalMap.containsKey(sgai.getSubGroup().getId())) {
							tempList = animalMap.get(sgai.getSubGroup().getId());
							tempList.add(sgai);
							animalMap.put(sgai.getSubGroup().getId(), tempList);
						}else {
							tempList = new ArrayList<>();
							tempList.add(sgai);
							animalMap.put(sgai.getSubGroup().getId(), tempList);
						}
					}
				}
				if(sgaicdcList != null && sgaicdcList.size() > 0) {
					SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
					List<SubGroupAnimalsInfoCrfDataCount> tempList = null;
					for(SubGroupAnimalsInfoCrfDataCount sgaidc : sgaicdcList) {
						if(sgaidc.getEntryDate() != null && !sgaidc.getEntryDate().equals("")) {
							Date fdate = sdformat.parse(sgaidc.getEntryDate());
							String fentryDate = sdf.format(fdate);
							sgaidc.setEntryDate(fentryDate);
						}
						if(map2.containsKey(sgaidc.getSubGroupAnimalsInfoAll().getId())) {
							tempList = map2.get(sgaidc.getSubGroupAnimalsInfoAll().getId());
							tempList.add(sgaidc);
							map2.put(sgaidc.getSubGroupAnimalsInfoAll().getId(), tempList);
						}else {
							tempList = new ArrayList<>();
							tempList.add(sgaidc);
							map2.put(sgaidc.getSubGroupAnimalsInfoAll().getId(), tempList);
						}
					}
				}
				for(StdSubGroupObservationCrfs sgoc : obvList) {
					String groupName = sgoc.getSubGroupInfo().getGroup().getGroupName();
					sgoc.setGroupName(groupName);
					List<String> datesList = new ArrayList<>();
					if(sgoc.getDayType().equals("day")) {
						String tempDays = sgoc.getDays();
						String[] temp = tempDays.split("\\,");
						if(temp.length > 0) {
							for(String st : temp) {
								if(st != null && !st.equals("")) {
									SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
									Calendar cal = Calendar.getInstance();
									cal.setTime(sm.getStartDate());
									// manipulate date
									cal.add(Calendar.DATE, Integer.parseInt(st)); 
									Date finalDate = cal.getTime();
									String fdate = sdf.format(finalDate);
									datesList.add(fdate);
								}
							}
						}
						sgoc.setConvDates(datesList);
					}else {
						String tempDays = sgoc.getDays();
						String[] temp = tempDays.split("\\,");
						if(temp.length > 0) {
							Date stDate = null;
							Date finalDate = null;
							SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
							for(String st : temp) {
								if(st != null && !st.equals("")) {
									Calendar cal = Calendar.getInstance();
									cal.setTime(sm.getStartDate());
									stDate = cal.getTime();
									int dayOfWeekNum = cal.get(Calendar.DAY_OF_WEEK);
									stDate = cal.getTime();
									int dffDay =0;
									if(dayOfWeekNum < 7)
										dffDay = 7 - dayOfWeekNum;
									cal.add(Calendar.DATE, dffDay);
									finalDate = cal.getTime();
									
									cal.setTime(finalDate);
									int count = Integer.parseInt(st);
									if(count != 0) {
										int no = 7*count;
										cal.add(Calendar.DATE, no);
										finalDate = cal.getTime();
										
										cal.add(Calendar.DATE, -6);
										stDate = cal.getTime();
									}
									if(stDate != null && finalDate != null) {
										String startDate = sdf2.format(stDate);
										String endDate = sdf2.format(finalDate);
										String fStr = startDate +" To "+endDate;
										datesList.add(fStr);
									}
								}
							}
							
						}
						
					}
					sgoc.setConvDates(datesList);
					obsvMap.put(sgoc.getId(), sgoc);
					List<SubGroupAnimalsInfoAll> tsgaList = animalMap.get(sgoc.getSubGroupInfo().getId());
					fanimalMap.put(sgoc.getId(), tsgaList);
				}
				obvsdto.setObsvMap(obsvMap);
				obvsdto.setAnimalMap(fanimalMap);
				obvsdto.setMap2(map2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obvsdto;
	}

	@Override
	public ObservationScheduleDto todayObservationScheduleDetails(Long studyId) {
		ObservationScheduleDto  obvsdto = null;
		List<StdSubGroupObservationCrfs> obvList = null;
		List<SubGroupAnimalsInfoAll> sgaList = null;
		List<SubGroupAnimalsInfoCrfDataCount> sgaicdcList = null;
		Map<Long, StdSubGroupObservationCrfs> obsvMap = new HashMap<>();
		Map<Long, List<SubGroupAnimalsInfoAll>> animalMap = new HashMap<>();
		Map<Long, List<SubGroupAnimalsInfoCrfDataCount>> map2 = new HashMap<>();
		Map<Long, List<SubGroupAnimalsInfoAll>> fanimalMap = new HashMap<>();
		StudyMaster sm = null;
		try {
			obvsdto = obvdDao.getObservationScheduleDetails(studyId);
			if(obvsdto != null) {
				sm = obvsdto.getSm();
				obvList = obvsdto.getObvList();
				sgaList = obvsdto.getSgaList();
				sgaicdcList = obvsdto.getSgaicdcList();
				if(sgaList != null && sgaList.size() > 0) {
					List<SubGroupAnimalsInfoAll> tempList = null;
					for(SubGroupAnimalsInfoAll sgai : sgaList) {
						if(animalMap.containsKey(sgai.getSubGroup().getId())) {
							tempList = animalMap.get(sgai.getSubGroup().getId());
							tempList.add(sgai);
							animalMap.put(sgai.getSubGroup().getId(), tempList);
						}else {
							tempList = new ArrayList<>();
							tempList.add(sgai);
							animalMap.put(sgai.getSubGroup().getId(), tempList);
						}
					}
				}
				if(sgaicdcList != null && sgaicdcList.size() > 0) {
					SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
					List<SubGroupAnimalsInfoCrfDataCount> tempList = null;
					for(SubGroupAnimalsInfoCrfDataCount sgaidc : sgaicdcList) {
						if(sgaidc.getEntryDate() != null && !sgaidc.getEntryDate().equals("")) {
							Date fdate = sdformat.parse(sgaidc.getEntryDate());
							String fentryDate = sdf.format(fdate);
							sgaidc.setEntryDate(fentryDate);
						}
						if(map2.containsKey(sgaidc.getSubGroupAnimalsInfoAll().getId())) {
							tempList = map2.get(sgaidc.getSubGroupAnimalsInfoAll().getId());
							tempList.add(sgaidc);
							map2.put(sgaidc.getSubGroupAnimalsInfoAll().getId(), tempList);
						}else {
							tempList = new ArrayList<>();
							tempList.add(sgaidc);
							map2.put(sgaidc.getSubGroupAnimalsInfoAll().getId(), tempList);
						}
					}
				}
				for(StdSubGroupObservationCrfs sgoc : obvList) {
					String groupName = sgoc.getSubGroupInfo().getGroup().getGroupName();
					sgoc.setGroupName(groupName);
					List<String> datesList = new ArrayList<>();
					boolean flag = false;
					if(sgoc.getSubGroupInfo().getId() == 111)
						System.out.println("condition");
					if(sgoc.getDayType().equals("day")) {
						String tempDays = sgoc.getDays();
						String[] temp = tempDays.split("\\,");
						if(temp.length > 0) {
							for(String st : temp) {
								if(st != null && !st.equals("")) {
									SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
									Calendar cal = Calendar.getInstance();
									cal.setTime(sm.getStartDate());
									// manipulate date
									cal.add(Calendar.DATE, Integer.parseInt(st)); 
									Date finalDate = cal.getTime();
									Date currentDate = new Date();
									String fdate = sdf.format(finalDate);
									String currDate = sdf.format(currentDate);
									Date d1 = sdf.parse(currDate);
									Date d2 = sdf.parse(fdate);
									if(d1.compareTo(d2) == 0) {
										 datesList.add(fdate);
										 flag = true;
									}
								}
							}
						}
						sgoc.setConvDates(datesList);
					}else {
						String tempDays = sgoc.getDays();
						String[] temp = tempDays.split("\\,");
						if(temp.length > 0) {
							Date stDate = null;
							Date finalDate = null;
							SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
							for(String st : temp) {
								if(st != null && !st.equals("")) {
									Calendar cal = Calendar.getInstance();
									cal.setTime(sm.getStartDate());
									stDate = cal.getTime();
									int dayOfWeekNum = cal.get(Calendar.DAY_OF_WEEK);
									stDate = cal.getTime();
									int dffDay =0;
									if(dayOfWeekNum < 7)
										dffDay = 7 - dayOfWeekNum;
									cal.add(Calendar.DATE, dffDay);
									finalDate = cal.getTime();
									
									cal.setTime(finalDate);
									int count = Integer.parseInt(st);
									if(count != 0) {
										int no = 7*count;
										cal.add(Calendar.DATE, no);
										finalDate = cal.getTime();
										
										cal.add(Calendar.DATE, -6);
										stDate = cal.getTime();
									}
									if(stDate != null && finalDate != null) {
										String startDate = sdf2.format(stDate);
										String endDate = sdf2.format(finalDate);
										Date currentDate = new Date();
										String currDate = sdf2.format(currentDate);
										Date cDate = sdf2.parse(currDate);
										Date sDate = sdf2.parse(startDate);
										Date eDate = sdf2.parse(endDate);
										if(cDate.compareTo(sDate) > 0  &&  cDate.compareTo(eDate) < 0) {
											String fStr = startDate +" To "+endDate;
											datesList.add(fStr);
											flag = true;
										}
									}
								}
							}
							
						}
						
					}
					sgoc.setConvDates(datesList);
					if(flag) {
						obsvMap.put(sgoc.getId(), sgoc);
						List<SubGroupAnimalsInfoAll> tsgaList = animalMap.get(sgoc.getSubGroupInfo().getId());
						fanimalMap.put(sgoc.getId(), tsgaList);
					}
					
				}
				obvsdto.setObsvMap(obsvMap);
				obvsdto.setAnimalMap(fanimalMap);
				obvsdto.setMap2(map2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obvsdto;
	}

}
