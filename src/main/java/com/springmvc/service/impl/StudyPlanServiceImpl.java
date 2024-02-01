package com.springmvc.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.covide.advity.reports.InputTextPdf;
import com.covide.advity.reports.TextPdf;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.springmvc.dao.StudyPlanDao;
import com.springmvc.model.StudyPlanFilesDetails;
import com.springmvc.service.StudyPlanService;

@Service("studyPlanService")
public class StudyPlanServiceImpl implements StudyPlanService {
	
	@Autowired
	StudyPlanDao studyPlanDao;

	@Override
	public StudyPlanFilesDetails getStudyPlanFilesDetailsRecord(Long studyId) {
		return studyPlanDao.getStudyPlanFilesDetailsRecord(studyId);
	}

	@Override
	public String saveStudyPlanFilesDetails(String username, MultipartFile file, Long studyId) {
		String result ="Failed";
		Blob blob = null;
		try {
			StudyPlanFilesDetails spfd = new StudyPlanFilesDetails();
			spfd.setCreatedBy(username);
			spfd.setCreatedOn(new Date());
			byte[] byteArr = file.getBytes();
			if(byteArr != null)
				blob = new SerialBlob(file.getBytes());
			spfd.setFile(blob);
			spfd.setFileName(file.getOriginalFilename());
			spfd.setFileType(file.getContentType());
			spfd.setStudyId(studyId);
			long spfdNo = studyPlanDao.saveStudyPlanFilesDetails(spfd);
			if(spfdNo > 0)
				result ="success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String generateStudyPlanReport(Long studyId, HttpServletRequest request, HttpServletResponse response) {
		String result = "Failed";
		try {
			StudyPlanFilesDetails spd = studyPlanDao.getStudyPlanFilesDetailsRecord(studyId);
			if(spd != null) {
				String realPath = request.getServletContext().getRealPath("/");
				String path = realPath + "/StudyPlanReport/";
				File directory = new File(path);
				if (!directory.exists()) {
					directory.mkdirs();
				}
				String fileName = path +"RoleWiseLinks.pdf";
				File newFile = new File(fileName);
				Blob blob = spd.getFile();
				FileOutputStream outFile = new FileOutputStream(newFile);
				outFile.write(blob.getBytes(1L, (int) blob.length()));
				outFile.flush();
				outFile.close();
				
				response.setContentType("application/pdf");
			    OutputStream out = response.getOutputStream();
			    Document document = new Document();
		        PdfWriter writer = PdfWriter.getInstance(document, out);
		        document.open();
		        PdfContentByte cb = writer.getDirectContent();
				List<InputStream> inslist = new ArrayList<InputStream>();
				InputStream stream = new FileInputStream(new File(fileName));
				inslist.add(stream);
				if (inslist.size() > 0) {
					for (InputStream in : inslist) {
			            PdfReader reader = new PdfReader(in);
			            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			                document.newPage();
			                //import the page from source pdf
			                PdfImportedPage page = writer.getImportedPage(reader, i);
			                //add the page to the destination pdf
			                cb.addTemplate(page, 0, 0);
			            }
			        }
			        
			        out.flush();
			        document.close();
			        out.close();
				}
			}else {
				InputTextPdf textPdf = new InputTextPdf();
				textPdf.generateTextPdf(request, response, "No Study Plan Document Aveliable For This Study.");
			}
			
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
