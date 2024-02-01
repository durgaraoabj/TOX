package com.covide.dto;

import java.io.Serializable;
import java.util.List;

import com.covide.crf.dto.CrfSectionElement;

@SuppressWarnings("serial")
public class CrfFormulaDto implements Serializable {
	
	private List<String> formulsList;
	private List<CrfSectionElement> secList;
	
	
	public List<String> getFormulsList() {
		return formulsList;
	}
	public void setFormulsList(List<String> formulsList) {
		this.formulsList = formulsList;
	}
	public List<CrfSectionElement> getSecList() {
		return secList;
	}
	public void setSecList(List<CrfSectionElement> secList) {
		this.secList = secList;
	}
	
	
	
	

}
