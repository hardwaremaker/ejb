package com.lp.server.finanz.service;

public interface ISepaCamtFormat {

	SepaCamtFormatEnum getCamtFormatEnum();
	
	SepaCamtVersionEnum getCamtVersionEnum();
	
	void setCamtVersionEnum(SepaCamtVersionEnum version);
	
	String toString();
}
