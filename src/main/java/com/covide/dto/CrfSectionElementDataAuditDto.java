package com.covide.dto;

import java.io.Serializable;
import java.util.Date;

import com.springmvc.model.AccessionAnimalsDataEntryDetails;
import com.springmvc.model.LoginUsers;

public class CrfSectionElementDataAuditDto implements Serializable{
	private static final long serialVersionUID = 538893270828472076L;
	private Long id;
	private String elementOldValue = "";
	private String elementValue = "";
	private Long loginUsersId;
	private String loginUserName = "";
	private String createdOn = "";
	private String userComment = "";
	private String dataEntryType = "";
	private boolean allowToSelect = true;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getElementOldValue() {
		return elementOldValue;
	}
	public void setElementOldValue(String elementOldValue) {
		this.elementOldValue = elementOldValue;
	}
	public String getElementValue() {
		return elementValue;
	}
	public void setElementValue(String elementValue) {
		this.elementValue = elementValue;
	}
	public Long getLoginUsersId() {
		return loginUsersId;
	}
	public void setLoginUsersId(Long loginUsersId) {
		this.loginUsersId = loginUsersId;
	}
	public String getLoginUserName() {
		return loginUserName;
	}
	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}
	
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getUserComment() {
		return userComment;
	}
	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}
	public String getDataEntryType() {
		return dataEntryType;
	}
	public void setDataEntryType(String dataEntryType) {
		this.dataEntryType = dataEntryType;
	}
	public boolean isAllowToSelect() {
		return allowToSelect;
	}
	public void setAllowToSelect(boolean allowToSelect) {
		this.allowToSelect = allowToSelect;
	}
	
	
}
