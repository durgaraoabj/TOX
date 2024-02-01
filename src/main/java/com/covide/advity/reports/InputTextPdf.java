package com.covide.advity.reports;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class InputTextPdf extends PdfPageEventHelper{
	
	@SuppressWarnings("unused")
	public void generateTextPdf(HttpServletRequest request, HttpServletResponse response, String str) {
		try {
			//Font bold = new Font(FontFamily.TIMES_ROMAN, 13, Font.BOLD);
			response.setContentType("application/pdf");
			OutputStream out = response.getOutputStream();
			Document document = new Document();
			document.setPageSize(PageSize.A4);
			document.setMargins(50, 45, 50, 120);
		    document.setMarginMirroring(false);
		    PdfWriter pdfWriter = PdfWriter.getInstance(document, out);
		    document.open();
		    Font red = new Font(FontFamily.TIMES_ROMAN, 13, Font.BOLD, BaseColor.RED);
		    Font blue = new Font(FontFamily.TIMES_ROMAN, 13, Font.BOLD, BaseColor.BLUE);
		    Font green = new Font(FontFamily.TIMES_ROMAN, 13, Font.BOLD, BaseColor.GREEN);

		    PdfPCell cell = null;
		    
		    PdfPTable table = new PdfPTable(1);
    		table.setWidthPercentage(100f);
    		cell = new PdfPCell(new Phrase(str, red));
		    cell.setBorder(Rectangle.NO_BORDER);
		    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(cell);
		    document.add(table);
		    
	        out.flush();
	        document.close();
	        out.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

