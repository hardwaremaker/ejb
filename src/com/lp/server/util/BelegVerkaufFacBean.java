/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.util;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerleihDto;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.rechnung.ejb.Rechnungposition;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.ejb.Mwstsatz;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.MwstsatzReportDto;
import com.lp.service.BelegVerkaufDto;
import com.lp.service.BelegpositionDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class BelegVerkaufFacBean extends Facade implements BelegVerkaufFac {
	@PersistenceContext
	private EntityManager em;

	private BigDecimal berechneRabattBetrag(Double rabattSatzInProzent,
			BigDecimal wert) {
		BigDecimal bdAllgemeinerRabattSatz = new BigDecimal(rabattSatzInProzent)
				.movePointLeft(2);
		bdAllgemeinerRabattSatz = Helper.rundeKaufmaennisch(
				bdAllgemeinerRabattSatz, 4);
		BigDecimal bdAllgemeinerRabattSumme = wert
				.multiply(bdAllgemeinerRabattSatz);
		bdAllgemeinerRabattSumme = Helper.rundeKaufmaennisch(
				bdAllgemeinerRabattSumme, FinanzFac.NACHKOMMASTELLEN);
		return bdAllgemeinerRabattSumme;
	}

	private int getIndexOfBelegPosition(Integer positionIId,
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos) {
		int i = 0;
		while (i < belegpositionVerkaufDtos.length
				&& !positionIId.equals(belegpositionVerkaufDtos[i].getIId()))
			++i;
		return i >= belegpositionVerkaufDtos.length ? -1 : i;
	}

	private BigDecimal berechneNettoZwischenSumme(
			BelegVerkaufDto belegVerkaufDto,
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			Integer vonPositionIId, Integer bisPositionIId) {
		if (vonPositionIId == null)
			return BigDecimal.ZERO;
		if (bisPositionIId == null)
			return BigDecimal.ZERO;

		int beginIndex = getIndexOfBelegPosition(vonPositionIId,
				belegpositionVerkaufDtos);
		if (-1 == beginIndex) {
			return BigDecimal.ZERO;
		}
		int endIndex = getIndexOfBelegPosition(bisPositionIId,
				belegpositionVerkaufDtos);
		if (-1 == endIndex) {
			return BigDecimal.ZERO;
		}

		return getNettoGesamtWert(belegpositionVerkaufDtos, belegVerkaufDto,
				beginIndex, endIndex);
	}

	private BigDecimal berechneZwischensumme(BelegVerkaufDto belegVerkaufDto,
			BelegpositionVerkaufDto thePosition,
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos) {
		BigDecimal wert = berechneNettoZwischenSumme(belegVerkaufDto,
				belegpositionVerkaufDtos, thePosition.getZwsVonPosition(),
				thePosition.getZwsBisPosition());
		BigDecimal rabattSumme = berechneRabattBetrag(
				thePosition.getFRabattsatz(), wert);
		BigDecimal minusRabattSumme = BigDecimal.ZERO.subtract(rabattSumme);
		thePosition.setNReportEinzelpreis(minusRabattSumme);
		thePosition
				.setNReportEinzelpreisplusversteckteraufschlag(minusRabattSumme);

		thePosition.setNEinzelpreis(minusRabattSumme);
		thePosition.setNEinzelpreisplusversteckteraufschlag(minusRabattSumme);
		thePosition.setNNettoeinzelpreis(minusRabattSumme);
		thePosition
				.setNNettoeinzelpreisplusversteckteraufschlag(minusRabattSumme);
		thePosition
				.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(minusRabattSumme);
		thePosition.setZwsNettoSumme(wert);
		thePosition.setNMenge(BigDecimal.ONE);

		return rabattSumme;
	}

	private BigDecimal getNettoGesamtWert(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, int vonIndex, int bisIndex) {
		BigDecimal gesamtWert = BigDecimal.ZERO;
		for (int i = vonIndex; i <= bisIndex; i++) {
			BelegpositionVerkaufDto belegpositionVerkaufDto = belegpositionVerkaufDtos[i];
			if (LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME
					.equals(belegpositionVerkaufDtos[i].getPositionsartCNr())) {
				gesamtWert = gesamtWert.subtract(berechneZwischensumme(
						belegVerkaufDto, belegpositionVerkaufDtos[i],
						belegpositionVerkaufDtos));
			} else {
				if (!LocaleFac.POSITIONSART_POSITION
						.equals(belegpositionVerkaufDtos[i]
								.getPositionsartCNr())
						&& belegpositionVerkaufDtos[i].getNMenge() != null) {
					gesamtWert = gesamtWert
							.add(getZeilenSummeNettoeinzelpreisPlusversteckteraufschlag(
									belegpositionVerkaufDto, belegVerkaufDto, 2));
				}
			}
		}

		return gesamtWert;
	}

	public BigDecimal getNettoGesamtWert(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, Integer iNachkommastellenPreis) {
		return getNettoGesamtWert(belegpositionVerkaufDtos, belegVerkaufDto, 0,
				belegpositionVerkaufDtos.length - 1);
		// BigDecimal gesamtWert = new BigDecimal(0);
		// for (int i = 0; i < belegpositionVerkaufDtos.length; i++) {
		// BelegpositionVerkaufDto belegpositionVerkaufDto =
		// belegpositionVerkaufDtos[i];
		// if (!belegpositionVerkaufDto.getPositionsartCNr().equals(
		// LocaleFac.POSITIONSART_POSITION))
		// if (belegpositionVerkaufDto.getNMenge() != null) {
		// gesamtWert = gesamtWert
		// .add(getZeilenSummeNettoeinzelpreisPlusversteckteraufschlag(
		// belegpositionVerkaufDto, belegVerkaufDto,
		// 2));
		// }
		// }
		// return gesamtWert;
	}

	public BigDecimal getNettoGesamtwertinBelegwaehrung(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto) {
		int iNachkommastellenPreis = getUINachkommastellenPreisVK(belegVerkaufDto
				.getMandantCNr());
		Double dAllgemeinerRabattsatz = belegVerkaufDto
				.getFAllgemeinerRabattsatz();
		Double dFProjektierungsrabattsatz = belegVerkaufDto
				.getFProjektierungsrabattsatz();

		BigDecimal belegWert = new BigDecimal(0);
		belegWert = getNettoGesamtWert(belegpositionVerkaufDtos,
				belegVerkaufDto, iNachkommastellenPreis);

		// - Allgemeiner Rabatt
		if (dAllgemeinerRabattsatz != null) {
			BigDecimal bdAllgemeinerRabattSatz = new BigDecimal(
					dAllgemeinerRabattsatz.doubleValue()).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdAllgemeinerRabattSatz = Helper.rundeKaufmaennisch(
					bdAllgemeinerRabattSatz, 4);
			BigDecimal bdAllgemeinerRabattSumme = belegWert
					.multiply(bdAllgemeinerRabattSatz);
			bdAllgemeinerRabattSumme = Helper.rundeKaufmaennisch(
					bdAllgemeinerRabattSumme, iNachkommastellenPreis);
			belegWert = belegWert.subtract(bdAllgemeinerRabattSumme);
		}

		// - Projektierungsrabatt
		if (dFProjektierungsrabattsatz != null) {
			BigDecimal bdProjektierungsRabatt = new BigDecimal(
					dFProjektierungsrabattsatz.doubleValue()).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdProjektierungsRabatt = Helper.rundeKaufmaennisch(
					bdProjektierungsRabatt, 4);
			BigDecimal bdNettogesamtpreisProjektierungsrabattSumme = belegWert
					.multiply(bdProjektierungsRabatt);
			bdNettogesamtpreisProjektierungsrabattSumme = Helper
					.rundeKaufmaennisch(
							bdNettogesamtpreisProjektierungsrabattSumme,
							iNachkommastellenPreis);
			belegWert = belegWert
					.subtract(bdNettogesamtpreisProjektierungsrabattSumme);
		}

		belegWert = Helper.rundeKaufmaennisch(belegWert, 2);
		return belegWert;
	}

	public BigDecimal getNettoGesamtwertinBelegwaehrungUST(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, TheClientDto theClientDto) {
		int iNachkommastellenPreis = getUINachkommastellenPreisVK(belegVerkaufDto
				.getMandantCNr());

		BigDecimal belegWertUST = new BigDecimal(0);
		belegWertUST = getNettoGesamtWertUST(belegpositionVerkaufDtos,
				belegVerkaufDto, iNachkommastellenPreis, theClientDto);

		belegWertUST = Helper.rundeKaufmaennisch(belegWertUST,
				iNachkommastellenPreis);
		return belegWertUST;
	}

	public BigDecimal getGesamtwertinBelegwaehrung(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto) {
		BigDecimal belegWertinBelegwaehrung = getNettoGesamtwertinBelegwaehrung(
				belegpositionVerkaufDtos, belegVerkaufDto);
		return belegWertinBelegwaehrung;
	}

	public BigDecimal getGesamtwertinMandantwaehrung(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto) {
		int iNachkommastellenPreis = getUINachkommastellenPreisVK(belegVerkaufDto
				.getMandantCNr());
		Double dWechselkursmandantwaehrungzubelegwaehrung = belegVerkaufDto
				.getFWechselkursmandantwaehrungzubelegwaehrung();
		BigDecimal belegWertinBelegwaehrung = getNettoGesamtwertinBelegwaehrung(
				belegpositionVerkaufDtos, belegVerkaufDto);

		if (dWechselkursmandantwaehrungzubelegwaehrung != null
				&& dWechselkursmandantwaehrungzubelegwaehrung != 0) {
			BigDecimal bdWechselkurs = Helper.getKehrwert(new BigDecimal(
					dWechselkursmandantwaehrungzubelegwaehrung.doubleValue()));
			belegWertinBelegwaehrung = belegWertinBelegwaehrung
					.multiply(bdWechselkurs);
		}

		belegWertinBelegwaehrung = Helper.rundeKaufmaennisch(
				belegWertinBelegwaehrung, iNachkommastellenPreis);
		return belegWertinBelegwaehrung;
	}

	public BigDecimal getGesamtwertInBelegswaehrungUST(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, TheClientDto theClientDto) {
		BigDecimal belegWertinBelegwaehrungUST = getNettoGesamtwertinBelegwaehrungUST(
				belegpositionVerkaufDtos, belegVerkaufDto, theClientDto);
		return belegWertinBelegwaehrungUST;
	}

	public BigDecimal getGesamtwertInMandantwaehrungUST(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, TheClientDto theClientDto) {
		int iNachkommastellenPreis = getUINachkommastellenPreisVK(belegVerkaufDto
				.getMandantCNr());
		Double dWechselkursmandantwaehrungzubelegwaehrung = belegVerkaufDto
				.getFWechselkursmandantwaehrungzubelegwaehrung();
		BigDecimal belegWertinBelegwaehrungUST = getNettoGesamtwertinBelegwaehrungUST(
				belegpositionVerkaufDtos, belegVerkaufDto, theClientDto);
		if (dWechselkursmandantwaehrungzubelegwaehrung != null
				&& dWechselkursmandantwaehrungzubelegwaehrung != 0) {
			BigDecimal bdWechselkurs = Helper.getKehrwert(new BigDecimal(
					dWechselkursmandantwaehrungzubelegwaehrung.doubleValue()));
			belegWertinBelegwaehrungUST = belegWertinBelegwaehrungUST
					.multiply(bdWechselkurs);
		}
		belegWertinBelegwaehrungUST = Helper.rundeKaufmaennisch(
				belegWertinBelegwaehrungUST, iNachkommastellenPreis);
		return belegWertinBelegwaehrungUST;
	}

	public BelegpositionVerkaufDto berechneBelegpositionVerkauf(
			BelegpositionVerkaufDto belegpositionVerkaufDto,
			BelegVerkaufDto belegVerkaufDto) {
		Integer iNachkommastellenPreis = getUINachkommastellenPreisVK(belegVerkaufDto
				.getMandantCNr());
		// Einzelpreisplusversteckteraufschlag
		belegpositionVerkaufDto
				.setNEinzelpreisplusversteckteraufschlag(getBdNEinzelpreisplusversteckteraufschlag(
						belegpositionVerkaufDto,
						belegVerkaufDto.getFVersteckterAufschlag(),
						iNachkommastellenPreis));
		// Nettoeinzelpreisplusversteckteraufschlag (minus positions rabatte)
		belegpositionVerkaufDto
				.setNNettoeinzelpreisplusversteckteraufschlag(getBdNNettoeinzelpreisplusversteckteraufschlag(
						belegpositionVerkaufDto,
						belegVerkaufDto.getFVersteckterAufschlag(),
						iNachkommastellenPreis));
		belegpositionVerkaufDto
				.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(getBdNEinzelpreisplusversteckteraufschlagminusrabatte(
						belegpositionVerkaufDto,
						belegVerkaufDto.getFAllgemeinerRabattsatz(),
						belegVerkaufDto.getFProjektierungsrabattsatz(),
						iNachkommastellenPreis));

		return belegpositionVerkaufDto;
	}

	public BelegpositionVerkaufDto berechnePauschalposition(
			BelegpositionVerkaufDto belegpositionVerkaufDto,
			BelegVerkaufDto belegVerkaufDto, BigDecimal neuWert,
			BigDecimal altWert) {
		if (neuWert != null) {
			int iNachkommastellenPreis = 4;
			altWert = Helper
					.rundeKaufmaennisch(altWert, iNachkommastellenPreis);
			Double dAllgemeinerRabattsatz = belegVerkaufDto
					.getFAllgemeinerRabattsatz();
			Double dFProjektierungsrabattsatz = belegVerkaufDto
					.getFProjektierungsrabattsatz();
			Double dVersteckterAufschlag = belegVerkaufDto
					.getFVersteckterAufschlag();

			// preis * 100
			BigDecimal bdx = belegpositionVerkaufDto.getNNettoeinzelpreis()
					.movePointRight(2);
			// (preis * 100)/ altgesamtwert
			BigDecimal bdy = bdx.divide(altWert, BigDecimal.ROUND_HALF_EVEN);
			bdy = Helper.rundeKaufmaennisch(bdy, iNachkommastellenPreis);
			// ((preis * 100)/ altgesamtwert)* neugesamtwert
			BigDecimal bdPreisNeu = neuWert.multiply(bdy);
			bdPreisNeu = Helper.rundeKaufmaennisch(bdPreisNeu,
					iNachkommastellenPreis);
			// (((preis * 100)/ altgesamtwert)* neugesamtwert)/100
			bdPreisNeu = bdPreisNeu.movePointLeft(2);
			bdPreisNeu = Helper.rundeKaufmaennisch(bdPreisNeu, 2);

			belegpositionVerkaufDto.setNNettoeinzelpreis(bdPreisNeu);
			belegpositionVerkaufDto.setBNettopreisuebersteuert(Helper
					.boolean2Short(true));

			belegpositionVerkaufDto
					.setNNettoeinzelpreisplusversteckteraufschlag(getBdNNettoeinzelpreisplusversteckteraufschlag(
							belegpositionVerkaufDto, dVersteckterAufschlag,
							iNachkommastellenPreis));

			belegpositionVerkaufDto
					.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(getBdNEinzelpreisplusversteckteraufschlagminusrabatte(
							belegpositionVerkaufDto, dAllgemeinerRabattsatz,
							dFProjektierungsrabattsatz, iNachkommastellenPreis));
		}
		return belegpositionVerkaufDto;
	}

	// Nettoeinzelpreisplusversteckteraufschlag
	public BigDecimal getBdNNettoeinzelpreisplusversteckteraufschlag(
			BelegpositionVerkaufDto belegpositionVerkaufDto,
			Double dVersteckterAufschlag, Integer iNachkommastellenPreis) {
		BigDecimal bdNettoeinzelpreis = belegpositionVerkaufDto
				.getNNettoeinzelpreis();
		BigDecimal bdNNettoeinzelpreisplusversteckteraufschlag = null;
		if (Helper.short2boolean(belegpositionVerkaufDto
				.getBNettopreisuebersteuert())) {
			bdNNettoeinzelpreisplusversteckteraufschlag = bdNettoeinzelpreis;
			if (dVersteckterAufschlag != 0) {
				BigDecimal bdVersteckterAufschlag = new BigDecimal(
						dVersteckterAufschlag.doubleValue()).movePointLeft(2);
				// Rabattsatz auf 4 Nachkommastellen gerundet, um den
				// Double-Fehler auszugleichen
				bdVersteckterAufschlag = Helper.rundeKaufmaennisch(
						bdVersteckterAufschlag, 4);
				BigDecimal bdNettoeinzelpreisVersteckterAufschlagSumme = bdNettoeinzelpreis
						.multiply(bdVersteckterAufschlag);
				bdNNettoeinzelpreisplusversteckteraufschlag = bdNNettoeinzelpreisplusversteckteraufschlag
						.add(bdNettoeinzelpreisVersteckterAufschlagSumme);
			}
			bdNNettoeinzelpreisplusversteckteraufschlag = Helper
					.rundeKaufmaennisch(
							bdNNettoeinzelpreisplusversteckteraufschlag,
							iNachkommastellenPreis);
		} else {
			bdNNettoeinzelpreisplusversteckteraufschlag = getNNettoeinzelpreisplusversteckteraufschlag(
					belegpositionVerkaufDto, iNachkommastellenPreis);
		}
		return bdNNettoeinzelpreisplusversteckteraufschlag;
	}

	public BigDecimal getNNettoeinzelpreisplusversteckteraufschlag(
			BelegpositionVerkaufDto belegpositionVerkaufDto,
			Integer iNachkommastellenPreis) {

		Double rabattsatz = belegpositionVerkaufDto.getFRabattsatz();
		Double zusatzrabattsatz = belegpositionVerkaufDto
				.getFZusatzrabattsatz();

		BigDecimal bdNNettoeinzelpreisplusversteckteraufschlag = belegpositionVerkaufDto
				.getNEinzelpreisplusversteckteraufschlag();

		if (rabattsatz != null && rabattsatz != 0) {
			BigDecimal nRabattsatz = new BigDecimal(rabattsatz.doubleValue())
					.movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			nRabattsatz = Helper.rundeKaufmaennisch(nRabattsatz, 4);
			BigDecimal nRabattsumme = bdNNettoeinzelpreisplusversteckteraufschlag
					.multiply(nRabattsatz);
			nRabattsumme = Helper.rundeKaufmaennisch(nRabattsumme,
					iNachkommastellenPreis);
			// Einzelpreis minus rabattsumme
			bdNNettoeinzelpreisplusversteckteraufschlag = bdNNettoeinzelpreisplusversteckteraufschlag
					.subtract(nRabattsumme);
		}
		if (zusatzrabattsatz != null && zusatzrabattsatz != 0) {
			BigDecimal nZusatzRabattsatz = new BigDecimal(
					zusatzrabattsatz.doubleValue()).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			nZusatzRabattsatz = Helper.rundeKaufmaennisch(nZusatzRabattsatz, 4);
			BigDecimal nZusatzRabattsumme = bdNNettoeinzelpreisplusversteckteraufschlag
					.multiply(nZusatzRabattsatz);
			nZusatzRabattsumme = Helper.rundeKaufmaennisch(nZusatzRabattsumme,
					iNachkommastellenPreis);
			// Einzelpreis minus rabattsumme minus zusatzrabattsumme
			bdNNettoeinzelpreisplusversteckteraufschlag = bdNNettoeinzelpreisplusversteckteraufschlag
					.subtract(nZusatzRabattsumme);
		}

		if (belegpositionVerkaufDto.getNMaterialzuschlag() != null) {
			bdNNettoeinzelpreisplusversteckteraufschlag = bdNNettoeinzelpreisplusversteckteraufschlag
					.add(belegpositionVerkaufDto.getNMaterialzuschlag());
		}

		bdNNettoeinzelpreisplusversteckteraufschlag = Helper
				.rundeKaufmaennisch(
						bdNNettoeinzelpreisplusversteckteraufschlag,
						iNachkommastellenPreis);
		/*
		 * 2009-08-05wh/ad/vf/hk Wir rechnen hier den exakten Netto-Einzelpreis
		 * auf die eingestellten Nachkommastellen aus Da hier ja die vom
		 * Anwender definierte Genauigkeit verwendet wird, m&uuml;ssen wir genau
		 * diesen gerundeten Wert (2 oder 4 NKs) verwenden und erst danach mit
		 * der Menge multiplizieren. Beispiel: Diesel kostet 0,937, 100L
		 * kosten also 93,70 und nicht 94,- !!
		 */
		return bdNNettoeinzelpreisplusversteckteraufschlag;
	}

	// Einzelpreisplusversteckteraufschlag
	public BigDecimal getBdNEinzelpreisplusversteckteraufschlag(
			BelegpositionVerkaufDto belegpositionVerkaufDto,
			Double dVersteckterAufschlag, Integer iNachkommastellenPreis) {
		BigDecimal bdEinzelpreis = belegpositionVerkaufDto.getNEinzelpreis();
		BigDecimal bdNNettoeinzelpreisplusversteckteraufschlag = bdEinzelpreis;
		if (dVersteckterAufschlag != 0) {
			BigDecimal bdVersteckterAufschlag = new BigDecimal(
					dVersteckterAufschlag.doubleValue()).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdVersteckterAufschlag = Helper.rundeKaufmaennisch(
					bdVersteckterAufschlag, 4);
			BigDecimal bdNettoeinzelpreisVersteckterAufschlagSumme = bdEinzelpreis
					.multiply(bdVersteckterAufschlag);
			bdNNettoeinzelpreisplusversteckteraufschlag = bdNNettoeinzelpreisplusversteckteraufschlag
					.add(bdNettoeinzelpreisVersteckterAufschlagSumme);
		}
		bdNNettoeinzelpreisplusversteckteraufschlag = Helper
				.rundeKaufmaennisch(
						bdNNettoeinzelpreisplusversteckteraufschlag,
						iNachkommastellenPreis);
		return bdNNettoeinzelpreisplusversteckteraufschlag;
	}

	public BigDecimal getBdNEinzelpreisplusversteckteraufschlagminusrabatte(
			BelegpositionVerkaufDto belegpositionVerkaufDto,
			Double dAllgemeinerRabattsatz, Double dFProjektierungsrabattsatz,
			Integer iNachkommastellenPreis) {
		BigDecimal bdNettoeinzelpreis = belegpositionVerkaufDto
				.getNNettoeinzelpreisplusversteckteraufschlag();
		BigDecimal bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte = bdNettoeinzelpreis;
		// - Allgemeiner Rabatt
		if (dAllgemeinerRabattsatz != null && dAllgemeinerRabattsatz != 0.0) {
			BigDecimal bdAllgemeinerRabattSatz = new BigDecimal(
					dAllgemeinerRabattsatz.doubleValue()).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdAllgemeinerRabattSatz = Helper.rundeKaufmaennisch(
					bdAllgemeinerRabattSatz, 4);
			BigDecimal bdAllgemeinerRabattSumme = bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte
					.multiply(bdAllgemeinerRabattSatz);
			bdAllgemeinerRabattSumme = Helper.rundeKaufmaennisch(
					bdAllgemeinerRabattSumme, iNachkommastellenPreis);
			bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte = bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte
					.subtract(bdAllgemeinerRabattSumme);
		}
		// - Projektierungsrabatt
		if (dFProjektierungsrabattsatz != null
				&& dFProjektierungsrabattsatz != 0.0) {
			BigDecimal bdProjektierungsRabatt = new BigDecimal(
					dFProjektierungsrabattsatz.doubleValue()).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdProjektierungsRabatt = Helper.rundeKaufmaennisch(
					bdProjektierungsRabatt, 4);
			BigDecimal bdNettogesamtpreisProjektierungsrabattSumme = bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte
					.multiply(bdProjektierungsRabatt);
			bdNettogesamtpreisProjektierungsrabattSumme = Helper
					.rundeKaufmaennisch(
							bdNettogesamtpreisProjektierungsrabattSumme,
							iNachkommastellenPreis);
			bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte = bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte
					.subtract(bdNettogesamtpreisProjektierungsrabattSumme);
		}
		bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte = Helper
				.rundeKaufmaennisch(
						bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte,
						iNachkommastellenPreis);
		return bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte;
	}

	public BigDecimal getBdBruttoeinzelpreis(
			BelegpositionVerkaufDto belegpositionVerkaufDto,
			Integer iNachkommastellenPreis, TheClientDto theClientDto) {
		MwstsatzDto mwstDto = null;
		try {
			mwstDto = getMandantFac().mwstsatzFindByPrimaryKey(
					belegpositionVerkaufDto.getMwstsatzIId(), theClientDto);
		} catch (EJBExceptionLP e) {
		} catch (RemoteException e) {
		}
		BigDecimal bdNettoeinzelpreis = belegpositionVerkaufDto
				.getNNettoeinzelpreisplusversteckteraufschlag();
		BigDecimal bdBruttoeinzelpreis = bdNettoeinzelpreis;
		if (mwstDto.getFMwstsatz() != 0) {
			BigDecimal bdMwst = new BigDecimal(mwstDto.getFMwstsatz()
					.doubleValue()).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdMwst = Helper.rundeKaufmaennisch(bdMwst, 4);
			BigDecimal bdMwstSumme = bdBruttoeinzelpreis.multiply(bdMwst);
			bdBruttoeinzelpreis = bdBruttoeinzelpreis.add(bdMwstSumme);
		}
		bdBruttoeinzelpreis = Helper.rundeKaufmaennisch(bdBruttoeinzelpreis,
				iNachkommastellenPreis);
		return bdBruttoeinzelpreis;

	}

	public BigDecimal getZeilenSummeNettoeinzelpreisPlusversteckteraufschlag(
			BelegpositionVerkaufDto belegpositionVerkaufDto,
			BelegVerkaufDto belegVerkaufDto, Integer iNachkommastellenPreis) {

		if (belegpositionVerkaufDto.getPositioniIdArtikelset() == null) {
			BigDecimal bdNNettoeinzelpreisplusversteckteraufschlag = belegpositionVerkaufDto
					.getNNettoeinzelpreisplusversteckteraufschlag();

			BigDecimal nZeilenSumme = belegpositionVerkaufDto.getNMenge()
					.multiply(bdNNettoeinzelpreisplusversteckteraufschlag);

			// Verleihtage
			if (belegpositionVerkaufDto.getVerleihIId() != null) {
				VerleihDto verleihDto = getArtikelFac()
						.verleihFindByPrimaryKey(

						belegpositionVerkaufDto.getVerleihIId());

				nZeilenSumme = nZeilenSumme.multiply(new BigDecimal(verleihDto
						.getFFaktor()));
			}

			nZeilenSumme = Helper.rundeKaufmaennisch(nZeilenSumme,
					iNachkommastellenPreis);
			System.out.println("Zeilensumme " + nZeilenSumme);
			return nZeilenSumme;
		} else {
			// Wenn Setartikel, dann preis auslassen
			return new BigDecimal(0);
		}

	}

	public BigDecimal getZeilenSumme(BigDecimal menge, BigDecimal preis,
			Integer iNachkommastellenPreis) {
		menge = Helper.rundeKaufmaennisch(menge, iNachkommastellenPreis);
		preis = Helper.rundeKaufmaennisch(preis, iNachkommastellenPreis);
		BigDecimal nZeilenSumme = menge.multiply(preis);
		nZeilenSumme = Helper.rundeKaufmaennisch(nZeilenSumme,
				iNachkommastellenPreis);
		return nZeilenSumme;
	}

	public BelegpositionVerkaufDto getBelegpositionVerkaufReport(
			BelegpositionVerkaufDto belegpositionVerkaufDto,
			BelegVerkaufDto belegVerkaufDto, Integer iNachkommastellenPreis) {
		// Einzelpreis
		BigDecimal bdEinzelpreis = belegpositionVerkaufDto.getNEinzelpreis();
		// if(Helper.short2boolean(belegpositionVerkaufDto.getBNettopreisuebersteuert()))
		// bdEinzelpreis = belegpositionVerkaufDto.getNNettoeinzelpreis();
		bdEinzelpreis = Helper.rundeKaufmaennisch(bdEinzelpreis,
				iNachkommastellenPreis);
		belegpositionVerkaufDto.setNReportEinzelpreis(bdEinzelpreis);

		// Einzelpreisplusversteckteraufschlag
		BigDecimal bdEinzelpreisplusversteckteraufschlag = belegpositionVerkaufDto
				.getNEinzelpreisplusversteckteraufschlag();
		// if(Helper.short2boolean(belegpositionVerkaufDto.getBNettopreisuebersteuert()))
		// bdEinzelpreisplusversteckteraufschlag =
		// belegpositionVerkaufDto.getNNettoeinzelpreisplusversteckteraufschlag();
		bdEinzelpreisplusversteckteraufschlag = Helper.rundeKaufmaennisch(
				bdEinzelpreisplusversteckteraufschlag, iNachkommastellenPreis);
		belegpositionVerkaufDto
				.setNReportEinzelpreisplusversteckteraufschlag(bdEinzelpreisplusversteckteraufschlag);

		// Nettoeinzelpreisplusversteckteraufschlag
		BigDecimal bdNettoeinzelpreisplusversteckteraufschlag = belegpositionVerkaufDto
				.getNNettoeinzelpreisplusversteckteraufschlag();

		bdNettoeinzelpreisplusversteckteraufschlag = Helper.rundeKaufmaennisch(
				bdNettoeinzelpreisplusversteckteraufschlag,
				iNachkommastellenPreis);
		belegpositionVerkaufDto
				.setNReportNettoeinzelpreisplusversteckteraufschlag(bdNettoeinzelpreisplusversteckteraufschlag);

		// Gesamtpreis
		BigDecimal bdGesamtpreis = belegpositionVerkaufDto
				.getNMenge()
				.multiply(
						belegpositionVerkaufDto
								.getNNettoeinzelpreisplusversteckteraufschlag());

		// Verleihtage
		BigDecimal bdVerleihfaktor = new BigDecimal(1);
		if (belegpositionVerkaufDto.getVerleihIId() != null) {
			VerleihDto verleihDto = getArtikelFac().verleihFindByPrimaryKey(

			belegpositionVerkaufDto.getVerleihIId());

			bdGesamtpreis = bdGesamtpreis.multiply(new BigDecimal(verleihDto
					.getFFaktor()));
			bdVerleihfaktor = new BigDecimal(verleihDto.getFFaktor());
		}

		bdGesamtpreis = Helper.rundeKaufmaennisch(bdGesamtpreis, 2);
		belegpositionVerkaufDto.setNReportGesamtpreis(bdGesamtpreis);

		Double dRabattsatz = 0.0;
		if (belegpositionVerkaufDto.getFRabattsatz() != null) {
			dRabattsatz = belegpositionVerkaufDto.getFRabattsatz();
		}
		belegpositionVerkaufDto.setDReportRabattsatz(dRabattsatz);

		Double dZusatzrabattsatz = 0.0;
		if (belegpositionVerkaufDto.getFZusatzrabattsatz() != null) {
			dZusatzrabattsatz = belegpositionVerkaufDto.getFZusatzrabattsatz();
		}
		belegpositionVerkaufDto.setDReportZusatzrabattsatz(dZusatzrabattsatz);

		// Mwstsatz
		Mwstsatz mwst = em.find(Mwstsatz.class,
				belegpositionVerkaufDto.getMwstsatzIId());
		belegpositionVerkaufDto.setDReportMwstsatz(mwst.getFMwstsatz());

		BigDecimal ust = belegpositionVerkaufDto
				.getNNettoeinzelpreisplusversteckteraufschlag().multiply(
						new BigDecimal(mwst.getFMwstsatz()).movePointLeft(2));
		ust = ust.multiply(belegpositionVerkaufDto.getNMenge().multiply(
				bdVerleihfaktor));

		// TODO: ghp, vorerst deaktiviert 28.02.2012
		// in den nachfolgenden Routinen wurde mit diesem gerundetem Wert
		// gerechnet.
		// Vorerst wird die mwst noch gefuellt, aber eigentlich ist dieser Wert
		// nur zur informationszwecken!
		// ust = Helper.rundeKaufmaennisch(ust, iNachkommastellenPreis);

		belegpositionVerkaufDto.setNReportMwstsatzbetrag(ust);

		return belegpositionVerkaufDto;
	}

	public BigDecimal getNettoGesamtWertUST(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, Integer iNachkommastellenPreis,
			TheClientDto theClientDto) {
		Double dAllgemeinerRabattsatz = belegVerkaufDto
				.getFAllgemeinerRabattsatz();
		Double dFProjektierungsrabattsatz = belegVerkaufDto
				.getFProjektierungsrabattsatz();

		BigDecimal gesamtWertUST = new BigDecimal(0);
		LinkedHashMap<Integer, MwstsatzReportDto> mwstMap = getMwstSumme(
				belegpositionVerkaufDtos, belegVerkaufDto, theClientDto);
		for (Iterator<?> iter = mwstMap.keySet().iterator(); iter.hasNext();) {
			Integer key = (Integer) iter.next();
			MwstsatzReportDto item = (MwstsatzReportDto) mwstMap.get(key);
			BigDecimal nSummeMwst = item.getNSummeMwstbetrag();

			// - Allgemeiner Rabatt
			if (dAllgemeinerRabattsatz != null) {
				BigDecimal bdAllgemeinerRabattSatz = new BigDecimal(
						dAllgemeinerRabattsatz.doubleValue()).movePointLeft(2);
				// Rabattsatz auf 4 Nachkommastellen gerundet, um den
				// Double-Fehler auszugleichen
				bdAllgemeinerRabattSatz = Helper.rundeKaufmaennisch(
						bdAllgemeinerRabattSatz, 4);
				BigDecimal bdAllgemeinerRabattSumme = nSummeMwst
						.multiply(bdAllgemeinerRabattSatz);
				bdAllgemeinerRabattSumme = Helper.rundeKaufmaennisch(
						bdAllgemeinerRabattSumme, iNachkommastellenPreis);
				nSummeMwst = nSummeMwst.subtract(bdAllgemeinerRabattSumme);
			}
			// - Projektierungsrabatt
			if (dFProjektierungsrabattsatz != null) {
				BigDecimal bdProjektierungsRabatt = new BigDecimal(
						dFProjektierungsrabattsatz.doubleValue())
						.movePointLeft(2);
				// Rabattsatz auf 4 Nachkommastellen gerundet, um den
				// Double-Fehler auszugleichen
				bdProjektierungsRabatt = Helper.rundeKaufmaennisch(
						bdProjektierungsRabatt, 4);
				BigDecimal bdNettogesamtpreisProjektierungsrabattSumme = nSummeMwst
						.multiply(bdProjektierungsRabatt);
				bdNettogesamtpreisProjektierungsrabattSumme = Helper
						.rundeKaufmaennisch(
								bdNettogesamtpreisProjektierungsrabattSumme,
								iNachkommastellenPreis);
				nSummeMwst = nSummeMwst
						.subtract(bdNettogesamtpreisProjektierungsrabattSumme);
			}
			Double fMwstsatz = new Double(0);
			Mwstsatz mwst = em.find(Mwstsatz.class, key);
			if (mwst != null) {
				fMwstsatz = mwst.getFMwstsatz();
			}
			BigDecimal bdMwstsatz = new BigDecimal(fMwstsatz).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdMwstsatz = Helper.rundeKaufmaennisch(bdMwstsatz, 4);

			// Achtung: Wenn diese Zeile geaendert wird, dann auch die
			// entsprechende Zeile in
			// MwstsatzReportDto.getNSummePositionsbetragMinusRabatte
			// (Helper.rundekaufmaennisch) anpassen!
			nSummeMwst = Helper.rundeKaufmaennisch(nSummeMwst, 2);

			BigDecimal ust = nSummeMwst.multiply(bdMwstsatz);
			ust = Helper.rundeKaufmaennisch(ust, 2);
			System.out.println("fMwstsatz " + fMwstsatz + " " + ust);
			gesamtWertUST = gesamtWertUST.add(ust);
		}
		return gesamtWertUST;
	}

	protected boolean isCalculateablePosition(
			BelegpositionVerkaufDto belegpositionVerkaufDto) {
		String positionsartCnr = belegpositionVerkaufDto.getPositionsartCNr();
		if (LocaleFac.POSITIONSART_IDENT.equals(positionsartCnr))
			return true;
		if (LocaleFac.POSITIONSART_HANDEINGABE.equals(positionsartCnr))
			return true;
		if (LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME
				.equals(positionsartCnr))
			return true;

		return false;
	}

	public LinkedHashMap<Integer, MwstsatzReportDto> getMwstSumme(
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, TheClientDto theClientDto) {
		Integer iNachkommastellenPreis = getUINachkommastellenPreisVK(belegVerkaufDto
				.getMandantCNr());
		LinkedHashMap<Integer, MwstsatzReportDto> mwstMap = new LinkedHashMap<Integer, MwstsatzReportDto>();
		Set<?> mwstSatzKeys;
		try {
			mwstSatzKeys = getMandantFac().mwstsatzIIdFindAllByMandant(
					belegVerkaufDto.getMandantCNr(), theClientDto);
			for (Iterator<?> iter = mwstSatzKeys.iterator(); iter.hasNext();) {
				Integer item = (Integer) iter.next();
				MwstsatzReportDto mwstsatzReportDto = new MwstsatzReportDto();
				mwstMap.put(item, mwstsatzReportDto);
			}
			Integer mwstsatzIId;
			for (int i = 0; i < belegpositionVerkaufDtos.length; i++) {
				if (isCalculateablePosition(belegpositionVerkaufDtos[i])) {
					// if
					// (belegpositionVerkaufDtos[i].getPositionsartCNr().equals(
					// LocaleFac.POSITIONSART_IDENT)
					// || belegpositionVerkaufDtos[i].getPositionsartCNr()
					// .equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
					mwstsatzIId = belegpositionVerkaufDtos[i].getMwstsatzIId();
					BigDecimal bdZeilenSumme = Helper.rundeKaufmaennisch(getZeilenSummeNettoeinzelpreisPlusversteckteraufschlag(
							belegpositionVerkaufDtos[i], belegVerkaufDto,
							iNachkommastellenPreis),2);
					MwstsatzReportDto rep = mwstMap.get(mwstsatzIId);
					rep.setNSummeMwstbetrag(rep.getNSummeMwstbetrag().add(
							bdZeilenSumme));
					System.out.println(" nSummeMwst " + bdZeilenSumme);
				}

			}
		} catch (EJBExceptionLP e) {
		} catch (RemoteException e) {
		}
		return mwstMap;
	}

	public Object[] getMwstTabelle_Orig(
			LinkedHashMap<Integer, MwstsatzReportDto> mwstMap,
			BelegVerkaufDto belegVerkaufDto, Locale locDruck,
			TheClientDto theClientDto) {

		// das Andrucken der gesammelten Mwstinformationen steuern
		StringBuffer sbMwstsatz = new StringBuffer();
		StringBuffer sbSummePositionsbetrag = new StringBuffer();
		StringBuffer sbWaehrung = new StringBuffer();
		StringBuffer sbSummeMwstbetrag = new StringBuffer();

		// UW 03.03.06: Die folgenden Informationen erscheinen am Druck, mit
		// diesen
		// Werten wird intern nicht mehr weitergerechnet -> daher auf 2
		// Stellen runden
		BigDecimal nEendbetragMitMwst = Helper.rundeKaufmaennisch(
				belegVerkaufDto.getNGesamtwertinbelegwaehrung(), 2);

		boolean bHatMwstWerte = false;

		for (Iterator<Integer> iter = mwstMap.keySet().iterator(); iter
				.hasNext();) {
			Integer key = (Integer) iter.next(); // IId des Mwstsatzes
			MwstsatzReportDto mwstsatzReportDto = (MwstsatzReportDto) mwstMap
					.get(key); // Summen der Mwstbetraege
			if (mwstsatzReportDto != null
					&& mwstsatzReportDto.getNSummeMwstbetrag().doubleValue() != 0.0) {
				Mwstsatz mwst = em.find(Mwstsatz.class, key);
				// MR: FIX, statt festverdrahtetem UST verwende
				// Localeabhaengigen Wert lp.ust
				sbMwstsatz.append(getTextRespectUISpr("lp.ust",
						theClientDto.getMandant(), locDruck));
				sbMwstsatz.append(": ");
				sbMwstsatz.append(Helper.formatZahl(mwst.getFMwstsatz(), 2,
						locDruck));
				sbMwstsatz.append(" % ");
				sbMwstsatz.append(
						getTextRespectUISpr("lp.ustvon",
								theClientDto.getMandant(), locDruck)).append(
						" ");
				// Fix Ende
				BigDecimal nPositionMwstbetrag = getMwstbetrag(
						mwstsatzReportDto.getNSummePositionsbetrag(),
						belegVerkaufDto);
				sbSummePositionsbetrag.append(Helper.formatZahl(
						nPositionMwstbetrag, 2, locDruck));
				sbWaehrung.append(belegVerkaufDto.getWaehrungCNr());
				BigDecimal nSummeMwstbetrag = getMwstbetrag(
						mwstsatzReportDto.getNSummeMwstbetrag(),
						belegVerkaufDto);
				sbSummeMwstbetrag.append(Helper.formatZahl(nSummeMwstbetrag, 2,
						locDruck));

				sbMwstsatz.append("\n");
				sbSummePositionsbetrag.append("\n");
				sbWaehrung.append("\n");
				sbSummeMwstbetrag.append("\n");
				if (nEendbetragMitMwst != null)
					nEendbetragMitMwst = nEendbetragMitMwst
							.add(nSummeMwstbetrag);

				bHatMwstWerte = true;
			}
		}

		if (bHatMwstWerte) {
			// die letzten \n wieder loeschen
			sbMwstsatz.delete(sbMwstsatz.length() - 1, sbMwstsatz.length());
			sbSummePositionsbetrag.delete(sbSummePositionsbetrag.length() - 1,
					sbSummePositionsbetrag.length());
			sbWaehrung.delete(sbWaehrung.length() - 1, sbWaehrung.length());
			sbSummeMwstbetrag.delete(sbSummeMwstbetrag.length() - 1,
					sbSummeMwstbetrag.length());
		}

		String P_MWST_TABELLE_LINKS = sbMwstsatz.toString();
		String P_MWST_TABELLE_SUMME_POSITIONEN = sbSummePositionsbetrag
				.toString();
		String P_MWST_TABELLE_WAEHRUNG = sbWaehrung.toString();
		String P_MWST_TABELLE_RECHTS = sbSummeMwstbetrag.toString();
		// BigDecimal P_ENDBETRAGMITMWST = nEendbetragMitMwst;
		BigDecimal P_ENDBETRAGMITMWST = Helper.rundeKaufmaennisch(
				nEendbetragMitMwst, 2);
		Object[] sr = new Object[] { P_MWST_TABELLE_LINKS,
				P_MWST_TABELLE_SUMME_POSITIONEN, P_MWST_TABELLE_WAEHRUNG,
				P_MWST_TABELLE_RECHTS, P_ENDBETRAGMITMWST };
		return sr;
	}

	public Object[] getMwstTabelle(
			LinkedHashMap<Integer, MwstsatzReportDto> mwstMap,
			BelegVerkaufDto belegVerkaufDto, Locale locDruck,
			TheClientDto theClientDto) {

		// das Andrucken der gesammelten Mwstinformationen steuern
		StringBuffer sbMwstsatz = new StringBuffer();
		StringBuffer sbSummePositionsbetrag = new StringBuffer();
		StringBuffer sbWaehrung = new StringBuffer();
		StringBuffer sbSummeMwstbetrag = new StringBuffer();

		// UW 03.03.06: Die folgenden Informationen erscheinen am Druck, mit
		// diesen
		// Werten wird intern nicht mehr weitergerechnet -> daher auf 2
		// Stellen runden
		BigDecimal nEendbetragMitMwst = Helper.rundeKaufmaennisch(
				belegVerkaufDto.getNGesamtwertinbelegwaehrung(), 2);

		boolean bHatMwstWerte = false;

		for (Iterator<Integer> iter = mwstMap.keySet().iterator(); iter
				.hasNext();) {
			Integer key = (Integer) iter.next(); // IId des Mwstsatzes
			MwstsatzReportDto mwstsatzReportDto = (MwstsatzReportDto) mwstMap
					.get(key); // Summen der Mwstbetraege
			if (mwstsatzReportDto != null
					&& mwstsatzReportDto.getNSummeMwstbetrag().doubleValue() != 0.0) {
				Mwstsatz mwst = em.find(Mwstsatz.class, key);
				// MR: FIX, statt festverdrahtetem UST verwende
				// Localeabhaengigen Wert lp.ust
				sbMwstsatz.append(getTextRespectUISpr("lp.ust",
						theClientDto.getMandant(), locDruck));
				sbMwstsatz.append(": ");
				sbMwstsatz.append(Helper.formatZahl(mwst.getFMwstsatz(), 2,
						locDruck));
				sbMwstsatz.append(" % ");
				sbMwstsatz.append(
						getTextRespectUISpr("lp.ustvon",
								theClientDto.getMandant(), locDruck)).append(
						" ");
				// Fix Ende
				BigDecimal nPositionMwstbetrag = mwstsatzReportDto
						.getNSummePositionsbetragMinusRabatte(
								belegVerkaufDto.getFAllgemeinerRabattsatz(),
								belegVerkaufDto.getFProjektierungsrabattsatz());
				sbSummePositionsbetrag.append(Helper.formatAndRoundCurrency(
						nPositionMwstbetrag, locDruck));

				sbWaehrung.append(belegVerkaufDto.getWaehrungCNr());

				BigDecimal nSummeMwstbetrag = mwstsatzReportDto
						.getNSummeMWSTbetragMinusRabatte(
								belegVerkaufDto.getFAllgemeinerRabattsatz(),
								belegVerkaufDto.getFProjektierungsrabattsatz(),
								mwst.getFMwstsatz());
				sbSummeMwstbetrag.append(Helper.formatAndRoundCurrency(
						nSummeMwstbetrag, locDruck));

				sbMwstsatz.append("\n");
				sbSummePositionsbetrag.append("\n");
				sbWaehrung.append("\n");
				sbSummeMwstbetrag.append("\n");
				if (nEendbetragMitMwst != null)
					nEendbetragMitMwst = nEendbetragMitMwst
							.add(nSummeMwstbetrag);

				bHatMwstWerte = true;
			}
		}

		if (bHatMwstWerte) {
			// die letzten \n wieder loeschen
			sbMwstsatz.delete(sbMwstsatz.length() - 1, sbMwstsatz.length());
			sbSummePositionsbetrag.delete(sbSummePositionsbetrag.length() - 1,
					sbSummePositionsbetrag.length());
			sbWaehrung.delete(sbWaehrung.length() - 1, sbWaehrung.length());
			sbSummeMwstbetrag.delete(sbSummeMwstbetrag.length() - 1,
					sbSummeMwstbetrag.length());
		}

		String P_MWST_TABELLE_LINKS = sbMwstsatz.toString();
		String P_MWST_TABELLE_SUMME_POSITIONEN = sbSummePositionsbetrag
				.toString();
		String P_MWST_TABELLE_WAEHRUNG = sbWaehrung.toString();
		String P_MWST_TABELLE_RECHTS = sbSummeMwstbetrag.toString();
		// BigDecimal P_ENDBETRAGMITMWST = nEendbetragMitMwst;
		BigDecimal P_ENDBETRAGMITMWST = Helper
				.rundeGeldbetrag(nEendbetragMitMwst);
		Object[] sr = new Object[] { P_MWST_TABELLE_LINKS,
				P_MWST_TABELLE_SUMME_POSITIONEN, P_MWST_TABELLE_WAEHRUNG,
				P_MWST_TABELLE_RECHTS, P_ENDBETRAGMITMWST };
		return sr;
	}

	public BigDecimal getMwstbetrag(BigDecimal mwstbetrag,
			BelegVerkaufDto belegVerkaufDto) {
		Double dAllgemeinerRabattsatz = belegVerkaufDto
				.getFAllgemeinerRabattsatz();
		Double dFProjektierungsrabattsatz = belegVerkaufDto
				.getFProjektierungsrabattsatz();

		BigDecimal nSummeMwst = mwstbetrag;

		// - Allgemeiner Rabatt
		if (dAllgemeinerRabattsatz != null && dAllgemeinerRabattsatz != 0.0) {
			BigDecimal bdAllgemeinerRabattSatz = new BigDecimal(
					dAllgemeinerRabattsatz.doubleValue()).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdAllgemeinerRabattSatz = Helper.rundeKaufmaennisch(
					bdAllgemeinerRabattSatz, 4);
			BigDecimal bdAllgemeinerRabattSumme = nSummeMwst
					.multiply(bdAllgemeinerRabattSatz);
			bdAllgemeinerRabattSumme = Helper.rundeKaufmaennisch(
					bdAllgemeinerRabattSumme, 2);
			nSummeMwst = nSummeMwst.subtract(bdAllgemeinerRabattSumme);
		}
		// - Projektierungsrabatt
		if (dFProjektierungsrabattsatz != null
				&& dFProjektierungsrabattsatz != 0.0) {
			BigDecimal bdProjektierungsRabatt = new BigDecimal(
					dFProjektierungsrabattsatz.doubleValue()).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdProjektierungsRabatt = Helper.rundeKaufmaennisch(
					bdProjektierungsRabatt, 4);
			BigDecimal bdNettogesamtpreisProjektierungsrabattSumme = nSummeMwst
					.multiply(bdProjektierungsRabatt);
			bdNettogesamtpreisProjektierungsrabattSumme = Helper
					.rundeKaufmaennisch(
							bdNettogesamtpreisProjektierungsrabattSumme, 2);
			nSummeMwst = nSummeMwst
					.subtract(bdNettogesamtpreisProjektierungsrabattSumme);
		}
		return nSummeMwst;
	}

	public BigDecimal berechneGesamtwertBelegProKundeProZeitintervall(
			Integer iIdKundeI, Date datVonI, Date datBisI, String belegartCNr)
			throws EJBExceptionLP {
		final String METHOD_NAME = "berechneGesamtwertBelegProKundeProZeitintervall";
		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iiKundeI == null"));
		}
		if (datVonI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("datVonI == null"));
		}
		if (datVonI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("datBisI == null"));
		}
		if (belegartCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("belegartCNr == null"));
		}

		BigDecimal bdWert = Helper.getBigDecimalNull();
		Query query = null;
		if (belegartCNr.equals(LocaleFac.BELEGART_ANGEBOT))
			query = em
					.createNamedQuery("getAngebotNettoWertByKundeBelegdatumVonBis");
		if (belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG))
			query = em
					.createNamedQuery("getAuftragNettoWertByKundeBelegdatumVonBis");
		if (belegartCNr.equals(LocaleFac.BELEGART_LIEFERSCHEIN))
			query = em
					.createNamedQuery("getLieferscheinNettoWertByKundeBelegdatumVonBis");
		if (belegartCNr.equals(LocaleFac.BELEGART_RECHNUNG))
			query = em
					.createNamedQuery("getRechunungNettoWertByKundeBelegdatumVonBis");
		if (belegartCNr.equals(LocaleFac.BELEGART_GUTSCHRIFT))
			query = em
					.createNamedQuery("getGutschriftNettoWertByKundeBelegdatumVonBis");
		query.setParameter(1, iIdKundeI);
		query.setParameter(2, datVonI);
		query.setParameter(3, datBisI);
		bdWert = (BigDecimal) query.getSingleResult();
		return bdWert;
	}

	public BigDecimal getWertPoisitionsartPosition(Integer iIdPositionI,
			String belegartCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		BigDecimal bdWert = new BigDecimal(0);
		Query query = null;
		if (belegartCNr.equals(LocaleFac.BELEGART_ANGEBOT))
			query = em.createNamedQuery("getWertAngebotpositionartPosition");
		if (belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG))
			query = em.createNamedQuery("getWertAuftragpositionartPosition");
		if (belegartCNr.equals(LocaleFac.BELEGART_LIEFERSCHEIN))
			query = em
					.createNamedQuery("getWertLieferscheinpositionartPosition");
		if (belegartCNr.equals(LocaleFac.BELEGART_RECHNUNG))
			query = em.createNamedQuery("getWertRechnungpositionartPosition");
		query.setParameter(1, iIdPositionI);
		query.setParameter(2, LocaleFac.POSITIONSART_IDENT);
		query.setParameter(3, LocaleFac.POSITIONSART_HANDEINGABE);
		bdWert = (BigDecimal) query.getSingleResult();
		return bdWert;
	}

	private BigDecimal totalSnrMenge(
			List<SeriennrChargennrMitMengeDto> knownSnrs) {
		BigDecimal knownMenge = BigDecimal.ZERO;
		for (SeriennrChargennrMitMengeDto seriennrChargennrMitMengeDto : knownSnrs) {
			knownMenge = knownMenge.add(seriennrChargennrMitMengeDto
					.getNMenge());
		}
		return knownMenge;
	}

	public void setupPositionWithIdentities(boolean zubuchen,
			BelegpositionDto rePosDto,
			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities,
			TheClientDto theClientDto) {
		ArtikelDto artikel = getArtikelFac().artikelFindByPrimaryKey(
				rePosDto.getArtikelIId(), theClientDto);
		if (!(artikel.isChargennrtragend() || artikel.isSeriennrtragend()))
			return;

		BigDecimal knownMenge = BigDecimal.ZERO;
		BigDecimal sollMenge = rePosDto.getNMenge().abs(); // negative
															// Lieferscheinmenge
															// (Gutschriften)
		boolean foundEntry = false;
		do {
			foundEntry = false;
			if (null == rePosDto.getSeriennrChargennrMitMenge()) {
				rePosDto.setSeriennrChargennrMitMenge(new ArrayList<SeriennrChargennrMitMengeDto>());
			}

			knownMenge = totalSnrMenge(rePosDto.getSeriennrChargennrMitMenge());
			if (knownMenge.compareTo(sollMenge) >= 0)
				break;

			for (SeriennrChargennrMitMengeDto snrMengeDto : notyetUsedIdentities) {
				Integer artikelIIdGefunden = zubuchen ? getLagerFac()
						.getArtikelIIdUeberSeriennummerAbgang(
								snrMengeDto.getCSeriennrChargennr(),
								theClientDto) : getLagerFac()
						.getArtikelIIdUeberSeriennummer(
								snrMengeDto.getCSeriennrChargennr(),
								theClientDto);

				if (!rePosDto.getArtikelIId().equals(artikelIIdGefunden))
					continue;

				if (knownMenge.add(snrMengeDto.getNMenge())
						.compareTo(sollMenge) <= 0) {
					rePosDto.getSeriennrChargennrMitMenge().add(snrMengeDto);
					notyetUsedIdentities.remove(snrMengeDto);
					foundEntry = true;
					break;
				}
			}
		} while (foundEntry && knownMenge.compareTo(sollMenge) < 0);

		knownMenge = totalSnrMenge(rePosDto.getSeriennrChargennrMitMenge());
		if (knownMenge.compareTo(sollMenge) < 0) {

			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_MENGE_UNGLEICH,
					new Exception(artikel.getCNr() + "("
							+ knownMenge.toString() + " von "
							+ rePosDto.getNMenge() + " vorhanden)"));

		}
	}

	/**
	 * Aus der Liste der eineindeutigen Seriennummern einige dieser Position
	 * zuordnen
	 * 
	 * @param rePosDto
	 *            ist die Belegposition, der Seriennummern zugeordnet werden
	 *            sollen, sofern es sich um eine seriennummerntragende Position
	 *            handelt
	 * @param notyetUsedIdentities
	 *            die Liste der noch verf&uuml;gbaren Seriennummern (mit deren Menge)
	 * @param theClientDto
	 */
	public void setupPositionWithIdentities(BelegpositionDto rePosDto,
			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities,
			TheClientDto theClientDto) {
		setupPositionWithIdentities(false, rePosDto, notyetUsedIdentities,
				theClientDto);
	}

	private int getBelegpositionIndex(Integer positionIId,
			BelegpositionVerkaufDto[] dtos) {
		for (int i = 0; i < dtos.length; i++) {
			if (positionIId.equals(dtos[i].getIId()))
				return i;
		}

		return -1;
	}

	private int findNextIntelligenteZwischensummePosition(int startIndex,
			BelegpositionVerkaufDto[] dtos) {
		for (int i = startIndex; i < dtos.length; i++) {
			if (LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME
					.equals(dtos[i].getPositionsartCNr())) {
				return i;
			}
		}

		return -1;
	}

	private int findNextPosition(int startIndex, int endIndex,
			BelegpositionVerkaufDto[] dtos) {
		PositionNumberHandler dtoNumberHandler = new PositionNumberHandler();

		while (startIndex <= dtos.length && (startIndex < endIndex)) {
			if (dtoNumberHandler
					.hasPositionNummer(new BelegPositionNumberDtoAdapter(
							dtos[startIndex]))) {
				return startIndex;
			}

			++startIndex;
		}

		return -1;
	}

	private int findPreviousPosition(int startIndex,
			BelegpositionVerkaufDto[] dtos) {
		PositionNumberHandler dtoNumberHandler = new PositionNumberHandler();
		while (startIndex >= 0) {
			if (dtoNumberHandler
					.hasPositionNummer(new BelegPositionNumberDtoAdapter(
							dtos[startIndex]))) {
				return startIndex;
			}

			--startIndex;
		}

		return -1;
	}

	private int findNextPossiblePosition(int startIndex, int endIndex,
			BelegpositionVerkaufDto[] dtos) {
		// Die Von-Position wird geloescht.
		int savedPositionIndex = startIndex;
		startIndex = findNextPosition(startIndex + 1, endIndex, dtos);
		if (-1 == startIndex) {
			startIndex = findPreviousPosition(savedPositionIndex - 1, dtos);
		}

		return startIndex;
	}

	private Class getEntityClassForBelegDto(BelegpositionVerkaufDto positionDto) {
		if (positionDto instanceof RechnungPositionDto)
			return Rechnungposition.class;
		if (positionDto instanceof AuftragpositionDto)
			return Auftragposition.class;
		return null;
	}

	private int reindexZwischensummenPositionRef(
			BelegpositionVerkaufDto belegPositionDto,
			BelegpositionVerkaufDto[] dtos, int positionIndex) {
		int zwsPosIndex = findNextIntelligenteZwischensummePosition(
				positionIndex, dtos);
		if (-1 == zwsPosIndex)
			return zwsPosIndex; // keine Zws vorhanden

		if (belegPositionDto.getIId().equals(
				dtos[zwsPosIndex].getZwsVonPosition())) {
			// Die Von-Position wird geloescht.
			int savedPositionIndex = positionIndex;
			positionIndex = findNextPossiblePosition(positionIndex,
					zwsPosIndex, dtos);
			if (-1 == positionIndex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_VON_KLEINER_BIS,
						dtos[savedPositionIndex].getIId().toString());
			}

			Class entityClass = getEntityClassForBelegDto(belegPositionDto);
			IZwsPosition rechnungPosition = (IZwsPosition) em.find(entityClass,
					dtos[zwsPosIndex].getIId());
			Integer oldVonPos = rechnungPosition.getZwsVonPosition();
			rechnungPosition.setZwsVonPosition(dtos[positionIndex].getIId());
			if (oldVonPos.equals(rechnungPosition.getZwsBisPosition())) {
				rechnungPosition
						.setZwsBisPosition(dtos[positionIndex].getIId());
			}
			em.merge(rechnungPosition);
			em.flush();
			return zwsPosIndex;
		}

		if (belegPositionDto.getIId().equals(
				dtos[zwsPosIndex].getZwsBisPosition())) {
			// Die Bis-Position wird geloescht
			int savedPositionIndex = positionIndex;
			positionIndex = findNextPossiblePosition(positionIndex,
					zwsPosIndex, dtos);

			if (-1 == positionIndex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_VON_KLEINER_BIS,
						dtos[savedPositionIndex].getIId().toString());
			}

			Class entityClass = getEntityClassForBelegDto(belegPositionDto);
			IZwsPosition rechnungPosition = (IZwsPosition) em.find(entityClass,
					dtos[zwsPosIndex].getIId());
			Integer oldBisPos = rechnungPosition.getZwsVonPosition();
			rechnungPosition.setZwsBisPosition(dtos[positionIndex].getIId());
			if (oldBisPos.equals(rechnungPosition.getZwsVonPosition())) {
				rechnungPosition
						.setZwsVonPosition(dtos[positionIndex].getIId());
			}
			em.merge(rechnungPosition);
			em.flush();
			return zwsPosIndex;
		}

		return zwsPosIndex;
	}

	public void processIntelligenteZwischensummeRemove(
			BelegVerkaufDto belegDto, BelegpositionVerkaufDto belegPositionDto,
			BelegpositionVerkaufDto[] belegPositionDtos) throws EJBExceptionLP {

		int positionIndex = getBelegpositionIndex(belegPositionDto.getIId(),
				belegPositionDtos);
		if (-1 == positionIndex)
			return;

		int zwsPosIndex = -1;
		while (-1 != (zwsPosIndex = reindexZwischensummenPositionRef(
				belegPositionDto, belegPositionDtos, positionIndex))) {
			positionIndex = zwsPosIndex + 1;
		}
	}

	private int getBelegPositionIndex(Integer positionIId,
			BelegpositionVerkaufDto[] dtos) {
		for (int i = 0; i < dtos.length; i++) {
			if (positionIId.equals(dtos[i].getIId()))
				return i;
		}

		return -1;
	}

	public boolean pruefeAufGleichenMwstSatz(
			BelegpositionVerkaufDto[] belegPositionDtos,
			Integer vonPositionNumber, Integer bisPositionNumber)
			throws EJBExceptionLP {
		if (null == belegPositionDtos || 0 == belegPositionDtos.length)
			return true;

		PositionNumberHandler numberHandler = new PositionNumberHandler();
		PositionNumberAdapter numberAdapter = new BelegPositionNumberDtoAdapter(
				belegPositionDtos);

		Integer vonPosIId = numberHandler.getPositionIIdFromPositionNummer(
				null, vonPositionNumber, numberAdapter);
		Integer bisPosIId = numberHandler.getPositionIIdFromPositionNummer(
				null, bisPositionNumber, numberAdapter);

		if (vonPosIId == null || bisPosIId == null)
			return true;

		int vonPosIndex = getBelegPositionIndex(vonPosIId, belegPositionDtos);
		int bisPosIndex = getBelegPositionIndex(bisPosIId, belegPositionDtos);

		if (vonPosIndex == -1 || bisPosIndex == -1)
			return true;

		Integer lastMwstsatzIId = null;
		for (int i = vonPosIndex; i <= bisPosIndex; i++) {
			Integer actualMwstsatzIId = belegPositionDtos[i].getMwstsatzIId();
			if (actualMwstsatzIId != null) {
				if (lastMwstsatzIId == null) {
					lastMwstsatzIId = actualMwstsatzIId;
				}

				if (!lastMwstsatzIId.equals(actualMwstsatzIId))
					return false;
			}
		}

		return true;
	}

}
