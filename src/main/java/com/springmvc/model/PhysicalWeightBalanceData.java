package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table ;
import javax.persistence.Transient;

@Entity
@Table(name="physical_weight_balance_data")
public class PhysicalWeightBalanceData extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="PhysicalWeightBalanceData_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private String dateAndTime = "";//Date & Time      : 31.12.2021 15:32:59
	private String batchNo = "";	//Batch No         : 
	private String nozzleNo = "";	//Nozzle No        : 
	private String grossWt = "";	//Gross Wt         : 0.404kg	
	private String tareWt = "";		//Tare Wt          : 0.404kg
	private String netWt = "";		//Net Wt           : 0.000kg
	private String status = "";		//Status           : MIN
	private String dataFrom = "";
	private String ipAddress = "";
	@Transient
	private int send_status = 0;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDateAndTime() {
		return dateAndTime;
	}
	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getNozzleNo() {
		return nozzleNo;
	}
	public void setNozzleNo(String nozzleNo) {
		this.nozzleNo = nozzleNo;
	}
	public String getGrossWt() {
		return grossWt;
	}
	public void setGrossWt(String grossWt) {
		this.grossWt = grossWt;
	}
	public String getTareWt() {
		return tareWt;
	}
	public void setTareWt(String tareWt) {
		this.tareWt = tareWt;
	}
	public String getNetWt() {
		return netWt;
	}
	public void setNetWt(String netWt) {
		this.netWt = netWt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDataFrom() {
		return dataFrom;
	}
	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public int getSend_status() {
		return send_status;
	}
	public void setSend_status(int send_status) {
		this.send_status = send_status;
	}
	@Override
	public String toString() {
		return "PhysicalWeightBalanceData [dateAndTime=" + dateAndTime + ", batchNo=" + batchNo + ", nozzleNo="
				+ nozzleNo + ", grossWt=" + grossWt + ", tareWt=" + tareWt + ", netWt=" + netWt + ", status=" + status
				+ "]";
	}
	
	
	
	
}
