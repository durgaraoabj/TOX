package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="INSTRUMENT_IP_ADDRESS")
public class InstrumentIpAddress extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="InstrumentIpAddress_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private String instrumentName;
	private String methodName = "";
	private String comPortNo;
	private String ipAddress;
	private int portNo;
	@ManyToOne
	@JoinColumn(name="activeStatusId")
	private StatusMaster activeStatus;
	private boolean configuredIp = true;
	private int orderNo = 0;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public String getInstrumentName() {
		return instrumentName;
	}
	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}
	public String getComPortNo() {
		return comPortNo;
	}
	public void setComPortNo(String comPortNo) {
		this.comPortNo = comPortNo;
	}
	public StatusMaster getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(StatusMaster activeStatus) {
		this.activeStatus = activeStatus;
	}
	public boolean isConfiguredIp() {
		return configuredIp;
	}
	public void setConfiguredIp(boolean configuredIp) {
		this.configuredIp = configuredIp;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getPortNo() {
		return portNo;
	}
	public void setPortNo(int portNo) {
		this.portNo = portNo;
	}
	
	public InstrumentIpAddress() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InstrumentIpAddress(String ipAddress, int portNo, StatusMaster activeStatus) {
		super();
		this.ipAddress = ipAddress;
		this.portNo = portNo;
		this.activeStatus = activeStatus;
	}
	public InstrumentIpAddress(String instrumentName, String methodName, String comPortNo, String ipAddress, int portNo,
			StatusMaster activeStatus, boolean configuredIp, int orderNo) {
		super();
		this.instrumentName = instrumentName;
		this.methodName = methodName;
		this.comPortNo = comPortNo;
		this.ipAddress = ipAddress;
		this.portNo = portNo;
		this.activeStatus = activeStatus;
		this.configuredIp = configuredIp;
		this.orderNo = orderNo;
	}
	
	
}
