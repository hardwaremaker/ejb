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
package com.lp.server.partner.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.partner.ejb.Kunde;

/**
 * <p>
 * <I>[Hier die Beschreibung der Klasse eingfuegen]</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>dd.mm.04</I>
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author Josef Ornetsmueller
 * @version 1.0
 */
public class KundeDtoAssembler {
	public static KundeDto createDto(Kunde kunde) {
		KundeDto kundeDto = new KundeDto();
		if (kunde != null) {
			kundeDto.setIId(kunde.getIId());
			kundeDto.setPartnerIId(kunde.getPartnerIId());
			kundeDto.setMandantCNr(kunde.getMandantCNr());
			kundeDto.setCWaehrung(kunde.getWaehrungCNr());
			kundeDto.setLieferartIId(kunde.getLieferartIId());
			kundeDto.setSpediteurIId(kunde.getSpediteurIId());
			kundeDto.setZahlungszielIId(kunde.getZahlungszielIId());
			kundeDto.setKostenstelleIId(kunde.getKostenstelleIId());
			kundeDto.setMwstsatzbezIId(kunde.getMwstsatzIId());
			kundeDto.setVkpfArtikelpreislisteIIdStdpreisliste(kunde
					.getVkpfartikelpreislisteIIdstdpreisliste());
			kundeDto.setFRabattsatz(kunde.getFRabattsatz());
			kundeDto.setIGarantieinmonaten(kunde.getIGarantieinmonaten());
			kundeDto.setXKommentar(kunde.getXKommentar());
			kundeDto.setTAnlegen(kunde.getTAnlegen());
			kundeDto.setPersonalAnlegenIID(kunde.getPersonalIIdAnlegen());
			kundeDto.setTAendern(kunde.getTAendern());
			kundeDto.setLagerIIdAbbuchungslager(kunde.getLagerIIdAbbuchungslager());
			kundeDto.setPersonalAendernIID(kunde.getPersonalIIdAendern());
			kundeDto.setCKurznr(kunde.getCKurznr());
			kundeDto.setNKreditlimit(kunde.getNKreditlimit());
			kundeDto.setTBonitaet(kunde.getTBonitaet());
			kundeDto.setTLiefersperream(kunde.getTLiefersperream());
			kundeDto.setBMindermengenzuschlag(kunde.getBMindermengenzuschlag());
			kundeDto.setBMonatsrechnung(kunde.getBMonatsrechnung());
			kundeDto.setBSammelrechnung(kunde.getBSammelrechnung());
			kundeDto.setBIstreempfaenger(kunde.getBIstreempfaenger());
			kundeDto.setBPreiseanlsandrucken(kunde.getBPreiseanlsandrucken());
			kundeDto.setIDefaultrekopiendrucken(kunde
					.getIDefaultrekopiendrucken());
			kundeDto.setIDefaultlskopiendrucken(kunde
					.getIDefaultlskopiendrucken());
			kundeDto.setBRechnungsdruckmitrabatt(kunde
					.getBRechnungsdruckmitrabatt());
			kundeDto.setIMitarbeiteranzahl(kunde.getIMitarbeiteranzahl());
			kundeDto.setCTour(kunde.getCTour());
			kundeDto.setCLieferantennr(kunde.getCLieferantennr());
			kundeDto.setBDistributor(kunde.getBDistributor());
			kundeDto.setCAbc(kunde.getCAbc());
			kundeDto.setTAgbuebermittelung(kunde.getTAgbuebermittelung());
			kundeDto.setBAkzeptiertteillieferung(kunde
					.getBAkzeptiertteillieferung());
			kundeDto.setBLsgewichtangeben(kunde.getBLsgewichtangeben());
			kundeDto.setParternbankIId(kunde.getPartnerbankIId());
			kundeDto.setbIstinteressent(kunde.getBIstinteressent());
			kundeDto.setCFremdsystemnr(kunde.getCFremdsystemnr());
			kundeDto.setIidDebitorenkonto(kunde.getKontoIIdDebitorenkonto());
			kundeDto.setIidErloeseKonto(kunde.getKontoIIdErloesekonto());
			kundeDto.setPersonaliIdProvisionsempfaenger(kunde
					.getPersonalIIdBekommeprovision());
			kundeDto.setPartnerIIdRechnungsadresse(kunde
					.getPartnerIIdRechnungsadresse());
			kundeDto.setSHinweisextern(kunde.getCHinweisextern());
			kundeDto.setSHinweisintern(kunde.getCHinweisintern());
			kundeDto.setFZessionsfaktor(kunde.getFZessionsfaktor());
			kundeDto.setBVersteckterlieferant(kunde.getBVersteckterlieferant());
			kundeDto.setBReversecharge(kunde.getBReversecharge());
			kundeDto.setIKundennummer(kunde.getIKundennummer());
			kundeDto.setCErwerbsberechtigungsbegruendung(kunde.getCErwerbsberechtigungsbegruendung());
			kundeDto.setTErwerbsberechtigung(kunde.getTErwerbsberechtigung());
			kundeDto.setILieferdauer(kunde.getILieferdauer());
			kundeDto.setCIdExtern(kunde.getCIdExtern()) ;
			kundeDto.setBZollpapier(kunde.getBZollpapier());
		}
		return kundeDto;
	}

	public static KundeDto[] createDtos(Collection<?> kundes) {
		List<KundeDto> list = new ArrayList<KundeDto>();
		if (kundes != null) {
			Iterator<?> iterator = kundes.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Kunde) iterator.next()));
			}
		}
		KundeDto[] returnArray = new KundeDto[list.size()];
		return (KundeDto[]) list.toArray(returnArray);
	}
}
