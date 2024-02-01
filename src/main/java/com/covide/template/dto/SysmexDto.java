package com.covide.template.dto;

import java.io.Serializable;
import java.util.List;

import com.springmvc.model.ObservationInturmentConfiguration;
import com.springmvc.model.SysmexData;
import com.springmvc.model.SysmexRawData;
import com.springmvc.model.SysmexTestCodeData;

public class SysmexDto implements Serializable{
	private ObservationInturmentConfiguration observationInturmentConfiguration;
	private SysmexData sysData;
	private SysmexRawData sysmexRawData;
	private List<SysmexTestCodeData> sysmexTestCodeDatas;
	public SysmexData getSysData() {
		return sysData;
	}
	public void setSysData(SysmexData sysData) {
		this.sysData = sysData;
	}
	public SysmexRawData getSysmexRawData() {
		return sysmexRawData;
	}
	public void setSysmexRawData(SysmexRawData sysmexRawData) {
		this.sysmexRawData = sysmexRawData;
	}
	public List<SysmexTestCodeData> getSysmexTestCodeDatas() {
		return sysmexTestCodeDatas;
	}
	public void setSysmexTestCodeDatas(List<SysmexTestCodeData> sysmexTestCodeDatas) {
		this.sysmexTestCodeDatas = sysmexTestCodeDatas;
	}
	public ObservationInturmentConfiguration getObservationInturmentConfiguration() {
		return observationInturmentConfiguration;
	}
	public void setObservationInturmentConfiguration(ObservationInturmentConfiguration observationInturmentConfiguration) {
		this.observationInturmentConfiguration = observationInturmentConfiguration;
	}
	
	
}
