package com.lp.server.fertigung.service;

import java.io.Serializable;

import com.lp.util.EJBExceptionLP;

public class LosablieferungResultDto implements Serializable {
	private static final long serialVersionUID = 8642921547099859894L;

	private LosablieferungDto losablieferungDto;
	private EJBExceptionLP ejbExceptionLP;
	private boolean losErledigt = false;
	
	public LosablieferungResultDto(LosablieferungDto losablieferungDto) {
		setLosablieferungDto(losablieferungDto);
	}

	public void setLosablieferungDto(LosablieferungDto losablieferungDto) {
		this.losablieferungDto = losablieferungDto;
	}
	
	public LosablieferungDto getLosablieferungDto() {
		return losablieferungDto;
	}
	
	public void setEjbExceptionLP(EJBExceptionLP ejbExceptionLP) {
		this.ejbExceptionLP = ejbExceptionLP;
	}
	
	public EJBExceptionLP getEjbExceptionLP() {
		return ejbExceptionLP;
	}
	
	public void setLosErledigt(boolean losErledigt) {
		this.losErledigt = losErledigt;
	}
	
	public boolean isLosErledigt() {
		return losErledigt;
	}
}
