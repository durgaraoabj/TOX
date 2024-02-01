package com.springmvc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.covide.crf.dto.Crf;
import com.covide.dto.CalculateStringValue;
import com.covide.dto.MeanAndSDStringCalculation;
import com.covide.dto.MeanCalculation;
import com.covide.dto.StandaredDeviation;
import com.springmvc.dao.MultiFormCalculationDao;
import com.springmvc.service.MultiFormCalculationService;

@Service("multiFormCalculationService")
public class MultiFormCalculationServiceImpl implements MultiFormCalculationService {
	
	@Autowired
	MultiFormCalculationDao mformCalcDao;

	@Override
	public List<Crf> getCrfsRecordsList() {
		return mformCalcDao.getCrfsRecordsList();
	}

	@Override
	public Crf getCrfRecord(Long crfId) {
		return mformCalcDao.getCrfRecord(crfId);
	}

	@Override
	public Map<String, String> getgetFormulaCalculationData(Long crfId, Long animalId, Long studyId) {
		Map<String, String> calcMap = new HashMap<>();
		Map<String, String> formulaMap = null;
		Map<String, String> actEleWithValMap = new HashMap<>(); //orignalname reaple value
		String str = "";
		String initialStr = "";
		try {
			formulaMap = mformCalcDao.getFormulaDataElements(crfId);
			if(formulaMap != null && formulaMap.size() > 0) {
				for(Map.Entry<String, String> forMap : formulaMap.entrySet()) {
					String finalStr = "";
					String element = forMap.getKey();
//					String str ="(valOf(form1!ele1)+valOf(form1!ele2))+valOf(form1!ele3)*(valOf(form1!ele4)+valOf(form2!ele5))";
					str =forMap.getValue();
					if(!str.contains("MEAN") && !str.contains("SD")) {
						initialStr = str;
						str = str.replaceAll("\\(", "");
						str = str.replaceAll("\\)", "");
//						str = str.replaceAll("valOf", "@");
						String[] arr = str.split("valOf");
						for(int i=0; i<arr.length; i++) {
//							System.out.println("arr["+i+"]  :"+arr[i]);
							String st = arr[i];
							st = st.replaceAll("[-+^*/]", " ");
							if(!st.equals("")) {
								String actElement = st.trim();
								String[] temp = st.split("\\!");
								String eleValue = mformCalcDao.getActualElementValue(temp[0].trim(), temp[1].trim(), studyId, animalId);
								if(eleValue == null || eleValue.equals(""))
									eleValue ="0";
								actEleWithValMap.put(actElement.trim(), eleValue.trim());
							}
						}
						if(actEleWithValMap.size() > 0) {
							for(Map.Entry<String, String> dataMap : actEleWithValMap.entrySet()) {
								String ele = dataMap.getKey();
								String eleVal = dataMap.getValue();
								if(!initialStr.equals("")) {
									if(finalStr.equals("")) {
										if(initialStr.contains(ele.trim())) {
											String rs = initialStr.replaceAll(ele.trim(), eleVal);
											finalStr = rs;
										}
									}else {
										if(finalStr.contains(ele.trim())) {
											finalStr = finalStr.replaceAll(ele.trim(), eleVal);
										}
									}
									
								}
							}
						}
//						System.out.println("Final Sting value is :"+finalStr);
						finalStr = finalStr.replaceAll("valOf", "");
						int calulatedVal = CalculateStringValue.getCalculateStringValue(finalStr);
//						System.out.println("Calculated Value is : "+calulatedVal);
						calcMap.put(element, calulatedVal+"");
//						System.out.println(calcMap.size());
					}else {
						if(str.contains("MEAN")) {
//							String str = "MEAN(form1!ele1,form1!ele2,form1!ele3,form1!ele4)+MEAN(form1!ele1,form1!ele2,form1!ele3,form1!ele6)";
							String[] meanArr = str.split("MEAN");
							List<String> meanList = new ArrayList<>();
							double meanVal = 0;
							for(int i=0; i<meanArr.length; i++) {
								if(!meanArr[i].equals("")) {
									meanList.add(meanArr[i]);
//									System.out.println("meanArr["+i+"]  is :"+meanArr[i]);
									String st = meanArr[i];
									st = st.replaceAll("\\(", "");
									st = st.replaceAll("\\)", "");
									st = st.replaceAll("[-+^*/]", " ");
									System.out.println("final String of "+i+" is :"+st);
									if(!st.equals("")) {
										String[] tempArr = st.split("\\,");
										double[] inputArr = new double[tempArr.length];
										String rst = "";
										if(tempArr.length > 0) {
											for(int j=0; j<tempArr.length; j++) {
												String[] tempStr = tempArr[j].trim().split("!");
												String eleValue = mformCalcDao.getActualElementValue(tempStr[0].trim(), tempStr[1].trim(), studyId, animalId);
												if(eleValue == null || eleValue.equals(""))
													eleValue ="0";
												double val = Double.parseDouble(eleValue);
												inputArr[j] = val;
												if(rst.equals(""))
													rst = tempArr[j].trim();
												else rst = rst+","+tempArr[j].trim();
											}
										}
										MeanCalculation meanCalc = new MeanCalculation();
										double finalVal = meanCalc.calculateMeanValue(inputArr);
//										System.out.println("Final Mean Val is : "+finalVal);
										str = str.replaceAll(rst, finalVal+"");
										if(meanList.size() == 1)
											meanVal = finalVal;
									}
								}
							}
//							System.out.println("Final String is : "+str);
							str = str.replaceAll("MEAN", "");
							if(meanList.size() > 1) {
								double val = MeanAndSDStringCalculation.getCalculateStringValue(str);
								calcMap.put(element, String.format("%.2f", val));
//								System.out.println(calcMap.size());
							}else {
//								finalNo = (int)(Math.round(meanVal));
//								System.out.println("finalNo is : "+String.format("%.2f", meanVal));
								calcMap.put(element, String.format("%.2f", meanVal));
//								System.out.println(calcMap.size());
							}
						}
						if(str.contains("SD")){
							
//							String str = "SD(FSRIAA!foot1,FSRIAA!foot2,FSRIAA!foot3)+SD(FSRIAA!foot1,FSRIAA!foot2,FSRIAA!foot3)";
							String[] sdArr = str.split("SD");
							List<String> sdList = new ArrayList<>();
							double sdVal = 0;
							for(int i=0; i<sdArr.length; i++) {
								if(!sdArr[i].equals("")) {
									sdList.add(sdArr[i]);
//									System.out.println("meanArr["+i+"]  is :"+meanArr[i]);
									String st = sdArr[i];
									st = st.replaceAll("\\(", "");
									st = st.replaceAll("\\)", "");
									st = st.replaceAll("[-+^*/]", " ");
//									System.out.println("final String of "+i+" is :"+st);
									if(!st.equals("")) {
										String[] tempArr = st.split("\\,");
										double[] inputArr = new double[tempArr.length];
										String rst = "";
										if(tempArr.length > 0) {
											for(int j=0; j<tempArr.length; j++) {
												String[] tempStr = tempArr[j].trim().split("!");
												String eleValue = mformCalcDao.getActualElementValue(tempStr[0].trim(), tempStr[1].trim(), studyId, animalId);
												if(eleValue == null || eleValue.equals(""))
													eleValue ="0";
												double val = Double.parseDouble(eleValue);
												inputArr[j] = val;
												if(rst.equals(""))
													rst = tempArr[j].trim();
												else rst = rst+","+tempArr[j].trim();
											}
										}
										StandaredDeviation sdPojo= new StandaredDeviation();
										double finalVal = sdPojo.caluclateStanderedDeviation(inputArr);
//										System.out.println("Final Mean Val is : "+finalVal);
										str = str.replaceAll(rst, finalVal+"");
										if(sdList.size() == 1)
											sdVal = finalVal;
									}
								}
							}
//							System.out.println("Final String is : "+str);
							str = str.replaceAll("SD", "");
							if(sdList.size() > 1) {
								double val = MeanAndSDStringCalculation.getCalculateStringValue(str);
								calcMap.put(element, String.format("%.2f", val));
//								System.out.println(calcMap.size());
							}else {
//								finalNo = (int)(Math.round(meanVal));
//								System.out.println("finalNo is : "+String.format("%.2f", meanVal));
								calcMap.put(element, String.format("%.2f", sdVal));
//								System.out.println(calcMap.size());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calcMap;
	}

	@Override
	public Map<String, String> getFormulaCalculationDataForCurrentForm(Long crfId, List<String> dataString,
			Long studyId) {
		Map<String, String> dataMap = new HashMap<>();
		Map<String, String> calcMap = new HashMap<>();
		Map<String, String> formulaMap = null;
		Map<String, String> actEleWithValMap = new HashMap<>(); //orignalname reaple value
		String initialStr = "";
		try {
			if(dataString.size() > 0) {
				for(String s : dataString) {
					String[] temp = s.split("\\@@");
					dataMap.put(temp[0], temp[1]);
				}
			}
			formulaMap = mformCalcDao.getFormulaDataElements(crfId);
			if(formulaMap != null && formulaMap.size() > 0) {
				for(Map.Entry<String, String> forMap : formulaMap.entrySet()) {
					String finalStr = "";
					String element = forMap.getKey();
//					String str ="(valOf(form1!ele1)+valOf(form1!ele2))+valOf(form1!ele3)*(valOf(form1!ele4)+valOf(form2!ele5))";
					String formula =forMap.getValue();
					String[] currentArr = formula.split("CURRENT");
					if(currentArr.length > 0) {
						for(String str : currentArr) {
							if(!str.equals("")) {
								if(!str.contains("MEAN") && !str.contains("SD")) {
									initialStr = str;
									str = str.replaceAll("\\(", "");
									str = str.replaceAll("\\)", "");
//									str = str.replaceAll("valOf", "@");
									String[] arr = str.split("valOf");
									for(int i=0; i<arr.length; i++) {
//										System.out.println("arr["+i+"]  :"+arr[i]);
										String st = arr[i];
										st = st.replaceAll("[-+^*/]", " ");
										if(!st.equals("")) {
											String actElement = st.trim();
											String eleValue = dataMap.get(actElement);
											actEleWithValMap.put(actElement.trim(), eleValue.trim());
										}
									}
									if(actEleWithValMap.size() > 0) {
										for(Map.Entry<String, String> valuesMap : actEleWithValMap.entrySet()) {
											String ele = valuesMap.getKey();
											String eleVal = valuesMap.getValue();
											/*if(!initialStr.equals("")) {
												if(finalStr.equals("")) {
													if(initialStr.contains(ele.trim())) {
														String rs = initialStr.replaceAll(ele.trim(), eleVal);
														finalStr = rs;
													}
												}else {
													if(finalStr.contains(ele.trim())) {
														finalStr = finalStr.replaceAll(ele.trim(), eleVal);
													}
												}
												
											}*/
											formula = formula.replaceAll(ele.trim(), eleVal);
										}
									}
//									System.out.println("Final Sting value is :"+finalStr);
									/*finalStr = finalStr.replaceAll("valOf", "");
									int calulatedVal = CalculateStringValue.getCalculateStringValue(finalStr);
//									System.out.println("Calculated Value is : "+calulatedVal);
									calcMap.put(element, calulatedVal+"");
//									System.out.println(calcMap.size());
*/								}else {
									if(str.contains("MEAN")) {
										String[] meanArr = str.split("MEAN");
										List<String> meanList = new ArrayList<>();
										double meanVal = 0;
										for(int i=0; i<meanArr.length; i++) {
											if(!meanArr[i].equals("")) {
												String st = meanArr[i];
												st = st.replaceAll("\\(", "");
												st = st.replaceAll("\\)", "");
												st = st.replaceAll("[-+^*/]", " ");
												System.out.println("final String of "+i+" is :"+st);
												if(!st.equals("")) {
													meanList.add(st.trim());
													String[] tempArr = st.split("\\,");
													double[] inputArr = new double[tempArr.length];
													String rst = "";
													if(tempArr.length > 0) {
														for(int j=0; j<tempArr.length; j++) {
															String eleValue = dataMap.get(tempArr[j].trim());
															double val = Double.parseDouble(eleValue);
															inputArr[j] = val;
															if(rst.equals(""))
																rst = tempArr[j].trim();
															else rst = rst+","+tempArr[j].trim();
														}
													}
													MeanCalculation meanCalc = new MeanCalculation();
													double finalVal = meanCalc.calculateMeanValue(inputArr);
//													System.out.println("Final Mean Val is : "+finalVal);
													formula = formula.replaceAll(rst, finalVal+"");
													if(meanList.size() == 1)
														meanVal = finalVal;
												}
											}
										}
//										System.out.println("Final String is : "+str);
//										str = str.replaceAll("MEAN", "");
										/*if(meanList.size() > 1) {
											double val = MeanAndSDStringCalculation.getCalculateStringValue(str);
											calcMap.put(element, String.format("%.2f", val));
//											System.out.println(calcMap.size());
										}else {
//											finalNo = (int)(Math.round(meanVal));
//											System.out.println("finalNo is : "+String.format("%.2f", meanVal));
											calcMap.put(element, String.format("%.2f", meanVal));
//											System.out.println(calcMap.size());
										}*/
										
									}
									if(str.contains("SD")){
										String[] sdArr = str.split("SD");
										List<String> sdList = new ArrayList<>();
										double sdVal = 0;
										for(int i=0; i<sdArr.length; i++) {
											if(!sdArr[i].equals("")) {
												String st = sdArr[i];
												st = st.replaceAll("\\(", "");
												st = st.replaceAll("\\)", "");
												st = st.replaceAll("[-+^*/]", " ");
//												System.out.println("final String of "+i+" is :"+st);
												if(!st.equals("")) {
													sdList.add(st);
													String[] tempArr = st.split("\\,");
													double[] inputArr = new double[tempArr.length];
													String rst = "";
													if(tempArr.length > 0) {
														for(int j=0; j<tempArr.length; j++) {
															String eleValue = dataMap.get(tempArr[j].trim());
															double val = Double.parseDouble(eleValue);
															inputArr[j] = val;
															if(rst.equals(""))
																rst = tempArr[j].trim();
															else rst = rst+","+tempArr[j].trim();
														}
													}
													StandaredDeviation sdPojo= new StandaredDeviation();
													double finalVal = sdPojo.caluclateStanderedDeviation(inputArr);
//													System.out.println("Final Mean Val is : "+finalVal);
//													str = str.replaceAll(rst, finalVal+"");
													formula = formula.replaceAll(rst, finalVal+"");
													if(sdList.size() == 1)
														sdVal = finalVal;
												}
											}
										}
//										System.out.println("Final String is : "+str);
										/*str = str.replaceAll("SD", "");
										if(sdList.size() > 1) {
											double val = MeanAndSDStringCalculation.getCalculateStringValue(str);
											calcMap.put(element, String.format("%.2f", val));
//											System.out.println(calcMap.size());
										}else {
//											finalNo = (int)(Math.round(meanVal));
//											System.out.println("finalNo is : "+String.format("%.2f", meanVal));
											calcMap.put(element, String.format("%.2f", sdVal));
//											System.out.println(calcMap.size());
										}*/
									}
								}
							}
							
						}
					}
					if(formula.contains("MEAN")) {
						formula = formula.replaceAll("MEAN", "");
						formula = formula.replaceAll("CURRENT", "");
					}
					if(formula.contains("SD")) {
						formula = formula.replaceAll("SD", "");
						formula = formula.replaceAll("CURRENT", "");
					}
					if(formula.contains("+") || formula.contains("-") || formula.contains("*") || formula.contains("/")) {
						double calulatedVal = MeanAndSDStringCalculation.getCalculateStringValue(formula);
						calcMap.put(element, String.format("%.2f", calulatedVal));
					}else {
						formula = formula.replaceAll("\\(", "");
						formula = formula.replaceAll("\\)", "");
						formula = formula.replaceAll("[-+^*/]", " ");
						calcMap.put(element, String.format("%.2f", formula));
					}
				}
			}
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return calcMap;
	}

}
