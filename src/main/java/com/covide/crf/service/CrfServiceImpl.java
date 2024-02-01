package com.covide.crf.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.io.DOMReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.covide.crf.dao.CrfDAO;
import com.covide.crf.dto.CRFGroupItem;
import com.covide.crf.dto.CRFGroupItemStd;
import com.covide.crf.dto.CRFItemValues;
import com.covide.crf.dto.CRFItemValuesStd;
import com.covide.crf.dto.CRFSections;
import com.covide.crf.dto.CRFSectionsStd;
import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfDateComparison;
import com.covide.crf.dto.CrfDescrpency;
import com.covide.crf.dto.CrfDescrpencyAudit;
import com.covide.crf.dto.CrfDescrpencyLog;
import com.covide.crf.dto.CrfEleCaliculation;
import com.covide.crf.dto.CrfGroup;
import com.covide.crf.dto.CrfGroupDataRows;
import com.covide.crf.dto.CrfGroupElement;
import com.covide.crf.dto.CrfGroupElementData;
import com.covide.crf.dto.CrfGroupElementValue;
import com.covide.crf.dto.CrfItems;
import com.covide.crf.dto.CrfItemsStd;
import com.covide.crf.dto.CrfMapplingTable;
import com.covide.crf.dto.CrfMapplingTableColumn;
import com.covide.crf.dto.CrfMapplingTableColumnMap;
import com.covide.crf.dto.CrfMetaData;
import com.covide.crf.dto.CrfMetaDataStd;
import com.covide.crf.dto.CrfRule;
import com.covide.crf.dto.CrfRuleWithOther;
import com.covide.crf.dto.CrfSection;
import com.covide.crf.dto.CrfSectionElement;
import com.covide.crf.dto.CrfSectionElementData;
import com.covide.crf.dto.CrfSectionElementDataAudit;
import com.covide.crf.dto.CrfSectionElementValue;
import com.covide.crf.dto.DataSet;
import com.covide.crf.dto.DataSetPhasewiseCrfs;
import com.covide.crf.xmlfiles.EleCaliculation;
import com.springmvc.dao.AcclimatizationDao;
import com.springmvc.dao.ExpermentalDesignDao;
import com.springmvc.dao.StudyDao;
import com.springmvc.dao.UserDao;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.ReviewLevel;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StdSubGroupObservationCrfsLog;
import com.springmvc.model.StudyAccessionCrfDescrpency;
import com.springmvc.model.StudyAccessionCrfDescrpencyLog;
import com.springmvc.model.StudyAccessionCrfSectionElementData;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.reports.RulesInfoTemp;

@Service("crfService")
@PropertySource(value = { "classpath:application.properties" })
public class CrfServiceImpl implements CrfService {

	@Autowired
	CrfDAO crfDAO;
	
	@Autowired
	StudyDao studyDao;
	@Autowired
	UserDao userdao;
	@Autowired
	private ExpermentalDesignDao expermentalDesignDao;
	@Autowired
	AcclimatizationDao acclimatizationDao;
	@Override
	public List<Crf> findAllCrfs() {
		// TODO Auto-generated method stub
		return crfDAO.findAllCrfs();
	}

	@Autowired
    private Environment environment;
	
	private Map<String, CrfSection> elementSections = null; // key-sectionName value-Section
	private Map<String, CrfSection> groupSections = null; // key-sectionName value-Section
	private Map<String, CrfGroup> groupsMap = null; // key-groupName value-CrfGroup
	private Set<String> secNames = null;
	private CrfExcelSheet exclData;
	private List<String> sectionNames;
	private List<String> groupNames;
	private boolean flag;
	@Override
	public Crf readCrfExcelFile(FileInputStream inputStream, String fileName) throws IOException {
		sectionNames = new ArrayList<>();
		groupNames = new ArrayList<>();
		flag = true;
		elementSections = new HashMap<>();
		groupSections = new HashMap<>(); 
		groupsMap = new HashMap<>();
		secNames = new HashSet<>();
		Crf crf = new Crf();
		Workbook guru99Workbook = null;
		// Find the file extension by splitting file name in substring and getting only
		// extension name
		String fileExtensionName = fileName.substring(fileName.indexOf("."));
		// Check condition if the file is xlsx file
		if (fileExtensionName.equals(".xlsx")) {
			// If it is xlsx file then create object of XSSFWorkbook class
			guru99Workbook = new XSSFWorkbook(inputStream);
		}
		// Check condition if the file is xls file
		else if (fileExtensionName.equals(".xls")) {
			// If it is xls file then create object of HSSFWorkbook class
			guru99Workbook = new HSSFWorkbook(inputStream);
		}else {
			return null;
		}

		// Read sheet inside the workbook by its name
			try {
				crf = readCrfSheet(crf, guru99Workbook.getSheet("CRF"));
//				Crf oldcrf = crfDAO.checkCrf(crf.getName());
//				if(oldcrf != null) {
//					crf.setMessage("duplicate");
//					return crf;
//				}
				
				checkSectionSheetData(guru99Workbook.getSheet("SECTION"));
				checkGroupSheetData(guru99Workbook.getSheet("GROUP"));
				checkSectionElementSheetData(guru99Workbook.getSheet("SECTION ELEMENTS"));
				checkGroupElementSheetData(guru99Workbook.getSheet("GROUP ELEMENT"));
				if(flag) {
					crf = readCrfSectionSheet(crf, guru99Workbook.getSheet("SECTION"));
					crf = readCrfGroupSheet(crf, guru99Workbook.getSheet("GROUP"));
					crf = readCrfSectionElements(crf, guru99Workbook.getSheet("SECTION ELEMENTS"));
					crf = readCrfGroupElements(crf, guru99Workbook.getSheet("GROUP ELEMENT"));
			
					List<CrfSection> secl = new ArrayList<>();
					for (String sc : secNames) {
						if (elementSections.get(sc) != null)
							secl.add(elementSections.get(sc));
						else {
			//				CrfSection sec  = groupSections.get(sc);
			//				CrfGroup g = sec.getGroup();
			//				for(CrfGroupElement e : g.getElement()) {
			//					System.out.println(e);
			//				}
							secl.add(groupSections.get(sc));
						}
					}
					Collections.sort(secl);
					crf.setSections(secl);
				}
				crf.setFlag(flag);
				crf.setExclData(exclData);
				return crf;			
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}

	private void checkSectionSheetData(Sheet sheet) {
		sectionNames = new ArrayList<>();
		List<String> order = new ArrayList<>();
		List<CrfSectionSheet> crfSheet = new ArrayList<>();
		crfSheet.add(new CrfSectionSheet());
		// Find number of rows in excel file
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		// Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount + 1; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				CrfSectionSheet exsheet = new CrfSectionSheet(i+1);
				// Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum(); j++) {
					// Print Excel data in console
					Cell cell = row.getCell(j);
					String s = "";
						boolean fg = false;
						if (cell == null) fg = true;
						switch (j + 1) {
						case 1:
							if(fg) {
								exsheet.setName("<font color='red'>Required Filed</font>");
								flag  = false;
							}else {
								s = getCellValue(cell, false);
								if(sectionNames.contains(s)) {
									exsheet.setName("<font color='red'>Duplicate Name</font>");
									flag  = false;
								}else {
									exsheet.setName(s);
									sectionNames.add(s);
								}
							}
							break;
						case 2:
							if(!fg)
								s = getCellValue(cell, false);
//							s = getCellValue(cell, false);
							if(!s.equals("")) exsheet.setDesc(s);
							break;
						case 3:
							if(!fg)
								s = getCellValue(cell, false);
//							s = getCellValue(cell, false);
							if(!s.equals(""))	exsheet.setHedding(s);
							break;
						case 4:
							if(!fg)
								s = getCellValue(cell, false);
//							s = getCellValue(cell, false);
							if(!s.equals(""))	exsheet.setSubHedding(s);
							break;
						case 5:
							try {
								if(!fg) {
									d = Double.parseDouble(getCellValue(cell, false));
									exsheet.setMaxRows(d.intValue()+"");
								}
							}catch (Exception e) {
								flag = false;
								exsheet.setMaxRows("<font color='red'>Required only number</font>");
							}
							break;
						case 6:
							try {
								if(!fg) {
									d = Double.parseDouble(getCellValue(cell, false));
									exsheet.setMaxColumns(d.intValue()+"");
								}
							}catch (Exception e) {
								flag = false;
								exsheet.setMaxColumns("<font color='red'>Required only number</font>");
							}
							break;
						case 7:
							try {
								if(!fg) {
									d = Double.parseDouble(getCellValue(cell, false));
									if(order.contains(d.intValue()+"")) {
										flag = false;
										exsheet.setOrder("<font color='red'>Duplicate Order</font>");
									}else
										exsheet.setOrder(d.intValue()+"");									
								}else {
									flag = false;
									exsheet.setOrder("<font color='red'>Required Filed</font>");
								}

							}catch (Exception e) {
								flag = false;
								exsheet.setOrder("<font color='red'>Required only number</font>");
							}
							
							break;
						case 8:
							try {
								s = getCellValue(cell, false);
							}catch (Exception e) {
								exsheet.setGender("<font color='red'>Required Filed</font>");
								flag  = false;
							}
//							s = getCellValue(cell, false);
							if(s.equals("")) {
								exsheet.setGender("<font color='red'>Required Filed</font>");
								flag= false;
							}else if(s.equals("ALL") || s.equals("MALE") ||	s.equals("FEMALE")) {
								exsheet.setGender(s);
							}else {
								flag= false;
								exsheet.setGender("<font color='red'>Invalied Data</font>");
							}
							break;
						case 9:
							if(fg) {
								exsheet.setRole("<font color='red'>Required Filed</font>");
								flag  = false;
							}else
								s = getCellValue(cell, false);
							if(s.equals("")) {
								exsheet.setRole("<font color='red'>Required Filed</font>");
								flag= false;
							}else if(s.equals("ALL")) {
								exsheet.setRole(s);
							}else {
								flag= false;
								exsheet.setRole("<font color='red'>Invalied Data</font>");
							}
							break;
						case 10:
							String v = null;
							if(fg) {
								exsheet.setContainsGroup("<font color='red'>Required Filed</font>");
								flag  = false;
							}else
								v = cell.getStringCellValue().trim();
							if(v.equals("")) {
								exsheet.setContainsGroup("<font color='red'>Required Filed</font>");
								flag= false;
							}else if(v.equals("NO") || v.equals("YES")) {
								exsheet.setContainsGroup(v);
							}else {
								flag= false;
								exsheet.setContainsGroup("<font color='red'>Invalied Data</font>");
							}
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						default:
							break;
						}
//					}
				}
				crfSheet.add(exsheet);
			}
		}
		exclData.setCrfSectinSheet(crfSheet);
	}	

	private void checkGroupSheetData(Sheet sheet) {
		groupNames = new ArrayList<>();
		List<CrfGroupSheet> crfGroupSheet = new ArrayList<>();
		crfGroupSheet.add(new CrfGroupSheet());
		try {
			// Find number of rows in excel file
			int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
			// Create a loop over all the rows of excel file to read it
			for (int i = 1; i < rowCount + 1; i++) {
				Row row = sheet.getRow(i);
				if (row != null) {
					CrfGroupSheet groupSheet = new CrfGroupSheet(i+1);
					// Create a loop to print cell values in a row
					for (int j = 0; j < row.getLastCellNum(); j++) {
						// Print Excel data in console
						String s = "";
						Cell cell = row.getCell(j);
						boolean fg = false;
						if (cell == null) fg = true;
						switch (j + 1) {
							case 1:
								if(fg) {
									flag = false;
									groupSheet.setName("<font color='red'>Required Field</font>");
								}else {
									s = getCellValue(cell, false);
									if(s.equals("")) {
										flag = false;
										groupSheet.setName("<font color='red'>Required Field</font>");
									}else if(groupNames.contains(s)) {
										flag = false;
										groupSheet.setName("<font color='red'>Duplicate Name</font>");
									}else {
										groupSheet.setName(s);
										groupNames.add(s);
									}
								}
								break;
							case 2:
								if(!fg)
									s = getCellValue(cell, false);
								if(!s.equals(""))	groupSheet.setDesc(s);
								break;
							case 3:
								if(!fg)
									s = getCellValue(cell, false);
								if(!s.equals(""))	groupSheet.setHedding(s);
								break;
							case 4:
								if(!fg)
									s = getCellValue(cell, false);
								if(!s.equals(""))	groupSheet.setSubHedding(s);
								break;
							case 5:
								try {
									if(fg) {
										flag = false;
										groupSheet.setMaxRows("<font color='red'>Required Field</font>");
									}else {
										d = Double.parseDouble(getCellValue(cell, false));
										if(d.intValue() < 1) {
											flag = false;
											groupSheet.setMaxRows("<font color='red'>Value Must grater then 0</font>");
										}else
											groupSheet.setMaxRows(d.intValue()+"");
									}
								}catch (Exception e) {
									flag = false;
									groupSheet.setMaxRows("<font color='red'>Required only number</font>");
								}
								break;
							case 6:
								try {
									if(fg) {
										flag = false;
										groupSheet.setMaxRows("<font color='red'>Required Field</font>");
									}else {
										d = Double.parseDouble(getCellValue(cell, false));
										if(d.intValue() < 1) {
											flag = false;
											groupSheet.setMaxRows("<font color='red'>Value Must grater then 0</font>");
										}else
											groupSheet.setMaxRows(d.intValue()+"");
									}
								}catch (Exception e) {
									flag = false;
									groupSheet.setMaxRows("<font color='red'>Required only number</font>");
								}
								break;
							case 7:
								try {
									if(fg) {
										flag = false;
										groupSheet.setMaxColumns("<font color='red'>Required Field</font>");
									}else {
										d = Double.parseDouble(getCellValue(cell, false));
										if(d.intValue() < 1) {
											flag = false;
											groupSheet.setMaxColumns("<font color='red'>Value Must grater then 0</font>");
										}else
											groupSheet.setMaxColumns(d.intValue()+"");
									}
								}catch (Exception e) {
									flag = false;
									groupSheet.setMaxColumns("<font color='red'>Required only number</font>");
								}
								break;
							case 8:
								if(fg) {
									flag = false;
									groupSheet.setSectionName("<font color='red'>Required Field</font>");
								}else {
									s = getCellValue(cell, false);
									if(s.trim().equals("")) {
										flag = false;
										groupSheet.setSectionName("<font color='red'>Required Field</font>");
									}else {
										if(sectionNames.contains(s)) {
											groupSheet.setSectionName(s);
										}else {
											flag = false;
											groupSheet.setSectionName("<font color='red'>Section Name Not Avilable</font>");
										}										
									}

								}
								break;
							default:
								break;
						}
					}
					crfGroupSheet.add(groupSheet);
				}
			}
						
		}catch (Exception e) {
			e.printStackTrace();
		}
		exclData.setCrfGroupSheet(crfGroupSheet);
	}

	private void checkSectionElementSheetData(Sheet sheet) {
		List<String> names = new ArrayList<>();
		List<CrfSectionElementSheet> eles = new ArrayList<>();
		eles.add(new CrfSectionElementSheet());
		// Find number of rows in excel file
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		// Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount + 1; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				CrfSectionElementSheet ele = new CrfSectionElementSheet(i+1);
				// Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum(); j++) {
					// Print Excel data in console
					String s = "";
					Cell cell = row.getCell(j);
//					if (cell != null) {
						switch (j + 1) {
						case 1:
							s = getCellValue(cell, false);
							if(s.equals("")) flag = true;
							else if(names.contains(s)) {
								flag = false;
								ele.setName("<font color='red'>Duplicate Name</font>");
							}else {
								ele.setName(s);
								names.add(s);
							}
							break;
						case 2:
							s = getCellValue(cell, false);
							if(!s.equals(""))	ele.setLeftDesc(s);
							break;
						case 3:
							s = getCellValue(cell, false);
							if(!s.equals(""))	ele.setRightDesc(s);
							break;
						case 4:
							s = getCellValue(cell, false);
							if(!s.equals(""))	ele.setMiddeDesc(s);
							break;
						case 5:
							s = getCellValue(cell, false);
							if(!s.equals(""))	ele.setTotalDesc(s);
							break;
						case 6:
							try {
								d = Double.parseDouble(getCellValue(cell, false));
								if(d.intValue() < 1) {
									flag = false;
									ele.setColumnNo("<font color='red'>Value Must grater then 0</font>");
								}else
									ele.setColumnNo(d.intValue()+"");
							}catch (Exception e) {
								flag = false;
								ele.setColumnNo("<font color='red'>Required only number</font>");
							}
							break;
						case 7:
							try {
								d = Double.parseDouble(getCellValue(cell, false));
								if(d.intValue() < 1) {
									flag = false;
									ele.setRowNo("<font color='red'>Value Must grater then 0</font>");
								}else
									ele.setRowNo(d.intValue()+"");
							}catch (Exception e) {
								flag = false;
								ele.setRowNo("<font color='red'>Required only number</font>");
							}
							break;
						case 8:
							s = getCellValue(cell, false);
							if(!s.equals(""))	ele.setTopDesc(s);
							break;
						case 9:
							s = getCellValue(cell, false);
							if(!s.equals(""))	ele.setBottomDesc(s);
							break;
						case 10:
							s = getCellValue(cell, false);
							if(s.equals("")) flag= false;
							else if(s.equals("text") || s.equals("testArea") ||	s.equals("radio")||
									s.equals("checkBox") || s.equals("select") ||	s.equals("date")||
									s.equals("dateAndTime") || s.equals("file") || s.equals("non") || 
									s.equals("selectTable") || s.equals("staticData")) {
								ele.setType(s);
							}else {
								flag= false;
								ele.setType("<font color='red'>Invalied Data</font>");
							}
							break;
						case 11:
							s = getCellValue(cell, false);
							if(!s.equals(""))	ele.setResponseType(s);
							break;
						case 12:
							s = getCellValue(cell, false);
							if(!s.equals("")) {
								if(s.equals("vertical"))
									ele.setDisplay(s);
								else {
									flag = false;
									ele.setDisplay("<font color='red'>Invalied Data</font>");
								}
							}
							break;
						case 13:
							s = getCellValue(cell, false);
							if(!s.equals(""))	ele.setValues(s);
							break;
						case 14:
							s = getCellValue(cell, false);
							if(!s.equals(""))	ele.setPattren(s);
							break;
						case 15:
							try {
								d = Double.parseDouble(getCellValue(cell, false));
								if (d.intValue() == 1)
									ele.setRequired(d.intValue()+"");
								else
									ele.setRequired("0");
							}catch (Exception e) {
								flag= false;
								ele.setRequired("<font color='red'>Required 0 or 1 only</font>");
							}
							break;
						case 16:
							s = getCellValue(cell, false);
							if(s.equals("")) flag= false;
							else if(sectionNames.contains(s)) {
								ele.setSectionName(s);
							}else {
								flag= false;
								ele.setSectionName("<font color='red'>Invalied Data</font>");
							}
							break;
						case 17:
							s = getCellValue(cell, false);
							if(s.equals("")) flag= false;
							else if(s.equals("Number") || s.equals("String")) {
								ele.setDataType(s);
							}else {
								flag= false;
								ele.setDataType("<font color='red'>Invalied Data</font>");
							}
							break;
						case 18:
							s = getCellValue(cell, false);
							if(!s.equals(""))	ele.setTypeOfTime(s);
							break;
						case 19:
							s = getCellValue(cell, false);
							if(!s.equals(""))	
								ele.setFormula(s);
							break;
						default:
							break;
						}
//					}
				}
				eles.add(ele);
			}
		}
		exclData.setCrfSectinElementSheet(eles);
	}
	private String getCellValue(Cell cell, boolean flag) {
		String value = "";
		try {
			switch (cell.getCellType()) {
				case Cell.CELL_TYPE_BOOLEAN:
	//	            System.out.print(cell.getBooleanCellValue());
		            value = cell.getBooleanCellValue()+"";
		            break;
		        case Cell.CELL_TYPE_STRING:
	//	            System.out.print(cell.getRichStringCellValue().getString());
		            value = cell.getRichStringCellValue().getString().trim();
		            break;
		        case Cell.CELL_TYPE_NUMERIC:
	//	            if (DateUtil.isCellDateFormatted(cell)) {
	//	                System.out.print(cell.getDateCellValue());
	//	                value = cell.getDateCellValue()+"";
	//	            } else {
	//	                System.out.print(cell.getNumericCellValue());
		                value = cell.getNumericCellValue()+"";
	//	                System.out.println(Long.parseLong(value));
		                if(flag && value != null && !value.equals(""))
							value = value.substring(0, value.indexOf('.'));
	//	            }
		            break;
		        case Cell.CELL_TYPE_FORMULA:
	//	            System.out.print(cell.getCellFormula());
		            value = cell.getNumericCellValue()+"";
	//	            System.out.println(Long.parseLong(value));
		            if(flag && value != null && !value.equals(""))
						value = value.substring(0, value.indexOf('.'));
		            break;
		        default:
		            System.out.print("");
			}
			value = value.trim();
			System.out.println(value);
			if(value != null && !value.equals("NAN"))
				return value;
			else return "";
		}
//		catch (NullPointerException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return "";
//		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}

	}
	private void checkGroupElementSheetData(Sheet sheet) {
		List<String> names = new ArrayList<>();
		List<CrfGroupElementSheet> crfgroupElementSheet = new ArrayList<>();
		crfgroupElementSheet.add(new CrfGroupElementSheet()); 
		try {
			List<CrfGroupElement> eles = new ArrayList<CrfGroupElement>();
			// Find number of rows in excel file
			int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
			// Create a loop over all the rows of excel file to read it
			for (int i = 1; i < rowCount + 1; i++) {
				Row row = sheet.getRow(i);
				String secName = "";
				if (row != null) {
					CrfGroupElementSheet ele = new CrfGroupElementSheet(i+1);
					// Create a loop to print cell values in a row
					for (int j = 0; j < row.getLastCellNum(); j++) {
						// Print Excel data in console
						Cell cell = row.getCell(j);
//						if (cell != null) {
							String s = "";
							switch (j + 1) {
							case 1:
								s = getCellValue(cell, false);
								if(s.equals("")) flag = false;
								else if(names.contains(s)) {
									flag = false;
									ele.setName("<font color='red'>Duplicate Value</font>");
								}else {
									names.add(s);
									ele.setName(s);
								}
								break;
							case 2:
								s = getCellValue(cell, false);
								if(!s.equals("")) ele.setDesc(s);
								break;
							case 3:
								try {
									d = Double.parseDouble(getCellValue(cell, false));
									if(d.intValue() < 1) {
										flag = false;
										ele.setColumnNo("<font color='red'>Value Must grater then 0</font>");
									}else
										ele.setColumnNo(d.intValue()+"");
								}catch (Exception e) {
									flag = false;
									ele.setColumnNo("<font color='red'>Required only number</font>");
								}
								break;
							case 4:
								try {
									d = Double.parseDouble(getCellValue(cell, false));
									if(d.intValue() < 1) {
										flag = false;
										ele.setRowNo("<font color='red'>Value Must grater then 0</font>");
									}else
										ele.setRowNo(d.intValue()+"");
								}catch (Exception e) {
									flag = false;
									ele.setRowNo("<font color='red'>Required only number</font>");
								}
								break;
							case 5:
								s = getCellValue(cell, false);
								if(s.equals("")) flag= false;
								else if(s.equals("text") || s.equals("testArea") ||	s.equals("radio")||
										s.equals("checkBox") || s.equals("select") ||	s.equals("date")||
										s.equals("dateAndTime") || s.equals("file") || s.equals("non")) {
									ele.setType(s);
								}else {
									flag= false;
									ele.setType("<font color='red'>Invalied Data</font>");
								}
								break;
							case 6:
								s = getCellValue(cell, false);
								if(!s.equals(""))	ele.setResponseType(s);
								break;
							case 7:
								s = getCellValue(cell, false);
								if(!s.equals("")) {
									if(s.equals("vertical"))
										ele.setDisplay(s);
									else {
										flag = false;
										ele.setDisplay("<font color='red'>Invalied Data</font>");
									}
								}
								
							case 8:
								s = getCellValue(cell, false);
								if(!s.equals(""))	ele.setValues(s);
								break;
							case 9:
								s = getCellValue(cell, false);
								if(!s.equals(""))	ele.setPattren(s);
								break;
							case 10:
								try {
									d = Double.parseDouble(getCellValue(cell, false));
									if (d.intValue() == 1)
										ele.setRequired(d.intValue()+"");
									else
										ele.setRequired("0");
								}catch (Exception e) {
									flag= false;
									ele.setRequired("<font color='red'>Required 0 or 1 only</font>");
								}
								break;
							case 11:
								s = getCellValue(cell, false);
								if(s.equals("")) flag= false;
								else if(sectionNames.contains(s)) {
									ele.setSectionName(s);
								}else {
									flag= false;
									ele.setSectionName("<font color='red'>Invalied Data</font>");
								}
								break;
							case 12:
								s = getCellValue(cell, false);
								if(s.equals("")) flag= false;
								else if(groupNames.contains(s)) {
									ele.setGroupName(s);
								}else {
									flag= false;
									ele.setGroupName("<font color='red'>Invalied Data</font>");
								}
								break;
							case 13:
								s = getCellValue(cell, false);
								if(s.equals("")) flag= false;
								else if(s.equals("Number") || s.equals("String")) {
									ele.setDataType(s);
								}else {
									flag= false;
									ele.setDataType("<font color='red'>Invalied Data</font>");
								}
								break;
							case 14:
								s = getCellValue(cell, false);
								ele.setFormula(s);
								break;
							default:
								break;
							}
//						}
					}
					crfgroupElementSheet.add(ele);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		exclData.setCrfgroupElementSheet(crfgroupElementSheet);
	}

	private Crf readCrfSheet(Crf crf, Sheet sheet) {
		flag = true;
		exclData = new CrfExcelSheet();
		// Find number of rows in excel file
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		// Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount + 1; i++) {
			if(i<2) {
				Row row = sheet.getRow(i);
				// Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum(); j++) {
					// Print Excel data in console
					Cell cell = row.getCell(j);
					if (cell != null) {
						switch (j + 1) {
						case 1:
							crf.setName(getCellValue(cell, false));
							break;
						case 2:
							crf.setTitle(getCellValue(cell, false));
							break;
						case 3:
							crf.setType(getCellValue(cell, false));
							break;
						case 4:
							crf.setGender(getCellValue(cell, false));
							break;
						case 5:
							crf.setVersion(getCellValue(cell, false));
							break;
						case 6:
							crf.setCrfCode(getCellValue(cell, false));
							break;
//						case 7:
//							crf.setConfigurationFor(getCellValue(cell, false));
//							break;
						default:
							break;
						}
					}
				}
				System.out.println();
			}
		}
		return crf;
	}

	private Double d = null;

	private Crf readCrfSectionSheet(Crf crf, Sheet sheet) {
		secNames = new HashSet<>();
		List<CrfSection> sections = null;
		if (crf.getSections() == null)
			sections = new ArrayList<CrfSection>();
		else
			sections = crf.getSections();
		// Find number of rows in excel file
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		// Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount + 1; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				CrfSection section = new CrfSection();
				section.setCrf(crf);
				// Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum(); j++) {
					// Print Excel data in console
					Cell cell = row.getCell(j);
					if (cell != null) {
						switch (j + 1) {
						case 1:
							section.setName(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 2:
							section.setTitle(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 3:
							section.setHedding(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 4:
							section.setSubHedding(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 5:
							d = Double.parseDouble(getCellValue(cell, false));
							section.setMaxRows(d.intValue());
							System.out.print(d + "\t");
							break;
						case 6:
							d = Double.parseDouble(getCellValue(cell, false));
							section.setMaxColumns(d.intValue());
							System.out.print(d + "\t");
							break;
						case 7:
							d = Double.parseDouble(getCellValue(cell, false));
							section.setOrder(d.intValue());
							System.out.print(d + "\t");
							break;
						case 8:
							section.setGender(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 9:
							section.setRoles(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 10:
							if (getCellValue(cell, false).trim().equals("YES")) {
								section.setContainsGroup(true);
								groupSections.put(section.getName(), section);
							} else {
								elementSections.put(section.getName(), section);
							}
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						default:
							break;
						}
					}
				}
				section.setElement(new ArrayList<>());
				System.out.println();
				sections.add(section);
				secNames.add(section.getName());
			}
		}
		crf.setSections(sections);
		return crf;
	}

	private Crf readCrfGroupSheet(Crf crf, Sheet sheet) {
		List<CrfGroup> groups = new ArrayList<CrfGroup>();
		// Find number of rows in excel file
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		// Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount + 1; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				CrfGroup group = new CrfGroup();
				group.setCrf(crf);
				// Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum(); j++) {
					// Print Excel data in console
					Cell cell = row.getCell(j);
					if (cell != null) {
						switch (j + 1) {
						case 1:
							group.setName(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 2:
							group.setTitle(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 3:
							group.setHedding(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 4:
							group.setSubHedding(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 5:
							d = Double.parseDouble(getCellValue(cell, false));
							group.setMinRows(d.intValue());
							System.out.print(d + "\t");
							break;
						case 6:
							d = Double.parseDouble(getCellValue(cell, false));
							group.setMaxRows(d.intValue());
							System.out.print(d + "\t");
							break;
						case 7:
							d = Double.parseDouble(getCellValue(cell, false));
							group.setMaxColumns(d.intValue());
							System.out.print(d + "\t");
							break;
						case 8:
							String secname = cell.getStringCellValue().trim();
							CrfSection sec = groupSections.get(secname);
							sec.setGroup(group);
							groupSections.put(sec.getName(), sec);
							break;
						default:
							break;
						}
					}
				}
				System.out.println();
				groups.add(group);
				groupsMap.put(group.getName(), group);
			}
		}
		return crf;
	}

	private Crf readCrfSectionElements(Crf crf, Sheet sheet) {
		List<CrfSectionElement> eles = new ArrayList<CrfSectionElement>();
		// Find number of rows in excel file
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		// Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount + 1; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				CrfSectionElement ele = new CrfSectionElement();
				ele.setCrf(crf);
				eles.add(ele);
				// Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum(); j++) {
					// Print Excel data in console
					Cell cell = row.getCell(j);
					if (cell != null) {
						switch (j + 1) {
						case 1:
							ele.setName(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 2:
							ele.setLeftDesc(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 3:
							ele.setRigtDesc(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 4:
							ele.setMiddeDesc(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 5:
							ele.setTotalDesc(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 6:
							d = Double.parseDouble(getCellValue(cell, false));
							ele.setColumnNo(d.intValue());
							System.out.print(d + "\t");
							break;
						case 7:
							d = Double.parseDouble(getCellValue(cell, false));
							ele.setRowNo(d.intValue());
							System.out.print(d + "\t");
							break;
						case 8:
							if (getCellValue(cell, false) != null) {
								ele.setTopDesc(getCellValue(cell, false));
								System.out.print(getCellValue(cell, false) + "\t");
							}
							break;
						case 9:
							if (getCellValue(cell, false) != null) {
								ele.setBottemDesc(getCellValue(cell, false));
								System.out.print(getCellValue(cell, false) + "\t");
							}
							break;
						case 10:
							ele.setType(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 11:
							if (getCellValue(cell, false) != null) {
								ele.setResponceType(getCellValue(cell, false));
								String[] sr = ele.getResponceType().split("##");
								List<CrfSectionElementValue> list = new ArrayList<>();
								for (int k = 0; k < sr.length; k++) {
									CrfSectionElementValue v = new CrfSectionElementValue();
									v.setSectionElement(ele);
									v.setValue(sr[k]);
									list.add(v);
								}
								ele.setElementValues(list);
								System.out.print(getCellValue(cell, false) + "\t");
							}
							break;
						case 12:
							if (getCellValue(cell, false) != null && cell.getStringCellValue().equals("vertical")) {
								ele.setDisplay("vertical");
								System.out.print(getCellValue(cell, false) + "\t");
							}
							break;
						case 13:
							if (getCellValue(cell, false) != null) {
								ele.setValues(getCellValue(cell, false));
								System.out.print(getCellValue(cell, false) + "\t");
							}
							break;
						case 14:
							if (getCellValue(cell, false) != null) {
								ele.setPattren(getCellValue(cell, false));
							}
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 15:
							d = Double.parseDouble(getCellValue(cell, false));
							if (d.intValue() == 1)
								ele.setRequired(true);
							System.out.print(d + "\t");
							break;
						case 16:
							String secname = cell.getStringCellValue().trim();
							CrfSection sec = elementSections.get(secname);
							List<CrfSectionElement> element = sec.getElement();
							if (element == null)
								element = new ArrayList<>();
							element.add(ele);
							sec.setElement(element);
							elementSections.put(sec.getName(), sec);
							break;
						case 17:
							String dataType = cell.getStringCellValue().trim();
							ele.setDataType(dataType);
							break;
						case 18:
							ele.setTypeOfTime(cell.getStringCellValue().trim());
							break;
						case 19:
							ele.setFormula(cell.getStringCellValue().trim());
							break;
						default:
							break;
						}
					}
				}
				System.out.println();
			}
		}
		return crf;
	}

	private Crf readCrfGroupElements(Crf crf, Sheet sheet) {
		List<CrfGroupElement> eles = new ArrayList<CrfGroupElement>();
		// Find number of rows in excel file
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		// Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount + 1; i++) {
			Row row = sheet.getRow(i);
			String secName = "";
			if (row != null) {
				CrfGroupElement ele = new CrfGroupElement();
				ele.setCrf(crf);
				eles.add(ele);
				// Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum(); j++) {
					// Print Excel data in console
					Cell cell = row.getCell(j);
					if (cell != null) {
						switch (j + 1) {
						case 1:
							ele.setName(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 2:
							ele.setTitle(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 3:
							d = Double.parseDouble(getCellValue(cell, false));
							ele.setColumnNo(d.intValue());
							System.out.print(d + "\t");
							break;
						case 4:
							d = Double.parseDouble(getCellValue(cell, false));
							ele.setRowNo(d.intValue());
							break;
						case 5:
							ele.setType(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 6:
							if (getCellValue(cell, false) != null) {
								ele.setResponceType(getCellValue(cell, false));
								String[] sr = ele.getResponceType().split("##");
								List<CrfGroupElementValue> list = new ArrayList<>();
								for (int k = 0; k < sr.length; k++) {
									CrfGroupElementValue v = new CrfGroupElementValue();
									v.setGroupElement(ele);
									v.setValue(sr[k]);
									list.add(v);
								}
								ele.setElementValues(list);
								System.out.print(getCellValue(cell, false) + "\t");
							}
							break;
						case 7:
							if (getCellValue(cell, false) != null && cell.getStringCellValue().equals("vertical")) {
								ele.setDisplay("vertical");
								System.out.print(getCellValue(cell, false) + "\t");
							}
						case 8:
							if (getCellValue(cell, false) != null) {
								ele.setValues(getCellValue(cell, false));
								System.out.print(getCellValue(cell, false) + "\t");
							}
							break;
						case 9:
							// DISPLAY VALUES PATTREN REQUIRED SECTION NAME GROUP NAME
							if (getCellValue(cell, false) != null) {
								ele.setPattren(getCellValue(cell, false));
							}
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 10:
							d = Double.parseDouble(getCellValue(cell, false));
							if (d.intValue() == 1)
								ele.setRequired(true);
							System.out.print(d + "\t");
							break;
						case 11:
							secName = cell.getStringCellValue().trim();
							System.out.print(secName + "\t");
							break;
						case 12:
							String gname = cell.getStringCellValue().trim();
							System.out.print(gname + "\t");
							CrfSection sec = groupSections.get(secName);
							CrfGroup g = sec.getGroup();
							CrfGroup g1 = groupsMap.get(gname);
							if (g.getName().equals(g1.getName())) {
								List<CrfGroupElement> element = g.getElement();
								if (element == null)
									element = new ArrayList<>();
								element.add(ele);
								g1.setElement(element);
								groupsMap.put(g.getName(), g1);
							}
							break;
						case 13:
							String dataType = cell.getStringCellValue().trim();
							ele.setDataType(dataType);
							break;
						case 14:
							String formula = cell.getStringCellValue().trim();
							ele.setFormula(formula);
							break;
						default:
							break;
						}
					}
				}
				System.out.println();
			}
		}
		return crf;
	}

	@Override
	public void saveCrf(Crf crf) {
		// TODO Auto-generated method stub
		crfDAO.saveCrf(crf);
	}

	@Override
	public Crf getCrf(Long crfId) {
		// TODO Auto-generated method stub
		return crfDAO.getCrf(crfId);
	}

	@Override
	public Crf changeCrfStatus(Long crfId , String username) {
		// TODO Auto-generated method stub
		try {
		Crf crf = crfDAO.getCrf(crfId);
		if(crf.isActive())  crf.setActive(false);
		else crf.setActive(true);
		crf.setUpdatedBy(username);
		crf.setUpdatedOn(new Date());
		crfDAO.updateCrf(crf);
		return crf;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	
	@Override
	public void copyLibCrfToStudy(StudyMaster sm, String username, List<Long> crfIds, Map<Long, String> type,  
			Map<Long, String> days, Long subGroupId, List<String> obDeptList, Map<Long, String> window, Map<Long, Integer> windowPeriod) {
		SubGroupInfo subGroupInfo = studyDao.stdGroupInfo(subGroupId);
//		Map<Long, Long> obdeptMap = new HashMap<>();
//		List<Long> deptIds = new ArrayList<>();
//		if(obDeptList != null && obDeptList.size() >0) {
//			for(String st : obDeptList) {
//				String[] temp = st.split("\\##");
//				obdeptMap.put(Long.parseLong(temp[0]), Long.parseLong(temp[1]));
//				deptIds.add(Long.parseLong(temp[1]));
//			}
//		}
//		Map<Long, DepartmentMaster> depts = expermentalDesignDao.separtmentMasters(deptIds);
		
		StudyDesignStatus sds = null;//expermentalDesignDao.getStudyDesignStatusRecord(sm.getId());
		Map<Long, StdSubGroupObservationCrfs> sdedMap = new HashMap<>();
		List<StdSubGroupObservationCrfs> stdcrfs = expermentalDesignDao.stdSubGroupObservationCrfs(crfIds, sm.getId(), subGroupId);
		stdcrfs.forEach((obj) -> {
			sdedMap.put(obj.getCrf().getId(), obj);
		});
		
		List<StdSubGroupObservationCrfs> updateSdedList = new ArrayList<>();
		List<StdSubGroupObservationCrfsLog> logs = new ArrayList<>();
		List<Crf> crfsList = acclimatizationDao.getCrfRecordsList(crfIds);
		List<StdSubGroupObservationCrfs> stdcrfssave = new ArrayList<>();
		for (Crf crf : crfsList) {
			StdSubGroupObservationCrfs stdCrf = sdedMap.get(crf.getId());
			if(stdCrf == null) {
				stdCrf = new StdSubGroupObservationCrfs();
				stdCrf.setSubGroupInfo(subGroupInfo);
				stdCrf.setCrf(crf);
				stdCrf.setStudy(sm);
				stdCrf.setObservationName(crf.getObservationName());
				stdCrf.setObservationDesc(crf.getObservationDesc());
				stdCrf.setDayType(type.get(crf.getId()));
				stdCrf.setDays(""+days.get(crf.getId()));
				stdCrf.setWindowSign(window.get(crf.getId()));
				stdCrf.setWindow(windowPeriod.get(crf.getId()));
				stdCrf.setCreatedBy(username);
//				DepartmentMaster dept =  expermentalDesignDao.getDepartmentMasterRecord(obdeptMap.get(no));
//				stdCrf.setDeptId(dept);
				try {
					if(crf.getSections() != null && crf.getSections().size() >0) {
						System.out.println(crf.getSections().size());
						stdCrf.setCrfConfig(true);
					}	
				}catch (Exception e) {
					e.printStackTrace();
				}
				if(crf.getTemplete() != null)
					stdCrf.setActive(true);
				stdcrfssave.add(stdCrf);
			}else {
				sdedMap.remove(crf.getId());
				
				if(!type.get(crf.getId()).equals(stdCrf.getDayType()) 
						|| !days.get(crf.getId()).equals(stdCrf.getDays()) || windowPeriod.get(crf.getId()) != stdCrf.getWindow()
						|| !window.get(crf.getId()).equals(stdCrf.getWindowSign())) {
					StdSubGroupObservationCrfsLog log = new StdSubGroupObservationCrfsLog();
					try {
						BeanUtils.copyProperties(log, stdCrf);
					} catch (IllegalAccessException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					stdCrf.setDayType(type.get(crf.getId()));
					stdCrf.setDays(days.get(crf.getId()));
					stdCrf.setWindow(windowPeriod.get(crf.getId()));
					stdCrf.setWindowSign(window.get(crf.getId()));
					stdCrf.setUpdatedBy(username);
					stdCrf.setUpdatedOn(new Date());
					stdCrf.setActive(true);
					updateSdedList.add(stdCrf);
					log.setSsgoc(stdCrf);
					logs.add(log);
					
				}
				
			}
		}
		
		sdedMap.forEach((k ,v) -> {
			v.setActive(false);
			v.setUpdatedBy(username);
			v.setUpdatedOn(new Date());
			updateSdedList.add(v);
		});
		
//		String result = expermentalDesignDao.saveorUpdateSubjectDataEntryDetails(updateSdedList, stdcrfssave);
//		crfDAO.updateStdSubGroupObservationCrfsList(stdcrfsUpdate,sgblog);
		crfDAO.saveStdSubGroupObservationCrfsList(updateSdedList, stdcrfssave, sm, username, sds, logs);
		
	}


	private List<CrfDateComparison> copyCrfRuleDate(Crf crf, Crf stdCrf, StudyMaster sm, String username,
			Map<Long, CrfSectionElement> se, Map<Long, CrfGroupElement> sge, Map<Long, Crf> c) {
		List<CrfDateComparison> rules = crfDAO.CrfDateComparisonAndSubElements(crf);
		List<CrfDateComparison> stdRules = new ArrayList<>();
		for(CrfDateComparison r : rules) {
			boolean flag = true;
			CrfDateComparison sr = new CrfDateComparison();
//			sr.setSm(sm);
			sr.setName(r.getName());
			sr.setDescreption(r.getDescreption());
			System.out.println(stdCrf.getId());
			sr.setSourcrf(stdCrf);

			if(r.getSecEle() != null) {
				if(se.containsKey(r.getSecEle().getId())) {
					sr.setSecEle(se.get(r.getSecEle().getId()));
				}
			}
			if(r.getGroupEle() != null) {
				if(sge.containsKey(r.getGroupEle().getId())) {
					sr.setGroupEle(sge.get(r.getGroupEle().getId()));
				}
			}
			sr.setRowNo(r.getRowNo());
			sr.setMessage(r.getMessage());
			sr.setCompare(r.getCompare());
			sr.setStatus(r.getStatus());
			
			if(c.containsKey(r.getCrf1().getId())) {
				sr.setCrf1(c.get(r.getCrf1().getId()));
			}else flag = false;
			if(r.getSecEle1() != null) {
				if(se.containsKey(r.getSecEle1().getId())) {
					sr.setSecEle1(se.get(r.getSecEle1().getId()));
				}
			}
			if(r.getGroupEle1() != null) {
				if(sge.containsKey(r.getGroupEle1().getId())) {
					sr.setGroupEle1(sge.get(r.getGroupEle1().getId()));
				}
			}
			sr.setRowNo1(r.getRowNo1());

			if(c.containsKey(r.getCrf2().getId())) {
				sr.setCrf2(c.get(r.getCrf2().getId()));
			}else flag = false;
			if(r.getSecEle2() != null) {
				if(se.containsKey(r.getSecEle2().getId())) {
					sr.setSecEle2(se.get(r.getSecEle2().getId()));
				}
			}
			if(r.getGroupEle2() != null) {
				if(sge.containsKey(r.getGroupEle2().getId())) {
					sr.setGroupEle2(sge.get(r.getGroupEle2().getId()));
				}
			}
			sr.setRowNo2(r.getRowNo2());
			
			if(c.containsKey(r.getCrf3().getId())) {
				sr.setCrf3(c.get(r.getCrf3().getId()));
			}else flag = false;
			if(r.getSecEle3() != null) {
				if(se.containsKey(r.getSecEle3().getId())) {
					sr.setSecEle3(se.get(r.getSecEle3().getId()));
				}
			}
			if(r.getGroupEle3() != null) {
				if(sge.containsKey(r.getGroupEle3().getId())) {
					sr.setGroupEle3(sge.get(r.getGroupEle3().getId()));
				}
			}
			sr.setRowNo3(r.getRowNo3());
			
			if(c.containsKey(r.getCrf4().getId())) {
				sr.setCrf4(c.get(r.getCrf4().getId()));
			}else flag = false;
			if(r.getSecEle4() != null) {
				if(se.containsKey(r.getSecEle4().getId())) {
					sr.setSecEle4(se.get(r.getSecEle4().getId()));
				}
			}
			if(r.getGroupEle4() != null) {
				if(sge.containsKey(r.getGroupEle4().getId())) {
					sr.setGroupEle4(sge.get(r.getGroupEle4().getId()));
				}
			}
			sr.setRowNo4(r.getRowNo4());
			if(flag)	stdRules.add(sr);
		}
		return stdRules;
	}
	
	private List<CrfRule> copyCrfRule(Crf crf, Crf stdCrf, StudyMaster sm, String username,
			Map<Long, CrfSectionElement> se, Map<Long, CrfGroupElement> sge) {
		List<CrfRule> rules = crfDAO.crfRuleWithCrfAndSubElements(crf);
		List<CrfRule> stdRules = new ArrayList<>();
		for(CrfRule rule : rules) {
			CrfRule sr = new CrfRule();
//			sr.setStudy(sm);
//			sr.setLibRule(rule);
			sr.setName(rule.getName());
			sr.setRuleDesc(rule.getRuleDesc());
			sr.setMessage(rule.getMessage());
			sr.setCrf(stdCrf);
			if(rule.getSecEle() != null) {
				if(se.containsKey(rule.getSecEle().getId())) {
					sr.setSecEle(se.get(rule.getSecEle().getId()));
				}
			}
			if(rule.getGroupEle() != null) {
				if(sge.containsKey(rule.getGroupEle().getId())) {
					sr.setGroupEle(sge.get(rule.getGroupEle().getId()));
				}
			}
			sr.setCompareWith(rule.getCompareWith());
			sr.setValue1(rule.getValue1());
			sr.setCondtion1(rule.getCondtion1());
			sr.setNcondtion1(rule.getNcondtion1());
			sr.setValue2(rule.getValue2());
			sr.setCondtion2(rule.getCondtion2());
			sr.setNcondtion2(rule.getNcondtion2());
			List<CrfRuleWithOther> otherCrfd = new ArrayList<>();
			for(CrfRuleWithOther crwo :  rule.getOtherCrf()) {
				CrfRuleWithOther srwo = new CrfRuleWithOther();
				srwo.setCrfRule(sr);
				srwo.setCrf(stdCrf);
				if(crwo.getSecEle() != null) {
					if(se.containsKey(crwo.getSecEle().getId())) {
						srwo.setSecEle(se.get(crwo.getSecEle().getId()));
					}
				}
				if(crwo.getGroupEle() != null) {
					if(sge.containsKey(crwo.getGroupEle().getId())) {
						srwo.setGroupEle(sge.get(crwo.getGroupEle().getId()));
						srwo.setRowNo(crwo.getRowNo());
					}
				}
				srwo.setCompareWith(crwo.getCompareWith());
				srwo.setCondtion(crwo.getCondtion());
				srwo.setNcondtion(crwo.getNcondtion());
				otherCrfd.add(srwo);
			}
			sr.setOtherCrf(otherCrfd);
			stdRules.add(sr);
		}
		return stdRules;
	}

	private Crf copyCrf(Crf crf, StudyMaster sm, String username) {
		Crf scrf = new Crf();
		scrf.setObservationName(crf.getObservationName());
		scrf.setObservationDesc(crf.getObservationDesc());
//		scrf.setStd(sm);
//		scrf.setLibCrf(crf);
		scrf.setName(crf.getName());
		scrf.setTitle(crf.getTitle());
		scrf.setType(crf.getTitle());
		scrf.setGender(crf.getGender());
		scrf.setVersion(crf.getVersion());
//		scrf.setOrderNo(crf.getOrderNo());
		scrf.setSections(copySectionStdSection(crf, scrf));
		
		return scrf;
	}

	private List<CrfSection> copySectionStdSection(Crf crf, Crf scrf) {
		List<CrfSection> secl = new ArrayList<>();
		for(CrfSection sec : crf.getSections()) {
			CrfSection s = new CrfSection();
			s.setName(sec.getName());
			s.setTitle(sec.getTitle());
			s.setHedding(sec.getHedding());
			s.setSubHedding(sec.getSubHedding());
			s.setMaxRows(sec.getMaxRows());
			s.setMaxColumns(sec.getMaxColumns());
			s.setOrder(sec.getOrder());
			s.setContainsGroup(sec.isContainsGroup());
			s.setGender(sec.getGender());
			s.setActive(sec.isActive());
			s.setRoles(sec.getRoles());
			s.setCrf(scrf);
			
			List<CrfSectionElement> selelist=  new ArrayList<>();
			for(CrfSectionElement ele : sec.getElement()) {
				CrfSectionElement scsce = new CrfSectionElement();
//				scsce.setId(ele.getId());
				scsce.setName(ele.getName());
				scsce.setLeftDesc(ele.getLeftDesc());
				scsce.setRigtDesc(ele.getRigtDesc());
				scsce.setMiddeDesc(ele.getMiddeDesc());
				scsce.setTotalDesc(ele.getTotalDesc());
				scsce.setTopDesc(ele.getTopDesc());
				scsce.setBottemDesc(ele.getBottemDesc());
				scsce.setRowNo(ele.getRowNo());
				scsce.setColumnNo(ele.getColumnNo());
				scsce.setType(ele.getType());
				scsce.setDataType(ele.getDataType());
				scsce.setResponceType(ele.getResponceType());
				scsce.setDisplay(ele.getDisplay());
				scsce.setValues(ele.getValues());
				scsce.setPattren(ele.getPattren());
				scsce.setRequired(ele.isRequired());
				scsce.setCrf(scrf);
				scsce.setSection(s);
				selelist.add(scsce);
				List<CrfSectionElementValue> selevl = new ArrayList<>();  
				List<CrfSectionElementValue> elevl = ele.getElementValues();
				if(elevl != null)
				for(CrfSectionElementValue elev : elevl) {
					CrfSectionElementValue selev =  new CrfSectionElementValue();
					selev.setValue(elev.getValue());
					selev.setSectionElement(scsce);
					selevl.add(selev);
				}
				scsce.setElementValues(selevl);
			}
			s.setElement(selelist);
//			CrfGroup g = crfDAO.getCrfGroup(sec.getId());
			CrfGroup group = sec.getGroup();
			if(group != null) {
				CrfGroup sgroup = new CrfGroup();
				sgroup.setName(group.getName());
				sgroup.setTitle(group.getTitle());
				sgroup.setHedding(group.getHedding());
				sgroup.setSubHedding(group.getSubHedding());
				sgroup.setMinRows(group.getMinRows());
				sgroup.setMaxRows(group.getMaxRows());
				sgroup.setMaxColumns(group.getMaxColumns());
				sgroup.setSection(s);
				sgroup.setCrf(scrf);
				List<CrfGroupElement> gelement = new ArrayList<>();
				List<CrfGroupElement> gelel = group.getElement();
				if(gelel != null) {
					for(CrfGroupElement ele : group.getElement()) {
						CrfGroupElement gele = new CrfGroupElement();
//						gele.setLibGroupElementId(ele.getId());
						gele.setName(ele.getName());
						gele.setTitle(ele.getTitle());
						gele.setRowNo(ele.getRowNo());
						gele.setColumnNo(ele.getColumnNo());
						gele.setType(ele.getType());
						gele.setDataType(ele.getDataType());
						gele.setResponceType(ele.getResponceType());
						gele.setDisplay(ele.getDisplay());
						gele.setValues(ele.getValues());
						gele.setPattren(ele.getPattren());
						gele.setRequired(ele.isRequired());
						gele.setCrf(scrf);
						gele.setSection(s);
						gele.setGroup(sgroup);
						List<CrfGroupElementValue> gelementValues = new ArrayList<>();
						List<CrfGroupElementValue> elementValues = ele.getElementValues();
						if(elementValues != null) {
							for(CrfGroupElementValue elev : elementValues) {
								CrfGroupElementValue selev = new CrfGroupElementValue();
								selev.setValue(elev.getValue().trim());
								selev.setGroupElement(gele);
								gelementValues.add(selev);
							}
						}
						gele.setElementValues(gelementValues);
						gelement.add(gele);
					}
				}
				sgroup.setElement(gelement);
				s.setGroup(sgroup);
			}
			secl.add(s);
		}
		return secl;
	}

	@Override
	public List<Crf> findAllCrfsWithSubElements() {
		// TODO Auto-generated method stub
		return crfDAO.findAllCrfsWithSubElemens();
	}

	@Override
	public Crf studyCrf(Long crfId) {
		// TODO Auto-generated method stub
		return crfDAO.studyCrf(crfId);
	}
	
	@Override
	public Crf getCrfForView(Long crfId) {
		// TODO Auto-generated method stub
		return crfDAO.getCrfForView(crfId);
	}

	@Override
	public String studyGroupElement(Long groupId, int rowNo) {
		// TODO Auto-generated method stub
		CrfGroup group = crfDAO.studyGroupFull(groupId);
		StringBuilder row  = new StringBuilder();
		Map<Integer, CrfGroupElement> eles = new HashMap<>();
		for(CrfGroupElement ele : group.getElement()) 
			eles.put(ele.getColumnNo(), ele);
		for(int col = 1; col <= group.getMaxColumns(); col++) {
			row.append("<td>");
			CrfGroupElement ele = eles.get(col);
			if(ele != null) {
				String id = "g_"+ele.getId()+"_"+ele.getName()+"_"+rowNo;
				if(ele.getType().equals("time")) {
					row.append("<div class='input-group clockpicker-with-callbacks'>");
					row.append("<input type='text' class='form-control' name='"+id+"' id='"+id+"' autocomplete='off' readonly='readonly' placeholder='time'/>");
					row.append("</div>");
				}else if(ele.getType().equals("text")) {
					if(ele.getPattren() == null || ele.getPattren().equals("")) {
						row.append("<input type='text' class='form-control' value='' name='"+id+"' id='"+id+"' />");
					}else {
						row.append("<input type='text' class='form-control' value='' name='"+id+"' id='"+id+"' placeholder='"+ele.getPattren()+"' />");
					}	
				}else if(ele.getType().equals("time")) {
					row.append("<input type='text' class='form-control' value='' name='"+id+"' id='"+id+"' placeholder='Now' autocomplete='off'/>");
					row.append("<script type='text/javascript'>var input = $('#"+id+"').clockpicker({  placement: 'bottom',align: 'left',autoclose: true,'default': 'now'});</script>");
				}else if(ele.getType().equals("date")) {
					row.append("<input type='text' class='form-control' value='' name='"+id+"' id='"+id+"' autocomplete='off'/>");
					row.append("<script type='text/javascript'>$(function(){$('#"+id+"').datepicker({dateFormat: $('#dateFormatJsp').val(),changeMonth:true,changeYear:true});});</script>");
				}else if(ele.getType().equals("datetime")) {
					row.append("<input type='text' class='form-control' value='' name='"+id+"' id='"+id+"' autocomplete='off'/>");
					row.append("<script type='text/javascript'>$(function(){$('#"+id+"').datepicker({dateFormat: $('#dateFormatJsp').val(),changeMonth:true,changeYear:true});});</script>");
				}else if(ele.getType().equals("testArea")) {
					if(ele.getPattren() == null || ele.getPattren().equals("")) {
						row.append("<textarea class='form-control' rows='3' cols='10' name='"+id+"' id='"+id+"' ></textarea>");
					}else {
						row.append("<textarea class='form-control' rows='3' cols='10' name='"+id+"' id='"+id+"' placeholder='"+ele.getPattren()+"'></textarea>");
					}
				}else if(ele.getType().equals("radio")) {
					if(ele.getDisplay().equals("horizantal")) {
						for(CrfGroupElementValue  ev: ele.getElementValues()) 
							row.append("<input type='radio' value='"+ev.getValue() +"' name='"+id+"' id='"+id+"'  >"+ev.getValue());
					}else {
						for(CrfGroupElementValue  ev: ele.getElementValues()) 
							row.append("<input type='radio' value='"+ev.getValue() +"' name='"+id+"' id='"+id+"' >"+ev.getValue()).append("<br/>");
					}
				}else if(ele.getType().equals("checkBox")) {
					if(ele.getDisplay().equals("horizantal")) {
						for(CrfGroupElementValue  ev: ele.getElementValues()) 
							row.append("<input type='checkbox' value='"+ev.getValue() +"' name='"+id+"' id='"+id+"' >"+ev.getValue());
					}else {
						for(CrfGroupElementValue  ev: ele.getElementValues()) 
							row.append("<input type='checkbox' value='"+ev.getValue() +"' name='"+id+"' id='"+id+"' >"+ev.getValue()).append("<br/>");
					}
				}else if(ele.getType().equals("select")) {
					row.append("<select class='form-control' name='"+id+"' id='"+id+"' >");
					row.append("<option value='-1'>--Select--</option>");
					for(CrfGroupElementValue  ev: ele.getElementValues()) 
						row.append("<option value='"+ev.getValue()+"'>"+ev.getValue()+"</option>");
					row.append("</select>");
				}else if(ele.getType().equals("date")) {
					row.append("<input type='text' class='form-control' value='' name='"+id+"' id='"+id+"' />");
					row.append("<script>$(function(){$('#"+id+"').datepicker({ dateFormat: $('#dateFormatJsp').val(), changeMonth:true, changeYear:true }); }); </script>");
//					<script type="text/javascript">
//					$(function(){
//						$("#${itemkey}").datepicker({
//							dateFormat: $("#dateFormatJsp").val(),
//							changeMonth:true,
//							changeYear:true
//						});
//					});
//				</script>
				}else{
					if(ele.getPattren() == null || ele.getPattren().equals("")) {
						row.append("<input type='text' class='form-control' value='' name='"+id+"' id='"+id+"' />");
					}else {
						row.append("<input type='text' class='form-control' value='' name='"+id+"' id='"+id+"' placeholder='"+ele.getPattren()+"' />");
					}
				}
				row.append("<font id='"+id+"_msg' color='red'></font>");
			}
			row.append("</td>");
		}
		return row.toString();
	}



	@Override
	public Map<String, String> getStudyCrfData(Crf crf, VolunteerPeriodCrf vpc) {
		Map<String, CrfSectionElementData> sectionData = crfDAO.studyCrfSectionElementDataList(vpc);
		Map<String, CrfGroupElementData> groupoData = crfDAO.studyCrfGroupElementDataList(vpc);		
		Map<Long, CrfGroupDataRows> groupoInfo = crfDAO.crfGroupDataRows(vpc);
		
		Map<String, String> crfData = new HashMap<>();
		for(CrfSection sec : crf.getSections()) {
			if(sec.getElement() != null && sec.getElement().size() > 0) {
				for(CrfSectionElement ele : sec.getElement()) {
					CrfSectionElementData data = sectionData.get(ele.getId()+"_"+ele.getName());
					System.out.println(data);
					if(data != null)
						crfData.put(data.getKayName(), data.getValue());
				}
			}
			CrfGroup group = sec.getGroup();
			if(group != null) {
				CrfGroupDataRows scgdr = groupoInfo.get(group.getId());
				if(scgdr != null ){
					int rowCount = scgdr.getNoOfRows();
					if(group.getElement() != null && group.getElement().size() >0) {
						for(CrfGroupElement  ele : group.getElement()) {
							for(int i=1;  i<=rowCount; i++) {
								String id = "g_"+ele.getId()+"_"+ele.getName()+"_"+i;
								CrfGroupElementData data = groupoData.get(id);
								if(data != null)
									crfData.put(data.getKayName(), data.getValue());
							}
						}
					}
				}
			}
		}
		return crfData;
	}

	@Override
	public Map<Long, CrfGroupDataRows> studyCrfGroupDataRows(VolunteerPeriodCrf vpc) {
		return crfDAO.crfGroupDataRows(vpc);
	}		
	
	@Override
	public String studyCrfDataEntry(Crf crf, VolunteerPeriodCrf vpc, HttpServletRequest request) {
		String status = "success";
//		String username = request.getSession().getAttribute("userName").toString();
//		//group id and each no of rows  information
//		String groupCountInfo = request.getParameter("groupCountInfo");
//		Map<Integer, Integer> groupCountInfoMap = new HashMap<>(); // key - group id , value = no of row's
//		if(groupCountInfo != null && !groupCountInfo.trim().equals("")) {
//			String[] eachGroupCountInfo = groupCountInfo.split("@");
//			for(String s : eachGroupCountInfo) {
//				if(!s.equals("")) {
//					String[] keyVal = s.split(",");
//					try {
//						groupCountInfoMap.put(Integer.parseInt(keyVal[0]), Integer.parseInt(keyVal[1]));
//					}catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//		List<CrfSectionElementDataAudit> audit = new ArrayList<>();
//		List<CrfSectionElementData> sectionData = new ArrayList<>();
//		List<CrfGroupElementData> groupoData = new ArrayList<>();
//		List<CrfGroupDataRows> groupoInfo = new ArrayList<>();
//		for(CrfSection sec : crf.getSections()) {
//			if(sec.getElement() != null && sec.getElement().size() > 0) {
//				for(CrfSectionElement ele : sec.getElement()) {
//					String id = ele.getId()+"_"+ele.getName();
//					String values = "";
//					if(ele.getType().equals("checkBox")) {
//						String[] s = request.getParameterValues(id);
//						boolean flag = true;
//						if(s != null && s.length > 0) {
//							for(String sv : s) {
//								if(flag) {
//									values = sv.trim(); 
//									flag = false;
//								}else values += "#####" +sv.trim();
//							}
//						}
//					}else {
//						if(request.getParameter(id) != null)
//							values = request.getParameter(id).trim();
//					}
//					CrfSectionElementData data = new CrfSectionElementData();
//					data.setKayName(id);
//					data.setValue(values);
//					data.setVolPeriodCrf(vpc);
//					data.setElement(ele);
//					data.setCreatedBy(username);
//					data.setCreatedOn(new Date());
//					sectionData.add(data);
//					
//					CrfSectionElementDataAudit saudit = new CrfSectionElementDataAudit();
//					saudit.setData(data);
//					saudit.setVolPeriodCrf(data.getVolPeriodCrf());
//					saudit.setElement(data.getElement());
//					saudit.setKayName(data.getKayName());
//					saudit.setNewValue(values);
//					saudit.setCreatedBy(username);
//					saudit.setCreatedOn(new Date());
//					audit.add(saudit);
//				}
//			}
//			CrfGroup group = sec.getGroup();
//			if(group != null) {
//				int gorupId = group.getId().intValue();
//				int rowCount = groupCountInfoMap.get(gorupId);
//				CrfGroupDataRows scgdr = new CrfGroupDataRows();
//				scgdr.setVolPeriodCrf(vpc);
//				scgdr.setGroup(group);
//				if(rowCount != 0) {
//					scgdr.setNoOfRows(rowCount);
//				}else {
//					scgdr.setNoOfRows(group.getMinRows());
//				}
//				groupoInfo.add(scgdr);
//				if(group.getElement() != null && group.getElement().size() >0) {
//					for(CrfGroupElement  ele : group.getElement()) {
//						
//						
//						for(int i=1;  i<=rowCount; i++) {
//							String id = "g_"+ele.getId()+"_"+ele.getName()+"_"+i;
//							String values = "";
//							if(ele.getType().equals("checkBox")) {
//								System.out.println(sec.getName() + " / " +ele.getType()+" : "+ ele.getName() +"---" + id);
//								if(request.getParameterValues(id) != null) {
//									String [] s = request.getParameterValues(id);
//									boolean flag = true;
//									if(s != null && s.length > 0) {
//										for(String sv : s) {
//											if(flag) {
//												values = sv.trim();
//												flag = false;
//											}else values += "#####" +sv.trim();
//										}
//									}
//								}
//							}else {
//								if(ele.getType().equals("radio")) {
//									System.out.println(id);
//									if(request.getParameter(id) != null)
//										values = request.getParameter(id).trim();
//									System.out.println(values);
//								}else if(request.getParameter(id) != null) {
//									values = request.getParameter(id);
//								}
//									
//							}
//							CrfGroupElementData data = new CrfGroupElementData();
//							data.setGroup(group);
//							data.setKayName(id);
//							data.setValue(values.trim());
//							data.setVolPeriodCrf(vpc);
//							data.setElement(ele);
//							data.setDataRows(rowCount);
//							data.setCreatedBy(username);
//							data.setCreatedOn(new Date());
//							groupoData.add(data);
//							CrfSectionElementDataAudit saudit = new CrfSectionElementDataAudit();
//							saudit.setGdata(data);
//							saudit.setVolPeriodCrf(data.getVolPeriodCrf());
//							saudit.setGelement(data.getElement());
//							saudit.setKayName(data.getKayName());
//							saudit.setNewValue(values);
//							saudit.setCreatedBy(username);
//							saudit.setCreatedOn(new Date());
//							audit.add(saudit);
//						}
//					}
//				}
//			}
//		}
//		
//		//save sectionData and groupoData
//		crfDAO.saveStudySectionData1(sectionData);
//		crfDAO.saveStudyGroupData(groupoData);
//		crfDAO.saveCrfGroupDataRows(groupoInfo);
//		crfDAO.saveCrfSectionElementDataAuditList(audit);
//		List<CrfDescrpency> desc = checkStudyCrfRules(sectionData,groupoData,groupoInfo, crf, vpc, username, false);
////		List<StudyCrfDescrpency> desc3 = checkStudyCrfRulesDate(sectionData,groupoData,groupoInfo, crf, vpc, username, true );
////		desc.addAll(desc3);
//		crfDAO.saveStudyCrfDescrpencyList(desc);
//		for(CrfDescrpency scd : desc) {
//			CrfDescrpencyAudit descAudit = new CrfDescrpencyAudit();
//			descAudit.setVol(vpc.getVol());
//			descAudit.setDesc(scd);
//			descAudit.setCrf(scd.getCrf());
//			descAudit.setVolPeriodCrf(scd.getVolPeriodCrf());
//			descAudit.setKayName(scd.getKayName());
//			descAudit.setSecElement(scd.getSecElement());
//			descAudit.setSecEleData(scd.getSecEleData());
//			descAudit.setGroupElement(scd.getGroupElement());
//			descAudit.setGroupEleData(scd.getGroupEleData());
//			descAudit.setRowNo(scd.getRowNo());
//			descAudit.setCrfRule(scd.getCrfRule());
//			descAudit.setStatus(scd.getStatus());
//			descAudit.setAssingnedTo(scd.getAssingnedTo());
//			descAudit.setRisedBy(scd.getRisedBy());
//			descAudit.setOldValue(scd.getOldValue());
//			descAudit.setOldStatus(scd.getOldStatus());
//			saveStudyCrfDescrpencyAudit(descAudit);
//		}
//		String inReview = request.getParameter("inReview");
//		if(inReview == null) inReview = "IN PROGRESS";
//		if(inReview.equals("IN REVIEW")) {
//			vpc.setCrfStatus("IN REVIEW");
//			status = "IN REVIEW";
//		}else {
//			vpc.setCrfStatus("IN PROGRESS");
//			status = "IN PROGRESS";
//		}
//		crfDAO.updateVpc(vpc);
		return status;
	}
	
	
	private List<CrfDescrpency> checkStudyCrfRules(List<CrfSectionElementData> sectionData,
			List<CrfGroupElementData> groupoData, List<CrfGroupDataRows> groupoInfo,
			Crf crf, VolunteerPeriodCrf vpc, String username, boolean dataFlag) {
		List<CrfDescrpency> descList = new ArrayList<>();
		Map<String, CrfDescrpency> map = new HashMap<>();
		List<CrfRule> rules = crfRuleWithCrfAndSubElements(crf);
		if(rules.size() >0) {
			Map<String, CrfSectionElementData> secData = new HashMap<>();
			Map<String, CrfGroupElementData> groupData = new HashMap<>();
			for(CrfSectionElementData s : sectionData) 
				secData.put(s.getKayName() , s);
			for(CrfGroupElementData s : groupoData) 
				groupData.put(s.getKayName(), s);
			
			for(CrfRule rule : rules) {
				
				if(rule.getSecEle() != null) {
					boolean result = false;
					CrfSectionElementData data = secData.get(rule.getSecEle().getId()+"_"+rule.getSecEle().getName());
					CrfSectionElementData sdata = null;
					CrfGroupElementData gdata = null;
					if(data != null && data.getValue() != null && !data.getValue().equals("")) {
						String value  = data.getValue();
						if(!rule.getCondtion1().equals("-1")) {
							result = checkCondition(value, rule.getValue1(), rule.getCondtion1(), 
									rule.getSecEle().getType(), rule.getSecEle().getDataType());
						}
						if(!rule.getCondtion2().equals("-1")) {
							boolean result2 = checkCondition(value, rule.getValue2(), rule.getCondtion2(), 
									rule.getSecEle().getType(), rule.getSecEle().getDataType());
							if(rule.getName().equals("and"))
								result = result && result2;
							else
								result = result || result2;
						}
						
						List<CrfRuleWithOther> otherCrf = rule.getOtherCrf();
						for(CrfRuleWithOther scrwo : otherCrf) {
							String v2 = null;
							if(scrwo.getCrf().getId() == crf.getId()) {
								if(scrwo.getSecEle() != null) {
									sdata = secData.get(scrwo.getSecEle().getId()+"_"+scrwo.getSecEle().getName());
									if(sdata != null)
										v2 = sdata.getValue();
								}
								if(scrwo.getGroupEle() != null) {
									gdata = groupData.get("g_"+scrwo.getGroupEle().getId()+""+scrwo.getGroupEle().getName()+"_"+scrwo.getRowNo());
									if(gdata != null)
										v2 = gdata.getValue();
								}
								
							}else {
								//check with other crf  logic
								if(scrwo.getSecEle() != null) v2 = crfDAO.studyCrfSectionElementData(vpc, scrwo.getSecEle());
								if(scrwo.getGroupEle() != null) v2 = crfDAO.studyCrfGroupElementData(vpc, scrwo.getGroupEle(), "g_"+scrwo.getGroupEle().getId()+"_"+scrwo.getGroupEle()+"_"+scrwo.getRowNo());
							}
							if(v2 != null) {
							boolean result2 = checkCondition(value, v2, scrwo.getCondtion(), 
									rule.getSecEle().getType(), rule.getSecEle().getDataType());
							if(scrwo.getNcondtion().equals("and")) 
								result = result && result2;
							else
								result = result || result2;
							}
						}
					}
					if(result) {
						CrfDescrpency scd = null;
						if(dataFlag)
							scd = crfDAO.studyCrfDescrpencySec(crf, vpc, data);
						if(scd == null) {
							scd =  new CrfDescrpency();
							scd.setCrf(crf);
							scd.setVolPeriodCrf(vpc);
							scd.setKayName(data.getKayName());
							scd.setSecElement(data.getElement());
							scd.setSecEleData(data);
							scd.setCrfRule(rule);
							scd.setCreatedBy(username);
							scd.setCreatedOn(new Date());
							if(map.get(data.getKayName()) == null) {
								descList.add(scd);
								
							}
						}
						map.put(data.getKayName(), scd);
					}
				}
				
				
				if(rule.getGroupEle() != null) {
					int count= rule.getGroupEle().getGroup().getMaxRows();
					for(int i=1; i<=count; i++) {
						System.out.println("g_"+rule.getGroupEle().getId()+"_"+rule.getGroupEle().getName()+i);
						CrfGroupElementData data = groupData.get("g_"+rule.getGroupEle().getId()+"_"+rule.getGroupEle().getName()+"_"+i);
						if(data !=null) {
							String value  = data.getValue();
							boolean result = false;
							CrfSectionElementData sdata = null;
							CrfGroupElementData gdata = null;
							if(!rule.getCondtion1().equals("-1")) {
								result = checkCondition(value, rule.getValue1(), rule.getCondtion1(), 
										rule.getGroupEle().getType(), rule.getGroupEle().getDataType());
							}
							if(!rule.getCondtion2().equals("-1")) {
								boolean result2 = checkCondition(value, rule.getValue2(), rule.getCondtion2(), 
										rule.getGroupEle().getType(), rule.getGroupEle().getDataType());
								if(rule.getName().equals("and"))
									result = result && result2;
								else
									result = result || result2;
							}
							
							List<CrfRuleWithOther> otherCrf = rule.getOtherCrf();
							for(CrfRuleWithOther scrwo : otherCrf) {
								String v2 = null;
								if(scrwo.getCrf().getId() == crf.getId()) {
									if(scrwo.getSecEle() != null) {
										sdata = secData.get(scrwo.getSecEle().getId()+"_"+scrwo.getSecEle().getName());
										if(sdata != null)
											v2 = sdata.getValue();
									}
									if(scrwo.getGroupEle() != null) {
										gdata = groupData.get("g_"+scrwo.getGroupEle().getId()+""+scrwo.getGroupEle().getName()+"_"+scrwo.getRowNo());
										if(gdata != null)
											v2 = gdata.getValue();
									}
									
								}else {
									//check with other crf  logic
									if(scrwo.getSecEle() != null) v2 = crfDAO.studyCrfSectionElementData(vpc, scrwo.getSecEle());
									if(scrwo.getGroupEle() != null) v2 = crfDAO.studyCrfGroupElementData(vpc, scrwo.getGroupEle(), "g_"+scrwo.getGroupEle()+"_"+scrwo.getRowNo());
								}
								if(v2 != null) {
									boolean result2 = checkCondition(value, v2, scrwo.getCondtion(), 
											rule.getSecEle().getType(), rule.getSecEle().getDataType());
									if(scrwo.getNcondtion().equals("and")) 
										result = result && result2;
									else
										result = result || result2;
								}
							}
							if(result) {
								//create Group Descrepency	
								CrfDescrpency scd = null;
								if(dataFlag)
									scd = crfDAO.studyCrfDescrpencyGroup(crf, vpc, data);
								if(scd == null) {
									scd =  new CrfDescrpency();
									scd.setCrf(crf);
									scd.setVolPeriodCrf(vpc);
									scd.setKayName(data.getKayName());
									scd.setGroupElement(data.getElement());
									scd.setGroupEleData(data);
									scd.setRowNo(i);
									scd.setCrfRule(rule);
									scd.setCreatedBy(username);
									scd.setCreatedOn(new Date());
									if(map.get(data.getKayName()) == null) {
										descList.add(scd);
									}
								}
								map.put(data.getKayName(), scd);
							}
						}
						
					}
				}
			}
			
		}
		
		return descList;
	}
	
	

	@Override
	public String studyCrfDataUpdate(Crf crf, VolunteerPeriodCrf vpc, HttpServletRequest request) {
		String status = "success";
//		String username = request.getSession().getAttribute("userName").toString();
//		//group id and each no of rows  information
//		String groupCountInfo = request.getParameter("groupCountInfo");
//		Map<Integer, Integer> groupCountInfoMap = new HashMap<>(); // key - group id , value = no of row's
//		if(groupCountInfo != null && !groupCountInfo.trim().equals("")) {
//			String[] eachGroupCountInfo = groupCountInfo.split("@");
//			for(String s : eachGroupCountInfo) {
//				if(!s.equals("")) {
//					String[] keyVal = s.split(",");
//					try {
//						groupCountInfoMap.put(Integer.parseInt(keyVal[0]), Integer.parseInt(keyVal[1]));
//					}catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//		
//		List<CrfSectionElementDataAudit> audit = new ArrayList<>();
//		List<CrfSectionElementData> sectionData = new ArrayList<>();
//		List<CrfGroupElementData> groupoData = new ArrayList<>();
//		List<CrfGroupDataRows> groupoInfo = new ArrayList<>();
//		List<CrfSectionElementData> sectionDataUpdate = new ArrayList<>();
//		List<CrfGroupElementData> groupoDataUpdate = new ArrayList<>();
//		List<CrfGroupDataRows> groupoInfoUpdate = new ArrayList<>();
//		for(CrfSection sec : crf.getSections()) {
//			if(sec.getElement() != null && sec.getElement().size() > 0) {
//				for(CrfSectionElement ele : sec.getElement()) {
//					String id = ele.getId()+"_"+ele.getName();
//					String values = "";
//					if(ele.getType().equals("checkBox")) {
//						String[] s = request.getParameterValues(id);
//						boolean flag = true;
//						if(s != null && s.length > 0) {
//							for(String sv : s) {
//								if(flag) {
//									values = sv.trim(); 
//									flag = false;
//								}else values += "#####" +sv.trim();
//							}
//						}
//					}else {
//						if(request.getParameter(id) != null)
//							values = request.getParameter(id).trim();
//					}
//					CrfSectionElementData data = null;
//					data = crfDAO.studyCrfSectionElementData(id, vpc, ele);
//					if(data == null) {
//						data = new CrfSectionElementData();
//						data.setKayName(id);
//						data.setValue(values);
//						data.setVolPeriodCrf(vpc);
//						data.setElement(ele);
//						data.setCreatedBy(username);
//						data.setCreatedOn(new Date());
//						data.setUpdatedBy(username);
//						data.setUpdatedOn(new Date());
//						sectionData.add(data);
//						CrfSectionElementDataAudit saudit = new CrfSectionElementDataAudit();
//						saudit.setData(data);
//						saudit.setVolPeriodCrf(data.getVolPeriodCrf());
//						saudit.setElement(data.getElement());
//						saudit.setKayName(data.getKayName());
//						saudit.setNewValue(values);
//						saudit.setCreatedBy(username);
//						saudit.setCreatedOn(new Date());
//						audit.add(saudit);
//					}else if(!data.getValue().equals(values)){
//						CrfSectionElementDataAudit saudit = new CrfSectionElementDataAudit();
//						saudit.setData(data);
//						saudit.setVolPeriodCrf(data.getVolPeriodCrf());
//						saudit.setElement(data.getElement());
//						saudit.setKayName(data.getKayName());
//						saudit.setOldValue(data.getValue());
//						saudit.setNewValue(values);
//						saudit.setUpdatedBy(username);
//						saudit.setUpdatedOn(new Date());
//						audit.add(saudit);
//						data.setValue(values);
//						data.setUpdatedBy(username);
//						data.setUpdatedOn(new Date());
//						sectionDataUpdate.add(data);
//					}
//				}
//			}
//			CrfGroup group = sec.getGroup();
//			if(group != null) {
//				int gorupId = group.getId().intValue();
//				int rowCount = groupCountInfoMap.get(gorupId);
//				CrfGroupDataRows scgdr = null;
//				scgdr = crfDAO.studyCrfGroupDataRows(vpc, group);
//				if(scgdr == null) {
//					scgdr = new CrfGroupDataRows();
//					scgdr.setVolPeriodCrf(vpc);
//					scgdr.setGroup(group);
//					if(rowCount != 0) {
//						scgdr.setNoOfRows(rowCount);
//					}else {
//						scgdr.setNoOfRows(group.getMinRows());
//					}
//					scgdr.setCreatedBy(username);
//					scgdr.setCreatedOn(new Date());
//					scgdr.setUpdatedBy(username);
//					scgdr.setUpdatedOn(new Date());
//					groupoInfo.add(scgdr);
//				}else if(scgdr.getNoOfRows() != rowCount){
////					scgdr.setNoOfRows(group.getMinRows());
//					scgdr.setNoOfRows(rowCount);
//					scgdr.setUpdatedBy(username);
//					scgdr.setUpdatedOn(new Date());
//					groupoInfoUpdate.add(scgdr);
//				}
//				if(group.getElement() != null && group.getElement().size() >0) {
//					for(CrfGroupElement  ele : group.getElement()) {
//						for(int i=1;  i<=rowCount; i++) {
//							String id = "g_"+ele.getId()+"_"+ele.getName()+"_"+i;
//							String values = "";
//							System.out.println(ele.getType() + " -------  " + id);
//							if(ele.getType().equals("checkBox")) {
//								System.out.println(sec.getName() + " / " +ele.getType()+" : "+ ele.getName() +"---" + id);
//								if(request.getParameterValues(id) != null) {
//									String [] s = request.getParameterValues(id);
//									boolean flag = true;
//									if(s != null && s.length > 0) {
//										for(String sv : s) {
//											if(flag) {
//												values = sv.trim();
//												flag = false;
//											}else values += "#####" +sv.trim();
//										}
//									}
//								}
//							}else {
//								if(ele.getType().equals("radio") && request.getParameter(id) != null) {
//									System.out.println(id);
//									values = request.getParameter(id).trim();
//									System.out.println(values);
//								}else if(request.getParameter(id) != null) {
//									values = request.getParameter(id).trim();
//								}
//								
//							}
//							CrfGroupElementData data = null;
//							data = crfDAO.studyCrfGroupElementData(id, vpc, ele);
//							if(data == null) {
//								data = new CrfGroupElementData();
//								data.setGroup(group);
//								data.setKayName(id);
//								data.setValue(values.trim());
//								data.setVolPeriodCrf(vpc);
//								data.setElement(ele);
//								data.setCreatedBy(username);
//								data.setCreatedOn(new Date());
//								data.setUpdatedBy(username);
//								data.setUpdatedOn(new Date());
//								data.setDataRows(rowCount);
//								groupoData.add(data);
//								
//								CrfSectionElementDataAudit saudit = new CrfSectionElementDataAudit();
//								saudit.setGdata(data);
//								saudit.setVolPeriodCrf(data.getVolPeriodCrf());
//								saudit.setGelement(data.getElement());
//								saudit.setKayName(data.getKayName());
//								saudit.setNewValue(values);
//								saudit.setCreatedBy(username);
//								saudit.setCreatedOn(new Date());
//								audit.add(saudit);
//							}else if(!data.getValue().equals(values)){
//								CrfSectionElementDataAudit saudit = new CrfSectionElementDataAudit();
//								saudit.setGdata(data);
//								saudit.setVolPeriodCrf(data.getVolPeriodCrf());
//								saudit.setGelement(data.getElement());
//								saudit.setKayName(data.getKayName());
//								saudit.setOldValue(data.getValue());
//								saudit.setNewValue(values);
//								saudit.setUpdatedBy(username);
//								saudit.setUpdatedOn(new Date());
//								audit.add(saudit);
//								
//								data.setValue(values);
//								data.setUpdatedBy(username);
//								data.setUpdatedOn(new Date());
//								groupoDataUpdate.add(data);
//							}
//						}
//					}
//				}
//			}
//		}
//		
//		
//		//save sectionData and groupoData
//		crfDAO.saveStudySectionData1(sectionData);
//		crfDAO.saveStudyGroupData(groupoData);
//		crfDAO.saveCrfGroupDataRows(groupoInfo);
//		crfDAO.saveStudySectionDataUpdate(sectionDataUpdate);
//		crfDAO.saveStudyGroupDataUpdate(groupoDataUpdate);
//		crfDAO.saveCrfGroupDataRowsUpdate(groupoInfoUpdate);
//		crfDAO.saveCrfSectionElementDataAuditList(audit);
//		
//		List<CrfDescrpency> desc = checkStudyCrfRules(sectionData,groupoData,groupoInfo, crf, vpc, username, false);
//		List<CrfDescrpency> desc2 = checkStudyCrfRules(sectionDataUpdate,groupoDataUpdate,groupoInfo, crf, vpc, username, true );
//		desc.addAll(desc2);
////		List<StudyCrfDescrpency> desc3 = checkStudyCrfRulesDate(sectionDataUpdate,groupoDataUpdate,groupoInfo, crf, vpc, username, true );
////		desc.addAll(desc3);
//		crfDAO.saveStudyCrfDescrpencyList(desc);
//		for(CrfDescrpency scd : desc) {
//			CrfDescrpencyAudit descAudit = new CrfDescrpencyAudit();
//			descAudit.setVol(vpc.getVol());
//			descAudit.setDesc(scd);
//			descAudit.setCrf(scd.getCrf());
//			descAudit.setVolPeriodCrf(scd.getVolPeriodCrf());
//			descAudit.setKayName(scd.getKayName());
//			descAudit.setSecElement(scd.getSecElement());
//			descAudit.setSecEleData(scd.getSecEleData());
//			descAudit.setGroupElement(scd.getGroupElement());
//			descAudit.setGroupEleData(scd.getGroupEleData());
//			descAudit.setRowNo(scd.getRowNo());
//			descAudit.setCrfRule(scd.getCrfRule());
//			descAudit.setStatus(scd.getStatus());
//			descAudit.setAssingnedTo(scd.getAssingnedTo());
//			descAudit.setRisedBy(scd.getRisedBy());
//			descAudit.setOldValue(scd.getOldValue());
//			descAudit.setOldStatus(scd.getOldStatus());
//			saveStudyCrfDescrpencyAudit(descAudit);
//		}
//		String inReview = request.getParameter("inReview");
//		if(inReview == null) {
//			inReview = "IN PROGRESS";
//			status = "IN PROGRESS";
//		}
//		if(inReview.equals("IN REVIEW")) {
//			vpc.setCrfStatus("IN REVIEW");
//			crfDAO.updateVpc(vpc);
//			status = "IN REVIEW";
//		}
		return status;
	}

	@Override
	public Map<String, String> requiredElementIdInJspStd(Crf crf, VolunteerPeriodCrf vpc, String userRole) {
		Map<String, String> map = new HashMap<>();
		for(CrfSection sec : crf.getSections()) {
			String[] roles = sec.getRoles().split("\\,");
			List<String> rolesList = new ArrayList<>();
			for(String role : roles) rolesList.add(role.trim());
			if(sec.getRoles().equals("ALL") || rolesList.contains(userRole)) {
				if(sec.getElement() != null && sec.getElement().size() > 0) {
					for(CrfSectionElement ele : sec.getElement()) {
						if(ele.isRequired())
							map.put(ele.getId()+"_"+ele.getName(), ele.getType());
					}
				}
				CrfGroup group = sec.getGroup();
				if(group != null) {
					int rowCount = group.getMinRows();
					if(group.getElement() != null && group.getElement().size() >0) {
						for(CrfGroupElement  ele : group.getElement()) {
							if(ele.isRequired()) {
								for(int i=1;  i<=rowCount; i++) {
									String id = "g_"+ele.getId()+"_"+ele.getName()+"_"+i;
									map.put(id, ele.getType());
								}
							}
						}
					}
				}
			}
		}
		return map;
	}
	
	@Override
	public Map<String, String> requiredElementIdInJspStdUpdate(Crf crf, VolunteerPeriodCrf vpc) {
		Map<String, String> map = new HashMap<>();
		for(CrfSection sec : crf.getSections()) {
				if(sec.getElement() != null && sec.getElement().size() > 0) {
					for(CrfSectionElement ele : sec.getElement()) {
						if(ele.isRequired())
							map.put(ele.getId()+"_"+ele.getName(), ele.getType());
					}
				}
				CrfGroup group = sec.getGroup();
				if(group != null) {
					CrfGroupDataRows scgdr = crfDAO.studyCrfGroupDataRows(vpc, group);
					int rowCount = scgdr.getNoOfRows();
					if(group.getElement() != null && group.getElement().size() >0) {
						for(CrfGroupElement  ele : group.getElement()) {
							for(int i=1;  i<=rowCount; i++) {
								if(ele.isRequired()) {
									String id = "g_"+ele.getId()+"_"+ele.getName()+"_"+i;
									map.put(id, ele.getType());
								}
							}
						}
					}
				}
		}
		return map;
	}
	
	@Override
	public Map<String, String> requiredElementIdInJspStdUpdate(Crf crf, VolunteerPeriodCrf vpc, String userRole) {
		Map<String, String> map = new HashMap<>();
		for(CrfSection sec : crf.getSections()) {
			String[] roles = sec.getRoles().split("\\,");
			List<String> rolesList = new ArrayList<>();
			for(String role : roles) rolesList.add(role.trim());
			if(sec.getRoles().equals("ALL") || rolesList.contains(userRole)) {
				if(sec.getElement() != null && sec.getElement().size() > 0) {
					for(CrfSectionElement ele : sec.getElement()) {
						if(ele.isRequired())
							map.put(ele.getId()+"_"+ele.getName(), ele.getType());
					}
				}
				CrfGroup group = sec.getGroup();
				if(group != null) {
					CrfGroupDataRows scgdr = crfDAO.studyCrfGroupDataRows(vpc, group);
					int rowCount = scgdr.getNoOfRows();
					if(group.getElement() != null && group.getElement().size() >0) {
						for(CrfGroupElement  ele : group.getElement()) {
							for(int i=1;  i<=rowCount; i++) {
								if(ele.isRequired()) {
									String id = "g_"+ele.getId()+"_"+ele.getName()+"_"+i;
									map.put(id, ele.getType());
								}
							}
						}
					}
				}
			}
		}
		return map;
	}
	
	@Override
	public String requiredGoupElementIdInJsp(Long groupId, int rowNo) {
		StringBuilder sb = new StringBuilder();//		key1@type1#key2@type2#key3@type3#key4@type4..........
		boolean flag = true;
		CrfGroup group = crfDAO.studyGroupFull(groupId);
		Map<Integer, CrfGroupElement> eles = new HashMap<>();
		for(CrfGroupElement ele : group.getElement()) 
			eles.put(ele.getColumnNo(), ele);
		for(int col = 1; col <= group.getMaxColumns(); col++) {
			CrfGroupElement ele = eles.get(col);
			if(ele != null) {
				String id = "g_"+ele.getId()+"_"+ele.getName()+"_"+rowNo;
				if(flag) {
					sb.append(id).append("@").append(ele.getType());
					flag = false;
				}else {
					sb.append("#").append(id).append("@").append(ele.getType());
				}
			}
		}
		return sb.toString();
	}
	
	@Override
	public Map<String, String> pattrenIdsAndPattrenStd(Crf crf, VolunteerPeriodCrf vpc) {
		Map<String, String> map = new HashMap<>();
		for(CrfSection sec : crf.getSections()) {
			if(sec.getElement() != null && sec.getElement().size() > 0) {
				for(CrfSectionElement ele : sec.getElement()) {
					if(ele.getPattren() != null && !ele.getPattren().trim().equals("")) {
							map.put(ele.getId()+"_"+ele.getName(), ele.getPattren());
					}
				}
			}
			CrfGroup group = sec.getGroup();
			if(group != null) {
				int rowCount = group.getMinRows();
				if(group.getElement() != null && group.getElement().size() >0) {
					for(CrfGroupElement  ele : group.getElement()) {
						if(ele.getPattren() != null && !ele.getPattren().trim().equals("")) {
							for(int i=1;  i<=rowCount; i++) {
								String id = "g_"+ele.getId()+"_"+ele.getName()+"_"+i;
								map.put(id, ele.getPattren());
							}
						}
					}
				}
			}
		}
		return map;
	}
	
	@Override
	public Map<String, String> pattrenIdsAndPattrenStdUpdate(Crf crf, VolunteerPeriodCrf vpc) {
		Map<String, String> map = new HashMap<>();
		for(CrfSection sec : crf.getSections()) {
			if(sec.getElement() != null && sec.getElement().size() > 0) {
				for(CrfSectionElement ele : sec.getElement()) {
					if(ele.getPattren() != null && !ele.getPattren().trim().equals("")) {
						map.put(ele.getId()+"_"+ele.getName(), ele.getPattren());
					}
				}
			}
			CrfGroup group = sec.getGroup();
			if(group != null) {
				CrfGroupDataRows scgdr = crfDAO.studyCrfGroupDataRows(vpc, group);
				int rowCount = scgdr.getNoOfRows();
				if(group.getElement() != null && group.getElement().size() >0) {
					for(CrfGroupElement  ele : group.getElement()) {
						if(ele.getPattren() != null && !ele.getPattren().trim().equals("")) {
							for(int i=1;  i<=rowCount; i++) {
									String id = "g_"+ele.getId()+"_"+ele.getName()+"_"+i;
									map.put(id, ele.getPattren());
							}
						}
					}
				}
			}
		}
		return map;
		
	}
	
	@Override
	public Map<String, String> allElementIdsTypesJspStd(Crf crf, VolunteerPeriodCrf vpc) {
		Map<String, String> map = new HashMap<>();
		for(CrfSection sec : crf.getSections()) {
			if(sec.getElement() != null && sec.getElement().size() > 0) {
				for(CrfSectionElement ele : sec.getElement()) {
						map.put(ele.getId()+"_"+ele.getName(), ele.getType());
				}
			}
			CrfGroup group = sec.getGroup();
			if(group != null) {
				int rowCount = group.getMinRows();
				if(group.getElement() != null && group.getElement().size() >0) {
					for(CrfGroupElement  ele : group.getElement()) {
						for(int i=1;  i<=rowCount; i++) {
							String id = "g_"+ele.getId()+"_"+ele.getName()+"_"+i;
							map.put(id, ele.getType());
						}
					}
				}
			}
		}
		return map;
	}
	
	@Override
	public Map<String, String> allElementIdsTypesJspStdUpdate(Crf crf, VolunteerPeriodCrf vpc) {
		Map<String, String> map = new HashMap<>();
		for(CrfSection sec : crf.getSections()) {
			if(sec.getElement() != null && sec.getElement().size() > 0) {
				for(CrfSectionElement ele : sec.getElement()) {
						map.put(ele.getId()+"_"+ele.getName(), ele.getType());
				}
			}
			CrfGroup group = sec.getGroup();
			if(group != null) {
				CrfGroupDataRows scgdr = crfDAO.studyCrfGroupDataRows(vpc, group);
				int rowCount = scgdr.getNoOfRows();
				if(group.getElement() != null && group.getElement().size() >0) {
					for(CrfGroupElement  ele : group.getElement()) {
						for(int i=1;  i<=rowCount; i++) {
								String id = "g_"+ele.getId()+"_"+ele.getName()+"_"+i;
								map.put(id, ele.getType());
						}
					}
				}
			}
		}
		return map;
	}

	@Override
	public String sectionEleSelect(Long id) {
		List<CrfSectionElement> list = crfDAO.sectionElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='secEleId' id='secEleId' class='form-control' onchange='secEleSelection(this.id, this.value)'>")
		.append("<option value='-1' selected='selected' >--Select--</option>");
		for(CrfSectionElement ele : list) {
			sb.append("<option value='"+ele.getId()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	@Override
	public String otherCrfSectionElements(Long id, int count) {
		List<CrfSectionElement> list = crfDAO.sectionElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='otherCrfSecEle"+count+"' id='otherCrfSecEle"+count+"' class='form-control' onchange='othersecEleSelection(this.id, this.value, "+count+")'>")
		.append("<option value='-1' selected='selected' >--Select--</option>");
		for(CrfSectionElement ele : list) {
			sb.append("<option value='"+ele.getId()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select> <font color='red' id='otherCrfSecEle"+count+"msg'></font>");
		return sb.toString();
	}
	

	@Override
	public String groupEleSelect(Long id) {
		List<CrfGroupElement> list = crfDAO.groupElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='groupEleId' id='groupEleId' class='form-control' onchange='groupEleSelection(this.id, this.value)'>")
		.append("<option value='-1' selected='selected'>--Select--</option>");
		for(CrfGroupElement ele : list) {
			sb.append("<option value='"+ele.getId()+","+ele.getGroup().getMaxRows()+","+ele.getGroup().getMaxColumns()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	@Override
	public String otherCrfGroupElements(Long id, int count) {
		List<CrfGroupElement> list = crfDAO.groupElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='otherCrfGroupEle"+count+"' id='otherCrfGroupEle"+count+"' class='form-control' onchange='othergroupEleSelection(this.id, this.value, "+count+")'>")
		.append("<option value='-1' selected='selected'>--Select--</option>");
		for(CrfGroupElement ele : list) {
			sb.append("<option value='"+ele.getId()+","+ele.getGroup().getMaxRows()+","+ele.getGroup().getMaxColumns()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select> <font color='red' id='otherCrfGroupEle"+count+"msg'></font>");
		return sb.toString();
	}

	@Override
	public String crfRuleElements(int rowId, Long crfId) {
		rowId++;
		Crf crf = crfDAO.getCrf(crfId);
//		List<Crf> list  = crfDAO.findAllCrfsWithSubElemens();
		StringBuilder sb = new StringBuilder();
		sb.append("<tr id='AddRow"+rowId+"'>");
		sb.append("<td><select class='form-control' name='otherCrf"+rowId+"' id='otherCrf"+rowId+"' class='form-control' onchange='otherCrf(this.id, this.value, "+rowId+")'>");
		sb.append("<option value='"+crf.getId()+"'>"+crf.getName()+"</option>");
//		sb.append("<option value='-1' selected='selected'>--Select--</option>");
//		for(Crf crf : list) {
//			sb.append("<option value='"+crf.getId()+"'>"+crf.getName()+"</option>");
//		}
		sb.append("</select><font color='red' id='otherCrf"+rowId+"msg'></font></td>");
		List<CrfSectionElement> slist = crfDAO.sectionElemets(crfId);
		sb.append("<td id='secEleIdTd"+rowId+"'><select class='form-control' name='otherCrfSecEle"+rowId+"' class='form-control' id='otherCrfSecEle"+rowId+"' onchange='secEleSelection(this.id, this.value, "+rowId+")'> ");
		sb.append("<option value='-1' selected='selected'>--Select--</option>");
		for(CrfSectionElement ele : slist) {
			sb.append("<option value='"+ele.getId()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select><font color='red' id='otherCrfSecEle"+rowId+"msg'></font></td>");
		
		List<CrfGroupElement> glist = crfDAO.groupElemets(crfId);
		sb.append("<td id='groupEleIdTd"+rowId+"'><select class='form-control' name='otherCrfGroupEle"+rowId+"' class='form-control' id='otherCrfGroupEle"+rowId+"' onchange='othergroupEleSelection(this.id, this.value, "+rowId+")'> ");
		sb.append("<option value='-1' selected='selected'>--Select--</option>");
		for(CrfGroupElement ele : glist) {
			sb.append("<option value='"+ele.getId()+","+ele.getGroup().getMaxRows()+","+ele.getGroup().getMaxColumns()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select><font color='red' id='otherCrfGroupEle"+rowId+"msg'></font></td>");
		
		sb.append("<td id='groupEleIdTdRow"+rowId+"'><select class='form-control' name='otherCrfGroupEleRowNo"+rowId+"' class='form-control' id='otherCrfGroupEleRowNo"+rowId+"'> ");
		sb.append("<option value='-1' selected='selected'>--Select--</option>");
		sb.append("</select></td>");
		
		
		sb.append("<td><select class='form-control' name='otherCrfContion"+rowId+"' class='form-control' id='otherCrfContion"+rowId+"'> ");
		sb.append("<option value='eq' selected='selected'>=</option>");
		sb.append("<option value='ne'>!=</option>");
		sb.append("<option value='le'>Less</option>");
		sb.append("<option value='leq'>Less and eq</option>");
		sb.append("<option value='gt'>Grater</option>");
		sb.append("<option value='gte'>grater and eq</option>");
		sb.append("</select></td>");
		
		sb.append("<td><select class='form-control' name='otherCrfNContion"+rowId+"' class='form-control' id='otherCrfNContion"+rowId+"'> ");
		sb.append("<option value='and' selected='selected'>And</option>");
		sb.append("<option value='or'>or</option>");
		sb.append("</select></td>");
		
		sb.append("</tr>");
		return sb.toString();
	}

	
	@Override
	public String saveCrfRule(String username, HttpServletRequest request) {
		Map<Long, Crf> crfMap = new HashMap<>();
		Map<Long, CrfSectionElement> secEleMap = new HashMap<>();
		Map<Long, CrfGroupElement> crfGroupEleMap = new HashMap<>();
		String name = request.getParameter("name");
		CrfRule rule = new CrfRule();
		rule.setCreatedBy(username);
		rule.setCreatedOn(new Date());
		rule.setName(name);
		rule.setRuleDesc(request.getParameter("desc"));
		rule.setMessage(request.getParameter("message"));
		Crf crf = crfDAO.getCrf(Long.parseLong(request.getParameter("crfid").toString()));
		crfMap.put(crf.getId(), crf);
		rule.setCrf(crf);
		if(!request.getParameter("secEleId").equals("-1")) {
			CrfSectionElement secEle = crfDAO.sectionElement(Long.parseLong(request.getParameter("secEleId").toString()));
			rule.setSecEle(secEle);
			secEleMap.put(secEle.getId(), secEle);
		}
		if(!request.getParameter("groupEleId").equals("-1")) { 
			String[] gidinfo = request.getParameter("groupEleId").toString().split(",");
			String gidinfoRowNo = request.getParameter("groupEleRowNo").toString();
			CrfGroupElement groupEle = crfDAO.groupElement(Long.parseLong(gidinfo[0]));
			rule.setGroupEle(groupEle);
			rule.setRowNumber(gidinfoRowNo);
			crfGroupEleMap.put(groupEle.getId(), groupEle);
			
//			crfGroupEleMapRowCount.put(groupEle.getId(), Integer.parseInt(request.getParameter("groupEleId").toString()))
		}
		String compareWith = request.getParameter("compareWith");
		rule.setCompareWith(compareWith);
		
		rule.setValue1(request.getParameter("userInput"));
		rule.setCondtion1(request.getParameter("compareWithCondition"));
		rule.setNcondtion1(request.getParameter("compareWithConditionN"));
		rule.setValue2(request.getParameter("userInput2"));
		rule.setCondtion2(request.getParameter("compareWithCondition2"));
		rule.setNcondtion2(request.getParameter("compareWithConditionN2"));		
		
		List<CrfRuleWithOther> list = new ArrayList<>();
		int newRows = Integer.parseInt(request.getParameter("newRows").toString());
		if(compareWith.equals("Other CRF Field") || newRows > 0) {
//			int newRows = Integer.parseInt(request.getParameter("newRows").toString());
			for(int i=1; i<= newRows; i++) {
				
				if(request.getParameter("otherCrf"+i) != null) {
					CrfRuleWithOther crwo = new CrfRuleWithOther();
					crwo.setCrfRule(rule);
					crwo.setCondtion(request.getParameter("otherCrfContion"+i));
					crwo.setNcondtion(request.getParameter("otherCrfNContion"+i));
					if(!crfMap.containsKey(Long.parseLong(request.getParameter("otherCrf"+i).toString()))) {
						Crf ocrf = crfDAO.getCrf(Long.parseLong(request.getParameter("otherCrf"+i).toString()));
						crfMap.put(ocrf.getId(), ocrf);
					}
					crwo.setCrf(crfMap.get(Long.parseLong(request.getParameter("otherCrf"+i).toString())));
					
					if(!request.getParameter("otherCrfSecEle"+i).equals("-1")){
						if(!secEleMap.containsKey(Long.parseLong(request.getParameter("otherCrfSecEle"+i).toString()))) {
							CrfSectionElement osecEle = crfDAO.sectionElement(Long.parseLong(request.getParameter("otherCrfSecEle"+i).toString()));
							secEleMap.put(osecEle.getId(), osecEle);
						}
						crwo.setSecEle(secEleMap.get(Long.parseLong(request.getParameter("otherCrfSecEle"+i).toString())));
					}
					
					if(!request.getParameter("otherCrfGroupEle"+i).equals("-1")){
						String[] gidinfo = request.getParameter("otherCrfGroupEle"+i).toString().split(",");
						if(!crfGroupEleMap.containsKey(Long.parseLong(gidinfo[0]))) {
							CrfGroupElement osecEle = crfDAO.groupElement(Long.parseLong(gidinfo[0]));
							crfGroupEleMap.put(osecEle.getId(), osecEle);
						}
						crwo.setGroupEle(crfGroupEleMap.get(Long.parseLong(gidinfo[0])));
						crwo.setRowNo(Integer.parseInt(request.getParameter("otherCrfGroupEleRowNo"+i)));
					}
					list.add(crwo);
				}
			}	
		}
		rule.setOtherCrf(list);
		name = crfDAO.saveCrfRule(rule);
		return name;
	}

	@Override
	public List<CrfRule> findAllCrfRules() {
		return crfDAO.findAllCrfRules();
	}

	@Override
	public String crfRuleChangeStatus(Long id) {
		CrfRule rule = crfDAO.crfRule(id);
		if(rule.isActive()) rule.setActive(false);
		else rule.setActive(true);
		try {
			crfDAO.updateCrfRule(rule);
			return rule.getName();
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Crf> findAllActiveCrfs() {
		return crfDAO.findAllActiveCrfs();
	}

	
	
	@Override
	public List<CrfRule> crfRuleWithCrfAndSubElements(Crf crf) {
		return crfDAO.crfRuleWithCrfAndSubElements(crf);
	}
	@Override
	public List<RulesInfoTemp> rulesFields(Crf crf, List<CrfRule> rules) {
		List<RulesInfoTemp> result = new ArrayList<>();
		for(int count = 0; count < rules.size(); count++) {
			CrfRule rule = rules.get(count); 
			RulesInfoTemp data = new RulesInfoTemp();
			Map<String, String> eleType = new HashMap<>();
			List<String> values = new ArrayList<>();
			if(rule.getSecEle() != null) {
				CrfSectionElement elde = rule.getSecEle();
				data.setKey(elde.getId()+"_"+elde.getName());
				eleType.put(elde.getId()+"_"+elde.getName(), elde.getType());
				List<CrfRuleWithOther> otherCrf = rule.getOtherCrf();
				for(CrfRuleWithOther s : otherCrf) {
					if(s.getCrf().getId() == crf.getId()) {
						if(s.getSecEle() != null) {
							CrfSectionElement sele = s.getSecEle();
							values.add(sele.getId()+"_"+sele.getName());
							eleType.put(sele.getId()+"_"+sele.getName(), sele.getType());
						}else if(s.getGroupEle() != null) {
							CrfGroupElement gele = s.getGroupEle();
							CrfGroup group  = gele.getGroup();
							for(int i=1; i< group.getMaxRows(); i++) {
								values.add("g_"+gele.getId()+"_"+gele.getName()+"_"+i);
								eleType.put("g_"+gele.getId()+"_"+gele.getName()+"_"+i, gele.getType());
							}		
						}
					}
				}
				data.setEleType(eleType);
				data.setOeles(values);
				result.add(data);
			}else if(rule.getGroupEle() != null) {
				CrfGroupElement ele = rule.getGroupEle();
				data.setKey("g_"+ele.getId()+"_"+ele.getName()+"_"+rule.getRowNumber());
				eleType.put("g_"+ele.getId()+"_"+ele.getName()+"_"+rule.getRowNumber(), ele.getType());
				List<CrfRuleWithOther> otherCrf = rule.getOtherCrf();
				for(CrfRuleWithOther s : otherCrf) {
					if(s.getCrf().getId() == crf.getId()) {
						if(s.getSecEle() != null) {
							CrfSectionElement sele = s.getSecEle();
							values.add(sele.getId()+"_"+sele.getName());
							eleType.put(sele.getId()+"_"+sele.getName(), sele.getType());
						}else if(s.getGroupEle() != null) {
							CrfGroupElement gele = s.getGroupEle();
							CrfGroup group  = gele.getGroup();
							for(int i=1; i< group.getMaxRows(); i++) {
								values.add("g_"+gele.getId()+"_"+gele.getName()+"_"+i);
								eleType.put("g_"+gele.getId()+"_"+gele.getName()+"_"+i, gele.getType());
							}		
						}
					}
				}
				data.setEleType(eleType);
				data.setOeles(values);
				result.add(data);
			}
		}
		return result;
	}
	@Override
	public String crfRuelCheck(Long crfId,  Long dbId, String id, String value, String values) {
//		TestMaster test = crfDAO.testMasterWithCrf(testId);
		Map<String, String> oeleMap = new HashMap<>();
		if(values != null && !values.equals("")) {
			String[] pairs = values.split("@@@");
			for(String s : pairs) {
				String[] idVal = s.split("@@");
				oeleMap.put(idVal[0], idVal[1]);
			}
		}
		List<CrfRule> rules = null;
		if(id.charAt(0) == 'g') {
			rules = crfDAO.crfRuleWithCrfAndSubElementsWith(dbId, crfId, true);
		}else
			rules = crfDAO.crfRuleWithCrfAndSubElementsWith(dbId, crfId, false);
		
		
		for(CrfRule rule : rules) {
			boolean result = false;
			if(rule.getCompareWith().equals("userInput")) {
				boolean ocount = true;
				String ncondition = "";
				if(!rule.getCondtion1().equals("-1")) {
					if(!rule.getValue1().trim().equals("")) {
						if(rule.getSecEle() != null)
							result = checkCondition(value, rule.getValue1(), rule.getCondtion1(), 
								rule.getSecEle().getType(), rule.getSecEle().getDataType());
						if(rule.getGroupEle() != null)
							result = checkCondition(value, rule.getValue1(), rule.getCondtion1(), 
								rule.getGroupEle().getType(), rule.getGroupEle().getDataType());
						ncondition = rule.getNcondtion1();
						ocount = false;
					}
				}
				if(!rule.getCondtion2().equals("-1")) {
					if(!rule.getValue2().trim().equals("")) {
						boolean result2 = false;
						if(rule.getSecEle() != null)
							result2 = checkCondition(value, rule.getValue2(), rule.getCondtion2(), 
								rule.getSecEle().getType(), rule.getSecEle().getDataType());
						if(rule.getGroupEle() != null)
							result2 = checkCondition(value, rule.getValue2(), rule.getCondtion2(), 
								rule.getGroupEle().getType(), rule.getGroupEle().getDataType());
						if(ncondition.equals("and"))
							result = result && result2;
						else
							result = result || result2;
						ncondition = rule.getNcondtion2();
						ocount = false;
					}
				}
				
				List<CrfRuleWithOther> otherCrf = rule.getOtherCrf();
				
				if(otherCrf.size() > 0) {
					int index = 0;
					while(index < otherCrf.size()) {
						String eleType = "";
						String eleDataType = "";
						String compatevalue = "";
						CrfRuleWithOther crf1 = otherCrf.get(index);
						if(crf1.getSecEle() != null) {
							compatevalue = oeleMap.get(crf1.getSecEle().getId()+"_"+crf1.getSecEle().getName());
							eleType = crf1.getSecEle().getType();
							eleDataType =  crf1.getSecEle().getDataType();
						}else if(crf1.getGroupEle() != null) {
							System.out.println("g_"+crf1.getGroupEle().getId()+"_"+crf1.getGroupEle().getName()+"_"+crf1.getRowNo());
							compatevalue = oeleMap.get("g_"+crf1.getGroupEle().getId()+"_"+crf1.getGroupEle().getName()+"_"+crf1.getRowNo());
							eleType = crf1.getGroupEle().getType();
							eleDataType =  crf1.getGroupEle().getDataType();
						}
						if(value != null && !value.equals("") && compatevalue != null && !compatevalue.equals("")) {
							boolean result2 = checkCondition(value, compatevalue, crf1.getCondtion(), eleType, eleDataType);
							if(ocount) result = result2;
							else if(ncondition.equals("and")) result = result && result2;
							else result = result || result2;
							
						}
						ncondition = crf1.getNcondtion();
						ocount = false;
						index++;
					}
				}
				
			}else {
				String compatevalue = "";
				List<CrfRuleWithOther> otherCrf = rule.getOtherCrf();
				boolean ocount = true;
				if(otherCrf.size() > 0) {
					int index = 0;
					String eleType = "";
					String eleDataType = "";
					while((index+1) < otherCrf.size()) {
						CrfRuleWithOther crf1 = otherCrf.get(index);
						if(crf1.getSecEle() != null) {
//							if(compatevalue.equals(""))
								compatevalue = oeleMap.get(crf1.getSecEle().getId()+"_"+crf1.getSecEle().getName());
							eleType = crf1.getSecEle().getType();
							eleDataType =  crf1.getSecEle().getDataType();
						}else if(crf1.getGroupEle() != null) {
							System.out.println("g_"+crf1.getGroupEle().getId()+"_"+crf1.getGroupEle().getName()+"_"+crf1.getRowNo());
//							if(compatevalue.equals(""))
								compatevalue = oeleMap.get("g_"+crf1.getGroupEle().getId()+"_"+crf1.getGroupEle().getName()+"_"+crf1.getRowNo());
							eleType = crf1.getGroupEle().getType();
							eleDataType =  crf1.getGroupEle().getDataType();
						}
						
						CrfRuleWithOther crf2 = otherCrf.get(index+1);
						String compatevalue2 = "";
						if(crf2.getSecEle() != null) {
							compatevalue2 = oeleMap.get(crf2.getSecEle().getId()+"_"+crf2.getSecEle().getName());
						}else if(crf2.getGroupEle() != null) {
							System.out.println("g_"+crf2.getGroupEle().getId()+"_"+crf2.getGroupEle().getName()+"_"+crf2.getRowNo());
							compatevalue2 = oeleMap.get("g_"+crf2.getGroupEle().getId()+"_"+crf2.getGroupEle().getName()+"_"+crf2.getRowNo());
						}
						
						if(compatevalue!=null && !compatevalue.equals("") && compatevalue2 != null && !compatevalue2.equals("")) {
							boolean result2 = checkCondition(compatevalue, compatevalue2, crf1.getCondtion(), 
									eleType, eleDataType);
							if(ocount) 
								result = result2;
							else if(crf1.getNcondtion().equals("and")) 
								result = result && result2;
							else
								result = result || result2;
						}
						ocount = false;
						index++;
					}
				}
				
			}
			
			if(result) {
				System.out.println(rule.getId());
				if(rule.getMessage() != null)
					return rule.getMessage();
				else
					rule.getRuleDesc();
			}
		}		
		return "";
	}
	@Override
	public List<RulesInfoTemp> rulesFieldsSecMap(Crf crf, List<CrfRule> rules) {
		List<RulesInfoTemp> dataList = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		for(CrfRule r : rules ) {
			String key = "";
			String value = "";
			if(r.getSecEle() != null) {
				CrfSectionElement ele = r.getSecEle();
				if(key.trim().equals("")) {
					key = ele.getId()+"";
				}else
					key = key+"##"+ele.getId();
			}
			List<String> oeles = new ArrayList<>();
			List<CrfRuleWithOther> otherCrf = r.getOtherCrf();
			for(CrfRuleWithOther s : otherCrf) {
				if(s.getCrf().getId() == crf.getId()) {
					if(r.getSecEle() != null) {
						CrfSectionElement ele = r.getSecEle();
						if(value.trim().equals("")) {
							value = ele.getId()+"_"+ele.getName();
						}else {
							value = value+"##"+ele.getId()+"_"+ele.getName();
						}
					}
					if(r.getGroupEle() != null) {
						CrfGroupElement ele = r.getGroupEle();
						CrfGroup group  = ele.getGroup();
						for(int i=1; i< group.getMaxRows(); i++) {
							if(value.trim().equals("")) {
								value = "g_"+ele.getId()+"_"+ele.getName()+"_"+i;
							}else
								value = value+"##g_"+ele.getId()+"_"+ele.getName()+"_"+i;
						}		
					}
					if(s.getSecEle() != null) {
						oeles.add(s.getSecEle().getId()+"_"+s.getSecEle().getName());
					}
					if(r.getGroupEle() != null) {
						CrfGroupElement ele = s.getGroupEle();
						CrfGroup group  = ele.getGroup();
						for(int i=1; i< group.getMaxRows(); i++) {
							oeles.add("g_"+ele.getId()+"_"+ele.getName()+"_"+i);
						}	
					}
				}
			}
			
			RulesInfoTemp data = new RulesInfoTemp();
			if(map.get(key) != null && !map.get(key).equals("")) {
				String val = map.get(key) + "##" + value; 
				map.put(key, val);
				data.setKey(key);
				data.setValue(value);
				data.setOeles(oeles);
			}else {
				map.put(key, value);
				data.setKey(key);
				data.setValue(value);
				data.setOeles(oeles);
			}
			dataList.add(data);
		}
		return dataList;
	}
	
	@Override
	public Map<String, String> rulesFieldsGroupMap(Crf crf, List<CrfRule> rules) {
		Map<String, String> map = new HashMap<>();
		for(CrfRule r : rules ) {
			String key = "";
			String value = "";
			if(r.getGroupEle() != null) {
				CrfGroupElement ele = r.getGroupEle();
				if(key.trim().equals("")) {
					key = ele.getId()+"";
				}else
					key = key+"##"+ele.getId();
			}
			
			List<CrfRuleWithOther> otherCrf = r.getOtherCrf();
			for(CrfRuleWithOther s : otherCrf) {
				if(s.getCrf().getId() == crf.getId()) {
					if(r.getSecEle() != null) {
						CrfSectionElement ele = r.getSecEle();
						if(value.trim().equals("")) {
							value = ele.getId()+"_"+ele.getName();
						}else {
							value = value+"##"+ele.getId()+"_"+ele.getName();
						}
					}
					if(s.getGroupEle() != null) {
						CrfGroupElement ele = s.getGroupEle();
						CrfGroup group  = ele.getGroup();
						for(int i=1; i< group.getMaxRows(); i++) {
							if(value.trim().equals("")) {
								value = "g_"+ele.getId()+"_"+ele.getName()+"_"+i;
							}else
								value = value+"##g_"+ele.getId()+"_"+ele.getName()+"_"+i;
						}		
					}
				}
			}
			if(map.get(key) != null && !map.get(key).equals("")) {
				String val = map.get(key) + "##" + value; 
				map.put(key, val);
			}else
				map.put(key, value);
		}
		return map;
	}

	@Override
	public Map<String, String> rulesFieldsMap(Crf crf) {
		Map<String, String> map = new HashMap<>();
		List<CrfRule> rules = crfDAO.crfRuleWithCrfAndSubElements(crf);
		for(CrfRule r : rules ) {
			String key = "";
			String value = "";
			if(r.getSecEle() != null) {
				CrfSectionElement ele = r.getSecEle();
				if(key.trim().equals("")) {
					key = ele.getId()+"_"+ele.getName();
				}else
					key = key+"##"+ele.getId()+"_"+ele.getName();
			}
			if(r.getGroupEle() != null) {
				CrfGroupElement ele = r.getGroupEle();
				CrfGroup group  = ele.getGroup();
				for(int i=1; i< group.getMaxRows(); i++) {
					if(key.trim().equals("")) {
						key = "g_"+ele.getId()+"_"+ele.getName()+"_"+i;
					}else
						key = key+"##g_"+ele.getId()+"_"+ele.getName()+"_"+i;
				}
			}
			
			List<CrfRuleWithOther> otherCrf = r.getOtherCrf();
			for(CrfRuleWithOther s : otherCrf) {
				if(s.getCrf().getId() == crf.getId()) {
					if(r.getSecEle() != null) {
						CrfSectionElement ele = r.getSecEle();
						if(value.trim().equals("")) {
							value = ele.getId()+"_"+ele.getName();
						}else {
							value = value+"##"+ele.getId()+"_"+ele.getName();
						}
					}
					if(s.getGroupEle() != null) {
						CrfGroupElement ele = s.getGroupEle();
						CrfGroup group  = ele.getGroup();
						for(int i=1; i< group.getMaxRows(); i++) {
							if(value.trim().equals("")) {
								value = "g_"+ele.getId()+"_"+ele.getName()+"_"+i;
							}else
								value = value+"##g_"+ele.getId()+"_"+ele.getName()+"_"+i;
						}		
					}
				}
			}
			if(map.get(key) != null && !map.get(key).equals("")) {
				String val = map.get(key) + "##" + value; 
				map.put(key, val);
			}else
				map.put(key, value);
		}
		return map;
	}

	
	
	

	@Override
	public String studyCrfSecRuelCheck(Long crfId,  Long dbId, String id, String value, String values, StudyMaster sm, String oeles) {
		Map<String, String> oeleMap = new HashMap<>();
		if(oeles != null && !oeles.equals("")) {
			String[] pairs = oeles.split("##");
			for(String s : pairs) {
				String[] idVal = s.split("@@");
				oeleMap.put(idVal[0], idVal[1]);
			}
		}
		//for section element validation
		
		//map with dbids and values
		Map<Long, String> secmap = new HashMap<>();
		Map<Long, String> groupmap = new HashMap<>();
		
		//map with ids and values
		Map<String, String> secmap2 = new HashMap<>();
		Map<String, String> groupmap2 = new HashMap<>();
		
		if(!id.equals(values)) {
			String[] arr = values.split("@@@##");
			for(String s : arr) {
				String[] s1 = s.split("@@##");
				
				String eleid = s1[0]; // id_name
				String[] dbidName = eleid.split("_");
				if(eleid.substring(0, 2).equals("g_")) {
					groupmap2.put(s1[0], s1[1]);
					groupmap.put(Long.parseLong(dbidName[1]), s1[1]);
				}else {
					secmap2.put(s1[0], s1[1]);
					secmap.put(Long.parseLong(dbidName[0]), s1[1]);
				}
			}
		}
		
		List<CrfRule> rules = crfDAO.studyCrfRuleWithCrfAndSubElementsWithSecEleId(dbId, "section");
		
		
		for(CrfRule rule : rules) {
			boolean result = false;
			if(rule.getCompareWith().equals("userInput")) {
				if(!rule.getCondtion1().equals("-1")) {
					if(!rule.getValue1().trim().equals("")) {
						if(rule.getSecEle() != null)
							result = checkCondition(value, rule.getValue1(), rule.getCondtion1(), 
								rule.getSecEle().getType(), rule.getSecEle().getDataType());
						if(rule.getGroupEle() != null)
							result = checkCondition(value, rule.getValue1(), rule.getCondtion1(), 
								rule.getGroupEle().getType(), rule.getGroupEle().getDataType());
					}
				}
				if(!rule.getCondtion2().equals("-1")) {
					if(!rule.getValue2().trim().equals("")) {
						boolean result2 = false;
						if(rule.getSecEle() != null)
							result2 = checkCondition(value, rule.getValue2(), rule.getCondtion2(), 
								rule.getSecEle().getType(), rule.getSecEle().getDataType());
						if(rule.getGroupEle() != null)
							result2 = checkCondition(value, rule.getValue2(), rule.getCondtion2(), 
								rule.getGroupEle().getType(), rule.getGroupEle().getDataType());
						if(rule.getName().equals("and"))
							result = result && result2;
						else
							result = result || result2;
					}
				}
			}else {
				List<CrfRuleWithOther> otherCrf = rule.getOtherCrf();
				for(CrfRuleWithOther scrwo : otherCrf) {
					String v2 = null;
					if(scrwo.getCrf().getId() == crfId) {
						
//						if(scrwo.getSecEle() != null) v2 = secmap.get(scrwo.getSecEle().getId());
//						if(scrwo.getGroupEle() != null) v2 = groupmap2.get("g_"+scrwo.getGroupEle().getId()+"_"+scrwo.getGroupEle().getName()+"_"+scrwo.getRowNo());
						
						if(scrwo.getSecEle() != null) v2 = oeleMap.get(scrwo.getSecEle().getId()+"_"+scrwo.getSecEle().getName());
						if(scrwo.getGroupEle() != null) v2 = oeleMap.get(scrwo.getGroupEle().getId()+"_"+scrwo.getSecEle().getName());
					}else {
						
//						//check with other crf  logic
//						VolunteerPeriodCrf vpc = studyDao.volunteerPeriodCrf(vpcId);
//						if(scrwo.getSecEle() != null) v2 = crfDAO.studyCrfSectionElementData(vpc, scrwo.getSecEle());
//						if(scrwo.getGroupEle() != null) v2 = crfDAO.studyCrfGroupElementData(vpc, scrwo.getGroupEle(), "g_"+scrwo.getGroupEle().getId()+"_"+scrwo.getGroupEle().getName()+"_"+scrwo.getRowNo());
					}
					if(v2 != null) {
						boolean result2 = checkCondition(value, v2, scrwo.getCondtion(), 
								rule.getSecEle().getType(), rule.getSecEle().getDataType());
						if(scrwo.getNcondtion().equals("and")) 
							result = result && result2;
						else
							result = result || result2;
					}
				}
			}
			
			if(result) {
				System.out.println(rule.getId());
				if(rule.getMessage() != null)
					return rule.getMessage();
				else
					rule.getRuleDesc();
			}
		}		
		return "";
	}
	
	@Override
	public String studyCrfGroupRuelCheck(Long crfId,  Long dbId, String id, String value, String values, StudyMaster sm) {
		//for Group element validation
		
		//map with dbids and values
		Map<Long, String> secmap = new HashMap<>();
		Map<Long, String> groupmap = new HashMap<>();
		
		//map with ids and values
		Map<String, String> secmap2 = new HashMap<>();
		Map<String, String> groupmap2 = new HashMap<>();
		
		if(!id.equals(values)) {
			String[] arr = values.split("@@@##");
			for(String s : arr) {
				String[] s1 = s.split("@@##");
				
				String eleid = s1[0]; // id_name
				String[] dbidName = eleid.split("_");
				if(eleid.substring(0, 2).equals("g_")) {
					groupmap2.put(s1[0], s1[1]);
					groupmap.put(Long.parseLong(dbidName[1]), s1[1]);
				}else {
					secmap2.put(s1[0], s1[1]);
					secmap.put(Long.parseLong(dbidName[0]), s1[1]);
				}
			}
		}
		
		List<CrfRule> rules = crfDAO.studyCrfRuleWithCrfAndSubElementsWithSecEleId(dbId,  "group");
		
		
		for(CrfRule rule : rules) {
			boolean result = false;
			if(rule.getCompareWith().equals("userInput")) {
				if(!rule.getCondtion1().equals("-1")) {
					if(rule.getSecEle() != null)
						result = checkCondition(value, rule.getValue1(), rule.getCondtion1(), 
							rule.getSecEle().getType(), rule.getSecEle().getDataType());
					if(rule.getGroupEle() != null)
						result = checkCondition(value, rule.getValue1(), rule.getCondtion1(), 
							rule.getGroupEle().getType(), rule.getGroupEle().getDataType());
				}
				if(!rule.getCondtion2().equals("-1")) {
					boolean result2 = false;
					if(rule.getSecEle() != null)
						result2 = checkCondition(value, rule.getValue2(), rule.getCondtion2(), 
							rule.getSecEle().getType(), rule.getSecEle().getDataType());
					if(rule.getGroupEle() != null)
						result2 = checkCondition(value, rule.getValue2(), rule.getCondtion2(), 
							rule.getGroupEle().getType(), rule.getGroupEle().getDataType());
					if(rule.getName().equals("and"))
						result = result && result2;
					else
						result = result || result2;
				}
			}else {
				List<CrfRuleWithOther> otherCrf = rule.getOtherCrf();
				for(CrfRuleWithOther scrwo : otherCrf) {
					String v2 = null;
					if(scrwo.getCrf().getId() == crfId) {
						if(scrwo.getSecEle() != null) v2 = secmap.get(scrwo.getSecEle().getId());
						if(scrwo.getGroupEle() != null) v2 = groupmap2.get("g_"+scrwo.getGroupEle().getId()+"_"+scrwo.getGroupEle().getName()+"_"+scrwo.getRowNo());
						
					}else {
						//check with other crf  logic
//						VolunteerPeriodCrf vpc = studyDao.volunteerPeriodCrf(vpcId);
//						if(scrwo.getSecEle() != null) v2 = crfDAO.studyCrfSectionElementData(vpc, scrwo.getSecEle());
//						if(scrwo.getGroupEle() != null) v2 = crfDAO.studyCrfGroupElementData(vpc, scrwo.getGroupEle(), "g_"+scrwo.getGroupEle().getId()+"_"+scrwo.getGroupEle().getName()+"_"+scrwo.getRowNo());
					}
					if(v2 != null) {
					boolean result2 = checkCondition(value, v2, scrwo.getCondtion(), 
							rule.getSecEle().getType(), rule.getSecEle().getDataType());
					if(scrwo.getNcondtion().equals("and")) 
						result = result && result2;
					else
						result = result || result2;
					}
				}
			}
			
			
			
			if(result) {
				if(rule.getMessage() != null)
					return rule.getMessage();
				else
					rule.getRuleDesc();
			}
		}		
		return "";
	}
	


	private boolean checkCondition(String value, String value1, String condtion, String type, String dataType) {
		boolean flag = false;
//		text/textArea/radio/select/checkBox/
		if(type.equals("text") || type.equals("textArea") || type.equals("select") || type.equals("checkBox")) {
			switch (condtion) {
			case "eq":
				if(value.equals(value1)) 
					flag = true;
				break;
			case "ne":
				if(!value.equals(value1)) 
					flag = true;
				break;
			case "le":
				if(dataType.equals("Number")) {
					if(Double.parseDouble(value) <  Double.parseDouble(value1))
						flag = true;
				}else {
					if(value.length() < Double.parseDouble(value1))
						flag = true;
				}
				break;
			case "leq":
				if(dataType.equals("Number")) {
					if(Double.parseDouble(value) <=  Double.parseDouble(value1))
						flag = true;
				}else {
					if(value.length() <= Double.parseDouble(value1))
						flag = true;
				}
				break;
			case "gt":
				if(dataType.equals("Number")) {
					
					if(Double.parseDouble(value) >  Double.parseDouble(value1))
						flag = true;
				}else {
					if(value.length() > Double.parseDouble(value1))
						flag = true;
				}
				break;
			case "gtq":
				if(dataType.equals("Number")) {
					if(Double.parseDouble(value) >=  Double.parseDouble(value1))
						flag = true;
				}else {
					if(value.length() >= Double.parseDouble(value1))
						flag = true;
				}
				break;
			case "gte":
				if(dataType.equals("Number")) {
					if(Double.parseDouble(value) >=  Double.parseDouble(value1))
						flag = true;
				}else {
					if(value.length() >= Double.parseDouble(value1))
						flag = true;
				}
				break;
			default:
				break;
			}
		}else {
//			date/dateAndTime/
			if(type.equals("date") || type.equals("date")) {
				SimpleDateFormat sdfo = null;
				if(type.equals("date")) sdfo = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
				else sdfo = new SimpleDateFormat(environment.getRequiredProperty("dateTimeFormat"));
					try {
						Date d1 = sdfo.parse(value); 
				        Date d2 = sdfo.parse(value1);
				        switch (condtion) {
							case "eq":
								if (d1.compareTo(d2) == 0) { 
									flag = true;
						        }
								break;
							case "ne":
								if (d1.compareTo(d2) != 0) { 
									flag = true;
						        } 
								break;
							case "le":
								if (d1.compareTo(d2) < 0) { 
									flag = true;
						        } 
								break;
							case "leq":
								if (d1.compareTo(d2) < 0 || d1.compareTo(d2) == 0) { 
									flag = true;
						        }
								break;
							case "gt":
								if (d1.compareTo(d2) > 0) { 
									flag = true;
						        }
								break;
							case "gtq":
								if (d1.compareTo(d2) > 0 || d1.compareTo(d2) >= 0) { 
									flag = true;
						        }
								break;
							case "gte":
								if (d1.compareTo(d2) > 0 || d1.compareTo(d2) >= 0) { 
									flag = true;
						        }
								break;
						}
				        
					}catch (Exception e) {
						e.printStackTrace();	
					}
			}
			
		}
		return flag;
	}

	@Override
	public List<String> userdiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc) {
		List<CrfDescrpency> list = crfDAO.userdiscrepencyInfo(crf, vpc);
		List<String> s = new ArrayList<>();
		for(CrfDescrpency sd : list) {
			if(!s.contains(sd.getKayName())) s.add(sd.getKayName());
		}
		return s;
	}

	@Override
	public List<String> reviewerdiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc) {
		List<CrfDescrpency> list = crfDAO.reviewerdiscrepencyInfo(crf, vpc);
		List<String> s = new ArrayList<>();
		for(CrfDescrpency sd : list) {
			if(!s.contains(sd.getKayName())) s.add(sd.getKayName());
		}
		return s;
	}

	@Override
	public List<String> closeddiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc) {
		List<CrfDescrpency> list = crfDAO.closeddiscrepencyInfo(crf, vpc);
		List<String> s = new ArrayList<>();
		for(CrfDescrpency sd : list) {
			if(!s.contains(sd.getKayName())) s.add(sd.getKayName());
		}
		return s;
	}

	@Override
	public List<String> onHolddiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc) {
		List<CrfDescrpency> list = crfDAO.onHolddiscrepencyInfo(crf, vpc);
		List<String> s = new ArrayList<>();
		for(CrfDescrpency sd : list) {
			if(!s.contains(sd.getKayName())) s.add(sd.getKayName());
		}
		return s;
	}

	@Override
	public List<String> alldiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc) {
		List<CrfDescrpency> list = crfDAO.alldiscrepencyInfo(crf, vpc);
		List<String> s = new ArrayList<>();
		for(CrfDescrpency sd : list) {
			if(!s.contains(sd.getKayName())) s.add(sd.getKayName());
		}
		return s;
	}
	
	@Override
	public List<String> opendiscrepencyInfo(Crf crf, VolunteerPeriodCrf vpc) {
		List<CrfDescrpency> list = crfDAO.opendiscrepencyInfo(crf, vpc);
		List<String> s = new ArrayList<>();
		for(CrfDescrpency sd : list) {
			if(!s.contains(sd.getKayName())) s.add(sd.getKayName());
		}
		return s;
	}

	@Override
	public List<CrfDescrpency> alldiscrepency(Crf crf, VolunteerPeriodCrf vpc) {
		return crfDAO.alldiscrepencyInfo(crf, vpc);
	}

	@Override
	public List<CrfDescrpency> alldiscrepencyEleInfo(Crf crf, VolunteerPeriodCrf vpc, String keyName) {
		return crfDAO.alldiscrepencyEleInfo(crf, vpc, keyName);
	}
	
	@Override
	public CrfSectionElement studyCrfSectionElement(Long id) {
		return crfDAO.studyCrfSectionElement(id);
	}

	@Override
	public CrfSectionElementData studyCrfSectionElementData(VolunteerPeriodCrf vpc, CrfSectionElement ele,
			String keyName) {
		return crfDAO.studyCrfSectionElementData(keyName, vpc, ele);
	}

	@Override
	public CrfSectionElementData studyCrfSectionElementData(Long dataId) {
		return crfDAO.studyCrfSectionElementData(dataId);
	}

	@Override
	public boolean saveStudyCrfDescrpency(CrfDescrpency scd) {
		return crfDAO.saveStudyCrfDescrpency(scd);
	}

	@Override
	public CrfGroupElement studyCrfGroupElement(Long id) {
		// TODO Auto-generated method stub
		return crfDAO.studyCrfGroupElement(id);
	}

	@Override
	public CrfGroupElementData studyCrfGroupElementData(VolunteerPeriodCrf vpc, CrfGroupElement ele,
			String keyName) {
		return crfDAO.studyCrfGroupElementData(keyName, vpc, ele);
	}

	@Override
	public CrfGroupElementData studyCrfGroupElementData(Long dataId) {
		return crfDAO.studyCrfGroupElementData(dataId);
	}

	@Override
	public List<CrfDescrpency> alldiscrepencyEleInfo(Crf crf, VolunteerPeriodCrf vpc, String keyName,
			String condition) {
		return crfDAO.alldiscrepencyEleInfo(crf, vpc, keyName, condition);
	}

	@Override
	public void saveDataSet(DataSet ds) {
		crfDAO.saveDataSet(ds);
	}

	@Override
	public List<DataSet> findAllDataSets() {
		return crfDAO.findAllDataSets();
	}
	
	@Override
	public List<DataSet> findAllDataSets(StudyMaster sm) {
		return crfDAO.findAllDataSets(sm);
	}

	@Override
	public DataSet dataSet(Long id) {
		return crfDAO.dataSet(id);
	}

	@Override
	public void updateDataSet(DataSet dataSet) {
		crfDAO.updateDataSet(dataSet);
	}

	@Override
	public DataSet fullDataSetInfo(Long id) {
		return crfDAO.fullDataSetInfo(id);
	}

	@Override
	public VolunteerPeriodCrf volunteerPeriodCrf(DataSetPhasewiseCrfs crf, StudyPeriodMaster priod, Volunteer v) {
		return crfDAO.volunteerPeriodCrf(crf, priod, v);
	}

	@Override
	public CrfSectionElementData studyCrfSectionElementData(VolunteerPeriodCrf vcp, CrfSectionElement ele) {
		return crfDAO.studyCrfSectionElementData2(vcp, ele);
	}

	@Override
	public CrfGroupElementData studyCrfSectionElementData(VolunteerPeriodCrf vcp, CrfGroupElement ele,
			String keyName) {
		return crfDAO.studyCrfSectionElementData(vcp, ele, keyName);
	}

	@Override
	public void uploadCrfCaliculationFile(String path, String username) throws ParserConfigurationException, SAXException, IOException {
		//create document builder factory
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						
				//create Document Builder
				DocumentBuilder builder = factory.newDocumentBuilder();
				
				//get inputstrem of xml file
				//file from folder location
				String filePath = path;
				File xmlFile = new File(filePath);
		        //file from class path location
		        ClassLoader cl = DOMReader.class.getClassLoader();
				InputStream is = cl.getResourceAsStream("com/covide/crf/xmlfiles/caliculation2.xml");
				
				//parse xml file and  get Document object
				//Document document = builder.parse(is);
				
				
				Document document = builder.parse(xmlFile);
				
				
				//get Root element of xml doc
				Element rootElement = document.getDocumentElement();
				
				//get <Eform> tag value
				Node first = rootElement.getFirstChild();
				Node sibling = first.getNextSibling();
				Node finalNode = sibling.getFirstChild();
				String value = finalNode.getNodeValue();
				
				System.out.println(value);
				 NodeList nodeList = document.getElementsByTagName("EleCaliculation");
		         List<EleCaliculation> empList = new ArrayList<EleCaliculation>();
		         for (int i = 0; i < nodeList.getLength(); i++) {
		             empList.add(getEleCaliculation(nodeList.item(i)));
		         }
		         
		         
		         Crf crf = crfDAO.crfByName(value);
		         List<CrfEleCaliculation> list = new ArrayList<>();
		         //lets print Employee list information
		         for (EleCaliculation emp : empList) {
		             System.out.println(emp.toString());
		             CrfEleCaliculation cec = new CrfEleCaliculation();
		             cec.setResultField(emp.getResultField());
		             cec.setCaliculation(emp.getRule());
		             cec.setCrf(crf);
		             cec.setCreatedOn(new Date());
		             cec.setCreatedBy(username);
		             list.add(cec);
		         }
		         
		         crfDAO.saveCrfEleCaliculationList(list);
	}

	private static EleCaliculation getEleCaliculation(Node node) {
        //XMLReaderDOM domReader = new XMLReaderDOM();
		EleCaliculation emp = new EleCaliculation();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            emp.setResultField(getTagValue("resultField", element));
            emp.setRule(getTagValue("rule", element));
//            emp.setAge(Integer.parseInt(getTagValue("age", element)));
        }

        return emp;
    }
	
	private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

	@Override
	public List<CrfEleCaliculation> crfEleCaliculationList() {
		List<CrfEleCaliculation> list = crfDAO.crfEleCaliculationList();
		return list;
	}

	
	@Override
	public void copyCrfCaliculationFromLib(StudyMaster sm) {
		List<CrfEleCaliculation> stdclList = new ArrayList<>();
		
		List<Crf> crfs = crfDAO.findAllStudyCrfsWithSubElements(sm);
		for(Crf crf : crfs) {
			List<CrfEleCaliculation> list = crfDAO.libcrfEleCaliculationList(crf);
			for(CrfEleCaliculation c : list) {
				if(c.getStatus().equals("active")) {
					CrfEleCaliculation sce = new CrfEleCaliculation(
							crf, c.getResultField(), c.getCaliculation(), "active");
					stdclList.add(sce);
				}
			}
		}
		
		
		crfDAO.saveCrfEleCaliculationList(stdclList);
		studyDao.updateStudy(sm);
	}

	@Override
	public Map<String, String> caliculationFieldSec(Crf crf) {
		List<CrfEleCaliculation> list = crfDAO.crfEleCaliculationList(crf);
		Map<String, String> map  = new HashMap<>();
		for(CrfEleCaliculation c : list) {
			String value = c.getCaliculation();
			String listofFields = getCrfEleCaliculationFields(value);
			if(listofFields != null)
				map.put(c.getResultField(), listofFields);
		}
		Map<String, Long> eleIds = new HashMap<>();
		List<CrfSection> sec = crf.getSections();
		for(CrfSection s : sec) {
			List<CrfSectionElement> eles = s.getElement();
			for(CrfSectionElement e : eles) {
				eleIds.put(e.getName(), e.getId());
			}
		}
		Map<String, String> result  = new HashMap<>();
		for(Map.Entry<String, String> m : map.entrySet()) {
			String[] s = m.getValue().split(",");
			StringBuffer sb = new StringBuffer();
			boolean flag = false;
			for(String ss : s) {
				if(eleIds.containsKey(ss)) {
					if(flag) {
						sb.append(",").append(eleIds.get(ss)+"_"+ss);
					}else {
						sb.append(eleIds.get(ss)+"_"+ss);
						flag = true;
					}
				}
			}
			result.put(m.getKey(), sb.toString());
		}
		return result;
	}

	private String getCrfEleCaliculationFields(String value) {
		Set<String> eles = new HashSet<>();
		value = value.replaceAll("ELE", "@");
		value = value.replaceAll("],", "#");
		
		StringBuilder sb =  new StringBuilder();
		boolean flag = false;
		for(int i=0; i< value.length(); i++) {
			if(value.charAt(i) == '@') {
				flag = true; i= i+2;
			}else if(value.charAt(i) == '#') {
				flag = false;
				eles.add(sb.toString());
				sb = new StringBuilder();
			}
			if(flag) {
				sb.append(value.charAt(i));
			}
		}
		sb = new StringBuilder();
		flag = false;
		for(String s : eles) {
			if(flag)
				sb.append(",").append(s);
			else {
				sb.append(s);
				flag = true;
			}
		}
		
//		boolean flag = false;
//		if(value.contains("sum")) {
//			sb.append(getSUMFiels(value));
//			flag = true;
//		}else if(value.contains("round")) {
//			sb.append(getSUMFiels(value));
//			flag = true;
//		}else if(value.contains("avg")) {
//			if(flag) sb.append(",");
//			sb.append(getAVGFiels(value));
//			flag = true;
//		}else if(value.contains("div")) {
//			if(flag) sb.append(",");
//			sb.append(getDIVFiels(value));
//			flag = true;
//		}else if(value.contains("mul")) {
//			if(flag) sb.append(",");
//			sb.append(getMULFiels(value));
//			flag = true;
//		}else if(value.contains("pow")) {
//			if(flag) sb.append(",");
//			sb.append(getPOWFiels(value));
//			flag = true;
//		}else if(value.contains("sqrt")) {
//			if(flag) sb.append(",");
//			sb.append(getSQRTFiels(value));
//			flag = true;
//		}else if(value.contains("cbrt")) {
//			if(flag) sb.append(",");
//			sb.append(getCBRTFiels(value));
//			flag = true;
//		}else {
//			sb.append(getFiels(value));
//			flag = true;
//		}
		return sb.toString();
	}
	private String getFiels(String value) {
		StringBuilder sb = new StringBuilder();
		sb.append("");
		boolean flag = true;
		String[] ss = value.split("],");
		for(String e : ss) {
			if(e.contains("ELE[")) {
				String se =  e.substring(e.lastIndexOf("[")+1);
				if(!se.equals("")) {
					if(flag) {
						sb.append(se); flag  = false;
					}else sb.append(",").append(se);
				}
			}
		}
		return sb.toString();
	}
	
	private String getCBRTFiels(String value) {
		int i = value.lastIndexOf("(")+1;
		int li = value.lastIndexOf(")");
		
		return getFiels(value.substring(i, li));
	}
	private String getSQRTFiels(String value) {
		int i = value.lastIndexOf("(")+1;
		int li = value.lastIndexOf(")");
		return getFiels(value.substring(i, li));
	}
	private String getPOWFiels(String value) {
		int i = value.lastIndexOf("(")+1;
		int li = value.lastIndexOf(")");
		return getFiels(value.substring(i, li));
	}
	private String getMULFiels(String value) {
		int i = value.lastIndexOf("(")+1;
		int li = value.lastIndexOf(")");
		return getFiels(value.substring(i, li));
	}
	private String getDIVFiels(String value) {
		int i = value.lastIndexOf("(")+1;
		int li = value.lastIndexOf(")");
		return getFiels(value.substring(i, li));
	}
	
	private String getAVGFiels(String value) {
		int i = value.lastIndexOf("(")+1;
		int li = value.lastIndexOf(")");
		return getFiels(value.substring(i, li));
	}
	
	private String getSUMFiels(String value) {
		int i = value.indexOf("(")+1;
		int li = value.lastIndexOf(")");
		return getFiels(value.substring(i, li));
	}
	
	@Override
	public String studyCrfElementCalculationValue(StudyMaster sm, Long crfId, String resultFiled, String fieldAndVales) {
		try {
			Map<String, Double> values  = new HashMap<>();
			Map<String, Double> eleValues  = new HashMap<>();
			String[] fvs = fieldAndVales.split(",");
			for(String s : fvs) {
				String[] se = s.split("-");
				String ele = se[0];
				String[] sss = s.split("_");
				String id = sss[0];
				ele = ele.replace(id+"_", "");
				values.put(ele, Double.parseDouble(se[1]));
				eleValues.put("ELE["+ele+"],", Double.parseDouble(se[1]));
			}
			
			CrfEleCaliculation  scec= crfDAO.studyCrfEleCaliculation(sm, crfId, resultFiled);
			String s = scec.getCaliculation();
			
			 // create a script engine manager
//		    ScriptEngineManager factory = new ScriptEngineManager();
		    // create a JavaScript engine
//		    ScriptEngine engine = factory.getEngineByName("JavaScript");
		    // evaluate JavaScript code from String
		    
			for(Map.Entry<String, Double> d : eleValues.entrySet()) {
				s = s.replace(d.getKey(), ""+d.getValue());		
			}
			System.out.println(s);
			String result  = replaceChars(s);
			result  = splitFormaula(result);
			
			return result;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "";
		}
		
	
	}
	
	private String splitFormaula(String string) {
		int count = 1;
		Map<Integer, String> map = new HashMap<>();
		map.put(count, string);
		int index = 0;
		while(index < string.length()) {
			if(string.charAt(index) == 'R' || string.charAt(index) == 'S' || string.charAt(index) == 'A' || 
					string.charAt(index) == 'P' || string.charAt(index) == 's' || string.charAt(index) == 'C') {
				System.out.println(string.substring(index+2));
				String s = string.substring(index);
				int open = 0;
				int close = 0;
				int endIndex = 0;
				System.out.println(s);
				for(int i = 0; i<s.length(); i++) {
					if(s.charAt(i) == '(') {
//						count ++;
						open ++;
					}else if(s.charAt(i) == ')') {
						close ++;
						if(open == close) {
								s = s.substring(1, i);
								endIndex = i+1;
								break;
						}
					}
				}
				
				String tempTotal = string.charAt(index) + s+")";
				map.put(++count, tempTotal);
				index += endIndex; 
			}else				
				index++;
		}
		
		
		int listSize = 1;
		while(listSize < map.size()) {
			listSize ++;
			map = splitFormaulaList(map, listSize);
			
		}
		
		Map<Integer, String> map2 = new HashMap<>(map);
//		System.out.println(map.size());
//		for(Map.Entry<Integer, String> m : map.entrySet()) {
//			System.out.println(m.getKey() + "  -  " + m.getValue());
//		}
		String result = caliculateValue(map, map2);
		return result;
	}
	
	private String caliculateValue(Map<Integer, String> map, Map<Integer, String> mapt) {
		int index = map.size();
		while(index > 0) {
			String prs0 = mapt.get(index);
			String ps = map.get(index);
			System.out.println(ps);
			String value = findValue(ps);
			map.put(index, value);
			index--;
			if(index > 0) {
				String prs = mapt.get(index);
				if(prs.contains(prs0)) {
					prs = prs.replace(prs0, value);
					map.put(index, prs);
				}
				
				for(Map.Entry<Integer, String> m : mapt.entrySet()) {
					if(prs.contains(m.getValue())) {
						prs = prs.replace(m.getValue(), map.get(m.getKey()));
						map.put(index, prs);
					}
				}
			}
		}
		System.out.println(map.size());
		for(Map.Entry<Integer, String> m : map.entrySet()) {
			System.out.println(m.getKey() + "  -  " + m.getValue());
		}
		return map.get(1);
	}
	
	private String findValue(String s) {
		 // create a script engine manager
	    ScriptEngineManager factory = new ScriptEngineManager();
	    // create a JavaScript engine
	    ScriptEngine engine = factory.getEngineByName("JavaScript");
	    // evaluate JavaScript code from String
	    
		while (s.contains("S(")) {
			String su = s.substring(2, s.length()-1);
			su = convertToString(su);
			s = sumof(su);
//			s = s.replace(s, result);
			System.out.println(s);
		}
		while (s.contains("A(")) {
			String su = s.substring(2, s.length()-1);
			su = convertToString(su);
			s = avgof(su);
//			s = s.replace(s, result);
			System.out.println(s);
		}
		while (s.contains("P(")) {
			String su = s.substring(2, s.length()-1);
			su = convertToString(su);
			s = powof(su);
//			s = s.replace(s, result);
			System.out.println(s);
		}
		while (s.contains("s(")) {
			String su = s.substring(2, s.length()-1);
			su = convertToString(su);
			s = sqrtof(su);
//			s = s.replace(s, result);
			System.out.println(s);
		}
		while (s.contains("C(")) {
			String su = s.substring(2, s.length()-1);
			su = convertToString(su);
			
			s = cbrtof(su);
//			s = s.replace(s, result);
			System.out.println(s);
		}
		while (s.contains("R(")) {
			String su = s.substring(2, s.length()-1);
			su = convertToString(su);
			s = round(su);
//			s = s.replace(s, result);
			System.out.println(s);
		}
		Object obj;
		try {
			obj = engine.eval(s);
			 System.out.println( obj );
		    String result =  obj.toString();
			return result;
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return s;
		}
	   
	}
	private String round(String s) {
		Double d = 0.0d;
		try {
			d = (double) Math.round(Double.parseDouble(s));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return d+ "";

	}
	
	private String cbrtof(String s) {
		Double d = 0.0d;
		try {
			d = Math.cbrt(Double.parseDouble(s));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return d+ "";
	}
	private String sqrtof(String s) {
		Double d = 0.0d;
		try {
			d = Math.sqrt(Double.parseDouble(s));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return d+ "";
	}
	
	private String powof(String s) {
		String[] ss = s.split(",");
		Double d = 0.0d;
		try {
			d = Math.pow(Double.parseDouble(ss[0]), Double.parseDouble(ss[0]));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return d+ "";
	}
	
	private String avgof(String s) {
		String[] ss = s.split(",");
		Double d = 0.0d;
		for(String sss : ss)	d = d + Double.parseDouble(sss);
		return (d/ss.length) + "";
	}
	
	private String sumof(String s) {
		String[] ss = s.split(",");
		Double d = 0.0d;
		for(String sss : ss)	d = d + Double.parseDouble(sss);
		return d + "";
	}
	private String convertToString(String s) {
		if(s.contains("(")) {
			s = simpleCaliculate(s);
		}
		// create a script engine manager
	    ScriptEngineManager factory = new ScriptEngineManager();
	    // create a JavaScript engine
	    ScriptEngine engine = factory.getEngineByName("JavaScript");
	    // evaluate JavaScript code from String
		Object obj;
		try {
			obj = engine.eval(s);
			 System.out.println( obj );
		    String result =  obj.toString();
			return result;
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return s;
		}
	}
	private String simpleCaliculate(String s) {
		String old = s;
		if(s.contains("(")) {
			int open = 0;
			int close = 0;
			System.out.println(s);
			for(int i = 0; i<s.length(); i++) {
				if(s.charAt(i) == '(') {
//					count ++;
					open ++;
				}else if(s.charAt(i) == ')') {
					close ++;
					if(open == close) {
							s = s.substring(s.indexOf('(')+1, i);
							break;
					}
				}
			}
			String s1 = simpleCaliculate(s);
			old = old.replace("("+s+")", s1);
		}else {
			return convertToString(s);
		}
		return old;
	}
	private Map<Integer, String> splitFormaulaList(Map<Integer, String> map, int listSize) {
		int count =map.size();
		String string = map.get(listSize);
		string = string.substring(2, string.length() -1);
		int index = 0;
		while(index < string.length()) {
			if(string.charAt(index) == 'R' || string.charAt(index) == 'S' || string.charAt(index) == 'A' || 
					string.charAt(index) == 'P' || string.charAt(index) == 's' || string.charAt(index) == 'C') {
				String s = string.substring(index);
				int open = 0;
				int close = 0;
				int endIndex = 0;
				for(int i = 0; i<s.length(); i++) {
					if(s.charAt(i) == '(') {
//						count ++;
						open ++;
					}else if(s.charAt(i) == ')') {
						close ++;
						if(open == close) {
								s = s.substring(1, i);
								endIndex = i+1;
								break;
						}
					}
				}
				
				String tempTotal = string.charAt(index) + s+")";
//				System.out.println(tempTotal);
//				System.out.println((count+1)  + "   " + tempTotal);
				if(!map.containsValue(tempTotal)) {
					map.put(map.size()+1, tempTotal);
				}
				String temp = tempTotal.substring(2);
				if(temp.contains("R") || temp.contains("S") || temp.contains("A") ||
						temp.contains("p") || temp.contains("s") || temp.contains("C")) {
					map = splitFormaulaList(map, count);
				}
				index += endIndex; 
			}else				
				index++;
		}
		return map;
	}
	private String replaceChars(String string) {
		System.out.println(string);
		if(string.contains("round(")) {
			string = string.replaceAll("round", "R");
		}
		if(string.contains("sum(")) {
			string = string.replaceAll("sum", "S");
		}
		if(string.contains("avg(")) {
			string = string.replaceAll("avg", "A");
		}
		if(string.contains("pow(")) {
			string = string.replaceAll("pow", "P");
		}
		if(string.contains("sqrt(")) {
			string = string.replaceAll("sqrt", "s");
		}
		if(string.contains("cbrt(")) {
			string = string.replaceAll("cbrt", "C");
		}
		System.out.println(string);
		return string;
	}
	
	
	
	@Override
	public List<CrfDescrpency> allOpendStudydiscrepency(StudyMaster sm, String username) {
		return crfDAO.allOpendStudydiscrepency(sm, username);
	}

	@Override
	public List<StudyAccessionCrfDescrpency> allStudyAccessionCrfDescrpency(StudyMaster sm, String username) {
		return crfDAO.allStudyAccessionCrfDescrpency(sm, username);
	}
	
	
	@Override
	public StudyAccessionCrfDescrpency studyAccessionCrfDescrpency(Long id) {
		// TODO Auto-generated method stub
		return crfDAO.studyAccessionCrfDescrpency(id);
	}

	@Override
	public CrfDescrpency studyCrfDescrpency(Long id) {
		return crfDAO.studyCrfDescrpency(id);
	}

	@Override
	public void updateStudyCrfDescrpency(CrfDescrpency scd) {
		crfDAO.updateStudyCrfDescrpency(scd);
	}
	
	
	@Override
	public String sectionEleSelect1(Long id) {
		List<CrfSectionElement> list = crfDAO.sectionElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='secEleId1' id='secEleId1' class='form-control' onchange='secEleSelection1(this.id, this.value)'>")
		.append("<option value='-1' selected='selected' >--Select--</option>");
		for(CrfSectionElement ele : list) {
			sb.append("<option value='"+ele.getId()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	@Override
	public String groupEleSelect1(Long id) {
		List<CrfGroupElement> list = crfDAO.groupElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='groupEleId1' id='groupEleId1' class='form-control' onchange='groupEleSelection1(this.id, this.value)'>")
		.append("<option value='-1' selected='selected'>--Select--</option>");
		for(CrfGroupElement ele : list) {
			sb.append("<option value='"+ele.getId()+","+ele.getGroup().getMaxRows()+","+ele.getGroup().getMaxColumns()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	@Override
	public String sectionEleSelect2(Long id) {
		List<CrfSectionElement> list = crfDAO.sectionElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='secEleId2' id='secEleId2' class='form-control' onchange='secEleSelection2(this.id, this.value)'>")
		.append("<option value='-1' selected='selected' >--Select--</option>");
		for(CrfSectionElement ele : list) {
			sb.append("<option value='"+ele.getId()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	@Override
	public String groupEleSelect2(Long id) {
		List<CrfGroupElement> list = crfDAO.groupElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='groupEleId2' id='groupEleId2' class='form-control' onchange='groupEleSelection2(this.id, this.value)'>")
		.append("<option value='-1' selected='selected'>--Select--</option>");
		for(CrfGroupElement ele : list) {
			sb.append("<option value='"+ele.getId()+","+ele.getGroup().getMaxRows()+","+ele.getGroup().getMaxColumns()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	@Override
	public String sectionEleSelect3(Long id) {
		List<CrfSectionElement> list = crfDAO.sectionElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='secEleId3' id='secEleId3' class='form-control' onchange='secEleSelection3(this.id, this.value)'>")
		.append("<option value='-1' selected='selected' >--Select--</option>");
		for(CrfSectionElement ele : list) {
			sb.append("<option value='"+ele.getId()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	@Override
	public String groupEleSelect3(Long id) {
		List<CrfGroupElement> list = crfDAO.groupElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='groupEleId3' id='groupEleId3' class='form-control' onchange='groupEleSelection3(this.id, this.value)'>")
		.append("<option value='-1' selected='selected'>--Select--</option>");
		for(CrfGroupElement ele : list) {
			sb.append("<option value='"+ele.getId()+","+ele.getGroup().getMaxRows()+","+ele.getGroup().getMaxColumns()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	@Override
	public String sectionEleSelect4(Long id) {
		List<CrfSectionElement> list = crfDAO.sectionElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='secEleId4' id='secEleId4' class='form-control' onchange='secEleSelection4(this.id, this.value)'>")
		.append("<option value='-1' selected='selected' >--Select--</option>");
		for(CrfSectionElement ele : list) {
			sb.append("<option value='"+ele.getId()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	@Override
	public String groupEleSelect4(Long id) {
		List<CrfGroupElement> list = crfDAO.groupElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='groupEleId4' id='groupEleId4' class='form-control' onchange='groupEleSelection4(this.id, this.value)'>")
		.append("<option value='-1' selected='selected'>--Select--</option>");
		for(CrfGroupElement ele : list) {
			sb.append("<option value='"+ele.getId()+","+ele.getGroup().getMaxRows()+","+ele.getGroup().getMaxColumns()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	@Override
	public String saveCrfDateComparison(String username, HttpServletRequest request) {
		try {
			CrfDateComparison data = new CrfDateComparison();
			data.setCreatedBy(username);
			data.setCreatedOn(new Date());
			data.setName(request.getParameter("name"));
			data.setDescreption(request.getParameter("descreption"));
			data.setSourcrf(crfDAO.getCrf(Long.parseLong(request.getParameter("crf"))));
			if(!request.getParameter("secEleId").equals("-1"))
				data.setSecEle(crfDAO.sectionElement(Long.parseLong(request.getParameter("secEleId").toString())));
			if(!request.getParameter("groupEleId").equals("-1")) {
				data.setGroupEle(crfDAO.groupElement(Long.parseLong(request.getParameter("groupEleId").toString())));
				data.setRowNo(Integer.parseInt(request.getParameter("rowgroupEleId")));
			}
			data.setMessage(request.getParameter("message"));
			data.setCompare(request.getParameter("condition"));
			
			data.setCrf1(crfDAO.getCrf(Long.parseLong(request.getParameter("crf1"))));
			if(!request.getParameter("secEleId1").equals("-1"))
				data.setSecEle1(crfDAO.sectionElement(Long.parseLong(request.getParameter("secEleId1").toString())));
			if(!request.getParameter("groupEleId1").equals("-1")) {
				data.setGroupEle1(crfDAO.groupElement(Long.parseLong(request.getParameter("groupEleId1").toString())));
				data.setRowNo1(Integer.parseInt(request.getParameter("rowgroupEleId1")));
			}
			
			data.setCrf2(crfDAO.getCrf(Long.parseLong(request.getParameter("crf2"))));
			if(!request.getParameter("secEleId2").equals("-1"))
				data.setSecEle2(crfDAO.sectionElement(Long.parseLong(request.getParameter("secEleId2").toString())));
			if(!request.getParameter("groupEleId2").equals("-1")) {
				data.setGroupEle2(crfDAO.groupElement(Long.parseLong(request.getParameter("groupEleId2").toString())));
				data.setRowNo2(Integer.parseInt(request.getParameter("rowgroupEleId2")));
			}
			
			data.setCrf3(crfDAO.getCrf(Long.parseLong(request.getParameter("crf3"))));
			if(!request.getParameter("secEleId3").equals("-1"))
				data.setSecEle3(crfDAO.sectionElement(Long.parseLong(request.getParameter("secEleId3").toString())));
			if(!request.getParameter("groupEleId3").equals("-1")) {
				data.setGroupEle3(crfDAO.groupElement(Long.parseLong(request.getParameter("groupEleId3").toString())));
				data.setRowNo3(Integer.parseInt(request.getParameter("rowgroupEleId3")));
			}
			
			data.setCrf4(crfDAO.getCrf(Long.parseLong(request.getParameter("crf4"))));
			if(!request.getParameter("secEleId4").equals("-1"))
				data.setSecEle4(crfDAO.sectionElement(Long.parseLong(request.getParameter("secEleId4").toString())));
			if(!request.getParameter("groupEleId4").equals("-1")) {
				data.setGroupEle4(crfDAO.groupElement(Long.parseLong(request.getParameter("groupEleId4").toString())));
				data.setRowNo4(Integer.parseInt(request.getParameter("rowgroupEleId4")));
			}
			crfDAO.saveCrfDateComparison(data);
			return data.getName();
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<CrfDateComparison> studyCrfDateComparisonAndSubElements(Crf crf) {
		List<CrfDateComparison> list = crfDAO.studyCrfDateComparisonAndSubElements(crf);
		return list;
	}
	
	@Override
	public String studyCrfSecDateRuelCheck(Long crfId, Long dateRuleDbId, String values) {
		String[] values1 = values.split("@@@");
		//sec ele name and value
		Map<String, String> map = new HashMap<>();
		String v1=null, v2=null, v3=null, v4=null;
		for(String s : values1) {
			String[] s1 = s.split("@@");
			map.put(s1[0], s1[1]);
		}
		
		
		CrfDateComparison rule = crfDAO.byCrfDateComparisonAndSubElementsById(dateRuleDbId);
		if(rule.getCrf1().getId() == crfId) {
			if(rule.getSecEle1() != null)	v1 = map.get(rule.getSecEle1().getId()+"_"+rule.getSecEle1().getName());
			else v1 = map.get("g_"+rule.getGroupEle1()+"_"+rule.getGroupEle1().getId()+"_"+rule.getGroupEle1().getName()+"_"+rule.getRowNo1());
				
		}
		if(rule.getCrf2().getId() == crfId) {
			if(rule.getSecEle2() != null)	v2 = map.get(rule.getSecEle2().getId()+"_"+rule.getSecEle2().getName());
			else v2 = map.get("g_"+rule.getGroupEle2()+"_"+rule.getGroupEle2().getId()+"_"+rule.getGroupEle2().getName()+"_"+rule.getRowNo2());
		}
		if(rule.getCrf3().getId() == crfId) {
			if(rule.getSecEle3() != null)	v3 = map.get(rule.getSecEle3().getId()+"_"+rule.getSecEle3().getName());
			else v3 = map.get("g_"+rule.getGroupEle3()+"_"+rule.getGroupEle3().getId()+"_"+rule.getGroupEle3().getName()+"_"+rule.getRowNo3());
		}
		if(rule.getCrf4().getId() == crfId) {
			if(rule.getSecEle4() != null)	v4 = map.get(rule.getSecEle4().getId()+"_"+rule.getSecEle4().getName());
			else v4 = map.get("g_"+rule.getGroupEle4()+"_"+rule.getGroupEle4().getId()+"_"+rule.getGroupEle4().getName()+"_"+rule.getRowNo4());
		}
		
		if(v1 != null && v2 != null && v3 != null && v4 != null ) {
			try {
				String message = "";
				if(rule.getSecEle() != null) message = rule.getSecEle().getId()+"_"+rule.getSecEle().getName();
				else message = "g_"+rule.getGroupEle().getId()+"_"+rule.getGroupEle().getName()+"_"+rule.getRowNo();
				message = message + "@@" + rule.getMessage();
				SimpleDateFormat sdf = new SimpleDateFormat(environment.getProperty("dateFormat"));
			    
				Date d1 = sdf.parse(v1);
			    Date d2 = sdf.parse(v2);
			    Date d3 = sdf.parse(v3);
			    Date d4 = sdf.parse(v4);
			    long difference = d1.getTime() - d2.getTime();
			    if(difference < 0) difference = (difference *-1);
			    float daysBetween1 = (difference / (1000*60*60*24));
			    difference = d3.getTime() - d4.getTime();
			    if(difference < 0) difference = (difference *-1);
			    float daysBetween2 = (difference / (1000*60*60*24));
			    
			    if(rule.getCompare().equals("eq")) { 
			    	if(daysBetween1 == daysBetween2) return message;
			    }else if(rule.getCompare().equals("ne")) { 
			    	if(daysBetween1 != daysBetween2) return message;
			    }else if(rule.getCompare().equals("le")) { 
			    	if(daysBetween1 < daysBetween2) return message;
			    }else if(rule.getCompare().equals("leq")) {
			    	if(daysBetween1 <= daysBetween2) return message;
			    }else if(rule.getCompare().equals("gt")) {
			    	if(daysBetween1 > daysBetween2) return message;
			    }else if(rule.getCompare().equals("gte")) {
			    	if(daysBetween1 >= daysBetween2) return message;
			    }
			}catch (Exception e) {
				// TODO: handle exception
			}
			return "";
		}else {
			return "";
		}
	}
	
	
	private List<CrfDescrpency> checkStudyCrfRulesDate(List<CrfSectionElementData> sectionData,
			List<CrfGroupElementData> groupoData, List<CrfGroupDataRows> groupoInfo,
			Crf crf, VolunteerPeriodCrf vpc, String username, boolean dataFlag) {
		List<CrfDescrpency> descList = new ArrayList<>();
		Map<String, CrfDescrpency> map = new HashMap<>();
		
		List<CrfDateComparison> list = crfDAO.studyCrfDateComparisonAndSubElements(crf);
		if(list.size() >0) {
			Map<String, String> map2 = new HashMap<>();
			String v1=null, v2=null, v3=null, v4=null;
			for(CrfSectionElementData s : sectionData) 
				map2.put(s.getKayName() , s.getValue());
			for(CrfGroupElementData s : groupoData) 
				map2.put(s.getKayName() , s.getValue());
			
			
			
			
			for(CrfDateComparison rule : list) {
				String message = "";
				if(rule.getCrf1().getId() == crf.getId()) {
					if(rule.getSecEle1() != null)	v1 = map2.get(rule.getSecEle1().getId()+"_"+rule.getSecEle1().getName());
					else v1 = map2.get("g_"+rule.getGroupEle1()+"_"+rule.getGroupEle1().getId()+"_"+rule.getGroupEle1().getName()+"_"+rule.getRowNo1());
						
				}
				if(rule.getCrf2().getId() == crf.getId()) {
					if(rule.getSecEle2() != null)	v2 = map2.get(rule.getSecEle2().getId()+"_"+rule.getSecEle2().getName());
					else v2 = map2.get("g_"+rule.getGroupEle2()+"_"+rule.getGroupEle2().getId()+"_"+rule.getGroupEle2().getName()+"_"+rule.getRowNo2());
				}
				if(rule.getCrf3().getId() == crf.getId()) {
					if(rule.getSecEle3() != null)	v3 = map2.get(rule.getSecEle3().getId()+"_"+rule.getSecEle3().getName());
					else v3 = map2.get("g_"+rule.getGroupEle3()+"_"+rule.getGroupEle3().getId()+"_"+rule.getGroupEle3().getName()+"_"+rule.getRowNo3());
				}
				if(rule.getCrf4().getId() == crf.getId()) {
					if(rule.getSecEle4() != null)	v4 = map2.get(rule.getSecEle4().getId()+"_"+rule.getSecEle4().getName());
					else v4 = map2.get("g_"+rule.getGroupEle4()+"_"+rule.getGroupEle4().getId()+"_"+rule.getGroupEle4().getName()+"_"+rule.getRowNo4());
				}
				
				if(v1 != null && v2 != null && v3 != null && v4 != null ) {
					try {
						String message2 = "";
						if(rule.getSecEle() != null) message2 = rule.getSecEle().getId()+"_"+rule.getSecEle().getName();
						else message2 = "g_"+rule.getGroupEle().getId()+"_"+rule.getGroupEle().getName()+"_"+rule.getRowNo();
						message2 = message2 + "@@" + rule.getMessage();
						SimpleDateFormat sdf = new SimpleDateFormat(environment.getProperty("dateFormat"));
					    
						Date d1 = sdf.parse(v1);
					    Date d2 = sdf.parse(v2);
					    Date d3 = sdf.parse(v3);
					    Date d4 = sdf.parse(v4);
					    long difference = d1.getTime() - d2.getTime();
					    if(difference < 0) difference = (difference *-1);
					    float daysBetween1 = (difference / (1000*60*60*24));
					    difference = d3.getTime() - d4.getTime();
					    if(difference < 0) difference = (difference *-1);
					    float daysBetween2 = (difference / (1000*60*60*24));
					    
					    if(rule.getCompare().equals("eq")) { 
					    	if(daysBetween1 == daysBetween2)  message=message2;
					    }else if(rule.getCompare().equals("ne")) { 
					    	if(daysBetween1 != daysBetween2) message=message2;
					    }else if(rule.getCompare().equals("le")) { 
					    	if(daysBetween1 < daysBetween2) message=message2;
					    }else if(rule.getCompare().equals("leq")) {
					    	if(daysBetween1 <= daysBetween2) message=message2;
					    }else if(rule.getCompare().equals("gt")) {
					    	if(daysBetween1 > daysBetween2) message=message2;
					    }else if(rule.getCompare().equals("gte")) {
					    	if(daysBetween1 >= daysBetween2) message=message2;
					    }
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
				
				if(!message.equals("")) {
					CrfDescrpency scd = null;
//					if(dataFlag)
//						crfDAO.studyCrfDescrpencySec(crf, vpc, data);
					if(scd == null) {
						scd =  new CrfDescrpency();
						scd.setCrf(crf);
						scd.setVolPeriodCrf(vpc);
						scd.setUpdateReason(message);
//						scd.setKayName(data.getKayName());
//						scd.setSecElement(data.getElement());
//						scd.setSecEleData(data);
//						scd.setCrfRule(rule);
						scd.setCreatedBy(username);
						scd.setCreatedOn(new Date());
//						if(map.get(data.getKayName()) == null) {
//							descList.add(scd);
//							
//						}
					}
//					map.put(data.getKayName(), scd);
				}
			}
			
		}
		
		return descList;
	}
	
	private Map<String, CrfSection> elementStudySections = null; // key-sectionName value-Section
	private Map<String, CrfSection> groupStudySections = null; // key-sectionName value-Section
	private Map<String, CrfGroup> groupsStudyMap = null; // key-groupName value-CrfGroup
	@Override
	public Crf readStudyCrfExcelFile(FileInputStream inputStream, String fileName, Long stdId) throws IOException {
		elementStudySections = new HashMap<>();
		groupStudySections = new HashMap<>();
		groupsStudyMap = new HashMap<>();
		secNames = new HashSet<>();
		// TODO Auto-generated method stub
		Crf crf = new Crf();
		Workbook guru99Workbook = null;
		// Find the file extension by splitting file name in substring and getting only
		// extension name
		String fileExtensionName = fileName.substring(fileName.indexOf("."));
		// Check condition if the file is xlsx file
		if (fileExtensionName.equals(".xlsx")) {
			// If it is xlsx file then create object of XSSFWorkbook class
			guru99Workbook = new XSSFWorkbook(inputStream);
		}
		// Check condition if the file is xls file
		else if (fileExtensionName.equals(".xls")) {
			// If it is xls file then create object of HSSFWorkbook class
			guru99Workbook = new HSSFWorkbook(inputStream);
		}

		// Read sheet inside the workbook by its name
		crf = readStudyCrfSheet(crf, guru99Workbook.getSheet("CRF"));
		Crf oldcrf = crfDAO.checkStudyCrf(crf.getName(), stdId);
		if(oldcrf != null) {
			crf.setMessage("duplicate");
			return crf;
		}
		crf = readStudyCrfSectionSheet(crf, guru99Workbook.getSheet("SECTION"));
		crf = readStudyCrfGroupSheet(crf, guru99Workbook.getSheet("GROUP"));
		crf = readStudyCrfSectionElements(crf, guru99Workbook.getSheet("SECTION ELEMENTS"));
		crf = readStudyCrfGroupElements(crf, guru99Workbook.getSheet("GROUP ELEMENT"));

		List<CrfSection> secl = new ArrayList<>();
		for (String sc : secNames) {
			if (elementStudySections.get(sc) != null)
				secl.add(elementStudySections.get(sc));
			else {
//				CrfSection sec  = groupSections.get(sc);
//				CrfGroup g = sec.getGroup();
//				for(CrfGroupElement e : g.getElement()) {
//					System.out.println(e);
//				}
				secl.add(groupStudySections.get(sc));
			}
		}
		Collections.sort(secl);
		crf.setSections(secl);
		return crf;
	}
	
	private Crf readStudyCrfSheet(Crf crf, Sheet sheet) {
		// Find number of rows in excel file
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		// Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount + 1; i++) {
			Row row = sheet.getRow(i);
			// Create a loop to print cell values in a row
			for (int j = 0; j < row.getLastCellNum(); j++) {
				// Print Excel data in console
				Cell cell = row.getCell(j);
				if (cell != null) {
					switch (j + 1) {
					case 1:
						crf.setName(getCellValue(cell, false));
						System.out.print(getCellValue(cell, false) + "\t");
						break;
					case 2:
						crf.setTitle(getCellValue(cell, false));
						System.out.print(getCellValue(cell, false) + "\t");
						break;
					case 3:
						crf.setType(getCellValue(cell, false));
						System.out.print(getCellValue(cell, false) + "\t");
						break;
					case 4:
						crf.setGender(getCellValue(cell, false));
						System.out.print(getCellValue(cell, false) + "\t");
						break;
					case 5:
						crf.setVersion(getCellValue(cell, false));
						System.out.print(getCellValue(cell, false) + "\t");
						break;
					default:
						break;
					}
				}
			}
			System.out.println();
		}
		return crf;
	}

	private Crf readStudyCrfSectionSheet(Crf crf, Sheet sheet) {
		List<CrfSection> sections = new ArrayList<CrfSection>();
//		if (crf.getSections() == null)
//			sections = new ArrayList<StudyCrfSection>();
//		else
//			sections = crf.getSections();
		// Find number of rows in excel file
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		// Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount + 1; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				CrfSection section = new CrfSection();
				section.setCrf(crf);
				// Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum(); j++) {
					// Print Excel data in console
					Cell cell = row.getCell(j);
					if (cell != null) {
						switch (j + 1) {
						case 1:
							section.setName(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 2:
							section.setTitle(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 3:
							section.setHedding(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 4:
							section.setSubHedding(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 5:
							d = Double.parseDouble(getCellValue(cell, false));
							section.setMaxRows(d.intValue());
							System.out.print(d + "\t");
							break;
						case 6:
							d = Double.parseDouble(getCellValue(cell, false));
							section.setMaxColumns(d.intValue());
							System.out.print(d + "\t");
							break;
						case 7:
							d = Double.parseDouble(getCellValue(cell, false));
							section.setOrder(d.intValue());
							System.out.print(d + "\t");
							break;
						case 8:
							section.setGender(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 9:
							section.setRoles(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 10:
							if (getCellValue(cell, false).trim().equals("YES")) {
								section.setContainsGroup(true);
								groupStudySections.put(section.getName(), section);
							} else {
								elementStudySections.put(section.getName(), section);
							}
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						default:
							break;
						}
					}
				}
				section.setElement(new ArrayList<>());
				System.out.println();
				sections.add(section);
				secNames.add(section.getName());
			}
		}
		crf.setSections(sections);
		return crf;
	}

	private Crf readStudyCrfGroupSheet(Crf crf, Sheet sheet) {
		List<CrfGroup> groups = new ArrayList<CrfGroup>();
		// Find number of rows in excel file
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		// Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount + 1; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				CrfGroup group = new CrfGroup();
				group.setCrf(crf);
				// Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum(); j++) {
					// Print Excel data in console
					Cell cell = row.getCell(j);
					if (cell != null) {
						switch (j + 1) {
						case 1:
							group.setName(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 2:
							group.setTitle(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 3:
							group.setHedding(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 4:
							group.setSubHedding(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 5:
							d = Double.parseDouble(getCellValue(cell, false));
							group.setMinRows(d.intValue());
							System.out.print(d + "\t");
							break;
						case 6:
							d = Double.parseDouble(getCellValue(cell, false));
							group.setMaxRows(d.intValue());
							System.out.print(d + "\t");
							break;
						case 7:
							d = Double.parseDouble(getCellValue(cell, false));
							group.setMaxColumns(d.intValue());
							System.out.print(d + "\t");
							break;
						case 8:
							String secname = cell.getStringCellValue().trim();
							CrfSection sec = groupStudySections.get(secname);
							sec.setGroup(group);
							groupStudySections.put(sec.getName(), sec);
							break;
						default:
							break;
						}
					}
				}
				System.out.println();
				groups.add(group);
				groupsStudyMap.put(group.getName(), group);
			}
		}
		return crf;
	}

	private Crf readStudyCrfSectionElements(Crf crf, Sheet sheet) {
		List<CrfSectionElement> eles = new ArrayList<CrfSectionElement>();
		// Find number of rows in excel file
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		// Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount + 1; i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				CrfSectionElement ele = new CrfSectionElement();
				ele.setCrf(crf);
				eles.add(ele);
				// Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum(); j++) {
					// Print Excel data in console
					Cell cell = row.getCell(j);
					if (cell != null) {
						switch (j + 1) {
						case 1:
							ele.setName(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 2:
							ele.setLeftDesc(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 3:
							ele.setRigtDesc(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 4:
							ele.setMiddeDesc(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 5:
							ele.setTotalDesc(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 6:
							d = Double.parseDouble(getCellValue(cell, false));
							ele.setColumnNo(d.intValue());
							System.out.print(d + "\t");
							break;
						case 7:
							d = Double.parseDouble(getCellValue(cell, false));
							ele.setRowNo(d.intValue());
							System.out.print(d + "\t");
							break;
						case 8:
							if (getCellValue(cell, false) != null) {
								ele.setTopDesc(getCellValue(cell, false));
								System.out.print(getCellValue(cell, false) + "\t");
							}
							break;
						case 9:
							if (getCellValue(cell, false) != null) {
								ele.setBottemDesc(getCellValue(cell, false));
								System.out.print(getCellValue(cell, false) + "\t");
							}
							break;
						case 10:
							ele.setType(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 11:
							if (getCellValue(cell, false) != null) {
								ele.setResponceType(getCellValue(cell, false));
								String[] sr = ele.getResponceType().split("##");
								List<CrfSectionElementValue> list = new ArrayList<>();
								for (int k = 0; k < sr.length; k++) {
									CrfSectionElementValue v = new CrfSectionElementValue();
									v.setSectionElement(ele);
									v.setValue(sr[k]);
									list.add(v);
								}
								ele.setElementValues(list);
								System.out.print(getCellValue(cell, false) + "\t");
							}
							break;
						case 12:
							if (getCellValue(cell, false) != null && cell.getStringCellValue().equals("vertical")) {
								ele.setDisplay("vertical");
								System.out.print(getCellValue(cell, false) + "\t");
							}
							break;
						case 13:
							if (getCellValue(cell, false) != null) {
								ele.setValues(getCellValue(cell, false));
								System.out.print(getCellValue(cell, false) + "\t");
							}
							break;
						case 14:
							if (getCellValue(cell, false) != null) {
								ele.setPattren(getCellValue(cell, false));
							}
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 15:
							d = Double.parseDouble(getCellValue(cell, false));
							if (d.intValue() == 1)
								ele.setRequired(true);
							System.out.print(d + "\t");
							break;
						case 16:
							String secname = cell.getStringCellValue().trim();
							CrfSection sec = elementStudySections.get(secname);
							List<CrfSectionElement> element = sec.getElement();
							if (element == null)
								element = new ArrayList<>();
							element.add(ele);
							sec.setElement(element);
							elementStudySections.put(sec.getName(), sec);
							break;
						case 17:
							String dataType = cell.getStringCellValue().trim();
							ele.setDataType(dataType);
							break;
						default:
							break;
						}
					}
				}
				System.out.println();
			}
		}
		return crf;
	}

	private Crf readStudyCrfGroupElements(Crf crf, Sheet sheet) {
		List<CrfGroupElement> eles = new ArrayList<CrfGroupElement>();
		// Find number of rows in excel file
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		// Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount + 1; i++) {
			Row row = sheet.getRow(i);
			String secName = "";
			if (row != null) {
				CrfGroupElement ele = new CrfGroupElement();
				ele.setCrf(crf);
				eles.add(ele);
				// Create a loop to print cell values in a row
				for (int j = 0; j < row.getLastCellNum(); j++) {
					// Print Excel data in console
					Cell cell = row.getCell(j);
					if (cell != null) {
						switch (j + 1) {
						case 1:
							ele.setName(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 2:
							ele.setTitle(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 3:
							d = Double.parseDouble(getCellValue(cell, false));
							ele.setColumnNo(d.intValue());
							System.out.print(d + "\t");
							break;
						case 4:
							d = Double.parseDouble(getCellValue(cell, false));
							ele.setRowNo(d.intValue());
							break;
						case 5:
							ele.setType(getCellValue(cell, false));
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 6:
							if (getCellValue(cell, false) != null) {
								ele.setResponceType(getCellValue(cell, false));
								String[] sr = ele.getResponceType().split("##");
								List<CrfGroupElementValue> list = new ArrayList<>();
								for (int k = 0; k < sr.length; k++) {
									CrfGroupElementValue v = new CrfGroupElementValue();
									v.setGroupElement(ele);
									v.setValue(sr[k].trim());
									list.add(v);
								}
								ele.setElementValues(list);
								System.out.print(getCellValue(cell, false) + "\t");
							}
							break;
						case 7:
							if (getCellValue(cell, false) != null && cell.getStringCellValue().equals("vertical")) {
								ele.setDisplay("vertical");
								System.out.print(getCellValue(cell, false) + "\t");
							}
						case 8:
							if (getCellValue(cell, false) != null) {
								ele.setValues(getCellValue(cell, false));
								System.out.print(getCellValue(cell, false) + "\t");
							}
							break;
						case 9:
							// DISPLAY VALUES PATTREN REQUIRED SECTION NAME GROUP NAME
							if (getCellValue(cell, false) != null) {
								ele.setPattren(getCellValue(cell, false));
							}
							System.out.print(getCellValue(cell, false) + "\t");
							break;
						case 10:
							d = Double.parseDouble(getCellValue(cell, false));
							if (d.intValue() == 1)
								ele.setRequired(true);
							System.out.print(d + "\t");
							break;
						case 11:
							secName = cell.getStringCellValue().trim();
							System.out.print(secName + "\t");
							break;
						case 12:
							String gname = cell.getStringCellValue().trim();
							System.out.print(gname + "\t");
							CrfSection sec = groupStudySections.get(secName);
							CrfGroup g = sec.getGroup();
							CrfGroup g1 = groupsStudyMap.get(gname);
							if (g.getName().equals(g1.getName())) {
								List<CrfGroupElement> element = g.getElement();
								if (element == null)
									element = new ArrayList<>();
								element.add(ele);
								g1.setElement(element);
								groupsStudyMap.put(g.getName(), g1);
							}
							break;
						case 13:
							String dataType = cell.getStringCellValue().trim();
							ele.setDataType(dataType);
							break;
						default:
							break;
						}
					}
				}
				System.out.println();
			}
		}
		return crf;
	}
	
	
	
	@Override
	public Crf changeStudyCrfStatus(Long crfId , String username) {
		// TODO Auto-generated method stub
		try {
			Crf crf = crfDAO.getStudyCrf(crfId);
			if(crf.isActive())  crf.setActive(false);
			else crf.setActive(true);
			crf.setUpdatedBy(username);
			crf.setUpdatedOn(new Date());
			crfDAO.updateStudyCrf(crf);
			return crf;
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}

	@Override
	public List<CrfEleCaliculation> studyCrfEleCaliculation(Long studyId) {
		return crfDAO.studyCrfEleCaliculation(studyId);
	}
	
	
	@Override
	public void uploadStudyCrfCaliculationFile(String path, String username, StudyMaster sm) throws ParserConfigurationException, SAXException, IOException {
		//create document builder factory
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						
				//create Document Builder
				DocumentBuilder builder = factory.newDocumentBuilder();
				
				//get inputstrem of xml file
				//file from folder location
				String filePath = path;
				File xmlFile = new File(filePath);
		        //file from class path location
		        ClassLoader cl = DOMReader.class.getClassLoader();
				InputStream is = cl.getResourceAsStream("com/covide/crf/xmlfiles/caliculation2.xml");
				
				//parse xml file and  get Document object
				//Document document = builder.parse(is);
				
				
				Document document = builder.parse(xmlFile);
				
				
				//get Root element of xml doc
				Element rootElement = document.getDocumentElement();
				
				//get <Eform> tag value
				Node first = rootElement.getFirstChild();
				Node sibling = first.getNextSibling();
				Node finalNode = sibling.getFirstChild();
				String value = finalNode.getNodeValue();
				
				System.out.println(value);
				 NodeList nodeList = document.getElementsByTagName("EleCaliculation");
		         List<EleCaliculation> empList = new ArrayList<EleCaliculation>();
		         for (int i = 0; i < nodeList.getLength(); i++) {
		             empList.add(getEleCaliculation(nodeList.item(i)));
		         }
		         
		         
		         Crf crf = crfDAO.studycrfByName(sm, value);
		         List<CrfEleCaliculation> list = new ArrayList<>();
		         //lets print Employee list information
		         for (EleCaliculation emp : empList) {
		             System.out.println(emp.toString());
		             CrfEleCaliculation cec = new CrfEleCaliculation();
		             cec.setResultField(emp.getResultField());
		             cec.setCaliculation(emp.getRule());
		             cec.setCrf(crf);
		             cec.setCreatedOn(new Date());
		             cec.setCreatedBy(username);
//		             cec.setCrfFrom("Study");
//		             cec.setStudyId(sm.getId());
		             list.add(cec);
		         }
		         
		         crfDAO.saveCrfEleCaliculationList(list);
	}
	
	@Override
	public CrfEleCaliculation changeStudyCrfEleCaliculationStatus(Long crfId , String username) {
		// TODO Auto-generated method stub
		try {
			CrfEleCaliculation crf = crfDAO.getCrfEleCaliculation(crfId);
			if(crf.getStatus().equals("active"))  crf.setStatus("In-Active");
			else crf.setStatus("active");
			crf.setUpdatedBy(username);
			crf.setUpdatedOn(new Date());
			crfDAO.updateCrfEleCaliculationStatus(crf);
			return crf;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<CrfRule> studyCrfRuleWithCrfAndSubElements(StudyMaster sm) {
		return crfDAO.studyCrfRuleWithCrfAndSubElements(sm);
	}

	@Override
	public List<Crf> findAllStudyCrfsForRules(StudyMaster sm) {
		return crfDAO.findAllStudyCrfsForRules(sm);
	}
	
	@Override
	public String studysectionEleSelect(Long id) {
		List<CrfSectionElement> list = crfDAO.studysectionElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='secEleId' id='secEleId' class='form-control' onchange='secEleSelection(this.id, this.value)'>")
		.append("<option value='-1' selected='selected' >--Select--</option>");
		for(CrfSectionElement ele : list) {
			sb.append("<option value='"+ele.getId()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	@Override
	public String studygroupEleSelect(Long id) {
		List<CrfGroupElement> list = crfDAO.studygroupElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='groupEleId' id='groupEleId' class='form-control' onchange='groupEleSelection(this.id, this.value)'>")
		.append("<option value='-1' selected='selected'>--Select--</option>");
		for(CrfGroupElement ele : list) {
			sb.append("<option value='"+ele.getId()+","+ele.getGroup().getMaxRows()+","+ele.getGroup().getMaxColumns()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	@Override
	public String studycrfRuleElements(StudyMaster sm, int rowId) {
		rowId++;
		List<Crf> list  = crfDAO.findAllStudyCrfsForRules(sm);
		StringBuilder sb = new StringBuilder();
		sb.append("<tr id='AddRow"+rowId+"'>");
		sb.append("<td><select class='form-control' name='otherCrf"+rowId+"' id='otherCrf"+rowId+"' class='form-control' onchange='otherCrf(this.id, this.value, "+rowId+")'>");
		sb.append("<option value='-1' selected='selected'>--Select--</option>");
		for(Crf crf : list) {
			sb.append("<option value='"+crf.getId()+"'>"+crf.getName()+"</option>");
		}
		sb.append("</select><font color='red' id='otherCrf"+rowId+"msg'></font></td>");
		
		sb.append("<td id='secEleIdTd"+rowId+"'><select class='form-control' name='otherCrfSecEle"+rowId+"' class='form-control' id='otherCrfSecEle"+rowId+"' onchange='secEleSelection(this.id, this.value)'> ");
		sb.append("<option value='-1' selected='selected'>--Select--</option>");
		sb.append("</select><font color='red' id='otherCrfSecEle"+rowId+"msg'></font></td>");
		
		sb.append("<td id='groupEleIdTd"+rowId+"'><select class='form-control' name='otherCrfGroupEle"+rowId+"' class='form-control' id='otherCrfGroupEle"+rowId+"'> ");
		sb.append("<option value='-1' selected='selected'>--Select--</option>");
		sb.append("</select><font color='red' id='otherCrfGroupEle"+rowId+"msg'></font></td>");
		
		sb.append("<td id='groupEleIdTdRow"+rowId+"'><select class='form-control' name='otherCrfGroupEleRowNo"+rowId+"' class='form-control' id='otherCrfGroupEleRowNo"+rowId+"'> ");
		sb.append("<option value='-1' selected='selected'>--Select--</option>");
		sb.append("</select></td>");
		
		sb.append("<td><select class='form-control' name='otherCrfContion"+rowId+"' class='form-control' id='otherCrfContion"+rowId+"'> ");
		sb.append("<option value='eq' selected='selected'>=</option>");
		sb.append("<option value='ne'>!=</option>");
		sb.append("<option value='le'>Less</option>");
		sb.append("<option value='leq'>Less and eq</option>");
		sb.append("<option value='gt'>Grater</option>");
		sb.append("<option value='gte'>grater and eq</option>");
		sb.append("</select></td>");
		
		sb.append("<td><select class='form-control' name='otherCrfNContion"+rowId+"' class='form-control' id='otherCrfNContion"+rowId+"'> ");
		sb.append("<option value='and' selected='selected'>And</option>");
		sb.append("<option value='or'>or</option>");
		sb.append("</select></td>");
		
		sb.append("</tr>");
		return sb.toString();
	}
	
	@Override
	public String otherStudyCrfSectionElements(Long id, int count) {
		List<CrfSectionElement> list = crfDAO.studysectionElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='otherCrfSecEle"+count+"' id='otherCrfSecEle"+count+"' class='form-control' onchange='othersecEleSelection(this.id, this.value, "+count+")'>")
		.append("<option value='-1' selected='selected' >--Select--</option>");
		for(CrfSectionElement ele : list) {
			sb.append("<option value='"+ele.getId()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select> <font color='red' id='otherCrfSecEle"+count+"msg'></font>");
		return sb.toString();
	}
	
	@Override
	public String otherStudyCrfGroupElements(Long id, int count) {
		List<CrfGroupElement> list = crfDAO.studygroupElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='otherCrfGroupEle"+count+"' id='otherCrfGroupEle"+count+"' class='form-control' onchange='othergroupEleSelection(this.id, this.value, "+count+")'>")
		.append("<option value='-1' selected='selected'>--Select--</option>");
		for(CrfGroupElement ele : list) {
			sb.append("<option value='"+ele.getId()+","+ele.getGroup().getMaxRows()+","+ele.getGroup().getMaxColumns()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select> <font color='red' id='otherCrfGroupEle"+count+"msg'></font>");
		return sb.toString();
	}
	
	
	
	@Override
	public String studycrfRuleChangeStatus(Long id) {
		CrfRule rule = crfDAO.studycrfRule(id);
		if(rule.isActive()) rule.setActive(false);
		else rule.setActive(true);
		try {
			crfDAO.updateCrfRule(rule);
			return rule.getName();
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	public String studysectionEleSelect1(Long id) {
		List<CrfSectionElement> list = crfDAO.studysectionElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='secEleId1' id='secEleId1' class='form-control' onchange='secEleSelection1(this.id, this.value)'>")
		.append("<option value='-1' selected='selected' >--Select--</option>");
		for(CrfSectionElement ele : list) {
			sb.append("<option value='"+ele.getId()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	@Override
	public String studygroupEleSelect1(Long id) {
		List<CrfGroupElement> list = crfDAO.studygroupElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='groupEleId1' id='groupEleId1' class='form-control' onchange='groupEleSelection1(this.id, this.value)'>")
		.append("<option value='-1' selected='selected'>--Select--</option>");
		for(CrfGroupElement ele : list) {
			sb.append("<option value='"+ele.getId()+","+ele.getGroup().getMaxRows()+","+ele.getGroup().getMaxColumns()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	@Override
	public String studysectionEleSelect2(Long id) {
		List<CrfSectionElement> list = crfDAO.studysectionElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='secEleId2' id='secEleId2' class='form-control' onchange='secEleSelection2(this.id, this.value)'>")
		.append("<option value='-1' selected='selected' >--Select--</option>");
		for(CrfSectionElement ele : list) {
			sb.append("<option value='"+ele.getId()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	@Override
	public String studygroupEleSelect2(Long id) {
		List<CrfGroupElement> list = crfDAO.studygroupElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='groupEleId2' id='groupEleId2' class='form-control' onchange='groupEleSelection2(this.id, this.value)'>")
		.append("<option value='-1' selected='selected'>--Select--</option>");
		for(CrfGroupElement ele : list) {
			sb.append("<option value='"+ele.getId()+","+ele.getGroup().getMaxRows()+","+ele.getGroup().getMaxColumns()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	@Override
	public String studysectionEleSelect3(Long id) {
		List<CrfSectionElement> list = crfDAO.studysectionElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='secEleId3' id='secEleId3' class='form-control' onchange='secEleSelection3(this.id, this.value)'>")
		.append("<option value='-1' selected='selected' >--Select--</option>");
		for(CrfSectionElement ele : list) {
			sb.append("<option value='"+ele.getId()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	@Override
	public String studygroupEleSelect3(Long id) {
		List<CrfGroupElement> list = crfDAO.studygroupElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='groupEleId3' id='groupEleId3' class='form-control' onchange='groupEleSelection3(this.id, this.value)'>")
		.append("<option value='-1' selected='selected'>--Select--</option>");
		for(CrfGroupElement ele : list) {
			sb.append("<option value='"+ele.getId()+","+ele.getGroup().getMaxRows()+","+ele.getGroup().getMaxColumns()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	@Override
	public String studysectionEleSelect4(Long id) {
		List<CrfSectionElement> list = crfDAO.studysectionElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='secEleId4' id='secEleId4' class='form-control' onchange='secEleSelection4(this.id, this.value)'>")
		.append("<option value='-1' selected='selected' >--Select--</option>");
		for(CrfSectionElement ele : list) {
			sb.append("<option value='"+ele.getId()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	@Override
	public String studygroupEleSelect4(Long id) {
		List<CrfGroupElement> list = crfDAO.studygroupElemets(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='groupEleId4' id='groupEleId4' class='form-control' onchange='groupEleSelection4(this.id, this.value)'>")
		.append("<option value='-1' selected='selected'>--Select--</option>");
		for(CrfGroupElement ele : list) {
			sb.append("<option value='"+ele.getId()+","+ele.getGroup().getMaxRows()+","+ele.getGroup().getMaxColumns()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	@Override
	public String saveStudyCrfDateComparison(StudyMaster sm, String username, HttpServletRequest request) {
		try {
			CrfDateComparison data = new CrfDateComparison();
//			data.setCrfFrom("Study");
//			data.setSm(sm);
			data.setCreatedBy(username);
			data.setCreatedOn(new Date());
			data.setName(request.getParameter("name"));
			data.setDescreption(request.getParameter("descreption"));
			data.setSourcrf(crfDAO.studyCrf(Long.parseLong(request.getParameter("crf"))));
			if(!request.getParameter("secEleId").equals("-1"))
				data.setSecEle(crfDAO.studyCrfSectionElement(Long.parseLong(request.getParameter("secEleId").toString())));
			if(!request.getParameter("groupEleId").equals("-1")) {
				data.setGroupEle(crfDAO.studyCrfGroupElement(Long.parseLong(request.getParameter("groupEleId").toString())));
				data.setRowNo(Integer.parseInt(request.getParameter("rowgroupEleId")));
			}
			data.setMessage(request.getParameter("message"));
			data.setCompare(request.getParameter("condition"));
			
			data.setCrf1(crfDAO.studyCrf(Long.parseLong(request.getParameter("crf1"))));
			if(!request.getParameter("secEleId1").equals("-1"))
				data.setSecEle1(crfDAO.studyCrfSectionElement(Long.parseLong(request.getParameter("secEleId1").toString())));
			if(!request.getParameter("groupEleId1").equals("-1")) {
				data.setGroupEle1(crfDAO.studyCrfGroupElement(Long.parseLong(request.getParameter("groupEleId1").toString())));
				data.setRowNo1(Integer.parseInt(request.getParameter("rowgroupEleId1")));
			}
			
			data.setCrf2(crfDAO.studyCrf(Long.parseLong(request.getParameter("crf2"))));
			if(!request.getParameter("secEleId2").equals("-1"))
				data.setSecEle2(crfDAO.studyCrfSectionElement(Long.parseLong(request.getParameter("secEleId2").toString())));
			if(!request.getParameter("groupEleId2").equals("-1")) {
				data.setGroupEle2(crfDAO.studyCrfGroupElement(Long.parseLong(request.getParameter("groupEleId2").toString())));
				data.setRowNo2(Integer.parseInt(request.getParameter("rowgroupEleId2")));
			}
			
			data.setCrf3(crfDAO.studyCrf(Long.parseLong(request.getParameter("crf3"))));
			if(!request.getParameter("secEleId3").equals("-1"))
				data.setSecEle3(crfDAO.studyCrfSectionElement(Long.parseLong(request.getParameter("secEleId3").toString())));
			if(!request.getParameter("groupEleId3").equals("-1")) {
				data.setGroupEle3(crfDAO.studyCrfGroupElement(Long.parseLong(request.getParameter("groupEleId3").toString())));
				data.setRowNo3(Integer.parseInt(request.getParameter("rowgroupEleId3")));
			}
			
			data.setCrf4(crfDAO.studyCrf(Long.parseLong(request.getParameter("crf4"))));
			if(!request.getParameter("secEleId4").equals("-1"))
				data.setSecEle4(crfDAO.studyCrfSectionElement(Long.parseLong(request.getParameter("secEleId4").toString())));
			if(!request.getParameter("groupEleId4").equals("-1")) {
				data.setGroupEle4(crfDAO.studyCrfGroupElement(Long.parseLong(request.getParameter("groupEleId4").toString())));
				data.setRowNo4(Integer.parseInt(request.getParameter("rowgroupEleId4")));
			}
			crfDAO.saveCrfDateComparison(data);
			return data.getName();
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<CrfDateComparison> crfDateComparisonlist() {
		return crfDAO.crfDateComparisonlist();
	}

	@Override
	public String crfDateComparisonChangeStatus(Long id) {
		CrfDateComparison rule = crfDAO.crfDateComparison(id);
		if(rule.getStatus().equals("active")) rule.setStatus("In-active");
		else rule.setStatus("active");
		try {
			crfDAO.updateCrfDateComparison(rule);
			return rule.getName();
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<CrfDateComparison> studycrfDateComparisonlistall(StudyMaster sm) {
		return crfDAO.studycrfDateComparisonlistall(sm);
	}
	
	@Override
	public List<CrfDateComparison> studycrfDateComparisonlist() {
		return crfDAO.studycrfDateComparisonlist();
	}

	@Override
	public String studycrfDateComparisonChangeStatus(Long id) {
		CrfDateComparison rule = crfDAO.studycrfDateComparison(id);
		if(rule.getStatus().equals("active")) rule.setStatus("In-active");
		else rule.setStatus("active");
		try {
			crfDAO.updateCrfDateComparison(rule);
			return rule.getName();
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	@Override
	public Crf crfForView(Long crfId) {
		return crfDAO.crfForView(crfId);
	}


	@Override
	public void saveStudyCrfSectionElementDataAudit(CrfSectionElementDataAudit saudit) {
		// TODO Auto-generated method stub
		crfDAO.saveCrfSectionElementDataAudit(saudit);
	}

	@Override
	public void saveStudyCrfDescrpencyAudit(CrfDescrpencyAudit descAudit) {
		crfDAO.saveCrfDescrpencyAudit(descAudit);
	}



	@Override
	public List<Crf> crfsByName(String name) {
		return crfDAO.crfsByName(name);
	}

	@Override
	public StdSubGroupObservationCrfs stdSubGroupObservationCrfsWithCrf(Long crfId) {
		// TODO Auto-generated method stub
		return crfDAO.stdSubGroupObservationCrfsWithCrf(crfId);
	}

	@Override
	public String saveCrfRecord(Crf crf, List<Long> ids) {
		return crfDAO.saveCrfRecord(crf,ids);
	}

	@Override
	public List<CrfDescrpency> alldiscrepencyOfElement(CrfSectionElementData data) {
		// TODO Auto-generated method stub
		return crfDAO.alldiscrepencyOfElement(data);
	}

	@Override
	public boolean saveAccessionDescrpency(Long dataId, Long accessionuserId, String comment, Long userId) {
		StudyAccessionCrfSectionElementData data = crfDAO.studyAccessionCrfSectionElementDataWithId(dataId);
		LoginUsers user = userdao.findById(userId);
		LoginUsers accessionuser = userdao.findById(accessionuserId);
		StudyAccessionCrfDescrpency scd = new StudyAccessionCrfDescrpency();
		scd.setStydyAccCrfSecEleData(data);
		scd.setStudy(data.getStudyAccAnimal().getAnimal().getStudy());
		scd.setAccAnimalDataEntryDetails(data.getStudyAccAnimal());
		scd.setAssingnedTo(accessionuser.getUsername());
		scd.setStatus("open");
		scd.setValue(data.getValue());
		scd.setCrf(data.getCrf());
		scd.setSecElement(data.getElement());
		scd.setRisedBy("reviewer");
		scd.setCreatedBy(user.getUsername());
		scd.setCreatedOn(new Date());
		scd.setComment(comment);
		scd.setKayName(data.getKayName());
		return crfDAO.saveStudyAccessionCrfDescrpency(scd);
	}
	
	@Override
	public CrfDescrpency saveObservationDescrpency(Long dataId, Long accessionuserId, String comment, Long userId) {
		CrfSectionElementData data = crfDAO.crfSectionElementDataWithId(dataId);
		LoginUsers user = userdao.findById(userId);
		LoginUsers accessionuser = userdao.findById(accessionuserId);
		CrfDescrpency scd = new CrfDescrpency();
		scd.setStudy(data.getSubjectDataEntryDetails().getStudy());
		scd.setValue(data.getValue());
		scd.setCrf(data.getCrf());
		scd.setStdSubGroupObservationCrfs(data.getSubjectDataEntryDetails().getObservationCrf());
		scd.setKayName(data.getKayName());
		scd.setSecElement(data.getElement());
		scd.setAssingnedTo(accessionuser.getUsername());
		scd.setSecEleData(data);
		scd.setRisedBy("reviewer");
		scd.setCreatedBy(user.getUsername());
		scd.setCreatedOn(new Date());
		scd.setComment(comment);
		scd.setStatus("open");
		scd.setSecEleData(data);
		return crfDAO.saveCrfDescrpency(scd);
	}
	
	@Override
	public boolean saveCrfDescrpency(Long dataId, String username, Long userId, String comment) {
		CrfSectionElementData data = crfDAO.studyCrfSectionElementData(dataId);
		LoginUsers user = userdao.findById(userId);
		CrfDescrpency scd = new CrfDescrpency();
		scd.setOldValue(data.getValue());
		scd.setCrf(data.getElement().getSection().getCrf());
		scd.setSecEleData(data);
		scd.setAssingnedTo(user.getUsername());
		scd.setRisedBy("reviewer");
		scd.setCreatedBy(username);
		scd.setCreatedOn(new Date());
		scd.setComment(comment);
		
		scd.setKayName(data.getKayName());
		scd.setSecElement(data.getElement());
		return crfDAO.saveStudyCrfDescrpency(scd);
	}

	@Override
	public StdSubGroupObservationCrfs saveRevewObsevation(LoginUsers user, Long subGroupInfoId, Long stdSubGroupObservationCrfsId, Integer descElementAll, List<Long> dataIds) {
		return crfDAO.saveRevewObsevation(user, subGroupInfoId, stdSubGroupObservationCrfsId, descElementAll, dataIds);
		
	}

	@Override
	public ReviewLevel reviewLevel() {
		// TODO Auto-generated method stub
		return crfDAO.reviewLevel();
	}

	@Override
	public List<CrfMapplingTable> findAllMappingTables() {
		// TODO Auto-generated method stub
		return crfDAO.findAllMappingTables();
	}

	@Override
	public String crfMappingTableColumns(Long id) {
		List<CrfMapplingTableColumn> list = crfDAO.crfMappingTableColumns(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='columnId' id='columnId' class='form-control'>")
		.append("<option value='-1' selected='selected' >--Select--</option>");
		for(CrfMapplingTableColumn ele : list) {
			sb.append("<option value='"+ele.getId()+"'>"+ele.getCloumnName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}

	@Override
	public CrfMapplingTableColumnMap mapTableToCrfSave(Long crfid, Long secEleId, Long tableId, Long columnId, String userName) {
		Crf crf = new Crf();
		crf.setId(crfid);
		CrfSectionElement element = new CrfSectionElement();
		element.setId(secEleId);
		CrfMapplingTable table = new CrfMapplingTable();
		table.setId(tableId);
		CrfMapplingTableColumn column = new CrfMapplingTableColumn();
		column.setId(columnId);
		CrfMapplingTableColumnMap map = new CrfMapplingTableColumnMap();
		map.setCrf(crf);
		map.setElement(element);
		map.setMappingTable(table);
		map.setMappingColumn(column);
		map.setCreatedBy(userName);
		return crfDAO.saveCrfMapplingTableColumnMap(map);
	}

	@Override
	public String crfSectionElementsSelectTable(Long id) {
		List<CrfSectionElement> list = crfDAO.sectionElemetsSelectTale(id);
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='secEleId' id='secEleId' class='form-control' onchange='secEleSelection(this.id, this.value)'>")
		.append("<option value='-1' selected='selected' >--Select--</option>");
		for(CrfSectionElement ele : list) {
			sb.append("<option value='"+ele.getId()+"'>"+ele.getName()+"</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}

	@Override
	public List<CrfMapplingTableColumnMap> allCrfMapplingTableColumnMap() {
		// TODO Auto-generated method stub
		return crfDAO.allCrfMapplingTableColumnMap();
	}

	@Override
	public String[] crfSectionElementValuesFromCrfMapplingTableColumnMap(CrfSectionElement secElement) {
		// TODO Auto-generated method stub
		return crfDAO.crfSectionElementValuesFromCrfMappingTableColumnMap(secElement);
	}

	@Override
	public Crf getCrfForDataEntryView(Long id) {
		// TODO Auto-generated method stub
		return crfDAO.getCrfForDataEntryView(id);
	}	
	
	
	@Override
	public Crf getCrfForDataEntryView(Long id, StudyMaster sm, Long dataDatesId, StudyAnimal animal,
			String activityType, String scheduleType) {
		return crfDAO.getCrfForDataEntryView(id, sm, dataDatesId, animal, activityType, scheduleType);
	}

	@Override
	public void copyLibCrfToStudy(StudyMaster sm, List<Integer> cids) {
		List<CrfMetaData> crfs = crfDAO.findAll();
		
		List<CrfMetaDataStd> stdcrfs = crfDAO.findAllStdCrfs(sm);
		List<CrfMetaDataStd> stdcrfsUpdate = new ArrayList<>();

		System.out.println(cids);

		List<Long> cidsrec = new ArrayList<>();
		for (CrfMetaDataStd stdc : stdcrfs) {
			System.out.println(stdc.getCrfmetaId());
			System.out.println(stdc.isActive());
			if (cids.contains(stdc.getCrfmetaId()) && stdc.isActive()) {
				// no need to update
//				cidsrec.add(stdc.getCrfmetaId());
				if (!stdc.isActive()) {
					// update required
					stdc.setActive(true);
					stdcrfsUpdate.add(stdc);
				}
			} else {
				// update required
				stdc.setActive(false);
				stdcrfsUpdate.add(stdc);
			}

			cidsrec.add(stdc.getCrfmetaId());
		}
		
		List<CrfMetaDataStd> stdcrfssave = new ArrayList<>();
		for (CrfMetaData crf : crfs) {
			if (cids.contains(crf.getId()) && !cidsrec.contains(crf.getId())) {
				stdcrfssave.add(copyCrf(crf, sm));
			}
		}
		
		
		stdcrfsUpdate.addAll(stdcrfssave);
		crfDAO.updatestdCrf(stdcrfsUpdate);
	}
	
	private CrfMetaDataStd copyCrf(CrfMetaData crf, StudyMaster sm) {
		CrfMetaDataStd scrf = new CrfMetaDataStd();
		scrf.setStd(sm);
		scrf.setCrfmetaId(crf.getId());
		scrf.setCode(crf.getCode());
		scrf.setCrfName(crf.getCrfName());
		scrf.setCrfDesc(crf.getCrfDesc());
		scrf.setGender(crf.getGender());
		scrf.setVersion(crf.getVersion());
		scrf.setOrderNo(crf.getOrderNo());
		scrf.setStringCellValue(crf.getStringCellValue());
		crfDAO.savestdCrf1(scrf);
		scrf.setSections(getSectionStd(crf.getSections(), scrf));
		for(CRFSectionsStd s : scrf.getSections()) {
			for(CrfItemsStd i : s.getItemList()) {
				System.out.println(i);
				List<CRFItemValuesStd> irvl = i.getItemResponceValues();			
			}
		}
		crfDAO.updatestdCrf(scrf);
		scrf.setGroups(copyGroupStd(crf.getGroups(), scrf));
		crfDAO.updatestdCrf(scrf);
		return scrf;
	}

	
	private List<CRFSectionsStd> getSectionStd(List<CRFSections> sections, CrfMetaDataStd scrf) {
		// TODO Auto-generated method stub
		List<CRFSectionsStd> ssections = new ArrayList<>();
		for (CRFSections sec : sections) {
			if (sec.isActive()) {
				CRFSectionsStd s = new CRFSectionsStd();
				s.setCrfId(scrf);
				s.setCrfSectionId(sec.getId());
				s.setSectionName(sec.getSectionName());
				s.setSectionDesc(sec.getSectionDesc());
				s.setMaxRows(sec.getMaxRows());
				s.setMaxColumns(sec.getMaxColumns());
				s.setUserRole(sec.getUserRole());
				s.setGender(sec.getGender());
				s.setOrderNo(sec.getOrderNo());
				s.setContainsGroup(sec.getContainsGroup());

				s.setUserRoleUpdateReason(sec.getUserRoleUpdateReason());
				s.setDataSetTechName(sec.getDataSetTechName());
				s.setDataSetTechValue(sec.getDataSetTechValue());
				s.setStatusUpdate(sec.getStatusUpdate());
				s.setCreatedOn(sec.getCreatedOn());
				s.setUpdatedOn(sec.getUpdatedOn());
				crfDAO.savestdCrfSections1(s);
				s.setItemList(copyItemList(sec.getItemList(), scrf, s));
				ssections.add(s);
			}
		}
//		employeeDao.savestdCrfSections(ssections);
		return ssections;
	}
	
	private Map<Long, CrfItemsStd> tempitems;

	private List<CrfItemsStd> copyItemList(List<CrfItems> itemList, CrfMetaDataStd scrf, CRFSectionsStd s) {
		List<CrfItemsStd> sitems = new ArrayList<>();
		tempitems = new HashMap<>();
		for (CrfItems i : itemList) {
			if (i.isActive()) {
				CrfItemsStd si = new CrfItemsStd();
				si.setCrfItemId(i.getCrfId().getId());
				si.setCrfId(scrf);
				si.setSection(s);
				tempitems.put(i.getId(), si);
				si.setItemName(i.getItemName());
				si.setItemDesc(i.getItemDesc());
				si.setItemDescRight(i.getItemDescRight());
				si.setItemType(i.getItemType());
				si.setResponseValue(i.getResponseValue());
				si.setResponseType(i.getResponseType());
				si.setRowNumber(i.getRowNumber());
				si.setColumnNo(i.getColumnNo());
				si.setValidation(i.getValidation());
				si.setPattern(i.getPattern());

				si.setCount(i.getCount());
				si.setDataSetTechName(i.getDataSetTechName());
				si.setDataSetTechValue(i.getDataSetTechValue());
				si.setType(i.getType());
				si.setIeTestCode(i.getIeTestCode());
				si.setHeader(i.getHeader());
				si.setSubheader(i.getSubheader());
				si.setIsSelected(i.getIsSelected());
				si.setItem_oid(i.getItem_oid());
				si.setStatusUpdate(i.getStatusUpdate());
				si.setCreatedOn(i.getCreatedOn());
				si.setUpdatedOn(i.getUpdatedOn());
				si.setChangeStatsu(i.getChangeStatsu());
				si.setGropItemName(i.getGropItemName());
				si.setTextStyle(i.getTextStyle());
				si.setFontStyle(i.getFontStyle());
				si.setFontSize(i.getFontSize());
				si.setUnderLine(i.getUnderLine());
				si.setPrintDesc(i.getPrintDesc());
				si.setPrintContent(i.getPrintContent());
				si.setDescCellWidth(i.getDescCellWidth());
				si.setValueWithImage(i.getValueWithImage());
				si.setValueCellWidth(i.getValueCellWidth());
				si.setCellHeight(i.getCellHeight());
				si.setCheckAlign(i.getCheckAlign());
				sitems.add(si);
				crfDAO.savestdCrfEle1(si);
				si.setItemResponceValues(copyitemResponceValues(i.getItemResponceValues(), si));
			}
		}
		return sitems;
	}
	
	private List<CRFItemValuesStd> copyitemResponceValues(List<CRFItemValues> itemResponceValues, CrfItemsStd si) {
		// TODO Auto-generated method stub
		List<CRFItemValuesStd> ivlist = new ArrayList<>();
		for (CRFItemValues v : itemResponceValues) {
			CRFItemValuesStd iv = new CRFItemValuesStd();
			iv.setCrfItemValueId(v);
			iv.setElemenstValue(v.getElemenstValue());
			iv.setCrfItem(si);
			ivlist.add(iv);
		}
		crfDAO.savestdCrfEleVal(ivlist);
		return ivlist;
	}
	
	private List<CRFGroupItemStd> copyGroupStd(List<CRFGroupItem> groups, CrfMetaDataStd scrf) {
		// TODO Auto-generated method stub
		List<CRFGroupItemStd> sglist = new ArrayList<>();
		for (CRFGroupItem g : groups) {
			if (g.isActive()) {
				CRFGroupItemStd sg = new CRFGroupItemStd();
				sg.setCrfId(scrf);
				sg.setGroupId(g.getId());
				sg.setGroupName(g.getGroupName());
				sg.setGroupDesc(g.getGroupDesc());
				List<CrfItems> itemList = g.getItemList();
				List<CrfItemsStd> sigl = new ArrayList<>();
				for (CrfItems i : itemList) {
					if (tempitems != null && tempitems.containsKey(i.getId())) {
						CrfItemsStd si = tempitems.get(i.getId());
						si.setGroup(sg);
						sigl.add(si);
					}
				}
				sg.setItemList(sigl);
				sg.setMaxColumns(g.getMaxColumns());
				sg.setDisplayRow(g.getDisplayRow());
				sg.setMaxRow(g.getMaxRow());
				sg.setDataSetTechName(g.getDataSetTechName());
				sg.setDataSetTechValue(g.getDataSetTechValue());
				sg.setStatusUpdate(g.getStatusUpdate());
				sg.setCreatedOn(g.getCreatedOn());
				sg.setUpdatedOn(g.getUpdatedOn());
				sg.setLabel_values(g.getLabel_values());
				sg.setFontSize(g.getFontSize());
				sg.setFontStyle(g.getFontStyle());
				sg.setTextStyle(g.getTextStyle());
				sg.setNoOfRowsDataContains(g.getNoOfRowsDataContains());
				sg.setChangeStatsu(g.getChangeStatsu());
				sg.setGroupValueSizes(g.getGroupValueSizes());
				sg.setTempId(g.getTempId());
				sglist.add(sg);
				crfDAO.savestdCrfGroups1(sg);
			}
		}

//		employeeDao.savestdCrfGroups(sglist);
		return sglist;
	}

	
	@Override
	public void updateStudyAccessionCrfDescrpency(StudyAccessionCrfDescrpency scd, StudyAccessionCrfDescrpencyLog log, CrfSectionElementDataAudit saudit) {
		// TODO Auto-generated method stub
		crfDAO.updateStudyAccessionCrfDescrpency(scd, log, saudit);
	}
	
	@Override
	public void updateStudyObservatoinCrfDescrpency(CrfDescrpency scd, CrfDescrpencyLog log, CrfDescrpencyAudit descAudit) {
		// TODO Auto-generated method stub
		crfDAO.updateStudyObservatoinCrfDescrpency(scd, log, descAudit);
	}

	@Override
	public List<StudyAccessionCrfDescrpency> allStudyAccessionCrfDescrepencyOfElement(Long id) {
		return crfDAO.allStudyAccessionCrfDescrepencyOfElement(id);
	}
	
	@Override
	public List<CrfDescrpency> allStudyCrfDescrpencyOfElement(Long id) {
		return crfDAO.allStudyCrfDescrpencyOfElement(id);
	}

	
	@Override
	public StudyAccessionCrfSectionElementData studyAccessionCrfSectionElementDataWithId(
			Long studyAccessionCrfSectionElementDataId) {
		// TODO Auto-generated method stub
		return crfDAO.studyAccessionCrfSectionElementDataWithId(studyAccessionCrfSectionElementDataId);
	}

	@Override
	public Long crfIdByName(String name) {
		// TODO Auto-generated method stub
		return crfDAO.crfIdByName(name);
	}

	@Override
	public CrfSectionElementData crfSectionElementDataWithId(Long crfSectionElementDataId) {
		// TODO Auto-generated method stub
		return crfDAO.crfSectionElementDataWithId(crfSectionElementDataId);
	}

	@Override
	public List<Crf> findAllActiveCrfsConfigurationForAcclimatization() {
		return crfDAO.findAllActiveCrfsConfigurationForAcclimatization();
	}

	@Override
	public List<Crf> findAllActiveCrfsConfigurationForObservation() {
		return crfDAO.findAllActiveCrfsConfigurationForObservation();
	}

	@Override
	public List<Crf> findAllActiveCrfsConfigurationForTreatment() {
		// TODO Auto-generated method stub
		return crfDAO.findAllActiveCrfsConfigurationForTreatment();
	}

	@Override
	public List<RoleMaster> allRoleMastersOfIds(List<Long> roleList) {
		// TODO Auto-generated method stub
		return crfDAO.allRoleMastersOfIds(roleList);
	}

	
	
	
	
	
}