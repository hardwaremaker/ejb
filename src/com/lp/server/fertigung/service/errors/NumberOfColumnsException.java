package com.lp.server.fertigung.service.errors;

public class NumberOfColumnsException extends ImportException {

	private static final long serialVersionUID = 4270576415180174795L;

	private Integer index;
	private Integer columns;
	
	public NumberOfColumnsException(Integer index, Integer columns) {
		super("Number of columns is " + columns + " but index is " + index);
		setIndex(index);
		setColumns(columns);
	}
	
	public NumberOfColumnsException() {
		super("Number of columns is null");
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public Integer getIndex() {
		return index;
	}
	
	public void setColumns(Integer columns) {
		this.columns = columns;
	}
	
	public Integer getColumns() {
		return columns;
	}
}
