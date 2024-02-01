package com.springmvc.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.covide.crf.dto.Crf;

public interface TemplateFileService {

	String  writeTemplateFile(CommonsMultipartFile tempfile, HttpSession session);

	String saveObservationTemplateFile(String filePath, String userName, Crf crf, List<Long> roleIds);

	String checkObservationNameValidationStatus(String obserName, String type);

}
