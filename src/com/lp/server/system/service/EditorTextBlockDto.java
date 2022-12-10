package com.lp.server.system.service;

import com.lp.server.util.EditorBlockIId;

public class EditorTextBlockDto extends EditorBaseBlockDto {

	private static final long serialVersionUID = 1L;

	private String text;

	public EditorTextBlockDto(int row, int column, String text) {
		super(row, column);
		this.text = text;
	}

	public EditorTextBlockDto() {
	}

	public String getText() {
		return text;
	}

	public EditorTextBlockDto setText(String text) {
		this.text = text;
		return this;
	}

}
