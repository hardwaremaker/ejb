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
package com.lp.server.util;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VerleihDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.ejb.Rechnungposition;
import com.lp.server.rechnung.ejbfac.CoinRoundingServiceFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.ejb.Mwstsatz;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
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

	@EJB
	private CoinRoundingServiceFac coinRoundingService;

	private BigDecimal berechneRabattBetrag(Double rabattSatzInProzent, BigDecimal wert) {
		BigDecimal bdAllgemeinerRabattSatz = new BigDecimal(rabattSatzInProzent).movePointLeft(2);
		bdAllgemeinerRabattSatz = Helper.rundeKaufmaennisch(bdAllgemeinerRabattSatz, 4);
		BigDecimal bdAllgemeinerRabattSumme = wert.multiply(bdAllgemeinerRabattSatz);
		bdAllgemeinerRabattSumme = Helper.rundeKaufmaennisch(bdAllgemeinerRabattSumme, FinanzFac.NACHKOMMASTELLEN);
		return bdAllgemeinerRabattSumme;
	}

	private int getIndexOfBelegPosition(Integer positionIId, BelegpositionVerkaufDto[] belegpositionVerkaufDtos) {
		int i = 0;
		while (i < belegpositionVerkaufDtos.length && !positionIId.equals(belegpositionVerkaufDtos[i].getIId()))
			++i;
		return i >= belegpositionVerkaufDtos.length ? -1 : i;
	}

	private int getIndexOfBelegPositionEnd(Integer positionIId, BelegpositionVerkaufDto[] belegpositionVerkaufDtos) {
		int i = belegpositionVerkaufDtos.length;
		while (--i >= 0) {
			if (positionIId.equals(belegpositionVerkaufDtos[i].getPositioniIdArtikelset())) {
				return i;
			}

			if (positionIId.equals(belegpositionVerkaufDtos[i].getIId())) {
				return i;
			}
		}
		return -1;
	}

	public ArrayList<BelegpositionVerkaufDto> getIntZwsPositions(BelegpositionVerkaufDto belegpositionVerkaufDtoZWS,
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos) {

		ArrayList<BelegpositionVerkaufDto> zwspos = new ArrayList<BelegpositionVerkaufDto>();

		int beginIndex = getIndexOfBelegPosition(belegpositionVerkaufDtoZWS.getZwsVonPosition(),
				belegpositionVerkaufDtos);

		int endIndex = getIndexOfBelegPositionEnd(belegpositionVerkaufDtoZWS.getZwsBisPosition(),
				belegpositionVerkaufDtos);

		if (beginIndex > -1 && endIndex > -1) {
			for (int i = beginIndex; i <= endIndex; i++) {
				BelegpositionVerkaufDto belegpositionVerkaufDto = belegpositionVerkaufDtos[i];
				if (!belegpositionVerkaufDtos[i].isIntelligenteZwischensumme() && belegpositionVerkaufDtos[i]
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte() != null) {
					zwspos.add(belegpositionVerkaufDto);
				}
			}
		}
		return zwspos;
	}

	private BigDecimal berechneNettoZwischenSumme(BelegVerkaufDto belegVerkaufDto,
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos, Integer vonPositionIId, Integer bisPositionIId) {
		if (vonPositionIId == null)
			return BigDecimal.ZERO;
		if (bisPositionIId == null)
			return BigDecimal.ZERO;

		int beginIndex = getIndexOfBelegPosition(vonPositionIId, belegpositionVerkaufDtos);
		if (-1 == beginIndex) {
			return BigDecimal.ZERO;
		}
		int endIndex = getIndexOfBelegPositionEnd(bisPositionIId, belegpositionVerkaufDtos);
		if (-1 == endIndex) {
			return BigDecimal.ZERO;
		}

		return getNettoGesamtWert(belegpositionVerkaufDtos, belegVerkaufDto, beginIndex, endIndex);
	}

	private BigDecimal berechneZwischensumme(BelegVerkaufDto belegVerkaufDto, BelegpositionVerkaufDto thePosition,
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos) {
		BigDecimal wert = berechneNettoZwischenSumme(belegVerkaufDto, belegpositionVerkaufDtos,
				thePosition.getZwsVonPosition(), thePosition.getZwsBisPosition());
		BigDecimal rabattSumme = berechneRabattBetrag(thePosition.getFRabattsatz(), wert);
		BigDecimal minusRabattSumme = BigDecimal.ZERO.subtract(rabattSumme);
		thePosition.setNReportEinzelpreis(minusRabattSumme);
		thePosition.setNReportEinzelpreisplusversteckteraufschlag(minusRabattSumme);

		thePosition.setNEinzelpreis(minusRabattSumme);
		thePosition.setNEinzelpreisplusversteckteraufschlag(minusRabattSumme);
		thePosition.setNNettoeinzelpreis(minusRabattSumme);
		thePosition.setNNettoeinzelpreisplusversteckteraufschlag(minusRabattSumme);
		thePosition.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(minusRabattSumme);
		thePosition.setZwsNettoSumme(wert);
		thePosition.setNMenge(BigDecimal.ONE);

		// int vonIndex =
		// getIndexOfBelegPosition(thePosition.getZwsVonPosition(),
		// belegpositionVerkaufDtos) ;
		// int bisIndex =

		// getIndexOfBelegPosition(thePosition.getZwsBisPosition(),
		// belegpositionVerkaufDtos) ;
		// if(vonIndex != -1 && bisIndex != -1) {
		// BigDecimal rabattSatz = new
		// BigDecimal(thePosition.getFRabattsatz()).movePointLeft(2);
		// rabattSatz = Helper.rundeKaufmaennisch(rabattSatz, 4);
		//
		// for(int index = vonIndex; index <= bisIndex; ++index) {
		// belegpositionVerkaufDtos[index].setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(
		// calculateIntZwsNettoEinzelpreis(belegpositionVerkaufDtos[index].getNNettoeinzelpreisplusversteckteraufschlagminusrabatte(),
		// rabattSatz));
		//
		// }
		// }
		//
		return rabattSumme;
	}

	/**
	 * &Uuml;berpr&uuml;fen, ob die gerade berechnete Zwischensumme(*) mit der zu
	 * pr&uuml;fenden Zws &uuml;berlappt, sofern der Rabattsatz der zur
	 * pr&uuml;fenden Zws <> 0 ist.</br>
	 * 
	 * <p>
	 * * diese wird durch myVonIndex und myBisIndex dargestellt
	 * </p>
	 * <p>
	 * Hintergrund: Hier in diesem Modul wird der Rabattbetrag der sich aus einer
	 * Zwischensumme etwas anders berechnet als die Berechnung, die f&uuml;r die
	 * Fibu notwendig ist. Es geht darum, dass im BelegVerkauf nur der Rabattbetrag
	 * notwendig ist (Summe der Einzelpositionen). F&uuml;r die Fibu - und auch die
	 * Lagerbewertung - wird der jeweilige Positionsbetrag um den zu gebenden Rabatt
	 * reduziert. Wenn sich nun Zwischensummen &uuml;berlappen bzw. nicht
	 * vollst&auml;ndig umschlie&szlig;en (aka die Endposition der beginnenden Zws
	 * gleich der Endposition der zu berechnenden Zws ist) wirkt der Unterschied der
	 * verschiedenen Herangehensweisen.
	 * </p>
	 * <p>
	 * Letztendlich wollen wir f&uuml;r rabattbehaftete Zwischensummen nur
	 * diejenigen, die sich vollst&auml;ndig umschliessen. Wenn die Zwischensummen
	 * nur zur Summenbildung dienen, also keinen Rabattsatz haben, wird die
	 * umschlie&szlig;ende Zwischensumme nicht gefordert.
	 * </p>
	 * 
	 * SP7993
	 * 
	 * @param verifyZwsPosition
	 * @param positionDtos
	 * @param myBeginnIndex
	 * @param myEndIndex
	 */
	private void validateZwsIsNotOverlapping(BelegpositionVerkaufDto verifyZwsPosition,
			BelegpositionVerkaufDto[] positionDtos, int myBeginnIndex, int myEndIndex) {
		myLogger.info("validate: forPosId " + verifyZwsPosition.getIId() + " " + verifyZwsPosition.getCBez()
				+ ", callerVonIndex [" + myBeginnIndex + "] = " + positionDtos[myBeginnIndex].getIId()
				+ ", callerBisIndex [" + myEndIndex + "] = " + positionDtos[myEndIndex].getIId());
		if (verifyZwsPosition.getFRabattsatz() == null)
			return;
		if (verifyZwsPosition.getZwsBisPosition() == null)
			return;

		BigDecimal rabattSatz = Helper
				.rundeKaufmaennisch(BigDecimal.valueOf(verifyZwsPosition.getFRabattsatz()).movePointLeft(2), 4);
		if (rabattSatz.signum() == 0)
			return;

		int endIndex = getIndexOfBelegPositionEnd(verifyZwsPosition.getZwsBisPosition(), positionDtos);
		if (endIndex != myEndIndex)
			return;

		throw EJBExcFactory.zwsPositionNichtUmschliessend(verifyZwsPosition, myBeginnIndex, myEndIndex);
	}

	private BigDecimal getNettoGesamtWert(BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, int vonIndex, int bisIndex) {
//		System.out.println("Berechne NettoGesamtWert (" + vonIndex + " bis " + bisIndex + ").");

		myLogger.info("Berechne nettoGesamtWert [" + vonIndex + "] bis [" + bisIndex + "] ...");
		BigDecimal gesamtWert = BigDecimal.ZERO;
		for (int i = vonIndex; i <= bisIndex; i++) {
			BelegpositionVerkaufDto belegpositionVerkaufDto = belegpositionVerkaufDtos[i];
			if (belegpositionVerkaufDtos[i].isIntelligenteZwischensumme()) {
				validateZwsIsNotOverlapping(belegpositionVerkaufDtos[i], belegpositionVerkaufDtos, vonIndex, bisIndex);
				BigDecimal zws = berechneZwischensumme(belegVerkaufDto, belegpositionVerkaufDtos[i],
						belegpositionVerkaufDtos);
				gesamtWert = gesamtWert.subtract(zws);

				myLogger.info("  abzüglich ZWS " + zws.toPlainString());
			} else {
				if (!LocaleFac.POSITIONSART_SEITENUMBRUCH.equals(belegpositionVerkaufDtos[i].getPositionsartCNr())
						&& belegpositionVerkaufDtos[i].isCalculateZws()) {
					gesamtWert = gesamtWert.add(getZeilenSummeNettoeinzelpreisPlusversteckteraufschlag(
							belegpositionVerkaufDto, belegVerkaufDto, 2));
				}
			}
		}

//		System.out.println("NettoGesamtWert (" + vonIndex + " bis " + bisIndex +
//				", " + belegpositionVerkaufDtos[vonIndex].getIId() + 
//				" - " + belegpositionVerkaufDtos[bisIndex].getIId() +  
//				") = " + gesamtWert.toPlainString());
		myLogger.info("Berechne nettoGesamtWert [" + vonIndex + "] bis [" + bisIndex + "] = "
				+ gesamtWert.toPlainString() + ".");
		return gesamtWert;
	}

	public BigDecimal getNettoGesamtWert(BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, Integer iNachkommastellenPreis) {
		return getNettoGesamtWert(belegpositionVerkaufDtos, belegVerkaufDto, 0, belegpositionVerkaufDtos.length - 1);
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

	public BigDecimal getNettoGesamtwertinBelegwaehrung(BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto) {
		int iNachkommastellenPreis = getUINachkommastellenPreisVK(belegVerkaufDto.getMandantCNr());
		Double dAllgemeinerRabattsatz = belegVerkaufDto.getFAllgemeinerRabattsatz();
		Double dFProjektierungsrabattsatz = belegVerkaufDto.getFProjektierungsrabattsatz();

		BigDecimal belegWert = getNettoGesamtWert(belegpositionVerkaufDtos, belegVerkaufDto, iNachkommastellenPreis);

		myLogger.info("Belegwert = " + belegWert.toPlainString());

		// - Allgemeiner Rabatt
		if (dAllgemeinerRabattsatz != null) {
			BigDecimal bdAllgemeinerRabattSatz = new BigDecimal(dAllgemeinerRabattsatz.doubleValue()).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdAllgemeinerRabattSatz = Helper.rundeKaufmaennisch(bdAllgemeinerRabattSatz, 4);
			BigDecimal bdAllgemeinerRabattSumme = belegWert.multiply(bdAllgemeinerRabattSatz);
			bdAllgemeinerRabattSumme = Helper.rundeKaufmaennisch(bdAllgemeinerRabattSumme, iNachkommastellenPreis);
			belegWert = belegWert.subtract(bdAllgemeinerRabattSumme);
		}

		// - Projektierungsrabatt
		if (dFProjektierungsrabattsatz != null) {
			BigDecimal bdProjektierungsRabatt = new BigDecimal(dFProjektierungsrabattsatz.doubleValue())
					.movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdProjektierungsRabatt = Helper.rundeKaufmaennisch(bdProjektierungsRabatt, 4);
			BigDecimal bdNettogesamtpreisProjektierungsrabattSumme = belegWert.multiply(bdProjektierungsRabatt);
			bdNettogesamtpreisProjektierungsrabattSumme = Helper
					.rundeKaufmaennisch(bdNettogesamtpreisProjektierungsrabattSumme, iNachkommastellenPreis);
			belegWert = belegWert.subtract(bdNettogesamtpreisProjektierungsrabattSumme);
		}

		belegWert = Helper.rundeKaufmaennisch(belegWert, 2);
		return belegWert;
	}

	public BigDecimal getNettoGesamtwertinBelegwaehrungUST(BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, TheClientDto theClientDto) {
		int iNachkommastellenPreis = getUINachkommastellenPreisVK(belegVerkaufDto.getMandantCNr());

		BigDecimal belegWertUST = getNettoGesamtWertUST(belegpositionVerkaufDtos, belegVerkaufDto,
				iNachkommastellenPreis, theClientDto);

		belegWertUST = Helper.rundeKaufmaennisch(belegWertUST, iNachkommastellenPreis);
		return belegWertUST;
	}

	public BelegpositionVerkaufDto erstelleVerpackungskostenpositionAnhandNettowert(Integer kundeIIdRechnungsadresse,
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos, BelegVerkaufDto belegVerkaufDto,
			BelegpositionVerkaufDto positionFuerRueckgabe, TheClientDto theClientDto) {

		if (belegpositionVerkaufDtos != null && belegpositionVerkaufDtos.length > 0) {
			int iNachkommastellenPreis = getUINachkommastellenPreisVK(belegVerkaufDto.getMandantCNr());
			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(kundeIIdRechnungsadresse, theClientDto);
			if (kdDto.getfVerpackungskostenInProzent() != null && kdDto.getfVerpackungskostenInProzent() > 0) {

				Integer artikelIId = getVerpackunskostenArtikel(theClientDto);

				if (artikelIId != null) {

					// PJ21885
					ArrayList<BelegpositionVerkaufDto> alBereinigt = new ArrayList<BelegpositionVerkaufDto>();

					for (int i = 0; i < belegpositionVerkaufDtos.length; i++) {

						if (belegpositionVerkaufDtos[i].getArtikelIId() != null) {
							ArtikelDto aDto = getArtikelFac()
									.artikelFindByPrimaryKey(belegpositionVerkaufDtos[i].getArtikelIId(), theClientDto);
							if (Helper.short2boolean(aDto.getBWerbeabgabepflichtig())) {
								continue;
							}
						}

						alBereinigt.add(belegpositionVerkaufDtos[i]);
					}

					belegpositionVerkaufDtos = (BelegpositionVerkaufDto[]) alBereinigt
							.toArray(new BelegpositionVerkaufDto[alBereinigt.size()]);

					ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
					BigDecimal belegWert = getNettoGesamtWert(belegpositionVerkaufDtos, belegVerkaufDto,
							iNachkommastellenPreis);

					if (belegWert.doubleValue() > 0) {

						BigDecimal verpackungskosten = Helper.rundeKaufmaennisch(
								belegWert.multiply(
										new BigDecimal(kdDto.getfVerpackungskostenInProzent()).movePointLeft(2)),
								iNachkommastellenPreis);

						positionFuerRueckgabe.setBelegIId(belegVerkaufDto.getIId());
						positionFuerRueckgabe.setArtikelIId(artikelIId);
						positionFuerRueckgabe.setPositionsartCNr(AngebotServiceFac.ANGEBOTPOSITIONART_IDENT);

						positionFuerRueckgabe.setNMenge(BigDecimal.ONE);
						positionFuerRueckgabe.setNNettoeinzelpreis(verpackungskosten);
						positionFuerRueckgabe.setNEinzelpreis(verpackungskosten);

						positionFuerRueckgabe.setFRabattsatz(0.0);
						positionFuerRueckgabe.setNRabattbetrag(BigDecimal.ZERO);
						positionFuerRueckgabe.setFZusatzrabattsatz(0.0);
						positionFuerRueckgabe.setBNettopreisuebersteuert(Helper.boolean2Short(false));
						positionFuerRueckgabe.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
						positionFuerRueckgabe.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));

						positionFuerRueckgabe.setEinheitCNr(aDto.getEinheitCNr());

						Integer mwstsatzBezId = null;
						try {
							ParametermandantDto parameterPosKont = getParameterFac().getMandantparameter(
									theClientDto.getMandant(), ParameterFac.KATEGORIE_KUNDEN,
									ParameterFac.PARAMETER_KUNDEN_POSITIONSKONTIERUNG);
							boolean isPositionskontierung = ((Boolean) parameterPosKont.getCWertAsObject())
									.booleanValue();

							mwstsatzBezId = aDto.getMwstsatzbezIId();
							if (!isPositionskontierung) {

								mwstsatzBezId = kdDto.getMwstsatzbezIId();
							}
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}
						MwstsatzDto mwstsatzDtoAktuell = getMandantFac().mwstsatzZuDatumValidate(mwstsatzBezId,
								belegVerkaufDto.getTBelegdatum(), theClientDto);

						BigDecimal mwstBetrag = new BigDecimal(0);

						if (mwstsatzDtoAktuell.getFMwstsatz().doubleValue() == 0) {
							positionFuerRueckgabe.setMwstsatzIId(mwstsatzDtoAktuell.getIId());
						} else {

							positionFuerRueckgabe.setMwstsatzIId(mwstsatzDtoAktuell.getIId());

							mwstBetrag = Helper.rundeKaufmaennisch(
									verpackungskosten.multiply(
											new BigDecimal(mwstsatzDtoAktuell.getFMwstsatz()).movePointLeft(2)),
									iNachkommastellenPreis);

						}

						positionFuerRueckgabe.setNBruttoeinzelpreis(verpackungskosten.add(mwstBetrag));
						positionFuerRueckgabe.setNMwstbetrag(mwstBetrag);

						return positionFuerRueckgabe;
					}

				} else {
					ArrayList al = new ArrayList();

					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_VERPACKUNGSKOSTENARTIKEL_NICHT_VORHANDEN, al,
							new Exception("VERPACKUNGSKOSTENARTIKEL_NICHT_VORHANDEN"));
				}

			}
		}
		return null;

	}

	public BigDecimal getGesamtwertinBelegwaehrung(BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto) {
		BigDecimal belegWertinBelegwaehrung = getNettoGesamtwertinBelegwaehrung(belegpositionVerkaufDtos,
				belegVerkaufDto);
		return belegWertinBelegwaehrung;
	}

	public BigDecimal getGesamtwertinMandantwaehrung(BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto) {
		int iNachkommastellenPreis = getUINachkommastellenPreisVK(belegVerkaufDto.getMandantCNr());
		Double dWechselkursmandantwaehrungzubelegwaehrung = belegVerkaufDto
				.getFWechselkursmandantwaehrungzubelegwaehrung();
		BigDecimal belegWertinBelegwaehrung = getNettoGesamtwertinBelegwaehrung(belegpositionVerkaufDtos,
				belegVerkaufDto);

		if (dWechselkursmandantwaehrungzubelegwaehrung != null && dWechselkursmandantwaehrungzubelegwaehrung != 0) {
			BigDecimal bdWechselkurs = Helper
					.getKehrwert(new BigDecimal(dWechselkursmandantwaehrungzubelegwaehrung.doubleValue()));
			belegWertinBelegwaehrung = belegWertinBelegwaehrung.multiply(bdWechselkurs);
		}

		belegWertinBelegwaehrung = Helper.rundeKaufmaennisch(belegWertinBelegwaehrung, iNachkommastellenPreis);
		return belegWertinBelegwaehrung;
	}

	public BigDecimal getGesamtwertInBelegswaehrungUST(BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, TheClientDto theClientDto) {
		BigDecimal belegWertinBelegwaehrungUST = getNettoGesamtwertinBelegwaehrungUST(belegpositionVerkaufDtos,
				belegVerkaufDto, theClientDto);
		return belegWertinBelegwaehrungUST;
	}

	public BigDecimal getGesamtwertInMandantwaehrungUST(BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, TheClientDto theClientDto) {
		int iNachkommastellenPreis = getUINachkommastellenPreisVK(belegVerkaufDto.getMandantCNr());
		Double dWechselkursmandantwaehrungzubelegwaehrung = belegVerkaufDto
				.getFWechselkursmandantwaehrungzubelegwaehrung();
		BigDecimal belegWertinBelegwaehrungUST = getNettoGesamtwertinBelegwaehrungUST(belegpositionVerkaufDtos,
				belegVerkaufDto, theClientDto);
		if (dWechselkursmandantwaehrungzubelegwaehrung != null && dWechselkursmandantwaehrungzubelegwaehrung != 0) {
			BigDecimal bdWechselkurs = Helper
					.getKehrwert(new BigDecimal(dWechselkursmandantwaehrungzubelegwaehrung.doubleValue()));
			belegWertinBelegwaehrungUST = belegWertinBelegwaehrungUST.multiply(bdWechselkurs);
		}
		belegWertinBelegwaehrungUST = Helper.rundeKaufmaennisch(belegWertinBelegwaehrungUST, iNachkommastellenPreis);
		return belegWertinBelegwaehrungUST;
	}

	public BelegpositionVerkaufDto berechneBelegpositionVerkauf(BelegpositionVerkaufDto belegpositionVerkaufDto,
			BelegVerkaufDto belegVerkaufDto) {
		Integer iNachkommastellenPreis = getUINachkommastellenPreisVK(belegVerkaufDto.getMandantCNr());
		// Einzelpreisplusversteckteraufschlag
		belegpositionVerkaufDto.setNEinzelpreisplusversteckteraufschlag(getBdNEinzelpreisplusversteckteraufschlag(
				belegpositionVerkaufDto, belegVerkaufDto.getFVersteckterAufschlag(), iNachkommastellenPreis));
		// Nettoeinzelpreisplusversteckteraufschlag (minus positions rabatte)
		belegpositionVerkaufDto.setNNettoeinzelpreisplusversteckteraufschlag(
				getBdNNettoeinzelpreisplusversteckteraufschlag(belegpositionVerkaufDto,
						belegVerkaufDto.getFVersteckterAufschlag(), iNachkommastellenPreis));
		belegpositionVerkaufDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(
				getBdNEinzelpreisplusversteckteraufschlagminusrabatte(belegpositionVerkaufDto,
						belegVerkaufDto.getFAllgemeinerRabattsatz(), belegVerkaufDto.getFProjektierungsrabattsatz(),
						iNachkommastellenPreis));

		return belegpositionVerkaufDto;
	}

	public BelegpositionVerkaufDto berechneBelegpositionVerkauf(BigDecimal zwsRabatt,
			BelegpositionVerkaufDto belegpositionVerkaufDto, BelegVerkaufDto belegVerkaufDto) {
		Integer iNachkommastellenPreis = getUINachkommastellenPreisVK(belegVerkaufDto.getMandantCNr());
		belegpositionVerkaufDto.setNEinzelpreisplusversteckteraufschlag(getBdNEinzelpreisplusversteckteraufschlag(
				belegpositionVerkaufDto, belegVerkaufDto.getFVersteckterAufschlag(), iNachkommastellenPreis));
		belegpositionVerkaufDto.setNNettoeinzelpreisplusversteckteraufschlag(
				getBdNNettoeinzelpreisplusversteckteraufschlag(belegpositionVerkaufDto,
						belegVerkaufDto.getFVersteckterAufschlag(), iNachkommastellenPreis));
		if (zwsRabatt != null && zwsRabatt.signum() != 0) {
			belegpositionVerkaufDto
					.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(calculateIntZwsNettoEinzelpreis(
							belegpositionVerkaufDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte(),
							zwsRabatt));
		}
		belegpositionVerkaufDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(
				getBdNEinzelpreisplusversteckteraufschlagminusrabatte(belegpositionVerkaufDto,
						belegVerkaufDto.getFAllgemeinerRabattsatz(), belegVerkaufDto.getFProjektierungsrabattsatz(),
						iNachkommastellenPreis));

		return belegpositionVerkaufDto;
	}

	private int getIndexFirstZwsPosition(BelegpositionVerkaufDto[] allePositionenDto) {
		for (int i = 0; i < allePositionenDto.length; i++) {
			if (allePositionenDto[i].isIntelligenteZwischensumme()) {
				return i;
			}
		}

		return -1;
	}

	public BelegpositionVerkaufDto berechneBelegpositionVerkauf(BelegpositionVerkaufDto belegpositionVerkaufDto,
			BelegVerkaufDto belegVerkaufDto, BelegpositionVerkaufDto[] allePositionenDto,
			Set<Integer> modifiedPositions) {
		int index = getIndexFirstZwsPosition(allePositionenDto);
		if (index != -1) {
			prepareIntZwsPositions(allePositionenDto, belegVerkaufDto);
			modifiedPositions.addAll(adaptIntZwsPositions(allePositionenDto));
		} else {
			berechneBelegpositionVerkauf(belegpositionVerkaufDto, belegVerkaufDto);
		}
		return belegpositionVerkaufDto;
	}

	public BelegpositionVerkaufDto berechnePauschalposition(BelegpositionVerkaufDto belegpositionVerkaufDto,
			BelegVerkaufDto belegVerkaufDto, BigDecimal neuWert, BigDecimal altWert) {
		if (neuWert != null) {
			int iNachkommastellenPreis = 4;
			altWert = Helper.rundeKaufmaennisch(altWert, iNachkommastellenPreis);
			Double dAllgemeinerRabattsatz = belegVerkaufDto.getFAllgemeinerRabattsatz();
			Double dFProjektierungsrabattsatz = belegVerkaufDto.getFProjektierungsrabattsatz();
			Double dVersteckterAufschlag = belegVerkaufDto.getFVersteckterAufschlag();

			// preis * 100
			BigDecimal bdx = belegpositionVerkaufDto.getNNettoeinzelpreis().movePointRight(2);
			// (preis * 100)/ altgesamtwert
			BigDecimal bdy = bdx.divide(altWert, BigDecimal.ROUND_HALF_EVEN);
			bdy = Helper.rundeKaufmaennisch(bdy, iNachkommastellenPreis);
			// ((preis * 100)/ altgesamtwert)* neugesamtwert
			BigDecimal bdPreisNeu = neuWert.multiply(bdy);
			bdPreisNeu = Helper.rundeKaufmaennisch(bdPreisNeu, iNachkommastellenPreis);
			// (((preis * 100)/ altgesamtwert)* neugesamtwert)/100
			bdPreisNeu = bdPreisNeu.movePointLeft(2);
			bdPreisNeu = Helper.rundeKaufmaennisch(bdPreisNeu, 2);

			belegpositionVerkaufDto.setNNettoeinzelpreis(bdPreisNeu);
			belegpositionVerkaufDto.setBNettopreisuebersteuert(Helper.boolean2Short(true));

			belegpositionVerkaufDto.setNNettoeinzelpreisplusversteckteraufschlag(
					getBdNNettoeinzelpreisplusversteckteraufschlag(belegpositionVerkaufDto, dVersteckterAufschlag,
							iNachkommastellenPreis));

			belegpositionVerkaufDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(
					getBdNEinzelpreisplusversteckteraufschlagminusrabatte(belegpositionVerkaufDto,
							dAllgemeinerRabattsatz, dFProjektierungsrabattsatz, iNachkommastellenPreis));
		}
		return belegpositionVerkaufDto;
	}

	// Nettoeinzelpreisplusversteckteraufschlag
	public BigDecimal getBdNNettoeinzelpreisplusversteckteraufschlag(BelegpositionVerkaufDto belegpositionVerkaufDto,
			Double dVersteckterAufschlag, Integer iNachkommastellenPreis) {
		BigDecimal bdNettoeinzelpreis = belegpositionVerkaufDto.getNNettoeinzelpreis();
		BigDecimal bdNNettoeinzelpreisplusversteckteraufschlag = null;
		if (Helper.short2boolean(belegpositionVerkaufDto.getBNettopreisuebersteuert())) {
			bdNNettoeinzelpreisplusversteckteraufschlag = bdNettoeinzelpreis;
			if (dVersteckterAufschlag != 0) {
				BigDecimal bdVersteckterAufschlag = new BigDecimal(dVersteckterAufschlag.doubleValue())
						.movePointLeft(2);
				// Rabattsatz auf 4 Nachkommastellen gerundet, um den
				// Double-Fehler auszugleichen
				bdVersteckterAufschlag = Helper.rundeKaufmaennisch(bdVersteckterAufschlag, 4);
				BigDecimal bdNettoeinzelpreisVersteckterAufschlagSumme = bdNettoeinzelpreis
						.multiply(bdVersteckterAufschlag);
				bdNNettoeinzelpreisplusversteckteraufschlag = bdNNettoeinzelpreisplusversteckteraufschlag
						.add(bdNettoeinzelpreisVersteckterAufschlagSumme);
			}
			bdNNettoeinzelpreisplusversteckteraufschlag = Helper
					.rundeKaufmaennisch(bdNNettoeinzelpreisplusversteckteraufschlag, iNachkommastellenPreis);
		} else {
			bdNNettoeinzelpreisplusversteckteraufschlag = getNNettoeinzelpreisplusversteckteraufschlag(
					belegpositionVerkaufDto, iNachkommastellenPreis);
		}
		return bdNNettoeinzelpreisplusversteckteraufschlag;
	}

	public BigDecimal getNNettoeinzelpreisplusversteckteraufschlag(BelegpositionVerkaufDto belegpositionVerkaufDto,
			Integer iNachkommastellenPreis) {

		Double rabattsatz = belegpositionVerkaufDto.getFRabattsatz();
		Double zusatzrabattsatz = belegpositionVerkaufDto.getFZusatzrabattsatz();

		BigDecimal bdNNettoeinzelpreisplusversteckteraufschlag = belegpositionVerkaufDto
				.getNEinzelpreisplusversteckteraufschlag();

		if (rabattsatz != null && rabattsatz != 0) {
			BigDecimal nRabattsatz = new BigDecimal(rabattsatz.doubleValue()).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			nRabattsatz = Helper.rundeKaufmaennisch(nRabattsatz, 4);
			BigDecimal nRabattsumme = bdNNettoeinzelpreisplusversteckteraufschlag.multiply(nRabattsatz);
			nRabattsumme = Helper.rundeKaufmaennisch(nRabattsumme, iNachkommastellenPreis);
			// Einzelpreis minus rabattsumme
			bdNNettoeinzelpreisplusversteckteraufschlag = bdNNettoeinzelpreisplusversteckteraufschlag
					.subtract(nRabattsumme);
		}
		if (zusatzrabattsatz != null && zusatzrabattsatz != 0) {
			BigDecimal nZusatzRabattsatz = new BigDecimal(zusatzrabattsatz.doubleValue()).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			nZusatzRabattsatz = Helper.rundeKaufmaennisch(nZusatzRabattsatz, 4);
			BigDecimal nZusatzRabattsumme = bdNNettoeinzelpreisplusversteckteraufschlag.multiply(nZusatzRabattsatz);
			nZusatzRabattsumme = Helper.rundeKaufmaennisch(nZusatzRabattsumme, iNachkommastellenPreis);
			// Einzelpreis minus rabattsumme minus zusatzrabattsumme
			bdNNettoeinzelpreisplusversteckteraufschlag = bdNNettoeinzelpreisplusversteckteraufschlag
					.subtract(nZusatzRabattsumme);
		}

		if (belegpositionVerkaufDto.getNMaterialzuschlag() != null) {
			bdNNettoeinzelpreisplusversteckteraufschlag = bdNNettoeinzelpreisplusversteckteraufschlag
					.add(belegpositionVerkaufDto.getNMaterialzuschlag());
		}

		bdNNettoeinzelpreisplusversteckteraufschlag = Helper
				.rundeKaufmaennisch(bdNNettoeinzelpreisplusversteckteraufschlag, iNachkommastellenPreis);
		/*
		 * 2009-08-05wh/ad/vf/hk Wir rechnen hier den exakten Netto-Einzelpreis auf die
		 * eingestellten Nachkommastellen aus Da hier ja die vom Anwender definierte
		 * Genauigkeit verwendet wird, m&uuml;ssen wir genau diesen gerundeten Wert (2
		 * oder 4 NKs) verwenden und erst danach mit der Menge multiplizieren. Beispiel:
		 * Diesel kostet 0,937, 100L kosten also 93,70 und nicht 94,- !!
		 */
		return bdNNettoeinzelpreisplusversteckteraufschlag;
	}

	// Einzelpreisplusversteckteraufschlag
	public BigDecimal getBdNEinzelpreisplusversteckteraufschlag(BelegpositionVerkaufDto belegpositionVerkaufDto,
			Double dVersteckterAufschlag, Integer iNachkommastellenPreis) {
		BigDecimal bdEinzelpreis = belegpositionVerkaufDto.getNEinzelpreis();
		if (bdEinzelpreis == null) {
			bdEinzelpreis = BigDecimal.ZERO;
		}
		BigDecimal bdNNettoeinzelpreisplusversteckteraufschlag = bdEinzelpreis;
		if (dVersteckterAufschlag != 0) {
			BigDecimal bdVersteckterAufschlag = new BigDecimal(dVersteckterAufschlag.doubleValue()).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdVersteckterAufschlag = Helper.rundeKaufmaennisch(bdVersteckterAufschlag, 4);
			BigDecimal bdNettoeinzelpreisVersteckterAufschlagSumme = bdEinzelpreis.multiply(bdVersteckterAufschlag);
			bdNNettoeinzelpreisplusversteckteraufschlag = bdNNettoeinzelpreisplusversteckteraufschlag
					.add(bdNettoeinzelpreisVersteckterAufschlagSumme);
		}
		bdNNettoeinzelpreisplusversteckteraufschlag = Helper
				.rundeKaufmaennisch(bdNNettoeinzelpreisplusversteckteraufschlag, iNachkommastellenPreis);
		return bdNNettoeinzelpreisplusversteckteraufschlag;
	}

	public BigDecimal getBdNEinzelpreisplusversteckteraufschlagminusrabatte(
			BelegpositionVerkaufDto belegpositionVerkaufDto, Double dAllgemeinerRabattsatz,
			Double dFProjektierungsrabattsatz, Integer iNachkommastellenPreis) {
		BigDecimal bdNettoeinzelpreis = belegpositionVerkaufDto.getNNettoeinzelpreisplusversteckteraufschlag();
		if (bdNettoeinzelpreis == null) {
			bdNettoeinzelpreis = BigDecimal.ZERO;
		}
		BigDecimal bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte = bdNettoeinzelpreis;
		// - Allgemeiner Rabatt
		if (dAllgemeinerRabattsatz != null && dAllgemeinerRabattsatz != 0.0) {
			BigDecimal bdAllgemeinerRabattSatz = new BigDecimal(dAllgemeinerRabattsatz.doubleValue()).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdAllgemeinerRabattSatz = Helper.rundeKaufmaennisch(bdAllgemeinerRabattSatz, 4);
			BigDecimal bdAllgemeinerRabattSumme = bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte
					.multiply(bdAllgemeinerRabattSatz);
			bdAllgemeinerRabattSumme = Helper.rundeKaufmaennisch(bdAllgemeinerRabattSumme, iNachkommastellenPreis);
			bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte = bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte
					.subtract(bdAllgemeinerRabattSumme);
		}
		// - Projektierungsrabatt
		if (dFProjektierungsrabattsatz != null && dFProjektierungsrabattsatz != 0.0) {
			BigDecimal bdProjektierungsRabatt = new BigDecimal(dFProjektierungsrabattsatz.doubleValue())
					.movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdProjektierungsRabatt = Helper.rundeKaufmaennisch(bdProjektierungsRabatt, 4);
			BigDecimal bdNettogesamtpreisProjektierungsrabattSumme = bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte
					.multiply(bdProjektierungsRabatt);
			bdNettogesamtpreisProjektierungsrabattSumme = Helper
					.rundeKaufmaennisch(bdNettogesamtpreisProjektierungsrabattSumme, iNachkommastellenPreis);
			bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte = bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte
					.subtract(bdNettogesamtpreisProjektierungsrabattSumme);
		}
		bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte = Helper
				.rundeKaufmaennisch(bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte, iNachkommastellenPreis);
		return bdNNettoeinzelpreisplusversteckteraufschlagminusrabatte;
	}

	@Override
	public BigDecimal getBdBruttoeinzelpreis(BelegpositionVerkaufDto belegpositionVerkaufDto,
			Integer iNachkommastellenPreis, TheClientDto theClientDto) {
		MwstsatzDto mwstDto = null;
		mwstDto = getMandantFac().mwstsatzFindByPrimaryKey(belegpositionVerkaufDto.getMwstsatzIId(), theClientDto);
		BigDecimal bdNettoeinzelpreis = belegpositionVerkaufDto.getNNettoeinzelpreisplusversteckteraufschlag();
		BigDecimal bdBruttoeinzelpreis = bdNettoeinzelpreis;
		if (mwstDto.getFMwstsatz() != 0) {
			BigDecimal bdMwst = new BigDecimal(mwstDto.getFMwstsatz().doubleValue()).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdMwst = Helper.rundeKaufmaennisch(bdMwst, 4);
			BigDecimal bdMwstSumme = bdBruttoeinzelpreis.multiply(bdMwst);
			bdBruttoeinzelpreis = bdBruttoeinzelpreis.add(bdMwstSumme);
		}
		bdBruttoeinzelpreis = Helper.rundeKaufmaennisch(bdBruttoeinzelpreis, iNachkommastellenPreis);
		return bdBruttoeinzelpreis;

	}

	private boolean isArtikelsetposition(BelegpositionVerkaufDto belegposDto) {
		// Belegposition selbst ist eine Artikelsetposition
		if (belegposDto.getPositioniIdArtikelset() != null) {
			return true;
		}

		// Ist Lieferscheinposition eine Teillieferung einer Artikelsetposition?
		if (belegposDto instanceof LieferscheinpositionDto) {
			LieferscheinpositionDto lsposDto = (LieferscheinpositionDto) belegposDto;
			if (lsposDto.getAuftragpositionIId() != null) {
				Auftragposition abpos = em.find(Auftragposition.class, lsposDto.getAuftragpositionIId());
				if (abpos != null && abpos.getPositionIIdArtikelset() != null) {
					return true;
				}
			}
		}

		return false;
	}

	public BigDecimal getZeilenSummeNettoeinzelpreisPlusversteckteraufschlag(
			BelegpositionVerkaufDto belegpositionVerkaufDto, BelegVerkaufDto belegVerkaufDto,
			Integer iNachkommastellenPreis) {

//		if (belegpositionVerkaufDto.getPositioniIdArtikelset() == null) {
		if (!isArtikelsetposition(belegpositionVerkaufDto)) {
			BigDecimal bdNNettoeinzelpreisplusversteckteraufschlag = belegpositionVerkaufDto
					.getNNettoeinzelpreisplusversteckteraufschlag();

			BigDecimal nZeilenSumme = belegpositionVerkaufDto.getNMenge()
					.multiply(bdNNettoeinzelpreisplusversteckteraufschlag);

			// Verleihtage
			if (belegpositionVerkaufDto.getVerleihIId() != null) {
				VerleihDto verleihDto = getArtikelFac().verleihFindByPrimaryKey(

						belegpositionVerkaufDto.getVerleihIId());

				nZeilenSumme = nZeilenSumme.multiply(new BigDecimal(verleihDto.getFFaktor()));
			}

			nZeilenSumme = Helper.rundeKaufmaennisch(nZeilenSumme, iNachkommastellenPreis);
//			System.out.println("BelegposId: " + belegpositionVerkaufDto.getIId() + 
//					", Zeilensumme " + nZeilenSumme);
			return nZeilenSumme;
		} else {
			// Wenn Setartikel, dann preis auslassen
			return BigDecimal.ZERO;
		}
	}

	public BigDecimal getZeilenSumme(BigDecimal menge, BigDecimal preis, Integer iNachkommastellenPreis) {
		menge = Helper.rundeKaufmaennisch(menge, iNachkommastellenPreis);
		preis = Helper.rundeKaufmaennisch(preis, iNachkommastellenPreis);
		BigDecimal nZeilenSumme = menge.multiply(preis);
		nZeilenSumme = Helper.rundeKaufmaennisch(nZeilenSumme, iNachkommastellenPreis);
		return nZeilenSumme;
	}

	public BelegpositionVerkaufDto getBelegpositionVerkaufReport(BelegpositionVerkaufDto belegpositionVerkaufDto,
			BelegVerkaufDto belegVerkaufDto, Integer iNachkommastellenPreis) {
		// Einzelpreis
		BigDecimal bdEinzelpreis = belegpositionVerkaufDto.getNEinzelpreis();
		// if(Helper.short2boolean(belegpositionVerkaufDto.getBNettopreisuebersteuert()))
		// bdEinzelpreis = belegpositionVerkaufDto.getNNettoeinzelpreis();
		bdEinzelpreis = Helper.rundeKaufmaennisch(bdEinzelpreis, iNachkommastellenPreis);
		belegpositionVerkaufDto.setNReportEinzelpreis(bdEinzelpreis);

		// Einzelpreisplusversteckteraufschlag
		BigDecimal bdEinzelpreisplusversteckteraufschlag = belegpositionVerkaufDto
				.getNEinzelpreisplusversteckteraufschlag();
		// if(Helper.short2boolean(belegpositionVerkaufDto.getBNettopreisuebersteuert()))
		// bdEinzelpreisplusversteckteraufschlag =
		// belegpositionVerkaufDto.getNNettoeinzelpreisplusversteckteraufschlag();
		bdEinzelpreisplusversteckteraufschlag = Helper.rundeKaufmaennisch(bdEinzelpreisplusversteckteraufschlag,
				iNachkommastellenPreis);
		belegpositionVerkaufDto.setNReportEinzelpreisplusversteckteraufschlag(bdEinzelpreisplusversteckteraufschlag);

		// Nettoeinzelpreisplusversteckteraufschlag
		BigDecimal bdNettoeinzelpreisplusversteckteraufschlag = belegpositionVerkaufDto
				.getNNettoeinzelpreisplusversteckteraufschlag();

		bdNettoeinzelpreisplusversteckteraufschlag = Helper
				.rundeKaufmaennisch(bdNettoeinzelpreisplusversteckteraufschlag, iNachkommastellenPreis);
		belegpositionVerkaufDto
				.setNReportNettoeinzelpreisplusversteckteraufschlag(bdNettoeinzelpreisplusversteckteraufschlag);

		// Gesamtpreis
		BigDecimal bdGesamtpreis = belegpositionVerkaufDto.getNMenge()
				.multiply(belegpositionVerkaufDto.getNNettoeinzelpreisplusversteckteraufschlag());

		// Verleihtage
		BigDecimal bdVerleihfaktor = new BigDecimal(1);
		if (belegpositionVerkaufDto.getVerleihIId() != null) {
			VerleihDto verleihDto = getArtikelFac().verleihFindByPrimaryKey(

					belegpositionVerkaufDto.getVerleihIId());

			bdGesamtpreis = bdGesamtpreis.multiply(new BigDecimal(verleihDto.getFFaktor()));
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
		Mwstsatz mwst = em.find(Mwstsatz.class, belegpositionVerkaufDto.getMwstsatzIId());
		belegpositionVerkaufDto.setDReportMwstsatz(mwst.getFMwstsatz());

		BigDecimal ust = belegpositionVerkaufDto.getNNettoeinzelpreisplusversteckteraufschlag()
				.multiply(new BigDecimal(mwst.getFMwstsatz()).movePointLeft(2));
		ust = ust.multiply(belegpositionVerkaufDto.getNMenge().multiply(bdVerleihfaktor));

		// TODO: ghp, vorerst deaktiviert 28.02.2012
		// in den nachfolgenden Routinen wurde mit diesem gerundetem Wert
		// gerechnet.
		// Vorerst wird die mwst noch gefuellt, aber eigentlich ist dieser Wert
		// nur zur informationszwecken!
		// ust = Helper.rundeKaufmaennisch(ust, iNachkommastellenPreis);

		belegpositionVerkaufDto.setNReportMwstsatzbetrag(ust);

		return belegpositionVerkaufDto;
	}

	/**
	 * Ermittelt den Steuerbetrag unter Ber&uuml;cksichtigung des allgemeinen bzw.
	 * Projektrabatts
	 * 
	 * @param belegpositionVerkaufDtos
	 * @param belegVerkaufDto
	 * @param iNachkommastellenPreis
	 * @param theClientDto
	 * @return der kummulierte Steuerbetrag (&uuml;ber alle Steuers&auml;tze hinweg)
	 */
	public BigDecimal getNettoGesamtWertUST(BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, Integer iNachkommastellenPreis, TheClientDto theClientDto) {
		BelegSteuerDto steuerDto = getNettoGesamtWertUSTInfo(belegpositionVerkaufDtos, belegVerkaufDto,
				iNachkommastellenPreis, theClientDto);
		return steuerDto.getTotalUst();
		/*
		 * Double dAllgemeinerRabattsatz = belegVerkaufDto .getFAllgemeinerRabattsatz();
		 * Double dFProjektierungsrabattsatz = belegVerkaufDto
		 * .getFProjektierungsrabattsatz();
		 * 
		 * BigDecimal gesamtWertUST = new BigDecimal(0); LinkedHashMap<Integer,
		 * MwstsatzReportDto> mwstMap = getMwstSumme( belegpositionVerkaufDtos,
		 * belegVerkaufDto, theClientDto); for (Iterator<?> iter =
		 * mwstMap.keySet().iterator(); iter.hasNext();) { Integer key = (Integer)
		 * iter.next(); MwstsatzReportDto item = (MwstsatzReportDto) mwstMap.get(key);
		 * BigDecimal nSummeMwst = item.getNSummeMwstbetrag();
		 * 
		 * // - Allgemeiner Rabatt if (dAllgemeinerRabattsatz != null) { BigDecimal
		 * bdAllgemeinerRabattSatz = new BigDecimal(
		 * dAllgemeinerRabattsatz.doubleValue()).movePointLeft(2); // Rabattsatz auf 4
		 * Nachkommastellen gerundet, um den // Double-Fehler auszugleichen
		 * bdAllgemeinerRabattSatz = Helper.rundeKaufmaennisch( bdAllgemeinerRabattSatz,
		 * 4); BigDecimal bdAllgemeinerRabattSumme = nSummeMwst
		 * .multiply(bdAllgemeinerRabattSatz); bdAllgemeinerRabattSumme =
		 * Helper.rundeKaufmaennisch( bdAllgemeinerRabattSumme, iNachkommastellenPreis);
		 * nSummeMwst = nSummeMwst.subtract(bdAllgemeinerRabattSumme); } // -
		 * Projektierungsrabatt if (dFProjektierungsrabattsatz != null) { BigDecimal
		 * bdProjektierungsRabatt = new BigDecimal(
		 * dFProjektierungsrabattsatz.doubleValue()) .movePointLeft(2); // Rabattsatz
		 * auf 4 Nachkommastellen gerundet, um den // Double-Fehler auszugleichen
		 * bdProjektierungsRabatt = Helper.rundeKaufmaennisch( bdProjektierungsRabatt,
		 * 4); BigDecimal bdNettogesamtpreisProjektierungsrabattSumme = nSummeMwst
		 * .multiply(bdProjektierungsRabatt);
		 * bdNettogesamtpreisProjektierungsrabattSumme = Helper .rundeKaufmaennisch(
		 * bdNettogesamtpreisProjektierungsrabattSumme, iNachkommastellenPreis);
		 * nSummeMwst = nSummeMwst
		 * .subtract(bdNettogesamtpreisProjektierungsrabattSumme); } Double fMwstsatz =
		 * new Double(0); Mwstsatz mwst = em.find(Mwstsatz.class, key); if (mwst !=
		 * null) { fMwstsatz = mwst.getFMwstsatz(); } BigDecimal bdMwstsatz = new
		 * BigDecimal(fMwstsatz).movePointLeft(2); // Rabattsatz auf 4 Nachkommastellen
		 * gerundet, um den Double-Fehler // auszugleichen bdMwstsatz =
		 * Helper.rundeKaufmaennisch(bdMwstsatz, 4);
		 * 
		 * // Achtung: Wenn diese Zeile geaendert wird, dann auch die // entsprechende
		 * Zeile in // MwstsatzReportDto.getNSummePositionsbetragMinusRabatte //
		 * (Helper.rundekaufmaennisch) anpassen! nSummeMwst =
		 * Helper.rundeKaufmaennisch(nSummeMwst, 2);
		 * 
		 * BigDecimal ust = nSummeMwst.multiply(bdMwstsatz); ust =
		 * Helper.rundeKaufmaennisch(ust, 2); System.out.println("fMwstsatz " +
		 * fMwstsatz + " " + ust); gesamtWertUST = gesamtWertUST.add(ust); } return
		 * gesamtWertUST;
		 */
	}

	public BelegSteuerDto getNettoGesamtWertUSTInfo(BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, Integer iNachkommastellenPreis, TheClientDto theClientDto) {
		Double dAllgemeinerRabattsatz = belegVerkaufDto.getFAllgemeinerRabattsatz();
		Double dFProjektierungsrabattsatz = belegVerkaufDto.getFProjektierungsrabattsatz();

		BigDecimal gesamtWertUST = BigDecimal.ZERO;
		LinkedHashMap<Integer, MwstsatzReportDto> mwstMap = getMwstSumme(belegpositionVerkaufDtos, belegVerkaufDto,
				theClientDto);

		for (MwstsatzReportDto item : mwstMap.values()) {
			BigDecimal nSummeMwst = item.getNSummeMwstbetrag();

			// - Allgemeiner Rabatt
			if (dAllgemeinerRabattsatz != null) {
				BigDecimal bdAllgemeinerRabattSatz = new BigDecimal(dAllgemeinerRabattsatz.doubleValue())
						.movePointLeft(2);
				// Rabattsatz auf 4 Nachkommastellen gerundet, um den
				// Double-Fehler auszugleichen
				bdAllgemeinerRabattSatz = Helper.rundeKaufmaennisch(bdAllgemeinerRabattSatz, 4);
				BigDecimal bdAllgemeinerRabattSumme = nSummeMwst.multiply(bdAllgemeinerRabattSatz);
				bdAllgemeinerRabattSumme = Helper.rundeKaufmaennisch(bdAllgemeinerRabattSumme, iNachkommastellenPreis);
				nSummeMwst = nSummeMwst.subtract(bdAllgemeinerRabattSumme);
			}

			// - Projektierungsrabatt
			if (dFProjektierungsrabattsatz != null) {
				BigDecimal bdProjektierungsRabatt = new BigDecimal(dFProjektierungsrabattsatz.doubleValue())
						.movePointLeft(2);
				// Rabattsatz auf 4 Nachkommastellen gerundet, um den
				// Double-Fehler auszugleichen
				bdProjektierungsRabatt = Helper.rundeKaufmaennisch(bdProjektierungsRabatt, 4);
				BigDecimal bdNettogesamtpreisProjektierungsrabattSumme = nSummeMwst.multiply(bdProjektierungsRabatt);
				bdNettogesamtpreisProjektierungsrabattSumme = Helper
						.rundeKaufmaennisch(bdNettogesamtpreisProjektierungsrabattSumme, iNachkommastellenPreis);
				nSummeMwst = nSummeMwst.subtract(bdNettogesamtpreisProjektierungsrabattSumme);
			}

			Mwstsatz mwst = em.find(Mwstsatz.class, item.getMwstsatzId());
			Double fMwstsatz = mwst != null ? mwst.getFMwstsatz() : new Double(0);
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
			item.setNSummeMwstbetrag(ust);
			gesamtWertUST = gesamtWertUST.add(ust);
		}

		BelegSteuerDto steuerDto = new BelegSteuerDto();
		steuerDto.setMwstMap(mwstMap);
		steuerDto.setTotalUst(gesamtWertUST);
		return steuerDto;
	}

	protected boolean isCalculateablePosition(BelegpositionVerkaufDto belegpositionVerkaufDto) {
		BelegpositionVerkaufDto dto = belegpositionVerkaufDto;
		return dto.isIdent() || dto.isHandeingabe() || dto.isIntelligenteZwischensumme();
	}

	private LinkedHashMap<Integer, MwstsatzReportDto> getMwstSumme(BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, TheClientDto theClientDto) {
		Integer iNachkommastellenPreis = getUINachkommastellenPreisVK(belegVerkaufDto.getMandantCNr());
		LinkedHashMap<Integer, MwstsatzReportDto> mwstMap = new LinkedHashMap<Integer, MwstsatzReportDto>();
		Set<?> mwstSatzKeys;
		try {
			mwstSatzKeys = getMandantFac().mwstsatzIIdFindAllByMandant(belegVerkaufDto.getMandantCNr(), theClientDto);
			for (Iterator<?> iter = mwstSatzKeys.iterator(); iter.hasNext();) {
				Integer satzId = (Integer) iter.next();
				MwstsatzReportDto mwstsatzReportDto = new MwstsatzReportDto(satzId);
				mwstMap.put(satzId, mwstsatzReportDto);
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
					BigDecimal bdZeilenSumme = Helper.rundeKaufmaennisch(
							getZeilenSummeNettoeinzelpreisPlusversteckteraufschlag(belegpositionVerkaufDtos[i],
									belegVerkaufDto, iNachkommastellenPreis),
							2);
					MwstsatzReportDto rep = mwstMap.get(mwstsatzIId);
					rep.setNSummeMwstbetrag(rep.getNSummeMwstbetrag().add(bdZeilenSumme));
//					System.out.println(" nSummeMwst " + bdZeilenSumme);
				}

			}
		} catch (EJBExceptionLP e) {
		} catch (RemoteException e) {
		}
		return mwstMap;
	}

	public Object[] getMwstTabelle_Orig(LinkedHashMap<Integer, MwstsatzReportDto> mwstMap,
			BelegVerkaufDto belegVerkaufDto, Locale locDruck, TheClientDto theClientDto) {

		// das Andrucken der gesammelten Mwstinformationen steuern
		StringBuffer sbMwstsatz = new StringBuffer();
		StringBuffer sbSummePositionsbetrag = new StringBuffer();
		StringBuffer sbWaehrung = new StringBuffer();
		StringBuffer sbSummeMwstbetrag = new StringBuffer();

		// UW 03.03.06: Die folgenden Informationen erscheinen am Druck, mit
		// diesen
		// Werten wird intern nicht mehr weitergerechnet -> daher auf 2
		// Stellen runden
		BigDecimal nEendbetragMitMwst = Helper.rundeKaufmaennisch(belegVerkaufDto.getNGesamtwertinbelegwaehrung(), 2);

		boolean bHatMwstWerte = false;

		for (Iterator<Integer> iter = mwstMap.keySet().iterator(); iter.hasNext();) {
			Integer key = (Integer) iter.next(); // IId des Mwstsatzes
			MwstsatzReportDto mwstsatzReportDto = (MwstsatzReportDto) mwstMap.get(key); // Summen der Mwstbetraege
			if (mwstsatzReportDto != null && mwstsatzReportDto.getNSummeMwstbetrag().doubleValue() != 0.0) {
				Mwstsatz mwst = em.find(Mwstsatz.class, key);
				// MR: FIX, statt festverdrahtetem UST verwende
				// Localeabhaengigen Wert lp.ust
				sbMwstsatz.append(getTextRespectUISpr("lp.ust", theClientDto.getMandant(), locDruck));
				sbMwstsatz.append(": ");
				sbMwstsatz.append(Helper.formatZahl(mwst.getFMwstsatz(), 2, locDruck));
				sbMwstsatz.append(" % ");
				sbMwstsatz.append(getTextRespectUISpr("lp.ustvon", theClientDto.getMandant(), locDruck)).append(" ");
				// Fix Ende
				BigDecimal nPositionMwstbetrag = getMwstbetrag(mwstsatzReportDto.getNSummePositionsbetrag(),
						belegVerkaufDto);
				sbSummePositionsbetrag.append(Helper.formatZahl(nPositionMwstbetrag, 2, locDruck));
				sbWaehrung.append(belegVerkaufDto.getWaehrungCNr());
				BigDecimal nSummeMwstbetrag = getMwstbetrag(mwstsatzReportDto.getNSummeMwstbetrag(), belegVerkaufDto);
				sbSummeMwstbetrag.append(Helper.formatZahl(nSummeMwstbetrag, 2, locDruck));

				sbMwstsatz.append("\n");
				sbSummePositionsbetrag.append("\n");
				sbWaehrung.append("\n");
				sbSummeMwstbetrag.append("\n");
				if (nEendbetragMitMwst != null)
					nEendbetragMitMwst = nEendbetragMitMwst.add(nSummeMwstbetrag);

				bHatMwstWerte = true;
			}
		}

		if (bHatMwstWerte) {
			// die letzten \n wieder loeschen
			sbMwstsatz.delete(sbMwstsatz.length() - 1, sbMwstsatz.length());
			sbSummePositionsbetrag.delete(sbSummePositionsbetrag.length() - 1, sbSummePositionsbetrag.length());
			sbWaehrung.delete(sbWaehrung.length() - 1, sbWaehrung.length());
			sbSummeMwstbetrag.delete(sbSummeMwstbetrag.length() - 1, sbSummeMwstbetrag.length());
		}

		String P_MWST_TABELLE_LINKS = sbMwstsatz.toString();
		String P_MWST_TABELLE_SUMME_POSITIONEN = sbSummePositionsbetrag.toString();
		String P_MWST_TABELLE_WAEHRUNG = sbWaehrung.toString();
		String P_MWST_TABELLE_RECHTS = sbSummeMwstbetrag.toString();
		// BigDecimal P_ENDBETRAGMITMWST = nEendbetragMitMwst;
		BigDecimal P_ENDBETRAGMITMWST = Helper.rundeKaufmaennisch(nEendbetragMitMwst, 2);
		Object[] sr = new Object[] { P_MWST_TABELLE_LINKS, P_MWST_TABELLE_SUMME_POSITIONEN, P_MWST_TABELLE_WAEHRUNG,
				P_MWST_TABELLE_RECHTS, P_ENDBETRAGMITMWST };
		return sr;
	}

	public Object[] getMwstTabelle_0(LinkedHashMap<Integer, MwstsatzReportDto> mwstMap, BelegVerkaufDto belegVerkaufDto,
			Locale locDruck, String waehrungCNrZusaetzlich, TheClientDto theClientDto) {

		// das Andrucken der gesammelten Mwstinformationen steuern
		StringBuffer sbMwstsatz = new StringBuffer();
		StringBuffer sbSummePositionsbetrag = new StringBuffer();
		StringBuffer sbWaehrung = new StringBuffer();
		StringBuffer sbSummeMwstbetrag = new StringBuffer();

		// UW 03.03.06: Die folgenden Informationen erscheinen am Druck, mit
		// diesen
		// Werten wird intern nicht mehr weitergerechnet -> daher auf 2
		// Stellen runden
		BigDecimal nEendbetragMitMwst = Helper.rundeKaufmaennisch(belegVerkaufDto.getNGesamtwertinbelegwaehrung(), 2);

		boolean bHatMwstWerte = false;

		for (Iterator<Integer> iter = mwstMap.keySet().iterator(); iter.hasNext();) {
			Integer key = (Integer) iter.next(); // IId des Mwstsatzes
			MwstsatzReportDto mwstsatzReportDto = (MwstsatzReportDto) mwstMap.get(key); // Summen der Mwstbetraege
			if (mwstsatzReportDto != null && mwstsatzReportDto.getNSummeMwstbetrag().doubleValue() != 0.0) {
				Mwstsatz mwst = em.find(Mwstsatz.class, key);
				// MR: FIX, statt festverdrahtetem UST verwende
				// Localeabhaengigen Wert lp.ust

				StringBuffer sbMwstsatzZeile = new StringBuffer();

				sbMwstsatzZeile.append(getTextRespectUISpr("lp.ust", theClientDto.getMandant(), locDruck));
				sbMwstsatzZeile.append(": ");
				sbMwstsatzZeile.append(Helper.formatZahl(mwst.getFMwstsatz(), 2, locDruck));
				sbMwstsatzZeile.append(" % ");
				sbMwstsatzZeile.append(getTextRespectUISpr("lp.ustvon", theClientDto.getMandant(), locDruck))
						.append(" ");

				sbMwstsatz.append(sbMwstsatzZeile);

				// Fix Ende
				BigDecimal nPositionMwstbetrag = mwstsatzReportDto.getNSummePositionsbetragMinusRabatte(
						belegVerkaufDto.getFAllgemeinerRabattsatz(), belegVerkaufDto.getFProjektierungsrabattsatz());

				// PJ19569
				if (waehrungCNrZusaetzlich != null
						&& !waehrungCNrZusaetzlich.equals(belegVerkaufDto.getWaehrungCNr())) {
					try {
						sbSummePositionsbetrag.append(Helper.formatAndRoundCurrency(
								getLocaleFac().rechneUmInAndereWaehrungZuDatum(nPositionMwstbetrag,
										belegVerkaufDto.getWaehrungCNr(), waehrungCNrZusaetzlich,
										new java.sql.Date(belegVerkaufDto.getTBelegdatum().getTime()), theClientDto),
								locDruck));
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
					sbWaehrung.append(waehrungCNrZusaetzlich);
				} else {
					sbSummePositionsbetrag.append(Helper.formatAndRoundCurrency(nPositionMwstbetrag, locDruck));

					sbWaehrung.append(belegVerkaufDto.getWaehrungCNr());
				}

				BigDecimal nSummeMwstbetrag = mwstsatzReportDto.getNSummeMWSTbetragMinusRabatte(
						belegVerkaufDto.getFAllgemeinerRabattsatz(), belegVerkaufDto.getFProjektierungsrabattsatz(),
						mwst.getFMwstsatz());

				// PJ19569
				if (waehrungCNrZusaetzlich != null
						&& !waehrungCNrZusaetzlich.equals(belegVerkaufDto.getWaehrungCNr())) {
					try {
						sbSummeMwstbetrag.append(Helper.formatAndRoundCurrency(
								getLocaleFac().rechneUmInAndereWaehrungZuDatum(nSummeMwstbetrag,
										belegVerkaufDto.getWaehrungCNr(), waehrungCNrZusaetzlich,
										new java.sql.Date(belegVerkaufDto.getTBelegdatum().getTime()), theClientDto),
								locDruck));
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				} else {
					sbSummeMwstbetrag.append(Helper.formatAndRoundCurrency(nSummeMwstbetrag, locDruck));
				}

				sbMwstsatz.append("\n");
				sbSummePositionsbetrag.append("\n");
				sbWaehrung.append("\n");
				sbSummeMwstbetrag.append("\n");
				if (nEendbetragMitMwst != null)
					nEendbetragMitMwst = nEendbetragMitMwst.add(nSummeMwstbetrag);

				bHatMwstWerte = true;

			}
		}

		if (bHatMwstWerte) {
			// die letzten \n wieder loeschen
			sbMwstsatz.delete(sbMwstsatz.length() - 1, sbMwstsatz.length());
			sbSummePositionsbetrag.delete(sbSummePositionsbetrag.length() - 1, sbSummePositionsbetrag.length());
			sbWaehrung.delete(sbWaehrung.length() - 1, sbWaehrung.length());
			sbSummeMwstbetrag.delete(sbSummeMwstbetrag.length() - 1, sbSummeMwstbetrag.length());
		}

		String P_MWST_TABELLE_LINKS = sbMwstsatz.toString();
		String P_MWST_TABELLE_SUMME_POSITIONEN = sbSummePositionsbetrag.toString();
		String P_MWST_TABELLE_WAEHRUNG = sbWaehrung.toString();
		String P_MWST_TABELLE_RECHTS = sbSummeMwstbetrag.toString();
		// BigDecimal P_ENDBETRAGMITMWST = nEendbetragMitMwst;
		BigDecimal P_ENDBETRAGMITMWST = Helper.rundeGeldbetrag(nEendbetragMitMwst);
		Object[] sr = new Object[] { P_MWST_TABELLE_LINKS, P_MWST_TABELLE_SUMME_POSITIONEN, P_MWST_TABELLE_WAEHRUNG,
				P_MWST_TABELLE_RECHTS, P_ENDBETRAGMITMWST };
		return sr;
	}

	public Object[] getMwstTabelle(LinkedHashMap<Integer, MwstsatzReportDto> mwstMap, BelegVerkaufDto belegVerkaufDto,
			Locale locDruck, String waehrungCNrZusaetzlich, TheClientDto theClientDto) {

		// das Andrucken der gesammelten Mwstinformationen steuern
		StringBuffer sbMwstsatz = new StringBuffer();
		StringBuffer sbSummePositionsbetrag = new StringBuffer();
		StringBuffer sbWaehrung = new StringBuffer();
		StringBuffer sbSummeMwstbetrag = new StringBuffer();

		// UW 03.03.06: Die folgenden Informationen erscheinen am Druck, mit
		// diesen
		// Werten wird intern nicht mehr weitergerechnet -> daher auf 2
		// Stellen runden
		BigDecimal nEendbetragMitMwst = Helper.rundeKaufmaennisch(belegVerkaufDto.getNGesamtwertinbelegwaehrung(), 2);

		boolean bHatMwstWerte = false;
		for (Iterator<Integer> iter = mwstMap.keySet().iterator(); iter.hasNext();) {
			Integer key = (Integer) iter.next(); // IId des Mwstsatzes
			MwstsatzReportDto mwstsatzReportDto = (MwstsatzReportDto) mwstMap.get(key); // Summen der Mwstbetraege
			if (mwstsatzReportDto != null && mwstsatzReportDto.getNSummeMwstbetrag().signum() != 0) {
				Mwstsatz mwst = em.find(Mwstsatz.class, key);
				// MR: FIX, statt festverdrahtetem UST verwende
				// Localeabhaengigen Wert lp.ust

				StringBuffer sbMwstsatzZeile = new StringBuffer();

				sbMwstsatzZeile.append(getTextRespectUISpr("lp.ust", theClientDto.getMandant(), locDruck));
				sbMwstsatzZeile.append(": ");
				sbMwstsatzZeile.append(Helper.formatZahl(mwst.getFMwstsatz(), 2, locDruck));
				sbMwstsatzZeile.append(" % ");
				sbMwstsatzZeile.append(getTextRespectUISpr("lp.ustvon", theClientDto.getMandant(), locDruck))
						.append(" ");

				sbMwstsatz.append(sbMwstsatzZeile);

				// Fix Ende
				BigDecimal nPositionMwstbetrag = mwstsatzReportDto.getNSummePositionsbetragMinusRabatte(
						belegVerkaufDto.getFAllgemeinerRabattsatz(), belegVerkaufDto.getFProjektierungsrabattsatz());

				// PJ19569
				if (waehrungCNrZusaetzlich != null
						&& !waehrungCNrZusaetzlich.equals(belegVerkaufDto.getWaehrungCNr())) {
					try {
						sbSummePositionsbetrag.append(Helper.formatAndRoundCurrency(
								getLocaleFac().rechneUmInAndereWaehrungZuDatum(nPositionMwstbetrag,
										belegVerkaufDto.getWaehrungCNr(), waehrungCNrZusaetzlich,
										new java.sql.Date(belegVerkaufDto.getTBelegdatum().getTime()), theClientDto),
								locDruck));
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
					sbWaehrung.append(waehrungCNrZusaetzlich);
				} else {
					sbSummePositionsbetrag.append(Helper.formatAndRoundCurrency(nPositionMwstbetrag, locDruck));

					sbWaehrung.append(belegVerkaufDto.getWaehrungCNr());
				}

				BigDecimal nSummeMwstbetrag = mwstsatzReportDto.getNSummeMWSTbetragMinusRabatte(
						belegVerkaufDto.getFAllgemeinerRabattsatz(), belegVerkaufDto.getFProjektierungsrabattsatz(),
						mwst.getFMwstsatz());
				nSummeMwstbetrag = coinRoundingService.roundUst(belegVerkaufDto, nSummeMwstbetrag, theClientDto);

				// PJ19569
				if (waehrungCNrZusaetzlich != null
						&& !waehrungCNrZusaetzlich.equals(belegVerkaufDto.getWaehrungCNr())) {
					try {
						sbSummeMwstbetrag.append(Helper.formatAndRoundCurrency(
								getLocaleFac().rechneUmInAndereWaehrungZuDatum(nSummeMwstbetrag,
										belegVerkaufDto.getWaehrungCNr(), waehrungCNrZusaetzlich,
										new java.sql.Date(belegVerkaufDto.getTBelegdatum().getTime()), theClientDto),
								locDruck));
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				} else {
					sbSummeMwstbetrag.append(Helper.formatAndRoundCurrency(nSummeMwstbetrag, locDruck));
				}

				sbMwstsatz.append("\n");
				sbSummePositionsbetrag.append("\n");
				sbWaehrung.append("\n");
				sbSummeMwstbetrag.append("\n");
				if (nEendbetragMitMwst != null)
					nEendbetragMitMwst = nEendbetragMitMwst.add(nSummeMwstbetrag);

				bHatMwstWerte = true;

			}
		}

		if (bHatMwstWerte) {
			// die letzten \n wieder loeschen
			sbMwstsatz.delete(sbMwstsatz.length() - 1, sbMwstsatz.length());
			sbSummePositionsbetrag.delete(sbSummePositionsbetrag.length() - 1, sbSummePositionsbetrag.length());
			sbWaehrung.delete(sbWaehrung.length() - 1, sbWaehrung.length());
			sbSummeMwstbetrag.delete(sbSummeMwstbetrag.length() - 1, sbSummeMwstbetrag.length());
		}

		String P_MWST_TABELLE_LINKS = sbMwstsatz.toString();
		String P_MWST_TABELLE_SUMME_POSITIONEN = sbSummePositionsbetrag.toString();
		String P_MWST_TABELLE_WAEHRUNG = sbWaehrung.toString();
		String P_MWST_TABELLE_RECHTS = sbSummeMwstbetrag.toString();
		// BigDecimal P_ENDBETRAGMITMWST = nEendbetragMitMwst;
		BigDecimal P_ENDBETRAGMITMWST = Helper.rundeGeldbetrag(nEendbetragMitMwst);
		Object[] sr = new Object[] { P_MWST_TABELLE_LINKS, P_MWST_TABELLE_SUMME_POSITIONEN, P_MWST_TABELLE_WAEHRUNG,
				P_MWST_TABELLE_RECHTS, P_ENDBETRAGMITMWST };
		return sr;
	}

	public BigDecimal getMwstbetrag(BigDecimal mwstbetrag, BelegVerkaufDto belegVerkaufDto) {
		Double dAllgemeinerRabattsatz = belegVerkaufDto.getFAllgemeinerRabattsatz();
		Double dFProjektierungsrabattsatz = belegVerkaufDto.getFProjektierungsrabattsatz();

		BigDecimal nSummeMwst = mwstbetrag;

		// - Allgemeiner Rabatt
		if (dAllgemeinerRabattsatz != null && dAllgemeinerRabattsatz != 0.0) {
			BigDecimal bdAllgemeinerRabattSatz = new BigDecimal(dAllgemeinerRabattsatz.doubleValue()).movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdAllgemeinerRabattSatz = Helper.rundeKaufmaennisch(bdAllgemeinerRabattSatz, 4);
			BigDecimal bdAllgemeinerRabattSumme = nSummeMwst.multiply(bdAllgemeinerRabattSatz);
			bdAllgemeinerRabattSumme = Helper.rundeKaufmaennisch(bdAllgemeinerRabattSumme, 2);
			nSummeMwst = nSummeMwst.subtract(bdAllgemeinerRabattSumme);
		}
		// - Projektierungsrabatt
		if (dFProjektierungsrabattsatz != null && dFProjektierungsrabattsatz != 0.0) {
			BigDecimal bdProjektierungsRabatt = new BigDecimal(dFProjektierungsrabattsatz.doubleValue())
					.movePointLeft(2);
			// Rabattsatz auf 4 Nachkommastellen gerundet, um den Double-Fehler
			// auszugleichen
			bdProjektierungsRabatt = Helper.rundeKaufmaennisch(bdProjektierungsRabatt, 4);
			BigDecimal bdNettogesamtpreisProjektierungsrabattSumme = nSummeMwst.multiply(bdProjektierungsRabatt);
			bdNettogesamtpreisProjektierungsrabattSumme = Helper
					.rundeKaufmaennisch(bdNettogesamtpreisProjektierungsrabattSumme, 2);
			nSummeMwst = nSummeMwst.subtract(bdNettogesamtpreisProjektierungsrabattSumme);
		}
		return nSummeMwst;
	}

	public BigDecimal getWertPoisitionsartPosition(Integer iIdPositionI, String belegartCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		BigDecimal bdWert = new BigDecimal(0);
		Query query = null;
		if (belegartCNr.equals(LocaleFac.BELEGART_ANGEBOT))
			query = em.createNamedQuery("getWertAngebotpositionartPosition");
		if (belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG))
			query = em.createNamedQuery("getWertAuftragpositionartPosition");
		if (belegartCNr.equals(LocaleFac.BELEGART_LIEFERSCHEIN))
			query = em.createNamedQuery("getWertLieferscheinpositionartPosition");
		if (belegartCNr.equals(LocaleFac.BELEGART_RECHNUNG))
			query = em.createNamedQuery("getWertRechnungpositionartPosition");
		query.setParameter(1, iIdPositionI);
		query.setParameter(2, LocaleFac.POSITIONSART_IDENT);
		query.setParameter(3, LocaleFac.POSITIONSART_HANDEINGABE);
		bdWert = (BigDecimal) query.getSingleResult();
		return bdWert;
	}

	private BigDecimal totalSnrMenge(List<SeriennrChargennrMitMengeDto> knownSnrs) {
		BigDecimal knownMenge = BigDecimal.ZERO;
		for (SeriennrChargennrMitMengeDto seriennrChargennrMitMengeDto : knownSnrs) {
			knownMenge = knownMenge.add(seriennrChargennrMitMengeDto.getNMenge());
		}
		return knownMenge;
	}

	public void setupPositionWithIdentities(boolean zubuchen, BelegpositionDto rePosDto,
			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities, TheClientDto theClientDto) {
		ArtikelDto artikel = getArtikelFac().artikelFindByPrimaryKey(rePosDto.getArtikelIId(), theClientDto);
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
				Integer artikelIIdGefunden = zubuchen
						? getLagerFac().getArtikelIIdUeberSeriennummerAbgang(snrMengeDto.getCSeriennrChargennr(),
								theClientDto)
						: getLagerFac().getArtikelIIdUeberSeriennummer(snrMengeDto.getCSeriennrChargennr(),
								theClientDto);

				if (!rePosDto.getArtikelIId().equals(artikelIIdGefunden))
					continue;

				if (knownMenge.add(snrMengeDto.getNMenge()).compareTo(sollMenge) <= 0) {
					rePosDto.getSeriennrChargennrMitMenge().add(snrMengeDto);
					notyetUsedIdentities.remove(snrMengeDto);
					foundEntry = true;
					break;
				}
			}
		} while (foundEntry && knownMenge.compareTo(sollMenge) < 0);

		knownMenge = totalSnrMenge(rePosDto.getSeriennrChargennrMitMenge());
		if (knownMenge.compareTo(sollMenge) < 0) {

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_MENGE_UNGLEICH, new Exception(
					artikel.getCNr() + "(" + knownMenge.toString() + " von " + rePosDto.getNMenge() + " vorhanden)"));

		}
	}

	private boolean identiesHaveIdentity(List<SeriennrChargennrMitMengeDto> entries,
			SeriennrChargennrMitMengeDto entity) {
		if (entries == null || entity == null)
			return false;

		for (SeriennrChargennrMitMengeDto snr : entries) {
			if (snr.equalsIdentity(entity)) {
				return true;
			}
		}
		return false;
	}

	private boolean identiesRemoveIdentity(List<SeriennrChargennrMitMengeDto> entries,
			SeriennrChargennrMitMengeDto entity) {
		if (entries == null || entity == null)
			return false;

		SeriennrChargennrMitMengeDto entry = null;
		for (SeriennrChargennrMitMengeDto snr : entries) {
			if (snr.equalsIdentity(entity)) {
				entry = snr;
				break;
			}
		}
		if (entry != null) {
			entries.remove(entry);
			return true;
		}

		return false;
	}

	@Override
	public void setupChangePositionWithIdentities(boolean zubuchen, BelegpositionDto posDto,
			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities, TheClientDto theClientDto) {
		ArtikelDto artikel = getArtikelFac().artikelFindByPrimaryKey(posDto.getArtikelIId(), theClientDto);
		if (!(artikel.isChargennrtragend() || artikel.isSeriennrtragend()))
			return;

		BigDecimal knownMenge = BigDecimal.ZERO;
		BigDecimal sollMenge = posDto.getNMenge().abs(); // negative
															// Lieferscheinmenge
															// (Gutschriften)
		if (posDto.getSeriennrChargennrMitMenge() == null) {
			posDto.setSeriennrChargennrMitMenge(new ArrayList<SeriennrChargennrMitMengeDto>());
		}
		List<SeriennrChargennrMitMengeDto> knownSnrs = new ArrayList<SeriennrChargennrMitMengeDto>(
				posDto.getSeriennrChargennrMitMenge());
		List<SeriennrChargennrMitMengeDto> sollSnrs = new ArrayList<SeriennrChargennrMitMengeDto>();

		boolean foundEntry = false;
		do {
			foundEntry = false;
			knownMenge = totalSnrMenge(sollSnrs);
			if (knownMenge.compareTo(sollMenge) >= 0)
				break;

			if (knownSnrs.size() > 0) {
				SeriennrChargennrMitMengeDto entity = knownSnrs.get(0);
				knownSnrs.remove(0);
				if (identiesHaveIdentity(notyetUsedIdentities, entity)) {
					sollSnrs.add(entity);
					identiesRemoveIdentity(notyetUsedIdentities, entity);
					foundEntry = true;
					continue;
				}
			}

			for (SeriennrChargennrMitMengeDto snrMengeDto : notyetUsedIdentities) {
				Integer artikelIIdGefunden = zubuchen
						? getLagerFac().getArtikelIIdUeberSeriennummerAbgang(snrMengeDto.getCSeriennrChargennr(),
								theClientDto)
						: getLagerFac().getArtikelIIdUeberSeriennummer(snrMengeDto.getCSeriennrChargennr(),
								theClientDto);

				if (!posDto.getArtikelIId().equals(artikelIIdGefunden))
					continue;

				if (knownMenge.add(snrMengeDto.getNMenge()).compareTo(sollMenge) <= 0) {
					sollSnrs.add(snrMengeDto);
					notyetUsedIdentities.remove(snrMengeDto);
					foundEntry = true;
					break;
				}
			}
		} while (foundEntry && knownMenge.compareTo(sollMenge) < 0);

		posDto.setSeriennrChargennrMitMenge(sollSnrs);
		knownMenge = totalSnrMenge(posDto.getSeriennrChargennrMitMenge());
		if (knownMenge.compareTo(sollMenge) < 0) {

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_MENGE_UNGLEICH, new Exception(
					artikel.getCNr() + "(" + knownMenge.toString() + " von " + posDto.getNMenge() + " vorhanden)"));

		}
	}

	/**
	 * Aus der Liste der eineindeutigen Seriennummern einige dieser Position
	 * zuordnen
	 * 
	 * @param rePosDto             ist die Belegposition, der Seriennummern
	 *                             zugeordnet werden sollen, sofern es sich um eine
	 *                             seriennummerntragende Position handelt
	 * @param notyetUsedIdentities die Liste der noch verf&uuml;gbaren Seriennummern
	 *                             (mit deren Menge)
	 * @param theClientDto
	 */
	public void setupPositionWithIdentities(BelegpositionDto rePosDto,
			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities, TheClientDto theClientDto) {
		setupPositionWithIdentities(false, rePosDto, notyetUsedIdentities, theClientDto);
	}

	private int getBelegpositionIndex(Integer positionIId, BelegpositionVerkaufDto[] dtos) {
		for (int i = 0; i < dtos.length; i++) {
			if (positionIId.equals(dtos[i].getIId()))
				return i;
		}

		return -1;
	}

	private int findNextIntelligenteZwischensummePosition(int startIndex, BelegpositionVerkaufDto[] dtos) {
		for (int i = startIndex; i < dtos.length; i++) {
			if (dtos[i].isIntelligenteZwischensumme()) {
				return i;
			}
		}

		return -1;
	}

	private int findNextPosition(int startIndex, int endIndex, BelegpositionVerkaufDto[] dtos) {
		PositionNumberHandler dtoNumberHandler = new PositionNumberHandler();

		while (startIndex <= dtos.length && (startIndex < endIndex)) {
			if (dtoNumberHandler.hasPositionNummer(new BelegPositionNumberDtoAdapter(dtos[startIndex]))) {
				return startIndex;
			}

			++startIndex;
		}

		return -1;
	}

	private int findPreviousPosition(int startIndex, BelegpositionVerkaufDto[] dtos) {
		PositionNumberHandler dtoNumberHandler = new PositionNumberHandler();
		while (startIndex >= 0) {
			if (dtoNumberHandler.hasPositionNummer(new BelegPositionNumberDtoAdapter(dtos[startIndex]))) {
				return startIndex;
			}

			--startIndex;
		}

		return -1;
	}

	private int findNextPossiblePosition(int startIndex, int endIndex, BelegpositionVerkaufDto[] dtos) {
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

	private int reindexZwischensummenPositionRef(BelegpositionVerkaufDto belegPositionDto,
			BelegpositionVerkaufDto[] dtos, int positionIndex) {
		int zwsPosIndex = findNextIntelligenteZwischensummePosition(positionIndex, dtos);
		if (-1 == zwsPosIndex)
			return zwsPosIndex; // keine Zws vorhanden

		if (belegPositionDto.getIId().equals(dtos[zwsPosIndex].getZwsVonPosition())) {
			// Die Von-Position wird geloescht.
			int savedPositionIndex = positionIndex;
			positionIndex = findNextPossiblePosition(positionIndex, zwsPosIndex, dtos);
			if (-1 == positionIndex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_VON_KLEINER_BIS,
						dtos[savedPositionIndex].getIId().toString());
			}

			Class entityClass = getEntityClassForBelegDto(belegPositionDto);
			IZwsPosition rechnungPosition = (IZwsPosition) em.find(entityClass, dtos[zwsPosIndex].getIId());
			Integer oldVonPos = rechnungPosition.getZwsVonPosition();
			rechnungPosition.setZwsVonPosition(dtos[positionIndex].getIId());
			if (oldVonPos.equals(rechnungPosition.getZwsBisPosition())) {
				rechnungPosition.setZwsBisPosition(dtos[positionIndex].getIId());
			}
			em.merge(rechnungPosition);
			em.flush();
			return zwsPosIndex;
		}

		if (belegPositionDto.getIId().equals(dtos[zwsPosIndex].getZwsBisPosition())) {
			// Die Bis-Position wird geloescht
			int savedPositionIndex = positionIndex;
			positionIndex = findNextPossiblePosition(positionIndex, zwsPosIndex, dtos);

			if (-1 == positionIndex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_VON_KLEINER_BIS,
						dtos[savedPositionIndex].getIId().toString());
			}

			Class entityClass = getEntityClassForBelegDto(belegPositionDto);
			IZwsPosition rechnungPosition = (IZwsPosition) em.find(entityClass, dtos[zwsPosIndex].getIId());
			Integer oldBisPos = rechnungPosition.getZwsVonPosition();
			rechnungPosition.setZwsBisPosition(dtos[positionIndex].getIId());
			if (oldBisPos.equals(rechnungPosition.getZwsVonPosition())) {
				rechnungPosition.setZwsVonPosition(dtos[positionIndex].getIId());
			}
			em.merge(rechnungPosition);
			em.flush();
			return zwsPosIndex;
		}

		return zwsPosIndex;
	}

	public void processIntelligenteZwischensummeRemove(BelegVerkaufDto belegDto,
			BelegpositionVerkaufDto belegPositionDto, BelegpositionVerkaufDto[] belegPositionDtos)
			throws EJBExceptionLP {

		int positionIndex = getBelegpositionIndex(belegPositionDto.getIId(), belegPositionDtos);
		if (-1 == positionIndex)
			return;

		int zwsPosIndex = -1;
		while (-1 != (zwsPosIndex = reindexZwischensummenPositionRef(belegPositionDto, belegPositionDtos,
				positionIndex))) {
			positionIndex = zwsPosIndex + 1;
		}
	}

	private int getBelegPositionIndex(Integer positionIId, BelegpositionVerkaufDto[] dtos) {
		for (int i = 0; i < dtos.length; i++) {
			if (positionIId.equals(dtos[i].getIId()))
				return i;
		}

		return -1;
	}

	private void validiereZwsAufGleichenMwstSatz(BelegpositionVerkaufDto[] positionDtos) {
		if (positionDtos == null || positionDtos.length == 0) {
			return;
		}
		
		PositionNumberHandler numberHandler = new PositionNumberHandler();
		PositionNumberAdapter numberAdapter = new BelegPositionNumberDtoAdapter(positionDtos);
		
		for (BelegpositionVerkaufDto positionDto : positionDtos) {			
			if (!positionDto.isIntelligenteZwischensumme()) {
				continue;
			}
			
			if (positionDto.getZwsVonPosition() == null || positionDto.getZwsBisPosition() == null) {
				continue;
			}
			
			int vonPosIndex = getBelegPositionIndex(positionDto.getZwsVonPosition(), positionDtos);
			int bisPosIndex = getBelegPositionIndex( positionDto.getZwsBisPosition(), positionDtos);
			if (vonPosIndex == -1 || bisPosIndex == -1) {
				Integer zwsPosNr = numberHandler.getPositionNummer(positionDto.getIId(), numberAdapter);
				Integer vonPosNr = vonPosIndex == -1 ? -1 : 
					numberHandler.getPositionNummer(positionDtos[vonPosIndex].getIId(), numberAdapter);
				Integer bisPosNr = bisPosIndex == -1 ? -1 :
					numberHandler.getPositionNummer(positionDtos[bisPosIndex].getIId(), numberAdapter);
				throw EJBExcFactory.zwsPositionUnvollstaendig(
						vonPosNr, bisPosNr, 
						Helper.emptyString(positionDto.getCBez()),
						positionDto.getIId(), zwsPosNr);
			}
			
			if (!validiereZwsGruppe(positionDtos, vonPosIndex,
					bisPosIndex, positionDto.getMwstsatzIId())) {
				Integer vonPosNr = numberHandler.getPositionNummer(
						positionDto.getZwsVonPosition(), numberAdapter);
				Integer bisPosNr = numberHandler.getPositionNummer(
						positionDto.getZwsBisPosition(), numberAdapter);
				throw EJBExcFactory.zwsPositionUnterschiedlicheMwstsatzIds(
						positionDto.getIId(), vonPosNr, bisPosNr);					
			}
		}		
	}
	
	@Override
	public void validiereZwsAufGleichenMwstSatzThrow(BelegpositionVerkaufDto[] dtos) {
		validiereZwsAufGleichenMwstSatz(dtos);
	}
	
	private boolean validiereZwsGruppe(BelegpositionVerkaufDto[] positionDtos,
			Integer vonPosIndex, Integer bisPosIndex, Integer zwsMwstsatzId) {		
		Integer lastMwstsatzIId = null;
		for (int i = vonPosIndex; i <= bisPosIndex; i++) {
			Integer actualMwstsatzIId = positionDtos[i].getMwstsatzIId();
			if (actualMwstsatzIId != null) {
				if (lastMwstsatzIId == null) {
					lastMwstsatzIId = actualMwstsatzIId;
				}

				if (!lastMwstsatzIId.equals(actualMwstsatzIId))
					return false;
			}
		}
		
		return lastMwstsatzIId.equals(zwsMwstsatzId);
	}
	
	public boolean pruefeAufGleichenMwstSatz(BelegpositionVerkaufDto[] belegPositionDtos, Integer vonPositionNumber,
			Integer bisPositionNumber) throws EJBExceptionLP {
		if (null == belegPositionDtos || 0 == belegPositionDtos.length)
			return true;

		PositionNumberHandler numberHandler = new PositionNumberHandler();
		PositionNumberAdapter numberAdapter = new BelegPositionNumberDtoAdapter(belegPositionDtos);

		Integer vonPosIId = numberHandler.getPositionIIdFromPositionNummer(null, vonPositionNumber, numberAdapter);
		Integer bisPosIId = numberHandler.getPositionIIdFromPositionNummer(null, bisPositionNumber, numberAdapter);

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

	private BigDecimal calculateIntZwsNettoEinzelpreis(BigDecimal originalPrice, BigDecimal rabattSatz) {
		if (originalPrice == null)
			return null;
		if (rabattSatz.signum() == 0)
			return originalPrice;

		BigDecimal value = originalPrice.multiply(rabattSatz);
		BigDecimal sum = Helper.rundeKaufmaennisch(value, 4);
		BigDecimal result = originalPrice.subtract(sum);
		return result;
	}

	public Set<Integer> adaptIntZwsPositions0(BelegpositionVerkaufDto[] positionDtos) {
		BigDecimal[] rabatts = new BigDecimal[positionDtos.length];

		Set<Integer> modifiedPositions = new HashSet<Integer>();
		for (BelegpositionVerkaufDto positionDto : positionDtos) {
			if (!positionDto.isIntelligenteZwischensumme())
				continue;

			if (positionDto.getZwsVonPosition() == null || positionDto.getZwsBisPosition() == null)
				continue;

			int vonIndex = getIndexOfBelegPosition(positionDto.getZwsVonPosition(), positionDtos);
			int bisIndex = getIndexOfBelegPositionEnd(positionDto.getZwsBisPosition(), positionDtos);
			if (vonIndex == -1 || bisIndex == -1)
				continue;

			BigDecimal rabattSatz = new BigDecimal(positionDto.getFRabattsatz()).movePointLeft(2);
			rabattSatz = Helper.rundeKaufmaennisch(rabattSatz, 4);

			for (int i = vonIndex; i <= bisIndex; i++) {
				// positionDtos[i].setNNettoeinzelpreisplusversteckteraufschlag(
				// calculateIntZwsNettoEinzelpreis(positionDtos[i].getNNettoeinzelpreisplusversteckteraufschlag(),
				// rabattSatz));
				if (positionDtos[i].isIntelligenteZwischensumme())
					continue;

				if (rabatts[i] == null) {
					rabatts[i] = BigDecimal.ZERO;
				}
				rabatts[i] = rabatts[i].add(rabattSatz);

				modifiedPositions.add(i);
			}
		}

		for (Integer posIndex : modifiedPositions) {
			positionDtos[posIndex]
					.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(calculateIntZwsNettoEinzelpreis(
							positionDtos[posIndex].getNNettoeinzelpreisplusversteckteraufschlagminusrabatte(),
							rabatts[posIndex]));

		}
		return modifiedPositions;
	}

	public Set<Integer> adaptIntZwsPositions(BelegpositionVerkaufDto[] positionDtos) {
		Set<Integer> modifiedPositions = new HashSet<Integer>();
		for (BelegpositionVerkaufDto positionDto : positionDtos) {
			if (!positionDto.isIntelligenteZwischensumme())
				continue;

			if (positionDto.getZwsVonPosition() == null || positionDto.getZwsBisPosition() == null)
				continue;

			int vonIndex = getIndexOfBelegPosition(positionDto.getZwsVonPosition(), positionDtos);
			int bisIndex = getIndexOfBelegPositionEnd(positionDto.getZwsBisPosition(), positionDtos);
			if (vonIndex == -1 || bisIndex == -1)
				continue;

			BigDecimal rabattSatz = new BigDecimal(positionDto.getFRabattsatz()).movePointLeft(2);
			rabattSatz = Helper.rundeKaufmaennisch(rabattSatz, 4);

			for (int i = vonIndex; i <= bisIndex; i++) {
				// positionDtos[i].setNNettoeinzelpreisplusversteckteraufschlag(
				// calculateIntZwsNettoEinzelpreis(positionDtos[i].getNNettoeinzelpreisplusversteckteraufschlag(),
				// rabattSatz));
				if (positionDtos[i].isIntelligenteZwischensumme())
					continue;

				positionDtos[i]
						.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(calculateIntZwsNettoEinzelpreis(
								positionDtos[i].getNNettoeinzelpreisplusversteckteraufschlagminusrabatte(),
								rabattSatz));
				modifiedPositions.add(i);
			}
		}
		return modifiedPositions;
	}

	public Set<Integer> prepareIntZwsPositions(BelegpositionVerkaufDto[] positionDtos,
			BelegVerkaufDto belegVerkaufDto) {
		Set<Integer> modifiedPositions = new HashSet<Integer>();
		for (BelegpositionVerkaufDto positionDto : positionDtos) {
			if (!positionDto.isIntelligenteZwischensumme())
				continue;

			if (positionDto.getZwsVonPosition() == null || positionDto.getZwsBisPosition() == null)
				continue;

			int vonIndex = getIndexOfBelegPosition(positionDto.getZwsVonPosition(), positionDtos);
			int bisIndex = getIndexOfBelegPositionEnd(positionDto.getZwsBisPosition(), positionDtos);

			if (vonIndex == -1 || bisIndex == -1)
				continue;

			for (int i = vonIndex; i <= bisIndex; i++) {
//				positionDtos[i]
//						.setNNettoeinzelpreisplusversteckteraufschlag(positionDtos[i]
//								.getNNettoeinzelpreis());
//				positionDtos[i]
//						.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(positionDtos[i]
//								.getNNettoeinzelpreis());
				berechneBelegpositionVerkauf(positionDtos[i], belegVerkaufDto);
				modifiedPositions.add(i);
			}
		}
		return modifiedPositions;
	}

	@Override
	public <T extends IBelegVerkaufEntity> void saveIntZwsPositions(BelegpositionVerkaufDto[] dtos,
			Set<Integer> modifiedPosIndex, Class<T> entityClass) {
		for (Integer index : modifiedPosIndex) {
			T pos = em.find(entityClass, dtos[index].getIId());
			pos.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(
					dtos[index].getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			em.merge(pos);
		}
	}

	@Override
	public void preiseEinesArtikelsetsUpdaten(Collection<IBelegVerkaufEntity> setPositions, BigDecimal kopfMenge,
			BigDecimal kopfNetto, KundeDto kundeDto, MwstsatzbezId bezId, String waehrungCnr,
			IArtikelsetPreisUpdate setPreisUpdate, TheClientDto theClientDto) throws RemoteException {
		java.sql.Date now = getDate();
		BigDecimal gesamtVK = calcGesamtWert(now, kundeDto, setPositions, kopfMenge, bezId, waehrungCnr, theClientDto);

		myLogger.info("GesamtVK: " + gesamtVK.toPlainString());
//		BigDecimal zuverteilen = kopfNetto.subtract(gesamtVK);
//		if(zuverteilen.signum() != 0) {
//			IBelegVerkaufEntity positionEntity = setPositions.iterator().next();
//			myLogger.warn("Artikelset-VK (" + gesamtVK.toPlainString() + 
//					") Kopfnetto (" + kopfNetto.toPlainString() +
//					") in Position Id " + ((IIId)positionEntity).getIId());
//
//			setPreisUpdate.updateNettoPreis(
//					((IIId) positionEntity).getIId(), zuverteilen);
//			gesamtVK = calcGesamtWert(now, kundeDto, setPositions, kopfMenge, bezId, 
//					waehrungCnr, theClientDto);			
//		}
		for (IBelegVerkaufEntity positionEntity : setPositions) {
			PositionsWert w = calcPositionsWert(now, kundeDto, positionEntity, bezId, waehrungCnr, theClientDto);
			BigDecimal p = w.getNettogesamtpreis();

			positionEntity.setNNettoeinzelpreis(BigDecimal.ZERO);
			positionEntity.setNNettogesamtpreis(BigDecimal.ZERO);
			positionEntity.setNNettogesamtpreisplusversteckteraufschlag(BigDecimal.ZERO);
			positionEntity.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(BigDecimal.ZERO);
			positionEntity.setNBruttogesamtpreis(BigDecimal.ZERO);
			positionEntity.setNMwstbetrag(BigDecimal.ZERO);
			positionEntity.setNRabattbetrag(BigDecimal.ZERO);
			positionEntity.setNMaterialzuschlag(BigDecimal.ZERO);

			setPreisUpdate.initializePreis(positionEntity);
//			if(positionEntity instanceof Rechnungposition) {
//				Rechnungposition rpos = (Rechnungposition) positionEntity;
//				rpos.setNEinzelpreis(BigDecimal.ZERO);
//				rpos.setNBruttoeinzelpreis(BigDecimal.ZERO);
//			}

			if (p.signum() != 0 && gesamtVK.signum() != 0) {
				BigDecimal bdAnteilVKWert = p.multiply(kopfMenge).divide(gesamtVK, 4, BigDecimal.ROUND_HALF_EVEN);

				BigDecimal bdPreis = kopfNetto.multiply(bdAnteilVKWert)
						.divide(positionEntity.getNMenge().multiply(kopfMenge), 4, BigDecimal.ROUND_HALF_EVEN);

				positionEntity.setNNettoeinzelpreis(bdPreis);

				positionEntity.setNMaterialzuschlag(w.getMaterialzuschlag());
//				positionEntity.setNBruttogesamtpreis(bdPreis);
				positionEntity.setNNettogesamtpreisplusversteckteraufschlag(bdPreis);
				positionEntity.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(bdPreis);
				positionEntity.setNNettogesamtpreis(bdPreis);

				myLogger.warn("Position (" + ((IIId) positionEntity).getIId() + ") new netto einzel preis "
						+ bdPreis.toPlainString() + ".");

// MwSt muss nicht berechnet werden, weil eh schon im Kopfartikel enthalten				
//				MwstsatzDto mwstDto = getMandantFac().mwstsatzFindByPrimaryKey(
//							positionEntity.getMwstsatzIId(), theClientDto);
//				BigDecimal bdBruttoeinzelpreis = positionEntity
//						.getNNettogesamtpreisplusversteckteraufschlag();

//				if (mwstDto.getFMwstsatz() != 0) {
//					BigDecimal bdMwst = new BigDecimal(mwstDto.getFMwstsatz()
//							.doubleValue()).movePointLeft(2);
//					bdMwst = Helper.rundeKaufmaennisch(bdMwst, 4);
//					BigDecimal bdMwstSumme = bdBruttoeinzelpreis.multiply(bdMwst);
//					positionEntity.setNBruttogesamtpreis(bdBruttoeinzelpreis.add(bdMwstSumme));
//					positionEntity.setNMwstbetrag(bdMwstSumme);					
//				}

				setPreisUpdate.setPreis(positionEntity, bdPreis);
//				if(positionEntity instanceof Rechnungposition) {
//					Rechnungposition rpos = (Rechnungposition) positionEntity;
//					rpos.setNEinzelpreis(bdPreis);
//					rpos.setNBruttoeinzelpreis(bdPreis);
//				}
			}
			em.merge(positionEntity);
		}

//		gesamtVK = calcGesamtWert(now, kundeDto, setPositions, kopfMenge, bezId, 
//				waehrungCnr, theClientDto);
		gesamtVK = sumGesamtWert(setPositions);
		BigDecimal zuverteilen = kopfNetto.subtract(gesamtVK);
		if (zuverteilen.signum() != 0) {
			IBelegVerkaufEntity positionEntity = setPositions.iterator().next();
			myLogger.warn("Artikelset-VK (" + gesamtVK.toPlainString() + ") Kopfnetto (" + kopfNetto.toPlainString()
					+ ") in Position Id " + ((IIId) positionEntity).getIId());

			myLogger.warn("--> zu verteilen: " + zuverteilen.toPlainString());
//			setPreisUpdate.updateNettoPreis(
//					((IIId) positionEntity).getIId(), zuverteilen);
			BigDecimal bdPreis = zuverteilen.divide(positionEntity.getNMenge(), 4, BigDecimal.ROUND_HALF_EVEN);

			positionEntity.setNNettoeinzelpreis(positionEntity.getNNettoeinzelpreis().add(bdPreis));
////			positionEntity.setNBruttogesamtpreis(bdPreis);
			positionEntity.setNNettogesamtpreisplusversteckteraufschlag(
					positionEntity.getNNettogesamtpreisplusversteckteraufschlag().add(bdPreis));
			positionEntity.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(
					positionEntity.getNNettogesamtpreisplusversteckteraufschlagminusrabatte().add(bdPreis));
			positionEntity.setNNettogesamtpreis(positionEntity.getNNettogesamtpreis().add(bdPreis));
//			
			setPreisUpdate.addPreis(positionEntity, bdPreis);
//			if(positionEntity instanceof Rechnungposition) {
//				Rechnungposition rpos = (Rechnungposition) positionEntity;
//				rpos.setNEinzelpreis(
//						rpos.getNEinzelpreis().add(bdPreis));
//				rpos.setNBruttoeinzelpreis(
//						rpos.getNBruttoeinzelpreis().add(bdPreis));
//			}

			em.merge(positionEntity);
		}
	}

	public void preiseEinesArtikelsetsUpdaten0(Collection<IBelegVerkaufEntity> setPositions, BigDecimal kopfMenge,
			BigDecimal kopfNetto, KundeDto kundeDto, MwstsatzbezId bezId, String waehrungCnr,
			IArtikelsetPreisUpdate setPreisUpdate, TheClientDto theClientDto) throws RemoteException {
		java.sql.Date now = getDate();
		BigDecimal gesamtVK = calcGesamtWert(now, kundeDto, setPositions, kopfMenge, bezId, waehrungCnr, theClientDto);

		myLogger.info("GesamtVK: " + gesamtVK.toPlainString());

		BigDecimal gesamtVerteilt = BigDecimal.ZERO;
		BigDecimal preisAenderung = BigDecimal.ZERO;
		for (IBelegVerkaufEntity positionEntity : setPositions) {
			PositionsWert w = calcPositionsWert(now, kundeDto, positionEntity, bezId, waehrungCnr, theClientDto);
			BigDecimal p = w.getNettogesamtpreis();
			positionEntity.setNNettoeinzelpreis(BigDecimal.ZERO);
			positionEntity.setNNettogesamtpreisplusversteckteraufschlag(BigDecimal.ZERO);
			positionEntity.setNBruttogesamtpreis(BigDecimal.ZERO);
			positionEntity.setNMwstbetrag(BigDecimal.ZERO);
			positionEntity.setNRabattbetrag(BigDecimal.ZERO);

//			VkpreisfindungDto vkpreisDto = getVkPreisfindungFac()
//					.verkaufspreisfindung(
//							positionEntity.getArtikelIId(),
//							kundeDto.getIId(),
//							positionEntity.getNMenge(),
//							now,
//							kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
//							getMandantFac()
//									.mwstsatzFindByMwstsatzbezIIdAktuellster(
//											bezId.id(), theClientDto)
//									.getIId(), waehrungCnr,
//							theClientDto);
//
//			VerkaufspreisDto kundenVKPreisDto = Helper
//					.getVkpreisBerechnet(vkpreisDto);
//
//			BigDecimal p = BigDecimal.ZERO;
//			if(kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null) {
//				p = kundenVKPreisDto.nettopreis;
//			} else {
//				p = positionEntity.getNNettoeinzelpreis();
//			}

			if (p.signum() != 0 && gesamtVK.signum() != 0) {
//			if (kundenVKPreisDto != null
//					&& kundenVKPreisDto.nettopreis != null
//					&& gesamtVK.signum() != 0) {
//				BigDecimal bdAnteilVKWert = p
//						.multiply(positionEntity.getNMenge().multiply(kopfMenge))
//						.divide(gesamtVK, 4, BigDecimal.ROUND_HALF_EVEN);
				BigDecimal bdAnteilVKWert = p.multiply(kopfMenge).divide(gesamtVK, 4, BigDecimal.ROUND_HALF_EVEN);

				BigDecimal bdPreis = kopfNetto.multiply(bdAnteilVKWert)
						.divide(positionEntity.getNMenge().multiply(kopfMenge), 4, BigDecimal.ROUND_HALF_EVEN);

				positionEntity.setNNettoeinzelpreis(bdPreis);

				positionEntity.setNMaterialzuschlag(w.getMaterialzuschlag());
				positionEntity.setNBruttogesamtpreis(bdPreis);
				positionEntity.setNNettogesamtpreisplusversteckteraufschlag(bdPreis);
				positionEntity.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(bdPreis);
				positionEntity.setNNettogesamtpreis(bdPreis);

				preisAenderung = bdPreis.subtract(p);
				gesamtVerteilt = gesamtVerteilt.add(bdPreis.multiply(positionEntity.getNMenge()));
			}
		}

		BigDecimal verteiltRest = kopfNetto.subtract(gesamtVerteilt);
		if (verteiltRest.signum() != 0 || preisAenderung.signum() != 0) {
			IBelegVerkaufEntity positionEntity = setPositions.iterator().next();
			myLogger.warn("Artikelset-VK (" + gesamtVK.toPlainString() + ") ErrechneterVK ("
					+ gesamtVerteilt.toPlainString() + ") verteile (" + verteiltRest.toPlainString()
					+ ") in Position Id " + ((IIId) positionEntity).getIId());
			positionEntity.setNBruttogesamtpreis(positionEntity.getNBruttogesamtpreis().add(verteiltRest));
			BigDecimal posDiff = verteiltRest.divide(positionEntity.getNMenge(), 4, BigDecimal.ROUND_HALF_EVEN);
			positionEntity.setNNettogesamtpreisplusversteckteraufschlag(
					positionEntity.getNNettogesamtpreisplusversteckteraufschlag().add(posDiff));
			positionEntity.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(
					positionEntity.getNNettogesamtpreisplusversteckteraufschlagminusrabatte().add(posDiff));
			positionEntity.setNNettogesamtpreis(positionEntity.getNNettogesamtpreis().add(verteiltRest));

			em.merge(positionEntity);
		}
	}

	private BigDecimal sumGesamtWert(Collection<IBelegVerkaufEntity> setPositions) {
		BigDecimal sum = BigDecimal.ZERO;
		for (IBelegVerkaufEntity positionEntity : setPositions) {
			BigDecimal pos = positionEntity.getNNettoeinzelpreis().multiply(positionEntity.getNMenge());
			sum = sum.add(pos);
		}
		return sum;
	}

	private BigDecimal calcGesamtWert(Date now, KundeDto kundeDto, Collection<IBelegVerkaufEntity> setPositions,
			BigDecimal kopfMenge, MwstsatzbezId bezId, String waehrungCnr, TheClientDto theClientDto)
			throws RemoteException {
		BigDecimal bdGesamtVKwert = BigDecimal.ZERO;

		for (IBelegVerkaufEntity positionEntity : setPositions) {
			PositionsWert w = calcPositionsWert(now, kundeDto, positionEntity, bezId, waehrungCnr, theClientDto);
			BigDecimal p = w.getNettogesamtpreis();
			bdGesamtVKwert = bdGesamtVKwert.add(p);

			myLogger.warn("Poswert id (" + ((IIId) positionEntity).getIId() + ", ArtikelId "
					+ positionEntity.getArtikelIId() + "): " + p.toPlainString() + " vkpf: " + w.hatPreisfindung()
					+ ", " + (w.hatPreisfindung() ? w.getVkpreisDto().nettopreis.toPlainString() : ""));
//			VkpreisfindungDto vkpreisDto = getVkPreisfindungFac()
//					.verkaufspreisfindung(
//							positionEntity.getArtikelIId(),
//							kundeDto.getIId(),
//							positionEntity.getNMenge(), now,
//							kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
//							getMandantFac()
//									.mwstsatzFindByMwstsatzbezIIdAktuellster(
//											bezId.id(), theClientDto)
//									.getIId(), waehrungCnr,
//							theClientDto);
//
//			VerkaufspreisDto kundenVKPreisDto = Helper
//					.getVkpreisBerechnet(vkpreisDto);
//
//			if (kundenVKPreisDto != null
//					&& kundenVKPreisDto.nettopreis != null) {
//				bdGesamtVKwert = bdGesamtVKwert
//						.add(kundenVKPreisDto.nettopreis.multiply(
//								positionEntity.getNMenge()));
//			}
		}

		bdGesamtVKwert = Helper.rundeKaufmaennisch(bdGesamtVKwert, 4);
		return bdGesamtVKwert;
	}

	private PositionsWert calcPositionsWert(Date now, KundeDto kundeDto, IBelegVerkaufEntity position,
			MwstsatzbezId bezId, String waehrungCnr, TheClientDto theClientDto) throws RemoteException {
		MwstsatzDto mwstsatz = getMandantFac().mwstsatzZuDatumValidate(bezId.id(),
				Helper.cutTimestamp(new Timestamp(now.getTime())), theClientDto);
		VkpreisfindungDto vkpreisDto = getVkPreisfindungFac().verkaufspreisfindung(position.getArtikelIId(),
				kundeDto.getIId(), position.getNMenge(), now, kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
//						getMandantFac()
//								.mwstsatzFindByMwstsatzbezIIdAktuellster(
//										bezId.id(), theClientDto).getIId(),
				mwstsatz.getIId(), waehrungCnr, theClientDto);

		VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisDto);

		PositionsWert posWert = new PositionsWert(kundenVKPreisDto, position.getNMenge());
		if (posWert.hatPreisfindung())
			return posWert;

		posWert.setNettoeinzelpreis(position.getNNettoeinzelpreis());
		posWert.setNettogesamtpreis(position.getNNettoeinzelpreis().multiply(position.getNMenge()));
		return posWert;

//		if (kundenVKPreisDto != null
//				&& kundenVKPreisDto.nettopreis != null
//				&& kundenVKPreisDto.nettopreis.signum() != 0) { 
//			return kundenVKPreisDto.nettopreis.multiply(position.getNMenge());
//		}
//		
//		return position.getNNettoeinzelpreis().multiply(position.getNMenge());		
	}

	/**
	 * Sind alle mengenbehafteten Positionen Teil einer Zwischensumme?</br>
	 * 
	 * <p>
	 * Beim Angebotsdruck mit aktivierter Zusammenfassung geht der Druck davon aus,
	 * dass alle mengenbehafteten Positionen in Zwischensummen auf der ersten Ebene
	 * enthalten sind. Dies wird hier &uuml;berpr&uuml;ft.
	 * </p>
	 * 
	 * <p>
	 * Implementierungsdetail: Es gibt keine Ueberpruefung der Ebene, weil eine
	 * Belegposition einer inneren Zwischensumme automatisch in der aeusseren
	 * Zwischensumme enthalten sein muss. Das koennte nur sein, wenn die
	 * Zwischensumme unvollstaendig definiert ist. Dies wird erkannt, weil dann die
	 * Belegpositionsnummer des von/bis Index nicht ermittelt werden kann.
	 * </p>
	 * 
	 * @param positionDtos
	 * @return true, wenn alle Pos in einer Zws enthalten sind.
	 */
	@Override
	public Set<Integer> calculatePositionsNotInZws(BelegpositionVerkaufDto[] positionDtos) {
		Set<Integer> calculatableSet = buildAllCalculatablePositions(positionDtos);
		Set<Integer> remainingSet = removePositionsInZws(positionDtos, calculatableSet);
		return remainingSet;
	}

	private Set<Integer> buildAllCalculatablePositions(BelegpositionVerkaufDto[] positionDtos) {
		Set<Integer> positionsInZws = new HashSet<Integer>();

		for (BelegpositionVerkaufDto posDto : positionDtos) {
			if (posDto.isIntelligenteZwischensumme()) {
				continue;
			}

			if (posDto.isCalculateZws()) {
				positionsInZws.add(posDto.getIId());
			}
		}

		return positionsInZws;
	}

	private Set<Integer> removePositionsInZws(BelegpositionVerkaufDto[] positionDtos, Set<Integer> calculatableSet) {
		for (BelegpositionVerkaufDto posDto : positionDtos) {
			if (!posDto.isIntelligenteZwischensumme())
				continue;

			if (posDto.getZwsVonPosition() == null || posDto.getZwsBisPosition() == null)
				continue;

			int vonIndex = getIndexOfBelegPosition(posDto.getZwsVonPosition(), positionDtos);
			int bisIndex = getIndexOfBelegPositionEnd(posDto.getZwsBisPosition(), positionDtos);

			if (vonIndex == -1 || bisIndex == -1)
				continue;

			for (int i = vonIndex; i <= bisIndex; i++) {
				if (positionDtos[i].isCalculateZws()) {
					calculatableSet.remove(positionDtos[i].getIId());
				}
			}
		}

		return calculatableSet;
	}

	class PositionsWert {
		private VerkaufspreisDto kundenVKPreisDto;
		private BigDecimal nettoeinzelpreis;
		private BigDecimal nettogesamtpreis;

		public PositionsWert(VerkaufspreisDto vkpreisDto, BigDecimal amount) {
			this.kundenVKPreisDto = vkpreisDto;
			if (hatPreisfindung()) {
				setNettoeinzelpreis(kundenVKPreisDto.nettopreis);
				setNettogesamtpreis(kundenVKPreisDto.nettopreis.multiply(amount));
			} else {
				setNettoeinzelpreis(BigDecimal.ZERO);
				setNettogesamtpreis(BigDecimal.ZERO);
			}
		}

		public VerkaufspreisDto getVkpreisDto() {
			return this.kundenVKPreisDto;
		}

		public boolean hatPreisfindung() {
			return kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null
					&& kundenVKPreisDto.nettopreis.signum() != 0;
		}

		public BigDecimal getMaterialzuschlag() {
			return kundenVKPreisDto != null ? kundenVKPreisDto.bdMaterialzuschlag : BigDecimal.ZERO;
		}

		public BigDecimal getNettoeinzelpreis() {
			return nettoeinzelpreis;
		}

		public void setNettoeinzelpreis(BigDecimal nettoeinzelpreis) {
			this.nettoeinzelpreis = nettoeinzelpreis;
		}

		public BigDecimal getNettogesamtpreis() {
			return nettogesamtpreis;
		}

		public void setNettogesamtpreis(BigDecimal nettogesamtpreis) {
			this.nettogesamtpreis = nettogesamtpreis;
		}
	}

	@Override
	public BelegpositionVerkaufDto berechneNeu(BelegpositionVerkaufDto posDto, KundeId kundeId, Timestamp belegDatum,
			TheClientDto theClientDto) {
		Validator.dtoNotNull(posDto, "posDto");
		Validator.pkFieldNotNull(kundeId, "kundeId");
		Validator.notNull(belegDatum, "belegDatum");

		return mwstsatzBestimmenUndNeuBerechnen(posDto, kundeId.id(), belegDatum, theClientDto);
	}
}
