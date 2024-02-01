package com.covide.crf.pdf.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.covide.crf.dao.CrfDAO;
import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfGroup;
import com.covide.crf.dto.CrfGroupDataRows;
import com.covide.crf.dto.CrfGroupElement;
import com.covide.crf.dto.CrfGroupElementData;
import com.covide.crf.dto.CrfSection;
import com.covide.crf.dto.CrfSectionElement;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.service.CrfPdfService;

public class GenerateSiglePdfAction {
    
	public String genrerateSinglePdf(HttpServletResponse response, HttpServletRequest request, Crf crf,
			VolunteerPeriodCrf vpc, CrfPdfService crfPdfService, CrfDAO crfDAO)throws DocumentException, IOException  {
		@SuppressWarnings("deprecation")
	    String unchkrdPath = request.getServletContext().getRealPath("/static/images/radioUncheck.png");
		String chkrdPath = request.getServletContext().getRealPath("/static/images/radiochecked.png");
		String unchkcbPath = request.getServletContext().getRealPath("/static/images/uncheckedCB.png");
		String chkcbPath =request.getServletContext().getRealPath("/static/images/checkedCB.png");
		String img = request.getServletContext().getRealPath("/static/images/SpiniosLogo.png");
		response.setContentType("application/pdf");
		//OutputStream out = response.getOutputStream();
		String realPath = request.getServletContext().getRealPath("/");
		String path = realPath +"/PdfFiles/SinglePdf/";
		File directory = new File(path);
		if(!directory.exists()) {
			directory.mkdirs();
		}
		String fileName = path+vpc.getId()+"_"+vpc.getStdCrf().getCrfName()+".pdf";
		Document document = new Document();
	    document.setPageSize(PageSize.A4);
		// PdfWriter.getInstance(document, out);
		 PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
		 RedBorder event1 = new RedBorder(img);
         Rectangle rec = document.getPageSize();
         writer.setPageEvent(event1);
         document.open();
         Font bold = new Font(FontFamily.TIMES_ROMAN, 14, Font.BOLD);
         Font regular = new Font(FontFamily.TIMES_ROMAN, 12);
         Font heading = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD);
         PdfPCell cell = null;
         //my_table.setWidthPercentage(100);
         
         PdfPTable mytable = new PdfPTable(3);
         mytable.setWidthPercentage(100);
         Phrase p = new Phrase("", regular);
         cell = new PdfPCell(p);
         cell.setFixedHeight(30f);
         cell.setColspan(3);
         cell.setBorder(Rectangle.NO_BORDER);
         mytable.addCell(cell);
         document.add(mytable);
 		
 	     List<CrfSection> secList = crf.getSections();
	     for(CrfSection sec : secList) {
	    	 if(!sec.getHedding().equals("")) {
	    		 PdfPTable hstable = new PdfPTable(1);
	    		 hstable.setWidthPercentage(95);
	    		 String headStr = sec.getHedding();
	    		 if(headStr.contains("&nbsp;")) {
	    			 headStr = headStr.replaceAll("&nbsp;", " ");
	    			 if(headStr.contains("</br>")) {
	    				 headStr.replaceAll("</br>", "\n");
	    			 }
	    		 }
	    		
	    		 cell = new PdfPCell(new Phrase(headStr, heading));
	    		 cell.setBorder(Rectangle.NO_BORDER);
	    		 hstable.addCell(cell);
	    		 document.add(hstable);
	    		 
	    	 }
	    	 if(!sec.getSubHedding().equals("")) {
	    		 PdfPTable hstable = new PdfPTable(1);
	    		 hstable.setWidthPercentage(95);
	    		 String subHedStr = sec.getSubHedding();
	    		 if(subHedStr.contains("&nbsp;")) {
	    			 subHedStr = subHedStr.replaceAll("&nbsp;", " ");
	    			 if(subHedStr.contains("</br>")) {
	    				 subHedStr.replaceAll("</br>", "\n");
	    			 }
	    		 }
	    		 cell = new PdfPCell(new Phrase(subHedStr, heading));
	    		 cell.setBorder(Rectangle.NO_BORDER);
	    		 hstable.addCell(cell);
	    		 document.add(hstable);
	    	 }
	    	 if(sec.getGroup() == null || sec.getGroup().equals("")) {
	    		 //section elements
	    			List<CrfSectionElement> secEleList = sec.getElement();
	    			Map<Integer, List<Map<Integer, CrfSectionElement>>> map = new HashMap<>();
	    	     	List<Map<Integer, CrfSectionElement>> tempListMap = null;
	    	     	Map<Integer, CrfSectionElement> tempMap = null;
	    			for(CrfSectionElement secele : secEleList) {
	    			    int rowNo = secele.getRowNo();
	    			    int columnNo = secele.getColumnNo();
	    			    if(map.get(rowNo) == null) {
	    			    	tempListMap = new ArrayList<>();
	    			    	tempMap = new HashMap<>();
	    			    	tempMap.put(columnNo, secele);
	    			    	tempListMap.add(tempMap);
	    			    	map.put(rowNo, tempListMap);
	    			    	
	    			    }else {
	    			    	List<Map<Integer, CrfSectionElement>> dataMap = map.get(rowNo);
	    			    	if(dataMap.size() != 0) {
	    			    		tempMap = new HashMap<>();
	    			    		tempMap.put(columnNo, secele);
	    			    		dataMap.add(tempMap);
	    			    		
	    			    	}
	    			    	 
	    			    }
	    			}
	    			for(int k=1; k<=map.size(); k++) {
	    					List<Map<Integer, CrfSectionElement>> mapList = map.get(k);
		    				PdfPTable totaldec = new PdfPTable(mapList.size());
	    					totaldec.setWidthPercentage(95);
	    					
		    				for(int i=0; i<mapList.size(); i++) {
		    					//Space purpose 
		    					PdfPTable table = new PdfPTable(3);
		    					table.setWidthPercentage(95);
		    					cell = new PdfPCell(new Phrase(""));
		    					cell.setBorder(Rectangle.NO_BORDER);
		    					cell.setColspan(3);
		    					cell.setFixedHeight(3f);
		    					table.addCell(cell);
		    					document.add(table);
		    					
		    					Map<Integer, CrfSectionElement> dataMap = mapList.get(i);
		    					CrfSectionElement scse = dataMap.get(i+1);
		    				    if(scse != null) {
		    				    	if(!scse.getTopDesc().equals("")) {
		    							 PdfPTable topdesc = new PdfPTable(1);
		    							// topdesc.setWidthPercentage(95);
		    							 Phrase top = new Phrase(scse.getTopDesc(), regular);
		    							 cell = new PdfPCell(top);
		    							 cell.setBorder(Rectangle.NO_BORDER);
		    							 cell.setNoWrap(false);
		    							 topdesc.addCell(cell);
		    							 document.add(topdesc);
		    						}
		    				        if(scse.getType().equals("radio")) {
		    				        	//Space purpose 
				    					PdfPTable space = new PdfPTable(3);
				    					space.setWidthPercentage(95);
				    					cell = new PdfPCell(new Phrase(""));
				    					cell.setBorder(Rectangle.NO_BORDER);
				    					cell.setColspan(3);
				    					cell.setFixedHeight(4f);
				    					space.addCell(cell);
				    					document.add(space);
				    					
		    				    		 if(!scse.getLeftDesc().equals("") && scse.getRigtDesc().equals("")) {
					    					
		    				    			String lstr = scse.getLeftDesc();
		    				    			if(lstr.contains("&nbsp;")) {
		    				    				lstr = lstr.replace("&nbsp;", " ");
		    				    			}
		    				    			 Phrase ldesc = new Phrase(lstr, regular);
					    					 String responeVal = scse.getResponceType();
					    					 String temp[] = responeVal.split("##");
					    					 PdfPTable desc = new PdfPTable(5);
					    					 desc.setWidthPercentage(95);
					    					 Phrase data = null;
					    					 String value ="";
					    					 if(vpc != null) {
					    						 value = crfDAO.studyCrfSectionElementData(vpc, scse);
					    						 if(value == null)
					    							 value ="";
					    						 if(!value.equals("")) {
					    							 if(value.contains("<br/>")) {
						    							 value = value.replaceAll("<br/>", "");
						    						}
				    						 		if(value.contains("</br>")) {
						    							 value = value.replaceAll("</br>", "");
						    						} 
				    						 		data = new Phrase(value, regular);
					    						 }else {
					    							 data = new Phrase(value, regular);
					    						 }
			    						 	 }else data = new Phrase(value, regular);
					    					 
					    					 //left heading
					    					 cell = new PdfPCell(ldesc);
					    					 cell.setNoWrap(false);
					    					 cell.setBorder(Rectangle.NO_BORDER);
					    					 desc.addCell(cell);
					    					
				    						 for(String s : temp) {
				    							 if(s.contains("<br/>") || s.contains("</br>")) {
				    								 if(s.contains("</br>"))
				    								    s = s.replaceAll("</br>", "");
				    								 if(s.contains("<br/>"))
				    								    s = s.replaceAll("<br/>", "");
				    							 }
				    							 if(!value.equals("") && value.equals(s.trim())) {
						    						 Image radioimg = Image.getInstance(chkrdPath);
						    						 //radioimg.setAbsolutePosition(10, 10); 
						    				          radioimg.scaleAbsolute(12, 12);
						    						 Phrase label = new Phrase(s, regular);
						    						 
						    						 cell = new PdfPCell(radioimg);
						    						 cell.setNoWrap(false);
						    						 cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						    						 cell.setBorder(Rectangle.NO_BORDER);
						    						 desc.addCell(cell);
						    						 
						    						 cell = new PdfPCell(label);
						    						 cell.setNoWrap(false);
						    						 cell.setBorder(Rectangle.NO_BORDER);
						    						 desc.addCell(cell);
						    						 //desc.addCell(imgtab);
				    							 }else {
				    								 Image radioimg = Image.getInstance(unchkrdPath);
						    						// radioimg.setAbsolutePosition(10, 10); 
				    								 radioimg.scaleAbsolute(20, 20);
						    						 Phrase label = new Phrase(s, regular);
						    						 
						    						 cell = new PdfPCell(radioimg);
						    						 cell.setNoWrap(false);
						    						 cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						    						 cell.setBorder(Rectangle.NO_BORDER);
						    						 desc.addCell(cell);
						    						 
						    						 cell = new PdfPCell(label);
						    						 cell.setNoWrap(false);
						    						 cell.setBorder(Rectangle.NO_BORDER);
						    						 desc.addCell(cell);
						    					 }
				    							
				    							 
					    					 }
				    						 cell = new PdfPCell(desc);
					    					 cell.setBorder(Rectangle.NO_BORDER);
					    					 totaldec.addCell(cell);
					    					 document.add(totaldec);
					    				 }else if(scse.getLeftDesc().equals("") && !scse.getRigtDesc().equals("")) {
						    					String rstr = scse.getRigtDesc();
			    				    			if(rstr.contains("&nbsp;")) {
			    				    				rstr = rstr.replaceAll("&nbsp;", " ");
			    				    			}
			    				    			
			    				    			 Phrase rdesc = new Phrase(rstr, regular);
						    					 String responeVal = scse.getResponceType();
						    					 String temp[] = responeVal.split("##");
						    					 PdfPTable desc = new PdfPTable(5);
						    					// desc.setWidthPercentage(80);
						    					 Phrase data = null;
						    					 String value ="";
						    					 if(vpc != null) {
						    						 value = crfDAO.studyCrfSectionElementData(vpc, scse);
						    						 if(value == null)
						    							 value ="";
						    						 if(!value.equals("")) {
						    							 if(value.contains("<br/>")) {
							    							 value = value.replaceAll("<br/>", "");
							    						}
					    						 		if(value.contains("</br>")) {
							    							 value = value.replaceAll("</br>", "");
							    						} 
					    						 		data = new Phrase(value, regular);
						    						 }else {
						    							 data = new Phrase(value, regular);
						    						 }
				    						 	 }else data = new Phrase(value, regular);
						    					 
						    					 //left heading
						    					 cell = new PdfPCell(rdesc);
						    					 cell.setNoWrap(false);
						    					 cell.setBorder(Rectangle.NO_BORDER);
						    					 desc.addCell(cell);
						    					
					    						 for(String s : temp) {
					    							 if(s.contains("<br/>") || s.contains("</br>")) {
					    								 if(s.contains("</br>"))
					    								    s = s.replaceAll("</br>", "");
					    								 if(s.contains("<br/>"))
					    								    s = s.replaceAll("<br/>", "");
					    							 }
					    							 if(!value.equals("") && value.equals(s)) {
							    						 Image radioimg = Image.getInstance(chkrdPath);
							    					     radioimg.scaleAbsolute(12, 12);
							    						 Phrase label = new Phrase(s, regular);
							    						 
							    						 cell = new PdfPCell(radioimg);
							    						 cell.setNoWrap(false);
							    						 cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							    						 cell.setBorder(Rectangle.NO_BORDER);
							    						 desc.addCell(cell);
							    						 
							    						 cell = new PdfPCell(label);
							    						 cell.setNoWrap(false);
							    						 cell.setBorder(Rectangle.NO_BORDER);
							    						 desc.addCell(cell);
							    					 }else {
					    								 Image radioimg = Image.getInstance(unchkrdPath);
					    								 radioimg.scaleAbsolute(20, 20);
							    						 Phrase label = new Phrase(s, regular);
							    						 
							    						 cell = new PdfPCell(radioimg);
							    						 cell.setNoWrap(false);
							    						 cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							    						 cell.setBorder(Rectangle.NO_BORDER);
							    						 desc.addCell(cell);
							    						 
							    						 cell = new PdfPCell(label);
							    						 cell.setNoWrap(false);
							    						 cell.setBorder(Rectangle.NO_BORDER);
							    						 desc.addCell(cell);
							    					 }
					    							
					    							 
						    					 }
					    						 cell = new PdfPCell(desc);
						    					 cell.setBorder(Rectangle.NO_BORDER);
						    					 totaldec.addCell(cell);
						    					 document.add(totaldec);
					    					 
		    				             }else if(scse.getLeftDesc().equals("") && scse.getRigtDesc().equals("")) {
						    					 String responeVal = scse.getResponceType();
						    					 String temp[] = responeVal.split("##");
						    					 PdfPTable desc = new PdfPTable(4);
						    					 Phrase data = null;
						    					 String value ="";
						    					 if(vpc != null) {
						    						 	value = crfDAO.studyCrfSectionElementData(vpc, scse);
						    						 if(value == null)
						    							 value ="";
						    						 if(!value.equals("")) {
						    							 if(value.contains("<br/>")) {
							    							 value = value.replaceAll("<br/>", "");
							    						}
					    						 		if(value.contains("</br>")) {
							    							 value = value.replaceAll("</br>", "");
							    						} 
					    						 		data = new Phrase(value, regular);
						    						 }else {
						    							 data = new Phrase(value, regular);
						    						 }
						    						 
				    						 	 }else {
				    						 		data = new Phrase(value, regular);
				    						 	 }
						    					 for(String s : temp) {
						    						 if(s.contains("<br/>") || s.contains("</br>")) {
					    								 if(s.contains("</br>"))
					    								    s = s.replaceAll("</br>", "");
					    								 if(s.contains("<br/>"))
					    								    s = s.replaceAll("<br/>", "");
					    							 }
						    						 
						    						 if(!value.equals("") && value.equals(s.trim())) {
							    						 Image radioimg = Image.getInstance(chkrdPath);
							    					     radioimg.scaleAbsolute(12, 12);
							    						 Phrase label = new Phrase(s, regular);
							    						 
							    						 cell = new PdfPCell(radioimg);
							    						 cell.setNoWrap(false);
							    						 cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							    						 cell.setBorder(Rectangle.NO_BORDER);
							    						 desc.addCell(cell);
							    						 
							    						 cell = new PdfPCell(label);
							    						 cell.setNoWrap(false);
							    						 cell.setBorder(Rectangle.NO_BORDER);
							    						 desc.addCell(cell);
							    					 }else {
					    								 Image radioimg = Image.getInstance(unchkrdPath);
							    						 radioimg.scaleAbsolute(20, 20);
							    						 Phrase label = new Phrase(s, regular);
							    						 
							    						 cell = new PdfPCell(radioimg);
							    						 cell.setNoWrap(false);
							    						 cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							    						 cell.setBorder(Rectangle.NO_BORDER);
							    						 desc.addCell(cell);
							    						 
							    						 cell = new PdfPCell(label);
							    						 cell.setNoWrap(false);
							    						 cell.setBorder(Rectangle.NO_BORDER);
							    						 desc.addCell(cell);
							    					 }
					    							
					    							 
						    					 }
					    						 cell = new PdfPCell(desc);
						    					 cell.setBorder(Rectangle.NO_BORDER);
						    					 totaldec.addCell(cell);
						    					 document.add(totaldec);
		    				            	 
		    				             }
		    				    		//Space purpose 
					    					PdfPTable space2 = new PdfPTable(3);
					    					space2.setWidthPercentage(95);
					    					cell = new PdfPCell(new Phrase(""));
					    					cell.setBorder(Rectangle.NO_BORDER);
					    					cell.setColspan(3);
					    					cell.setFixedHeight(5f);
					    					space2.addCell(cell);
					    					document.add(space2);
		    				    	}else if(scse.getType().equals("checkBox")) {
		    				    		 //Space after data
		    			    		     PdfPTable space = new PdfPTable(3);
		    			    		     space.setWidthPercentage(95);
		    			    		     Phrase empty2 = new Phrase("");
		    			    		     cell = new PdfPCell(empty2);
		    			    		     cell.setBorder(Rectangle.NO_BORDER);
		    			    		     cell.setFixedHeight(3f);
		    			    		     cell.setColspan(3);
		    			    		     space.addCell(cell);
		    			    		     document.add(space);
		    			    		     
		    				    		 if(!scse.getLeftDesc().equals("") && scse.getRigtDesc().equals("")) {
		    				    			 
					    					
		    				    			 String lstr = scse.getRigtDesc();
		    				    			if(lstr.contains("&nbsp;")) {
		    				    				lstr = lstr.replaceAll("&nbsp;", " ");
		    				    			}
		    				    			 Phrase ldesc = new Phrase(lstr, regular);
					    					 String responeVal = scse.getResponceType();
					    					 PdfPTable desc = new PdfPTable(3);
					    					// desc.setWidthPercentage(80);
					    					 Phrase data = null;
					    					 String value ="";
					    					 if(vpc != null) {
					    						  value = crfDAO.studyCrfSectionElementData(vpc, scse);
					    						  if(value == null)
					    							  value ="";
					    						  data = new Phrase(value, regular);
			    						 	 }else data = new Phrase(value, regular);
					    					 
					    					 //left heading
					    					 cell = new PdfPCell(ldesc);
					    					 cell.setNoWrap(false);
					    					 cell.setBorder(Rectangle.NO_BORDER);
					    					 desc.addCell(cell);
					    					 
				    					 	 if(!value.equals("") && value.equals(responeVal)) {
					    						 Image cbimg = Image.getInstance(chkcbPath);
					    						 //radioimg.setAbsolutePosition(10, 10); 
					    				          cbimg.scaleAbsolute(15, 15);
					    						 Phrase label = new Phrase(responeVal+cbimg, regular);
					    						 
					    						 cell = new PdfPCell(cbimg);
					    						 cell.setNoWrap(false);
					    						 cell.setBorder(Rectangle.NO_BORDER);
					    						 desc.addCell(cell);
					    						 
					    						 cell = new PdfPCell(label);
					    						 cell.setNoWrap(false);
					    						 cell.setBorder(Rectangle.NO_BORDER);
					    						 desc.addCell(cell);
					    						 //desc.addCell(imgtab);
			    							 }else {
			    								 Image cbimg = Image.getInstance(unchkcbPath);
					    						// radioimg.setAbsolutePosition(10, 10); 
			    								 cbimg.scaleAbsolute(15, 15);
					    						 Phrase label = new Phrase(responeVal+cbimg, regular);
					    						 
					    						 cell = new PdfPCell(cbimg);
					    						 cell.setNoWrap(false);
					    						 cell.setBorder(Rectangle.NO_BORDER);
					    						 desc.addCell(cell);
					    						 
					    						 cell = new PdfPCell(label);
					    						 cell.setNoWrap(false);
					    						 cell.setBorder(Rectangle.NO_BORDER);
					    						 desc.addCell(cell);
					    					 }
				    							
				    							 
					    					 cell = new PdfPCell(desc);
					    					 cell.setBorder(Rectangle.NO_BORDER);
					    					 totaldec.addCell(cell);
					    					 document.add(totaldec);
					    				 }else if(scse.getLeftDesc().equals("") && !scse.getRigtDesc().equals("")) {
					    				    String rstr = scse.getRigtDesc();
		    				    			if(rstr.contains("&nbsp;")) {
		    				    				rstr = rstr.replaceAll("&nbsp;", " ");
		    				    			}
		    				    			 Phrase rdesc = new Phrase(rstr, regular);
					    					 String responeVal = scse.getResponceType();
					    					 PdfPTable desc = new PdfPTable(3);
					    					// desc.setWidthPercentage(80);
					    					 Phrase data = null;
					    					 String value ="";
					    					 if(vpc != null) {
					    						 value = crfDAO.studyCrfSectionElementData(vpc, scse);
					    						 if(value == null)
					    							 value ="";
					    						 data = new Phrase(value, regular);
			    						 	 }else data = new Phrase(value, regular);
					    					 
					    					 //left heading
					    					 cell = new PdfPCell(rdesc);
					    					 cell.setNoWrap(false);
					    					 cell.setBorder(Rectangle.NO_BORDER);
					    					 desc.addCell(cell);
					    					 
				    					 	 if(!value.equals("") && value.equals(responeVal)) {
					    						 Image cbimg = Image.getInstance(chkcbPath);
					    						 //radioimg.setAbsolutePosition(10, 10); 
					    				          cbimg.scaleAbsolute(15, 15);
					    						 Phrase label = new Phrase(responeVal, regular);
					    						 
					    						 cell = new PdfPCell(cbimg);
					    						 cell.setNoWrap(false);
					    						 cell.setBorder(Rectangle.NO_BORDER);
					    						 desc.addCell(cell);
					    						 
					    						 cell = new PdfPCell(label);
					    						 cell.setNoWrap(false);
					    						 cell.setBorder(Rectangle.NO_BORDER);
					    						 desc.addCell(cell);
					    						 //desc.addCell(imgtab);
			    							 }else {
			    								 Image cbimg = Image.getInstance(unchkcbPath);
					    						// radioimg.setAbsolutePosition(10, 10); 
			    								 cbimg.scaleAbsolute(15, 15);
					    						 Phrase label = new Phrase(responeVal, regular);
					    						 
					    						 cell = new PdfPCell(cbimg);
					    						 cell.setNoWrap(false);
					    						 cell.setBorder(Rectangle.NO_BORDER);
					    						 desc.addCell(cell);
					    						 
					    						 cell = new PdfPCell(label);
					    						 cell.setNoWrap(false);
					    						 cell.setBorder(Rectangle.NO_BORDER);
					    						 desc.addCell(cell);
					    					 }
				    							
				    							 
					    					 cell = new PdfPCell(desc);
					    					 cell.setBorder(Rectangle.NO_BORDER);
					    					 totaldec.addCell(cell);
					    					 document.add(totaldec);
					    				 }else {
					    					 String responeVal = scse.getResponceType();
					    					 PdfPTable desc = new PdfPTable(2);
					    					// desc.setWidthPercentage(80);
					    					 Phrase data = null;
					    					 String value ="";
					    					 if(vpc != null) {
					    						 value = crfDAO.studyCrfSectionElementData(vpc, scse);
					    						 if(value == null)
					    							 value = "";
					    						 data = new Phrase(value, regular);
			    						 	 }else data = new Phrase(value, regular);
					    					 
					    					 if(!value.equals("") && value.equals(responeVal)) {
					    						 Image cbimg = Image.getInstance(chkcbPath);
					    						 //radioimg.setAbsolutePosition(10, 10); 
					    				          cbimg.scaleAbsolute(15, 15);
					    				          
					    						 Phrase label = new Phrase(responeVal, regular);
					    						 
					    						 cell = new PdfPCell(cbimg);
					    						 cell.setNoWrap(false);
					    						 cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					    						 cell.setBorder(Rectangle.NO_BORDER);
					    						 desc.addCell(cell);
					    						 
					    						 cell = new PdfPCell(label);
					    						 cell.setNoWrap(false);
					    						 cell.setBorder(Rectangle.NO_BORDER);
					    						 desc.addCell(cell);
					    						 
					    						 
					    						 //desc.addCell(imgtab);
			    							 }else {
			    								 Image cbimg = Image.getInstance(unchkcbPath);
					    						// radioimg.setAbsolutePosition(10, 10); 
			    								 cbimg.scaleAbsolute(15, 15);
					    						 Phrase label = new Phrase(responeVal, regular);
					    						 
					    						 cell = new PdfPCell(cbimg);
					    						 cell.setNoWrap(false);
					    						 cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					    						 cell.setBorder(Rectangle.NO_BORDER);
					    						 desc.addCell(cell);
					    						 
					    						 cell = new PdfPCell(label);
					    						 cell.setNoWrap(false);
					    						 cell.setBorder(Rectangle.NO_BORDER);
					    						 desc.addCell(cell);
					    						 
					    					 }
				    							
				    							 
					    					 cell = new PdfPCell(desc);
					    					 cell.setBorder(Rectangle.NO_BORDER);
					    					 totaldec.addCell(cell);
					    					 document.add(totaldec); 
					    					 
					    					 //Space after data
							    		     PdfPTable space2 = new PdfPTable(3);
							    		     space2.setWidthPercentage(95);
							    		     Phrase empty3 = new Phrase("");
							    		     cell = new PdfPCell(empty3);
							    		     cell.setBorder(Rectangle.NO_BORDER);
							    		     cell.setFixedHeight(3f);
							    		     cell.setColspan(3);
							    		     space2.addCell(cell);
							    		     document.add(space2);
					    				 }
		    				    	}else if(scse.getType().equals("checkBoxLabel") || scse.getType().equals("radioLabel")){
		    				    		if(!scse.getLeftDesc().equals("") && !scse.getRigtDesc().equals("")) {
		    				    			PdfPTable radtab  = null;
		    				    			String leftdesc = scse.getLeftDesc();
		    				    			String rightDesc = scse.getRigtDesc();
		    				    			String[] lefTemp = null;
		    				    			String[] rightTemp = null;
		    				    		   if(scse.getType().equals("radioLabel")) {
		    				    			   if(leftdesc.contains("</br>")) leftdesc = leftdesc.replaceAll("</br>", "/n");
		    				    			   else if(leftdesc.contains("<br/>")) leftdesc = leftdesc.replaceAll("<br/>", "");
		    				    			   else if(leftdesc.contains("&nbsp;")) leftdesc = leftdesc.replaceAll("&nbsp;", "");
		    				    			   if(leftdesc.contains("/")) {
			    				    				lefTemp = leftdesc.split("/");
			    				    			}else if(leftdesc.contains("&nbsp;")) {
			    				    				lefTemp = leftdesc.split("&nbsp;");
			    				    			}
			    				    			if(rightDesc.contains("/")) {
			    				    				rightTemp = rightDesc.split("/");
			    				    			}else if(rightDesc.contains("&nbsp;")) {
			    				    				rightTemp = rightDesc.split("&nbsp;");
			    				    			}
		    				    		   }else {
		    				    			   if(leftdesc.contains("</br>")) {
		    				    				   leftdesc = leftdesc.replaceAll("</br>", "/n");
		    				    			   }else if(leftdesc.contains("<br/>")) {
		    				    				   leftdesc = leftdesc.replaceAll("<br/>", "");
		    				    			   }else if(leftdesc.contains("&nbsp;")) {
		    				    				   leftdesc = leftdesc.replaceAll("&nbsp;", "");
		    				    			   }
		    				    		   }
		    				    			
		    				    			
		    				    			if( (lefTemp != null && lefTemp.length !=0) || (rightTemp != null && rightTemp.length !=0)) {
		    				    					int count =1;
				    				    			if(lefTemp.length != 0) {
				    				    				int no=0;
				    				    				for(String st : lefTemp) {
				    				    					if(!st.equals("")) {
				    				    						count = no+1;
				    				    						no++;
				    				    					}
				    				    				}
				    				    			}
				    				    			radtab = new PdfPTable(count*2);
			    				    				for(String le : lefTemp) {
			    				    					if(!le.equals("")) {
			    				    						cell = new PdfPCell(new Phrase(""));
				    				    					cell.setBorder(Rectangle.NO_BORDER);
				    				    					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				    				    					cell.setNoWrap(false);
				    				    					radtab.addCell(cell);
				    				    					
				    				    					Phrase pr = new Phrase(le, regular);
				    				    					cell = new PdfPCell(pr);
				    				    					cell.setBorder(Rectangle.NO_BORDER);
				    				    					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				    				    					cell.setNoWrap(false);
				    				    					radtab.addCell(cell);
			    				    					}
			    				    					
			    				    				}
			    				    				
			    								if((rightTemp !=null && rightTemp.length != 0)){
	    				    							int no=0;
				    				    				for(String st : rightTemp) {
				    				    					if(!st.equals("")) {
				    				    						count = no+1;
				    				    						no++;
				    				    					}
				    				    				}
					    					   		radtab = new PdfPTable(count*2);
			    				    				for(String ri : rightTemp) {
			    				    					if(!ri.equals("")) {
			    				    						cell = new PdfPCell(new Phrase(""));
				    				    					cell.setBorder(Rectangle.NO_BORDER);
				    				    					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				    				    					cell.setNoWrap(false);
				    				    					radtab.addCell(cell);
				    				    					
				    				    					Phrase pr = new Phrase(ri, regular);
				    				    					cell = new PdfPCell(pr);
				    				    					cell.setBorder(Rectangle.NO_BORDER);
				    				    					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				    				    					cell.setNoWrap(false);
				    				    					radtab.addCell(cell);
			    				    					}
			    				    					
			    				    				}
		    				    				}
		    				    			}else {
		    				    				radtab = new PdfPTable(4);
		    				    				
		    				    				Phrase pr = new Phrase(leftdesc, regular);
	    				    					cell = new PdfPCell(pr);
	    				    					cell.setBorder(Rectangle.NO_BORDER);
	    				    					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    				    					cell.setNoWrap(false);
	    				    					radtab.addCell(cell);
	    				    					
	    				    					cell = new PdfPCell(new Phrase(""));
	    				    					cell.setBorder(Rectangle.NO_BORDER);
	    				    					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    				    					cell.setNoWrap(false);
	    				    					radtab.addCell(cell);
	    				    					
	    				    					Phrase pr1 = new Phrase(rightDesc, regular);
	    				    					cell = new PdfPCell(pr1);
	    				    					cell.setBorder(Rectangle.NO_BORDER);
	    				    					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    				    					cell.setNoWrap(false);
	    				    					radtab.addCell(cell);
	    				    					
	    				    					cell = new PdfPCell(new Phrase(""));
	    				    					cell.setBorder(Rectangle.NO_BORDER);
	    				    					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    				    					cell.setNoWrap(false);
	    				    					radtab.addCell(cell);
	    				    				}
		    				    			
		    				    			cell = new PdfPCell(radtab);
		    				    			cell.setBorder(Rectangle.NO_BORDER);
		    				    			cell.setNoWrap(false);
		    				    			totaldec.addCell(cell);
		    				    			document.add(totaldec);
		    				    			 
		    				    			
		    				    		}else if(!scse.getLeftDesc().equals("") && scse.getRigtDesc().equals("")){
		    				    			PdfPTable radtab  = null;
		    				    			String leftdesc = scse.getLeftDesc();
		    				    			String[] lefTemp = null;
		    				    			if(scse.getType().equals("radioLabel")) {
		    				    				if(leftdesc.contains("/")) {
			    				    				lefTemp = leftdesc.split("/");
			    				    			}else if(leftdesc.contains("&nbsp;")) {
			    				    				lefTemp = leftdesc.split("&nbsp;");
			    				    			}
		    				    			}else {
			    				    			   if(leftdesc.contains("</br>")) {
			    				    				   leftdesc = leftdesc.replaceAll("</br>", "/n");
			    				    			   }else if(leftdesc.contains("<br/>")) {
			    				    				   leftdesc = leftdesc.replaceAll("<br/>", "");
			    				    			   }else if(leftdesc.contains("&nbsp;")) {
			    				    				   leftdesc = leftdesc.replaceAll("&nbsp;", "");
			    				    			   }
				    				         }
		    				    			
		    				    			int count =1;
		    				    			if(lefTemp !=null && lefTemp.length != 0) {
		    				    				int no=0;
		    				    				for(String st : lefTemp) {
		    				    					if(!st.equals("")) {
		    				    						count = no+1;
		    				    						no++;
		    				    					}
		    				    				}
		    				    				radtab = new PdfPTable((count)*2);
		    				    				for(int f=0; f<lefTemp.length; f++) {
		    				    					if(!lefTemp[f].equals("")) {
		    				    						cell = new PdfPCell(new Phrase(""));
			    				    					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			    				    					cell.setBorder(Rectangle.NO_BORDER);
			    				    					cell.setNoWrap(false);
			    				    					radtab.addCell(cell);
			    				    					
		    				    						Phrase pr = new Phrase(lefTemp[f], regular);
			    				    					cell = new PdfPCell(pr);
			    				    					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			    				    					cell.setBorder(Rectangle.NO_BORDER);
			    				    					cell.setNoWrap(false);
			    				    					radtab.addCell(cell);
			    				    					
			    				    					
		    				    					}
		    				    					
		    				    				}
		    				    				
		    				    			}else {
		    				    				radtab = new PdfPTable(2);
		    				    				Phrase pr = new Phrase(leftdesc, regular);
	    				    					cell = new PdfPCell(pr);
	    				    					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				    					cell.setBorder(Rectangle.NO_BORDER);
	    				    					cell.setNoWrap(false);
	    				    					radtab.addCell(cell);
	    				    					
	    				    					cell = new PdfPCell(new Phrase(""));
	    				    					cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
	    				    					cell.setBorder(Rectangle.NO_BORDER);
	    				    					cell.setNoWrap(false);
	    				    					radtab.addCell(cell);
		    				    			}
		    				    			
		    				    			cell = new PdfPCell(radtab);
	    				    				cell.setBorder(Rectangle.NO_BORDER);
	    				    				cell.setNoWrap(false);
	    				    				totaldec.addCell(cell);
	    				    				document.add(totaldec);
		    				    		}else if(scse.getLeftDesc().equals("") && !scse.getRigtDesc().equals("")){
		    				    			PdfPTable radtab  = null;
		    				    			String rightDesc = scse.getRigtDesc();
		    				    			String[] rightTemp = null;
		    				    			if(scse.getType().equals("radioLabel")) {
		    				    				if(rightDesc.contains("/")) {
			    				    				rightTemp = rightDesc.split("/");
			    				    			}else if(rightDesc.contains("&nbsp;")) {
			    				    				rightTemp = rightDesc.split("&nbsp;");
			    				    			}
		    				    			}else {
			    				    			   if(rightDesc.contains("</br>")) {
			    				    				   rightDesc = rightDesc.replaceAll("</br>", "/n");
			    				    			   }else if(rightDesc.contains("<br/>")) {
			    				    				   rightDesc = rightDesc.replaceAll("<br/>", "");
			    				    			   }else if(rightDesc.contains("&nbsp;")) {
			    				    				   rightDesc = rightDesc.replaceAll("&nbsp;", "");
			    				    			   }
				    				         }
		    				    			
		    				    			int count =1;
		    				    			if((rightTemp != null &&rightTemp.length !=0)) {
		    				    				int no=0;
		    				    				for(String st : rightTemp) {
		    				    					if(!st.equals("")) {
		    				    						count = no+1;
		    				    						no++;
		    				    					}
		    				    				}
		    				    				radtab = new PdfPTable(count*2);
		    				    				for(String re : rightTemp) {
		    				    					if(!re.equals("")) {
		    				    						
		    				    						cell = new PdfPCell(new Phrase(""));
			    				    					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			    				    					cell.setBorder(Rectangle.NO_BORDER);
			    				    					cell.setNoWrap(false);
			    				    					radtab.addCell(cell);
			    				    					
		    				    						Phrase pr = new Phrase(re, regular);
			    				    					cell = new PdfPCell(pr);
			    				    					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			    				    					cell.setBorder(Rectangle.NO_BORDER);
			    				    					cell.setNoWrap(false);
			    				    					radtab.addCell(cell);
		    				    					}
		    				    					
		    				    				}
		    				    			}else {
		    				    				radtab = new PdfPTable(2);
		    				    			
		    				    				Phrase pr1 = new Phrase(rightDesc, regular);
	    				    					cell = new PdfPCell(pr1);
	    				    					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    				    					cell.setBorder(Rectangle.NO_BORDER);
	    				    					cell.setNoWrap(false);
	    				    					radtab.addCell(cell);

		    				    				cell = new PdfPCell(new Phrase(""));
	    				    					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    				    					cell.setBorder(Rectangle.NO_BORDER);
	    				    					cell.setNoWrap(false);
	    				    					radtab.addCell(cell);
	    				    	 			}
		    				    			 cell = new PdfPCell(radtab);
		    				    			 cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					    					 cell.setBorder(Rectangle.NO_BORDER);
					    					 totaldec.addCell(cell);
					    					 document.add(totaldec);
		    				    		}
		    				    	}else {
		    				    		 //Space after data
						    		     PdfPTable space2 = new PdfPTable(3);
						    		     space2.setWidthPercentage(95);
						    		     Phrase empty3 = new Phrase("");
						    		     cell = new PdfPCell(empty3);
						    		     cell.setBorder(Rectangle.NO_BORDER);
						    		     cell.setFixedHeight(3f);
						    		     cell.setColspan(3);
						    		     space2.addCell(cell);
						    		     document.add(space2);
						    		     
						    		     if(!scse.getTotalDesc().equals("")) {
						    		    	 	 cell = new PdfPCell(new Phrase(scse.getTotalDesc(), regular));
						    					// cell.setFixedHeight(20f);
						    					 cell.setNoWrap(false);
						    					 cell.setBorder(Rectangle.NO_BORDER);
						    					 cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						    					 totaldec.addCell(cell);
						    		     }
						    		     
		    				    		 if(!scse.getLeftDesc().equals("") && !scse.getRigtDesc().equals("")) {
		    				    			 PdfPTable desc = new PdfPTable(1);
		    				    			 String value ="";
		    				    			 if(vpc != null) {
						    					value = crfDAO.studyCrfSectionElementData(vpc, scse);
						    					if(value == null)
						    						value = "";
						    			     }
		    				    			 Phrase ldesc = new Phrase(scse.getLeftDesc()+" "+value, regular);
						    				 Phrase rdesc = new Phrase(scse.getRigtDesc()+" "+value, regular);
			    						   
						    				 //left heading
					    					 cell = new PdfPCell(ldesc);
					    					// cell.setFixedHeight(20f);
					    					 cell.setNoWrap(false);
					    					 cell.setBorder(Rectangle.NO_BORDER);
					    					 cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					    					 desc.addCell(cell);
					    					
					    					 //right heading
					    					 cell = new PdfPCell(rdesc);
					    					// cell.setFixedHeight(20f);
					    					 cell.setNoWrap(false);
					    					 cell.setBorder(Rectangle.NO_BORDER);
					    					 cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					    					 desc.addCell(cell);
					    					 
					    					// document.add(desc);
					    					 cell = new PdfPCell(desc);
					    					 cell.setBorder(Rectangle.NO_BORDER);
					    					 cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					    					 totaldec.addCell(cell);
					    					 document.add(totaldec);
					    				 }else if(!scse.getLeftDesc().equals("") && scse.getRigtDesc().equals("")) {
					    					 PdfPTable desc = new PdfPTable(1);
					    					 String value ="";
		    				    			 if(vpc != null) {
						    					 value = crfDAO.studyCrfSectionElementData(vpc, scse);
						    					 if(value == null)
						    						 value ="";
						    			     }
		    				    			 String str = scse.getLeftDesc();
		    				    			 if(str.contains("&nbsp;")) {
		    				    				 str = str.replaceAll("&nbsp;", " ");
		    				    			 }
		    				    			 if (str.contains("</br>")) {
		    				    				 str = str.replaceAll("</br>", "\n");
		    				    			 }
		    				    			
					    					 Phrase ldesc = new Phrase(str+"  "+value, regular);
							    				 	
					    					 //left desc
					    					 cell = new PdfPCell(ldesc);
					    					// cell.setFixedHeight(20f);
					    					 cell.setNoWrap(false);
					    					 cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					    					 cell.setBorder(Rectangle.NO_BORDER);
					    					 desc.addCell(cell);
					    					
					    					 cell = new PdfPCell(desc);
					    					 cell.setBorder(Rectangle.NO_BORDER);
					    					 cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					    					 totaldec.addCell(cell);
					    					 document.add(totaldec);
					    					 
					    				 }else if(scse.getLeftDesc().equals("") && !scse.getRigtDesc().equals("")) {
					    					 PdfPTable desc = new PdfPTable(1);
					    					 String value ="";
					    					 
		    				    			 if(vpc != null) {
						    					 value = crfDAO.studyCrfSectionElementData(vpc, scse);
						    					 if(value == null)
						    						 value ="";
						    			     }
		    				    			 Phrase rdesc = new Phrase(scse.getRigtDesc()+" "+value, regular);
							    									    					 
					    					 //right desc
					    					 cell = new PdfPCell(rdesc);
					    					// cell.setFixedHeight(20f);
					    					 cell.setNoWrap(false);
					    					 cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					    					 cell.setBorder(Rectangle.NO_BORDER);
					    					 desc.addCell(cell);
					    					 
					    					 cell = new PdfPCell(desc);
					    					 cell.setBorder(Rectangle.NO_BORDER);
					    					 cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					    					 totaldec.addCell(cell);
					    					 document.add(totaldec);
					    					 
					    				 }
		    				    		//Space after data
						    		     PdfPTable space3 = new PdfPTable(3);
						    		     space2.setWidthPercentage(95);
						    		     Phrase empty4 = new Phrase("");
						    		     cell = new PdfPCell(empty4);
						    		     cell.setBorder(Rectangle.NO_BORDER);
						    		     cell.setFixedHeight(3f);
						    		     cell.setColspan(3);
						    		     space3.addCell(cell);
						    		     document.add(space3);
		    				    	}
		    				    	if(!scse.getBottemDesc().equals("")) {
		    							 PdfPTable botemtable = new PdfPTable(1);
		    							// botemtable.setWidthPercentage(95);
		    							 Phrase bottom = new Phrase(scse.getBottemDesc(), regular);
		    							 cell = new PdfPCell(bottom);
		    							 cell.setNoWrap(false);
		    							 cell.setBorder(Rectangle.NO_BORDER);
		    							 botemtable.addCell(cell);
		    							 document.add(botemtable);
		    						 }
		    				    	 
		    				    }
		    					
		    				}
		    	//		}
	    			}
	    			
	    			
	    	 }else {
	    		 //Group
	    		 //Space purpose
	    		 PdfPTable table_space = new PdfPTable(3);
	    		 Phrase empty = new Phrase("");
	    		 cell = new PdfPCell(empty);
	    		 cell.setBorder(Rectangle.NO_BORDER);
	    		 cell.setColspan(3);
	    		 cell.setFixedHeight(20f);
	    		 table_space.addCell(cell);
	    		 document.add(table_space);
	    		 
	    		 CrfGroup group = sec.getGroup();
	    		 CrfGroupDataRows rowsPojo = null;
	    		 if(vpc != null) {
	    			 rowsPojo = crfDAO.studyCrfGroupDataRows(vpc, group); 
	    		 }
	    		 
	    		 List<CrfGroupElement> scrgroup = group.getElement();
	    		 //Radio Text exist or not checking purpose
	    		 int colNo = group.getMaxColumns();
	    		 List<Integer> pos = new ArrayList<>();
	    		 boolean flag = false;
	    		 Map<Integer, String> radiocmTitle = new HashMap<>();
	    		 Map<Integer, List<String>> radioSecondTitle = new HashMap<>();
	    		 List<Integer> sechPo = new ArrayList<>();
	    		 List<String> tempList = null;
	    		 for(int m=0; m<scrgroup.size(); m++) {
		    		 if(scrgroup.get(m).equals("radioLabel") || scrgroup.get(m).getType().equals("checkBoxLabel") 
		    				    || scrgroup.get(m).getType().equals("radio")) {
		    			 pos.add(m);
		    			 flag = true;
		    			 String value = scrgroup.get(m).getTitle();
		    			 String[] temp = null;
		    			 if(value.contains("</br>")) {
		    				 temp = value.split("</br>");
		    				 if(temp.length >1) {
		    					radiocmTitle.put(m, temp[0]);
		    					if(temp[1].contains("/")) {
		    						String[] str = temp[1].split("/");
		    						if(radioSecondTitle.get(m) == null)
		    							tempList = new ArrayList<>();
		    						else tempList = radioSecondTitle.get(m);
		    						int po =m;
		    						for(String st : str) {
		    							tempList.add(st);
		    							radioSecondTitle.put(m, tempList);
		    							sechPo.add(po);
		    							po++;
		    						}
		    					}else {
		    						if(radioSecondTitle.get(m) == null)
		    							tempList = new ArrayList<>();
		    						else tempList = radioSecondTitle.get(m);
		    						tempList.add(temp[1]);
		    						radioSecondTitle.put(m, tempList);
		    						sechPo.add(m);
		    					}
		    					
		    					
		    				 }else {
		    					 if(radioSecondTitle.get(m) == null)
		    							tempList = new ArrayList<>();
		    						else tempList = radioSecondTitle.get(m);
		    					   tempList.add(temp[1]);
		    					   radioSecondTitle.put(m, tempList);
		    					   sechPo.add(m);
		    				 }
		    			 }else {
		    				 radiocmTitle.put(m, value);
		    				 String str = scrgroup.get(m).getResponceType();
		    				 if(str != null &&!str.equals("")) {
		    					 String[] tempArr = str.split("##");
		    					 if(radioSecondTitle.get(m) == null)
		    							tempList = new ArrayList<>();
		    						else tempList = radioSecondTitle.get(m);
		    					 int po =m;
		    					 for(String  st : tempArr) {
		    						 tempList.add(st);
		    						 radioSecondTitle.put(m, tempList);
		    						 sechPo.add(po);
		    						 po++;
		    					 }
		    					 
		    				 }
		    				 
		    			 }
		    		 }
		    		 
		    	 }
	    		 System.out.println("colNo :"+colNo);
	    		 System.out.println("flag value is : "+flag);
	    		 if(radioSecondTitle.size() !=0) {
    			 	int length =0;
	    		    for(Map.Entry<Integer, List<String>> entry : radioSecondTitle.entrySet()) {
	    		    	List<String> list = entry.getValue();
	    		    	length = length +list.size();
	    		    }
	    			colNo = (colNo)+((length)-1); 
	    		 }
	    		 System.out.println("After Column No : "+colNo);
	    		 PdfPTable table1 = new PdfPTable(colNo);
 	    		 table1.setWidthPercentage(95);
 	    		 PdfPTable table2 = new PdfPTable(colNo);
 	    		 table2.setWidthPercentage(95);
 	    		 PdfPTable datatb = new PdfPTable(colNo);
	 	    	 datatb.setWidthPercentage(95);
	 	    	 
	 	    	 //data Population
	    			// List<StudyCrfGroupElementData> scgdataList = null;
	    			 Map<String, String> dataMap = new HashMap<>();
	    			 List<CrfGroupElementData> scgMaindataList = null;
	    			 Map<Integer, String> strMap = new HashMap<>();
	    			 List<Long> secIdsList = new ArrayList<>();
	    			 	try {
	    			 		if(rowsPojo != null)
	    			 			scgMaindataList = crfPdfService.studyCrfGroupElementDataList(rowsPojo, group);
		    		    }catch(Exception e) {
		    		    	e.printStackTrace();
		    		    }
	    			 	//data saparating into row wise
	    			 	int sp =0;
	    			 	int rowCount =0;
	    			 	if(scgMaindataList != null && scgMaindataList.size() !=0) {
	    			 		rowCount = scgMaindataList.get(0).getDataRows();
	    			 		Set<Long> sectionIds = new HashSet<>();
	    			 		for(CrfGroupElementData sced : scgMaindataList) {
	    			 			sectionIds.add(sced.getElement().getId());
	    			 		}
	    			 		
	    			 		for(Long i : sectionIds) {
	    			 			secIdsList.add(i);
	    			 		}
	    			 		//sorting set
	    			 		Collections.sort(secIdsList);
	    			 		List<String> strList = null;
	    			 		for(int i=1; i<=scgMaindataList.get(0).getDataRows(); i++) {
	    			 			//for(Long secId : sectionIds) {
	    			 				for(int d=0; d<scgMaindataList.size(); d++) {
	    			 					//String str = "g_"+secId+"_group_"+(d)+"_"+i;
	    			 					String keyValue = scgMaindataList.get(d).getKayName();
	    			 					String value = scgMaindataList.get(d).getValue();
	    			 					dataMap.put(keyValue, value);
	    			 					
	    			 				}
	    			 			//}
	    			 		}
	    			 	}
	 	    	 
	    		 if(flag) {
	    			   //header part
	    			// for(int g=0; g<colNo;g++) {
	    				 for(int m=0; m<scrgroup.size(); m++) {
	    					 if(pos.contains(m)) {
	    						String title = radiocmTitle.get(m);
	    						int colSpan =2;
	    						if(title.contains("<br/>"))
	    							title = title.replaceAll("<br/>", "\n");
	    						else if(title.contains("</br>"))
	    							title = title.replaceAll("</br>", "");
	    						else if(title.contains("&nbsp;"))
	    							title = title.replaceAll("&nbsp;", "");
	    						//Placing header
	    						cell = new PdfPCell(new Phrase(title, regular));
	    						cell.setColspan(colSpan);
	    						table1.addCell(cell);
	    					 }else {
	    						 	String title = scrgroup.get(m).getTitle();
		    						if(title.contains("<br/>"))
		    							title = title.replaceAll("<br/>", "\n");
		    						else if(title.contains("</br>"))
		    							title = title.replaceAll("</br>", "");
		    						else if(title.contains("&nbsp;"))
		    							title = title.replaceAll("&nbsp;", "");
		    						//Placing header
		    						cell = new PdfPCell(new Phrase(title, regular));
		    						cell.setRowspan(2);
		    						table1.addCell(cell);
	    					 }
		    			 } 
	    			// }
	    			
	    			 //Second Heading
	    			 if(radioSecondTitle.size() !=0) {
	    				 for(int t=0; t<colNo; t++) {
	    					 if(sechPo.contains(t)) {
	    						 if(radioSecondTitle.containsKey(t)) {
	    							 List<String> strList = radioSecondTitle.get(t);
		    						 int count =0;
		    						 if(strList.size() >0) {
		    							 for(String s : strList) {
			    							 cell = new PdfPCell(new Phrase(s, regular));
		    								 table1.addCell(cell);
		    								 count++;
			    						 }
		    						 }
		    						 if(strList.size() == count)
		    							 t++; 
	    						 }
	    					 }
	    				 }
	    				 
	    			 }
	    			  //data writing
	    			 	if(dataMap.size() !=0) {
	    			 			for(int i=1; i<=rowCount; i++) {
			    			    	for(int s=0; s<group.getMaxColumns(); s++) {
			    			    		Long eleId = scrgroup.get(s).getId();
				    			    	String eleName = scrgroup.get(s).getName();
				    			    	String str1 = "g_"+eleId+"_"+eleName+"_"+i;
	   			 						String value = dataMap.get(str1);
    			 						if(value != null && !value.equals("")) {
    			 							if(value.contains("</br>"))
	    			 							value = value.replaceAll("</br>", "");
	    			 						else if(value.contains("<br/>"))
	    			 							value = value.replaceAll("<br/>", "");
	    			 						else if(value.contains("&nbsp;"))
	    			 							value = value.replaceAll("&nbsp;", "");
    			 						}else value ="";
    				    				//for(StudyCrfGroupElement scg : group.getElement()) {
    		    			 				if(pos.size() !=0) {
    		    			 					if(pos.contains((s))) {
    		    			 						int po =0;
    		    			 						List<String> strList = radioSecondTitle.get(s);
    		    			 						for(String st : strList) {
    		    			 						   if(st.equals(value)) {
    		    			 								Image chkImg = Image.getInstance(chkrdPath);
    				    				    				chkImg.scaleAbsolute(10, 10);
    				    				    				cell = new PdfPCell(chkImg);
    				    				    				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    							    						datatb.addCell(cell);
    		    			 							}else {
	    		    			 							Image chkImg = Image.getInstance(unchkrdPath);
	    			    				    				chkImg.scaleAbsolute(15, 15);
	    			    				    				cell = new PdfPCell(chkImg);
	    			    				    				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    						    						datatb.addCell(cell);
    		    			 							}
    		    			 							po++;
    		    			 						}
    		    			 						if(radioSecondTitle.size() == po) {
    		    			 							s = s+((radioSecondTitle.size())-po);
    		    			 						}
    		    			 					}else {
    		    			 						System.out.println("value is :"+value);
    		    			 						cell = new PdfPCell(new Phrase(value, regular));
    		    			 						datatb.addCell(cell);
    		    			 					}
    		    			 				}else {
    		    			 					System.out.println("value1 is :"+value);
    	    			 						cell = new PdfPCell(new Phrase(value, regular));
    	    			 						datatb.addCell(cell);
    		    			 				}
    		    			 			//}
    				    			}
	    			 		    }
	    			 			
	    			 			
	    			 		//}
	    			 		
	    			 	}
	    		 }else { //flag false condition
	    			  //header part 
	    			   for(int t=0; t<colNo; t++) {
	    				   String title = scrgroup.get(t).getTitle();
    						if(title.contains("<br/>"))
    							title = title.replaceAll("<br/>", "\n");
    						else if(title.contains("</br>"))
    							title = title.replaceAll("</br>", "");
    						else if(title.contains("&nbsp;"))
    							title = title.replaceAll("&nbsp;", "");
    						//Placing heder
    						cell = new PdfPCell(new Phrase(title, regular));
    						table1.addCell(cell);
	    			   }
	    			   
	    			   //data writing
	    			   if(dataMap.size() !=0) {
	    				   for(int j=1; j<=rowCount; j++) {
	    					   for(int i=0; i<colNo; i++) {
	    						    Long eleId = scrgroup.get(i).getId();
		    			    		String eleName = scrgroup.get(i).getName();
		    			    		String str1 = "g_"+eleId+"_"+eleName+"_"+j;
			 							String value = dataMap.get(str1);
			 						if(value != null && !value.equals("")) {
			 							if(value.contains("</br>"))
    			 							value = value.replaceAll("</br>", "");
    			 						else if(value.contains("<br/>"))
    			 							value = value.replaceAll("<br/>", "");
    			 						else if(value.contains("&nbsp;"))
    			 							value = value.replaceAll("&nbsp;", "");
			 						}else value ="";
	    						        System.out.println("final value is :"+value);
				 						cell = new PdfPCell(new Phrase(value, regular));
				 						datatb.addCell(cell);
	    						  // }
	    						    
		    			 		}
	    				   }
	    			 		
	    			 }
	    		 }
	    		 document.add(table1);
    		     document.add(table2);
    		     document.add(datatb);

    		     
    		     //Space after data
    		     PdfPTable space = new PdfPTable(3);
    		     space.setWidthPercentage(95);
    		     Phrase empty2 = new Phrase("");
    		     cell = new PdfPCell(empty2);
    		     cell.setBorder(Rectangle.NO_BORDER);
    		     cell.setFixedHeight(20f);
    		     cell.setColspan(3);
    		     space.addCell(cell);
    		     document.add(space);
	    	 }
	     }
         
        
         document.close();
        /* out.flush();
         out.close();*/
		return fileName;
	}

	public void mergePdf(HttpServletRequest request, HttpServletResponse response, List<String> fileNames) throws IOException, DocumentException {
		response.setContentType("application/pdf");
		OutputStream out = response.getOutputStream();
		Document document = new Document();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, out);
        document.open();
		List<InputStream> list = new ArrayList<InputStream>();
		for(String st : fileNames) {
			InputStream stream = new FileInputStream(new File(st));
            list.add(stream);
		}
		
		if(list.size() > 0) {
			PdfContentByte pdfContentByte = pdfWriter.getDirectContent();
            for (InputStream inputStream : list){
                PdfReader pdfReader = new PdfReader(inputStream);
                for (int i = 1; i <= pdfReader.getNumberOfPages(); i++){
                    document.newPage();
                    PdfImportedPage page = pdfWriter.getImportedPage(pdfReader, i);
                    pdfContentByte.addTemplate(page, 0, 0);
                }
            }
 
            out.flush();
            document.close();
            out.close();
		}
	}
}
