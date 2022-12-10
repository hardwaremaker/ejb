package com.lp.service.edifact;

public class Separators {
	private Character composite;
	private Character data;
	private Character decimal;
	private Character escape;
	private Character segment;
	
	public Character getComposite() {
		return composite;
	}
	public void setComposite(Character composite) {
		this.composite = composite;
	}
	public Character getData() {
		return data;
	}
	public void setData(Character data) {
		this.data = data;
	}
	public Character getDecimal() {
		return decimal;
	}
	public void setDecimal(Character decimal) {
		this.decimal = decimal;
	}
	public Character getEscape() {
		return escape;
	}
	public void setEscape(Character escape) {
		this.escape = escape;
	}
	public Character getSegment() {
		return segment;
	}
	public void setSegment(Character segment) {
		this.segment = segment;
	}
}
