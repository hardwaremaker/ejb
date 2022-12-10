package com.lp.server.eingangsrechnung.service;

import com.lp.server.partner.service.PartnerDto;

public class ZahlungsvorschlagExportException extends Exception {
	private static final long serialVersionUID = 6944575236802005158L;

	private Integer code;
	private PartnerDto partnerDto;
	private EingangsrechnungDto erDto;
	private Integer severity;
	
	public static final Integer SEVERITY_NONE    = 0 ;
	public static final Integer SEVERITY_DEBUG   = 1 ;
	public static final Integer SEVERITY_INFO    = 2 ;
	public static final Integer SEVERITY_WARNING = 3 ;
	public static final Integer SEVERITY_ERROR   = 4 ;

	public ZahlungsvorschlagExportException(EingangsrechnungDto erDto,
			PartnerDto partnerDto, Integer iCode, Integer severity) {
		setCode(iCode);
		setPartnerDto(partnerDto);
		setErDto(erDto);
		setSeverity(severity);
	}

	public ZahlungsvorschlagExportException(Integer iCode, Integer severity) {
		setCode(iCode);
		setSeverity(severity);
	}

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	public EingangsrechnungDto getErDto() {
		return erDto;
	}

	public void setErDto(EingangsrechnungDto erDto) {
		this.erDto = erDto;
	}
	
	public Integer getCode() {
		return code;
	}
	
	public void setCode(Integer code) {
		this.code = code;
	}
	
	public Integer getSeverity() {
		return severity;
	}
	
	public void setSeverity(Integer severity) {
		this.severity = severity;
	}
}
