package com.lp.server.finanz.service;

import java.io.Serializable;

public class SepaCamtFormat052 implements ISepaCamtFormat, Serializable {

	private static final long serialVersionUID = -1548875691551133940L;
	
	private SepaCamtVersionEnum version;
	
	public SepaCamtFormat052() {
	}

	@Override
	public SepaCamtFormatEnum getCamtFormatEnum() {
		return SepaCamtFormatEnum.CAMT052;
	}

	@Override
	public SepaCamtVersionEnum getCamtVersionEnum() {
		return version;
	}

	@Override
	public void setCamtVersionEnum(SepaCamtVersionEnum version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return getCamtFormatEnum().getValue() + ".001." + getCamtVersionEnum().getValue();
	}

}
