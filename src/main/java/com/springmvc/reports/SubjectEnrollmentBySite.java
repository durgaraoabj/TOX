package com.springmvc.reports;

public class SubjectEnrollmentBySite {

	private String siteName;
	private int enrolled;
	private int withDron;
	private int totalSubjects;
	private int persentage;
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public int getEnrolled() {
		return enrolled;
	}
	public void setEnrolled(int enrolled) {
		this.enrolled = enrolled;
	}
	public int getTotalSubjects() {
		return totalSubjects;
	}
	public void setTotalSubjects(int totalSubjects) {
		this.totalSubjects = totalSubjects;
	}
	public int getPersentage() {
		return persentage;
	}
	public void setPersentage(int persentage) {
		this.persentage = persentage;
	}
	public int getWithDron() {
		return withDron;
	}
	public void setWithDron(int withDron) {
		this.withDron = withDron;
	}
	
	
}
