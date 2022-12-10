package com.lp.server.system.service;

public class JobDetailsKpiReportDto extends JobDetailsDto {
	private static final long serialVersionUID = -3969843370139566346L;
	private String cPfadPattern;
	private Integer iTage;
	private Integer iArchivierungstage;
	private String cEmailEmpfaenger;
	
	public String getcPfadPattern() {
		return cPfadPattern;
	}
	public void setcPfadPattern(String cPfadPattern) {
		this.cPfadPattern = cPfadPattern;
	}
	public Integer getiTage() {
		return iTage;
	}
	public void setiTage(Integer iTage) {
		this.iTage = iTage;
	}
	public Integer getiArchivierungstage() {
		return iArchivierungstage;
	}
	public void setiArchivierungstage(Integer iArchivierungstage) {
		this.iArchivierungstage = iArchivierungstage;
	}
	public String getcEmailEmpfaenger() {
		return cEmailEmpfaenger;
	}
	public void setcEmailEmpfaenger(String cEmailEmpfaenger) {
		this.cEmailEmpfaenger = cEmailEmpfaenger;
	}
}
