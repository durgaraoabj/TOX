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
@Table(name="crf_group_item_std")
public class CRFGroupItemStd extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CRFGroupItemStd_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	private CrfMetaDataStd crfId;
	@ManyToOne
	private CRFSectionsStd section;
	private Long groupId;
	@Column(columnDefinition="TEXT")
	private String groupName = "";
	@Column(columnDefinition="TEXT")
	private String groupDesc = "";
	@OneToMany
	@JoinColumn(name="group_id")
	private List<CrfItemsStd> itemList;
	private int maxColumns;
	@Column(name = "is_active")	
	private boolean active = true;//status
	private Integer displayRow = 0;
	private Integer maxRow = 0;
	@Column(columnDefinition="TEXT")
	private String dataSetTechName="";
	@Column(columnDefinition="TEXT")
	private String dataSetTechValue="";
	@OneToOne
	@JoinColumn(name="statusUpdate")
	private DTNAME statusUpdate;
	
	private String label_values ="";
	private int fontSize = 0;
	private String fontStyle = "";
	private String textStyle = "";
	private int noOfRowsDataContains;
	private char changeStatsu = 'C';// N - newFieldGoup, I - InActive(updated), A-Active(updated) and C - noChange
	
	private String groupValueSizes="";
	
	private int tempId;
	
	
	public void setId(Long id) {
		this.id = id;
	}
	
	

	public Long getGroupId() {
		return groupId;
	}



	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}



	public Long getId() {
		return id;
	}



	public List<CrfItemsStd> getItemList() {
		return itemList;
	}

	public void setItemList(List<CrfItemsStd> itemList) {
		this.itemList = itemList;
	}
	
	public CrfMetaDataStd getCrfId() {
		return crfId;
	}
	public void setCrfId(CrfMetaDataStd crfId) {
		this.crfId = crfId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupDesc() {
		return groupDesc;
	}
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}
	public Integer getDisplayRow() {
		return displayRow;
	}
	public void setDisplayRow(Integer displayRow) {
		this.displayRow = displayRow;
	}
	public Integer getMaxRow() {
		return maxRow;
	}
	public void setMaxRow(Integer maxRow) {
		this.maxRow = maxRow;
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

	
	@Transient
	public int getNoOfRowsDataContains() {
		return noOfRowsDataContains;
	}
	public void setNoOfRowsDataContains(int noOfRowsDataContains) {
		this.noOfRowsDataContains = noOfRowsDataContains;
	}
	
	public char getChangeStatsu() {
		return changeStatsu;
	}
	public void setChangeStatsu(char changeStatsu) {
		this.changeStatsu = changeStatsu;
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
	public String getGroupValueSizes() {
		return groupValueSizes;
	}
	public void setGroupValueSizes(String groupValueSizes) {
		this.groupValueSizes = groupValueSizes;
	}
	@Transient
	public int getTempId() {
		return tempId;
	}
	public void setTempId(int tempId) {
		this.tempId = tempId;
	}
	public CRFSectionsStd getSection() {
		return section;
	}
	public void setSection(CRFSectionsStd section) {
		this.section = section;
	}
	
}
