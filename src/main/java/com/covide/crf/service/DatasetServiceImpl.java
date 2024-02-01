package com.covide.crf.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.covide.crf.dao.CrfDAO;
import com.covide.crf.dao.DataSetDao;
import com.covide.crf.dto.DataSet;
import com.covide.crf.dto.DataSetPhase;
import com.covide.crf.dto.DataSetPhasewiseCrfs;
import com.covide.crf.dto.CrfGroupElement;
import com.covide.crf.dto.CrfGroupElementData;
import com.covide.crf.dto.CrfSectionElement;
import com.covide.crf.dto.CrfSectionElementData;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.service.StudyService;

@Service("datasetSer")
public class DatasetServiceImpl implements DatasetService {

	@Autowired
	CrfDAO crfDAO;

	@Autowired
	DataSetDao datasetDAO;
	
	@Override
	public List<DatasetService> findAllDataSets() {
		return datasetDAO.findAllDataSets();
	}

	@Autowired
	CrfService crfSer;
	
	@Autowired
	StudyService studyService;
	
	@Override
	public File exportDataToExcel(DataSet dataSet,   HttpServletRequest request) throws IOException {
	
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy");
		String dateinSting = sdf.format(new  Date());
		
		// Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances of various things like DataFormat, 
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet(dataSet.getName()+"_"+dateinSting);

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        int rowCount = 0;
        // Create a Row
        Row headerRow = sheet.createRow(rowCount++);
        Cell cell = headerRow.createCell(0);
        cell.setCellValue("Dataset Name:");
        cell.setCellStyle(headerCellStyle);
        cell = headerRow.createCell(1);
        cell.setCellValue(dataSet.getName());
        cell.setCellStyle(headerCellStyle);
        
     // Create Cell Style for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MMM-yyyy"));
        headerRow = sheet.createRow(rowCount++);
        cell = headerRow.createCell(0);
        cell.setCellValue("Dataset Description:");
        cell.setCellStyle(dateCellStyle);
        cell = headerRow.createCell(1);
        cell.setCellValue(dataSet.getDescription());
        cell.setCellStyle(dateCellStyle);
        
        headerRow = sheet.createRow(rowCount++);
        cell = headerRow.createCell(0);
        cell.setCellValue("Item Status:");
        cell.setCellStyle(dateCellStyle);
        cell = headerRow.createCell(1);
        cell.setCellValue(dataSet.getItemStatus());
        cell.setCellStyle(dateCellStyle);
        
        headerRow = sheet.createRow(rowCount++);
        cell = headerRow.createCell(0);
        cell.setCellValue("Study Name:");
        cell.setCellStyle(dateCellStyle);
        cell = headerRow.createCell(1);
        cell.setCellValue(dataSet.getStudy().getStudyNo());
        cell.setCellStyle(dateCellStyle);
        
        headerRow = sheet.createRow(rowCount++);
        cell = headerRow.createCell(0);
        cell.setCellValue("Protocol ID:");
        cell.setCellStyle(dateCellStyle);
        cell = headerRow.createCell(1);
        cell.setCellValue(dataSet.getStudy().getProtocalNo());
        cell.setCellStyle(dateCellStyle);
        
        headerRow = sheet.createRow(rowCount++);
        cell = headerRow.createCell(0);
        cell.setCellValue("Date:");
        cell.setCellStyle(dateCellStyle);
        cell = headerRow.createCell(1);
        cell.setCellValue(new Date());
        cell.setCellStyle(dateCellStyle);
        
        headerRow = sheet.createRow(rowCount++);
        cell = headerRow.createCell(0);
        cell.setCellValue("Meta Data Version ODM ID:");
        cell.setCellStyle(dateCellStyle);
        cell = headerRow.createCell(1);
        cell.setCellValue(dataSet.getMetaDataVersion());
        cell.setCellStyle(dateCellStyle);
        
        headerRow = sheet.createRow(rowCount++);
        cell = headerRow.createCell(0);
        cell.setCellValue("Meta Data Version Name:");
        cell.setCellStyle(dateCellStyle);
        cell = headerRow.createCell(1);
        cell.setCellValue(dataSet.getMetaDataVersionName());
        cell.setCellStyle(dateCellStyle);

        headerRow = sheet.createRow(rowCount++);
        cell = headerRow.createCell(0);
        cell.setCellValue("Previous Study ODM ID:");
        cell.setCellStyle(dateCellStyle);
        cell = headerRow.createCell(1);
        cell.setCellValue(dataSet.getStudyODMIdName());
        cell.setCellStyle(dateCellStyle);
        
        headerRow = sheet.createRow(rowCount++);
        cell = headerRow.createCell(0);
        cell.setCellValue("Previous MetaDataVersion ODM ID:");
        cell.setCellStyle(dateCellStyle);
        cell = headerRow.createCell(1);
        cell.setCellValue(dataSet.getPreviousMetaDataVersionName());
        cell.setCellStyle(dateCellStyle);
        		
        headerRow = sheet.createRow(rowCount++);
        cell = headerRow.createCell(0);
        cell.setCellValue("Subjects:");
        cell.setCellStyle(dateCellStyle);
        cell = headerRow.createCell(1);
        cell.setCellValue(dataSet.getStudy().getSubjects()+"");
        cell.setCellStyle(dateCellStyle);

        headerRow = sheet.createRow(rowCount++);
        cell = headerRow.createCell(0);
        cell.setCellValue("Study Event Definitions");
        cell.setCellStyle(dateCellStyle);
        cell = headerRow.createCell(1);
        cell.setCellValue(dataSet.getPhases().size()+"");
        cell.setCellStyle(dateCellStyle);
        
        List<DataSetPhase> phases = dataSet.getPhases();
        for(int i = 1; i <= phases.size(); i++) {
        	DataSetPhase phase = phases.get(i-1);
            headerRow = sheet.createRow(rowCount++);
            cell = headerRow.createCell(0);
            cell.setCellValue("Study Event Definitions : " + i);
            cell.setCellStyle(dateCellStyle);
            cell = headerRow.createCell(1);
            cell.setCellValue(phase.getPhase().getName());
            cell.setCellStyle(dateCellStyle);
            cell = headerRow.createCell(3);
            cell.setCellValue("E"+i);
            cell.setCellStyle(dateCellStyle);
            
            List<DataSetPhasewiseCrfs> crfs = phase.getPhases();
            for(int j=0; j<crfs.size(); j++) {
            	DataSetPhasewiseCrfs crf = crfs.get(j);
                headerRow = sheet.createRow(rowCount++);
                cell = headerRow.createCell(0);
                cell.setCellValue(crf.getCrf().getName());
                cell.setCellStyle(dateCellStyle);
                cell = headerRow.createCell(1);
                cell.setCellValue(crf.getCrf().getTitle());
                cell.setCellStyle(dateCellStyle);
                cell = headerRow.createCell(3);
                cell.setCellValue("C"+(j+1));
                cell.setCellStyle(dateCellStyle);
            }
        }
        headerRow = sheet.createRow(rowCount++);
        cell = headerRow.createCell(0);
        cell.setCellValue("");
        cell.setCellStyle(dateCellStyle);
        
        
        headerRow = sheet.createRow(rowCount++);
        cell = headerRow.createCell(0);
        cell.setCellValue("Study Subject ID");
        cell.setCellStyle(dateCellStyle);
        cell = headerRow.createCell(1);
        cell.setCellValue("Protocol ID");
        cell.setCellStyle(dateCellStyle);
        
        int cols = 2;
        for(DataSetPhase dsp: phases) {
        	 List<DataSetPhasewiseCrfs> crfs = dsp.getPhases();
        	 for(DataSetPhasewiseCrfs crf: crfs) {
        		 if(crf.getSecEleIds() != null) {
        			 String[] scids = crf.getSecEleIds().split(",");
        			 for(String scid : scids) {
        				 if(!scid.trim().equals("")) {
	        				 CrfSectionElement ele = crfSer.studyCrfSectionElement(Long.parseLong(scid));
	        				 cell = headerRow.createCell(cols++);
	        		         cell.setCellValue(ele.getName());
	        		         cell.setCellStyle(dateCellStyle);
        				 }
        			 }
        		 }
        		 if(crf.getGroupEleIds() != null) {
        			 try {
	        			 String[] scids = crf.getGroupEleIds().split(",");
	        			 for(String scid : scids) {
	        				 if(!scid.trim().equals("")) {
		        				 CrfGroupElement ele = crfSer.studyCrfGroupElement(Long.parseLong(scid));
		        				 for(int row = 1; row <= ele.getGroup().getMaxRows(); row++) {
		        					 cell = headerRow.createCell(cols++);
		            		         cell.setCellValue(ele.getName()+"_"+row);
		            		         cell.setCellStyle(dateCellStyle);
		        				 }
	        				 }
	        			 }
	        		 }catch (Exception e) {
							// TODO: handle exception
	 					 e.printStackTrace();
	 					 System.out.println("---------------------------------"+crf.getCrf().getName());
					}
 				 
        		 }
        	 }
        }
        int noOfcols = cols+1;
        
        
        Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm =  studyService.findByStudyId(activeStudyId);
		
//		if(sm.isVolConfiguation()) {
			List<Volunteer> volList = studyService.studyVolunteerList(sm);
			for(Volunteer v : volList) {
				headerRow = sheet.createRow(rowCount++);
	            cell = headerRow.createCell(0);
	            cell.setCellValue(v.getVolId());
	            cell.setCellStyle(dateCellStyle);
	            cell = headerRow.createCell(1);
	            cell.setCellValue(sm.getProtocalNo());
	            cell.setCellStyle(dateCellStyle);
	            cols = 2;
	            for(DataSetPhase dsp: phases) {
	            	List<DataSetPhasewiseCrfs> crfs = dsp.getPhases();
	            	 for(DataSetPhasewiseCrfs crf: crfs) {
	            		 VolunteerPeriodCrf vcp = crfSer.volunteerPeriodCrf(crf, dsp.getPhase(), v);
	            		 if(crf.getSecEleIds() != null) {
	            			 String[] scids = crf.getSecEleIds().split(",");
	            			 for(String scid : scids) {
	            				 if(!scid.trim().equals("")) {
		            				 CrfSectionElement ele = crfSer.studyCrfSectionElement(Long.parseLong(scid));
		            				 cell = headerRow.createCell(cols++);
		            				 CrfSectionElementData data = crfSer.studyCrfSectionElementData(vcp, ele);
		            				 if(data != null) {
		            					 String s = data.getValue().replaceAll("<br/>", "");
		            					 if(ele.getType().equals("checkBox")) {
		            						 cell.setCellValue(s.replaceAll("####", ","));
		            					 }else
		            						 cell.setCellValue(s);
		            				 }else
		            					 cell.setCellValue(" ");
		            				 cell.setCellStyle(dateCellStyle);
	            				 }
	            			 }
	            		 }
	            		 if(crf.getGroupEleIds() != null) {
	            			 try {
		            			 String[] scids = crf.getGroupEleIds().split(",");
		            			 
		            			 for(String scid : scids) {
		            				 if(!scid.trim().equals("")) {
			            				 CrfGroupElement ele = crfSer.studyCrfGroupElement(Long.parseLong(scid));
			            				 for(int row = 1; row <= ele.getGroup().getMaxRows(); row++) {
		    	            				 cell = headerRow.createCell(cols++);
		    	            				 CrfGroupElementData data = 
		    	            						 crfSer.studyCrfSectionElementData(vcp, ele, "g_"+ele.getId()+"_"+ele.getName()+"_"+row);
		    	            				 if(data != null){
		    	            					 String s = data.getValue().replaceAll("<br/>", "");
		    	            					 if(ele.getType().equals("checkBox")) {
		    	            						 cell.setCellValue(s.replaceAll("####", ","));
		    	            					 }else
		    	            						 cell.setCellValue(s);
		    	            				 }else
		    	            					 cell.setCellValue(" ");
		    	            		         cell.setCellStyle(dateCellStyle);
			            				 }
		            				 }
		            			 }
	            		 	}catch (Exception e) {
								// TODO: handle exception
	            		 		e.printStackTrace();
							}
	            		 }
	            	 }
	            }
			}
//        }
        
        
        // Resize all columns to fit the content size
        for(int i = 0; i < cols; i++) {
            sheet.autoSizeColumn(i);
        }
        
        
        String path=request.getSession().getServletContext().getRealPath("/") +dataSet.getName()+"_" + dateinSting;
        System.out.println(path );
        
        File file = new File(path+".xlsx");
        
        if (!file.exists()) {
            file.createNewFile();
        }else {
        	file.delete();
        	file.createNewFile();
        }
       
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(path+".xlsx");
        workbook.write(fileOut);
        fileOut.close();
        // Closing the workbook
        workbook.close();
        
        
        return file;
	}
	
	
}
