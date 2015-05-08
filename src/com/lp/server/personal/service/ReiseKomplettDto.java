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
package com.lp.server.personal.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.TreeMap;

import com.lp.util.Helper;

public class ReiseKomplettDto implements Serializable {

	private TreeMap<java.sql.Timestamp, ReiseDto> tmReiseBeginn = new TreeMap<java.sql.Timestamp, ReiseDto>();

	public ReiseDto getReiseEnde() {
		return reiseEnde;
	}

	public void setReiseEnde(ReiseDto reiseEnde) {
		this.reiseEnde = reiseEnde;
	}

	public TreeMap<java.sql.Timestamp, ReiseDto> getTmReiseBeginn() {
		return tmReiseBeginn;
	}

	private ReiseDto reiseEnde;

	public void addBeginn(ReiseDto reiseDto) {
		tmReiseBeginn.put(reiseDto.getTZeit(), reiseDto);
	}

	public BigDecimal getAnteiligeKostenEinesAbschnitts(Integer reiseIId,
			BigDecimal bdkmKosten) {
		BigDecimal bdKostenAnteilig = null;

		if (bdkmKosten != null) {

			if (tmReiseBeginn.size() == 1) {
				BigDecimal bdkmKostenInklSpesen=bdkmKosten;
				if(getReiseEnde().getNSpesen()!=null){
					bdkmKostenInklSpesen=bdkmKostenInklSpesen.add(getReiseEnde().getNSpesen());
				}
				return bdkmKostenInklSpesen;
			} else if (tmReiseBeginn.size() > 1) {

				int iAnzahlKeinFaktorAngegeben = 0;
				double dFaktorGesamt = 0;
				Iterator it = tmReiseBeginn.keySet().iterator();
				while (it.hasNext()) {
					ReiseDto rDto = (ReiseDto) tmReiseBeginn.get(it.next());
					if (rDto.getFFaktor() == null) {
						iAnzahlKeinFaktorAngegeben++;
					} else {
						dFaktorGesamt = dFaktorGesamt + rDto.getFFaktor();
					}
				}

				double dFaktorFuerReisenOhneFaktor = 0;
				if (dFaktorGesamt < 100 && iAnzahlKeinFaktorAngegeben > 0) {
					dFaktorFuerReisenOhneFaktor = (100 - dFaktorGesamt)
							/ iAnzahlKeinFaktorAngegeben;
				}

				it = tmReiseBeginn.keySet().iterator();
				while (it.hasNext()) {
					ReiseDto rDto = (ReiseDto) tmReiseBeginn.get(it.next());

					if (rDto.getIId().equals(reiseIId)) {

						BigDecimal bdkmKostenInklSpesen=bdkmKosten;
						if(getReiseEnde().getNSpesen()!=null){
							bdkmKostenInklSpesen=bdkmKostenInklSpesen.add(getReiseEnde().getNSpesen());
						}
						
						if (rDto.getFFaktor() == null) {
							return Helper
									.getProzentWert(bdkmKostenInklSpesen, new BigDecimal(
											dFaktorFuerReisenOhneFaktor), 4);
						} else {
							return Helper.getProzentWert(bdkmKostenInklSpesen,
									new BigDecimal(rDto.getFFaktor()), 4);
						}

					}

				}

			}

		} else {
			return null;
		}

		return bdKostenAnteilig;
	}

}
