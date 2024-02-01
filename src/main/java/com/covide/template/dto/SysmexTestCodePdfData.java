package com.covide.template.dto;

import com.springmvc.model.SysmexTestCodeData;

public class SysmexTestCodePdfData {

	private Integer order;
	private SysmexTestCodeData absolutetc;
	private SysmexTestCodeData persentagetc;
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public SysmexTestCodeData getAbsolutetc() {
		return absolutetc;
	}
	public void setAbsolutetc(SysmexTestCodeData absolutetc) {
		this.absolutetc = absolutetc;
	}
	public SysmexTestCodeData getPersentagetc() {
		return persentagetc;
	}
	public void setPersentagetc(SysmexTestCodeData persentagetc) {
		this.persentagetc = persentagetc;
	}
	
}
