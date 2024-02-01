package com.springmvc.service;

import com.covide.dto.ObservationScheduleDto;

public interface ObserVationDesinService {

	ObservationScheduleDto getObservationScheduleDetails(Long studyId);

	ObservationScheduleDto todayObservationScheduleDetails(Long studyId);

}
