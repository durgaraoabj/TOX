package com.covide.dto;

import java.io.Serializable;

public class StudyCreationDto implements Serializable{
	private Long id;
	private String studyId;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStudyId() {
		return studyId;
	}

	public void setStudyId(String studyId) {
		this.studyId = studyId;
	}
	
	
}
