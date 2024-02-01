package com.springmvc.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.springmvc.model.StagoData;

public class StagoDataDto implements Serializable{
	List<StagoData> list = new ArrayList<>();

	public List<StagoData> getList() {
		return list;
	}

	public void setList(List<StagoData> list) {
		this.list = list;
	}
	
}
