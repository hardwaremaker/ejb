package com.lp.server.fertigung.service;

import com.lp.server.system.service.JobDetailsDto;

public class JobDetailsBedarfsuebernahmeOffeneDto extends JobDetailsDto {
	private static final long serialVersionUID = -8390919541570039009L;

	private String cEmailEmpfaenger;

	public JobDetailsBedarfsuebernahmeOffeneDto() {
	}

	public void setCEmailEmpfaenger(String cEmailEmpfaenger) {
		this.cEmailEmpfaenger = cEmailEmpfaenger;
	}
	
	public String getCEmailEmpfaenger() {
		return cEmailEmpfaenger;
	}
}
