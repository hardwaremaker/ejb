package com.lp.server.system.service;

import java.io.Serializable;

public class ImportProgressDto implements Serializable {

	private static final long serialVersionUID = 7707245463964007427L;

	private Integer maximum;
	private Integer current;
	
	public ImportProgressDto() {
	}

	public ImportProgressDto(Integer maximum, Integer current) {
		setMaximum(maximum);
		setCurrent(current);
	}

	public Integer getMaximum() {
		return maximum;
	}

	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}

	public Integer getCurrent() {
		return current;
	}

	public void setCurrent(Integer current) {
		this.current = current;
	}
	
	public Integer getProgress() {
		if (getMaximum() == null || getMaximum() == 0 || getCurrent() == null || getCurrent() == 0) {
			return 0;
		}
		return getCurrent() * 100 / getMaximum();
	}
}
