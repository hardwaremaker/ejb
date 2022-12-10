/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
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
package com.lp.server.projekt.assembler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.projekt.ejb.Projekt;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.util.EditorContentIId;

public class ProjektDtoAssembler {
	public static ProjektDto createDto(Projekt projekt) {
		ProjektDto projektDto = new ProjektDto();
		if (projekt != null) {
			projektDto.setIId(projekt.getIId());
			projektDto.setCNr(projekt.getCNr());
			projektDto.setKategorieCNr(projekt.getKategorieCNr());
			projektDto.setCTitel(projekt.getCTitel());
			projektDto.setPersonalIIdErzeuger(projekt.getPersonalIIdErzeuger());
			projektDto.setPersonalIIdZugewiesener(projekt
					.getPersonalIIdZugewiesener());
			projektDto.setProjekttypCNr(projekt.getProjProjekttypCNr());
			projektDto.setIPrio(projekt.getIPrio());
			projektDto.setStatusCNr(projekt.getProjProjektstatusCNr());
			projektDto.setMandantCNr(projekt.getMandantCNr());
			projektDto.setOAttachments(projekt.getOAttachments());
			projektDto.setCAttachmentsType(projekt.getCAttachmentstype());
			projektDto.setCDateiname(projekt.getCDateiname());
			projektDto.setXFreetext(projekt.getXFreetext());
			projektDto.setTZielwunschdatum(projekt.getTZielwunschdatum());
			projektDto.setPartnerIId(projekt.getPartnerIId());
			projektDto.setAnsprechpartnerIId(projekt.getAnsprechpartnerIId());
			projektDto.setIVerrechenbar(projekt.getIVerrechenbar());
			projektDto.setPersonalIIdAnlegen(projekt.getPersonalIIdAnlegen());
			projektDto.setTAnlegen(projekt.getTAnlegen());
			projektDto.setPersonalIIdAendern(projekt.getPersonalIIdAendern());
			projektDto.setTAendern(projekt.getTAendern());
			projektDto.setDDauer(projekt.getFDauer());
			projektDto.setTZeit(projekt.getTZeit());
			projektDto.setTErledigt(projekt.getTErledigt());
			projektDto.setPersonalIIdErlediger(projekt
					.getPersonalIIdErlediger());
			projektDto.setBFreigegeben(projekt.getBFreigegeben());
			projektDto.setISort(projekt.getISort());
			projektDto.setIWahrscheinlichkeit(projekt.getIWahrscheinlichkeit());
			projektDto.setNUmsatzgeplant(projekt.getNUmsatzgeplant());
			projektDto.setProjekterledigungsgrundIId(projekt
					.getProjekterledigungsgrundIId());
			projektDto.setBereichIId(projekt.getBereichIId());
			projektDto.setProjektIIdNachfolger(projekt
					.getProjektIIdNachfolger());
			projektDto.setTInternerledigt(projekt.getTInternerledigt());
			projektDto.setPersonalIIdInternerledigt(projekt
					.getPersonalIIdInternerledigt());
			projektDto.setBuildNumber(projekt.getBuildNumber());
			projektDto.setDeployNumber(projekt.getDeployNumber());
			projektDto.setPartnerIIdBetreiber(projekt.getPartnerIIdBetreiber());
			projektDto.setVkfortschrittIId(projekt.getVkfortschrittIId());
			projektDto.setTRealisierung(projekt.getTRealisierung());
			projektDto.setAnsprechpartnerIIdBetreiber(projekt.getAnsprechpartnerIIdBetreiber());
			projektDto.setArtikelIId(projekt.getArtikelIId());
			projektDto.setContentId(new EditorContentIId(projekt.getEditorContentIId()));
		}
		return projektDto;
	}

	public static ProjektDto[] createDtos(Collection<?> projekts) {
		List<ProjektDto> list = new ArrayList<ProjektDto>();
		if (projekts != null) {
			Iterator<?> iterator = projekts.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Projekt) iterator.next()));
			}
		}
		ProjektDto[] returnArray = new ProjektDto[list.size()];
		return (ProjektDto[]) list.toArray(returnArray);
	}
}
