package com.covide.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.covide.crf.dto.CrfSectionElementInstrumentValue;

public class CrfSectionElementDto implements Serializable{
	private static final long serialVersionUID = 4612415335387710856L;
	private Long crfSectionElementId;
	private String name = "";
	private String leftDesc = "";
	private String rigtDesc = "";
	private String middeDesc = "";
	private String totalDesc = ""; //  left+middle+right
	private String topDesc = "";
	private String bottemDesc = "";
	private String type= "";// text/textArea/radio/select/checkBox/date/dateAndTime/non/selectTable/instrumentData
	private String dataType = "String"; // Number/String
	private String responceType = "";
	private String values = "";
	private String pattren = "";
	private boolean required;
	private String typeOfTime = "";
	private List<String> elementValues; 
	private String formula;
	private List<CrfSectionElementInstrumentValueDto> instumentDat = new ArrayList<>();
	
	public List<CrfSectionElementInstrumentValueDto> getInstumentDat() {
		return instumentDat;
	}
	public void setInstumentDat(List<CrfSectionElementInstrumentValueDto> instumentDat) {
		this.instumentDat = instumentDat;
	}
	public Long getCrfSectionElementId() {
		return crfSectionElementId;
	}
	public void setCrfSectionElementId(Long crfSectionElementId) {
		this.crfSectionElementId = crfSectionElementId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLeftDesc() {
		return leftDesc;
	}
	public void setLeftDesc(String leftDesc) {
		this.leftDesc = leftDesc;
	}
	public String getRigtDesc() {
		return rigtDesc;
	}
	public void setRigtDesc(String rigtDesc) {
		this.rigtDesc = rigtDesc;
	}
	public String getMiddeDesc() {
		return middeDesc;
	}
	public void setMiddeDesc(String middeDesc) {
		this.middeDesc = middeDesc;
	}
	public String getTotalDesc() {
		return totalDesc;
	}
	public void setTotalDesc(String totalDesc) {
		this.totalDesc = totalDesc;
	}
	public String getTopDesc() {
		return topDesc;
	}
	public void setTopDesc(String topDesc) {
		this.topDesc = topDesc;
	}
	public String getBottemDesc() {
		return bottemDesc;
	}
	public void setBottemDesc(String bottemDesc) {
		this.bottemDesc = bottemDesc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getResponceType() {
		return responceType;
	}
	public void setResponceType(String responceType) {
		this.responceType = responceType;
	}
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}
	public String getPattren() {
		return pattren;
	}
	public void setPattren(String pattren) {
		this.pattren = pattren;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getTypeOfTime() {
		return typeOfTime;
	}
	public void setTypeOfTime(String typeOfTime) {
		this.typeOfTime = typeOfTime;
	}
	public List<String> getElementValues() {
		return elementValues;
	}
	public void setElementValues(List<String> elementValues) {
		this.elementValues = elementValues;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	
	
}
