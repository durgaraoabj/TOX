package com.covide.excels;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;




public class ExcelDataReading {
	
	@SuppressWarnings({"incomplete-switch", "unused" })
	public Map<String, Map<Integer, Map<Integer, String>>> ReadXlsxExcelData(String type, String Location) throws IOException {
		Map<Integer, List<String>> map = new HashMap<>();
		Map<String, Map<Integer, Map<Integer, String>>> auditMap = new HashMap<>();
		Map<Integer, Map<Integer, String>> rowMap = null;
		Map<Integer, String> colMap = null;
		FileInputStream file = null;
		FileInputStream filecontent = null;
		XSSFWorkbook my_xlsx_workbook=null;
		HSSFWorkbook my_xls_workbook=null;
     	List<String> strList = null;
		DataFormatter dataFormatter = new DataFormatter();
		String sheetname ="";
		 DecimalFormat df = new DecimalFormat("0.00");
		if(type.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			try {
				file = new FileInputStream(Location);
				my_xlsx_workbook = new XSSFWorkbook(file);
				int sheetNo = my_xlsx_workbook.getNumberOfSheets();
				String shtName ="";
				Iterator<Row> rowiterator = null;
				for(int i=0; i<sheetNo; i++) {
					XSSFSheet my_xlsxSheet = my_xlsx_workbook.getSheetAt(i);
					shtName = my_xlsxSheet.getSheetName();
					rowiterator = my_xlsxSheet.iterator();
					while(rowiterator.hasNext()) {
						Row row = rowiterator.next();
						int rowno = ((row.getRowNum()) +1);
						Iterator<Cell> cellIterator = row.cellIterator();
						while(cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
							int columnNo = cell.getColumnIndex();
							ExcelColumnNo excol = new ExcelColumnNo();
							switch(cell.getCellTypeEnum()) { 
							  case STRING:
								String cellVal = cell.getStringCellValue();
								String colNo = excol.printString((columnNo+1));
								/*String finalVal = shtName+"##"+cellVal+"##"+rowno+"##"+colNo;
								if(map.get(rowno) == null) strList = new ArrayList<>();
								else strList = map.get(rowno);
								strList.add(finalVal);
								map.put(rowno, strList);*/
								auditMap = getAuditMapRecords(cellVal, colNo, columnNo,  rowno, shtName, rowMap, colMap, auditMap);
								break;
							  case FORMULA:
								if (cell.getCellTypeEnum() == CellType.FORMULA) {
									switch (cell.getCachedFormulaResultTypeEnum()) {
									case NUMERIC:
//										System.out.println("Last evaluated as: " + cell.getNumericCellValue());
										String cellValue = df.format(cell.getNumericCellValue()) + "";
										String col = excol.printString((columnNo + 1));
										/*String fVal = shtName + "##" + cellValue + "##" + rowno + "##" + col;
										if (map.get(rowno) == null)
											strList = new ArrayList<>();
										else
											strList = map.get(rowno);
										strList.add(fVal);
										map.put(rowno, strList);*/
										auditMap = getAuditMapRecords(cellValue, col, columnNo,  rowno, shtName, rowMap, colMap, auditMap);
										break;
									case STRING:
//										System.out.println("Last evaluated as \"" + cell.getRichStringCellValue() + "\"");
										String cVal = cell.getRichStringCellValue() + "";
										String col2 = excol.printString((columnNo + 1));
										/*String fVal2 = shtName + "##" + cVal + "##" + rowno + "##" + col2;
										if (map.get(rowno) == null)
											strList = new ArrayList<>();
										else
											strList = map.get(rowno);
										strList.add(fVal2);
										map.put(rowno, strList);*/
										auditMap = getAuditMapRecords(cVal, col2, columnNo,  rowno, shtName, rowMap, colMap, auditMap);
										break;
									}
								}
								  	 
					            break;
							  case NUMERIC:
						 	 		 if (DateUtil.isCellDateFormatted(cell)) {
						 	 			  cellVal = dataFormatter.formatCellValue(cell);
						 	 			  String colno = excol.printString((columnNo+1));
						 	 			 /* if(map.get(rowno) == null) strList = new ArrayList<>();
						 	 			  else strList = map.get(rowno);
						 	 			  strList.add(shtName+"##"+cellVal+"##"+rowno+"##"+colno);
						 	 			  map.put(rowno, strList);*/
						 	 			  auditMap = getAuditMapRecords(cellVal, colno, columnNo,  rowno, shtName, rowMap, colMap, auditMap);
						 	 			  break;
						 	 		 }else {
						 	 			  cellVal = dataFormatter.formatCellValue(cell);
						 	 			  String colno = excol.printString((columnNo+1));
						 	 			 /* if(map.get(rowno) == null) strList = new ArrayList<>();
						 	 			  else strList = map.get(rowno);
						 	 			  strList.add(shtName+"##"+cellVal+"##"+rowno+"##"+colno);
						 	 			  map.put(rowno, strList);*/
						 	 			  auditMap = getAuditMapRecords(cellVal, colno, columnNo,  rowno, shtName, rowMap, colMap, auditMap);
						 	 			  break;
						 	 		 }
							  case BLANK:
								  String colno = excol.printString((columnNo+1));
								  /*if(map.get(rowno) == null) strList = new ArrayList<>();
				 	 			  else strList = map.get(rowno);
								  strList.add(shtName+"##"+" "+"##"+rowno+"##"+colno);*/
								  auditMap = getAuditMapRecords("", colno, columnNo,  rowno, shtName, rowMap, colMap, auditMap);
								  map.put(rowno, strList);
								  break;
							}
							
						}
					}
				}
			
			} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				file.close();
			}
		}else {
			try {
				file = new FileInputStream(Location);
				my_xls_workbook = new HSSFWorkbook(file);
				int sheetNo = my_xls_workbook.getNumberOfSheets();
				String shtName ="";
				Iterator<Row> rowiterator = null;
				for(int i=0; i<sheetNo; i++) {
					HSSFSheet my_xlsSheet = my_xls_workbook.getSheetAt(i);
					shtName = my_xlsSheet.getSheetName();
					rowiterator = my_xlsSheet.iterator();
					while(rowiterator.hasNext()) {
						Row row = rowiterator.next();
						int rowno = ((row.getRowNum()) +1);
						Iterator<Cell> cellIterator = row.cellIterator();
						while(cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
							int columnNo = cell.getColumnIndex();
							ExcelColumnNo excol = new ExcelColumnNo();
							switch(cell.getCellTypeEnum()) { 
							  case STRING:
								String cellVal = cell.getStringCellValue();
								String colNo = excol.printString((columnNo+1));
								String finalVal = shtName+"##"+cellVal+"##"+rowno+"##"+colNo;
								if(map.get(rowno) == null) strList = new ArrayList<>();
								else strList = map.get(rowno);
								strList.add(finalVal);
								map.put(rowno, strList);
								auditMap = getAuditMapRecords(cellVal, colNo, columnNo,  rowno, shtName, rowMap, colMap, auditMap);
								break;
							  case FORMULA:
								  if(cell.getCellTypeEnum() == CellType.FORMULA) {
									  switch(cell.getCachedFormulaResultTypeEnum()) {
							            case NUMERIC:
							                System.out.println("Last evaluated as: " + cell.getNumericCellValue());
							                 String cellValue = df.format(cell.getNumericCellValue())+"";
							                 String col = excol.printString((columnNo+1));
											 /*String fVal = shtName+"##"+cellValue+"##"+rowno+"##"+col;
											 if(map.get(rowno) == null) strList = new ArrayList<>();
											 else strList = map.get(rowno);
											 strList.add(fVal);
											 map.put(rowno, strList);*/
											 auditMap = getAuditMapRecords(cellValue, col, columnNo,  rowno, shtName, rowMap, colMap, auditMap);
							 	 		 	 break;
							            case STRING:
							                System.out.println("Last evaluated as \"" + cell.getRichStringCellValue() + "\"");
							                 String  cVal = cell.getRichStringCellValue()+"";
							                 String col2 = excol.printString((columnNo+1));
											/* String fVal2 = shtName+"##"+cVal+"##"+rowno+"##"+col2;
											 if(map.get(rowno) == null) strList = new ArrayList<>();
											 else strList = map.get(rowno);
											 strList.add(fVal2);*/
											 map.put(rowno, strList);
											 auditMap = getAuditMapRecords(cVal, col2, columnNo,  rowno, shtName, rowMap, colMap, auditMap);
							                 break;
							        } 
								  }
								  break;
							  case NUMERIC:
						 	 		 if (DateUtil.isCellDateFormatted(cell)) {
						 	 			  cellVal = dataFormatter.formatCellValue(cell);
						 	 			  String colno = excol.printString((columnNo+1));
						 	 			  /*if(map.get(rowno) == null) strList = new ArrayList<>();
						 	 			  else strList = map.get(rowno);
						 	 			  strList.add(shtName+"##"+cellVal+"##"+rowno+"##"+colno);
						 	 			  map.put(rowno, strList);*/
						 	 			  auditMap = getAuditMapRecords(cellVal, colno, columnNo,  rowno, shtName, rowMap, colMap, auditMap);
						 	 			  break;
						 	 		 }else {
						 	 			  cellVal = dataFormatter.formatCellValue(cell);
						 	 			  String colno = excol.printString((columnNo+1));
						 	 			  /*if(map.get(rowno) == null) strList = new ArrayList<>();
						 	 			  else strList = map.get(rowno);
						 	 			  strList.add(shtName+"##"+cellVal+"##"+rowno+"##"+colno);
						 	 			  map.put(rowno, strList);*/
						 	 			  auditMap = getAuditMapRecords(cellVal, colno, columnNo,  rowno, shtName, rowMap, colMap, auditMap);
						 	 			  break;
						 	 		 }
							  case BLANK:
								  String colno = excol.printString((columnNo+1));
								  /*if(map.get(rowno) == null) strList = new ArrayList<>();
				 	 			  else strList = map.get(rowno);
								  strList.add(shtName+"##"+" "+"##"+rowno+"##"+colno);
								  map.put(rowno, strList);*/
								  auditMap = getAuditMapRecords("", colno, columnNo,  rowno, shtName, rowMap, colMap, auditMap);
								  break;
							}
							
						}
					}
				}
			
			} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				file.close();
			}
		}
		
		
		return auditMap;
	}

	private Map<String, Map<Integer, Map<Integer, String>>> getAuditMapRecords(String cellVal, String colNo, int columnNo, int rowno,
			String shtName, Map<Integer, Map<Integer, String>> rowMap, Map<Integer, String> colMap,
			Map<String, Map<Integer, Map<Integer, String>>> auditMap) {
		Map<String, Map<Integer, Map<Integer, String>>> finalMap = new HashMap<>();
		if(auditMap.size() > 0)
			finalMap.putAll(auditMap);
		try {
			if(finalMap.containsKey(shtName)) {
				rowMap = finalMap.get(shtName);
				if(rowMap != null && rowMap.size() > 0) {
					if(rowMap.containsKey(rowno)) {
						colMap = rowMap.get(rowno);
						if(colMap != null && colMap.size() > 0) {
							String colStr = colNo+"##"+cellVal;
							colMap.put(columnNo, colStr);
							rowMap.put(rowno, colMap);
							finalMap.put(shtName, rowMap);
						}else {
							colMap = new HashMap<>();
							String colStr = colNo+"##"+cellVal;
							colMap.put(columnNo, colStr);
							rowMap.put(rowno, colMap);
							finalMap.put(shtName, rowMap);
						}
					}else {
						colMap = new HashMap<>();
						String colStr = colNo+"##"+cellVal;
						colMap.put(columnNo, colStr);
						rowMap.put(rowno, colMap);
						finalMap.put(shtName, rowMap);
					}
				}else {
					rowMap = new HashMap<>();
					colMap = new HashMap<>();
					String colStr = colNo+"##"+cellVal;
					colMap.put(columnNo, colStr);
					rowMap.put(rowno, colMap);
					finalMap.put(shtName, rowMap);
				}
				
			}else {
				rowMap = new HashMap<>();
				colMap = new HashMap<>();
				String colStr = colNo+"##"+cellVal;
				colMap.put(columnNo, colStr);
				rowMap.put(rowno, colMap);
				finalMap.put(shtName, rowMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalMap;
	}
}
