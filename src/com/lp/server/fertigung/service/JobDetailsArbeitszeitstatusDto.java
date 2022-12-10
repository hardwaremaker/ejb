package com.lp.server.fertigung.service;

import com.lp.server.system.service.JobDetailsDto;

public class JobDetailsArbeitszeitstatusDto extends JobDetailsDto {
	private static final long serialVersionUID = 4258453313351989256L;

	private String cPfadPattern;
	private String cEmailEmpfaenger;
	private Integer iTageBisStichtag;
	private Integer iArchivierungstage;
	
	public JobDetailsArbeitszeitstatusDto() {
	}

	public String getCPfadPattern() {
		return cPfadPattern;
	}
	
	public void setCPfadPattern(String cPfadPattern) {
		this.cPfadPattern = cPfadPattern;
	}
	
	public String getCEmailEmpfaenger() {
		return cEmailEmpfaenger;
	}
	
	public void setCEmailEmpfaenger(String cEmailEmpfaenger) {
		this.cEmailEmpfaenger = cEmailEmpfaenger;
	}
	
	public Integer getITageBisStichtag() {
		return iTageBisStichtag;
	}
	
	public void setITageBisStichtag(Integer iTageBisStichtag) {
		this.iTageBisStichtag = iTageBisStichtag;
	}
	
	public Integer getIArchivierungstage() {
		return iArchivierungstage;
	}
	
	public void setIArchivierungstage(Integer iArchivierungstage) {
		this.iArchivierungstage = iArchivierungstage;
	}
}
