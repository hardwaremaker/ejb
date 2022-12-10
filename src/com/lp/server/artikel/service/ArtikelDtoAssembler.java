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
package com.lp.server.artikel.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.artikel.ejb.Artikel;

public class ArtikelDtoAssembler {
	public static ArtikelDto createDto(Artikel artikel) {
		ArtikelDto artikelDto = new ArtikelDto();
		if (artikel != null) {
			artikelDto.setIId(artikel.getIId());
			artikelDto.setCNr(artikel.getCNr());
			artikelDto.setHerstellerIId(artikel.getHerstellerIId());
			artikelDto.setCArtikelbezhersteller(artikel
					.getCArtikelbezhersteller());
			artikelDto.setCArtikelnrhersteller(artikel
					.getCArtikelnrhersteller());
			artikelDto.setArtgruIId(artikel.getArtgruIId());
			artikelDto.setArtklaIId(artikel.getArtklaIId());
			artikelDto.setArtikelartCNr(artikel.getArtikelartCNr());
			artikelDto.setEinheitCNr(artikel.getEinheitCNr());
			artikelDto.setBSeriennrtragend(artikel.getBSeriennrtragend());
			artikelDto.setBChargennrtragend(artikel.getBChargennrtragend());
			artikelDto.setBLagerbewertet(artikel.getBLagerbewertet());
			artikelDto.setBLagerbewirtschaftet(artikel
					.getBLagerbewirtschaftet());
			artikelDto.setBDokumentenpflicht(artikel.getBDokumentenpflicht());
			artikelDto.setCReferenznr(artikel.getCReferenznr());
			artikelDto.setFLagermindest(artikel.getFLagermindest());
			artikelDto.setFLagersoll(artikel.getFLagersoll());
			artikelDto.setFVerpackungsmenge(artikel.getFVerpackungsmenge());
			artikelDto.setFVerschnittfaktor(artikel.getFVerschnittfaktor());
			artikelDto.setFVerschnittbasis(artikel.getFVerschnittbasis());
			artikelDto.setFJahresmenge(artikel.getFJahresmenge());
			artikelDto.setMwstsatzbezIId(artikel.getMwstsatzIId());
			artikelDto.setMaterialIId(artikel.getMaterialIId());
			artikelDto.setFGewichtkg(artikel.getFGewichtkg());
			artikelDto.setFMaterialgewicht(artikel.getFMaterialgewicht());
			artikelDto.setNUmrechnungsfaktor(artikel.getNUmrechnungsfaktor());
			artikelDto.setFarbcodeIId(artikel.getFarbcodeIId());
			artikelDto.setEinheitCNrBestellung(artikel
					.getEinheitCNrBestellung());
			artikelDto.setBAntistatic(artikel.getBAntistatic());
			artikelDto.setIGarantiezeit(artikel.getIGarantiezeit());
			artikelDto.setArtikelIIdZugehoerig(artikel
					.getArtikelIIdZugehoerig());
			artikelDto.setArtikelIIdErsatz(artikel.getArtikelIIdErsatz());
			artikelDto.setFVertreterprovisionmax(artikel
					.getFVertreterprovisionmax());
			artikelDto.setFMinutenfaktor1(artikel.getFMinutenfaktor1());
			artikelDto.setFMinutenfaktor2(artikel.getFMinutenfaktor2());
			artikelDto.setFMindestdeckungsbeitrag(artikel
					.getFMindestdeckungsbeitrag());
			artikelDto.setCVerkaufseannr(artikel.getCVerkaufseannr());
			artikelDto.setCVerpackungseannr(artikel.getCVerpackungseannr());
			artikelDto.setCWarenverkehrsnummer(artikel
					.getCWarenverkehrsnummer());
			artikelDto.setBRabattierbar(artikel.getBRabattierbar());
			artikelDto.setMandantCNr(artikel.getMandantCNr());
			artikelDto.setTAnlegen(artikel.getTAnlegen());
			artikelDto.setPersonalIIdAnlegen(artikel.getPersonalIIdAnlegen());
			artikelDto.setTAendern(artikel.getTAendern());
			artikelDto.setPersonalIIdAendern(artikel.getPersonalIIdAendern());
			artikelDto.setBVersteckt(artikel.getBVersteckt());
			artikelDto.setLandIIdUrsprungsland(artikel
					.getLandIIdUrsprungsland());
			artikelDto.setFFertigungssatzgroesse(artikel
					.getFFertigungssatzgroesse());
			artikelDto.setFMaxfertigungssatzgroesse(artikel
					.getFMaxfertigungssatzgroesse());
			artikelDto.setIWartungsintervall(artikel.getIWartungsintervall());
			artikelDto.setISofortverbrauch(artikel.getISofortverbrauch());
			artikelDto.setCRevision(artikel.getCRevision());
			artikelDto.setCIndex(artikel.getCIndex());
			artikelDto.setFStromverbrauchmax(artikel.getFStromverbrauchmax());
			artikelDto.setFStromverbrauchtyp(artikel.getFStromverbrauchtyp());
			artikelDto.setBVerleih(artikel.getBVerleih());
			artikelDto.setFDetailprozentmindeststand(artikel
					.getFDetailprozentmindeststand());
			artikelDto.setLfliefergruppeIId(artikel.getLfliefergruppeIId());
			artikelDto.setbNurzurinfo(artikel.getbNurzurinfo());
			artikelDto.setbReinemannzeit(artikel.getbReinemannzeit());
			artikelDto.setShopgruppeIId(artikel.getShopgruppeIId());
			artikelDto.setbBestellmengeneinheitInvers(artikel
					.getbBestellmengeneinheitInvers());
			artikelDto.setBWerbeabgabepflichtig(artikel
					.getBWerbeabgabepflichtig());
			artikelDto.setTLetztewartung(artikel.getTLetztewartung());
			artikelDto.setPersonalIIdLetztewartung(artikel
					.getPersonalIIdLetztewartung());
			artikelDto.setBKalkulatorisch(artikel.getBKalkulatorisch());
			artikelDto.setNAufschlagBetrag(artikel.getNAufschlagBetrag());
			artikelDto.setFAufschlagProzent(artikel.getFAufschlagProzent());
			artikelDto.setCUL(artikel.getCUL());
			artikelDto.setReachIId(artikel.getReachIId());
			artikelDto.setRohsIId(artikel.getRohsIId());
			artikelDto.setAutomotiveIId(artikel.getAutomotiveIId());
			artikelDto.setMedicalIId(artikel.getMedicalIId());
			artikelDto.setFUeberproduktion(artikel.getFUeberproduktion());
			artikelDto.setVorzugIId(artikel.getVorzugIId());
			artikelDto.setCEccn(artikel.getCEccn());
			artikelDto.setFFertigungsVpe(artikel.getFFertigungsVpe());
			artikelDto.setBRahmenartikel(artikel.getBRahmenartikel());
			artikelDto.setNVerschnittmenge(artikel.getNVerschnittmenge());
			artikelDto.setVerpackungsmittelIId(artikel.getVerpackungsmittelIId());
			artikelDto.setNVerpackungsmittelmenge(artikel.getNVerpackungsmittelmenge());
			artikelDto.setNMindestverkaufsmenge(artikel.getNMindestverkaufsmenge());
			artikelDto.setFMultiplikatorZugehoerigerartikel(artikel.getFMultiplikatorZugehoerigerartikel());
			artikelDto.setBAzinabnachkalk(artikel.getBAzinabnachkalk());
			artikelDto.setBKommissionieren(artikel.getBKommissionieren());
			artikelDto.setBKeineLagerzubuchung(artikel.getBKeineLagerzubuchung());
			artikelDto.setIExternerArbeitsgang(artikel.getIExternerArbeitsgang());
			artikelDto.setILaengemaxSnrchnr(artikel.getILaengemaxSnrchnr());
			artikelDto.setILaengeminSnrchnr(artikel.getILaengeminSnrchnr());
			artikelDto.setBWepinfoAnAnforderer(artikel.getBWepinfoAnAnforderer());
			artikelDto.setBVkpreispflichtig(artikel.getBVkpreispflichtig());
			artikelDto.setIPassiveReisezeit(artikel.getIPassiveReisezeit());
			artikelDto.setBSummeInBestellung(artikel.getBSummeInBestellung());
			artikelDto.setBBevorzugt(artikel.getBBevorzugt());
			artikelDto.setBMultiplikatorInvers(artikel.getBMultiplikatorInvers());
			artikelDto.setBMultiplikatorAufrunden(artikel.getBMultiplikatorAufrunden());
			artikelDto.setPersonalIIdFreigabe(artikel.getPersonalIIdFreigabe());
			artikelDto.setTFreigabe(artikel.getTFreigabe());
			artikelDto.setCFreigabeZuerueckgenommen(artikel.getCFreigabeZuerueckgenommen());
			artikelDto.setBBewilligungspflichtig(artikel.getBBewilligungspflichtig());
			artikelDto.setBMeldepflichtig(artikel.getBMeldepflichtig());
			
			artikelDto.setWaffenausfuehrungIId(artikel.getWaffenausfuehrungIId());
			artikelDto.setWaffentypIId(artikel.getWaffentypIId());
			artikelDto.setWaffentypFeinIId(artikel.getWaffentypFeinIId());
			artikelDto.setWaffenkategorieIId(artikel.getWaffenkategorieIId());
			artikelDto.setWaffenzusatzIId(artikel.getWaffenzusatzIId());
			artikelDto.setWaffenkaliberIId(artikel.getWaffenkaliberIId());
			artikelDto.setNPreisZugehoerigerartikel(artikel.getNPreisZugehoerigerartikel());
			artikelDto.setLaseroberflaecheIId(artikel.getLaseroberflaecheIId());
			
		}
		return artikelDto;
	}

	public static ArtikelDto[] createDtos(Collection<?> artikels) {
		List<ArtikelDto> list = new ArrayList<ArtikelDto>();
		if (artikels != null) {
			Iterator<?> iterator = artikels.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Artikel) iterator.next()));
			}
		}
		ArtikelDto[] returnArray = new ArtikelDto[list.size()];
		return (ArtikelDto[]) list.toArray(returnArray);
	}
}
