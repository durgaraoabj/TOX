package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="template_file_audit_trail_log")
public class TemplateFileAuditTrailLog extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="TemplateFileAuditTrailLog_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="slotd")
	private StudyLevelObservationTemplateData slotd;
	@Column(name="position")
	private String position;
	@Column(name="sheetName")
	private String sheetName;
	@Column(name="col")
	private String col;
	@Column(name="row")
	private String row;
	@Column(name="old_value")
	private String old_value;
	@Column(name="new_value")
	private String new_value;
	@Column(name="modified_date")
	private Date modified_date;
	@Column(name="modified_by")
	private String modifiedBy;
	@ManyToOne
	@JoinColumn(name="tfaudit")
	private TemplateFileAuditTrail tfaudit;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StudyLevelObservationTemplateData getSlotd() {
		return slotd;
	}
	public void setSlotd(StudyLevelObservationTemplateData slotd) {
		this.slotd = slotd;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getCol() {
		return col;
	}
	public void setCol(String col) {
		this.col = col;
	}
	public String getRow() {
		return row;
	}
	public void setRow(String row) {
		this.row = row;
	}
	public String getOld_value() {
		return old_value;
	}
	public void setOld_value(String old_value) {
		this.old_value = old_value;
	}
	public String getNew_value() {
		return new_value;
	}
	public void setNew_value(String new_value) {
		this.new_value = new_value;
	}
	public Date getModified_date() {
		return modified_date;
	}
	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public TemplateFileAuditTrail getTfaudit() {
		return tfaudit;
	}
	public void setTfaudit(TemplateFileAuditTrail tfaudit) {
		this.tfaudit = tfaudit;
	}
	
	

	

}
