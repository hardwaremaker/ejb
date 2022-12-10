/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2016 HELIUM V IT-Solutions GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of theLicense, or
 * (at your option) any later version.
 *
 * According to sec. 7 of the GNU Affero General Public License, version 3,
 * the terms of the AGPL are supplemented with the following terms:
 *
 * "HELIUM V" and "HELIUM 5" are registered trademarks of
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.finanz.assembler;

import java.io.Serializable;

import org.hibernate.internal.util.SerializationHelper;

// import org.hibernate.util.SerializationHelper;

import com.lp.server.finanz.bl.sepa.SepaKontoauszugFactory;
import com.lp.server.finanz.ejb.Sepakontoauszug;
import com.lp.server.finanz.service.SepaKontoauszugVersionEnum;
import com.lp.server.finanz.service.SepakontoauszugDto;

public class SepakontoauszugDtoAssembler {

	public static SepakontoauszugDto createDto(Sepakontoauszug entity) {
		SepakontoauszugDto dto = new SepakontoauszugDto();
		if (entity != null) {
			dto = createDtoSmall(entity);
			SepaKontoauszugFactory factory = new SepaKontoauszugFactory();
			dto.setOKontoauszug(factory.getSepaKontoauszug(dto.getVersion(), 
					SerializationHelper.deserialize(entity.getOKontoauszug())));
		}
		
		return dto;
	}
	
	public static SepakontoauszugDto createDtoSmall(Sepakontoauszug entity) {
		SepakontoauszugDto dto = new SepakontoauszugDto();
		if (entity != null) {
			dto.setIId(entity.getIId());
			dto.setIAuszug(entity.getIAuszug());
			dto.setNAnfangssaldo(entity.getNAnfangssaldo());
			dto.setNEndsaldo(entity.getNEndsaldo());
			dto.setVersion(SepaKontoauszugVersionEnum.fromInteger(entity.getIVersion()));
			dto.setCCamtFormat(entity.getCCamtFormat());
			dto.setPersonalIIdAendern(entity.getPersonalIIdAendern());
			dto.setPersonalIIdAnlegen(entity.getPersonalIIdAnlegen());
			dto.setTAendern(entity.getTAendern());
			dto.setTAnlegen(entity.getTAnlegen());
			dto.setPersonalIIdVerbuchen(entity.getPersonalIIdVerbuchen());
			dto.setTVerbuchen(entity.getTVerbuchen());
			dto.setTAuszug(entity.getTAuszug());
			dto.setBankverbindungIId(entity.getBankverbindungIId());
			dto.setStatusCNr(entity.getStatusCnr());
		}
		
		return dto;
	}

	public static Sepakontoauszug setEntity(Sepakontoauszug entity, SepakontoauszugDto dto) {
		entity.setIAuszug(dto.getIAuszug());
		entity.setIVersion(dto.getVersion().getValue());
		entity.setNAnfangssaldo(dto.getNAnfangssaldo());
		entity.setNEndsaldo(dto.getNEndsaldo());
		entity.setCCamtFormat(dto.getCCamtFormat());
		entity.setPersonalIIdAendern(dto.getPersonalIIdAendern());
		entity.setPersonalIIdAnlegen(dto.getPersonalIIdAnlegen());
		entity.setPersonalIIdVerbuchen(dto.getPersonalIIdVerbuchen());
		entity.setTAendern(dto.getTAendern());
		entity.setTAnlegen(dto.getTAnlegen());
		entity.setTVerbuchen(dto.getTVerbuchen());
		entity.setTAuszug(dto.getTAuszug());
		entity.setBankverbindungIId(dto.getBankverbindungIId());
		entity.setStatusCnr(dto.getStatusCNr());
		
		SepaKontoauszugFactory factory = new SepaKontoauszugFactory();
		Serializable kontoauszugObject = factory.getSepaKontoauszugObject(
				dto.getVersion(), dto.getKontoauszug());
		entity.setOKontoauszug(SerializationHelper.serialize(kontoauszugObject));
		
		return entity;
	}
}
