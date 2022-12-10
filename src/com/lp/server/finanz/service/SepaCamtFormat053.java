package com.lp.server.finanz.service;

import java.io.Serializable;

public class SepaCamtFormat053 implements ISepaCamtFormat, Serializable {

	private static final long serialVersionUID = 81702818890643169L;

	private SepaCamtVersionEnum version;
	
	public SepaCamtFormat053() {
	}

	@Override
	public SepaCamtFormatEnum getCamtFormatEnum() {
		return SepaCamtFormatEnum.CAMT053;
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
