package com.lp.server.finanz.service;

import com.lp.server.finanz.ejb.Iso20022Bankverbindung;

public class Iso20022BankverbindungDtoAssembler {

	public static Iso20022BankverbindungDto createDto(Iso20022Bankverbindung entity) {
		Iso20022BankverbindungDto dto = new Iso20022BankverbindungDto();
		dto.setIId(entity.getIId());
		dto.setBankverbindungIId(entity.getBankverbindungIId());
		dto.setLastschriftSchemaIId(entity.getLastschriftschemaIId());
		if (entity.getLastschriftschemaIId() != null) {
			dto.setLastschriftSchemaDto(Iso20022SchemaDtoAssembler.createDto(entity.getLastschriftschema().getSchemaObject()));
		}
		dto.setZahlungsauftragSchemaIId(entity.getZahlungsauftragschemaIId());
		if (entity.getZahlungsauftragschemaIId() != null) {
			dto.setZahlungsauftragSchemaDto(Iso20022SchemaDtoAssembler.createDto(entity.getZahlungsauftragschema().getSchemaObject()));
		}
		dto.setStandardEnum(Iso20022StandardEnum.lookup(entity.getZahlungsauftragschema().getStandardObject().getCNr()));
		
		return dto;
	}

	public static Iso20022Bankverbindung setEntity(Iso20022Bankverbindung entity, Iso20022BankverbindungDto dto) {
		entity.setBankverbindungIId(dto.getBankverbindungIId());
		entity.setLastschriftschemaIId(dto.getLastschriftSchemaIId());
		entity.setZahlungsauftragschemaIId(dto.getZahlungsauftragSchemaIId());
		return entity;
	}
}
