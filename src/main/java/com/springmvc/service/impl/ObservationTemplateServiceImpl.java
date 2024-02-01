package com.springmvc.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.covide.excels.ExcelDataReading;
import com.covide.template.dto.TemplateAuditTrailDto;
import com.covide.template.dto.TemplateCommentsDto;
import com.covide.template.dto.TemplateFilesDto;
import com.springmvc.dao.ObservationTemplateDao;
import com.springmvc.model.ObserVationTemplates;
import com.springmvc.model.StudyLevelObservationTemplateData;
import com.springmvc.model.StudyLevelObservationTemplateDataLog;
import com.springmvc.model.TemplateFileAuditTrail;
import com.springmvc.model.TemplateFileAuditTrailLog;
import com.springmvc.service.ObservationTemplateService;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;


@Service("observationTemplateService")
public class ObservationTemplateServiceImpl implements ObservationTemplateService {
	
	@Autowired
	ObservationTemplateDao obsertTempDao;

	@Override
	public TemplateFilesDto getTempleteForView(Long studyId, Long groupId, Long subGroupId, Long subgroupAnimalId,
			Long templateId, HttpServletRequest request) {
		String result ="";
		StudyLevelObservationTemplateData slotdata = null;
		ObserVationTemplates obvt = null;
		TemplateFilesDto tfdto = null;
		try {
			tfdto = obsertTempDao.getTemplateFilesRecords(studyId, groupId, subGroupId, subgroupAnimalId, templateId);
			if(tfdto != null) {
				slotdata = tfdto.getSlotdata();
				if(slotdata != null) {
					result  = writeTemplateFile(slotdata.getBlob());
					tfdto.setFile(result);
				}else {
					obvt = tfdto.getObvt();
					if(obvt != null) {
						result = writeTemplateFile(obvt.getBlob());
						tfdto.setFile(result);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tfdto;
	}

	private String writeTemplateFile(Blob blob)throws SQLException {
		byte[] bdata = blob.getBytes(1L, (int) blob.length());
		BASE64Encoder encoder = new BASE64Encoder();
		String fileStr  = encoder.encode(bdata);
		return fileStr;
	}

	@Override
	public String saveOrupdateTemplateFile(long studyId, long groupId, long subGroupId, long subGroupanimalId,
			long templateId, String modifidDoc, String username, HttpServletRequest request) {
		String result ="";
		TemplateFilesDto tfdto = null;
		try {
			tfdto = obsertTempDao.getTemplateFilesRecords(studyId, groupId, subGroupId, subGroupanimalId, templateId);
			if(tfdto != null)
				tfdto.setGroupId(groupId);
				tfdto.setSubGroupId(subGroupId);
				tfdto.setSubgroupAnimalId(subGroupanimalId);
				tfdto.setStudyId(studyId);
				result = updateAndAuditTemplateFile(modifidDoc, tfdto, request, username);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private String updateAndAuditTemplateFile(String modifidDoc, TemplateFilesDto tfdto, HttpServletRequest request, String username) {
		String result ="Failed";
		String filePath = "";
		String modDocPath = "";
		String type = "";
		Blob blob = null;
		String fileName = "";
		String modifiedVals = "";
		StudyLevelObservationTemplateData slotdata = null;
		ObserVationTemplates obvt = null;
		List<TemplateFileAuditTrail>auditList = null;
		/*Map<Integer, List<String>> excelDataMap = null;
		Map<Integer, List<String>> excelDatamap2 = null;*/
		Map<String, Map<Integer, Map<Integer, String>>> excelDataMap = null;
		Map<String, Map<Integer, Map<Integer, String>>> updatedExcelMap = null;
		BASE64Decoder decoder = new BASE64Decoder();
		ExcelDataReading edr = new ExcelDataReading();
		String realPath = request.getServletContext().getRealPath("/");
		String path = realPath + "/AuditCapture/";
		File directory = new File(path);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		try {
			slotdata = tfdto.getSlotdata();
			obvt = tfdto.getObvt();
			if(slotdata != null) {
				type = slotdata.getObstData().getType();
				filePath = path + slotdata.getObstData().getFileName();
				blob = slotdata.getBlob();
				fileName = slotdata.getObstData().getFileName();
			}
				
			if(obvt != null) {
				type = obvt.getObstd().getType();
				filePath = path + obvt.getObstd().getFileName();
				blob = obvt.getBlob();
				fileName = obvt.getObstd().getFileName();
			}
			if (!filePath.equals("")) {
			   if(blob != null) {
				   byte[] arr = blob.getBytes(1L, (int) blob.length());
					//decodedByte = decoder.decodeBuffer(dynamicFile);
					FileOutputStream fos = new FileOutputStream(filePath);
					fos.write(arr);
					fos.flush();
					fos.close();  
			   }
				// Reading Original Excel
				excelDataMap = edr.ReadXlsxExcelData(type, filePath);
			}
			// Reading updated Excel
			String[] temp = null;
			if(!fileName.equals(""))
			 temp = fileName.split("\\.");
			if(temp.length > 0)
				modDocPath = path +"updatedExcelFile."+temp[1];
			if (!modDocPath.equals("")) {
				if (!modifidDoc.equals("")) {
					byte[] decodedByte = null;
					decodedByte = decoder.decodeBuffer(modifidDoc);
					FileOutputStream fos = new FileOutputStream(modDocPath);
					fos.write(decodedByte);
					fos.flush();
					fos.close();
				}
				// Reading Original Excel
				updatedExcelMap = edr.ReadXlsxExcelData(type, modDocPath);
			}
			modifiedVals = getModifiedAuditData(excelDataMap, updatedExcelMap, modifiedVals);
			if(modifiedVals != null && !modifiedVals.equals("")) {
				auditList = verifyDraftTemplateAuditData(modifiedVals, username);	
			}
			//Updated Record Saving Part
			long save = saveOrUpdateTemplateFileWithLog(username, tfdto, modifidDoc, auditList);
			if(save > 0) 
				result ="success";
			else
				result ="Failed";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private String getModifiedAuditData(Map<String, Map<Integer, Map<Integer, String>>> excelDataMap,
			Map<String, Map<Integer, Map<Integer, String>>> updatedExcelMap, String modifiedVals) {
		try {
			if ((excelDataMap != null && excelDataMap.size() > 0 ) && (updatedExcelMap != null && updatedExcelMap.size() > 0)) {
				for(Map.Entry<String, Map<Integer, Map<Integer, String>>> entryMap : updatedExcelMap.entrySet()) {
					String sheetName = entryMap.getKey();
					Map<Integer, Map<Integer, String>> rowMap = entryMap.getValue();
					if(rowMap != null && rowMap.size() > 0) {
						for(Map.Entry<Integer, Map<Integer, String>> colMap : rowMap.entrySet()) {
							int rowNo = colMap.getKey();
							Map<Integer, String> upColMap = colMap.getValue();
							for(Map.Entry<Integer, String> colData : upColMap.entrySet()) {
								int colNo = colData.getKey();
								String upColData = colData.getValue();
								Map<Integer, Map<Integer, String>> excelRowMap = excelDataMap.get(sheetName);
								if(excelRowMap != null && excelRowMap.size() > 0) {
									Map<Integer, String> excelColMap = excelRowMap.get(rowNo);
									String excelColStr = excelColMap.get(colNo);
									String[] tempArr = upColData.split("\\##");
									String[] temp2Arr = excelColStr.split("\\##");
									if(tempArr.length > 0 && temp2Arr.length > 0) {
										if(tempArr.length > 1 && temp2Arr.length ==1) {
											String val = sheetName+"##"+""+"##"+tempArr[1]+"##"+rowNo+"##"+tempArr[0];
											if(modifiedVals.equals(""))
												modifiedVals = val;
											else  modifiedVals = modifiedVals +","+val;
										}else if(tempArr.length == 1 && temp2Arr.length >1) {
												String val = sheetName+"##"+temp2Arr[1]+"##"+""+"##"+rowNo+"##"+tempArr[0];
												if(modifiedVals.equals(""))
													modifiedVals = val;
												else  modifiedVals = modifiedVals +","+val;
											
										}else if(tempArr.length > 1 && temp2Arr.length >1) {
											if(!tempArr[1].equals(temp2Arr[1])) {
												String val = sheetName+"##"+temp2Arr[1]+"##"+tempArr[1]+"##"+rowNo+"##"+tempArr[0];
												if(modifiedVals.equals(""))
													modifiedVals = val;
												else  modifiedVals = modifiedVals +","+val;
											}
										}
										
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modifiedVals;
	}

	private long saveOrUpdateTemplateFileWithLog(String username, TemplateFilesDto tfdto, String modifidDoc, List<TemplateFileAuditTrail> auditList) {
		long no = 0;
		StudyLevelObservationTemplateDataLog slotdLog = null;
		StudyLevelObservationTemplateData slotdata = null; 
		ObserVationTemplates obvt = null;
		boolean flag = false;
		Blob blob = null;
		boolean auditFlag = false;
		try {
			slotdata = tfdto.getSlotdata();
			obvt = tfdto.getObvt();
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] decodedByte = null;
			decodedByte = decoder.decodeBuffer(modifidDoc);
			blob = new SerialBlob(decodedByte);
			if(slotdata != null) {
				slotdLog = new StudyLevelObservationTemplateDataLog();
				slotdLog.setBlob(slotdata.getBlob());
				slotdLog.setCreatedBy(username);
				slotdLog.setCreatedOn(new Date());
				slotdLog.setSlotd(slotdata);
				slotdLog.setReAssignComments(slotdata.getReAssignComments());
				slotdLog.setReviewComments(slotdata.getReviewComments());
				long slotdLogNo = obsertTempDao.saveTemplateLogRecord(slotdLog);
				if(slotdLogNo > 0) {
					slotdata.setUpdatedBy(username);
					slotdata.setUpdatedOn(new Date());
					slotdata.setBlob(blob);
					flag = obsertTempDao.updateTemplateRecord(slotdata);
					if(flag) {
						if(auditList != null && auditList.size() > 0)
							auditFlag = saveOrUpdateStudyLevelTmplateFileData(auditList, slotdata, username);
						else auditFlag = true;
						if(auditFlag)
							no = 1;
					}
						
				}
			}else if(obvt != null) {
				slotdata = new StudyLevelObservationTemplateData();
				slotdata.setAnimalId(tfdto.getSubgroupAnimalId());
				slotdata.setBlob(blob);
				slotdata.setCreatedBy(username);
				slotdata.setCreatedOn(new Date());
				slotdata.setGroupId(tfdto.getGroupId());
				slotdata.setObstData(obvt.getObstd());
				slotdata.setReAssignComments("");
				slotdata.setReviewComments("");
				slotdata.setStudyId(tfdto.getStudyId());
				slotdata.setSubGroupId(tfdto.getSubGroupId());
				long slotdataNo = obsertTempDao.saveStudyLevelObservationTemplateDataRecord(slotdata);
				if(slotdataNo > 0) {
					if(auditList != null && auditList.size() > 0)
						auditFlag = saveOrUpdateStudyLevelTmplateFileData(auditList, slotdata, username);
					else auditFlag = true;
					if(auditFlag)
					    no = slotdataNo;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return no;
	}

	private boolean saveOrUpdateStudyLevelTmplateFileData(List<TemplateFileAuditTrail> auditList,
			StudyLevelObservationTemplateData slotdata, String username) {
		boolean flag=false;
		boolean logFlag = false;
		boolean updateFlag = false;
		int no = 0; 
		List<TemplateFileAuditTrailLog> tfatLogList = new ArrayList<>();
		List<TemplateFileAuditTrail> datList = new ArrayList<>();
		List<TemplateFileAuditTrail> updatList = new ArrayList<>();
		List<String> positions = new ArrayList<String>();
		Map<String, TemplateFileAuditTrail> map = new HashMap<String ,TemplateFileAuditTrail>();
		for(TemplateFileAuditTrail audit : auditList){
			positions.add(audit.getPosition().trim());
			map.put(audit.getPosition(), audit);
	//		System.out.println("map Id's : "+audit.getSm().getId()+"_"+audit.getTemp_id().getSno()+"_"+audit.getPosition());
		}
	
		if(positions.size() !=0) {
			Map<String, TemplateFileAuditTrail> tempMap = new HashMap<>();
			List<TemplateFileAuditTrail> result = null;
			if(slotdata != null)
				result = obsertTempDao.getDraftTemplateAuditRecord(positions, slotdata);
			if(result != null && result.size() > 0) {
				for (TemplateFileAuditTrail tfat : result) {
					tempMap.put(tfat.getPosition(), tfat);
				}
			}
			if(map.size() > 0) {
				for(Map.Entry<String, TemplateFileAuditTrail> fMap : map.entrySet()) {
					String keyVal = fMap.getKey();
					TemplateFileAuditTrail auditPojo = fMap.getValue();
					auditPojo.setSlotd(slotdata);
					if(tempMap.get(keyVal) != null) {
						TemplateFileAuditTrail tfat = tempMap.get(keyVal);
						if(auditPojo != null) {
							TemplateFileAuditTrailLog tfaLog = new TemplateFileAuditTrailLog();
							BeanUtils.copyProperties(auditPojo, tfaLog);
							tfaLog.setOld_value(tfat.getNew_value());
							tfaLog.setNew_value(auditPojo.getNew_value());
							tfaLog.setModifiedBy(username);
							tfaLog.setModified_date(new Date());
							tfaLog.setTfaudit(tfat);
							tfatLogList.add(tfaLog);
							
							tfat.setOld_value(tfat.getNew_value());
							tfat.setNew_value(auditPojo.getNew_value());
							tfat.setModified_date(new Date());
							tfat.setModifiedBy(username);
							updatList.add(tfat);
						}
					}else {
						auditPojo.setSlotd(slotdata);
						datList.add(auditPojo); 
					}
				}
			}
			
			if(tfatLogList.size() > 0) {
				logFlag = obsertTempDao.saveTemplateAuditTrailLogRecords(tfatLogList);
			}else logFlag = true;
			if(updatList.size() > 0) {
				updateFlag = obsertTempDao.updateTemplateAuditTrailRecords(updatList);
			}else updateFlag = true;
			if(datList.size() > 0) {
				no = obsertTempDao.saveTemplateAuditTrailRecordList(datList);
			}else no =1;
		
			if(logFlag && updateFlag && no > 0)
				flag = true;
		}else flag = true;
		return flag;
	}

	private List<TemplateFileAuditTrail> verifyDraftTemplateAuditData(String modifiedVals, String username) {
		List<TemplateFileAuditTrail> list1 = new ArrayList<>();
		if (modifiedVals != null && !modifiedVals.equals("")) {
			String array[] = modifiedVals.split(",");
			for (int i = 0; i < array.length; i++) {
				String str1 = array[i];
				String[] temp = str1.split("##");
				// System.out.println("length of array is : "+temp.length);
//				if (temp.length == 5) {
					String st = temp[2];
					String st1 = temp[1];
					if (!st.contains("%") && !st1.contains("%")) {
						TemplateFileAuditTrail dtat = new TemplateFileAuditTrail();
						// System.out.println("count is :"+i);
						dtat.setPosition(temp[0] + "#" + temp[4] + "#" + temp[3]);
						dtat.setSheetName(temp[0]);
						dtat.setCol(temp[4]);
						dtat.setRow(temp[3]);
						dtat.setOld_value(temp[1]);
						dtat.setNew_value(temp[2]);
						dtat.setModifiedBy(username);
						dtat.setModified_date(new Date());
//						dtat.setSlotd(slotdata);
						list1.add(dtat);
					}
//				}
			}

		}

		return list1;
	}

	@Override
	public TemplateFilesDto getTemplateFileDetails(Long studyId, Long groupId, Long subGroupId, Long subGroupanimalId,
			Long templateId) {
		return obsertTempDao.getTemplateFileDetails(studyId, groupId, subGroupId, subGroupanimalId,templateId);
	}

	@Override
	public boolean completeReviewForTemplate(Long templateId, String statusStr,String comments, String userName) {
		return obsertTempDao.completeReviewForTemplate(templateId, statusStr, comments, userName);
	}

	@Override
	public TemplateAuditTrailDto getTemplateAuditTrailData(Long templateId) {
		return obsertTempDao.getTemplateAuditTrailData(templateId);
	}

	@Override
	public List<TemplateCommentsDto> getTemplateCommentsData(Long templateId, String type) {
		List<TemplateCommentsDto> tcdtoList = new ArrayList<>();
		StudyLevelObservationTemplateData slotdata = null;
		List<StudyLevelObservationTemplateDataLog> slotLogList = null;
		TemplateCommentsDto tcdto = null;
		String comments ="";
		try {
			tcdto = obsertTempDao.getTemplateCommentsData(templateId);
			if(tcdto != null) {
				slotdata = tcdto.getSlotdata();
				slotLogList = tcdto.getSlotLogList();
				if(slotdata != null) {
					if(type.equals("reviewComments")) {
						tcdto = new TemplateCommentsDto();
						tcdto.setComments(slotdata.getReviewComments());
						tcdto.setCreatedBy(slotdata.getUpdatedBy());
						tcdto.setCreatedOn(slotdata.getUpdatedOn());
						tcdtoList.add(tcdto);
						comments = slotdata.getReviewComments();
					}else {
						tcdto = new TemplateCommentsDto();
						tcdto.setComments(slotdata.getReAssignComments());
						tcdto.setCreatedBy(slotdata.getUpdatedBy());
						tcdto.setCreatedOn(slotdata.getUpdatedOn());
						tcdtoList.add(tcdto);
						comments = slotdata.getReAssignComments();
						
					}
					
				}
				if(slotLogList != null && slotLogList.size() > 0) {
					for(StudyLevelObservationTemplateDataLog slotl : slotLogList) {
						if(type.equals("reviewComments")) {
							if(!comments.equals(slotl.getReviewComments())) {
								tcdto = new TemplateCommentsDto();
								tcdto.setComments(slotl.getReviewComments());
								tcdto.setCreatedBy(slotl.getCreatedBy());
								tcdto.setCreatedOn(slotl.getCreatedOn());
								tcdtoList.add(tcdto);
							}
						}else {
							if(!comments.equals(slotl.getReAssignComments())) {
								tcdto = new TemplateCommentsDto();
								tcdto.setComments(slotl.getReAssignComments());
								tcdto.setCreatedBy(slotl.getCreatedBy());
								tcdto.setCreatedOn(slotl.getCreatedOn());
								tcdtoList.add(tcdto);
							}
							
						}
						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tcdtoList;
	}

}
