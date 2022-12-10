package com.lp.server.system.service;

import java.io.Serializable;

public class ImportRueckgabeZeile  implements Serializable {
	public ImportRueckgabeZeile(String text, int zeile) {
		super();
		this.text = text;
		this.zeile = zeile;
	}
	private String text;
	private int zeile;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getZeile() {
		return zeile;
	}
	public void setZeile(int zeile) {
		this.zeile = zeile;
	}
}
