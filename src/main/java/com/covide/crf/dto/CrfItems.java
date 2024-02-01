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
@Table(name="crf_item")
public class CrfItems extends CommonMaster implements Serializable,Comparable<CrfItems>{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfItems_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="crf_id")
	private CrfMetaData crfId;
	@ManyToOne
	@JoinColumn(name="section_id")
	private CRFSections section;
	@ManyToOne
	@JoinColumn(name="group_id")
	private CRFGroupItem group;
	@Column(columnDefinition="TEXT")
	private String itemName = "";
	@Column(columnDefinition="TEXT")
	private String itemDesc = "";
	@Column(columnDefinition="TEXT")
	private String itemDescRight = "";
	private String itemType = "";
	private String responseType = "";
	private String responseValue = "";
	private Integer rowNumber = 0;
	private Integer columnNo = 0;
	private Integer validation = 0;
	private String pattern = "";
	@OneToMany
	@JoinColumn(name="crf_item")
	private List<CRFItemValues> itemResponceValues;
	@Column(name = "is_active")	
	private boolean active = true;//status
	
	private String display = "horizantal"; //horizantal or vertical
	private Integer count = 0;
	@Column(columnDefinition="TEXT")
	private String dataSetTechName="";
	@Column(columnDefinition="TEXT")
	private String dataSetTechValue="";
	private String type = "";
	private String ieTestCode = "";
	@Column(columnDefinition="TEXT")
	private String header = "";
	@Column(columnDefinition="TEXT")
	private String subheader = "";
	private String isSelected = "";
	private String item_oid = "";
	@OneToOne
	@JoinColumn(name="statusUpdate")
	private DTNAME statusUpdate;
	
	private char changeStatsu = 'C';// N - newField, I - InActive(updated), A-Active(updated) and C - noChange
	@Transient
	private String itemNameWithGroupIndex;
	@Transient
	private String description;
	@Transient
	private String gropItemId;
	@Column(columnDefinition="TEXT")
	private String gropItemName;
	@Transient
	private String gropItemDesc;
	
	private String textStyle="";
	private String fontStyle="";
	private int fontSize=0;
	
	private String underLine="";
	@Column(columnDefinition="TEXT")
	private int printDesc=0;
	private int printContent = 0;
	
	private float descCellWidth = 0.0f;
	private char valueWithImage = 'N';
	private String valueCellWidth ="0";
	private float cellHeight=0;
	private String checkAlign="Right";
	@Transient
	private int tempId;
	

	public CrfMetaData getCrfId() {
		return crfId;
	}
	public void setCrfId(CrfMetaData crfId) {
		this.crfId = crfId;
	}
	public CRFSections getSection() {
		return section;
	}
	public void setSection(CRFSections section) {
		this.section = section;
	}
	public CRFGroupItem getGroup() {
		return group;
	}
	public void setGroup(CRFGroupItem group) {
		this.group = group;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
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
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getItemDescRight() {
		return itemDescRight;
	}
	public void setItemDescRight(String itemDescRight) {
		this.itemDescRight = itemDescRight;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	public String getResponseValue() {
		return responseValue;
	}
	public void setResponseValue(String responseValue) {
		this.responseValue = responseValue;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public Integer getValidation() {
		return validation;
	}
	public void setValidation(Integer validation) {
		this.validation = validation;
	}
	
	public String getIeTestCode() {
		return ieTestCode;
	}
	public void setIeTestCode(String ieTestCode) {
		this.ieTestCode = ieTestCode;
	}
	public Integer getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}
	public Integer getColumnNo() {
		return columnNo;
	}
	public void setColumnNo(Integer columnNo) {
		this.columnNo = columnNo;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getSubheader() {
		return subheader;
	}
	public void setSubheader(String subheader) {
		this.subheader = subheader;
	}
	public String getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}
	public String getItem_oid() {
		return item_oid;
	}
	public void setItem_oid(String item_oid) {
		this.item_oid = item_oid;
	}
		
	public List<CRFItemValues> getItemResponceValues() {
		return itemResponceValues;
	}
	public void setItemResponceValues(List<CRFItemValues> itemResponceValues) {
		this.itemResponceValues = itemResponceValues;
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
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public char getChangeStatsu() {
		return changeStatsu;
	}
	public void setChangeStatsu(char changeStatsu) {
		this.changeStatsu = changeStatsu;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getItemNameWithGroupIndex() {
		return itemNameWithGroupIndex;
	}
	public void setItemNameWithGroupIndex(String itemNameWithGroupIndex) {
		this.itemNameWithGroupIndex = itemNameWithGroupIndex;
	}
	
	public String getGropItemId() {
		return gropItemId;
	}
	public void setGropItemId(String gropItemId) {
		this.gropItemId = gropItemId;
	}
	
	public String getGropItemName() {
		return gropItemName;
	}
	public void setGropItemName(String gropItemName) {
		this.gropItemName = gropItemName;
	}
	public String getGropItemDesc() {
		return gropItemDesc;
	}
	public void setGropItemDesc(String gropItemDesc) {
		this.gropItemDesc = gropItemDesc;
	}

	
	@Override
	public int compareTo(CrfItems o) {
		// TODO Auto-generated method stub
		return (int) (this.getCrfId().getId()-o.getCrfId().getId());
	}
	public String getTextStyle() {
		return textStyle;
	}
	public void setTextStyle(String textStyle) {
		this.textStyle = textStyle;
	}
	public String getFontStyle() {
		return fontStyle;
	}
	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public String getUnderLine() {
		return underLine;
	}
	public void setUnderLine(String underLine) {
		this.underLine = underLine;
	}
	public int getPrintDesc() {
		return printDesc;
	}
	public void setPrintDesc(int printDesc) {
		this.printDesc = printDesc;
	}
	public int getPrintContent() {
		return printContent;
	}
	public void setPrintContent(int printContent) {
		this.printContent = printContent;
	}
	public float getDescCellWidth() {
		return descCellWidth;
	}
	public void setDescCellWidth(float descCellWidth) {
		this.descCellWidth = descCellWidth;
	}
	public char getValueWithImage() {
		return valueWithImage;
	}
	public void setValueWithImage(char valueWithImage) {
		this.valueWithImage = valueWithImage;
	}
	public String getValueCellWidth() {
		return valueCellWidth;
	}
	public void setValueCellWidth(String valueCellWidth) {
		this.valueCellWidth = valueCellWidth;
	}
	public float getCellHeight() {
		return cellHeight;
	}
	public void setCellHeight(float cellHeight) {
		this.cellHeight = cellHeight;
	}
	public String getCheckAlign() {
		return checkAlign;
	}
	public void setCheckAlign(String checkAlign) {
		this.checkAlign = checkAlign;
	}
	public int getTempId() {
		return tempId;
	}
	public void setTempId(int tempId) {
		this.tempId = tempId;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	
}
