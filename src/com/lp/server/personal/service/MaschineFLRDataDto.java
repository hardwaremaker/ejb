package com.lp.server.personal.service;

import java.io.Serializable;

public class MaschineFLRDataDto implements IMaschineFLRData, Serializable {

	private static final long serialVersionUID = -4885365989236992320L;

	private Integer personalIdStarter;
	private Long gestartetUm;
	private Integer lossollarbeitsplanIId;
	
	public MaschineFLRDataDto() {
	}

	public MaschineFLRDataDto(Integer personalIdStarter, Long gestartetUm, Integer lossollarbeitsplanIId) {
		setPersonalIdStarter(personalIdStarter);
		setGestartetUm(gestartetUm);
		setLossollarbeitsplanIId(lossollarbeitsplanIId);
	}

	@Override
	public Integer getPersonalIdStarter() {
		return personalIdStarter;
	}

	public void setPersonalIdStarter(Integer personalIdStarter) {
		this.personalIdStarter = personalIdStarter;
	}

	@Override
	public Long getGestartetUm() {
		return gestartetUm;
	}

	public void setGestartetUm(Long gestartetUm) {
		this.gestartetUm = gestartetUm;
	}

	public Integer getLossollarbeitsplanIId() {
		return lossollarbeitsplanIId;
	}

	public void setLossollarbeitsplanIId(Integer lossollarbeitsplanIId) {
		this.lossollarbeitsplanIId = lossollarbeitsplanIId;
	}

}
