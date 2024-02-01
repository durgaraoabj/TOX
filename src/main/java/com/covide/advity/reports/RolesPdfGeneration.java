package com.covide.advity.reports;


	
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.springmvc.model.RoleMaster;

	public class RolesPdfGeneration  {
		
		@SuppressWarnings("unused")
		public void generateReport(HttpServletResponse response, HttpServletRequest request, List<RoleMaster> rolesList) throws DocumentException, IOException {
				response.setContentType("application/pdf");
			    OutputStream out = response.getOutputStream();
				String realPath = request.getServletContext().getRealPath("/");
				String path = realPath + "/AdvityRoleLinks/";
				File directory = new File(path);
				if (!directory.exists()) {
					directory.mkdirs();
				}
				String fileName = path +"RoleWiseLinks.pdf";
				Document document = new Document();
				PdfWriter writer = PdfWriter.getInstance(document, out);
				document.addTitle("Total Roles List");
				document.setPageSize(PageSize.A4);
				document.setMargins(80, 80, 50, 80);
				document.setMarginMirroring(false);
				document.open();
//				Font bold = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD);
				Font regular = new Font(FontFamily.TIMES_ROMAN, 10);
				Font heading = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD);
				Font sideheading = new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD);
				PdfPCell cell = null;
				
				PdfPTable table = new PdfPTable(7);
				table.setWidthPercentage(95f);
				int count =1;
				
				cell = new PdfPCell(new Phrase("Total Roles", heading));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setFixedHeight(20f);
				cell.setColspan(7);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase("SNo", sideheading));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase("Role", sideheading));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase("Role Description", sideheading));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase("Status", sideheading));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase("Transaction Password", sideheading));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase("CreatedBy", sideheading));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase("CreatedOn", sideheading));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell);
				
				for(RoleMaster rm : rolesList) {
					cell = new PdfPCell(new Phrase(count+"", regular));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setFixedHeight(20f);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(rm.getRole(), regular));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setFixedHeight(20f);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(rm.getRoleDesc(), regular));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setFixedHeight(20f);
					table.addCell(cell);
					
					if(rm.getStatus().equals('T')) {
						cell = new PdfPCell(new Phrase("Active", regular));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setFixedHeight(20f);
						table.addCell(cell);
					}else {
						cell = new PdfPCell(new Phrase("In-Active", regular));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setFixedHeight(20f);
						table.addCell(cell);
					}
					if(rm.isTranPassword()) {
						cell = new PdfPCell(new Phrase("Yes", regular));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setFixedHeight(20f);
						table.addCell(cell);
					}else {
						cell = new PdfPCell(new Phrase("No", regular));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setFixedHeight(20f);
						table.addCell(cell);
					}
					cell = new PdfPCell(new Phrase(rm.getCreatedBy(), regular));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setFixedHeight(20f);
					table.addCell(cell);
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String createdOn = sdf.format(rm.getCreatedOn());
					
					cell = new PdfPCell(new Phrase(createdOn, regular));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setFixedHeight(20f);
					table.addCell(cell);
					
					count++;
					
				}
				document.add(table);
				document.close();
				out.flush(); 
				out.close();
			
			
		}

}
