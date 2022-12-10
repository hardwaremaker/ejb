package com.lp.server.finanz.service;

import com.lp.server.finanz.ejb.IIso20022NachrichtEntity;
import com.lp.server.finanz.ejb.Iso20022LastschriftSchema;
import com.lp.server.finanz.ejb.Iso20022ZahlungsauftragSchema;

public class Iso20022NachrichtDtoAssembler {

	public static Iso20022ZahlungauftragDto createDto(Iso20022ZahlungsauftragSchema entity) {
		Iso20022ZahlungauftragDto dto = new Iso20022ZahlungauftragDto();
		setupDto(dto, entity);
		return dto;
	}
	
	public static Iso20022LastschriftDto createDto(Iso20022LastschriftSchema entity) {
		Iso20022LastschriftDto dto = new Iso20022LastschriftDto();
		setupDto(dto, entity);
		return dto;
	}
	
	private static void setupDto(Iso20022NachrichtDto dto, IIso20022NachrichtEntity entity) {
		dto.setIId(entity.getIId());
		dto.setStandardIId(entity.getStandardIId());
		dto.setSchemaIId(entity.getSchemaIId());
		dto.setSchemaDto(Iso20022SchemaDtoAssembler.createDto(entity.getSchemaObject()));
	}
}
