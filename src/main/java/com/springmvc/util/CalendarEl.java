package com.springmvc.util;

import java.util.ArrayList;
import java.util.List;

public class CalendarEl {
	private String initialDate = "2022-04-04";
	private boolean editable = true;
	private boolean selectable = true;
	private boolean businessHours = true;
	private boolean dayMaxEvents = true;
	
	private List<CEvents> events = new ArrayList<>();
	public String getInitialDate() {
		return initialDate;
	}
	public void setInitialDate(String initialDate) {
		this.initialDate = initialDate;
	}
	
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public boolean isSelectable() {
		return selectable;
	}
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}
	public boolean isBusinessHours() {
		return businessHours;
	}
	public void setBusinessHours(boolean businessHours) {
		this.businessHours = businessHours;
	}
	public boolean isDayMaxEvents() {
		return dayMaxEvents;
	}
	public void setDayMaxEvents(boolean dayMaxEvents) {
		this.dayMaxEvents = dayMaxEvents;
	}
	public List<CEvents> getEvents() {
		return events;
	}
	public void setEvents(List<CEvents> events) {
		this.events = events;
	}
	
}
