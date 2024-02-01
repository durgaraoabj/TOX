package com.springmvc.reports;

public class SubjectEnrollmentForStudy {
	
	private String studyNo;
	private int enrolled;
	private int totalSubjects;
	private int persentage;
	public String getStudyNo() {
		return studyNo;
	}
	public void setStudyNo(String studyNo) {
		this.studyNo = studyNo;
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
	
}
