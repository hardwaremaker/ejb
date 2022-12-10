package com.lp.server.personal.service;

import java.io.Serializable;


public class HvmarolleDto implements Serializable {
	private Integer iId;
	private Integer systemrolleIId;

	private Integer hvmarechtIId;
	

	private static final long serialVersionUID = 1L;


	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getSystemrolleIId() {
		return systemrolleIId;
	}

	public void setSystemrolleIId(Integer systemrolleIId) {
		this.systemrolleIId = systemrolleIId;
	}

	public Integer getHvmarechtIId() {
		return hvmarechtIId;
	}

	public void setHvmarechtIId(Integer hvmarechtIId) {
		this.hvmarechtIId = hvmarechtIId;
	}
}
