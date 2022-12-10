package com.lp.server.rechnung.service;

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

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class AbrechnungsvorschlagUeberleitenDto implements Serializable {
	ArrayList<ZeileFuerAbrechnungsvorschlagUeberleitungDto> zeilenUeberzuleiten = new ArrayList<ZeileFuerAbrechnungsvorschlagUeberleitungDto>();

	public void addZeile(ZeileFuerAbrechnungsvorschlagUeberleitungDto zeile) {
		zeilenUeberzuleiten.add(zeile);
	}

	public Map<String, ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto> getZeilenVerdichtetNachArtikelOderAuftragsposition() {

		Map<String, ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto> m = new LinkedHashMap<String, ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto>();

		for (int i = 0; i < zeilenUeberzuleiten.size(); i++) {

			ZeileFuerAbrechnungsvorschlagUeberleitungDto zeileTemp = zeilenUeberzuleiten.get(i);

			Map<Integer, Map<Integer, DauerUndZeitraumDto>> mGetrenntNachArtikel = zeileTemp
					.getGetrenntNachArtikelnAusVerrechnungsmodell();

			Iterator itArtikel = mGetrenntNachArtikel.keySet().iterator();

			while (itArtikel.hasNext()) {

				Integer artikelIId=(Integer)itArtikel.next();
				
				Map<Integer, DauerUndZeitraumDto> mAufpos = mGetrenntNachArtikel.get(artikelIId);

				Iterator itAufpos = mAufpos.keySet().iterator();

				while (itAufpos.hasNext()) {

					Integer auftragspositionIIdIId = (Integer) itAufpos.next();

					DauerUndZeitraumDto dauerUndZeitraumDto = mAufpos.get(auftragspositionIIdIId);
					
					BigDecimal bdOffen = dauerUndZeitraumDto.getBdDauer();

					ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto zeileFuerClientVeridchtetNachArtikelOderAuftragsposition = null;
					
					
					String key=artikelIId+"";
					
					if(!dauerUndZeitraumDto.bVerdichtetNachArtikel) {
						key=artikelIId+"|"+auftragspositionIIdIId;
					}
					
					if (m.containsKey(key)) {
						zeileFuerClientVeridchtetNachArtikelOderAuftragsposition = m.get(key);
						zeileFuerClientVeridchtetNachArtikelOderAuftragsposition.add2Offen(bdOffen);

						if(dauerUndZeitraumDto.getMZeitraeume()!=null) {
							
							Iterator itM=dauerUndZeitraumDto.getMZeitraeume().keySet().iterator();
							while(itM.hasNext()) {
								Integer zeitdatenIId=(Integer)itM.next();
								
								ArrayList<ZeitVonBisUndDauer> alZeitraeume=dauerUndZeitraumDto.getAlZeitraeume(zeitdatenIId);
								
								for(int h=0;h<alZeitraeume.size();h++) {
									ZeitVonBisUndDauer temp=alZeitraeume.get(h);
									zeileFuerClientVeridchtetNachArtikelOderAuftragsposition.getDauerUndZeitraumDto().add2Zeitraeume(zeitdatenIId,temp.tVon, temp.tBis,temp.bdStunden);
									zeileFuerClientVeridchtetNachArtikelOderAuftragsposition.getDauerUndZeitraumDto().add2Dauer(bdOffen);
								}
							}
							
							
							
							
						}
						zeileFuerClientVeridchtetNachArtikelOderAuftragsposition.add2BetroffenAV(zeileTemp.getAvDto());

					} else {
						zeileFuerClientVeridchtetNachArtikelOderAuftragsposition = new ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto(artikelIId,
								auftragspositionIIdIId, bdOffen, zeileTemp.getAvDto(),dauerUndZeitraumDto);
					}

					m.put(key, zeileFuerClientVeridchtetNachArtikelOderAuftragsposition);
				}

			}

		}

		return m;

	}

	public void bereitsAbgerechneteStundenAbziehen(Integer artikelIId, Integer auftragspositionIId,
			BigDecimal bdBereitsAbgerechnet) {

		for (int i = 0; i < zeilenUeberzuleiten.size(); i++) {
			ZeileFuerAbrechnungsvorschlagUeberleitungDto zeile = zeilenUeberzuleiten.get(i);
			Map<Integer, Map<Integer, DauerUndZeitraumDto>> mArtikel = zeile.getGetrenntNachArtikelnAusVerrechnungsmodell();
			if (mArtikel.containsKey(artikelIId)) {

				Map<Integer, DauerUndZeitraumDto> mGetrenntNachAuftragspositionen = mArtikel.get(artikelIId);

				if (mGetrenntNachAuftragspositionen.containsKey(auftragspositionIId)) {

					DauerUndZeitraumDto dauerUndZeitraumDto=mGetrenntNachAuftragspositionen.get(auftragspositionIId);
					
					BigDecimal bdOffen = dauerUndZeitraumDto.getBdDauer();

					if (bdBereitsAbgerechnet.doubleValue() >= bdOffen.doubleValue()) {
						mGetrenntNachAuftragspositionen.remove(auftragspositionIId);
						bdBereitsAbgerechnet = bdBereitsAbgerechnet.subtract(bdOffen);
					} else {
						bdOffen = bdOffen.subtract(bdBereitsAbgerechnet);
						
						dauerUndZeitraumDto.setBdDauer(bdOffen);
						
						mGetrenntNachAuftragspositionen.put(auftragspositionIId, dauerUndZeitraumDto);
						mArtikel.put(artikelIId, mGetrenntNachAuftragspositionen);
						return;
					}
				}

			}

		}

	}

}
