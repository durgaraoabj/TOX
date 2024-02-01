package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "AUDIT_TRIAL")
public class AuditTrail implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="AuditTrail_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	@Column(name = "AUDIT_ID")
	private long auditId;
	@Column(name = "TRANSACTION_ID")
	private Long transactionId;
	@Column(name = "USER_NAME")
	private String userName;
	@Column(name = "ACTION")
	private String action;
	@Column(name = "TABLE_ID")
	private String tableId;
	@Column(name = "TABLE_CLASS")
	private String tableClass;
	@Column(name = "FIELD_NAME")
	private String fieldName;
	@Column(name = "OLD_VALUE", length = 1000)
	private String oldValue;
	@Column(name = "NEW_VALUE", length = 1000)
	private String newValue;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	@Column(name = "REASON")
	private String reason;
	@Column(name = "ROW_ID")
	private Long rowId;

	public AuditTrail() {
	}

	public AuditTrail(String userName, String action, Long rowId, String tableClass, String fieldName, String oldValue, String newValue) {
		this.userName = userName;
		this.action = action;
		this.rowId = rowId;
		this.tableClass = tableClass;
		this.fieldName = fieldName;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.setCreatedTime(new Date());
	}

	public long getAuditId() {
		return auditId;
	}

	public void setAuditId(long auditId) {
		this.auditId = auditId;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getTableClass() {
		return tableClass;
	}

	public void setTableClass(String tableClass) {
		this.tableClass = tableClass;
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

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Long getRowId() {
		return rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}

	private String loginUser;

	@Transient
	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	@Override
	public String toString() {
		return "AuditTrail [auditId=" + auditId + ", transactionId=" + transactionId + ", userName=" + userName
				+ ", action=" + action + ", tableId=" + tableId + ", tableClass=" + tableClass + ", fieldName="
				+ fieldName + ", oldValue=" + oldValue + ", newValue=" + newValue + ", createdTime=" + createdTime
				+ ", reason=" + reason + ", rowId=" + rowId + ", loginUser=" + loginUser + "]";
	}

}
