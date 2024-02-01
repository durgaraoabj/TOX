package com.springmvc.util;

import java.util.List;

public class ExprementalData {
	private int max = 0;
	private Long groupId;
	private String groupName;
	private Long subgroupId;
	private String subGroupName;
	private Long obserVationId;
	private String obserVationName;
	private String gender;
	private String from;
	private String to;
	private String dayOrWeek;
	private List<String> days;
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Long getSubgroupId() {
		return subgroupId;
	}
	public void setSubgroupId(Long subgroupId) {
		this.subgroupId = subgroupId;
	}
	public String getSubGroupName() {
		return subGroupName;
	}
	public void setSubGroupName(String subGroupName) {
		this.subGroupName = subGroupName;
	}
	public Long getObserVationId() {
		return obserVationId;
	}
	public void setObserVationId(Long obserVationId) {
		this.obserVationId = obserVationId;
	}
	
	public String getObserVationName() {
		return obserVationName;
	}
	public void setObserVationName(String obserVationName) {
		this.obserVationName = obserVationName;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getDayOrWeek() {
		return dayOrWeek;
	}
	public void setDayOrWeek(String dayOrWeek) {
		this.dayOrWeek = dayOrWeek;
	}
	public List<String> getDays() {
		return days;
	}
	public void setDays(List<String> days) {
		this.days = days;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	
	
}
