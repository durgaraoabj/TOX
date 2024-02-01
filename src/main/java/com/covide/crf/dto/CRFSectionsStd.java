package com.covide.crf.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.springmvc.model.CommonMaster;

@Component
@Entity
@Table(name="crf_sections_std")
public class CRFSectionsStd extends CommonMaster implements Serializable,Comparable<CRFSectionsStd>{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CRFSectionsStd_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne(cascade=CascadeType.ALL)
	private CrfMetaDataStd crfId;
	@ManyToOne
	@JoinColumn(name = "group_id")
	private CRFGroupItemStd group;
	private Long crfSectionId;
	@Column(columnDefinition="TEXT")
	private String sectionName = "";
	@Column(columnDefinition="TEXT")
	private String sectionDesc = "";
	@Column(columnDefinition="TEXT")
	private String hedding = "";
	@Column(columnDefinition="TEXT")
	private String subHedding = "";
	private int maxRows;
	private int maxColumns;
	private String userRole = "admin";
	@Column(name = "is_active")	
	private boolean active = true;//status
	private String gender = "Male";
	private int orderNo;
	private String containsGroup = "no"; // yes/no
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="section_id")	
	private List<CrfItemsStd> itemList;
	@Transient
	private List<String> sectioRoles;
	
	private String userRoleUpdateReason = "";
	private String dataSetTechName="";
	private String dataSetTechValue="";
	@OneToOne
	@JoinColumn(name = "statusUpdate")
	private DTNAME statusUpdate;
	
	
	private int label_id = 0;
	private String label_values="";
	private String fontStyle="";
	private String textStyle="";
	private int fontSize = 0;
	private String SignatureField = "No";
	private String SingleColumn = "No";
	@Transient
	private String currentRole="N/A";
	@Transient
	private boolean sectionVisibleStatus = true;
	private char changeStatsu = 'C';// N - newFieldSectionl, I - InActive(updated), A-Active(updated) and C - noChange
	
	private String tableLines = "YES";
	private String ColumnValues = "";
	private String ColumnSizes ="";
	@Transient
	private int tempId;

	
	
	
	public Long getCrfSectionId() {
		return crfSectionId;
	}
	public void setCrfSectionId(Long crfSectionId) {
		this.crfSectionId = crfSectionId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public CRFGroupItemStd getGroup() {
		return group;
	}
	public void setGroup(CRFGroupItemStd group) {
		this.group = group;
	}
	public CrfMetaDataStd getCrfId() {
		return crfId;
	}
	public void setCrfId(CrfMetaDataStd crfId) {
		this.crfId = crfId;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	public String getUserRoleUpdateReason() {
		return userRoleUpdateReason;
	}
	public void setUserRoleUpdateReason(String userRoleUpdateReason) {
		this.userRoleUpdateReason = userRoleUpdateReason;
	}
	public String getSectionDesc() {
		return sectionDesc;
	}
	public void setSectionDesc(String sectionDesc) {
		this.sectionDesc = sectionDesc;
	}
	
	public String getDataSetTechName() {
		return dataSetTechName;
	}
	public void setDataSetTechName(String dataSetTechName) {
		this.dataSetTechName = dataSetTechName;
	}
	public String getDataSetTechValue() {
		return dataSetTechValue;
	}
	public void setDataSetTechValue(String dataSetTechValue) {
		this.dataSetTechValue = dataSetTechValue;
	}
	
	
	public List<CrfItemsStd> getItemList() {
		return itemList;
	}
	public void setItemList(List<CrfItemsStd> itemList) {
		this.itemList = itemList;
	}
	public List<String> getSectioRoles() {
		return sectioRoles;
	}
	public void setSectioRoles(List<String> sectioRoles) {
		this.sectioRoles = sectioRoles;
	}
	public int getMaxRows() {
		return maxRows;
	}
	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}
	public int getMaxColumns() {
		return maxColumns;
	}
	public void setMaxColumns(int maxColumns) {
		this.maxColumns = maxColumns;
	}
	
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public DTNAME getStatusUpdate() {
		return statusUpdate;
	}
	public void setStatusUpdate(DTNAME statusUpdate) {
		this.statusUpdate = statusUpdate;
	}
	
	
	public char getChangeStatsu() {
		return changeStatsu;
	}
	public void setChangeStatsu(char changeStatsu) {
		this.changeStatsu = changeStatsu;
	}
	
	public int getLabel_id() {
		return label_id;
	}
	public void setLabel_id(int label_id) {
		this.label_id = label_id;
	}
	public String getLabel_values() {
		return label_values;
	}
	public void setLabel_values(String label_values) {
		this.label_values = label_values;
	}
	public String getFontStyle() {
		return fontStyle;
	}
	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}
	public String getTextStyle() {
		return textStyle;
	}
	public void setTextStyle(String textStyle) {
		this.textStyle = textStyle;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public String getSignatureField() {
		return SignatureField;
	}
	public void setSignatureField(String signatureField) {
		SignatureField = signatureField;
	}
	public String getSingleColumn() {
		return SingleColumn;
	}
	public void setSingleColumn(String singleColumn) {
		SingleColumn = singleColumn;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getCurrentRole() {
		return currentRole;
	}
	public void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}
	
	public boolean isSectionVisibleStatus() {
		return sectionVisibleStatus;
	}
	public void setSectionVisibleStatus(boolean sectionVisibleStatus) {
		this.sectionVisibleStatus = sectionVisibleStatus;
	}
	@Override
	public int compareTo(CRFSectionsStd o) {
		// TODO Auto-generated method stub
		if(this.getOrderNo() > o.getOrderNo())
			return 1;
		else if(this.getOrderNo() < o.getOrderNo())
			return -1;
		else  return 0;
	}
	public String getTableLines() {
		return tableLines;
	}
	public void setTableLines(String tableLines) {
		this.tableLines = tableLines;
	}
	public String getColumnValues() {
		return ColumnValues;
	}
	public void setColumnValues(String columnValues) {
		ColumnValues = columnValues;
	}
	public String getColumnSizes() {
		return ColumnSizes;
	}
	public void setColumnSizes(String columnSizes) {
		ColumnSizes = columnSizes;
	}
	public int getTempId() {
		return tempId;
	}
	public void setTempId(int tempId) {
		this.tempId = tempId;
	}
	public String getContainsGroup() {
		return containsGroup;
	}
	public void setContainsGroup(String containsGroup) {
		this.containsGroup = containsGroup;
	}
	public String getHedding() {
		return hedding;
	}
	public void setHedding(String hedding) {
		this.hedding = hedding;
	}
	public String getSubHedding() {
		return subHedding;
	}
	public void setSubHedding(String subHedding) {
		this.subHedding = subHedding;
	}
	
}
