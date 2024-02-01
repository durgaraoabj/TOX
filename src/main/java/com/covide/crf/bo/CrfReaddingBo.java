package com.covide.crf.bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import com.covide.crf.dto.CRFGroupItem;
import com.covide.crf.dto.CRFItemValues;
import com.covide.crf.dto.CRFSections;
import com.covide.crf.dto.CrfItems;
import com.covide.crf.dto.CrfMetaData;
import com.covide.crf.dto.DTNAME;
import com.springmvc.controllers.CRFController;
@Service("crfbo")
public class CrfReaddingBo {

		
	public CrfMetaData crf; 
	public CrfMetaData getDisplayableItemList(HSSFWorkbook wb){
		try {
			CRFController.dt = new DTNAME();
			CRFController.sectionsMap = new HashMap<>();
			CRFController.groupsMap = new HashMap<>();
			HSSFSheet sheet = wb.getSheetAt(0);
			crfmeataDataSheet(sheet, CRFController.dt);
			sheet = wb.getSheetAt(1);
			crfSectionDataSheet(sheet, CRFController.dt);
			crf.setSections(CRFController.sections);
			sheet = wb.getSheetAt(2);
			crfGroupDataSheet(sheet, CRFController.dt);
			crf.setGroups(CRFController.groups);
			sheet = wb.getSheetAt(3);
			crfItemSheet(sheet, CRFController.dt);

			CRFController.crfSections = crf.getSections();
			Collections.sort(CRFController.crfSections);
			CRFController.rowContainsGroup = new HashSet<>();
			for (CrfItems item : CRFController.items) {
				if (item.getGroup() != null) {
					CRFController.rowContainsGroup.add(item.getSection().getTempId() + "_" + item.getGroup().getTempId() + "_"
							+ item.getRowNumber());
				}
			}

			for (CRFSections sec : CRFController.crfSections) {
				sec.setItemList(CRFController.sectionsItems.get(sec.getSectionName()));
			}
			crf.setSections(CRFController.crfSections);
			for (CRFGroupItem group : crf.getGroups()) {
				group.setItemList(CRFController.groupItems.get(group.getGroupName()));
			}
			return crf;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	 
	private void crfmeataDataSheet(HSSFSheet sheet, DTNAME dt) {
		System.out.println("Seet name: " + sheet.getHeader());
		crf = new CrfMetaData();
		Iterator<Row> rowIterator = sheet.iterator();
		rowIterator.next();
		int i = 1;
		while (rowIterator.hasNext() && i == 1) {

			// Get Each Row
			Row row = rowIterator.next();
			// Iterating through Each column of Each Row
			Iterator<Cell> cellIterator = row.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				switch (i) {
				case 1:
					crf.setCrfDesc(cell.getStringCellValue());
					System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 2:
					crf.setCrfName(cell.getStringCellValue());
					System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 3:
					crf.setGender(cell.getStringCellValue());
					System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 4:
					crf.setType(cell.getStringCellValue());
					System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 5:
					crf.setStringCellValue(cell.getStringCellValue());
					System.out.print(cell.getStringCellValue() + "\t");
					break;
				default:
					break;
				}
				i++;
			}

			System.out.println("");
		}
	}
	
	private Double d = null;
	private void crfSectionDataSheet(HSSFSheet sheet, DTNAME dt) {
		CRFController.sections = new ArrayList<>();
		CRFController.crfsections = new HashMap<>();
		CRFController.sectionsItems = new HashMap<>();
		Iterator<Row> rowIterator = sheet.iterator();
		rowIterator.next();
		int count = 1;
		while (rowIterator.hasNext()) {
			CRFSections section = new CRFSections();
			section.setTempId(count++);
			section.setCrfId(crf);
			section.setCreatedOn(dt.getDate());
			int i = 1;
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				try {
					 System.out.print(cell.getStringCellValue()+" ");
				} catch (Exception e) {
					// TODO: handle exception
					 System.out.print(cell.getNumericCellValue()+" ");
				}
				switch (i) {
				case 1:
					section.setSectionDesc(cell.getStringCellValue());
					 System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 2:
					section.setSectionName(cell.getStringCellValue());
					 System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 3:
					d = cell.getNumericCellValue();
					section.setMaxRows(d.intValue());
					 System.out.print(d+ "\t");
					break;
				case 4:
					d = cell.getNumericCellValue();
					section.setMaxColumns(d.intValue());
						
					 System.out.print(d+ "\t");
					break;
				case 5:
					section.setGender(cell.getStringCellValue());
					 System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 6:
					d = cell.getNumericCellValue();
					section.setOrderNo(d.intValue());
					 System.out.print(d+ "\t");
					 if(CRFController.maxSections < d.intValue())
						 CRFController.maxSections = d.intValue();
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 7:
					section.setContainsGroup(cell.getStringCellValue());
					 System.out.print(cell.getStringCellValue() + "\t");
					break;
/*				case 5:
					if (cell.getStringCellValue() != null)
						section.setDataSetTechName(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 6:
					d = cell.getNumericCellValue();
					section.setFontSize(d.intValue());
					// System.out.print(d + "\t");
					// System.out.print( "\t");
					break;
				case 7:
					if (cell.getStringCellValue() != null)
						section.setSignatureField(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 8:
					if (cell.getStringCellValue() != null)
						section.setSingleColumn(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 9:
					if (cell.getStringCellValue() != null)
						section.setUserRole(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 10:
					if (cell.getStringCellValue() != null)
						section.setTableLines(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 11:
					d = cell.getNumericCellValue();
					section.setOrderNo(d.intValue());
					// System.out.print(d + "\t");
					// System.out.print("\t");
					break;*/
				default:
					break;
				}
				i++;
			}
			// System.out.println("");
			CRFController.sections.add(section);
			
			CRFController.crfsections.put(section.getOrderNo(), section);
			CRFController.sectionsMap.put(section.getSectionName(), section);
			List<CrfItems> items = new ArrayList<>();
			CRFController.sectionsItems.put(section.getSectionName(), items);
		}
	}

	
	private void crfGroupDataSheet(HSSFSheet sheet, DTNAME dt) {
		// TODO Auto-generated method stub
		CRFController.groups = new ArrayList<>();
		CRFController.groupItems = new HashMap<>();
		Iterator<Row> rowIterator = sheet.iterator();
		rowIterator.next();
		int count = 1;
		while (rowIterator.hasNext()) {
			CRFGroupItem group = new CRFGroupItem();
			group.setTempId(count++);
			group.setCrfId(crf);
			group.setCreatedOn(dt.getDate());
			int i = 1;
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				switch (i) {
				case 1:
					group.setGroupDesc(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 2:
					group.setGroupName(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 3:
					d = cell.getNumericCellValue();
					group.setDisplayRow(d.intValue());
					// System.out.print(d + "\t");
					break;
				case 4:
					d = cell.getNumericCellValue();
					group.setMaxColumns(d.intValue());
					// System.out.print("\t");
					break;
				case 5:
					d = cell.getNumericCellValue();
					group.setMaxRow(d.intValue());
					// System.out.print(d + "\t");
					// System.out.print("\t");
					break;
/*				case 6:
					if (cell.getStringCellValue() != null)
						group.setDataSetTechName(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 7:
					if (cell.getStringCellValue() != null)
						group.setLabel_values(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 8:
					d = cell.getNumericCellValue();
					group.setFontSize(d.intValue());
					// System.out.print(d + "\t");
					// System.out.print("\t");
					break;
				case 9:
					if (cell.getStringCellValue() != null)
						group.setFontStyle(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 10:
					if (cell.getStringCellValue() != null)
						group.setGroupValueSizes(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;*/
				default:
					break;
				}
				i++;
			}
			CRFController.groups.add(group);
			CRFController.groupsMap.put(group.getGroupName(), group);
			List<CrfItems> items = new ArrayList<>();
			CRFController.groupItems.put(group.getGroupName(), items);
		}
	}

	
	private List<CRFItemValues> itemValues;
	private int maxRows = 0;
	private void crfItemSheet(HSSFSheet sheet, DTNAME dt) {
		// System.out.println("");
		CRFController.items = new ArrayList<>();
		
		CRFController.crfItems = new  HashMap<>();
		Iterator<Row> rowIterator = sheet.iterator();
		rowIterator.next();
		int count = 1;
		while (rowIterator.hasNext()) {
			CrfItems item = new CrfItems();
			item.setTempId(count++);
			item.setCrfId(crf);
			item.setCreatedOn(dt.getDate());
			int i = 1;
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				switch (i) {
				case 1:
					item.setItemName(cell.getStringCellValue());
					 System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 2:
					item.setItemDesc(cell.getStringCellValue());
					 System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 3:
					item.setItemDescRight(cell.getStringCellValue());
					 System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 4:
					d = cell.getNumericCellValue();
					item.setColumnNo(d.intValue());
					// System.out.print(d + "\t");
					break;
				case 5:
					if (cell.getStringCellValue() != null)
						item.setItemType(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 6:
					if (cell.getStringCellValue() != null)
						item.setResponseType(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 7:
					if (!cell.getStringCellValue().trim().equals("")) {
						item.setResponseValue(cell.getStringCellValue());
						itemValues = new ArrayList<>();
						String[] s = cell.getStringCellValue().split("##");
						for (int j = 0; j < s.length; j++) {
							CRFItemValues itemValue = new CRFItemValues();
							itemValue.setCrfItem(item);
							itemValue.setElemenstValue(s[j]);
							itemValues.add(itemValue);
						}
						item.setItemResponceValues(itemValues);
					}
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 8:
					d = cell.getNumericCellValue();
					item.setRowNumber(d.intValue());
					if (item.getRowNumber() > maxRows)
						maxRows = item.getRowNumber();
					// System.out.print(d);
					// System.out.print("\t");
					break;
				case 9:
					if (!cell.getStringCellValue().trim().equals("")) {
						if (CRFController.groupsMap.get(cell.getStringCellValue().trim()) != null) {
							item.setGroup(CRFController.groupsMap.get(cell.getStringCellValue().trim()));
							List<CrfItems> tempItems = CRFController.groupItems.get(cell.getStringCellValue().trim());
							tempItems.add(item);
							CRFController.groupItems.put(cell.getStringCellValue().trim(), tempItems);
						}
					}
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 10:
					if (!cell.getStringCellValue().trim().equals("")) {
						if (CRFController.sectionsMap.get(cell.getStringCellValue().trim()) != null) {
							item.setSection(CRFController.sectionsMap.get(cell.getStringCellValue().trim()));
							List<CrfItems> tempItems = CRFController.sectionsItems.get(cell.getStringCellValue().trim());
							tempItems.add(item);
							CRFController.sectionsItems.put(cell.getStringCellValue().trim(), tempItems);
						}
					}
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
/*				case 11:
					if (cell.getStringCellValue() != null)
						item.setDataSetTechName(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 12:
					d = cell.getNumericCellValue();
					item.setFontSize(d.intValue());// System.out.println(d+"\t");
					break;
				case 13:
					if (cell.getStringCellValue() != null)
						item.setFontStyle(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 14:
					if (cell.getStringCellValue() != null)
						item.setUnderLine(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 15:
					d = cell.getNumericCellValue();
					item.setPrintDesc(d.intValue());
					// System.out.print(d + "\t");
					break;
				case 16:
					d = cell.getNumericCellValue();
					item.setIeTestCode("" + d.intValue());
					// System.out.print(d + "\t");
					break;
				case 17:
					d = cell.getNumericCellValue();
					item.setPrintContent(d.intValue());
					// System.out.print(d + "\t");
					break;
				case 18:
					d = cell.getNumericCellValue();
					item.setCellHeight(d.intValue());
					// System.out.print(d + "\t");
					break;
				case 19:
					d = cell.getNumericCellValue();
					item.setDescCellWidth(d.intValue());
					// System.out.print(d + "\t");
					break;
				case 20:
					d = cell.getNumericCellValue();
					item.setValueCellWidth("" + d.intValue());
					// System.out.print(d + "\t");
					break;
				case 21:
					if (cell.getStringCellValue() != null)
						item.setValueWithImage(cell.getStringCellValue().charAt(0));
					// System.out.print(cell.getStringCellValue() + "\t");
					break;
				case 22:
					if (cell.getStringCellValue() != null)
						item.setCheckAlign(cell.getStringCellValue());
					// System.out.print(cell.getStringCellValue() + "\t");
					break;*/
				default:
					break;
				}
				i++;
			}
			CRFController.items.add(item);
			CRFController.crfItems.put(item.getSection().getSectionName()+","+item.getRowNumber()+","+item.getColumnNo(), item);
		}

	}

}
