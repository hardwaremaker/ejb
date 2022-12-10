package com.lp.server.system.service;

import java.io.Serializable;

import com.lp.server.util.EditorBlockIId;

public class EditorBaseBlockDto implements Serializable{
	private static final long serialVersionUID = 1L;

	private EditorBlockIId id;
	private int row;
	private int column;

	private EditorContentDto contentDto;

	protected void setContentDto(EditorContentDto contentDto) {
		this.contentDto = contentDto;
	}

	public EditorBlockIId getId() {
		return id;
	}

	public EditorBaseBlockDto setId(EditorBlockIId id) {
		this.id = id;
		return this;
	}

	public int getRow() {
		return row;
	}

	public EditorBaseBlockDto(int row, int column) {
		this();
		this.row = row;
		this.column = column;
	}
	
	public EditorBaseBlockDto() {
		id = new EditorBlockIId(null);
	}

	public EditorBaseBlockDto setRow(int row) {
		this.row = row;
		return this;
	}

	public int getColumn() {
		return column;
	}

	public EditorBaseBlockDto setColumn(int column) {
		this.column = column;
		return this;
	}

	public EditorContentDto getContentDto() {
		return contentDto;
	}
}
