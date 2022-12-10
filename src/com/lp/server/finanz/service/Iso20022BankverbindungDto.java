package com.lp.server.finanz.service;

import java.io.Serializable;

public class Iso20022BankverbindungDto implements Serializable {
	private static final long serialVersionUID = 8911879876649037465L;

	private Integer iId;
	private Integer bankverbindungIId;
	private Integer zahlungsauftragSchemaIId;
	private Iso20022SchemaDto zahlungsauftragSchemaDto;
	private Integer lastschriftSchemaIId;
	private Iso20022SchemaDto lastschriftSchemaDto;
	private Iso20022StandardEnum standardEnum;
	
	public Integer getIId() {
		return iId;
	}
	public void setIId(Integer iId) {
		this.iId = iId;
	}
	
	public Integer getBankverbindungIId() {
		return bankverbindungIId;
	}
	public void setBankverbindungIId(Integer bankverbindungIId) {
		this.bankverbindungIId = bankverbindungIId;
	}
	
	public Integer getZahlungsauftragSchemaIId() {
		return zahlungsauftragSchemaIId;
	}
	public void setZahlungsauftragSchemaIId(Integer zahlungsauftragSchemaIId) {
		this.zahlungsauftragSchemaIId = zahlungsauftragSchemaIId;
	}
	
	public Iso20022SchemaDto getZahlungsauftragSchemaDto() {
		return zahlungsauftragSchemaDto;
	}
	public void setZahlungsauftragSchemaDto(Iso20022SchemaDto zahlungsauftragSchemaDto) {
		this.zahlungsauftragSchemaDto = zahlungsauftragSchemaDto;
	}
	
	public Integer getLastschriftSchemaIId() {
		return lastschriftSchemaIId;
	}
	public void setLastschriftSchemaIId(Integer lastschriftSchemaIId) {
		this.lastschriftSchemaIId = lastschriftSchemaIId;
	}
	
	public Iso20022SchemaDto getLastschriftSchemaDto() {
		return lastschriftSchemaDto;
	}
	public void setLastschriftSchemaDto(Iso20022SchemaDto lastschriftSchemaDto) {
		this.lastschriftSchemaDto = lastschriftSchemaDto;
	}
	
	public void setStandardEnum(Iso20022StandardEnum standardEnum) {
		this.standardEnum = standardEnum;
	}
	public Iso20022StandardEnum getStandardEnum() {
		return standardEnum;
	}
	
	public boolean hasZahlungsauftragSchema() {
		return getZahlungsauftragSchemaIId() != null;
	}
	
	public boolean hasLastschriftSchema() {
		return getLastschriftSchemaIId() != null;
	}
}
