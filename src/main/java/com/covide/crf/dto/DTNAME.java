package com.covide.crf.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name="DTNAME")
public class DTNAME implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long dtNameid;
//	private EmployeeDetails emp;
	private Date date;
	private String rason;
	private String comment;
	
	public DTNAME() {
		super();
	}
	
	


	public Long getDtNameid() {
		return dtNameid;
	}

	public void setDtNameid(Long dtNameid) {
		this.dtNameid = dtNameid;
	}

//	@ManyToOne
//	@JoinColumn(name = "emp_id")
//	public EmployeeDetails getEmp() {
//		return emp;
//	}
//
//	public void setEmp(EmployeeDetails emp) {
//		this.emp = emp;
//	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRason() {
		return rason;
	}

	public void setRason(String rason) {
		this.rason = rason;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
