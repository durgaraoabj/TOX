package com.springmvc.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.covide.crf.dto.Crf;
import com.springmvc.dao.TemplateFileDao;
import com.springmvc.model.ObserVationTemplates;
import com.springmvc.model.ObservationTemplateData;
import com.springmvc.service.TemplateFileService;

@Service("templateFileService")
public class TemplateFileServiceImpl implements TemplateFileService {
	
	@Autowired
	TemplateFileDao templateFileDao;

	@Override
	public String writeTemplateFile(CommonsMultipartFile tempfile, HttpSession session) {
		String filePath = "";
		try {
			if(tempfile != null) {
				String fileFolder= session.getServletContext().getRealPath("/")+"template_files/";
				File f = new File(fileFolder);
				if (!f.exists())
					f.mkdir();
				String fileName = tempfile.getOriginalFilename();
				String fileWriteName = fileName.replace(" ", "");
				String type = tempfile.getContentType();
				byte fileDataArray[]=tempfile.getBytes();
				String path = fileFolder+fileWriteName;
				try(FileOutputStream fos=new FileOutputStream(path)){
					fos.write(fileDataArray);
					filePath = path+"##"+fileWriteName+"##"+type;
					session.setAttribute("templateFile", filePath);
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}

	@Override
	public String saveObservationTemplateFile(String filePath, String userName , Crf crf, List<Long> roleIds) {
		String result = "Failed";
		try {
			if(!filePath.equals("")) {
				String[] temp = filePath.split("\\##");
				if(temp.length > 0) {
					File file = new File(temp[0]);
					if(file.exists()) {
						ObservationTemplateData obtd = new ObservationTemplateData();
						ObserVationTemplates obvt = new ObserVationTemplates();
						 byte[] arr = Files.readAllBytes(file.toPath());
						 Blob blob = null;
						 if(arr != null) {
							 blob = new SerialBlob(arr);
						 }
						 obtd.setCreatedBy(userName);
						 obtd.setCreatedOn(new Date());
						 obtd.setFileName(temp[1]);
						 obtd.setType(temp[2]);
						 obtd.setObserVationDesc(crf.getObservationName());
						 obtd.setObserVationName(crf.getObservationDesc());
						 obvt.setBlob(blob);
						 boolean flag = templateFileDao.saveObservationTemplateData(obtd, obvt, crf, roleIds);
						 if(flag)
							 result ="success";
					}else result ="NoFile";
				}else result= "NoFile";
			}else result= "NoFileInSession";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String checkObservationNameValidationStatus(String obserName, String type) {
		String result = "";
		Crf crf = null;
		try {
			crf = templateFileDao.getCrfRecord(obserName, type);
			if(crf != null)
				result ="Observation '"+obserName+"' In Active State. Please Inactive Observation";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
