package com.springmvc.dao;

import java.util.List;

import com.covide.crf.dto.Crf;
import com.springmvc.model.ObserVationTemplates;
import com.springmvc.model.ObservationTemplateData;

public interface TemplateFileDao {

	boolean saveObservationTemplateData(ObservationTemplateData obtd, ObserVationTemplates obvt, Crf crf, List<Long> roleIds);

	Crf getCrfRecord(String obserName, String type);

}
