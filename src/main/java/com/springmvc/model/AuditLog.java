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
@Table(name="audit_log")
public class AuditLog implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="audit_log_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private long id;
	@ManyToOne
	@JoinColumn(name="activity_log_id")
	private ActivityLog activityLogId;
	@Column(name="field_name")
	private String fieldName;
	@Column(name="old_value")
	private String oldValue;
	@Column(name="new_value")
	private String newValue;
	@Column(name="table_name")
	private String tableName;
	@ManyToOne
	@JoinColumn(name="study_id")
	private StudyMaster studyId;
//	@ManyToOne
//	@JoinColumn(name="volunteer_id")
//	private StudyVolunteerReporting volunteerId;
	@ManyToOne
	@JoinColumn(name="animal_id")
	private StudyAnimal animal;
	@Column(name="comments")
	private String comments;
	@Column(name="field_type")
	private String fieldType; // dynamic,static
	@Column(name="action")
	private String action;
	@Column(name="databasefieldname")
	private String databasefieldname;
	@Column(name="created_by")
	private String createdBy;
	@Column(name="created_on")
	private Date createdOn;
	@Column(name="updated_by")
	private String updatedBy;
	@Column(name="updated_on")
	private Date updatedOn;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public ActivityLog getActivityLogId() {
		return activityLogId;
	}
	public void setActivityLogId(ActivityLog activityLogId) {
		this.activityLogId = activityLogId;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public StudyMaster getStudyId() {
		return studyId;
	}
	public void setStudyId(StudyMaster studyId) {
		this.studyId = studyId;
	}
	public StudyAnimal getAnimal() {
		return animal;
	}
	public void setAnimal(StudyAnimal animal) {
		this.animal = animal;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getDatabasefieldname() {
		return databasefieldname;
	}
	public void setDatabasefieldname(String databasefieldname) {
		this.databasefieldname = databasefieldname;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
}
