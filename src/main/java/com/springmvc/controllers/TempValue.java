package com.springmvc.controllers;

public class TempValue {
	private String temp = "";

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public TempValue(String temp) {
		super();
		this.temp = temp;
	}

	public TempValue() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return temp;
	}

	
	
	
}
