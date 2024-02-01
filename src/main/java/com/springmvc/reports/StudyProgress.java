package com.springmvc.reports;

public class StudyProgress {
	private String name;
	private int enrolled;
	private int totalSubjects;
	private int persentage;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
