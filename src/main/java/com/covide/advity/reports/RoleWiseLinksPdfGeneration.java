package com.covide.advity.reports;


	
	import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

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
import com.springmvc.model.RolesWiseModules;

	public class RoleWiseLinksPdfGeneration  {
		
		@SuppressWarnings("unused")
		public void generateReport(HttpServletResponse response, HttpServletRequest request, Map<String, Map<String, List<RolesWiseModules>>> menusMap) throws DocumentException, IOException {
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
				document.addTitle("Rolewise Configured Links");
				document.setPageSize(PageSize.A4);
				document.setMargins(80, 80, 50, 80);
				document.setMarginMirroring(false);
				document.open();
//				Font bold = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD);
				Font regular = new Font(FontFamily.TIMES_ROMAN, 10);
				Font heading = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD);
				Font sideheading = new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD);
				PdfPCell cell = null;
				
				PdfPTable table = new PdfPTable(3);
				table.setWidthPercentage(95f);
				
				String sMenu = "";
				String rolename= "";
				for(Map.Entry<String, Map<String, List<RolesWiseModules>>> map1 : menusMap.entrySet()) {
					String roleName = map1.getKey();
					Map<String, List<RolesWiseModules>> smLinksMap = map1.getValue();
					for(Map.Entry<String, List<RolesWiseModules>> map2 : smLinksMap.entrySet()) {
						String sideMenu = map2.getKey();
						List<RolesWiseModules> rwmList = map2.getValue();
						for(RolesWiseModules rwm : rwmList) {
							if(rolename.equals("")) {
								cell = new PdfPCell(new Phrase(roleName, heading));
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell.setFixedHeight(35f);
								cell.setColspan(3);
								table.addCell(cell);
								rolename = roleName;
							}
							
							if(!sMenu.equals(sideMenu)) {
								cell = new PdfPCell(new Phrase(sideMenu, sideheading));
								cell.setHorizontalAlignment(Element.ALIGN_LEFT);
								cell.setFixedHeight(20f);
								cell.setColspan(3);
								table.addCell(cell);
								sMenu = sideMenu;
							}
							cell = new PdfPCell(new Phrase("", regular));
							cell.setFixedHeight(20f);
							table.addCell(cell);
							
							cell = new PdfPCell(new Phrase(rwm.getAppsideMenu().getSideLink()));
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell.setFixedHeight(20f);
							cell.setColspan(2);
							table.addCell(cell);
							
						}
					}
					document.add(table);
				}
				
				
				document.close();
				out.flush(); 
				out.close();
			
			
		}

}
