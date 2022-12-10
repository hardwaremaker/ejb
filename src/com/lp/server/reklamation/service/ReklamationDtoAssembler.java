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
package com.lp.server.reklamation.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.reklamation.ejb.Reklamation;

public class ReklamationDtoAssembler {

	public static ReklamationDto createDto(Reklamation reklamation) {
		ReklamationDto reklamationDto = new ReklamationDto();
		if (reklamation != null) {
			reklamationDto.setIId(reklamation.getIId());
			reklamationDto.setMandantCNr(reklamation.getMandantCNr());
			reklamationDto.setCNr(reklamation.getCNr());
			reklamationDto.setReklamationartCNr(reklamation
					.getReklamationartCNr());
			reklamationDto.setTBelegdatum(reklamation.getTBelegdatum());
			reklamationDto.setKostenstelleIId(reklamation.getKostenstelleIId());
			reklamationDto.setFehlerangabeIId(reklamation.getFehlerangabeIId());
			reklamationDto.setAufnahmeartIId(reklamation.getAufnahmeartIId());
			reklamationDto.setPersonalIIdAufnehmer(reklamation
					.getPersonalIIdAufnehmer());
			reklamationDto.setBArtikel(reklamation.getBArtikel());
			reklamationDto.setArtikelIId(reklamation.getArtikelIId());
			reklamationDto.setCHandartikel(reklamation.getCHandartikel());
			reklamationDto.setNMenge(reklamation.getNMenge());
			reklamationDto.setBFremdprodukt(reklamation.getBFremdprodukt());
			reklamationDto.setCGrund(reklamation.getCGrund());
			reklamationDto.setLosIId(reklamation.getLosIId());
			reklamationDto.setBestellungIId(reklamation.getBestellungIId());
			reklamationDto.setLieferscheinIId(reklamation.getLieferscheinIId());
			reklamationDto.setRechnungIId(reklamation.getRechnungIId());
			reklamationDto.setKundeIId(reklamation.getKundeIId());
			reklamationDto.setLieferantIId(reklamation.getLieferantIId());
			reklamationDto.setAnsprechpartnerIId(reklamation
					.getAnsprechpartnerIId());
			reklamationDto.setAnsprechpartnerIIdLieferant(reklamation
					.getAnsprechpartnerIIdLieferant());
			reklamationDto.setCProjekt(reklamation.getCProjekt());
			reklamationDto.setXAnalyse(reklamation.getXAnalyse());
			reklamationDto.setXKommentar(reklamation.getXKommentar());
			reklamationDto.setFehlerIId(reklamation.getFehlerIId());
			reklamationDto.setBBerechtigt(reklamation.getBBerechtigt());
			reklamationDto.setPersonalIIdRuecksprache(reklamation
					.getPersonalIIdRuecksprache());
			reklamationDto.setTRuecksprache(reklamation.getTRuecksprache());
			reklamationDto.setCRuecksprachemit(reklamation
					.getCRuecksprachemit());
			reklamationDto.setNKostenmaterial(reklamation.getNKostenmaterial());
			reklamationDto.setNKostenarbeitszeit(reklamation
					.getNKostenarbeitszeit());
			reklamationDto.setMassnahmeIIdKurz(reklamation
					.getMassnahmeIIdKurz());
			reklamationDto.setTMassnahmebiskurz(reklamation
					.getTMassnahmebiskurz());
			reklamationDto.setTEingefuehrtkurz(reklamation
					.getTEingefuehrtkurz());
			reklamationDto.setPersonalIIdEingefuehrtkurz(reklamation
					.getPersonalIIdEingefuehrtkurz());
			reklamationDto.setMassnahmeIIdMittel(reklamation
					.getMassnahmeIIdMittel());
			reklamationDto.setTMassnahmebismittel(reklamation
					.getTMassnahmebismittel());
			reklamationDto.setTEingefuehrtmittel(reklamation
					.getTEingefuehrtmittel());
			reklamationDto.setPersonalIIdEingefuehrtmittel(reklamation
					.getPersonalIIdEingefuehrtmittel());
			reklamationDto.setMassnahmeIIdLang(reklamation
					.getMassnahmeIIdLang());
			reklamationDto.setTMassnahmebislang(reklamation
					.getTMassnahmebislang());
			reklamationDto.setTEingefuehrtlang(reklamation
					.getTEingefuehrtlang());
			reklamationDto.setPersonalIIdEingefuehrtlang(reklamation
					.getPersonalIIdEingefuehrtlang());
			reklamationDto.setPersonalIIdAnlegen(reklamation
					.getPersonalIIdAnlegen());
			reklamationDto.setTAnlegen(reklamation.getTAnlegen());
			reklamationDto.setPersonalIIdAendern(reklamation
					.getPersonalIIdAendern());
			reklamationDto.setTAendern(reklamation.getTAendern());
			reklamationDto.setTErledigt(reklamation.getTErledigt());
			reklamationDto.setPersonalIIdErledigt(reklamation
					.getPersonalIIdErledigt());
			reklamationDto.setSchwereIId(reklamation.getSchwereIId());
			reklamationDto.setBehandlungIId(reklamation.getBehandlungIId());
			reklamationDto.setWareneingangIId(reklamation.getWareneingangIId());

			reklamationDto.setWirksamkeitIId(reklamation.getWirksamkeitIId());
			reklamationDto.setXWirksamkeit(reklamation.getXWirksamkeit());
			reklamationDto.setXMassnahmeKurz(reklamation.getXMassnahmeKurz());
			reklamationDto.setXMassnahmeMittel(reklamation
					.getXMassnahmeMittel());
			reklamationDto.setXMassnahmeLang(reklamation.getXMassnahmeLang());

			reklamationDto.setBBetrifftgelieferte(reklamation
					.getBBetrifftgelieferte());
			reklamationDto.setBBetrifftlagerstand(reklamation
					.getBBetrifftlagerstand());
			reklamationDto.setNStueckgelieferte(reklamation
					.getNStueckgelieferte());
			reklamationDto.setNStuecklagerstand(reklamation
					.getNStuecklagerstand());

			reklamationDto.setXGrundLang(reklamation.getXGrundLang());
			reklamationDto.setPersonalIIdWirksamkeit(reklamation
					.getPersonalIIdWirksamkeit());
			reklamationDto.setTWirksamkeitbis(reklamation.getTWirksamkeitbis());
			reklamationDto.setTWirksamkeiteingefuehrt(reklamation
					.getTWirksamkeiteingefuehrt());
			reklamationDto.setStatusCNr(reklamation.getStatusCNr());
			reklamationDto.setCKdlsnr(reklamation.getCKdlsnr());
			reklamationDto.setCKdreklanr(reklamation.getCKdreklanr());
			reklamationDto.setMaschineIId(reklamation.getMaschineIId());
			reklamationDto.setLossollarbeitsplanIId(reklamation
					.getLossollarbeitsplanIId());
			reklamationDto.setPersonalIIdVerursacher(reklamation
					.getPersonalIIdVerursacher());
			reklamationDto.setCSeriennrchargennr(reklamation
					.getCSeriennrchargennr());
			reklamationDto.setIKundeunterart(reklamation.getIKundeunterart());
			reklamationDto.setProjektIId(reklamation.getProjektIId());
			reklamationDto.setCLflsnr(reklamation.getCLflsnr());
			reklamationDto.setCLfreklanr(reklamation.getCLfreklanr());
			reklamationDto.setTWareErhalten(reklamation.getTWareErhalten());
			reklamationDto.setCBestellnummer(reklamation.getCBestellnummer());
			reklamationDto.setCWareneingang(reklamation.getCWareneingang());

		}
		return reklamationDto;
	}

	public static ReklamationDto[] createDtos(Collection<?> reklamations) {
		List<ReklamationDto> list = new ArrayList<ReklamationDto>();
		if (reklamations != null) {
			Iterator<?> iterator = reklamations.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Reklamation) iterator.next()));
			}
		}
		ReklamationDto[] returnArray = new ReklamationDto[list.size()];
		return (ReklamationDto[]) list.toArray(returnArray);
	}
}
